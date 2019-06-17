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

        Log.i("startstr","1");
        String payloadString = new String(payload);
        int activityName = Integer.parseInt(payloadString.substring(0,1));
        int transactionType = Integer.parseInt(payloadString.substring(1,2));
        String item = payloadString.substring(2);

        if (activityName == 0) {
            Set<TransactionTuple> updatedSet = new HashSet<>();
            Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(item);

            if (prevSets != null) {
                Iterator<TransactionTuple> itr = prevSets.iterator();
                while (itr.hasNext()) {
                    TransactionTuple x = (TransactionTuple) ((Iterator) itr).next();

                    if (!deps.contains(x.transaction)) {
                        updatedSet.add(x);
                    }
                }
            }

            MainActivity.dependencySets.put(item, updatedSet);

            TransactionTuple t = new TransactionTuple(tx_id, transactionType);
            updatedSet.add(t);
            MainActivity.dependencySets.put(item, updatedSet);

            MainActivity.latestTransactions.put(tx_id.getDeviceID(), tx_id);

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

            Set<String> newSet = addSet;
            newSet.removeAll(removeSetTop);
            Log.i("new set: ", newSet.toString());
            MainActivity.items.clear();
            MainActivity.items.addAll(newSet);

            MainActivity.latestTransactions.put(tx_id.getDeviceID(),tx_id);
            
        }

//            if (tx_id.getDeviceID().equals(MainActivity.deviceId)) {
//                Log.i("enters if",MainActivity.items.toString());
//                if (transactionType == 0) {
//                    MainActivity.items.remove(item);
//                    Set<TransactionTuple> newSet = new HashSet<>();
//                    newSet.add(new TransactionTuple(tx_id,transactionType));
//                    MainActivity.dependencySets.put(item, newSet);
//                }
//                else {
//                    if (!MainActivity.items.contains(item)) {
//                        MainActivity.items.add(item);
//                    }
//                }
//            }
//            else {
//                Log.i("enters else",MainActivity.items.toString());
//                Iterator<TransactionTuple> it = updatedSet.iterator();
//                boolean flag = false;
//                while (it.hasNext()) {
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (x.transactionType == 0) { //0 = remove
//                        // remove item from array in MainActivity
//                        MainActivity.items.remove(item);
//                        Set<TransactionTuple> newSet = new HashSet<>();
//                        newSet.add(x);
//                        MainActivity.dependencySets.put(item, newSet);
//                        flag = true;
//                        break;
//                    }
//                }
//                Log.i("flag check", Boolean.toString(flag));
//
//                if (!flag) {
//                    Log.i("enters flag",MainActivity.items.toString());
//                    //add item to array in MainActivity
//                    if (!MainActivity.items.contains(item)) {
//                        MainActivity.items.add(item);
//                    }
//                }
//                Log.i("exits else",MainActivity.items.toString());
//            }
//        }

//        else {
//            Set<TransactionTuple> updatedSet = new HashSet<>();
//            Set<TransactionTuple> prevSets = MainActivity2.dependencySets.get(item);
//
//
//            if (prevSets != null) {
//                Iterator<TransactionTuple> itr = prevSets.iterator();
//                while (itr.hasNext()) {
//                    TransactionTuple x = (TransactionTuple) ((Iterator) itr).next();
//
//
//                    //if (!this.containsX(deps,x.transaction)) {
//                    if (!deps.contains(x.transaction)) {
////                        Log.i("x ID",x.transaction.getDeviceID());
////                        Log.i("x height",Long.toString(x.transaction.getTransactionHeight()));
////                        Log.i("enters if not depen",deps.toString());
//                        updatedSet.add(x);
//                    }
//                }
//            }
//            //updatedSet.clear();
//            TransactionTuple t = new TransactionTuple(tx_id, transactionType);
//            updatedSet.add(t);
//            MainActivity2.dependencySets.put(item, updatedSet);
//
//            MainActivity2.latestTransactions.put(tx_id.getDeviceID(), tx_id);
//
//
//            if (tx_id.getDeviceID().equals(MainActivity2.deviceId)) {
////                Log.i("enters if",MainActivity2.items.toString());
//                if (transactionType == 0) {
//                    MainActivity2.items.remove(item);
//                    Set<TransactionTuple> newSet = new HashSet<>();
//                    newSet.add(new TransactionTuple(tx_id,transactionType));
//                    MainActivity2.dependencySets.put(item, newSet);
//                }
//                else {
//                    if (!MainActivity2.items.contains(item)) {
//                        MainActivity2.items.add(item);
//                    }
//                }
//            }
//            else {
//                Log.i("enters else",MainActivity2.items.toString());
//                Iterator<TransactionTuple> it = updatedSet.iterator();
//                boolean flag = false;
//                while (it.hasNext()) {
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (x.transactionType == 0) { //0 = remove
//                        // remove item from array in MainActivity
//                        MainActivity2.items.remove(item);
//                        Set<TransactionTuple> newSet = new HashSet<>();
//                        newSet.add(x);
//                        MainActivity2.dependencySets.put(item, newSet);
//                        flag = true;
//                        break;
//                    }
//                }
//                Log.i("flag check", Boolean.toString(flag));
//
//                if (!flag) {
//                    Log.i("enters flag",MainActivity2.items.toString());
//                    //add item to array in MainActivity
//                    if (!MainActivity2.items.contains(item)) {
//                        MainActivity2.items.add(item);
//                    }
//                }
////                Log.i("exits else",MainActivity2.items.toString());
//            }
//        }

//        Log.i("test", MainActivity.dependencySets.toString());

        //Log.i("From app del",MainActivity.items.toString());

    }


}