package com.vegvisir.pub_sub;


import com.vegvisir.core.datatype.proto.Block;

/**
 * A unique identifier for a transaction
 */
public class TransactionID {


    /* A ID for device who owns and created this transaction */
    private String deviceID;

    /* A transaction height is a counter value of how many transactions has been
    * created on the device that created this transaction */
    private long transactionHeight;


    public TransactionID(String deviceID, long transactionHeight) {
        this.deviceID = deviceID;
        this.transactionHeight = transactionHeight;
    }

    public long getTransactionHeight() {
        return transactionHeight;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
