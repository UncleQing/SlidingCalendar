package com.zidian.slidingcalendar;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AppDateTools {

    public static final String DATE_FORMAT ="yyyy年MM月dd日";
    public static final String DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm:ss";


    /**
     * 获取系统时间 格式为："yyyy/MM/dd "
     **/
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /**
     * 获取系统时间 格式为："yyyy "
     **/
    public static String getCurrentYear() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        return sf.format(d);
    }

    /**
     * 获取系统时间 格式为："MM"
     **/
    public static String getCurrentMonth() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        return sf.format(d);
    }

    /**
     * 获取系统时间 格式为："dd"
     **/
    public static String getCurrentDay() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        return sf.format(d);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTime() {
        long d = System.currentTimeMillis() / 1000;
        return d;
    }

    /**
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time) {
        return getDateToString(time,DATE_FORMAT);
    }


    /**
     * 时间戳转换成字符窜
     */
    public static String getDateToString(long time, String format) {
        Date d = new Date(time);
        if (TextUtils.isEmpty(format)){
            format = DATE_FORMAT;
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }



    /**
     * 时间戳中获取年
     */
    public static String getYearFromTime(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat  sf = new SimpleDateFormat("yyyy");
        return sf.format(d);
    }

    /**
     * 时间戳中获取月
     */
    public static String getMonthFromTime(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        return sf.format(d);
    }

    /**
     * 时间戳中获取日
     */
    public static String getDayFromTime(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        return sf.format(d);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * Date转为yyyy-MM-dd
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Date转为yyyy年-MM月
     * @param dateDate
     * @return
     */
    public static String dateToStrYM(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 时间戳差值转为天数差
     * @param diffTime
     * @return
     */
    public static int diffTime2diffDay(long diffTime) {
        return (int) (diffTime / 1000 / 3600 / 24);
    }
}