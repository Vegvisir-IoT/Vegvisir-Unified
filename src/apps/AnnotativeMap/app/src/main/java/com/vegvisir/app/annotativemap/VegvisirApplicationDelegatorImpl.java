package com.vegvisir.app.annotativemap;

import com.vegvisir.app.annotativemap.ui.login.LoginActivity;
import com.vegvisir.pub_sub.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import android.util.Log;
import android.app.Activity;

import java.util.Set;

import com.vegvisir.pub_sub.*;

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

//        LoginActivity.delegator.applyTransaction(topics,payload,tx_id,deps);

//        String payloadString = new String(payload);
//        Log.i("payload",payloadString);
//
//        int transactionType = Integer.parseInt(payloadString.substring(0,1));
//        int first = payloadString.indexOf(",");
//        int x = Integer.parseInt(payloadString.substring(1,first));
//        int second = payloadString.indexOf(",", first + 1);
//        int y = Integer.parseInt(payloadString.substring(first+1,second));
//        String anno = payloadString.substring(second+1);
//
//        Coordinates coords = new Coordinates(x,y);
//
//        PictureTagLayout image = MainActivity.currentPicture.findViewById(R.id.image);
//        PictureTagView pointView = image.justHasView(x,y);
//        if (pointView != null) {
//            Log.i("View","found");
//            x = pointView.getXVal();
//            y = pointView.getYVal();
//        }
//        else {
//            Log.i("View","not found");
//        }
//
//        Set<TransactionTuple> updatedSet = new HashSet<>();
//        Set<TransactionTuple> prevSets = MainActivity.mapDependencySets.get(coords);
//        String deviceId = tx_id.getDeviceID();
//
//
//        if (prevSets != null) {
//            Iterator<TransactionTuple> itr = prevSets.iterator();
//            while (itr.hasNext()) {
//                TransactionTuple tt = (TransactionTuple) ((Iterator) itr).next();
//
//                if (!deps.contains(tt.transaction)) {
//
//                    updatedSet.add(tt);
//                }
//            }
//        }
//
//        TransactionTuple t = new TransactionTuple(tx_id, transactionType);
//        updatedSet.add(t);
//        MainActivity.mapDependencySets.put(coords, updatedSet);
//
//        MainActivity.mapLatestTransactions.put(deviceId, tx_id);
//
//        for (TransactionID d : deps) {
//            MainActivity.mapTopDeps.remove(d);
//        }
//        MainActivity.mapTopDeps.add(tx_id);
//        HashSet<FullAnnotation> addSet = new HashSet<>();
//        HashSet<FullAnnotation> removeSet = new HashSet<>();
//
//        for (TransactionID d : deps) {
//            if (MainActivity.mapTwoPSets.containsKey(d)) {
//                addSet.addAll(MainActivity.mapTwoPSets.get(d).getAddSet());
//                removeSet.addAll(MainActivity.mapTwoPSets.get(d).getRemoveSet());
//            }
//        }
//
//        if (transactionType == 1) {
//            PictureTagView view = image.justHasView(x,y);
//            if (view != null) {
//
//                boolean doesExist = false;
//                for (FullAnnotation fa: addSet) {
//                    Coordinates c = fa.getCoords();
//                    if (image.justHasView(c.getX(),c.getY()) != null) {
//                        doesExist = true;
//                        break;
//                    }
//                }
//
//                if (addSet.isEmpty() || !doesExist) {
//                    addSet.add(new FullAnnotation(coords,anno));
//                }
//
//                for(FullAnnotation fa: removeSet) {
//                    Coordinates c = fa.getCoords();
//                    if (image.justHasView(c.getX(),c.getY()) != null) {
//                        removeSet.remove(fa);
//                        break;
//                    }
//                }
//            }
//
//            else {
//                addSet.add(new FullAnnotation(coords,anno));
//            }
//
//        } else {
//            PictureTagView view = image.justHasView(x,y);
//            if (view != null) {
//
//                for (FullAnnotation fa: addSet) {
//                    Coordinates c = fa.getCoords();
//                    if (image.justHasView(c.getX(),c.getY()) != null) {
//                        addSet.remove(fa);
//                        break;
//                    }
//                }
//
//                boolean doesExist = false;
//                for(FullAnnotation fa: removeSet) {
//                    Coordinates c = fa.getCoords();
//
//                    if (image.justHasView(c.getX(),c.getY()) != null) {
//                        doesExist = true;
//                        break;
//                    }
//                }
//
//                if (removeSet.isEmpty() || !doesExist) {
//                    removeSet.add(new FullAnnotation(coords,anno));
//                }
//            }
//
//            else {
//                addSet.add(new FullAnnotation(coords,anno));
//            }
//
//        }
//        FullAnnotation faToModify = null;
//        for (FullAnnotation fa: addSet) {
//            Coordinates c = fa.getCoords();
//            if (coords.equals(c)) {
//                faToModify = fa;
//            }
//        }
//        if (faToModify != null) {
//            FullAnnotation newFa = new FullAnnotation(faToModify.getCoords(),anno);
//            addSet.remove(faToModify);
//            addSet.add(newFa);
//        }
//
//        MainActivity.mapTwoPSets.put(tx_id, new TwoPSet(addSet, removeSet));
//
//        Log.i("addset",addSet.toString());
//        Log.i("remset",removeSet.toString());
//
//        HashSet<FullAnnotation> addSetTop = new HashSet<>();
//        HashSet<FullAnnotation> removeSetTop = new HashSet<>();
//
////        Log.i("txid",tx_id.toString());
////        Log.i("topdeps",MainActivity.mapTopDeps.toString());
////        Log.i("TwoPSets",MainActivity.mapTwoPSets.toString());
//
//        for (TransactionID d : MainActivity.mapTopDeps) {
////            Log.i("d",d.toString());
//            if (MainActivity.mapTwoPSets.containsKey(d)) {
////                Log.i("gets here","");
//                addSetTop.addAll(MainActivity.mapTwoPSets.get(d).getAddSet());
//                removeSetTop.addAll(MainActivity.mapTwoPSets.get(d).getRemoveSet());
//            }
//        }
//
//        Log.i("addsettop",addSetTop.toString());
//        Log.i("remsettop",removeSetTop.toString());
//
//        MainActivity.mapTwoPSets.put(MainActivity.mapTop, new TwoPSet(addSetTop, removeSetTop));
//
////       c
//
////        Set<Coordinates> newSet = addSetTop;
////        newSet.removeAll(removeSetTop);
//
//        PictureTagView v = image.justHasView(coords.getX(),coords.getY());
//        if (v != null) {
//            if (MainActivity.annotations.containsKey(coords)) {
//                Log.i("set","case");
//                MainActivity.annotations.get(coords).setAnnotation(anno);
//            }
//            else {
//                MainActivity.annotations.put(coords,new Annotation(anno));
//            }
//        }
//        else {
//            Log.i("View","does not exist");
//            MainActivity.annotations.put(coords,new Annotation(anno));
//        }
//
//        for (FullAnnotation fa: addSetTop) {
//            Coordinates c = fa.getCoords();
//            String annotation = fa.getAnnotation();
//
//            if (!MainActivity.annotations.containsKey(c)) {
//                Log.i("hashmap","doesn't contain anno");
//                MainActivity.annotations.put(c, new Annotation(annotation));
//            }
//
//        }
//
////        for (Map.Entry<Coordinates, PictureTagLayout> entry : MainActivity.imageAtCoords.entrySet()) {
////
////        }
//
//        for (FullAnnotation fa: removeSetTop) {
//            Coordinates c = fa.getCoords();
//
//            PictureTagView view = image.justHasView(c.getX(),c.getY());
//            if (view != null) {
//                Coordinates adjustedCoords = new Coordinates(view.getXVal(),view.getYVal());
//                MainActivity.annotations.get(adjustedCoords).setShouldRemove(true);
//            }
//            else {
//                Log.i("Remove view","not found");
//            }
//
////            for (Map.Entry<Coordinates, PictureTagView> entry : MainActivity.imageAtCoords.entrySet()) {
////                PictureTagView view = entry.getValue();
////                if (view.justHasView(c.getX(),c.getY())) {
////                    Log.i("here","comes");
////                    MainActivity.annotations.get(entry.getKey()).setShouldRemove(true);
////                }
////            }
//
//
////            if (MainActivity.imageAtCoords.containsKey(c)) {
////                MainActivity.annotations.get(c).setShouldRemove(true);
////            }
//        }
//
//        Log.i("annosinimpl",MainActivity.annotations.toString());


    }

    public void onNewReconciliationFinished() {

    }

}