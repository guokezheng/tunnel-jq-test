package com.tunnel.deal.plc.omron.exception;

public class PlcException extends Exception{

    private Object protocol;

    public PlcException(String message, Object protocol) {
        super(message);
        this.protocol = protocol;
    }
}
