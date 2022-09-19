package com.tunnel.platform.business.vms.device;


import com.tunnel.platform.business.vms.core.DevicesManager;
import com.tunnel.business.utils.util.CRC16;
import com.tunnel.business.utils.util.CommonUtil;

/**
 *
 */
public class DisPlayUtil {


    private DisPlayUtil() {

    }

    //电明内容格式
    private static String DIANMING_FILE = "2E6C73742B";
    //电明内容偏移地址长度
    private static String FIRST_DIANMING_FILE_ADDRESS = "3030303030303030";
    private static String SECOND_DIANMING_FILEADDRESS = "3030303032303438";
    private static String THIRD_DIANMING_FILEADDRESS = "3030303034303936";
    //电明内容返回值格式：(目的地址)(源地址)(指令头)
    private static String RETURN_DIANMING_FILEFORMAT = "3030303130372E2F706C61796C6973742F706C6179";


    //显科文件格式
    private static String XIANKE_FILE = "2E786B6C";
    //显科内容偏移地址长度
    private static String FIRST_XIANKE_FILE_ADDRESS = "30303030";
    private static String SECOND_XIANKE_FILE_ADDRESS = "30303031";
    private static String THIRD_XIANKE_FILE_ADDRESS = "30303032";
    //显科内容返回值格式：(目的地址)(源地址)(指令头)
    private static String RETURN_XIANKE_FILE_FORMAT = "323130303031326C6973745C";


    //三思内容偏移地址长度
    private static String FIRST_SANSI_FILE_ADDRESS = "00000000";
    private static String SECOND_SANSI_FILE_ADDRESS = "00000800";
    private static String THIRD_SANSI_FILE_ADDRESS = "00001000";


    public static String getListDisPlayForTongZhouAndDianMing(String deviceId, String currentVersion) {

        StringBuffer DIANMINGFILE = new StringBuffer(DisPlayUtil.DIANMING_FILE);
        StringBuffer FIRSTFILEADDRESS = new StringBuffer(DisPlayUtil.FIRST_DIANMING_FILE_ADDRESS);
        StringBuffer SECONDFILEADDRESS = new StringBuffer(DisPlayUtil.SECOND_DIANMING_FILEADDRESS);
        StringBuffer THIRDFILEADDRESS = new StringBuffer(DisPlayUtil.THIRD_DIANMING_FILEADDRESS);


        if (CommonUtil.isNull(currentVersion))
            return null;
        //返回情报板内容
        StringBuffer content = new StringBuffer();

        //获取当前播放列表编号
        String result = DevicesManager.getInstance().executeCommand(deviceId, currentVersion,"100");
        if (result == null) {
            return null;
        }

        String listNumber = result.substring(21, result.length() - 9);

        listNumber = listNumber.replaceAll(" ", "");
        StringBuffer firstCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_DIANMING_FILEFORMAT);
        StringBuffer listNumberBuffer = new StringBuffer(listNumber);
        StringBuffer appendCommand = firstCommandBuffer.append(listNumberBuffer).append(DIANMINGFILE).append(FIRSTFILEADDRESS);

        StringBuffer firstTempCommand = new StringBuffer(appendCommand);

        for (int i = appendCommand.length() - 2; i > 0; i -= 2) {
            appendCommand.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(appendCommand.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);


        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);

        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        String firstResult = DevicesManager.getInstance().executeCommand(deviceId, firstCommand.toString(),"100");
        if (firstResult == null) {
            return null;
        }
        String firstContent = firstResult.replaceAll(" ", "");

        //去帧头，目的地址，源地址，指令头，不定长完整路径名，文件下载选项，文件偏移地址------ 取不定长数据   ---校验码，帧尾
        int index1 = firstContent.indexOf("2B3030303030303030");
        String substring = firstContent.substring(0, index1);
        String subContent = firstContent.substring(index1);

        subContent = subContent.replaceAll("1B00", "1B");
        subContent = subContent.replaceAll("1BE7", "02");
        subContent = subContent.replaceAll("1BE8", "03");
        subContent = subContent.substring(18, subContent.length() - 6);

        byte[] subContentBytes = firstContent.getBytes();
        content = content.append(subContent);
        if ((subContentBytes.length / 2) >= 2048) {
            StringBuffer secondCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_DIANMING_FILEFORMAT);
            StringBuffer secondAppendCommand = secondCommandBuffer.append(listNumberBuffer).append(DIANMINGFILE).append(SECONDFILEADDRESS);
            StringBuffer secondTempCommand = new StringBuffer(secondAppendCommand);
            for (int i = secondAppendCommand.length() - 2; i > 0; i -= 2) {
                secondAppendCommand.insert(i, ",");
            }

            String secondcommandInfo = hex2Str(secondAppendCommand.toString());
            byte[] secondBytes = secondcommandInfo.getBytes();
            String secondCrcResult = CRC16.CRC_16_XMODEM(secondBytes, secondBytes.length);
            StringBuffer secondCrcResultBuffer = new StringBuffer(secondCrcResult);
            startBuffer = new StringBuffer("02");
            StringBuffer secondCommand = startBuffer.append(secondTempCommand).append(secondCrcResultBuffer).append("03");
            for (int i = secondCommand.length() - 2; i > 0; i -= 2) {
                secondCommand.insert(i, " ");
            }
            String secondResult = DevicesManager.getInstance().executeCommand(deviceId, secondCommand.toString(),"100");
            if (secondResult != null) {
                String secondContent = secondResult.replaceAll(" ", "");
                int index2 = secondContent.indexOf("2B3030303032303438");
                String secondSubContent = secondContent.substring(index2);

                secondSubContent = secondSubContent.replaceAll("1B00", "1B");
                secondSubContent = secondSubContent.replaceAll("1BE7", "02");
                secondSubContent = secondSubContent.replaceAll("1BE8", "03");
                secondSubContent = secondSubContent.substring(18, secondSubContent.length() - 6);
                byte[] secondSubContentBytes = secondSubContent.getBytes();
                content = content.append(secondSubContent);

                if ((secondSubContentBytes.length / 2) >= 2048) {
                    StringBuffer thirdCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_DIANMING_FILEFORMAT);
                    StringBuffer thirdAppendCommand = thirdCommandBuffer.append(listNumberBuffer).append(DIANMINGFILE).append(THIRDFILEADDRESS);
                    StringBuffer thirdTempCommand = new StringBuffer(thirdAppendCommand);
                    for (int i = thirdAppendCommand.length() - 2; i > 0; i -= 2) {
                        thirdAppendCommand.insert(i, ",");
                    }
                    String thirdCommandInfo = hex2Str(thirdAppendCommand.toString());
                    byte[] thirdBytes = thirdCommandInfo.getBytes();
                    String thirdCrcResult = CRC16.CRC_16_XMODEM(thirdBytes, thirdBytes.length);
                    StringBuffer thirdCrcResultBuffer = new StringBuffer(thirdCrcResult);
                    startBuffer = new StringBuffer("02");
                    StringBuffer thirdCommand = startBuffer.append(thirdTempCommand).append(thirdCrcResultBuffer).append("03");
                    for (int i = thirdCommand.length() - 2; i > 0; i -= 2) {
                        thirdCommand.insert(i, " ");
                    }
                    String thirdResult = DevicesManager.getInstance().executeCommand(deviceId, secondCommand.toString(),"100");
                    if (secondResult != null) {
                        String thirdContent = thirdResult.replaceAll(" ", "");
                        int index3 = thirdContent.indexOf("2B3030303034303936");
                        String thirdSutContent = thirdContent.substring(index3);
                        thirdSutContent = thirdSutContent.replaceAll("1B00", "1B");
                        thirdSutContent = thirdSutContent.replaceAll("1BE7", "02");
                        thirdSutContent = thirdSutContent.replaceAll("1BE8", "03");
                        thirdSutContent = thirdSutContent.substring(18, thirdSutContent.length() - 6);
                        content = content.append(thirdSutContent);
                    }
                }
            }

        }
        StringBuffer stringBuffer = new StringBuffer(substring);
        content = stringBuffer.append(content).append(new StringBuffer(index1)).append(new StringBuffer("000003"));
        for (int i = content.length() - 2; i > 0; i -= 2) {
            content.insert(i, " ");
        }
        return content.toString();
    }

    public static String getXianKePlaylistDisPlay(String deviceId, String currentVersion) {


        StringBuffer XIANKEFILE = new StringBuffer(DisPlayUtil.XIANKE_FILE);
        StringBuffer FIRSTXIANKEFILEADDRESS = new StringBuffer(DisPlayUtil.FIRST_XIANKE_FILE_ADDRESS);
        StringBuffer SECONDXIANKEFILEADDRESS = new StringBuffer(DisPlayUtil.SECOND_XIANKE_FILE_ADDRESS);
        StringBuffer THIRDXIANKEFILEADDRESS = new StringBuffer(DisPlayUtil.THIRD_XIANKE_FILE_ADDRESS);


        if (CommonUtil.isNull(currentVersion))
            return null;
        //返回情报板内容
        StringBuffer content = new StringBuffer();

        //获取当前播放列表编号
        String result = DevicesManager.getInstance().executeCommand(deviceId, currentVersion,"100");
        String listNumber = result.substring(18, result.length() - 21);

        listNumber = listNumber.replaceAll(" ", "");
        StringBuffer firstCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_XIANKE_FILE_FORMAT);
        StringBuffer listNumberBuffer = new StringBuffer(listNumber);
        StringBuffer appendCommand = firstCommandBuffer.append(listNumberBuffer).append(XIANKEFILE).append(FIRSTXIANKEFILEADDRESS);

        StringBuffer firstTempCommand = new StringBuffer(appendCommand);

        for (int i = appendCommand.length() - 2; i > 0; i -= 2) {
            appendCommand.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(appendCommand.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        String firstResult = DevicesManager.getInstance().executeCommand(deviceId, firstCommand.toString(),"100");
        String firstContent = firstResult.replaceAll(" ", "");


        //去帧头，目的地址，源地址，指令头，不定长完整路径名，文件下载选项，文件偏移地址------ 取不定长数据   ---校验码，帧尾
        int index1 = firstContent.indexOf("2E786B6C30303030");
        String substring = firstContent.substring(0, index1);
        String subContent = firstContent.substring(index1);
        subContent = subContent.substring(16, subContent.length() - 6);
        byte[] subContentBytes = subContent.getBytes();
        content = content.append(subContent);
        if ((subContentBytes.length / 2) >= 2048) {
            StringBuffer secondCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_XIANKE_FILE_FORMAT);
            StringBuffer secondAppendCommand = secondCommandBuffer.append(listNumberBuffer).append(XIANKEFILE).append(SECONDXIANKEFILEADDRESS);
            StringBuffer secondTempCommand = new StringBuffer(secondAppendCommand);
            for (int i = secondAppendCommand.length() - 2; i > 0; i -= 2) {
                secondAppendCommand.insert(i, ",");
            }

            String secondcommandInfo = hex2Str(secondAppendCommand.toString());
            byte[] secondBytes = secondcommandInfo.getBytes();
            String secondCrcResult = CRC16.CRC_16_XMODEM(secondBytes, secondBytes.length);
            StringBuffer secondCrcResultBuffer = new StringBuffer(secondCrcResult);
            startBuffer = new StringBuffer("02");
            StringBuffer secondCommand = startBuffer.append(secondTempCommand).append(secondCrcResultBuffer).append("03");
            for (int i = secondCommand.length() - 2; i > 0; i -= 2) {
                secondCommand.insert(i, " ");
            }
            String secondResult = DevicesManager.getInstance().executeCommand(deviceId, secondCommand.toString(),"100");
            String secondContent = secondResult.replaceAll(" ", "");
            int index2 = secondContent.indexOf("2E786B6C30303031");
            String secondSubContent = secondContent.substring(index2);
            secondSubContent = secondSubContent.substring(16, secondSubContent.length() - 6);
            byte[] secondSubContentBytes = secondSubContent.getBytes();
            content = content.append(secondSubContent);

            if ((secondSubContentBytes.length / 2) >= 2048) {
                StringBuffer thirdCommandBuffer = new StringBuffer(DisPlayUtil.RETURN_XIANKE_FILE_FORMAT);
                StringBuffer thirdAppendCommand = thirdCommandBuffer.append(listNumberBuffer).append(XIANKEFILE).append(THIRDXIANKEFILEADDRESS);
                StringBuffer thirdTempCommand = new StringBuffer(thirdAppendCommand);
                for (int i = thirdAppendCommand.length() - 2; i > 0; i -= 2) {
                    thirdAppendCommand.insert(i, ",");
                }
                String thirdCommandInfo = hex2Str(thirdAppendCommand.toString());
                byte[] thirdBytes = thirdCommandInfo.getBytes();
                String thirdCrcResult = CRC16.CRC_16_XMODEM(thirdBytes, thirdBytes.length);
                StringBuffer thirdCrcResultBuffer = new StringBuffer(thirdCrcResult);
                startBuffer = new StringBuffer("02");
                StringBuffer thirdCommand = startBuffer.append(thirdTempCommand).append(thirdCrcResultBuffer).append("03");
                for (int i = thirdCommand.length() - 2; i > 0; i -= 2) {
                    thirdCommand.insert(i, " ");
                }
                String thirdResult = DevicesManager.getInstance().executeCommand(deviceId, secondCommand.toString(),"100");
                String thirdContent = thirdResult.replaceAll(" ", "");
                int index3 = thirdContent.indexOf("2E786B6C30303032");
                String thirdSutContent = thirdContent.substring(index3);
                thirdSutContent = thirdSutContent.substring(16, thirdSutContent.length() - 6);
                content = content.append(thirdSutContent);
            }
        }
        StringBuffer stringBuffer = new StringBuffer(substring);
        content = stringBuffer.append(content).append(new StringBuffer(index1)).append(new StringBuffer("000003"));
        for (int i = content.length() - 2; i > 0; i -= 2) {
            content.insert(i, " ");
        }
        return content.toString();
    }

    public static String getlistDisPlayForSanSiAndGuangDian(String firstCommand, String deviceId) {


        StringBuffer FIRSTSANSIFILEADDRESS = new StringBuffer(DisPlayUtil.FIRST_SANSI_FILE_ADDRESS);
        StringBuffer SECONDSANSIFILEADDRESS = new StringBuffer(DisPlayUtil.SECOND_SANSI_FILE_ADDRESS);
        StringBuffer THIRDSANSIFILEADDRESS = new StringBuffer(DisPlayUtil.THIRD_SANSI_FILE_ADDRESS);

        //返回情报板内容
        StringBuffer content = new StringBuffer();

        String firstResult = DevicesManager.getInstance().executeCommand(deviceId, firstCommand.toString(),"100");
        if (firstResult == null) {
            return null;
        }
        String firstContent = firstResult.replaceAll(" ", "");


        //去帧头，目的地址址------ 取不定长数据   ---校验码，帧尾
        firstContent = firstContent.replaceAll("1BE7", "02");
        firstContent = firstContent.replaceAll("1BE8", "03");
        firstContent = firstContent.replaceAll("1B00", "1B");

        String subContent = firstContent.substring(6, firstContent.length() - 6);


        content = content.append(subContent);
        byte[] subContentBytes = subContent.getBytes();

        if ((subContentBytes.length / 2) >= 2048) {

            String secondAppendCommand = firstCommand.replaceAll(FIRSTSANSIFILEADDRESS.toString(), SECONDSANSIFILEADDRESS.toString());

            String secondSubstring = secondAppendCommand.substring(2, secondAppendCommand.length() - 2);

            StringBuffer secondCommandBuffer = new StringBuffer(secondSubstring);
            for (int i = secondCommandBuffer.length() - 2; i > 0; i -= 2) {
                secondCommandBuffer.insert(i, ",");
            }
            String secondcommandInfo = hex2Str(secondCommandBuffer.toString());
            byte[] secondBytes = secondcommandInfo.getBytes();
            String secondCrcResult = CRC16.CRC_16_XMODEM(secondBytes, secondBytes.length);
            StringBuffer secondCrcResultBuffer = new StringBuffer(secondCrcResult);
            StringBuffer startBuffer = new StringBuffer("02");
            StringBuffer secondCommand = startBuffer.append(secondCrcResultBuffer).append("03");
            for (int i = secondCommand.length() - 2; i > 0; i -= 2) {
                secondCommand.insert(i, " ");
            }
            String secondResult = DevicesManager.getInstance().executeCommand(deviceId, secondCommand.toString(),"100");
            if (secondResult != null) {
                String secondContent = secondResult.replaceAll(" ", "");

                //去帧头，目的地址址------ 取不定长数据   ---校验码，帧尾

                secondContent = secondContent.replaceAll("1BE7", "02");
                secondContent = secondContent.replaceAll("1BE8", "03");
                secondContent = secondContent.replaceAll("1B00", "1B");

                String secondSubContent = secondContent.substring(6, firstContent.length() - 6);
                content = content.append(secondSubContent);
                byte[] secondSubContentBytes = secondSubContent.getBytes();
                if ((secondSubContentBytes.length / 2) >= 2048) {


                    String thirdAppendCommand = firstCommand.replaceAll(FIRSTSANSIFILEADDRESS.toString(), THIRDSANSIFILEADDRESS.toString());

                    String thirdSubstring = thirdAppendCommand.substring(2, thirdAppendCommand.length() - 2);

                    StringBuffer thirdCommandBuffer = new StringBuffer(thirdSubstring);
                    for (int i = thirdCommandBuffer.length() - 2; i > 0; i -= 2) {
                        thirdCommandBuffer.insert(i, ",");
                    }
                    String thirdcommandInfo = hex2Str(thirdCommandBuffer.toString());
                    byte[] thirdBytes = thirdcommandInfo.getBytes();
                    String thirdCrcResult = CRC16.CRC_16_XMODEM(thirdBytes, thirdBytes.length);
                    StringBuffer thirdCrcResultBuffer = new StringBuffer(thirdCrcResult);
                    startBuffer = new StringBuffer("02");
                    StringBuffer thirdCommand = startBuffer.append(thirdCrcResultBuffer).append("03");
                    for (int i = thirdCommand.length() - 2; i > 0; i -= 2) {
                        thirdCommand.insert(i, " ");
                    }
                    String thirdResult = DevicesManager.getInstance().executeCommand(deviceId, thirdCommand.toString(),"100");

                    if (secondResult != null) {
                        String thirdContent = thirdResult.replaceAll(" ", "");
                        //去帧头，目的地址址------ 取不定长数据   ---校验码，帧尾
                        thirdContent = thirdContent.replaceAll("1BE7", "02");
                        thirdContent = thirdContent.replaceAll("1BE8", "03");
                        thirdContent = thirdContent.replaceAll("1B00", "1B");

                        String thirdSubContent = thirdContent.substring(6, firstContent.length() - 6);
                        content = content.append(thirdSubContent);
                    }
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer("023031");
        content = stringBuffer.append(content).append(new StringBuffer("000003"));
        for (int i = content.length() - 2; i > 0; i -= 2) {
            content.insert(i, " ");
        }
        return content.toString();
    }

    public static String getDingEnDisPlay(String firstCommand, String deviceId) {


        return null;
    }


    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        String[] split = hex.split(",");
        for (String str : split) {
            int i = Integer.parseInt(str, 16);
            sb.append((char) i);
        }
        return sb.toString();
    }
}

