package com.vegvisir.app.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.vegvisir.app.tasklist.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Main Screen Used by TaskList App
 */
public class MainActivity extends AppCompatActivity {

    private ListView mTaskList;
    private EditText mItemEdit;
    private Button mAddButton;
//    private Button mSwitchButton;
    private Button mLogoutButton;

    public OrderAdapter mAdapter;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new OrderAdapter(this, R.layout.swipe, new ArrayList<>());

        mTaskList = (ListView) findViewById(R.id.task_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);
//        mSwitchButton = findViewById(R.id.switch_button);
        mLogoutButton = findViewById(R.id.logout_button);

        mTaskList.setAdapter(mAdapter);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> mAdapter.handleActivity( LoginActivity.items));
            }
        },0,2000);


        /*#########################
         * Add Button Logic      3
         ######################*/
        mAddButton.setOnClickListener(e -> {
            String item = mItemEdit.getText().toString().trim();

            mItemEdit.setText("");
            mAdapter.setTheTransaction(item, 2); //Initially All items are medium priority

            MainActivity.this.runOnUiThread(() -> mAdapter.handleActivity(LoginActivity.items));
        });

        /*#########################
         * Logout Button Logic   3
         ######################*/
        mLogoutButton.setOnClickListener( e -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }       // End of method onCreate()
}