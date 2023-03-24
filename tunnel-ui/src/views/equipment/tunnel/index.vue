<template>
  <div class="app-container">
    <!-- 全局搜索 -->
    <el-row :gutter="20" class="topFormRow">
      <el-col :span="7">
          <el-button
            size="small"
            @click="handleAdd"
            v-hasPermi="['system:tunnels:add']"
            >新增
          </el-button>

          <el-button
            size="small"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:tunnels:remove']"
            >删除
          </el-button>
        <el-button
          size="small"
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:tunnels:export']"
        >导出
        </el-button>
          <el-button size="small" @click="resetQuery"
          >刷新</el-button
          >
      </el-col>
      <el-col :span="6" :offset="11">
        <div ref="main" class="grid-content bg-purple">
          <el-input
            v-model="queryParams.searchValue"
            placeholder="请输入隧道名称、桩号,回车搜索"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          >
            <el-button
              slot="append"
              icon="icon-gym-Gsearch"
              @click="boxShow = !boxShow"
            ></el-button>
          </el-input>
        </div>
      </el-col>
    </el-row>
    <div ref="cc" class="searchBox" v-show="boxShow">
      <el-form
        ref="queryForm"
        :inline="true"
        :model="queryParams"
        label-width="80px"
      >
        <el-form-item label="是否启用" prop="poll">
          <el-select
            v-model="queryParams.poll"
            placeholder="请选择是否启用"
            clearable
            size="small"
          >
            <el-option
              v-for="dict in pollOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="bottomBox">
          <el-button size="small" type="primary" @click="handleQuery"
          >搜索</el-button
          >
          <el-button size="small" @click="resetQuery" type="primary" plain
          >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
<!--    <el-form-->
<!--      :model="queryParams"-->
<!--      ref="queryForm"-->
<!--      :inline="true"-->
<!--      v-show="showSearch"-->
<!--      label-width="80px"-->
<!--    >-->
<!--      <el-form-item label="隧道名称" prop="tunnelId" v-show="manageStatin == '0'">-->
<!--        <el-select-->
<!--          v-model="queryParams.tunnelId"-->
<!--          placeholder="请选择所属隧道"-->
<!--          clearable-->
<!--          size="small"-->
<!--        >-->
<!--          <el-option-->
<!--            v-for="item in tunnelData"-->
<!--            :key="item.tunnelId"-->
<!--            :label="item.tunnelName"-->
<!--            :value="item.tunnelId"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="是否启用" prop="poll">-->
<!--        <el-select-->
<!--          v-model="queryParams.poll"-->
<!--          placeholder="请选择是否启用"-->
<!--          clearable-->
<!--          size="small"-->
<!--        >-->
<!--          <el-option-->
<!--            v-for="dict in pollOptions"-->
<!--            :key="dict.dictValue"-->
<!--            :label="dict.dictLabel"-->
<!--            :value="dict.dictValue"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" size="mini" @click="handleQuery"-->
<!--          >搜索</el-button-->
<!--        >-->
<!--        <el-button size="mini" @click="resetQuery" type="primary" plain-->
<!--          >重置</el-button-->
<!--        >-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['system:tunnels:add']"-->
<!--          >新增-->
<!--        </el-button>-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:tunnels:edit']"-->
<!--          >修改-->
<!--        </el-button>-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['system:tunnels:remove']"-->
<!--          >删除-->
<!--        </el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
    <div class="tableTopHr" ></div>
    <el-table
      v-loading="loading"
      :data="tunnelsList"
      ref="tableRef"
      @selection-change="handleSelectionChange"
      class="allTable"
      height="62vh"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :index="indexMethod" label="序号" align="center" width="60"></el-table-column>
      <!-- <el-table-column label="隧道ID" align="center" prop="tunnelId" /> -->
      <el-table-column label="隧道ID" align="center" prop="tunnelId" width="210"/>
      <el-table-column label="隧道名称" align="center" prop="tunnelName" width="100"/>
      <el-table-column label="隧道地址" align="center" prop="tunnelAddress" width="160"/>
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <!-- <el-table-column label="路段ID" align="center" prop="roadId" /> -->
      <!-- <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="修改者" align="center" prop="updateBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" /> -->
      <el-table-column label="车道数量" align="center" prop="lane" />
      <el-table-column
        label="隧道长度(米)"
        align="center"
        prop="tunnelLength"
        width="180"
      />
      <el-table-column
        label="隧道开始桩号"
        align="center"
        prop="startPile"
        width="150"
      />
      <el-table-column
        label="隧道结束桩号"
        align="center"
        prop="endPile"
        width="150"
      />
      <el-table-column label="三维坐标" align="center" prop="coordinates" />
      <el-table-column
        label="所属部门"
        align="center"
        prop="deptName"
        width="140"
      />
      <!--      <el-table-column label="备注" align="center" prop="remake" />-->
      <el-table-column
        label="是否启用"
        align="center"
        prop="poll"
        :formatter="pollFormat"
      />
      <!-- <el-table-column label="存储配置图的html" align="center" prop="storeConfigure" /> -->
      <!-- <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改者" align="center" prop="updateBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="240"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate"
            v-hasPermi="['system:tunnels:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="handleUpdate(scope.row,'info')"
            >详情
          </el-button>
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="tunnelConfig(scope.row)"
            v-hasPermi="['system:tunnels:edit']"
            >配置
          </el-button>
          <el-button
            size="mini"
            class="tableBlueButtton"
            @click="tunnelAssociationConfig(scope.row)"
            v-hasPermi="['system:tunnels:edit']"
          >关联配置
          </el-button>
<!--          <el-button-->
<!--            size="mini"-->
<!--            class="tableBlueButtton"-->
<!--            v-hasPermi="['system:tunnels:edit']"-->
<!--          >关联配置-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            class="tableBlueButtton"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['system:tunnels:edit']"-->
<!--            >修改-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            class="tableDelButtton"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['system:tunnels:remove']"-->
<!--            >删除-->
<!--          </el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改隧道关联关系对话框 -->
    <!--<el-dialog :title="titles" :visible.sync="opens" width="500px" append-to-body>
      <el-form ref="forms" :model="forms" :rules="rule" label-width="140px">
        <el-form-item label="隧道" prop="tunnelId">
          <el-input v-model="forms.tunnelId" placeholder="请输入隧道" :disabled="true"/>
        </el-form-item>
        <el-form-item label="外部系统" prop="externalSystemId">
&lt;!&ndash;          <el-input v-model="forms.externalSystemId" placeholder="请选择外部系统" />&ndash;&gt;
          <el-select v-model="forms.externalSystemId" placeholder="请选择外部系统" class="externalSystemId" style="width: 100%">
            <el-option v-for="item in externalSystemData"
                       :key="item.id"
                       :label="item.systemName"
                       :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="隧道方向" prop="tunnelDirection">
          <el-input v-model="forms.tunnelDirection" placeholder="请输入隧道方向" />
        </el-form-item>
        <el-form-item label="外部系统隧道ID" prop="externalSystemTunnelId">
          <el-input v-model="forms.externalSystemTunnelId" placeholder="请输入外部系统隧道ID" />
        </el-form-item>
        <el-form-item label="外部系统隧道方向" prop="externalSystemTunnelDirection">
          <el-input v-model="forms.externalSystemTunnelDirection" placeholder="请输入外部系统隧道方向" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="forms.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForms">确 定</el-button>
        <el-button @click="cancels">取 消</el-button>
      </div>
    </el-dialog>-->

    <!----------------------------------------------------------------------------------------------------------------->

    <el-dialog :title="titles" :visible.sync="opens" width="1500px" append-to-body>
      <el-form ref="forms" :model="forms" :rules="rule" label-width="80px">
        <el-row>
          <el-col :span="5">
            <el-form-item label="隧道名称" prop="tunnelId">
              <el-select v-model="forms.tunnelId" placeholder="请选择隧道" disabled>
                <el-option
                  v-for="item in tunnelList"
                  :key="item.tunnelId"
                  :label="item.tunnelName"
                  :value="item.tunnelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <template>
          <div class="dialogButton addFormButton" @click="addFrom()">添加</div>
          <el-row v-for="(item,index) in tunnelAssociations" :key="index">
            <el-col :span="4">
              <el-form-item label="隧道方向" prop="tunnelDirection">
                <el-select v-model="item.tunnelDirection" placeholder="请输入隧道方向" clearable>
                  <el-option
                    v-for="dict in dict.type.sd_direction"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="5" >
              <el-form-item label="外部系统" prop="externalSystemId">
                <el-select v-model="item.externalSystemId" placeholder="请选择外部系统">
                  <el-option
                    v-for="item in externalSystemData"
                    :key="item.id"
                    :label="item.systemName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="5">
              <el-form-item label="外部系统隧道ID" prop="externalSystemTunnelId" label-width="110px">
                <el-input v-model="item.externalSystemTunnelId" placeholder="请输入外部系统隧道ID"/>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="外部系统隧道方向" prop="externalSystemTunnelDirection" label-width="130px" >
                <el-input v-model="item.externalSystemTunnelDirection" placeholder="请输入外部系统隧道方向" />
              </el-form-item>
            </el-col>

            <el-col :span="3">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="item.remark" placeholder="请输入备注" />
              </el-form-item>
            </el-col>

            <el-col :span="1">
              <div class="dialogButton" @click="updataDeleteForm(index)" style="margin-left: 15px">删除</div>
            </el-col>
          </el-row>
        </template>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForms">确 定</el-button>
        <el-button @click="cancels">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 添加或修改隧道对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000" append-to-body class="addUserDialog"
      :before-close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="隧道ID" prop="tunnelId">
              <el-input
                v-model="form.tunnelId"
                placeholder="请输入隧道ID"
                id="aaa"
                name="aaa"
                readonly="readonly"
                :disabled="disabledInfo"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="隧道名称" prop="tunnelName">
              <el-input
                v-model="form.tunnelName"
                placeholder="请输入隧道名称"
                :disabled="disabledInfo"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="隧道地址" prop="tunnelAddress">
              <el-input
                v-model="form.tunnelAddress"
                placeholder="请输入隧道地址"
                :disabled="disabledInfo"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路段ID" prop="roadId">
              <el-input v-model="form.roadId" placeholder="请输入路段ID"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="道路编码" prop="roadCode">
              <el-input v-model="form.roadCode" placeholder="请输入道路编码"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="道路名称" prop="roadCode">
              <el-input v-model="form.roadCode" placeholder="请输入道路名称"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="等级编码" prop="levelCode">
              <el-input v-model="form.levelCode" placeholder="请输入等级编码"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="等级名称" prop="levelName">
              <el-input v-model="form.levelName" placeholder="请输入等级名称"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="长度编码" prop="lengthCode">
              <el-input v-model="form.lengthCode" placeholder="请输入长度编码"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="长度名称" prop="lengthName">
              <el-input v-model="form.lengthName" placeholder="请输入长度名称"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="修建年度" prop="constructYear">
              <el-input v-model="form.constructYear" placeholder="请输入修建年度"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建成通车日期" prop="activateTime">
              <el-input v-model="form.activateTime" placeholder="请输入建成通车日期"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管养单位ID" prop="managerId">
              <el-input v-model="form.managerId" placeholder="请输入管养单位ID"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区划编码" prop="orgCode">
              <el-input v-model="form.orgCode" placeholder="请输入区划编码"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区划名称" prop="orgName">
              <el-input v-model="form.orgName" placeholder="请输入区划名称"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度"
              :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!--      <el-form-item label="车道数量" prop="lane">
                    <el-input v-model="form.lane"  placeholder="请输入车道数量"  @keyup.native="number"/>
                  </el-form-item> -->
            <el-form-item label="车道数目" prop="lane">
              <el-select v-model="form.lane" placeholder="请选择车道数目" style="width:100%"
              :disabled="disabledInfo">
                <el-option label="1车道" value="1"></el-option>
                <el-option label="2车道" value="2"></el-option>
                <el-option label="3车道" value="3"></el-option>
                <el-option label="4车道" value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="隧道长度" prop="tunnelLength">
              <el-col :span="24">
                <el-input
                  v-model="form.tunnelLength"
                  placeholder="请输入隧道长度"
                  :disabled="disabledInfo"
                />
              </el-col>
              <el-col :span="2">
                <!-- <p>米</p> -->
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="三维坐标" prop="coordinates">
              <el-input
                v-model="form.coordinates"
                placeholder="请输入三维坐标"
                :disabled="disabledInfo"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="开始桩号" prop="startPile">
              <el-col :span="24">
                <el-input
                  v-model="form.startPile"
                  @blur="setPileInt('start')"
                  placeholder="请输入开始桩号"
                  :disabled="disabledInfo"
                />
              </el-col>
              <el-col :span="2">
                <!-- <p>米</p> -->
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始桩号(整形)" prop="startPileNum">
              <el-input v-model="form.startPileNum" disabled="disabled"  />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="结束桩号" prop="endPile">
              <el-col :span="24">
                <el-input v-model="form.endPile"
                          @blur="setPileInt('end')"
                          placeholder="请输入结束桩号"
                          :disabled="disabledInfo"
                />
              </el-col>
            </el-form-item>
          </el-col>
              <el-col :span="2">
                <!-- <p>米</p> -->
              </el-col>
          <el-col :span="12">
            <el-form-item label="结束桩号(整形)" prop="endPileNum">
              <el-input v-model="form.endPileNum" disabled="disabled"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <treeselect
                v-model="form.deptId"
                :options="deptOptions"
                :show-count="true"
                placeholder="请选择归属部门"
                :disabled="disabledInfo"
              />

              <!--<el-select
                v-model="form.deptId"
                placeholder="请选择所属部门"
                clearable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="item in deptsData"
                  :key="item.deptId"
                  :label="item.deptName"
                  :value="item.deptId"
                />
              </el-select>-->
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="是否启用" prop="poll">
              <el-select v-model="form.poll" placeholder="请选择是否启用" style="width:100%" :disabled="disabledInfo">
                <el-option
                  v-for="dict in pollOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="parseInt(dict.dictValue)"

                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注" prop="remake">
              <el-input v-model="form.remake" placeholder="请输入备注" :disabled="disabledInfo"/>
            </el-form-item>
          </el-col>
          <!--    <el-col :span="12">
                <el-form-item label="存储配置图的html" prop="storeConfigure">
                  <el-input v-model="form.storeConfigure" placeholder="请输入存储配置图的html" />
                </el-form-item>
              </el-col> -->
          <!-- <el-form-item label="隧道所ID" prop="tunnelStationId">
            <el-input v-model="form.tunnelStationId" placeholder="请输入隧道所ID" />
          </el-form-item>
          <el-form-item label="隧道所名称" prop="tunnelStationName">
            <el-input v-model="form.tunnelStationName" placeholder="请输入隧道所名称" />
          </el-form-item> -->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listTunnels1,
  getTunnels,
  delTunnels,
  addTunnels,
  updateTunnels, exportTunnels,
} from "@/api/equipment/tunnel/api.js";
import { listDept,treeselect,treeselectExcYG1 } from "@/api/system/dept";
import { getUserDeptId } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  addAssociation,
  updateAssociation,
  getAssociationsByTunnelId,
  delAssociationByTunnelIds,
  delAssociation,
  updateTunnelAssociations,
} from "@/api/equipment/deviceassociation/association";
import { listSystem } from "@/api/equipment/externalsystem/system";
import {listAllTunnels} from "@/api/equipment/tunnel/api.js";
import {exportDevices} from "@/api/equipment/eqlist/api";
import {exportList} from "@/api/electromechanicalPatrol/taskManage/task";


export default {
  name: "Tunnels",
  dicts: ['sd_direction'],
  components: { Treeselect },
  data() {
    const validateLongitude = (rule, value, callback) => {
      var longreg =
        /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/;
      if (!value) {
        callback();
      } else {
        if (!longreg.test(value)) {
          callback(new Error("经度整数部分为0-180,小数部分为0到15位!"));
        }
        callback();
      }
    };
    const validateLatitude = (rule, value, callback) => {
      var latreg =
        /^(\-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/;
      if (!value) {
        callback();
      } else {
        if (!latreg.test(value)) {
          callback(new Error("纬度整数部分为0-90,小数部分为0到15位!"));
        }
        callback();
      }
    };
    return {

      // 导出遮罩层
      exportLoading: false,
      disabledInfo:false,
      tunnelInfoForm:{},
      openTunnelInfo:false,
      boxShow: false,
      manageStatin:this.$cache.local.get("manageStation"),
      oper: "add", //add 添加  edit修改
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 隧道表格数据
      tunnelsList: [],
      externalSystemData: [],
      // 弹出层标题
      title: "",
      titles: "",
      // 是否显示弹出层
      open: false,
      opens: false,
      // 是否使用字典
      pollOptions: [],
      // 查询参数
      queryParams: {
        searchValue:null,
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        tunnelName: null,
        poll: null,
        deptId: this.userDeptId,
        ids:"",
        /* storeConfigure: null, */
      },
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      deptsData: {},
      userDeptId: null,
      // 表单参数
      form: {},
      forms: {},
      // 表单校验
      rules: {
        tunnelName: [
          { required: true, message: "请填写隧道名称", trigger: "blur" },
        ],
        startPile: [
          { required: true, message: "请填写隧道开始桩号", trigger: "blur" },
        ],
        endPile: [
          { required: true, message: "请填写隧道结束桩号", trigger: "blur" },
        ],
        tunnelAddress: [
          { required: true, message: "请填写隧道地址", trigger: "blur" },
        ],
        lane: [
          { required: true, message: "请选择车道数目", trigger: "change" },
        ],
        tunnelId: [
          { required: true, message: "请填写隧道ID", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "请选择所属部门", trigger: "change" },
        ],
        longitude: [
          { validator: validateLongitude, trigger: "blur", required: false },
          { validator: validateLongitude, trigger: "change", required: false },
        ],
        latitude: [
          { validator: validateLatitude, trigger: "blur" },
          { validator: validateLatitude, trigger: "change" },
        ],
        poll: [
          { required: true, message: "请选择是否可用", trigger: "change" },
        ],
      },
      rule: {
        tunnelId: [
          { required: true, message: "请填写隧道ID", trigger: "blur" },
        ],
      },
      // selectedTunnel:{},
      // 隧道列表
      tunnelData: [],
      // 部门树选项
      deptOptions: undefined,
      tunnelAssociations: [],
      tunnelList: []

    };
  },
  created() {
    this.getTreeselect();
    // this.getList();
    this.getTunnel();
    this.getDicts("sys_tunnel_use").then((response) => {
      this.pollOptions = response.data;
    });
    this.getDepts();
    this.getUserDept();
    this.getExternalsystem();
    this.getTunnelList()
  },
  mounted() {
    document.addEventListener("click", this.bodyCloseMenus);
    if (window.history && window.history.pushState) {
      // 向历史记录中插入了当前页
      history.pushState(null, null, document.URL);
      window.addEventListener("popstate", this.goBack, false);
    }
  },
  destroyed() {
    window.removeEventListener("popstate", this.goBack, false);
  },
  methods: {
    //翻页时不刷新序号
    indexMethod(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    tunnelInfo(item){
      console.log(item,"item")
      this.open = true
      // this.tunnelInfoForm = item
    },
    bodyCloseMenus(e) {
      let self = this;
      if (!this.$refs.main.contains(e.target) && !this.$refs.cc.contains(e.target)) {
        if (self.boxShow == true){
          self.boxShow = false;
        }
      }
    },
    getTunnelList() {
      listAllTunnels().then((response) => {
        this.tunnelList = response.data;
        console.log("tunnelList>>>>>>>",this.tunnelList);
      });
    },
    addFrom() {
      let item = {
        id: null,
        tunnelId: this.forms.tunnelId,
        tunnelDirection: null,
        externalSystemId: null,
        externalSystemTunnelId: null,
        externalSystemTunnelDirection: null,
        remark: null
      }
      this.tunnelAssociations.push(item);
    },
    updataDeleteForm(index) {
      let rowid = this.tunnelAssociations[index].id
      if (rowid) {
        delAssociation(rowid).then(res => {
          if (res.code = 200) {
            this.$modal.msgSuccess("删除成功");
          }
        })
      }
      this.tunnelAssociations.splice(index, 1)
    },


    getExternalsystem() {
      listSystem(this.queryParams).then(response => {
        this.externalSystemData = response.rows;
      });
    },
    getTreeselect() {
      treeselectExcYG1().then((response) => {
        this.deptOptions = response.data;
        console.log(this.deptOptions);
      });
    },
    setPileInt(param){
      if(param=='start'){
        let startPile = this.form.startPile;
        if (startPile == null) {
          return;
        }
        //var reg = startPile.replace(/[\u4e00-\u9fa5]/g, "");
        let pileInt = startPile.replace(/[^\u4e00-\u9fa50-9]/g, '')
        this.form.startPileNum = pileInt;
      }else{
        let endPile = this.form.endPile;
        if (endPile == null) {
          return;
        }
        let pileInt = endPile.replace(/[^\u4e00-\u9fa50-9]/g, '')
        this.form.endPileNum = pileInt;
      }
    },
    getTunnel() {
      listTunnels1().then((response) => {
        this.tunnelData = response.rows;
      });
    },
    getDepts() {
      listDept().then((response) => {
        this.deptsData = response.data;
      });
    },
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        this.userDeptId = response.rows[0].deptId;
        this.queryParams.deptId = response.rows[0].deptId;
        this.getList();
      });
    },
    number() {
      this.form.lane = this.form.lane.replace(/[^\.\d]/g, "");
      this.form.lane = this.form.lane.replace(".", "");
    },
    /* 进入配置界面*/
    tunnelConfig(row) {
      const tunnelId = row.tunnelId || this.ids;
      const selectedTunnel = {
        id: row.tunnelId,
        name: row.tunnelName,
        lane: row.lane,
      };
      // this.$router.push({path: '/tunnelConfig', query: {"tunnel": selectedTunnel}})
      this.$router.push({
        path: "/tunnelConfig",
        query: { id: row.tunnelId, name: row.tunnelName, lane: row.lane },
      });
    },
    /** 查询隧道列表 */
    getList() {
      this.loading = true;
      if(this.manageStatin == '1'){
          this.queryParams.tunnelId = this.$cache.local.get("manageStationSelect")
        }
      listTunnels1(this.queryParams).then((response) => {
        console.log(response,"隧道管理列表")
        this.tunnelsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 是否使用字典翻译
    pollFormat(row, column) {
      return this.selectDictLabel(this.pollOptions, row.poll);
    },
    // 取消按钮
    cancels() {
      this.opens = false;
      this.resets();
    },
    cancel() {
      this.open = false;
      this.disabledInfo = false
      this.reset();
    },
    // 表单重置
    resets() {
      this.forms = {
        id: null,
        tunnelId: null,
        tunnelDirection: null,
        externalSystemId: null,
        externalSystemTunnelId: null,
        externalSystemTunnelDirection: null,
        remark: null
      };
      this.resetForm("forms");
    },
    reset() {
      this.form = {
        tunnelId: null,
        tunnelName: null,
        /*      tunnelCode: null, */
        tunnelAddress: null,
        longitude: null,
        latitude: null,
        tunnelStationId: null,
        tunnelStationName: null,
        tunnelLength: null,
        lane: null,
        coordinates: null,
        remake: null,
        poll: null,
        storeConfigure: null,
        createBy: null,
        createTime: null,
        endPile: null,
        startPile: null,
        endPileNum: null,
        startPileNum: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.ids = [];
      this.queryParams.searchValue = '';
      this.queryParams.ids = '';
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.tunnelId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加隧道";
      this.$nextTick(() => {
        document.getElementById("aaa").removeAttribute("readOnly");
      });
    },
    /* 进入隧道关联关系配置 */
    tunnelAssociationConfig(row) {
      const tunnelId = row.tunnelId || this.ids;
      console.log("开启隧道关系配置弹窗");
      this.resets();
      if (tunnelId != null) {
        getAssociationsByTunnelId(tunnelId).then(response => {
          if (response.data.length == 0) {
            let item = {
              id: null,
              tunnelId: tunnelId,
              tunnelDirection: null,
              externalSystemId: null,
              externalSystemTunnelId: null,
              externalSystemTunnelDirection: null,
              remark: null
            }
            this.tunnelAssociations = []
            this.tunnelAssociations.push(item);
          } else {
            this.tunnelAssociations = response.data;
          }
          this.forms.tunnelId = tunnelId
          this.opens = true;
          this.titles = "修改隧道关联关系";
        });
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row,type) {
      this.reset();
      // this.$nextTick(() => {
      //   document.getElementById("aaa").setAttribute("readOnly", true);
      // });
      this.oper = "edit";
      const tunnelId = row.tunnelId || this.ids;
      this.selectedTunnel = {
        id: row.tunnelId,
        name: row.tunnelName,
      };
      getTunnels(tunnelId).then((response) => {
        console.log(response,"修改隧道")
        this.form = response.data;
        this.open = true;
        if(type == 'info'){
          this.title = "隧道详情";
          this.disabledInfo = true
        }else{
          this.title = "修改隧道";
        }
      });
    },
    submitForms() {
      this.$refs["forms"].validate(valid => {
        if (valid) {
          if (this.tunnelAssociations.length>0) {
            console.log("》》tunnelAssociations》》》",this.tunnelAssociations)
            updateTunnelAssociations(this.tunnelAssociations).then(response => {
              if(response.code==200){
                this.$modal.msgSuccess("修改成功");
                this.opens = false;
                this.getList();
              }
            });
          }else{
            this.opens = false;
          }
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (
            !new RegExp("^[1-9][0-9]*$").test(this.form.startPileNum) ||
            !new RegExp("^[1-9][0-9]*$").test(this.form.endPileNum)
          ) {
            this.$modal.msgWarning("桩号格式输入有误！");
            return;
          }
          if (this.oper == "edit") {
            updateTunnels(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.oper = "add";
                this.getList();
              }
            });
          } else {
            addTunnels(this.form).then((response) => {
              if (response.code === 200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.oper = "add";
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this
      const tunnelIds = row.tunnelId || this.ids;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delTunnels(tunnelIds);
        })
        .then(() => {
          delAssociationByTunnelIds(tunnelIds);
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(function (e) {
          that.getList()
        });
    },

/*导出*/
    handleExport() {
      this.queryParams.ids = this.ids.join();
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出隧道管理数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportTunnels(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
  watch: {
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "0000000000000000000000");
      this.getList();
    }
  }
};
</script>
<style scoped>
.app-main {
  height: calc(100% - 105px) !important;
}
::v-deep .el-table::before {
  width: 0px !important;
}


.dialogButton{
  width: 50px;
  height: 24px;
  border: solid 1px #ccc;
  border-radius: 6px;
  font-size: 12px;
  margin-top: 5px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}
.addFormButton{
  position: absolute;
  top: 55px;
  left: 23%;

}
</style>

<style lang="scss" scoped>
.tunnelInfoDialog {
  .el-col{
    display: flex;
    height: 40px;
    align-items: center;
    >div:first-of-type{
      width: 100px;
      color: #01AAFD;
    }
    >div{
      line-height: 40px;
    }
  }
}
::v-deep .el-dialog__body{
    max-height: 70vh;
    overflow: auto;
  }
</style>
