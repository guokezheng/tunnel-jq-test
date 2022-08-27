package com.tunnel.platform.service.dataInfo.impl;

import com.tunnel.platform.domain.dataInfo.SdCallRecord;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.mapper.dataInfo.SdCallRecordMapper;
import com.tunnel.platform.service.dataInfo.ISdCallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 紧急电话记录Service业务层处理
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
@Service
public class SdCallRecordServiceImpl implements ISdCallRecordService {
	@Autowired
	private SdCallRecordMapper sdCallRecordMapper;

	/**
	 * 查询紧急电话记录
	 * 
	 * @param id
	 *            紧急电话记录ID
	 * @return 紧急电话记录
	 */
	@Override
	public SdCallRecord selectSdCallRecordById(Long id) {
		return sdCallRecordMapper.selectSdCallRecordById(id);
	}

	/**
	 * 查询紧急电话记录列表
	 * 
	 * @param sdCallRecord
	 *            紧急电话记录
	 * @return 紧急电话记录
	 */
	@Override
	public List<SdCallRecord> selectSdCallRecordList(SdCallRecord sdCallRecord) {
		// 表格
		return sdCallRecordMapper.selectSdCallRecordList(sdCallRecord);
	}

	/**
	 * 新增紧急电话记录
	 * 
	 * @param sdCallRecord
	 *            紧急电话记录
	 * @return 结果
	 */
	@Override
	public int insertSdCallRecord(SdCallRecord sdCallRecord) {
		sdCallRecord.setCreateTime(DateUtils.getNowDate());
		return sdCallRecordMapper.insertSdCallRecord(sdCallRecord);
	}

	/**
	 * 修改紧急电话记录
	 * 
	 * @param sdCallRecord
	 *            紧急电话记录
	 * @return 结果
	 */
	@Override
	public int updateSdCallRecord(SdCallRecord sdCallRecord) {
		return sdCallRecordMapper.updateSdCallRecord(sdCallRecord);
	}

	/**
	 * 批量删除紧急电话记录
	 * 
	 * @param ids
	 *            需要删除的紧急电话记录ID
	 * @return 结果
	 */
	@Override
	public int deleteSdCallRecordByIds(Long[] ids) {
		return sdCallRecordMapper.deleteSdCallRecordByIds(ids);
	}

	/**
	 * 删除紧急电话记录信息
	 * 
	 * @param id
	 *            紧急电话记录ID
	 * @return 结果
	 */
	@Override
	public int deleteSdCallRecordById(Long id) {
		return sdCallRecordMapper.deleteSdCallRecordById(id);
	}
	/**
	 * 查询紧急电话信息
	 */
	@Override
	public List<Map<String, String>> selectSdCallRecordEcharts(SdCallRecord sdCallRecord) {
		List<Map<String, String>> list = sdCallRecordMapper.selectSdCallRecordEcharts(sdCallRecord);
		List<Map<String, String>> returnList = getEmptyToZeroList(list, sdCallRecord);
		return returnList;
	}

	private List<Map<String, String>> getEmptyToZeroList(List<Map<String, String>> list, SdCallRecord sdCallRecord) {
		return findDaysStr(sdCallRecord.getTimeStart(), sdCallRecord.getTimeEnd(), list);
	}
	/**
	 * 查询时间段的紧急电话信息
	 * @param beginTime
	 * @param endTime
	 * @param result
	 * @return
	 */
	private List<Map<String, String>> findDaysStr(String beginTime, String endTime, List<Map<String, String>> result) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dBegin = sdf.parse(beginTime);
			Date dEnd = sdf.parse(endTime);
			List<String> daysStrList = new ArrayList<String>();
			Calendar calBegin = Calendar.getInstance();
			calBegin.setTime(dBegin);
			Calendar calEnd = Calendar.getInstance();
			while (dBegin.getTime() <= dEnd.getTime()) {
				daysStrList.add(sdf.format(dBegin));
				calEnd.setTime(dBegin);
				// 增加一天 放入集合
				calEnd.add(Calendar.DATE, 1);
				dBegin = calEnd.getTime();
			}
			
			for (int i = 0; i < daysStrList.size(); i++) {
				String time = daysStrList.get(i);
				Boolean addflag = true;
				for (int j = 0; j < result.size(); j++) {
					String xDate = result.get(j).get("recordDate");
					if (time.equals(xDate)) {
						addflag = false;
						resultList.add(result.get(j));
					}
				}
				if (addflag == true) {
					Map<String, String> analysis = new HashMap<String, String>();
					analysis.put("recordCount", "0");
					analysis.put("recordDate", time);
					resultList.add(analysis);
				}
			}
		} catch (Exception e) {

		}
		return resultList;
	}

	/**
	 * 
	 * 批量插入
	 */
	@Override
	public int insertSdCallRecordList(List<SdCallRecord> sdCallRecordList) {
		// 不存在的插入
		int result = sdCallRecordMapper.insertSdCallRecordList(sdCallRecordList);
		return result;
	}
}