package com.vegvisir.application.profiling;

import android.content.Context;

import com.vegvisir.application.VegvisirInstanceV1;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VegvisirAdapter {

    private VegvisirInstance instance;
    private int blockRate;
    private int blockSize;
    private Date stTime;
    private Date edTime;

    private static final int KB = 1024;
    private static final int UNIT = KB;

    public VegvisirAdapter(Context ctx) {
        instance = VegvisirInstanceV1.getInstance(ctx);
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setBlockRate(int blockRate) {
        this.blockRate = blockRate;
    }

    public void setEdTime(Date edTime) {
        this.edTime = edTime;
    }

    public void setStTime(Date stTime) {
        this.stTime = stTime;
    }

    /**
     * Start an experiment. Make sure all parameters are set correctly before call this method.
     */
    public void startCreatingBlocks() {
        Timer t = new Timer();
        final byte[] payload = new byte[blockSize * UNIT];
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                instance.addTransaction(null, Collections.singleton("Test"), payload, Collections.EMPTY_SET);
                if (edTime.before(new Date())) {
                    t.cancel();
                }
            }
        }, stTime, 1000 / blockRate);
    }
}
