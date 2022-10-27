package com.tunnel.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GLZInfoConfig {
    public static String wzbHost;

    public static String thHost;

    public static String mzHost;

    public static String ytsHost;

    public static String jlyHost;

    public static String wzbDeptId;

    public static String thDeptId;

    public static String mzDeptId;

    public static String ytsDeptId;

    public static String jlyDeptId;


    public static String getWzbHost() {
        return wzbHost;
    }

    @Value("${authorize.glz_host.wzb_glz_host}")
    public void setWzbHost(String wzbHost) {
        this.wzbHost = wzbHost;
    }

    public static String getThHost() {
        return thHost;
    }

    @Value("${authorize.glz_host.th_glz_host}")
    public void setThHost(String thHost) {
        this.thHost = thHost;
    }

    public static String getMzHost() {
        return mzHost;
    }

    @Value("${authorize.glz_host.mz_glz_host}")
    public void setMzHost(String mzHost) {
        this.mzHost = mzHost;
    }

    public static String getYtsHost() {
        return ytsHost;
    }

    @Value("${authorize.glz_host.yts_glz_host}")
    public void setYtsHost(String ytsHost) {
        this.ytsHost = ytsHost;
    }

    public static String getJlyHost() {
        return jlyHost;
    }

    @Value("${authorize.glz_host.jly_glz_host}")
    public void setJlyHost(String jlyHost) {
        this.jlyHost = jlyHost;
    }

    public static String getWzbDeptId() {
        return wzbDeptId;
    }

    @Value("${authorize.dept_id.wzb_glz_dept_id}")
    public void setWzbDeptId(String wzbDeptId) {
        this.wzbDeptId = wzbDeptId;
    }

    public static String getThDeptId() {
        return thDeptId;
    }

    @Value("${authorize.dept_id.th_glz_dept_id}")
    public void setThDeptId(String thDeptId) {
        this.thDeptId = thDeptId;
    }

    public static String getMzDeptId() {
        return mzDeptId;
    }

    @Value("${authorize.dept_id.mz_glz_dept_id}")
    public void setMzDeptId(String mzDeptId) {
        this.mzDeptId = mzDeptId;
    }

    public static String getYtsDeptId() {
        return ytsDeptId;
    }

    @Value("${authorize.dept_id.yts_glz_dept_id}")
    public void setYtsDeptId(String ytsDeptId) {
        this.ytsDeptId = ytsDeptId;
    }

    public static String getJlyDeptId() {
        return jlyDeptId;
    }

    @Value("${authorize.dept_id.jly_glz_dept_id}")
    public void setJlyDeptId(String jlyDeptId) {
        this.jlyDeptId = jlyDeptId;
    }
}
