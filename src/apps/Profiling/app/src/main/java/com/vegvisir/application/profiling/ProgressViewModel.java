package com.vegvisir.application.profiling;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class ProgressViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private AppendObservableList<String> logData;

    public ProgressViewModel() {

    }

    public void setLogData(AppendObservableList<String> logData) {
        this.logData = logData;
    }
}
