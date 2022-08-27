package com.tunnel.platform.datacenter.domain.enumeration;

/**
 * 设备类型
 *
 * @author yangqichao
 * @date 2019/9/18 14:45
 */
public enum DevicesTypeEnum {

    PU_TONG_CHE_ZHI(1, "普通车指"),
    TU_SHU_CHE_ZHI(2, "特殊车指"),
    JIAO_TONG_XIN_HAO_DENG(3, "交通信号灯"),
    ZUO_JIAO_TONG_XIN_HAO_DENG(4, "带左转交通信号灯"),
    DONG_NEI_LIANG_DU(5, "洞内亮度"),
    DONG_WAI_LIANG_DU(6, "洞外亮度"),
    JIA_QIANG_ZHAO_MING(7, "加强照明"),
    YIN_DAO_ZHAO_MING(8, "引道照明"),
    JI_BEN_ZHAO_MING(9, "基本照明"),
    FENG_JI_1(10, "风机1"),
    FENG_JI_2(11, "风机2"),
    PENG_DONG_ZHAO_MING(12, "棚洞照明"),
	FENG_XIANG(13,"风向"),
	CO_JIAN_CE(14,"CO检测器"),
	VI_JIAN_CE(15,"VI检测器"),
	FENG_SU_JIAN_CE(16,"风速检测器"),
	JUAN_LIAN_MEN(17,"卷帘门"),
    SHUI_BENG(18,"水泵"),
    SHUI_BENG_ZHUANGTAI(19,"手自动状态"),
    SHUI_BENG_YEWEI(20,"液位");


    private int code;
    private String name;

    DevicesTypeEnum(int code, String name) {
        this.code = code;
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

        for(DevicesTypeEnum typeEnum: DevicesTypeEnum.values()){

            if(code.equals(typeEnum.code)){
                return true;
            }

        }

        return false;
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


        if(type == PU_TONG_CHE_ZHI.code){

            switch (point){
                case 1:
                    return "正绿反红";
                case 2:
                    return "正红反绿";
                case 3:
                    return "正红反红";
                case 4:
                    return "正反关闭";
            }
        }else if(type == TU_SHU_CHE_ZHI.code){


            switch (point){
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

        }else if(type == JIAO_TONG_XIN_HAO_DENG.code){

            switch (point){
                case 1:
                    return "绿灯亮";
                case 2:
                    return "黄灯亮";
                case 3:
                    return "红灯亮";
                case 4:
                    return "全灭";
            }
        } else if(type == ZUO_JIAO_TONG_XIN_HAO_DENG.code){

            switch (point){
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
        } else if(type == JIA_QIANG_ZHAO_MING.code){

            switch (point){
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        }else if(type == YIN_DAO_ZHAO_MING.code){

            switch (point){
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        }else if(type == JI_BEN_ZHAO_MING.code){

            switch (point){
                case 1:
                    return "照明关闭";
                case 0:
                    return "照明打开";
            }
        }else if(type == FENG_JI_1.code){

            switch (point){
                case 1:
                    return "风机1正转启动";
                case 8:
                    return "风机1反转启动";
            }
        }else if(type == FENG_JI_2.code){

            switch (point){

                case 2:
                    return "风机2正转启动";
                case 10:
                    return "风机2反转启动";
            }
        }

        return "";
    }

    public static String getPointName(Integer code, char[] point) {

        if (!contains(code)) {
            return null;
        }

        int type = code.intValue();


        if (type == PU_TONG_CHE_ZHI.code) {

            if (point[0] == '1'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '1' ){

                return "正绿反红";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '1'
                    && point[3] == '0' ){

                return "正红反绿";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '0'
                    && point[3] == '1' ){

                return "正红反红";
            }

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '0' ){

                return "正反关闭";
            }


        } else if (type == TU_SHU_CHE_ZHI.code) {

            if (point[0] == '1'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '1'
                    && point[4] == '0' ){

                return "正绿反红";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '1'
                    && point[3] == '0'
                    && point[4] == '0' ){

                return "正红反绿";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '0'
                    && point[3] == '1'
                    && point[4] == '0' ){

                return "正红反红";
            }

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '0'
                    && point[4] == '0' ){

                return "正反关闭";
            }


            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '1'
                    && point[4] == '1' ){

                return "左转绿灯";
            }


        } else if (type == JIAO_TONG_XIN_HAO_DENG.code) {

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '1'){

                return "绿灯";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '0'){

                return "黄灯";
            }

            if (point[0] == '1'
                    && point[1] == '0'
                    && point[2] == '0'){

                return "红灯";
            }


        } else if (type == ZUO_JIAO_TONG_XIN_HAO_DENG.code) {

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '1'
                    && point[3] == '0'){

                return "绿灯";
            }

            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '0'
                    && point[3] == '0'){

                return "黄灯";
            }

            if (point[0] == '1'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '0'){

                return "红灯";
            }

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '1'){

                return "左转";
            }

        } else if (type == JIA_QIANG_ZHAO_MING.code) {

            if (point[0] == '0'){

                return "关";
            }

            if (point[1] == '0'){

                return "开";
            }

        } else if (type == YIN_DAO_ZHAO_MING.code) {

            if (point[0] == '0'){

                return "关";
            }

            if (point[1] == '0'){

                return "开";
            }


        } else if (type == JI_BEN_ZHAO_MING.code) {

            if (point[0] == '0'){

                return "关";
            }

            if (point[1] == '0'){

                return "开";
            }


        } else if (type == FENG_JI_1.code) {

            if (point[0] == '1'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '0'){

                return "自动";
            }
            if (point[0] == '0'
                    && point[1] == '1'
                    && point[2] == '0'
                    && point[3] == '0'){

                return "正转";
            }

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '1'
                    && point[3] == '0'){

                return "故障";
            }

            if (point[0] == '0'
                    && point[1] == '0'
                    && point[2] == '0'
                    && point[3] == '1'){

                return "反转";
            }
        }


        return "";

    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
