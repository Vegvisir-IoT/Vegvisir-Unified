package com.vegvisir.application.profiling;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vegvisir.util.profiling.VegvisirStatsCollector;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExperimentManager {

    private ExecutorService service;
    private LogFileManager fileManager;
    private VegvisirAdapter adapter;
    private LocationManager locationManager;
    private AppendObservableList<String> logTexts;


    public ExperimentManager(VegvisirAdapter adapter, LogFileManager fileManager, Context ctx) {
        service = Executors.newCachedThreadPool();
        this.fileManager = fileManager;
        this.adapter = adapter;
        logTexts = new AppendObservableList<>();
        initLocationService(ctx);
    }

    private void initLocationService(Context ctx) {
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("LOC", "initLocationService: GPS is available");
        } else {
            Log.d("LOC", "initLocationService: GPS is not available");
            Log.d("LOC", "initLocationService: This device has the following providers:");
            locationManager.getAllProviders().forEach(p -> {
                Log.d("LOC", "initLocationService: "+p);
            });

        }
        if (ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ctx.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("", "initLocationService: No Permission");
            return;
        }
        Log.d("", "initLocationService: Permission Granted");
    }


    public void startExperiment(ExperimentParameter parameter) {
        logTexts.clear();
        adapter.setBlockSize(parameter.getBlockSize());
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
//                samplingWriter.flush();
//                samplingWriter.close();
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
            ow.write("timestamp,#reconciliation,#bytes,distance,#blocks,latitude,longitude\n");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        ExperimentManager self = this;
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                @SuppressLint("MissingPermission")
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double  latitude = location == null ? 0.0 : location.getLatitude();
                double longitude = location == null ? 0.0 : location.getLongitude();
                Log.d("LOC", "run: " + latitude + "||"+longitude);
                List<String> loc = new ArrayList<>();
                loc.add(String.valueOf(latitude));
                loc.add(String.valueOf(longitude));
                logTexts.append(collector.logFixedRateSamplingEvent(ow, loc));
                if (edTime.before(new Date())) {
                    try {
                        ow.flush();
                        ow.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    t.cancel();
                }
            }
        }, stTime, period);
    }

    public AppendObservableList<String> getLogTexts() {
        return logTexts;
    }
}
