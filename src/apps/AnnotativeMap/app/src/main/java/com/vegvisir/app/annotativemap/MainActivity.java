package com.vegvisir.app.annotativemap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.*;

import android.util.Log;

import com.vegvisir.app.annotativemap.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button editButton = null;
    private static TimerTask task;
    public static boolean runningMainActivity;
    public static boolean printedOnce = false;

//    public static VegvisirApplicationContext context = null;
//    private static LoginImpl delegator = new LoginImpl();
    private String anno;

//    public static VirtualVegvisirInstance virtual = VirtualVegvisirInstance.getInstance();
//    public static VegvisirInstance instance = null;
    private static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("comes","here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        virtual.registerApplicationDelegator(context,delegator);
        Context androidContext = getApplicationContext();

//        instance = VegvisirInstanceV1.getInstance(androidContext);
//        instance.registerApplicationDelegator(LoginActivity.context, LoginActivity.delegator);

        editButton = findViewById(R.id.editimg);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, picture.class);
//                Log.i("Before starting","1");
                startActivity(intent);
            }
        });
        AnnotativeMapApplication thisApp = (AnnotativeMapApplication) this.getApplication();

        task = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(() -> {
//                    Log.i("timer","running");
                    if (LoginActivity.currentPicture != null && runningMainActivity) {
                        if (!printedOnce) {
                            Log.i("annosatstart", thisApp.getAnnotations().toString());
                        }
                        Set<Coordinates> entriesToRemove = new HashSet<>();

//                            Log.i("before",annotations.toString());
                        for (Map.Entry<Coordinates, Annotation> entry : thisApp.getAnnotations().entrySet()) {

                            Coordinates coords = entry.getKey();
                            if (!printedOnce) {
                                Log.i("coords", coords.getX() + "," + coords.getY());
//                                Log.i("shouldRemove",entry.getValue().getShouldRemove().toString());
                                Log.i("alreadyAdded", entry.getValue().getAlreadyAdded().toString());
                            }
                            Annotation annoObj = entry.getValue();
//                            PictureTagLayout image = annoObj.getLayout();
                            anno = annoObj.getAnnotation();
                            PictureTagLayout image = LoginActivity.currentPicture.findViewById(R.id.image);


                            if (annoObj.getShouldRemove()) {
//                                Log.i("Should remove", "reached");
                                entriesToRemove.add(coords);
                                View v = image.justHasView(coords.getX(),coords.getY());
                                if (v != null) {
                                    image.removeView(v);
                                }
                                else {
                                    Log.i("view","for remove not found");
                                }

                            } else {
                                if (!printedOnce) {
                                    Log.i("annoobj", annoObj.toString());
                                }
                                if (!annoObj.getAlreadyAdded()) {
//
                                    PictureTagView view = image.justHasView(coords.getX(), coords.getY());
                                    if (view == null) {
                                        view = (PictureTagView) image.addItem(coords.getX(), coords.getY());
                                    }

                                    view.setAnnotation(anno);

//                                    Log.i("ok", "nice");
                                    annoObj.setAlreadyAdded(true);
                                } else {
                                    PictureTagView view = image.justHasView(coords.getX(), coords.getY());
                                    if (view != null) {
//                                        Log.i("edit","case");
                                        view.setAnnotation(anno);
                                    }
                                }
                            }

                        }

                        for (Coordinates coords : entriesToRemove) {
                            thisApp.getAnnotations().remove(coords);
                        }
                        if (!printedOnce) {
                            Log.i("annos", thisApp.getAnnotations().toString());
                        }
                        printedOnce = true;

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
//                            if (mapDependencySets.containsKey(c)) {
//                                Iterator<TransactionTuple> it = mapDependencySets.get(c).iterator();
//                                while(it.hasNext()) {
//                                    TransactionTuple x = (TransactionTuple) ((Iterator)it).next();
//                                    dependencies.add(x.transaction);
//                                }
//                            }
//                            if (mapLatestTransactions.containsKey("DeviceB")) {
//                                dependencies.add(mapLatestTransactions.get("DeviceB"));
//                            }
//
//                            instance.addTransaction(context, topics, payload, dependencies);

                    }
                });
            }
        };

        timer = new Timer();

        timer.schedule(task,0,1000);

        Intent intent = new Intent(MainActivity.this, picture.class);
//                Log.i("Before starting","1");
        startActivity(intent);
//        runningMainActivity = true;

    }

//    public static void pause() {
//        Log.i("timer","cancel");
//        timer.cancel();
//    }
//
//    public static void resume() {
//        if (timer != null) {
//            Log.i("timer","resume");
//            timer = new Timer();
//            timer.schedule( task, 0, 1000 );
//        }
//    }


    @Override
    public void onBackPressed() {
        Log.d("Main Activity", "onBackPressed: Pressed");
        return;
    }
}
