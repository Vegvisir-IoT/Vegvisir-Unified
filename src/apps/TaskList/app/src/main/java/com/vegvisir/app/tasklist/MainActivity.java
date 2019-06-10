package com.vegvisir.app.tasklist;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

//import com.vegvisir.app.VirtualVegvisirInstance;

public class MainActivity extends AppCompatActivity {

    private ListView mTaskList;
    private EditText mItemEdit;
    private Button mAddButton;
    private Button mRemoveButton;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskList = (ListView) findViewById(R.id.task_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);
        mRemoveButton = (Button) findViewById(R.id.remove_button);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mTaskList.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                mAdapter.add(item);
                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                mAdapter.remove(item);
                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
            }
        });

    }

//    private VirtualVegvisirInstance loadVirtualVegvisirInstance() {
//        return VirtualVegvisirInstance.getInstance();
//    }

}
