package com.vegvisir.app.tasklist.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vegvisir.app.tasklist.FourPSet;
import com.vegvisir.app.tasklist.MainActivity;
import com.vegvisir.app.tasklist.R;
import com.vegvisir.app.tasklist.data.TransactionTuple;
import com.vegvisir.app.tasklist.data.TwoPSetUser;
import com.vegvisir.application.VegvisirInstanceV1;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

public class LoginActivity extends AppCompatActivity {

    //public static String deviceId = "";
    // mapping from device ID to Transaction ID
    public static HashMap<String, TransactionID> MainLatestTransactions = new HashMap<>();
    public static HashMap<String, Set<TransactionTuple>> MainDependencySets = new HashMap<>();
    public static HashMap<TransactionID, FourPSet> fourPSets = new HashMap<>();
    public static Set<TransactionID> witnessedTransactions = new HashSet<TransactionID>();
    public static Set<TransactionID> notWitnessedTransactions = new HashSet<TransactionID>();
    public static Set<TransactionID> MainTopDeps = new HashSet<>();
    public static TransactionID MainTop = new TransactionID("", -1);
    public static ArrayList<String> items = new ArrayList<>();
    public static HashMap<String, Priority> priorities = new HashMap<>();
    private LoginViewModel loginViewModel;
    public static String deviceId = "";
    // mapping from device ID to Transaction ID
    public static HashMap<String, TransactionID> latestTransactions = new HashMap<>();
    // mapping from an item to dependencies
    public static HashMap<String, Set<TransactionTuple>> dependencySets = new HashMap<>();
    //mapping from transaction ID to its 2P set
    public static HashMap<TransactionID, TwoPSetUser> twoPSets = new HashMap<>();
    public static HashMap<String, String> usernames = new HashMap<>();
    public static Set<TransactionID> topDeps = new HashSet<>();
    public static TransactionID top = new TransactionID("", -1);
    public static VegvisirApplicationContext context = null;
    public static LoginImpl delegator = new LoginImpl();
    public static String topic = "Blue team";
    private String appID = "123";
    private  String desc = "task list";
    private Set<String> channels = new HashSet<String>();
    private Timer timer;
    public static VegvisirInstance instance = null;
    public static Context androidContext;

    //public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        //Get permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        }

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        channels.add(topic);
        context = new VegvisirApplicationContext(appID, desc, channels);
        androidContext = getApplicationContext();

        instance = VegvisirInstanceV1.getInstance(androidContext);
        instance.registerApplicationDelegator(context, delegator);
        this.deviceId = instance.getThisDeviceID();

        //virtual.registerApplicationDelegator(context, delegator);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        loginViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                    finish();
//                    startActivity(getIntent());
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    String displayString = loginViewModel.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    Toast.makeText(getApplicationContext(),displayString,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                    finish();
//                    startActivity(getIntent());

                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }



    public enum Priority{
        High(3), Medium(2), Low(1);
        private final int value;
        Priority(int val){
            value = val;
        }

        public int getValue() {
            return value;
        }

        public static int priorityComparator( Priority p1, Priority p2) {
            if (p1.equals(p2))
                return p1.getValue() - p2.getValue();
            else
                return p1.compareTo(p2);
        }


        /**
         * @dependency :: Context must be initiated prior to calling
         * @return Integer representation of Priority Color
         */
        public int getAssociatedColor() {

            if (this == High)
                return ContextCompat.getColor(androidContext, R.color.Red);
            else if (this == Medium)
                return ContextCompat.getColor(androidContext, R.color.Blue);
            else
                return ContextCompat.getColor(androidContext, R.color.Green);

            }

    }

}
