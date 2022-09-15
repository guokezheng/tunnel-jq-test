package com.tunnel.platform.business.vms.display;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.business.vms.core.IDeviceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonDeviceDisplay implements IDeviceDisplay {

    private static final Logger logger = LoggerFactory.getLogger(CommonDeviceDisplay.class);

    @Override
    public String getCurrentBrightness(String responseContent) {
        return null;
    }

    @Override
    public List<String> getDeviceFault(String responseContent) {
        return null;
    }

    @Override
    public String getCurrentDisplay(String responseContent) {
        return null;
    }

    @Override
    public String getPlayListDisplay(String responseContent) {
        return null;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static Boolean isMessyCode(String strName) {

        Boolean isMessy = false;
        try {

            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            String temp1 = temp.replaceAll("\\+", "").replaceAll("\\-", "").replaceAll("\\=", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
            char[] ch = temp1.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        isMessy = true;
                        return isMessy;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMessy;
    }

    /**
     * 判断字符串是否为空
     *
     * @param result 字符串
     * @return 是否是乱码
     */
    public static Boolean isEmptyCode(String result) {
        List<Object> allDisplayList = new ArrayList<Object>();
        Boolean isEmpty = false;
        JSONArray info = JSONArray.parseArray(result);
        if (info == null || info.size() == 0) {//返回内容为空
            return true;
        } else {//拼接item
            for (int i = 0; i < info.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象ITEM000
                JSONObject job = info.getJSONObject(i);
                JSONArray item = null;
                if (i < 10) {
                    item = job.getJSONArray("ITEM00" + i);
                } else {
                    item = job.getJSONArray("ITEM0" + i);

                }
                for (int j = 0; j < item.size(); j++) {
                    JSONObject itemjob = item.getJSONObject(j);
                    allDisplayList.add(itemjob.get("CONTENT"));
                }
            }
            if (allDisplayList == null && allDisplayList.size() == 0) {
                return true;
            }
        }
        return isEmpty;
    }
}