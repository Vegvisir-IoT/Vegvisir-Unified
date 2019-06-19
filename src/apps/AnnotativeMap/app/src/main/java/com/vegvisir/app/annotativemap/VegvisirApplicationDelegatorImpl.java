
package com.vegvisir.app.annotativemap;

import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.util.Log;

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
        int first = payloadString.indexOf("is");
        int x = Integer.parseInt(payloadString.substring(1,first));
        int second = payloadString.indexOf("is", first + 1);
        int y = Integer.parseInt(payloadString.substring(first+1,second));
        String anno = payloadString.substring(second+1);

        Coordinates coords = new Coordinates(x,y);

        Set<TransactionTuple> updatedSet = new HashSet<>();
        Set<TransactionTuple> prevSets = MainActivity.dependencySets.get(coords);
        String deviceId = tx_id.getDeviceID();


        if (prevSets != null) {
            Iterator<TransactionTuple> itr = prevSets.iterator();
            while (itr.hasNext()) {
                TransactionTuple tt = (TransactionTuple) ((Iterator) itr).next();

                if (!deps.contains(tt.transaction)) {

                    updatedSet.add(tt);
                }
            }
        }

        TransactionTuple t = new TransactionTuple(tx_id, transactionType);
        updatedSet.add(t);
        MainActivity.dependencySets.put(coords, updatedSet);

        MainActivity.latestTransactions.put(deviceId, tx_id);

        for (TransactionID d : deps) {
            MainActivity.topDeps.remove(d);
        }
        MainActivity.topDeps.add(tx_id);
        HashSet<Coordinates> addSet = new HashSet<>();
        HashSet<Coordinates> removeSet = new HashSet<>();

        for (TransactionID d : deps) {
            if (MainActivity.twoPSets.containsKey(d)) {
                addSet.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSet.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        if (transactionType == 1) {
            addSet.add(coords);
            removeSet.remove(coords);
        } else {
            addSet.remove(coords);
            removeSet.add(coords);
        }

        MainActivity.twoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

        HashSet<Coordinates> addSetTop = new HashSet<>();
        HashSet<Coordinates> removeSetTop = new HashSet<>();

        for (TransactionID d : MainActivity.topDeps) {
            if (MainActivity.twoPSets.containsKey(d)) {
                addSetTop.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSetTop.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        MainActivity.twoPSets.put(MainActivity.top, new TwoPSet(addSetTop, removeSetTop));

//        Set<Coordinates> newSet = addSetTop;
//        newSet.removeAll(removeSetTop);

        for (Coordinates c: addSetTop) {
            if (!MainActivity.imageAtCoords.containsKey(c)) {
                PictureTagLayout image = MainActivity.currentPicture.findViewById(R.id.image);
                MainActivity.imageAtCoords.put(c,image);
            }
        }

//        MainActivity.items.clear();
//        MainActivity.items.addAll(newSet);

    }


}
