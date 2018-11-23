package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import static util.DateUtil.getUsDateTime;
import static util.StringUtil.*;

public class DateUtil {

    protected static Logger log = Logger.getLogger(DateUtil.class);
    // LogSaveDao log = new LogSaveDao();
    protected Calendar calendar = Calendar.getInstance();

    public enum DateTimeType {

        Long, Short
    }

    public enum DateLocaleType {

        US, TW
    }

    static String[] formatStrings = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy/MM", "yyyyMMddHHmmss", "yyyyMMdd", "yyyyMM"};

    /**
     * 嘗試使用幾個不同的方式將string轉成Date
     *
     * @param dateString 目前可接受格式"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd
     * HH:mm:ss", "yyyy/MM/dd"
     * @return
     */
    public static Date tryParse(String dateString) {
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            } catch (ParseException e) {
            }
        }

        return null;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11 21:10:03
     *
     * @param usDateTime
     * @return 回傳格式為yyy/MM/dd HH:mm:ss
     */
    public static String getTwDateTime(Date usDateTime) {
        if (usDateTime == null) {
            return "";
        }
        /*
         * StringBuilder twYear = new StringBuilder();
         * twYear.append(Integer.parseInt(UsDateTime.substring(0, 4)) - 1911);
         * String twDate = twYear.toString() + UsDateTime.substring(4);
         * twDate=twDate.replaceAll("-", "/");
         */
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
			// Date date= sdf.parse(UsDateTime);
            // Date date= tryParse(usDateTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(usDateTime);
            int year = cal.get(Calendar.YEAR) - 1911;
            twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/" + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2) + " "
                    + addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + ":" + addPrefixZero(cal.get(Calendar.MINUTE) + "", 2) + ":"
                    + addPrefixZero(cal.get(Calendar.SECOND) + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return twDateString;
    }

    /**
     * 從西元年月日時分秒轉換成時分秒 如:102/10/11 21:10:03轉成 21:10:03
     *
     * @param usDateTime
     * @return 回傳格式為 HH:mm:ss
     */
    public static String getTwDateTimeByYYYYMMdd(Date usDateTime) {
        if (usDateTime == null) {
            return "";
        }

        String twDateString = "";
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(usDateTime);
            twDateString = addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + addPrefixZero(cal.get(Calendar.MINUTE) + "", 2)
                    + addPrefixZero(cal.get(Calendar.SECOND) + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return twDateString;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11 21:10:03
     *
     * @param usDateTime 格式為yyyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyy/MM/dd HH:mm:ss
     */
    public static String getTwDateTime(String usDateTime) {
        if (nvl(usDateTime).equals("")) {
            return "";
        }
        /*
         * StringBuilder twYear = new StringBuilder();
         * twYear.append(Integer.parseInt(UsDateTime.substring(0, 4)) - 1911);
         * String twDate = twYear.toString() + UsDateTime.substring(4);
         * twDate=twDate.replaceAll("-", "/");
         */
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(UsDateTime);
            Date date = tryParse(usDateTime);
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR) - 1911;
                twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/" + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2) + " "
                        + addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + ":" + addPrefixZero(cal.get(Calendar.MINUTE) + "", 2) + ":"
                        + addPrefixZero(cal.get(Calendar.SECOND) + "", 2);

            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return twDateString;
    }

    public static String getTwDateTimeByPage(String usDateTime) {
        if (nvl(usDateTime).equals("")) {
            return "";
        }
        /*
         * StringBuilder twYear = new StringBuilder();
         * twYear.append(Integer.parseInt(UsDateTime.substring(0, 4)) - 1911);
         * String twDate = twYear.toString() + UsDateTime.substring(4);
         * twDate=twDate.replaceAll("-", "/");
         */
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(UsDateTime);
            Date date = tryParse(usDateTime);
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR) - 1911;
                twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/" + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2) + ":"
                        + addPrefixZero(cal.get(Calendar.HOUR_OF_DAY) + "", 2) + ":" + addPrefixZero(cal.get(Calendar.MINUTE) + "", 2) + ":"
                        + addPrefixZero(cal.get(Calendar.SECOND) + "", 2);

            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return twDateString;
    }

    public static String replace2TwYear(String usDateTime) {
        if (nvl(usDateTime).equals("") || nvl(usDateTime).length() < 4) {
            return "";
        }
        StringBuilder twYear = new StringBuilder();
        twYear.append(Integer.parseInt(usDateTime.substring(0, 4)) - 1911);
        String twDate = twYear.toString() + usDateTime.substring(4);
        twDate = twDate.replaceAll("-", "/");
        return twDate;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成102/10/11
     *
     * @param usDate 格式為yyyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyy/MM/dd
     */
    public static String getTwDate(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR) - 1911;
            twDateString = year + "/" + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + "/" + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    /**
     * 從西元年月日時分秒轉換成民國年月日時分秒 如:2013-10-11 21:10:03轉成10210
     *
     * @param usDate 格式為yyyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyyMM
     */
    public static String getTwDateByYYYMM(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR) - 1911;
            twDateString = year + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    /**
     * 從民國年月日時分秒轉換成西元年月日 如:102/10/11 21:10:03轉成20131011
     *
     * @param usDate 格式為yyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyyy/MM/dd
     */
    public static String getTwDateByYYYYMMdd(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR) + 1911;
            twDateString = year + addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2) + addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    /**
     * 從民國年月日時分秒轉換成西元年月日 如:102/10/11 21:10:03轉成2013
     *
     * @param usDate 格式為yyy-MM-dd HH:mm:ss
     * @return 回傳格式為yyyy
     */
    public static String getTwDateByYYYY(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR) + 1911;
            twDateString = String.valueOf(year);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    public static String getTwDateByWeek(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
        int dayOfWeek = 0;
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    twDateString = "日";
                    break;
                case Calendar.MONDAY:
                    twDateString = "一";
                    break;
                case Calendar.TUESDAY:
                    twDateString = "二";
                    break;
                case Calendar.WEDNESDAY:
                    twDateString = "三";
                    break;
                case Calendar.THURSDAY:
                    twDateString = "四";
                    break;
                case Calendar.FRIDAY:
                    twDateString = "五";
                    break;
                case Calendar.SATURDAY:
                    twDateString = "六";
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    /**
     * 從民國年月日時分秒轉換成西元年月日 如:102/10/11 21:10:03轉成10
     *
     * @param usDate 格式為yyy-MM-dd HH:mm:ss
     * @return 回傳格式為MM
     */
    public static String getTwDateByMM(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            twDateString = addPrefixZero(cal.get(Calendar.MONTH) + 1 + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    public static String getTwDateByDD(String usDate) {
        if (nvl(usDate).equals("")) {
            return "";
        }
        String twDateString = "";
		// SimpleDateFormat sdf = new SimpleDateFormat();
        // sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // Date date= sdf.parse(usDateTime);
            Date date = tryParse(usDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            twDateString = addPrefixZero(cal.get(Calendar.DAY_OF_MONTH) + "", 2);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }

        return twDateString;
    }

    /*
     * 從民國年轉換成西元年
     */
    public static String getUsDateTime(String TwDateTime) {
        if (nvl(TwDateTime).equals("")) {
            return "";
        }
        String[] TwDateTimeList = new String[3];
        if (TwDateTime.indexOf("/") != -1) {
            TwDateTimeList = TwDateTime.split("/");
        } else if (TwDateTime.indexOf("-") != -1) {
            TwDateTimeList = TwDateTime.split("-");
        }
        StringBuilder usYear = new StringBuilder();
        usYear.append(Integer.parseInt(TwDateTimeList[0]) + 1911);
        String USDate = usYear.toString() + TwDateTime.substring(3);
        USDate = USDate.replaceAll("-", "/");
        return USDate;
    }

    /*
     * 從西元年轉換成民國年 回傳格式 DateTimeType.Long : yyyy/MM/dd HH:mm:ss
     * DateTimeType.Short : yyyy/MM/dd DateLocaleType.US : 西元格式
     * DateLocaleType.TW : 民國格式
     */
    public static String getFormatDateTime(Date sourceDate, DateTimeType type, DateLocaleType locale) throws ParseException {
        String handleDateStr = "";
        String resultDateStr = "";
        if (type == DateTimeType.Long) {
            handleDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(sourceDate);
            ;
        } else if (type == DateTimeType.Short) {
            handleDateStr = new SimpleDateFormat("yyyy/MM/dd").format(sourceDate);
            ;
        }

        if (locale == DateLocaleType.US) {
            resultDateStr = getUsDateTime(handleDateStr);
        } else if (locale == DateLocaleType.TW) {
            resultDateStr = getTwDateTime(handleDateStr);
        }
        return resultDateStr;
    }

	// 下面保留日期運算功能

    /*
     * 
     */
    public void SysDate(String date) {
        setDate(date);
    }

    /*
     * 
     */
    public void SysDate(int year, int month, int day) {
        setDate(year, month, day);
    }

    /*
     * 
     */
    public void setDate(String date) {
        try {
            DateFormat df = DateFormat.getDateInstance();
            df.setCalendar(calendar);
            df.parse(date);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
    }

    /*
     * 
     */
    public void setDate(int year, int month, int day) {
        try {
            calendar.clear();
            calendar.set(year, month - 1, day);
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
    }

    /*
     * 
     */
    public void add(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String retVal = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        return retVal;
    }

    /**
     * 取得系統現在的日期時間，格式為:yyyy/MM/dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        // 目前時間
        java.util.Date now = new java.util.Date();
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }
    
    /**
     * 取得系統現在的日期時間，格式為:yyyy/MM/dd HH:mm
     *
     * @return
     */
    public static String getDateTimeHHmm() {
        // 目前時間
        java.util.Date now = new java.util.Date();
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }
    /**
     * 取得系統現在的日期，格式為:yyyyMMdd
     *
     * @return
     */
    
    public static String getTodayDate() {
        // 目前時間
        java.util.Date now = new java.util.Date();
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }

    /**
     * 取得系統現在的日期時間，格式為:yyyyMMddHHmmss,給SybaseDB使用
     *
     * @return
     */
    public static String getSystemTimeDB() {
        // 目前時間
        java.util.Date now = new java.util.Date();
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }

    /**
     * 取得yyyy/MM/dd格式
     *
     * @param now
     * @return
     */
    public static String get8DateFormat(Date now) {
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }

    /**
     * 取得yyyy-MM-dd格式
     *
     * @param now
     * @return
     */
    public static String get8DateFormatDB(Date now) {
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }

    /**
     * 取得8碼的DB格式(yyyyMMdd)
     *
     * @param date 年份為民國年 目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd", "yyy/MM/dd
     * HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMMdd
     */
    public static String get8UsDateFormatDB(String date) {
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 進行轉換
        String dateString = sdf.format(d);
        return dateString;
    }

    /**
     * 取得6碼的DB格式(yyyyMMdd)
     *
     * @param date 年份為民國年 目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd", "yyy/MM/dd
     * HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMM
     */
    public static String get6UsDateFormatDB(String date) {
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        // 進行轉換
        String dateString = sdf.format(d);
        return dateString;
    }

    /**
     * 取得7碼的民國年格式(yyy/MM/dd)
     *
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd)
     */
    public static String get7TwDateFormat(String date) {
        Date d = DateUtil.tryParse(date);
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // 進行轉換
        String dateString = sdf.format(d);
        dateString = DateUtil.getTwDateTime(dateString);
        dateString = dateString.substring(0, 9);// 去掉後面的時間
        return dateString;
    }

    /**
     * 取得今日日期
     *
     * @return 民國年格式(yyyMMdd) ex:1060907
     */
    public static String getRocDate() {
        Date d = Calendar.getInstance().getTime();
        int rocYear = Calendar.getInstance().get(Calendar.YEAR) - 1911;
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        // 進行轉換
        String dateString = sdf.format(d);
        return rocYear + dateString;
    }

    /**
     * 取得今日前後幾天/月/年之日期
     *
     * @param type  ex:Calendar.YEAR
     * @param diff  ex:-2 =>兩年前
     * @return 民國年格式(yyyMMdd) ex:1060907 / 0990101
     */
    public static String getRocDate(int type, int diff) {
        Calendar c = Calendar.getInstance();
        c.add(type, diff);
        Date d = c.getTime();
        int rocYear = c.get(Calendar.YEAR) - 1911;
        String rocYearStr = String.valueOf(rocYear);
        if(rocYearStr.length()==2){
            rocYearStr = "0" + rocYear;
        }
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        // 進行轉換
        String dateString = sdf.format(d);
        return rocYearStr + dateString;
    }
    
    /**
     * 取得今日前後幾天/月/年之日期
     *
     * @param type  ex:Calendar.YEAR
     * @param diff  ex:-2 =>兩年前
     * @return yyyy-MM-dd hh:mm:ss
     */
    public static String getDateTime(int type, int diff) {
        Calendar c = Calendar.getInstance();
        c.add(type, diff);
        Date d = c.getTime();
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        
        return sdf.format(d);        
    }

    public static String get7TwDateFormatByYYYY(String date) {
        Date d = DateUtil.tryParse(date);
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // 進行轉換
        String dateString = sdf.format(d);
        dateString = DateUtil.getTwDateTime(dateString);
        dateString = dateString.substring(0, 9);// 去掉後面的時間
        return dateString;
    }

    /**
     * 取得7碼的民國年格式(yyy/MM)
     *
     * @param date DB的格式為yyyyMM，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM)
     */
    public static String get5TwDateFormat(String date) {
        Date d = DateUtil.tryParse(date);
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        // 進行轉換
        String dateString = sdf.format(d);
        dateString = DateUtil.getTwDateTime(dateString);
        dateString = dateString.substring(0, 6);// 去掉後面的日期跟時間
        return dateString;
    }

    /**
     * 將民國年格式轉換為西元年，並且回傳Date物件
     *
     * @param date 為民國年格式(yyy/MM/dd)
     * @return Date物件
     */
    public static Date getDateObject(String date) {
        Date tmp = null;
        if (date.length() > 3) {
            int year = Integer.parseInt(date.substring(0, 3)) + 1911;
            date = year + date.substring(3);
        }
        tmp = DateUtil.tryParse(date);
        return tmp;
    }

    /**
     * 民國轉西元 取得14碼的DB格式(yyyyMMddHHmmss)
     *
     * @param date 年份為民國年 目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd", "yyy/MM/dd
     * HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMMddHHmmss
     */
    public static String get14UsDateFormatDB(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        if (d != null) {
			// Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            // 進行轉換
            dateString = sdf.format(d);
        }
        return dateString;
    }

    public static String getDateFormatDB(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        if (d != null) {
			// Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            // 進行轉換
            dateString = sdf.format(d);
        }
        return dateString;
    }

    /**
     * 民國轉西元 取得14碼的西元年格式(yyyy/MM/dd HH:mm:ss)
     *
     * @param date 年份為民國年 目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd", "yyy/MM/dd
     * HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyy/MM/dd HH:mm:ss
     */
    public static String get14UsDateFormat(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        if (d != null) {
			// Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 進行轉換
            dateString = sdf.format(d);
        }
        return dateString;
    }

    /**
     * 取得13碼的民國 DB格式(yyyyMMddHHmmss)
     *
     * @param date 年份為民國年 目前可接受格式"yyy-MM-dd HH:mm:ss", "yyy-MM-dd", "yyy/MM/dd
     * HH:mm:ss", "yyy/MM/dd"
     * @return 西元年yyyyMMddHHmmss
     */
    public static String get13TwDateFormatDB(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
        if (d != null) {
			// Date d = DateUtil.tryParse(DateUtil.getUsDateTime(date));
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			// 進行轉換

            dateString = date.substring(0, 3) + sdf.format(d);
        }
        return dateString;
    }

    /**
     * 取得13碼的民國年格式(yyy/MM/dd HH:mm:ss)
     *
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd HH:mm:ss)
     */
    public static String get13TwDateFormat(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(date);
        if (d != null) {
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 進行轉換
            dateString = sdf.format(d);
            dateString = DateUtil.getTwDateTime(dateString);
        }
        return dateString;
    }

    /**
     * 取得14碼的西元年格式(yyyy/MM/dd HH:mm:ss)
     *
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 西元年格式(yyyy/MM/dd HH:mm:ss)
     */
    public static String get14DateFormat(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(date);
        if (d != null) {
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 進行轉換
            dateString = sdf.format(d);
            String year = dateString.substring(0, 4);
            String day = dateString.substring(4, dateString.length());
            int y = Integer.valueOf(year) - 1911;
            dateString = String.valueOf(y) + day;
        }
        return dateString;
    }

    /**
     * 取得12碼的西元年格式(yyyy/MM/dd HH:mm)
     *
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 西元年格式(yyyy/MM/dd HH:mm:ss)
     */
    public static String get12DateFormat(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(date);
        if (d != null) {
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            // 進行轉換
            dateString = sdf.format(d);
        }
        return dateString;
    }

    /**
     * 取得11碼的民國年格式(yyy/MM/dd HH:mm)
     *
     * @param date DB的格式為yyyyMMddHHmmss，直接將DB的欄位傳入即可
     * @return 民國年格式(yyy/MM/dd HH:mm)
     */
    public static String get11TwDateFormat(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(date);
        if (d != null) {
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 進行轉換
            dateString = sdf.format(d);
            dateString = DateUtil.getTwDateTime(dateString);
            if (nvl(dateString).length() > 0) {
                dateString = dateString.substring(0, dateString.length() - 3);
            }
        }

        return dateString;
    }

    /**
     * 取得yyyy/MM/dd HH:mm:ss格式
     *
     * @param now
     * @return
     */
    public static String get14DateFormat(Date now) {
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 進行轉換
        String dateString = sdf.format(now);
        return dateString;
    }

    /**
     * 取得yyyyMMddHHmmss格式
     *
     * @param now
     * @return
     */
    public static String get14DateFormatDB(Date now) {
        // 設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // 進行轉換
        String dateString = sdf.format(now);

        return dateString;
    }

    /**
     * 將來自DB的各個欄位 西元年月日時分 轉成民國年 YYY/MM/DD HH:MM
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @return
     */
    public static String to11TwDateTime(String year, String month, String day, String hour, String min) {
        String tmp = "";
        year = nvl(year);
        month = nvl(month);
        day = nvl(day);
        hour = nvl(hour);
        min = nvl(min);
        if (year.length() != 0 && month.length() != 0 && day.length() != 0 && hour.length() != 0 && min.length() != 0) {
            int twYear;
            twYear = Integer.parseInt(year) - 1911;
            tmp = twYear + "/" + month + "/" + day + " " + hour + ":" + min;
        }
        return tmp;
    }

    /**
     * 將來自DB的各個欄位 西元年月日 轉成民國年 YYY/MM/DD
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String to7TwDateTime(String year, String month, String day) {
        String tmp = "";
        year = nvl(year);
        month = nvl(month);
        day = nvl(day);
        if (year.length() != 0 && month.length() != 0 && day.length() != 0) {
            int twYear;
            twYear = Integer.parseInt(year) - 1911;
            tmp = twYear + "/" + month + "/" + day;
        }
        return tmp;
    }

    /**
     * 將來自操作畫面的各個欄位民國年 YYY/MM/DD 轉成西元年月日陣列
     *
     * @param date YYY/MM/DD
     * @return 西元年月日陣列，0-西元年，1-月，2-日
     */
    public static String[] split8UsDateTime(String date) {
        String tmp[] = new String[]{"", "", ""};
        date = nvl(date);
        if (date.length() != 0) {
            int twYear = Integer.parseInt(date.substring(0, date.indexOf("/"))) + 1911;
            String month = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
            String day = date.substring(date.lastIndexOf("/") + 1);
            tmp[0] = String.valueOf(twYear);
            tmp[1] = month;
            tmp[2] = day;
        }
        return tmp;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyyMMdd(HH:mm)
     *
     * @param date tryParse所能接受的格式
     * @return yyyMMdd(HH:mm)
     */
    public static String getReportDateTime(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MMdd(HH:mm)");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyyMMddHHmm
     *
     * @param date tryParse所能接受的格式
     * @return yyyMMddHHmm
     */
    public static String getReportDateTime11(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy/MM/dd HH:mm
     *
     * @param date tryParse所能接受的格式
     * @return yyyMMdd(HH:mm)
     */
    public static String getReportDateTime5(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("/MM/dd HH:mm");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH:mm
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 15:53
     */
    public static String getReportDateTime2(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH時mm分
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 15時53分
     */
    public static String getReportDateTime3(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH時mm分");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH時
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 15時
     */
    public static String getReportDateTime4(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH時");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日 HH時mm分
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 HH:mm 如:103年04月10日 15時53分
     */
    public static String getReportDateTime6(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("M月d日HH時mm分");
                // 進行轉換
                dateString = "中華民國" + addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyyMMdd
     *
     * @param date tryParse所能接受的格式
     * @return yyyMMdd
     */
    public static String getReportDate(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyyMMdd
     *
     * @param date tryParse所能接受的格式
     * @return yyyMM
     */
    public static String getReport5Date(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + sdf.format(d);
            }
        }
        return dateString;
    }

    public static String getBirthdayTime(String date) {
        String dateTime = "";
        Integer year = null;
        String tempyear = "";
        tempyear = date.substring(0, 3);
        year = Integer.valueOf(tempyear) + 1911;
        String day = date.substring(3, date.length());
        String full = String.valueOf(year) + day;
        full = full.substring(0, 8);
        return full;
    }

    public static String convertBirthdayTime(String type, String date) {
        String newTime = "";
        String year = "";
        String day = "";
        year = date.substring(0, 4);
        day = date.substring(4, date.length());
        Integer newYear = Integer.valueOf(year) - 1911;
        if (type.equals("0") || type.equals("1")) {
            newTime = "0" + String.valueOf(newYear) + day;
        } else if (type.equals("2")) {
            newTime = String.valueOf(newYear) + day;
        }
        return newTime;
    }

    public static String previewConvertBirthdayTime(String date) {
        String newTime = "";
        String year = "";
        String month = "";
        String day = "";
        year = date.substring(0, 4);
        month = date.substring(4, 6);
        day = date.substring(6, 8);
        Integer newYear = Integer.valueOf(year) - 1911;
        String tempYear = String.valueOf(newYear);
        if (tempYear.length() > 2) {
            newTime = tempYear + "/" + month + "/" + day;
        } else {
            newTime = "0" + tempYear + "/" + month + "/" + day;
        }
        return newTime;
    }

    public static String convertBir(String date) {
        String newBir = "";
        String[] tempBir = date.split("/");
        String tempYear = tempBir[0];
        Integer Year = Integer.valueOf(tempYear) + 1911;
        String tempMonth = tempBir[1];
        String tempDay = tempBir[2];
        return Year + tempMonth + tempDay;
    }

    public static String convertBirthday(String date) {
        String newTime = "";
        String year = "";
        String month = "";
        String day = "";
        String tempYear = "";
        year = date.substring(0, 4);
        month = date.substring(4, 6);
        day = date.substring(6, date.length());
        Integer newYear = Integer.valueOf(year) - 1911;
        if (newYear.toString().length() == 2) {
            tempYear = "0" + String.valueOf(newYear);
        } else if (newYear.toString().length() == 3) {
            tempYear = String.valueOf(newYear);
        }
        newTime = tempYear + "/" + month + "/" + day;
        return newTime;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy年MM月dd日
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 如:103年04月10日
     */
    public static String getReportDate2(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 使用tryParse將傳入參數date轉乘Date物件，再將日期格式化成報表所需要的格式yyy/MM/dd
     *
     * @param date tryParse所能接受的格式
     * @return yyy年MM月dd日 如:103/04/10/
     */
    public static String getReportDate3(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "/" + sdf.format(d);
            }
        }
        return dateString;
    }

    public static String addPrefixZero(String value, int valueLength) {
        String tmp = value;
        if (value != null && value.length() < valueLength) {
            int diff = valueLength - value.length();
            for (int i = 0; i < diff; i++) {
                tmp = "0" + tmp;
            }
        }
        return tmp;
    }

    /**
     * 計算兩個日期差幾天,時,分,秒
     *
     * @param pastDate 較早的日期
     * @param futureDate 較晚的日期
     *
     * @return long[4] 0:days 1:hours 2:mins 3:secs
     */
    public static long[] diffDate(Date pastDate, Date futureDate) {
        long[] result = {0, 0, 0, 0}; //days hours mins secs
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(pastDate);
        calendar2.setTime(futureDate);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        result[0] = diffDays;
        result[1] = diffHours;
        result[2] = diffMinutes;
        result[3] = diffSeconds;

        return result;
    }

    public static void main(String[] args) {
//		System.out.println(DateUtil.get14UsDateFormatDB("103/6/20"));
//		System.out.println(DateUtil.get14UsDateFormatDB("103/6/20 14:33"));
//		System.out.println(DateUtil.get14UsDateFormatDB("103/6/20 14:33:12"));
//		System.out.println(DateUtil.get13TwDateFormat("20140620143312"));
        System.out.println(DateUtil.get11TwDateFormat("20140620143312"));
//		System.out.println(DateUtil.get7TwDateFormat("20140620143312"));
//		System.out.println(getDateObject("103/05/01"));
//		System.out.println(getReportDateTime("20030721072114"));
//		System.out.println(getReportDateTime2("20030721072114"));
//		System.out.println(getReportDate("20140721072114"));
//		System.out.println(getReportDate2("20140721072114"));
//        System.out.println(DateUtil.tryParse("770424"));
//        System.out.println(getUsDateTime("103/06/20 15:15:00"));
        System.out.println(get8UsDateFormatDB("103/06/20 15:15:00"));
        System.out.println(getTwDateByYYYY("103/06/20 15:15:00"));
        System.out.println(getTwDateByMM("103/06/20 15:15:00"));
        System.out.println(getTwDateByDD("103/06/20 15:15:00"));
        System.out.println(getTwDateByYYYYMMdd("074/06/20 15:15:00"));
        System.out.println(getDateFormatDB("105/08/18:14:32:03"));
        System.out.println(getTwDateByWeek(getUsDateTime("105/08/10 09:40:00")));
        System.out.println(get13TwDateFormat("20160811094000"));
        System.out.println(getBirthdayTime("0770424000000"));
        System.out.println(convertBirthdayTime("1", "19880424"));
        System.out.println(convertBirthday("19880424"));
        System.out.println(getReportDateTime_3("20160424104500"));
    }

    /**
     * 取得幾天前的日期
     *
     * @param StandardDay 基準日期
     * @param DelDay 扣掉的日期
     *
     * @return string ex:20160830
     */
    public static String getday(String StandardDay, int DelDay) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(StandardDay));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - DelDay);
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            log.error(ExceptionUtil.toString(e));
        }
        return "";
    }

    /**
     * 傳105/09/10－>105年9月10日
     *
     * @param date
     * @return
     */
    public static String getNowTime() {
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(now);
        String[] dateString = date.split("/");
        Integer tempYear = Integer.valueOf(dateString[0]) - 1911;
        String Year = String.valueOf(tempYear);
        String month = dateString[1];
        String day = dateString[2];
        String fullTime = Year + "年" + month + "月" + day + "日";
        return fullTime;
    }

    public static String getYearMMDD(String date) {
        String[] dateString = date.split("/");
        String year = dateString[0];
        String month = dateString[1];
        String day = dateString[2];
        String fullTime = Integer.valueOf(year) + "年" + Integer.valueOf(month) + "月" + Integer.valueOf(day) + "日";
        return fullTime;
    }

    public static String getYearMM(String date) {
        String[] dateString = date.split("/");
        String year = dateString[0];
        String month = dateString[1];
        String fullTime = Integer.valueOf(year) + "年" + Integer.valueOf(month) + "月";
        return fullTime;
    }

    /**
     * DB年月日轉民國年月日 FOR 報表 傳20160909－>105年9月09日
     *
     * @param date
     * @return
     */
    public static String getReportYearMMDD(String date) {
        String str = get11TwDateFormat(date);
        String[] dateString = str.split("/");
        String year = dateString[0];
        String month = dateString[1];
        String dayStr = dateString[2];
        String day = dayStr.split(" ")[0];
        String fullTime = Integer.valueOf(year) + "年" + Integer.valueOf(month) + "月" + Integer.valueOf(day) + "日";
        return fullTime;
    }

    public static String getReportDateTime_3(String date) {
        String dateString = "";
        if (nvl(date).length() > 0) {
            Date d = tryParse(date);
            if (d != null) {
                int twYear = d.getYear() - 11;
                // 設定日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH時mm分");
                // 進行轉換
                dateString = addPrefixZero(twYear + "", 3) + "年" + sdf.format(d);
            }
        }
        return dateString;
    }

    /**
     * 依據傳入的西元年字串取得轉換後的民國年；只有1912年(含)以後的西元年可轉換為民國年。 回傳的民國年為3碼靠右左補零。如[2005] ==>
     * [094]
     *
     * @param eYear 欲轉換的西元年字串
     * @return 轉換後的民國年；如果轉換的西元年字串無法進行轉換，則傳回空字串
     *
     * @see #getYearC2E(String)
     */
    public static String getYearE2C(String eYear) {
        if (null == eYear) {
            return "";
        }

        String tmpYear = eYear.trim();
        java.util.regex.Pattern pat = java.util.regex.Pattern.compile("^[0-9]{4}$");
        java.util.regex.Matcher mat = pat.matcher(tmpYear);
        if (mat.find()) {
            int nYear = (Integer.valueOf(tmpYear).intValue() - 1911);
            if (nYear < 1) {
                return "";
            }
            return String.format("%1$03d", new Object[]{Integer.valueOf(nYear)});
        } else {
            return "";
        }
    }

    /**
     * 依據傳入的民國年字串取得轉換後的西元年；只有民國元年(含)以後的民國年可轉換為西元年。 回傳的西元年為4碼靠右左補零。如[94] ==>
     * [2005]
     *
     * @param cYear 欲轉換的民國年字串
     * @return 轉換後的西元年；如果轉換的民國年字串無法進行轉換，則傳回空字串
     *
     * @see #getYearE2C(String)
     */
    public static String getYearC2E(String cYear) {
        if (null == cYear) {
            return "";
        }

        String tmpYear = cYear.trim();
        java.util.regex.Pattern pat = java.util.regex.Pattern.compile("^[0-9]{1,3}$");
        java.util.regex.Matcher mat = pat.matcher(tmpYear);
        if (mat.find()) {
            int nYear = (Integer.valueOf(tmpYear).intValue() + 1911);
            if (nYear < 1912) {
                return "";
            }
            return String.format("%1$04d", new Object[]{Integer.valueOf(nYear)});
        } else {
            return "";
        }
    }

    public static String getDateFormatForOldSys(String date) {
        String dateString = "";
        Date d = DateUtil.tryParse(date);
        if (d != null) {
            // 設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 進行轉換
            dateString = sdf.format(d);
            /*
             String year = dateString.substring(0, 4);
             String day = dateString.substring(4, dateString.length());
             int y = Integer.valueOf(year) - 1911;
             dateString = String.valueOf(y) + day;
             */
        }
        return dateString;
    }

    // 103/09/09 to 1030909

    public static String JStoDate(String date) {
        if (date != null && !date.equals("")) {
            String format = date.replace("/", "");
            String newformat = format.replace(":", "");
            String finalformat = newformat.replace(" ", "");
            return finalformat;
        } else {
            return date;
        }
    }

    //DatetoDBformat 103/08/20  to 2014-08-20

    public static String DatetoDBformat(String date) {
        if(date==null || date.length()<3){
            log.error("date error:" + date);
        }
        String format = date.replace("/", "-");
        format = String.valueOf(Integer.parseInt(format.substring(0, 3)) + 1911) + format.substring(3);
        return format;
    }
    
    // 1030909 to 103/09/09

    public static String DatetoJS(String date) {
       String newdate;
       newdate=date.substring(0,3)+"/"+date.substring(3,5)+"/"+date.substring(5,7);
        return newdate;
    }
}
