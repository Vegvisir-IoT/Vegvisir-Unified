package com.vegvisir.app.annotativemap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import android.util.Log;

import com.vegvisir.app.annotativemap.ui.login.LoginActivity;
import com.vegvisir.app.annotativemap.ui.login.LoginImpl;
import com.vegvisir.application.VegvisirInstanceV1;
import com.vegvisir.pub_sub.TransactionID;
import com.vegvisir.pub_sub.VegvisirApplicationContext;
import com.vegvisir.pub_sub.VegvisirInstance;

public class MainActivity extends AppCompatActivity {

    private Button editButton = null;
    public static ConcurrentHashMap<Coordinates,Annotation> annotations = new ConcurrentHashMap<>();
    public static HashMap<Coordinates,Set<TransactionTuple>> dependencySets = new HashMap<>();
    public static HashMap<String, TransactionID> latestTransactions = new HashMap<>();
    public static HashMap<TransactionID,TwoPSet> twoPSets = new HashMap<>();
    public static Set<TransactionID> topDeps = new HashSet<>();
    public static TransactionID top = new TransactionID("",-1);
    public static picture currentPicture = null;

//    public static VegvisirApplicationContext context = null;
//    private static LoginImpl delegator = new LoginImpl();
    private String anno;

//    public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();
    public static VegvisirInstance instance = null;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        virtual.registerApplicationDelegator(context,delegator);
        Context androidContext = getApplicationContext();

        instance = VegvisirInstanceV1.getInstance(androidContext);
        instance.registerApplicationDelegator(LoginActivity.context, LoginActivity.delegator);

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
                MainActivity.this.runOnUiThread(() -> {
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



//                            Random rand = new Random();
//                            int rand1 = rand.nextInt(500);
//                            int rand2 = rand.nextInt(500);
//
//                            String payloadString = "1" + 500 + "," + 500 + "," + "abcdef";
//                            byte[] payload = payloadString.getBytes();
//                            count+=1;
//                            Set<String> topics = new HashSet<>();
//                            topics.add(topic);
//                            Set<TransactionID> dependencies = new HashSet<>();
//                            Coordinates c = new Coordinates(500,500);
//                            if (dependencySets.containsKey(c)) {
//                                Iterator<TransactionTuple> it = dependencySets.get(c).iterator();
//                                while(it.hasNext()) {
//                                    TransactionTuple x = (TransactionTuple) ((Iterator)it).next();
//                                    dependencies.add(x.transaction);
//                                }
//                            }
//                            if (latestTransactions.containsKey("DeviceB")) {
//                                dependencies.add(latestTransactions.get("DeviceB"));
//                            }
//
//                            instance.addTransaction(context, topics, payload, dependencies);

                    }
                });
            }

        },0,2000);

    }

}
