package com.vegvisir.app.annotativemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import android.util.Log;

import com.vegvisir.app.annotativemap.PictureTagView.Status;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VirtualVegvisirInstance;

public class MainActivity extends AppCompatActivity {

    private Button editButton = null;
    public static HashMap<Coordinates,Annotation> annotations = new HashMap<>();
    public static HashMap<Coordinates,PictureTagLayout> imageAtCoords = new HashMap<>();
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
    private int counter = 0;
    private String anno;

    public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();
    private Timer timer;

    public static String deviceID2 = "DeviceB";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channels.add(topic);
        context = new VegvisirApplicationContext(appID,desc,channels);

        virtual.registerApplicationDelegator(context,delegator);

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
                            Set<Coordinates> entriesToRemove = new HashSet<>();

//                            Log.i("before",annotations.toString());
                            for(Map.Entry<Coordinates, Annotation> entry : annotations.entrySet()) {

                                Coordinates coords = entry.getKey();
                                Log.i("coords",coords.getX() + "," + coords.getY());
//                                Log.i("shouldRemove",entry.getValue().getShouldRemove().toString());
                                Log.i("alreadyAdded",entry.getValue().getAlreadyAdded().toString());

//                            picture pic = layoutCoords.get(coords);
//                            PictureTagLayout image = pic.findViewById(R.id.image);

                                Annotation annoObj = entry.getValue();
                                PictureTagLayout image = imageAtCoords.get(coords);
//                            PictureTagLayout image = annoObj.getLayout();
                                anno = annoObj.getAnnotation();
                                View v = image.clickView;
                                image.clickView = null;
                                image.removeView(v);
                                if (annoObj.getShouldRemove()) {
//                                    Log.i("Should remove","reached");
                                    entriesToRemove.add(coords);
                                }
                                else {
                                    Log.i("annoobj",annoObj.toString());
                                    if (!annoObj.getAlreadyAdded()){

                                        View view = image.addItem(coords.getX(), coords.getY());
                                        ((PictureTagView) view).setAnnotation(anno);
                                        Log.i("ok","nice");
                                        annoObj.setAlreadyAdded(true);
                                    }

                                }

                            }

                            for (Coordinates coords: entriesToRemove) {
                                annotations.remove(coords);
                                imageAtCoords.remove(coords);
                            }

                            Log.i("annos",annotations.toString());

                            try{
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {}

                            Random rand = new Random();
                            int rand1 = rand.nextInt(500);
                            int rand2 = rand.nextInt(500);

                            String payloadString = "1" + rand1 + "," + rand2 + "," + "abcdef";
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

                            virtual.addTransaction(context, topics, payload, dependencies);

                        }
                    }
                });
            }

        },0,2000);

    }

}


/* public class MainActivity extends AppCompatActivity {

     private Button addButton, editButton, removeButton = null;

    // private static final int READ_REQUEST_CODE = 42;

    // private static final HashMap<String, Uri> fileNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addimg);
        editButton = (Button) findViewById(R.id.editimg);
        removeButton = (Button) findViewById(R.id.remimg);

        addButton.setOnClickListener(new View.OnClickListener() {

            public void performFileSearch() {

                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be " \ * / * " (Without the space in-between).
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }

            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RemoveActivity.class);
                intent.putExtra("filenames",fileNameMap.keySet().toArray());
                Log.i("Pre button click", "");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        Uri uri = null;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData()

            if (resultData != null) {
                uri = resultData.getData();
                fileNameMap.put(uri.toString(),uri);
            }
        }
    }
} */
