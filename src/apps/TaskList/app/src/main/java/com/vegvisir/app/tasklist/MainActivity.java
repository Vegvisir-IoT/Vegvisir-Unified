package com.vegvisir.app.tasklist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.vegvisir.pub_sub.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.util.Log;
//import android.app.Activity.;


public class MainActivity extends AppCompatActivity {

    private ListView mTaskList;
    private EditText mItemEdit;
    private Button mAddButton;
    public static ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    // mapping from device ID to Transaction ID
    public static HashMap<String, TransactionID> latestTransactions = new HashMap<>();
    // mapping from an item to dependencies
    public static HashMap<String, Set<TransactionTuple>> dependencySets = new HashMap<>();
    private VegvisirApplicationContext context = new VegvisirApplicationContext();
    private VegvisirApplicationDelegatorImpl delegator = new VegvisirApplicationDelegatorImpl();
    private String topic = "Red team";
    private String appID = "123";
    private  String desc = "task list";
    public static String deviceId = "DeviceA";
    private Set<String> channels = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskList = (ListView) findViewById(R.id.task_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);

        context.setAppID(appID);
        context.setDesc(desc);
        channels.add(topic);
        context.setChannels(channels);

        VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();
        virtual.registerApplicationDelegator(context, delegator);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.RED);
                return view;
            }
        };

        mTaskList.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
//                mAdapter.add(item);
//                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
                String payloadString = "1" + item;
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
                try {
                    virtual.addTransaction(context, topics, payload, dependencies);
                } catch (NullPointerException e) {
                    virtual.addTransactionByDeviceAndHeight(deviceId, 0, topics, payload, dependencies);
                }
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

//        mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                mAdapter.remove(mAdapter.getItem(position));
//                // Refresh the adapter
//                mAdapter.notifyDataSetChanged();
//
//            }
//        });

        mTaskList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View viewItem, int pos, long id) {
                        // Remove the item within array at position
                        String item = mAdapter.getItem(pos);
                        String payloadString = "0" + item;
                        byte[] payload = payloadString.getBytes();
                        Set<String> topics = new HashSet<>();
                        topics.add(topic);
                        Set<TransactionID> dependencies = new HashSet<>();

//                        if (dependencySets.containsKey(item)){
                            Iterator<TransactionTuple> it = dependencySets.get(item).iterator();

                            while(it.hasNext()){
                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                                dependencies.add(x.transaction);
                            }
//                        }
//                        Log.i("before addtx","1");
                        virtual.addTransaction(context, topics, payload, dependencies);
//                        Log.i("after addtx","1");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.clear();
                                mAdapter.addAll(items);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                        //mAdapter.remove(mAdapter.getItem(pos));
                        //mAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)

                    }

                });

    }


}
