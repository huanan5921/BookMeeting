package com.obg.bookmeeting.vo;/*
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

    private Attr meetingDuringTime;

    private Attr personNum;

    private Attr isMedia;

    private Attr address;

    public MeetingRoom() {
        this.meetingTheme = new Attr("0", "theme", "主题");
        this.meetingDate = new Attr("1", "date", "日期");
        this.startTime = new Attr("2", "time", "开始时间");
        this.endTime = new Attr("3", "time", "结束时间");
        this.meetingDuringTime = new Attr("4", "number", "会议时长");
        this.personNum = new Attr("5", "number", "参会人数");
        this.isMedia = new Attr("6", "boolean", "是否需要投影");
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

    public Attr getIsMedia() {
        return isMedia;
    }

    public void setIsMedia(String isMedia) {
        this.isMedia.setAttrName(isMedia);
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

    public Attr getMeetingDuringTime() {
        return meetingDuringTime;
    }

    public void setMeetingDuringTime(String duringTime) {
        this.meetingDuringTime.setAttrName(duringTime);
    }

    public Attr getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String num) {
        this.personNum.setAttrName(num);
    }
}
