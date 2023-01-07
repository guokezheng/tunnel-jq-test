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
    JQ_LIGHT_OPENCLOSE(28, "加强照明开关状态"),
    INFORMATION_BOARD_CONTENT(29, "情报板显示内容"),
    CAMERA_ERROR_CONTETN(30, "相机异常事件描述"),
    RADAR_ERROR_CONTETN(31, "雷达异常事件描述"),

    JUAN_LIAN_MEN(32, "卷帘门状态"),
    XIN_HAO_DENG(33, "信号灯状态"),
    ZHUO_ZHUAN_XIN_HAO_DENG(34, "带左转交通信号灯"),
    YING_JI_ZHAO_MING(35, "应急照明"),
    YIN_DAO_ZHAO_MING(36, "引道照明"),
    JI_BEN_ZHAO_MING(37, "基本照明"),
    GUANG_FU_PENG_DONG(38, "光伏棚洞"),
    XIAO_FANG_SHUI_BENG(39, "消防水泵"),
    PLC_ZHU_JI(40, "PLC主机"),
    YE_WEI_CHUAN_GAN_QI(41, "液位传感器"),
    WEI_BO_CHE_LIANG_JIAN_CE_QI(42, "微波车辆检测器"),
    JIN_JI_DIAN_HUA(43, "紧急电话"),
    GUANG_BO(44, "广播"),
    YUN_TAI_QIU_JI(45, "云台摄像机（球机）"),
    ZHUA_PAI_CAMERA(46, "抓拍摄像机"),
    HAO_MI_BO_RADAR(47, "毫米波雷达"),
    YUAN_CHUAN_YA_LI_BIAO(48, "远传压力表"),
    XUN_JIAN_ROBOT(49, "巡检机器人"),
    ZHI_NENG_XIAO_FANG_PAO(50, "智能消防炮"),
    FIRE_ALARN_CONTROL(51, "火灾报警控制器"),
    BIAN_DIAN_SUO(52, "变电所"),
    MEN_JIA_VMS(53, "门架式可变信息标志"),
    ZHOU_LIU_FENG_JI(54, "轴流风机"),
    XIAO_FANG_SHUAN(55, "消防栓"),
    SHENG_GUANG_ALARM(56, "声光报警器报警"),
    EVACUATION_SIGN_CONTROL(58, "智能疏散标志控制器"),
    GUIDANCE_LAMP_CONTROL(59, "智能诱导灯控制器"),
    HONGMENG_CONTROL(60, "鸿蒙控制器"),
    NEI_WAI_ZHEN_DONG_YI_JIAN_CE_QI(61, "内外振动仪检测器"),
    JQ_LIGHT_BRIGHNESS(62, "加强照明亮度"),
    WENSHI_DU_CHUAN_GAN_QI(63, "温湿度传感器"),
    SHUI_JIN_CHUAN_GAN_QI(64, "水浸传感器"),
    DI_PIN_XUN_JIAN_GUI(65, "低频巡检柜"),
    DIAN_BAN_RE(66, "电伴热"),
    JING_SHI_DENG_DAI(67, "警示灯带"),
    DELINEATOR_CONTROL_MODE(68, "轮廓标控制模式"),
    DELINEATOR_BRIGHNESS(69, "轮廓标亮度"),
    DELINEATOR_FREQUENCY(70, "轮廓标频率"),
    DELINEATOR_IS_OPEN(71, "轮廓标是否开灯"),
    DELINEATOR_CONTROL(72, "轮廓标控制器"),
    JI_BEN_ZHAO_MING_OPENCLOSE(73, "基本照明开关状态"),
    JB_LIGHT_BRIGHNESS(74, "基本照明亮度");


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
