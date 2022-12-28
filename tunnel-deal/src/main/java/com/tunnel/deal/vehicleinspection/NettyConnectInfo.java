package com.tunnel.deal.vehicleinspection;

import com.tunnel.business.domain.dataInfo.SdDevices;
import io.netty.channel.*;

import java.sql.Timestamp;

public class NettyConnectInfo {
    private int port;
    private Channel channel;
    private boolean connected;
    private Timestamp activeTime;
    private SdDevices sdDevices;
    private byte[] buffer;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Timestamp getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Timestamp activeTime) {
        this.activeTime = activeTime;
    }

    public SdDevices getSdDevices() {
        return sdDevices;
    }

    public void setSdDevices(SdDevices sdDevices) {
        this.sdDevices = sdDevices;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}
