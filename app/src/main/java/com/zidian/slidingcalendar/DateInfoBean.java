package com.zidian.slidingcalendar;

public class DateInfoBean {
    //view类型：空白，年月标题，日期
    public static final int TYPE_DATE_BLANK = 0;
    public static final int TYPE_DATE_TITLE = 1;
    public static final int TYPE_DATE_NORMAL = 2;
    //选择区间类型：开始、中间、结束
    public static final int TYPE_INTERVAL_START = 0;
    public static final int TYPE_INTERVAL_MIDDLE = 1;
    public static final int TYPE_INTERVAL_END = 2;

    private int year = 2015;
    private int month;
    private int date;
    private int type;
    //分组
    private String groupName;
    private boolean isWeekend;
    //当天
    private boolean isToday;
    //选择日期
    private boolean isChooseDay;
    //区间类型
    private int intervalType;


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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public boolean isChooseDay() {
        return isChooseDay;
    }

    public void setChooseDay(boolean chooseDay) {
        isChooseDay = chooseDay;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public int getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(int intervalType) {
        this.intervalType = intervalType;
    }

    public String dateToString(){
        String sMonth = month < 10 ? String.format("0%d", month) : String.format("%d", month);
        String sDate = date < 10 ? String.format("0%d", date) : String.format("%d", date);
        return year + "年" + sMonth + "月" + sDate + "日";
    }
}
