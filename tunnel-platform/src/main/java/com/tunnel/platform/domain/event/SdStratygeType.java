package com.tunnel.platform.domain.event;

import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import lombok.Data;

import java.util.List;

@Data
public class SdStratygeType extends SdEquipmentType {

    private List<SdStrategy> sdStrategies;

}
