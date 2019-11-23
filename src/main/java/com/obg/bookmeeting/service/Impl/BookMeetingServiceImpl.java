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
 * Created by 华南 on 2019/10/29.
 */

import com.obg.bookmeeting.common.Kb;
import com.obg.bookmeeting.common.Util;
import com.obg.bookmeeting.service.BookMeetingService;
import com.obg.bookmeeting.vo.LuisResult;
import com.obg.bookmeeting.vo.MeetingRoom;
import com.obg.bookmeeting.vo.Message;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service("bookMeetingservice")
public class BookMeetingServiceImpl implements BookMeetingService {

    private String attrId;

    private boolean changeTime = false;

    @Override
    public Message bookMeeting(String seesionId, String attrId, String requestText) {

        //通过Luis获取意图  实体
        JSONObject luisResultString = Util.getLuisResult(requestText);

        LuisResult luisResult = Util.createLuisResult(luisResultString);
        //判断是否已经唤醒机器人 “小智同学”
        Message msg = Util.checkWakeUp(seesionId, luisResult);
        if (msg != null){
            return msg;
        }
        //判断是不是打招呼意图
        msg = Util.checkSayHello(seesionId, luisResult);
        if (msg != null){
            return msg;
        }
        //根据响应详细首先判断是否需要走Luis，如果是判断，或者一个数字并且对应一个数字的属性就不需要
        Message message = new Message();
        Object o = Kb.sessions.get(seesionId);
        if (o != null) {
            MeetingRoom meetingRoom = (MeetingRoom)o;
            if ("2".equals(attrId)) {
                this.attrId = attrId;
            }else if ("3".equals(attrId)) {
                this.attrId = attrId;
            }else if ("0".equals(attrId)) {//判断是否存在修改意图
                //获取会议主题节点
                if (requestText != null && !"".equals(requestText.trim())){
                    meetingRoom.setMeetingTheme(requestText);
                    //判断当前参数是否晚辈
                    if (isFullMeetingPara(meetingRoom)){
                        //参数完整，直接返回
                        message.setSeesionId(seesionId);
                        message.setType("4");
                        HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
                        message.setData(map);
                    }else {
                        //根据当前参数情况响应用户信息
                        message = getResponseMessage(seesionId, meetingRoom);
                    }
                }else {
                    //主题数据无效，重新获取参数
//                    message = getResponseMessage(seesionId, meetingRoom);
                    message = luisResultProcess(seesionId, luisResult);
                }
            }else if ("5".equals(attrId)){
                //若只有一个参会人数请求
                if (isInteger(requestText)){
                    meetingRoom.setPersonNum(requestText);
                    message = getResponseMessage(seesionId, meetingRoom);
                }else {
                    message = luisResultProcess(seesionId, luisResult);
                }
            }else if ("6".equals(attrId)){
                //若是投影设备请求
                if ("YES".equals(requestText) || "NO".equals(requestText)){
                    meetingRoom.setIsMedia(requestText);
                    message = getResponseMessage(seesionId, meetingRoom);
                }else {
                    message = luisResultProcess(seesionId, luisResult);
//                    message = getResponseMessage(seesionId, meetingRoom);
                }
            }else if ("7".equals(attrId)){
                //判断是否是会议室地点信息
                if (requestText != null && !"".equals(requestText.trim()) && Kb.meetingRooms.contains(requestText)){
                    meetingRoom.setAddress(requestText);
                    if (isFullMeetingPara(meetingRoom)){
                        //移除session
                        Kb.sessions.remove(seesionId);
                    }
                    message = getResponseMessage(seesionId, meetingRoom);
                    message.setResponseText("您已成功预定会议室！");
                }else {
                    message = luisResultProcess(seesionId, luisResult);
//                    message = getResponseMessage(seesionId, meetingRoom);
                }
            }else {
                message = luisResultProcess(seesionId, luisResult);
            }
        }else {
            message = luisResultProcess(seesionId, luisResult);
        }

        return message;
    }

    private HashMap<String,Object> getFullMeetingPara(MeetingRoom meetingRoom) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("meetingTheme", meetingRoom.getMeetingTheme().getAttrName());
        map.put("meetingDate", meetingRoom.getMeetingDate().getAttrName());
        map.put("startTime", meetingRoom.getStartTime().getAttrName());
        map.put("endTime", meetingRoom.getEndTime().getAttrName());
        map.put("personNum", meetingRoom.getPersonNum().getAttrName());
        map.put("isMedia", meetingRoom.getIsMedia().getAttrName());
        map.put("address", meetingRoom.getAddress().getAttrName());

        return map;
    }

    private boolean isFullMeetingPara(MeetingRoom meetingRoom) {
        if (meetingRoom.getMeetingTheme().getAttrName() == null)
            return false;
        if (meetingRoom.getMeetingDate().getAttrName() == null)
            return false;
        if (meetingRoom.getStartTime().getAttrName() == null)
            return false;
        if (meetingRoom.getEndTime().getAttrName() == null)
            return false;
        if (meetingRoom.getPersonNum().getAttrName() == null)
            return false;
        if (meetingRoom.getIsMedia().getAttrName() == null)
            return false;
        if (meetingRoom.getAddress().getAttrName() == null)
            return false;
        return true;
    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * 从Luis返回的结果中分析对应的有效意图和实体
     */
    public Message luisResultProcess(String seesionId, LuisResult result){
        MeetingRoom meetingRoom=null;
        String responseText = "";
        Object o = Kb.sessions.get(seesionId);
        Message message = new Message();
        if (o == null){
            //用户第一次进入
            //判断意图是否有效
            Map<String, Object> topIntent = result.getTopIntent();
            Random r = new Random(1);
            if (topIntent != null){
                double score = (double)topIntent.get("score");
                int topScore = (int) (score*100);
                if (topScore >= Kb.intentScore ){
                    //意图有效
                    String intent = (String)topIntent.get("intent");
                    if (Kb.bookMeetingIntent.equals(intent)){
                        //确认是预定会议室流程
                        meetingRoom = new MeetingRoom();
                        //获取实体参数
                        setMeetingRoomParameters(result, meetingRoom);
                        Kb.sessions.put(seesionId, meetingRoom);
                        //根据当前参数情况响应用户信息
                        message = getResponseMessage(seesionId, meetingRoom);
                        return message;
                    }else {
                        responseText = Kb.smallTalk.get(r.nextInt(100)%Kb.smallTalk.size());
                    }
                }else {
                    responseText = Kb.smallTalk.get(r.nextInt(100)%Kb.smallTalk.size());
                }
            }else {
                responseText = Kb.smallTalk.get(r.nextInt(100)%Kb.smallTalk.size());
            }
            message.setSeesionId(seesionId);
            message.setType("1");
            message.setResponseText(responseText);
            return message;
        }else {
            //已经存在对应的意图流程
            meetingRoom = (MeetingRoom)o;
            //获取实体参数
            setMeetingRoomParameters(result, meetingRoom);
            message = getResponseMessage(seesionId, meetingRoom);
            if (this.changeTime){
                this.changeTime = false;
                message.setResponseText("(修改时间请同时确认开始于结束时间！)<br>"+message.getResponseText());
            }
            return message;
        }

    }

    private Message getResponseMessage(String seesionId, MeetingRoom meetingRoom) {
        Message response = new Message();
        StringBuilder attrIds = new StringBuilder();
        StringBuilder responseText = new StringBuilder();
        if (meetingRoom.getMeetingDate().getAttrName() == null){
            attrIds.append(meetingRoom.getMeetingDate().getId()+",");
            responseText.append(meetingRoom.getMeetingDate().getDesc()+",");
        }
        if (meetingRoom.getStartTime().getAttrName() == null){
            attrIds.append(meetingRoom.getStartTime().getId()+",");
            responseText.append(meetingRoom.getStartTime().getDesc()+",");
        }
        if (meetingRoom.getEndTime().getAttrName() == null){
            attrIds.append(meetingRoom.getEndTime().getId()+",");
            responseText.append(meetingRoom.getEndTime().getDesc()+",");
        }
        if (meetingRoom.getPersonNum().getAttrName() == null){
            attrIds.append(meetingRoom.getPersonNum().getId()+",");
            responseText.append(meetingRoom.getPersonNum().getDesc()+",");
        }
        String attrId = attrIds.toString();
        String text = responseText.toString();
        String title = "请确认会议：";
        if (attrId == null || "".equals(attrId)){
            if (meetingRoom.getIsMedia().getAttrName() == null){
                //提示是否需要投影
                attrId = meetingRoom.getIsMedia().getId();
                text = meetingRoom.getIsMedia().getDesc();
                response.setSeesionId(seesionId);
                response.setType("2");
                response.setAttrId(attrId);
                response.setResponseText(title+text);
                HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
                response.setData(map);
            }else {
                if (meetingRoom.getMeetingTheme().getAttrName() == null){
                    //提示获取会议主题
                    attrId = meetingRoom.getMeetingTheme().getId();
                    text = meetingRoom.getMeetingTheme().getDesc();
                    response.setSeesionId(seesionId);
                    response.setType("1");
                    response.setAttrId(attrId);
                    response.setResponseText(title+text);
                    HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
                    response.setData(map);
                }else {
                    if (meetingRoom.getAddress().getAttrName() == null){
                        attrId = meetingRoom.getAddress().getId();
                        text = meetingRoom.getAddress().getDesc();
                        response.setSeesionId(seesionId);
                        response.setType("3");
                        response.setAttrId(attrId);
                        response.setResponseText(text);
                        HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
                        map.put("meetingRooms.txt", Kb.meetingRooms);
                        response.setData(map);
                    }else {
                        //参数完整，直接返回
                        response.setSeesionId(seesionId);
                        response.setType("4");
                        HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
                        response.setData(map);
                    }
                }
            }
        }else {
            attrId = attrId.substring(0, attrId.length()-1);
            text = text.substring(0, text.length()-1);
            response.setSeesionId(seesionId);
            response.setType("1");
            response.setAttrId(attrId);
            response.setResponseText(title+text);
            HashMap<String, Object> map = getFullMeetingPara(meetingRoom);
            response.setData(map);
        }

        return response;
    }

    /**
     * 从Luis返回结果中提取参数 到实体MeetingRoom
     * @param result
     * @param meetingRoom
     */
    private void setMeetingRoomParameters(LuisResult result, MeetingRoom meetingRoom) {
        List<Map<String, Object>> meetingDate = new ArrayList<>();
        List<Map<String, Object>> meetingTime = new ArrayList<>();
        List<Map<String, Object>> meetingDuringTime = new ArrayList<>();
        List<Map<String, Object>> personNum = new ArrayList<>();


        List<Map<String, Object>> entities = result.getEntities();
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
            }else if ("meetingDuringTime".equals(map.get("type").toString())){
                if (Util.checkEntity(map)){
                    meetingDuringTime.add(map);
                }
            }else if ("personNum".equals(map.get("type").toString())){
                if (Util.checkEntity(map)){
                    personNum.add(map);
                }
            }
        }
        //判断是否有 “日期” 参数实体 : meetingDate
        if (meetingDate.size() > 0){
            String date = (String) meetingDate.get(0).get("resolution");
            //将日期转化为系统标准日期
            date = Util.getDate(date);
            meetingRoom.setMeetingDate(date);
            if (meetingRoom.getAddress() != null){
                meetingRoom.setAddress(null);
            }
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
                if ("2".equals(this.attrId)){
                    meetingRoom.setStartTime(time);
                }else if ("3".equals(this.attrId)){
                    meetingRoom.setEndTime(time);
                }else {
                    if (meetingRoom.getStartTime().getAttrName() == null){
                        meetingRoom.setStartTime(time);
                    }else {
                        if (meetingRoom.getEndTime().getAttrName() == null){
                            meetingRoom.setEndTime(time);
                        }else {
                            //两个都不为空的时候，怎么存放这一个时间
                            this.changeTime = true;
                        }
                    }
                }
                if (meetingRoom.getStartTime().getAttrName() != null
                        && meetingRoom.getEndTime().getAttrName() != null){
                    Util.setMeetingTimes(meetingRoom.getStartTime().getAttrName(),
                            meetingRoom.getEndTime().getAttrName(), meetingRoom);
                }
                if (meetingRoom.getAddress() != null && this.changeTime == false){
                    meetingRoom.setAddress(null);
                }
            }else {
                //包含两个时间，根据规则产生开始时间和结束时间
                if (startTime != null && endTime != null){
                    Util.setMeetingTimes(startTime, endTime, meetingRoom);
                }
                if (meetingRoom.getAddress() != null){
                    meetingRoom.setAddress(null);
                }
            }
//            meetingRoom.setStartTime(startTime);
//            meetingRoom.setEndTime(endTime);
        }
        //判断是否有会议 “持续时间” 参数  : meetingDuringTime
        if (meetingDuringTime.size() > 0){
            //提取其中的数字
            Integer num = getNumFromEntity(meetingDuringTime);
            meetingRoom.setMeetingDuringTime(num==null?null : String.valueOf(num));
            if (meetingRoom.getAddress() != null){
                meetingRoom.setAddress(null);
            }
        }
        //判断是否有 “人数” 参数  : personNum
        if (personNum.size() > 0){
            //提取其中的数字
            Integer num = getNumFromEntity(personNum);
            meetingRoom.setPersonNum(num==null?null : String.valueOf(num));
            if (meetingRoom.getAddress() != null){
                meetingRoom.setAddress(null);
            }
        }
    }

    /**
     * 从字符串中提取数字  数字有可能是中文数字
     * @param entityList
     * @return
     */
    private Integer getNumFromEntity(List<Map<String, Object>> entityList) {

        if (entityList == null || entityList.size() == 0){
            return null;
        }
        //找出得分最大的实体
        int index = 0;
        double score = (Double) entityList.get(0).get("score");
        for (int i=0; i<entityList.size(); i++){
            double s = (Double) entityList.get(i).get("score");
            if (s > score){
                index = i;
                score = s;
            }
        }
        String value = (String) entityList.get(index).get("entity");
        int start = -1;
        int end = -1;
        for (int i=0; i<value.length(); i++){
            if (Character.isDigit(value.charAt(i))){
                if (start == -1){
                    start = i;
                    end = i;
                }else {
                    end = i;
                    break;
                }
            }
        }
        if (start == -1 && end == -1){
            //没有阿拉伯数字，但可能有中文数字
            value = value.replaceAll("个人", "").replaceAll("人", "");
            if (Util.checkChineseDigit(value)){
                return Util.solve(value);
            }else {
                return null;
            }
        }else {
            String s = value.substring(start, end - start + 1);
            return Integer.valueOf(s);
        }
    }

}
