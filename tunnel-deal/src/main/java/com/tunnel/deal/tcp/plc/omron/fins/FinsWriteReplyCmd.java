package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins 写回复指令
 *
 * @author zs
 * @date 2023/8/17
 */
public class FinsWriteReplyCmd extends FinsGeneralCmd {


    /**
     * 主请求码，1字节
     */
    private String mrc = "01";

    /**
     * 次请求码，1字节，主/次请求码组合：0101是读操作；0102是写操作代码；2301是强制操作代码；
     */
    private String src = "02";

    /**
     * 错误码,2字节（结束码，即响应码，正常时返回 0000）
     */
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取完整的命令
     * @return
     */
    public String getFullCmd(){
        StringBuffer s = new StringBuffer();
        s.append(header).append(length).append(command).append(fixedErrorCode).append(icf)
                .append(rsv).append(gct).append(dna).append(da1).append(da2).append(sna)
                .append(sa1).append(sa2).append(sid).append(mrc).append(src)
                .append(errorCode);
        String result = s.toString();
        return result;
    }

}
