package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.StringUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 * 
 * @author tunnel
 */
public class AjaxResultb extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResultb()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public AjaxResultb(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResultb(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static AjaxResultb success()
    {
        return AjaxResultb.success("操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static AjaxResultb success(Object data)
    {
        return AjaxResultb.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResultb success(String msg)
    {
        return AjaxResultb.success(msg, null);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResultb success(String msg, Object data)
    {
        return new AjaxResultb(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return
     */
    public static AjaxResultb error()
    {
        return AjaxResultb.error("操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResultb error(String msg)
    {
        return AjaxResultb.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResultb error(String msg, Object data)
    {
        return new AjaxResultb(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResultb error(int code, String msg)
    {
        return new AjaxResultb(code, msg, null);
    }

    public class Type {
        public static final int SUCCESS = 1;
        public static final int WARN = 2;
        public static final int ERROR = 3;
    }
}
