package com.tunnel.platform.business.vms.model;

import java.util.ArrayList;

/**
 * java后台缓存
 *
 * @author tianjianwei
 * @date 2018-01-18
 */
public class Cache {

    private String key;//缓存ID
    private ArrayList<String> value;//缓存数据
    private long timeOut;//更新时间
    private boolean expired; //是否终止

    public Cache() {
        super();
        setTimeOut(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        setExpired(false);
    }

    public Cache(String key, ArrayList<String> value, long timeOut, boolean expired) {
        this.key = key;
        this.value = value;
        this.timeOut = timeOut;
        this.expired = expired;
    }

    public String getKey() {
        return key;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setKey(String string) {
        key = string;
    }

    public void setTimeOut(long l) {
        timeOut = l;
    }

    public void setValue(ArrayList<String> obj) {
        value = obj;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean b) {
        expired = b;
    }
}