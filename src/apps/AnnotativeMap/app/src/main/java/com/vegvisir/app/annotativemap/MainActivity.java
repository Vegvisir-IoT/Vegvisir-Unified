package com.vegvisir.app.annotativemap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.app.Activity;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import android.util.Log;

import com.vegvisir.app.annotativemap.PictureTagView.Status;
import com.vegvisir.application.VegvisirInstanceV1;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirInstance;
import com.vegvisir.pub_sub.VirtualVegvisirInstance;

public class MainActivity extends AppCompatActivity {

    private Button editButton = null;
    public static ConcurrentHashMap<Coordinates,Annotation> annotations = new ConcurrentHashMap<>();
    public static HashMap<Coordinates,Set<TransactionTuple>> dependencySets = new HashMap<>();
    public static HashMap<String, TransactionID> latestTransactions = new HashMap<>();
    public static HashMap<TransactionID,TwoPSet> twoPSets = new HashMap<>();
    public static Set<TransactionID> topDeps = new HashSet<>();
    public static TransactionID top = new TransactionID("",-1);
    public static picture currentPicture = null;

    public static String deviceId = "deviceA";
    public static VegvisirApplicationContext context = null;
    private static VegvisirApplicationDelegatorImpl delegator = new VegvisirApplicationDelegatorImpl();
    public static String topic = "Map";
    private static String appID = "456";
    private static String desc = "Annotated map";
    private static Set<String> channels = new HashSet<>();
    private String anno;

//    public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();
    public static VegvisirInstance instance = null;
    private Timer timer;

    public static String deviceID2 = "DeviceB";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channels.add(topic);
        context = new VegvisirApplicationContext(appID,desc,channels);

//        virtual.registerApplicationDelegator(context,delegator);
        Context androidContext = getApplicationContext();

        instance = VegvisirInstanceV1.getInstance(androidContext);
        instance.registerApplicationDelegator(context, delegator);
        this.deviceId = instance.getThisDeviceID();

        editButton = findViewById(R.id.editimg);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, picture.class);
//                Log.i("Before starting","1");
                startActivity(intent);
            }
        });

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(currentPicture != null) {
                            Log.i("annosatstart",annotations.toString());
                            Set<Coordinates> entriesToRemove = new HashSet<>();

//                            Log.i("before",annotations.toString());
                            for(Map.Entry<Coordinates, Annotation> entry : annotations.entrySet()) {

                                Coordinates coords = entry.getKey();
                                Log.i("coords",coords.getX() + "," + coords.getY());
//                                Log.i("shouldRemove",entry.getValue().getShouldRemove().toString());
                                Log.i("alreadyAdded",entry.getValue().getAlreadyAdded().toString());

                                Annotation annoObj = entry.getValue();
//                            PictureTagLayout image = annoObj.getLayout();
                                anno = annoObj.getAnnotation();
                                PictureTagLayout image = currentPicture.findViewById(R.id.image);


                                if (annoObj.getShouldRemove()) {
                                    Log.i("Should remove","reached");
                                    entriesToRemove.add(coords);
                                    View v = image.clickView;
                                    image.removeView(v);
                                    image.clickView = null;

                                }
                                else {
                                    Log.i("annoobj",annoObj.toString());
                                    if (!annoObj.getAlreadyAdded()){
//
                                        PictureTagView view = image.justHasView(coords.getX(),coords.getY());
                                        if (view == null) {
                                            view = (PictureTagView) image.addItem(coords.getX(), coords.getY());
                                        }
                                            view.setAnnotation(anno);
                                            Log.i("ok","nice");
                                            annoObj.setAlreadyAdded(true);
                                    }
                                    else {
                                        PictureTagView view = image.justHasView(coords.getX(),coords.getY());
                                        if (view != null) {
                                            view.setAnnotation(anno);
                                        }
                                    }
                                }

                            }

                            for (Coordinates coords: entriesToRemove) {
                                annotations.remove(coords);
                            }

                            Log.i("annos",annotations.toString());



                            Random rand = new Random();
                            int rand1 = rand.nextInt(500);
                            int rand2 = rand.nextInt(500);

                            String payloadString = "1" + 500 + "," + 500 + "," + "abcdef";
                            byte[] payload = payloadString.getBytes();
                            count+=1;
                            Set<String> topics = new HashSet<>();
                            topics.add(topic);
                            Set<TransactionID> dependencies = new HashSet<>();
                            Coordinates c = new Coordinates(500,500);
                            if (dependencySets.containsKey(c)) {
                                Iterator<TransactionTuple> it = dependencySets.get(c).iterator();
                                while(it.hasNext()) {
                                    TransactionTuple x = (TransactionTuple) ((Iterator)it).next();
                                    dependencies.add(x.transaction);
                                }
                            }
                            if (latestTransactions.containsKey("DeviceB")) {
                                dependencies.add(latestTransactions.get("DeviceB"));
                            }

                            instance.addTransaction(context, topics, payload, dependencies);

                        }
                    }
                });
            }

        },0,2000);

    }

}
