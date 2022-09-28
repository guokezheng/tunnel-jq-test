package com.tunnel.business.utils.util;


import ch.ethz.ssh2.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class SSHUtil {
    public static Connection conn = null;
    public static boolean login(String ip,String usr,String psword){
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        conn = new Connection(ip);
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            return conn.authenticateWithPassword(usr, psword);
        } catch (IOException e) {
            System.err.printf("用户%s密码%s登录服务器%s失败！", usr, psword, ip);
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 上传本地文件到服务器目录下
     * @param conn Connection对象
     * @param fileName 本地文件
     * @param remotePath 服务器目录
     */
    public static void putFile(Connection conn, String fileName, String remotePath){
        SCPClient sc = new SCPClient(conn);
        try {
            //将本地文件放到远程服务器指定目录下，默认的文件模式为 0600，即 rw，
            //如要更改模式，可调用方法 put(fileName, remotePath, mode),模式须是4位数字且以0开头
            sc.put(fileName, remotePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载服务器文件到本地目录
     * @param fileName 服务器文件
     * @param localPath 本地目录
     */
    public static void copyFile(Connection conn, String fileName,String localPath){
        SCPClient sc = new SCPClient(conn);
        try {
            sc.get(fileName, localPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，获取文件各个属性
     * @param[in] conn Conncetion对象
     * @param[in] remotePath 远程主机的指定目录
     */
    public static void getFileProperties(Connection conn, String remotePath){
        try {
            SFTPv3Client sft = new SFTPv3Client(conn);
            Vector<?> v = sft.ls(remotePath);

            for(int i=0;i<v.size();i++){
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //文件名
                String filename = s.filename;
                //文件的大小
                Long fileSize = s.attributes.size;
            }

            sft.close();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，删除指定文件
     * @param[in] fileName 文件名
     * @param[in] remotePath 远程主机的指定目录
     * @return
     */
    public static void delFile(Connection conn,String remotePath, String fileName) {
        try {
            SFTPv3Client sft = new SFTPv3Client(conn);
            //获取远程目录下文件列表
            Vector<?> v = sft.ls(remotePath);

            for (int i = 0; i < v.size(); i++) {
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //判断列表中文件是否与指定文件名相同
                if (s.filename.equals(fileName)) {
                    //rm()方法中，须是文件绝对路径+文件名称
                    sft.rm(remotePath + s.filename);
                }
                sft.close();
            }
        } catch(Exception e1){
            e1.printStackTrace();
        }
    }
    /**
     * 执行脚本
     * @param conn Connection对象
     * @param cmds 要在linux上执行的指令
     */
    public static int exec(Connection conn, String cmds){
        InputStream stdOut = null;
        InputStream stdErr = null;
        int ret = -1;
        try {
            //在connection中打开一个新的会话
            Session session = conn.openSession();
            //在远程服务器上执行linux指令
            session.execCommand(cmds);
            //指令执行结束后的输出
            stdOut = new StreamGobbler(session.getStdout());
            //指令执行结束后的错误
            stdErr = new StreamGobbler(session.getStderr());
            //等待指令执行结束
            session.waitForCondition(ChannelCondition.EXIT_STATUS, 60);
            //取得指令执行结束后的状态
            ret = session.getExitStatus();

            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return ret;
    }


}
