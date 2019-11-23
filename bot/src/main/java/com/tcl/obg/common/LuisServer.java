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
 * Created by 华南 on 2019/11/16.
 */

import com.tcl.obg.vo.LuisResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟Luis  通过文本获取意图和参数
 */
public class LuisServer {

    private static String[] wakeUpTexts = {"小智同学", "小天同学", "小周同学", "小子同学"};

    public static LuisResult getLuisResponse(String text){
        LuisResult luisResult = new LuisResult();
        luisResult.setQuery(text);

        HashMap<String, Object> topIntent = new HashMap<>();
        ArrayList<Map<String, Object>> intents = new ArrayList<>();
        ArrayList<Map<String, Object>> entities = new ArrayList<>();

        if (haveWakeUpText(text)){
            topIntent.put("intent", Kb.wakeUpIntent);
            topIntent.put("score", 1.0);
        }else if (havaBookMeetingText(text)){
            topIntent.put("intent", Kb.bookMeetingIntent);
            topIntent.put("score", 1.0);
            findParametersFromText(text, entities);
        }else {
            topIntent.put("intent", "None");
            topIntent.put("score", 1.0);
            findParametersFromText(text, entities);
        }
        luisResult.setTopIntent(topIntent);
        luisResult.setIntents(intents);
        luisResult.setEntitys(entities);
        return luisResult;
    }

    private static void findParametersFromText(String text, ArrayList<Map<String, Object>> entities) {
        //获得日期
        for (int i=0; i<Kb.dateEntities.size(); i++){
            String key = Kb.dateEntities.get(i);
            int index = text.indexOf(key);
            if (index != -1){
                String val = Kb.dateEntityMap.get(key);
                HashMap<String, Object> map = new HashMap<>();
                map.put("entity", key);
                map.put("type", "meetingDate");
                map.put("startIndex", index);
                map.put("endIndex", index+key.length()-1);
                map.put("score", 1.0);
                map.put("resolution", val);
                entities.add(map);
                break;
            }
        }
        // 会议开始/结束时间
        for (int i=0; i<Kb.timeEntities.size(); i++){
            String key = Kb.timeEntities.get(i);
            int index = text.indexOf(key);
            if (index != -1){
                String val = Kb.timeEntityMap.get(key);
                HashMap<String, Object> map = new HashMap<>();
                map.put("entity", key);
                map.put("type", "meetingTime");
                map.put("startIndex", index);
                map.put("endIndex", index+key.length()-1);
                map.put("score", 1.0);
                map.put("resolution", val);
                entities.add(map);
            }
        }
        //会议室地点
        for (int i=0; i<Kb.meetingRoomEntities.size(); i++){
            String key = Kb.meetingRoomEntities.get(i);
            int index = text.indexOf(key);
            if (index != -1){
                String val = Kb.meetingRoomEntityMap.get(key);
                HashMap<String, Object> map = new HashMap<>();
                map.put("entity", key);
                map.put("type", "roomAddres");
                map.put("startIndex", index);
                map.put("endIndex", index+key.length()-1);
                map.put("score", 1.0);
                map.put("resolution", val);
                entities.add(map);
            }
        }
    }

    /**
     * 判断text参数是否包含预定会议室关键字
     * @param text
     * @return
     */
    private static boolean havaBookMeetingText(String text) {
        if ((text.indexOf("预定") != -1 || text.indexOf("预订") != -1
                || text.indexOf("订") != -1 || text.indexOf("定") != -1)
                && (text.indexOf("会议室") != -1 || text.indexOf("会议是") != -1) || text.indexOf("会议事") != -1){
            return true;
        }
        return false;
    }

    /**
     * 判断text参数是否包含唤醒关键字
     * @param text
     * @return
     */
    private static boolean haveWakeUpText(String text) {
        for (int i=0; i<wakeUpTexts.length; i++){
            if (text.indexOf(wakeUpTexts[i]) != -1){
                return true;
            }
        }
        return false;
    }
}
