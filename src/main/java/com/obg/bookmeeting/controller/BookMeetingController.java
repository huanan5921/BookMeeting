package com.obg.bookmeeting.controller;/*
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

import com.obg.bookmeeting.service.BookMeetingService;
import com.obg.bookmeeting.vo.MeetingRoom;
import com.obg.bookmeeting.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class BookMeetingController {

    @Autowired
    public BookMeetingService bookMeetingService;

    @GetMapping("/index")
    public String index(){
        return "index"; //当浏览器输入/index时，会返回 /static/home.html的页面
    }


    @RequestMapping("/bookMeeting")
    @ResponseBody
    public Message bookMeeting(String seesionId, String attrId, String text){

        Message message = bookMeetingService.bookMeeting(seesionId, attrId, text);

        return message;
    }

}
