package com.tunnel.platform.controller.workspace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.platform.business.vms.core.DevicesManager;
import com.tunnel.platform.business.instruction.EquipmentControlInstruction;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdIcyRoad;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.service.event.ISdEventService;
import com.tunnel.platform.utils.util.CRC8Util;
import com.tunnel.platform.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台
 *
 * @author
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/workspace")
public class workspaceController extends BaseController {
    @Autowired
    private ISdEventService sdEventService;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private static RedisCache redisCache;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    //临时
    String url = "http://172.16.14.71:8082/BESServer/api/v1.0/getLightList";
    String url2 = "http://172.16.14.71:8082/BESServer/notoken/api/debugPointInfo";

    /**
     * 查询设备状态
     */
    @PreAuthorize("@ss.hasPermi('system:event:list')")
    @GetMapping("/getDevState/{tunnelId}")
    public String list(@PathVariable String tunnelId) {
        if (tunnelId == null || tunnelId.equals("")){
            SdTunnels sdTunnels = new SdTunnels();
            tunnelId = sdTunnelsService.selectSdTunnelsList(sdTunnels).get(0).getTunnelId();
        }
        //String sr= HttpRequest.sendGet(url, "");
        //Integer i= new Random().nextInt(3);;
        //String sr1= HttpRequest.sendPost(url2, "{\"f_init_val\":\""+i+"\",\"f_work_mode\":\"1\",\"f_sys_name\":\"VDO\"}");
        redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        List<Map<String, Object>> devList = new ArrayList<>();
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for (SdDevices sdDevice : list) {
            //redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("devId", sdDevice.getEqId().toString());
            if (sdDevice.getEqType() != null)
                json.put("devType", sdDevice.getEqType().toString());
            //方向
            if (sdDevice.getEqDirection() != null)
                json.put("direction", sdDevice.getEqDirection());
            //设备状态
            if (sdDevice.getEqType() != null && "109".equals(sdDevice.getEqType().toString())) {
                continue;
            }
            if (sdDevice.getEqType() != null && "110".equals(sdDevice.getEqType().toString())) {
                SdIcyRoad sdIcyRoad = (SdIcyRoad) redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
                json.put("state", JSONArray.toJSON(sdIcyRoad).toString());
            } else {
                json.put("state", redisCache.getCacheObject(String.valueOf(sdDevice.getEqId())) + "");
            }
            devList.add(json);
        }
        String res = JSON.toJSONString(devList);
        return res;
    }
   /* @PostMapping("/controlDevice")
    public void controlDevice(String devId,String devType,String state) {
        EquipmentControlInstruction eci = new EquipmentControlInstruction();
        eci.controlInstruction(Long.parseLong(devType),null,devId,"0",null,state);
    }*/

    //3d测试
    @PostMapping("/test")
    public String test() {
        return "get 3d info";
    }
    //admin yujieying
   /* @GetMapping("/getDevState")
    public String list() {
        redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
        SdDevices sdDevices = new SdDevices();
        List<Map<String,String>> devList = new ArrayList<>();
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for(SdDevices sdDevice:list){
            String a = redisCache.getCacheObject(sdDevice.getEqId().toString());
            Map<String,String> json = new HashMap<String,String>();
            json.put("devId",sdDevice.getEqId().toString());
            json.put("plcId",sdDevice.getEqHostId().toString());
            String direction = "";
            if(sdDevice.getEqType()==1||sdDevice.getEqType()==2){
                if(sdDevice.getEqName().contains("YK")){
                    direction="1";
                }else{
                    direction="0";
                }
            }
            json.put("direction",direction);
            json.put("state","1");
            devList.add(json);
        }
        String res = JSON.toJSONString(devList);
        return res;
    }

    @PostMapping("/controlDevice")
    public void controlDevice(String devId,String devType,String state) {
        EquipmentControlInstruction eci = new EquipmentControlInstruction();
        eci.controlInstruction(Long.parseLong(devType),null,Long.parseLong(devId),"0",null,state);
    }*/
    //admin liulun

    /**
     * 查询设备状态
     */
   /* @PreAuthorize("@ss.hasPermi('system:event:list')")
    @GetMapping("/getDevState")
    public String list() {
        redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
        SdDevices sdDevices = new SdDevices();
        List<Map<String,String>> devList = new ArrayList<>();
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for(SdDevices sdDevice:list){
            String a = redisCache.getCacheObject(sdDevice.getEqId());
            Map<String,String> json = new HashMap<String,String>();
            json.put("devId",sdDevice.getEqId());
            json.put("plcId",sdDevice.getEqHostId().toString());
            json.put("direction",sdDevice.getEqDirection());
            json.put("state","1");
            devList.add(json);
        }
        String res = JSON.toJSONString(devList);
        return res;
    }*/

    /*@GetMapping("/controlDevice")
    public void controlDevice(@PathVariable("devId") List<String> devId,@PathVariable("devType") String devType,@PathVariable("state") String state) {
        EquipmentControlInstruction eci = new EquipmentControlInstruction();
        eci.controlInstruction(Long.parseLong(devType),null,devId,"0",null,state);
    }*///gongfanfei
    @PostMapping("/controlDevice")
    public void controlDevice(String devId, String hostId,String tunnelId, String devType, String state) {
        EquipmentControlInstruction eci = new EquipmentControlInstruction();
        eci.controlInstructionHandleOne(Long.parseLong(devType),tunnelId, devId, "0", null, state);
    }

    /**
     * 隧道诱导发送控制指令。
     *
     * @param hostIP           主机IP
     * @param corModel         闪烁  00：闪烁  02：常量   03：关闭
     * @param Zlane            左车道，全部分区
     * @param Ylane            右车道，全部分区
     * @param whiteLight       白灯亮度
     * @param yellowLight      黄灯亮度
     * @param twinkleModel     闪烁模式
     * @param twinkleFrequency 闪烁频率
     * @param lightTime        闪烁时常
     * @return
     */
    @PostMapping("/controlGuide")
    public AjaxResult controlGuide(String hostIP,
                                   String corModel,
                                   String Zlane,
                                   String Ylane,
                                   String whiteLight,
                                   String yellowLight,
                                   String twinkleModel,
                                   String twinkleFrequency,
                                   String lightTime
    ) {
        if (twinkleModel == null || "".equals(twinkleModel)) {
            throw new RuntimeException("闪烁模式不能为空");
        } else if (twinkleFrequency == null || "".equals(twinkleFrequency)) {
            throw new RuntimeException("闪烁频率不能为空");
        } else if (lightTime == null || "".equals(lightTime)) {
            throw new RuntimeException("亮灯时长不能为空");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("00 06 ");//命令
        sb.append("01 ");//data[0] 0x01:表示当前配置参数 0x02:表示默认配置参数
        sb.append("FF ");//data[1] 需要获取的诱导灯的节点编号、车道信息、分区等数据高位
        sb.append("F");//data[2] 需要获取的诱导灯的节点编号、车道信息、分区等数据低位
        if ("true".equals(Zlane) && "true".equals(Ylane)) {
            sb.append("F ");//data[2] 全部车道，全部分区
        } else if ("true".equals(Zlane)) {
            sb.append("D ");//data[2] 左车道，全部分区
        } else if ("true".equals(Ylane)) {
            sb.append("E ");//data[2] 右车道，全部分区
        } else {
            sb.append("C ");//data[2] 单车道，全部分区
        }
        sb.append("FF ");//data[3] 下面8byte配置数据的掩码，对应bit为1表示执行该数据 对应bit为0表示使用默认数据，主要针对亮度数据
        sb.append(corModel + " ");//闪烁  00：闪烁  02：常量   03：关闭
        String wLight = "00";
        String yLight = "00";
        if (!whiteLight.equals("0")) {
            wLight = Integer.toHexString((Integer.parseInt(whiteLight) * 255) / 100);
        }
        if (!yellowLight.equals("0")) {
            yLight = Integer.toHexString((Integer.parseInt(yellowLight) * 255) / 100);
        }
        sb.append(wLight + " ");//白灯亮度 0xcc/0xff = 80%
        sb.append(yLight + " ");//黄灯亮度 0xcc/0xff = 80%
        if ("00".equals(corModel)) {
            sb.append(twinkleModel + " ");//单闪 01单闪 02双闪 03三闪 。。。。
            String tF = Integer.toHexString(Integer.parseInt(twinkleFrequency)).toUpperCase();
            sb.append((tF.length() == 2 ? tF : "0" + tF) + " ");//频率 20次/分钟
            String lT = Integer.toHexString(Integer.parseInt(lightTime) / 10).toUpperCase();
            sb.append((lT.length() == 2 ? lT : "0" + lT) + " ");//时常 1E*10=300毫秒
        } else if ("02".equals(corModel) || "03".equals(corModel)) {
            sb.append("00 ");//单闪 01单闪 02双闪 03三闪 。。。。
            sb.append("00 ");//频率 20次/分钟
            sb.append("00 ");//时常 1E*10=300毫秒
        }
        sb.append("00 00 ");//保留位
        sb.append(CRC8Util.createCheck(sb.toString().trim()) + " ");//校验位
        sb.insert(0, "67 68 00 0E ");//协议头
        sb.append("0D 0A 00");//协议尾
        String content = DevicesManager.getInstance().hostConfigurationExecuteCommand(hostIP, sb.toString(), "112");
        return undoCmd(content);
    }

    public AjaxResult undoCmd(String respStr) {
        //67 68 00 06 80 06 01 00 00 00 B1 0D 0A
        if (respStr != "" && respStr != null) {
            String[] srtArr = respStr.split("67 68 00 06 80 06 ");
            if (srtArr.length > 1) {
                if (srtArr[1].substring(0, 2).equals("01")) {
                    return AjaxResult.success("成功，正在下发数据给诱导灯");
                } else if (srtArr[1].substring(0, 2).equals("00")) {
                    String substring = srtArr[1].substring(3, 5) + srtArr[1].substring(6, 8);
                    int s = (Integer.parseInt(substring, 16) * 4) / 1000;
                    return AjaxResult.error("失败，主机还有数据没有发送完成,再经过 " + s + " 秒后再试。");
                }
            }
        }
        return AjaxResult.error("失败,接收数据异常");
    }

    @PostMapping("/getHostData")
    public Map getHostData(String hostIP) {
        Map<String, String> map = new HashMap<>();
        String content = DevicesManager.getInstance().hostConfigurationExecuteCommand(hostIP, "67 68 00 02 00 08 38 0D 0A", "112");
        //67 68 00 0E 80 08 01 FF FF 55 00 19 33 02 14 32 00 00 27 0D 0A
        if (content.indexOf("67 68 00 0E 80 08 01") != -1) {
            String[] srtArr = content.split("67 68 00 0E 80 08 01 FF F")[1].split("\\s");
            //车道信息
            if ("F,C".contains(srtArr[0])) {
                map.put("Zlane", "true");
                map.put("Ylane", "true");
            } else if (srtArr[0].equals("D")) {
                map.put("Zlane", "true");
            } else if (srtArr[0].equals("E")) {
                map.put("Ylane", "true");
            }
            map.put("corModel", srtArr[2]);//闪烁模式
            map.put("whiteLight", String.valueOf(Math.round((float) (Integer.parseInt(srtArr[3], 16) * 100) / 255)));
            map.put("yellowLight", String.valueOf(Math.round((float) (Integer.parseInt(srtArr[4], 16) * 100) / 255)));
            if ("00".equals(srtArr[2])) {
                map.put("twinkleModel", srtArr[5]);
                map.put("twinkleFrequency", String.valueOf(Integer.parseInt(srtArr[6], 16)));
                map.put("lightTime", String.valueOf(Integer.parseInt(srtArr[7], 16) * 10));
            }
        }
        return map;
    }

    /**
     * 根据隧道id,方向,所属车道筛选车道指示器
     * @param sdDevices
     * @return
     */
    @PostMapping("/updateCarFingerById")
    public AjaxResult updateCarFingerById(@RequestBody List<SdDevices> sdDevices){
        return AjaxResult.success(sdDevicesService.updateCarFingerById(sdDevices));
    }
}
