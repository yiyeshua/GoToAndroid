/*
 * Copyright (C) 2014 Togic Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pili.basemodel.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateTimeUtil {

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMMSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDD = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> YYYYMMDDHHMM = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        }
    };
    private static final Pattern UniversalDatePattern = Pattern.compile(
            "([0-9]{4})-([0-9]{2})-([0-9]{2})[\\s]+([0-9]{2}):([0-9]{2}):([0-9]{2})"
    );

    public static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
    private static final Calendar GMT_CALENDAR = Calendar.getInstance(GMT_ZONE);

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] DATE_PATTERNS = {
        "EEE, dd MMM yyyy HH:mm:ss Z", // RFC 822, updated by RFC 1123
        "EEEE, dd-MMM-yy HH:mm:ss Z", // RFC 850, obsoleted by RFC 1036
        "EEE MMM d HH:mm:ss yyyy" // ANSI C's asctime() format
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate string date that's type like YYYY-MM-DD HH:mm:ss
     * @return {@link Date}
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, YYYYMMDDHHMMSS.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat formatter) {
        try {
            return formatter.parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            Log.d("oschina", e.getMessage());
        }
        return defValue;
    }
    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }
    /**
     * YYYY-MM-DD HH:mm:ss格式的时间字符串转换为{@link Calendar}类型
     *
     * @param str YYYY-MM-DD HH:mm:ss格式字符串
     * @return {@link Calendar}
     */
    public static Calendar parseCalendar(String str) {
        Matcher matcher = UniversalDatePattern.matcher(str);
        Calendar calendar = Calendar.getInstance();
        if (!matcher.find()) return null;
        calendar.set(
                matcher.group(1) == null ? 0 : toInt(matcher.group(1)),
                matcher.group(2) == null ? 0 : toInt(matcher.group(2)) - 1,
                matcher.group(3) == null ? 0 : toInt(matcher.group(3)),
                matcher.group(4) == null ? 0 : toInt(matcher.group(4)),
                matcher.group(5) == null ? 0 : toInt(matcher.group(5)),
                matcher.group(6) == null ? 0 : toInt(matcher.group(6))
        );
        return calendar;
    }
    public static String getCurrentTimeStr() {
        return YYYYMMDDHHMMSS.get().format(new Date());
    }
    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = YYYYMMDD.get().format(today);
            String timeDate = YYYYMMDD.get().format(time);
            if (nowDate.equals(timeDate)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 是否是相同的一天
     *
     * @param sdate1 sdate1
     * @param sdate2 sdate2
     * @return
     */
    public static boolean isSameDay(String sdate1, String sdate2) {
        if (TextUtils.isEmpty(sdate1) || TextUtils.isEmpty(sdate2)) {
            return false;
        }
        Date date1 = toDate(sdate1);
        Date date2 = toDate(sdate2);
        if (date1 != null && date2 != null) {
            String d1 = YYYYMMDD.get().format(date1);
            String d2 = YYYYMMDD.get().format(date2);
            if (d1.equals(d2)) {
                return true;
            }
        }
        return false;
    }
    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @param date1
     * @param date2
     * @return
     * @author 火蚁 2015-2-9 下午4:50:06
     */
    public static long calDateDifferent(String date1, String date2) {
        try {
            Date d1 = YYYYMMDDHHMMSS.get().parse(date1);
            Date d2 = YYYYMMDDHHMMSS.get().parse(date2);
            // 毫秒ms
            long diff = d2.getTime() - d1.getTime();
            return diff / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @param calendar {@link Calendar}
     * @return 星期n
     */
    public static String formatWeek(Calendar calendar) {
        if (calendar == null) return "星期?";
        return new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"}[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String format(long time) {
        time = time / 1000;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
    }

    public static String formatTimeOffset(long time) {
        time = time / 1000;
        int day = (int) (time / 86400);
        int hour = (int) ((time % 86400) / 3600);
        int min = (int) ((time % 3600) / 60);
        StringBuilder builder = new StringBuilder();
        if (day > 0) {
            builder.append(day).append("天");
            builder.append(hour).append("时");
        } else {
            if (hour > 0) {
                builder.append(hour).append("时");
            }
        }
        min = min > 0 ? min : 1;
        builder.append(min).append("分");
        return builder.toString();
    }

    public static String formatDate(long time) {
        return formatDate(time, DEFAULT_DATE_PATTERN);
    }

    public static String formatDate(long time, String pattern) {
        SimpleDateFormat formatter = null;
        if (StringUtil.isEmptyString(pattern)) {
            formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.CHINA);
        } else {
            formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        }

        return formatter.format(new Date(time));
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat formatter = null;
        if (StringUtil.isEmptyString(pattern)) {
            formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.CHINA);
        } else {
            formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        }
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public static final String formatFileModifyDate(long time) {
        final Calendar c = GMT_CALENDAR;
        c.setTimeInMillis(time);
        return String.format(Locale.US, "%ta, %<td %<tb %<tY %<tT GMT", c);
    }

    public static final Date parseDate(String time) {
        for (String pattern : DATE_PATTERNS) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
                df.setTimeZone(GMT_ZONE);
                return df.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Date(0);
    }

    public static Date playTimeToDate(String playTime) {
        String[] temp = playTime.split(":");
        int hour = Integer.valueOf(temp[0]);
        int minute = Integer.valueOf(temp[1]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public static String getTimeFormatString(int duration) {
        int sumSecond = duration % 60;
        int sumMinute = (duration / 60) % 60;
        int sumHour = ((duration / 60) / 60);

        String formatString = String.format(Locale.CHINA, "%02d:%02d:%02d", sumHour,
                sumMinute, sumSecond);
        return formatString;

    }

    public static int getHour(int time) {
        return (time / 60) / 60;
    }

    public static int getMinute(int time) {
        return (time / 60) % 60;
    }

    public static int getSecond(int time) {
        return time % 60;
    }

    public static String getCalendar(long timer) {
        SimpleDateFormat format = new SimpleDateFormat("M月d日", Locale.CHINA);
        return format.format(new Date(timer));
    }

    public static String getCalendarTime(long timer) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return format.format(new Date(timer));
    }

    public static String getCalendar(long timer, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.CHINA);
        return format.format(new Date(timer));
    }
    
    /**
     * 根据时间戳获取时间字符串
     * 
     * @param timeStamp
     * @return
     */
    public static String getDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
                Locale.CHINA);
        return sdf.format(timeStamp);
    }

    /**
     * 获取当前时间字符串
     * 
     * @return
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
                Locale.CHINA);
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 通过时间字符串得到时间戳
     * 
     * @param strDate
     * @return
     */
    public static String getTimeStamp(String strDate) {

        // 注意format的格式要与日期String的格式相匹配
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date date = df.parse(strDate);
            return String.valueOf(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
