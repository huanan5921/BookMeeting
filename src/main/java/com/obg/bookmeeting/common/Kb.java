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
 * Created by 华南 on 2019/10/29.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Kb {

    /**
     * 接口中的属性默认为static  和  final修饰
     */

//    HashMap<String, String> attrIds = new HashMap<>();

//    HashMap<String, String> respondText = new HashMap<>();

    HashMap<String, Object> sessions = new HashMap<>();

    HashMap<String, String > intentMap = new HashMap<>();

    String appId = "b53dc742-f729-478f-a1de-ae7f62301a6d";

    String subKey = "84cfbbd756f0444287ac710937ac1b26";

    int intentScore = 70;

    int entityScore = 70;

    List<String> smallTalk = new ArrayList<>();

    List<String> meetingRooms = new ArrayList<>();

}
