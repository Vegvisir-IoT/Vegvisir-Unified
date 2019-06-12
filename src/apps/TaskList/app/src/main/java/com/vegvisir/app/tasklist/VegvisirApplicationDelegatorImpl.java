package com.vegvisir.app.tasklist;

import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.util.Log;
import android.app.Activity;

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

        String payloadString = new String(payload);
        int transactionType = Integer.parseInt(payloadString.substring(0,1));
        String item = payloadString.substring(1);

        Set<TransactionTuple> updatedSet = new HashSet<>();
        Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(item);
        Log.i("item",item);
        if (prevSets != null) {
            Iterator<TransactionTuple> itr = prevSets.iterator();
            while (itr.hasNext()) {
                TransactionTuple x = (TransactionTuple) ((Iterator) itr).next();
                if (!deps.contains(x.transaction)) {
                    updatedSet.add(x);
                }
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
                MainActivity.items.remove(item);
                Set<TransactionTuple>  newSet = new HashSet<>();
                newSet.add(x);
                MainActivity.dependencySets.put(item, newSet);
                flag = true;
                break;
            }
        }

        if (!flag){
            //add item to array in MainActivity
            if (!MainActivity.items.contains(item)){
                MainActivity.items.add(item);
            }
        }

    }

}