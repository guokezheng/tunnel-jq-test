package com.tunnel.business.domain.energyManagement;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 分时段报表Dto
 *
 * @date: 2022/3/19 15:58
 * @author: why
 * @version: 1.0
 */
public class SplitTimeDto {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 尖用电电量
     */
    private Double jValue;

    /**
     * 峰用电电量
     */
    private Double fValue;

    /**
     * 平用电电量
     */
    private Double pValue;

    /**
     * 谷用电电量
     */
    private Double gValue;

    /**
     * 深谷用电电量
     */
    private Double sValue;

    /**
     * 尖单价
     */
    private Double jPrice = 1.20;

    /**
     * 峰单价
     */
    private Double fPrice = 1.06;

    /**
     * 平单价
     */
    private Double pPrice = 0.72;

    /**
     * 谷单价
     */
    private Double gPrice = 0.37;

    /**
     * 深谷单价
     */
    private Double sPrice = 0.28;

    /**
     * 总用电电量
     */
    private Double sumValue;

    /**
     * 总金额
     */
    private Double sumPrice;

    /**
     * 尖总金额
     */
    private Double sumJPrice;
    /**
     * 峰总金额
     */
    private Double sumFPrice;
    /**
     * 平总金额
     */
    private Double sumPPrice;
    /**
     * 谷总金额
     */
    private Double sumGPrice;
    /**
     * 深总金额
     */
    private Double sumSPrice;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getjValue() {
        return jValue;
    }

    public void setjValue(Double jValue) {
        this.jValue = jValue;
    }

    public Double getfValue() {
        return fValue;
    }

    public void setfValue(Double fValue) {
        this.fValue = fValue;
    }

    public Double getpValue() {
        return pValue;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public Double getgValue() {
        return gValue;
    }

    public void setgValue(Double gValue) {
        this.gValue = gValue;
    }

    public Double getjPrice() {
        return jPrice;
    }

    public void setjPrice(Double jPrice) {
        this.jPrice = jPrice;
    }

    public Double getfPrice() {
        return fPrice;
    }

    public void setfPrice(Double fPrice) {
        this.fPrice = fPrice;
    }

    public Double getpPrice() {
        return pPrice;
    }

    public void setpPrice(Double pPrice) {
        this.pPrice = pPrice;
    }

    public Double getgPrice() {
        return gPrice;
    }

    public void setgPrice(Double gPrice) {
        this.gPrice = gPrice;
    }

    public Double getSumValue() {
        return sumValue;
    }

    public void setSumValue(Double sumValue) {
        this.sumValue = sumValue;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Double getsValue() {
        return sValue;
    }

    public void setsValue(Double sValue) {
        this.sValue = sValue;
    }

    public Double getsPrice() {
        return sPrice;
    }

    public void setsPrice(Double sPrice) {
        this.sPrice = sPrice;
    }

    public Double getSumJPrice() {
        return sumJPrice;
    }

    public void setSumJPrice(Double sumJPrice) {
        this.sumJPrice = sumJPrice;
    }

    public Double getSumFPrice() {
        return sumFPrice;
    }

    public void setSumFPrice(Double sumFPrice) {
        this.sumFPrice = sumFPrice;
    }

    public Double getSumPPrice() {
        return sumPPrice;
    }

    public void setSumPPrice(Double sumPPrice) {
        this.sumPPrice = sumPPrice;
    }

    public Double getSumGPrice() {
        return sumGPrice;
    }

    public void setSumGPrice(Double sumGPrice) {
        this.sumGPrice = sumGPrice;
    }

    public Double getSumSPrice() {
        return sumSPrice;
    }

    public void setSumSPrice(Double sumSPrice) {
        this.sumSPrice = sumSPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("name", name)
                .append("jValue", jValue)
                .append("fValue", fValue)
                .append("pValue", pValue)
                .append("gValue", gValue)
                .append("sValue", sValue)
                .append("jPrice", jPrice)
                .append("fPrice", fPrice)
                .append("pPrice", pPrice)
                .append("gPrice", gPrice)
                .append("sPrice", sPrice)
                .append("sumValue", sumValue)
                .append("sumPrice", sumPrice)
                .append("sumJPrice", sumJPrice)
                .append("sumFPrice", sumFPrice)
                .append("sumPPrice", sumPPrice)
                .append("sumGPrice", sumGPrice)
                .append("sumSPrice", sumSPrice)
                .toString();
    }
}
