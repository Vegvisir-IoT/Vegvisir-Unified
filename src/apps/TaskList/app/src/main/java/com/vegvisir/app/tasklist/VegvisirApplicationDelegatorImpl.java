package com.vegvisir.app.tasklist;

import com.vegvisir.pub_sub.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * Ideally, all applications should implement this interface.
 */
public class VegvisirApplicationDelegatorImpl implements VegvisirApplicationDelegator {


    /**
     * Vegvisir will call this function to init and run application.
     * @param instance a underlying Vegvisir instance for application use.
     */
    public void init(VegvisirInstance instance) {

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

        String item = "haircut";
        int transactionType = 0;

        if (Integer.parseInt(tx_id.getDeviceID()) > Integer.parseInt(MainActivity.latestTransactions.get(item).getDeviceID()) ){

            Set<TransactionTuple> updatedSet = Collections.emptySet();
            Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(item);

            Iterator<TransactionTuple> itr = prevSets.iterator();
            while(itr.hasNext()){
                TransactionTuple x =  (TransactionTuple) ((Iterator) itr).next();
                if (!deps.contains(x.transaction)) {
                    updatedSet.add(x);
                }
            }
            TransactionTuple t = new TransactionTuple(tx_id, transactionType);
            updatedSet.add(t);
            MainActivity.dependencySets.put(item, updatedSet);

            MainActivity.latestTransactions.put(tx_id.getDeviceID(), tx_id);

            Iterator<TransactionTuple> it = updatedSet.iterator();
            boolean flag = false;
            while(it.hasNext()){
                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                if (x.transactionType == 0) { //0 = remove
                    // remove item from array in MainActivity
                    MainActivity.mAdapter.remove(item);
                    MainActivity.mAdapter.notifyDataSetChanged();
                    flag = true;
                    break;
                }
            }

            if (!flag){
                //add item to array in MainActivity
                MainActivity.mAdapter.add(item);
                MainActivity.mAdapter.notifyDataSetChanged();
            }

        }


    }
}