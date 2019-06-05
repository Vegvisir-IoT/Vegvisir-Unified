package com.vegvisir.app.shoppinglist;

import com.vegvisir.application.VegvisirApplicationDelegator;
import com.vegvisir.application.VegvisirInstance;
import com.vegvisir.core.datatype.proto.Block;

public class VegvisirApplicationDelegatorImpl implements VegvisirApplicationDelegator {


    /**
     * Vegvisir will call this function to init and run application.
     *
     * @param instance a underlying Vegvisir instance for application use.
     */
    @Override
    public void init(VegvisirInstance instance) {
        /* TODO: Implement this */
    }


    /**
     * An application implemented function. This function will get called whenever a new transaction
     * subscribed by this application arrives.
     *
     * @param tx the new transaction that this application may interest.
     */
    @Override
    public void applyTransaction(Block.Transaction tx) {
        /* TODO: Implement this */
    }


    /**
     * Similar to above function except this function passes transactions in a batch for performance
     * optimization.
     *
     * @param txs a list of transactions to be applied.
     */
    @Override
    public void applyTransactions(Iterable<Block.Transaction> txs) {
        /* TODO: Implement this */
    }
}
