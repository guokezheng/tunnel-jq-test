package com.tunnel.platform.business.board.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tunnel.platform.business.board.core.DevicesManager;
import com.tunnel.platform.business.board.core.IDeviceProtocol;
import com.tunnel.platform.business.board.protocol.ProtocolCheckAlgorithm;
import com.tunnel.platform.utils.util.CommonUtil;
import com.tunnel.platform.utils.util.RadixUtil;

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
        if (itemContent == null || "".equals(itemContent) || itemContent.length() < 5)
            return itemContent;
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
                if (protocolType.equals(IDeviceProtocol.IB_XIANKE_V12) && contents[index].startsWith(TRANSLATE_SIGN[1][3])) {
                    textValue = contents[index].substring(1, contents[index].length());
                } else if ((protocolType.equals(IDeviceProtocol.IB_DIANMING_V12) || protocolType.equals(IDeviceProtocol.IB_DIANMING_V121) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) && contents[index].startsWith(TRANSLATE_SIGN[2][3])) {
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
                    if (protocolType.equals(IDeviceProtocol.IB_XIANKE_V12)) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals(IDeviceProtocol.IB_SANSI_V6) || protocolType.equals(IDeviceProtocol.IB_SANSI_V61) || protocolType.equals(IDeviceProtocol.IB_GUANGDIAN_V33)) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DIANMING_V12) || protocolType.equals(IDeviceProtocol.IB_DIANMING_V121) || protocolType.equals(IDeviceProtocol.IB_TONGZHOU)) {
                        propertyObj.put("ACTION", "0");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DINGEN_V10)) {
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


                    if (protocolType.equals(IDeviceProtocol.IB_XIANKE_V12)) {
                        propertyObj.put("STAY", "3");
                    } else if (protocolType.equals(IDeviceProtocol.IB_SANSI_V6) || protocolType.equals(IDeviceProtocol.IB_SANSI_V61) || protocolType.equals(IDeviceProtocol.IB_GUANGDIAN_V33)) {
                        propertyObj.put("STAY", "300");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DIANMING_V12) || protocolType.equals(IDeviceProtocol.IB_DIANMING_V121) || protocolType.equals(IDeviceProtocol.IB_TONGZHOU)) {
                        propertyObj.put("STAY", "30");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DINGEN_V10)) {
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
                    if (protocolType.equals(IDeviceProtocol.IB_XIANKE_V12)) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals(IDeviceProtocol.IB_SANSI_V6) || protocolType.equals(IDeviceProtocol.IB_SANSI_V61) || protocolType.equals(IDeviceProtocol.IB_GUANGDIAN_V33)) {
                        propertyObj.put("ACTION", "1");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DIANMING_V12) || protocolType.equals(IDeviceProtocol.IB_DIANMING_V121) || protocolType.equals(IDeviceProtocol.IB_TONGZHOU)) {
                        propertyObj.put("ACTION", "0");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DINGEN_V10)) {
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


                    if (protocolType.equals(IDeviceProtocol.IB_XIANKE_V12)) {
                        propertyObj.put("STAY", "3");
                    } else if (protocolType.equals(IDeviceProtocol.IB_SANSI_V6) || protocolType.equals(IDeviceProtocol.IB_SANSI_V61) || protocolType.equals(IDeviceProtocol.IB_GUANGDIAN_V33)) {
                        propertyObj.put("STAY", "300");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DIANMING_V12) || protocolType.equals(IDeviceProtocol.IB_DIANMING_V121) || protocolType.equals(IDeviceProtocol.IB_TONGZHOU)) {
                        propertyObj.put("STAY", "30");
                    } else if (protocolType.equals(IDeviceProtocol.IB_DINGEN_V10)) {
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

        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType) || IDeviceProtocol.IB_DINGEN_V10.equals(protocolType)) {
            propertyObj.put("STAY", str[0]);
            propertyObj.put("ACTION", str[1]);
            propertyObj.put("SPEED", str[2]);
        } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
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
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType))
            translateSign = TRANSLATE_SIGN[0];
        if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType))
            translateSign = TRANSLATE_SIGN[1];
        if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType))
            translateSign = TRANSLATE_SIGN[2];
        if (IDeviceProtocol.IB_DINGEN_V10.equals(protocolType))
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


    /**
     * 解析和智慧高速指定的JSON格式数据，拼接成情报板所需内容
     *
     * @param devId
     * @param jsonArray
     * @return
     */
    public static String jsonObjProcess(String devId, JSONArray jsonArray, String protocolType) {
        String[] translate_sign = {};
        String split = "\r\n";
        StringBuffer conent = new StringBuffer();
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
            translate_sign = TRANSLATE_SIGN[0];
            conent.append("[Playlist]").append(split);
            conent.append("time_to_live=0").append(split);
            conent.append("levle=0").append(split);
            conent.append("Item_No=").append(jsonArray.size()).append(split);
        } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
            translate_sign = TRANSLATE_SIGN[1];
            conent.append("[LIST]").append(split);
            conent.append("ItemCount=").append(String.format("%03d", jsonArray.size())).append(split);
        } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
            translate_sign = TRANSLATE_SIGN[2];
            conent.append("[PLAYLIST]").append(split);
            conent.append("ITEM_NO=").append(String.format("%03d", jsonArray.size())).append(split);
        } else if (IDeviceProtocol.IB_DINGEN_V10.equals(protocolType)) {
            translate_sign = TRANSLATE_SIGN[3];
            conent.append("[playlist]").append(split);
            conent.append("item_no=").append(jsonArray.size()).append(split);
        }
        for (int m = 0; m < jsonArray.size(); m++) {
            Map<String, Object> propertyMap = new HashMap<>();
            if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
                conent.append("Item").append(m).append("=");
            } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
                conent.append("Item").append(String.format("%02d", m)).append("=");
            } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
                conent.append("ITEM").append(String.format("%03d", m)).append("=");
            }
            JSONObject jObj = (JSONObject) jsonArray.get(m);
            JSONArray jArray = jObj.getJSONArray("Contents");
            for (int i = 0; i < jArray.size(); i++) {
                JSONObject jObject = (JSONObject) jArray.get(i);
                Integer stay = jObject.getInteger("Stay");
                Integer action = jObject.getInteger("Action");
                Integer speed = jObject.getInteger("Speed");
                if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType)) {
                    if (propertyMap.get(protocolType + stay + action + speed) == null) {
                        conent.append(stay).append(", ");// 停留时间
                        conent.append(action).append(",");// 入屏方式
                        conent.append(speed);// 入屏速度
                        propertyMap.put(protocolType + stay + action + speed, stay + action + speed);
                    }
                } else if (IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
                    if (propertyMap.get(protocolType + stay + action + speed) == null) {
                        conent.append(stay).append(", ");//停留时间
                        conent.append(action).append(", ");//入屏方式
                        conent.append(speed).append(" ,");//入屏速度
                        propertyMap.put(protocolType + stay + action + speed, stay + action + speed);
                    }
                } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
                    if (propertyMap.get(protocolType + stay + action + speed) == null) {
                        conent.append(stay).append(",");
                        conent.append(action).append(",").append("0,").append("1,");
                        conent.append(speed).append(",");
                        propertyMap.put(protocolType + stay + action + speed, stay + action + speed);
                    }
                } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
                    if (propertyMap.get(protocolType + stay + action + speed) == null) {
                        conent.append(stay).append(",");
                        conent.append(action).append(",").append("0,").append("0,");
                        conent.append(speed).append(",");
                        propertyMap.put(protocolType + stay + action + speed, stay + action + speed);
                    }
                } else if (IDeviceProtocol.IB_DINGEN_V10.equals(protocolType)) {
                    if (propertyMap.get(protocolType + stay + action + speed) == null) {
                        conent.append(stay).append(", ");//停留时间
                        conent.append(action).append(",");//入屏方式
                        conent.append(speed).append(",");//入屏速度
                        propertyMap.put(protocolType + stay + action + speed, stay + action + speed);
                    }
                }
                String coordinate = jObject.getString("Coordinate");
                if (propertyMap.get(protocolType + coordinate) == null) {
                    conent.append("\\").append(translate_sign[0]).append(coordinate);
                    propertyMap.put(protocolType + coordinate, coordinate);
                }
                String font = jObject.getString("Font");
                String size = jObject.getString("Size");
                if (propertyMap.get(protocolType + font + size) == null) {
                    conent.append("\\").append(translate_sign[1]).append(fontTranslate(font)).append(size);
                    if (!IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
                        conent.append(size);
                    }
                    propertyMap.put(protocolType + font + size, font + size);
                }
                String color = jObject.getString("Color");
                if (propertyMap.get(protocolType + color) == null) {
                    conent.append("\\").append(translate_sign[2]).append(colorTranslate(color));
                    propertyMap.put(protocolType + color, color);
                }
                String content = jObject.getString("Content");
                if (propertyMap.get(protocolType + content) == null) {
                    if (!"".equals(translate_sign[5])) {
                        conent.append(translate_sign[5]).append(content);
                    } else {
                        conent.append(content);
                    }
                    propertyMap.put(protocolType + content, content);
                }
            }
            if (m != jsonArray.size() - 1)
                conent.append(split);
        }
        return conent.toString();
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

    public static String contentToGb2312_CG(String devId, String content, String protocolType) {
        try {
            if (protocolType.equals(IDeviceProtocol.IB_SANSI_V6) || protocolType.equals(IDeviceProtocol.IB_SANSI_V61)) {
                String reback = RadixUtil.wordToGb2312(content).toUpperCase();
                if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType)) {//帧类型(向可变信息标志上载文件)
                    reback = "30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
                } else if (IDeviceProtocol.IB_SANSI_V61.equals(protocolType)) {
                    reback = "30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
                }
                reback = reback.replaceAll(" D ", " 0D ");
                reback = reback.replaceAll(" A ", " 0A ");
                reback = reback.replaceAll(" C ", " 5C 66 ");
                reback = reback.replaceAll(" 5C 6E ", " 0D 0A ");
                reback = reback + " 0D 0A";
                String crc = ProtocolCheckAlgorithm.gen_crc(reback);
                StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
                checkCode.insert(2, " ");
                reback = "02 " + reback + " " + checkCode.toString() + " 03";
                return reback;
            } else if (IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
                String reback = RadixUtil.wordToGb2312(content).toUpperCase();
                reback = "30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
                reback = reback.replaceAll(" D ", " 0D ");
                reback = reback.replaceAll(" A ", " 0A ");
                reback = reback.replaceAll(" C ", " 5C 66 ");
                reback = reback.replaceAll(" 5C 6E ", " 0D 0A ");
                reback = reback + " 0D 0A";
                String crc = ProtocolCheckAlgorithm.gen_crc(reback);
                StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
                checkCode.insert(2, " ");
                reback = "02 " + reback + " " + checkCode.toString() + " 03";
                return reback;
            } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
                String reback = RadixUtil.wordToGb2312(content).toUpperCase();
                reback = "32 30 30 30 31 30 30 31 32 6C 69 73 74 5C 30 30 30 2E 78 6B 6C 30 30 30 30 " + reback;
                reback = reback.replaceAll(" D ", " 0D ");
                reback = reback.replaceAll(" A ", " 0A ");
                reback = reback.replaceAll(" C ", " 5C 66 ");
                reback = reback + " 0D 0A";
                String crc = ProtocolCheckAlgorithm.gen_crc(reback);
                StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
                checkCode.insert(2, " ");
                reback = "02 " + reback + " " + checkCode.toString() + " 03";
                return reback;
            } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
                //获取当前播放列表编号
                String result = DevicesManager.getInstance().executeCommand(devId, "02 30 30 30 31 34 39 93 35 03","100");
                if (result == null) {
                    return null;
                }
                String listNumber = result.substring(21, result.length() - 9);
                String reback = RadixUtil.wordToGb2312(content).toUpperCase();
                reback = "30 30 30 31 37 31 2B 30 30 30 30 30 30 30 30 70 6C 61 79 " + listNumber + " 2E 6C 73 74 " + reback;
                reback = reback.replaceAll(" D ", " 0D ");
                reback = reback.replaceAll(" A ", " 0A ");
                reback = reback.replaceAll(" C ", " 5C 66 ");
                reback = reback + " 0D 0A";
                String crc = ProtocolCheckAlgorithm.gen_crc(reback);
                StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
                checkCode.insert(2, " ");
                reback = "02 " + reback + " " + checkCode.toString() + " 03";
                return reback;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文本转GB2312发送
     *
     * @param content
     * @param protocolType
     * @return
     * @throws Exception
     */
    public static String contentToGb2312(String content, String protocolType) {
        StringBuffer controlContent = new StringBuffer("");
        try {
            String con = RadixUtil.wordToGb2312(content);
            int conLen = getStringByteLenths(con);// 获取内容长度
            if (conLen <= 2048) {// 上载文件时，如文件长度超过 2048 字节，必须把文件分割成等于 2048 字节的若干段发送
                return contentToGb2312ByLength(content, protocolType);
            } else {
                int count = conLen / 2048;
                if (conLen % 2048 != 0) {
                    for (int i = 0; i <= count; i++) {
                        String subcontent = substringByte(content, 0 + 2048 * i, i == count ? content.getBytes("utf-8").length : 2048);//按字节数截取制定长度的字符串
                        controlContent.append(contentToGb2312ByLength(subcontent, protocolType));
                    }
                } else {
                    for (int i = 0; i <= count; i++) {
                        String subcontent = substringByte(content, 0 + 2048 * i, 2048);//按字节数截取制定长度的字符串
                        controlContent.append(contentToGb2312ByLength(subcontent, protocolType));
                    }
                    controlContent.append(contentToGb2312ByLength("", protocolType));
                }
            }
            return controlContent.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照长度拼接
     *
     * @param content
     * @param protocolType
     * @return
     * @throws Exception
     */
    private static String contentToGb2312ByLength(String content, String protocolType) throws Exception {
        if (IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
            String reback = RadixUtil.wordToGb2312(content).toUpperCase();
            reback = reback.replaceAll(" 5C 5C ", " 5C ");//双斜线转义替换
            reback = reback.replaceAll(" 2C 20 ", " 20 2C ");//" ,"替换为", "
            //reback = reback.replaceAll(" 2C 30 ", " 20 2C 30 20 2C ");//",0"替换为" ,0 ,"
            reback = "30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
            reback = reback.replaceAll(" D ", " 0D ");
            reback = reback.replaceAll(" A ", " 0A ");
            reback = reback.replaceAll(" A1D ", " A1 0D ");
            reback = reback.replaceAll(" D0D ", " D0 0D ");
//			reback = reback.replaceAll(" C ", " 5C 66 ");
            reback = reback + " 0D 0A";
            reback = reback.replaceAll(" 9 ", " ");//转16进制出现多余的9需要去掉
            if ("9".equals(reback.substring(reback.length() - 1, reback.length()))) {
                reback = reback.substring(0, reback.length() - 1);
            }
            String crc = ProtocolCheckAlgorithm.gen_crc(reback);
            StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
            checkCode.insert(2, " ");
            reback = "02 " + reback + " " + checkCode.toString() + " 03";
            return reback;
        } else if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType)) {
            String reback = RadixUtil.wordToGb2312(content).toUpperCase();
            reback = reback.replaceAll(" 5C 5C ", " 5C ");
            if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType)) {//帧类型(向可变信息标志上载文件)
                reback = "31 36 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
            } else if (IDeviceProtocol.IB_SANSI_V61.equals(protocolType)) {
                reback = "31 36 30 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 " + reback;
            }
            reback = reback.replaceAll(" D ", " 0D ");
            reback = reback.replaceAll(" A ", " 0A ");
            reback = reback.replaceAll(" 68D ", " 68 0D ");
//			reback = reback.replaceAll(" C ", " 5C 66 ");
            reback = reback + " 0D 0A";
            reback = reback.replaceAll(" 9 ", " ");
            if ("9".equals(reback.substring(reback.length() - 1, reback.length()))) {
                reback = reback.substring(0, reback.length() - 1);
            }
            String crc = ProtocolCheckAlgorithm.gen_crc(reback);
            StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
            checkCode.insert(2, " ");
            reback = "02 " + reback + " " + checkCode.toString() + " 03";
            return reback;
        } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
            String reback = RadixUtil.wordToGb2312(content).toUpperCase();
            reback = reback.replaceAll(" 5C 5C ", " 5C ");
            reback = "32 30 30 30 31 30 30 31 32 6C 69 73 74 5C 30 30 30 2E 78 6B 6C 30 30 30 30 " + reback;
            reback = reback.replaceAll(" D ", " 0D ");
            reback = reback.replaceAll(" A ", " 0A ");
            reback = reback.replaceAll(" 68D ", " 68 0D ");
            reback = reback.replaceAll(" A3D ", " A3 0D ");
            reback = reback.replaceAll(" F6 CF ", " F6 20 CF ");//"鱿"替换为"� �"
//			reback = reback.replaceAll(" C ", " 5C 66 ");
            reback = reback + " 0D 0A";
            reback = reback.replaceAll(" 9 ", " ");//必须放在后面
            if ("9".equals(reback.substring(reback.length() - 1, reback.length()))) {
                reback = reback.substring(0, reback.length() - 1);
            }
            String crc = ProtocolCheckAlgorithm.gen_crc(reback);
            StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
            checkCode.insert(2, " ");
            reback = "02 " + reback + " " + checkCode.toString() + " 03";
            return reback;
        } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
            String reback = RadixUtil.wordToGb2312(content).toUpperCase();
            reback = reback.replaceAll(" 5C 5C ", " 5C ");
            reback = "31 31 32 32 33 39 2B 30 30 30 30 30 30 30 30 70 6C 61 79 30 30 2E 6C 73 74 " + reback;
            reback = reback.replaceAll(" D ", " 0D ");
            reback = reback.replaceAll(" A ", " 0A ");
            reback = reback.replaceAll(" 68D ", " 68 0D ");
//			reback = reback.replaceAll(" C ", " 5C 66 ");
            reback = reback + " 0D 0A";
            reback = reback.replaceAll(" 9 ", " ");
            if ("9".equals(reback.substring(reback.length() - 1, reback.length()))) {
                reback = reback.substring(0, reback.length() - 1);
            }
            String crc = ProtocolCheckAlgorithm.gen_crc(reback);
            StringBuffer checkCode = new StringBuffer(crc.toUpperCase());
            checkCode.insert(2, " ");
            reback = "02 " + reback + " " + checkCode.toString() + " 03";
            return reback;
        }
        return null;
    }


    /**
     * 将JSON数据拼成情报板识别的[PlayList]串 例:(显科)
     * ItemYY = DelayTime, EffectIn, EffectShow, EffectOut, MoveStep, strContent
     * 说明： YY 为动作序号（两位数），依次从 0 至(XXX-1)。
     * DelayTime 为动作执行完后的停留时间，单位为秒。
     * EffectIn 为显示信息的入字方式，范围[0,17]，缺省值 1。
     * EffectShow 为停留时候的显示效果（系统保留待以后扩展）。
     * EffectOut 为显示信息的出字方式，范围[0,17]，缺省值 1。
     * MoveStep 为播放速度，不为 0，一般取值为{1,2,4,8,16}，越大越快。
     * <p>
     * [LIST]
     * ItemCount=002
     * Item00=2,1,0,1,1,\C000000\Fs32\T255000000000\B000000000000\U深圳显科科技有限公司
     * Item01=2,1,0,1,1,\C000000\Fs32\T000255000000\B000000000000\U深圳显科科技有限公司
     *
     * @param contentArray
     * @param protocolType 解析协议
     * @return
     */
    public static String jsonToItemContent(JSONArray contentArray, String protocolType) {
        StringBuffer XIANKEFormat = new StringBuffer("");
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
            XIANKEFormat.append("[playlist]");
            XIANKEFormat.append(System.getProperty("line.separator"));
            XIANKEFormat.append("item_no = ");
            XIANKEFormat.append(contentArray.size());
        } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
            XIANKEFormat.append("[LIST]");
            XIANKEFormat.append(System.getProperty("line.separator"));
            XIANKEFormat.append("ItemCount = ");
            XIANKEFormat.append(String.format("%03d", contentArray.size()));
        } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
            XIANKEFormat.append("[PLAYLIST]");
            XIANKEFormat.append(System.getProperty("line.separator"));
            XIANKEFormat.append("ITEM_NO=");
            XIANKEFormat.append(String.format("%03d", contentArray.size()));
        }
        String[] translateSign = {};
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType))
            translateSign = TRANSLATE_SIGN[0];
        if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType))
            translateSign = TRANSLATE_SIGN[1];
        if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType))
            translateSign = TRANSLATE_SIGN[2];
        for (int i = 0; i < contentArray.size(); i++) {
            JSONObject itemObject = contentArray.getJSONObject(i);
            JSONArray itemContentArray = itemObject.getJSONArray("Contents");// (JSONArray)itemObject.get("Contents");
            if (itemContentArray == null)
                continue;
            JSONObject backObj = (JSONObject) itemContentArray.getJSONObject(0).clone();//第一个Object为最初属性
            StringBuffer contentFormat = new StringBuffer();
            for (int j = 0; j < itemContentArray.size(); j++) {
                JSONObject propertyObject = itemContentArray.getJSONObject(j);
                String content = propertyObject.getString("Content");// 文本
                String color = propertyObject.getString("Color");// 颜色
                String size = propertyObject.getString("Size");// 字体大小
                String font = propertyObject.getString("Font");// 字体
                String coordinate = propertyObject.getString("Coordinate");// 起始位置
                String speed = propertyObject.getString("Speed");// 入屏速度
                String stay = propertyObject.getString("Stay");// 停留时间
                String action = propertyObject.getString("Action");// 入屏方式
                if (j != 0) { // ITEM中含有多中不同属性时和第一个属性对比,属性相同时不追加,不同时追加相应属性
                    content = content == null || content.equals(backObj.getString("Content")) ? "" : content;
                    color = color == null || color.equals(backObj.getString("Color")) ? "" : color;
                    size = size == null || size.equals(backObj.getString("Size")) ? "" : size;
                    font = font == null || font.equals(backObj.getString("Font")) ? "" : font;
                    coordinate = coordinate == null || coordinate.equals(backObj.getString("Coordinate")) ? "" : coordinate;
                    speed = speed == null || speed.equals(backObj.getString("Speed")) ? "" : speed;
                    stay = stay == null || stay.equals(backObj.getString("Stay")) ? "" : stay;
                    action = action == null || action.equals(backObj.getString("Action")) ? "" : action;
                }
                if ("".equals(content) && "".equals(color) && "".equals(size) && "".equals(font)//所有属性都为空时,退出循环
                        && "".equals(coordinate) && "".equals(speed) && "".equals(stay) && "".equals(action)) {
                    continue;
                }
                //动作属性
                if (!"".equals(stay) && !"".equals(action) && !"".equals(speed))
                    contentFormat.append(actionJsonToContent(stay, action, speed, protocolType));
                //坐标属性
                if (!"".equals(coordinate))
                    contentFormat.append("\\").append(translateSign[0]).append(coordinate);
                //字体属性
                contentFormat.append(fontJsonToContent(font, size, protocolType));
                //颜色属性
                contentFormat.append(colorJsonToContent(color, protocolType));
                String textTranslateSign = "";//文本起始符(三思、光电为空)
                if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType))
                    textTranslateSign = TRANSLATE_SIGN[1][3];
                if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType))
                    textTranslateSign = TRANSLATE_SIGN[2][3];
                //文本属性
//				if(!(j>0 && "".equals(content)))
                if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType))
                    contentFormat.append(textTranslateSign).append(content);
                else
                    contentFormat.append("\\").append(textTranslateSign).append(content);
            }
            if (!"".equals(contentFormat.toString())) {
                XIANKEFormat.append(System.getProperty("line.separator"));
                if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
                    XIANKEFormat.append("Item").append(i).append(" = ").append(contentFormat.toString());
                } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
                    XIANKEFormat.append("Item").append(String.format("%02d", i)).append(" = ").append(contentFormat.toString());
                } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
                    XIANKEFormat.append("ITEM").append(String.format("%03d", i)).append("=").append(contentFormat.toString());
                } else if (IDeviceProtocol.IB_DINGEN_V10.equals(protocolType)) {
                    XIANKEFormat.append("item").append(i).append("=").append(contentFormat.toString());
                }
            }
        }
        return XIANKEFormat.toString();
    }


    /**
     * 动作处理(JsonToContent)
     *
     * @param stay         停留时间
     * @param action       入屏方式
     * @param speed        入屏速度
     * @param protocolType 解析协议
     * @return
     */
    private static String actionJsonToContent(String stay, String action, String speed, String protocolType) {
        StringBuffer contFormat = new StringBuffer();
        contFormat.append(stay).append(",")//停留时间
                .append(action).append(",");//入屏方式
//		if(IDeviceProtocol.IB_SANSI_V6.equals(protocolType)||IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)){
//		}//三思和光电无
        if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
            contFormat.append("").append(",")//停留时显示效果(系统保留以后扩展)
                    .append("").append(",");//出屏方式
        } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
            contFormat.append("").append(",")//停留时显示效果(系统保留以后扩展)
                    .append("").append(",");//出屏方式
        }
        contFormat.append(speed).append(",");//播放速度
        return contFormat.toString();
    }

    /**
     * 字体解析(ContentToJson)
     *
     * @param f
     * @return
     */
    /*private static String fontContentToJson(String f) {
        String font = "";
		String encoding = System.getProperty("file.encoding","UTF-8");
		try {
			switch (f) {
			case "h":
				font = new String("黑体".getBytes(encoding), "UTF-8");
				break;
			case "k":
				font = new String("楷体".getBytes(encoding), "UTF-8");
				break;
			case "s":
				font = new String("宋体".getBytes(encoding), "UTF-8");
				break;
			case "f":
				font = new String("仿宋".getBytes(encoding), "UTF-8");
				break;
			case "l":
				font = new String("隶书".getBytes(encoding), "UTF-8");
				break;
			}
			return font;
		} catch (UnsupportedEncodingException e) {
		}
		return font;
	}*/
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

    /**
     * 字体处理(JsonToContent)(h：黑体 ； k：楷体； f：仿宋； s：宋体)
     *
     * @param font         字体
     * @param size         大小
     * @param protocolType 解析协议
     * @return
     */
    private static String fontJsonToContent(String font, String size, String protocolType) {
        String fontTranslateSign = "f";
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType))
            fontTranslateSign = TRANSLATE_SIGN[0][1];
        StringBuffer contFormat = new StringBuffer();
        if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType))
            fontTranslateSign = TRANSLATE_SIGN[1][1];
        if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType))
            fontTranslateSign = TRANSLATE_SIGN[2][1];
        if ("黑体".equals(font)) {
            contFormat.append("\\").append(fontTranslateSign).append("h").append(size);
        } else if ("楷体".equals(font)) {
            contFormat.append("\\").append(fontTranslateSign).append("k").append(size);
        } else if ("仿宋".equals(font)) {
            contFormat.append("\\").append(fontTranslateSign).append("f").append(size);
        } else if ("宋体".equals(font)) {
            contFormat.append("\\").append(fontTranslateSign).append("s").append(size);
        } else if ("隶书".equals(font)) {
            contFormat.append("\\").append(fontTranslateSign).append("l").append(size);
        }
        return contFormat.toString();
    }

    /**
     * 字体颜色解析(ContentToJson)
     *
     * @return
     */
    /*private static String fontColorContentToJson(String fc) {
        String fontColor = "";
		String encoding = System.getProperty("file.encoding","UTF-8");
		try {
			switch (fc) {
			case "255255255000":
				fontColor = new String("白色".getBytes(encoding), "UTF-8");
				break;
			case "255255000000":
				fontColor = new String("黄色".getBytes(encoding), "UTF-8");
				break;
			case "000255000000":
				fontColor = new String("绿色".getBytes(encoding), "UTF-8");
				break;
			case "255000000000":
				fontColor = new String("红色".getBytes(encoding), "UTF-8");
				break;
			case "t":
				fontColor = new String("透明色".getBytes(encoding), "UTF-8");
				break;
			}
			return fontColor;
		} catch (UnsupportedEncodingException e) {
		}
		return fontColor;
	}*/
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

    /**
     * 颜色处理(JsonToContent)
     *
     * @param color
     * @param protocolType 解析协议
     * @return
     */
    private static String colorJsonToContent(String color, String protocolType) {
        String colorTranslateSign = "c";
        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType) || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType))
            colorTranslateSign = TRANSLATE_SIGN[0][2];
        if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType))
            colorTranslateSign = TRANSLATE_SIGN[1][2];
        if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType) || IDeviceProtocol.IB_TONGZHOU.equals(protocolType))
            colorTranslateSign = TRANSLATE_SIGN[2][2];
        if (IDeviceProtocol.IB_DINGEN_V10.equals(protocolType))
            colorTranslateSign = TRANSLATE_SIGN[3][2];
        StringBuffer contFormat = new StringBuffer();
        if ("红色".equals(color)) {
            contFormat.append("\\").append(colorTranslateSign).append("255000000000");
        } else if ("黄色".equals(color)) {
            contFormat.append("\\").append(colorTranslateSign).append("255255000000");
        } else if ("绿色".equals(color)) {
            contFormat.append("\\").append(colorTranslateSign).append("000255000000");
        } else if ("白色".equals(color)) {
            contFormat.append("\\").append(colorTranslateSign).append("255255255000");
        } else if ("透明色".equals(color)) {
            contFormat.append("\\").append(colorTranslateSign).append("t");
        }
        return contFormat.toString();
    }

    /**
     * 获取字符串的长度
     * @param str
     * @return
     */
//	public static int getStringByteLength(String str) {
//		str = str.replaceAll("[^\\x00-\\xff]", "**");
//		return str.length();
//	}

    /**
     * 按照字节截取指定长度字符串
     *
     * @param orignal
     * @param start
     * @param count
     * @return
     */
    private static String substringByte(String orignal, int start, int count) {
        // 如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal))
            return orignal;
        // 截取Byte长度必须>0
        if (count <= 0)
            return orignal;
        // 截取的起始字节数必须比
        if (start < 0)
            start = 0;
        // 目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();
        try {
            // 截取字节起始字节位置大于目标String的Byte的length则返回空值
            if (start >= getStringByteLenths(orignal))
                return null;
            // int[] arrlen=getByteLenArrays(orignal);
            int len = 0;
            char c;
            // 遍历String的每一个Char字符，计算当前总长度
            // 如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {
                c = orignal.charAt(i);
                // 当起始位置为0时候
                if (start == 0) {
                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len <= count)
                        buff.append(c);
                    else
                        break;
                } else {
                    // 截取字符串从非0位置开始
                    len += String.valueOf(c).getBytes("utf-8").length;
                    if (len >= start && len <= start + count) {
                        buff.append(c);
                    }
                    if (len > start + count)
                        break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 返回最终截取的字符结果;
        // 创建String对象，传入目标char Buff对象
        return new String(buff);
    }

    private static int getStringByteLenths(String args) throws UnsupportedEncodingException {
        return args != null && !"".equals(args) ? args.getBytes("utf-8").length : 0;
    }

    /**
     * @return
     * @Description:CRC16的校验码生成。
     */
    private static String gen_crc(String str) {
        if (CommonUtil.isNull(str))
            return null;
        byte[] datas = RadixUtil.hex2Byte(str.trim().split(" "));
        return gen_crc(datas);
    }

    /**
     * @param buffer
     * @return
     * @Description:CRC16的校验码生成。
     * @author: HuangXianbo
     */
    private static String gen_crc(byte buffer[]) {
        char c, treat, bcrc;
        int wcrc = 0;
        int i, j;
        int buffer_length = buffer.length;
        for (i = 0; i < buffer_length; i++) {
            c = (char) (buffer[i] & 0xFF);
            for (j = 0; j < 8; j++) {
                treat = (char) (c & 0x80);
                c <<= 1;
                bcrc = (char) ((wcrc >> 8) & 0x80);
                wcrc <<= 1;
                if (treat != bcrc)
                    wcrc ^= 0x1021;
                wcrc &= 0xFFFF;
            }
        }
        return String.format("%04x", wcrc & 0xFFFF);
    }

    /**
     * 按不同厂家进行拼装并下发
     *
     * @param content
     * @param deviceCode
     * @return
     */
    public static String transformToService(Map<String, String> content, String deviceCode) {
        // 获取该设备的协议编号
        Map<String, String> support = support(deviceCode);
        String protocol_type = support.get("PROTOCOL_TYPE");
        String device_name = support.get("DEVICE_NAME");
        // 列表头
        String playList = "";
        // 内容条数
        String Item_Start = "";
        // 字符内容
        String Item_Content = "";
        if (protocol_type.equals("SANSI_V61") || protocol_type.equals("GUANGDIAN_V33")
                || protocol_type.equals("DINGEN_V10")) {
            playList = "[Playlist]\r\ntime_to_live=0\r\nlevle=0\r\n";
            Item_Start = "Item_No=";
            Item_Content = "Item";
        } else if (protocol_type.equals("XIANKE_V12")) {
            playList = "[LIST]\r\n";
            Item_Start = "ItemCount=00";
            Item_Content = "Item0";
        } else if (protocol_type.equals("DIANMING_V121")) {
            playList = "[PLAYLIST]\r\n";
            Item_Start = "ITEM_NO=00";
            Item_Content = "ITEM00";
        }
        String result = "";
        result += playList;
        String length = content.size() + "";
        String Item_No = Item_Start + length + "\r\n";
        result += Item_No;
        for (Entry<String, String> entry : content.entrySet()) {
            String no = entry.getKey().substring(4);
            String value = addEscape(entry.getValue(), deviceCode);
            result += Item_Content + no + "=" + value + "\r\n";
        }
//		System.out.println(result);
        result = decodingModify(result, protocol_type, device_name);
        return result;
    }

    /**
     * 获得协议支持的内容
     *
     * @param deviceCode
     * @return
     */
    private static Map<String, String> support(String deviceCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * ");
        sql.append(" FROM UAPMEE_T_PROTOCOL_TYPE A ");
        sql.append(" JOIN UAPMEE_T_PROTOCOL_SUPPORT B ");
        sql.append(" WHERE A.`DEVICECODE`='");
        sql.append(deviceCode);
        sql.append("' AND A.`PROTOCOL_TYPE`=B.`PROTOCOL_TYPE`");
//		DBHelper dbHelper = new DBHelper();
        Map<String, String> map = null;
//		try {
//			map = dbHelper.queryForMap(sql.toString());
//		} catch (DBException e) {
//			e.printStackTrace();
//		}
        return map;
    }

    // 对服务器增加转义符并统一格式
    private static String addEscape(String result, String deviceCode) {
        Map<String, String> escape = escape(deviceCode);
        String font = escape.get("FONT");
        String color = escape.get("COLOR");
//		String action = escape.get("ACTION");
        String text = escape.get("TEXT");
        String newline = escape.get("NEWLINE");
        String coordinate = escape.get("COORDINATE");
        String shadow_color = escape.get("SHADOW_COLOR");
        String background_color = escape.get("BACKGROUND_COLOR");
        String word_space = escape.get("WORD_SPACE");
        result = result.replaceAll("\\\\" + newline, " ");

        result = result.replaceAll("/动作", "");
        result = result.replaceAll("/坐标", "\\\\" + coordinate);
        result = result.replaceAll("/宋体", "\\\\" + font + "s");
        result = result.replaceAll("/楷体", "\\\\" + font + "k");
        result = result.replaceAll("/黑体", "\\\\" + font + "h");
        result = result.replaceAll("/仿宋", "\\\\" + font + "f");
        result = result.replaceAll("/红色", "\\\\" + color + "255000000000");
        result = result.replaceAll("/黄色", "\\\\" + color + "255255000000");
        result = result.replaceAll("/绿色", "\\\\" + color + "000255000000");
        result = result.replaceAll("/蓝色", "\\\\" + color + "000000255000");
        result = result.replaceAll("/颜色", "\\\\" + color);
        result = result.replaceAll("/背景颜色", "\\\\" + background_color);
        result = result.replaceAll("/阴影颜色", "\\\\" + shadow_color);
        result = result.replaceAll("/换行", "\\\\" + newline);
        result = result.replaceAll("/字间距", "\\\\" + word_space);
        result = result.replaceAll("/文本", "\\\\" + text);
        return result;
    }

    // 获取转义符
    private static Map<String, String> escape(String deviceCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT B.* ");
        sql.append(" FROM UAPMEE_T_PROTOCOL_TYPE A ");
        sql.append(" JOIN UAPMEE_T_PROTOCOL_ESCAPE B ");
        sql.append(" WHERE A.`DEVICECODE`='");
        sql.append(deviceCode);
        sql.append("' AND A.`PROTOCOL_TYPE`=B.`PROTOCOL_TYPE`");

//		DBHelper dbHelper = new DBHelper();
        Map<String, String> map = null;
//		try {
//			map = dbHelper.queryForMap(sql.toString());
//		} catch (DBException e) {
//			e.printStackTrace();
//		}
        return map;
    }

    /**
     * 获取不同厂家设备支持相关内容
     */
    public static Map<String, String> getSupport(String deviceId, String protocolType) {
        if (deviceId == null && protocolType == null)
            return null;
        Map<String, String> items = new HashMap<String, String>();
        items.put("PROTOCOL_TYPE", protocolType);
        items.put("FONT_SIZE", "3232,2424,1616");
        items.put("DEVICEID", deviceId);
        items.put("COLOR", "红色,绿色,蓝色,黄色");
        items.put("FONT", "宋体,黑体,楷体");

        if (IDeviceProtocol.IB_SANSI_V6.equals(protocolType) || IDeviceProtocol.IB_SANSI_V61.equals(protocolType)
                || IDeviceProtocol.IB_GUANGDIAN_V33.equals(protocolType)) {
            ;
        } else if (IDeviceProtocol.IB_XIANKE_V12.equals(protocolType)) {
            ;
        } else if (IDeviceProtocol.IB_DIANMING_V12.equals(protocolType) || IDeviceProtocol.IB_DIANMING_V121.equals(protocolType)
                || IDeviceProtocol.IB_TONGZHOU.equals(protocolType)) {
            ;
        }
        return items;
    }

    private static String decodingModify(String parameters, String supplier, String spec) {
        // 转换为16进制
        try {
            parameters = RadixUtil.wordToGb2312(parameters).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String startCode = "";
        // 计算校验码
        if (supplier.equals("SANSI_V61") || supplier.equals("GUANGDIAN_V33")) {
            startCode = "30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 ";
        } else if (supplier.equals("XIANKE_V12")) {
            startCode = "32 30 30 30 31 30 30 31 32 6C 69 73 74 5C 30 30 30 2E 78 6B 6C 30 30 30 30 ";
        } else if (supplier.equals("DINGEN_V10")) {
            startCode = "00 00 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 ";
        } else if (supplier.equals("DIANMING_V121")) {
            startCode = "30 30 30 30 37 31 2B 30 30 30 30 30 30 30 30 70 6C 61 79 30 30 2E 6C 73 74 ";
            if (spec.equals("F板") && supplier.equals("DIANMING_V121")) {
                startCode = "30 32 30 31 37 31 2B 30 30 30 30 30 30 30 30 70 6C 61 79 30 30 2E 6C 73 74 ";
            }
        }
        String checkPassword = gen_crc(startCode + parameters);
        // 组合
        parameters = "02 " + startCode + parameters + checkPassword + " 03";
        parameters = parameters.replaceAll(" ", "");
        return parameters;
    }
}
