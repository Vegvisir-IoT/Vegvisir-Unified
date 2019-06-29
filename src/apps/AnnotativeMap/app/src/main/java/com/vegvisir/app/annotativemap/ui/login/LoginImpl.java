package com.vegvisir.app.annotativemap.ui.login;

import android.util.Log;

import com.vegvisir.app.annotativemap.Annotation;
import com.vegvisir.app.annotativemap.Coordinates;
import com.vegvisir.app.annotativemap.FullAnnotation;
import com.vegvisir.app.annotativemap.MainActivity;
import com.vegvisir.app.annotativemap.PictureTagLayout;
import com.vegvisir.app.annotativemap.PictureTagView;
import com.vegvisir.app.annotativemap.R;
import com.vegvisir.app.annotativemap.TransactionTuple;
import com.vegvisir.app.annotativemap.TwoPSet;
import com.vegvisir.app.annotativemap.User;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Ideally, all applications should implement this interface.
 */
public class LoginImpl implements VegvisirApplicationDelegator {


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
        if (transactionType > 4) {
            int usernamePos = payloadString.indexOf(",");
            String username = payloadString.substring(1,usernamePos);
            String password = payloadString.substring(usernamePos + 1);

            Set<TransactionTuple> updatedSet = new HashSet<>();
            Set<TransactionTuple> prevSets = LoginActivity.dependencySets.get(username);
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
            LoginActivity.dependencySets.put(username, updatedSet);

            LoginActivity.latestTransactions.put(deviceId, tx_id);

            for (TransactionID d : deps) {
                LoginActivity.topDeps.remove(d);
            }
            LoginActivity.topDeps.add(tx_id);
            HashSet<User> addSet = new HashSet<>();
            HashSet<User> removeSet = new HashSet<>();

            for (TransactionID d : deps) {
                if (LoginActivity.twoPSets.containsKey(d)) {
                    addSet.addAll(LoginActivity.twoPSets.get(d).getAddSet());
                    removeSet.addAll(LoginActivity.twoPSets.get(d).getRemoveSet());
                }
            }

            if (transactionType == 5) {
                addSet.add(new User(username, password));
                removeSet.remove(username);
            } else {
                addSet.remove(username);
                removeSet.add(new User(username, password));
            }

            LoginActivity.twoPSets.put(tx_id, new TwoPSetUser(addSet, removeSet));

            HashSet<User> addSetTop = new HashSet<>();
            HashSet<User> removeSetTop = new HashSet<>();

            for (TransactionID d : LoginActivity.topDeps) {
                if (LoginActivity.twoPSets.containsKey(d)) {
                    addSetTop.addAll(LoginActivity.twoPSets.get(d).getAddSet());
                    removeSetTop.addAll(LoginActivity.twoPSets.get(d).getRemoveSet());
                }
            }

            LoginActivity.twoPSets.put(LoginActivity.top, new TwoPSetUser(addSetTop, removeSetTop));


            Set<User> newSet = addSetTop;
            newSet.removeAll(removeSetTop);
            //Log.i("new set", newSet.toString());
            LoginActivity.usernames.clear();
            for (User u: newSet){
                LoginActivity.usernames.put(u.getUsername(), u.getPassword());
            }
        }

        else{
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
                x = pointView.getXVal();
                y = pointView.getYVal();
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
                        if (image.justHasView(c.getX(),c.getY()) != null) {
                            doesExist = true;
                            break;
                        }
                    }

                    if (addSet.isEmpty() || !doesExist) {
                        addSet.add(new FullAnnotation(coords,anno));
                    }

                    for(FullAnnotation fa: removeSet) {
                        Coordinates c = fa.getCoords();
                        if (image.justHasView(c.getX(),c.getY()) != null) {
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
                        if (image.justHasView(c.getX(),c.getY()) != null) {
                            addSet.remove(fa);
                            break;
                        }
                    }

                    boolean doesExist = false;
                    for(FullAnnotation fa: removeSet) {
                        Coordinates c = fa.getCoords();

                        if (image.justHasView(c.getX(),c.getY()) != null) {
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
            FullAnnotation faToModify = null;
            for (FullAnnotation fa: addSet) {
                Coordinates c = fa.getCoords();
                if (coords.equals(c)) {
                    faToModify = fa;
                }
            }
            if (faToModify != null) {
                FullAnnotation newFa = new FullAnnotation(faToModify.getCoords(),anno);
                addSet.remove(faToModify);
                addSet.add(newFa);
            }

            MainActivity.twoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

            Log.i("addset",addSet.toString());
            Log.i("remset",removeSet.toString());

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
            Log.i("remsettop",removeSetTop.toString());

            MainActivity.twoPSets.put(MainActivity.top, new TwoPSet(addSetTop, removeSetTop));

//       c

//        Set<Coordinates> newSet = addSetTop;
//        newSet.removeAll(removeSetTop);

            PictureTagView v = image.justHasView(coords.getX(),coords.getY());
            if (v != null) {
                if (MainActivity.annotations.containsKey(coords)) {
                    Log.i("set","case");
                    MainActivity.annotations.get(coords).setAnnotation(anno);
                }
                else {
                    MainActivity.annotations.put(coords,new Annotation(anno));
                }
            }
            else {
                Log.i("View","does not exist");
                MainActivity.annotations.put(coords,new Annotation(anno));
            }

            for (FullAnnotation fa: addSetTop) {
                Coordinates c = fa.getCoords();
                String annotation = fa.getAnnotation();

                if (!MainActivity.annotations.containsKey(c)) {
                    Log.i("hashmap","doesn't contain anno");
                    MainActivity.annotations.put(c, new Annotation(annotation));
                }

            }

//        for (Map.Entry<Coordinates, PictureTagLayout> entry : MainActivity.imageAtCoords.entrySet()) {
//
//        }

            for (FullAnnotation fa: removeSetTop) {
                Coordinates c = fa.getCoords();

                PictureTagView view = image.justHasView(c.getX(),c.getY());
                if (view != null) {
                    Coordinates adjustedCoords = new Coordinates(view.getXVal(),view.getYVal());
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

            Log.i("annosinimpl",MainActivity.annotations.toString());

        }

    }

    public void onNewReconciliationFinished(){

    }


}
