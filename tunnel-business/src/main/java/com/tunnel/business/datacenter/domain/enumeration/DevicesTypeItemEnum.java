package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 设备类型数据项表
 */
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
    GUIDANCE_LAMP_FREQUENCY(18, "诱导灯频率"),
    EVACUATION_SIGN_CONTROL_MODE(19, "疏散标志控制模式"),
    EVACUATION_SIGN_BRIGHNESS(20, "疏散标志亮度"),
    EVACUATION_SIGN_FREQUENCY(21, "疏散标志频率"),
    EVACUATION_SIGN_FIREMARK(22, "疏散标志事件标号"),
    GUIDANCE_LAMP_IS_OPEN(23, "诱导灯是否开灯"),
    EVACUATION_SIGN_IS_OPEN(24, "疏散标志是否开灯"),
    FLAME_DETECTOR_ALARM(25, "火焰探测器报警"),
    SHOU_BAO_ALARM(26, "手报报警"),
    FENG_JI_STATUS(27, "风机状态"),
    JIA_QIANG_ZHAO_MING_OPEN_STATUS(28, "加强照明开关状态"),
    INFORMATION_BOARD_CONTENT(29, "情报板显示内容"),
    CAMERA_ERROR_CONTETN(30, "相机异常事件描述"),
    RADAR_ERROR_CONTETN(31, "雷达异常事件描述"),
    JACK_LIGHT_BRIGHNESS(32, "照明灯亮度"),
    JACK_LIGHT_OPENCLOSE(33, "照明灯开与关");


    private int code;
    private String name;

    DevicesTypeItemEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

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
