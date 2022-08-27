package com.tunnel.platform.service.dataInfo.impl;

import com.tunnel.platform.domain.dataInfo.SdEventDetection;
import com.tunnel.platform.mapper.dataInfo.SdEventDetectionMapper;
import com.tunnel.platform.service.dataInfo.ISdEventDetectionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

/**
 * 事件监测记录Service业务层处理
 * 
 * @author liubaokui
 * @date 2021-04-21
 */
@Service
public class SdEventDetectionServiceImpl implements ISdEventDetectionService 
{
    @Autowired
    private SdEventDetectionMapper sdEventDetectionMapper;

    /**
     * 查询事件监测记录
     * 
     * @param id 事件监测记录ID
     * @return 事件监测记录
     */
    @Override
    public SdEventDetection selectSdEventDetectionById(Long id)
    {
        return sdEventDetectionMapper.selectSdEventDetectionById(id);
    }

    /**
     * 查询事件监测记录列表
     * 
     * @param sdEventDetection 事件监测记录
     * @return 事件监测记录
     */
    @Override
    public List<SdEventDetection> selectSdEventDetectionList(SdEventDetection sdEventDetection)
    {
    	List<SdEventDetection> list = sdEventDetectionMapper.selectSdEventDetectionList(sdEventDetection);
    	for(int i=0;i<list.size();i++){
    		String ftpAddress = list.get(i).getEventPicFtpAddress();
    		String url = ioToBase64(ftpAddress);
    		list.get(i).setEventPicFtpAddress(url);
    	}
        return list;
    }
    
    public static String ioToBase64(String url) {
		String strBase64 = null;
		if (url == null || "".equals(url)) {
			strBase64 = new String("data:image/jpg;base64,");
		} else {
			String user = url.substring(url.indexOf("/") + 2, url.indexOf("/") + 6);
			String password = url.substring(11, 17);
			String ip = url.substring(url.indexOf("@") + 1, url.indexOf("@") + 10);
			String remoteAbsoluteFile = url.substring(StringUtils.ordinalIndexOf(url,"/",3));
			try {
				FTPClient fc = new FTPClient();
				// 连接ftp
				fc.connect(ip, 21);
				// 登录
				fc.login(user, password);
				// 设置编码格式 
				fc.setControlEncoding("UTF-8");
				remoteAbsoluteFile = new String(remoteAbsoluteFile.getBytes("UTF-8"), "ISO-8859-1"); 
				// 获得输入流
				FTPFile[] fs = fc.listFiles();
				// 获得文件大小
				int size = (int) fs[0].getSize();
				byte[] bytes = new byte[size];
				ByteArrayOutputStream os = new ByteArrayOutputStream(); 
				// 写入输出流 
				fc.retrieveFile(remoteAbsoluteFile,os); 
				bytes = os.toByteArray();
				strBase64 = new String("data:image/jpg;base64," + Base64.getEncoder().encodeToString(bytes));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strBase64;
	}

    /**
     * 新增事件监测记录
     * 
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    @Override
    public int insertSdEventDetection(SdEventDetection sdEventDetection)
    {
        return sdEventDetectionMapper.insertSdEventDetection(sdEventDetection);
    }

    /**
     * 修改事件监测记录
     * 
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    @Override
    public int updateSdEventDetection(SdEventDetection sdEventDetection)
    {
        return sdEventDetectionMapper.updateSdEventDetection(sdEventDetection);
    }

    /**
     * 批量删除事件监测记录
     * 
     * @param ids 需要删除的事件监测记录ID
     * @return 结果
     */
    @Override
    public int deleteSdEventDetectionByIds(Long[] ids)
    {
        return sdEventDetectionMapper.deleteSdEventDetectionByIds(ids);
    }

    /**
     * 删除事件监测记录信息
     * 
     * @param id 事件监测记录ID
     * @return 结果
     */
    @Override
    public int deleteSdEventDetectionById(Long id)
    {
        return sdEventDetectionMapper.deleteSdEventDetectionById(id);
    }
}
