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

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitServer implements CommandLineRunner{


    @Override
    public void run(String... strings) throws Exception {
        initSmallTalk();
        initMeetingRooms();
    }

    public void initSmallTalk(){
        Kb.smallTalk.add("对不起，我没有听懂你在说什么，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("我没有听懂，请再说一遍，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("对不起，我现在还在学习中，我现在只能处理会议室预定流程");
        Kb.smallTalk.add("听不懂，我现在只能处理会议室预定流程");

    }

    public void initMeetingRooms(){
        Kb.meetingRooms.add("深圳 TCL电子大厦 2楼 胡志明会议室");
        Kb.meetingRooms.add("深圳 TCL电子大厦 9楼 一号会议室");
        Kb.meetingRooms.add("深圳 TCL电子大厦 11楼 VIP会议室");
        Kb.meetingRooms.add("深圳 TCL电子大厦 12楼 摩尔会议室");
    }
}
