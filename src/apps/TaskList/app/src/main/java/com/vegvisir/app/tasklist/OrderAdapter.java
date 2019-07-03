package com.vegvisir.app.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vegvisir.app.tasklist.ui.login.LoginActivity;
import com.vegvisir.pub_sub.TransactionID;

import java.util.HashSet;
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
//        Log.i("getView","is called");
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
        LoginActivity.Priority p = LoginActivity.priorities.get(item);
        if (p == LoginActivity.Priority.Low) {
            int green = this.context.getResources().getColor(R.color.Green);
            selected_item.setTextColor(green);
        }
        else if (p == LoginActivity.Priority.Medium){
            int blue = this.context.getResources().getColor(R.color.Blue);
            selected_item.setTextColor(blue);
        }
        else{
            int red = this.context.getResources().getColor(R.color.Red);
            selected_item.setTextColor(red);
        }

        //OnClick listeners for all the buttons on the ListView Item
        low_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payloadString = "1" + item;
                byte[] payload = payloadString.getBytes();
                Set<String> topics = new HashSet<>();
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
//                Iterator<TransactionTuple> it = MainActivity.MainDependencySets.get(item).iterator();
//
//                while(it.hasNext()){
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (!x.transaction.getDeviceID().equals(MainActivity.deviceId)){
//                        dependencies.add(x.transaction);
//                    }
//                }

                if (LoginActivity.MainLatestTransactions.containsKey(LoginActivity.deviceId)){
                    dependencies.add(LoginActivity.MainLatestTransactions.get(LoginActivity.deviceId));
                }

                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
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
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
//                Iterator<TransactionTuple> it = MainActivity.MainDependencySets.get(item).iterator();
//
//                while(it.hasNext()){
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (!x.transaction.getDeviceID().equals(MainActivity.deviceId)){
//                        dependencies.add(x.transaction);
//                    }
//                }

                if (LoginActivity.MainLatestTransactions.containsKey(LoginActivity.deviceId)){
                    dependencies.add(LoginActivity.MainLatestTransactions.get(LoginActivity.deviceId));
                }

                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
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
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
//                Iterator<TransactionTuple> it = MainActivity.MainDependencySets.get(item).iterator();
//
//                while(it.hasNext()){
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (!x.transaction.getDeviceID().equals(MainActivity.deviceId)){
//                        dependencies.add(x.transaction);
//                    }
//                }

                if (LoginActivity.MainLatestTransactions.containsKey(LoginActivity.deviceId)){
                    dependencies.add(LoginActivity.MainLatestTransactions.get(LoginActivity.deviceId));
                }

                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
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
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

//
//                Iterator<TransactionTuple> it = MainActivity.MainDependencySets.get(item).iterator();
//
//                while(it.hasNext()){
//                    TransactionTuple x = (TransactionTuple) ((Iterator) it).next();
//                    if (!x.transaction.getDeviceID().equals(MainActivity.deviceId)){
//                        dependencies.add(x.transaction);
//                    }
//                }

                if (LoginActivity.MainLatestTransactions.containsKey(LoginActivity.deviceId)){
                    dependencies.add(LoginActivity.MainLatestTransactions.get(LoginActivity.deviceId));
                }

                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
//                clear();
//                addAll(MainActivity.items);
//                notifyDataSetChanged();
            }
        });

        return listItemView;
    }

}
