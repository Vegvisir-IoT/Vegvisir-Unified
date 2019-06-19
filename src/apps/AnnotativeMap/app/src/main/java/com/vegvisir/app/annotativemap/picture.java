package com.vegvisir.app.annotativemap;

/**
 * Created by jiangyi on 09/07/2017.
 */

import com.vegvisir.app.annotativemap.PictureTagView.Status;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import java.util.*;


public class picture extends AppCompatActivity implements View.OnClickListener{
    private Button send = null;
    public int cur_pic_number = 0;
    final String ANNOTATION = "Annotation";
    final String TIME = "CURRENT";
    final String DEL = "Delete";
    String anno = "";
    String cur_time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i("Before click","2.25");
        super.onCreate(savedInstanceState);
//        Log.i("Before click","2.5");
        setContentView(R.layout.pic);
//        Log.i("Before click","3");
        PictureTagLayout image = findViewById(R.id.image);
//        Log.i("Before click","4");
        image.setBackgroundResource(R.drawable.example_map);
//        Log.i("Before click","5");
        image.load(); //read /sdcard/info.txt and init the subviews(annotations)
//        image.write();

        send = findViewById(R.id.send);
        send.setOnClickListener(this);
        Log.i("Before click","2");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                Intent intent = new Intent(picture.this, send.class);
                PictureTagLayout image = findViewById(R.id.image);
                String content = image.message();
                image.write(content);
                intent.putExtra("CONTENT", content);
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent annotation) {
        if (resultCode == 0) { //Done
            anno = annotation.getStringExtra(ANNOTATION);
            cur_time = annotation.getStringExtra(TIME);
            if (!TextUtils.isEmpty(anno)) {
                PictureTagLayout image = findViewById(R.id.image);
                //Log.i("image",image.toString());
//                Log.i("add x", Integer.toString(image.startX));
//                Log.i("add y", Integer.toString(image.startY));
                MainActivity.annotations.put(new Coordinates(image.startX,image.startY),anno);
//                Log.i("after anno",MainActivity.annotations.toString());
                MainActivity.layoutCoords.put(new Coordinates(image.startX,image.startY),image);

                //image.setStatus(Status.Normal,anno);

            }
            super.onActivityResult(requestCode,resultCode,annotation);
        }
        else if (resultCode == 1) { //Del
            String del = annotation.getStringExtra(DEL);
            PictureTagLayout image = findViewById(R.id.image);
            //image.setStatus(Status.Del,del);
//            Log.i("del x", Integer.toString(image.startX));
//            Log.i("del y", Integer.toString(image.startY));

            for(Map.Entry<Coordinates, PictureTagLayout> entry : MainActivity.layoutCoords.entrySet()) {
                Coordinates coords = entry.getKey();
                PictureTagLayout i = entry.getValue();

                if (i.justHasView(image.startX,image.startY)) {
                    MainActivity.annotations.remove(coords);
                    MainActivity.layoutCoords.remove(coords);
                }

            }

            super.onActivityResult(requestCode,resultCode,annotation);
        }
        else if (resultCode == 2) { //Send Done
            super.onActivityResult(requestCode,resultCode,annotation);
        }
    }

    public void init() {
        InputStream is = picture.this.getClass().getClassLoader().
                getResourceAsStream("assets/" + "info.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject info = new JSONObject(stringBuilder.toString());
            JSONObject anno_array = info.getJSONObject("ANNOTATION");
            JSONArray cur_pic_anno_array = anno_array.getJSONArray(cur_pic_number+"");
            if (cur_pic_anno_array.length()==0);
            else {
                for (int i = 0;i < cur_pic_anno_array.length(); i++) {
                    JSONObject temp = cur_pic_anno_array.getJSONObject(i);
                    int x = temp.getInt("x");
                    int y = temp.getInt("y");
                    String a = temp.getString("anno");

                    PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
                    View view = image.addItem(x,y);
                    ((PictureTagView)view).setAnnotation(a);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
