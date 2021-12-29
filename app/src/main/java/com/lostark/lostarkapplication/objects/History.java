package com.lostark.lostarkapplication.objects;

public class History implements Comparable<History> {
    private String name, date, content;
    private int year, month, day, hour, minute, second;

    public History(String name, String date, String content) {
        this.name = name;
        this.date = date;
        this.content = content;
        year = Integer.parseInt(date.substring(0, date.indexOf("년")));
        month = Integer.parseInt(date.substring(date.indexOf("년")+2, date.indexOf("월")));
        day = Integer.parseInt(date.substring(date.indexOf("월")+2, date.indexOf("일")));
        hour = Integer.parseInt(date.substring(date.indexOf("일")+2, date.indexOf("시")));
        minute = Integer.parseInt(date.substring(date.indexOf("시")+2, date.indexOf("분")));
        second = Integer.parseInt(date.substring(date.indexOf("분")+2, date.indexOf("초")));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int compareTo(History o) {
        if (year < o.getYear()) return 1;
        else if (year == o.getYear()) {
            if (month < o.getMonth()) return 1;
            else if (month == o.getMonth()) {
                if (day < o.getDay()) return 1;
                else if (day == o.getDay()) {
                    if (hour < o.getHour()) return 1;
                    else if (hour == o.getHour()) {
                        if (minute < o.getMinute()) return 1;
                        else if (minute == o.getMinute()) {
                            if (second <= o.getSecond()) return 1;
                            else return -1;
                        } else return -1;
                    } else return -1;
                } else return -1;
            } else return -1;
        } else return -1;
    }
}
