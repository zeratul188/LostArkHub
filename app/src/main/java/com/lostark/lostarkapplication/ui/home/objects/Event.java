package com.lostark.lostarkapplication.ui.home.objects;

import android.graphics.Bitmap;

public class Event implements Comparable<Event> {
    private int number;
    private String startdate, enddate, url;
    private int year, month, day;
    private int end_year, end_month, end_day;
    private Bitmap bitmap = null;
    private boolean isFail = false;

    public Event(int number, String startdate, String enddate, String url) {
        this.number = number;
        this.startdate = startdate;
        this.enddate = enddate;
        this.url = url;
        year = Integer.parseInt(startdate.substring(0, 4));
        month = Integer.parseInt(startdate.substring(startdate.indexOf("년 ")+2, startdate.indexOf("월")));
        day = Integer.parseInt(startdate.substring(startdate.indexOf("월 ")+2, startdate.indexOf("일")));
        end_year = Integer.parseInt(enddate.substring(0, 4));
        end_month = Integer.parseInt(enddate.substring(enddate.indexOf("년 ")+2, enddate.indexOf("월")));
        end_day = Integer.parseInt(enddate.substring(enddate.indexOf("월 ")+2, enddate.indexOf("일")));
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getEnd_year() {
        return end_year;
    }

    public int getEnd_month() {
        return end_month;
    }

    public int getEnd_day() {
        return end_day;
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

    @Override
    public int compareTo(Event o) {
        if (year < o.getYear()) return 1;
        else if (year == o.getYear()) {
            if (month < o.getMonth()) return 1;
            else if (month == o.getMonth()) {
                if (day < o.getDay()) return 1;
                else if (day == o.getDay()) {
                    if (end_year > o.getEnd_year()) return 1;
                    else if (end_year == o.getEnd_year()) {
                        if (end_month > o.getEnd_month()) return 1;
                        else if (end_month == o.getEnd_month()) {
                            if (end_day > o.getEnd_day()) return 1;
                            else if (end_day == o.getEnd_day()) return 0;
                            else return -1;
                        } else return -1;
                    } else return -1;
                }
                else return -1;
            } else return -1;
        } else return -1;
    }
}
