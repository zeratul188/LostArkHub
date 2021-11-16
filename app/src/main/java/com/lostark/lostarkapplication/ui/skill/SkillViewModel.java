package com.lostark.lostarkapplication.ui.skill;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SkillViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SkillViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is chracter fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}