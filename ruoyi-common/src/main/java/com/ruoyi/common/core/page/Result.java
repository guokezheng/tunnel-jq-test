package com.ruoyi.common.core.page;

import com.ruoyi.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * describe:操作消息提醒
 *  AjaxResult【在swagger文档中不显示响应参数】
 *  添加此类方便在swagger文档中展示响应参数
 * @author zs
 * @date 2022/8/5
 */
@ApiModel(value = "Result", description = "操作消息提醒")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("返回内容")
    private String msg;

    @ApiModelProperty("数据对象")
    private T data;

    public static <T> Result<T> success()
    {
        return restResult(null, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data)
    {
        return restResult(data, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> Result<T> success( String msg,T data)
    {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    public static <T> Result<T> error()
    {
        return restResult(null, HttpStatus.ERROR, "操作失败");
    }

    public static <T> Result<T> error(String msg)
    {
        return restResult(null, HttpStatus.ERROR, msg);
    }

    public static <T> Result<T> error(T data)
    {
        return restResult(data, HttpStatus.ERROR, "操作失败");
    }

    public static <T> Result<T> error(String msg,T data)
    {
        return restResult(data, HttpStatus.ERROR, msg);
    }

    public static <T> Result<T> error(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    public static Result toResult(int rows){
        return rows > 0 ? Result.success() : Result.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    public static Result toResult(boolean result){
        return result ? Result.success() : Result.error();
    }

}
