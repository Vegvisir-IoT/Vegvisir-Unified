package com.vegvisir.application.profiling;

import java.util.Date;

public class ExperimentParameter {

    private long blockSize;
    private int blockRate;
    private Date startTime;
    private Date endTime;
    private int distance;

    private String name;

    public ExperimentParameter(long blockSize,
                               int blockRate,
                               Date startTime,
                               Date endTime,
                               int distance) {
        this.blockRate = blockRate;
        this.blockSize = blockSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public int getBlockRate() {
        return blockRate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getDistance() {
        return distance;
    }

    public String getExperimentName() {
        if (name == null) {
            name = startTime.getTime() + "-"+endTime.getTime()+"-"+distance;
        }
        return name;
    }

    public static ExperimentParameter.Builder getBuilder() {
        return new ExperimentParameter.Builder();
    }

    static class Builder {
        private long blockSize;
        private int blockRate;
        private Date startTime;
        private Date endTime;
        private int distance;

        Builder() {

        }

        public Builder setBlockRate(int blockRate) {
            this.blockRate = blockRate;
            return this;
        }

        public Builder setBlockSize(long blockSize) {
            this.blockSize = blockSize;
            return this;
        }

        public Builder setStartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setDistance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder setEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public ExperimentParameter build() {
            return new ExperimentParameter(blockSize, blockRate, startTime, endTime, distance);
        }
    }
}
