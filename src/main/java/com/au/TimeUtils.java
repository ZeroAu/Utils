package com.au;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * formatTime 格式化时间
 * formatToDate 字符串转换为Date
 * getTime 获取现在时间
 * beforeByDate 计算x天前的日子
 * beforeByMouth 计算x月前的日子
 * beforeByYear 计算x年前的日子
 * timeToChinese 格式化时间戳为xx天xx小时xx分钟xx秒
 * parse 时间表达式转换为Date
 * countDiff 比较时间
 */
public class TimeUtils {

    private final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatTime(Date d, String pattern) {
        return new SimpleDateFormat(pattern).format(d);
    }

    public static Date formatToDate(String d){
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取现在时间
     * @return
     */
    public static String getTime(){
        return formatTime(new Date(), DEFAULT_PATTERN);
    }

    /**
     * 获取现在时间
     * @return
     */
    public static String getTime(String pattern){
        return formatTime(new Date(), pattern);
    }

    public static Date beforeByDate(int day){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -day);
        return c.getTime();
    }

    public static Date beforeByDate(Date date, int day){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -day);
        return c.getTime();
    }

    public static Date beforeByDate(Calendar calendar, int day){
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    public static Date beforeByMouth(int month){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -month);
        return c.getTime();
    }

    public static Date beforeByMouth(Date date, int month){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -month);
        return c.getTime();
    }

    public static Date beforeByMouth(Calendar calendar, int month){
        calendar.add(Calendar.MONTH, -month);
        return calendar.getTime();
    }

    public static Date beforeByYear(int year){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -year);
        return c.getTime();
    }

    public static Date beforeByYear(Date date, int year){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -year);
        return c.getTime();
    }

    public static Date beforeByYear(Calendar calendar, int year){
        calendar.add(Calendar.YEAR, -year);
        return calendar.getTime();
    }

    public static String timeToChinese(long timestamp){
        String result;

        long day = timestamp / (24 * 60 * 60 * 1000);
        long hour = (timestamp / (60 * 60 * 1000) - day * 24);
        long min = ((timestamp / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (timestamp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day != 0) {
            result = day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        } else if (hour != 0) {
            result = hour + "小时"+ min + "分钟" + sec + "秒";
        } else if (min != 0) {
            result = min + "分钟" + sec + "秒";
        } else if (sec != 0) {
            result = sec + "秒" ;
        } else {
            result = "0秒";
        }
        return result;
    }

    public static Date parse(String strDate, String pattern) throws ParseException {
        return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
    }

    public static Date parse(String strDate) throws ParseException {
        return parse(strDate, DEFAULT_PATTERN);
    }

    /**
     * 比较时间
     * @param date1
     * @param date2
     * @return
     */
    public static long countDiff(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * 比较时间
     * @param date1
     * @param date2
     * @return
     */
    public static long countDiff(String date1, String date2, String pattern) throws ParseException {
        return countDiff(parse(date1, pattern), parse(date2, pattern));
    }

    /**
     * 比较时间
     * @param date1
     * @param date2
     * @return
     */
    public static long compare(String date1, String date2) throws ParseException {
        return countDiff(date1, date2, DEFAULT_PATTERN);
    }


}
