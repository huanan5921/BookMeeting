package com.obg.bookmeeting.service.Impl;/*
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
 * Created by 华南 on 2019/11/14.
 */

import com.obg.bookmeeting.common.HttpTestJDK;
import com.obg.bookmeeting.common.Kb;
import com.obg.bookmeeting.common.LuisServer;
import com.obg.bookmeeting.common.Util;
import com.obg.bookmeeting.service.BotService;
import com.obg.bookmeeting.vo.LuisResult;
import com.obg.bookmeeting.vo.MeetingRoom;
import com.obg.bookmeeting.vo.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

@Service("botService")
public class BotServiceImpl implements BotService {
    private Set<String> attrIds;
    @Override
    public Message botProcess(String seesionId, String attrId, String requestText) {
        HashSet<String> set = new HashSet<>();
        for (String s : attrId.split(",")){
            set.add(s);
        }
        this.attrIds = set;
        //通过Luis获取意图  实体
        JSONObject luisResultString = Util.getLuisResult(requestText);
        //将返回结果转化为数据对象
        LuisResult luisResult = Util.createLuisResult(luisResultString);
        if (this.attrIds.size()==1 && this.attrIds.contains("0") && !"".equals(requestText.trim())){
            Date date = Kb.sessionTimes.get(seesionId);
            MeetingRoom o = (MeetingRoom) Kb.sessions.get(seesionId);
            if (!Util.isTimeOutSession(date) && o!=null){
                o.setMeetingTheme(requestText.trim());
                Message message = new Message();
                message.setSeesionId(seesionId);
                message.setType("4");
                message.setResponseText("会议室预定成功");
                message.setVoiceUrl("/wavs/tts_video/bm_meetingRoomssuccess.wav");
                HashMap<String, Object> map = new HashMap<>();
                map.put("data", o);
                message.setData(map);
                return message;
            }
        }
//        LuisResult luisResult = LuisServer.getLuisResponse(requestText);
        if (luisResult ==null){
            //Luis意图获取失败
            return Util.getSmallTalkResponse(seesionId);
        }
        //判断是否已经唤醒机器人 “小智同学”
        Message msg = Util.checkWakeUp(seesionId, luisResult);
        if (msg != null){
            return msg;
        }
        //已经唤醒，并且不是唤醒意图
        MeetingRoom meetingRoom = (MeetingRoom)Kb.sessions.get(seesionId);
        if (meetingRoom != null){
            //当前存在会议室预定意图流程，不需要判断意图，直接获取参数
            updataMeetingRoomPatameters(luisResult, meetingRoom);
            //meetingRoom判断参数是否完整
            try {
                msg = getResponse(seesionId, meetingRoom);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            //当前不存在流程，判断是否是预定会议室流程
            if (Util.isAssignIntent(luisResult, Kb.bookMeetingIntent)){
                //确认是预定会议室意图
                meetingRoom = new MeetingRoom();
                //获取实体参数
                updataMeetingRoomPatameters(luisResult, meetingRoom);
                Kb.sessions.put(seesionId, meetingRoom);
                try {
                    msg = getResponse(seesionId, meetingRoom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                msg = Util.getSmallTalkResponse(seesionId);
            }
        }
        return msg;
    }

    @Override
    public String clearSeesion(String seesionId) {
        try {
            Kb.sessions.remove(seesionId);
            Kb.sessionTimes.remove(seesionId);
            return "1";
        }catch (RuntimeException e){
            e.printStackTrace();
            return "-1";
        }
    }

    /**
     * 根据当前会议是参数生成返回信息
     * @param seesionId
     * @param meetingRoom   会议室参数
     * @return
     */
    private Message getResponse(String seesionId, MeetingRoom meetingRoom) throws ParseException {
        String meetingDate = meetingRoom.getMeetingDate().getAttrName();
        String startTime = meetingRoom.getStartTime().getAttrName();
        String endTime = meetingRoom.getEndTime().getAttrName();
        String address = meetingRoom.getAddress().getAttrName();
        Message message = new Message();
        if (meetingDate!=null && startTime!=null && endTime!=null){
            //时间完整
            String s = meetingDate+" "+startTime;
            String e = meetingDate+" "+endTime;
            //判断开始时间是否大于等于结束时间或者开始时间在历史时间
            String r = Util.isValidMeetingTime(meetingRoom);
            if (r != null){
                message.setSeesionId(seesionId);
                message.setType("1");
                message.setResponseText(r.split("=")[0]);
                message.setVoiceUrl(r.split("=")[1]);
            }
            //根据时间获取可用的会议室
            List<Object> meetingRooms = getMeetingRoomsByTime(s, e);
            if (address != null){
                if (this.attrIds.size()==1 && this.attrIds.contains("7")){
                    message.setSeesionId(seesionId);
                    message.setType("1");
                    message.setAttrId("0");
                    message.setResponseText("请确认会议主题");
                    message.setVoiceUrl("/wavs/tts_video/bm_meetingRoomTheme.wav");
                    return message;
                }else {
                    //如果指定会议室再查询结果当中，将指定会议室放第一个位置
                    if (meetingRooms != null && meetingRooms.size() > 0) {
                        for (int i = 0; i < meetingRooms.size(); i++) {
                            JSONObject o = (JSONObject) meetingRooms.get(i);
                            String roomName = (String) o.get("fdName");
                            if (address.equals(roomName)) {
                                meetingRooms.remove(i);
                                meetingRooms.add(0, o);
                                break;
                            }
                        }
                    }
                }
            }
            if (meetingRooms.size() > 0){
                message.setSeesionId(seesionId);
                message.setType("3");
                message.setAttrId("7");
                message.setResponseText(s + " - " + endTime + "<br/>期间有以下会议室可用,请选择");
                message.setVoiceUrl("/wavs/tts_video/bm_meetingRoomsByTime.wav");
                HashMap<String, List> map = new HashMap();
                map.put("meetingRooms", meetingRooms);
                message.setData(map);
            }else {
                message.setSeesionId(seesionId);
                message.setType("1");
                message.setResponseText(s + " - " + endTime + "<br/>期间没有可用的会议室，您可考虑更换会议时间");
                message.setVoiceUrl("/wavs/tts_video/bm_notMeetingRoomByTime.wav");
            }
        } else if (address != null){
            //时间不完整，地点完整
            int days = 5;
            //首先判断用户是否有当前会议室权限

            //根据会议室获取可用的时间
            List<String> timeList = getfreeTimeByMeetingRoom(meetingDate, days, address);//默认只要今天之后的5天
            message.setSeesionId(seesionId);
            message.setType("5");
            message.setResponseText("会议室有以下时间空闲，请选择");
            message.setVoiceUrl("/wavs/tts_video/bm_timeByMeetingRoom.wav");
            HashMap<String, List> map = new HashMap();
            map.put("timeList", timeList);
            message.setData(map);
        }else {
            //都不完整
            String r = Util.isValidMeetingTime(meetingRoom);
            if (r != null){
                message.setSeesionId(seesionId);
                message.setType("1");
                message.setResponseText(r.split("=")[0]);
                message.setVoiceUrl(r.split("=")[1]);
                return message;
            }
            String ids = "";
            String text = "";
            String voiceUrl = "bm";
            if (meetingDate == null){
                ids += meetingRoom.getMeetingDate().getId()+",";
                text += meetingRoom.getMeetingDate().getDesc()+",";
                voiceUrl += "_date";
            }
            if (startTime == null){
                ids += meetingRoom.getStartTime().getId()+",";
                text += meetingRoom.getStartTime().getDesc()+",";
                voiceUrl += "_start";
            }
            if (endTime == null){
                ids += meetingRoom.getEndTime().getId()+",";
                text += meetingRoom.getEndTime().getDesc()+",";
                voiceUrl += "_end";
            }
            if (!"".equals(ids)){
                ids = ids.substring(0, ids.length()-1);
                text = text.substring(0, text.length()-1);
            }
            message.setSeesionId(seesionId);
            message.setType("1");
            message.setAttrId(ids);

            message.setResponseText("请确认会议："+text);
            if (!"bm".equals(voiceUrl)){
                message.setVoiceUrl("/wavs/tts_video/"+voiceUrl+"Time.wav");
            }

        }
        return message;
    }



    /**
     * 获取指定会议室的空闲时间
     * @param days 查询时间天数，从当天往后
     * @param address
     * @return
     */
    private List<String> getfreeTimeByMeetingRoom(String meetingDate, int days, String address) {
        if (meetingDate != null){

        }else {

        }
        return null;
    }

    private List<Object> getMeetingRoomsByTime(String startTime, String endTime) {
        String url = "http://10.126.8.7:8080/ekp/km/imeeting/km_imeeting_inter/kmImeetingInter.do?method=queryImeeting&startTime=2019-11-25 09:30:00&endTime=2019-11-25 10:30:00";
        String result = HttpTestJDK.doGet(url);
        if (result == null){
            return Kb.meetingRooms;
        }
        JSONObject r = JSONObject.fromObject(result);
        String flag = (String)r.get("restMsg");
        ArrayList<Object> list = new ArrayList<>();
        if (flag != null && "success".equals(flag)){
            //请求成功
            JSONArray data = (JSONArray)r.get("data");
            if (data.size() > 0){
                //有数据
                for (int i=0; i<data.size(); i++){
                    JSONObject o = (JSONObject)data.get(i);
                    list.add(o);
                }
            }
            return list;
        }else {
            //如果失败，创建备用数据
            return Kb.meetingRooms;
        }
    }

    public void updataMeetingRoomPatameters(LuisResult luisResult, MeetingRoom meetingRoom) {
        List<Map<String, Object>> meetingDate = new ArrayList<>();
        List<Map<String, Object>> meetingTime = new ArrayList<>();
        List<Map<String, Object>> meetingRoomEntity = new ArrayList<>();

        //提取其中的实体参数
        List<Map<String, Object>> entities = luisResult.getEntities();
        for (int i=0; i<entities.size(); i++){
            Map<String, Object> map = entities.get(i);
            if ("meetingDate".equals(map.get("type").toString())){
                if (Util.checkEntity(map)){
                    meetingDate.add(map);
                }
            }else if ("meetingTime".equals(map.get("type").toString())){
                if (Util.checkEntity(map)){
                    meetingTime.add(map);
                }
            }else if ("roomAddres".equals(map.get("type").toString())){
                if (Util.checkEntity(map)){
                    meetingRoomEntity.add(map);
                }
            }
        }
        //判断是否有会议室地点参数
        if (meetingRoomEntity.size()>0){
            String address = (String) meetingRoomEntity.get(0).get("resolution");
            meetingRoom.setAddress(address.split("/")[1]);
        }
        //判断是否有 “日期” 参数实体 : meetingDate
        if (meetingDate.size() > 0){
            String date = (String) meetingDate.get(0).get("resolution");
            //将日期转化为系统标准日期
            date = Util.getDate(date);
            meetingRoom.setMeetingDate(date);
        }
        //判断是否有 “时间” 参数  : meetingTime
        if (meetingTime.size() > 0){
            Map meetingTimePara = Util.getMeetingTime(meetingTime);
            //标准时间字符串
            String time = (String) meetingTimePara.get("time");
            String startTime = (String) meetingTimePara.get("startTime");
            String endTime = (String) meetingTimePara.get("endTime");
            if (time != null){
                //只有一个时间 time
                if (this.attrIds.contains("2")){
                    //判断开始时间是否大于等于结束时间
                    meetingRoom.setStartTime(time);
                }else if (this.attrIds.contains("3")){
                    //判断开始时间是否大于等于结束时间
                    meetingRoom.setEndTime(time);
                }
//                if (meetingRoom.getStartTime().getAttrName() != null
//                        && meetingRoom.getEndTime().getAttrName() != null){
//                    Util.setMeetingTimes(meetingRoom.getStartTime().getAttrName(),
//                            meetingRoom.getEndTime().getAttrName(), meetingRoom);
//                }

            }else {
                //包含两个时间，根据规则产生开始时间和结束时间
                if (startTime != null && endTime != null){
                    meetingRoom.setStartTime(startTime);
                    meetingRoom.setEndTime(endTime);
//                    Util.setMeetingTimes(startTime, endTime, meetingRoom);
                }

            }
        }

    }
}
