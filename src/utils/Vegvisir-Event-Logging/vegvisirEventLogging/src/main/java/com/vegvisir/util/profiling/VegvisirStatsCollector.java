package com.vegvisir.util.profiling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.Date;

public class VegvisirStatsCollector {

    private static VegvisirStatsCollector collector;

    private int distance;

    private OutputStreamWriter writer;

    private VegvisirProfilingStats stats;

    private String DATA_TRANSFER_TAG = "[DATA TRANSFER]";

    public static synchronized VegvisirStatsCollector getInstance() {
        if (collector == null) {
            collector = new VegvisirStatsCollector();
        }
        return collector;
    }

    private VegvisirStatsCollector() {
        stats = new VegvisirProfilingStats();
    }

    public void startCollecting(OutputStreamWriter writer) {
        this.writer = writer;
    }

    public void logDistanceChangedEvent(int newDistance) {
        this.distance = newDistance;
        logEvent("New distance: "+this.distance);
    }

    public void logEvent(OutputStreamWriter ow, String message) {
        Date time = new Date();
        String output = String.format("[%d] %s\n", time.getTime(), message);
        try {
            if (ow != null)
                ow.write(output);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void logEvent(String message) {
        logEvent(this.writer, message);
    }

    public void logReconciliationStartEvent(String remoteID) {
        stats.incrementReconciliation();
//        logEvent(String.format("Reconciliation with %s Start", remoteID));
    }

    public void logReconciliationEndEvent(String remoteID) {
        stats.incrementReconciliation();
//        logEvent(String.format("Reconciliation with %s end", remoteID));

    }

    public void logExperimentStartEvent(String name) {
//        logEvent(String.format("Experiment %s Start", Base64.getEncoder().encodeToString(name.getBytes())));

    }

    public void logExperimentEndEvent(String name) {
//        logEvent(String.format("Experiment %s end", Base64.getEncoder().encodeToString(name.getBytes())));

    }

    public synchronized void logDataTransferredEvent(long bytes) {
        stats.addBytes(bytes);
//        logEvent(String.format("bytes transferred: %d", bytes));
    }

    public synchronized void logNewBlockCount(int blocks){
        stats.setBlocks(blocks);
    }

    public VegvisirProfilingStats getStats() {
        return stats;
    }

    public int getDistance() {
        return distance;
    }

    public String logFixedRateSamplingEvent(OutputStreamWriter ow, Iterable<String> extra) {
        StringBuilder builder = new StringBuilder();
        builder.append(stats.getNumOfReconciliation()).append(",");
        builder.append(stats.getBytesSoFar()).append(",");
        builder.append(distance).append(",");
        builder.append(stats.getBlocks()).append(",");
        for (String s : extra) {
            builder.append(s).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        String logText = builder.toString();
        logEvent(ow, logText);
        return logText;
    }
}
