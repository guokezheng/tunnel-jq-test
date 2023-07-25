package com.tunnel.platform.controller.energyManagement.enumeration;

/**
 * 用电报表日期类型
 *
 * @date: 2023/7/17 10:37
 * @author: why
 * @version: 1.0
 */
public enum EnumElectricityReport {
    /**
     * 日 周 月 年
     */
    day("day"),week("week"),month("month"),year("year");

    private String type;

    EnumElectricityReport(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Integer enumToInt(com.tunnel.platform.controller.energyManagement.enumeration.EnumElectricityReport type) {
        switch (type) {
            case week:
                return 3;
            case month:
                return 1;
            case year:
                return 2;
            case day:
            default:
                return 0;
        }
    }
}
