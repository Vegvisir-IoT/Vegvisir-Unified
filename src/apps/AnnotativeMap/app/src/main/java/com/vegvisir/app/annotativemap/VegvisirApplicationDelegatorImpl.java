//import com.vegvisir.core.datatype.proto.Block;
package com.vegvisir.app.annotativemap;

import java.util.Set;

import com.vegvisir.pub_sub.*;

/**
 * Ideally, all applications should implement this interface.
 */
public class VegvisirApplicationDelegatorImpl implements VegvisirApplicationDelegator{

    /**
     * Vegvisir will call this function to init and run application.
     * @param instance a underlying Vegvisir instance for application use.
     */
    public void init(VegvisirInstance instance){

    }


    /**
     * An application implemented function. This function will get called whenever a new transaction
     * subscribed by this application arrives.
     * @param topics topics that this transaction is created for.
     * @param payload application specific data.
     * @param tx_id A unique identifier for the transaction.
     * @param deps which transactions this transaction depends on.
     */
    public void applyTransaction(
            Set<String> topics,
            byte[] payload,
            TransactionID tx_id,
            Set<TransactionID> deps) {

    }
}