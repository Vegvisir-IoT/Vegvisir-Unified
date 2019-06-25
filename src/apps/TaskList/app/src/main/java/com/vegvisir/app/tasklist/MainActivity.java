package com.vegvisir.app.tasklist;
//123456 password

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vegvisir.app.tasklist.ui.login.LoginActivity;
import com.vegvisir.application.VegvisirInstanceV1;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirInstance;
import com.vegvisir.pub_sub.VirtualVegvisirInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ListView mTaskList;
    private EditText mItemEdit;
    private Button mAddButton;
    private Button mSwitchButton;
    private Button mLogoutButton;
    public static ArrayList<String> items = new ArrayList<>();
    public enum Priority{
        High, Medium, Low;
    }
    public static HashMap<String,Priority > priorities = new HashMap<>();
    //private ArrayAdapter<String> mAdapter;
    public OrderAdapter mAdapter;
    public static String deviceId = "";
    // mapping from device ID to Transaction ID
    public static HashMap<String, TransactionID> latestTransactions = new HashMap<>();
    // mapping from an item to dependencies
    public static HashMap<String, Set<TransactionTuple>> dependencySets = new HashMap<>();
    //mapping from transaction ID to its 2P set
    public static HashMap<TransactionID, FourPSet> fourPSets = new HashMap<>();
    public static Set<TransactionID> topDeps = new HashSet<>();
    public static TransactionID top = new TransactionID("", -1);
    public static VegvisirApplicationContext context = null;
    private VegvisirApplicationDelegatorImpl delegator = new VegvisirApplicationDelegatorImpl();
    public static String topic = "Red team";
    private String appID = "123";
    private  String desc = "task list";
    private Set<String> channels = new HashSet<String>();
    private Timer timer;
    private static VegvisirInstance instance = null;

    public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Get Permission */
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
        } else {
        }

        mAdapter = new OrderAdapter(this, R.layout.swipe, new ArrayList<String>());

        mTaskList = (ListView) findViewById(R.id.task_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);
        mSwitchButton = findViewById(R.id.switch_button);
        mLogoutButton = findViewById(R.id.logout_button);

        channels.add(topic);
        context = new VegvisirApplicationContext(appID, desc, channels);
        Context androidContext = getApplicationContext();

        instance = VegvisirInstanceV1.getInstance(androidContext);
        instance.registerApplicationDelegator(context, delegator);
//        this.deviceId = instance.getThisDeviceID();
        this.deviceId = virtual.getThisDeviceID();
        virtual.registerApplicationDelegator(context, delegator);

//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = (TextView) view.findViewById(android.R.id.text1);
//                text.setTextColor(Color.RED);
//                return view;
//            }
//        };

        mTaskList.setAdapter(mAdapter);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.clear();
                        mAdapter.addAll(items);
                        mAdapter.notifyDataSetChanged();
                        Log.i("items main", items.toString());
                        //Log.i("From refresh",MainActivity.items.toString());

//                        String payloadString2 = "0" + "a";
//                        byte[] payload2 = payloadString2.getBytes();
//                        Set<String> topics2 = new HashSet<String>();
//                        topics2.add(topic);
//                        Set<TransactionID> dependencies2 = new HashSet<>();
//
//                        if (dependencySets.containsKey("a")) {
//                            Iterator<TransactionTuple> it = dependencySets.get("a").iterator();
//                            while (it.hasNext()) {
//                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                                dependencies2.add(x.transaction);
//                            }
//                        }
//                        if (latestTransactions.containsKey("DeviceB")){
//                            dependencies2.add(latestTransactions.get("DeviceB"));
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
//                        Log.i("depSets",dependencySets.toString());
//                        if (dependencySets.containsKey("a")) {
//                            Log.i("Right","Place");
//                            Iterator<TransactionTuple> it = dependencySets.get("a").iterator();
//                            while (it.hasNext()) {
//                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                                dependencies.add(x.transaction);
//                            }
//                        }
//                        if (latestTransactions.containsKey("DeviceB")){
//                            dependencies.add(latestTransactions.get("DeviceB"));
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
                String item = mItemEdit.getText().toString();
//                mAdapter.add(item);
//                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
                String payloadString = "2" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<String>();
                topics.add(topic);
                Set<TransactionID> dependencies = new HashSet<>();

                if (dependencySets.containsKey(item)) {
                    Iterator<TransactionTuple> it = dependencySets.get(item).iterator();
                    while (it.hasNext()) {
                        TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                        dependencies.add(x.transaction);
                    }
                }
                if (latestTransactions.containsKey(deviceId)){
                    dependencies.add(latestTransactions.get(deviceId));
                }

                virtual.addTransaction(context, topics, payload, dependencies);
                //instance.addTransaction(context, topics, payload, dependencies);


                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.clear();
                        mAdapter.addAll(items);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


//        mTaskList.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapter,
//                                                   View viewItem, int pos, long id) {
//                        // Remove the item within array at position
//                        String item = mAdapter.getItem(pos);
////                        mAdapter.remove(item);
////                        mAdapter.notifyDataSetChanged();
//                        String payloadString = "0" + item;
//                        byte[] payload = payloadString.getBytes();
//                        Set<String> topics = new HashSet<>();
//                        topics.add(topic);
//                        Set<TransactionID> dependencies = new HashSet<>();
//
////
//                        Iterator<TransactionTuple> it = dependencySets.get(item).iterator();
//
//                        while(it.hasNext()){
//                            TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                            dependencies.add(x.transaction);
//                        }
//
//                        if (latestTransactions.containsKey(deviceId)){
//                            dependencies.add(latestTransactions.get(deviceId));
//                        }
//
//                        virtual.addTransaction(context, topics, payload, dependencies);
//                        //instance.addTransaction(context, topics, payload, dependencies);
//
//
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Log.i("From main remove",MainActivity.items.toString());
//                                mAdapter.clear();
//                                mAdapter.addAll(items);
//                                mAdapter.notifyDataSetChanged();
//
//                            }
//                        });
//
//                    }
//
//                });

        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(i);
            }

            });

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
