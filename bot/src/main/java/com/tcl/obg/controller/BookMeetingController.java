package com.tcl.obg.controller;/*
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

import com.tcl.obg.service.BotService;
import com.tcl.obg.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookMeetingController {

    @Autowired
    public BotService botService;

    @GetMapping("/bot")
    public String bot(){
        return "bot"; //当浏览器输入/index时，会返回 /static/home.html的页面
    }


    @RequestMapping("/botProcess")
    @ResponseBody
    public Message botProcess(String seesionId, String attrId, String text){

        Message message = botService.botProcess(seesionId, attrId, text);

        return message;
    }

    @RequestMapping("/clearSeesion")
    @ResponseBody
    public String clearSeesion(String seesionId){

        String flag = botService.clearSeesion(seesionId);


        return flag;
    }

}
