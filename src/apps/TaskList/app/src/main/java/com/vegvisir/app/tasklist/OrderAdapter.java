package com.vegvisir.app.tasklist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vegvisir.pub_sub.TransactionID;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OrderAdapter extends ArrayAdapter<String> {
    private List<String> list;
    private int resource;
    private Context context;

    TextView selected_item,
            low_priority,
            medium_priority,
            high_priority,
            delete_item;

    public OrderAdapter(Context ctxt, int res, List<String> its) {
        super(ctxt, res, its);
        this.list = its;
        this.context = ctxt;
        this.resource = res;

    }


    public View getView(final int position, View convertView, ViewGroup parent){
        Log.i("getView","is called");
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                   this.resource,null,false
            );
        }

        final String item = getItem(position);

        selected_item = (TextView)listItemView.findViewById(R.id.selected_item);
        high_priority = (TextView)listItemView.findViewById(R.id.high_prioirty);
        low_priority = (TextView)listItemView.findViewById(R.id.low_priority);
        medium_priority = (TextView)listItemView.findViewById(R.id.medium_priority);
        delete_item = (TextView)listItemView.findViewById(R.id.delete_item);

        //Set the text of the meal, amount and quantity
        selected_item.setText(item);
        MainActivity.Priority p = MainActivity.priorities.get(item);
        if (p == MainActivity.Priority.Low) {
            selected_item.setTextColor(Color.GREEN);
        }
        else if (p == MainActivity.Priority.Medium){
            selected_item.setTextColor(Color.BLUE);
        }
        else{
            selected_item.setTextColor(Color.RED);
        }

        //OnClick listeners for all the buttons on the ListView Item
        low_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payloadString = "1" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<>();
                topics.add(MainActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
                Iterator<TransactionTuple> it = MainActivity.dependencySets.get(item).iterator();

                while(it.hasNext()){
                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                    dependencies.add(x.transaction);
                }

                if (MainActivity.latestTransactions.containsKey(MainActivity.deviceId)){
                    dependencies.add(MainActivity.latestTransactions.get(MainActivity.deviceId));
                }

                MainActivity.virtual.addTransaction(MainActivity.context, topics, payload, dependencies);
//                clear();
//                addAll(MainActivity.items);
//                notifyDataSetChanged();
            }
        });

        medium_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payloadString = "2" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<>();
                topics.add(MainActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
                Iterator<TransactionTuple> it = MainActivity.dependencySets.get(item).iterator();

                while(it.hasNext()){
                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                    dependencies.add(x.transaction);
                }

                if (MainActivity.latestTransactions.containsKey(MainActivity.deviceId)){
                    dependencies.add(MainActivity.latestTransactions.get(MainActivity.deviceId));
                }

                MainActivity.virtual.addTransaction(MainActivity.context, topics, payload, dependencies);
//                clear();
//                addAll(MainActivity.items);
//                notifyDataSetChanged();
            }
        });

        high_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payloadString = "3" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<>();
                topics.add(MainActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
                Iterator<TransactionTuple> it = MainActivity.dependencySets.get(item).iterator();

                while(it.hasNext()){
                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                    dependencies.add(x.transaction);
                }

                if (MainActivity.latestTransactions.containsKey(MainActivity.deviceId)){
                    dependencies.add(MainActivity.latestTransactions.get(MainActivity.deviceId));
                }

                MainActivity.virtual.addTransaction(MainActivity.context, topics, payload, dependencies);
//                clear();
//                addAll(MainActivity.items);
//                notifyDataSetChanged();
            }
        });

        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payloadString = "0" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<>();
                topics.add(MainActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
                Iterator<TransactionTuple> it = MainActivity.dependencySets.get(item).iterator();

                while(it.hasNext()){
                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
                    dependencies.add(x.transaction);
                }

                if (MainActivity.latestTransactions.containsKey(MainActivity.deviceId)){
                    dependencies.add(MainActivity.latestTransactions.get(MainActivity.deviceId));
                }

                MainActivity.virtual.addTransaction(MainActivity.context, topics, payload, dependencies);
//                clear();
//                addAll(MainActivity.items);
//                notifyDataSetChanged();
            }
        });

        return listItemView;
    }

}
