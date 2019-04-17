package com.zidian.slidingcalendar.bean;

public class DateInfoBean {
    //view类型：空白，年月标题，日期
    public static final int TYPE_DATE_BLANK = 0;
    public static final int TYPE_DATE_TITLE = 1;
    public static final int TYPE_DATE_NORMAL = 2;
    //选择区间类型：开始、中间、结束
    public static final int TYPE_INTERVAL_START = 0;
    public static final int TYPE_INTERVAL_MIDDLE = 1;
    public static final int TYPE_INTERVAL_END = 2;
    //最近几天
    public static final String STR_RECENT_TODAY = "今天";
    public static final String STR_RECENT_TOMORROW = "明天";
    public static final String STR_RECENT_ACQUIRED = "后天";

    private int year = 2018;
    private int month;
    private int date;
    private int type;
    //分组
    private String groupName;
    //周末
    private boolean isWeekend;
    //节日
    private String festival;
    //最近几天
    private boolean isRecentDay;
    private String recentDayName;
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


    public boolean isRecentDay() {
        return isRecentDay;
    }

    public void setRecentDay(boolean recentDay) {
        isRecentDay = recentDay;
    }

    public String getRecentDayName() {
        return recentDayName;
    }

    public void setRecentDayName(String recentDayName) {
        this.recentDayName = recentDayName;
    }

    public int getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(int intervalType) {
        this.intervalType = intervalType;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String dateToString(){
        String sMonth = month < 10 ? String.format("0%d", month) : String.format("%d", month);
        String sDate = date < 10 ? String.format("0%d", date) : String.format("%d", date);
        return year + "年" + sMonth + "月" + sDate + "日";
    }
    public String monthToString(){
        String sMonth = month < 10 ? String.format("0%d", month) : String.format("%d", month);
        return year + "年" + sMonth + "月";
    }
}
