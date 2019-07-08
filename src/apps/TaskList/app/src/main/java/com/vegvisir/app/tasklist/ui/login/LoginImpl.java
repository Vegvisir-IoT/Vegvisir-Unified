package com.vegvisir.app.tasklist.ui.login;

import android.util.Log;

import com.vegvisir.app.tasklist.FourPSet;
import com.vegvisir.app.tasklist.data.TransactionTuple;
import com.vegvisir.app.tasklist.User;
import com.vegvisir.app.tasklist.data.TwoPSetUser;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationDelegator;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.util.Arrays;
import java.util.Comparator;

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
        Set<TransactionTuple> updatedSet;
        String deviceId = tx_id.getDeviceID();

        if (transactionType > 4 && transactionType < 8){
            int usernamePos = payloadString.indexOf(",");
            String username = payloadString.substring(1,usernamePos);
            String password = payloadString.substring(usernamePos + 1);

            Set<TransactionTuple> prevSets = LoginActivity.dependencySets.get(username);

            updatedSet = TransactionTuple.createSetFromPrevious(prevSets, deps);

            TransactionTuple t = new TransactionTuple(tx_id, transactionType);
            updatedSet.add(t);
            LoginActivity.dependencySets.put(username, updatedSet);

            LoginActivity.latestTransactions.put(deviceId, tx_id);

            for (TransactionID d : deps) {
                LoginActivity.topDeps.remove(d);
            }
            LoginActivity.topDeps.add(tx_id);

            TwoPSetUser regular = new TwoPSetUser();
            //regular.loadDependencies( deps, LoginActivity.twoPSets);
            regular.attachUser( transactionType, username, password);
            LoginActivity.twoPSets.put(tx_id, regular );

            TwoPSetUser userTop = new TwoPSetUser();
            userTop.loadDependencies( LoginActivity.topDeps, LoginActivity.twoPSets );

            LoginActivity.twoPSets.put(LoginActivity.top, userTop);
            LoginActivity.usernames.clear();
            for (User u: userTop.filterRemove()){
                LoginActivity.usernames.put(u.getUsername(), u.getPassword());
            }
        }
        else {

            String item = payloadString.substring(1);
            if (transactionType > 7){
                int first = payloadString.indexOf(",");
                int second = payloadString.indexOf(",", first + 1);
                int y = Integer.parseInt(payloadString.substring(first+1,second));  //
                item = payloadString.substring(second+1);
                if (transactionType == 8){
                    transactionType = 0;
                }
                else if (transactionType == 9){
                    transactionType = 2;
                }
            }

            Set<TransactionTuple> prevSets = LoginActivity.MainDependencySets.get(item);
            updatedSet = TransactionTuple.createSetFromPrevious(prevSets, deps);

            updatedSet.add( new TransactionTuple( tx_id, transactionType) );
            LoginActivity.MainDependencySets.put(item, updatedSet);


            LoginActivity.MainLatestTransactions.put(deviceId, tx_id);

            for (TransactionID d : deps) {
                LoginActivity.MainTopDeps.remove(d);
            }
            LoginActivity.MainTopDeps.add(tx_id);

            FourPSet reg4PSet = new FourPSet();
            reg4PSet.adjustForDependencies(deps, LoginActivity.fourPSets);
            reg4PSet.updateBySetByType( transactionType, item);

            LoginActivity.fourPSets.put(tx_id, reg4PSet);


            FourPSet top4PSet = new FourPSet();
            top4PSet.adjustForDependencies( LoginActivity.MainTopDeps, LoginActivity.fourPSets );

            LoginActivity.fourPSets.put(LoginActivity.MainTop, top4PSet);

            LoginActivity.items.clear();
            LoginActivity.priorities.clear();

            LoginActivity.updateByPriority( FourPSet.filterSetByList(top4PSet.getLowSet(),
                    Arrays.asList(top4PSet.getMediumSet(), top4PSet.getHighSet(),
                            top4PSet.getRemoveSet())), LoginActivity.Priority.Low);

            LoginActivity.updateByPriority( FourPSet.filterSetByList(top4PSet.getMediumSet(),
                    Arrays.asList( top4PSet.getHighSet(), top4PSet.getRemoveSet())),
                    LoginActivity.Priority.Medium);

            LoginActivity.updateByPriority( FourPSet.filterSetByList(top4PSet.getHighSet(),
                    Arrays.asList( top4PSet.getRemoveSet())), LoginActivity.Priority.High );

            LoginActivity.notWitnessedTransactions.add(tx_id);

            LoginActivity.items.sort(new ItemComparator());

            Log.i("lowset",reg4PSet.getLowSet().toString());
            Log.i("mediumset",reg4PSet.getMediumSet().toString());
            Log.i("highset", reg4PSet.getHighSet().toString());
            Log.i("removeset",reg4PSet.getRemoveSet().toString());

            Log.i("topdeps", LoginActivity.MainTopDeps.toString());

            Log.i("lowsettop",top4PSet.getHighSet().toString());
            Log.i("mediumsettop",top4PSet.getMediumSet().toString());
            Log.i("highsettop",top4PSet.getHighSet().toString());
            Log.i("removesettop",top4PSet.getRemoveSet().toString());

            Log.i("sets", LoginActivity.fourPSets.toString());  
            Log.i("items", LoginActivity.items.toString());


        }

    }

    public void onNewReconciliationFinished(){

    }

    public class ItemComparator implements Comparator<String> {
        @Override
        public int compare(String s1,String s2) {
            LoginActivity.Priority p1 = LoginActivity.priorities.get(s1);
            LoginActivity.Priority p2 = LoginActivity.priorities.get(s2);
            if( p1 == p2)
                return s1.compareTo(s2);
            else
                return p1.compareTo(p2);

        }
    }
}

