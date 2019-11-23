package com.tcl.obg.common;/*
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface Kb {

    /**
     * 接口中的属性默认为static  和  final修饰
     */

//    HashMap<String, String> attrIds = new HashMap<>();

//    HashMap<String, String> respondText = new HashMap<>();
    // 会议室预定信息
    HashMap<String, Object> sessions = new HashMap<>();

    //session过期存储
    HashMap<String, Date> sessionTimes = new HashMap<>();

    HashMap<String, String> intentMap = new HashMap<>();

    String appId = "b53dc742-f729-478f-a1de-ae7f62301a6d";

    String subKey = "84cfbbd756f0444287ac710937ac1b26";

    // 意图最低有效得分
    int intentScore = 70;

    //实体最低有效得分
    int entityScore = 80;

    //session 失效时间 单位 秒
    long validessionTime = 30;

    //不能获取意图的反馈信息，初始化静态数据
    List<String> smallTalk = new ArrayList<>();

    //可预订的会议室，初始化静态数据
    List<Object> meetingRooms = new ArrayList<>();

    //唤醒回应信息，初始化静态数据
    List<String> wakeUpResponse = new ArrayList<>();

    //打招呼回应信息，初始化静态数据
    List<String> sayHelloResponse = new ArrayList<>();


    //唤醒意图
    String wakeUpIntent = "wakeUp";

    //预定会议室意图
    String bookMeetingIntent = "bookMeeting";

    //打招呼意图
    String sayHello = "sayHello";

    //会议室日期实体列表
    List<String> dateEntities = new ArrayList<>();

    //会议室日期映射
    HashMap<String, String> dateEntityMap = new HashMap<>();

    //会议室时间实体列表
    List<String> timeEntities = new ArrayList<>();

    //会议室时间映射
    HashMap<String, String> timeEntityMap = new HashMap<>();

    //会议室实体列表
    List<String> meetingRoomEntities = new ArrayList<>();

    //会议室时间映射
    HashMap<String, String> meetingRoomEntityMap = new HashMap<>();
}
