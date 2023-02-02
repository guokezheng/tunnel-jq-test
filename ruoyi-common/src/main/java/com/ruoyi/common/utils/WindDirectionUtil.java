package com.ruoyi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhai
 * @date 2023/2/1
 */
public class WindDirectionUtil {

    private static final Logger log = LoggerFactory.getLogger(WindDirectionUtil.class);

    //风向描述类型一
    private static final String[] directArrOne = new String[]{
            "北", "东北偏北", "东北", "东北偏东",
            "东", "东南偏东", "东南", "东南偏南",
            "南", "西南偏南", "西南", "西南偏西",
            "西", "西北偏西", "西北", "西北偏北"};

    //风向描述类型二
    private static final String[] directArrTwo = new String[]{
            "北", "北东北", "东北", "东东北",
            "东", "东东南", "东南", "南东南",
            "南", "南西南", "西南", "西西南",
            "西", "西西北", "西北", "北西北"};

    /**
     * 风向角度转具体风向
     * @param degrees 风向角度 0 <= degrees <= 360
     * @return 具体风向
     */
    public static String windDirectionSwitch(float degrees) {
        int index = 0;
        if (348.75 <= degrees && degrees <= 360) {
            index = 0;
        } else if (0 <= degrees && degrees <= 11.25) {
            index = 0;
        } else if (11.25 < degrees && degrees <= 33.75) {
            index = 1;
        } else if (33.75 < degrees && degrees <= 56.25) {
            index = 2;
        } else if (56.25 < degrees && degrees <= 78.75) {
            index = 3;
        } else if (78.75 < degrees && degrees <= 101.25) {
            index = 4;
        } else if (101.25 < degrees && degrees <= 123.75) {
            index = 5;
        } else if (123.75 < degrees && degrees <= 146.25) {
            index = 6;
        } else if (146.25 < degrees && degrees <= 168.75) {
            index = 7;
        } else if (168.75 < degrees && degrees <= 191.25) {
            index = 8;
        } else if (191.25 < degrees && degrees <= 213.75) {
            index = 9;
        } else if (213.75 < degrees && degrees <= 236.25) {
            index = 10;
        } else if (236.25 < degrees && degrees <= 258.75) {
            index = 11;
        } else if (258.75 < degrees && degrees <= 281.25) {
            index = 12;
        } else if (281.25 < degrees && degrees <= 303.75) {
            index = 13;
        } else if (303.75 < degrees && degrees <= 326.25) {
            index = 14;
        } else if (326.25 < degrees && degrees < 348.75) {
            index = 15;
        } else {
            log.error("风向角度[{}] 大于 360", degrees);
        }
        return directArrOne[index];
    }
}
