package com.tunnel.business.domain.electromechanicalPatrol;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人员班组关系表 sd_user_teams
 */
@ApiModel("人员班组关系表")
public class SdUserTeams extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty("班组id")
    private String teamsId;

    @ApiModelProperty("人员id")
    private Long userId;


    public String getTeamsId() {
        return this.teamsId;
    }

    public void setTeamsId( String teamsId) {
        this.teamsId = teamsId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId( Long userId) {
        this.userId = userId;
    }
}
