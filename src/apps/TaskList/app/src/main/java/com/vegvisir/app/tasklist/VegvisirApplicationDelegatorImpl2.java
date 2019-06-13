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
public class VegvisirApplicationDelegatorImpl2 implements VegvisirApplicationDelegator {


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

        Set<Thread> threads = Thread.getAllStackTraces().keySet();

        for (Thread thread: threads){
            Log.i("ac",thread.getName());
        }

        String payloadString = new String(payload);
        int transactionType = Integer.parseInt(payloadString.substring(0,1));
        String item = payloadString.substring(1);
        Log.i("deps2",deps.toString());
        for (TransactionID dep:deps) {
            Log.i("dep ID",dep.getDeviceID());
            Log.i("dep height",Long.toString(dep.getTransactionHeight()));
        }

        Set<TransactionTuple> updatedSet = new HashSet<>();
        Set<TransactionTuple> prevSets = MainActivity2.dependencySets.get(item);


        if (prevSets != null) {
            Iterator<TransactionTuple> itr = prevSets.iterator();
            while (itr.hasNext()) {
                TransactionTuple x = (TransactionTuple) ((Iterator) itr).next();


                if (!this.containsX(deps,x.transaction)) {
                    Log.i("x ID",x.transaction.getDeviceID());
                    Log.i("x height",Long.toString(x.transaction.getTransactionHeight()));
                    Log.i("enters if not depen",deps.toString());
                    updatedSet.add(x);
                }
            }
        }
        //updatedSet.clear();
        TransactionTuple t = new TransactionTuple(tx_id, transactionType);
        updatedSet.add(t);
        MainActivity2.dependencySets.put(item, updatedSet);

        MainActivity2.latestTransactions.put(tx_id.getDeviceID(), tx_id);


        if (tx_id.getDeviceID().equals(MainActivity2.deviceId)) {
            Log.i("enters if",MainActivity2.items.toString());
            if (transactionType == 0) {
                MainActivity2.items.remove(item);
                Set<TransactionTuple> newSet = new HashSet<>();
                newSet.add(new TransactionTuple(tx_id,transactionType));
                MainActivity2.dependencySets.put(item, newSet);
            }
            else {
                if (!MainActivity2.items.contains(item)) {
                    MainActivity2.items.add(item);
                }
            }
        }
        else {
            Log.i("enters else",MainActivity2.items.toString());
            Iterator<TransactionTuple> it = updatedSet.iterator();
            boolean flag = false;
            while (it.hasNext()) {
                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                if (x.transactionType == 0) { //0 = remove
                    // remove item from array in MainActivity
                    MainActivity2.items.remove(item);
                    Set<TransactionTuple> newSet = new HashSet<>();
                    newSet.add(x);
                    MainActivity2.dependencySets.put(item, newSet);
                    flag = true;
                    break;
                }
            }
            Log.i("flag check", Boolean.toString(flag));

            if (!flag) {
                Log.i("enters flag",MainActivity2.items.toString());
                //add item to array in MainActivity
                if (!MainActivity2.items.contains(item)) {
                    MainActivity2.items.add(item);
                }
            }
            Log.i("exits else",MainActivity2.items.toString());
        }
        Log.i("test", MainActivity2.dependencySets.toString());

        //Log.i("From app del",MainActivity.items.toString());

    }

    public boolean containsX(Set<TransactionID> s, TransactionID t) {
        Iterator<TransactionID> itr2 = s.iterator();
        while (itr2.hasNext()) {
            TransactionID id = (TransactionID) ((Iterator) itr2).next();
            if (t.getDeviceID().equals(id.getDeviceID()) && (t.getTransactionHeight() == id.getTransactionHeight())) {
                Log.i("works", "ok");
                return true;
            }
        }

        return false;

    }

}