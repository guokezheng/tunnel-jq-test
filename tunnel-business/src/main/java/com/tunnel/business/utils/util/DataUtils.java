package com.tunnel.business.utils.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 情报板数据解析工具类
 *
 */
public class DataUtils {

    private DataUtils() {
    }

    /**
     * 不同 厂家转义符 {{三思、光电},{显科},{电明、同州},{鼎恩}}
     * 例(三思):{'坐标','字体','字体颜色','文本起始符','文本换行符','拼接情报板控制文本时所需换行符'}
     */
    static final String[][] TRANSLATE_SIGN = {{"C", "f", "c", "", "\\\\n", ""}, {"C", "F", "T", "U", "\\\\N", "\\U"}, {"C", "F", "T", "W", "\\\\A", "\\W"}, {"C", "f", "c", "", "\\\\n", "\\n"}};

//    public static JSONObject propertyObj;

    /**
     * 将ITEM文本数据解析成JSON格式数据(ContentToJson)
     *
     * @param itemContent  item字符串内容
     * @param protocolType 解析协议(IDeviceVender常量中的几种)
     * @return
     */
    public static String itemContentToJson(String itemContent, String protocolType) {
        if (itemContent == null || "".equals(itemContent) || itemContent.length() < 5) {
            return itemContent;
        }
        itemContent = itemContent.replaceAll("\n", "<n>");
        itemContent = itemContent.replaceAll("\r", "<r>");
        itemContent = itemContent.replaceAll("\r\n","<r><n>");
        itemContent = itemContent.replaceAll("\\\\n","<br>");
        String splitSign = itemContent.substring(0, 4);//直接和硬件通讯获取的结果
        itemContent = itemContent.substring(itemContent.indexOf("="), itemContent.length());//直接和硬件通讯获取的结果
        JSONArray jsonArrList = new JSONArray();
        int count = -1;
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        String[] contents = {};
        contents = itemContent.split(splitSign);
        for (String content : contents) {
            if ("".equals(content.trim()) || content.indexOf("=") == -1 || content.indexOf("\\") == -1 || content.length() <= 7)
                continue;
            int x = content.indexOf("=");
            String con = content.substring(x + 1, content.length());
            if (",".equals(con.substring(con.length() - 1, con.length()))) {
                con = con.substring(0, con.length() - 1);
            }
            JSONObject jsonObj = contentToJsonByCount(con, protocolType, ++count);
            jsonArrList.add(jsonObj);
        }
        return JSON.toJSONString(jsonArrList, SerializerFeature.DisableCircularReferenceDetect);
    }


    /**
     * 逐条解析ITEM文本并返回JSONObject(ContentToJson)
     *
     * @param content      文本
     * @param protocolType 解析协议
     * @param index        文本index
     * @return
     */
    private static JSONObject contentToJsonByCount(String content, String protocolType, int index) {
        JSONArray jsonArrList = new JSONArray();
        JSONObject obj = new JSONObject();
        String[] contents = content.split("\\\\");
        JSONObject propertyObj = new JSONObject();
        detailContentProcess(contents, protocolType, jsonArrList, propertyObj);
        obj.put("ITEM" + String.format("%03d", index), jsonArrList);
        return obj;
    }

    /**
     * coentent详细内容解析(ContentToJson)
     *
     * @param contents    ITEM属性数组
     * @param jsonArrList
     */
    private static void detailContentProcess(String[] contents, String protocolType, JSONArray jsonArrList, JSONObject propertyObj) {
        Pattern numStartPat = Pattern.compile("[0-9]*");//数字开头正则
        Pattern numEndPat = Pattern.compile("\\d+");//数字结尾正则
        Pattern strContain = Pattern.compile("[\u4e00-\u9fa5]");//是否包含文字
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");//字母开头正正则


        for (int index = 0; index < contents.length; index++) {
            if ("".equals(contents[index])) {
                continue;
            }
            if (numStartPat.matcher(contents[index].charAt(0) + "").matches() && contents[index].indexOf(",") != -1) {
                propertyObj = actionContentToJson(contents[index].split(","), protocolType,propertyObj);
                continue;
            }
            if (!strContain.matcher(contents[index]).find()) {
                 propertyObj = propertyProcess(contents[index], protocolType,propertyObj);
                Boolean state = (Boolean)propertyObj.get("STATE");
                propertyObj.remove("STATE");
                if (state) {
                    continue;
                }

            }
            String textValue = "";
            if (strContain.matcher(contents[index]).find()) {
                if (protocolType.equals("XIANKE_V12") && contents[index].startsWith(TRANSLATE_SIGN[1][3])) {
                    textValue = contents[index].substring(1, contents[index].length());
                } else if ((protocolType.equals("DIANMING_V12") || protocolType.equals("DIANMING_V121") || "TONGZHOU".equals(protocolType)) && contents[index].startsWith(TRANSLATE_SIGN[2][3])) {
                    textValue = contents[index].substring(1, contents[index].length());
                } else if (contents[index].startsWith(TRANSLATE_SIGN[0][0])) {
                    String propertyValue = contents[index].substring(0, 7);
                    propertyProcess(propertyValue, protocolType,propertyObj);
                    textValue = contents[index].substring(7, contents[index].length());
                } else if (contents[index].startsWith(TRANSLATE_SIGN[0][2])) {
                    String propertyValue = contents[index].substring(0, 13);
                    propertyProcess(propertyValue, protocolType,propertyObj);
                    textValue = contents[index].substring(13, contents[index].length());
                } else if (contents[index].startsWith(TRANSLATE_SIGN[0][1])) {
                    String propertyValue = contents[index].substring(0, 6);
                    propertyProcess(propertyValue, protocolType,propertyObj);
                    textValue = contents[index].substring(6, contents[index].length());
                } else {
                    textValue = contents[index];
                }
            }


            if (!"".equals(textValue)) {//文本内容不为空时
                propertyObj.put("CONTENT", textValue);//文本信息赋值


                if (!propertyObj.containsKey("COLOR") || "".equals(propertyObj.getString("COLOR"))) {
                    propertyObj.put("COLOR", "黄色");
                }
                if (!propertyObj.containsKey("SPEED") || "".equals(propertyObj.getString("SPEED"))) {
                    propertyObj.put("SPEED", "1");
                }
                if (!propertyObj.containsKey("FONT_SIZE") || "".equals(propertyObj.getString("FONT_SIZE"))) {
                    propertyObj.put("FONT_SIZE", "2424");
                }
                if (!propertyObj.containsKey("FONT_SIZE") || "".equals(propertyObj.getString("FONT_SIZE"))) {
                    propertyObj.put("FONT_SIZE", "2424");
                }
                if (!propertyObj.containsKey("ACTION") || "".equals(propertyObj.getString("ACTION"))) {
                    if (protocolType.equals("XIANKE_V12")) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals("SANSI_V6") || protocolType.equals("SANSI_V61") || protocolType.equals("GUANGDIAN_V33")) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals("DIANMING_V12") || protocolType.equals("DIANMING_V121") || protocolType.equals("TONGZHOU")) {
                        propertyObj.put("ACTION", "0");
                    } else if (protocolType.equals("DINGEN_V10")) {
                        propertyObj.put("STAY", "1");
                    }

                }
                if (!propertyObj.containsKey("COORDINATE") || "".equals(propertyObj.getString("COORDINATE"))) {
                    propertyObj.put("COORDINATE", "000000");
                }
                if (!propertyObj.containsKey("FONT") || "".equals(propertyObj.getString("FONT"))) {
                    propertyObj.put("FONT", "黑体");
                }
                if (!propertyObj.containsKey("STAY") || "".equals(propertyObj.getString("STAY"))) {


                    if (protocolType.equals("XIANKE_V12")) {
                        propertyObj.put("STAY", "3");
                    } else if (protocolType.equals("SANSI_V6") || protocolType.equals("SANSI_V61") || protocolType.equals("GUANGDIAN_V33")) {
                        propertyObj.put("STAY", "300");
                    } else if (protocolType.equals("DIANMING_V12") || protocolType.equals("DIANMING_V121") || protocolType.equals("TONGZHOU")) {
                        propertyObj.put("STAY", "30");
                    } else if (protocolType.equals("DINGEN_V10")) {
                        propertyObj.put("STAY", "500");
                    }


                }

                if (index == contents.length - 1) {//如果文字在属性数组的最后
                    jsonArrList.add(propertyObj);
                } else { //一个ITEM中显示文本信息对应多个属性时(例：颜色),第一个文字信息后还有文本
                    jsonArrList.add(propertyObj);
                    //将另一个文本对应的属性放入一个新属性数组
                    String[] newContents = new String[contents.length - index - 1];
                    for (int i = 0; i < contents.length - index - 1; i++) {
                        newContents[i] = contents[index + i + 1];
                    }
                    //通过相同逻辑循环对相应属性赋值
                    detailContentProcess(newContents, protocolType, jsonArrList, (JSONObject) propertyObj.clone());
                    break;//属性已经在新数组中处理，上一个对应属性循环赋值需退出
                }


            }
            if (index == contents.length - 1 && jsonArrList.size() == 0) {//文本内容为空时
                if (!propertyObj.containsKey("COLOR") || "".equals(propertyObj.getString("COLOR"))) {
                    propertyObj.put("COLOR", "黄色");
                }
                if (!propertyObj.containsKey("SPEED") || "".equals(propertyObj.getString("SPEED"))) {
                    propertyObj.put("SPEED", "1");
                }
                if (!propertyObj.containsKey("FONT_SIZE") || "".equals(propertyObj.getString("FONT_SIZE"))) {
                    propertyObj.put("FONT_SIZE", "2424");
                }
                if (!propertyObj.containsKey("FONT_SIZE") || "".equals(propertyObj.getString("FONT_SIZE"))) {
                    propertyObj.put("FONT_SIZE", "2424");
                }
                if (!propertyObj.containsKey("ACTION") || "".equals(propertyObj.getString("ACTION"))) {
                    if (protocolType.equals("XIANKE_V12")) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals("SANSI_V6") || protocolType.equals("SANSI_V61") || protocolType.equals("GUANGDIAN_V33")) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals("DIANMING_V12") || protocolType.equals("DIANMING_V121") || protocolType.equals("TONGZHOU")) {
                        propertyObj.put("ACTION", "0");
                    } else if (protocolType.equals("DINGEN_V10")) {
                        propertyObj.put("STAY", "1");
                    }

                }
                if (!propertyObj.containsKey("COORDINATE") || "".equals(propertyObj.getString("COORDINATE"))) {
                    propertyObj.put("COORDINATE", "000000");
                }
                if (!propertyObj.containsKey("FONT") || "".equals(propertyObj.getString("FONT"))) {
                    propertyObj.put("FONT", "黑体");
                }
                if (!propertyObj.containsKey("STAY") || "".equals(propertyObj.getString("STAY"))) {


                    if (protocolType.equals("XIANKE_V12")) {
                        propertyObj.put("STAY", "3");
                    } else if (protocolType.equals("SANSI_V6") || protocolType.equals("SANSI_V61") || protocolType.equals("GUANGDIAN_V33")) {
                        propertyObj.put("STAY", "300");
                    } else if (protocolType.equals("DIANMING_V12") || protocolType.equals("DIANMING_V121") || protocolType.equals("TONGZHOU")) {
                        propertyObj.put("STAY", "30");
                    } else if (protocolType.equals("DINGEN_V10")) {
                        propertyObj.put("STAY", "500");
                    }


                }

                propertyObj.put("CONTENT", "请输入内容");
                jsonArrList.add(propertyObj);//循环结束前未发现文本信息,将其他属性信息添加至ArrList
            }
        }
    }

    /**
     * 情报板动作解析(ContentToJson)
     *
     * @param str
     * @return
     */
    private static JSONObject actionContentToJson(String[] str, String protocolType, JSONObject propertyObj) {

        if ("SANSI_V6".equals(protocolType) || "SANSI_V61".equals(protocolType) || "GUANGDIAN_V33".equals(protocolType) || "DINGEN_V10".equals(protocolType)) {
            propertyObj.put("STAY", str[0]);
            propertyObj.put("ACTION", str[1]);
            propertyObj.put("SPEED", str[2]);
        } else if ("XIANKE_V12".equals(protocolType) || "DIANMING_V12".equals(protocolType) || "DIANMING_V121".equals(protocolType) || "TONGZHOU".equals(protocolType)) {
            propertyObj.put("STAY", str[0]);
            propertyObj.put("ACTION", str[1]);
            if (str.length >= 5)
                propertyObj.put("SPEED", str[4]);
        }
        return propertyObj;
    }


    /**
     * 属性解析(ContentToJson)
     *
     * @param str
     * @return
     */
    private static JSONObject propertyProcess(String str, String protocolType, JSONObject propertyObj) {
        Boolean result = false;
        String[] translateSign = {};
        if ("SANSI_V6".equals(protocolType) || "SANSI_V61".equals(protocolType) || "GUANGDIAN_V33".equals(protocolType))
            translateSign = TRANSLATE_SIGN[0];
        if ("XIANKE_V12".equals(protocolType))
            translateSign = TRANSLATE_SIGN[1];
        if ("DIANMING_V12".equals(protocolType) || "DIANMING_V121".equals(protocolType) || "TONGZHOU".equals(protocolType))
            translateSign = TRANSLATE_SIGN[2];
        if ("DINGEN_V10".equals(protocolType))
            translateSign = TRANSLATE_SIGN[3];
        String compareStr = str.substring(0, 1);
        if (translateSign[0].equals(compareStr)) {// 坐标值
            propertyObj.put("COORDINATE", str.length() >= 7 ? str.substring(1, 7) : str.substring(1, str.length()));
            result = true;
        }
        if (translateSign[1].equals(compareStr)) {// 字体、字号
            propertyObj.put("FONT", fontContentToJson(str.substring(1, 2)));
            propertyObj.put("FONT_SIZE", str.substring(2, str.length() > 6 ? 6 : str.length()));
            result = true;
        }
        if (translateSign[2].equals(compareStr) && str.length() <= 13) {// 颜色
            propertyObj.put("COLOR", fontColorContentToJson(str.substring(1, str.length())));
            result = true;

        }
        propertyObj.put("STATE",result);
        return propertyObj;
    }




    public static String fontTranslate(String font) {
        if ("黑体".equals(font)) {
            return "h";
        } else if ("楷体".equals(font)) {
            return "k";
        } else if ("隶书".equals(font)) {
            return "l";
        } else if ("仿宋".equals(font)) {
            return "f";
        }
        return "s";//默认宋体
    }

    public static String colorTranslate(String color) {
        if ("红色".equals(color)) {
            return "255000000000";
        } else if ("黄色".equals(color)) {
            return "255255000000";
        } else if ("绿色".equals(color)) {
            return "000255000000";
        } else if ("白色".equals(color)) {
            return "255255255000";
        } else if ("透明色".equals(color)) {
            return "t";
        }
        return "255255000000";//默认黄色
    }

    private static String fontContentToJson(String f) {
        String font = "";
        switch (f) {
            case "h":
                font = "黑体";
                break;
            case "k":
                font = "楷体";
                break;
            case "s":
                font = "宋体";
                break;
            case "f":
                font = "仿宋";
                break;
            case "l":
                font = "隶书";
                break;
        }
        return font;
    }

    private static String fontColorContentToJson(String fc) {
        String fontColor = "";
        switch (fc) {
            case "255255255000":
                fontColor = "白色";
                break;
            case "255255000000":
                fontColor = "黄色";
                break;
            case "000255000000":
                fontColor = "绿色";
                break;
            case "255000000000":
                fontColor = "红色";
                break;
            case "t":
                fontColor = "透明色";
                break;
        }
        return fontColor;
    }
}
