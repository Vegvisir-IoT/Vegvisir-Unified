package com.vegvisir.app.annotativemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;

public class RemoveActivity extends AppCompatActivity {

    private ListView fileNameList;
    private EditText fileNameEdit;
    private Button removeButton;
    private ArrayAdapter<String> fileNameAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        fileNameList = (ListView) findViewById(R.id.remove_listView);
        fileNameEdit = (EditText) findViewById(R.id.item_editText);
        removeButton = (Button) findViewById(R.id.remove_button);

        fileNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        fileNameList.setAdapter(fileNameAdapter);

        Intent cur_intent = getIntent();
        final String[] fileNames = cur_intent.getStringArrayExtra("filenames");

        Log.i("File search print", fileNames.toString());

        for (String s: fileNames) {
            fileNameAdapter.add(s);
            fileNameAdapter.notifyDataSetChanged();
            fileNameEdit.setText("");
        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = fileNameEdit.getText().toString();
                fileNameAdapter.remove(item);
                fileNameAdapter.notifyDataSetChanged();
                fileNameEdit.setText("");
            }
        });


    }
}
