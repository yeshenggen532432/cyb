package com.qwb.utils;

import android.annotation.SuppressLint;

import com.qwb.common.DateFormatEnum;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */

public class MyTimeUtils {

    /**
     * 获取年份
     */
    private static Calendar calendar = Calendar.getInstance();

    public static int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     */
    public static int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取几号（月份）
     */
    public static int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取几时
     */
    public static int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取几分
     */
    public static int getMin() {
        return calendar.get(Calendar.MINUTE);
    }

    public static int getYear(String date) {
        return Integer.valueOf(date.split("-")[0]);
    }

    /**
     * 获取月份
     */
    public static int getMonth(String date) {
        return Integer.valueOf(date.split("-")[1]) - 1;
    }

    /**
     * 获取几号（月份）
     */
    public static int getDay(String date) {
        return Integer.valueOf(date.split("-")[2]);
    }

    /**
     * 获取几号（周一）
     */
    public static int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获取今天日期的字符串
     */
    public static String getTodayStr() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.format(date);
        return format.format(date);
    }

    /**
     * 获取当前时间
     */
    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.format(date);
        return format.format(date);
    }

    /**
     * 得到本周周一
     */
    public static String getMondayOfThisWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return sdf.format(c.getTime());
    }

    /**
     * 当月第一天
     */
    public static String getFirstOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }

    /**
     * 当月最后一天
     */
    public static String getLastOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getFirstOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getLastOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * 得到上周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getFirstOfShangWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek - 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * 得到上周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getLastOfShangWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * 获取今天日期--格式为 2014-12-11 的字符串
     */
    public static String getToday_nyr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 上月第一天
     */
    public static String getFirstOfShangMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(calendar.getTimeInMillis());
        return sf.format(calendar.getTime());
    }

    /**
     * 上月最后一天
     */
    public static String getLastOfShangMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(calendar.getTimeInMillis());
        return sf.format(calendar.getTime());
    }

    /**
     * 获取--昨天
     */
    public static String getYesterday() {
        Date date = new Date();// 取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取日期--格式为 2014-12-11 的字符串 只有i：把日期往后增加一天.整数往后推,负数往前移动
     * 0:今天，-1：昨天，-2:前天，1：明天
     */
    public static String getDate_nyr(int i) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取日期--格式为 2014-12-11 的字符串 只有i：把日期往后增加一天.整数往后推,负数往前移动
     * 0:今天，-1：昨天，-2:前天，1：明天
     */
    public static String getDateYmdhm(int i) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 年月日时分秒 转 年月日
     */
    public static String getNyrByNyrsfm(String s) {
        String dateStr = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(s);
            dateStr = formatter2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 是否超过12小时
     */
    public static boolean judgmentDate(String startDate, String endDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = simpleDateFormat.parse(startDate);
            Date end = simpleDateFormat.parse(endDate);
            long cha = end.getTime() - start.getTime();
            if (cha < 0) {
                return false;
            }
            double result = cha * 1.0 / (1000 * 60 * 60);
            if (result >= 12) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 时间字符串转Date
     */
    public static Calendar getStrToDate(String dateStr) {
        Calendar calendar = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatEnum.Y_M_D_H_M.getType());
            Date date = simpleDateFormat.parse(dateStr);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    // 获取当前日期
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }


    /**
     * 将当前时间戳转化为标准时间函数
     */
    public static String getTime(String time1) {
        int timestamp = Integer.parseInt(time1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            Date currentdate = new Date(); // 当前时间

            long i = (currentdate.getTime() / 1000 - timestamp) / (60);
            System.out.println(currentdate.getTime());
            System.out.println(i);
            Timestamp now = new Timestamp(System.currentTimeMillis()); // 获取系统当前时间
            System.out.println("now-->" + now); // 返回结果精确到毫秒。

            String str = sdf.format(new Timestamp(intToLong(timestamp)));
            time = str.substring(11, 16);

            String month = str.substring(5, 7);
            String day = str.substring(8, 10);
            System.out.println(str);
            System.out.println(time);
            System.out.println(getDate(month, day));
            time = getDate(month, day) + time;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    public static String getTime(int timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = null;
        try {
            String str = sdf.format(new Timestamp(intToLong(timestamp)));
            time = str.substring(11, 16);

            String month = str.substring(5, 7);
            String day = str.substring(8, 10);
            time = getDate(month, day) + time;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }


    // java Timestamp构造函数需传入Long型
    public static long intToLong(int i) {
        long result = (long) i;
        result *= 1000;
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate(String month, String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
        java.util.Date d = new java.util.Date();
        ;
        String str = sdf.format(d);
        @SuppressWarnings("unused")
        String nowmonth = str.substring(5, 7);
        String nowday = str.substring(8, 10);
        String result = null;

        int temp = Integer.parseInt(nowday) - Integer.parseInt(day);
        switch (temp) {
            case 0:
                result = "今天";
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "前天";
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.parseInt(month) + "月");
                sb.append(Integer.parseInt(day) + "日");
                result = sb.toString();
                break;
        }
        return result;
    }

    /**
     * 获取日期--格式为 2014-12-11 的字符串 只有i：把日期往后增加一天.整数往后推,负数往前移动
     * 0:今天，-1：昨天，-2:前天，1：明天
     */
    public static String getDateYmdhms(int i) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }


}
