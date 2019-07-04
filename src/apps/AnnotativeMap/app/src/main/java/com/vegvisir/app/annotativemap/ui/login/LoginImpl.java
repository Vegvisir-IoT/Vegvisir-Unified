package com.vegvisir.app.annotativemap.ui.login;

import android.app.Activity;
import android.util.Log;

import com.vegvisir.app.annotativemap.Annotation;
import com.vegvisir.app.annotativemap.Coordinates;
import com.vegvisir.app.annotativemap.FullAnnotation;
import com.vegvisir.app.annotativemap.PictureTagLayout;
import com.vegvisir.app.annotativemap.PictureTagView;
import com.vegvisir.app.annotativemap.R;
import com.vegvisir.app.annotativemap.TwoPSet;
import com.vegvisir.app.annotativemap.User;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Ideally, all applications should implement this interface.
 */
public class LoginImpl implements VegvisirApplicationDelegator {

    private Activity loginActivity;

    public LoginImpl(Activity a) {
        this.loginActivity = a;
    }

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
        Log.i("payload",payloadString);
        int transactionType = Integer.parseInt(payloadString.substring(0,1));
        if (transactionType > 4) {
            int usernamePos = payloadString.indexOf(",");
            String username = payloadString.substring(1,usernamePos);
            String password = payloadString.substring(usernamePos + 1);

            Set<TransactionID> updatedSet = new HashSet<>();
            Set<TransactionID> prevSets = LoginActivity.dependencySets.get(username);
            String deviceId = tx_id.getDeviceID();


            if (prevSets != null) {
                Iterator<TransactionID> itr = prevSets.iterator();
                while (itr.hasNext()) {
                    TransactionID x = (TransactionID) ((Iterator) itr).next();

                    if (!deps.contains(x)) {

                        updatedSet.add(x);
                    }
                }
            }

            updatedSet.add(tx_id);
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
            Log.i("new set", newSet.toString());
            LoginActivity.usernames.clear();
            for (User u: newSet){
                LoginActivity.usernames.put(u.getUsername(), u.getPassword());
            }

//            loginActivity.runOnUiThread(() -> LoginActivity.loginButton.setEnabled(true));
        }

        else{
            Log.i("start","of anno case");
            int first = payloadString.indexOf(",");
            if (first == -1){
                return;
            }
            int x = Integer.parseInt(payloadString.substring(1,first));
            int second = payloadString.indexOf(",", first + 1);
            int y = Integer.parseInt(payloadString.substring(first+1,second));
            String anno = payloadString.substring(second+1);

            Coordinates coords = new Coordinates(x,y);
            Log.i("before","while");
            //while (LoginActivity.currentPicture == null);
            Log.i("after","while");
            PictureTagLayout image = null;
            PictureTagView pointView;
            if (LoginActivity.currentPicture != null) {
                image = LoginActivity.currentPicture.findViewById(R.id.image);
                pointView = image.justHasView(x,y);
                if (pointView != null) {
                    Log.i("View","found");
                    x = pointView.getXVal();
                    y = pointView.getYVal();
                }
                else {
                    Log.i("View","not found");
                }
            }

            Set<TransactionID> updatedSet = new HashSet<>();
            Set<TransactionID> prevSets = LoginActivity.mapDependencySets.get(coords);
            String deviceId = tx_id.getDeviceID();


            if (prevSets != null) {
                Iterator<TransactionID> itr = prevSets.iterator();
                while (itr.hasNext()) {
                    TransactionID t = (TransactionID) ((Iterator) itr).next();

                    if (!deps.contains(t)) {

                        updatedSet.add(t);
                    }
                }
            }

            updatedSet.add(tx_id);
            LoginActivity.mapDependencySets.put(coords, updatedSet);

            LoginActivity.mapLatestTransactions.put(deviceId, tx_id);

            for (TransactionID d : deps) {
                LoginActivity.mapTopDeps.remove(d);
            }
            LoginActivity.mapTopDeps.add(tx_id);
            HashSet<FullAnnotation> addSet = new HashSet<>();
            HashSet<FullAnnotation> removeSet = new HashSet<>();

            for (TransactionID d : deps) {
                if (LoginActivity.mapTwoPSets.containsKey(d)) {
                    addSet.addAll(LoginActivity.mapTwoPSets.get(d).getAddSet());
                    removeSet.addAll(LoginActivity.mapTwoPSets.get(d).getRemoveSet());
                }
            }

            if (transactionType == 1) {
                PictureTagView view = null;
                if (image != null) {
                    view = image.justHasView(x, y);
                }
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

                PictureTagView view = null;
                if (image != null) {
                    view = image.justHasView(x,y);
                }
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
                    removeSet.add(new FullAnnotation(coords,anno));
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

            LoginActivity.mapTwoPSets.put(tx_id, new TwoPSet(addSet, removeSet));

            Log.i("addset",addSet.toString());
            Log.i("remset",removeSet.toString());

            HashSet<FullAnnotation> addSetTop = new HashSet<>();
            HashSet<FullAnnotation> removeSetTop = new HashSet<>();

//        Log.i("txid",tx_id.toString());
//        Log.i("topdeps",MainActivity.mapTopDeps.toString());
//        Log.i("TwoPSets",MainActivity.mapTwoPSets.toString());

            for (TransactionID d : LoginActivity.mapTopDeps) {
//            Log.i("d",d.toString());
                if (LoginActivity.mapTwoPSets.containsKey(d)) {
//                Log.i("gets here","");
                    addSetTop.addAll(LoginActivity.mapTwoPSets.get(d).getAddSet());
                    removeSetTop.addAll(LoginActivity.mapTwoPSets.get(d).getRemoveSet());
                }
            }

            Log.i("addsettop",addSetTop.toString());
            Log.i("remsettop",removeSetTop.toString());

            LoginActivity.mapTwoPSets.put(LoginActivity.mapTop, new TwoPSet(addSetTop, removeSetTop));

//       c

//        Set<Coordinates> newSet = addSetTop;
//        newSet.removeAll(removeSetTop);

            PictureTagView v = null;
            if (image != null) {
                v = image.justHasView(coords.getX(), coords.getY());
            }
            if (v != null) {
                if (LoginActivity.annotations.containsKey(coords)) {
                    Log.i("set","case");
                    LoginActivity.annotations.get(coords).setAnnotation(anno);

                }
                else {
                    LoginActivity.annotations.put(coords,new Annotation(anno));
                }
            }
            else {
                Log.i("View","does not exist");
                LoginActivity.annotations.put(coords,new Annotation(anno));
            }

            for (FullAnnotation fa: addSetTop) {
                Coordinates c = fa.getCoords();
                String annotation = fa.getAnnotation();

                if (!LoginActivity.annotations.containsKey(c)) {
                    Log.i("hashmap","doesn't contain anno");
                    LoginActivity.annotations.put(c, new Annotation(annotation));
                }

            }

//        for (Map.Entry<Coordinates, PictureTagLayout> entry : MainActivity.imageAtCoords.entrySet()) {
//
//        }

            for (FullAnnotation fa: removeSetTop) {
                Coordinates c = fa.getCoords();
                if (LoginActivity.annotations.containsKey(c)) {
                    Log.i("removefound", LoginActivity.annotations.get(c).getAnnotation());
                    LoginActivity.annotations.get(c).setShouldRemove(true);
                }
            }

            HashSet<Coordinates> entriesToRemove = new HashSet<>();

            for(Map.Entry<Coordinates, Annotation> entry : LoginActivity.annotations.entrySet()) {
                Coordinates c = entry.getKey();
                Annotation a = entry.getValue();
                FullAnnotation annoFa = new FullAnnotation(c,a.getAnnotation());
                if (!addSetTop.contains(annoFa)) {
                    entriesToRemove.add(c);
                }
            }

            for (Coordinates c: entriesToRemove) {
                LoginActivity.annotations.get(c).setShouldRemove(true);
            }

            Log.i("annosinimpl", LoginActivity.annotations.toString());

        }

    }

    public void onNewReconciliationFinished(){

    }


}
