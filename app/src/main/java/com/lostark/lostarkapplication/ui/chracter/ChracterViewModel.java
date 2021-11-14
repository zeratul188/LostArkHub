package com.lostark.lostarkapplication.ui.chracter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChracterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChracterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is chracter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}