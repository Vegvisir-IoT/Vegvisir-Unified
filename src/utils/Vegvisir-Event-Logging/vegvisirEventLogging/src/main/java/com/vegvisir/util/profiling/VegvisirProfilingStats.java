package com.vegvisir.util.profiling;

import java.util.Date;

public class VegvisirProfilingStats {

    private long bytesSoFar = 0;

    private int numOfReconciliation = 0;

    private Date startTime;

    private Date endTime;

    public VegvisirProfilingStats() {

    }

    public void start() {
        startTime =  new Date();
    }

    public void addBytes(long bytes) {
        this.bytesSoFar += bytes;
    }

    public void incrementReconciliation() {
        numOfReconciliation ++;
    }

    public void end() {
        endTime = new Date();
    }

    public long getBytesSoFar() {
        return bytesSoFar;
    }

    public int getNumOfReconciliation() {
        return numOfReconciliation / 2;
    }
}
