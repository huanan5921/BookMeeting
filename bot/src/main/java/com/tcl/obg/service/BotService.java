package com.tcl.obg.service;/*
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

import com.tcl.obg.vo.LuisResult;
import com.tcl.obg.vo.MeetingRoom;
import com.tcl.obg.vo.Message;

public interface BotService {

    /**
     * 机器人处理入口
     * @param seesionId  当前意图会话Id标识
     * @param attrId 当前需要获得的属性
     * @param requestText   请求文本
     * @return
     */
    Message botProcess(String seesionId, String attrId, String requestText);

    /**
     * 用户长时间没有回应，清楚session
     * @param seesionId
     * @return
     */
    String clearSeesion(String seesionId);

    /**
     * 从响应中获取参数，放入会议室参数实体
     * @param luisResult
     * @param meetingRoom
     */
    public void updataMeetingRoomPatameters(LuisResult luisResult, MeetingRoom meetingRoom);
}
