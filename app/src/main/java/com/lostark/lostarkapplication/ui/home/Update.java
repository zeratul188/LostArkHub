package com.lostark.lostarkapplication.ui.home;

public class Update implements Comparable<Update>{
    private String date, url;
    private int year, month, day;

    public Update(String date, String url) {
        this.date = date;
        this.url = url;
        year = Integer.parseInt(date.substring(0, 4));
        month = Integer.parseInt(date.substring(date.indexOf("년 ")+2, date.indexOf("월")));
        day = Integer.parseInt(date.substring(date.indexOf("월 ")+2, date.indexOf("일")));
        System.out.println("Year : "+year+", Month : "+month+", Day : "+day);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Update o) {
        if (year < o.getYear()) return 1;
        else if (year == o.getYear()) {
            if (month < o.getMonth()) return 1;
            else if (month == o.getMonth()) {
                if (day < o.getDay()) return 1;
                else if (day == o.getDay()) return 0;
                else return -1;
            } else return -1;
        } else return -1;
    }
}
