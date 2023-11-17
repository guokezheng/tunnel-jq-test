package com.tunnel.business.service.dataInfo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.strategy.factory.DeviceDataStrategyFactory;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 设备Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@Service
public class SdDevicesServiceImpl implements ISdDevicesService {
    private static final Logger log = LoggerFactory.getLogger(SdDevicesServiceImpl.class);
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    @Autowired
    private ISdFixedCodeService sdFixedCodeService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private ISdEquipmentTypeService equipmentTypeService;
    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdEquipmentIconFileMapper sdEquipmentIconFileMapper;

    @Autowired
    private SdOperationLogMapper sdOperationLogMapper;

    @Autowired
    private DeviceDataStrategyFactory deviceDataStrategyFactory;

    @Autowired
    private SdTunnelsMapper tunnelsMapper;


    /**
     * 查询设备
     *
     * @param eqId 设备ID
     * @return 设备
     */
    @Override
    public SdDevices selectSdDevicesById(String eqId) {
        return sdDevicesMapper.selectSdDevicesById(eqId);
    }

    @Override
    public SdDevices selectBoardSdDevicesById(String eqId) {
        return sdDevicesMapper.selectBoardSdDevicesById(eqId);
    }

    /**
     * 查询设备详情
     *
     * @param eqId 设备ID
     * @return 设备
     */
    @Override
    public Map<String, String> queryDeviceById(String eqId) {
        Map<String, String> devices = sdDevicesMapper.queryDeviceById(eqId);
        if (devices.get("eqStatus") == null || "".equals(devices.get("eqStatus")) || "0".equals(devices.get("eqStatus"))) {
            devices.put("eqStatus","2");
        }
        String eqType = String.valueOf(devices.get("eqType"));
        SdDeviceData sdDeviceData = new SdDeviceData();
        if (String.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode()).equals(eqType)) {
            sdDeviceData.setDeviceId(devices.get("fEqId"));
        } else {
            sdDeviceData.setDeviceId(eqId);
        }
        List<SdDeviceData> deviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (!deviceDataList.isEmpty()) {
            //匹配策略
            DeviceDataStrategyService strategyService = deviceDataStrategyFactory.strategy(eqType);

            for (int i = 0;i < deviceDataList.size();i++) {
                SdDeviceData data = deviceDataList.get(i);
                strategyService.getDeviceData(devices,data);
            }
        }
        return devices;
    }

    /**
     * 查询设备列表
     *
     * @param sdDevices 设备
     * @return 设备
     */
    @Override
    public List<SdDevices> selectSdDevicesList(SdDevices sdDevices) {
        List<SdDevices> devicesList = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
        return devicesList;
    }

    /**
     * 根据隧道ID、设备类型获取设备列表
     *
     * @param tunnelId 隧道ID
     * @param typeList 设备类型列表
     * @return
     */
    @Override
    public List<SdDevices> selectDeviceList(String tunnelId, List<String> typeList) {

        return sdDevicesMapper.selectDeviceList(tunnelId,typeList);
    }

    /**
     * 查询设备列表-导出
     *
     * @param sdDevices 设备
     * @return 设备
     */
    @Override
    public List<SdDevices> selectSdDevicesList_exp(SdDevices sdDevices) {
        List<SdDevices> devicesList = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
        return devicesList;
    }

    /**
     * 故障管理页面----根据设备名称获取设备详情
     * @param eqId
     * @return
     */
    @Override
    public List<SdDevices> getEquipmentInfo(String eqId) {
        return sdDevicesMapper.getEquipmentInfo(eqId);
    }

    /**
     * 查询设备列表--新增巡查点
     * @param tunnelId
     * @param deviceType
     * @return
     */
    @Override
    public List<SdDevices> getDevicesList(String searchValue,String tunnelId, String deviceType,String eqType) {
        return sdDevicesMapper.getDevicesList(searchValue,tunnelId,deviceType,eqType);
    }

    /**
     * 新增设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    @Override
    public int insertSdDevices(SdDevices sdDevices) {
        sdDevices.setCreateTime(DateUtils.getNowDate());
        return sdDevicesMapper.insertSdDevices(sdDevices);
    }

    /**
     * 修改设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    @Override
    public int updateSdDevices(SdDevices sdDevices) {
        sdDevices.setUpdateTime(DateUtils.getNowDate());
        return sdDevicesMapper.updateSdDevices(sdDevices);
    }

    /**
     * 批量删除设备
     *
     * @param eqIds 需要删除的设备ID
     * @return 结果
     */
    @Override
    public int deleteSdDevicesByIds(String[] eqIds) {
        return sdDevicesMapper.deleteSdDevicesByIds(eqIds);
    }

    /**
     * 删除设备信息
     *
     * @param eqId 设备ID
     * @return 结果
     */
    @Override
    public int deleteSdDevicesById(String eqId) {
        return sdDevicesMapper.deleteSdDevicesById(eqId);
    }

    /**
     * 查询传感器信息
     */
    @Override
    public List<SdDevices> selectSensorList() {
        return sdDevicesMapper.selectSensorList();
    }

    /**
     * 根据隧道ID查询传感器信息
     */
    @Override
    public List<SdDevices> selectSensorListByTunnelId(String eqTunnelId, String direction) {
        return sdDevicesMapper.selectSensorListByTunnelId(eqTunnelId, direction);
    }

    @Override
    public List<SdDevices> selectRippleListByTunnelId(String eqTunnelId, String direction) {
        return sdDevicesMapper.selectRippleListByTunnelId(eqTunnelId, direction);
    }

    /**
     * 通过隧道id查询设备信息
     */
    @Override
    public List<Map<String, Object>> selectSdDevicesByTunnelId(SdDevices sdDevices) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<SdDevices> list = sdDevicesMapper.selectSdDevicesByTunnelId(sdDevices);//查询所有的设备类型名称
        for (SdDevices devices : list) {
            Map<String, Object> map = new HashMap<>();
            int count = sdDevicesMapper.selectTypeNameCount(devices.getEqType(), devices.getEqTunnelId());//查询每种设备类型对应的设备数量
            map.put("count", count);
            map.put("eqTypeName", devices.getEqName());
            listMap.add(map);
        }

        return listMap;
    }
    /*@Override
    public List<SdDevices> selectSdDevicesEqidList(Long eqHostId) {
        return sdDevicesMapper.selectSdDevicesEqidList(eqHostId);
    }*/

    //根据设备类型查询相应的设备列表
    @Override
    public List<SdDevices> selectEqListByEqTypes(String[] eqTypes) {
        return sdDevicesMapper.selectEqListByEqTypes(eqTypes);
    }

    /**
     * 检索查询，控制指令出错的数据
     */
    @Override
    public List<SdDevices> getChecklist(List<SdDevices> checklist) {
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(new SdEquipmentState());
        List<SdDevices> devicesList = new ArrayList<>();
        for (SdDevices devices : checklist) {
            //查询指令 不为空
//            if (StringUtils.isNotNull(devices.getControlPointAddress()) && StringUtils.isNotEmpty(devices.getControlPointAddress())) {
//                if (StringUtils.isNotNull(devices.getInstructionSeat()) && StringUtils.isNotEmpty(devices.getInstructionSeat())) {
//                    //校验DM模式+IP+机位
//                    if (devices.getInstructionSeat().contains("DM")) {
//
//                        String[] dmeast = devices.getInstructionSeat().split("_");
//                        StringBuffer dmQuery = getCommandCode(devices, dmeast[1], dmeast[0], "0");
//                        //DM控制（模式+机位）不为空
////                        if (StringUtils.isNotNull(devices.getDmcontrolSeat()) && StringUtils.isNotEmpty(devices.getDmcontrolSeat())) {
////                            String[] dmcontrolSeat = devices.getDmcontrolSeat().split("_");
////                            StringBuffer dmControl = getCommandCode(devices, dmcontrolSeat[1], dmcontrolSeat[0], "1");
////                            //DM控制命令或者DM查询命令 校验
////                            if (!getControlCommands(devices, dmControl.toString(), list) || !devices.getControlPointAddress().contains(dmQuery.toString())) {
////                                devicesList.add(devices);
////                            }
////                        } else {
//                            //DM查询 （模式+机位）不为空
//                            if (!devices.getControlPointAddress().contains(dmQuery.toString())) {
//                                devicesList.add(devices);
//                            }
////                        }
//                    } else {
//                        //CIO
//                        String[] cioeast = devices.getInstructionSeat().split("_");
//                        //查询命令
//                        StringBuffer cioQuery = getCommandCode(devices, cioeast[1], cioeast[0], "0");
//                        // StringBuffer cioControl= cioControlAndQuery(plcIp,cioeast[1],"CIO_1");//控制
//                        //|| !getControlCommands(devices).toString().contains(cioControl.toString())
//                        if (!devices.getControlPointAddress().contains(cioQuery.toString())) {
//                            devicesList.add(devices);
//                        }
//                    }
//                } else {
                    //只校验IP
//                    devicesList= checkIp(devices,devicesList,list);
//                }

//            } else {
                devicesList.add(devices);
//            }
        }
        return devicesList;
    }

    public List<SdDevices> checkIp(SdDevices devices, List<SdDevices> devicesList, List<SdEquipmentState> list) {
        String devicesIp = devices.getControlPointAddress().substring(40, 42);
        SdDevices plc = sdDevicesMapper.selectSdDevicesById(devices.getFEqId());
        String plcIp = plc.getIp().split("\\.")[3];
        if (!intToHex(Integer.parseInt(plcIp)).equals(devicesIp)) {
            //查询指令出错
            devicesList.add(devices);
        } else {
            //控制指令
            SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
            sdDeviceCmd.setCodeDeviceId(devices.getEqId());
            List<SdDeviceCmd> cmdList = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
            if (cmdList.size() > 0) {
                for (SdDeviceCmd sdDeviceCmd1 : cmdList) {
                    String command = sdDeviceCmd1.getCommand().substring(40, 42);
                    if (!intToHex(Integer.parseInt(plcIp)).equals(command)) {
                        //查询指令出错
                        devicesList.add(devices);
                        break;
                    }
                }
            } else {
                if (!isControl(devices, list)) {
                    devicesList.add(devices);
                }
            }
        }
        return devicesList;
    }

    //获取控制指令列表
    public Boolean getControlCommands(SdDevices devices, String dmControl, List<SdEquipmentState> list) {
        //控制指令
        SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
        sdDeviceCmd.setCodeDeviceId(devices.getEqId());
        List<SdDeviceCmd> cmdList = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
        if (cmdList.size() > 0) {
            for (SdDeviceCmd sdDeviceCmd1 : cmdList) {
                String command = sdDeviceCmd1.getCommand();
                if (!command.contains(dmControl)) {
                    return false;
                }
            }
        } else {
            return isControl(devices, list);
        }
        return true;
    }

    //DM 查询
    public StringBuffer getCommandCode(SdDevices devices, String seat, String deviceState, String deviceType) {
        StringBuffer sb = new StringBuffer();
        SdFixedCode sdFixedCode = new SdFixedCode();
        sdFixedCode.setModelCode(deviceState);
        if ("0".equals(deviceType)) {
            sdFixedCode.setQueryControl("0101");
        } else {
            sdFixedCode.setQueryControl("0102");
        }
        List<SdFixedCode> sdFixedCodes = sdFixedCodeService.selectSdFixedCodeList(sdFixedCode);
        if (sdFixedCodes.size() > 0) {
            sb.append(sdFixedCodes.get(0).getFixedCode());
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(devices.getFEqId());
            String ip = plc.getIp().split("\\.")[3];
            sb.append(getIpleftPad(intToHex(Integer.parseInt(ip))) + "00");//目标ip地址
            sb.append(sdFixedCodes.get(0).getLocalIp());//本地IP
            sb.append("00" + sdFixedCodes.get(0).getQueryControl());//查询/控制
            sb.append(sdFixedCodes.get(0).getModelNum());//查询
            sb.append(getIpleftPad(intToHex(Integer.parseInt(seat))) + "00");//机位
            if (sdFixedCodes.get(0).getControlCode() != null) {
                sb.append(sdFixedCodes.get(0).getControlCode());//DM控制固定码
            }
        }
        return sb;
    }

    public String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }

    public String getIpleftPad(String ip) {
        return StringUtils.leftPad(ip, 4, "0");
    }

    //校验控制操作
    public Boolean isControl(SdDevices devices, List<SdEquipmentState> list) {

        for (SdEquipmentState sdEquipmentState : list) {
            if (sdEquipmentState.getStateTypeId().equals(devices.getEqType())) {
                return false;//有权限
            }
        }
        return true;
    }

    @Override
    public String importSdDevices(List<SdDevices> sdDevicesList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(sdDevicesList) || sdDevicesList.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SdDevices devices : sdDevicesList) {
            try {
                /*SdDevices d = sdDevicesMapper.selectSdDevicesById(devices.getEqId());*/
                SdDevices d = sdDevicesMapper.selectSdDevicesById(devices.getEqId());
                if (StringUtils.isNull(d)) {
                    Map map = checkDevices(devices,isUpdateSupport);
                    if ((Boolean) map.get("flag")) {
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getControlPointAddress()));//点位地址
//                        devices.setControlPointAddress(sb.toString());
                        devices.setCreateBy(operName);
                        this.insertSdDevices(devices);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else if (isUpdateSupport) {
                    Map map = checkDevices(devices,isUpdateSupport);
                    if ((Boolean) map.get("flag")) {
                        devices.setUpdateBy(operName);
                        //todo 目前没有点位信息，先注释掉生成指令相关代码
//                        StringBuilder sb=new StringBuilder();
//                        sb.append(getCommandCode(devices,devices.getInstructionSeat().split("_")[1],devices.getInstructionSeat().split("_")[0],"0"));
//                        sb.append(getIpleftPad(devices.getControlPointAddress()));//点位地址
//                        devices.setControlPointAddress(sb.toString());
                        this.updateSdDevices(devices);
//                        insertOrUpdateOrDeleteSdDeviceCmd(devices);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devices.getEqId() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备ID " + devices.getEqId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            if (successNum > 0) {
                failureMsg.insert(0, successNum + " 条数据导入成功" + "，" + failureNum + " 条数据格式不正确，导入失败！错误如下：");
            } else {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            }
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public Map checkDevices(SdDevices devices, Boolean isUpdateSupport) {
        StringBuilder failureMsg = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        Long eqType = devices.getEqType();
        SdEquipmentType sdeq = devices.getTypeName();
        String fEqId = devices.getFEqId();
        //所属隧道
        SdTunnels sdTunnels = sdTunnelsService.selectSdTunnelsById(devices.getEqTunnelId());
        /*//根据隧道id和设备名称查询
        List<SdDevices> sdDevicesList = sdDevicesMapper.tunnelEqNameOnly(devices.getEqTunnelId(), devices.getEqName());*/
        //设备类型
        SdEquipmentType sdEquipmentType = equipmentTypeService.selectSdEquipmentTypeById(eqType);
//        SdEquipmentState sdEquipmentState = sdEquipmentStateService.selectSdEquipmentStateById(devices.getEqType());
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels>tunnelList = sdTunnelsService.selectTunnels(deptId);
        String tunnels = "";
        if(tunnelList.size()>0){
            for(SdTunnels sdTunnel : tunnelList){
                tunnels += sdTunnel.getTunnelId()+',';
            }

        }
        /*判断id不能包含汉字*/
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(devices.getEqId());
        if (m.find()) {
            failureMsg.append("、设备ID " + devices.getEqId() + " ID不能包含汉字,请检查");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        }

        if(tunnels.indexOf(devices.getEqTunnelId())==-1){//不包含改隧道
            failureMsg.append("、设备ID " + devices.getEqId() + " 所属隧道ID有误,请检查");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        }

        if (!StringUtil.isEmpty(fEqId)) {
            //有些设备不需要关联PLC，只有当录入PLC主机ID时，才校验PLC
            SdDevices plc = sdDevicesMapper.selectSdDevicesById(fEqId);
            if (StringUtils.isNull(plc)) {
                failureMsg.append("、设备ID " + devices.getEqId() + " PLC主机ID不正确");
                map.put("flag", false);
                map.put("failureMsg", failureMsg);
                return map;
            }
        } else if (StringUtils.isNull(sdTunnels)) {
            failureMsg.append("、设备ID " + devices.getEqId() + " 所属隧道ID不正确");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        } else if (StringUtils.isNull(sdEquipmentType)) {
            failureMsg.append("、设备ID " + devices.getEqId() + " 设备类型ID不正确");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        } else if (StringUtils.isEmpty(devices.getEqName())||StringUtils.isLongNotNull(devices.getEqType()) ||StringUtils.isLongNotNull(devices.getFEqType())) {
            failureMsg.append("、设备ID " + devices.getEqId() + "设备名称或设备类型或设备大类不能为空 ");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        } else if (StringUtils.isEmpty(devices.getDirection())) {
            failureMsg.append("、设备ID " + devices.getEqId() + "设备方向不能为空 ");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        }
        /*else if (sdDevicesList!=null&&sdDevicesList.size()>0&&!sdDevicesList.get(0).getEqId().equals(devices.getEqId())) {
            failureMsg.append("、设备ID " + devices.getEqId() + "该隧道下设备名称重复");
            map.put("flag", false);
            map.put("failureMsg", failureMsg);
            return map;
        }*/
        //设备方向
        //devices.getEqDirection();
        //控制模式+机位
        //devices.getDmcontrolSeat();
        //查询模式+机位 指令校验
        /*if (devices.getInstructionSeat()!=null){
            StringBuffer sbquery;
            if (devices.getInstructionSeat().contains("DM")){
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("DM")[1],"DM","0");//查询
            }else {
                sbquery= getCommandCode(sdHosts.getPlcIp().split("\\.")[3],devices.getInstructionSeat().split("CIO")[1],"CIO","0");//查询
            }
            if (!devices.getControlPointAddress().contains(sbquery.toString()) ){
                failureMsg.append("、设备ID " + devices.getEqId() + " 指令不正确");
                map.put("flag",false);
                map.put("failureMsg",failureMsg);
                return map;
            }
        }*/
        map.put("flag", true);
        return map;
    }

    /**
     * @param devices
     * @return
     */
    @Override
    public void insertOrUpdateOrDeleteSdDeviceCmd(SdDevices devices) {
        SdEquipmentState sdEquipmentState = new SdEquipmentState();
        sdEquipmentState.setStateTypeId(devices.getEqType());
        sdEquipmentState.setIsControl(1);
        List<SdEquipmentState> sdEquipmentStates = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        if (sdEquipmentStates.size() > 0) {
            for (SdEquipmentState sdEquipmentState1 : sdEquipmentStates) {
                //根据类型查询类型状态表
                SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
                sdDeviceCmd.setCodeDeviceId(devices.getEqId());
                sdDeviceCmd.setCodePlcId(devices.getFEqId());
                /*if (StringUtils.isNotNull(devices.getDmcontrolSeat()) && StringUtils.isNotEmpty(devices.getDmcontrolSeat())) {
                    sdDeviceCmd.setDeviceTypeId(devices.getEqType());
                    sdDeviceCmd.setCodeDeviceState(sdEquipmentState1.getDeviceState());
                    StringBuilder sb = new StringBuilder();
                    sb.append(getCommandCode(devices, devices.getDmcontrolSeat().split("_")[1], devices.getDmcontrolSeat().split("_")[0], "1"));
                    sb.append(getIpleftPad(sdEquipmentState1.getDeviceState()));//点位地址
                    List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                    if (sdDeviceCmds.size() > 0) {
                        sdDeviceCmds.get(0).setCommand(sb.toString());
                        sdDeviceCmdService.updateSdDeviceCmd(sdDeviceCmds.get(0));
                    } else {
                        sdDeviceCmd.setCommand(sb.toString());
                        sdDeviceCmdService.insertSdDeviceCmd(sdDeviceCmd);
                    }
                } else {
                        List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
                        for (SdDeviceCmd sdDeviceCmd1:sdDeviceCmds){
                            sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
                        }
                    }*/
            }
        } else {
            //没有设备状态，删除该设备关联的设备报文指令
            SdDeviceCmd sdDeviceCmd = new SdDeviceCmd();
            sdDeviceCmd.setCodeDeviceId(devices.getEqId());
            sdDeviceCmd.setCodePlcId(devices.getFEqId());
            List<SdDeviceCmd> sdDeviceCmds = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
            for (SdDeviceCmd sdDeviceCmd1 : sdDeviceCmds) {
                sdDeviceCmdService.deleteSdDeviceCmdById(sdDeviceCmd1.getCodeId());
            }
        }

    }

    @Override
    public List<SdDevices> getAllPressureGaugesMsg() {
        String deptId = SecurityUtils.getDeptId();
        return sdDevicesMapper.getAllPressureGaugesMsg(deptId);
    }


    @Override
    public List<Map<String, Object>> getDevicesStatus(String tunnelId) {
        List<Map<String, Object>> devicesStatus = sdDevicesMapper.getDevicesStatus(tunnelId);
//        List<Map<String, Object>> devicesType = sdDevicesMapper.getDevicesType(tunnelId);
//        devicesType.add(devicesStatus.get(0));
        return devicesStatus;
    }

    @Override
    public List<Map<String, Object>> obtainEquipmentEnergyConsumption(String tunnelId) {
        List<Map<String, Object>> devicesType = sdDevicesMapper.getDevicesType(tunnelId);
        for (int i = 0; i < devicesType.size(); i++) {
            Map<String, Object> map = devicesType.get(i);
            map.put("electricityConsumption", String.valueOf(new Random().nextInt(2000)));
        }
        return devicesType;
    }

    @Override
    public String autoId(String tunnelId, Integer typeId) {
        //所有隧道类型
        List<SdEquipmentType> equipmentTypes = sdDevicesMapper.selectList();
        //当前设备类型的设备id 如果为null则该设备没添加
        Map<Long, String> collect = new HashMap<>();
        equipmentTypes.forEach(f -> {
            collect.put(f.getTypeId(), f.getTypeAbbr());
        });
        //设备类型名称简称
        String typeAbbr = collect.get(typeId.longValue());
        String ss = tunnelId + "-" + typeAbbr + "-";
        List<String> list = sdDevicesMapper.selectID(ss);
        log.info("当前设备类型名称简称-->>>>>>>>>>>>>>>>>>{}", typeAbbr);
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(list)) {
            log.info("进来了--->当前设备类型数据库已有");
            List<Integer> integerList = list.stream().map(
                    f -> {
                        //获取到最后标志位
                        int i = f.lastIndexOf("-");
                        int k = f.lastIndexOf("+");
                        int length = 0;
                        if(k > 0){
                            length = k;
                        }else {
                            length = f.length();
                        }
                        String s = f.substring(i + 1, length).trim();
                        return Integer.parseInt(s);
                    }
            ).sorted().collect(Collectors.toList());
            Integer integer = integerList.get(integerList.size() - 1);
            String s = String.format("%0" + 3 + "d", integer + 1);
            sb.append(tunnelId).append("-").append(typeAbbr).append("-").append(s);
        } else {
            //当前设备类型在数据库新建
            log.info("----当前设备类型数据库新建----");
            sb.append(tunnelId).append("-").append(typeAbbr).append("-").append("001");
        }
        return sb.toString();
    }

    /**
     * 一键车道控制
     *
     * @param map
     * @return
     */
    @Override
    public List<SdDevices> batchControlCarFinger(Map<String, Object> map) {
        List<SdDevices> sdDevicesList = new ArrayList<>();
        String tunnelName = (String) map.get("tunnelId");
        String direction = map.get("direction").toString();
        List<Object> list = (List<Object>) map.get("lane");
        if (map.get("lane") == null || list.size() == 0) {
            throw new RuntimeException("车指批量控制隧道车道信息为空");
        }
        for (Object lane : list) {
            if (lane == null || lane.toString().equals("")) {
                throw new RuntimeException("车指批量控制隧道车道信息为空");
            }
            SdDevices devices = new SdDevices();
            devices.setEqTunnelId(tunnelName);
            Long [] eqTypes = new Long[]{1L,2L};
            //devices.setEqType(1L);
            devices.setEqTypes(eqTypes);
            devices.setEqDirection(direction);
            devices.setLane(lane.toString());
            List<SdDevices> sdDevicesLists = sdDevicesMapper.batchControlCarFinger(devices);
            sdDevicesList.addAll(sdDevicesLists);
        }
        return sdDevicesList;
    }

    /**
     * 查询可控设备列表
     *
     * @param sdDevices
     * @return
     */
    @Override
    public List<SdDevices> selectIsControlSdDevicesList(SdDevices sdDevices) {
        List<SdDevices> devicesList = sdDevicesMapper.selectIsControlSdDevicesList(sdDevices);
        return devicesList;
    }

    @Override
    public List<Map<String, Object>> getDevicesByTypeAndTunnel(SdDevices sdDevices) {
        List<Map<String, Object>> devicesByTypeAndTunnel = sdDevicesMapper.getDevicesByTypeAndTunnel(sdDevices);
        return devicesByTypeAndTunnel;
    }

    @Override
    public int updateSdDevicesByFEqId(SdDevices sdDevices) {
        sdDevices.setEqStatusTime(new Date());
        return sdDevicesMapper.updateSdDevicesByFEqId(sdDevices);
    }

    @Override
    public List<Map<String, Object>> getDeviceAndState(String tunnelId) {
        List<Map<String, Object>> deviceData = sdDevicesMapper.selectDeviceDataAndState(tunnelId);
        if(deviceData.size()<1){
            return null;
        }
        Function<Map<String,Object>, String> direction = new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String, Object> t) {
                Object object = t.get("direction");
                String string = object.toString();
                return string;
            }
        };
        Map<String, List<Map<String, Object>>> collect = deviceData.stream().collect(Collectors.groupingBy(direction));
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> m : collect.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", m.getKey());
            map.put("list", m.getValue());
            result.add(map);
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(0).get("list");
        // 根据lane去重
        list = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing((o)-> o.get("lane") + ";" + o.get("lane")))), ArrayList::new));
        result.get(0).put("list",list);
        System.out.println(result);
        return result;
    }
    public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<String> fireMarkList(String eqId) {
        SdDevices devices = sdDevicesMapper.selectSdDevicesById(eqId);
        if (devices == null) {
            throw new RuntimeException("当前设备信息为空，请到设备管理中进行核对");
        }
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(devices.getEqTunnelId());
        sdDevices.setEqType(devices.getEqType());
        sdDevices.setEqDirection(devices.getEqDirection());
        List<String> devicesFireMarkList = sdDevicesMapper.getDevicesFireMarkList(sdDevices);
        if (devicesFireMarkList.isEmpty()) {
            devicesFireMarkList = new ArrayList<>();
        }
        devicesFireMarkList.add("0");
        devicesFireMarkList.add("255");
        return devicesFireMarkList;
    }

    @Override
    public List<SdDevicesBrand> getDevBrandList() {
        return sdDevicesMapper.getDevBrandList();
    }

    @Override
    public List<SdDevices> getAppDevicesList(String param, String eqType, String tunnelId, String eqStatus, Integer pageSize, Integer pageNum) {
        List<String> tunnelArray = null;
        String start = null;
        String end = null;
        String deptId = SecurityUtils.getDeptId();
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }
        SdDevices sdDevices = new SdDevices();
        if(pageNum!=null&&pageSize!=null){
            pageNum = (pageNum-1)*pageSize ;
        }
        sdDevices.getParams().put("tunnelId", tunnelId);
        sdDevices.getParams().put("tunnelArray", tunnelArray);
        sdDevices.getParams().put("param", param);
        sdDevices.getParams().put("eqType", eqType);
        sdDevices.getParams().put("eqStatus", eqStatus);
        sdDevices.getParams().put("pageSize", pageSize);
        sdDevices.getParams().put("pageNum", pageNum);
        return sdDevicesMapper.getAppDevicesList(sdDevices);
    }



    /**
     * app端获取设备列表
     * @return
     */
    /*@Override
    public Map getAppDevicesList(String param, String eqType, String eqStatus) {
        Map<String, Object> map=new HashMap<String, Object>();
        List<SdDevices> sdDevicesList = sdDevicesMapper.getAppDevicesList(param,eqType,eqStatus);
        if(sdDevicesList!=null && sdDevicesList.size()>=0){
            for(int i =0;i<sdDevicesList.size();i++){
                String fileId = sdDevicesList.get(i).getIconFileId();
                if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                    SdEquipmentStateIconFile sdEquipmentStateIconFilee = new SdEquipmentStateIconFile();
                    sdEquipmentStateIconFilee.setStateIconId(fileId);
                    sdDevicesList.get(i).setiFileList(sdEquipmentIconFileMapper.selectSdTypeImgFileList(sdEquipmentStateIconFilee));
                }
            }
        }
        List<SdDevices> num = sdDevicesMapper.getDevicesNum(param,eqType,eqStatus);
        map.put("sdDevicesList",sdDevicesList);
        map.put("numList",num);
        return map;
    }*/

    /**
     * app端设备信息
     * @param eqId
     * @return
     */
    @Override
    public List<SdDevices> getAppDevicesInfo(String eqId) {
        return sdDevicesMapper.getAppDevicesInfo(eqId);
    }

    @Override
    public List<SdDevices> selectSdDevicesListByEqTypes(Long guidanceLampTypeId, Long lunKuoBiaoTypeId) {
        List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesListByEqTypes(guidanceLampTypeId, lunKuoBiaoTypeId);
        return devicesList;
    }

    /**
     * app端查询设备状态
     * @param eqId
     * @return
     */
    @Override
    public List<SdDevices> getAppDevicesStatus(String eqId) {
        List<SdDevices>list = sdDevicesMapper.getAppDevicesStatus(eqId);
        if(list!=null&&list.size()>0){
            String iconFileId = list.get(0).getIconFileId();
            if (iconFileId != null && !"".equals(iconFileId) && !"null".equals(iconFileId)) {
                if (!"-1".equals(iconFileId)) {
                    SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                    sdEquipmentStateIconFile.setStateIconId(iconFileId);
                    list.get(0).setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
                }
            }
        }
         return list;
    }

    /**
     * 根据隧道+方向+类型 获取广播设备
     * @param sdDevices
     * @return
     */
    @Override
    public List<SdDevices> getSpkList(SdDevices sdDevices){
        return sdDevicesMapper.getSpkList(sdDevices);
    }

    /**
     * 根据隧道+方向+类型+段号(通过external_device_id字段关联) 获取广播设备
     * @param sdDevices
     * @return
     */
    @Override
    public SdDevices getLight(SdDevices sdDevices){
        return sdDevicesMapper.getLight(sdDevices);
    }

    @Override
    public SdDevices getDeviceByAssociationDeviceId(Long deviceId) {
        return sdDevicesMapper.getDeviceByAssociationDeviceId(deviceId);
    }

    @Override
    public List<SdDevices> selectDevicesLineList(String deptId,String eqtype) {
        return sdDevicesMapper.selectDevicesLineList(deptId,eqtype);
    }

    @Override
    public AjaxResult getTreeDeviceList(SdDevices sdDevices) {
        SdTunnelsMapper sdTunnelsMapper = SpringUtils.getBean(SdTunnelsMapper.class);
        SdDevices sdDevicesParam = new SdDevices();
        sdDevicesParam.setEqTunnelId(sdDevices.getEqTunnelId());
        sdDevicesParam.setEqType(sdDevices.getEqType());
        //隧道中双向设备
        List<Map<String, Object>> sdDevicesList = sdDevicesMapper.getTreeDevicesData(sdDevicesParam);
        //隧道中设备方向
        List<String> tunnelDirection = sdDevicesMapper.getTunnelDirection(sdDevicesParam);
        //隧道信息
        SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(sdDevices.getEqTunnelId());
        //如果没有设备则返回空
        if(sdDevicesList.size() == 0){
            return AjaxResult.success(new ArrayList<>());
        }
        //处理相关数据组合成级联相关数据结构
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> directionList = new ArrayList<>();
        for(String direction : tunnelDirection){
            Map<String, Object> map1 = new HashMap<>();
            List<Map<String, Object>> collect = sdDevicesList.stream().filter(item -> direction.equals(item.get("eqDirection").toString())).collect(Collectors.toList());
            //查询方向字典值
            String directionLabel = SpringUtils.getBean(SysDictDataMapper.class).selectDictLabel(DictTypeEnum.sd_direction.getCode(), direction);
            map1.put("label", directionLabel);
            map1.put("children",collect);
            directionList.add(map1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("label",sdTunnels.getTunnelName());
        map.put("children",directionList);
        list.add(map);
        return AjaxResult.success(list);
    }

    @Override
    public List<SdDevices> verifyEqNameOnly(String eqId,String eqName) {
        return sdDevicesMapper.verifyEqNameOnly(eqId,eqName);
    }


    @Override
    public List<SdDevices> tunnelEqNameOnly(String eqTunnelId,String eqName) {
        return sdDevicesMapper.tunnelEqNameOnly(eqTunnelId,eqName);
    }

    @Override
    public int getAppDevicesCountList(String param, String eqType,String tunnelId,  String eqStatus, String deptId) {
        List<String> tunnelArray = null;
        String start = null;
        String end = null;
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }
        SdDevices sdDevices = new SdDevices();
        sdDevices.getParams().put("tunnelId", tunnelId);
        sdDevices.getParams().put("tunnelArray", tunnelArray);
        sdDevices.getParams().put("param", param);
        sdDevices.getParams().put("eqType", eqType);
        sdDevices.getParams().put("eqStatus", eqStatus);
        return sdDevicesMapper.getAppDevicesCountList(sdDevices);
    }

    @Override
    public List<SdDevices> getDevicesNum(String param, String eqType,String tunnelId, String eqStatus, Integer pageSize, Integer pageNum) {
        List<String> tunnelArray = null;
        String deptId = SecurityUtils.getDeptId();
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }
        SdDevices sdDevices = new SdDevices();
        sdDevices.getParams().put("tunnelId", tunnelId);
        sdDevices.getParams().put("tunnelArray", tunnelArray);
        sdDevices.getParams().put("param", param);
        sdDevices.getParams().put("eqType", eqType);
        sdDevices.getParams().put("eqStatus", eqStatus);
        sdDevices.getParams().put("pageSize", pageSize);
        sdDevices.getParams().put("pageNum", pageNum);
        return sdDevicesMapper.getDevicesNum(sdDevices);
    }


    /**
     * 修改设备以及子设备状态在线
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
    @Override
    public int updateOnlineStatus(String deviceId,boolean cascade){
      return updateDeviceStatus(deviceId, DevicesStatusEnum.DEVICE_ON_LINE.getCode(),cascade);
    }

    /**
     * 修改设备以及子设备状态离线
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
    @Override
    public int updateOfflineStatus(String deviceId,boolean cascade){
      return updateDeviceStatus(deviceId,DevicesStatusEnum.DEVICE_OFF_LINE.getCode(),cascade);
    }

    /**
     * 修改设备以及子设备状态故障
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
    @Override
    public int updateFaultStatus(String deviceId,boolean cascade){
      return updateDeviceStatus(deviceId,DevicesStatusEnum.DEVICE_ERROR.getCode(),cascade);
    }

    /**
     * 设置设备以及子设备状态
     * @param deviceId 设备ID
     * @param status 设备状态
     * @param cascade 是否级联修改子设备状态，true为修改
     */
    @Override
    public int updateDeviceStatus(String deviceId,String status,boolean cascade){
        int result;

        SdDevices sdDevices = new SdDevices();
        //设置设备状态
        sdDevices.setEqId(deviceId);
        sdDevices.setEqStatus(status);
        sdDevices.setEqStatusTime(new Date());
        result = updateSdDevices(sdDevices);

        //级联修改子设备状态
        if(cascade){
            //设置子设备状态
            sdDevices.setFEqId(deviceId);
           result = updateSdDevicesByFEqId(sdDevices);
        }
        return result;
    }

    /**
     * 批量设置设备以及子设备状态
     *
     * @param list    设备ID集合
     * @param status  设备状态
     */
    @Override
    public int updateDeviceStatusBatch(List<String> list, String status) {
       return sdDevicesMapper.updateDeviceStatusBatch(list,status);
    }



    /**
     * 根据外部设备ID查询设备信息
     *
     * @param externalId 外部设备ID
     * @return
     */
    @Override
    public SdDevices getDevicesListByExternalId(String externalId) {
        return sdDevicesMapper.getDevicesListByExternalId(externalId);
    }

    /**
     * 根据协议Id、设备IP等条件查询设备列表
     *
     * @param sdDevices 设备信息
     * @return
     */
    @Override
    public List<SdDevices> selectDevicesByProtocol(SdDevices sdDevices) {
        return sdDevicesMapper.selectDevicesByProtocol(sdDevices);
    }

    /**
     * 根据协议Id、设备IP等条件查询设备列表
     *
     * @param protocolId 协议ID
     * @param noTypeList 排除的设备类型
     * @return
     */
    @Override
    public List<SdDevices> getDevicesByProtocol(Long protocolId, List<Long> noTypeList) {
        return sdDevicesMapper.getDevicesByProtocol(protocolId,noTypeList);
    }

    /**
     * 按照设备实时数据的时间筛选在线设备
     *
     * @param protocolId 协议ID
     * @param noTypeList 排除的设备类型
     * @param offlineTime 离线时间
     * @return
     */
    @Override
    public List<String> selectOnlineDeviceByUpdateTime(Long protocolId, List<Long> noTypeList,int offlineTime) {
        return sdDevicesMapper.selectOnlineDeviceByUpdateTime(protocolId,noTypeList,offlineTime);
    }

    /**
     * 获取测控执行器设备
     * @return
     */
    @Override
    public List<Map> getMcaDevList() {

        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<String> tunnelArray = null;
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }

        List<Map> list = sdDevicesMapper.getMcaDevList(tunnelArray);

        for(Map map : list){

            String devId = map.get("device_id") != null ? map.get("device_id").toString() : null ;
            if(devId != null){
                map.put("function",sdDevicesMapper.getMacItem(devId));
            }
        }
        return list;
    }

    @Override
    public List<Map> getMcaList() {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<String> tunnelArray = null;
        // 超级管理员，可以看到全部数据
        if(!SecurityUtils.getUsername().equals("admin")){
            //获取所属部门下隧道列表
            tunnelArray = sdOperationLogMapper.getTunnelArrayByDeptId(deptId);
        }


        List<Map> list = sdDevicesMapper.getMcaList(tunnelArray);

        return list;
    }

    @Override
    public AjaxResult getCamera(SdDevices devices) {
        if(devices.getRlModel() == null){
            List<SdDevices> devLeft = sdDevicesMapper.getDevLeft(devices);
            if(devLeft.size() == 0){
                List<SdDevices> devRight = sdDevicesMapper.getDevRight(devices);
                if(devRight.size() > 0){
                    return AjaxResult.success(devRight.get(0));
                }
            }
            return AjaxResult.success(devLeft.get(devLeft.size() - 1));
        }
        if("left".equals(devices.getRlModel())){
            List<SdDevices> devLeft = sdDevicesMapper.getDevLeft(devices);
            if(devLeft.size() > 0){
                return AjaxResult.success(devLeft.get(devLeft.size() - 1));
            }
        }else {
            List<SdDevices> devRight = sdDevicesMapper.getDevRight(devices);
            if(devRight.size() > 0){
                return AjaxResult.success(devRight.get(0));
            }
        }
        return AjaxResult.success("","");
    }

    @Override
    public List<Map> getMoreMcaListByMac(String mac) {
        return sdDevicesMapper.getMoreMcaListByMac(mac);
    }

    @Override
    public List<Map> getMoreDevListByMacAndEqType(String mac,String eqType) {
        return sdDevicesMapper.getMoreDevListByMacAndEqType(mac,eqType);
    }

    @Override
    public List<String> getDevicesListByMacAndEqTypeAndLane(String mac, String eqType, String lane) {
        return sdDevicesMapper.getDevicesListByMacAndEqTypeAndLane(mac,eqType,lane);
    }

    @Override
    public int updateFDeviceStatusByEqId(String deviceId, String status) {
        SdDevices sdDevices = new SdDevices();
        //设置设备状态
        sdDevices.setEqId(deviceId);
        sdDevices.setEqStatus(status);
        sdDevices.setEqStatusTime(new Date());
        sdDevices.setUpdateTime(new Date());

        return sdDevicesMapper.updateFDeviceStatusByEqId(sdDevices);
    }


    @Override
    public List<Map> devicesOfSubstationInfo(List<String> eqTunnelList) {
        return sdDevicesMapper.devicesOfSubstationInfo(eqTunnelList);
    }

    @Override
    public void syncData(HashMap<String, Object> requestData) {
        JSONObject jsonObject = JSONObject.parseObject(requestData.get("requestData").toString());
        JSONArray tunnels = jsonObject.getJSONArray("tunnelList");
        JSONArray devices = jsonObject.getJSONArray("deviceList");
        List<String> list = new ArrayList<>();
        for(int i = 0; i < tunnels.size(); i++){
            SdTunnels sdTunnels = JSONObject.parseObject(tunnels.get(i).toString(), SdTunnels.class);
            tunnelsMapper.updateSdTunnels(sdTunnels);
            list.add(sdTunnels.getTunnelId());
        }
        if(list.size() > 0){
            String tunnelIds = StringUtils.join(list,",");
            sdDevicesMapper.deleteDev(tunnelIds);
            for(int i = 0; i < devices.size(); i++){
                try {
                    SdDevices sdDevices = JSONObject.parseObject(devices.get(i).toString(), SdDevices.class);
                    sdDevicesMapper.insertSdDevices(sdDevices);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("同步设备数据报错：" + e.getMessage());
                }
            }
        }
    }
}
