package com.vegvisir.application.profiling;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VegvisirPowerProfiler {
    private IntentFilter filter;
    private Intent manager;
    private Context ourContext;
    private Activity main;
    private double cap = 0;

    private static final Object lock = new Object();
    private BatteryMetrics batteryMetrics;

    VegvisirPowerProfiler( Context c, Activity m){
        ourContext = c;
        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        manager= ourContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                synchronized(lock) {
                    batteryMetrics = retrieveBatteryStatus(intent);
                }
                Log.d("Battery", "onReceive: battery changed");
            }
        }, filter);
        main = m;
        batteryMetrics = retrieveBatteryStatus(manager);
    }

    /**
     * getPowerLevel
     * @return float representation of percentage of remaining Battery Life (format :: 22.79f)
     */
    public float getPowerLevel(Intent intent) {

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float)scale) * 100.0f;
        Log.i("Second Findings", "Level: " + level + " Scale: " + scale +
                " Percentage: " + batteryPct);
        return batteryPct;
    }


    /**
     * retrieveBatteryCapacity
     * @return double that represents the battery size of device in mAh
     */
    public double retrieveBatteryCapacity() {
        if ( cap != 0 )                 // Capacity does not change for a device.
            return cap;
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(ourContext);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity;
    }

    public boolean isCharging(Intent intent){
        int condition = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return condition == BatteryManager.BATTERY_STATUS_CHARGING;
    }

    public boolean isCharging(){
        int condition = manager.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return condition == BatteryManager.BATTERY_STATUS_CHARGING;
    }

    public String getDateTime(){
        return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /**
     * saveBatterySettings
     * @param fileName : String
     * format of saving: time, percentage, capacity, isCharging
     * Saved in directory: /data/user/_someNumber_/com.vegvisir.powerapplication/files/
     */
    public void saveBatteryStatus( String fileName, String comment){

        // Step I: Ensure main is set
        if (main == null){
            Log.d("MAIN", "Main activity lost. saveBatteryStatus Failed");
            return;
        }
        try {
            // What one needs to know is if the file exists. If it doesn't exist
            // create the file
            File o =new File (main.getExternalFilesDir("Vegvisir"), fileName);
            Boolean exists = o.exists();

            OutputStreamWriter writer = initSaveWriter( fileName );
            BatteryMetrics metrics = batteryMetrics;
            if (!exists)
                writer.write( metrics.getHeader() + "\n" );
            writer.write( metrics.getFullMetrics() + "\n" );
            writer.flush();
            writer.close();
        }catch(IOException e){
            Log.d("FILE", "Failed to write to file.");
            e.printStackTrace();
        }
    }

    public void writeMetrics(OutputStreamWriter ow, String tag) throws IOException{
         BatteryMetrics metrics =  batteryMetrics;
         ow.write(metrics.getFullMetrics() + "\n");
         ow.flush();
    }

    public OutputStreamWriter initSaveWriter ( String name) throws IOException{
        String state =  Environment.getExternalStorageState();
        if (!state.equals( Environment.MEDIA_MOUNTED)){
            throw new IOException("External file system not mounted");
        }
        File ourFile = new File (main.getExternalFilesDir("Vegvisir"), name);
        if ( ourFile.createNewFile()){
            Log.d("LOGFILE:", "createFile: "+ ourFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream( ourFile, true );
            return new OutputStreamWriter(fos);
        }
        else if( ourFile != null && ourFile.exists()){
            FileOutputStream fos = new FileOutputStream( ourFile, true );
            return new OutputStreamWriter(fos);
        }
        else
            throw new IOException( "CreateFile: " + name + " failed");
    }


    public BatteryMetrics retrieveBatteryStatus(Intent intent){
        return new BatteryMetrics(getDateTime(), getPowerLevel(intent),
                retrieveBatteryCapacity(), isCharging(intent), "");
    }

    public List<String> getBatteryStatus() {
        synchronized (lock) {
            return batteryMetrics.getFullMetricsList();
        }
    }


    /**
     * BatteryMetrics
     * Toy Class representing the metrics that can be obtained by the library.
     * A list of these would translate to readouts of a whole setting.
     */
    class BatteryMetrics{
        private String dateTime;
        private float percentage;
        private float remaining;
        private double capacity;
        private boolean isCharge;
        private String comment;

        public BatteryMetrics( String a, float b, double c, Boolean d, String e){
            dateTime   = a;
            percentage = b;
            capacity   = c;
            isCharge   = d;
            remaining  = (float) ((b /100.0f) * c);
            comment    = e;
        }



        /**
         * Helper Function :: Non-Dynamic
         * @return String of variables titles that are returned in the
         * getFullMetrics().
         */
        public String getHeader() {
            return  "dateTime, Percentage, Remaining, IsCharge, Purpose";
        }


        public String getFullMetrics(){
            return dateTime +", " + String.valueOf(percentage) +", "+
                    String.valueOf(remaining) + ", " + String.valueOf(isCharge) +
                    ", " + comment;
        }

        public List<String> getFullMetricsList(){
            List<String> metrics = new ArrayList<>();
            metrics.add(percentage + "");
            metrics.add(remaining+ "");
            metrics.add(isCharge+ "");
            return metrics;
        }

        /**
         * Helper Method
         * @param aString : String that contains at least 1 comma as delimeter
         * @return The cdr of the string
         */
        public String getCdr( String aString ){
            String returnString = aString.substring( aString.indexOf(",")+1);
            returnString.trim();
            return returnString;
        }

        @Override
        public String toString() {
            return "BatteryMetrics\n{" +
                    "dateTime='" + dateTime + '\'' +
                    ", percentage= " + percentage +
                    ",\nremaining= " + remaining +
                    " mAh, capacity= " + capacity +
                    " mAh, isCharge= " + isCharge +
                    " Purpose = " + comment +
                    '}';
        }
    }
}
