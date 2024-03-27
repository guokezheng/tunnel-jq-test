package com.zc.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.zc.domain.informationCenterDto.*;
import com.zc.service.InformationDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * @author ：zhangguoli
 */
@RestController
@RequestMapping("/information")
@Api(tags = "数据中台")
@ApiSupport(order = 16)
public class InformationDicController {

    @Autowired
    InformationDicService informationDicService;

    /**
     * 查询信息分中心字典
     * @param opmaSubCenterDto
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/queryInformatDic")
    @ApiOperation("查询信息分中心字典")
    public Result queryInformatDic(@RequestBody OpmaSubCenterDto opmaSubCenterDto) throws URISyntaxException {
        return Result.toResult((Integer) informationDicService.queryInformatDic(opmaSubCenterDto));
    }

    /**
     * 查询分中心（拆账）
     * @param splitSubCenterDto
     * @return
     */
    @PostMapping("/querySplitSubCenterInformation")
    @ApiOperation("查询分中心（拆账）")
    public Result querySplitSubCenterInformation(@RequestBody SplitSubCenterDto splitSubCenterDto)  {
        return Result.toResult((Integer)informationDicService.querySplitSubCenterInformation(splitSubCenterDto));
    }

    /**
     * 查询国标公路字典
     * @param roadDto
     * @return
     */
    @PostMapping("/queryRoadInformation")
    @ApiOperation("查询国标公路字典")
    public Result queryRoadInformation(@RequestBody RoadDto roadDto)  {
        return Result.toResult((Integer)informationDicService.queryRoadInformation(roadDto));
    }

    /**
     * 分页查询收费站字典
     * @param tollStatDto
     * @return
     */
    @PostMapping("/queryTollStartInformation")
    @ApiOperation("分页查询收费站字典")
    public Result queryTollStartInformation(@RequestBody TollStatDto tollStatDto)  {
        return Result.toResult((Integer)informationDicService.queryTollStartInformation(tollStatDto));
    }

    /**
     * 查询收费路段字典
     * @param tollSectionDto
     * @return
     */
    @PostMapping("/queryTollSectionInformation")
    @ApiOperation("查询收费路段字典")
    public Result queryTollSectionInformation(@RequestBody TollSectionDto tollSectionDto)  {
        return Result.toResult((Integer)informationDicService.queryTollSectionInformation(tollSectionDto));
    }

    /**
     * 查询管养公司编码信息
     * @param opmaManagerDto
     * @return
     */
    @PostMapping("/queryOpmaManagerInformation")
    @ApiOperation("查询管养公司编码信息")
    public Result queryOpmaManagerInformation(@RequestBody OpmaManagerDto opmaManagerDto) {
        return Result.toResult((Integer)informationDicService.queryOpmaManagerInformation(opmaManagerDto));
    }

    /**
     * 查询管养单位字典
     * @param managerCompanyDto
     * @return
     */
    @PostMapping("/queryManagerCompanyInformation")
    @ApiOperation("查询管养单位字典")
    public Result queryManagerCompanyInformation(@RequestBody ManagerCompanyDto managerCompanyDto)  {
        return Result.toResult((Integer)informationDicService.queryManagerCompanyInformation(managerCompanyDto));
    }

    /**
     * 查询管辖路段（拆账）
     * @param splitSectionDto
     * @return
     */
    @PostMapping("/querySplitSectionInformation")
    @ApiOperation("查询管辖路段（拆账）")
    public Result querySplitSectionInformation(@RequestBody SplitSectionDto splitSectionDto)  {
        return Result.toResult((Integer)informationDicService.querySplitSectionInformation(splitSectionDto));
    }

    /**
     * 查询运营管理单位（拆账）表
     * @param managerDto
     * @return
     */
    @PostMapping("/querySplitManagerInformation")
    @ApiOperation("查询运营管理单位（拆账）表")
    public Result querySplitManagerInformation(@RequestBody SplitManagerDto managerDto)  {
        return Result.toResult((Integer)informationDicService.querySplitManagerInformation(managerDto));
    }

    /**
     * 查询集团公司表信息
     * @param opmaCliqueDto
     * @return
     */
    @PostMapping("/queryOpmaCliqueInformation")
    @ApiOperation("查询集团公司表信息")
    public Result queryOpmaCliqueInformation(@RequestBody OpmaCliqueDto opmaCliqueDto) {
        return Result.toResult((Integer)informationDicService.queryOpmaCliqueInformation(opmaCliqueDto));
    }

    /**
     * 节点信息查询列表
     * @param nodeDto
     * @return
     */
    @PostMapping("/queryNodeInformation")
    @ApiOperation("节点信息查询列表")
    public Result queryNodeInformation(@RequestBody NodeDto nodeDto) {
        return Result.toResult((Integer)informationDicService.queryNodeInformation(nodeDto));
    }

    /**
     * 相邻门架查询
     * @param adjacentGantryDto
     * @return
     */
    @PostMapping("/queryAdjacentGantry")
    @ApiOperation("相邻门架查询")
    public Result queryAdjacentGantry(@RequestBody AdjacentGantryDto adjacentGantryDto)  {
        return Result.toResult((Integer)informationDicService.queryAdjacentGantry(adjacentGantryDto));
    }
}

