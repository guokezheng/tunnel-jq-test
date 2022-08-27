package com.tunnel.platform.hkdevice;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Component;

/**
 * 报警信息回调函数
 * @author wangxiao
 *
 */
@Component
public class ExceptionCallBack implements HCNetSDK.FExceptionCallBack {

    @Override
    public void invoke(int dwType, NativeLong lUserID, NativeLong lHandle, Pointer pUser) {

        System.out.println("dwType" + dwType);
        System.out.println("lUserID" + lUserID);

    }
}
