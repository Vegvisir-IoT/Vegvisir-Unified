package com.vegvisir.app.tasklist;

import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.util.Log;
import java.lang.Thread;

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

//        if (activityName == 0) {
        Set<TransactionTuple> updatedSet = new HashSet<>();
        Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(item);
        String deviceId = tx_id.getDeviceID();


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

        MainActivity.latestTransactions.put(deviceId, tx_id);

        for (TransactionID d : deps) {
            MainActivity.topDeps.remove(d);
        }
        MainActivity.topDeps.add(tx_id);
        HashSet<String> addSet = new HashSet<>();
        HashSet<String> removeSet = new HashSet<>();

        for (TransactionID d : deps) {
            if (MainActivity.twoPSets.containsKey(d)) {
                addSet.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSet.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        if (transactionType == 1) {
            addSet.add(item);
            removeSet.remove(item);
        } else {
            addSet.remove(item);
            removeSet.add(item);
        }

        MainActivity.twoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

        HashSet<String> addSetTop = new HashSet<>();
        HashSet<String> removeSetTop = new HashSet<>();

        for (TransactionID d : MainActivity.topDeps) {
            if (MainActivity.twoPSets.containsKey(d)) {
                addSetTop.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSetTop.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        MainActivity.twoPSets.put(MainActivity.top, new TwoPSet(addSetTop, removeSetTop));

        Log.i("2pset", MainActivity.twoPSets.toString());
        Log.i("weird", "okok" + tx_id.getDeviceID());

        Log.i("topdeps", MainActivity.topDeps.toString());

        Log.i("addTop", addSetTop.toString());
        Log.i("removeTop", removeSetTop.toString());


        Set<String> newSet = addSetTop;
        newSet.removeAll(removeSetTop);
        Log.i("new set", newSet.toString());
        MainActivity.items.clear();
        MainActivity.items.addAll(newSet);


    }


}