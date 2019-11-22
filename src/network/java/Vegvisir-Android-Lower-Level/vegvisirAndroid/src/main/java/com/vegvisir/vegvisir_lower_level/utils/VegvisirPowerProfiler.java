package com.vegvisir.vegvisir_lower_level.utils;

import android.app.Activity;
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
import java.util.Date;
import java.util.Locale;

public class VegvisirPowerProfiler {
    private IntentFilter filter;
    private Intent manager;
    private Context ourContext;
    private Activity main;
    private double cap = 0;

    VegvisirPowerProfiler( Context c, Activity m){
        ourContext = c;
        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        manager= ourContext.registerReceiver(null, filter);
        main = m;
    }

    /**
     * getPowerLevel
     * @return float representation of percentage of remaining Battery Life (format :: 22.79f)
     */
    public float getPowerLevel() {

        int level = manager.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = manager.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

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
    public void saveBatteryStatus( String fileName, String comment, Activity m){

        // Step I: Ensure main is set
        if (main == null){
            Log.d("MAIN", "Main activity lost. Restoring");
            this.main = m;
        }
        try {
            // What one needs to know is if the file exists. If it doesn't exist
            // create the file
            File o =new File (main.getExternalFilesDir("Vegvisir"), fileName);
            Boolean exists = o.exists();

            OutputStreamWriter writer = initSaveWriter( fileName );
            BatteryMetrics metrics = retrieveBatteryStatus(comment);
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


    public BatteryMetrics retrieveBatteryStatus(String purpose){
        return new BatteryMetrics(getDateTime(), getPowerLevel(),
                retrieveBatteryCapacity(), isCharging(), purpose);
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
