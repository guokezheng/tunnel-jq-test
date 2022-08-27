package com.tunnel.platform.service.dataInfo.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.dataInfo.SdIcyRoad;
import com.tunnel.platform.mapper.dataInfo.SdIcyRoadMapper;
import com.tunnel.platform.service.dataInfo.ISdIcyRoadService;
import com.tunnel.platform.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 道路结冰记录Service业务层处理
 *
 * @author liubaokui
 * @date 2021-03-26
 */
@Service
public class SdIcyRoadServiceImpl implements ISdIcyRoadService
{
    @Autowired
    private SdIcyRoadMapper sdIcyRoadMapper;

    @Autowired
    private static RedisCache redisCache;

    /**
     * 查询道路结冰记录
     *
     * @param id 道路结冰记录ID
     * @return 道路结冰记录
     */
    @Override
    public SdIcyRoad selectSdIcyRoadById(Long id)
    {
        return sdIcyRoadMapper.selectSdIcyRoadById(id);
    }

    /**
     * 查询道路结冰记录列表
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 道路结冰记录
     */
    @Override
    public List<SdIcyRoad> selectSdIcyRoadList(SdIcyRoad sdIcyRoad)
    {
		Long deptId = SecurityUtils.getDeptId();
		sdIcyRoad.getParams().put("deptId",deptId);
        return sdIcyRoadMapper.selectSdIcyRoadList(sdIcyRoad);
    }

    /**
     * 新增道路结冰记录
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 结果
     */
    @Override
    public int insertSdIcyRoad(SdIcyRoad sdIcyRoad)
    {
    	redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
    	SdIcyRoad sdIcyRoadData = (SdIcyRoad) redisCache.getCacheObject(sdIcyRoad.getDeviceId());
        return sdIcyRoadMapper.insertSdIcyRoad(sdIcyRoadData);
    }

    /**
     * 修改道路结冰记录
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 结果
     */
    @Override
    public int updateSdIcyRoad(SdIcyRoad sdIcyRoad)
    {
        return sdIcyRoadMapper.updateSdIcyRoad(sdIcyRoad);
    }

    /**
     * 批量删除道路结冰记录
     *
     * @param ids 需要删除的道路结冰记录ID
     * @return 结果
     */
    @Override
    public int deleteSdIcyRoadByIds(Long[] ids)
    {
        return sdIcyRoadMapper.deleteSdIcyRoadByIds(ids);
    }

    /**
     * 删除道路结冰记录信息
     *
     * @param id 道路结冰记录ID
     * @return 结果
     */
    @Override
    public int deleteSdIcyRoadById(Long id)
    {
        return sdIcyRoadMapper.deleteSdIcyRoadById(id);
    }

    /**
     * 道路结冰协议解析
     *
     * @param content 返回内容
     */
	@Override
	public void ParsingProtocol(String content,String deviceId) {
		SdIcyRoad sdIcyRoad = new SdIcyRoad();
		redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
		if(content == null || "".equals(content)){
			redisCache.setCacheObject(deviceId,new SdIcyRoad());
		}else{
			try{
				String[] data = {};
				data = content.split(" ");
				// 设备状态
				String status = data[10];
				// 通道100路表面温度
				if(content.contains("64 00")){
					data = content.split("64 00 ")[1].split(" ");
					String td100 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setSurfaceTemperature(td100 + "℃");
				}else{
					sdIcyRoad.setSurfaceTemperature("无");
				}

				// 通道110路表面理论结冰温度
				if(content.contains("6E 00")){
					data = content.split("6E 00 ")[1].split(" ");
					String td110 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setFreezeTemperature(td110 + "℃");
				}else{
					sdIcyRoad.setFreezeTemperature("无");
				}

				// 通道600路表面积水厚度
				if(content.contains("58 02")){
					data = content.split("58 02 ")[1].split(" ");
					String td600 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setRoadWater(td600 + "um");
				}else{
					sdIcyRoad.setRoadWater("无");
				}

				// 通道800路表面冰厚
				if(content.contains("20 03")){
					data = content.split("20 03 ")[1].split(" ");
					String td800 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setIceThickness(td800);
				}else{
					sdIcyRoad.setIceThickness("无");
				}

				// 通道810路表面盐度值
				if(content.contains("2A 03")){
					data = content.split("2A 03 ")[1].split(" ");
					String td810 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setSalinityValue(td810 + "%");
				}else{
					sdIcyRoad.setSalinityValue("无");
				}

				// 通道820湿滑系数
				if(content.contains("34 03")){
					data = content.split("34 03 ")[1].split(" ");
					String td820 = HexConvertToString(data[4] + data[3] + data[2] + data[1]);
					sdIcyRoad.setWetslidingCoefficient(td820 );
				}else{
					sdIcyRoad.setWetslidingCoefficient("无");
				}

				// 通道900路面状况
				if(content.contains("84 03")){
					data = content.split("84 03 ")[1].split(" ");
					String td900 = data[1];
					switch(td900){
						case "00":
							sdIcyRoad.setRoadCondition("干燥");
							break;
						case "01":
							sdIcyRoad.setRoadCondition("潮湿");
							break;
						case "02":
							sdIcyRoad.setRoadCondition("有水");
							break;
						case "04":
							sdIcyRoad.setRoadCondition("有雪/冰");
							break;
						case "06":
							sdIcyRoad.setRoadCondition("危险");
							break;
					}
				}else{
					sdIcyRoad.setRoadCondition("无");
				}

				sdIcyRoad.setDeviceId(deviceId);
				if("00".equals(status)){
					sdIcyRoad.setStatus("正常");
				}else{
					sdIcyRoad.setStatus("异常");
				}

				sdIcyRoad.setCreateTime(new Date());
				redisCache.setCacheObject(deviceId,sdIcyRoad);

			}catch(Exception e){
				System.out.println(e);
				redisCache.setCacheObject(deviceId,new SdIcyRoad());
			}
		}
	}

	/**
	 * 16进制字符串转字符串
	 * @param hex
	 * @return
	 */
	public String HexConvertToString(String hex) {
        byte[] bytes = HexString2Bytes(hex);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        float value;
		try {
			value = dataInputStream.readFloat();
		} catch (IOException e) {
			e.printStackTrace();
			value = 0;
		}

        return String.valueOf(value);
    }

	public byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 每两个分成一组
	 * @param space
	 * @return
	 */
	private String convert(String space) {
	    String str = String.valueOf(space);
	    StringBuilder builder = new StringBuilder(str);
	    str = builder.reverse().toString();
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < str.length(); i++) {
	        if (i%2==0) {
	            //防越界&保留最高位
	            if (i+2>str.length()){
	                stringBuilder.append(str.substring(i));
	                break;
	            }
	            stringBuilder.append(str.substring(i, i + 2) + " ");
	        }
	    }
	    str = stringBuilder.reverse().toString();
	    //消除字符串长度为2的倍数时多出的' '
	    if (str.charAt(0)==' '){
	        str = str.substring(1);
	    }
	    return str;
	}

	/**
	 * 道路结冰失败
	 *
	 */
	@Override
	public void ErrorParsingProtocol(SdIcyRoad sdIcyRoad, String deviceId) {
		redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
		redisCache.setCacheObject(deviceId,sdIcyRoad);
	}

	/**
	 * 查询某隧道最新的数据记录
	 *
	 * @param tunnelId
	 */
	@Override
	public List<SdIcyRoad> selectLatestIcyRoadList(String tunnelId) {
		return sdIcyRoadMapper.selectLatestIcyRoadList(tunnelId);
	}
}
