package com.vegvisir.app.tasklist;
//123456 password

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.vegvisir.app.tasklist.ui.login.LoginActivity;
import com.vegvisir.pub_sub.TransactionID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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

        mAdapter = new OrderAdapter(this, R.layout.swipe, new ArrayList<String>());

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
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.clear();
                        mAdapter.addAll(LoginActivity.items);
                        mAdapter.notifyDataSetChanged();
                        //Log.i("From refresh",MainActivity.items.toString());

//                        String payloadString2 = "0" + "a";
//                        byte[] payload2 = payloadString2.getBytes();
//                        Set<String> topics2 = new HashSet<String>();
//                        topics2.add(topic);
//                        Set<TransactionID> dependencies2 = new HashSet<>();
//
//                        if (MainDependencySets.containsKey("a")) {
//                            Iterator<TransactionTuple> it = MainDependencySets.get("a").iterator();
//                            while (it.hasNext()) {
//                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                                dependencies2.add(x.transaction);
//                            }
//                        }
//                        if (MainLatestTransactions.containsKey("DeviceB")){
//                            dependencies2.add(MainLatestTransactions.get("DeviceB"));
//                        }
//
//                        virtual.addTransaction(context, topics2, payload2, dependencies2);
//
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        String payloadString = "1" + "a";
//                        byte[] payload = payloadString.getBytes();
//                        Set<String> topics = new HashSet<String>();
//                        topics.add(topic);
//                        Set<TransactionID> dependencies = new HashSet<>();
//
//                        Log.i("depSets",MainDependencySets.toString());
//                        if (MainDependencySets.containsKey("a")) {
//                            Log.i("Right","Place");
//                            Iterator<TransactionTuple> it = MainDependencySets.get("a").iterator();
//                            while (it.hasNext()) {
//                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                                dependencies.add(x.transaction);
//                            }
//                        }
//                        if (MainLatestTransactions.containsKey("DeviceB")){
//                            dependencies.add(MainLatestTransactions.get("DeviceB"));
//                        }
//
//                        virtual.addTransaction(context, topics, payload, dependencies);

//                        mAdapter.clear();
//                        mAdapter.addAll(items);
//                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

        },0,2000);



        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString().trim();
//                mAdapter.add(item);
//                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
                String payloadString = "2" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<String>();
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//                if (MainDependencySets.containsKey(item)) {
//                    Iterator<TransactionTuple> it = MainDependencySets.get(item).iterator();
//                    while (it.hasNext()) {
//                        TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                        dependencies.add(x.transaction);
//                    }
//                }
                if (LoginActivity.MainLatestTransactions.containsKey(LoginActivity.deviceId)){
                    dependencies.add(LoginActivity.MainLatestTransactions.get(LoginActivity.deviceId));
                }


                //virtual.addTransaction(context, topics, payload, dependencies);
                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.clear();
                        mAdapter.addAll(LoginActivity.items);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


//        mSwitchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this,MainActivity2.class);
//                startActivity(i);
//            }
//
//            });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });

    }




}
