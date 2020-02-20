package ckc.android.develophelp.lib.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static final String TAG = "TimeUtil";
    public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat FORMAT_LOG_NAME_FILE = new SimpleDateFormat("yyyyMMdd");
    private static IApp sApp = null;

    /**
     * 初始化
     */
    public static void init(IApp app) {
        sApp = app;
    }

    public static String getLogNameTime() {
        return FORMAT_LOG_NAME_FILE.format(new Date(getSystemTime()));
    }

    public static String getDefaultDate(long timeMilli) {
        return DEFAULT_DATE_FORMAT.format(new Date(timeMilli));
    }

    public static String getDefaultDate() {
        return DEFAULT_DATE_FORMAT.format(new Date(getSystemTime()));
    }

    public static String getDefaultTime(long timeMilli) {
        return DEFAULT_TIME_FORMAT.format(new Date(timeMilli));
    }

    public static String getDefaultTime() {
        return DEFAULT_TIME_FORMAT.format(new Date(getSystemTime()));
    }

    public static String getDefaultDateTime() {
        return DEFAULT_FORMAT.format(new Date(getSystemTime()));
    }

    /**
     * 获取DateTime类型格式的当前时间
     */
    public static String getMysqlDateTimeFormat() {
        return DEFAULT_FORMAT.format(new Date(getSystemTime()));
    }

    /**
     * 获取DateTime类型格式的当前时间
     */
    public static String getMysqlDateTimeFormat(long timeMilli) {
        return DEFAULT_FORMAT.format(new Date(timeMilli));
    }

    /**
     * 获取一天开始或结束的DateTime类型格式的时间
     */
    public static String getDayBeginOrEndMysqlDateTimeFormat(String date, int flag) {
        if (date == null) return null;
        if (flag == FLAG_DAY_BEGIN) {
            return date + " 00:00:00";
        } else {
            return date + " 23:59:59";
        }
    }

    /**
     * 获取一天开始或结束的DateTime类型格式的时间
     */
    public static String getDayBeginOrEndMysqlDateTimeFormat(long timeMilli, int flag) {
        return DEFAULT_FORMAT.format(getDateBeginOrEnd(new Date(timeMilli), flag));
    }

    /**
     * 获取一天开始或结束的DateTime类型格式的时间
     */
    public static String getDayBeginOrEndMysqlDateTimeFormat(int flag) {
        return DEFAULT_FORMAT.format(getDateBeginOrEnd(new Date(getSystemTime()), flag));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getRefreshShowTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(getSystemTime()));
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getDayOfWeek(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    public static long getSystemTime() {
        if (sApp != null) {
            return sApp.onGetSystemTime();
        } else {
            return System.currentTimeMillis();
        }
    }

    public static long getDayDurationMilli() {
        return 24 * 60 * 60 * 1000;
    }

    /**
     * <p>
     * 将录像时长转化为列表显示模式，比如"01:01:30"
     * </p>
     *
     * @param diffSeconds
     * @return
     * @author hanlifeng 2014-6-16 下午4:01:04
     */
    public static String convToUIDuration(long diffSeconds) {
        long min = diffSeconds / 60;
        String minStr = "";
        long sec = diffSeconds % 60;
        String secStr = "";
        String hStr = "";

        if (min >= 59) {
            long hour = min / 60;
            long temp = min % 60;
            if (hour < 10) {
                if (hour > 0) {
                    hStr = "0" + hour;
                } else {
                    hStr = "00";
                }
            } else {
                hStr = "" + hour;
            }
            if (temp < 10) {
                if (temp > 0) {
                    minStr = "0" + temp;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + temp;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        } else {
            hStr = "00";
            if (min < 10) {
                if (min > 0) {
                    minStr = "0" + min;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + min;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        }
    }

    /**
     * <p>
     * 将时间转化为月和日格式，比如 6月17号
     * </p>
     *
     * @param queryDate
     * @return
     * @author hanlifeng 2014-6-17 下午1:34:23
     */
    public static String converToMonthAndDay(Date queryDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(queryDate);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        StringBuffer buffer = new StringBuffer();
        return buffer.append(month).append("月").append(day).append("日").toString();
    }

    private static SimpleDateFormat mFormatMDHM = new SimpleDateFormat("MM月dd日 HH:mm");

    public static String getMDHM(long ms) {
        return mFormatMDHM.format(new Date(ms));
    }

    public static String getYMDHMS(long ms) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(new Date(ms));
    }

    public static String getHMS(long ms) {
        return DEFAULT_TIME_FORMAT.format(new Date(ms));
    }


    public static final int FLAG_DAY_BEGIN = 0;
    public static final int FLAG_DAY_END = 1;

    public static Calendar getCalendarBeginOrEnd(Date date, int flag) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(getDateBeginOrEnd(date, flag));
        return instance;
    }

    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date getDateBeginOrEnd(Date date, int flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

        if (flag == 0) {
            return cal.getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
        }
        return cal.getTime();
    }

    /**
     * 凌晨
     *
     * @param date
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    public static Date getConvertDate(Date date, int flag) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

        if (flag == 0) {
            return cal.getTime();
        } else if (flag == 1) {
            //凌晨23:59:59
            cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
        }
        return cal.getTime();
    }

    private static SimpleDateFormat mFormatY = new SimpleDateFormat("yyyy");

    public static String getYear(long ms) {
        return mFormatY.format(new Date(ms));
    }

    public static boolean isSelectedDateValid(Date selC) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(selC);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTime(lastSelC);
        return isSelectedDateValid(calendar1);
    }

    public static boolean isSelectedDateValid(Calendar selC) {
        Calendar nowC = Calendar.getInstance();
        nowC.setTime(new Date());
        int nowY = nowC.get(Calendar.YEAR);
        int nowM = nowC.get(Calendar.MONTH) + 1;
        int nowD = nowC.get(Calendar.DAY_OF_MONTH);
        int selY = selC.get(Calendar.YEAR);
        int selM = selC.get(Calendar.MONTH) + 1;
        int selD = selC.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "nowY=" + nowY + ",nowM=" + nowM + ",nowD=" + nowD + "\n"
                + ",selY=" + selY + ",selM=" + selM + ",selD=" + selD);
        if (selY < nowY) {
            return true;
//            return !isDateEqual(selC, lastSelC);
        } else if (selY == nowY) {
            if (selM < nowM) {
                return true;
//                return !isDateEqual(selC, lastSelC);
            } else if (selM == nowM) {
                if (selD <= nowD) {
                    return true;
//                    return !isDateEqual(selC, lastSelC);
                }
            }
        }
        return false;
    }

    public static boolean isDateEqual(Date selC, Date lastSelC) {
        if (lastSelC == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(selC);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(lastSelC);
        return isDateEqual(calendar1, calendar2);
    }

    public static boolean isDateEqual(Calendar selC, Calendar lastSelC) {
        if (lastSelC == null) {
            return false;
        }
        int nowY = lastSelC.get(Calendar.YEAR);
        int nowM = lastSelC.get(Calendar.MONTH) + 1;
        int nowD = lastSelC.get(Calendar.DAY_OF_MONTH);
        int selY = selC.get(Calendar.YEAR);
        int selM = selC.get(Calendar.MONTH) + 1;
        int selD = selC.get(Calendar.DAY_OF_MONTH);
        if (nowY == selY && nowM == selM && nowD == selD) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * String格式的date转化为Calendar对象
     */
    public static Calendar dateToCalendar(String date) {
        if (TextUtils.isEmpty(date)) {
            return Calendar.getInstance();
        }
        date = date.substring(0, 10);
        Calendar ret = Calendar.getInstance();
        String[] strings = date.split("-");
        ret.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1, Integer.parseInt(strings[2]));
        return ret;
    }

    public interface IApp {
        long onGetSystemTime();
    }
}
