package com.tunnel.platform.datacenter.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型数据项表
 */
@Getter
@AllArgsConstructor
public enum DevicesTypeItemEnum {

    CO(1, "CO"),
    VI(2, "VI"),
    FENG_SU(5, "风速"),
    FENG_XIANG(6, "风向"),
    LIANG_DU_INSIDE(7, "洞内亮度检测器"),
    LIANG_DU_OUTSIDE(12, "洞外亮度检测器"),
    PU_TONG_CHE_ZHI(14, "车道指示器"),
    ZHUO_ZHUAN_CHE_ZHI(15, "带左转车道指示器"),
    GUIDANCE_LAMP_CONTROL_MODE(16, "诱导灯控制模式"),
    GUIDANCE_LAMP_BRIGHNESS(17, "诱导灯亮度"),
    GUIDANCE_LAMP_FREQUENCY(18, "诱导灯频率");


    private int code;
    private String name;

    /**
     * 判断是否包含
     * @param code
     * @return
     */
    public static Boolean contains(Integer code){

        if(null == code){
            return false;
        }

        for(DevicesTypeItemEnum typeEnum: DevicesTypeItemEnum.values()){

            if(code.equals(typeEnum.code)){
                return true;
            }

        }

        return false;
    }

    /**
     * 传code返回name
     * @param code
     * @return
     */
    public static String getValue(int code) {
        // 遍历枚举
        for (DevicesTypeItemEnum value : DevicesTypeItemEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }


    /**
     * 获取对应点位名称
     * @return
     */
    public static String getPointName(Integer code, Integer point){

        if(!contains(code)){
            return null;
        }
        int type = code.intValue();
        return "";
    }

    public static String getPointName(Integer code, char[] point) {

        if (!contains(code)) {
            return null;
        }
        int type = code.intValue();
        return "";

    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
