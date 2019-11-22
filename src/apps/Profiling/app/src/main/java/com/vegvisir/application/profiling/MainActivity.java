package com.vegvisir.application.profiling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vegvisir.util.profiling.VegvisirStatsCollector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
//    private TextView mTextMessage;

    private Fragment parameters;

    private LogFileManager logFileManager;

    private ExperimentManager experimentManager;

    private VegvisirAdapter vegvisirAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
        new Thread(this::initializeExperiment).start();
    }

    private void initializeExperiment() {
        parameters = new Parameters();
        logFileManager = new LogFileManager(getApplicationContext(), this);
        vegvisirAdapter = new VegvisirAdapter(getApplicationContext());
        experimentManager = new ExperimentManager(vegvisirAdapter, logFileManager, getApplicationContext());
    }


    public void showTimePickerDialog(View v) {
        EditText text;
        if (v.getId() == R.id.startTimePicker) {
            text = findViewById(R.id.startTime);
        } else {
            text = findViewById(R.id.endTime);
        }
        DialogFragment newFragment = new TimepickerFragment(text);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void startExperiment(View v) {
        try {
             experimentManager.startExperiment(readParameters());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private ExperimentParameter readParameters() throws ParseException  {
        ExperimentParameter.Builder builder = ExperimentParameter.getBuilder();
        EditText stText = (EditText)findViewById(R.id.startTime);
        Date stTime = DateFormat.getTimeInstance().parse(stText.getText().toString());
        EditText edText = (EditText)findViewById(R.id.endTime);
        Date edTime = DateFormat.getTimeInstance().parse(edText.getText().toString());
        EditText blockSizeText = (EditText)findViewById(R.id.blockSize);
        EditText blockRateText = (EditText)findViewById(R.id.blockRate);
        EditText distanceText = (EditText)findViewById(R.id.distance);
        int blockSize = Integer.parseInt(blockSizeText.getText().toString());
        int blockRate = Integer.parseInt(blockRateText.getText().toString());
        int distance = Integer.parseInt(distanceText.getText().toString());
        Date stDate = new Date();
        Date edDate = new Date();
        stDate.setHours(stTime.getHours());
        stDate.setMinutes(stTime.getMinutes());
        stDate.setSeconds(stTime.getSeconds());
        edDate.setHours(edTime.getHours());
        edDate.setMinutes(edTime.getMinutes());
        edDate.setSeconds(edTime.getSeconds());
        return builder.setBlockRate(blockRate).setBlockSize(blockSize).setDistance(distance).setEndTime(edDate).setSamplingRate(1)
                .setStartTime(stDate).build();
    }

}
