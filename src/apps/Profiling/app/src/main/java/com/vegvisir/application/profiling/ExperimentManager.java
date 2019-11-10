package com.vegvisir.application.profiling;

import android.content.Context;
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
    private VegvisirAdapter adapter;


    public ExperimentManager(VegvisirAdapter adapter, LogFileManager fileManager) {
        service = Executors.newCachedThreadPool();
        this.fileManager = fileManager;
        this.adapter = adapter;

    }

    public void startExperiment(ExperimentParameter parameter) {
        adapter.setBlockRate(parameter.getBlockSize());
        adapter.setBlockRate(parameter.getBlockRate());
        adapter.setStTime(parameter.getStartTime());
        adapter.setEdTime(parameter.getEndTime());
        service.submit(() -> {
            try {
                Thread current = Thread.currentThread();
                OutputStreamWriter verboseWriter = fileManager.createFile(parameter.getExperimentName()+"-verbose");
                OutputStreamWriter samplingWriter = fileManager.createFile(parameter.getExperimentName()+"-sampling-"+parameter.getSamplingRate());
                VegvisirStatsCollector collector = VegvisirStatsCollector.getInstance();
                collector.startCollecting(verboseWriter);
                collector.logDistanceChangedEvent(parameter.getDistance());
                waitToTime(parameter.getStartTime(), () -> {
                    collector.logExperimentStartEvent(parameter.getExperimentName());
                    adapter.startCreatingBlocks();
                    samplingAtFixedRate(parameter.getStartTime(), parameter.getEndTime(), parameter.getSamplingPeriod(), samplingWriter);
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
                verboseWriter.flush();
                verboseWriter.close();
                samplingWriter.flush();
                samplingWriter.close();
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

    private void samplingAtFixedRate(Date stTime, Date edTime, int period, OutputStreamWriter ow) {
        VegvisirStatsCollector collector = VegvisirStatsCollector.getInstance();
        Timer t = new Timer();
        try {
            ow.write("timestamp,number of reconciliation,bytes so far,distance\n");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                collector.logFixedRateSamplingEvent(ow);
                if (edTime.before(new Date())) {
                    t.cancel();
                }
            }
        }, stTime, period);
    }

}
