package com.obg.bookmeeting.common;/*
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　佛主保佑 　┣┓
 * 　　　　┃　　永无BUG 　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┗┻┛　┗┻┛
 * Created by 华南 on 2019/10/30.
 */

import com.obg.bookmeeting.vo.MeetingRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Util {


    /**
     * 阿拉伯数字转汉字数字
     * @param str
     * @return
     */
    public static String toChinese(String str) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
        String result = "";
        int n = str.length();
        for (int i = 0; i < n; i++) {
            int num = str.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;
    }

    /**
     * 判断字符串是否是中文数字，是则返回true
     * @param s
     * @return
     */
    public static boolean checkChineseDigit(String s){
        char[] s1 = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '百', '千', '万'};
        int n = s.length();

        for (int i = 0; i < n; i++) {
            boolean flag = false;
            for (int j=0;j<s1.length; j++){
                if (s1[j] == s.charAt(i)){
                    flag = true;
                    break;
                }
            }
            if (flag == false){
                return false;
            }
        }
        return true;
    }

    /**
     * 汉字数字转阿拉伯数字
     * @param s
     * @return
     */
    public static int solve(String s) {
        int i = s.indexOf("万");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i+1));
            return l*10000 + r;
        }
        i = s.indexOf("千");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i+1));
            return l*1000 + r;
        }
        i = s.indexOf("百");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i+1));
            return l*100 + r;
        }
        i = s.indexOf("十");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            if (l == 0)
                l = 1;
            int r = solve(s.substring(i+1));
            return l*10 + r;
        }
        i = s.indexOf("零");
        if (i != -1) {
            int l = solve(s.substring(0, i));
            int r = solve(s.substring(i+1));
            return l + r;
        }
        i = 0;
        switch (s) {
            case "九":
                return 9;
            case "八":
                return 8;
            case "七":
                return 7;
            case "六":
                return 6;
            case "五":
                return 5;
            case "四":
                return 4;
            case "三":
                return 3;
            case "二":
                return 2;
            case "一":
                return 1;
        }
        return 0;
    }

    public static String getDate(String s){
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int WeekOfYear = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (DateConstant.today.equals(s)){
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.tomorrow.equals(s)){
            calendar.add(calendar.DATE,1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_tomorrow.equals(s)){
            calendar.add(calendar.DATE,2);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Mon.equals(s)){
            calendar.add(calendar.DATE,1-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Tues.equals(s)){
            calendar.add(calendar.DATE,2-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Wed.equals(s)){
            calendar.add(calendar.DATE,3-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Thur.equals(s)){
            calendar.add(calendar.DATE,4-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Fri.equals(s)){
            calendar.add(calendar.DATE,5-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Sat.equals(s)){
            calendar.add(calendar.DATE,6-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.Sun.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Mon.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Tues.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+2);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Wed.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+3);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Thur.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+4);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Fri.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+5);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Sat.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+6);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }else if (DateConstant.next_Sun.equals(s)){
            calendar.add(calendar.DATE,7-WeekOfYear+7);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
            date=calendar.getTime();
            String d = dateformat.format(date);
            return d;
        }
    return null;
    }

    /**
     * 将两个时间按照一定的规则转化成正确的会议时间，考虑24小时制
     * @param startTime
     * @param endTime
     * @param meetingRoom
     */
    public static void setMeetingTimes(String startTime, String endTime, MeetingRoom meetingRoom) {

        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String morningTime = "08:00:00";
        String d = dateformat.format(date);
        String d1 = d+" "+startTime;
        String d2 = d+" "+endTime;
        try {
            Date date1 = df.parse(d1);
            Date date2 = df.parse(d2);
            Date time8Line = df.parse(d + " " + morningTime);
            if (date1.getTime() < date2.getTime()){
                if (date1.getTime() >= time8Line.getTime()){
                    meetingRoom.setStartTime(df2.format(date1));
                    meetingRoom.setEndTime(df2.format(date2));
                }else {
                    meetingRoom.setStartTime(df2.format(date1.getTime()+12*60*60*1000));
                    meetingRoom.setEndTime(df2.format(date2.getTime()+12*60*60*1000));
                }
            }else if (date1.getTime() > date2.getTime()){
                meetingRoom.setStartTime(df2.format(date1));
                meetingRoom.setEndTime(df2.format(date2.getTime()+12*60*60*1000));
            }else {
                meetingRoom.setStartTime(df.format(date1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            meetingRoom.setStartTime(null);
            meetingRoom.setEndTime(null);
        }
    }
}
