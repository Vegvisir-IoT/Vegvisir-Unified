package com.vegvisir.application.profiling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LogFileManager {

    private Context context;
    private Activity main;
    private static final int WRITE_REQUEST_CODE = 43;

    public LogFileManager(Context context, Activity main) {
        this.context = context;
        this.main = main;
    }

    public OutputStreamWriter createFile(String name) throws VegvisirProfilingException, IOException {

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            throw new VegvisirProfilingException("No external storage available");
        }

        File logfile = new File(main.getExternalFilesDir("Vegvisir"), name);
        if (logfile.createNewFile()) {
            Log.d("LogFile", "createFile: "+logfile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(logfile, true);
            return new OutputStreamWriter(fos);
        } else {
            throw new VegvisirProfilingException("Created file with name("+name+") failed");
        }
    }
}