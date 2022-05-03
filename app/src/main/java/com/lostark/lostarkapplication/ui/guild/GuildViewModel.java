package com.lostark.lostarkapplication.ui.guild;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GuildViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GuildViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is guild fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}