package com.vegvisir.application.profiling;

import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimepickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int hourOfDay;
    private int minute;
    private EditText timetext;

    public TimepickerFragment(EditText text) {
        this.timetext = text;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        this.hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        this.minute = minute;
        this.hourOfDay = hourOfDay;
        Date now = new Date();
        now.setHours(this.hourOfDay);
        now.setMinutes(this.minute);
        now.setSeconds(0);
        this.timetext.setText(java.text.DateFormat.getTimeInstance().format(now));
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }
}
