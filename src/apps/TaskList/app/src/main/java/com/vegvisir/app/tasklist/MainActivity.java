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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private ListView mTaskList;
    private EditText mItemEdit;
    private Button mAddButton;
    public static ArrayAdapter<String> mAdapter;
    // mapping from device ID to Transaction ID
    public static HashMap<String, TransactionID> latestTransactions;
    // mapping from an item to dependencies
    public static HashMap<String, Set<TransactionTuple>> dependencySets;
    private VegvisirApplicationContext context = null;
    private VegvisirApplicationDelegatorImpl delegator = new VegvisirApplicationDelegatorImpl();
    private String topic = "Red team";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskList = (ListView) findViewById(R.id.task_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);

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
                String payloadString = "1" + item ;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = Collections.emptySet();
                topics.add(topic);
                Set<TransactionID> dependencies = Collections.emptySet();

                if (dependencySets.containsKey(item)){
                    Iterator<TransactionTuple> it = dependencySets.get(item).iterator();

                    while(it.hasNext()){
                        TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                        dependencies.add(x.transaction);
                    }
                }
                virtual.addTransaction(context, topics, payload, dependencies);

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

        mTaskList.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        String payloadString = "0" + item ;
                        byte[] payload = payloadString.getBytes();
                        Set<String> topics = Collections.emptySet();
                        topics.add(topic);
                        Set<TransactionID> dependencies = Collections.emptySet();

                        if (dependencySets.containsKey(item)){
                            Iterator<TransactionTuple> it = dependencySets.get(item).iterator();

                            while(it.hasNext()){
                                TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                                dependencies.add(x.transaction);
                            }
                        }
                        virtual.addTransaction(context, topics, payload, dependencies);

                        //mAdapter.remove(mAdapter.getItem(pos));
                        //mAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });

    }


}
