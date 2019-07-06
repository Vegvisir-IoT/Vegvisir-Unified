package com.vegvisir.app.annotativemap;

/**
 * Created by jiangyi on 09/07/2017.
 */

import com.vegvisir.app.annotativemap.ui.login.LoginActivity;
import com.vegvisir.pub_sub.TransactionID;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.*;

public class picture extends AppCompatActivity implements View.OnClickListener{
    private Button logout = null;
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
        image.setBackgroundResource(R.drawable.chicago_map);

//        Log.i("Before click","5");
        image.load(); //read /sdcard/info.txt and init the subviews(annotations)
//        image.write();
        LoginActivity.currentPicture = this;
        MainActivity.runningMainActivity = true;

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                Intent intent = new Intent(picture.this, LoginActivity.class);
                startActivity(intent);
//                MainActivity.pause();
                MainActivity.runningMainActivity = false;
                Set<Coordinates> coords = LoginActivity.annotations.keySet();
                for (Coordinates c: coords) {
                    LoginActivity.annotations.get(c).setAlreadyAdded(false);
                }
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent annotation) {
        if (resultCode == 0) { //Done
            try {
                anno = annotation.getStringExtra(ANNOTATION);
            } catch (NullPointerException e) {
                Log.i("anno","not in intent");
            }
            cur_time = annotation.getStringExtra(TIME);
            PictureTagLayout image = findViewById(R.id.image);
            //if (!TextUtils.isEmpty(anno)) {
                //Log.i("image",image.toString());
                Log.i("add x", Integer.toString(image.startX));
                Log.i("add y", Integer.toString(image.startY));
                Coordinates coords;
                PictureTagView view = image.justHasView(image.startX,image.startY);

                if (view != null) {
                    coords = new Coordinates(view.getXVal(),view.getYVal());
                }
                else{
                    coords = new Coordinates(image.startX,image.startY);
                }

                Set<String> topics = new HashSet<>();
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

                if (LoginActivity.mapDependencySets.containsKey(coords)) {
                    Iterator<TransactionID> it = LoginActivity.mapDependencySets.get(coords).iterator();
                    while (it.hasNext()) {
                        TransactionID t = (TransactionID) ((Iterator) it).next();
                        dependencies.add(t);
                    }
                }
//                if (MainActivity.mapLatestTransactions.containsKey(LoginActivity.deviceId)) {
//                    dependencies.add(MainActivity.mapLatestTransactions.get(LoginActivity.deviceId));
//                }

                String payloadString = "9" + coords.getX() + "," + coords.getY() + "," + anno;
                byte[] payload = payloadString.getBytes();
                Log.i("gets","here");
                LoginActivity.instance.addTransaction(LoginActivity.context,topics,payload,dependencies);
                Log.i("gets","after");
//                MainActivity.annotations.put(coords,new Annotation(anno));
//                MainActivity.imageAtCoords.put(coords,image);
                //image.setStatus(Status.Normal,anno);

            //}
            super.onActivityResult(requestCode,resultCode,annotation);
        }
        else if (resultCode == 1) { //Del
            try {
                //String del = annotation.getStringExtra(DEL);
                PictureTagLayout image = findViewById(R.id.image);
                //image.setStatus(Status.Del,del);
                Log.i("del x", Integer.toString(image.startX));
                Log.i("del y", Integer.toString(image.startY));
                PictureTagView view = image.justHasView(image.startX, image.startY);
                Coordinates coords;
                if (view != null) {
                    coords = new Coordinates(view.getXVal(), view.getYVal());
//                    Log.i("view x",""+view.getXVal());
//                    Log.i("view y",""+view.getYVal());
                } else {
                    Log.i("View", "somehow not found");
                    coords = new Coordinates(image.startX, image.startY);
                }
                Set<String> topics = new HashSet<>();
                topics.add(LoginActivity.topic);
                Set<TransactionID> dependencies = new HashSet<>();

                if (LoginActivity.mapDependencySets.containsKey(coords)) {
                    Iterator<TransactionID> it = LoginActivity.mapDependencySets.get(coords).iterator();
                    while (it.hasNext()) {
                        TransactionID t = (TransactionID) ((Iterator) it).next();
                        dependencies.add(t);
                    }
                }
//                if (MainActivity.mapLatestTransactions.containsKey(LoginActivity.deviceId)) {
//                    dependencies.add(MainActivity.mapLatestTransactions.get(LoginActivity.deviceId));
//                }

                if (LoginActivity.annotations.containsKey(coords)) {
                    anno = LoginActivity.annotations.get(coords).getAnnotation();
                } else {
                    Log.i("How did we", "get here");
                }

//            for(Map.Entry<Coordinates, Annotation> entry : MainActivity.annotations.entrySet()) {
//                Coordinates c = entry.getKey();
//                Annotation annoObj = entry.getValue();
//                PictureTagView view = MainActivity.imageAtCoords.get(c);
//
//                if (view.justHasView(image.startX,image.startY)) {
//                    anno = MainActivity.annotations.get(c).getAnnotation();
//                    break;
//                }
//
//            }
                String payloadString = "8" + view.getXVal() + "," + view.getYVal() + "," + anno;
                byte[] payload = payloadString.getBytes();
                LoginActivity.instance.addTransaction(LoginActivity.context, topics, payload, dependencies);
                Log.i("In the try","");
                super.onActivityResult(requestCode, resultCode, annotation);
            } catch (Exception e) {
                Log.i("In the catch","");
            }
        }
        else if (resultCode == 2) { //Send Done
            super.onActivityResult(requestCode,resultCode,annotation);
        }
    }

//    public void init() {
//        InputStream is = picture.this.getClass().getClassLoader().
//                getResourceAsStream("assets/" + "info.json");
//        InputStreamReader streamReader = new InputStreamReader(is);
//        BufferedReader reader = new BufferedReader(streamReader);
//        String line;
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            reader.close();
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            JSONObject info = new JSONObject(stringBuilder.toString());
//            JSONObject anno_array = info.getJSONObject("ANNOTATION");
//            JSONArray cur_pic_anno_array = anno_array.getJSONArray(cur_pic_number+"");
//            if (cur_pic_anno_array.length()==0);
//            else {
//                for (int i = 0;i < cur_pic_anno_array.length(); i++) {
//                    JSONObject temp = cur_pic_anno_array.getJSONObject(i);
//                    int x = temp.getInt("x");
//                    int y = temp.getInt("y");
//                    String a = temp.getString("anno");
//
//                    PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
//                    View view = image.addItem(x,y);
//                    ((PictureTagView)view).setAnnotation(a);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public PictureTagView findView() {
        return (findViewById(R.id.image));
    }

}
