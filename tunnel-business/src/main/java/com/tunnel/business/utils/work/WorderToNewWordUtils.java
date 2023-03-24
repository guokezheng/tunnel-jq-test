package com.tunnel.business.utils.work;

import com.tunnel.business.service.event.impl.SdEventServiceImpl;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author zhai
 * @date 2023/3/8
 */
public class WorderToNewWordUtils {

    private static final Logger logger = LoggerFactory.getLogger(WorderToNewWordUtils.class);

    /**
     * 根据模板生成word文档
     * @param inputUrl 模板路径
     * @param textMap 需要替换的文本内容
     * @param mapList 需要动态生成的内容
     * @param type 2：不处理表头3:处理表头
     * @param listNum 第*个list需要合并列
     * @return
     */
    public static CustomXWPFDocument changWord(String inputUrl, Map <String, Object> textMap, List<List<String[]>> mapList, int type, int listNum) {
        CustomXWPFDocument document = null;
        try {
            //获取docx解析对象
            document = new CustomXWPFDocument(new ClassPathResource(inputUrl).getInputStream());
            //解析替换文本段落对象
            WorderToNewWordUtils.changeText(document, textMap);
            //解析替换表格对象
            WorderToNewWordUtils.changeTable(document, textMap, mapList, type, listNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 替换段落文本
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changeText(CustomXWPFDocument document, Map <String, Object> textMap){
        //获取段落集合
        List <XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            System.out.println(text);
            if(checkText(text)){
                List <XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    Object ob = changeValue(run.toString(), textMap);
                    if (ob instanceof String){
                        run.setText((String)ob,0);
                    }else if (ob instanceof Map){
                        run.setText("",0);
                        Map pic = (Map)ob;
                        int width = Integer.parseInt(pic.get("width").toString());
                        int height = Integer.parseInt(pic.get("height").toString());
                        int picType = getPictureType(pic.get("type").toString());
                        byte[] byteArray = (byte[]) pic.get("content");
                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                        try {
                            //document.createPicture(ind, width , height,paragraph);
                            run.addPicture(byteInputStream, picType, "", width*=9525, height*=9525);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 替换表格对象方法
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     * @param mapList 需要动态生成的内容
     */

    public static void changeTable(CustomXWPFDocument document, Map <String, Object> textMap, List<List<String[]>> mapList, int type, int listNum){
        //获取表格对象集合
        List <XWPFTable> tables = document.getTables();
        //循环所有需要进行替换的文本，进行替换
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            if(checkText( table.getText())){
                List< XWPFTableRow> rows = table.getRows();
                //遍历表格,并替换模板
                eachTable(document,rows, textMap);
            }
        }
        //操作word中的表格
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格，且不循环表头
            XWPFTable table = tables.get(i);
            //第二个表格使用daList，插入数据
            insertTable(table, null,mapList.get(i),type);
            List<Integer[]> indexList = startEnd(mapList.get(i));
            for (int c=0;c <indexList.size();c++){
                //合并行
                mergeCellVertically(table,0,indexList.get(c)[0]+(type == 2 ? 1 : 0),indexList.get(c)[1]+(type == 2 ? 1 : 0));
            }
            if(listNum-1 == i){
                List<String[]> list = mapList.get(i);
                for(int j = 0; j < list.size(); j++){
                    if(list.get(j)[list.get(j).length-1] == ""){
                        mergeCellsHorizontal(table,j,0,1);
                    }
                }
            }
        }
    }

    /**
     * 遍历表格
     * @paramrows 表格行对象
     * @paramtextMap 需要替换的信息集合
     */
    public static void eachTable(CustomXWPFDocument document, List<XWPFTableRow> rows , Map <String,Object> textMap){
        /*for (XWPFTableRow row : rows) {
            List <XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if(checkText(cell.getText())){
                    List <XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List <XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            Object ob = changeValue(run.toString(), textMap);
                            if (ob instanceof String){
                                run.setText((String)ob,0);
                            }else if (ob instanceof Map){
                                run.setText("",0);
                            }
                        }
                    }
                }
            }
        }*/
        for (XWPFTableRow row : rows) {
            List <XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if(checkText(cell.getText())){
                    List <XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List <XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            Object ob = changeValue(run.toString(), textMap);
                            if (ob instanceof String){
                                run.setText((String)ob,0);
                            }else if (ob instanceof Map){
                                run.setText("",0);

                                Map pic = (Map)ob;
                                int width = Integer.parseInt(pic.get("width").toString());
                                int height = Integer.parseInt(pic.get("height").toString());
                                int picType = getPictureType(pic.get("type").toString());
                                byte[] byteArray = (byte[]) pic.get("content");
                                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                                try {
                                    run.addPicture(byteInputStream, picType, "", width, height);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     * @param table 需要插入数据的表格
     * @param tableList 第四个表格的插入数据
     * @param daList 第二个表格的插入数据
     * @param type 表格类型：1-第一个表格 2-第二个表格 3-第三个表格 4-第四个表格
     */
    public static void insertTable(XWPFTable table, List <String> tableList,List <String[]> daList,Integer type){
        if(2 == type || 3 == type){
            //2不处理表头3处理表头
            insterWordTableData(table,daList,type);
        }else if (4 == type){
            //插入表头下面第一行的数据
            for(int i = 0;i < tableList.size(); i++){
                XWPFTableRow row = table.createRow();
                List<XWPFTableCell> cells = row.getTableCells();
                cells.get(0).setText(tableList.get(i));
            }
        }
    }

    public static void insterWordTableData(XWPFTable table,List <String[]> daList, int type){
        //创建行和创建需要的列
        for(int i = 1; i < daList.size(); i++){
            XWPFTableRow row = table.insertNewTableRow(1);//添加一个新行
            //根据内容添加列
            int length = daList.get(i).length;
            for(int j = 0; j < length; j++){
                row.createCell();
            }
            /*row.createCell();//添加第一个列
            row.createCell();//添加第二个列*/
        }
        //创建行,根据需要插入的数据添加新行，不处理表头
        for(int i = 0;i < daList.size(); i++){
            List<XWPFTableCell> cells = table.getRow(i+(type == 2 ? 1 : 0)).getTableCells();
            for(int j = 0; j < cells.size(); j++){
                XWPFTableCell cell02 = cells.get(j);
                cell02.setText(daList.get(i)[j]);
            }
        }
    }

    /**
     * 判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkText(String text){
        boolean check = false;
        if(text.indexOf("$")!= -1){
            check = true;
        }
        return check;
    }

    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static Object changeValue(String value, Map <String, Object> textMap){
        Set<Entry<String, Object>> textSets = textMap.entrySet();
        Object valu = "";
        for (Entry <String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = textSet.getKey();
            if(value.indexOf(key)!= -1){
                valu = textSet.getValue();
            }
        }
        return valu;
    }

    /**
     * 得到文件流
     * @param url 图片地址
     * @return
     */
    public static byte[] getFileStream(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3 * 1000);
            conn.setReadTimeout(3 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 功能描述:读取文件流
     *
     * @param inStream
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static byte[] readInputStream(InputStream inStream) throws
            Exception {
        ByteArrayOutputStream outStream = new
                ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 将输入流中的数据写入字节数组
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose){
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(isClose){
                try {
                    in.close();
                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 合并行
     * @param table
     * @param col 需要合并的列
     * @param fromRow 开始行
     * @param toRow 结束行
     */
    public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++){
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if(rowIndex == fromRow){
                vmerge.setVal(STMerge.RESTART);
            } else {
                vmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    /**
     * 合并列
     * @param table
     * @param row 需要合并的行
     * @param fromCell 开始列
     * @param toCell 结束列
     */
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if ( cellIndex == fromCell ) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 获取需要合并单元格的下标
     * @return
     */
    public static List<Integer[]> startEnd(List <String[]> daList){
        List <Integer[]> indexList = new ArrayList <Integer[]>();
        List <String> list = new ArrayList <String>();
        for (int i=0;i <daList.size();i++){
            list.add(daList.get(i)[0]);
        }
        Map<Object, Integer> tm = new HashMap<Object,Integer>();
        for (int i=0;i <daList.size();i++){
            if (!tm.containsKey(daList.get(i)[0])) {
                tm.put(daList.get(i)[0], 1);
            } else {
                int count = tm.get(daList.get(i)[0]) + 1;
                tm.put(daList.get(i)[0], count);
            }
        }
        for (Entry<Object, Integer> entry : tm.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (list.indexOf(key) != (-1)){
                /*Integer[] index = new Integer[2];
                index[0] = list.indexOf(key);
                index[1] = list.lastIndexOf(key);*/
                indexList.addAll(startOrEnd(key, value, list));
            }
        }
        return indexList;
    }

    public static List<Integer[]> startOrEnd(String key, String value, List<String> list){
        Integer[] index = new Integer[2];
        List<Integer[]> indexList = new ArrayList<>();
        int count = 0;
        int flaseCount = 0;
        for(int i = 0; i < list.size()-1; i++){
            if(count != 0){
                flaseCount++;
            }

            if(key.equals(list.get(i)) && key.equals(list.get(i+1))){
                count++;
                if(count == 1){
                    index[0] = i;
                }
                index[1] = i+1;
                flaseCount = 0;
            }

            if(flaseCount != 0){
                indexList.add(index);
                index = new Integer[2];
                count = 0;
                flaseCount = 0;
            }

            if(i+2 == list.size() && indexList.size() == 0){
                index[0] = list.indexOf(key);;
                index[1] = list.lastIndexOf(key);
                indexList.add(index);
            }
        }
        return indexList;
    }
}
