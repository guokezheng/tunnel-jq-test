package com.zc.service;

import com.zc.domain.informationCenterDto.*;

import java.net.URISyntaxException;

public interface InformationDicService {

    /**
     * 查询信息分中心字典
     * @param opmaSubCenterDto
     * @return
     * @throws URISyntaxException
     */
    public Object queryInformatDic(OpmaSubCenterDto opmaSubCenterDto) throws URISyntaxException;

    /**
     * 查询分中心（拆账）
     * @param splitSubCenterDto
     * @return
     */
    public Object querySplitSubCenterInformation (SplitSubCenterDto splitSubCenterDto);

    /**
     * 查询国标公路字典
     * @param roadDto
     * @return
     */
    public Object queryRoadInformation(RoadDto roadDto);

    /**
     * 分页查询收费站字典
     * @param tollStatDto
     * @return
     */
    public Object queryTollStartInformation(TollStatDto tollStatDto);

    /**
     * 查询收费路段字典
     * @param tollSectionDto
     * @return
     */
    public Object queryTollSectionInformation(TollSectionDto tollSectionDto);

    /**
     * 查询管养公司编码信息
     * @param opmaManagerDto
     * @return
     */
    public Object queryOpmaManagerInformation(OpmaManagerDto opmaManagerDto);

    /**
     * 查询管养单位字典
     * @param managerCompanyDto
     * @return
     */
    public Object queryManagerCompanyInformation(ManagerCompanyDto managerCompanyDto);

    /**
     * 查询管辖路段（拆账）
     * @param splitSectionDto
     * @return
     */
    public Object querySplitSectionInformation(SplitSectionDto splitSectionDto);

    /**
     * 查询运营管理单位（拆账）表
     * @param managerDto
     * @return
     */
    public Object querySplitManagerInformation(SplitManagerDto managerDto);

    /**
     * 查询集团公司表信息
     * @param opmaCliqueDto
     * @return
     */
    public Object queryOpmaCliqueInformation(OpmaCliqueDto opmaCliqueDto);

    /**
     * 节点信息查询列表
     * @param nodeDto
     * @return
     */
    public Object queryNodeInformation(NodeDto nodeDto);

    /**
     * 相邻门架查询
     * @param adjacentGantryDto
     * @return
     */
    public Object queryAdjacentGantry(AdjacentGantryDto adjacentGantryDto);
}
