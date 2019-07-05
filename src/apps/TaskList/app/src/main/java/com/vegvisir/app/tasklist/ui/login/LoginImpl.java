package com.vegvisir.app.tasklist.ui.login;

import android.util.Log;

import com.vegvisir.app.tasklist.FourPSet;
import com.vegvisir.app.tasklist.data.TransactionTuple;
import com.vegvisir.app.tasklist.User;
import com.vegvisir.app.tasklist.data.TwoPSetUser;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.Comparator;
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
        Log.i("loginimpl",payloadString);
        int transactionType = Integer.parseInt(payloadString.substring(0,1));

        if (transactionType > 4){
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
            String item = payloadString.substring(1);

            Set<TransactionTuple> updatedSet = new HashSet<>();
            Set<TransactionTuple> prevSets = LoginActivity.MainDependencySets.get(item);
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
            LoginActivity.MainDependencySets.put(item, updatedSet);

            LoginActivity.MainLatestTransactions.put(deviceId, tx_id);

            for (TransactionID d : deps) {
                LoginActivity.MainTopDeps.remove(d);
            }
            LoginActivity.MainTopDeps.add(tx_id);
            HashSet<String> lowSet = new HashSet<>();
            HashSet<String> mediumSet = new HashSet<>();
            HashSet<String> highSet = new HashSet<>();
            HashSet<String> removeSet = new HashSet<>();

            for (TransactionID d : deps) {
                if (LoginActivity.fourPSets.containsKey(d)) {
                    lowSet.addAll(LoginActivity.fourPSets.get(d).getLowSet());
                    mediumSet.addAll(LoginActivity.fourPSets.get(d).getMediumSet());
                    highSet.addAll(LoginActivity.fourPSets.get(d).getHighSet());
                    removeSet.addAll(LoginActivity.fourPSets.get(d).getRemoveSet());
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

            LoginActivity.fourPSets.put(tx_id, new FourPSet(lowSet, mediumSet, highSet, removeSet));

            HashSet<String> lowSetTop = new HashSet<>();
            HashSet<String> mediumSetTop = new HashSet<>();
            HashSet<String> highSetTop = new HashSet<>();
            HashSet<String> removeSetTop = new HashSet<>();

            for (TransactionID d : LoginActivity.MainTopDeps) {
                if (LoginActivity.fourPSets.containsKey(d)) {
                    lowSetTop.addAll(LoginActivity.fourPSets.get(d).getLowSet());
                    mediumSetTop.addAll(LoginActivity.fourPSets.get(d).getMediumSet());
                    highSetTop.addAll(LoginActivity.fourPSets.get(d).getHighSet());
                    removeSetTop.addAll(LoginActivity.fourPSets.get(d).getRemoveSet());
                }
            }

            LoginActivity.fourPSets.put(LoginActivity.MainTop, new FourPSet(lowSetTop, mediumSetTop, highSetTop, removeSetTop));

            LoginActivity.items.clear();
            LoginActivity.priorities.clear();

            Set<String> newLowSet = lowSetTop;
            newLowSet.removeAll(mediumSetTop);
            newLowSet.removeAll(highSetTop);
            newLowSet.removeAll(removeSetTop);

            for(String lowItem: newLowSet) {
                LoginActivity.items.add(lowItem);
                LoginActivity.priorities.put(lowItem, LoginActivity.Priority.Low);
            }

            Set<String> newMediumSet = mediumSetTop;
            newMediumSet.removeAll(highSetTop);
            newMediumSet.removeAll(removeSetTop);

            for(String mediumItem: newMediumSet) {
                LoginActivity.items.add(mediumItem);
                LoginActivity.priorities.put(mediumItem, LoginActivity.Priority.Medium);
            }

            Set<String> newHighSet = highSetTop;
            newHighSet.removeAll(removeSetTop);

            for(String highItem: newHighSet) {
                LoginActivity.items.add(highItem);
                LoginActivity.priorities.put(highItem, LoginActivity.Priority.High);
            }

            LoginActivity.notWitnessedTransactions.add(tx_id);


            LoginActivity.items.sort(new ItemComparator());

            Log.i("lowset",lowSet.toString());
            Log.i("mediumset",mediumSet.toString());
            Log.i("highset",highSet.toString());
            Log.i("removeset",removeSet.toString());

            Log.i("topdeps", LoginActivity.MainTopDeps.toString());

            Log.i("lowsettop",lowSetTop.toString());
            Log.i("mediumsettop",mediumSetTop.toString());
            Log.i("highsettop",highSetTop.toString());
            Log.i("removesettop",removeSetTop.toString());

            Log.i("sets", LoginActivity.fourPSets.toString());
//
            Log.i("items", LoginActivity.items.toString());

        }

    }

    public void onNewReconciliationFinished(){

    }

    // TODO: FIX
    // if p1 == p2
    // compare s1.s2
    // else p1.compareTo(p2)
    public class ItemComparator implements Comparator<String> {
        @Override
        public int compare(String s1,String s2) {
            LoginActivity.Priority p1 = LoginActivity.priorities.get(s1);
            LoginActivity.Priority p2 = LoginActivity.priorities.get(s2);
            if( p1 == p2)
                return s1.compareTo(s2);
            else
                return p1.compareTo(p2);
           /* if (p1 == LoginActivity.Priority.High) {
                if (p2 == LoginActivity.Priority.High) {
                    return s1.compareTo(s2);
                }
                else {
                    return -1;
                }
            }
            else if (p1 == LoginActivity.Priority.Medium) {
                if (p2 == LoginActivity.Priority.High) {
                    return 1;
                }
                else if (p2 == LoginActivity.Priority.Medium) {
                    return s1.compareTo(s2);
                }
                else {
                    return -1;
                }
            }
            else {
                if (p2 == LoginActivity.Priority.Low) {
                    return s1.compareTo(s2);
                }
                else {
                    return 1;
                } */
        }
    }
}

