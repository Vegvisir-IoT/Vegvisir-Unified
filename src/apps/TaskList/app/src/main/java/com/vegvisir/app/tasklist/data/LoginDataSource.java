package com.vegvisir.app.tasklist.data;

import com.vegvisir.app.tasklist.data.model.LoggedInUser;

import java.io.IOException;
import java.util.HashMap;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private HashMap<String, String> usernames = new HashMap<>();

    public Result<LoggedInUser> login(String username, String password) {
        usernames.put("karan@gmail.com", "123456");

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser user = null;

            if (usernames.containsKey(username)){
                if (usernames.get(username).equals(password)){
                    user =  new LoggedInUser(
                            username,
                            username);
                }
                else{
                    return new Result.Error(new IOException("Incorrect password, please try again", new IOException()));
                }
            }
            else{
                usernames.put(username, password);
                user =
                    new LoggedInUser(
                            username,
                            username);
            }
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
