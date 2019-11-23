package com.vegvisir.application.profiling;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;


public class ProgressFragment extends Fragment {

    private ProgressViewModel mViewModel;
    private AppendObservableList<String> logData;
    private LinearLayout linearLayout;
    private Activity main;

    public static ProgressFragment newInstance(AppendObservableList<String> logData, Activity main) {
        return new ProgressFragment(logData, main);
    }

    public ProgressFragment(AppendObservableList<String> logData, Activity main) {
        super();
        this.logData = logData;
        this.main = main;
        if (main != null)  {
            linearLayout = new TableLayout(main.getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TableRow header = new TableRow(main.getApplicationContext());
            for (String s: "timestamp,#reconciliation,#bytes,distance,#blocks,#receivedBytes,latitude,longitude".split(",")) {
                TextView txt = new TextView(main.getApplicationContext());
                txt.setText(s);
                txt.setPadding(5,2,5,2);
                txt.setGravity(Gravity.CENTER);
                header.addView(txt);
                }
            linearLayout.addView(header);
            logData.setAppendEventListener((x) -> {
                TableRow row = new TableRow(main.getApplicationContext());
                TextView dtxt = new TextView(main.getApplicationContext());
                dtxt.setPadding(5,2,5,2);
                dtxt.setText(new Date().toLocaleString());
                row.addView(dtxt);
                for (String r : x.split(",")) {
                    TextView txt = new TextView(main.getApplicationContext());
                    txt.setText(r);
                    txt.setPadding(5,2,5,2);
                    txt.setGravity(Gravity.CENTER);
                    row.addView(txt);
                }
                main.runOnUiThread(() -> {
                    linearLayout.addView(row, 1);
                });
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.progress_fragment, container, false);
    }

    public void setMain(Activity main) {
        this.main = main;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (linearLayout != null) {
            ScrollView view = getActivity().findViewById(R.id.log_scroll);
            view.addView(linearLayout);
            super.onActivityCreated(savedInstanceState);
            mViewModel = ViewModelProviders.of(this).get(ProgressViewModel.class);
        }
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        ScrollView view = getActivity().findViewById(R.id.log_scroll);
        view.removeAllViews();
        Log.d("DEBUG", "onDestroyView: View Destroyed");
        super.onDestroyView();
    }
}
