package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 设备类型数据项表
 */
public enum DevicesBrandEnum {

    HUI_ER_SHI("0001", "惠尔视"),
    TONG_DA("0002", "同大"),
    DING_SHANG("0003", "鼎商"),
    SU_TENG("0004", "速腾"),
    DA_TANG_GAO_HONG("0005", "大唐高鸿"),
    SAN_SI("0006", "三思"),
    YU_SHI("0007", "宇视"),
    TAI_XIN("0008", "太新"),
    QI_LU_TONG("0009", "齐鲁通"),
    NAN_JING_DING_EN("0010", "南京鼎恩"),
    DIAN_MING_TONG_ZHOU("0011", "电明同洲"),
    TIAN_JIN_GUANG_DIAN("0012", "天津光电"),
    HAI_KANG_WEI_SHI("0013", "海康威视"),
    DA_HUA("0014", "大华"),
    TAI_XING("0015", "钛星"),
    MAN_DE_KE("0016", "曼德克"),
    SHANG_HAI_XUN_FEI("0017", "上海勋飞"),
    TIAN_XING_BEI_DOU("0018", "天星北斗"),
    SHAN_GAO_JIAN_CE("0019", "山高检测中心"),
    JIAO_KE_YUAN("0020", "交科院"),
    GONG_KE_FEI_DA("0021", "公科飞达"),
    BU_LASER("0022", "Bu-Laser"),
    SHAN_DONG_HUA_KE("0023", "山东华科"),
    ZHAO_SHANG_XIN_ZHI("0024", "招商新智"),
    HANG_ZHOU_RONG_PAN("0025", "杭州融磐"),
    DE_GUAN_LONG("0026", "河北德冠隆"),
    FEI_BO_SAI_SI("0027", "山东飞博赛斯"),
    SHEN_ZHEN_QIU_TIAN("0028", "深圳秋田"),
    USREGAL("0029", "USRegal"),
    AO_BANG("0030", "奥邦"),
    DING_XUN("0031", "鼎讯"),
    SHAN_DONG_GAO_SU_XIN_XI("0032", "山东高速信息集团"),
    HUA_RUI_JIAO_TONG("0033", "华睿交通科技"),
    JIANG_SU_SHU_ZHI_YUAN("0034", "江苏数智元"),
    KUANG_LI("0035", "匡力"),
    LU_AN("0036", "路安"),
    SHEN_ZHEN_HUA_LU_AN("0037", "深圳华路安 "),
    ZHE_JIANG_GAO_SU_XIN_XI("0038", "浙江高速信息"),
    ZHONG_GUO_YI_DONG("0039", "中国移动"),
    ZHONG_LU_ZHI_LIAN("0040", "中路智链科技"),
    BEI_JING_AN_HANG_DA("0041", "北京安航达"),
    LAN_YU_JI_GUANG("0042", "东莞市蓝宇激光"),
    XIAN_CE_DAO_KE("0043", "天津纤测道客"),
    BAO_LUN_DIAN_ZI("0044", "保伦电子"),
    SAI_KANG_JIAO_AN("0045", "赛康交安"),
    XIN_KE_FU_HE_PING("0046", "信可复合屏"),
    HUA_WEI("0048", "华为"),
    XIN_TONG_ZHI_HE("0049", "信通智合"),
    GUANG_DIAN_BI_TE("0050", "光电比特"),
    ALI("0052", "阿里"),
    WAN_JI("0053", "万集"),
    REN_ZHI_XIN_XI("0054", "仁智信息"),
    JIANG_SU_XIN_AI_NENG("0055", "江苏新爱能"),
    OU_MU_LONG("0056", "欧姆龙"),
    SHAN_DONG_ZHENG_CHEN("0057", "山东正晨"),
    SHANG_HAI_XUN_GUANG("0058", "上海勋光"),
    SHEN_ZHEN_XIAN_KE("0059", "深圳显科"),
    SHEN_ZHEN_XING_DIAN("0060", "深圳兴电"),
    XI_HE_CAI_QIAO("0061", "西核彩桥"),
    SAN_JING("0062", "三晶"),
    HONG_MENG("0063", "鸿蒙"),
    ZHUO_SHI_ZHI_TONG("086d2f094ea64d759e5eda92f71392db", "卓视智通");


    private String code;
    private String name;

    DevicesBrandEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(String code) {
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

        for(DevicesBrandEnum typeEnum: DevicesBrandEnum.values()){

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
        for (DevicesBrandEnum value : DevicesBrandEnum.values()) {
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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
