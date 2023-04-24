package com.tunnel.business.service.electromechanicalPatrol;


import com.tunnel.business.domain.electromechanicalPatrol.SdTeamsList;

import java.util.List;

/**
 * 班组Service接口
 *
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ISdTeamsListService
{


    List<SdTeamsList> selectSdTeamsList(SdTeamsList sdTeamsList);

    String checkTeamsNameUnique(SdTeamsList sdTeamsList);

    int insertTeams(SdTeamsList sdTeamsList);

    int updateTeams(SdTeamsList sdTeamsList);

    public SdTeamsList selectTeamsById(String deptId);


    int deleteTeamsById(String deptId);

    int teamsUserSelectAll(String deptId, Long[] userIds);

    boolean checkTeamsExistUser(String deptId);

    String existInTeams(Long userId);
}
