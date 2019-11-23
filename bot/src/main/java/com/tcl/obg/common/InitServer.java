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

import net.sf.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class InitServer implements CommandLineRunner{

    public static void main(String[] args) {
        InitServer initServer = new InitServer();
        String info = initServer.getBookMeetingParameters();
        System.out.println(info);
    }

    @Override
    public void run(String... strings) throws Exception {
        initSmallTalk();
        initMeetingRooms();
        initWakeUpResponse();
        initSayHelloResponse();

        //初始化会议室参数静态实体
        getBookMeetingParameters();
    }

    public String getBookMeetingParameters() {
        String msg = "";
        InputStreamReader intput = null;
        try {
            Resource resource = new ClassPathResource("bookMeetingDateParas.txt");
            intput = new InputStreamReader(resource.getInputStream());
            BufferedReader reader = new BufferedReader(intput);
            String line = "";
            while((line = reader.readLine())!=null){
                if (!"".equals(line.trim())){
                    line = ascii2native(line);
                    String[] split = line.split("=");
                    String val = split[0];
                    String text = split[1];
                    String[] texts = text.split(",");
                    for (String s : texts){
                        if (s != null && !"".equals(s.trim())){
                            Kb.dateEntities.add(s.trim());
                            Kb.dateEntityMap.put(s.trim(), val);
                        }
                    }
                    msg += line+"\n";
                }
            }

            resource = new ClassPathResource("bookMeetingTimeParas.txt");
            intput = new InputStreamReader(resource.getInputStream());
            reader = new BufferedReader(intput);
            while((line = reader.readLine())!=null){
                if (!"".equals(line.trim())){
                    line = ascii2native(line);
                    String[] split = line.split("=");
                    String val = split[0];
                    String text = split[1];
                    String[] texts = text.split(",");
                    for (String s : texts){
                        if (s != null && !"".equals(s.trim())){
                            Kb.timeEntities.add(s.trim());
                            Kb.timeEntityMap.put(s.trim(), val);
                            String[] split1 = val.split(":");
                            int oclock = Integer.valueOf(split1[0]) + 12;
                            String hour = String.valueOf(oclock);
                            if (oclock == 24) {
                                hour = "00";
                            }
                            String time = hour+":"+split1[1]+":"+split1[2];

                            Kb.timeEntities.add("下午"+s.trim());
                            Kb.timeEntityMap.put("下午"+s.trim(), time);

                            Kb.timeEntities.add("晚上"+s.trim());
                            Kb.timeEntityMap.put("晚上"+s.trim(), time);
                        }
                    }
                    msg += line+"\n";
                }
            }
            resource = new ClassPathResource("meetingRooms.txt");
            intput = new InputStreamReader(resource.getInputStream());
            reader = new BufferedReader(intput);
            while((line = reader.readLine())!=null){
                if (!"".equals(line.trim())){
                    line = ascii2native(line);
                    String[] split = line.split("=");
                    String val = split[0];
                    String text = split[1];
                    String[] texts = text.split(",");
                    for (String s : texts){
                        if (s != null && !"".equals(s.trim())){
                            Kb.meetingRoomEntities.add(s.trim());
                            Kb.meetingRoomEntityMap.put(s.trim(), val);
                        }
                    }
                    msg += line+"\n";
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return msg;
    }

    /**
     * 字符串中存在的unicode编码转为汉字
     * @param asciicode
     * @return
     */
    private static String ascii2native ( String asciicode )
    {
        String[] asciis = asciicode.split ("\\\\u");
        String nativeValue = asciis[0];
        try
        {
            for ( int i = 1; i < asciis.length; i++ )
            {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt (code.substring (0, 4), 16);
                if (code.length () > 4)
                {
                    nativeValue += code.substring (4, code.length ());
                }
            }
        }
        catch (NumberFormatException e)
        {
            return asciicode;
        }
        return nativeValue;
    }

    public void initSmallTalk(){
        Kb.smallTalk.add("对不起，我没有听懂你在说什么，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("我没有听懂，请再说一遍，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("对不起，我现在还在学习中，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("听不懂，我现在只能处理会议室预定流程");

    }

    public void initMeetingRooms(){
        JSONObject o1 = new JSONObject();
        o1.accumulate("fdID", "141e90455a545c3945ebf254e13855cd");
        o1.accumulate("fdName", "OBC(2楼A区)马尼拉厅");
        JSONObject o2 = new JSONObject();
        o2.accumulate("fdID", "145076bed446629de503a84460498202");
        o2.accumulate("fdName", "9楼A区2号会议室");
        JSONObject o3 = new JSONObject();
        o3.accumulate("fdID", "141e9081efa5c686755fe434860b82a1");
        o3.accumulate("fdName", "OBC(2楼B区)雅加达厅");
        JSONObject o4 = new JSONObject();
        o4.accumulate("fdID", "14bd9531e445181cbc404e54475a0e68");
        o4.accumulate("fdName", "9楼B区4号会议室");
        Kb.meetingRooms.add(o1);
        Kb.meetingRooms.add(o2);
        Kb.meetingRooms.add(o3);
        Kb.meetingRooms.add(o4);
    }

    public void initWakeUpResponse(){
        Kb.wakeUpResponse.add("我在听");
    }
    public void initSayHelloResponse(){
        Kb.sayHelloResponse.add("你好，我是小智");
        Kb.sayHelloResponse.add("Hi");
        Kb.sayHelloResponse.add("你好");
    }
}
