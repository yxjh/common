package com.yxjh.api;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * @program: common
 * @author: xuWei
 * @create: 2019/06/17
 * @description: 常用的公共方法
 */
public class CommonUtils {

    /**
     * 获取月开始时间
     * @param year
     * @param month
     * @return
     */
    public static String getMonthBeginTime(int year,int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));
        Date date= Date.from(zonedDateTime.toInstant());
        return dateToStr(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取月结束时间
     * @param year
     * @param month
     * @return
     */
    public static String getMonthEndTime(int year,int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        Date date=Date.from(zonedDateTime.toInstant());
        return dateToStr(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间日期转为字符串
     * @param date
     * @param formdate
     * @return
     */
    public static String dateToStr(Date date,String formdate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formdate);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间日期转为字符串类型
     * @param date
     * @return
     */
    public static String dateToStr(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     *时间字符串转为date
     * @param dateString
     * @return
     */
    public static Date strToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getDate(simpleDateFormat,dateString);
    }

    /**
     * 获取当前时间
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate(Date date){
        return dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 空对象判断
     * @param obj
     * @return
     */
    public static boolean isNullObject(Object obj) {
        boolean result = false;
        if (null == obj || "".equals(obj)) {
            result = true;
        }
        return result;
    }

    private static Date getDate(SimpleDateFormat simpleDateFormat,String dateString){
        Date result=null;
        try {
            result = simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]){
        System.out.println(getMonthBeginTime(2019,6));
        System.out.println(getMonthEndTime(2019,6));
        System.out.println(getCurrentDate(new Date()));
    }
}
