package com.tunnel.business.domain.event;

import lombok.Data;

@Data
public class SdDeviceNowState {

    private String eqId;

    private Long eqType;

    private String eqStatus;

    private String eqDirection;

    private String eqName;

    private String eqTunnelId;

    private String pile;

    private String state;

    private String fs;

    private String fx;

    private String co;

    private String vi;

    private String brightness;

    private String frequency;

}
