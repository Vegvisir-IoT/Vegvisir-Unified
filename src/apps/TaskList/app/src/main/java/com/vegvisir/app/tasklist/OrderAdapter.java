package com.vegvisir.app.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vegvisir.app.tasklist.ui.login.LoginActivity;
import com.vegvisir.pub_sub.TransactionID;

import java.util.Arrays;
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
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                   this.resource,null,false
            );
        }

        final String item = getItem(position);

        selected_item    = (TextView)listItemView.findViewById(R.id.selected_item);
        high_priority    = (TextView)listItemView.findViewById(R.id.high_prioirty);
        low_priority     = (TextView)listItemView.findViewById(R.id.low_priority);
        medium_priority  = (TextView)listItemView.findViewById(R.id.medium_priority);
        delete_item      = (TextView)listItemView.findViewById(R.id.delete_item);

        selected_item.setText(item);
        LoginActivity.Priority p = LoginActivity.priorities.get(item);
        selected_item.setTextColor( p.getAssociatedColor( context ));
        listItemView.setBackgroundColor(0);


        //OnClick listeners for all the buttons on the ListView Item
        low_priority.setOnClickListener( e -> setTheTransaction( item, 1, convertView));
        medium_priority.setOnClickListener( e -> setTheTransaction( item, 2, convertView));
        high_priority.setOnClickListener( e -> setTheTransaction( item, 3, convertView));
        delete_item.setOnClickListener( e -> setTheTransaction( item, 0, convertView));

        return listItemView;
    }

    /**
     * Handler for TextView
     * @param item
     * @param myNumber
     */
    public void setTheTransaction (String item, int myNumber, View v ) {
        if (v != null){
            if (myNumber == 1){
                v.setBackgroundColor(this.context.getResources().getColor(R.color.LightGreen));
            }
            else if (myNumber == 2){
                v.setBackgroundColor(this.context.getResources().getColor(R.color.LightSkyBlue));
            }
            else if (myNumber == 3){
                v.setBackgroundColor(this.context.getResources().getColor(R.color.LightPink));
            }
            else{
                v.setBackgroundColor(this.context.getResources().getColor(R.color.LightGrey));
            }

        }

        String payloadString = myNumber + item;
        byte[] payload = payloadString.getBytes();
        Set<String> topics = new HashSet<>(Arrays.asList( LoginActivity.topic));
        Set<TransactionID> dependencies = new HashSet<>();

        dependencies = LoginActivity.MainTopDeps;

        LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
    }

    /**
     * Handles MainActivity Calls to populate with most cu
     * @param aList :: List of Strings
     */
    public void handleActivity(List<String> aList){
        this.clear();
        this.addAll( aList  );
        this.notifyDataSetChanged();
    }

}
