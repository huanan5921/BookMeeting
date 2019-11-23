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
 * Created by 华南 on 2019/10/30.
 */

import com.tcl.obg.vo.LuisResult;
import com.tcl.obg.vo.MeetingRoom;
import com.tcl.obg.vo.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static void main(String[] args) {
        Locale.setDefault(Locale.CHINESE);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //若一周第一天为星期天，则-1
        if(isFirstSunday){
            weekDay = weekDay - 1;
            if(weekDay == 0){
                weekDay = 7;
            }
        }
        System.out.println(weekDay);
    }

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
        //一周第一天是否为星期天
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        int WeekOfYear = calendar.get(Calendar.DAY_OF_WEEK);
        //若一周第一天为星期天，则-1
        if(isFirstSunday){
            WeekOfYear = WeekOfYear - 1;
            if(WeekOfYear == 0){
                WeekOfYear = 7;
            }
        }
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

    /**
     * 判断是否是指定意图
     * @param luisResult
     * @return
     */
    public static boolean isAssignIntent(LuisResult luisResult, String targetIntent) {

        Map<String, Object> topIntent = luisResult.getTopIntent();
        if (topIntent != null) {
            double score = (double) topIntent.get("score");
            int topScore = (int) (score * 100);
            if (topScore >= Kb.intentScore) {
                //意图有效
                String intent = (String) topIntent.get("intent");
                if (targetIntent.equals(intent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTimeOutSession(Date oldDate) {
        Date date = new Date();
        long s = (date.getTime()-oldDate.getTime())/1000;
        if (s > Kb.validessionTime){
            return true;
        }else {
            return false;
        }
    }

    public static Message checkWakeUp(String seesionId, LuisResult luisResult) {
        Date date = Kb.sessionTimes.get(seesionId);
        if (date == null){
            //没有唤醒，检查是否是唤醒意图
            if (Util.isAssignIntent(luisResult, Kb.wakeUpIntent)){
                //返回回应信息
                Random r = new Random(1);r.nextInt(100);
                Message message = new Message();
                message.setType("1");
                message.setResponseText(Kb.wakeUpResponse.get(r.nextInt(100)%Kb.wakeUpResponse.size()));
                message.setVoiceUrl("/wavs/tts_video/bm_sayHello.wav");
                Kb.sessionTimes.put(seesionId, new Date());
                return message;
            }else {
                //返回错误信息，不予理睬
                Message message = new Message();
                message.setType("-1");
                return message;
            }
        }else {
            //唤醒过，检查是否超时
            if (Util.isTimeOutSession(date)){
                //session 超时，清楚seesionTime、sessions 中的内容，返回错误信息，不予理睬
                if (Util.isAssignIntent(luisResult, Kb.wakeUpIntent)){
                    //是唤醒意图
                    Kb.sessions.remove(seesionId);
                    Random r = new Random(1);r.nextInt(100);
                    Message message = new Message();
                    message.setType("1");
                    message.setResponseText(Kb.wakeUpResponse.get(r.nextInt(100)%Kb.wakeUpResponse.size()));
                    message.setVoiceUrl("/wavs/tts_video/bm_sayHello.wav");
                    Kb.sessionTimes.put(seesionId, new Date());
                    return message;
                }else {
                    //不是唤醒意图
                    Kb.sessions.remove(seesionId);
                    Kb.sessionTimes.remove(seesionId);
                    Message message = new Message();
                    message.setType("-1");
                    return message;
                }
            }else {
                //正在有效seesion时间中
                if (Util.isAssignIntent(luisResult, Kb.wakeUpIntent)){
                    //是唤醒意图
                    Random r = new Random(1);r.nextInt(100);
                    Message message = new Message();
                    message.setType("1");
                    message.setResponseText(Kb.wakeUpResponse.get(r.nextInt(100)%Kb.wakeUpResponse.size()));
                    message.setVoiceUrl("/wavs/tts_video/bm_sayHello.wav");
                    Kb.sessionTimes.put(seesionId, new Date());
                    return message;
                }else {
                    //不是唤醒意图，更新sesion时间
                    Kb.sessionTimes.put(seesionId, new Date());
                    //如果语句既没有意图，也没有实体，则返回听不懂
                    if (!Util.isAssignIntent(luisResult, Kb.bookMeetingIntent) && luisResult.getEntities().size()==0){
                        Message message = Util.getSmallTalkResponse(seesionId);
                        return message;
                    }
                    //不做任何处理，跳出继续执行
                }
            }
        }
        return null;
    }

    public static Message checkSayHello(String seesionId, LuisResult luisResult) {
        if (Util.isAssignIntent(luisResult, Kb.sayHello)){
            Random r = new Random(1);r.nextInt(100);
            Message message = new Message();
            message.setType("1");
            message.setResponseText(Kb.sayHelloResponse.get(r.nextInt(100)%Kb.sayHelloResponse.size()));
            return message;
        }
        return null;
    }

    /**
     * 将返回的json参数组装成LuisResult对象
     * @param data
     * @return
     */
    public static LuisResult createLuisResult(JSONObject data){

        if (data == null){
            return null;
        }
        try {
            LuisResult luisResult = new LuisResult();
            //查询参数
            luisResult.setQuery(data.getString("query"));

            //topIntent
            JSONObject topScoringIntent = (JSONObject)data.get("topScoringIntent");
            HashMap<String, Object> topIntent = new HashMap<>();
            topIntent.put("intent", topScoringIntent.get("intent"));
            topIntent.put("score", topScoringIntent.get("score"));
            luisResult.setTopIntent(topIntent);

            //intents
            JSONArray intentsArray = (JSONArray)data.get("intents");
            List<Map<String, Object>> intents = new ArrayList<>();
            if (intentsArray.size() > 0){
                for (int i=0; i<intentsArray.size(); i++){
                    JSONObject intent = (JSONObject) intentsArray.get(i);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("intent", intent.get("intent"));
                    map.put("score", intent.get("score"));
                    intents.add(map);
                }
            }
            luisResult.setIntents(intents);

            //intents
            JSONArray entityArray = (JSONArray)data.get("entities");
            List<Map<String, Object>> entities = new ArrayList<>();
            if (entityArray.size() > 0){
                for (int i=0; i<entityArray.size(); i++){
                    JSONObject entity = (JSONObject) entityArray.get(i);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("entity", entity.get("entity"));
                    map.put("type", entity.get("type"));
                    map.put("startIndex", entity.get("startIndex"));
                    map.put("endIndex", entity.get("endIndex"));
                    map.put("score", entity.get("score"));
                    JSONObject resolution = (JSONObject)entity.get("resolution");
                    if (resolution != null){
                        JSONArray value = (JSONArray)resolution.get("values");
                        if (value != null){
                            map.put("resolution", value.get(0));
                        }
                    }
                    entities.add(map);
                }
            }
            luisResult.setEntitys(entities);
            return luisResult;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static JSONObject getLuisResult(String text) {
        HttpClient httpclient = HttpClients.createDefault();

        try {
            String url = "https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/" + Kb.appId;
            ArrayList<NameValuePair> parame = new ArrayList<>();
            parame.add(new BasicNameValuePair("timezoneOffset", "0"));
            parame.add(new BasicNameValuePair("verbose", "true"));
            parame.add(new BasicNameValuePair("subscription-key", Kb.subKey));
            parame.add(new BasicNameValuePair("q", text));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parame, "utf-8");
            InputStream inputStream = formEntity.getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readLine = bufferedReader.readLine();
            String parameStr = URLDecoder.decode(readLine, "utf-8");
//            System.out.println("readLine============="+readLine);
//            System.out.println("paramStr============="+parameStr);
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            sb.append("?");
            sb.append(readLine);
//            System.out.println("url=================="+sb.toString());
            HttpGet request = new HttpGet(sb.toString());

//            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/" + Kb.appId+"?q="+text);
//
//            builder.setParameter("timezoneOffset", "0");
//            builder.setParameter("verbose", "true");
//
//
////            builder.setParameter("spellCheck", "{boolean}");
////            builder.setParameter("staging", "{boolean}");
////            builder.setParameter("log", "{boolean}");
//
//            URI uri = builder.build();
//            HttpGet request = new HttpGet(uri);
////            request.setHeader("Content-Type", "application/json");
//            request.setHeader("Ocp-Apim-Subscription-Key", Kb.subKey);
//
//
//            // Request body
////            StringEntity reqEntity = new StringEntity(text);
////            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JSONObject data = JSONObject.fromObject(result);
                System.out.println(data.toString());
                return data;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean checkEntity(Map<String, Object> map) {
        Object o = map.get("score");
        if (o != null){
            int score = (int)((Double)o*100) ;
            if (score  >= Kb.entityScore){
                return true;
            }
        }else if (map.get("resolution") != null){
            return true;
        }
        return false;
    }
    /**
     * 从多个实体中提取会议时间
     * @param meetingTime
     * @return
     */
    public static Map getMeetingTime(List<Map<String, Object>> meetingTime) {

        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        HashMap<String, String> result = new HashMap<>();
        //从叠加的时间筛选正确的时间，eg：十一点   一点
        if (meetingTime.size()>0){
            mapList.add(meetingTime.get(0));
            for (int i= 1; i<meetingTime.size(); i++){
                Map<String, Object> m = meetingTime.get(i);
                for (int j=0; j<mapList.size(); j++){
                    Map<String, Object> sm = mapList.get(j);
                    Integer m_startIndex = (Integer) m.get("startIndex");
                    Integer m_endIndex = (Integer) m.get("endIndex");
                    Integer sm_startIndex = (Integer) sm.get("startIndex");
                    Integer sm_endIndex = (Integer) sm.get("endIndex");
                    if (m_startIndex == sm_startIndex || m_endIndex == sm_endIndex){
                        //有相同时间，进行合并
                        if (m_startIndex == sm_startIndex && m_endIndex > sm_endIndex){
                            mapList.remove(j);
                            mapList.add(j, m);
                        }else if (m_startIndex < sm_startIndex && m_endIndex == sm_endIndex){
                            mapList.remove(j);
                            mapList.add(j, m);
                        }
                        m = null;
                        break;
                    }
                }
                if (m != null){
                    mapList.add(m);
                }
            }
        }
        //从mapList中选择正确的时间或世家区间
        int size = mapList.size();
        if (size > 0){
            if (size == 1){
                result.put("time", (String) mapList.get(0).get("resolution"));
            }else if (size >= 2){
                Integer startIndex_0 = (Integer) mapList.get(0).get("startIndex");
                Integer startIndex_1 = (Integer) mapList.get(1).get("startIndex");
                if (startIndex_0 < startIndex_1){
                    result.put("startTime", (String) mapList.get(0).get("resolution"));
                    result.put("endTime", (String) mapList.get(1).get("resolution"));
                }else {
                    result.put("startTime", (String) mapList.get(1).get("resolution"));
                    result.put("endTime", (String) mapList.get(0).get("resolution"));
                }
            }
        }
        return result;
    }

    /**
     * 不能识别意图，随机返回一个闲聊
     * @param seesionId
     * @return
     */
    public static Message getSmallTalkResponse(String seesionId) {
        Message msg = new Message();
        Random r = new Random(1);
        String responseText = Kb.smallTalk.get(r.nextInt(100)%Kb.smallTalk.size());
        msg.setSeesionId(seesionId);
        msg.setType("1");
        msg.setResponseText(responseText);
        msg.setVoiceUrl("/wavs/tts_video/bm_notUnderstand.wav");
        return msg;
    }

    /**
     * 判断会议日期时间是否有效
     * @param meetingRoom
     * @return
     */
    public static String isValidMeetingTime(MeetingRoom meetingRoom) throws ParseException {
        String meetingDate = meetingRoom.getMeetingDate().getAttrName();
        String startTime = meetingRoom.getStartTime().getAttrName();
        String endTime = meetingRoom.getEndTime().getAttrName();
        Date currentTime = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String firstTime = dateformat.format(new Date());
        String url1 = "/wavs/tts_video/bm_notHistoryTime.wav";
        String url2 = "/wavs/tts_video/bm_endBeforeStartTime.wav";
        if (meetingDate != null){
            String e1 = meetingDate+" 00:00:00";
            Date eTime1 = df.parse(e1);
            if (eTime1.getTime() < df.parse(firstTime+" 00:00:00").getTime()){
                return "会议日期和时间不能为历史时间！"+"="+url1;
            }
            if (startTime != null && endTime == null){
                String s = meetingDate+" "+startTime;
                Date sTime = df.parse(s);
                if (sTime.getTime() < currentTime.getTime()){
                    return "会议日期和时间不能为历史时间！"+"="+url1;
                }
            }
            if (startTime == null && endTime != null){
                String e = meetingDate+" "+endTime;
                Date eTime = df.parse(e);
                if (eTime.getTime() <= currentTime.getTime()){
                    return "会议日期和时间不能为历史时间！"+"="+url1;
                }
            }
            if (startTime != null && endTime != null){
                String s = meetingDate+" "+startTime;
                String e = meetingDate+" "+endTime;
                if (df.parse(s).getTime() >= df.parse(e).getTime()){
                    return "会议开始时间不能大于等于结束时间！"+"="+url2;
                }
            }
        }else {
            if (startTime != null && endTime != null){
                String s = firstTime+" "+startTime;
                String e = firstTime+" "+endTime;
                if (df.parse(s).getTime() >= df.parse(e).getTime()){
                    return "会议开始时间不能大于等于结束时间！"+"="+url2;
                }
            }
        }
        return null;
    }
}
