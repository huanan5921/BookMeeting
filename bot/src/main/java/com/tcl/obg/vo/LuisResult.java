package com.tcl.obg.vo;/*
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

import java.util.List;
import java.util.Map;

public class LuisResult {

    private String query;

    private Map<String, Object> topIntent;

    private List<Map<String, Object>> intents;

    private List<Map<String, Object>> entities;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getTopIntent() {
        return topIntent;
    }

    public void setTopIntent(Map<String, Object> topIntent) {
        this.topIntent = topIntent;
    }

    public List<Map<String, Object>> getIntents() {
        return intents;
    }

    public void setIntents(List<Map<String, Object>> intents) {
        this.intents = intents;
    }

    public List<Map<String, Object>> getEntities() {
        return entities;
    }

    public void setEntitys(List<Map<String, Object>> entities) {
        this.entities = entities;
    }

}
