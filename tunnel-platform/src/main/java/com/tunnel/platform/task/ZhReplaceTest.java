package com.tunnel.platform.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 施工桩号 和实际运营桩号替换工具类
 * 说明 ： 1.读取 新旧桩号对应关系文件 文件格式 加强段YK72+670 = 加强段YK179+015  @ YK179+015    分为三段 1 旧设备桩号名称 2 新设备桩号名称  3 新桩号  然后放到map集合
 *        2.读取就隧道表中的配置图的html  然后根绝 上面map 替换配置图的html 中的旧桩号改成新的桩号
 */
public class ZhReplaceTest {
    public static void main(String[] args) {
//        String filePath = "path/to/your/file.txt";
        String filePath = "D:\\作业\\hs\\nps.txt";

        Map<String, String> map = readFileAndStoreInMap(filePath);
        System.out.println(map);
        System.out.println("df");

        String filePath1 = "D:\\作业\\hs\\npssy.txt"; // 指定你的文件路径
        String line = "";
        StringBuilder jsonString = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath1))) {
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = JSON.parseObject(jsonString.toString());
        JSONArray eqList = jsonObject.getJSONArray("eqList");
        for (int i = 0; i < eqList.size(); i++) {
            JSONObject eqObj = eqList.getJSONObject(i);
            String oldEqName = eqObj.getString("eqName");
            String oldpile = eqObj.getString("pile");
            System.out.println("Original eqName: " + oldEqName); // 输出原始的eqName值
            if("固定摄像机YK72+875".equals(oldEqName)){
                System.out.println("wwww");
            }
            String s = map.get(oldEqName); // 替换为你想要的值
            if(s==null){
                continue;
            }
            String eqName = "";
            String pile = "";
            int ds = 0;
            ds =  s.split("@").length;
            if(ds>=2){
                eqName = s.split("@")[0].trim();
                pile = s.split("@")[1].trim();
            }else  if(ds>=1){
                eqName = s.split("@")[0].trim();
            }
            eqObj.put("eqName", eqName);
            eqObj.put("pile", pile);
        }
        String outputFilePath = "D:\\作业\\hs\\npssy1.txt"; // 指定输出文件的路径
        try (FileWriter writer1 = new FileWriter(outputFilePath)) {
            jsonObject.writeJSONString(writer1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
    }
    public static  Map<String, String> readFileAndStoreInMap(String filePath) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("="); // 假设每一行都是"wqwq = ewew"这样的结构
                if (parts.length == 2) { // 确保每一行都有两个部分
                    String key = parts[0].trim(); // 去除空格
                    String value = parts[1].trim(); // 去除空格
                    map.put(key, value); // 将键值对存入Map集合中
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
