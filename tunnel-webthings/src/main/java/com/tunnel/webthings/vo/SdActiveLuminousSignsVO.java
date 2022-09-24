package com.tunnel.webthings.vo;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.domain.ActiveLuminousSigns;
import com.tunnel.webthings.domain.ReceiveTopic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/7/19 9:54
 * 主动发光标志业务类
 */
@ApiModel("主动发光标志业务类")
public class SdActiveLuminousSignsVO extends ReceiveTopic {

    @ApiModelProperty("拓展")
    private ActiveLuminousSigns expands;

    private String ex;

    public void setEx(ActiveLuminousSigns expands) {
        JSONObject jsonObject = JSONUtil.parseObj(expands);
        this.ex = jsonObject.toString();
    }

    public ActiveLuminousSigns getExpands() {
        return expands;
    }

    public void setExpands(ActiveLuminousSigns expands) {
        this.expands = expands;
    }

    @Override
    public String getEx() {
        return ex;
    }

    @Override
    public void setEx(String ex) {
        this.ex = ex;
    }

    @Override
    public String toString() {
        return "SdActiveLuminousSignsVO{" +
                "expands=" + expands +
                ", ex='" + ex + '\'' +
                '}';
    }
}
