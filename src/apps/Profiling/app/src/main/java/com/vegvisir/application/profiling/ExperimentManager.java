package com.vegvisir.application.profiling;

import android.util.Log;

import com.vegvisir.util.profiling.VegvisirStatsCollector;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExperimentManager {

    private ExecutorService service;
    private LogFileManager fileManager;


    public ExperimentManager(LogFileManager fileManager) {
        service = Executors.newCachedThreadPool();
        this.fileManager = fileManager;
    }

    public void startExperiment(ExperimentParameter parameter) {
        service.submit(() -> {
            try {
                Thread current = Thread.currentThread();
                OutputStreamWriter writer = fileManager.createFile(parameter.getExperimentName());
                VegvisirStatsCollector collector = VegvisirStatsCollector.getInstance();
                collector.startCollecting(writer);
                Log.d("ExP", "startExperiment: "+parameter.getStartTime().toString());
                Log.d("ExP", "startExperiment: "+parameter.getEndTime().toString());
                waitToTime(parameter.getStartTime(), () -> {
                    collector.logExperimentStartEvent(parameter.getExperimentName());
                });
                waitToTime(parameter.getEndTime(), () -> {
                    collector.logExperimentEndEvent(parameter.getExperimentName());
                    synchronized (current) {
                        current.notify();
                    }
                });
                synchronized (current) {
                    current.wait();
                }
                Log.d("ExP", "startExperiment: Not wait!");
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (VegvisirProfilingException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
            }
        });
    }

    private void waitToTime(Date time, Runnable callback) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                callback.run();
            }
        }, time);
    }

}
