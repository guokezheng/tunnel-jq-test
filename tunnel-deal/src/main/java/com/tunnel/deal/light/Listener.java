package com.tunnel.deal.light;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class Listener {
    /**
     * 通过轮询模式监听串口COM15
     * 当串口COM8传来数据的时候读取数据
     */
    public static void main(String[] args) {
        CommPortIdentifier com15 = null;//定义需要接收数据的端口(未打开)
        SerialPort serialCom15 = null;//打开的端口
        InputStream inputStream = null;//端口输入流
        System.out.println("开始监听端口COM15收到的数据");
        try {
            //获取并打开串口com15
            com15 =CommPortIdentifier.getPortIdentifier("COM15");
            serialCom15 = (SerialPort) com15.open("Com15Listener", 1000);
            //System.out.println(serialCom15);
            //获取串口的输入流对象
            inputStream = serialCom15.getInputStream();
            //从串口读出数=
            //定义用于缓存读入数据的数组
            byte []cache = new byte[1024];
            //记录已经到达串口COM15且未被读取的数据的字节（Byte）数
            int availableBytes = 0;
            //通过无限循环的方式来扫描COM15,检查是否有数据到达端口,间隔20ms
            while (true) {
                //获取串口15收到的字节数
                availableBytes = inputStream.available();
                //如果可用字节数大于0则开始转换并获取数据
                while (availableBytes>0) {
                    //从串口的输入流对象中读入数据并将数据存放到缓存数组中
                    inputStream.read(cache);
                    //将获取到的数据进行转码并输出
                    for (int i = 0; i < cache.length&&i<availableBytes; i++) {
                        System.out.print((char)cache[i]);
                    }
                    System.out.println();
                    availableBytes = inputStream.available();
                }
                //线程睡眠20ms
                Thread.sleep(20);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch (NoSuchPortException e) {
            //找不到串口的情况下抛出该异常
            e.printStackTrace();
        } catch (PortInUseException e) {
            //如果因为端口被占用而导致打开失败，则抛出该异常
            e.printStackTrace();
        } catch (IOException e) {
            //如果获取输出流失败，则抛出该异常
            e.printStackTrace();
        }
    }
}
