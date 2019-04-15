package com.zidian.slidingcalendar;

import java.util.List;

public class MonthInfoBean {
    //属于哪年
    private int year;
    //哪一月
    private int month;
    //日期list
    private List<DateInfoBean> dateList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<DateInfoBean> getDateList() {
        return dateList;
    }

    public void setDateList(List<DateInfoBean> dateList) {
        this.dateList = dateList;
    }
}
