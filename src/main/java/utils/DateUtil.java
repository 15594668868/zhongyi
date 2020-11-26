package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 */
public class DateUtil{
    /**
     * 传入格式获取日期
     * yyyy-MM-dd HH:mm:ss
     * yyyyMMddHHmmssSSS
     * @return 日期
     */
    public static String getDate(Date date, String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat (pattern);
        return formatter.format(date);
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static String getCurrentTimeMillis(){
       return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * 日期格式转换从yyyy-MM-dd HH:mm:ss到 yyyyMMddHHmmss
     * @param strDateTime
     * @return
     */
     public static String lengthConvesion(String strDateTime){
	      String strYMDHMS = "";
	      if(strDateTime == null){
	      	return "" ;
	      }  
	      if(strDateTime.length() == 10) {
	      	strYMDHMS = " 23:59:59";
	      }
	      return  strDateTime+strYMDHMS; 
	   }

    /**
     * 得到系统当前日期是星期几，格式 "星期一"
     * @return
     */
    public static String getWeek(){
    	String strCurrentWeek = "";
    	Date currentWeek = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat ("E");
        strCurrentWeek= formatter.format(currentWeek);
        return strCurrentWeek; 
    }
    
    /**
     * 得到任意输入的一个日期的星期数，传入格式"2020-06-09" 返回格式 "星期一"
     * @param strDate
     * @return
     */
    public static String getDateToWeek(String strDate){
        String strWeek="";
        int iYear = Integer.parseInt(strDate.substring(0, 4));
        int iMonth = Integer.parseInt(strDate.substring(5, 7));
        int iDay = Integer.parseInt(strDate.substring(8, 10));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, iYear);
        cal.set(Calendar.MONTH, iMonth-1);
        cal.set(Calendar.DAY_OF_MONTH, iDay);
        Date currentDate = cal.getTime();        
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        strWeek= formatter.format(currentDate);                
        return strWeek;
    }


    /**
     * 传入正数或者负数天数，及时间格式，返回对应的时间，例如传入1返回当前日期后一天，传入-1返回昨天。disposeDate(0, "yyyy-MM-dd HH:mm:ss")
     * @param day
     * @param pattern
     * @return
     */
    public static String disposeDate(int day, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        return format.format(c.getTime());
    }

    /**
     * 传入类似："2020-06-02 08:58:19"，返回传入时间时间戳
     * @param date
     * @return
     */
    public static long getTime(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = simpleDateFormat.parse(date);
            return newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 因有较多查询条件获取时间戳格式，所以写此方法，用于获取时间戳的数组
     * @param startDay 开始时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @param endDay 结束时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @return
     */
    public static Long[] getTimeList(int startDay, int endDay) {
        //获取到当前时间往前推1天的日期和时间戳
        String startDate = disposeDate(startDay, "yyyy-MM-dd HH:mm:ss");
        Long startTime = getTime(startDate);
        //获取到当前时间的日期和时间戳
        String endDate = disposeDate(endDay, "yyyy-MM-dd HH:mm:ss");
        Long endTime = getTime(endDate);
        return new Long[] {startTime, endTime};
    }

    /**
     * 因有较多查询条件获取时间戳格式，所以写此方法，用于获取时间戳的数组
     * 与上方方法不同的是,该查询只返回00:00:00时间,时间精确到天
     * @param startDay 开始时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @param endDay 结束时间传入的天，根据正数或负数在当前时间进行增加或减少
     * @return
     */
    public static Long[] getTimeListDay(int startDay, int endDay) {
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, startDay);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        Long startTime = cal1.getTimeInMillis();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, endDay);
        // 将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long endTime = c.getTimeInMillis();
        return new Long[] {startTime, endTime};
    }

    /**
     * 获取今日零点零分零秒时间
     */
    public static Calendar getCurrentTime() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1;
    }


    /**
     *
     * 本地时间转化为UTC时间
     */
    public static Date localToUTC(String localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate= null;
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis=localDate.getTime();
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        Date utcDate=new Date(calendar.getTimeInMillis());
        return utcDate;
    }

    /**
     *
     * UTC时间转化为本地时间
     *
     */
    public static Date utcToLocal(String utcTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        String localTime = sdf.format(utcDate.getTime());
        try {
            locatlDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return locatlDate;
    }
}

