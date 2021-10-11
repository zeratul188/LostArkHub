package com.example.lostarkapplication.ui.commander;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommanderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CommanderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}