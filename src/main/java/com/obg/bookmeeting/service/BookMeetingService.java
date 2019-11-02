package com.obg.bookmeeting.service;/*
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

import com.obg.bookmeeting.vo.MeetingRoom;
import com.obg.bookmeeting.vo.Message;

public interface BookMeetingService {

    /**
     * 预定会议室处理
     * @param seesionId  当前意图会话Id标识
     * @param requestText   请求文本
     * @return
     */
    Message bookMeeting(String seesionId, String attrId, String requestText);

}
