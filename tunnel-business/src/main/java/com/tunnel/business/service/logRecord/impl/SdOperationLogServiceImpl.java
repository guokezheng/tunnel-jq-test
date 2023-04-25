package com.tunnel.business.service.logRecord.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作日志Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
@Service
public class SdOperationLogServiceImpl implements ISdOperationLogService {
    @Autowired
    private SdOperationLogMapper sdOperationLogMapper;

    /**
     * 查询操作日志
     *
     * @param id 操作日志ID
     * @return 操作日志
     */
    @Override
    public SdOperationLog selectSdOperationLogById(Long id) {
        return sdOperationLogMapper.selectSdOperationLogById(id);
    }

    @Override
    public int selectSdOperationLogCountList(SdOperationLog sdOperationLog) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }

        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            List<String> tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
            sdOperationLog.getParams().put("tunnelArray", tunnelArray);
        }
        return sdOperationLogMapper.selectSdOperationLogCountList(sdOperationLog);
    }

    @Override
    public int selectAppOperationLogCountList(String eqId,String time, String deptId) {
        List<String> tunnelArray = null;
        String start = null;
        String end = null;
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }
        if(time!=null&&!"".equals(time)){
            start = String.valueOf(getTimeStamp(time).get(0));
            end = String.valueOf(getTimeStamp(time).get(1));
        }
        return sdOperationLogMapper.selectAppOperationLogCountList(eqId,start,end,tunnelArray);
    }

    /**
     * 查询操作日志列表
     *
     * @param sdOperationLog 操作日志
     * @return 操作日志
     */
    @Override
    public List<SdOperationLog> selectSdOperationLogList(SdOperationLog sdOperationLog) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }

        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            List<String> tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
            sdOperationLog.getParams().put("tunnelArray", tunnelArray);
        }

        return sdOperationLogMapper.selectSdOperationLogList(sdOperationLog);
    }

    /**
     * 新增操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int insertSdOperationLog(SdOperationLog sdOperationLog) {
        //sdOperationLog.setCreateTime(DateUtils.getNowDate());
        return sdOperationLogMapper.insertSdOperationLog(sdOperationLog);
    }

    @Override
    public int insertBatchSdOperationLog(List<SdOperationLog> list) {
        return sdOperationLogMapper.insertBatchSdOperationLog(list);
    }

    /**
     * 修改操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int updateSdOperationLog(SdOperationLog sdOperationLog) {
        sdOperationLog.setUpdateTime(DateUtils.getNowDate());
        return sdOperationLogMapper.updateSdOperationLog(sdOperationLog);
    }

    /**
     * 批量删除操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationLogByIds(Long[] ids) {
        return sdOperationLogMapper.deleteSdOperationLogByIds(ids);
    }

    /**
     * 删除操作日志信息
     *
     * @param id 操作日志ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationLogById(Long id) {
        return sdOperationLogMapper.deleteSdOperationLogById(id);
    }

    @Override
    public int deleteOprationLogOver3month() {
        List<SdOperationLog> sdOperationLogs = sdOperationLogMapper.selectOprationLogOver3month();
        if (sdOperationLogs.size() > 0) {
            for (int i = 0; i < sdOperationLogs.size(); i++) {
                SdOperationLog sdOperationLog = sdOperationLogs.get(i);
                sdOperationLogMapper.deleteSdOperationLogById(sdOperationLog.getId());
            }
            return 1;
        }
        return 0;
    }

    /**
     * 新增时 设备执行记录接口 websocket推送
     *
     * @param sdOperationLog
     * @return
     */
    @Override
    public AjaxResult operationLog(SdOperationLog sdOperationLog) {
        sdOperationLog.setCreateTime(DateUtils.getNowDate());
        sdOperationLog.setCreateBy(SecurityUtils.getUsername());
        int i = sdOperationLogMapper.operationLog(sdOperationLog);
        if (i > -1) {
            WebSocketService.broadcast("operationLog", sdOperationLog);
        }
        return AjaxResult.success();
    }

    @Override
    public List<Map> getDispatchExecuted(String eventId) {
        return sdOperationLogMapper.getDispatchExecuted(eventId);
    }

    /**
     * app端获取操作日志
     * @param time
     * @return
     */
    @Override
    public List<SdOperationLog> selectAppOperationLogList(String eqId,String time,String deptId,Integer pageSize,Integer pageNum) {
        List<String> tunnelArray = null;
        String start = null;
        String end = null;
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }
        if(time!=null&&!"".equals(time)){
            start = String.valueOf(getTimeStamp(time).get(0));
            end = String.valueOf(getTimeStamp(time).get(1));
        }
        SdOperationLog sdOperationLog = new SdOperationLog();
        pageNum = (pageNum-1)*pageSize;
        sdOperationLog.getParams().put("pageSize",pageSize);
        sdOperationLog.getParams().put("pageNum", pageNum);
        sdOperationLog.getParams().put("tunnelArray", tunnelArray);
        sdOperationLog.getParams().put("start", start);
        sdOperationLog.getParams().put("end", end);
        sdOperationLog.getParams().put("eqId", eqId);
        /*SdOperationLog sdOperationLog = new SdOperationLog();
        return sdOperationLogMapper.selectSdOperationLogList(sdOperationLog);*/
        return sdOperationLogMapper.selectAppOperationLogList(sdOperationLog);
    }

    private List getTimeStamp(String time) {
        List<String>timeStamp = new ArrayList<>();
        String start = "";
        String end = "";
        if("0".equals(time)){//最近一小时
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = df.format(calendar.getTime());
            end = df.format(new Date());
        }else if("1".equals(time)){//最近一天
            Date today = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String yesterday = simpleDateFormat.format(today);//获取昨天日期
            start = simpleDateFormat.format(today);
            end = simpleDateFormat .format(new Date());
        }else if("2".equals(time)){//最近一周
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            end = format.format(date);//结束时间
            c.setTime(new Date());
            c.add(Calendar.DATE, - 7);
            Date d = c.getTime();
            start = format.format(d);
        }else if("3".equals(time)){//最近一月
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            end = format.format(date);//结束时间
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            Date m = c.getTime();
            start = format.format(m);
        }
        timeStamp.add(start);
        timeStamp.add(end);
        return timeStamp;
    }
}
