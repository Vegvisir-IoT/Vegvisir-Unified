package com.vegvisir.app.tasklist;

import android.util.Log;

import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.HashSet;
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



        String payloadString = new String(payload);

        int transactionType = Integer.parseInt(payloadString.substring(0,1));
        String item = payloadString.substring(1);

        Set<TransactionTuple> updatedSet = new HashSet<>();
        Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(item);
        String deviceId = tx_id.getDeviceID();

        Log.i("deps",deps.toString());

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
        HashSet<String> lowSet = new HashSet<>();
        HashSet<String> mediumSet = new HashSet<>();
        HashSet<String> highSet = new HashSet<>();
        HashSet<String> removeSet = new HashSet<>();

        for (TransactionID d : deps) {
            if (MainActivity.fourPSets.containsKey(d)) {
                lowSet.addAll(MainActivity.fourPSets.get(d).getLowSet());
                mediumSet.addAll(MainActivity.fourPSets.get(d).getMediumSet());
                highSet.addAll(MainActivity.fourPSets.get(d).getHighSet());
                removeSet.addAll(MainActivity.fourPSets.get(d).getRemoveSet());
            }
        }

        if (transactionType == 1) {
            lowSet.add(item);
            mediumSet.remove(item);
            highSet.remove(item);
            removeSet.remove(item);
        }
        else if (transactionType == 2) {
            lowSet.remove(item);
            mediumSet.add(item);
            highSet.remove(item);
            removeSet.remove(item);
        }
        else if (transactionType == 3) {
            lowSet.remove(item);
            mediumSet.remove(item);
            highSet.add(item);
            removeSet.remove(item);
        }
        else if (transactionType == 0){
            lowSet.remove(item);
            mediumSet.remove(item);
            highSet.remove(item);
            removeSet.add(item);
        }

        MainActivity.fourPSets.put(tx_id, new FourPSet(lowSet, mediumSet, highSet, removeSet));

        HashSet<String> lowSetTop = new HashSet<>();
        HashSet<String> mediumSetTop = new HashSet<>();
        HashSet<String> highSetTop = new HashSet<>();
        HashSet<String> removeSetTop = new HashSet<>();

        for (TransactionID d : MainActivity.topDeps) {
            if (MainActivity.fourPSets.containsKey(d)) {
                lowSetTop.addAll(MainActivity.fourPSets.get(d).getLowSet());
                mediumSetTop.addAll(MainActivity.fourPSets.get(d).getMediumSet());
                highSetTop.addAll(MainActivity.fourPSets.get(d).getHighSet());
                removeSetTop.addAll(MainActivity.fourPSets.get(d).getRemoveSet());
            }
        }

        MainActivity.fourPSets.put(MainActivity.top, new FourPSet(lowSetTop, mediumSetTop, highSetTop, removeSetTop));

        MainActivity.items.clear();
        MainActivity.priorities.clear();

        Set<String> newLowSet = lowSetTop;
        newLowSet.removeAll(mediumSetTop);
        newLowSet.removeAll(highSetTop);
        newLowSet.removeAll(removeSetTop);

        for(String lowItem: newLowSet) {
            MainActivity.items.add(lowItem);
            MainActivity.priorities.put(lowItem, MainActivity.Priority.Low);
        }

        Set<String> newMediumSet = mediumSetTop;
        newMediumSet.removeAll(highSetTop);
        newMediumSet.removeAll(removeSetTop);

        for(String mediumItem: newMediumSet) {
            MainActivity.items.add(mediumItem);
            MainActivity.priorities.put(mediumItem, MainActivity.Priority.Medium);
        }

        Set<String> newHighSet = highSetTop;
        newHighSet.removeAll(removeSetTop);

        for(String highItem: newHighSet) {
            MainActivity.items.add(highItem);
            MainActivity.priorities.put(highItem, MainActivity.Priority.High);
        }

        Log.i("setnew low",newLowSet.toString());
        Log.i("setnew medium",newMediumSet.toString());
        Log.i("setnew high",newHighSet.toString());
        Log.i("items",MainActivity.items.toString());


    }


}