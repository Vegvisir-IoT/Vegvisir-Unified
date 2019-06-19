
package com.vegvisir.app.annotativemap;

import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
        int first = payloadString.indexOf(",");
        int x = Integer.parseInt(payloadString.substring(1,first));
        int second = payloadString.indexOf(",", first + 1);
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
        HashSet<FullAnnotation> addSet = new HashSet<>();
        HashSet<FullAnnotation> removeSet = new HashSet<>();

        for (TransactionID d : deps) {
            if (MainActivity.twoPSets.containsKey(d)) {
                addSet.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSet.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        if (transactionType == 1) {
            if (addSet.isEmpty()) {
                addSet.add(new FullAnnotation(coords,anno));
            }
            else{
                boolean doesExist = false;
                for(FullAnnotation fa: addSet) {
                    Coordinates c = fa.getCoords();
                    String a = fa.getAnnotation();
                    PictureTagLayout image = MainActivity.imageAtCoords.get(c);

                    if (!image.justHasView(x, y)) {
                        doesExist = true;
                        break;
                    }
                }
                if (!doesExist) {
                    addSet.add(new FullAnnotation(coords, anno));
                }

            }

            for(FullAnnotation fa: removeSet) {
                Coordinates c = fa.getCoords();
                String a = fa.getAnnotation();
                PictureTagLayout image = MainActivity.imageAtCoords.get(c);

                if (image.justHasView(x,y)) {
                    removeSet.remove(fa);
                    break;
                }
            }
        } else {
            for(FullAnnotation fa: addSet) {
                Coordinates c = fa.getCoords();
                String a = fa.getAnnotation();
                PictureTagLayout image = MainActivity.imageAtCoords.get(c);

                if (image.justHasView(x,y)) {
                    addSet.remove(fa);
                    break;
                }
            }

            if (removeSet.isEmpty()){
                removeSet.add(new FullAnnotation(coords,anno));
            }
            else{
                boolean doesExist = false;
                for(FullAnnotation fa: removeSet) {
                    Coordinates c = fa.getCoords();
                    String a = fa.getAnnotation();
                    PictureTagLayout image = MainActivity.imageAtCoords.get(c);

                    if (image.justHasView(x,y)) {
                        doesExist = true;
                        break;
                    }
                }
                if (!doesExist) {
                    removeSet.add(new FullAnnotation(coords,anno));
                }
            }

        }

        Log.i("addset",addSet.toString());
        Log.i("remset",removeSet.toString());

        MainActivity.twoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

        HashSet<FullAnnotation> addSetTop = new HashSet<>();
        HashSet<FullAnnotation> removeSetTop = new HashSet<>();

        Log.i("txid",tx_id.toString());
        Log.i("topdeps",MainActivity.topDeps.toString());
        Log.i("TwoPSets",MainActivity.twoPSets.toString());

        for (TransactionID d : MainActivity.topDeps) {
            Log.i("d",d.toString());
            if (MainActivity.twoPSets.containsKey(d)) {
                Log.i("gets here","");
                addSetTop.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSetTop.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        MainActivity.twoPSets.put(MainActivity.top, new TwoPSet(addSetTop, removeSetTop));

//        Log.i("addsettop",addSetTop.toString());

//        Set<Coordinates> newSet = addSetTop;
//        newSet.removeAll(removeSetTop);

        for (FullAnnotation fa: addSetTop) {
            Coordinates c = fa.getCoords();
            if (!MainActivity.imageAtCoords.containsKey(c)) {
                PictureTagLayout image = MainActivity.currentPicture.findViewById(R.id.image);
                MainActivity.imageAtCoords.put(c,image);
                MainActivity.annotations.put(c,new Annotation(fa.getAnnotation()));
            }
            else {
                MainActivity.annotations.get(c).setAnnotation(anno);
            }
        }

        for (FullAnnotation fa: removeSetTop) {
            Coordinates c = fa.getCoords();
            if (MainActivity.imageAtCoords.containsKey(c)) {
                MainActivity.annotations.get(c).setShouldRemove(true);
            }
        }

    }


}
