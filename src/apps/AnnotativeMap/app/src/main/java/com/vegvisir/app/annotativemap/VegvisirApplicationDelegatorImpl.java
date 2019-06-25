
package com.vegvisir.app.annotativemap;

import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
        int first = payloadString.indexOf(",");
        int x = Integer.parseInt(payloadString.substring(1,first));
        int second = payloadString.indexOf(",", first + 1);
        int y = Integer.parseInt(payloadString.substring(first+1,second));
        String anno = payloadString.substring(second+1);

        Coordinates coords = new Coordinates(x,y);

        PictureTagLayout image = MainActivity.currentPicture.findViewById(R.id.image);
        PictureTagView pointView = image.justHasView(x,y);
        if (pointView != null) {
            Log.i("View","found");
            if (MainActivity.coordForViews.containsKey(pointView)) {
                coords = MainActivity.coordForViews.get(pointView);
                x = coords.getX();
                y = coords.getY();
            }
            else {
                Log.i("View","Should have a coordinate mapping");
                MainActivity.coordForViews.put(pointView,coords);
            }
        }
        else {
            Log.i("View","not found");
        }

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
            PictureTagView view = image.justHasView(x,y);
            if (view != null) {

                boolean doesExist = false;
                for (FullAnnotation fa: addSet) {
                    Coordinates c = fa.getCoords();
                    if (view.justHasView(c.getX(),c.getY())) {
                        doesExist = true;
                        break;
                    }

                }

                if (addSet.isEmpty() || !doesExist) {
                    addSet.add(new FullAnnotation(coords,anno));
                }

                for(FullAnnotation fa: removeSet) {
                    Coordinates c = fa.getCoords();

                    if (view.justHasView(c.getX(),c.getY())) {
                        removeSet.remove(fa);
                        break;
                    }
                }
            }

            else {
                addSet.add(new FullAnnotation(coords,anno));
            }

        } else {
            PictureTagView view = image.justHasView(x,y);
            if (view != null) {

                for (FullAnnotation fa: addSet) {
                    Coordinates c = fa.getCoords();
                    if (view.justHasView(c.getX(),c.getY())) {
                        addSet.remove(fa);
                        break;
                    }
                }

                boolean doesExist = false;
                for(FullAnnotation fa: removeSet) {
                    Coordinates c = fa.getCoords();

                    if (view.justHasView(c.getX(),c.getY())) {
                        doesExist = true;
                        break;
                    }
                }

                if (removeSet.isEmpty() || !doesExist) {
                    removeSet.add(new FullAnnotation(coords,anno));
                }
            }

            else {
                addSet.add(new FullAnnotation(coords,anno));
            }

        }

        MainActivity.twoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

        HashSet<FullAnnotation> addSetTop = new HashSet<>();
        HashSet<FullAnnotation> removeSetTop = new HashSet<>();

//        Log.i("txid",tx_id.toString());
//        Log.i("topdeps",MainActivity.topDeps.toString());
//        Log.i("TwoPSets",MainActivity.twoPSets.toString());

        for (TransactionID d : MainActivity.topDeps) {
//            Log.i("d",d.toString());
            if (MainActivity.twoPSets.containsKey(d)) {
//                Log.i("gets here","");
                addSetTop.addAll(MainActivity.twoPSets.get(d).getAddSet());
                removeSetTop.addAll(MainActivity.twoPSets.get(d).getRemoveSet());
            }
        }

        Log.i("addsettop",addSetTop.toString());
        Log.i("remset",removeSetTop.toString());

        MainActivity.twoPSets.put(MainActivity.top, new TwoPSet(addSetTop, removeSetTop));

//       c

//        Set<Coordinates> newSet = addSetTop;
//        newSet.removeAll(removeSetTop);

        for (FullAnnotation fa: addSetTop) {
            Coordinates c = fa.getCoords();
//            boolean exists = false;

            PictureTagView view = image.justHasView(c.getX(),c.getY());
            if (view != null) {
                Coordinates adjustedCoords = MainActivity.coordForViews.get(view);
                MainActivity.annotations.get(adjustedCoords).setAnnotation(anno);
            }

            else {
                MainActivity.annotations.put(c,new Annotation(fa.getAnnotation()));
            }

//            for (Map.Entry<Coordinates, PictureTagView> entry : MainActivity.imageAtCoords.entrySet()) {
//                PictureTagView view = entry.getValue();
//                Coordinates coordinates = entry.getKey();
//                if (view.justHasView(c.getX(),c.getY()) || coordinates.equals(c)) {
//                    MainActivity.annotations.get(entry.getKey()).setAnnotation(anno);
//                    exists = true;
//                    break;
//                }
//            }
//
//            if (!exists) {
//                MainActivity.annotations.put(c,new Annotation(fa.getAnnotation()));
//        }
        }

//        for (Map.Entry<Coordinates, PictureTagLayout> entry : MainActivity.imageAtCoords.entrySet()) {
//
//        }

        for (FullAnnotation fa: removeSetTop) {
            Coordinates c = fa.getCoords();

            PictureTagView view = image.justHasView(c.getX(),c.getY());
            if (view != null) {
                Coordinates adjustedCoords = MainActivity.coordForViews.get(view);
                MainActivity.annotations.get(adjustedCoords).setShouldRemove(true);
            }
            else {
                Log.i("Remove view","not found");
            }

//            for (Map.Entry<Coordinates, PictureTagView> entry : MainActivity.imageAtCoords.entrySet()) {
//                PictureTagView view = entry.getValue();
//                if (view.justHasView(c.getX(),c.getY())) {
//                    Log.i("here","comes");
//                    MainActivity.annotations.get(entry.getKey()).setShouldRemove(true);
//                }
//            }


//            if (MainActivity.imageAtCoords.containsKey(c)) {
//                MainActivity.annotations.get(c).setShouldRemove(true);
//            }
        }

    }


}