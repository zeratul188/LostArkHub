package com.lostark.lostarkapplication.ui.gallery;

public class StoneHistory {
    private int num;
    private String content;
    private boolean isSuccess;

    public StoneHistory(int num, String content, boolean isSuccess) {
        this.num = num;
        this.content = content;
        this.isSuccess = isSuccess;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
