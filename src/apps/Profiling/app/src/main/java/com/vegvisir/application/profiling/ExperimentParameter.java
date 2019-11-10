package com.vegvisir.application.profiling;

import java.util.Date;

public class ExperimentParameter {

    private int blockSize;
    private int blockRate;
    private Date startTime;
    private Date endTime;
    private int distance;
    private int samplingRate;

    private String name;

    public ExperimentParameter(int blockSize,
                               int blockRate,
                               Date startTime,
                               Date endTime,
                               int distance,
                               int samplingRate) {
        this.blockRate = blockRate;
        this.blockSize = blockSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.samplingRate = samplingRate;
    }

    public int getBlockSize() {
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

    public int getSamplingRate() {
        return samplingRate;
    }

    public int getSamplingPeriod() {
        return 1000 / samplingRate;
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
        private int blockSize;
        private int blockRate;
        private Date startTime;
        private Date endTime;
        private int distance;
        private int samplingRate;

        Builder() {

        }

        public Builder setBlockRate(int blockRate) {
            this.blockRate = blockRate;
            return this;
        }

        public Builder setBlockSize(int blockSize) {
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

        public Builder setSamplingRate(int samplingRate) {
            this.samplingRate = samplingRate;
            return this;
        }

        public ExperimentParameter build() {
            return new ExperimentParameter(blockSize, blockRate, startTime, endTime, distance, samplingRate);
        }
    }
}
