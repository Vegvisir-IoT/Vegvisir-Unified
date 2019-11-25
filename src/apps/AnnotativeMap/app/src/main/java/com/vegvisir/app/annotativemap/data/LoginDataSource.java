package com.vegvisir.app.annotativemap.data;

import android.util.Log;

import com.vegvisir.app.annotativemap.data.model.LoggedInUser;
import com.vegvisir.app.annotativemap.ui.login.LoginActivity;
import com.vegvisir.pub_sub.TransactionID;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    //private HashMap<String, String> usernames = new HashMap<String, String>(){{put("karan@gmail.com", "123456");}};

    public Result<LoggedInUser> login(String username, String password) throws IOException {


        // TODO: handle loggedInUser authentication
        LoggedInUser user = null;
        Log.i("usernames",LoginActivity.usernames.toString());
        if (LoginActivity.usernames.containsKey(username)){
            String hash = Integer.toString((username + password).hashCode());

            if (LoginActivity.usernames.get(username).equals(hash)){
                user =  new LoggedInUser(
                        username,
                        username);
            }
            else{
                throw new IOException("Incorrect password, please try again");
            }
        }
        else{
            throw new IOException("Incorrect username, please try again");
        }
        return new Result.Success<>(user);
    }

    public Result<LoggedInUser> register(String username, String password) throws IOException {

        // TODO: handle loggedInUser authentication
        LoggedInUser user = null;

        if (LoginActivity.usernames.containsKey(username)){
            throw new IOException("This username already exists");
        }
        else{
            Log.i("Usernames","doesn't have this");
//            LoginActivity.loginButton.setEnabled(false);
            //usernames.put(username, password);
            int hash = (username + password).hashCode();
            String payloadString = "5" +  username + "," + hash;
            byte[] payload = payloadString.getBytes();
            Set<String> topics = new HashSet<String>();
            topics.add(LoginActivity.topic);
            Set<TransactionID> dependencies = new HashSet<>();

            if (LoginActivity.dependencySets.containsKey(username)) {
                Iterator<TransactionID> it = LoginActivity.dependencySets.get(username).iterator();
                while (it.hasNext()) {
                    TransactionID x = (TransactionID) ((Iterator) it).next();
                    dependencies.add(x);
                }
            }
            if (LoginActivity.latestTransactions.containsKey(LoginActivity.deviceId)){
                dependencies.add(LoginActivity.latestTransactions.get(LoginActivity.deviceId));
            }
            Log.i("before","add");
            LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
            Log.i("after","add");
            //instance.addTransaction(context, topics, payload, dependencies);
            return new Result.Success<>(user);
        }
    }

    public void logout() {
        // TODO: revoke authentication

    }
}