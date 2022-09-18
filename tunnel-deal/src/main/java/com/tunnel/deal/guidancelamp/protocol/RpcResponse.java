package com.tunnel.deal.guidancelamp.protocol;

import java.util.List;
import java.util.Map;

/**
 * 响应协议
 */
public class RpcResponse {

    //信息类型  固定通用信息
    private String infoType;

    //设备类型
    private String equipmentTpye;

    //设备编号
    private String equipmentCode;

    //指令类型
    private String instructType;

    //协议版本
    private String protocolCode;

    //指令编号
    private String instructCode;

    // 数据内容
    private List<Map<String,Object>> data;

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getEquipmentTpye() {
        return equipmentTpye;
    }

    public void setEquipmentTpye(String equipmentTpye) {
        this.equipmentTpye = equipmentTpye;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getInstructType() {
        return instructType;
    }

    public void setInstructType(String instructType) {
        this.instructType = instructType;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public String getInstructCode() {
        return instructCode;
    }

    public void setInstructCode(String instructCode) {
        this.instructCode = instructCode;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "infoType='" + infoType + '\'' +
                ", equipmentTpye='" + equipmentTpye + '\'' +
                ", equipmentCode='" + equipmentCode + '\'' +
                ", instructType='" + instructType + '\'' +
                ", protocolCode='" + protocolCode + '\'' +
                ", instructCode='" + instructCode + '\'' +
                ", data=" + data +
                '}';
    }
}
