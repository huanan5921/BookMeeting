package com.obg.bookmeeting.vo;/*
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

import java.util.HashMap;

public class Message {

    //意图会话唯一标识
    private String seesionId;

    //返回信息类型 1:消息，2:判断，3:选择会议室，4:预定成功
    private String type;

    //当前需要的属性Id
    private String attrId;

    //消息响应
    private String responseText;

    //返回的数据信息
    private HashMap data;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getSeesionId() {
        return seesionId;
    }

    public void setSeesionId(String seesionId) {
        this.seesionId = seesionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public HashMap getData() {
        return data;
    }

    public void setData(HashMap data) {
        this.data = data;
    }
}
