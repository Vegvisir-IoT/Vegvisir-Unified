package com.vegvisir.app.tasklist.ui.login;

import com.vegvisir.app.tasklist.TransactionTuple;
import com.vegvisir.app.tasklist.User;
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

        if (transactionType == 1) {
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


}