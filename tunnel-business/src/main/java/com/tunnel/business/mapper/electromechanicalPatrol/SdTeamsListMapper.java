package com.tunnel.business.mapper.electromechanicalPatrol;

import com.tunnel.business.domain.electromechanicalPatrol.SdTeamsList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡查任务Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-04
 */
public interface SdTeamsListMapper
{

    List<SdTeamsList> selectSdTeamsList(SdTeamsList sdTeamsList);

    SdTeamsList checkTeamsNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    SdTeamsList selectTeamsById(@Param("parentId") String parentId);

    int insertTeams(SdTeamsList sdTeamsList);

    int updateTeams(SdTeamsList sdTeamsList);

    String selectTeamsChildByPid(@Param("parentId") String parentId);

    SdTeamsList selectTeamsInfoById(@Param("deptId")String deptId);


    int deleteTeamsById(@Param("deptId")String deptId);
}
