package com.zc.iot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.iot.domain.*;
import com.zc.iot.mapper.IotDeviceMapper;
import com.zc.iot.service.IIotDeviceService;
import com.zc.iot.service.IIotFunctionService;
import com.zc.relay.dto.DownData;
import com.zc.relay.dto.DownDataChildDevice;
import com.zc.relay.dto.DownSetData;
import com.zc.relay.handler.down.DownInstructHandle;
import com.zc.relay.handler.down.DownInstructRedisPubHandleImpl;
import com.zc.relay.dto.DownSetDataChildDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 设备Service业务层处理
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-04
 */
@Service
public class IotDeviceServiceImpl implements IIotDeviceService 
{
    @Autowired
    private IotDeviceMapper iotDeviceMapper;

    @Resource
    private IIotFunctionService iIotFunctionService;

    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /**
         * 添加数据到 redis 缓存
         */
        addIotDeviceCache();
    }
    /**
     * 添加数据到 redis 缓存
     */
    public void addIotDeviceCache()
    {
        // 获取全部设备列表数据
        List<IotDevice> iotDevices = selectIotDeviceList(null);
        if (iotDevices == null || iotDevices.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_DEVICE);
        redisCache.deleteObject(RedisKeyConstants.IOT_DEVICE_CODE_ID);

        // 添加 redis 缓存数据
        iotDevices.forEach(val -> {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE, val.getId(), val);
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, val.getDeviceCode(), val.getId());
        });
    }


    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    @Override
    public IotDevice selectIotDeviceById(Long id)
    {
        return iotDeviceMapper.selectIotDeviceById(id);
    }

    /**
     * 查询设备列表
     * 
     * @param iotDevice 设备
     * @return 设备
     */
    @Override
    public List<IotDevice> selectIotDeviceList(IotDevice iotDevice)
    {
        return iotDeviceMapper.selectIotDeviceList(iotDevice);
    }

    /**
     * 新增设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    @Override
    public int insertIotDevice(IotDevice iotDevice)
    {

        String deviceCode = iotDevice.getDeviceCode();
        String deviceName = iotDevice.getDeviceName();
        Long productKey = iotDevice.getProductKey();
        Long groupKey = iotDevice.getGroupKey();

        if (StringUtils.isEmpty(deviceCode))
        {
            throw new ServiceException("设备编号不存在", HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(deviceName))
        {
            throw new ServiceException("设备名称不存在", HttpStatus.BAD_REQUEST);
        }

        if (productKey == null)
        {
            throw new ServiceException("所属产品不存在", HttpStatus.BAD_REQUEST);
        }

        IotProduct iotProduct = redisCache.getCacheMapValue(RedisKeyConstants.IOT_PRODUCT, productKey);

        if (groupKey == null)
        {
            throw new ServiceException("所属组织不存在", HttpStatus.BAD_REQUEST);
        }

        IotGroup iotGroup = redisCache.getCacheMapValue(RedisKeyConstants.IOT_GROUP, groupKey);

        if (iotGroup == null)
        {
            throw new ServiceException("所属组织不存在", HttpStatus.BAD_REQUEST);
        }

        if (iotProduct == null)
        {
            throw new ServiceException("所属产品不存在", HttpStatus.BAD_REQUEST);
        }

        IotDevice iotDevice1 = iotDeviceMapper.selectIotDeviceByCode(deviceCode);

        if (iotDevice1 != null)
        {
            throw new ServiceException("设备编号不能重复", HttpStatus.CONFLICT);
        }

        iotDevice.setCreateTime(DateUtils.getNowDate());

        // 添加到数据库
        int result = iotDeviceMapper.insertIotDevice(iotDevice);
        // 添加到缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE, iotDevice.getId(), iotDevice);
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, iotDevice.getDeviceCode(), iotDevice.getId());
        }
        return result;
    }

    /**
     * 修改设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    @Override
    public int updateIotDevice(IotDevice iotDevice)
    {

        Long id = iotDevice.getId();

        if (id == null)
        {
            throw new ServiceException("主键不存在", HttpStatus.BAD_REQUEST);
        }

        IotDevice iotDevice1 = selectIotDeviceById(id);

        if(iotDevice1 == null)
        {
            throw new ServiceException("没有找到对应的数据", HttpStatus.CONFLICT);
        }

        String deviceCode = iotDevice.getDeviceCode();

        if (StringUtils.isEmpty(deviceCode))
        {
            throw new ServiceException("设备编号不存在", HttpStatus.BAD_REQUEST);
        }

        String deviceName = iotDevice.getDeviceName();

        if (StringUtils.isEmpty(deviceName))
        {
            throw new ServiceException("设备名称不存在", HttpStatus.BAD_REQUEST);
        }

        Long productKey = iotDevice.getProductKey();

        if (productKey == null)
        {
            throw new ServiceException("所属产品不存在", HttpStatus.BAD_REQUEST);
        }

        IotProduct iotProduct = redisCache.getCacheMapValue(RedisKeyConstants.IOT_PRODUCT, productKey);

        if (iotProduct == null)
        {
            throw new ServiceException("所属产品不存在", HttpStatus.BAD_REQUEST);
        }

        Long groupKey = iotDevice.getGroupKey();

        if (groupKey == null)
        {
            throw new ServiceException("所属组织不存在", HttpStatus.BAD_REQUEST);
        }

        // 组织
        IotGroup iotGroup = redisCache.getCacheMapValue(RedisKeyConstants.IOT_GROUP, groupKey);

        if (iotGroup == null)
        {
            throw new ServiceException("所属组织不存在", HttpStatus.BAD_REQUEST);
        }

        // 检查设备编号是否重复
        IotDevice iotDevice2 = iotDeviceMapper.selectIotDeviceByCode(deviceCode);

        if (iotDevice2 != null && !iotDevice2.getId().equals(id))
        {
            throw new ServiceException("设备编号不能重复", HttpStatus.CONFLICT);
        }

        // 更新时间
        iotDevice.setUpdateTime(DateUtils.getNowDate());

        // 更新数据库数据
        int result = iotDeviceMapper.updateIotDevice(iotDevice);
        // 更新缓存
        if (result == 1)
        {
            if (!iotDevice1.getDeviceCode().equals(iotDevice.getDeviceCode()))
            {
                redisCache.delCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, iotDevice1.getDeviceCode());
                redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, iotDevice.getDeviceCode(), id);
            }

            redisCache.setCacheMapValue(RedisKeyConstants.IOT_DEVICE, iotDevice.getId(), iotDevice);
        }

        return result;
    }

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteIotDeviceByIds(Long[] ids)
    {

        int result = iotDeviceMapper.deleteIotDeviceByIds(ids);

        if (result > 0)
        {

            for (Long id : ids) {
                IotDevice iotDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, id);

                if (iotDevice == null) continue;

                redisCache.delCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, iotDevice.getDeviceCode());

            }

            /*删除设备缓存数据*/
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_DEVICE, ids);
            /*删除实时数据缓存数据*/
            Arrays.asList(ids).forEach(id -> redisCache.deleteObject(RedisKeyConstants.getDeviceFunctionValueKey(id)));
        }

        return result;
    }

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    @Override
    public int deleteIotDeviceById(Long id)
    {

        IotDevice iotDevice = selectIotDeviceById(id);

        if (iotDevice == null)
        {
            throw new ServiceException("没有找到对应的数据", HttpStatus.CONFLICT);
        }

        int result = iotDeviceMapper.deleteIotDeviceById(id);

        if (result == 0)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, iotDevice.getDeviceName());
            /*删除设备缓存数据*/
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_DEVICE, id);
            /*删除实时数据缓存数据*/
            redisCache.deleteObject(RedisKeyConstants.getDeviceFunctionValueKey(id));
        }

        return result;
    }

    /**
     * 查询设备功能
     * @param id 设备主键
     * @return
     */
    @Override
    public List<IotFunction> selectIotDeviceFunction(Long id)
    {

        if (id == null)
        {
            return null;
        }


        /*根据设备 id 获取缓存中设备信息*/
        IotDevice iotDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, id);

        if (iotDevice == null)
        {
            return null;
        }

        Long productKey = iotDevice.getProductKey();

        /*缓存中获取产品信息*/
        IotProduct iotProduct = redisCache.getCacheMapValue(RedisKeyConstants.IOT_PRODUCT, productKey);

        if (iotProduct == null)
        {
            return null;
        }

        Long categoryKey = iotProduct.getCategoryKey();

        IotFunction iotFunction = new IotFunction();

        iotProduct.setCategoryKey(categoryKey);

        /*查询出所属设备的功能信息*/
        List<IotFunction>  iotFunctions = iIotFunctionService.selectIotFunctionList(iotFunction);

        if (iotFunctions == null)
        {
           return null;
        }

        /*缓存中取出设备功能的实时数据*/
        iotFunctions.forEach(item ->
        {

            /*判断功能是否可读*/
            if (!IotFunction.READABLE_WRITABLE.equals(item.getReadWriteType()) && !IotFunction.READABLE.equals(item.getReadWriteType()))
            {
                return;
            }

            Number value = redisCache.getCacheMapValue(RedisKeyConstants.getDeviceFunctionValueKey(id), item.getId());
            if (value == null) return;
            item.setRealValue(value);
        });

        return iotFunctions;
    }

    /**
     *  功能调试，属性设置
     * @param iotDebugAttribute 属性调试参数
     * @return 请求结果
     */
    @Override
    public boolean setAttribute(IotDebugAttribute iotDebugAttribute)
    {

        if (iotDebugAttribute == null)
        {
            return false;
        }

        /*调试属性值*/
        Number value = iotDebugAttribute.getValue();

        if (value == null)
        {
            return false;
        }

        /*获取下发的数据*/
        DownData downData = getDownData(iotDebugAttribute);

        if (downData == null)
        {
            return false;
        }

        DownInstructHandle downInstructRedisPubHandle = new DownInstructRedisPubHandleImpl();

        /*下发子设备数据*/
        if (downData instanceof DownDataChildDevice)
        {

            DownSetDataChildDevice<Number> downSetData = new DownSetDataChildDevice<>();

            downSetData.setAttrs((DownDataChildDevice)downData);
            downSetData.setValue(value);

            return downInstructRedisPubHandle.setAttribute(downSetData);
        }

        DownSetData<Number> downSetData = new DownSetData<>();

        downSetData.setAttrs(downData);
        downSetData.setValue(value);

        return downInstructRedisPubHandle.setAttribute(downSetData);


    }

    /**
     *  功能调试，属性获取
     * @param iotDebug
     * @return 请求结果
     */
    @Override
    public boolean getAttribute(IotDebug iotDebug)
    {
        if (iotDebug == null)
        {
            return false;
        }

        DownData downData = getDownData(iotDebug);

        if (downData == null)
        {
            return false;
        }

        DownInstructHandle downInstructRedisPubHandle = new DownInstructRedisPubHandleImpl();

        /*下发子设备数据*/
        if (downData instanceof DownDataChildDevice)
        {
            return downInstructRedisPubHandle.getAttribute((DownDataChildDevice)downData);
        }

        return downInstructRedisPubHandle.getAttribute(downData);

    }


    /**
     *  功能调试，服务调用
     * @param iotDebugServe 服务调式参数
     * @return 请求结果
     */
    @Override
    public boolean invokeService(IotDebugServe iotDebugServe)
    {
        if (iotDebugServe == null)
        {
            return false;
        }
        /*调试属性值*/
        JSONObject value = iotDebugServe.getValue();

        if (value == null)
        {
            return false;
        }

        DownData downData = getDownData(iotDebugServe);

        if (downData == null)
        {
            return false;
        }

        DownInstructHandle downInstructRedisPubHandle = new DownInstructRedisPubHandleImpl();

        /*下发子设备数据*/
        if (downData instanceof DownDataChildDevice)
        {
            DownSetDataChildDevice<JSONObject> downSetData = new DownSetDataChildDevice<>();

            downSetData.setAttrs((DownDataChildDevice)downData);
            downSetData.setValue(value);

            return downInstructRedisPubHandle.serveInvoke(downSetData);
        }

        DownSetData<JSONObject> downSetData = new DownSetData<>();

        downSetData.setAttrs(downData);
        downSetData.setValue(value);

        return downInstructRedisPubHandle.serveInvoke(downSetData);

    }

    /**
     * 获取下发数据
     * @param iotDebug 调试数据
     * @return
     */
    private DownData getDownData(IotDebug iotDebug)
    {

        String identifier = iotDebug.getIdentifier();
        Long deviceId = iotDebug.getDeviceId();
        Long functionId = iotDebug.getFunctionId();

        if (deviceId == null || functionId == null)
        {
            return null;
        }

        /*根据设备 id 获取缓存中设备信息*/
        IotDevice iotDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, deviceId);

        /*不存在则返回*/
        if (iotDevice == null)
        {
            return null;
        }

        /*根据功能主键获取缓存功能信息*/
        IotFunction iotFunction = redisCache.getCacheMapValue(RedisKeyConstants.IOT_FUNCTION, functionId);

        /*不存在则返回*/
        if (iotFunction == null)
        {
            return null;
        }

        /*设备编号*/
        String deviceCode = iotDevice.getDeviceCode();
        /*功能标识*/
        String functionIdentifier = iotFunction.getIdentifier();

        if (StringUtils.isEmpty(deviceCode) || StringUtils.isEmpty(functionIdentifier))
        {
            return null;
        }

        /*产品主键*/
        Long productKey = iotDevice.getProductKey();

        /*根据产品 id 获取缓存中产品信息*/
        IotProduct iotProduct = redisCache.getCacheMapValue(RedisKeyConstants.IOT_PRODUCT, productKey);

        if (iotProduct == null)
        {
            return null;
        }


        /*判断是否是网关子设备*/
        if (IotProduct.SUBSET_DEVICE.equals(iotProduct.getNodeType()))
        {
            /*所属网关设备主键*/
            Long gatewayDevice = iotDevice.getGatewayDevice();

            if (gatewayDevice == null)
            {
                return null;
            }

            /*缓存中获取所属网关设备信息*/
            IotDevice IotGatewayDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, gatewayDevice);

            if (IotGatewayDevice == null)
            {
                return null;
            }

            /*网关设备编号*/
            String gatewayDeviceCode = IotGatewayDevice.getDeviceCode();

            if (StringUtils.isEmpty(gatewayDeviceCode))
            {
                return null;
            }

            DownDataChildDevice downDataChildDevice = new DownDataChildDevice();
            downDataChildDevice.setFunctionIdentify(functionIdentifier);
            downDataChildDevice.setIdentifier(identifier);

            downDataChildDevice.setDeviceCode(gatewayDeviceCode);
            downDataChildDevice.setChildDeviceCode(deviceCode);

            return downDataChildDevice;
        }

        DownData downData = new DownData();

        downData.setIdentifier(identifier);
        downData.setDeviceCode(deviceCode);
        downData.setFunctionIdentify(functionIdentifier);

        return downData;

    }

}
