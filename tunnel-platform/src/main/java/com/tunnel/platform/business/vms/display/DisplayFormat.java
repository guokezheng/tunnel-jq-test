package com.tunnel.platform.business.vms.display;


import com.tunnel.business.utils.util.CommonUtil;

public class DisplayFormat {


    /*三思协议
     *一、应答帧结构：
	 *  | 帧头 | 1 | 0x02，表明一帧的开始，为接收方提供同步 |
		| 地址 | 2 | 可变信息标志地址，由可变信息标志厂家设定 |
		| 帧数据 | 不定长 | |
		| 帧校验 | 2 | 供接收方判断所收帧的正确性 |
		| 帧尾 | 1 | 0x03，表明一帧的结束，为接收方提供同步 |
	 *二、 5. 取可变信息标志的当前显示内容
	    应答：
	    序号 3 字节 ASCII 码，当前显示内容在播放表中的序号
	    停留时间 5 字节 ASCII 码，当前显示内容的停留时间
	    出字方式 2 字节 ASCII 码，当前显示内容的出字方式
	    出字速度 5 字节 ASCII 码，当前显示内容的出字速度
	    显示字符串 不定长 ASCII 码字符串，当前正在显示的内容，带转义符
	*  18（字节）*2+18（空格） = 36+18=54
	*/

    /**
     * 去当前亮度调节方式和亮度
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preSanSiBrightnessHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(8, temp.length() - 8);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 去当前设备故障信息
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preSanSiFaultHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(8, temp.length() - 8);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 当前展示内容
     * 三思和光电
     * 去掉协议的帧头、地址、序号、停留时间、出字方式、出字速度
     *
     * @param content
     * @return
     */

    public static String preSanSiCurrentHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(54, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 当前全部内容
     * 三思和光电
     *
     * @throws
     * @Title: preSanSiHandle
     * @Description:
     * @param: @param content
     * @param: @return
     * @return: String
     */
    public static String preSanSiAllListHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(9, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }


    /*电明协议：
     * 返回数据
	 * 格式：
	0x02(帧头) 0x30 0x31(目的地址) 0x30 0x30(源地址) 0x37 0x34(指令头) 
	0x30 0x30 0x30 (播放列表的ITEM编号，即当前是当前列表的第几幕，三个字
	节)0x30 0x30 0x30 0x30 0x30(停留时间，五个字节)0x30 0x30(入屏方式，两
	个字节)0x30 0x30（停留方式，两个字节）0x30 0x30（出屏方式，两个字节）
	0x30 0x30(出入屏速度，两个字节)0x30…0x30（不定长数据，为当前显示内容，
	具体定义见播放列表格式各转义符含义，如：
	“\C000001\Fs3232\T255000000000\K000000000000\WHello World”）CRCH 
	CRCL(校验位) 0x03(帧尾)*/


    /**
     * 去当前亮度调节方式和亮度
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preTongZhouBrightnessHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(21, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 当前展示内容
     * 同洲和电明
     * 去掉协议的开始、结束和标识符
     *
     * @param content
     * @return
     */

    public static String preTongZhouCurrentHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");

            temp = temp.substring(69, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /*
        电明协议：文件回传通用指令  获取全部显示内容
        返回值格式：
        0x02(帧头)
        0x30 0x31(目的地址) 0x30 x30 (源地址) 0x30 0x38(指令头) 0x30…0x30(不定长文件完整路径名)
        0x30(文件下载选项,”-“表示覆盖,”+”表示追 加，目前下位机中已经不区分此项，统一使用“+”即可)
        0x30 0x30 0x30 0x30 0x30 0x30 0x30 0x30 (文件偏移地址，八个字节，高位在前)
        ……(不定长数据) CRCH CRCL(校验位)
        0x03(帧尾)
    */

    /**
     * 当前全部内容
     * 电明和同洲设备
     *
     * @throws
     * @Title: preTongZhouAllListHandle
     * @Description:
     * @param: @param content
     * @param: @return
     * @return: String
     */
    public static String preTongZhouAllListHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(9, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }





    /**
     * 去当前亮度调节方式和亮度
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preXianKeBrightnessHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(18, temp.length() - 15);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 当前展示内容
     * 显科
     * 去掉协议的开始、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preXianKeCurrentHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(27, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 查询当前显示列表
     * 显科
     * 去掉协议的开始、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preXianKeAllListHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(75, temp.length() - 9);
            return temp.trim();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 去当前设备故障信息
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preXianKeFaultHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(15, temp.length() - 8);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 当前展示内容
     * 鼎恩
     * 去掉协议的开始、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preDingEnHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(2, temp.length() - 8);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 过滤掉内容中的显示格式
     *
     * @param result
     * @return
     */
    public static String filterContent(String result) {
        if (CommonUtil.isNull(result))
            return null;
        //result = result.replaceFirst("\\\\C.{6}", " ");
        result = result.replaceAll("\\\\C\\d{6}", " ");
        result = result.replaceAll("\\\\B.{3}", " ");
        result = result.replaceAll("\\\\y.{1}", " ");
        result = result.replaceAll("\\\\c.{12}", " ");
        result = result.replaceAll("\\\\bt", " ");
        result = result.replaceAll("\\\\b.{12}", " ");
        result = result.replaceAll("\\\\s.{12}", " ");
        result = result.replaceAll("\\\\S.{2}", " ");
        result = result.replaceAll("\\\\f\\w{5}", " ");
        result = result.replaceAll("\\\\N.{2}", " ");
        result = result.replaceAll("\\\\r.{12}", " ");

        result = result.replaceAll("\\\\F.{6}", " ");
        result = result.replaceAll("T\\d{12}", " ");

        result = result.replaceAll("\\\\M.{2}", " ");
        result = result.replaceAll("\\\\K.{12}", " ");
        //result = result.replaceAll("K.{12}", " ");

        result = result.replaceAll("\\\\n", " ");
        result = result.replaceAll("\\\\nj", " ");
        result = result.replaceAll("\\\\A", " ");
        result = result.replaceAll("\\\\W", " ");
        return result.trim();
    }

    /**
     * 三思和光电协议
     * 过滤掉内容中的显示格式
     *
     * @param result
     * @return
     */
    public static String sanSiGuangDianFilterContent(String result) {
        if (CommonUtil.isNull(result))
            return null;
        for (int i = 0; i < result.length(); i++) {
            int bnbn = result.indexOf(" \\");
            if (bnbn != -1) {
                result = result.substring(0, bnbn) + result.substring(bnbn + 1, result.length());
            } else {
                break;
            }
        }
        result = result.replaceAll("\\\\C\\d{6}", "");
        result = result.replaceAll("\\\\C-\\d{5}", "");
        result = result.replaceAll("\\\\B\\d{3}", "");
        result = result.replaceAll("\\\\I\\d{3}", "");
        result = result.replaceAll("\\\\F\\w{5}", "");
        result = result.replaceAll("\\\\y\\d{1}", "");
        result = result.replaceAll("\\\\c\\d{12}", "");
        result = result.replaceAll("\\\\b\\d{12}", "");
        result = result.replaceAll("\\\\s\\d{12}", "");
        result = result.replaceAll("\\\\S\\d{2}", "");
        result = result.replaceAll("\\\\f\\w{5}", "");
        result = result.replaceAll("\\\\N\\d{2}", "");
        result = result.replaceAll("\\\\r\\d{12}", "");
        result = result.replaceAll("\\\\P\\d{2}", "");
        result = result.replaceAll("\\n", "");
        result = result.replaceAll("\\\\n", "");
        result = result.replaceAll("\\\\W", "");
        result = result.replaceAll("\\\\\bt", "");
        result = result.replaceAll("\\\\bt", "");
        result = result.replaceAll("\\bt", "");
        return result.trim();
    }

    /**
     * 同洲和电明协议
     * 过滤掉内容中的显示格式
     *
     * @param result
     * @return
     */
    public static String touZhouDianMingFilterContent(String result) {
        if (CommonUtil.isNull(result))
            return null;
        result = result.replaceAll("\\\\C\\d{6}", "");
        result = result.replaceAll("\\\\B\\d{3}", "");
        result = result.replaceAll("\\\\P\\d{3}", "");
        result = result.replaceAll("\\\\J\\d{3}", "");
        result = result.replaceAll("\\\\G\\d{3}", "");
        result = result.replaceAll("\\\\M\\d{2}", "");
        result = result.replaceAll("\\\\N\\d{3}", "");
        result = result.replaceAll("\\\\Fs\\d{4}", "");
        result = result.replaceAll("\\\\F\\w{5}", "");
        result = result.replaceAll("\\\\T\\d{12}", "");
        result = result.replaceAll("\\\\K\\d{12}", "");
        result = result.replaceAll("\\\\R\\d{12}", "");
        result = result.replaceAll("\\\\A", "");
        result = result.replaceAll("\\\\W默认", "");
        result = result.replaceAll("\\\\W", "");
        result = result.replaceAll("\\n", "");
        result = result.replaceAll("\\\\n", "");
        return result.trim();
    }
    /**
     * 去当前设备故障信息
     * 去掉协议的开始、地址位、结束和标识符
     *
     * @param content
     * @return
     */
    public static String preDianMingFaultHandle(String content) {
        try {
            String temp = content.trim();
            temp = temp.replaceAll("1B E7", "02");
            temp = temp.replaceAll("1B E8", "03");
            temp = temp.replaceAll("1B 00", "1B");
            temp = temp.substring(21, temp.length() - 8);
            return temp.trim();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 显科协议
     * 过滤掉内容中的显示格式
     *
     * @param result
     * @return
     */
    public static String xianKeFilterContent(String result) {
        if (CommonUtil.isNull(result))
            return null;
        result = result.replaceAll("\\\\C\\d{6}", "");
        result = result.replaceAll("\\\\I\\d{3}", "");
        result = result.replaceAll("\\\\A\\d{3}", "");
        result = result.replaceAll("\\\\F\\w{2}", "");
        result = result.replaceAll("\\\\T\\d{12}", "");
        result = result.replaceAll("\\\\B\\d{12}", "");
        result = result.replaceAll("\\\\Mx\\d{2}", "");
        //result = result.replaceAll("\\U", " ");
        //result = result.replaceAll("\\N", " ");
        result = result.replaceAll("\\\\R\\d{2}", "");
        result = result.replaceAll("\\\\X\\d{12}", "");
        result = result.replaceAll("\\n", "");
        result = result.replaceAll("\\\\n", "");
        result = result.replaceAll("\\\\W", "");
        return result.trim();
    }

    /**
     * 鼎恩协议
     * 过滤掉内容中的显示格式
     *
     * @param result
     * @return
     */
    public static String dingEnFilterContent(String result) {
        if (CommonUtil.isNull(result))
            return null;
        result = result.replaceAll("\\\\C\\d{6}", "");
        result = result.replaceAll("\\\\B\\{3}", "");
        result = result.replaceAll("\\\\c\\d{12}", "");
        result = result.replaceAll("\\\\b\\d{12}", "");
        result = result.replaceAll("\\\\S\\d{2}", "");
        result = result.replaceAll("\\\\f\\w{5}", "");
        result = result.replaceAll("\\\\N\\d{2}", "");
        result = result.replaceAll("\\n", "");
        result = result.replaceAll("\\\\n", "");
        result = result.replaceAll("\\\\W", "");
        //result = result.replaceAll("\n", " ");
        return result.trim();
    }
}