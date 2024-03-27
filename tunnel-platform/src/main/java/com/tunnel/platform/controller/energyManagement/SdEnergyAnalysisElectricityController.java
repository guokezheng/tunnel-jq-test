package com.tunnel.platform.controller.energyManagement;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.service.energyManagement.ISdEnergyAnalysisElectricityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 能耗报表Controller
 *
 * @author tjw
 * @date 2023-7-12
 */
@RestController
@RequestMapping("/energy/analysis")
@Api(tags = "能耗报表")
@ApiSupport(order = 16)
public class SdEnergyAnalysisElectricityController extends BaseController
{

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdTaskListService sdTaskListService;


    @Autowired
    private ISdEnergyAnalysisElectricityService sdEnergyAnalysisElectricityService;

    /**
     * 站点用能左侧机构树
     * @return
     */
    @PostMapping("/siteTree")
    @ApiOperation("站点用能左侧机构树")
    public Result treeSelectYG1(String deptName) {
        List<SysDeptTunnel>deptTunnels = new ArrayList<>();
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new NullPointerException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        SysDept dept = new SysDept();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if(user.isAdmin())
            dept.setDeptName(deptName);
        else{
            dept.setDeptName(deptName);
            dept.setDeptId(deptId);
        }
        List<SysDept> source = deptService.listSiteDeptYG1(dept);

        List<SdTunnels> tunnels = tunnelsService.selectSiteTunnelList(deptId,deptName);

        List<SysDept> target = new ArrayList<>();

        boolean hasChildren = deptService.hasChildByDeptId(deptId);
        if (hasChildren) {
            target = deptService.selectChildrenIncludeSelfByIdNormal(deptId);
        }else{
            getTreeListByDeptId(source, deptId, target);
        }

        if(target!=null&&target.size()>0){
            for(int i = 0;i<target.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId((target.get(i).getDeptId()).toString());
                deptTunnel.setDeptName(target.get(i).getDeptName());
                deptTunnel.setParentId((target.get(i).getParentId()).toString());
                deptTunnel.setParentName(target.get(i).getParentName());
                deptTunnels.add(deptTunnel);
            }
        }
        if(tunnels!=null&&tunnels.size()>0){
            for(int i = 0;i<tunnels.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(tunnels.get(i).getTunnelId());
                deptTunnel.setDeptName(tunnels.get(i).getTunnelName());
                deptTunnel.setParentId((tunnels.get(i).getDeptId()).toString());
                deptTunnel.setParentName(tunnels.get(i).getTunnelStationName());
                deptTunnels.add(deptTunnel);
            }
        }

        return Result.success(sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels));


    }

    public List<SysDept> getTreeListByDeptId(List<SysDept> source, String deptId, List<SysDept> target) {
        for (SysDept sysDept : source) {
            if (deptId.equals(sysDept.getDeptId())) {
                target.add(sysDept);
                String parentId = sysDept.getParentId();

                if (org.apache.commons.lang3.StringUtils.isNotBlank(parentId)) {
                    getTreeListByDeptId(source, parentId, target);
                }
            }
        }

        return target;
    }




    /**
     * 获取分项树
     */
    @ApiOperation("获取分项树")
    @GetMapping("/itemizedTreeselect")
    public AjaxResult itemizedTreeselect(EnergyConfigcenterItemized itemized) {
        List<EnergyConfigcenterItemized> itemizeds = sdEnergyAnalysisElectricityService.selectItemizedList(itemized);
        List<SysDeptTunnel>deptTunnels = new ArrayList<>();
        if(itemizeds!=null&&itemizeds.size()>0){
            for(int i = 0;i<itemizeds.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(itemizeds.get(i).getItemizedCode());
                deptTunnel.setDeptName(itemizeds.get(i).getItemizedName());
                deptTunnel.setParentId(itemizeds.get(i).getParentCode());
                deptTunnel.setParentName("");
                deptTunnels.add(deptTunnel);
            }
        }


        return AjaxResult.success(sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels));
    }


    /**
     * 获取分类树
     */
    @ApiOperation("获取分类树")
    @GetMapping("/classificationTreeselect")
    public AjaxResult classificationTreeselect(EnergyConfigcenterClassification classification) {
        // 能耗查询只查光伏自用，市电，柴发，风电，其他
        List<EnergyConfigcenterClassification> classifications = sdEnergyAnalysisElectricityService.selectClassificationList(classification);
        List<SysDeptTunnel>deptTunnels = new ArrayList<>();
        if(classifications!=null&&classifications.size()>0){
            for(int i = 0;i<classifications.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(classifications.get(i).getClassificationCode());
                deptTunnel.setDeptName(classifications.get(i).getClassificationName());
                deptTunnel.setParentId(classifications.get(i).getParentCode());
                deptTunnel.setParentName("");
                deptTunnels.add(deptTunnel);
            }
        }

        return AjaxResult.success(sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels));
    }





}
