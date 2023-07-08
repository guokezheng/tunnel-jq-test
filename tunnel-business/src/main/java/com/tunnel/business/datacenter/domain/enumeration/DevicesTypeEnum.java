package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 设备类型
 *
 * @author yangqichao
 * @date 2019/9/18 14:45
 */
public enum DevicesTypeEnum {

    PU_TONG_CHE_ZHI(1L, "普通车道指示器"),
    ZHUO_ZHUAN_CHE_ZHI(2L, "带左转车道指示器"),
    JIAO_TONG_XIN_HAO_DENG(3L, "交通信号灯"),
    ZUO_JIAO_TONG_XIN_HAO_DENG(4L, "带左转交通信号灯"),
    LIANG_DU_JIAN_CE(5L, "亮度检测器(洞外)"),
    YING_JI_ZHAO_MING(6L, "应急照明"),
    JIA_QIANG_ZHAO_MING(7L, "加强照明"),
    YIN_DAO_ZHAO_MING(8L, "引道照明"),
    JI_BEN_ZHAO_MING(9L, "基本照明"),
    FENG_JI(10L, "射流风机"),
    PENG_DONG_ZHAO_MING(11L, "光伏棚洞"),
    JUAN_LIAN_MEN(12L, "卷帘门"),
    SHUI_BENG(13L, "消防水泵"),
    PLC(14L, "PLC主机"),
    SHUI_BENG_YE_WEI(15L, "消防水池液位传感器"),
    VMS(16L, "隧道内可变信息标志"),
    FENG_SU_FENG_XIANG(17L, "风速风向检测器"),
    LIANG_DU_JIAN_CE_INSIDE(18L, "亮度检测器(洞内)"),
    CO_VI(19L, "CO/VI检测器"),
    WEI_BO_CHE_JIAN(20L, "微波车辆检测器"),
    ET(21L, "紧急电话"),
    LS(22L, "扬声器"),
    CAMERA_BOX(23L, "固定摄像机（枪机）"),
    CAMERA_DOME(24L, "云台摄像机（球机）"),
    CAMERA_ZP(25L, "抓拍摄像机"),
    LIDAR(26L, "激光雷达"),
    MMW_RADAR(27L, "毫米波雷达"),
    YA_LI_BIAO(28L, "远传压力表"),
    ROBOT(29L, "巡检机器人"),
    SHU_SAN_BIAO_ZHI(30L, "智能疏散标志"),
    YOU_DAO_DENG(31L, "智能诱导灯"),
    SHENG_GUANG_BAO_JING(32L, "声光报警器"),
    XIAO_FANG_PAO(33L, "智能消防炮"),
//    FIRE_ALARM_HOST(34L, "火灾报警主机"),
    BIAN_SIAN_SUO(35L, "变电所"),
    MEN_JIA_VMS(36L, "门架式可变信息标志"),
    ZHOU_LIU_FENG_JI(37L, "轴流风机"),
    XIAO_FANG_SHUAN(38L, "消防栓"),
    SHOU_BAO(39L, "智能手动报警按钮"),
    HUO_YAN_TAN_CE_QI(40L, "火焰探测器"),
    WEN_SHI_DU_CHUAN_GAN_QI(41L, "温湿度传感器"),
    SHUI_JIN_CHUAN_GAN_QI(42L, "水浸传感器"),
    DI_PIN_XUN_JIAN_GUI(43L, "低频巡检柜"),
    DIAN_BAN_RE(44L, "电伴热"),
    JING_SHI_DENG_DAI(45L, "警示灯带"),
    LUN_KUO_BIAO(46L, "轮廓标"),
    CE_KONG_ZHI_XING_QI(47L, "测控执行器"),
    NEI_WAI_ZHEN_DONG_YI_JIANCEQI(48L, "内外振动仪检测器"),
    SHU_SAN_BIAO_ZHI_CONTROL(1030L, "智能疏散标志控制器"),
    YOU_DAO_DENG_CONTROL(1031L, "智能诱导灯控制器"),
    LUN_KUO_BIAO_CONTROL(1046L, "轮廓标控制器");


    private Long code;
    private String name;

    DevicesTypeEnum(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 判断是否包含
     *
     * @param code
     * @return
     */
    public static Boolean contains(Long code) {

        if (null == code) {
            return false;
        }

        for (DevicesTypeEnum typeEnum : DevicesTypeEnum.values()) {

            if (code.equals(typeEnum.code)) {
                return true;
            }

        }

        return false;
    }

    /**
     * 传code返回name
     *
     * @param code
     * @return
     */
    public static String getValue(Long code) {
        // 遍历枚举
        for (DevicesTypeEnum value : DevicesTypeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }


    /**
     * 获取对应点位名称
     *
     * @return
     */
    public static String getPointName(Long code, Integer point) {

        if (!contains(code)) {
            return null;
        }

        int type = code.intValue();


        if (type == PU_TONG_CHE_ZHI.code) {

            switch (point) {
                case 1:
                    return "正绿反红";
                case 2:
                    return "正红反绿";
                case 3:
                    return "正红反红";
                case 4:
                    return "正反关闭";
            }
        } else if (type == ZHUO_ZHUAN_CHE_ZHI.code) {


            switch (point) {
                case 1:
                    return "正绿反红";
                case 2:
                    return "正红反绿";
                case 3:
                    return "正红反红";
                case 4:
                    return "正反关闭";
                case 5:
                    return "左转绿灯";
            }

        } else if (type == JIAO_TONG_XIN_HAO_DENG.code) {

            switch (point) {
                case 1:
                    return "绿灯亮";
                case 2:
                    return "黄灯亮";
                case 3:
                    return "红灯亮";
                case 4:
                    return "全灭";
            }
        } else if (type == ZUO_JIAO_TONG_XIN_HAO_DENG.code) {

            switch (point) {
                case 1:
                    return "绿灯亮";
                case 2:
                    return "黄灯亮";
                case 3:
                    return "红灯亮";
                case 4:
                    return "全灭";
                case 5:
                    return "左转灯亮";
                case 6:
                    return "左转灯灭";
            }
        } else if (type == JIA_QIANG_ZHAO_MING.code) {

            switch (point) {
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        } else if (type == YIN_DAO_ZHAO_MING.code) {

            switch (point) {
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        } else if (type == JI_BEN_ZHAO_MING.code) {

            switch (point) {
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        } else if (type == FENG_JI.code) {

            switch (point) {
                case 1:
                    return "风机1正转启动";
                case 8:
                    return "风机1反转启动";
            }
        }
//        else if(type == FENG_JI_2.code){
//
//            switch (point){
//
//                case 2:
//                    return "风机2正转启动";
//                case 10:
//                    return "风机2反转启动";
//            }
//        }

        return "";
    }

    public static String getPointName(Long code, char[] point) {

        if (!contains(code)) {
            return null;
        }

        int type = code.intValue();


        if (type == PU_TONG_CHE_ZHI.code) {

            if (point[0] == '1' && point[1] == '0' && point[2] == '0' && point[3] == '1') {

                return "正绿反红";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '1' && point[3] == '0') {

                return "正红反绿";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '0' && point[3] == '1') {

                return "正红反红";
            }

            if (point[0] == '0' && point[1] == '0' && point[2] == '0' && point[3] == '0') {

                return "正反关闭";
            }


        } else if (type == ZHUO_ZHUAN_CHE_ZHI.code) {

            if (point[0] == '1' && point[1] == '0' && point[2] == '0' && point[3] == '1' && point[4] == '0') {

                return "正绿反红";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '1' && point[3] == '0' && point[4] == '0') {

                return "正红反绿";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '0' && point[3] == '1' && point[4] == '0') {

                return "正红反红";
            }

            if (point[0] == '0' && point[1] == '0' && point[2] == '0' && point[3] == '0' && point[4] == '0') {

                return "正反关闭";
            }


            if (point[0] == '0' && point[1] == '0' && point[2] == '0' && point[3] == '1' && point[4] == '1') {

                return "左转绿灯";
            }


        } else if (type == JIAO_TONG_XIN_HAO_DENG.code) {

            if (point[0] == '0' && point[1] == '0' && point[2] == '1') {

                return "绿灯";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '0') {

                return "黄灯";
            }

            if (point[0] == '1' && point[1] == '0' && point[2] == '0') {

                return "红灯";
            }


        } else if (type == ZUO_JIAO_TONG_XIN_HAO_DENG.code) {

            if (point[0] == '0' && point[1] == '0' && point[2] == '1' && point[3] == '0') {

                return "绿灯";
            }

            if (point[0] == '0' && point[1] == '1' && point[2] == '0' && point[3] == '0') {

                return "黄灯";
            }

            if (point[0] == '1' && point[1] == '0' && point[2] == '0' && point[3] == '0') {

                return "红灯";
            }

            if (point[0] == '0' && point[1] == '0' && point[2] == '0' && point[3] == '1') {

                return "左转";
            }

        } else if (type == JIA_QIANG_ZHAO_MING.code) {

            if (point[0] == '0') {

                return "关";
            }

            if (point[1] == '0') {

                return "开";
            }

        } else if (type == YIN_DAO_ZHAO_MING.code) {

            if (point[0] == '0') {

                return "关";
            }

            if (point[1] == '0') {

                return "开";
            }


        } else if (type == JI_BEN_ZHAO_MING.code) {

            if (point[0] == '0') {

                return "关";
            }

            if (point[1] == '0') {

                return "开";
            }


        } else if (type == FENG_JI.code) {

            if (point[0] == '1' && point[1] == '0' && point[2] == '0' && point[3] == '0') {

                return "自动";
            }
            if (point[0] == '0' && point[1] == '1' && point[2] == '0' && point[3] == '0') {

                return "正转";
            }

            if (point[0] == '0' && point[1] == '0' && point[2] == '1' && point[3] == '0') {

                return "故障";
            }

            if (point[0] == '0' && point[1] == '0' && point[2] == '0' && point[3] == '1') {

                return "反转";
            }
        }


        return "";

    }

    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
