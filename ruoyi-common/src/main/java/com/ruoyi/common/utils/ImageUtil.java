package com.ruoyi.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

public class ImageUtil {

	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author:dzy
	 * @CreateTime:
	 * @param file base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	 */
	public static String generateImage(String file, String path,String even) {
		// 解密
		try {
			// 图片分类路径+图片名+图片后缀
			String imgClassPath = path.concat(even).concat(UUID.randomUUID().toString()).replaceAll("-","").concat(".jpg");
			// 解密
			Base64.Decoder decoder = Base64.getDecoder();
			// 去掉base64前缀 data:image/jpeg;base64,
			file = file.substring(file.indexOf(",", 1) + 1, file.length());
			byte[] b = decoder.decode(file);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// 保存图片
			OutputStream out = new FileOutputStream(new File(imgClassPath));
			out.write(b);
			out.flush();
			out.close();
			// 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
			return imgClassPath;
		} catch (IOException e) {
			return null;
		}
	}
}
