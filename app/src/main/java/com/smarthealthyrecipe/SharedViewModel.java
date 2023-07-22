package com.smarthealthyrecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> inputData = new MutableLiveData<>();

    public void setInputData(String data) {
        inputData.setValue(data);
    }

    public LiveData<String> getInputData() {
        return inputData;
    }
}

