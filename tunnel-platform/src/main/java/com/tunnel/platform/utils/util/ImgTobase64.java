package com.tunnel.platform.utils.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

public class ImgTobase64 {
    public static String ioToBase64(String url) {
        String strBase64 = null;
        if (url == null || "".equals(url)) {
            strBase64 = new String("data:image/jpg;base64,");
        } else {
            try {
                InputStream in = new FileInputStream(url);
                // in.available()返回文件的字节长度
                byte[] bytes = new byte[in.available()];
                // 将文件中的内容读入到数组中
                in.read(bytes);
                //将字节流数组转换为字符串
                strBase64 = new String("data:image/jpg;base64,"+ Base64.getEncoder().encodeToString(bytes));
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strBase64;
    }
}
