package com.tunnel.platform.business.othersystem.power;


import org.springframework.stereotype.Component;

/**
 * modbus通讯工具类,采用modbus4j实现
 * 数据读取
 * @author YangChao
 * @dependencies modbus4j-3.0.3.jar
 * @website https://github.com/infiniteautomation/modbus4j
 */
@Component
public class Modbus4jUtils {
    /**
     * 工厂。
     *//*
	private static final Logger log = LoggerFactory.getLogger(Modbus4jUtils.class);
	 @Autowired
	    private static ISdPowerRecordService sdPowerRecordService;
	 @Autowired
	    private static ISdPowerForwardingTableService sdPowerForwardingTableService;
    static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    *//**
     * 获取master
     *
     * @return
     * @throws ModbusInitException
     *//*
    public static ModbusMaster getMaster() throws ModbusInitException {
        IpParameters params = new IpParameters();
       *//* params.setHost("10.7.97.144");// 指定ip
*//*        params.setHost("10.7.98.189");
        params.setPort(503);// 指定端口
        //
        // modbusFactory.createRtuMaster(wapper); //RTU 协议
        // modbusFactory.createUdpMaster(params);//UDP 协议
        // modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
        ModbusMaster master = modbusFactory.createTcpMaster(params, false);// TCP 协议
        master.init();//初始化

        return master;
    }

    *//**
     * 读取[01 Coil Status 0x]类型 开关数据
     *
     * @param slaveId
     *            slaveId--从站编号,一般是1,2,3...
     * @param offset
     *            位置--偏移位置,就是地址,一般是1,2,3..
     * @return 读取值
     * @throws ModbusTransportException
     *             异常
     * @throws ErrorResponseException
     *             异常
     * @throws ModbusInitException
     *             异常
     *//*
    public static Boolean readCoilStatus(int slaveId, int offset)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 01 Coil Status
        BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }

    *//**
     * 读取[02 Input Status 1x]类型 开关数据
     *
     * @param slaveId
     * @param offset
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     *//*
    public static Boolean readInputStatus(int slaveId, int offset)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 02 Input Status
        BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }

    *//**
     * 读取[03 Holding Register类型 2x]模拟量数据
     *
     * @param slaveId
     *            slave Id
     * @param offset
     *            位置
     * @param dataType
     *            数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return
     * @throws ModbusTransportException
     *             异常
     * @throws ErrorResponseException
     *             异常
     * @throws ModbusInitException
     *             异常
     *//*
    public static Number readHoldingRegister(int slaveId, int offset, int dataType)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 03 Holding Register类型数据读取
    	System.out.println("v030:" + 3);
        BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
        System.out.println(loc);
        Number value = getMaster().getValue(loc);
        return value;
    }

    *//**
     * 读取[04 Input Registers 3x]类型 模拟量数据
     *
     * @param slaveId
     *            slaveId
     * @param offset
     *            位置
     * @param dataType
     *            数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return 返回结果
     * @throws ModbusTransportException
     *             异常
     * @throws ErrorResponseException
     *             异常
     * @throws ModbusInitException
     *             异常
     *//*
    public static Number readInputRegisters(int slaveId, int offset, int dataType)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
    	System.out.println("v030:" + 2);
        // 04 Input Registers类型数据读取
        BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
        Number value = getMaster().getValue(loc);
        return value;
    }

    *//**
     * 批量读取使用方法
     *
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     *//*
    public static void batchRead() throws ModbusTransportException, ErrorResponseException, ModbusInitException {

        BatchRead<Integer> batch = new BatchRead<Integer>();// 批量

        batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.FOUR_BYTE_FLOAT));
        batch.addLocator(1, BaseLocator.inputStatus(1, 0));

        ModbusMaster master = getMaster();

        batch.setContiguousRequests(false);
        BatchResults<Integer> results = master.send(batch);// 添加多个要读取的变量地址,一起发送
        System.out.println(results.getValue(0));
        System.out.println(results.getValue(1));
    }

    *//**
     * 电力监控数据对接
     *
     * @param args
     *//*
    //  public static void ss(){
	*//**
	 *//*
	public static void domain() {
        try {
        	sdPowerForwardingTableService = (ISdPowerForwardingTableService) SpringContextUtils.getBean(ISdPowerForwardingTableService.class);
        	SdPowerForwardingTable sdPowerForwardingTable = new SdPowerForwardingTable();
        	List<SdPowerForwardingTable> list = sdPowerForwardingTableService.selectSdPowerForwardingTableList(sdPowerForwardingTable);
        	*//*Map<String,SdPowerRecord> powerMap = new HashMap<String,SdPowerRecord>();//定义集合
        	 * 
*//*        	//baiyan
        	*//*for(int j=1;j<=9;j++){*//*
        		
        	//shanting
        	for(int j=1;j<=11;j++){
        		SdPowerRecord sdPowerRecord = new SdPowerRecord();
        		for(int i=0;i<list.size();i++){
        			int slaveId = Integer.valueOf(list.get(i).getDeviceId());
        			if(j != slaveId){
        				continue;
        			}
            		int type = Integer.valueOf(list.get(i).getDeviceType());
            		int offset = Integer.valueOf(list.get(i).getPointAddress());
            		int dataType = 8;
            		String  tunnelId = list.get(i).getTunnelId();
            		//baiyan
            		*//*if(slaveId == j && "S29-LinYiCompany-BaiYanStation-001".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}else if(slaveId == j && "S29-LinYiCompany-BaiYanStation-002".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}else if(slaveId == j && "S29-LinYiCompany-BaiYanStation-003".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}*//*
            		//shanting
            		if(slaveId == j && "S29-ZaoZhuangCompany-ShanTingStation-001,S29-ZaoZhuangCompany-ShanTingStation-002,S29-ZaoZhuangCompany-ShanTingStation-003,S29-ZaoZhuangCompany-ShanTingStation-004".contains(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}*//*else if(slaveId == j && "S29-ZaoZhuangCompany-ShanTingStation-002".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}else if(slaveId == j && "S29-ZaoZhuangCompany-ShanTingStation-003".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}else if(slaveId == j && "S29-ZaoZhuangCompany-ShanTingStation-004".equals(tunnelId)){
            			sdPowerRecord.setTunnelId(tunnelId);
            			selectdPowerForwardingTableType(sdPowerRecord, type, slaveId, offset, dataType);
            		}*//*
            	}
        		sdPowerRecord.setEqId(j+"");
                sdPowerRecordService = (ISdPowerRecordService) SpringContextUtils.getBean(ISdPowerRecordService.class);
                sdPowerRecordService.insertSdPowerRecord(sdPowerRecord);
        	}	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static  void selectdPowerForwardingTableType(SdPowerRecord sdPowerRecord, int type,int slaveId, int offset, int dataType){
		try {
			slaveId = 1;
		if(type == 1){
			Number monitorIa = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Ia = (double)monitorIa.intValue()/10;//电流A
			sdPowerRecord.setElectricCurrenta(String.valueOf(Ia));
		}else if(type == 2){
			Number monitorIb = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Ib = (double)monitorIb.intValue()/10;//电流B
			sdPowerRecord.setElectricCurrentb(String.valueOf(Ib));
		}else if(type == 3){
			Number monitorIc = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Ic = (double)monitorIc.intValue()/10;//电流C
			sdPowerRecord.setElectricCurrentc(String.valueOf(Ic));
		}else if(type == 4){
			Number monitorUab = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Uab =(double) monitorUab.intValue()/100;//电压AB
			sdPowerRecord.setVoltageab(String.valueOf(Uab));
		}else if(type == 5){
			Number monitorUbc = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Ubc =(double) monitorUbc.intValue()/100;//电压BC
			sdPowerRecord.setVoltagebc(String.valueOf(Ubc));
		}else if(type == 6){
			Number monitorUca = readHoldingRegister(slaveId, offset, DataType.FOUR_BYTE_FLOAT);// 注意,float
			double Uca =(double) monitorUca.intValue()/100;//电压CA
			sdPowerRecord.setVoltagac(String.valueOf(Uca));
		}
		} catch (Exception e) {
            e.printStackTrace();
        }
	} */

}