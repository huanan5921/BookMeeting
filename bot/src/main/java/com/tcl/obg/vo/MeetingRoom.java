package com.tcl.obg.vo;/*
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
 * Created by 华南 on 2019/10/29.
 */

public class MeetingRoom {

    private Attr meetingTheme;

    private Attr meetingDate;

    private Attr startTime;

    private Attr endTime;

    private Attr address;

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "meetingTheme=" + meetingTheme.getAttrName() +
                ", meetingDate=" + meetingDate.getAttrName() +
                ", startTime=" + startTime.getAttrName() +
                ", endTime=" + endTime.getAttrName() +
                ", address=" + address.getAttrName() +
                '}';
    }

    public MeetingRoom() {
        this.meetingTheme = new Attr("0", "theme", "主题");
        this.meetingDate = new Attr("1", "date", "日期");
        this.startTime = new Attr("2", "time", "开始时间");
        this.endTime = new Attr("3", "time", "结束时间");
        this.address = new Attr("7", "address", "根据您的要求有以下会议室可用，请选择");

    }

    public Attr getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address.setAttrName(address);
    }

    public Attr getMeetingTheme() {
        return meetingTheme;
    }

    public void setMeetingTheme(String meetingTheme) {
        this.meetingTheme.setAttrName(meetingTheme);
    }

    public Attr getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String date) {
        this.meetingDate.setAttrName(date);
    }

    public Attr getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime.setAttrName(time);
    }

    public Attr getEndTime() {
        return endTime;
    }

    public void setEndTime(String time) {
        this.endTime.setAttrName(time);
    }

}
