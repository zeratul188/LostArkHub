package com.example.lostarkapplication.ui.home;

public class Event {
    private int number;
    private String startdate, enddate, url;

    public Event(int number, String startdate, String enddate, String url) {
        this.number = number;
        this.startdate = startdate;
        this.enddate = enddate;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
