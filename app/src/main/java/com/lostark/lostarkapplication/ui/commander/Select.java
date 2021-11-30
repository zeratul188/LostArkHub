package com.lostark.lostarkapplication.ui.commander;

import androidx.annotation.Nullable;

public class Select {
    private String content;
    private boolean isChecked;

    public Select(String content) {
        this.content = content;
        isChecked = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return content.equals(((Select)obj).getContent());
    }
}
