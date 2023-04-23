<template>
  <div class="app-container">
    <div
      class="my-back"
      :style="{ height: 'calc(100vh - (' + navigationHeight + 'px))' }"
    >
      <div class="header workbench-header">
        <el-row
          class="menu-b"
          style="display: flex; align-items: center;z-index:8"
          :style="isManagementStation ? 'padding-left:100px;' : ''"
        >
          <el-cascader
            v-model="tunnelQueryParams.deptId"
            :options="siteList"
            :props="siteProps"
            :show-all-levels="false"
            @change="changeSite"
            placeholder="请选择"
            size="mini"
            :class="manageStation == '1' ? 'siteClassDisabled' : 'siteClass'"
            popper-class="popper-class-site"
            v-show="!isManagementStation"
          />
          <el-button-group
            class="menu-button-group"
            style="margin-left: 3px"
            :style="isManagementStation ? 'display: flex;' : ''"
          >
            <el-tooltip
              class="item"
              popper-class="wb-tip"
              v-for="(item, index) in tunnelList"
              :key="item.tunnelId"
              effect="dark"
              :content="item.tunnelLength"
              placement="top-start"
            >
              <el-button
                type="info"
                size="mini"
                @click="setTunnel(item, index)"
                :class="index == buttonIndex ? 'tunnelBtnStyle' : ''"
                style="
                  display: flex;
                  justify-content: center;
                  height: 32px;
                  line-height: 20px;
                  font-size: 15px;
                "
              >
                <div>{{ item.tunnelName }}</div>
              </el-button>
            </el-tooltip>
          </el-button-group>
        </el-row>

        <div class="flex-row" style="z-index: 8">
          <div class="display-box zoomClass">
            <p class="zoom-title" style="font-size: 14px; margin-right: 10px">
              {{ carShow ? "实时车辆关" : "实时车辆开" }}
            </p>
            <el-switch
              v-model="carShow"
              class="switchStyle"
              @change="carShowChange"
            ></el-switch>
          </div>
          <div class="display-box zoomClass" ref="treeBox">
            <!-- <div class="display-box">
              <p class="zoom-title" style="font-size: 14px">
                {{ carShow ? "实时车辆开" : "实时车辆关" }}
              </p>
              <el-switch
                v-model="carShow"
                class="carShow"
                @change="carShowChange"
              ></el-switch>
            </div> -->
            <el-input
              placeholder="请输入内容"
              v-model="screenEqName"
              class="input-with-select"
              clearable
              @click.native="treeClick()"
              @keyup.enter.native="screenEqNameButton(screenEqName)"
              @clear="treeClear"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="screenEqNameButton(screenEqName)"
              ></el-button>
            </el-input>
            <!-- 搜索栏树状结构 -->
            <div class="treeBox" ref="treeBox" v-show="treeShow" :style="dragFlag?'47%':'54.5%'">
              <el-tree
                :show-checkbox="false"
                :data="treeData"
                :props="defaultProps"
                @node-click="handleNodeClick"
                accordion
                ref="tree"
              ></el-tree>
            </div>
          </div>
          <div class="display-box zoomClass">
            <p class="zoom-title" style="font-size: 14px">缩放：</p>
            <el-input-number
              v-model="zoom"
              :step="10"
              :min="60"
              :max="140"
              step-strictly
              @change="zoomChange"
            >
              {{ zoom + "%" }}
            </el-input-number>
          </div>
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px">
              {{ displayNumb == 0 ? "桩号开" : "桩号关" }}
            </p>
            <el-switch v-model="displayNumb" class="switchStyle"></el-switch>
          </div> -->
          <div class="display-box zoomClass">
            <p class="zoom-title" style="font-size: 14px">
              {{ zoomSwitch == 0 ? "缩放开" : "缩放关" }}
            </p>
            <el-switch
              v-model="zoomSwitch"
              class="switchStyle"
              @change="zoomSwitchChange"
            ></el-switch>
          </div>
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px;">{{displayThumbnail == 0 ? '缩略图开' : '缩略图关'}}</p>
            <el-switch v-model="displayThumbnail" active-color="#6ea2ec" inactive-color="#8a8c8e" @change="changeThumbnail"></el-switch>
          </div> -->
          <!-- <div class="display-box">
            <p class="zoom-title" style="font-size: 14px;">统计图表</p>
            <el-switch v-model="displayThumbnail" @change="changeThumbnail"></el-switch>
          </div> -->
          <el-button
              v-if="resetCanvasFlag"
              class="flex-row"
              type="primary"
              size="mini"
              icon="el-icon-position"
              @click="resetCanvas"
          >
            地图复位
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-s-operation"
            @click="strategyPage"
          >
            控制策略
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-files"
            @click="batchManage"
            v-if="batchManageType == 1"
          >
            批量操作
          </el-button>
          <div v-if="batchManageType == 2" class="batchManageButton">
            <div @click="closeBatchManageDialog">取消</div>
            <div @click="implementBatchManage">执行</div>
          </div>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-top"
            @click="iconExplain"
          >
            图标含义
          </el-button>
          <el-button
            class="flex-row"
            type="primary"
            size="mini"
            icon="el-icon-tickets"
            @click="operationLogPage"
          >
            操作日志
          </el-button>

        </div>
      </div>
      <div class="vehicleLane">
        <div
          class="content"
          ref="divRoller"
          @wheel.prevent="handleTableWheel"
          @contextmenu.prevent
          style="position: relative; left: 2%"
        >
          <!-- :class="topNav?'contentTopNav':'contentLeftNav'" -->
          <!-- <div class="tunnelBox" :style="{ width: currentTunnel.lane.width + 80 + 'px' }" style="border: solid 1px yellow;"> -->

          <div
            class="workbench-content"
            @mousedown="dragImg"
            ref="dragImgDom"
            @contextmenu.prevent
          >
            <!--画布区域-->
            <div>
              <el-row
                class="config-img-area"
                id="config-img-id"
                :style="{ width: currentTunnel.lane.width + 'px' }"
              >
                <el-image
                  class="back-img"
                  :src="currentTunnel.lane.url"
                  :style="{ width: currentTunnel.lane.width + 'px' }"
                ></el-image>
<!--                <div class="carBox" v-show="carShow">-->
<!--                  <span-->
<!--                    v-for="(value, key) in carList"-->
<!--                    :key="key"-->
<!--                    :style="{-->
<!--                      left: value.left,-->
<!--                      top: value.top,-->
<!--                      background: value.background,-->
<!--                    }"-->
<!--                  ></span>-->
<!--                </div>-->
                <div class="carBox" v-if="carShow"  v-for="(value, key) in carList">
                  <div
                    id="carShowSpan"
                    v-for="data in value"
                    :key="key1"
                    :style="{
                      right: data.right,
                      left: data.left,
                      top: data.top,
                    }"
                  ><p :style="{color: data.background}"> {{ data.vehicleLicense }}</p></div>
                </div>
                <div class="accident" v-if="carShow"  v-for="(value, key) in carList">
<!--                  <div-->
<!--                    id="carShowSpan"-->
<!--                    v-for="data in value"-->
<!--                    :key="key1"-->
<!--                    :style="{-->
<!--                      right: data.right,-->
<!--                      left: data.left,-->
<!--                      top: data.top,-->
<!--                    }"-->
<!--                  ><p :style="{color: data.background}"> {{ data.vehicleLicense }}</p></div>-->
                  <svg t="1682220360907" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2449"><path d="M363.3 318.7c1.2 3.7 48.7 146.2-89.4 186.5 143.2 27.9 92.6 179.5 80.3 192.4 26.4-11.2 128.9-97.2 206.6 65.5 21.8-128 118.1-89.1 120.5-88.2-1.1-2.4-43.6-94.4 83.6-159.8-111.4-21.5-88.7-144.6-61.5-166-148.4 49.2-130.9-74.5-130.9-74.5s-111.3 205.3-209.2 44.1z m480.1 196.4s-200.9 64.6-53.1 233.1c-218.3-52.5-226.1 90.7-226.1 90.7s-87.5-206-272.1-33.7c67.9-180.8-8.4-249.5-67-274.4-28.1-12-54.2-13.2-54.2-13.2s22.2-4.8 52.7-22.1c56.4-32 133.4-104.6 98.9-253.9 0 0 125.7 193.1 261.1-45.4 9.7 136.1 86.8 136.7 166.5 124.4-105.7 142.5 93.3 194.5 93.3 194.5z" fill="#FF2B3F" p-id="2450"></path><path d="M436.1 442.3c7.4 22.1-0.5 58-36.5 88.2 55.1 14.9 44.7 88.1 44.7 88.1 90.1-14.9 110.2 42.1 110.2 42.1 5.8-35.6 54.4-41.4 89.4-38.9-5.2-11-22.7-71.9 27.8-118.6-79.9-8.6-99.7-123.7-99.7-123.7-50.5 82.1-134.1 62.2-134.1 62.2l-1.8 0.6z m267.3-93.2c-27.2 21.4-49.9 144.4 61.5 166-127.2 65.4-84.7 157.5-83.6 159.8-2.3-0.9-98.7-39.8-120.5 88.2-77.7-162.6-180.2-76.7-206.6-65.5 12.3-12.9 62.8-164.5-80.3-192.4 138.1-40.3 90.7-182.8 89.4-186.5 97.8 161.2 209.2-44.1 209.2-44.1S555 398.3 703.4 349.1z" fill="#FFEB00" p-id="2451"></path><path d="M671.7 503.3c-50.5 46.6-33 107.6-27.8 118.6-35-2.6-83.6 3.3-89.4 38.9 0 0-20.1-57-110.2-42.1 0 0 10.4-73.2-44.7-88.1 36-30.2 43.9-66.1 36.5-88.2l1.7-0.6s83.6 19.9 134.1-62.2c0-0.1 19.8 115.1 99.8 123.7z" fill="#FFFFFF" p-id="2452"></path><path d="M568.7 865.1l-10.1-23.8c-0.3-0.8-35.5-81.6-105.6-96.4-47.3-10-100.1 11.8-156.9 64.8L277.4 827l9-23.9c29.5-78.6 34.2-142.9 14-191.3-17.8-42.7-52.1-64.6-77.7-75.5-26.7-11.4-51.9-12.7-52.1-12.7l-45.7-2.2 44.7-9.6c0.2 0 21.8-4.9 51-21.4 24.8-14.1 59.1-39.5 81.2-80.6 24.7-46 29.7-102.1 14.8-166.8l-7.6-33.1 18.5 28.4c0.5 0.7 48.4 73.1 114.5 71 47.6-1.5 93.5-40.5 136.4-116.1l9.8-17.2 1.4 19.8c3.7 51.4 17.1 85.6 41 104.7 30.1 24 74.8 21 118.6 14.2l14.4-2.2-8.7 11.7c-27.2 36.8-35.8 70.3-25.5 99.7 21.2 60.2 114.5 85.2 115.4 85.4l19.9 5.2-19.6 6.3c-0.8 0.3-82.9 27.4-100 89.6-10.8 39 5.9 84 49.6 133.8l12.5 14.2-18.4-4.4c-79.6-19.1-139.5-14.3-178 14.3-38 28.3-40.7 70.5-40.7 70.9l-1.4 25.9zM430.2 730.4c8.6 0 17.1 0.9 25.4 2.7 31.7 6.7 61.4 26.8 86.1 58 8.6 10.9 15.2 21.3 19.8 29.4 4.7-16.8 15.8-42.1 42.2-61.8 38.5-28.7 95.9-35.5 170.6-20.2-37.9-47.8-51.6-91.9-40.7-131.3 7.9-28.5 29.1-54.4 61.4-75.1 11.4-7.3 22.2-12.7 30.5-16.4-9.6-3.4-22.6-8.7-36.3-16-36.8-19.5-61.4-44.3-71.1-71.9-10.6-30.2-4.2-63.5 19.1-99.4-42 5.5-83.8 5.5-114.2-18.7-23.6-18.8-37.9-49.9-43.6-94.8-42.6 69.1-88.6 104.8-137.1 106.3-30.3 0.9-61.5-12.1-90.3-37.6-7.4-6.5-13.7-13.1-18.8-18.9 9.1 57.4 2.2 107.9-20.7 150.5-23.5 43.6-59.7 70.5-85.9 85.3-11.4 6.5-21.6 11.2-30.1 14.7 9.1 2 19.9 5.2 31 9.9 27.7 11.8 64.7 35.5 84.1 82 19.5 46.6 17.4 106.5-6.1 178.2 43.6-36.5 85.5-54.9 124.7-54.9z" fill="#91000A" p-id="2453"></path><path d="M476.1 467.2c-5.4 17.2-14 33.3-19.4 50.5-1.6 5 5.6 7.7 8.2 3.5 11.2-17.5 20.2-36.7 34.3-52.2-2.5-1.5-5-2.9-7.5-4.4-5.8 19.3-20.5 35.9-19.6 56.9 0.2 3.9 4.8 5.9 7.7 3.2 14.5-13.4 23.3-32 33.1-48.8-2.7-1.2-5.5-2.3-8.2-3.5-6.6 19.1-20.3 35.4-23.3 55.7-0.6 3.8 2.9 7 6.6 5.1 10.7-5.6 17.7-16.5 23.6-26.6 6.4-11.1 11.7-23 19.5-33.3l-7.8-4.5c-12.7 25.4-24.9 52.4-25.2 81.3-0.1 5.8 8.9 5.8 9 0 0.3-27.2 12.1-52.9 24-76.8 2.6-5.3-4.3-9.1-7.8-4.5-7.2 9.5-12.2 20.5-18.1 30.9-5.5 9.6-11.6 20.5-21.8 25.8l6.6 5.1c3-20.2 16.8-36.7 23.3-55.7 1.7-4.9-5.7-7.8-8.2-3.5-9.4 16.2-17.7 34.1-31.7 47 2.6 1.1 5.1 2.1 7.7 3.2-0.9-19.8 13.8-36.3 19.3-54.5 1.3-4.2-4.5-7.7-7.5-4.4-14.6 16.2-24.1 35.8-35.7 54 2.7 1.2 5.5 2.3 8.2 3.5 5.4-17.2 14-33.3 19.4-50.5 1.8-5.6-6.9-8-8.7-2.5zM491.6 582.8c11.3-2.3 22.8-1.3 34.2-1.2-0.5-1.9-1-3.7-1.5-5.6-10.8 7.8-24.5 11.7-33 22.5-1.8 2.2-0.2 5.9 2.9 5 10-2.7 19.6-8.6 30.3-7.7-0.9-1.5-1.7-3-2.6-4.5-5 7.2-12.2 12.2-19 17.5-1.7 1.3-0.8 4.7 1.3 5 8.1 1.4 15.9-0.2 23-4.2-1.2-1.6-2.4-3.1-3.6-4.7-5.7 7-11.9 13.5-15 22.1-0.8 2.2 1.6 4.2 3.7 3.7 7.7-1.9 13.9-7 19.5-12.4 2.8-2.7-1.5-6.9-4.2-4.2-4.8 4.7-10.1 9.2-16.8 10.9l3.7 3.7c2.8-7.5 8.5-13.3 13.5-19.4 2.1-2.5-0.5-6.5-3.6-4.7-5.6 3.2-12 4.7-18.4 3.6 0.4 1.7 0.9 3.3 1.3 5 7.3-5.7 14.5-11.1 19.9-18.7 1.4-2-0.5-4.3-2.6-4.5-11.3-1-21.3 5-31.9 7.9 1 1.7 1.9 3.3 2.9 5 8.2-10.3 21.4-14 31.8-21.6 2.3-1.7 1.7-5.6-1.5-5.6-12-0.1-23.9-1-35.8 1.4-3.8 0.7-2.2 6.4 1.5 5.7zM590.7 512.9c8.6 1.2 17.3 2.6 25.9 3.9-0.7-2.5-1.3-5-2-7.5-7.8 7.4-18.8 10.1-27.6 16.1-4 2.8-1.7 9.7 3.5 8.2 9.2-2.7 18.9-7.2 28.7-5.2 5.6 1.2 8.1-7.5 2.4-8.7-11.7-2.5-22.5 1.9-33.5 5.2 1.2 2.7 2.3 5.5 3.5 8.2 9.5-6.5 20.9-9.5 29.4-17.5 2.2-2.1 1.4-7-2-7.5-8.6-1.2-17.3-2.6-25.9-3.9-5.7-0.7-8.1 7.9-2.4 8.7zM596.6 549.1c6.4-2.4 13.1-3.7 19.9-3.8 3.9-0.1 3.9-6.1 0-6-7.4 0.1-14.7 1.4-21.5 4-3.6 1.4-2 7.2 1.6 5.8z" fill="#91000A" p-id="2454"></path><path d="M607 563.9c0.5-0.5 1.1-1 1.6-1.5 0.2-0.1 0.1-0.1-0.1 0.1 0.1-0.1 0.3-0.2 0.4-0.3 0.3-0.2 0.3-0.1-0.1 0 0.3-0.1 0.3-0.1-0.2 0 1.6-0.1 3.1-1.3 3-3-0.1-1.6-1.3-3.1-3-3-2.5 0.1-4.3 1.8-6 3.5-1.1 1.1-1.2 3.1 0 4.2 1.3 1.1 3.2 1.2 4.4 0z" fill="#6E080B" p-id="2455"></path><path d="M546.6 524.4c1.9 0 1.9-3 0-3-2 0-2 3 0 3z" fill="#7A3E1A" p-id="2456"></path><path d="M571.2 579c3.9 0 3.9-6 0-6s-3.9 6 0 6zM467.5 566c2.5-0.1 4.3-1.8 6-3.5 1.1-1.1 1.2-3.1 0-4.2s-3.1-1.2-4.2 0c-0.5 0.5-1.1 1-1.6 1.5-0.2 0.1-0.1 0.1 0.1-0.1-0.1 0.1-0.3 0.2-0.4 0.3-0.3 0.2-0.3 0.1 0.1 0-0.3 0.1-0.3 0.1 0.1 0-1.6 0.1-3.1 1.3-3 3 0 1.6 1.2 3.1 2.9 3zM555.6 463.7c3.9 0 3.9-6 0-6-3.8 0-3.8 6 0 6zM562.1 507.7c3.9 0 3.9-6 0-6s-3.9 6 0 6zM541.4 490.9h1.3c1.6 0 3.1-1.4 3-3-0.1-1.6-1.3-3-3-3h-1.3c-1.6 0-3.1 1.4-3 3 0 1.6 1.3 3 3 3zM577.7 477.9c3.9 0 3.9-6 0-6s-3.9 6 0 6zM545.7 548.8v-1.3c0-1.6-1.4-3.1-3-3-1.6 0.1-3 1.3-3 3v1.3c0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3zM632.1 611.4c3.9 0 3.9-6 0-6s-3.9 6 0 6zM607.9 657.6v-3.9c0-1.6-1.4-3.1-3-3-1.6 0.1-3 1.3-3 3v3.9c0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3zM578.1 599.3v-0.8c0 0.3-0.1 0.5 0 0.8-0.1-0.1-0.1-0.1 0-0.2-0.1 0.2-0.2 0.5-0.3 0.7 0.1-0.2 0-0.1-0.4 0.5-0.6 0.4-0.7 0.5-0.5 0.4-0.2 0.1-0.5 0.2-0.7 0.3h0.2c-0.3 0-0.5 0.1-0.8 0.1h0.8c1.6 0 3.1-1.4 3-3-0.1-1.6-1.3-3-3-3-1.2 0-2.4 0.2-3.3 1-0.9 0.9-1 2.1-1 3.2 0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3zM512 393.3v-2.6c0-1.6-1.4-3.1-3-3-1.6 0.1-3 1.3-3 3v2.6c0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3z" fill="#6E080B" p-id="2457"></path><path d="M458.4 446.7c-11.7 24.8-22 50.3-33.7 75.2-2.5 5.2 5.3 9.8 7.8 4.5 11.7-24.8 22-50.3 33.7-75.2 2.5-5.1-5.3-9.7-7.8-4.5zM474.2 602.4c16-4.9 32.5-8 48.6-12.6 5.6-1.6 3.2-10.3-2.4-8.7-16.1 4.7-32.6 7.7-48.6 12.6-5.5 1.7-3.1 10.4 2.4 8.7zM493.3 615.7c11.7-4.3 23.7-7.4 35.7-10.3 5.6-1.3 3.2-10-2.4-8.7-12.1 2.9-24.1 5.9-35.7 10.3-5.4 2.1-3.1 10.8 2.4 8.7zM483.1 633c12.9-9.5 30.2-8.3 45.3-10.1 5.7-0.7 5.7-9.7 0-9-16.7 2-35.6 0.9-49.9 11.3-4.6 3.4-0.1 11.2 4.6 7.8z" fill="#91000A" p-id="2458"></path><path d="M401.4 599.7c6.2-0.2 11.9-2.9 15.6-8 2.2-3.1-3-6.1-5.2-3-2.4 3.3-6.4 4.9-10.4 5-3.8 0.1-3.8 6.1 0 6zM403.5 625.5c3.7-1.2 7.5-2.5 11.1-4.2 3.5-1.7 0.4-6.9-3-5.2-3.1 1.5-6.4 2.5-9.6 3.6-3.7 1.3-2.2 7 1.5 5.8zM367.3 495.7c0.1 3.2 1.3 6.3 3.5 8.6 1.9 2 4.9 0.3 5.1-2.1 0 0.3 0.3-0.2 0.4-0.4 0.4-0.5 0.9-1 1.4-1.4 2.7-2.7-1.5-7-4.2-4.2-1.7 1.8-3.2 3.5-3.5 6l5.1-2.1c-1-1-1.7-2.8-1.7-4.4-0.2-3.9-6.2-3.9-6.1 0zM388.3 441.2c8.6 3.9 14.4 11.6 22 16.9 2.4 1.7 4.7-1.1 4.4-3.4-0.3-2.1-2.3-3.3-2.6-5.2-0.6-3.8-6.3-2.2-5.8 1.6 0.3 2.1 2.3 3.3 2.6 5.2 1.5-1.1 2.9-2.3 4.4-3.4-7.7-5.3-13.4-12.9-22-16.9-3.5-1.6-6.6 3.6-3 5.2zM388.3 472.3c4.5 2.8 9.9 3.3 14-0.5 2.9-2.6-1.4-6.8-4.2-4.2-2.1 1.9-4.6 0.8-6.7-0.5-3.4-2-6.4 3.2-3.1 5.2zM577.2 359.6c0 2.6 0.3 5.3 2.2 7.3 1.1 1.2 3.1 1.1 4.2 0 1.2-1.2 1.1-3 0-4.2l-0.2-0.2c0.2 0.2 0.2 0.3 0.1 0.1-0.1-0.2-0.1-0.1 0 0 0.1 0.2 0.1 0.2 0 0s-0.1-0.4-0.2-0.6c0-0.1-0.2-0.8-0.1-0.4s0-0.2 0-0.3v-0.6-1.1c0-1.6-1.4-3-3-3s-3 1.4-3 3zM633.4 414.4c2.3 0 4.3-0.6 6-2.2 1.1-1.1 1.2-3.2 0-4.2-1.2-1.1-3-1.2-4.2 0-0.3 0.2-0.3 0.3 0 0.1-0.1 0.1-0.3 0.2-0.4 0.3 0.6-0.4-0.1 0-0.3 0h0.3-1.3c-1.6 0-3.1 1.4-3 3-0.1 1.7 1.2 3 2.9 3zM689.9 524.5c4.2-1.6 8.5-2.7 12.8-3.8 2.1-0.5 5.9-0.8 7.2-2.8 1.3-2.1-0.5-4.3-2.6-4.5h-0.1c-3.8-0.4-3.8 5.6 0 6h0.1c-0.9-1.5-1.7-3-2.6-4.5 0.6-0.5 1.2-0.9 1.8-1.4-2.2 0.9-4.8 1.2-7.1 1.8-3.8 1-7.5 2-11.1 3.3-3.6 1.4-2 7.2 1.6 5.9zM683.9 544c3.7-0.1 7.3-1.4 10.6-3 3.5-1.7 0.4-6.9-3-5.2-2.3 1.1-5 2.1-7.5 2.2-3.9 0.1-4 6.1-0.1 6zM645.5 644.2c1.7 1.7 4.2 3.1 6.5 1.6 2.1-1.3 2.5-4.1 2.5-6.3 0-1.6-1.4-3.1-3-3-1.6 0.1-3 1.3-3 3v1.1c0 0.6-0.1 0 0-0.1 0 0-0.1 0.3-0.1 0.4 0 0.1-0.1 0.3-0.1 0.4-0.1 0.4-0.3 0.1 0.1-0.2-0.1 0.1-0.1 0.2-0.1 0.2-0.2 0.4 0.5-0.5 0.2-0.3-0.2 0.2 0.6-0.4 0.4-0.3 0.1-0.1 0.8-0.3 1.1-0.3-0.4 0 0.5 0.1 0.5 0.1-0.1 0-0.5-0.2 0 0 0.6 0.3-0.3-0.2-0.3-0.2 0.1 0 0.5 0.4 0.2 0.1l-0.5-0.5c-1.1-1.1-3.1-1.2-4.2 0-1.3 1.2-1.4 3.1-0.2 4.3zM545.3 674.9h1c-0.3 0-0.5-0.1-0.8-0.1 0.2 0 0.3 0.1 0.5 0.1 0.3 0.1-0.8-0.4-0.5-0.2 0.3 0.2-0.6-0.6-0.3-0.3s-0.4-0.7-0.3-0.4c0 0.1 0.1 0.2 0.1 0.2-0.4-0.5-0.3-0.7-0.1-0.3 0 0.1 0.1 0.3 0.1 0.4 0-0.3-0.1-0.5-0.1-0.8 0 0.3 0 0.5 0.1 0.8 0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3 0-1.9-0.6-3.7-2.2-4.8-1-0.7-2.2-0.8-3.4-0.8-1.6 0-3.1 1.4-3 3-0.1 1.8 1.2 3.2 2.9 3.2zM498.2 658.9v1.3c0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3v-1.3c0-1.6-1.4-3.1-3-3-1.6 0.1-3 1.3-3 3zM445.9 408.8v-0.9 0.9c-0.1-0.1-0.1-0.2 0-0.2 0.1-0.3-0.3 0.5-0.2 0.5l0.1-0.1c0.2-0.3-0.5 0.6-0.3 0.3l0.9-0.9c1.1-1.1 1.2-3.1 0-4.2s-3.1-1.2-4.2 0c-0.6 0.6-1.2 1.2-1.6 1.9-0.5 0.9-0.6 1.8-0.6 2.8 0 1.6 1.4 3.1 3 3 1.5-0.1 2.9-1.4 2.9-3.1z" fill="#FFFFFF" p-id="2459"></path><path d="M375.7 722.4c3.2-1.7 5.8-4.4 8.4-6.9 2.7-2.7-1.5-7-4.2-4.2-2.2 2.2-4.4 4.5-7.2 6-3.4 1.7-0.4 6.9 3 5.1zM436.1 699.9c1.4 6.7 5.7 13.6 13.3 13.9 3.9 0.1 3.9-5.9 0-6-4.6-0.2-6.7-5.7-7.5-9.5-0.8-3.8-6.6-2.2-5.8 1.6zM470.6 727.1c0.6 0.6 1.3 1.1 2 1.5 0.1 0.1 0.2 0.1 0.3 0.2 0.3 0.2 0.1 0.3-0.2-0.2l0.1 0.1-0.3-0.3c-0.1-0.2-0.1-0.3-0.2-0.5 0 0.1 0 0.2 0.1 0.2 0-0.3-0.1-0.5-0.1-0.8v0.3c0.1 1.6 1.3 3.1 3 3 1.6-0.1 3.1-1.3 3-3-0.1-1.6-0.8-2.8-2-3.7-0.3-0.2-0.5-0.3-0.8-0.5-0.2-0.1-0.4-0.3-0.5-0.4 0.4 0.3 0.4 0.3 0.2 0.1l-0.3-0.3c-1.1-1.1-3.1-1.2-4.2 0-1.2 1.3-1.3 3.2-0.1 4.3zM685.2 699.5c2.3 0 4.3-0.6 6-2.2 1.1-1.1 1.2-3.2 0-4.2-1.2-1.1-3-1.2-4.2 0-0.3 0.2-0.3 0.3 0 0.1-0.1 0.1-0.3 0.2-0.4 0.3 0.4-0.1 0.4-0.1 0.1 0-0.1 0-0.2 0.1-0.3 0.1h0.3-1.3c-1.6 0-3.1 1.4-3 3-0.1 1.5 1.1 2.9 2.8 2.9zM715.4 666.7v-5.2c0-1.6-1.4-3-3-3s-3 1.4-3 3v5.2c0 1.6 1.4 3 3 3s3-1.4 3-3zM717.6 620.4c3.9 0 3.9-6 0-6s-3.9 6 0 6zM477.5 329.8v1.3c0 1.6 1.4 3.1 3 3 1.6-0.1 3-1.3 3-3v-1.3c0-1.6-1.4-3.1-3-3-1.7 0.1-3 1.3-3 3z" fill="#FFE63C" p-id="2460"></path><path d="M470.1 350.1h-5.2c-1.6 0-3 1.4-3 3s1.4 3 3 3h5.2c1.6 0 3-1.4 3-3s-1.4-3-3-3z" fill="#FFE63C" p-id="2461"></path><path d="M313.8 449.5l-0.4 0.4c-1.1 1.1-1.2 3.1 0 4.2s3 1.2 4.2 0c0.6-0.5 1.1-1.1 1.7-1.7 1.1-1.1 1.2-3.1 0-4.2s-3.1-1.2-4.2 0l-1.7 1.7 4.2 4.2 0.4-0.4c1.1-1.1 1.2-3.1 0-4.2s-3-1.2-4.2 0z" fill="#FFE63C" p-id="2462"></path><path d="M344.4 431.3c3.9 0 3.9-6 0-6-3.8 0-3.8 6 0 6z" fill="#FFE63C" p-id="2463"></path><path d="M662.6 231.9l36.3-36.3c-3.3-1.9-6.7-3.9-10-5.8-5.5 15.8-11 31.6-16.6 47.4-2.1 6 5.9 9.6 10 5.8 13-11.9 25.9-23.8 38.9-35.8-3.1-2.4-6.3-4.8-9.4-7.3-8.2 15.9-16.5 31.8-24.7 47.7-3 5.8 4.5 12.4 9.4 7.3 15.6-16.3 31.2-32.7 46.7-49 5.3-5.6-3.1-14.1-8.5-8.5-15.6 16.3-31.2 32.7-46.7 49 3.1 2.4 6.3 4.8 9.4 7.3 8.2-15.9 16.5-31.8 24.7-47.7 3.1-6-4.3-12-9.4-7.3-13 11.9-25.9 23.8-38.9 35.8 3.3 1.9 6.7 3.9 10 5.8 5.5-15.8 11-31.6 16.6-47.4 2-5.8-6-9.9-10-5.8l-36.3 36.3c-5.5 5.5 3 14 8.5 8.5zM825.2 400.1c-3.6 1.7-7.2 3.3-10.8 5 2 3.5 4 6.9 6.1 10.4 9.9-7 21.7-9.3 33.4-6.1-0.5-3.7-1-7.3-1.4-11-13.2 5.6-25.2 13.4-35.9 22.8-4.6 4 0.3 11.6 5.8 10 12-3.4 23.9-6.7 35.9-10.1-1.9-3.3-3.9-6.7-5.8-10l-28.2 28.8c-4 4.1-0.7 9.6 4.2 10.2 18.6 2.4 37.7-2.7 52.6-14 6.1-4.6 0.1-15-6.1-10.4-13.5 10.2-29.8 14.5-46.6 12.3l4.2 10.2 28.2-28.8c4.2-4.2 0-11.7-5.8-10-12 3.4-23.9 6.7-35.9 10.1 1.9 3.3 3.9 6.7 5.8 10 10-8.8 21.3-15.7 33.5-21 5-2.1 3.1-9.7-1.4-11-14.6-4-30.3-1.5-42.7 7.3-6.3 4.5-1 13.6 6.1 10.4 3.6-1.7 7.2-3.3 10.8-5 7.1-3 1-13.3-6-10.1zM692.2 812.8c5.6 15.7 13.8 30 24.4 42.9 3.6 4.4 9.9 0.4 10.2-4.2 1.6-20.6 0.6-41.2-2.7-61.6-0.5-2.9-3.9-4.7-6.6-4.3-3.2 0.4-4.8 2.9-5.2 5.9-2.1 16.7 3.2 33.1 13.8 46 3 3.7 8.8 1.3 10-2.6 3.2-10.8 4.4-22 3.5-33.3l-10.2 4.2c8.3 9.6 16.7 19.3 25 28.9 4 4.6 11.6-0.3 10-5.8-4.5-16.1-14-29.6-27.2-39.8-2.6-2-6.7-0.4-8.2 2.2-1.8 3.1-0.4 6.2 2.2 8.2 10.6 8.2 18.1 19.8 21.7 32.6 3.3-1.9 6.7-3.9 10-5.8-8.3-9.6-16.7-19.3-25-28.9-3.3-3.8-10.7-0.9-10.2 4.2 0.9 10.3-0.1 20.2-3 30.1 3.3-0.9 6.7-1.8 10-2.6-8.6-10.6-12-24.1-10.3-37.5-3.9 0.5-7.9 1.1-11.8 1.6 3.1 19.3 3.8 38.9 2.2 58.4l10.2-4.2c-9.2-11.2-16.4-23.9-21.3-37.6-2.6-7.4-14.2-4.3-11.5 3zM390.2 846.6c-4.3 10.5-8.5 21-12.8 31.6-2.5 6.3 7.9 10.7 11 4.6 6.6-13.1 14.4-25.6 23.6-37l-10.2-4.2c-1.5 12.1-5 24.1-10.2 35.2-3.2 7 5.9 12.5 10.4 6.1 5.6-8.1 11.2-16.1 16.8-24.2-3.7-1.5-7.3-3.1-11-4.6-1.2 7.7-2.4 15.3-3.6 23-0.6 4 2.6 9.2 7.4 7.4 14.3-5.6 23-22.4 16.8-37.1-2.9-6.9-14-4.4-11.6 3.2 2.6 8.1 5.3 16.2 7.7 24.4 2.2 7.4 13.8 4.2 11.6-3.2-2.4-8.2-5.2-16.3-7.7-24.4-3.9 1.1-7.7 2.1-11.6 3.2 3.4 8.2 0.3 18.9-8.5 22.4l7.4 7.4c1.2-7.7 2.4-15.3 3.6-23 1-6.7-6.9-10.4-11-4.6-5.6 8.1-11.2 16.1-16.8 24.2 3.5 2 6.9 4 10.4 6.1 6.1-13.2 10-26.8 11.8-41.2 0.6-5-7.1-8.1-10.2-4.2-9.9 12.2-18.5 25.3-25.5 39.4 3.7 1.5 7.3 3.1 11 4.6 4.3-10.5 8.5-21 12.8-31.6 2.9-7.5-8.7-10.6-11.6-3.5zM151.9 622.3c17.5-10.2 37.4-14.8 57.6-13.9-1-3.7-2-7.5-3-11.2-13.3 6.5-26.7 13.1-40 19.6-6.7 3.3-1.5 11.8 4.6 11 14.8-2 29.4-4.9 43.8-8.7-1.5-3.7-3.1-7.3-4.6-11l-34.8 16.5c-5.6 2.7-2.6 11.1 3 11.2 13.1 0.1 26.1-3.6 36.6-11.5 6-4.5 0.7-14.4-6.1-10.4-12.7 7.6-25.4 15.2-38 22.8-6.3 3.7-1.8 11.9 4.6 11 11.7-1.6 22.8-5.8 32.5-12.6 6.3-4.4 0.3-14.8-6.1-10.4-9 6.2-18.9 9.8-29.6 11.3 1.5 3.7 3.1 7.3 4.6 11 12.7-7.6 25.4-15.2 38-22.8-2-3.5-4-6.9-6.1-10.4-8.9 6.7-19.3 10-30.5 9.8 1 3.7 2 7.5 3 11.2l34.8-16.5c6.3-3 1.8-12.6-4.6-11-14.4 3.8-29.1 6.7-43.8 8.7 1.5 3.7 3.1 7.3 4.6 11 13.3-6.5 26.7-13.1 40-19.6 5.7-2.8 2.5-10.9-3-11.2-22.2-1-44.4 4.3-63.6 15.5-6.6 4.1-0.5 14.5 6.1 10.6zM160.2 408.9c14.9 9.2 31.7 14.3 49.2 15 5.8 0.2 8.3-8.5 3-11.2-12.6-6.4-23.7-14.9-33.2-25.4l-4.2 10.2c10.5 0.2 20.6 2.3 30.3 6.2 4.2 1.7 8.7-3.3 7.4-7.4-4.6-14.1-15.6-24.9-29.9-29.1-6.9-2-9.9 7.2-4.6 11 8.5 6.1 17 12.3 25.5 18.4 4.6 3.3 9.7-2.2 8.8-6.8-1.2-6.2-2.5-12.4-3.7-18.7-1.5-7.6-13.1-4.4-11.6 3.2 1.2 6.2 2.5 12.4 3.7 18.7 2.9-2.3 5.9-4.5 8.8-6.8-8.5-6.1-17-12.3-25.5-18.4-1.5 3.7-3.1 7.3-4.6 11 10.1 3 18.2 10.6 21.5 20.7l7.4-7.4c-10.7-4.3-22-6.4-33.5-6.6-5.4-0.1-7.6 6.5-4.2 10.2 10.1 11.2 22.2 20.4 35.6 27.2 1-3.7 2-7.5 3-11.2-15.4-0.6-30-5.2-43.1-13.3-6.6-4-12.6 6.4-6.1 10.5zM387 183.3c13.6 6.8 24.4 18.3 29.7 32.6 2 5.3 9.9 6.1 11.6 0 4.7-17.2 6.3-35 4.4-52.7-3.7 1-7.5 2-11.2 3 4 8 9.7 15 16.7 20.5 2.1 1.6 4.9 2.5 7.3 0.9 14.7-9.6 26.6-22.9 34.1-38.9 2-4.2-0.4-8.8-5.2-9-5.7-0.3-10.7 1.8-14.7 5.7-5.6 5.4 2.9 13.8 8.5 8.5 1.9-1.8 3.7-2.3 6.2-2.2-1.7-3-3.5-6-5.2-9-6.7 14.1-16.7 26-29.8 34.6 2.4 0.3 4.8 0.6 7.3 0.9-6.4-4.9-11.3-10.8-14.9-18-2.6-5.1-11.8-2.9-11.2 3 1.8 16.7 0.5 33.3-3.9 49.5h11.6c-6.4-17.2-18.7-31.5-35.2-39.8-6.9-3.4-13 6.9-6.1 10.4z" fill="#91000A" p-id="2464"></path></svg>
                </div>
                <div
                  class="wrapper"
                  id="eq-wrapper"
                  @mouseover="mouseoversImage"
                  @mouseleave="mouseleaveImage"
                >
                  <!-- <div
                  class="wrapper"
                  id="eq-wrapper"
                  @mousedown="dian"
                  @mousemove="yi"
                  @mouseup="li"
                  @mouseover="mouseoversImage"
                  @mouseleave="mouseleaveImage"
                > -->
                  <!-- 鼠标移动时产生的蓝框 -->
                  <div
                    id="container"
                    v-if="move == true"
                    :style="{
                      backgroundColor: back,
                      height: h + 'px',
                      width: w + 'px',
                      position: 'absolute',
                      left: left - 48 + 'px',
                      top: top - 30 + 'px',
                      opacity: 0.5,
                      border: len + 'px dashed #176CBF',
                      'z-index': '1000',
                      'pointer-events': 'none',
                    }"
                  ></div>
                  <template>
                    <div
                      style="
                        width: 80%;
                        height: 15px;
                        border-bottom: 4px #ccc solid;
                        position: relative;
                        top: 242px;
                        left: 166px;
                      "
                      v-show="robotShow"
                      @click="clickRobot"
                    >
                      <img
                        src="../../../assets/logo/equipment_log/机器人-在线.png"
                        class="robotAnimation"
                      />
                    </div>
                  </template>
                  <!-- 设备图标-->
                  <div
                    class="icon-box mouseHover"
                    v-for="(item, index) in selectedIconList"
                    :key="item.eqId"
                    :style="{
                      left: item.position.left + 'px',
                      top: item.position.top + 'px',
                      'z-index': item.eqType || item.eqType == 0 ? '' : '-1',
                    }"
                    :class="
                      item.eqType == 7 || item.eqType == 8 || item.eqType == 9
                        ? 'light-' + item.position.left
                        : ''
                    "
                    @click="openStateSwitch(item)"
                    @mousemove="openTooltip(item, index)"
                    @mouseleave="closeTooltip(item)"
                  >
                    <!-- 设备图标上提示文字 -->
                    <div
                      v-if="item.eqName == screenEqName"
                      class="screenEqNameBox"
                    >
                      {{ screenEqName }}
                    </div>
                    <div v-if="item.textFalse" class="textFalseBox">
                      请选择同种设备
                    </div>
                    <div v-if="item.textKKFalse" class="textFalseBox">
                      请选择可控设备
                    </div>
                    <!-- <div class="tooltip" v-if="showTooltipIndex == index && showTooltip">{{ sensorContent(item) }}</div> -->

                    <el-tooltip
                      effect="dark"
                      placement="right"
                      :title="item.pile"
                      :disabled="sensorDisabledTwo(item)"
                      style="position: relative; top: 0px; left: 0px"
                      popper-class="tipCase"
                    >
                      <!-- :content="sensorContent(item)" -->

                      <!-- 巡检机器人 -->

                      <div
                      v-show="
                          (item.eqType != 7 &&
                            item.eqType != 15 &&
                            item.eqType != 8 &&
                            item.eqType != 9 &&
                            item.display == true) ||
                          ((item.eqType == 7 ||
                            item.eqType == 8 ||
                            item.eqType == 9 ||
                            item.eqType == 21) &&
                            item.display == true &&
                            lightSwitch == 1)"
                        :class="{ focus: item.focus }"
                      >
                        <img
                          v-show="item.eqType != '31'"
                          v-for="(url, indexs) in item.url"
                          style="position: absolute"
                          :style="{
                            left: indexs * 14 + 'px',
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            transform:
                              item.eqType == 23 && item.eqDirection == 2
                                ? 'scale(-1,1)'
                                : '',
                          }"
                          :width="item.iconWidth"
                          :height="item.iconHeight"
                          :key="item.eqId + indexs"
                          :src="url"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        />
                        <img
                          v-show="item.eqType == '31'"
                          style="position: absolute"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.iconWidth + 'px',
                            height: item.iconHeight + 'px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        />
                        <div
                          v-show="item.eqType == 16"
                          class="boardBox1"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'width',
                                  item.eqType
                                ) + 'px'
                              : item.iconWidth + 'px',
                            height: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'height',
                                  item.eqType
                                ) + 'px'
                              : item.iconHeight + 'px',
                            fontSize: item.associated_device_id
                              ? getBoardStyle(
                                  item.associated_device_id,
                                  'fontSize',
                                  item.eqType
                                ) + 'px'
                              : '15px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        >
                          <div
                            :style="{
                              animation:
                                'boardBox1 ' +
                                Number(getBoardStyle(
                                  item.associated_device_id,
                                  'content'
                                ).length)*1.3 +
                                's' +
                                ' linear infinite',
                            }"
                          >
                            <span
                              v-for="itm in getBoardStyle(
                                item.associated_device_id,
                                'array'
                              )"
                              :key="itm.associated_device_id"
                              :style="{
                                color: getColorStyle(itm.COLOR),
                              }"
                              style="padding-top: 10px"
                              >{{ itm.CONTENT }}</span
                            >
                          </div>
                        </div>
                        <div
                          v-show="item.eqType == 36"
                          class="boardBox2"
                          :style="{
                            cursor:
                              item.eqType || item.eqType == 0 ? 'pointer' : '',
                            border:
                              item.click == true ? 'solid 2px #09C3FC' : '',
                            width:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'width',
                                    item.eqType
                                  ) + 'px'
                                : item.iconWidth + 'px',
                            height:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'height',
                                    item.eqType
                                  ) + 'px'
                                : item.iconHeight + 'px',
                            fontSize:
                              item.associated_device_id != undefined
                                ? getBoardStyle(
                                    item.associated_device_id,
                                    'fontSize',
                                    item.eqType
                                  ) + 'px'
                                : '15px',
                          }"
                          :src="getTypePic(item)"
                          :class="
                            item.eqName == screenEqName
                              ? 'screenEqNameClass'
                              : ''
                          "
                        >
                          <div
                            :style="{
                              animation:
                                'boardBox2 ' +
                                Number(getBoardStyle(
                                  item.associated_device_id,
                                  'content'
                                ).length)*1.3 +
                                's' +
                                ' linear infinite',
                            }"
                          >
                            <span
                              v-for="itm in getBoardStyle(
                                item.associated_device_id,
                                'array'
                              )"
                              :key="itm.associated_device_id"
                              :style="{
                                color: getColorStyle(itm.COLOR),
                              }"
                              style="padding-top: 10px"
                              >{{ itm.CONTENT }}</span
                            >
                          </div>
                        </div>
                        <!-- 调光数值 -->
                        <label
                          style="
                            color: yellow;
                            position: absolute;
                            left: 30px;
                            bottom: 2px;
                            pointer-events: none;
                          "
                          v-if="item.eqType == 21"
                          >{{ item.lightValue }}</label
                        >
                        <!-- CO/VI -->
                        <label
                          style="color: #79e0a9"
                          class="labelClass"
                          v-if="item.eqType == 19"
                        >
                          {{ item.num }}
                          <!-- <label v-if="item.eqType == 19" style="font-size:14px;">ppm</label> -->
                          <!-- <label v-if="item.eqType == 15" style="font-size:14px;">x10-3m<sup>-1</sup></label>-->
                        </label>
                        <!-- 风速风向 -->
                        <label
                          style="color: #79e0a9"
                          class="labelClass"
                          v-if="item.eqType == 17"
                        >
                          {{ item.num }}
                          <!-- <label v-if="item.eqType == 16" style="font-size:14px;">m/s</label> -->
                        </label>
                        <!-- 洞内洞外 -->
                        <label
                          style="color: #f2a520"
                          class="labelClass"
                          v-if="item.eqType == 5 || item.eqType == 18"
                        >
                          {{ item.num }}
                        </label>
                      </div>
                    </el-tooltip>
                    <!-- 桩号 -->
                    <input
                      :class="[
                        item.eqType == 7 ||
                        item.eqType == 8 ||
                        item.eqType == 9 ||
                        item.eqType == 21
                          ? 's-config-img-input'
                          : 'config-img-input',
                      ]"
                      v-if="
                        (item.display == true &&
                          displayNumb == true &&
                          item.eqType != 7 &&
                          item.eqType != 8 &&
                          item.eqType != 9 &&
                          item.eqType != 21) ||
                        ((item.eqType == 7 ||
                          item.eqType == 8 ||
                          item.eqType == 9 ||
                          item.eqType == 21) &&
                          item.display == true &&
                          lightSwitch == 1 &&
                          displayNumb == true)
                      "
                      v-show="item.eqType || item.eqType == 0"
                      type="text"
                      v-model="item.pile"
                      disabled="true"
                      style="color: #055270; margin-left: -20px"
                    />
                    <div v-else style="width: 80px"></div>
                  </div>
                </div>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <div class="siblings">
        <div class="eqTypeListClass">
          <div
            type="info"
            size="small"
            v-for="(item, index) in dictList"
            :key="item.raw.dictCode"
            :label="item.label"
            :value="index"
            @click="displayControl(index, item.label)"
            class="leftButtonS"
            :style="topNav ? 'width:100px' : 'width:100px'"
          >
            <div>{{ item.label }}</div>
          </div>
        </div>

        <!-- 右侧侧窗 -->
        <div
          style="
            position: absolute;
            display: flex;
            justify-content: space-between;
            flex-direction: column;
            height: 100%;
            z-index:8;
          "
          :class="topNav ? 'topNavRightDeawer' : 'leftNavRightDeawer'"
        >
          <div class="indicatorLight" @click="isDrawerA()">
            <i
              :class="[drawerA ? 'el-icon-caret-right' : 'el-icon-caret-left']"
            ></i
            >一键控制模块
          </div>
          <!-- 定时控制模块 -->
          <div class="brightnessControl" @click="isDrawerB()">
            <i
              :class="[drawerB ? 'el-icon-caret-right' : 'el-icon-caret-left']"
            ></i
            >分时控制模块
          </div>
          <div class="triggerControl" @click="isDrawerC()">
            <i
              :class="[
                drawerCVisible ? 'el-icon-caret-right' : 'el-icon-caret-left',
              ]"
            ></i
            >触发控制模块
          </div>
        </div>

        <!-- 一键车道控制模块 -->
        <el-drawer
          title="一键控制"
          :visible.sync="drawerA"
          :modal="false"
          :append-to-body="true"
          class="drawerTop"
        >
          <div style="width: 100%; height: 100%; position: relative">
            <div class="chezhiDrawerDirection">
              {{ directionList[0].dictLabel }}-车道指示器
            </div>
            <div class="chezhiDrawerInfo">
              <div class="chezhiName">车道:</div>
              <el-select
                v-model="chezhiForm1.lane"
                size="small"
                multiple
                collapse-tags
                class="chezhiLaneSelect"
              >
                <el-option
                  v-for="item in chezhiLaneList"
                  :key="item.laneId"
                  :label="item.laneName"
                  :value="item.laneId"
                />
              </el-select>
              <div class="chezhiName">状态:</div>
              <el-select
                v-model="chezhiForm1.state"
                size="small"
                class="chezhiStateSelect"
              >
                <el-option
                  v-for="item in chezhiStateList"
                  :key="item.Id"
                  :value="item.deviceState"
                  :label="item.stateName"
                >
                  <div style="display: flex; align-items: center">
                    <el-image
                      :src="item.url[0]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <el-image
                      :src="item.url[1]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <div style="margin-left: 4px">{{ item.stateName }}</div>
                  </div>
                </el-option>
              </el-select>
              <el-button
                class="chezhiControlButton"
                @click="chezhiControl(1)"
                :disabled="chezhiDisabled"
                v-hasPermi="['workbench:dialog:save']"
              >
                控制
              </el-button>
            </div>

            <div class="chezhiDrawerDirection">
              {{ directionList[1].dictLabel }}-车道指示器
            </div>
            <div class="chezhiDrawerInfo">
              <div class="chezhiName">车道:</div>
              <el-select
                v-model="chezhiForm2.lane"
                size="small"
                multiple
                collapse-tags
                class="chezhiLaneSelect"
              >
                <el-option
                  v-for="item in chezhiLaneList"
                  :key="item.laneId"
                  :label="item.laneName"
                  :value="item.laneId"
                />
              </el-select>
              <div class="chezhiName">状态:</div>
              <el-select
                v-model="chezhiForm2.state"
                size="small"
                class="chezhiStateSelect"
              >
                <el-option
                  v-for="item in chezhiStateList"
                  :key="item.Id"
                  :value="item.deviceState"
                  :label="item.stateName"
                >
                  <div style="display: flex; align-items: center">
                    <el-image
                      :src="item.url[1]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <el-image
                      :src="item.url[0]"
                      style="width: 20px; height: 20px"
                    ></el-image>
                    <div style="margin-left: 4px">{{ item.stateName }}</div>
                  </div>
                </el-option>
              </el-select>
              <el-button
                class="chezhiControlButton"
                @click="chezhiControl(2)"
                :disabled="chezhiDisabled"
                v-hasPermi="['workbench:dialog:save']"
              >
                控制
              </el-button>
            </div>
            <div class="chezhiDrawerDirection" style="margin: 10px 0">
              {{ directionList[0].dictLabel }} -广播
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">播放次数:</div>
              <el-input-number
                v-model.number="phoneForm1.loopCount"
                controls-position="right"
                @change="handleChangePhone(1)"
                :min="0"
                size="small"
              />
              <el-checkbox
                v-model="phoneForm1.loop"
                label="循环播放"
                border
                class="phoneCheckBox"
                >循环播放</el-checkbox
              >
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">音量:</div>

              <el-slider
                v-model="phoneForm1.volume"
                :max="100"
                class="sliderClass"
              ></el-slider>
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">播放文件:</div>
              <el-select
                v-model="phoneForm1.fileNames"
                placeholder="请选择播放文件"
                clearable
                size="small"
                @click.native="clickFileNames(directionList[1].dictValue)"
              >
                <el-option
                  v-for="item in fileNamesList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.fileName"
                />
              </el-select>
              <el-button
                class="chezhiControlButton"
                size="mini"
                @click="phoneControl(directionList[0].dictValue)"
                v-hasPermi="['workbench:dialog:save']"
              >
                控制
              </el-button>
            </div>
            <div class="chezhiDrawerDirection" style="margin: 10px 0">
              {{ directionList[1].dictLabel }} -广播
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">播放次数:</div>
              <el-input-number
                v-model.number="phoneForm2.loopCount"
                controls-position="right"
                @change="handleChangePhone(2)"
                size="small"
                :min="0"
              />
              <el-checkbox
                v-model="phoneForm2.loop"
                label="循环播放"
                border
                class="phoneCheckBox"
                >循环播放</el-checkbox
              >
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">音量:</div>
              <el-slider
                v-model="phoneForm2.volume"
                :max="100"
                class="sliderClass"
              ></el-slider>
            </div>
            <div class="phoneBox1">
              <div class="chezhiName">播放文件:</div>
              <el-select
                v-model="phoneForm2.fileNames"
                placeholder="请选择播放文件"
                clearable
                size="small"
                @click.native="clickFileNames(directionList[1].dictValue)"
              >
                <el-option
                  v-for="item in fileNamesList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.fileName"
                />
              </el-select>
              <el-button
                class="chezhiControlButton"
                size="mini"
                @click="phoneControl(directionList[1].dictValue)"
                v-hasPermi="['workbench:dialog:save']"
              >
                控制
              </el-button>
            </div>
          </div>
        </el-drawer>
        <el-drawer
          title="分时控制模块"
          :visible.sync="drawerB"
          :modal="false"
          :append-to-body="true"
          class="drawerCenter"
        >
          <div
            v-for="(item, index) in timStrategyList"
            :key="item.strategy_id"
            style="width: 100%"
          >
            <div class="ledLighting">
              <span>{{ item.strategy_name }} </span>
              <el-switch
                v-model="item.strategy_state"
                active-value="0"
                inactive-value="1"
                @change="timStrategySwitch(item)"
              >
              </el-switch>
            </div>
            <div class="Time">
              <div class="timeStart">
                <span class="setTime">起始时间：</span>
                <el-time-picker
                  v-model="item.arr[0]"
                  size="mini"
                  :clearable="false"
                  value-format="HH:mm:ss"
                >
                </el-time-picker>
              </div>
              <div class="timeEnd">
                <span class="setTime">结束时间：</span>
                <el-time-picker
                  v-model="item.arr[1]"
                  size="mini"
                  :clearable="false"
                  value-format="HH:mm:ss"
                  @change="changeEndTime(item.arr[0],item.arr[1],index)"
                >
                </el-time-picker>
              </div>
              <el-button
                type="primary"
                size="mini"
                class="handleLightClass"
                @click="timingStrategy(item)"
                v-hasPermi="['workbench:dialog:save']"
                :disabled="timingStrategyDisabled"
                >确定
              </el-button>
            </div>
          </div>
        </el-drawer>
        <!-- 触发控制模块 -->
        <el-drawer
          title="触发控制"
          :visible.sync="drawerCVisible"
          :modal="false"
          :append-to-body="true"
          class="drawerBottom"
        >
          <div
            style="
              height: 150px;
              overflowy: auto;
              padding: 5px;
              padding-left: 10px;
            "
          >
            <div
              style="
                border-bottom: 1px solid rgba(224, 231, 237, 0.2);
                color: #00c2ff;
              "
            >
              <span style="padding-left: 5px">预警类型</span>
              <span style="padding-left: 28px; line-height: 40px">触发值</span>
              <span style="padding-left: 28px; line-height: 40px"
                >相关预案</span
              >
            </div>
            <div
              v-for="(item, index) in isDrawerCList"
              :key="index"
              style="
                display: flex;
                padding: 4px;
                line-height: 30px;
                border-bottom: 1px solid rgba(224, 231, 237, 0.2);
              "
            >
              <div style="width: 80px; margin-right: 5px; padding-left: 5px">
                {{ item.name }}
              </div>
              <div style="width: 66px; margin-right: 5px; padding-left: 5px">
                {{ item.str }}
              </div>
              <div
                class="reservePlan"
                v-for="(itm, inx) in item.plan"
                :key="inx"
              >
                {{ itm }}
              </div>
            </div>
          </div>
        </el-drawer>
      </div>

      <!-- <div class="tunnelBox tunnelBoxBottom" ></div> -->
      <!--配置区域-->
      <div class="footer" v-show="displayThumbnail == true">
        <div class="fourBox">
          <div class="footMiniBox" v-show="footChangeRadio == '图表'">
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="carIcon"
                  style="width: 18px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>车辆监测</p>
                <p>Vehicle detection</p>
              </div>
            </div>
            <div id="vehicle"></div>
          </div>
          <div
            class="footMiniBox footerRight"
            v-show="footChangeRadio == '图表'"
          >
            <div class="footTitle">
              <!-- <div class="footTriangle"></div> -->
              <div class="footTitleCont">
                <img
                  :src="energyIcon"
                  style="width: 18px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>能耗监测</p>
                <p>Energy consumption monitoring</p>
              </div>
            </div>
            <div id="energyConsumption"></div>
          </div>

          <div
            class="footMiniBox footerRight"
            v-show="footChangeRadio == '图表'"
          >
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="keyVehiclesIcon"
                  style="width: 17px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>重点车辆</p>
                <p>Key vehicles</p>
              </div>
            </div>
            <div id="focusCar"></div>
          </div>
          <div
            class="footerRight footMiniBox"
            v-show="footChangeRadio == '图表'"
          >
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="warningIcon"
                  style="width: 16px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>预警事件</p>
                <p>Alert event</p>
              </div>
            </div>
            <div
              v-if="trafficList.length == 0"
              style="
                width: 100%;
                text-align: center;
                font-size: 14px;
                margin-top: 80px;
              "
            >
              暂无交通事件
            </div>
            <div v-if="trafficList" @click="jumpYingJi">
              <vue-seamless-scroll
                :class-option="defaultOption"
                class="listContent"
                :data="trafficList"
              >
                <ul style="padding-left: 0">
                  <li
                    v-for="(item, index) of trafficList"
                    :key="index"
                    style="cursor: pointer; list-style: none"
                  >
                    <el-row
                      class="listRow"
                      :data-index="JSON.stringify(item)"
                      :id="item.id"
                    >
                      <!-- @click.native="jumpYingJi(item.id)"  -->
                      <el-col :span="2">
                        <div
                          style="
                            width: 30px;
                            height: 30px;
                            display: flex;
                            justify-content: right;
                            align-items: center;
                            transform: scale(0.7);
                          "
                        >
                          <img :src="item.eventType.iconUrl" style="width:100%"/>
                        </div>
                      </el-col>
                      <el-col style="display: flex" :span="4">
                        <div
                          style="width: 100%"
                          :style="{
                            color:
                              item.eventType.prevControlType == '0'
                                ? 'red'
                                : item.eventType.prevControlType == '1'
                                ? '#0B92FE'
                                : 'yellow',
                          }"
                        >
                          {{ item.eventType.simplifyName }}
                        </div>
                      </el-col>
                      <el-col :span="18" style="display: flex">
                        <!-- {{ item.startTime }} {{ item.tunnels.tunnelName }}发生{{
                        item.eventType.eventType
                      }}事件 -->
                        <div
                          style="
                            width: 210px;
                            overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            z-index: 10;
                          "
                        >
                          <span v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS'">{{ item.eventTitle }}</span>
                          <span v-else>{{ item.frameEventTitle }}</span>
                        </div>
                        <div
                          style="
                            font-size: 12px;
                            float: right;
                            margin-right: 10px;
                          "
                        >
                          {{ item.startTime }}
                        </div>
                      </el-col>
                    </el-row>
                    <div class="lineBT">
                      <div></div>
                      <div></div>
                      <div></div>
                    </div>
                  </li>
                </ul>
              </vue-seamless-scroll>
            </div>
          </div>
          <div class="footMiniBox" v-show="footChangeRadio == '视频'">
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="warningIcon"
                  style="width: 16px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>{{videoTitle1}}</p>
                <p>real-time video</p>
              </div>
            </div>
            <videoPlayer
              v-if="liveUrl1 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
              :rtsp="liveUrl1"
              :open="cameraPlayer1"
            ></videoPlayer>
            <video
              v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic1"
                id="h5sVideo2"
                class="h5video_"
                controls
                muted
                loop
                disablePictureInPicture="true"
                style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
              ></video>
              <div class="noPicBox" v-show="videoNoPic1">
                <img  src="../../../assets/image/noVideo.png" />

              </div>
          </div>
          <div
            class="footMiniBox footerRight"
            v-show="footChangeRadio == '视频'"
          >
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="warningIcon"
                  style="width: 16px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>{{videoTitle2}}</p>
                <p>real-time video</p>
              </div>
            </div>
            <videoPlayer
              v-if="liveUrl2 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
              :rtsp="liveUrl2"
              :open="cameraPlayer2"
            ></videoPlayer>
            <video
              v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic1"
                id="h5sVideo3"
                class="h5video_"
                controls
                muted
                loop
                autoplay
                webkit-playsinline
                playsinline
                disablePictureInPicture="true"
                controlslist="nodownload noplaybackrate noremoteplayback"
                style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
              ></video>
              <div class="noPicBox" v-show="videoNoPic1">
                <img  src="../../../assets/image/noVideo.png" />

              </div>
          </div>
          <div
            class="footMiniBox footerRight"
            v-show="footChangeRadio == '视频'"
          >
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="warningIcon"
                  style="width: 16px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>{{ videoTitle3 }}</p>
                <p>real-time video</p>
              </div>
            </div>
            <videoPlayer
              v-if="liveUrl3 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS'"
              :rtsp="liveUrl3"
              :open="cameraPlayer3"
            ></videoPlayer>
            <video
              v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic2"
                id="h5sVideo4"
                class="h5video_"
                controls
                muted
                loop
                autoplay
                webkit-playsinline
                playsinline
                disablePictureInPicture="true"
                controlslist="nodownload noplaybackrate noremoteplayback"
                style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
              ></video>
              <div class="noPicBox" v-show="videoNoPic2">
                <img  src="../../../assets/image/noVideo.png" />

              </div>
          </div>
          <div
            class="footMiniBox footerRight"
            v-show="footChangeRadio == '视频'"
          >
            <div class="footTitle">
              <div class="footTitleCont">
                <img
                  :src="warningIcon"
                  style="width: 16px; margin-right: 5px"
                  v-show="sideTheme != 'theme-blue'"
                />
                <p>{{videoTitle4}}</p>
                <p>real-time video</p>
              </div>
            </div>
            <videoPlayer
              v-if="liveUrl4 && tunnelId != 'WLJD-JiNan-YanJiuYuan-FHS' "
              :rtsp="liveUrl4"
              :open="cameraPlayer4"
            ></videoPlayer>
            <video
              v-if="tunnelId == 'WLJD-JiNan-YanJiuYuan-FHS' && !videoNoPic2"
                id="h5sVideo5"
                class="h5video_"
                loop
                controls
                muted
                autoplay
                webkit-playsinline
                playsinline
                disablePictureInPicture="true"
                controlslist="nodownload noplaybackrate noremoteplayback"
                style="width: 100%; height: 200px; object-fit: cover; z-index: -100"
              ></video>
              <div class="noPicBox" v-show="videoNoPic2">
                <img  src="../../../assets/image/noVideo.png" />

              </div>
          </div>
        </div>

        <div class="footChangeButton">
          <el-radio-group v-model="footChangeRadio" @change="videoRadioChange">
            <el-radio-button label="图表"></el-radio-button>
            <el-radio-button label="视频"></el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <!-- <div class="footer" v-show="displayThumbnail == false"></div> -->
    </div>
    <!-- 批量操作弹窗 -->

    <el-dialog
      class="workbench-dialog vehicle-dialog"
      :title="title"
      :visible.sync="batchManageDialog"
      width="450px"
      append-to-body
      v-dialogDrag
      :close-on-click-modal="false"
      :model="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-table
        ref="batchManageTable"
        :data="batchManageList"
        tooltip-effect="dark"
        style="width: 100%; margin-bottom: 10px !important"
        max-height="220"
        size="mini"

        @row-click="handleRowClick"
      >
        <el-table-column
          prop="eqName"
          label="设备名称"
          width="200"
          align="center"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="pile"
          label="桩号"
          width="120"
          align="center"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="方向" align="center">
          <template slot-scope="scope">
            <span>{{ getDirection(scope.row.eqDirection) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-form
        :model="batchManageForm"
        label-width="90px"
        label-position="left"
        size="mini"
        style="padding: 15px; padding-top: 0px"
      >
        <el-form-item label="配置状态:">
          <div class="wrap">
            <el-radio-group
              v-for="(item, index) in eqTypeStateList2"
              :key="item.state"
              v-model="batchManageForm.state"
              style="display: flex; flex-direction: column"
              @change="$forceUpdate()"
            >
              <el-radio
                v-if="batchManageForm.eqType == item.type && item.control == 1"
                class="el-radio flex-row"
                :label="item.state"
                style="align-items: center"
                :class="[
                  String(batchManageForm.state) == String(item.state)
                    ? 'el-radio-selcted'
                    : '',
                ]"
              >
                <el-row
                  class="flex-row"
                  v-if="
                    batchManageForm.eqDirection == '1' &&
                    (batchManageForm.eqType == 1 || batchManageForm.eqType == 2)
                  "
                >
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[0]"
                  />
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[1]"
                    v-if="item.url.length > 1"
                  />
                  <div style="margin: 0 0 0 10px; display: inline-block">
                    {{ item.name }}
                  </div>
                </el-row>
                <el-row
                  class="flex-row"
                  v-if="
                    batchManageForm.eqDirection == '2' &&
                    (batchManageForm.eqType == 1 || batchManageForm.eqType == 2)
                  "
                >
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[1]"
                  />
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[0]"
                    v-if="item.url.length > 1"
                  />
                  <div style="margin: 0 0 0 10px; display: inline-block">
                    {{ item.name }}
                  </div>
                </el-row>

                <el-row
                  class="flex-row"
                  v-if="
                    batchManageForm.eqType != 1 && batchManageForm.eqType != 2
                  "
                >
                  <img
                    :width="iconWidth"
                    :height="iconHeight"
                    :src="item.url[0]"
                  />

                  <div style="margin: 0 0 0 10px; display: inline-block">
                    {{ item.name }}
                  </div>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="batchManageOK()"
          class="submitButton"
          >确 定</el-button
        >
        <el-button
          class="closeButton"
          @click="closeBatchManageDialog()"
          >取 消</el-button
        >
      </div>
    </el-dialog>
    <!-- 操作日志 弹窗 -->
    <el-dialog
      class="explain-table operationDiglog"
      :title="title"
      :visible.sync="operationLogDialog"
      :before-close="cancel"
      width="1000px"
      append-to-body
      v-dialogDrag
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-tabs v-model="operationActive" @tab-click="handleTabClick">
        <el-tab-pane label="系统日志" name="xitong"></el-tab-pane>
        <el-tab-pane label="操作日志" name="caozuo"></el-tab-pane>
      </el-tabs>

      <!-- <div ref="main" style = "margin-left: 60%;margin-bottom: -2%;"> -->
      <el-row
        :gutter="20"
        style="margin: 0px 0 6px"
        v-show="operationActive == 'xitong'"
      >
        <el-col :span="4">
          <el-button size="small" @click="resetQuery"
              >刷新</el-button
            >
        </el-col>
        <el-col :span="10" :offset="10">
          <div class="grid-content bg-purple" ref="main">
            <el-input
              placeholder="请输入登录地址、用户名称，回车搜索"
              v-model="operationParam_xt.ipaddr"
              @keyup.enter.native="handleQueryOperationParam"
              size="small"
            >
              <el-button
                slot="append"
                class="searchTable"
                @click="syxt_boxShow = !syxt_boxShow"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="syxt_searchBox" v-show="syxt_boxShow" ref="cc">
        <el-form
          ref="operationParam_xt"
          :inline="true"
          :model="operationParam_xt"
          label-width="68px"
          v-show="operationActive == 'xitong'"
        >
          <el-form-item label="登录状态" prop="status">
            <el-select
              v-model="operationParam_xt.status"
              clearable
              placeholder="请选择登录状态"
              size="small"
              style="width: 100%"
            >
              <el-option
                v-for="dict in dict.type.sys_common_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="登录时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 100%"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="setoptions"
              :default-time="['00:00:00', '23:59:59']"
              :class="this.sideTheme != 'theme-dark' ? 'themeDarkPicker' : ''"
            ></el-date-picker>
          </el-form-item>
          <el-form-item class="bottomBox">
            <el-button
              size="small"
              type="primary"
              @click="handleQueryOperationParam"
              >搜索</el-button
            >
            <el-button size="small" @click="resetQuery" type="primary" plain
              >重置</el-button
            >
          </el-form-item>
        </el-form>
      </div>
      <!-- </div> -->

      <!--    <el-form :model="operationParam" ref="operationParam" :inline="true" v-show="operationActive == 'xitong'"
             label-width="68px" style="margin-top: 10px">

      <el-form-item label="状态" prop="status">
        <el-select
          v-model="operationParam.status"
          placeholder="登录状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="登录时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH-mm-ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQueryOperationParam">搜索</el-button>
        <el-button size="mini" @click="resetQuery" type="primary" plain>重置</el-button>
      </el-form-item>
    </el-form>-->
      <el-row
        :gutter="20"
        style="margin: 0px 0 6px"
        v-show="operationActive == 'caozuo'"
      >
        <el-col :span="4">
          <el-button size="small" @click="resetQuery"
              >刷新</el-button
            >
        </el-col>
        <el-col :span="10" :offset="10">
          <div class="grid-content bg-purple" ref="main1">
            <el-input
              placeholder="请输入设备名称、桩号、操作地址，回车搜索"
              v-model="operationParam.operIp"
              @keyup.enter.native="handleQueryOperationParam"
              size="small"
            >
              <el-button
                slot="append"
                class="searchTable"
                @click="sycz_boxShow1 = !sycz_boxShow1"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="syxt_searchBox" v-show="sycz_boxShow1" ref="cc1">
        <el-form
          ref="operationParam"
          :inline="true"
          :model="operationParam"
          label-width="68px"
          v-show="operationActive == 'caozuo'"
        >
          <el-form-item label="设备类型" prop="eqTypeId" style="width: 100%">
            <el-select
              v-model="operationParam.eqTypeId"
              clearable
              placeholder="请选择设备类型"
              size="small"
            >
              <el-option
                v-for="item in eqTypeData"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="隧道名称"
            prop="tunnelId"
            v-show="manageStation == '0'"
          >
            <el-select
              v-model="operationParam.tunnelId"
              placeholder="请选择隧道"
              style="width: 252px"
              clearable
              size="small"
            >
              <el-option
                v-for="item in eqTunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="控制方式" prop="controlType" style="width: 100%">
            <el-select
              v-model="operationParam.controlType"
              clearable
              placeholder="请选择控制方式"
              size="small"
            >
              <el-option
                v-for="dict in dict.type.sd_control_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange1"
              size="small"
              style="width: 252px"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="setoptions"
              :default-time="['00:00:00', '23:59:59']"
            ></el-date-picker>
          </el-form-item>
          <el-form-item class="bottomBox">
            <el-button
              size="small"
              type="primary"
              @click="handleQueryOperationParam"
              >搜索</el-button
            >
            <el-button size="small" @click="resetQuery" type="primary" plain
              >重置</el-button
            >
          </el-form-item>
        </el-form>
      </div>

      <!--    <el-form :model="operationParam" ref="operationParam" :inline="true" v-show="operationActive == 'caozuo'"
               label-width="68px" style="margin-top: 10px">
      <el-form-item label="设备类型" prop="eqTypeId">
        <el-select
          v-model="operationParam.eqTypeId"
          placeholder="请选择设备类型"
          clearable
          size="small"
        >
          <el-option
            v-for="item in eqTypeData"
            :key="item.typeId"
            :label="item.typeName"
            :value="item.typeId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="隧道名称" prop="tunnelId" v-show="manageStation == '0'">
        <el-select
          v-model="operationParam.tunnelId"
          placeholder="请选择隧道"
          clearable
          size="small"
        >
          <el-option
            v-for="item in eqTunnelData"
            :key="item.tunnelId"
            :label="item.tunnelName"
            :value="item.tunnelId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="控制方式" prop="controlType">
        <el-select v-model="operationParam.controlType" placeholder="请选择控制方式" clearable size="small">
          <el-option
            v-for="dict in dict.type.sd_control_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 360px"
          value-format="yyyy-MM-dd HH-mm-ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" @click="handleQueryOperationParam"
        >搜索</el-button
        >
        <el-button size="mini" @click="resetQuery" type="primary" plain
        >重置</el-button
        >
      </el-form-item>
    </el-form>-->
      <el-table
        ref="tables"
        v-loading="loading"
        :data="operationList1"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="operationActive == 'xitong'"
        :default-sort="{ prop: 'loginTime', order: 'descending' }"
        max-height="430"
        :key="1"
      >
        <!-- <el-table-column type="selection" align="center" /> -->
        <el-table-column type="index" :index="indexMethod" label="序号" width="50" align="center"></el-table-column>
        <!--      <el-table-column label="访问编号" align="center" prop="infoId" />-->
        <el-table-column
          label="用户名称"
          align="center"
          prop="userName"
          width="70"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="登录地址"
          align="center"
          prop="ipaddr"
          width="100"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="登录地点"
          align="center"
          width="70"
          prop="loginLocation"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="浏览器"
          align="center"
          prop="browser"
          width="100"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="操作系统"
          align="center"
          prop="os"
        />
        <el-table-column label="登录状态" align="center" prop="status" width="80">
          <template slot-scope="scope">
            <!-- <dict-tag
              :options="dict.type.sys_common_status"
              :value="scope.row.status"
            /> -->
            <span
            :style="{
              color: scope.row.status == '0' ? '#00FF00' : 'red',
            }"
            >{{ pollFormat(scope.row.status) }}</span
          >
          </template>
        </el-table-column>
        <el-table-column label="操作信息" align="center" prop="msg" width="80"/>
        <el-table-column
          label="登录日期"
          align="center"
          prop="loginTime"
          sortable
          width="150"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.loginTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-table
        v-loading="loading"
        :data="operationList2"
        max-height="430"
        :default-sort="{ prop: 'createTime', order: 'descending' }"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        v-show="operationActive == 'caozuo'"
        :key="1"
      >
        <!-- <el-table-column type="selection" align="center" /> -->
        <el-table-column type="index" :index="indexMethod2" label="序号" width="50" align="center"></el-table-column>
        <el-table-column
          label="隧道名称"
          align="center"
          prop="tunnelName.tunnelName"
          width="90"
        />
        <el-table-column
          label="设备类型"
          align="center"
          prop="typeName.typeName"
        />
        <el-table-column label="设备名称" align="center" prop="eqName.eqName" />
        <el-table-column label="桩号" align="center" prop="pile" />
        <el-table-column
          label="操作状态"
          align="center"
          prop="stateName.stateName"
          width="80"
        />
        <el-table-column label="控制方式" align="center" prop="controlType" width="80" :formatter="controlTypeFormat"/>
        <el-table-column label="操作结果" align="center" prop="state" />
        <el-table-column label="操作地址" align="center" prop="operIp" />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          sortable
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total1 > 0 && operationActive == 'xitong'"
        :total="total1"
        :page.sync="operationParam_xt.pageNum"
        :limit.sync="operationParam_xt.pageSize"
        @pagination="getOperationList(operationActive)"
        class="paginationWorkbench"
      />
      <pagination
        v-show="total2 > 0 && operationActive == 'caozuo'"
        :total="total2"
        :page.sync="operationParam.pageNum"
        :limit.sync="operationParam.pageSize"
        @pagination="getOperationList(operationActive)"
        class="paginationWorkbench"
      />
    </el-dialog>
    <!-- 隧道选择对话框-->
    <el-dialog
      class="workbench-dialog"
      :title="title"
      :visible.sync="tunnelVisible"
      width="600px"
      append-to-body
    >
      <el-row>
        <el-checkbox-group v-model="checkboxTunnel">
          <el-checkbox
            class="tunnel-checkbox"
            v-for="(item, index) in tunnelList"
            :key="index"
            :label="item"
            border
          >
            {{ item.tunnelName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-row>
      <div slot="footer">
        <el-button type="primary" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!--状态切换对话框-->
    <!-- <el-dialog v-dialogDrag class="workbench-dialog" :title="title" :visible.sync="stateSwitchVisible" width="450px"
      append-to-body :class="stateForm.manufacturers?'robot-dialog':''">
      <div style="width: 100%;height: 30px;display: flex;justify-content: space-between;">
        <div class="dialogLine"></div>
        <img src="../../../assets/cloudControl/dialogHeader.png" style="height: 30px;transform: translateY(-30px);"/>
      </div>

      <el-form ref="form" :model="stateForm" label-width="80px" label-position="left" size="mini"
                style="position: relative;padding: 20px;padding-top: 0px;">
        <el-row>
         <el-col :span="12">
           <el-form-item label="设备类型:">
             {{ stateForm.eqTypeName }}
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="隧道名称:">
             {{ stateForm.tunnelName }}
           </el-form-item>
         </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="位置桩号:">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属方向:">
              <div v-if="stateForm.eqDirection == 0">
                {{ rightDirection + "方向" }} -->
    <!-- <img src="../../../assets/image/workbench/long-arrowhead.png"/> -->
    <!-- </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }} -->
    <!--  <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                /> -->
    <!-- </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
         <el-col :span="12">
           <el-form-item label="所属机构:">
             {{ stateForm.eqTypeName }}
           </el-form-item>
         </el-col>
         <el-col :span="12">
           <el-form-item label="设备厂商:">
             {{ stateForm.tunnelName }}
           </el-form-item>
         </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="运行状态:">
              {{ '正常' }}
            </el-form-item>
          </el-col>
        </el-row>



        <el-form-item label="调光强度:" v-if="stateForm.eqType == 99999999">
          <div style="width: 200px; float: left">
            <slider :min="0" :max="100" v-model="stateForm.lightValue"></slider>
          </div>
          <label style="color: yellow; margin-left: 10px; pointer-events: none">
            {{ stateForm.lightValue }}</label>
        </el-form-item> -->
    <!-- 所有设备 -->

    <!-- plc、紧急电话、手报
        <el-form-item label="配置状态:" v-if="stateForm.eqType == (14 || 21 || 34) ">
          {{ '在线'}}
        </el-form-item> -->
    <!-- 28：压力表  -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 28">
          {{ '正常'}}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 21">

        <el-form-item label="运行状态:">
          {{ '正常' }}
        </el-form-item>
         plc -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 14">
          {{ '在线'}}
        </el-form-item> -->
    <!-- 压力表 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 28">
          {{ '正常'}}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType == 21">

          {{ '在线'}}
        </el-form-item>  -->

    <!-- 消防水泵 -->
    <!-- <el-form-item label="水位:" v-if="stateForm.eqType == 13">
          {{ '水位'}}
        </el-form-item>

        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="配置状态:" >
              {{ '正常'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上传时间:" >
              {{ '今天'}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="车道:" >
              {{ '这是车道'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车流量:" >
              {{ '10辆/没分钟'}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.value && stateForm.eqType == 20">
          <el-col :span="12">
            <el-form-item label="平均车速:">
              {{ '60公里/每小时'}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="占有率:">
              {{ '60%'}}
            </el-form-item>
          </el-col>
        </el-row>





        <template  v-if="stateForm.value &&stateForm.eqType == 20">
          <el-table
            ref="multipleTable"
            :data="weiboList"
            tooltip-effect="dark"
            style="width: 100%"
            size="mini"
            empty-text="暂无数据"
          >
            <el-table-column label="车道" align="center" prop="byLane">
              <template slot-scope="scope">
                <span>第{{ scope.row.byLane }}车道</span>
              </template>
            </el-table-column>
            <el-table-column
              label="车流量(辆/分钟)"
              align="center"
              prop="fSpaceOccupyRation"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.fSpaceOccupyRation }}辆/分钟</span>
              </template>
            </el-table-column>
            <el-table-column label="平均车速" align="center" prop="bySpeed">
              <template slot-scope="scope">
                <span>{{ scope.row.bySpeed }}km/h</span>
              </template>
            </el-table-column>
            <el-table-column
              label="占有率"
              align="center"
              prop="fSpaceOccupyRation"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.fSpaceOccupyRation }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="上传时间" align="center" prop="createTime">
              <template slot-scope="scope">
                <span>{{
                    parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
                  }}</span>
              </template>
            </el-table-column>
          </el-table>
        </template> -->

    <!-- ====================微博检查结束============ -->
    <!-- <el-form-item label="状态:" v-if="stateForm.value && ![5, 6, 14, 13,15, 16,20].includes(stateForm.eqType)">
          {{ stateForm.value }}
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 999">
          {{ stateForm.value }} ppm
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 15">
          {{ stateForm.value }} x10-3m<sup>-1</sup>
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 16">
          {{ stateForm.value }} m/s
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && [5].includes(stateForm.eqType)">
          {{ stateForm.value }} cd/m²
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.value && stateForm.eqType == 999">
          {{ stateForm.value }} m
        </el-form-item>
        <el-form-item label="检测值:" v-if="stateForm.eqType == 123">
          {{ stateForm.state }}
        </el-form-item> -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 33 && stateForm.eqType != 21 && !stateForm.value && stateForm.eqType != 123 && stateForm.eqType != 28  && stateForm.eqType != 34""> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
          <div v-if="stateForm.eqType == 31" class="EqType31">
            <el-form label-width="60px" label-position="left" size="mini">
              <el-form-item class="radioEqType31">
                <el-radio-group v-model="radioEqType31"  >
                   <el-radio :label="1">关灯</el-radio>
                   <el-radio :label="2">同步单闪</el-radio>
                   <el-radio :label="3">流水灯</el-radio>
                 </el-radio-group>
              </el-form-item>

              <el-form-item label="亮度:">
                  <el-slider v-model="brightness"  style="width: 90%;" :min="10" :max="155">
                  </el-slider>
              </el-form-item>
              <el-form-item label="频率:">
                 <el-slider v-model="frequency"  style="width: 90%;" :min="10" :max="155">
                 </el-slider>
              </el-form-item>
            </el-form>
          </div>
          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item> -->
    <!-- 应急照明 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 6 "> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>

          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item> -->
    <!-- 消防水泵 -->
    <!-- <el-form-item label="配置状态:" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 123 && stateForm.eqType == 13"> -->
    <!-- <div class="wrap" v-if="stateForm.eqType != 111">
            <el-radio-group v-for="(item, index) in eqTypeStateList" :key="index" v-model="stateForm.state" style="display: flex; flex-direction: column"
              :disabled="getFjDisabled(stateForm, item.state)">
              <el-radio v-if="stateForm.eqType == item.type && item.control == 1" class="el-radio flex-row" :label="item.state"
                style="align-items: center" :class="[
                  stateForm.state == item.state ? 'el-radio-selcted' : '',
                ]">
                <el-row v-if="item.url.length > 1 && stateForm.eqDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img v-for="url in item.url" class="equipment-img" :src="url" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length > 1 && stateForm.eqDirection == 1">
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="item.url[1]" />
                    <img class="equipment-img" :src="item.url[0]" />
                  </el-row>
                  <el-row class="flex-row align-items-center form-direction" style="margin-top: 5px">
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="item.url.length == 1" class="flex-row">
                  <img :width="item.width" :height="item.height" :src="item.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ item.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>

          <div v-if="stateForm.eqType == 111">
            <el-form label-width="100px" label-position="left" size="mini">
              <el-form-item label="设备名称:">
                {{ stateForm.equipmentName }}
              </el-form-item>
              <el-form-item label="所属隧道:">
                {{ stateForm.tunnelName }}
              </el-form-item>
              <el-form-item label="当前压力值:">
                {{ stateForm.analogQuantity }}Mp
              </el-form-item>
              <el-form-item label="压力上限:">
                {{ stateForm.highest }}Mp
              </el-form-item>
              <el-form-item label="压力下限:">
                {{ stateForm.low }}Mp
              </el-form-item>
              <el-form-item label="采集时间:">
                {{ stateForm.createTime }}
              </el-form-item>
            </el-form>
          </div>
        </el-form-item>


        <template v-if="stateForm.eqType == 112">
          <div class="robotBox">
            <video v-if="watchVideo" controls muted autoplay loop :src="robotVideoUrl" style="width: 250px; height: 150px;">
              <source src type="video/mp4" />
            </video>
            <el-image v-else :src="robotUrl">
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
            <div class="switchIcon" @click="watchVideo = !watchVideo">
              <i class="el-icon-sort"></i>
            </div>
          </div>
          <el-form-item label="生产厂家:" v-if="stateForm.manufacturers">
            <div v-if="!(stateForm.manufacturers == ''|| stateForm.manufacturers == null)">{{
                stateForm.manufacturers
              }}
            </div>
            <div v-else style="white-space: nowrap;">暂未获取</div>
          </el-form-item>
          <el-form-item label="设备电量:">
            <battery-icon v-if="!(stateForm.electricQuantity == ''|| stateForm.electricQuantity == null)" style="width:auto;"
              :quantity="stateForm.electricQuantity" rotate="0" />
            <div v-else style="white-space: nowrap;">暂未获取</div>
          </el-form-item>
          <el-tabs class="robotTabs" v-model="robotInformationActive" @tab-click="robotTabsClick">
            <el-tab-pane label="车流量情况" name="trafficFlow">
              <el-row>车流量：307</el-row>
              <el-col :span="8">客一：217</el-col>
              <el-col :span="8">客二：2</el-col>
              <el-col :span="8">客三：1</el-col>
              <el-col :span="8">客四：2</el-col>
              <el-col :span="8">货一：35</el-col>
              <el-col :span="8">货二：18</el-col>
              <el-col :span="8">货三：21</el-col>
              <el-col :span="8">货四：4</el-col>
              <el-col :span="8">货五：0</el-col>
              <el-col :span="8">货六：5</el-col>
              <el-col :span="8">其他：2</el-col>
            </el-tab-pane>
            <el-tab-pane label="事件情况" name="eventSituation">
              <el-row>
                <el-col :span="8">事件数：3次</el-col>
                <el-col :span="8">未处置：2次</el-col>
                <el-col :span="8">已处置：1次</el-col>
              </el-row>
              <el-card class="box-card" :span="24">
                <el-col :span="12">事件类型：追尾</el-col>
                <el-col :span="12">事件标题：追尾</el-col>
                <el-col :span="12">事件状态：处理中</el-col>
                <el-col :span="12">时间：2022-2-28 09:30:00</el-col>
                <el-col :span="12">级别：一级</el-col>
                <el-col :span="12">受伤：0; 死亡：0;</el-col>
              </el-card>


            </el-tab-pane>
            <el-tab-pane label="地道的路面情况" name="theRoad"></el-tab-pane>
            <el-tab-pane label="状态记录" name="stateRecords">
              <div id="robotEchart" />
            </el-tab-pane>
          </el-tabs>
        </template>

      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" v-if="stateForm.eqType != 21 && !stateForm.value" @click="submitState"
          class="submitButton">确 定
        </el-button>
        <el-button type="primary" size="mini" v-if="stateForm.eqType != 21 && stateForm.value && stateForm.eqType != 14"
          @click="submitState" class="submitButton">确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
    <!--批量管理对话框-->
    <!-- <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table batch-dialog"
      :title="title"
      :visible.sync="batchVisible"
      width="560px"
      append-to-body
      @close="batchForm.eqDirection = ''"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-form
        ref="batchForm"
        :rules="rules"
        :model="batchForm"
        label-width="80px"
        label-position="left"
        size="mini"
        hide-required-asterisk
        style="padding: 0px 20px 20px"
      >
        <el-form-item label="设备类型:">
          <el-select v-model="batchForm.eqType" size="mini">
            <el-option
              v-for="item in eqTypeList"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeId"
            />

          </el-select>
        </el-form-item>
        <el-form-item label="方向">
          <el-select v-model="batchForm.eqDirection" size="mini" clearable>
            <el-option
              v-if="allDirection.length > 0"
              v-for="item in allDirection"
              :key="item.eqDirection"
              :label="item.eqDirectionName"
              :value="item.eqDirection"
            />
          </el-select>
        </el-form-item>
        <template v-if="batchForm.eqType == 1">
          <el-form-item label="车道">
            <el-select v-model="batchForm.eqlane" size="mini">
              <el-option
                v-if="allLane.length > 0"
                v-for="item in allLane"
                :key="item.lane"
                :label="item.laneName"
                :value="item.lane"
              />
            </el-select>
          </el-form-item>
        </template>
        <el-table
          ref="multipleTable"
          :data="devicesList"
          tooltip-effect="dark"
          style="width: 100%"
          max-height="220"
          size="mini"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
          empty-text="暂无设备"
          :key="1"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            prop="eqName"
            label="设备名称"
            width="220"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            prop="pile"
            label="桩号"
            width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="eqDirectionName" label="方向">
          </el-table-column>
        </el-table>

        <el-form-item label="配置状态:" prop="state" style="margin-top: 20px">
          <div class="wrap">
            <el-radio-group
              v-for="(items, index) in eqTypeStateList"
              :key="index"
              v-model="batchForm.state"
              style="display: flex; flex-direction: column"
            >
              <el-radio
                v-if="batchForm.eqType == items.type && items.control == 1"
                class="el-radio flex-row"
                :label="items.state"
                :class="[
                  batchForm.state == items.state ? 'el-radio-selcted' : '',
                ]"
                style="align-items: center"
              >
                <el-row v-if="items.url.length > 1 && batchFormDirection == 0">
                  <el-row class="flex-row align-items-center">
                    <img
                      v-for="(url, indexs) in items.url"
                      class="equipment-img"
                      :key="indexs"
                      :src="url"
                    />
                  </el-row>
                  <el-row
                    class="flex-row align-items-center form-direction"
                    style="margin-top: 5px"
                  >
                    <el-row>正</el-row>
                    <el-row>反</el-row>
                  </el-row>
                </el-row>
                <el-row
                  v-else-if="items.url.length > 1 && batchFormDirection == 1"
                >
                  <el-row class="flex-row align-items-center form-direction">
                    <img class="equipment-img" :src="items.url[1]" />
                    <img class="equipment-img" :src="items.url[0]" />
                  </el-row>
                  <el-row
                    class="flex-row align-items-center form-direction"
                    style="margin-top: 5px"
                  >
                    <el-row>反</el-row>
                    <el-row>正</el-row>
                  </el-row>
                </el-row>
                <el-row v-else-if="items.url.length == 1" class="flex-row">
                  <img class="equipment-img" :src="items.url[0]" />
                  <el-row style="margin: 5px 0 0 10px">{{ items.name }}</el-row>
                </el-row>
              </el-radio>
            </el-radio-group>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          size="mini"
          @click="submitBatchState('batchForm')"
          class="submitButton"
          >确 定
        </el-button>
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
    <!--微波车检对话框-->
    <!-- <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="weiboVisible"
      width="45vw"
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="weiboList"
        tooltip-effect="dark"
        style="width: 100%"
        size="mini"
        empty-text="暂无数据"
      >
        <el-table-column label="车道" align="center" prop="byLane" width="200">
          <template slot-scope="scope">
            <span>第{{ scope.row.byLane }}车道</span>
          </template>
        </el-table-column>
        <el-table-column
          label="车流量(辆/分钟)"
          align="center"
          prop="fSpaceOccupyRation"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.byVehicelNum }}辆/分钟</span>
          </template>
        </el-table-column>
        <el-table-column label="平均车速" align="center" prop="bySpeed">
          <template slot-scope="scope">
            <span>{{ scope.row.bySpeed }}km/h</span>
          </template>
        </el-table-column>
        <el-table-column
          label="占有率"
          align="center"
          prop="fSpaceOccupyRation"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.fSpaceOccupyRation }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="上传时间" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
            }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->

    <com-video
      class="comClass"
      ref="videoRef"
    ></com-video>
    <com-light
      class="comClass"
      ref="lightRef"
    ></com-light>
    <com-covi
      class="comClass"
      ref="coviRef"
    ></com-covi>
    <!--   消防泵  -->
    <!-- <com-xfsb
      class="comClass"
      v-if="this.eqInfo.clickEqType == 13"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-xfsb> -->
    <!--   潜水深井泵  -->
    <!-- <com-sjb
      class="comClass"
      v-if="this.eqInfo.clickEqType == 49"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-sjb> -->
    <!--    温湿传感器  -->
    <!-- <com-temperatureHumidity
      class="comClass"
      v-if="this.eqInfo.clickEqType == 41"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-temperatureHumidity> -->
    <!--    液位传感器  -->
    <!-- <com-liquidLevel
      class="comClass"
      v-if="this.eqInfo.clickEqType == 42"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-liquidLevel> -->
    <com-data
      class="comClass"
      ref="dataRef"
    ></com-data>
    <com-wind
      class="comClass"
      ref="windRef"
    ></com-wind>
    <com-pressure
      class="comClass"
      ref="pressureRef"
    ></com-pressure>
    <com-vehicleDetec
      class="comClass"
      ref="vehicleDetecRef"
    ></com-vehicleDetec>
    <com-callPolice
      class="comClass"
      ref="callPoliceRef"
    ></com-callPolice>
    <!-- <com-robot
      class="comClass"
      v-if="this.eqInfo.clickEqType == 29"
      :brandList="this.brandList"
      :directionList="this.directionList"
      :eqTypeDialogList="this.eqTypeDialogList"
      :eqInfo="this.eqInfo"
      @dialogClose="dialogClose"
    ></com-robot> -->
    <robot class="comClass robotHtmlBox" v-if="this.eqInfo.clickEqType == 29"></robot>
    <com-bright
      class="comClass"
      ref="brightRef"
    ></com-bright>
    <com-youdao
      class="comClass"
      ref="youdaoRef"
    ></com-youdao>
    <com-board
      class="comClass"
      ref="boardRef"
    ></com-board>
    <com-radio
      class="comClass"
      ref="radioRef"
    ></com-radio>
    <!--摄像机对话框-->
    <!-- <el-dialog v-dialogDrag class="workbench-dialog batch-table video-dialog" :title="title" :visible="cameraVisible"
      width="860px" append-to-body @opened="loadFlv" :before-close="handleClosee">
      <div style="width: 100%;height: 30px;display: flex;justify-content: space-between;">
        <div class="dialogLine"></div>
        <img src="../../../assets/cloudControl/dialogHeader.png" style="height: 30px;transform: translateY(-30px);"/>
      </div>
      <el-form ref="form" :model="stateForm" label-width="60px" label-position="left" size="mini"
              style="padding: 20px;padding-top: 0px;">
        <el-row>
          <el-col :span="8">
            <el-form-item label="摄像机:" label-width="60px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="30px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img src="../../../assets/image/workbench/long-arrowhead.png" style="transform: rotate(180deg); margin-left: 10px" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实时视频:" label-width="80px">
          <video id="videoBox" controls muted autoplay loop style="width: 80%;"></video>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="shuibengVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form label-width="80px" label-position="left" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="1#泵状态:">
              {{ shuibengObj.beng1 }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="2#泵状态:">
              {{ shuibengObj.beng2 }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="缺水故障:">
              {{ shuibengObj.queshui }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电源故障:">
              {{ shuibengObj.dianyuan }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联动指示:">
              {{ shuibengObj.liandong }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- <el-form-item label="设备状态:">
              {{shuibengObj.devState}}
            </el-form-item> -->
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--道路结冰对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="daolujiebingVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form ref="form" :model="stateForm" label-position="left" size="mini">
        <el-row>
          <el-col :span="8">
            <el-form-item label="所处位置:" label-width="80px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="40px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="表面温度:">
              {{ dljbObj.surfaceTemperature }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面积水:">
              {{ dljbObj.roadWater }}
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="结冰温度:">
              {{ dljbObj.freezeTemperature }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冰厚度:">
              {{ dljbObj.IceThickness }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="盐度值:">
              {{ dljbObj.salinityValue }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="湿滑系数:">
              {{ dljbObj.wetslidingCoefficient }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="路面状况:">
              {{ dljbObj.roadCondition }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备状态:">
              {{ dljbObj.status }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 路面状态 -->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table"
      :title="title"
      :visible.sync="lumianVisible"
      width="40vw"
      append-to-body
      style="left: 30%"
    >
      <el-form ref="form" :model="stateForm" label-position="left" size="mini">
        <el-row>
          <el-col :span="8">
            <el-form-item label="所处位置:" label-width="80px">
              {{ stateForm.pile }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="IP:" label-width="40px">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:" label-width="50px">
              <div v-if="stateForm.eqDirection == 0">
                <img src="../../../assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="../../../assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="路面温度:">
              {{ lumianList.pavementtemp }}℃
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面积水:">
              {{ lumianList.waterthickness }}毫米
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="湿滑系数:">
              {{ lumianList.frictionalcoe }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="冰厚度:">
              {{ lumianList.icethickness }}毫米
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="积雪厚度:">
              {{ lumianList.icethickness }}毫米
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路面状况:">
              {{ lumianList.pavementstatus }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态:" label-width="80px">
              {{ lmList == 1 ? "正常" : "报警" }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采集时间:">
              <template slot-scope="scope">
                <span>
                  {{
                    parseTime(
                      lumianList.timeCollect,
                      "{yyyy}-{m}-{d} {h}:{i}:{s}"
                    )
                  }}</span
                >
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- <el-dialog v-dialogDrag class="workbench-dialog" :title="title" :visible.sync="coviVisible" width="560px"
      append-to-body>
      <div style="height: 400px;" v-if="stateForm.eqType == 17" id="fengsu"></div>
      <div style="height: 400px;" v-if="stateForm.eqType == 19" id="covi"></div>
    </el-dialog> -->
    <!-- 结束 -->
    <!--图标含义对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog explain-table icon-dialog"
      :title="title"
      :visible.sync="explainVisible"
      width="1240px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <img
        src="@/assets/logo/equipment_log/all.png"
        style="width: 100%; height: auto;"
      />
      <!-- <el-table
        ref="multipleTable"
        :data="eqIcon"
        tooltip-effect="dark"
        style="width: 100%"
        :max-height="400"
        size="mini"
        @selection-change="handleSelectionChange"
        empty-text="暂无设备"
      >
        <el-table-column label="图标">
          <template slot-scope="scope">
            　
            <img
              :src="scope.row.url"
              style="vertical-align: middle; max-height: 30px; padding: 2px 0"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="type"
          label="所属类型"
          width="120"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column prop="explain" label="含义说明" show-overflow-tooltip>
        </el-table-column>
      </el-table> -->
      <div slot="footer">
        <el-button type="primary" size="mini" @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!--查看控制策略对话框-->
    <el-dialog
      v-dialogDrag
      class="explain-table operationDiglog"
      :title="title"
      :visible.sync="strategyVisible"
      width="1000px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="dialogStyleBox">
        <div class="dialogLine"></div>
        <div class="dialogCloseButton"></div>
      </div>
      <el-tabs v-model="strategyActive" @tab-click="handleClick">
        <el-tab-pane label="日常策略" name="richang"></el-tab-pane>
<!--        <el-tab-pane label="预警策略" name="yujing"></el-tab-pane>-->
      </el-tabs>
      <el-row
        :gutter="20"
        style="margin: 0px 0 6px;"
        v-show="strategyActive == 'richang'"
      >
        <el-col :span="4">
          <el-button size="small" @click="resetQuery">刷新</el-button>
        </el-col>
        <el-col :span="10" :offset="10">
          <div class="grid-content bg-purple" ref="main2">
            <el-input
              v-model="queryParams.strategyName"
              placeholder="请输入策略名称"
              @keyup.enter.native="handlestrategyQuery"
              size="small"
            >
              <el-button
                slot="append"
                class="searchTable"
                @click="syxt_boxShow2 = !syxt_boxShow2"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="syxt_searchBox" v-show="syxt_boxShow2"  ref="cc2">
        <el-form
          ref="operationParam"
          :inline="true"
          :model="operationParam"
          label-width="68px"
        >
          <el-form-item label="隧道名称" prop="tunnelId">
            <el-select
              v-model="queryParams.tunnelId"
              placeholder="请选择隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="方向" prop="direction">
            <el-select
              v-model="queryParams.direction"
              placeholder="请选择方向"
              clearable
              size="small"
            >
              <el-option
                v-for="(item, index) in directionOptions"
                :key="index"
                :label="item.dictLabel"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="策略类型" prop="strategyType">
            <el-select
              v-model="queryParams.strategyType"
              placeholder="请选择策略类型"
              clearable
              size="small"
            >
              <el-option
                v-for="dict in strategyTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item class="bottomBox">
            <el-button size="small" @click="handlestrategyQuery"
              >搜索</el-button
            >
            <el-button size="small" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-row
        :gutter="20"
        style="margin: 0px 0 6px; padding: 0px 5px"
        v-show="strategyActive == 'yujing'"
      >
        <el-col :span="4">
          <el-button size="small" @click="resetQuery">刷新</el-button>
        </el-col>
        <el-col :span="10" :offset="10">
          <div class="grid-content bg-purple" ref="main3">
            <el-input
              v-model="queryParams.strategyName"
              placeholder="请输入策略名称"
              @keyup.enter.native="handlestrategyQuery"
              size="small"
            >
              <el-button
                slot="append"
                class="searchTable"
                @click="sycz_boxShow3 = !sycz_boxShow3"
              ></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="syxt_searchBox" v-show="sycz_boxShow3"  ref="cc3">
        <el-form
          ref="operationParam"
          :inline="true"
          :model="operationParam"
          label-width="68px"
        >
          <el-form-item label="隧道名称" prop="tunnelId">
            <el-select
              v-model="queryParams.tunnelId"
              placeholder="请选择隧道"
              clearable
              size="small"
            >
              <el-option
                v-for="item in tunnelData"
                :key="item.tunnelId"
                :label="item.tunnelName"
                :value="item.tunnelId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="策略类型" prop="strategyType">
            <el-select
              v-model="queryParams.strategyType"
              placeholder="请选择策略类型"
              clearable
              size="small"
            >
              <el-option
                v-for="dict in strategyTypeEvent"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item class="bottomBox">
            <el-button size="small" @click="handlestrategyQuery"
              >搜索</el-button
            >
            <el-button size="small" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="multipleTable"
        :data="strategyList"
        tooltip-effect="dark"
        style="width: 100%;"
        :max-height="400"
        size="mini"
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
        empty-text="暂无策略"
        :key="1"
      >
      <el-table-column
            type="index"
            width="70"
            align="center"
            :index="indexMethod3"
            label="序号">
          </el-table-column>
        <el-table-column
          label="隧道名称"
          align="center"
          prop="tunnels.tunnelName"
        />
        <el-table-column
          label="事件类型"
          align="center"
          prop="tunnels.tunnelName"
          v-if="strategyActive == 'yujing'"
        />
        <el-table-column label="策略名称" align="center" prop="strategyName" />
        <el-table-column
          label="方向"
          align="center"
          prop="direction"
          :formatter="directionFormat"
        />
        <el-table-column
          label="策略类型"
          align="center"
          prop="strategyType"
          :formatter="strategyTypeFormat"
          v-if="strategyActive=='richang'"
        />
        <el-table-column
          label="策略类型"
          align="center"
          prop="strategyType"
          :formatter="strategyTypeFormatEvent"
          v-if="strategyActive=='yujing'"
        />

        <el-table-column
          label="策略信息"
          align="center"
          prop="strategyInfo"
          :show-overflow-tooltip="true"

        >
          <template slot-scope="scope" v-if="scope.row.slist != []">
            <div v-for="(item, index) in scope.row.slist" :key="index">
              {{ item }}
            </div>
          </template>
          <div v-else>暂无信息</div>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="schedulerTime">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.strategyState"
              active-color="#39ADFF"
              inactive-color="#ccc"
              active-value="0"
              inactive-value="1"
              @change="changeStrategyState(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              v-show="scope.row.strategyType == 0"
              size="mini"
              type="text"
              icon="el-icon-thumb"
              @click="handleController(scope.row)"
              >手动控制
            </el-button>
            <el-button
                size="mini"
                class="tableBlueButtton"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:strategy:edit']"
              >编辑</el-button
              >
              <el-button
                size="mini"
                class="tableDelButtton"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:strategy:remove']"
              >删除</el-button
              >
          </template>
        </el-table-column> -->
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="handlestrategyQuery"
        class="paginationWorkbench"
      />
      <!-- <div slot="footer">
        <el-button type="primary" @click="strategyCancel">关 闭</el-button>
      </div> -->
    </el-dialog>
    <!-- 情报板编辑弹窗 -->
    <vms-content-update ref="vmsContentUpdate"></vms-content-update>
    <!-- 情报板批量编辑对话窗 -->
    <content-batch-edit ref="contentBatchEdit"></content-batch-edit>
    <!-- 火灾报警 对话框 -->
    <el-dialog
      title="火灾报警提示"
      :visible.sync="HZdialogVisible"
      width="25%"
      :before-close="HZhandleClose"
      class="temperatureDialog"
    >
      <div slot="title">
        <span
          ><i
            class="el-icon-message-solid"
            style="color: #e6a23c; margin-right: 10px"
          ></i
        ></span>
        <span style="color: white">火灾报警提示</span>
      </div>
      <div>
        <div>隧道名称</div>
        <div>{{ tunnelName }}</div>
      </div>
      <div>
        <div>隧道温度</div>
        <div>{{ temperature }}°C</div>
      </div>
      <div class="HZdialogBg"></div>
    </el-dialog>
    <!-- 限高控制 对话框 -->
    <el-dialog
      title="限高杆控制"
      :visible.sync="controlHeightVisible"
      :before-close="controlHeightClose"
      class="workbench-dialog"
      v-dialogDrag
      width="560px"
      append-to-body
    >
      <div>
        <span>隧道名称：</span>
        <span>{{ tunnelNameEarlyWarn }}</span>
      </div>
      <div>
        <span>限高杆高度：</span>
        <span>2米</span>
      </div>
      <div>
        <el-progress
          :text-inside="true"
          :stroke-width="26"
          :percentage="percentage"
          :color="customColors"
          style="width: 60%; display: inline-block"
        ></el-progress>
        <div
          style="
            display: inline-block;
            margin-left: 20px;
            color: white !important;
          "
        >
          <el-button-group>
            <el-button icon="el-icon-minus" @click="decrease"></el-button>
            <el-button icon="el-icon-plus" @click="increase"></el-button>
          </el-button-group>
        </div>
      </div>
    </el-dialog>
    <!--隧道诱导对话框-->
    <el-dialog
      v-dialogDrag
      class="workbench-dialog batch-table youdaoDialog"
      :title="title"
      :visible.sync="youdaoVisible"
      width="40vw"
      append-to-body
    >
      <el-form
        ref="form"
        :model="stateForm"
        label-width="80px"
        label-position="left"
        size="mini"
      >
        <el-row>
          <!-- <el-col :span="8">
            <el-form-item label="所处位置:">
              {{stateForm.pile}}
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="IP:">
              {{ stateForm.eqFeedbackAddress1 }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="方向:">
              <div v-if="stateForm.eqDirection == 0">
                <img src="@/assets/image/workbench/long-arrowhead.png" />
                {{ rightDirection + "方向" }}
              </div>
              <div v-else-if="stateForm.eqDirection == 1">
                {{ leftDirection + "方向" }}
                <img
                  src="@/assets/image/workbench/long-arrowhead.png"
                  style="transform: rotate(180deg); margin-left: 10px"
                />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="控制模式:" prop="corModel">
              <el-radio-group v-model="stateForm.corModel" @change="outLight">
                <el-col :span="4">
                  <el-radio
                    key="00"
                    label="00"
                    style="padding: 5px 20px 5px 10px"
                    >闪烁</el-radio
                  >
                </el-col>
                <el-col :span="4">
                  <el-radio
                    key="02"
                    label="02"
                    style="padding: 5px 20px 5px 10px; margin-left: 25px"
                    >常亮</el-radio
                  >
                </el-col>
                <el-col :span="4">
                  <el-radio
                    key="03"
                    label="03"
                    style="padding: 5px 20px 5px 10px; margin-left: 50px"
                    >常灭</el-radio
                  >
                </el-col>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车道:" prop="lane">
              <el-checkbox v-model="stateForm.Zlane" label="D" key="D"
                >左车道</el-checkbox
              >
              <el-checkbox v-model="stateForm.Ylane" label="E" key="E"
                >右车道</el-checkbox
              >
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="白灯亮度:">
              <el-slider
                v-model="stateForm.whiteLight"
                :step="10"
                style="width: 90%"
              >
              </el-slider>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="黄灯亮度:">
              <el-slider
                v-model="stateForm.yellowLight"
                :step="10"
                style="width: 90%"
              >
              </el-slider>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.corModel == '00'">
          <el-col :span="12">
            <el-form-item label="闪烁模式:">
              <el-select
                v-model="stateForm.twinkleModel"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in twinkleModelOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="闪烁频率:">
              <el-select
                v-model="stateForm.twinkleFrequency"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in twinkleFrequencyOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              次/分钟
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="stateForm.corModel == '00'">
          <el-col :span="12">
            <el-form-item label="亮灯时长:">
              <el-select
                v-model="stateForm.lightTime"
                placeholder="请选择"
                style="width: 60%"
              >
                <el-option
                  v-for="(item, index) in lightTimeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              毫秒
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" size="mini" @click="submitCorLight"
          >确 定</el-button
        >
        <el-button type="primary" size="mini" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import flvjs from "flv.js";
import { math } from "@/utils/math.js";
import moment from "moment";
import { displayH5sVideoAll } from "@/api/icyH5stream";

import vueSeamlessScroll from "vue-seamless-scroll";
import $ from "jquery";
import "jquery-ui-dist/jquery-ui";
import "jquery-ui-dist/jquery-ui.min.css";
import bus from "@/utils/bus";
import { mapState } from "vuex";

import {
  getLiPowerDevices,
  //initLipowerDevice
} from "@/api/system/lipowerDevice";

import {
  listDevices,
  getDevices,
  updateDevices,
  getAudioFileList,
  playVoiceGroup,
  videoStreaming,
  getDeviceById
} from "@/api/equipment/eqlist/api";
import {
  listType,
  listHasType,
  getType,
  groupByBigType,
  hasListByBigType,
  loadPicture,
} from "@/api/equipment/type/api.js";
import { getTemplateInfo } from "@/api/board/template.js";
import {
  listTunnels,
  getTunnels,
  updateTunnels,
  getConfigData,
  // getStorageData,
  setConfigData,
  pressure,
  sendAnalogCom,
  getHostData,
  setCorLight,
  updateCarFinger,
  getDeviceDataAndState,
  getJlyTunnel,
  energyConsumptionDetection,
  getBoardContent,
} from "@/api/equipment/tunnel/api.js";
import {
  listEqTypeState,
  getEqTypeState,
  updateEqTypeState,
  getStateByRun,
  getStateByData,
} from "@/api/equipment/eqTypeState/api";
import {
  icon,
  // eqTypeStateIcon,
  laneImage,
} from "../../../utils/configData.js";
import {
  listStrategy,
  getStrategy,
  handleStrategy,
  workTriggerInfo,
  updateState,
} from "@/api/event/strategy";
import { getEntranceExitVideo } from "@/api/eventDialog/api.js";
import { selectByEqDeno } from "@/api/business/roadState.js";
import videoPlayer from "@/views/event/vedioRecord/myVideo";
import vmsContentUpdate from "@/views/workbench/config/vms-content-update"; //单个编辑
import contentBatchEdit from "@/views/workbench/config/content-batch-edit"; //批量编辑
import comVideo from "@/views/workbench/config/components/video"; //摄像机弹窗
import comLight from "@/views/workbench/config/components/light"; //各种带单选框的弹窗
import comCovi from "@/views/workbench/config/components/covi"; //covi弹窗
import comBright from "@/views/workbench/config/components/bright"; //亮度检测器等只有基本信息的弹窗
import comWind from "@/views/workbench/config/components/wind"; //风速风向弹窗
import comPressure from "@/views/workbench/config/components/pressure"; //压力表弹窗
import comVehicleDetec from "@/views/workbench/config/components/vehicleDetec"; //微波车检弹窗
import comCallPolice from "@/views/workbench/config/components/callPolice"; //声光报警弹窗
import comRobot from "@/views/workbench/config/components/robot"; //机器人弹窗
import comData from "@/views/workbench/config/components/data"; //只有数据的弹窗
import comYoudao from "@/views/workbench/config/components/youdao"; //诱导灯弹窗
import comBoard from "@/views/workbench/config/components/board"; //情报板弹窗
import comRadio from "@/views/workbench/config/components/radio"; //广播弹窗
import comXfsb from "@/views/workbench/config/components/xfsb"; //消防水泵弹窗
import comSjb from "@/views/workbench/config/components/sjb"; //潜水深水泵
import robot from "@/views/workbench/config/components/robotManagement"; //消防水泵弹窗
import comTemperatureHumidity from "@/views/workbench/config/components/temperatureHumidity"; //温湿传感器
import comLiquidLevel from "@/views/workbench/config/components/liquidLevel"; //液位传感器

import { getLocalIP } from "@/api/event/vedioRecord";
import { getHosts } from "@/api/equipment/plc/api";
import * as echarts from "echarts";
import { listUser, getUserDeptId } from "@/api/system/user";
import * as deviceApi from "@/api/equipment/device/api";
import { listLog } from "@/api/system/log";
import {
  listDept,
  listDeptExcludeChild,
  roleDeptTreeselect,
  getTreeByDeptId,
} from "@/api/system/dept";
import bg from "@/assets/cloudControl/right_button.png";
import hoverbg from "@/assets/cloudControl/right_button_hover2.png";
import {
  getSystemWarningMsg,
  getTrafficIncident,
  proportionOfEquipment,
  trafficFlowInformation,
  vehicleMonitoringInRecent24Hours,
  special,
  getDeviceData,
  addBoardContent,
  batchControlCarFinger,
  timeSharing,
  updateControlTime,
  timeStrategySwitch,
  specialVehicleMonitoringInRecent24Hours,
} from "@/api/workbench/config.js";
import {
  getDeviceBase,
  getNewBoardEditInfo,
  templateList,
  batchControlDevice,
  getCategoryTree,
  carSwitchType,
} from "@/api/workbench/config";
import BatteryIcon from "@/components/BatteryIcon";
import {listEvent, getWarnEvent, getReservePlanDataa} from "@/api/event/event";
import { getVehicleSelectList } from "@/api/surveyType/api"; //车辆类型
import { list } from "@/api/monitor/logininfor";

let configData = {}; //配置信息
let wrapperClientX = 0;
let wrapperClientY = 0;
let boxEqList = [];
let mode = "";
export default {
  carShow: false, //车辆是否
  name: "Workbench",
  dicts: ["sd_sys_name", "sys_common_status", "sd_control_type"],
  components: {
    // DragItDude,
    videoPlayer,
    vueSeamlessScroll,
    vmsContentUpdate,
    BatteryIcon,
    contentBatchEdit,
    comVideo,
    comLight,
    comCovi,
    comBright,
    comWind,
    comPressure,
    comVehicleDetec,
    comCallPolice,
    comRobot,
    comData,
    comYoudao,
    comBoard,
    comRadio,
    comXfsb,
    comSjb,//深水泵
    robot,
    comTemperatureHumidity, //温湿度传感器
    comLiquidLevel,//液位传感器
  },

  data() {
    return {
      dialogEqType:'',
      loginStatusOptions:[],
      timingStrategyDisabled:false,
      videoNoPic1:false,
      videoNoPic2:false,
      videoTitle1:'',
      videoTitle2:'',
      videoTitle3:'',
      videoTitle4:'',

      footChangeRadio: "图表",
      syxt_boxShow: false,
      syxt_boxShow2: false,
      sycz_boxShow1: false,
      sycz_boxShow3: false,
      rccl_boxShow: false,
      yjcl_boxShow: false,
      treeShow: false,
      //地图复位按钮
      resetCanvasFlag : false,
      //搜索树状数据
      treeData: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      boardObj: {},
      fileNamesList: [],
      phoneForm1: {
        loopCount: "1",
        loop: false,
        volume: 0,
        fileNames: [],
      },
      phoneForm2: {
        loopCount: 1,
        loop: false,
        volume: 0,
        fileNames: [],
      },
      strategyTypeOptions: [],
      strategyTypeEvent:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tunnelId: null,
        strategyName: null,
        strategyType: null,
        strategyInfo: null,
        schedulerTime: null,
        jobTime: null,
        strategyGroup: 1,
      },
      dictCode: "0",
      strategyTypeGroup: [],
      strategyActive: "richang",
      total2: 0,
      total1: 0,
      operationList1: [],
      operationList2: [],
      operationParam: {
        ipaddr: "",
        userName: "",
        status: "",
        eqTypeId: "",
        tunnelId: "",
        controlType: "",
        operIp:"",
        pageNum: 1,
        pageSize: 10,
      },

      operationParam_xt: {
        ipaddr: "",
        status: "",
        pageNum: 1,
        pageSize: 10,
      },

      dateRange: [],
      operationActive: "xitong",
      manageStation: this.$cache.local.get("manageStation"),
      heightRatio: "",
      lane: "",
      carList: new Map(),
      tunnelKm: "", //隧道实际长度
      tunnelLength: "", //隧道px长度
      chezhiDisabled: false, //车指按钮 返回接口结果前禁用
      // 批量选择设备图标
      iconWidth: "",
      iconHeight: "",
      // 多选框选中项表单
      batchManageForm: {
        state: "",
        eqType: "",
        eqDirection: "",
      },
      itemEqId: [], //选中项id数组
      itemEqType: "", //判断多选的设备类型是否相同
      batchManageList: [], //多选设备数组
      addBatchManage: false, //点击多选设备
      batchManageDialog: false, //批量操作弹窗
      batchManageType: 1,
      screenEqName: "", //筛选设备名称
      timStrategyList: [], //定时控制
      BulkData: [],
      realTimeList: [], //websockt推送实时车辆数据
      eqInfo: {},
      brandList: [],
      directionList: [{}, {}], //设备方向字典
      // coviVisible: false,

      // radioEqType31: 2,
      dictList: [],
      robotShow: false,
      drawerLineList: [
        {
          value: 1,
          label: "",
        },
        {
          value: 2,
          label: "",
        },
        {
          value: 3,
          label: "",
        },
      ],
      checkList1: [],
      checkList2: [],

      //抽屉
      drawerA: false,
      drawerB: false,
      drawerCVisible: false,
      kaiGuan1: false,
      kaiGuan2: false,
      kaiGuan3: false,
      kaiGuan4: false,
      kaiGuan5: false,
      kaiGuan6: false,
      // checked1: false,
      // checked2: false,
      // checked3: false,
      // checked4: false,
      // checked5: false,
      // checked6: false,
      isDrawerCList: [
        // {
        //   str:'>12',
        //   name:'CO预警',
        //   plan:['射流风机控制执行：正转']
        // }
      ],

      value1: new Date(),
      // wheel:'wheel.prevent',
      buttonIndex: 0,
      sevenDaysBefore: [],
      trafficJamList: [],
      operationLogDialog: false, //操作日志弹窗
      //设备类型
      eqTypeData: {},
      tunnelData: [],
      //所属隧道
      eqTunnelData: {},
      // 设备方向字典
      directionOptions: [],
      setoptions: {
        // 时间不能大于当前时间
        disabledDate(time) {
          let current_time = new Date().format('yyyy-MM-dd')+' 23:59:59';  //时间日期为：‘当前日期 23:59:59’
          let t = new Date(current_time).getTime(); //‘当前日期 23:59:59’的时间戳
          return time.getTime() > t;
        },
        selectableRange: '00:00:00 - 23:59:59'
      },
      // 日期范围
      dateRange1: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        eqTypeId: null,
        tunnelId: null,
        userName: null,
        eqId: null,
        direction:null,
        /* eqName: null, */
        code: null,
        cmd: null,
        beforeState: null,
        operationState: null,
        controlType: null,
        state: null,
        description: null,
        strategyName: null,
        strategyType: null,
        strategyInfo: null,
        schedulerTime: null,
        jobTime: null,
        strategyGroup: 1,
      },
      // 操作日志表格数据
      logList: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 控制方式   3：手动 1：时间控制 2：光强控制字典
      controlTypeOptions: [],
      //操作状态 0：成功，1：失败
      operationStateOptions: [],
      total: 0,
      // 进度条
      customColors: [
        {
          color: "#f56c6c",
          percentage: 20,
        },
        {
          color: "#e6a23c",
          percentage: 40,
        },
        {
          color: "#5cb87a",
          percentage: 60,
        },
        {
          color: "#1989fa",
          percentage: 80,
        },
        {
          color: "#6f7ad3",
          percentage: 100,
        },
      ],
      percentage: 70,
      tunnelNameEarlyWarn: "",
      tunnelId: "",
      displayThumbnail: true,
      SysEarlyWarning: [],
      trafficList: [],
      hour1: "",
      hour2: "",
      hour3: "",

      selectSite: "",
      siteList: null,
      linealDeptTree: [],
      /*siteProps: {
        value: "deptId",
        label: "deptName",
        children: "children",
      },*/

      siteProps: {
        value: "id",
        label: "label",
        children: "children",
      },

      seamless: false, //情报板轮播
      /* ---------火灾报警---------------*/
      alarmBell: false, //是否启用报警铃声
      tunnelName: "",
      temperature: "",
      HZdialogVisible: false,
      controlHeightVisible: false,
      carIcon: require("@/assets/icons/carIcon.png"),
      energyIcon: require("@/assets/icons/energyIcon.png"),
      keyVehiclesIcon: require("@/assets/icons/keyVehiclesIcon.png"),
      warningIcon: require("@/assets/icons/warningIcon.png"),
      railingList: [
        {
          position: {
            top: 85,
            left: 0,
          },
          height: 2,
          image: require("@/assets/logo/equipment_log/railing.png"),
          tunnelName: "凤凰山隧道",
        },
      ],
      /* ---------工作台轮播情报版---------------*/
      newsList: [
        {
          title: "下雨路滑 请谨慎驾驶",
        },
        {
          title: "文明出行 安全第一",
        },
      ],
      /* ---------鼠标右键拖动---------------*/
      mouseLeft: 0,
      mouseTop: 0,
      curX: 0,
      curY: 0,
      dragFlag: false,
      wheelFlag: null,
      oldWidth: 0,
      oldHeight: 0,
      scaleX: 1,
      scaleY: 1,
      newWidth: 0,
      newHeight: 0,
      bgX: 0,
      bgY: 0,
      ww: null,
      wh: null,
      imgw: null,
      imgh: null,
      scaleSize: null,
      testDivDom: null,
      dragImgDom: null,
      wi: null,
      hi: null,
      /* ---------鼠标拖动---------------*/
      tunnelName: "",
      move: false,
      py: "", //开始y轴的位置
      px: "", //开始x轴的位置
      back: "#1E90FF", //框框背景颜色
      h: "", //框框的高度控制
      w: "", //框框的宽度控制
      top: "", //框框的位置控制
      left: "", //框框的位置控制
      len: 0, //框框的边框
      tabLeft: 0,
      tabTop: 0,
      whiteLight: 0,
      yellowLight: 0,
      boxTypeList: [],
      systemState: "良好",
      moveTop: 0.11,
      /* -------------------------*/
      timer: null, //定时器
      windowHeight: document.documentElement.clientHeight, //实时屏幕高度
      displayNumb: false, //显示编号
      zoomSwitch: false, //缩放
      currentTunnel: {
        id: "",
        name: "",
        lane: {},
      }, //选中的隧道
      lightState: "正常",
      dljbObj: {},
      lmList: {},
      lumianList: {},
      loading: true,
      title: "", //对话框标题
      tunnelVisible: false,
      stateSwitchVisible: false,
      batchVisible: false,
      lumianVisible: false,
      weiboVisible: false,
      cameraVisible: false,
      daolujiebingVisible: false,
      lightControlDialog: false,
      youdaoVisible: false,
      cameraErrorInfo: "",
      explainVisible: false,
      eqTypeList: [], //设备类型
      eqBigTypeList: [], //设备类型
      selectBigType: {
        bigType: "全部设备",
        index: 0,
      },
      checkboxTunnel: [], //可点的隧道
      tunnelList: [], //隧道
      selectedIconList: [], //配置图标
      stateForm: {
        state: 1,
      }, //配置表单
      brightness: 50,
      frequency: 69,
      weiboList: [],
      shuibengVisible: false,
      shuibengObj: {},
      batchForm: {
        eqType: "",
        eqList: [],
        state: "",
        eqlane: "",
        eqDirection: "",
      },
      lightSwitch: 0,
      //车道按钮样式
      // tunnelBtnStyle: [
      //   {
      //     "border-color": "#ffbf1d",
      //     // "box-shadow": "0px 0px 10px #e2f3fa inset",
      //     "color": "#0a88bd",
      //     "background-color": "#e2f3fa !important",
      //   },
      // ],
      /* playerOptions: {
          playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
          autoplay: false, // 如果为true,浏览器准备好时开始回放。
          muted: false, // 默认情况下将会消除任何音频。
          loop: false, // 是否视频一结束就重新开始。
          preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
          language: 'zh-CN',
          aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
          fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
          sources: [{
            type: "video/mp4", // 类型
            src: require('D:/123.mp4') // url地址
          }],
          poster: '', // 封面地址
          notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
          controlBar: {
            timeDivider: true, // 当前时间和持续时间的分隔符
            durationDisplay: true, // 显示持续时间
            remainingTimeDisplay: false, // 是否显示剩余时间功能
            fullscreenToggle: true // 是否显示全屏按钮
          }
        }, */

      timer: null,

      //车道列表
      laneUrlList: laneImage,
      //画布上下的传感器数据集合
      upList: [],
      downList: [],
      devicesList: [],
      // eqIcon: icon,
      //各状态对应图标列表（灵活性差，后期最好在类型状态管理中动态添加）
      eqTypeStateList: [],
      eqTypeStateList2: [],
      itemEqTypeStateList: [],
      // 策略visible
      strategyVisible: null,
      strategyList: [],
      asd: 50,
      fjState: null,
      hostIP: null,
      userDeptId: null,
      tunnelQueryParams: {
        deptId: "",
        // deptName: "",
      },
      userQueryParams: {
        userName: this.$store.state.user.name,
      },
      //验证
      rules: {
        state: [
          {
            required: true,
            message: "请选择设备状态",
            trigger: "change",
          },
        ],
      },
      systemStateClass: {
        良好: "system-state-normal",
        较差: "system-state-abnormal",
      },
      //table样式
      // rowStyle: {
      //   "background-color": "#304156",
      //   color: "#C0CCDA",
      // },
      // explainRowStyle: {
      //   "background-color": "#304156",
      //   color: "#fff",
      // },
      // explainHeaderStyle: {
      //   "background-color": "#304156",
      //   color: "#fff",
      //   "border-color": "#304156",
      // },
      leftDirection: "潍坊", //左侧方向
      rightDirection: "济南", //右侧方向
      batchFormDirection: 1, //批量对话框默认的设备方向
      twinkleModelOptions: [
        {
          value: "01",
          label: "单闪",
        },
        {
          value: "02",
          label: "双闪",
        },
        {
          value: "03",
          label: "三闪",
        },
        {
          value: "04",
          label: "四闪",
        },
        {
          value: "05",
          label: "五闪",
        },
      ],
      twinkleFrequencyOptions: [
        {
          value: "20",
          label: "20",
        },
        {
          value: "30",
          label: "30",
        },
        {
          value: "45",
          label: "45",
        },
        {
          value: "60",
          label: "60",
        },
        {
          value: "90",
          label: "90",
        },
      ],
      lightTimeOptions: [
        {
          value: "100",
          label: "100",
        },
        {
          value: "150",
          label: "150",
        },
        {
          value: "200",
          label: "200",
        },
        {
          value: "250",
          label: "250",
        },
        {
          value: "300",
          label: "300",
        },
        {
          value: "350",
          label: "350",
        },
        {
          value: "400",
          label: "400",
        },
        {
          value: "500",
          label: "500",
        },
      ],
      // 一键车道指示器 车道下拉框
      chezhiLaneList: [],
      chezhiLaneOptionList: [
        {
          laneId: 1,
          laneName: "一车道",
        },
        {
          laneId: 2,
          laneName: "二车道",
        },
        {
          laneId: 3,
          laneName: "三车道",
        },
        {
          laneId: 4,
          laneName: "四车道",
        },{
          laneId: 5,
          laneName: "五车道",
        },
        {
          laneId: 6,
          laneName: "六车道",
        },
        {
          laneId: 7,
          laneName: "七车道",
        },,{
          laneId: 8,
          laneName: "八车道",
        },
        {
          laneId: 9,
          laneName: "九车道",
        },

      ],
      // 一键车指状态下拉框
      chezhiStateList: [],
      // 一键车道指示器表单
      chezhiForm1: {
        lane: [],
        state: "",
      },
      chezhiForm2: {
        lane: [],
        state: "",
      },
      lightControForm: {
        index: 0,
        lCDirection: "",
        radioLightControlTop: [],
        radioLightControlBottom: [],
        state: "",
        lane: "",
        name: "",
      },
      lCTop: false,
      lCBottom: false,

      carShow: false,
      carShowTwo: false,
      carShowLeft: "",
      light:
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAYAAADOvw4PAAAAAXNSR0IArs4c6QAABUBJREFUaEPtWk9oXEUc/r63UA9JerCHQiuIVRDNoYfY6CGBrlrtIftsQ6J4Ezx4EUnxmD3uCl4aPHkTb4Uaa0wDjRR2iyJKVqRgDgGrohgv9rS7gXpIpuz7N/Nm3uzO22xA9O1ld7Pz3pf3m2++3+/3zRDRS6yiggNcAXEOwHj89xzvXQi0QFzlIjZs1xU4RmSOOG4ESADsgtH8zO/b52dtooISrkCUzoEcD6/1wnukPnu9ewKMfgvGBJ+7oNeC511leSfA6d0BYhU1CCznIFT/oUSdC6jqgwqcARE+bNxCMkWT3+/dq/NS15yfjRM1gMshoTQSpb5n/Kb/HhKuztkfqxSfYQ7AzZERLL4R4XNB3rfAcYywU9x6JIg1IlGqWLEy3mO1UdQIns/K/WTexVcn53BQupmhTBrhYlXrEU35HJBKJV+C6VNcRwNE2TEE7sMEmnwdLybpuMBxi50RNzbgsRymJhux1HSmpzVrumvy4q6cn9tnGgDLiYplEiZJiVFqtBAtSavB/9XsKVkbwIRbBHKN6nARxxOSFTiDgxemuw4XDmTcbhxrA5yI6qpslQoIqKY4p3TX4YVfJE7z2QgnJpJRa/UnVlKf6UrndXokE4OffrgRXAzXXlD3FThKEPunO87/I+O2NiZkCrMV3xmkkoW4Nd3x/LbE+WYqxLGTRRLZlhqD67UUCqIgWY71M9yiUWsmt3TH1zpy8jceFTm7O0Vx4u4vW5U405I4381EZM6jYHEqVtXLxCxINkqSOXd3KvFi9ZBqxLn7cvJvnRJmt+fc3UXKoqtL+J0vfC1xWhfSJEtUyVSmRFl15VItjUQVCyXLQTEgpWSrFG52geoluaU7XvxTTv7tMwPSmKoc+dIdpzYlzl0/wrGRSlW4rPSsP6dcPIWSDaSZVB0u7MtJuXEsJJlhVjp3dzYzE3z5nsRpTio12VDdnbVg59kvJM72G9lkVmsso8FQVVjpYpO4hLEoSKaSbEC64+UHclK+HI9IZqa7w5qZPP+TUpA/J0mWFNwO/pRDuuPkNYmz85Z8Hr1TzXL8NSLJBWf6dv9Tkg1nZtJvKwX5CWXlD9fd2cxMzmxJnO9no8I/D7GUFKp3i4oy8elPJM7P72hdrK5SmkWikizoZ9TdhnR6OEqfrMtF6b8doR83ACd/d5e5Xwd2Ofd34ieKzdOKr5TRZQ1vZnb5/B2J88MrbdCbCO0FnWhu3Z3FQ+vyqY8lzm/vtYEejsu2VMTNYOzAV/e/5/ivsgF4oXPtJOmu6a7U5Kt/KA75kw0wwsn0jfJYAaluscmpTYlz93K4I2NgxMqSZR/oqpOxwQ00+cRHEuf398PnyYpZstswkFDmAKLJ4FSEwPoQl/e/RN+DGylOKt35XNiXe3Brj1QguJ4OVsbe3SCHXDcze8/z0j2Jc2eyApTWU9swIzEzSz7Pfi5xtt+sgFxP7ynm6+7kglMWlPB8Pv6hxNldrkCIKG4jZYMf6N2/+3REv3Tn1Tn/wDxNsH68BnjRaQLr3p21uzNUA6yzvG3ifDtdA0ry1EKsNum9uz7bMUa6q3Pymomz83YNHpeThaOSOen47N1dhjrV+dgHJs5f1RqEGOFpHNZ5qlaVhd91+ACWQEwDGBuCy3sQ2IKHFfX0hX4f0Q/HzczcA7gFeCu81LGeHhG3TvoAlwBvGuSYsWWiG4fmUZU9sIdTWuFsy47TKvtAaQngNOiFOIlKZlkOip8U1lkRjrfCZz614/z6rg8hlsBS+DxOpUCqGN8DsQWBFZ6u23F2qz4oliAOwQMN5yErndtqYIc5/QAAAABJRU5ErkJggg==",
      dark: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJkAAAARCAMAAAD5Yf49AAAAAXNSR0IArs4c6QAAAUdQTFRFAAAArq6utbW1ubm5v7+/w8PDycnJzc3N09PTsLCwtra2vb29wMDAysrKzc3N1NTUsbGxt7e3wcHBzs7OsLCwtra2uLi4v7+/wcHBy8vLzc3N1dXVrq6usLCwtLS0tbW1t7e3uLi4v7+/wcHBw8PDycnJy8vLzMzMzc3N1NTU1dXVr6+vsLCwtbW1t7e3v7+/wcHBysrKzMzM1dXVr6+vsLCwtLS0uLi4vb29v7+/wsLCyMjIycnJzs7O1NTU1dXVr6+vsLCwtLS0uLi4vb29wsLCycnJzs7O1NTUr6+vsLCwsbGxsrKys7OztLS0tbW1t7e3uLi4ubm5urq6u7u7vLy8vb29vr6+v7+/wcHBwsLCw8PDxMTExcXFxsbGx8fHyMjIycnJysrKzMzMzc3Nzs7Oz8/P0NDQ0dHR0tLS09PT1NTU1dXV1E4hgwAAAEl0Uk5TAExMTExMTExMTU1NTU1NTU5OTk6EhISEhISEhL6+vr6+vr6+vr6+vr6+vvLy8vLy8vLy8vPz8/Pz8/Pz8/Pz8/T09PT09PT09GJ8jKsAAAG6SURBVEjH7dZHU8JAFMDxxQ5SLFgJKAhKsRuxK3aJGkrovffvf/YF4gwLL3jB0QPvyH928hsO+5YQomRYb/ewjIJ0ZkAxnr7DfMB8wvBu05hUVOazQDAYFIRQKBQORyLnlgmpqK2XyWQqlUqnM5lsNpe7sk1JRWu/KVcqlWq1VqvX643GrWNG/Fnv7R89+aFwHEfJeN631C7L/kCAkkWi0ZV2WY0nEpQsl8+vtct6qVSmZM1my0CIwouNkgwsHCLzqaCM+xFZTA1lMo7IChoo00VE1tIRBv0+AyfkixGVbUDZRGUWKFuobBvKDipzEhf6fReckC+7qGwPyj4qO4ByiMqOoByjshPixQdOyBcOlfmg+FFZDEr8W5bulhWgFFFZ65dkAiJL/IGMH8lGMlzW+H+yao8Mv7VY2fuMRe6zjszddZ/Rsov2fYbJrrvuM1p2N5QdwPftAAHZAb2yfM8OoGVOopTdjgMKKhP3pgqViXtTjcrEvalFZbqhvDX4vreGgLw1EBn11qBkBjHMMR7q4x5GKb2bBhTjPSV7MKmkMm9+pGRPllmpLFifKdmLTSOVRfsrJXtzwD/2Betpxj8VdhGZAAAAAElFTkSuQmCC",
      allDirection: [
        {
          eqDirection: "0",
          eqDirectionName: "济南方向",
        },
        {
          eqDirection: "1",
          eqDirectionName: "潍坊方向",
        },
      ], // 所有方向
      allLane: [], // 所有车道
      dialogVisible: false, // 情报板批量编辑弹窗
      aaa: "444",
      zoomList: [70, 80, 90, 100, 110, 120, 130], // 工作台缩放比例列表
      zoom: 100, // 工作台缩放比例
      spanEqtypeDate: true, // 控制设备配置状态不存在时显示“暂未获取”
      // 巡检机器人----------------------------------------------------------------start
      robotUrl: require("@/assets/images/robot.png"),
      watchVideo: false,
      robotVideoUrl: require("@/assets/Example/v1.mp4"),
      // tab页
      robotInformationActive: "trafficFlow",
      robotInformationCollectionData: null,
      // 状态记录
      robotRecordList: null,
      robotChartDom: null,
      robotOption: null,
      robotStateData: null,
      robotXData: null,
      // 巡检机器人-----------------------------------------------------------------end
      isManagementStation: false, // 当前账号的权限是否是管理站
      showTooltipIndex: 999,
      showTooltip: false,
      tooltipShow: false, //是否展示提示内容
      imageTimer: null, //定时器
      isLogin: false,
      handleTableWheelSwithch: false,

      vehicleXData: [],
      vehicleYData: [],
      keyVehiclesXData: [],
      keyVehiclesYData: [],

      weiboList: [
        {
          id: 1,
          byLane: "一",
          fSpaceOccupyRation: "29",
          bySpeed: "74",
          fSpaceOccupyRation: "47",
          createTime: "2022-09-05 17:44:32",
        },
        {
          id: 2,
          byLane: "二",
          fSpaceOccupyRation: "29",
          bySpeed: "74",
          fSpaceOccupyRation: "47",
          createTime: "2022-09-05 18:54:19",
        },
        {
          id: 3,
          byLane: "三",
          fSpaceOccupyRation: "39",
          bySpeed: "54",
          fSpaceOccupyRation: "91",
          createTime: "2022-09-05 19:25:52",
        },
      ], //假数据
      clickEqType: "", //点击设备的eqType
      equipmentId: "",
      // eqInfo: {},
      liveUrl1: "",
      liveUrl2: "",
      liveUrl3: "",
      liveUrl4: "",
      cameraPlayer1: false,
      cameraPlayer2: false,
      cameraPlayer3: false,
      cameraPlayer4: false,
      tunnelItem:null,
      timer: null,
      catNumber:1,//小车计数
      catTime:null,//小车计数定时器
    };
  },

  directives: {
    //注册指令
    drag: function (el, binding) {
      let oDiv = el; //当前元素
      $(oDiv).bind(
        "mousewheel",

        function (e) {
          console.log(e);
          // 获取鼠标所在位置

          let { clientX, clientY } = e;
          // 获取元素距离屏幕边界左边和上边距离

          let offsetX = $(oDiv).offset().left;

          let offsetY = $(oDiv).offset().top;

          // 获取鼠标距离当前元素边界左边和上边距离

          let mouseToBorderX = clientX - offsetX;

          let mouseToBorderY = clientY - offsetY;

          // 获取元素width,height,left,top;注意元素为relative或absolute定位

          let width = $(oDiv).width();

          let height = $(oDiv).height();

          let left = parseFloat($(oDiv).css("left"));

          let top = parseFloat($(oDiv).css("top"));

          // 设置一下缩放幅度 ,值越大缩放的越快

          let ratio = e.originalEvent.deltaY < 0 ? 0.1 : -0.1;

          // 设置缩放后的宽高

          width = width * (1 + ratio);

          height = height * (1 + ratio);

          // 这里是关键一步

          // 可以想象,当元素宽度增加0.1倍,如果此时元素left值不变化，那么元素是会向右变大的，那么鼠标相

          // 对元素左上角的距离与最开始的距离相比就变了，变化量为mouseToBorderX*ratio，那么让元素left

          //  减去变化量即可保证鼠标相对元素不动

          left = left - mouseToBorderX * ratio;

          top = top - mouseToBorderY * ratio;

          if (width > 100) {
            $(oDiv).css({
              width: width + "px",

              height: height + "px",

              left: left + "px",

              top: top + "px",
            });
          }
        }
      );
    },
  },
  created() {
    debugger
    //小车运行渲染定时任务
    clearInterval(this.timer)
    this.timer = null
    this.carSetTimer()
    this.getUserDept();
    this.getDicts("sd_strategy_direction").then((data) => {
      console.log(data, "方向");
      this.directionList = data.data;
    });
    this.getDicts("sys_common_status").then((response) => {
      this.loginStatusOptions = response.data;
    });
    // this.flvPlayer()
    this.trafficFlowLane();
    this.getEqTypeStateIcon();
    this.getWarnList();
    // this.$store.dispatch('app/toggleSideBar')
    getLocalIP().then((response) => {
      this.hostIP = response;
    });

    // this.getTunnelList();
    // this.selectEquipmentType()
    // this.initWebSocket()
    this.lightSwitchFunc();
    this.getEqType();
    this.getTunnel();
    listTunnels().then((response) => {
      this.tunnelData = response.rows;
    });
    // 策略类型
    this.getDicts("sd_strategy_type").then((response) => {
      console.log("策略类型",response)
      this.strategyTypeOptions = response.data;
    });
    this.getDicts("sys_common_event").then((response) => {
      this.strategyTypeEvent = response.data;
      console.log(this.strategyTypeEvent, "this.strategyTypeEvent");
    });
    this.getDicts("sd_control_type").then((response) => {
      this.controlTypeOptions = response.data;
    });
    // 策略组信息
    this.getDicts("sd_strategy_group").then((response) => {
      this.strategyTypeGroup = response.data;
      console.log(this.strategyTypeGroup, "this.strategyTypeGroup");
    });
    this.getDicts("sd_device_opt_state").then((response) => {
      console.log(response.data, "操作结果");
      this.operationStateOptions = response.data;
    });
    this.getDicts("brand").then((data) => {
      console.log(data, "设备厂商");
      this.brandList = data.data;
    });

    this.getDicts("sd_monitor_state").then((data) => {
      console.log(data, "设备类型");
      this.eqTypeDialogList = data.data;
    });
    /*this.getDicts("sd_wj_vehicle_type").then((data) => {
      console.log(data, "车型列表");
      this.vehicleTypeList = data.data;
    });*/
    getVehicleSelectList({}).then((response) => {
      console.log(response, "车辆类型");
      this.vehicleTypeList = response;
    });

    this.getDicts("sd_strategy_direction").then((response) => {
      response.data.forEach((item) => {
        this.directionOptions.push(item);
      });
    });
    this.getTunnelState();
    // this.carchange();
    //调取滚动条
    this.srollAuto();

  },
  watch: {
    // 工作台搜索关键词匹配
    screenEqName(val) {
      this.$refs.tree.filter(val);
    },
    // tunnelList: function (newVal, oldVal) {
    //   console.log(newVal, "8888888888888888");
    // },
    "$store.state.manage.manageStationSelect": function (newVal, oldVal) {
      console.log(newVal, "监听到隧道啦监听到隧道啦监听到隧道啦监听到隧道啦");

      if (this.manageStation == "1") {
        getJlyTunnel().then((res) => {
          var arr = [];
          for (let item of res.data) {
            if (newVal == item.tunnelId) {
              var atr = item.ancestors.slice(2);
              arr = atr.split(",");
              arr.push(item.deptId);
              this.tunnelQueryParams.deptId = item.deptId;
              this.changeSite(arr);
            }
          }
        });
      }
      console.log(11111111111111);
      this.getTunnelList();
    },
    sdEventList(event) {
      debugger
      console.log(event, "websockt工作台接收事件弹窗");
      for (var item of event) {
        this.trafficList.unshift(item);
      }
    },
    sdSvgEventList(){
      debugger
      console.log(event, "websockt工作台接收事件弹窗");
      switch (event.eventTypeId){
        case "18" :

          break
        case "12": //交通事故

          break
        default:
      }
    },
    radarDataList(event) {
      console.log(event)
      let tunnelItems = null;
      if(!!this.tunnelItem){
        tunnelItems = this.tunnelItem
      }else{
        tunnelItems = this.tunnelList[0]
      }
      if(tunnelItems.tunnelId!=event[0].tunnelId){
        return;
      }

      console.log(this.catNumber)
      if(this.catNumber>20){
        debugger
        this.carList.clear()
        this.catNumber=1
      }
      for (let i = 0; i < event.length; i++) {
        //车辆实际经度
        var lng = Number(event[i].lng);

        //车辆实际距离入口距离
        var carKm = event[i].distance;

        // 根据车道数进行判断
        if (this.lane == 2) {
          console.log(this.lane, "66666666666");
          if (event[i].laneNo == 1) {
            event[i].top = 360 + "px";
          } else if (event[i].laneNo == 2) {
            event[i].top = 450 + "px";
          }
        }
        //计算
        let imgId =  document.getElementById("config-img-id")
        //上
        let topHigh = 73/580
        //上道
        let centreHighs = 60/580
        //中
        let centreHigh = 57/580
        //下道
        let nextHigh = 73/580


        if(event[i].roadDir == 1){//上行
          //计算最终经度 右边距
          event[i].right =
            math.add(math.multiply(+carKm * this.proportion) + 200) + "px";
          event[i].left =""
          //计算经度  上边距
          switch (event[i].laneNo) {
            case "3":
              event[i].top = imgId.offsetHeight*topHigh+(imgId.offsetHeight*topHigh/3)+ "px";
              break;
            case "2":
              event[i].top = imgId.offsetHeight*topHigh+imgId.offsetHeight*centreHighs+(imgId.offsetHeight*centreHighs/3)+ "px";
              break;
            case "1":
              event[i].top = imgId.offsetHeight*topHigh+imgId.offsetHeight*centreHighs*2+(imgId.offsetHeight*centreHighs/3)+ "px";
              break;
            default:
          }
        }else if(event[i].roadDir==2){//下行
          //计算最终经度 左边距
          event[i].left =
            math.add(math.multiply(+carKm * this.proportion) + 190) + "px";
          event[i].right = ""
          //计算经度  上边距
          switch (event[i].laneNo) {
            case "1":
              event[i].top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*3+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
              break;
            case "2":
              event[i].top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*4+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
              break;
            case "3":
              event[i].top = ( imgId.offsetHeight*topHigh + imgId.offsetHeight*centreHighs*5+imgId.offsetHeight*centreHigh)+(imgId.offsetHeight*topHigh/2)+ "px";
              break;
            default:
          }
        }
        console.log(math.add(math.multiply(+carKm * this.proportion) + 200))
        console.log(math.add(math.multiply(+carKm * this.proportion) + 200))
        if(math.add(math.multiply(+carKm * this.proportion) + 190)>1480 || math.add(math.multiply(+carKm * this.proportion) + 200)>1480){
          this.carList.delete(event[i].vehicleLicense);
          continue
        }
        //车辆类型
        if(event[i].vehicleType == "40"){//危化车
          event[i].background = "red";
        }else if(event[i].vehicleType == "26"||event[i].vehicleType == "14"||event[i].vehicleType == "17"){//货车
          event[i].background = "yellow";
        }else if(event[i].vehicleType == "16"||event[i].vehicleType == "25"||event[i].vehicleType == "15"){//客车
          event[i].background = "blue";
        }
        // if (
        //   event[i].vehicleType == "3" ||
        //   event[i].vehicleType == "7" ||
        //   event[i].vehicleType == "8"
        // ) {
        //   event[i].background = "yellow";
        // } else {
        //   event[i].background = "yellow";
        // }
        // //是否超速 或者低俗行驶
        // if (event[i].speed > Number(120) || event[i].speed < Number(60)) {
        //   event[i].background = "red";
        // }
        this.carList.set(event[i].vehicleLicense, event[i]);
        console.log( this.carList)
      }
    },

    deviceStatus(event) {
      this.deviceStatusList = event;
    },
    // 设备类型
    "batchForm.eqType"(val) {
      console.log(val);
      console.log(mode);
      if (mode == "buttonSelection") {
        let param = {
          eqTunnelId: this.currentTunnel.id,
          eqType: val,
          lane: this.batchForm.eqlane,
          eqDirection: this.batchForm.eqDirection,
        };
        this.selectEquipment(param);
        this.selectDirection(param);
        this.selectLane(param);
      } else {
        for (let i = 0; i < boxEqList.length; i++) {
          if (val == boxEqList[i].typeId) {
            this.devicesList = boxEqList[i].eqlist;
          }
        }
      }
    },
    // 方向
    "batchForm.eqDirection"(val) {
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqType: this.batchForm.eqType,
        eqDirection: val,
        lane: this.batchForm.eqlane,
      };
      this.selectEquipment(param);
    },
    // 车道
    "batchForm.eqlane"(val) {
      let param = {
        eqTunnelId: this.currentTunnel.id,
        eqDirection: this.batchForm.eqDirection,
        lane: val,
      };
      this.selectLane(param);
    },
    batchVisible(val) {
      if (val == false) {
        this.cleanUp();
      }
    },
    stateSwitchVisible(val) {
      if (val == false) {
        this.cleanUp();
      }
    },
    w(val) {
      this.move = true;
    },
    // 监听隧道温度
    temperatureList: {
      handler(val) {
        for (var item of val) {
          if (item.temperature >= 50) {
            this.temperature = item.temperature;
            this.tunnelName = item.tunnelName;
            this.HZdialogVisible = true;
            // 当设备异常时
            this.$refs.audio.currentTime = 0; //从头开始播放提示音
            this.$refs.audio.play(); //播放
          } else {
            this.HZdialogVisible = false;
          }
        }
      },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true,
    },
    // 监听声音开关按钮
    alarmBell: {
      handler(val) {
        if ((val = true)) {
          this.$refs.audio.currentTime = 0; //从头开始播放提示音
          this.$refs.audio.play(); //播放
          // 谷歌浏览器禁止一打开就播放声音 所以需要通过开关打开声音
          // 打开后会声音直接播放 所以设置10毫秒 用户就不会听到了
          setTimeout(() => {
            this.$refs.audio.pause();
            this.$refs.audio.load();
          }, 10);
        } else {
          this.$refs.audio.pause();
          this.$refs.audio.load();
        }
      },
      // 这里是关键，代表递归监听 demo 的变化
      deep: true,
    },
  },
  computed: {
    defaultOption() {
      return {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      };
    },
    // 监听属性 类似于data概念
    optionLeft() {
      return {
        step: 0.4,
        direction: 1, // 0向下 1向上 2向左 3向右
        limitMoveNum: 2,
        waitTime: 1000,
      };
    },
    navigationHeight() {
      this.topNav = this.$store.state.settings.topNav;
      const needTagsView = this.$store.state.settings.tagsView;
      let h = 0;
      h += 72;
      // if (!this.topNav) {
      //   if (needTagsView) h += 22;
      // }

      return h;
    },
    ...mapState({
      deviceStatus: (state) => state.websocket.deviceStatus,
      radarDataList: (state) => state.websocket.radarDataList,
      sdEventList: (state) => state.websocket.sdEventList,
      sdSvgEventList: (state) => state.websocket.sdSvgEventList,
    }),
    sideTheme: {
      get() {
        return this.$store.state.settings.sideTheme;
      },
    },
  },
  mounted() {
    window.addEventListener("click", this.otherClose);
    $(document).on("click", function (e) {
      let dom = $(".treebox")[0]; // 自定义div的class
      console.log(dom);
      if (dom && that.treeShow) {
        // 如果点击的区域不在自定义dom范围
        if (!dom.contains(e.target) && that.treeShow == true) {
          that.treeShow = false;
        }
      }
    });
    // document.addEventListener("click", (e) => {
    //   if (!this.$refs.treeBox.contains(e.target)) this.treeShow = false;
    // });
    // this.initEnergyConsumption();
    this.getTimeData();
    // this.vehicleEcharts()
    // this.specialEcharts();
    let that = this;
    window.onresize = () => {
      return (() => {
        this.windowHeight = document.documentElement.clientHeight;
      })();
    };

    // 隧道调取数据两秒一次
    this.timer = setInterval(() => {
      setTimeout(this.getRealTimeData, 0);
      // setTimeout(this.getLiPowerDevice, 0)
    }, 1000 * 5);

    // 模拟实时隧道温度 调完了再删
    // setInterval(()=>{
    //         this.temperatureList[1].temperature += 2

    // },2000)
    // setInterval(()=>{
    //         this.temperatureList[1].temperature -= 20

    // },22000)
    // console.log(this.temperatureList[1].temperature,"隧道温度")
    // 一进页面判断温度超过50度 弹窗提示
    // for (var item of this.temperatureList) {
    //   if (item.temperature >= 50) {
    //     this.HZdialogVisible = true
    //     this.temperature = item.temperature
    //     this.tunnelName = item.tunnelName
    //   }
    // }
    // this.initeChartsEnd();
    // this.loadFocusCar();
    // this.srollAuto()
    document.addEventListener("click", this.bodyCloseMenus);
    document.addEventListener("click", this.bodyCloseMenus1);
    document.addEventListener("click", this.bodyCloseMenus2);
    document.addEventListener("click", this.bodyCloseMenus3);

  },

  methods: {
    carSetTimer() {
      if(this.timer == null) {
        this.timer = setInterval( () => {
          if( this.carShow == true){
            this.carShow = false
            this.carShow = true
          }
          // this.$forceUpdate();
        }, 200)
      }
    },
    async  destroyedDelete () {

      await  carSwitchType("destroyed", 1).then((res) =>{})
    },
    pollFormat(row) {
      return this.selectDictLabel(this.loginStatusOptions, row);
    },
    // 参数timer是过去的n个小时
    getPastTime() {
      //alert(this.timeFormat(new Date(new Date().setHours(0, 0, 0, 0)).getTime()));
      // 获取过去的时间
      //const lastTime = new Date().getTime() - `${timer * 60 * 60 * 1000}`;
      //获取当天0点时间
      const lastTime = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
      const startTime = this.timeFormat(lastTime);
      // 当天24点时间
      let time = new Date(new Date().setHours(0, 0, 0, 0) + 24 * 60 * 60 * 1000 - 1).getTime();
      const endTime = this.timeFormat(time);
      return [startTime, endTime];

    },

    //时间生成并处理
    timeFormat(time) {
      // 对应的方法
      const timeType = [
        "getFullYear",
        "getMonth",
        "getDate",
        "getHours",
        "getMinutes",
        "getSeconds"
      ];
      // 分隔符
      const separator = {
        getFullYear: "-",
        getMonth: "-",
        getDate: " ",
        getHours: ":",
        getMinutes: ":",
        getSeconds: ""
      };
      let resStr = "";
      for (let i = 0; i < timeType.length; i++) {
        const element = timeType[i];
        let resTime = new Date(time)[element]();
        // 获取月份的要+1
        resTime = element == "getMonth" ? resTime + 1 : resTime;
        // 小于10，前面加0
        resTime = resTime > 9 ? resTime : "0" + resTime;
        resStr = resStr + resTime + separator[element];
      }
      return resStr;
    },


    changeEndTime(start,end,index){
      console.log(start,end,"start,end")
      let date = new Date();
      let a = start.split(":");
      let b = end.split(":");
      let bool = date.setHours(a[0],a[1]) > date.setHours(b[0],b[1])
      if(bool){
        this.$modal.msgWarning("结束时间必须大于开始时间");
        console.log(this.timStrategyList,"this.timStrategyList")
        for(let i=0;i< this.timStrategyList.length;i++){
          this.timStrategyList[index].arr[1] = ''
          this.timingStrategyDisabled = true

        }
      }else{
        this.timingStrategyDisabled = false
      }
    },
 //翻页时不刷新序号
    indexMethod(index){
      return index+(this.operationParam_xt.pageNum-1)*this.operationParam_xt.pageSize+1
    },
    //翻页时不刷新序号
    indexMethod2(index){
      return index+(this.operationParam.pageNum-1)*this.operationParam.pageSize+1
    },
    //翻页时不刷新序号
    indexMethod3(index){
      return index+(this.queryParams.pageNum-1)*this.queryParams.pageSize+1
    },
    videoRadioChange() {
      if (this.footChangeRadio == "视频" && this.tunnelId) {
        this.getFooterVideo();
      } else {
        this.cameraPlayer1 = false;
        this.cameraPlayer2 = false;
        this.cameraPlayer3 = false;
        this.cameraPlayer4 = false;
      }
    },
    getFooterVideo() {
        // 潍坊方向
        getEntranceExitVideo(this.tunnelId, this.directionList[0].dictValue).then((res) => {
          if(res.data.length == 0){
            this.videoNoPic2 = true
          }else{
          this.videoTitle3 = res.data[0].inletName;
          this.videoTitle4 = res.data[0].outletName;
          console.log(res,"后两个视频")
          if (this.tunnelId == "WLJD-JiNan-YanJiuYuan-FHS") {
            getDeviceById(res.data[0].inlet).then((response)=>{
                displayH5sVideoAll(response.data.secureKey,'h5sVideo4',3);
            })
            getDeviceById(res.data[0].outlet).then((response)=>{
                displayH5sVideoAll(response.data.secureKey,'h5sVideo5',4);
            })
          }else{
            videoStreaming(res.data[0].inlet).then((res) => {
              console.log(res,'入口视频')
            if(res.code == 200 && res.data) {
              this.liveUrl1 = res.data.liveUrl;
              this.cameraPlayer1 = true;
            }else{
                this.$modal.msgWarning("获取视频失败");
              }
          });
          videoStreaming(res.data[0].outlet).then((res) => {
            if(res.code == 200 && res.data) {
              this.liveUrl2 = res.data.liveUrl;
              this.cameraPlayer2 = true;
            }else{
              this.$modal.msgWarning("获取视频失败");
            }
          });
          }
          }


        });
        // 济南方向
        getEntranceExitVideo(this.tunnelId, this.directionList[1].dictValue).then((res) => {
          console.log(res,"前两个视频")
          if(res.data.length == 0){
            this.videoNoPic1 = true
          }else{
          this.videoTitle1 = res.data[0].inletName;
          this.videoTitle2 = res.data[0].outletName;
          if (this.tunnelId == "WLJD-JiNan-YanJiuYuan-FHS") {
            getDeviceById(res.data[0].inlet).then((response)=>{
              console.log(response,"0000000000000")
                displayH5sVideoAll(response.data.secureKey,'h5sVideo2',1);
            })
            getDeviceById(res.data[0].outlet).then((response)=>{
                displayH5sVideoAll(response.data.secureKey,'h5sVideo3',2);
            })
          }else{
            videoStreaming(res.data[0].inlet).then((res) => {
              console.log(res,'入口视频')
              if(res.code == 200 && res.data){
                this.liveUrl3 = res.data.liveUrl;
                this.cameraPlayer3 = true;
              }else{
                this.$modal.msgWarning("获取视频失败");
              }

            });
            videoStreaming(res.data[0].outlet).then((res) => {
            if(res.code == 200 && res.data) {
              this.liveUrl4 = res.data.liveUrl;
              this.cameraPlayer4 = true;
            }else{
              this.$modal.msgWarning("获取视频失败");
            }
            });
          }
          }
        });

    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    beforeDestroy() {
      document.removeEventListener("click", this.bodyCloseMenus);
      document.removeEventListener("click", this.bodyCloseMenus1);
      document.removeEventListener("click", this.bodyCloseMenus2);
      document.removeEventListener("click", this.bodyCloseMenus3);

    },
    bodyCloseMenus(e) {
      let self = this;
      if(self.syxt_boxShow == true){
        self.$nextTick(()=>{
          if (
            !self.$refs.main.contains(e.target) &&
            !self.$refs.cc.contains(e.target)
          ) {
            if (self.syxt_boxShow == true) {
              self.syxt_boxShow = false;
            }
          }
        })
      }

    },
    bodyCloseMenus1(e) {
      let self = this;
      if(self.sycz_boxShow1 == true){
        self.$nextTick(()=>{
          if (
            !self.$refs.main1.contains(e.target) &&
            !self.$refs.cc1.contains(e.target)
          ) {
            if (self.sycz_boxShow1 == true) {
              self.sycz_boxShow1 = false;
            }
          }
        })
      }


    },
    bodyCloseMenus2(e) {
      let self = this;
      if(self.syxt_boxShow2 == true){
        self.$nextTick(()=>{
          if (
            !self.$refs.main2.contains(e.target) &&
            !self.$refs.cc2.contains(e.target)
          ) {
            if (self.syxt_boxShow2 == true) {
              self.syxt_boxShow2 = false;
            }
          }
        })
      }


    },
    bodyCloseMenus3(e) {
      let self = this;
      if(self.sycz_boxShow3 == true){
        self.$nextTick(()=>{
          if (
            !self.$refs.main3.contains(e.target) &&
            !self.$refs.cc3.contains(e.target)
          ) {
            if (self.sycz_boxShow3 == true) {
              self.sycz_boxShow3 = false;
            }
          }
        })
      }


    },
    otherClose(e) {
      if(this.treeShow == true){
        if (!this.$refs.treeBox.contains(e.target)) this.treeShow = false;
      }
    },
    treeClear() {
      for (var item of this.selectedIconList) {
        if (item.eqName.indexOf(this.screenEqName) > -1) {
          console.log(item.eqName);
          item.click = false;
        }
      }
    },
    // 模糊查询
    treeClick() {
      console.log(this.screenEqName);
      this.treeShow = !this.treeShow;
    },
    //点击树状图获取值
    handleNodeClick(data) {
      console.log(data,"data");
      // 如果存在children，则代表是父级
      if(data.children){
        // 点击父级业务
      }else{
        this.treeShow = false;
        console.log(data.label);
        this.screenEqName = data.label;
        this.screenEqNameButton(data.label);
      }
    },
    // 筛选设备名称
    // screenEqNameButton(screenEqName) {
    //   // console.log(screenEqName);
    //   for (var item of this.selectedIconList) {
    //     if (item.eqName.indexOf(screenEqName) != -1) {
    //       item.click = true;
    //     } else {
    //       item.click = false;
    //     }
    //   }
    // },
    changeStrategyState(row) {
      let data = { strategyId: row.id, change: row.strategyState };
      updateState(data).then((result) => {
        if(row.strategyState == 0){
          this.$modal.msgSuccess('开启成功');
        }else if(row.strategyState == 1){
          this.$modal.msgSuccess('关闭成功');
        }
      });
    },
    directionFormat(row, column) {
      return this.selectDictLabel(this.directionList, row.direction);
    },
    // 策略类型字典翻译
    strategyTypeFormat(row, column) {
      return this.selectDictLabel(this.strategyTypeOptions, row.strategyType);
    },
    // 预警字典值翻译
    strategyTypeFormatEvent(row, column){
      return this.selectDictLabel(this.strategyTypeEvent, row.strategyType);
    },
    // 点击侧边栏文件列表下拉框
    clickFileNames(direction) {
      const params = {
        tunnelId: this.tunnelId,
        direction: direction,
      };
      getAudioFileList(params).then((res) => {
        console.log(res, "广播一键文件列表");
        this.fileNamesList = res.data;
      });
    },

    getBoardStyle(id, type, eqType) {
      if (this.boardObj[id]) {
        if (JSON.parse(this.boardObj[id]).content) {
          let content = JSON.parse(this.boardObj[id]).content;
          let devicePixel = JSON.parse(this.boardObj[id]).devicePixel;
          if (type == "width") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[1] / 2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[1] / 4;
            }
          } else if (type == "height") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[0] / 2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[0] / 4;
            }
          }
          let array = [];
          let arr = "";
          let fontS = "";
          for (let i = 0; i < content.length; i++) {
            var itemId = "ITEM" + this.formatNum(i, 3);
            var con = content[i][itemId][0];
            con.CONTENT = con.CONTENT.replace("<br>", " ").replace(
              " &nbsp",
              " "
            );
            array.push(con);
            arr += con.CONTENT.replace("<br>", " ").replace(" &nbsp", " ");
            arr += " ";
            fontS = Number(con.FONT_SIZE.substring(0, 2));
          }

          if (type == "content") {
            return arr;
          } else if (type == "fontSize") {
            if (eqType && eqType == 16) {
              return fontS / 2;
            } else if (eqType && eqType == 36) {
              return fontS / 4;
            }
          } else if (type == "array") {
            return array;
          }
        } else {
          let devicePixel = JSON.parse(this.boardObj[id]).devicePixel;
          if (type == "width") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[1] / 2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[1] / 4;
            }
          } else if (type == "height") {
            if (eqType && eqType == 16) {
              return devicePixel.split("*")[0] / 2;
            } else if (eqType && eqType == 36) {
              return devicePixel.split("*")[0] / 4;
            }
          } else if (type == "content") {
            return "山东高速欢迎您";
          } else if (type == "fontSize") {
            return 15;
          } else if (type == "array") {
            let array = [{ CONTENT: "山东高速欢迎您", COLOR: "黄色" }];
            return array;
          }
        }
      } else {
        if (type == "width") {
          return 24;
        } else if (type == "height") {
          return 72;
        } else if (type == "content") {
          return "山东高速欢迎您";
        }
      }
    },
    formatNum(num, length) {
      return (Array(length).join("0") + parseInt(num)).slice(-length);
    },
    // 转颜色
    getColorStyle(font) {
      if (font == "黄色") {
        return "yellow";
      } else if (font == "红色") {
        return "red";
      } else if (font == "绿色") {
        return "#00FF00";
      } else if (font == "蓝色") {
        return "blue";
      } else {
        return font;
      }
    },
    phoneControl(direction) {
      if (direction == 1) {
        const param = {
          lib: "YeastarHost",
          loop: this.phoneForm1.loop,
          loopCount: this.phoneForm1.loopCount,
          volume: this.phoneForm1.volume,
          fileNames: Array(this.phoneForm1.fileNames),
          direction: direction,
          tunnelId: this.currentTunnel.id,
          controlType: "0",
        };
        console.log(param, "param");
        playVoiceGroup(param).then((res) => {});
      } else {
        const param = {
          lib: "YeastarHost",
          loop: this.phoneForm2.loop,
          loopCount: this.phoneForm2.loopCount,
          volume: this.phoneForm2.volume,
          fileNames: Array(this.phoneForm2.fileNames),
          direction: direction,
          tunnelId: this.currentTunnel.id,
          controlType: "0",
        };
        console.log(param, "param");
        playVoiceGroup(param).then((res) => {
          this.$modal.msgSuccess("控制成功");
        });
      }
      // console.log(direction,"广播一键控制方向");
    },
    // 广播播放控制次数
    handleChangePhone(num) {},
    // 操作日志 搜索
    handleQueryOperationParam() {
      this.operationList2 = [];
      this.syxt_boxShow = false
      this.sycz_boxShow1 = false
      this.operationParam.pageNum = 1;
      this.operationParam_xt.pageNum = 1;
      this.getOperationList(this.operationActive);
    },
    getOperationList(inx) {
      if (this.manageStation == "1") {
        this.operationParam.tunnelId = this.$cache.local.get(
          "manageStationSelect"
        );
      }
      this.loading = true;
      if ( inx == 'xitong' ) {
      list(this.addDateRange(this.operationParam_xt, this.dateRange)).then(
        (response) => {
          console.log(response, "系统日志");
          this.operationList1 = response.rows;
          this.total1 = response.total;
          this.loading = false;
        }
      );
      } else if (inx == 'caozuo' ) {

      listLog(this.addDateRange(this.operationParam, this.dateRange1)).then(
        (response) => {
          console.log(response, "操作日志");
          this.operationList2 = response.rows;
          this.total2 = response.total;
          this.loading = false;
        }
      );
      }
    },
    carShowChange(val) {
      debugger
      let tunnelItems = null;
      if(!!this.tunnelItem){
        tunnelItems = this.tunnelItem
      }else{
        tunnelItems = this.tunnelList[0]
      }
      carSwitchType(tunnelItems.tunnelId, this.carShow ? 0:1).then((res) =>{})
      this.$nextTick(() => {
        this.getEnergyConsumption(this.currentTunnel.id);
      });
      if(this.carShow){
        let that = this
        //小车展示秒表 定时清除不动的车辆
        this.catTime = setInterval(function(){
          debugger
          console.log( that.catNumber)
          that.catNumber = that.catNumber+1
        },1000)
      }else{
        clearInterval(this.catTime)
      }
      this.carShow = val;
    },
    getStartTime(time) {
      return moment(time).format("HH:mm:ss");
    },
    //     getWarnTime(time){
    // // let times = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
    //       // console.log(times,"times")
    //       var now = new Date(time)
    //       console.log(now,"8888888888888888888888888")
    //     },
    // 获取设备图片
    getTypePic(item) {
      if (item.eqId.substring(item.eqId.length - 2) == "-1") {
        return item.url[0];
      } else if (item.eqId.substring(item.eqId.length - 2) == "-2") {
        return item.url[1];
      }
    },
    // 批量操作弹窗获取方向
    getDirection(num) {
      for (var item of this.directionList) {
        if (item.dictValue == num) {
          return item.dictLabel;
        }
      }
    },
    // 批量操作 弹窗确定
    batchManageOK() {
      const param = {
        eqId: this.itemEqId.toString(),
        eqDirection: this.batchManageForm.eqDirection,
        state: this.batchManageForm.state,
      };
      batchControlDevice(param).then((res) => {
        this.$modal.msgSuccess("控制成功");
        this.batchManageDialog = false;
        this.closeBatchManageDialog();
      });
    },
    // 新版批量操作 点击变俩按钮
    batchManage() {
      this.batchManageType = 2;
      this.batchManageList = [];
      this.addBatchManage = true;
    },
    // 批量操作 执行
    implementBatchManage() {
      var that = this;
      this.title = "批量操作";
      that.eqTypeStateList2 = [];
      let eqType = "";
      for (var item of this.selectedIconList) {
        if (item.click) {
          console.log(item, "batchManageDialog");
          this.batchManageList.push(item);
          this.batchManageDialog = true;
          eqType = item.eqType;
        }
      }
      let list = [];
      const param = {
        stateTypeId: eqType,
        isControl: 1,
      };
      getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
        list = response.rows;
        for (let i = 0; i < list.length; i++) {
          let iconUrl = [];
          if (list[i].iFileList != null) {
            for (let j = 0; j < list[i].iFileList.length; j++) {
              // let img = await that.picture(list[i].iFileList[j].url);
              let img = list[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          that.eqTypeStateList2.push({
            stateType: list[i].stateType,
            type: list[i].stateTypeId,
            state: list[i].deviceState,
            name: list[i].stateName,
            control: list[i].isControl,
            url: iconUrl,
          });
        }
      });
      console.log(that.eqTypeStateList2, "that.eqTypeStateList");
    },
    // 关闭批量操作弹窗 / 批量操作取消
    closeBatchManageDialog() {
      this.batchManageDialog = false;
      this.batchManageType = 1;
      this.itemEqId = [];
      this.itemEqType = "";
      this.addBatchManage = false;
      for (var item of this.selectedIconList) {
        item.click = false;
        this.$forceUpdate();
      }
    },
    // 筛选设备名称
    screenEqNameButton() {
      console.log(this.screenEqName);
      if (this.screenEqName) {
        for (var item of this.selectedIconList) {
          if (item.eqName.indexOf(this.screenEqName) > -1) {
            console.log(item.eqName);
            item.click = true;
          } else {
            item.click = false;
          }
        }
      } else {
        for (var item of this.selectedIconList) {
          item.click = false;
        }
      }
    },
    // 抽屉车指批量控制 车道下拉框
    getTunnelLane(tunnelLane) {
      let laneArray = JSON.parse(JSON.stringify(this.chezhiLaneOptionList));
      this.chezhiLaneList = laneArray.slice(0,tunnelLane);
    },
    // 抽屉车指批量控制 状态下拉框
    getTunnelState() {
      const param = {
        stateTypeId: 1,
        isControl: 1,
      };
      getStateByData(param).then((response) => {
        console.log(response, "查询设备状态图标");
        // this.chezhiStateList = response.rows;
        this.chezhiStateList = [];
        for (let i = 0; i < response.rows.length; i++) {
          let iconUrl = [];
          if (response.rows[i].iFileList != null) {
            for (let j = 0; j < response.rows[i].iFileList.length; j++) {
              let img = response.rows[i].iFileList[j].url;
              iconUrl.push(img);
            }
          }
          this.chezhiStateList.push({
            deviceState: response.rows[i].deviceState,
            stateName: response.rows[i].stateName,
            url: iconUrl,
          });
        }
      });
    },
    // 控制按钮
    chezhiControl(num) {
      this.chezhiDisabled = true;
      const param = {
        tunnelId: this.tunnelId,
        direction: num,
        state: this["chezhiForm" + num].state,
        lane: this["chezhiForm" + num].lane,
      };
      batchControlCarFinger(param).then((res) => {
        console.log(res);
        if (res.data == 0) {
          this.$modal.msgWarning("控制失败");
        } else if (res.data == 1) {
          this.$modal.msgSuccess("控制成功");
        }
      });
      this.chezhiDisabled = false;
    },
    getDeviceDataAndStateData() {
      getDeviceDataAndState(this.tunnelId).then((result) => {
        console.log(result, "批量控制");
        this.BulkData = result.data;
      });
    },
    // 定时控制
    timingControl() {
      const param = {
        tunnelId: this.tunnelId,
        strategyType: 3,
      };
      timingStrategyList(param).then((res) => {
        console.log(res);
      });
    },
    timingStrategy(item) {
      var time = item.arr.join("-");
      updateControlTime(item.strategy_id, time).then((res) => {
        this.$modal.msgSuccess("修改时间成功");
      });
    },
    timStrategySwitch(item) {
      timeStrategySwitch(item.strategy_id, item.strategy_state).then((res) => {
        if (item.strategy_state == 0) {
          this.$modal.msgSuccess("开启成功");
        } else if (item.strategy_state == 1) {
          this.$modal.msgSuccess("关闭成功");
        }
      });
    },
    // // 抽屉 车指控制
    // controlCheZhi(num) {
    //   console.log(num, "num");
    //   console.log(this.checkList1, "checkList1");
    //   console.log(this.checkList2, "checkList2");
    //   // 上传成功后记得把this.checkList清空
    // },
    // 预警事件点击跳转应急调度
    jumpYingJi(e) {
      const item = e.target.closest(".listRow");
      if (item) {
        // 是否是滚动组件的某一行/列
        const { index } = item.dataset;
        let id = JSON.parse(index).id;
        setTimeout(() => {
          bus.$emit("getPicId", id);
        }, 200);
        bus.$emit("openPicDialog");
      }
    },
    // 车型通过字典表获取值
    getCheXing(num) {
      for (var item of this.vehicleTypeList) {
        if (num == Number(item.vehicleTypeCode)) {
          return item.vehicleTypeName;
        }
      }
    },
    // 关闭弹窗子组件
    dialogClose() {
      this.mouseoversImplement = true;
      this.$refs.videoRef.handleClosee()
      this.$refs.lightRef.handleClosee()
      this.$refs.coviRef.handleClosee()
      this.$refs.dataRef.handleClosee()
      this.$refs.windRef.handleClosee()
      this.$refs.pressureRef.handleClosee()
      this.$refs.vehicleDetecRef.handleClosee()
      this.$refs.callPoliceRef.handleClosee()
      this.$refs.brightRef.handleClosee()
      this.$refs.youdaoRef.handleClosee()
      this.$refs.boardRef.handleClosee()
      this.$refs.radioRef.handleClosee()
    },
    // 车辆监测数据
    vehicleEcharts() {
      // console.log(this.tunnelId,"this.tunnelIdthis.tunnelIdthis.tunnelId")
      const param = {
        tunnelId: this.tunnelId,
      };
      vehicleMonitoringInRecent24Hours(param).then((res) => {
        console.log(res, "车辆监测数据");
        var vehicleXData = [];
        var vehicleYData = [];
        for (var item of res.data) {
          vehicleXData.push(item.hour);
          vehicleYData.push(item.count);
        }
        this.initeChartsEnd(vehicleXData, vehicleYData);
      });
    },
    // 重点车辆监测数据
    specialVehicleEcharts() {
      // console.log(this.tunnelId,"this.tunnelIdthis.tunnelIdthis.tunnelId")
      const param = {
        tunnelId: this.tunnelId,
      };
      specialVehicleMonitoringInRecent24Hours(param).then((res) => {
        console.log(res, "重点车辆监测数据");
        var specialVehicleXData = [];
        var specialVehicleYData = [];
        for (var item of res.data) {
          specialVehicleXData.push(item.hour);
          specialVehicleYData.push(item.count);
        }
        this.loadFocusCar(specialVehicleXData, specialVehicleYData);
      });
    },
    // 重点车辆监测数据
    specialEcharts() {
      const param = {
        tunnelId: this.tunnelId,
      };
      special(param).then((res) => {
        console.log(res, "重点车辆监测数据");
        this.keyVehiclesXData = res.data[0];
        this.keyVehiclesYData = res.data[1];
      });
    },
    // 滚动条动画
    srollAuto() {
      if (this.mouseoversImplement == false) {
        return;
      }
      var parent = document.getElementsByClassName("content");
      // console.log(parent,'parentparent')
      clearInterval(this.imageTimer);
      this.imageTimer = setInterval(() => {
        // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
        if (
          Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
          parent[0].scrollWidth
        ) {
          clearInterval(this.imageTimer);
          this.imageTimer = setInterval(() => {
            parent[0].scrollLeft--;
            if (
              Math.round(parent[0].scrollLeft) + parent[0].clientWidth ===
              parent[0].clientWidth
            ) {
              this.srollAuto();
              // console.log(123213,'12321')
            }
          }, 20);
        } else {
          parent[0].scrollLeft++;
        }
      }, 20);
    },

    mouseoversImage() {
      //  console.log(this.imageTimer,'清定时器')
      clearInterval(this.imageTimer);
      this.imageTimer = null;
    },
    mouseleaveImage() {
      // console.log('离开了')
      this.srollAuto();
    },
    //抽屉
    isDrawerA() {
      this.drawerA = !this.drawerA;
      this.drawerB = false;
      this.drawerCVisible = false;
      // Object.assign(this.$data.phoneForm1, this.$options.data().phoneForm1)
      this.phoneForm1 = {
        loopCount: "1",
      };
      this.phoneForm2 = {
        loopCount: "1",
      };
      this.$forceUpdate();
    },
    isDrawerB() {
      this.drawerB = !this.drawerB;
      this.drawerA = false;
      this.drawerCVisible = false;
      this.timingStrategyDisabled = false
      if (this.tunnelId) {
        timeSharing(this.tunnelId).then((res) => {
          for (var item of res.data) {
            item.arr = item.time.split("-");
            console.log(item, "item");
          }
          this.timStrategyList = res.data;
          console.log(this.timStrategyList, "this.timStrategyList");
        });
      }
    },
    isDrawerC() {
      this.drawerCVisible = !this.drawerCVisible;
      this.drawerA = false;
      this.drawerB = false;

      workTriggerInfo(this.currentTunnel.id).then((response) => {
        console.log(response, "自动触发抽屉");
        this.isDrawerCList = response.data;
      });
    },
    zoomSwitchChange(val) {
      console.log(val, "val");
      if (val == false) {
        this.handleTableWheelSwithch = false;
      } else {
        this.handleTableWheelSwithch = true;
      }
    },
    async flvPlayer() {
      if (flvjs.isSupported()) {
        var video = document.getElementsByClassName("videoElement");
        console.log(video[0], "video");
        if (video) {
          //创建播放器实例
          var player = flvjs.createPlayer({
            type: "flv",
            isLive: true,
            hasAudio: false,
            url: `http://10.166.139.12:8081/live/22456.flv`,
          });
          player.attachMediaElement(video);
          try {
            player.load();
            player.play();
          } catch (error) {
            console.log(error);
          }
        }
      }
    },
    // 交通流状况 拥堵状况
    trafficFlowLane() {
      trafficFlowInformation().then((response) => {
        this.trafficJamList = response.data;
        for (var item of response.data) {
          console.log(
            item.left,
            "left",
            item.width,
            "width",
            item.color,
            "color",
            item.tunnelId,
            item.type
          );
        }
      });
    },
    /** 设备类型 */
    getEqType() {
      listType().then((response) => {
        this.eqTypeData = response.rows;
      });
    },
    /** 所属隧道 */
    getTunnel() {
      listTunnels().then((response) => {
        this.eqTunnelData = response.rows;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      // this.getList();
    },
    /** 查询操作日志列表 */
    // getList() {
    //   this.loading = true;
    //   listLog(this.addDateRange(this.queryParams, this.dateRange)).then(
    //     (response) => {
    //       console.log(response, "操作日志列表");
    //       this.logList = response.rows;
    //       this.total = response.total;
    //       this.loading = false;
    //     }
    //   );
    // },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = this.getPastTime();
      this.dateRange1 = this.getPastTime();
      this.resetForm("queryForm");
      this.resetForm("operationParam1");

      this.queryParams.strategyName = "";
      this.queryParams.tunnelId = "";
      this.queryParams.direction = null;
      this.queryParams.strategyType = "";
      this.operationParam.ipaddr = "";
      this.operationParam.status = null;
      this.operationParam.operIp = "";
      this.operationParam.eqTypeId = null;
      this.operationParam.tunnelId = null;
      this.operationParam.controlType = null;
      this.operationParam_xt.status = null;
      this.operationParam_xt.operIp = "";
      this.operationParam_xt.ipaddr = ''
      this.queryParams.pageNum = 1;
      this.handleQueryOperationParam();
      this.handlestrategyQuery();
    },
    // 控制方式   3：手动 1：时间控制 2：光强控制字典翻译
    controlTypeFormat(row, column) {
      return this.selectDictLabel(this.controlTypeOptions, row.controlType);
    },
    //操作是否成功 0：成功 1：失败
    stateFormat(row, column) {
      return this.selectDictLabel(this.operationStateOptions, row.state);
    },
    openTooltip(item, index) {
      this.showTooltipIndex = index;
      // this.showTooltip = true;
      this.sensorDisabled(item);
    },
    closeTooltip(item) {
      this.showTooltipIndex = 999;
      // this.showTooltip = false
    },
    increase() {
      this.percentage += 10;
      if (this.percentage > 100) {
        this.percentage = 100;
      }
    },
    decrease() {
      this.percentage -= 10;
      if (this.percentage < 0) {
        this.percentage = 0;
      }
    },
    controlHeight(item) {
      console.log(item, "item");
    },
    getWarnList() {
      const param = {
        eventState: "3",
      };
      // getSystemWarningMsg().then((res) => {
      //   console.log(res, "系统预警")
      //   res.data.forEach(val => {
      //     if (val) {
      //       this.SysEarlyWarning = res.data
      //     }
      //   })
      // })
      // getTrafficIncident().then((response) => {
      //   this.trafficList = response.data
      // })

      getWarnEvent(param).then((response) => {
        console.log(response.data, "预警事件");
        this.trafficList = response.data;
      });
    },
    /** 查询部门列表 */
    /*getDeptList() {
      var that = this;
      var id = this.userDeptId;
      var iid = "";
      const params = {
        status: 0,
      };
      listDept(params)
        .then((response) => {
          var list = that.handleTree(response.data, "deptId");
          console.log(list, "级联选择器格式");
          let arr = [];
          this.checkData(list[0], arr);
          function a(list) {
            list.forEach((item) => {
              if (item.deptId == id) {
                that.siteList = item.children || [];
              } else {
                item.children && a(item.children);
              }
            });
          }
          a(list);
        })
        // .then(() => {
        //   that.siteList.filter((item) => {
        //     if (item.children) {
        //       item.children.forEach((val) => {
        //         iid ? "" : (iid = val.deptId);
        //         that.tunnelQueryParams.deptId == iid
        //           ? ""
        //           : (that.tunnelQueryParams.deptId = iid);
        //       });
        //     } else {
        //       iid ? "" : (iid = item.deptId);
        //       that.tunnelQueryParams.deptId == iid
        //         ? ""
        //         : (that.tunnelQueryParams.deptId = iid);
        //     }
        //     console.log(that.tunnelQueryParams.deptId,"3333333333333333333333")
        //   });
        // })
        .then(() => {
          // 获取隧道
          console.log(22222222222);

          that.getTunnelList();
          if (this.manageStation == "1") {
            // that.tunnelQueryParams.deptId == "555503";

            let arr = ["6266", "5555", "555503"];
            this.changeSite(arr);
          }
          that.siteList.length == 0 ? (that.isManagementStation = true) : "";
        });
    },*/
    getDeptList() {
      var userDeptId = this.userDeptId;
      const params = { status: 0 };
      getTreeByDeptId(params)
        .then((response) => {
          console.log(response.data, "级联");
          const options = response.data;
          let childs = [];
          function a(list) {
            list.forEach((item) => {
              if (item.id == userDeptId) {
                childs = item.children || [];
              } else {
                item.children && a(item.children);
              }
            });
          }
          a(options);
          if (childs.length == 0) {
            this.siteList = options[0].children;
          } else {
            this.siteList = childs;
            console.log(this.siteList, "位置list");
          }
          let arr = [];
          this.checkData(this.siteList[0], arr);
        })
        .then(() => {
          // this.getTunnelList();
          // console.log(222222222)
          if (this.manageStation == "1") {
            //let arr = ["6266", "5555", "555503"];
            let arr = ["YG118", "YG11801", "YG1180103"];
            this.changeSite(arr);
          }
        });
    },

    /*checkData(obj, arr) {
      if (obj.children && obj.children.length > 0) {
        arr.push(obj.deptId);
        this.checkData(obj.children[0], arr);
      } else {
        arr.push(obj.deptId);
        arr.shift();
        this.changeSite(arr);

        this.$forceUpdate();
      }
    },*/

    checkData(obj, arr) {
      if (obj.children && obj.children.length > 0) {
        arr.push(obj.id);
        this.checkData(obj.children[0], arr);
      } else {
        arr.push(obj.id);
        arr.shift();
        this.changeSite(arr);

        this.$forceUpdate();
      }
    },
    // 获取最近24小时时间数组
    getTimeData() {
      //获取当前时间-24小时
      let nows = new Date();
      let hourArr = getLastDayHour(nows, 12);

      function getLastDayHour(d, s) {
        let result = [];
        d.setHours(d.getHours());
        for (var i = 0; i < s; i++) {
          d.setHours(d.getHours() - 1);
          var h = d.getHours() + ":00";
          result.push(h);
        }
        return result.reverse();
      }

      return hourArr;
    },
    // 改变站点
    changeSite(index) {
      // console.log(index, "index------------------------1");
      if (index) {
        this.tunnelQueryParams.deptId = index[index.length - 1];
        this.$forceUpdate();

        this.getTunnelList();
        // this.srollAuto()
      }
    },
    // 机器人tabs页
    robotTabsClick(tab, event) {
      var that = this;
      if (tab.paneName == "stateRecords") {
        this.robotChartDom ||
          (this.robotChartDom = echarts.init(
            document.getElementById("robotEchart")
          ));
        that.$nextTick(() => {
          that.robotEchartInit();
        });
      }
    },
    // 机器人状态记录图表初始化
    robotEchartInit() {
      this.robotStateData || (this.robotStateData = [0, 1, 2]);
      this.robotXData || (this.robotXData = ["8:30", "10:30", "11:30"]);
      this.robotChartDom ||
        (this.robotChartDom = echarts.init(
          document.getElementById("robotEchart")
        ));
      this.robotEchart();
      this.robotChartDom.resize();
    },
    robotEchart() {
      this.robotOption = {
        // title: {
        //   text: '运行状态',
        //   textStyle: {
        //     color: '#fff'
        //   }
        // },
        tooltip: {
          trigger: "axis",
          formatter: function (value) {
            for (var j = 0; j < value.length; j++) {
              if (value[j].value == 0) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 正常"
                );
              } else if (value[j].value == 1) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 离线"
                );
              } else if (value[j].value == 2) {
                return (
                  value[j].name + "<br/> " + value[j].seriesName + ": 故障"
                );
              }
            }
          },
          textStyle: {
            color: "powderblue",
          },
          backgroundColor: "rgba(0,0,0,0.5)",
          extraCssText: "box-shadow: 0px 0px 10px rgb(104 181 255);",
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          top: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          boundaryGap: true,
          data: this.robotXData,
          axisLine: {
            lineStyle: {
              color: "#fff",
            },
          },
        },
        yAxis: {
          type: "category",
          axisLine: {
            lineStyle: {
              color: "#fff",
            },
          },
          data: [
            {
              value: "正常",
              textStyle: {
                fontSize: 20,
                color: "yellow",
              },
            },
            {
              value: "离线",
              textStyle: {
                fontSize: 20,
                color: "#2c5ea2",
              },
            },
            {
              value: "故障",
              textStyle: {
                fontSize: 20,
                color: "red",
              },
            },
          ],
          axisLine: {
            show: true,
          },
          textStyle: {
            color: "#fff",
          },
        },
        series: [
          {
            name: "运行状态",
            type: "line",
            step: "end",
            data: this.robotStateData,
          },
        ],
      };
      this.robotOption && this.robotChartDom.setOption(this.robotOption);
    },

    // 缩略图开关
    changeThumbnail(val) {
      var vehicleLane = document.getElementsByClassName("vehicleLane");
      var footer = document.getElementsByClassName("footer");
      if (!val) {
        vehicleLane[0].style.height = "87%";
        footer[1].style.height = "0%";
      } else {
        vehicleLane[0].style.removeProperty("height");
        this.initEharts();
      }
    },
    // 初始化echart图
    initEharts() {
      var that = this;
      proportionOfEquipment({
        tunnelId: that.currentTunnel.id,
      }).then((res) => {
        console.log(res, "设备类型占比");
        // that.initechartsB(res.data)
      });
      // that.initeChartsEnd()
      //that.loadFocusCar();
    },
    // 获取最近七天数组
    dateFormat(dateData) {
      // let times = moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
      // console.log(times,"times")
      var now = new Date(dateData),
        // y = now.getFullYear(),//年份
        m = now.getMonth() + 1, //月份
        d = now.getDate(); //日期
      return (m < 10 ? "0" + m : m) + "." + (d < 10 ? "0" + d : d);
    },
    getEnergyConsumption(id) {
      energyConsumptionDetection(id).then((res) => {
        console.log(res, "能耗监测");
        let xDataN = [];
        let xDataY = [];
        let xDataR = [];

        // let yDataN = []
        // let yDataY = []
        // let yDataR = []
        let xData = [];
        let yData = [];
        yData.push({
          name: "年",
          type: "line",
          color: "#59c5f9",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          // 渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#59c5f9",
                },
                {
                  offset: 1,
                  color: "rgba(89,197,249,0.3)",
                },
              ]),
            },
          },
          data: res.data.year.map((item) => item.value),
        });
        yData.push({
          name: "月",
          type: "line",
          color: "#db72a7",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          stack: "Total",
          areaStyle: {},
          emphasis: {
            focus: "series",
          },
          //渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#db72a7",
                },
                {
                  offset: 1,
                  color: "rgba(219,114,167,0.3)",
                },
              ]),
            },
          },
          data: res.data.month.map((item) => item.value),
        });
        yData.push({
          name: "日",
          type: "line",
          color: "#FDB400",
          symbol: "circle",
          symbolSize: [7, 7],
          itemStyle: {
            normal: {
              borderColor: "white",
            },
          },
          smooth: true,
          // 渐变色
          areaStyle: {
            normal: {
              //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#FDB400",
                },
                {
                  offset: 1,
                  color: "rgba(253,180,0,0.3)",
                },
              ]),
            },
          },
          data: res.data.day.map((item) => item.value),
        });
        for (let item of res.data.year) {
          xData.push(item.rt);
          xDataN.push(item.rt);

          // yDataN.push(item.value)
        }
        for (let item of res.data.month) {
          xDataY.push(item.rt);
          // yDataY.push(item.value)
        }
        for (let item of res.data.day) {
          xDataR.push(item.rt);
          // yDataR.push(item.value)
        }
        // let xData = []
        // console.log(xData,yData,xDataN,xDataY,xDataR,"能耗年月日")
        this.$nextTick(() => {
          this.initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR);
        });
      });
    },
    // 能耗监测echarts
    initEnergyConsumption(xData, yData, xDataN, xDataY, xDataR) {
      var energyConsumption = echarts.init(
        document.getElementById("energyConsumption")
      );

      var option = {
        tooltip: {
          trigger: "axis",
        },
        legend: {
          show: true,
          icon: "rect",
          itemWidth: 10,
          itemHeight: 10,
          selectedMode: "single", // 单选
          selected: {
            年: true,
            月: false,
            日: false,
          },
          x: "center",
          data: ["年", "月", "日"],
          textStyle: {
            //图例文字的样式
            color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
            fontSize: 12,
          },
        },
        calculable: true,
        grid: {
          top: "24%",
          bottom: "20%",
          left: "14%",
          right: "14%",
        },
        xAxis: [
          {
            // name: "小时",
            nameTextStyle: {
              fontFamily: "PingFang",
            },
            type: "category",
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            boundaryGap: false,
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
                fontFamily: "PingFang",
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "kwh",
            nameTextStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            type: "value",
            minInterval: 1,
            // min: 0,
            // max: 200,
            axisTick: {
              show: false,
            },
            // max: 200,
            // min: 0,
            splitNumber: 5,
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["#4E6B83"],
                width: 1,
                type: "dashed",
              },
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
          },
        ],
        series: yData,
      };

      energyConsumption.on("legendselectchanged", (obj) => {
        var options = energyConsumption.getOption();
        //这里是选择切换什么样的x轴，那么他会进行对Y值的切换
        if (obj.name == "年") {
          options.xAxis[0].data = xDataN;
        } else if (obj.name == "月") {
          options.xAxis[0].data = xDataY;
        } else if (obj.name == "日") {
          options.xAxis[0].data = xDataR;
        }

        energyConsumption.setOption(options, true);
      });

      // }
      energyConsumption.setOption(option);
      window.addEventListener("resize", function () {
        energyConsumption.resize();
      });
    },
    initeChartsEnd(vehicleXData, vehicleYData) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      //然后异步执行echarts的初始化函数
      newPromise.then(() => {
        //	此dom为echarts图标展示dom
        var vehicle = echarts.init(document.getElementById("vehicle"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          // legend: {
          //   show: true,
          //   icon: "rect",
          //   itemWidth: 10,
          //   itemHeight: 10,
          //   x: 'center',
          //   data: ['客车', '货车', '专项车'],
          //   textStyle: { //图例文字的样式
          //     color: this.sideTheme!='theme-blue'?'#fff':'#003a5d',
          //     fontSize: 12
          //   }
          // },
          calculable: true,
          grid: {
            top: "24%",
            bottom: "20%",
            left: "14%",
            right: "14%",
          },
          xAxis: [
            {
              name: "小时",
              nameTextStyle: {
                fontFamily: "PingFang",
              },
              type: "category",
              axisTick: {
                show: false,
              },
              splitLine: {
                show: false,
              },
              boundaryGap: false,
              axisLabel: {
                textStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                  fontFamily: "PingFang",
                },
              },
              axisLine: {
                show: true,
                lineStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                },
              },
              data: vehicleXData,
            },
          ],
          yAxis: [
            {
              name: "总车量",
              nameTextStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
                padding: [0, 20, 0, 0],
              },
              type: "value",
              minInterval: 1,
              axisTick: {
                show: false,
              },
              splitNumber: 5,
              splitLine: {
                show: true,
                lineStyle: {
                  //分割线的样式
                  color: ["#4E6B83"],
                  width: 1,
                  type: "dashed",
                },
              },
              axisLine: {
                show: false,
              },
              axisLabel: {
                textStyle: {
                  color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                  fontSize: 10,
                },
              },
            },
          ],
          series: [
            {
              name: "车辆总数",
              type: "line",
              color: "#59c5f9",
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              smooth: true,
              // 渐变色
              areaStyle: {
                normal: {
                  //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#59c5f9",
                    },
                    {
                      offset: 1,
                      color: "rgba(89,197,249,0.3)",
                    },
                  ]),
                },
              },
              data: vehicleYData,
            },
            // {
            //   name: '货车',
            //   type: 'line',
            //   color: '#db72a7',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   stack: 'Total',
            //   areaStyle: {},
            //   emphasis: {
            //     focus: 'series'
            //   },
            //   //渐变色
            //   areaStyle: {
            //     normal: {
            //       //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色，
            //       color: new echarts.graphic.LinearGradient(
            //         0, 0, 0, 1,
            //         [{
            //             offset: 0,
            //             color: '#db72a7'
            //           },
            //           {
            //             offset: 1,
            //             color: 'rgba(219,114,167,0.3)'
            //           }
            //         ]
            //       )
            //     }
            //   },
            //   data: [90, 70, 50, 30, 80, 90, 30, 60, 70, 80, 90, 20]
            // }, {
            //   name: '专项车',
            //   type: 'line',
            //   color: '#ffb600',
            //   symbol: 'circle',
            //   symbolSize: [7, 7],
            //   itemStyle: {
            //     normal: {
            //       borderColor: "white"
            //     }
            //   },
            //   smooth: true,
            //   data: [20, 30, 40, 50, 70, 80, 90, 60, 40, 30, 20, 60]
            // },
          ],
        };
        vehicle.setOption(option);
        window.addEventListener("resize", function () {
          vehicle.resize();
        });
      });
    },
    loadFocusCar(specialVehicleXData, specialVehicleYData) {
      let newPromise = new Promise((resolve) => {
        resolve();
      });
      newPromise.then(() => {
        var focusCar = echarts.init(document.getElementById("focusCar"));
        var option = {
          tooltip: {
            trigger: "axis",
          },
          toolbox: {
            show: true,
            feature: {
              // magicType: { show: true, type: ['stack', 'tiled'] },
              // saveAsImage: { show: true }
            },
          },
          grid: {
            top: "20%",
            bottom: "18%",
            left: "14%",
            right: "14%",
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            // data: this.keyVehiclesXData,
            data: specialVehicleXData,
            name: "小时",
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              },
            },
          },
          yAxis: {
            type: "value",
            name: "总车辆",
            nameTextStyle: {
              color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
              fontSize: 10,
              padding: [0, 20, 0, 0],
            },
            minInterval: 1, //y轴的刻度只显示整数
            axisLabel: {
              textStyle: {
                color: this.sideTheme != "theme-blue" ? "#fff" : "#003a5d",
                fontSize: 10,
              },
            },

            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              show: true,
              lineStyle: {
                //分割线的样式
                color: ["#4E6B83"],
                width: 1,
                type: "dashed",
              },
            },
          },
          series: [
            {
              type: "line",
              color: "#00c8ff",
              symbol: "none",
              smooth: true,
              stack: "Total",
              areaStyle: {},
              symbol: "circle",
              symbolSize: [7, 7],
              itemStyle: {
                normal: {
                  borderColor: "white",
                },
              },
              emphasis: {
                focus: "series",
              },
              //渐变色
              areaStyle: {
                normal: {
                  //前四个参数代表位置 左下右上，如下表示从上往下渐变色 紫色到暗蓝色
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#33b0ee",
                    },
                    {
                      offset: 1,
                      color: "rgba(51,176,238,0.3)",
                    },
                  ]),
                },
              },
              // data: this.keyVehiclesYData,
              data: specialVehicleYData,
            },
          ],
        };

        focusCar.setOption(option);
        window.addEventListener("resize", function () {
          focusCar.resize();
        });
      });
    },
    // 情报板编辑
    IntelligenceBoardEdit(item) {
      console.log(item);
      this.dialogVisible = true;
      // this.$refs.contentBatchEdit.deviceId = item.id;
      this.$refs.contentBatchEdit.vmsSize = "1024*768"; //item.vmsSize
      // this.$refs.contentBatchEdit.list = item.item;
      // this.$refs.vmsContentUpdate.add = true;
      this.$refs.vmsContentUpdate.init();
    },
    // 火灾报警弹窗
    HZhandleClose() {
      this.HZdialogVisible = false;
    },
    handleClosee() {
      this.cameraVisible = false;
    },
    controlHeightClose() {
      this.controlHeightVisible = false;
    },
    showA() {
      this.carShow = !this.carShow;
      // this.carMapSportCar();
      // this.carMapRight();
    },
    showB() {
      this.carShowTwo = !this.carShowTwo;
      // this.carMapLeft();
    },
    // 跑车
    carMapSportCar() {
      clearInterval(this.timer);
      const car = this.$refs["sportCar"];
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              item.url[0] = this.dark;
            }
          }
          this.carShow = false;
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              if (car.style.left == "100px") {
                this.carShow = false;
              }
              if (parseInt(car.style.left) - item.position["left"] <= 150) {
                item.url[0] = this.light;
              }
              if (item_1.position.top == 156) {
                if (parseInt(car.style.left) - item.position["left"] <= -150) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    // 下行车道
    carMapLeft() {
      clearInterval(this.timer);
      const car = this.$refs["carLeft"];
      let speed = 210;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) >= 1130) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 0) ||
              (item.eqType == 8 && item.eqDirection == 0) ||
              (item.eqType == 9 && item.eqDirection == 0)
            ) {
              item.url[0] = this.dark;
            }
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 0) ||
              (item.eqType == 8 && item.eqDirection == 0) ||
              (item.eqType == 9 && item.eqDirection == 0)
            ) {
              // 到达终点隐藏
              if (car.style.left == "1130px") {
                this.carShow = false;
                return false;
              }
              // 如果车辆当前位置和路灯位置相差小于100，则亮灯
              if (parseInt(car.style.left) - item.position["left"] <= -100) {
                item.url[0] = this.light;
              }
              if (item_1.position.top != 0) {
                // 如果车辆位置 - 当前路灯位置 大于等于 100 则代表车辆已经离开当前路灯区域，则灭灯
                if (parseInt(car.style.left) - item.position["left"] >= 100) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) + 24 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    // 上行车道
    carMapRight() {
      clearInterval(this.timer);
      const car = this.$refs["carRight"];
      // item.position['left']  当前隧道灯的距离出口的距离
      // car.style.left    当前距离隧道出口的距离
      let speed = 200;
      this.timer = setInterval(() => {
        if (parseInt(car.style.left) <= 30) {
          // 车辆停止运动
          clearInterval(this.timer);
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              item.url[0] = this.dark;
            }
          }
          this.carShow = false;
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            let item = this.selectedIconList[i];
            let item_1 = this.selectedIconList[i - 1];
            // 取到所有的隧道灯
            if (
              (item.eqType == 7 && item.eqDirection == 1) ||
              (item.eqType == 8 && item.eqDirection == 1) ||
              (item.eqType == 9 && item.eqDirection == 1)
            ) {
              if (car.style.left == "100px") {
                this.carShow = false;
              }
              if (parseInt(car.style.left) - item.position["left"] <= 150) {
                item.url[0] = this.light;
              }
              if (item_1.position.top == 156) {
                if (parseInt(car.style.left) - item.position["left"] <= -150) {
                  item_1.url[0] = this.dark;
                }
              }
            }
          }
          car.style.left = parseInt(car.style.left) - 20 + "px";
        }
      }, speed);
      this.$forceUpdate();
    },
    getUserDept() {
      getUserDeptId(this.userQueryParams).then((response) => {
        console.log(response, "管理站级联");

        this.userDeptId = response.rows[0].deptId;
        // this.tunnelQueryParams.deptId = response.rows[0].deptId;

        this.getDeptList();
      });
    },
    //地图复位
    resetCanvas(){
      this.resetCanvasFlag = false;
      this.$refs.dragImgDom.style.left = "0px";
      this.$refs.dragImgDom.style.top = "0px";
    },
    //右键拖动
    dragImg(e) {
      console.log(e, "e");
      if (e.button == 0) {
        return;
      }
      this.dragFlag = true;
      this.mouseLeft = e.clientX - parseInt(this.$refs.dragImgDom.offsetLeft);
      this.mouseTop = e.clientY - parseInt(this.$refs.dragImgDom.offsetTop);
      document.onmousemove = (e) => {
        if (this.dragFlag) {
          this.resetCanvasFlag = true;
          this.curX = e.clientX - this.mouseLeft;
          this.curY = e.clientY - this.mouseTop;
          this.$refs.dragImgDom.style.left = this.curX + "px";
          this.$refs.dragImgDom.style.top = this.curY + "px";
        }
      };
      document.onmouseup = () => {
        this.dragFlag = false;
      };
    },
    lightSwitchFunc() {
      this.getConfigKey("lightSwitch").then((response) => {
        this.lightSwitch = response.msg;
      });
    },

    /* -------------------鼠标拖动-------------------*/
    /* 鼠标点击*/
    dian(event) {
      if (event.button != 0) {
        return;
      }
      // clientX 和 clientY 是起点
      let parentObj = document.getElementById("eq-wrapper");
      wrapperClientX = parentObj.getBoundingClientRect().left;
      wrapperClientY = parentObj.getBoundingClientRect().top;
      this.px =
        event.clientX + event.clientX * this.moveTop - wrapperClientX - 30;
      this.py = event.clientY + event.clientY * this.moveTop - wrapperClientY;
      boxEqList = [];
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
    },
    /*鼠标移动*/
    yi(event) {
      // console.log(event)
      if (this.px == "" || this.py == "") {
        return;
      }
      let px1 = this.px;
      let px2 = this.py;
      this.left = event.clientX + event.clientX * this.moveTop - wrapperClientX;
      this.top = event.clientY + event.clientY * this.moveTop - wrapperClientY;

      this.h = this.top - this.py;
      this.w = this.left - this.px;
      let hc = -this.h;
      let wc = -this.w;
      this.len = 1.5;
      this.back = "#1E90FF";
      if (this.h < 0 && this.w >= 0) {
        this.h = hc;
        this.left = px1;
      } else if (this.h >= 0 && this.w < 0) {
        this.w = wc;
        this.top = px2;
      } else if (this.h < 0 && this.w < 0) {
        this.h = hc;
        this.w = wc;
      } else {
        this.left = this.px;
        this.top = this.py;
      }
      if (this.w < 0) {
        this.w = 0 - this.w;
      }
      if (this.h < 0) {
        this.h = 0 - this.h;
      }
    },
    /* 鼠标离开*/
    li() {
      this.move = false;
      let str = [108, 110, 18, 21, 13, 14, 15, 16, 5, 6, 7, 8, 9];
      let list = this.selectedIconList;
      for (let i = 0; i < list.length; i++) {
        // 传入每个设备并判断是否在范围之内
        let inRange = this.inRange(list[i]);
        if (inRange == true) {
          //判断当前圈选设备的remark字段中是否为null，且是否包含“hjpz”，如果不包含，则返回-1，证明是正常设备，则可以显示圈选效果
          const remark = this.selectedIconList[i].remark;
          if (remark != null && remark.indexOf("hjpz") == -1) {
            this.selectedIconList[i].isfocus = true;
          }
          // debugger
          // this.selectedIconList[i].isfocus = true;
          if (boxEqList.length > 0) {
            let index = -1;
            for (let k = 0; k < boxEqList.length; k++) {
              if (list[i].eqType == boxEqList[k].typeId) {
                index = k;
                break;
              }
            }
            if (index > -1) {
              boxEqList[index].eqlist.push(list[i]);
            } else {
              if (!str.includes(parseInt(list[i].eqType))) {
                boxEqList.push({
                  typeId: list[i].eqType,
                  eqlist: [list[i]],
                });
                this.boxType(list[i].eqType);
              }
            }
          } else {
            if (!str.includes(parseInt(list[i].eqType))) {
              boxEqList.push({
                typeId: list[i].eqType,
                eqlist: [list[i]],
              });
              this.boxType(list[i].eqType);
            }
          }
        } else {
          // console.log('bu在范围内')
        }
      }
      if (boxEqList.length > 0 && this.boxTypeList.length > 0) {
        if (boxEqList.length == 1 && boxEqList[0].eqlist.length == 1) {
          //单个配置
          this.openStateSwitch(boxEqList[0].eqlist[0]);
        } else {
          //超过1个设备进行批量配置
          this.batchForm.eqType = this.boxTypeList[0].typeId;
          console.log(this.batchForm.eqType, "批量操作设备类型");
          let exist = true; //假设获取到所有信息
          boxEqList = boxEqList.filter((e) => e.typeId != undefined);
          for (let b = 0; b < boxEqList.length; b++) {
            for (let bl = 0; bl < boxEqList[b].eqlist.length; bl++) {
              if (
                boxEqList[b].eqlist[bl].eqDirection == undefined ||
                boxEqList[b].eqlist[bl].eqDirection == null ||
                boxEqList[b].eqlist[bl].typeName == undefined
              ) {
                exist = false; //取不到设备信息，不可配置
                this.$message({
                  message: "未能获取所有设备信息,请重新配置！",
                  type: "warning",
                  customClass: "workbench-msg",
                });
                this.cleanUp();
                this.empty();
                break;
              }
              if (exist == false) {
                break;
              }
            }
          }
          if (exist == true) {
            mode = "boxSelection";
            this.title = "批量操作";
            if (this.$refs["batchForm"]) {
              this.$refs["batchForm"].resetFields();
            }
            this.devicesList = this.changeDirection(boxEqList[0].eqlist);
            let param = {
              eqTunnelId: this.currentTunnel.id,
              eqType: this.devicesList[0].eqType,
              lane: this.batchForm.eqlane,
              eqDirection: this.batchForm.eqDirection,
            };
            this.selectDirection(param);
            this.batchVisible = true;
          }
        }
      }
      this.empty();
    },
    boxType(type) {
      for (let i = 0; i < this.eqTypeList.length; i++) {
        if (type == this.eqTypeList[i].typeId || type == 100) {
          this.boxTypeList.push({
            typeId: type,
            typeName: this.eqTypeList[i].typeName,
          });
        }
      }
    },
    // 获取风机状态
    getFjDisabled(item, state) {
      // 主机
      if (item.eqType == "10") {
        // item.state是当前状态 选择状态
        if (item.fjState == "1" && state == "2") {
          return true;
        }
        //
        else if (item.fjState == "2" && state == "1") {
          return true;
        }
        //
        else {
          return false;
        }
      } else {
        return false;
      }
    },
    /* 是否在范围内*/
    inRange(eqIcon) {
      let inRange = false;
      if (eqIcon.display == true) {
        let maxX = Math.max(this.left + this.w, eqIcon.position.left + 60);
        let maxY = Math.max(this.top + this.h, eqIcon.position.top - 100 + 30);
        let minX = Math.min(this.left, eqIcon.position.left);
        let minY = Math.min(this.top, eqIcon.position.top - 100);
        if (maxX - minX <= this.w + 10 && maxY - minY <= this.h + 20) {
          inRange = true;
        } else {
          inRange = false;
        }
      }
      return inRange;
    },
    empty() {
      this.px = "";
      this.py = "";
      this.h = "";
      this.w = "";
      this.top = "";
      this.left = "";
      this.len = 0;
      this.back = "";
    },
    /* 关闭窗口，清空框选*/
    cleanUp() {
      this.empty();
      this.boxTypeList = [];
      this.move = false;
      mode = "";
      //清除聚焦
      let list = this.selectedIconList;
      for (let i = 0; i < list.length; i++) {
        if (list[i].hasOwnProperty("isfocus")) {
          delete this.selectedIconList[i].isfocus;
        }
      }
    },
    // onmousemove(e) {
    //   let et = e || window.event;
    //   et.preventDefault(); // 阻止默认事件发生
    // },
    /* -------------------鼠标拖动end------------------*/

    /* 查询隧道列表 */
    getTunnelList() {
      // console.log(this.tunnelQueryParams, "44444444444");
      listTunnels(this.tunnelQueryParams).then((response) => {
        console.log(response, "查询隧道列表");
        if (!response.rows[0]) {
          this.tunnelList = [];
          return false;
        }
        this.tunnelNameEarlyWarn = response.rows[0].tunnelName;
        this.tunnelId = response.rows[0].tunnelId;
        // this.specialEcharts(this.tunnelId)
        this.vehicleEcharts();
        this.specialVehicleEcharts();
        this.getDeviceDataAndStateData();
        var newDict = this.dict.type.sd_sys_name;
        if (this.tunnelId != "JQ-JiNan-WenZuBei-MJY") {
          // this.robotShow = false;
          this.dictList = newDict.slice(0, 8);
        } else if (this.tunnelId == "JQ-JiNan-WenZuBei-MJY") {
          this.dictList = this.dict.type.sd_sys_name;
          // this.robotShow = true;
        }
        // this.tunnelList = [];
        this.checkboxTunnel = [];
        let list = response.rows;
        if (list.length > 0) {
          for (let i = 0; i < list.length; i++) {
            if (list[i].tunnelLength == null || list[i].tunnelLength == "") {
              let num = 0;
              list[i].tunnelLength = num.toString();
            }
          }
          // this.buttonIndex = 0;
          if (this.manageStation == "0") {
            this.tunnelList = [];
            this.tunnelList = list;
            this.checkboxTunnel.push(list[0]);
            this.currentTunnel.id = list[0].tunnelId;
            this.currentTunnel.name = list[0].tunnelName;
            this.selectEquipmentType(this.currentTunnel.id);
            this.getTunnelData(this.currentTunnel.id);
            // this.$nextTick(()=>{
            //   this.initEnergyConsumption(this.currentTunnel.id)

            // })
          } else {
            for (let i = 0; i < list.length; i++) {
              if (
                list[i].tunnelId == this.$cache.local.get("manageStationSelect")
              ) {
                this.tunnelList = [];
                this.tunnelList.push(list[i]);
                this.checkboxTunnel.push(list[i]);
                this.setTunnel(list[i], 0);
              }
            }
          }
        }
        this.getTunnelLane(response.rows[0].lane);
        this.$nextTick(() => {
          this.getEnergyConsumption(this.currentTunnel.id);
        });
        // this.timingControl()
      });
    },
    /* 查询设备类型*/
    selectEquipmentType(currentTunnelId) {
      this.eqTypeList = [];
      listHasType(currentTunnelId).then((response) => {
        let typeList = response.rows;
        this.eqTypeList = typeList;
        // this.eqTypeList.unshift({
        //   typeId: "00",
        //   typeName: "全部",
        // });

        // this.eqBigTypeList = [];
        groupByBigType().then((response) => {
          var typeList = response.rows;
          // this.eqBigTypeList = typeList;
          // this.eqBigTypeList.unshift({
          //   typeId: "00",
          //   bigType: "全部",
          // });
      });
      });
    },
    // 查询方向
    selectDirection(param) {
      listDevices(param).then((response) => {
        this.allDirection = this.changeDirection(response.rows);
        for (let i = 0; i < this.allDirection.length; i++) {
          for (let j = i + 1; j < this.allDirection.length; j++) {
            if (
              this.allDirection[i].eqDirection ==
              this.allDirection[j].eqDirection
            ) {
              this.allDirection.splice(j, 1); //splice会改变原数组
              j--;
            }
          }
        }
      });
    },
    // 查询车道
    selectLane(param) {
      listDevices(param).then((response) => {
        this.allLane = this.changeDirection(response.rows);
        for (let i = 0; i < this.allLane.length; i++) {
          for (let j = i + 1; j < this.allLane.length; j++) {
            if (this.allLane[i].lane == this.allLane[j].lane) {
              this.allLane.splice(j, 1);
              j--;
            }
          }
        }
        this.allLane.forEach((item) => {
          switch (Number(item.lane)) {
            case 1:
              item.laneName = "第一车道";
              break;
            case 2:
              item.laneName = "第二车道";
              break;
            case 3:
              item.laneName = "第三车道";
              break;
            case 0:
              item.laneName = "其它";
              break;
          }
        });
      });
    },
    /* 查询设备*/
    selectEquipment(param) {
      let that = this;
      listDevices(param).then((response) => {
        that.devicesList = that.changeDirection(response.rows);
      });
    },
    changeDirection(list) {
      for (let i = 0; i < list.length; i++) {
        if (list[i].eqDirection == "1") {
          list[i].eqDirectionName = this.leftDirection + "方向";
        } else if (list[i].eqDirection == "0") {
          list[i].eqDirectionName = this.rightDirection + "方向";
        } else {
          list[i].eqDirectionName = "";
        }
      }
      return list;
    },
    /* 查询设备状态图标*/
    async getEqTypeStateIcon() {
      let that = this;
      let queryParams = {
        stateTypeId: null,
        deviceState: null,
        stateName: null,
        // isControl: 1,
      };
      await listEqTypeState(queryParams).then((response) => {
        let list = response.rows;
        that.getEqUrl(list);
      });
    },
    async getEqUrl(list) {
      let that = this;
      // that.eqTypeStateList = [];
      that.eqTypeStateList = [];
      for (let i = 0; i < list.length; i++) {
        let iconUrl = [];
        if (list[i].iFileList != null) {
          for (let j = 0; j < list[i].iFileList.length; j++) {
            // let img = await that.picture(list[i].iFileList[j].url);
            let img = list[i].iFileList[j].url;
            iconUrl.push(img);
          }
        }
        that.eqTypeStateList.push({
          stateType: list[i].stateType,
          type: list[i].stateTypeId,
          state: list[i].deviceState,
          name: list[i].stateName,
          control: list[i].isControl,
          url: iconUrl,
        });
      }
      console.log(that.eqTypeStateList, "设备图标eqTypeStateList");
    },
    /* 请求图片base64地址*/
    picture(fileUrl) {
      return new Promise((resolve, reject) => {
        loadPicture({
          url: fileUrl,
        }).then((response) => {
          if (response.code == 200) {
            let base64 = response.msg;
            resolve(base64); //不可缺少
          }
        });
      });
      return resolve(base64);
    },
    /* 视频 */
    changeLoading() {
      this.loading = false;
      this.cameraErrorInfo = "抱歉！视频信号丢失！";
    },
    /* 获取隧道配置信息*/
    getTunnelData(tunnelId) {
      let that = this;
      that.upList = [];
      that.downList = [];
      const params = {
        tunnelId: tunnelId,
      };
      this.carchange(tunnelId);
      getTunnels(tunnelId).then((res1) => {
        console.log(res1, "获取隧道配置信息");
        that.loading = false;
        let res = res1.data.storeConfigure;
        //存在配置内容
        if (res != null && res != "" && res != undefined) {
          res = JSON.parse(res);
          console.log(res, "eqList");
          listType("")
            .then((response) => {
                for (let i = 0; i < res.eqList.length; i++) {
                  res.eqList[i].focus = false;
                  for (let j = 0; j < response.rows.length; j++) {
                    if (response.rows[j].typeId == res.eqList[i].eqType) {
                      let iconWidth = Number(response.rows[j].iconWidth);
                      let iconHeight = Number(response.rows[j].iconHeight);
                      res.eqList[i].iconWidth = iconWidth;
                      res.eqList[i].iconHeight = iconHeight;
                      break;
                    }
                  }
                }
                debugger
              that.selectedIconList = res.eqList; //设备zxczczxc
              // 匹配设备方向
              listDevices().then((data)=>{
                console.log(data,"设备表")
                for(let item of that.selectedIconList){
                  for(let itm of data.rows){
                    if(item.eqId == itm.eqId){
                      item.eqDirection = itm.eqDirection
                    }
                  }
                }
              })
              // 匹配设备是否可控
              listType().then((response) => {
                console.log(response.rows,"设备图标 是否可控")
                for(let item of that.selectedIconList){
                  for(let itm of response.rows){
                    if(item.eqType == itm.typeId){
                      item.isControl = itm.isControl
                    }
                  }
                }
              });
              debugger
              that.getRealTimeData();

              console.log(
                that.selectedIconList,
                "所有设备图标selectedIconList"
              );
              for (var item of that.selectedIconList) {
               // if(item.eqType == 45){
                 // console.log(item,"警示灯带")
               // }
                if (
                  this.tunnelId == "JQ-JiNan-WenZuBei-MJY" &&
                  item.eqType == 29
                ) {
                  this.robotShow = true;
                } else {
                  this.robotShow = false;
                }
              }
            })
            .then(() => {
              that.initEharts();
              // 切换隧道配置信息时，联动大类查询
              that.displayControl(
                that.selectBigType.index.toString(),
                that.selectBigType.bigType.toString()
              );
            });

          if (res.upList != undefined) {
            that.upList = res.upList;
          }
          if (res.downList != undefined) {
            that.downList = res.downList;
          }
          if (res.leftDirection != undefined && res.leftDirection != "") {
            that.leftDirection = res.leftDirection;
          }
          if (res.rightDirection != undefined && res.leftDirection != "") {
            that.rightDirection = res.rightDirection;
          }

          let id = res.lane;
          for (let i = 0; i < that.laneUrlList.length; i++) {
            if (that.laneUrlList[i].id == id) {
              that.currentTunnel.lane = that.laneUrlList[i];
              // that.railingList[1].position.left = that.laneUrlList[i].width
            }
          }
        } else {
          console.log("不存在");
          //不存在
          that.selectedIconList = [];
          that.initEharts();
          //工作台默认背景图
          // that.currentTunnel.lane = this.getLanUrl(response.data.lane);
          that.upList = [];
          that.downList = [];
          that.leftDirection = "";
          that.rightDirection = "";
        }
      });
      // 树状搜索
      getCategoryTree(tunnelId).then((res) => {
        console.log(res, "-------------------------");
        this.treeData = res.data;
      });
    },

    /* 根据车道数获取车道图*/
    getLanUrl(num) {
      let lane = this.laneUrlList[0];
      for (let i = 0; i < this.laneUrlList.length; i++) {
        if (this.laneUrlList[i].num == num) {
          lane = this.laneUrlList[i];
          break;
        }
      }
      return lane;
    },
    /* 获取照明设备数据*/
    getLiPowerDevice() {
      getLiPowerDevices().then((response) => {
        let res = response.data;
        if (res.state == 0) {
          this.lightState = "正常";
        } else if (res.state == -2) {
          this.lightState = "异常";
        }
        // 照明设备
        for (let i = 0; i < res.list.length; i++) {
          // 图标
          for (let j = 0; j < this.selectedIconList.length; j++) {
            // 设备id如果相等
            if (res.list[i].eqId == this.selectedIconList[j].eqId) {
              // 设备状态
              for (let k = 0; k < this.eqTypeStateList.length; k++) {
                // 设备状态相同且设备类型相同
                if (
                  this.selectedIconList[j].eqType ==
                    this.eqTypeStateList[k].type &&
                  res.list[i].switchStatus == this.eqTypeStateList[k].state
                ) {
                  // 改变图标
                  this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                  this.selectedIconList[j].lightValue = res.list[i].lightValue;
                }
              }
            }
          }
        }
      });
    },
    /* 获取实时数据PLC*/
    getRealTimeData() {
      // 真实
      //getConfigData(this.currentTunnel.id)
      // 模拟
      // getStorageData({
      //     tunnelId: this.currentTunnel.id
      //   })
      //   .then((response) => {
      //     for (let i = 0; i < response.length; i++) {
      //       // 实时状态
      //       let type = response[i].devType;
      //       if (type != "" && type != undefined) {
      //         for (let j = 0; j < this.selectedIconList.length; j++) {
      //           if (response[i].devId == this.selectedIconList[j].eqId) {
      //             // 需要换光标的
      //             for (let k = 0; k < this.eqTypeStateList.length; k++) {
      //               if (
      //                 this.selectedIconList[j].eqType ==
      //                 this.eqTypeStateList[k].type &&
      //                 response[i].state == this.eqTypeStateList[k].state
      //               ) {
      //                 let url = this.eqTypeStateList[k].url;
      //                 this.selectedIconList[j].eqDirection =
      //                   response[i].direction;
      //                 if (response[i].direction == "1") {
      //                   //上行车道
      //                   if (url.length > 1) {
      //                     this.selectedIconList[j].url = [url[1], url[0]];
      //                   } else {
      //                     this.selectedIconList[j].url = url;
      //                   }
      //                 } else {
      //                   this.selectedIconList[j].url =
      //                     this.eqTypeStateList[k].url;
      //                 }
      //                 this.selectedIconList[j].state = response[i].state;
      //               }
      //               // 微波车检
      //               else if (
      //                 this.selectedIconList[j].eqType ==
      //                 this.eqTypeStateList[k].type &&
      //                 this.selectedIconList[j].eqType == "108" &&
      //                 type == "108"
      //               ) {
      //                 this.selectedIconList[j].wbList = response[i].state;
      //               }
      //               // 路面状态
      //               else if (
      //                 // this.selectedIconList[j].eqType == this.eqTypeStateList[k].type &&
      //                 this.selectedIconList[j].eqType == "120" && type == "120"
      //               ) {
      //                 this.selectedIconList[j].lmList = response[i].state;
      //               }
      //               // 道路结冰
      //               else if (
      //                 this.selectedIconList[j].eqType == "110" &&
      //                 type == "110"
      //               ) {
      //                 this.selectedIconList[j].dljb = response[i].state;
      //               }
      //               /* // 水泵
      //           else if (this.selectedIconList[j].eqType == '18' && type == '18') {
      //             this.selectedIconList[j].shuibeng = response[i].state
      //             if (response[i].state) {
      //               let devState = JSON.parse(response[i].state).devState
      //               if (this.selectedIconList[j].eqType == this.eqTypeStateList[k].type &&
      //                 devState == this.eqTypeStateList[k].state) {
      //                 this.selectedIconList[j].url = this.eqTypeStateList[k].url
      //               }
      //             }
      //           } */
      //             }
      //             // 不需要换光标的
      //             let paramType = [5, 6, 13, 14, 15, 16, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
      //             if (paramType.includes(parseInt(type))) {
      //               if (response[i].state == "null" || !response[i].state) {
      //                 this.selectedIconList[j].value = "0";
      //               } else {
      //                 this.selectedIconList[j].value = response[i].state;
      //               }
      //             }
      //           }
      //         }
      //       }
      //     }
      //   })
      //   .catch((err) => {
      //     // this.systemState = "较差";
      //   });
      getDeviceData({
        tunnelId: this.currentTunnel.id,
      }).then((response) => {
        // for (let i = 0; i < response.data.length; i++) {
        // debugger;
        // 实时状态
        // let type = response.data[i].eqType;
        // if (type != "" && type != undefined) {
        for (let j = 0; j < this.selectedIconList.length; j++) {
          var eqId = this.selectedIconList[j].eqId;
          var deviceData = response.data[eqId];
          // console.log(deviceData,'deviceDatadeviceData')
          if (deviceData) {
            // let type = deviceData.eqType;

            // 需要换光标的
            for (let k = 0; k < this.eqTypeStateList.length; k++) {
              if (
                this.selectedIconList[j].eqType == this.eqTypeStateList[k].type
              ) {
                //无法控制设备状态的设备类型，比如PLC、摄像机
                let arr = [
                  5, 14, 17, 18, 19, 20, 21, 23, 24, 25, 28, 29, 32, 33, 35, 22,
                  40, 39, 48, 41
                ];
                if (arr.includes(deviceData.eqType)) {
                  if (
                    // 摄像机之类的只有在线 离线 故障图标
                    this.eqTypeStateList[k].stateType == "1" &&
                    this.eqTypeStateList[k].state == deviceData.eqStatus
                  ) {
                    debugger
                    //取设备监测状态图标
                    this.selectedIconList[j].url = this.eqTypeStateList[k].url;
                    // if(deviceData.eqType == 48){
                    //   console.log(deviceData,"内外振动仪")
                    //   console.log(this.selectedIconList[j],"selectedIconListselectedIconListselectedIconList")
                    // }
                    if (deviceData.eqStatus == 1) {
                      if (deviceData.eqType == 19) {
                        this.selectedIconList[j].num =
                          "CO:" +
                          parseFloat(deviceData.CO).toFixed(2) +
                          "/PPM  VI:" +
                          parseFloat(deviceData.VI).toFixed(2) +
                          "M";
                      } else if (deviceData.eqType == 17) {
                        this.selectedIconList[j].num =
                          parseFloat(deviceData.FS).toFixed(2) +
                          "m/s " +
                          deviceData.FX;
                      } else if (deviceData.eqType == 5) {
                        if (deviceData.DWLD) {
                          this.selectedIconList[j].num =
                            parseFloat(deviceData.DWLD).toFixed(2) + "lux";
                        }
                      } else if (deviceData.eqType == 18) {
                        if (deviceData.DNLD) {
                          this.selectedIconList[j].num =
                            parseFloat(deviceData.DNLD).toFixed(2) + "lux";
                        }
                      }
                    }
                  }
                } else {
                  //可以控制设备状态的设备类型，比如车指
                  if (deviceData.eqStatus == "1") {
                    // 在线
                    if (
                      // 车指之类的包括正红反绿之类的图标 == 2
                      this.eqTypeStateList[k].stateType == "2"
                    ) {
                      if (this.eqTypeStateList[k].state == deviceData.state) {
                        //取设备运行状态图标
                        let url = this.eqTypeStateList[k].url;
                        this.selectedIconList[j].eqDirection =
                          deviceData.eqDirection;
                        if (deviceData.eqDirection == "1") {
                          //上行车道
                          if (url.length > 1) {
                            this.selectedIconList[j].url = [url[1], url[0]];
                          } else {
                            this.selectedIconList[j].url = url;
                          }
                        } else {
                          this.selectedIconList[j].url =
                            this.eqTypeStateList[k].url;
                        }
                      }
                    }
                  } else {
                    //如果是离线、故障等状态
                    if (
                      this.eqTypeStateList[k].stateType == "1" &&
                      this.eqTypeStateList[k].state == deviceData.eqStatus
                    ) {
                      //取设备监测状态图标
                      this.selectedIconList[j].url =
                        this.eqTypeStateList[k].url;
                    }
                  }
                }

                // let url = this.eqTypeStateList[k].url;
                // this.selectedIconList[j].eqDirection =
                // deviceData.eqDirection;
                // if (deviceData.eqDirection == "1") {
                //   //上行车道
                //   if (url.length > 1) {
                //     this.selectedIconList[j].url = [url[1], url[0]];
                //   } else {
                //     this.selectedIconList[j].url = url;
                //   }
                // } else {
                //   this.selectedIconList[j].url =
                //     this.eqTypeStateList[k].url;
                // }
                // this.selectedIconList[j].state = deviceData.eqStatus;
              }
            }
            // 不需要换光标的
            // let paramType = [5,17, 18, 19, 20]; //5 洞内 6 洞外 13 风向 14 CO监测 15 能见度 16 风速 20 水池液位
            // if (paramType.includes(parseInt(type))) {
            //   if (deviceData.eqStatus == "null" || !deviceData.eqStatus) {
            //     this.selectedIconList[j].value = "0";
            //   } else {
            //     this.selectedIconList[j].value = deviceData.eqStatus;
            //   }
            // }
          }
        }
      });
      if (this.currentTunnel.id != null && this.currentTunnel.id != "") {
        addBoardContent(this.currentTunnel.id).then((res) => {
          // console.log(res,"情报板显示内容查询");
          this.boardObj = res;
        });
      }
    },
    /* 选择隧道*/
    switchTunnel() {
      this.title = "切换隧道";
      this.tunnelVisible = true;
    },
    /* 选择隧道*/
    setTunnel(item, index) {
      console.log(item)
      console.log(index)
      this.tunnelItem = item
      // if(this.manageStation == "0" && item.tunnelId == "JQ-WeiFang-JiuLongYu-HSD"){
      //   this.$store.dispatch("manage/changeTunnelId", "JQ-WeiFang-JiuLongYu-HSD");
      //   return;
      // }
      this.getTunnelLane(item.lane);
      this.buttonIndex = index;
      this.tunnelNameEarlyWarn = item.tunnelName;
      this.tunnelId = item.tunnelId;
      this.lightControForm.index = index;
      this.lightControForm.name = item.tunnelName;
      // this.tunnelBtnStyle = []
      // this.tunnelBtnStyle[index] = {
      //   "border-color": "#ffbf1d",
      //   // "box-shadow": "0px 0px 10px #68B5FF inset",
      //   "color": "0a88bd",
      //   "background-color": "#e2f3fa !important",
      // };
      (this.currentTunnel.id = item.tunnelId),
        (this.currentTunnel.name = item.tunnelName),
        this.selectEquipmentType(this.currentTunnel.id);
      this.getTunnelData(this.currentTunnel.id);
      //先删除
      this.carList =[]
      this.carList = new Map()
      //小车显示控制
      carSwitchType(item.tunnelId, this.carShow ? 0:1).then((res) =>{})
      this.$nextTick(() => {
        this.getEnergyConsumption(this.currentTunnel.id);
      });
    },
    onActivated(key) {},
    onDragging(key) {},
    onDropped(key) {},
    monitorWebsocket() {},
    carchange(tunnelId) {
      getTunnels(tunnelId).then((res) => {
        console.log(res.data, "当前隧道信息");
        const tunnel = res.data;
        // math.subtract(a-b)//减
        // math.multiply(a*b)//乘
        // math.divide(a/b)//除
        // 计算公里数
        let Mileage = math.divide(
          math.subtract(+tunnel.endPileNum - +tunnel.startPileNum) / 1000
        );
        this.tunnelKm = Mileage;
        if (Mileage <= 1.3) {
          var length = +1300;
        } else if (Mileage < +2.6) {
          var length = +1300 * 1.5;
        } else if (Mileage > +2.6) {
          var length = +1300 * 2;
        }
        this.lane = tunnel.lane;
        this.tunnelLength = length; //px长度
        this.proportion = math.divide(length / (Mileage * 1000)); //计算px和米的比例
      });
      // 首页获取隧道长度，根据隧道长度判断车辆行驶的全部距离
    },
    /*点击设备类型*/
    displayControl(value, lable) {
      // carShow
      for (var item of this.selectedIconList) {
        if (this.tunnelId == "JQ-JiNan-WenZuBei-MJY" && item.eqType == 29) {
          this.robotShow = true;
        } else {
          this.robotShow = false;
        }
      }
      $(".leftButtonS")
        .eq(value)
        .addClass("leftButton_hover")
        .siblings()
        .removeClass("leftButton_hover");
      this.selectBigType = {
        bigType: lable,
        index: value,
      };
      let data = this.eqBigTypeList;

      var val = value.toString();
      hasListByBigType(val).then((response) => {
        let typelist = response.rows;
        let typeIndex = [];
        if (typelist.length > 0) {
          for (let y = 0; y < typelist.length; y++) {
            for (let i = 0; i < this.selectedIconList.length; i++) {
              if (typelist[y].typeId == this.selectedIconList[i].eqType) {
                typeIndex.push(i);
              }
              this.selectedIconList[i].display = false;

              // 没有eqType属性的图片依旧显示 例如：隧道名称
              if (
                this.selectedIconList[i].eqType == null ||
                this.selectedIconList[i].eqType == undefined
              ) {
                this.selectedIconList[i].display = true;
              }
            }
          }
          for (let i = 0; i < typeIndex.length; i++) {
            this.selectedIconList[typeIndex[i]].display = true;
          }
        } else {
          for (let i = 0; i < this.selectedIconList.length; i++) {
            this.selectedIconList[i].display = false;
          }
        }
      });
    },
    /* 传感器设备提示框 */
    sensorContent(item) {
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return "";
      } else {
        let deviceName = "设备名称：" + item.eqName; // 设备名称
        let deviceStake = " 桩号：" + item.pile; // 桩号
        let state = " 检测值：" + (item.value || "无"); // 检测值
        return (deviceName + deviceStake + state).toString();
      }
    },
    sensorDisabled(item) {
      // 需要显示那类设备，直接把设备的eqType值放入就可以
      let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        this.showTooltip = false;
      } else {
        this.showTooltip = true;
      }
    },
    sensorDisabledTwo(item) {
      let sensorDevice = [];
      // let sensorDevice = [5, 17, 19];
      if (sensorDevice.indexOf(item.eqType) == -1) {
        return true;
      } else {
        return false;
      }
    },
    clickRobot() {
      this.eqInfo.clickEqType = 29;
      for (var item of this.selectedIconList) {
        if (item.eqType == 29) {
          console.log(item, "机器人");
        }
      }
    },
    //================================================单个配置开始==================================
    /* 打开配置界面*/
    async openStateSwitch(item) {
      console.log(item, "item");
      if (this.addBatchManage == true) {
        // 判断设备是否可控 不可控的不弹批量弹窗
        if(item.isControl == '1'){
           // 判断是否有选中项 有的话 判断本次点击和上次点击 设备类型是否一样
        // 要求每次点击选中的设备类型相同
          if (this.itemEqType) {
            // 第二次点击时
            for (var itm of this.selectedIconList) {
              //  多选 选择的设备类型相同时
              if (itm.eqId == item.eqId && this.itemEqType == item.eqType) {
                // 比对id 如果曾点过 取消选框
                const result = this.itemEqId.findIndex(
                  (item) => item == itm.eqId
                );
                if (result === -1) {
                  itm.click = true;
                  this.itemEqId.push(itm.eqId);
                  this.$forceUpdate();
                } else {
                  this.itemEqId.splice(result, 1);
                  itm.click = false;
                  this.$forceUpdate();
                  if (this.itemEqId.length == 0) {
                    this.itemEqType = "";
                    this.batchManageForm.eqType = "";
                    this.batchManageForm.eqDirection = "";
                    // this.addBatchManage = false;
                    this.$forceUpdate();
                  }
                }
              }
              // 多选 选择的设备类型不同时 提示红字
              else if (itm.eqId == item.eqId && this.itemEqType != item.eqType) {
                itm.textFalse = true;
                this.$forceUpdate();
              }
            }
          } else {
            // 第一次点击时
            for (let itm of this.selectedIconList) {
              // console.log(itm);
              if (itm.eqId == item.eqId) {
                itm.click = true;
                this.itemEqId.push(itm.eqId);
                this.itemEqType = itm.eqType;
                this.batchManageForm.eqType = itm.eqType;
                this.batchManageForm.eqDirection = itm.eqDirection;
                this.$forceUpdate();
                getType(itm.eqType).then((res) => {
                  console.log(res, "查询设备图标宽高");
                  this.iconWidth = res.data.iconWidth;
                  this.iconHeight = res.data.iconHeight;
                });
              }
            }
          }
        }else if(item.isControl == '0'){
          item.textKKFalse = true
          this.$forceUpdate();
        }

      } else if (this.addBatchManage == false) {
        this.mouseoversImplement = false;
        console.log(item, "点击的设备");
        this.eqInfo = {
          clickEqType: item.eqType,
          equipmentId: item.eqId,
        };

        if(this.dialogEqType != item.eqType){
          this.dialogClose()
        }else{
          this.dialogEqType = item.eqType
        }
        this.$nextTick(()=>{
          if([23, 24, 25].includes(item.eqType)){
            this.$refs.videoRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if([1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 45, 49].includes(item.eqType)){
            this.$refs.lightRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 19){
            this.$refs.coviRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if([14, 21, 32, 33, 15, 35, 39, 40, 41, 42, 48 ].includes(item.eqType)){
            this.$refs.dataRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 17){
            this.$refs.windRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 28){
            this.$refs.pressureRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 20){
            this.$refs.vehicleDetecRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 34){
            this.$refs.callPoliceRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 5 || item.eqType == 18){
            this.$refs.brightRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 30 || item.eqType == 31){
            this.$refs.youdaoRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 16 || item.eqType == 36){
            this.$refs.boardRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }else if(item.eqType == 22){
            this.$refs.radioRef.init(this.eqInfo,this.brandList,this.directionList,this.eqTypeDialogList)
          }

        })




        // 防止 ‘暂未获取’ 和 配置状态单选同时出现
        // this.spanEqtypeDate = true;
        // let newPromise = new Promise((resolve) => {
        //   resolve();
        // });
        // await newPromise.then(() => {
        //   this.eqTypeStateList.forEach((val) => {
        //     if (item.eqType == val.type && val.control == 1) {
        //       this.itemEqTypeStateList.push(val);
        //     }
        //   });
        // });
        // if (this.itemEqTypeStateList != []) this.spanEqtypeDate = false;
        // 传感器（模拟量）
        // let sensorDevice = [1, 2, 3, 4, 7, 8, 9, 10, 12, 13, 14, 15, 16];
        // if (sensorDevice.indexOf(item.eqType) != -1) {
        //   // this.stateSwitchVisible = true;
        //   // this.stateForm = {
        //   //   value: item.value,
        //   // };
        //   // this.$nextTick(()=>{
        //   //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
        //   // })
        // }

        //跳转微波车检
        // if (item.eqType == "108") {
        //   this.weiboList = eval(item.wbList);
        //   this.title = item.eqName;
        //   this.weiboVisible = true;
        //   return;
        // }
        // // 跳转路面状态
        // else if (item.eqType == "120") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1,
        //   };
        //   const eqId = {
        //     eqId: item.eqId
        //   };
        //   selectByEqDeno(eqId).then(response => {
        //     this.lumianList = response.data;
        //   })
        //   if (item.lmList) {
        //     this.lmList = JSON.parse(item.lmList)
        //   }
        //   this.title = item.eqName;
        //   this.lumianVisible = true;
        //   return;
        // }
        // else if (item.eqType == "10") {
        //   this.spanEqtypeDate = false
        //   // 风机
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //     tunnelName:item.tunnelName.tunnelName
        //   };
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        // }
        // else if (item.eqType == "8") {
        //   this.spanEqtypeDate = false
        //   // 引道照明
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //   };
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;

        // }
        //跳转 摄像机
        // else if (item.eqType == "24" || item.eqType == "23") {
        //   console.log("点击摄像机")
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //   };
        //   this.title = item.eqName;
        //   // this.cameraVisible = true;
        //   this.loading = true;
        //   // this.flvPlayer()
        //   setTimeout(this.changeLoading, 2000);
        //   return;
        // }
        // else if(item.eqType == "17"){
        //   this.title = '风速风向检测器'
        //   this.coviVisible = true
        // }
        // else if(item.eqType == "19"){
        //   this.title = 'CO/VI检测器'
        //   this.coviVisible = true
        // }
        // 水泵
        /* else if (item.eqType == "18") {
          if (item.shuibeng && item.shuibeng!='null') {
            this.shuibengObj = JSON.parse(item.shuibeng);
            this.shuibengObj.devState = "正常";
          } else if(!item.shuibeng) {
            this.shuibengObj = {
              "beng1": "关闭", "beng2": "关闭", "dianyuan": "无", "queshui": "无", "liandong": "未联动", "devState": "离线"
            }
          }
          this.title = item.eqName
          this.shuibengVisible = true
          return;
        } */
        //跳转道路结冰
        // else if (item.eqType == "110") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: "", //默认状态
        //     pile: item.pile,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1,
        //   };
        //   if (item.dljb) {
        //     this.dljbObj = JSON.parse(item.dljb);
        //   }
        //   this.title = item.eqName;
        //   this.daolujiebingVisible = true;
        //   return;
        // }
        // //打开情报板
        // else if (item.eqType == "100") {
        //   await getDeviceBase(item.eqId).then((data) => {
        //     console.log('情报板大小')
        //     this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
        //   });
        //   console.log(item, '情报板信息');
        //   this.dialogVisible = true;
        //   this.$refs.vmsContentUpdate.deviceId = item.eqId;
        //   this.$refs.vmsContentUpdate.isAdd = true;
        //   // this.$refs.contentBatchEdit.list = item;
        //   this.$refs.vmsContentUpdate.init();
        //   // this.dialogVisible = true;
        //   // this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,item);
        // } else if (item.eqType == "111") {
        //   const eqId = {
        //     equipmentId: item.eqId,
        //     tunnelId: this.tunnelId
        //   }
        //   this.spanEqtypeDate = false
        //   var pressureData = {}
        //   pressure(eqId).then(response => {
        //     console.log(response, "压力表")
        //     pressureData = response.data[0]
        //     this.stateForm = {
        //       eqType: item.eqType,
        //       eqTypeName: item.typeName.typeName,
        //       eqDirection: item.eqDirection,
        //       pile: item.pile,
        //       value: item.value,
        //       equipmentName: pressureData.equipmentName,
        //       position: pressureData.position,
        //       tunnelName: pressureData.tunnelName,
        //       analogQuantity: pressureData.analogQuantity,
        //       highest: pressureData.highest,
        //       low: pressureData.low,
        //       createTime: pressureData.createTime
        //     };
        //   });
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        // } else if (item.eqType == "112") {
        //   // 巡检机器人
        //   this.stateForm = {
        //     ...item,
        //     value: item.value,
        //     state: '正常',
        //     electricQuantity: '80',
        //     eqTypeName: item.typeName.typeName,
        //     eqTunnelName: item.tunnelName.tunnelName,
        //     manufacturers: '苹果AI',
        //     dateOfManufacture: '2022/2/23 14:30:45',
        //   };
        //   this.robotInformationCollectionData = {}
        //   // 状态记录
        //   this.robotRecordList = [{
        //       robotName: '巡检机器人-001',
        //       tunnelName: '马家峪隧道',
        //       DateTime: '2022-2-23 15:45:13',
        //       state: '离线'
        //     },
        //     {
        //       robotName: '巡检机器人-001',
        //       tunnelName: '马家峪隧道',
        //       DateTime: '2022-2-23 10:23:00',
        //       state: '正常'
        //     },
        //   ]
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;

        // } else if (item.eqType == "118") {
        //   if (!item.eqFeedbackAddress1) {
        //     this.$modal.msgWarning('没有给设备配置点位地址')
        //     this.youdaoVisible = false
        //     return
        //   }
        //   this.title = item.eqName
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqDirection: item.eqDirection,
        //     eqFeedbackAddress1: item.eqFeedbackAddress1
        //   }
        //   // 诱导灯
        //   getHostData({
        //     hostIP: item.eqFeedbackAddress1
        //   }).then(response => {
        //     this.stateForm = {
        //       eqId: item.eqId,
        //       eqDirection: item.eqDirection,
        //       eqFeedbackAddress1: item.eqFeedbackAddress1,
        //       corModel: response.corModel,
        //       Zlane: (response.Zlane == "true" ? true : false),
        //       Ylane: (response.Ylane == "true" ? true : false),
        //       whiteLight: parseInt(response.whiteLight),
        //       yellowLight: parseInt(response.yellowLight),
        //       twinkleModel: response.twinkleModel,
        //       twinkleFrequency: response.twinkleFrequency,
        //       lightTime: response.lightTime
        //     }
        //   })
        //   this.youdaoVisible = true
        // }
        // else if (item.eqType == 1){
        //   this.batchVisible = false;
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state, //默认状态
        //     pile: item.pile,
        //     value: item.value,
        //     lightValue: !item.lightValue ? 0 : item.lightValue,
        //     fjState: item.state,
        //   };
        // }
        // else if (item.eqType == "123") {
        //   this.stateForm = {
        //     eqId: item.eqId,
        //     eqHostId: item.eqHostId,
        //     eqName: item.eqName,
        //     eqType: item.eqType,
        //     eqTypeName: item.typeName.typeName,
        //     eqDirection: item.eqDirection,
        //     state: item.state == '1' ? '已报警' : '未报警',
        //     pile: item.pile,
        //   };
        //   console.log(this.stateForm, "声光报警")
        //   this.title = item.eqName;
        //   this.stateSwitchVisible = true;
        //   return;
        // } else {
        //   //配置
        //   if (item.environmentType == undefined) {
        //     this.batchVisible = false;
        //     if (
        //       item.typeName == undefined ||
        //       item.eqDirection == undefined ||
        //       item.eqDirection == null
        //     ) {
        //       this.$message({
        //         message: "未能获取设备信息,请重新配置！",
        //         type: "warning",
        //         customClass: "workbench-msg",
        //       });
        //       this.cleanUp();
        //       this.empty();
        //     } else {
        //       // 红绿灯
        //       this.spanEqtypeDate = false
        //       this.stateForm = {
        //         eqId: item.eqId,
        //         eqHostId: item.eqHostId,
        //         eqName: item.eqName,
        //         eqType: item.eqType,
        //         eqTypeName: item.typeName.typeName,
        //         eqDirection: item.eqDirection,
        //         state: item.state, //默认状态
        //         pile: item.pile,
        //         value: item.value,
        //         lightValue: !item.lightValue ? 0 : item.lightValue,
        //         fjState: item.state,
        //       };
        //       this.title = item.eqName;
        //       // this.stateSwitchVisible = true;
        //       let sensorDevice = [ 15, 16, 20, 28,  34,31];
        //       if (sensorDevice.indexOf(item.eqType) != -1) {
        //         this.stateSwitchVisible = true;
        //         // this.stateForm = {
        //         //   value: item.value,
        //         // };
        //         // this.$nextTick(()=>{
        //         //    if(item.value == '0'||item.value)this.alarmsCharts(item.eqType)
        //         // })
        //       }
        //     }
        //   }
        // }
      }
    },
    // loadFlv() {
    //   if (flvjs.isSupported()) {
    //     var videoElement = document.getElementById("videoBox");
    //     var flvPlayer = flvjs.createPlayer({
    //       type: 'flv',
    //       url: 'http://10.166.139.12:8081/live/22456.flv' //你的url地址
    //     });
    //     flvPlayer.attachMediaElement(videoElement);
    //     flvPlayer.load();
    //     flvPlayer.play();
    //   }
    // },
    /* 确认配置（单项）*/
    // submitState() {
    //   this.stateSwitchVisible = false;
    //   // this.stateForm ={}
    //   let param = {
    //     // devId: this.stateForm.eqId,
    //     // devType: this.stateForm.eqType,
    //     // hostId: this.stateForm.eqHostId,
    //     // state: this.stateForm.state,
    //     // tunnelId: this.currentTunnel.id,
    //     deviceId: this.stateForm.eqId,
    //     devType: this.stateForm.eqType,
    //     hostId: this.stateForm.eqHostId,
    //     state:
    //       this.stateForm.eqType == 31
    //         ? this.radioEqType31
    //         : this.stateForm.state,
    //     tunnelId: this.currentTunnel.id,
    //     brightness: this.brightness,
    //     frequency: this.frequency,
    //   };
    //   // 发送模拟指令
    //   this.sendAnalogCommand(param);
    //   // 发送正式指令
    //   // this.sendDirective(param)
    //   // if (await this.sendDirective(param)) {
    //   //   // 非照明类（plc）提交
    //   //   this.setConfigData(param);
    //   // } else {
    //   //   this.setWebsocket(param);
    //   // }
    // },
    sendAnalogCommand(param) {
      sendAnalogCom(param).then((response) => {
        if (response.code == 200) {
          this.$modal.msgSuccess("配置成功");
        }
      });
      // const res = await deviceApi.sendAnalogCom(param)
      // console.log(res)
      // return new Promise((resolve, reject) => {
      //   resolve(true);
      // });
    },

    //正式方法
    async sendDirective(param) {
      const res = await deviceApi.sendDirective(param);
      return new Promise((resolve, reject) => {
        resolve(true);
      });
    },
    async getTypeFlag(param) {
      let type = param.devType;
      let hostId = param.hostId;
      const response = await getHosts(hostId);
      let plcControlType = response.data.plcControlType;
      return new Promise((resolve, reject) => {
        if (plcControlType == "CIO") {
          resolve(true);
        } else {
          // 加强、基本、
          if (type == 7 || type == 8 || type == 9 || type == 21) {
            resolve(false);
          } else {
            resolve(true);
          }
        }
      });
    },

    /* 提交配置*/
    setConfigData(param) {
      setConfigData(param)
        .then((response) => {
          console.log("记录成功");
        })
        .catch((err) => {
          console.log("记录失败,后台报错：" + err);
        });
    },
    setWebsocket(param) {
      let actions = "";
      // 发送指令
      if (param.devType == 21) {
        actions = "Light LightValue " + param.devId + " ";
        actions = actions + this.stateForm.lightValue;
      } else {
        let state = "";
        if (param.state == 1) {
          state = 1;
        } else if (param.state == 2) {
          state = 0;
        }
        actions = "Device PowerSet " + param.devId + " " + state;
      }

      this.websocketsend(actions);
    },

    /* 诱导确认配置（单项）*/
    submitCorLight() {
      if (this.stateForm.corModel == null) {
        this.$modal.msgError("请选择控制模式后重试！");
        return;
      }
      let param = {
        hostIP: this.stateForm.eqFeedbackAddress1,
        corModel: this.stateForm.corModel,
        Zlane: this.stateForm.Zlane,
        Ylane: this.stateForm.Ylane,
        whiteLight: this.stateForm.whiteLight,
        yellowLight: this.stateForm.yellowLight,
        twinkleModel: this.stateForm.twinkleModel,
        twinkleFrequency: this.stateForm.twinkleFrequency,
        lightTime: this.stateForm.lightTime,
      };
      this.setCorLight(param)
        .then((response) => {
          this.$modal.msgSuccess("控制成功");
          this.youdaoVisible = false;
        })
        .catch(() => {
          this.$modal.msgError("控制失败，请检查设备或稍后重试！");
        });
    },
    setCorLight(param) {
      setCorLight(param)
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log("配置失败,后台报错：" + err);
        });
    },
    /**诱导弹窗-控制模式改变 */
    outLight() {
      this.stateForm.whiteLight = 0;
      this.stateForm.yellowLight = 0;
    },
    //================================================单个配置结束==================================
    //================================================批量配置开始====================================
    /* 点击批量配置*/
    // ------------------------------------------老版批量操作功能开始----------------------------------
    // batchManage() {
    // this.devicesList = [];
    // this.batchVisible = true;
    // mode = "buttonSelection";
    // this.title = "批量操作";
    // // this.$refs.batchForm.clearValidate();
    // this.batchForm.eqType = this.eqTypeList[1].typeId;
    // let param = {
    //   eqTunnelId: this.currentTunnel.id,
    //   eqType: this.eqTypeList[1].typeId,
    //   eqDirection: "",
    //   lane: "",
    // };
    // this.batchFormDirection = 1;
    // this.selectEquipment(param);
    // },
    // ------------------------------------------老版批量操作功能结束----------------------------------

    /* 批量选择后*/
    handleSelectionChange(val) {
      this.ids = val.map((item) => item.id);
      this.single = val.length !== 1;
      this.multiple = !val.length;
      this.batchForm.eqList = [];
      // this.batchFormDirection = 1
      for (let i = 0; i < val.length; i++) {
        if (val[i + 1] != undefined) {
          if (
            val[i].typeName.typeName.indexOf("车道指示器") != -1 &&
            val[i].eqDirection != val[i + 1].eqDirection
          ) {
            this.$message({
              message: "请选择相同方向的设备进行配置！",
              type: "warning",
              customClass: "workbench-msg",
            });
            this.$refs.multipleTable.clearSelection();
            this.batchForm.eqList = [];
            break;
          } else {
            this.batchFormDirection = val[i].eqDirection;
            this.batchForm.eqList.push(val[i]);
          }
        } else {
          this.batchFormDirection = val[i].eqDirection;
          this.batchForm.eqList.push(val[i]);
        }
      }
    },
    everyCheck(number) {
      return number != 100;
    },
    /* 批量确认按钮*/
    // submitBatchState(formName) {
    //   let that = this;
    //   if (that.batchForm.eqList.length == 0) {
    //     return that.$modal.msgWarning("请选择设备");
    //   }

    //   // 批量情报板编辑
    //   // 一 是否有选中情报板
    //   // 二 选中是否全部为情报板
    //   // 三 所有选中情报板分辨率是否一致
    //   // 满足条件后执行批量编辑
    //   // this.$refs.contentBatchEdit.vmsSize = list[0].vmsSize;
    //   // this.$refs.contentBatchEdit.list = list;
    //   // this.$refs.contentBatchEdit.radio1 = this.radio1;
    //   // this.$refs.contentBatchEdit.init();
    //   let IntelligenceBoard = that.batchForm.eqList;
    //   for (let i = 0; i < IntelligenceBoard.length; i++) {
    //     var arr = IntelligenceBoard[i].eqType;
    //     if (IntelligenceBoard[0].eqType != arr) {
    //       return that.$modal.msgWarning("设备类型不一致");
    //     }
    //   }
    //   let brr = [];
    //   that.batchForm.eqList.forEach((item) => {
    //     brr.push(item.eqType);
    //   });
    //   if (brr.every(this.everyCheck) == false) {
    //     //如果是情报板设备，则取消验证
    //     this.rules.state = true;
    //     // this.$refs.contentBatchEdit.init();
    //     // 上面已经判断过是否一致，所以此处不再判断，直接传第一个设备的eqid
    //     // 打开情报板编辑页面
    //     getDeviceBase(that.batchForm.eqList[0].eqId).then((data) => {
    //       this.$refs.vmsContentUpdate.vmsSize = data.data.devicePixel;
    //     });
    //     // console.log(item,'情报板信息');
    //     this.dialogVisible = true;
    //     let batchFormList = that.batchForm.eqList;
    //     let deviceList = "";
    //     batchFormList.forEach((item) => {
    //       deviceList = deviceList.concat(item.eqId);
    //     });

    //     this.$refs.vmsContentUpdate.deviceId = deviceList;
    //     this.$refs.vmsContentUpdate.isAdd = false;
    //     this.$refs.vmsContentUpdate.init();
    //     return false;
    //   }

    //   // end
    //   this.$refs[formName].validate((valid) => {
    //     let eqIdList = [];
    //     if (valid) {
    //       for (let i = 0; i < that.batchForm.eqList.length; i++) {
    //         eqIdList.push(that.batchForm.eqList[i].eqId);
    //       }
    //       if (eqIdList.length > 0) {
    //         for (let i = 0; i < eqIdList.length; i++) {
    //           let param = {
    //             devId: eqIdList[i],
    //             devType: that.batchForm.eqType,
    //             state: that.batchForm.state,
    //             tunnelId: that.currentTunnel.id,
    //           };
    //           that.setConfigData(param);
    //           that.sendAnalogCommand({
    //             deviceId: that.batchForm.eqList[i].eqId,
    //             devType: that.batchForm.eqList[i].eqType,
    //             hostId: that.batchForm.eqList[i].eqHostId,
    //             state: that.batchForm.state,
    //             tunnelId: that.currentTunnel.id,
    //           });

    //           that.$refs["batchForm"].clearValidate();
    //           that.batchVisible = false;
    //         }
    //         // this.dialogVisible = true;
    //         // this.$refs.intelligenceBoard.childerfunction(this.dialogVisible,eqIdList);
    //       }
    //     } else {
    //       return false;
    //     }
    //   });
    // },
    // 点击某一行，将其选中
    handleRowClick(row, i, a) {
      this.$refs.multipleTable.toggleRowSelection(row);
    },
    //========================================批量配置结束================================================
    //========================================控制策略开始================================================
    /* 跳至策略页面*/
    strategyPage() {
      //this.$router.push('/strategy/index')
      this.loading = true;
      this.queryParams.strategyName = ''
      this.strategyVisible = true;
      this.title = "控制策略";
      this.queryParams.pageNum = 1;
      this.handlestrategyQuery();

      // if (this.currentTunnel.id) {
      //   listStrategy({
      //     strategyType: 0,
      //     tunnelId: this.currentTunnel.id,
      //   }).then((response) => {
      //     this.strategyList = response.rows;
      //     this.loading = false;
      //   });
      // }
    },
    handleClick(tab, event) {
      this.dictCode = tab.index;
      this.queryParams.strategyGroup = Number(tab.index) + Number(1);
      // this.syxt_boxShow = false
      // this.sycz_boxShow1 = false
      // this.syxt_boxShow2 = false
      // this.sycz_boxShow3 = false
      this.handleQueryOperationParam()
      this.handlestrategyQuery();
    },
    //系统日志操作日志tab切换
    handleTabClick(tab,event){
      if(tab.name == 'xitong'){
        // 系统日志
        this.dateRange = this.getPastTime();
        this.getOperationList("xitong");
      }else{
        // 操作日志
        this.dateRange1 = this.getPastTime();
        this.getOperationList("caozuo");

      }
    },
    // 关闭控制策略对话框
    strategyCancel() {
      this.strategyVisible = false;
    },
    handlestrategyQuery() {
      this.loading = true;
      // this.syxt_boxShow = false
      // this.sycz_boxShow1 = false
      this.syxt_boxShow2 = false
      this.sycz_boxShow3 = false
      listStrategy(this.queryParams).then((response) => {
        this.strategyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 发送控制指令
    handleController(row) {
      this.$modal.msgSuccess("手动控制中.......");
      handleStrategy(row.id);
    },
    // 查看策略，表格的行样式
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 2 == 0) {
        return "tableEvenRow";
      } else {
        return "tableOddRow";
      }
    },
    //========================================控制策略结束================================================
    /* 跳至操作日志页面*/
    operationLogPage() {
      // this.$router.push({
      //   name: "OperationLog",
      // });
      this.dateRange = this.getPastTime();
      this.dateRange1 = this.getPastTime();
      this.title = "操作日志";
      this.operationActive = 'xitong';
      this.operationLogDialog = true;
      this.operationParam_xt.ipaddr = ''
      this.operationParam.operIp = ''
      this.getOperationList("xitong");
      // this.getList();
    },

    /* 打开图标说明对话框*/
    iconExplain() {
      this.explainVisible = true;
      this.title = "图标含义";
    },
    /* 关闭所有对话框*/
    cancel() {
      this.operationParam.pageNum = 1;
      this.operationParam_xt.pageNum = 1;
      this.operationLogDialog = false;
      this.tunnelVisible = false;
      this.stateSwitchVisible = false;
      this.explainVisible = false;
      this.batchVisible = false;
      this.shuibengVisible = false;
      this.lumianVisible = false;
      this.weiboVisible = false;
      this.cameraVisible = false;
      this.daolujiebingVisible = false;
      this.lightControlDialog = false;
      this.youdaoVisible = false;
      this.cameraErrorInfo = "";
      this.batchForm = {
        eqType: "",
        eqList: [],
        state: "",
      };
      this.stateForm = {};
    },
    handleTableWheel(event) {
      console.log(event, "event");
      // let obj = document.getElementsByClassName('content')
      let obj = this.$refs.divRoller;
      if (this.handleTableWheelSwithch == true) {
        this.tableZoom(obj, event);
      }
    },
    tableZoom(obj, event) {
      console.log(obj, event, "obj, event");
      // 一开始默认是100%
      let zoom = parseInt(obj.style.zoom, 10) || 100;
      // 滚轮滚一下wheelDelta的值增加或减少120
      zoom += event.wheelDelta / (Math.abs(event.wheelDelta) / 10);

      // zoom > 130 ? zoom = 100 : ''
      zoom < 60 ? (zoom = 60) : "";
      zoom > 140 ? (zoom = 140) : "";
      this.zoom = zoom;
      this.setMoveTop(zoom);
      if (zoom > 0) {
        obj.style.zoom = zoom + "%";
      }
      this.fnWheel(obj, event, zoom);
      return false;
    },
    setMoveTop(zoom) {
      console.log(zoom, "zoom");
      var ii = 0;
      // if(!zoom){
      //   zoom = 100
      // }
      switch (zoom) {
        case 110:
          ii = 1;
          break;
        case 120:
          ii = 3.5;
          break;
        case 130:
          ii = 7;
          break;
        case 140:
          ii = 11;
          break;
        default:
          ii = 0;
          break;
      }
      if (zoom == 90) {
        ii = 1; //0.11
      } else if (zoom == 80) {
        ii = 5; //0.25
      } else if (zoom == 70) {
        ii = 13; //0.43
      } else if (zoom == 60) {
        ii = 26; //0.66
      } else if (zoom == 50) {
        ii = 50; //1
      } else if (zoom == 40) {
        ii = 90; //1.5
      } else if (zoom == 30) {
        ii = 164; //2.34
      } else if (zoom == 20) {
        ii = 320; //4
      } else if (zoom == 10 || zoom == 0) {
        ii = 800; //9
      }
      this.moveTop = (100 - zoom + ii) / 100;
    },
    fnWheel(obj, e, zoom) {
      // 思路：以鼠标为中心实现滚动的话，那么将会鼠标在背景图片（注意我这里是用背景图片的，不是img的）中滚动的时候的位置比率是不会变的
      if (e.wheelDelta > 0) {
        this.wheelFlag = true;
      } else {
        this.wheelFlag = false;
      }
      console.log(this.$refs.divRoller, "this.$refs.divRoller");
      this.oldWidth = this.$refs.divRoller.offsetWidth;
      this.oldHeight = this.$refs.divRoller.offsetHeight;
      this.mouseLeft = e.clientX - this.$refs.divRoller.offsetLeft;
      this.mouseTop = e.clientY - this.$refs.divRoller.offsetTop;
      // 分别计算出scaleX,scaleY的倍数
      this.scaleX = this.mouseLeft / this.oldWidth;
      this.scaleY = this.mouseTop / this.oldHeight;

      obj.style.zoom = zoom + "%";
      // 鼠标为中心的开始点,如果去掉这个将会以左上角开始滚动图片
      this.newWidth = this.$refs.divCanvas.offsetWidth;
      this.newHeight = this.$refs.divCanvas.offsetHeight;
      obj.style.left = zoom + "%";
      obj.style.top =
        this.$refs.divRoller.offsetTop -
        this.scaleY * (this.newHeight - this.oldHeight) +
        "px";
    },
    // 所有指示器控制 打开弹窗
    // lightControl(){
    //   this.lightControlDialog = true
    //   this.title = '车指批量控制'
    //   //获取当前选择的是哪个隧道
    //   const index = this.lightControForm.index
    //   listTunnels().then(response => {
    //       //获取当前隧道有几个车道
    //       this.lightControForm.lane = response.rows[index].lane
    //       if(!this.lightControForm.name){
    //           //获取当前隧道名字
    //           this.lightControForm.name = response.rows[0].tunnelName
    //       }
    //   });
    // },
    // 所有车指器弹窗确定按钮
    // lightSubmitState(){
    //     // 所有隧道的所有设备
    //     var itemArr = []
    //     listDevices({eqType:1}).then((response) => {
    //             for(var item of response.rows){
    //                 if(this.lightControForm.name == item.tunnelName.tunnelName){
    //                     // 通过隧道名 获取当前隧道的所有设备
    //                     if(this.lightControForm.lCDirection == item.eqDirection){
    //                         // 当前选中的车道 和 设备所属车道对应上 获取该车道里设备的id
    //                         if(this.lightControForm.state == item.lane){
    //                             itemArr = itemArr.concat(item)
    //                         }
    //                     }
    //                 }
    //             }
    //           for(var index of itemArr){
    //               let param = {
    //                 devId: index.eqId,
    //                 devType: index.eqType,
    //                 state: index.lane,
    //                 tunnelId: index.eqTunnelId,
    //                 hostId: index.eqHostId,
    //               }
    //               setConfigData(param).then(response => {
    //                 this.lightControForm.lCDirection = ''
    //                 this.lightControForm.state = ''
    //                 this.lightControForm.radioLightControlTop = ''
    //                 this.lightControForm.radioLightControlBottom = ''

    //               }).catch(err => {
    //               })
    //           }

    //     });

    //     this.lightControlDialog = false
    // },
    // changeLightControlTop(){
    //     if(!this.lCTop){
    //         this.lCTop = true
    //     }else{
    //         this.lCTop = false
    //     }
    // },
    // changeLightControlBottom(){
    //     if(!this.lCBottom){
    //         this.lCBottom = true
    //     }else{
    //         this.lCBottom = false
    //     }
    // },
    // 模拟量曲线图
    alarmsCharts(type) {
      let units = "";
      if (type == 14) units = "ppm"; // CO
      // if(type == 13) units = 'ppm' // 风向
      if (type == 5) units = "cd/m2"; // 光强
      if (type == 15) units = "ppm"; // 能见度
      if (type == 16) units = "m/s"; // 能见度
      var xData = [];
      for (let i = 2; i <= 24; i += 2) {
        xData.push(i + ":00");
      }
      var monthCharts = echarts.init(document.getElementById("analogQuantity"));
      let yData = [0, 1, 1, 2, 0, 1, 1, 1, 0, 1, 1, 0];

      var option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
              fontSize: 12,
            },
          },
          textStyle: {
            fontSize: "100%",
          },
          formatter: (v) => {
            return `
                    <div class='u-p-2'>
                      <div class='fz-10'>时间：${v[0].axisValue}</div>
                      <div class='fz-10 u-mt-2'>模拟量：${v[0].value}${units}</div>
                    </div>
                  `;
          },
        },
        grid: {
          top: "20%",
          left: "10%",
          bottom: "15%",
        },
        xAxis: {
          type: "category",
          boundaryGap: false, // 两边留白
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: 14,
            },
          },
          data: xData,
          axisLine: {
            lineStyle: {
              color: "#000",
              width: 1,
            },
          },
          splitLine: {
            lineStyle: {
              type: "dashed",
              color: "rgba(255,255,255,.2)",
              width: 1,
            },
            show: false,
          },
        },
        yAxis: {
          name: units,
          type: "value",
          scale: true,
          nameTextStyle: {
            color: "#fff",
            align: "left",
            padding: [0, 10, 0, 0],
            fontSize: "100%",
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: "100%",
            },
            margin: 8,
          },
          axisLine: {
            lineStyle: {
              color: "#000",
              width: 1,
            },
          },
          splitLine: {
            lineStyle: {
              type: "solid",
              color: "rgba(255,255,255,.2)",
              width: 1,
            },
            show: false,
          },
        },
        series: {
          name: "",
          type: "line",
          stack: "",
          // 修改的是线下区域的颜色
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: "rgba(236, 99, 123, .5)",
              },
              {
                offset: 1,
                color: "rgba(102, 212, 250,.0)",
              },
            ]),
          },
          // 修改的是线的颜色
          lineStyle: {
            color: {
              type: "linear",
              x: 0,
              y: 1,
              x2: 0,
              y2: 0,
              // 0% 处的颜色                           // 100% 处的颜色
              colorStops: [
                {
                  offset: 0,
                  color: "#3a76f6",
                },
                {
                  offset: 0.25,
                  color: "#66d4fa",
                },
                {
                  offset: 0.75,
                  color: "#f8d470",
                },
                {
                  offset: 1,
                  color: "#ec637b",
                },
              ],
              global: false, // 缺省为 false
            },
            width: 2,
          },
          //图例样式，默认空心圆，设为none是实心，还有'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow', 'none'
          symbol: "none",
          data: yData,
        },
      };
      // 使用刚指定的配置项和数据显示图表。
      monthCharts.setOption(option);
    },
    // 跳转页面
    jumpLink(url) {
      if (url == "/15/status") {
        this.$modal.msgWarning("跳转页面暂未完成");
        return;
      }
      this.$router.push({
        path: url,
        query: {},
      });
    },
    // 监听缩放比例变化
    zoomChange(val) {
      val < 70 ? (val = this.zoom) : "";
      val > 130 ? (val = this.zoom) : "";
      if (val) {
        this.$refs.divRoller.style.zoom = val + "%";
        this.setMoveTop(val);
      }
    },
  },
  //实例销毁前清除定时器
  beforeDestroy() {
    //修改小车redis状态
    this.destroyedDelete()
    // 每次离开当前界面时，小车清除定时器
    clearInterval(this.timer)
    this.timer = null
    clearInterval(this.timer);
    this.timer = null;
    clearInterval(this.imageTimer);
    this.imageTimer = null;
    window.removeEventListener("click", this.otherClose);
  },
};
</script>

<style lang="scss" scoped>
.searchTable {
  margin: 0px;
  width: 100% !important;
  height: 32px !important;
}
.robotHtmlBox{
  width: 770px;
  position: absolute;
  left: 400px;
  z-index:96659;
  background: #071727;
}
.batchManageButton {
  width: 120px;
  display: flex;
  justify-content: space-around;
  padding: 0 5px;
  color: #e1feff;
  background: linear-gradient(2deg, #00070d, #005aa8) !important;
  border: 1px solid #00c8ff;
  font-size: 12px;
  height: 32px;
  align-items: center;
  margin-right: 10px;
  border-radius: 3px;
  color: white;
  text-align: center;
  > div {
    width: 50px;
    height: 20px;
    border-radius: 13px;
    line-height: 20px;
    cursor: pointer;
  }
  > div:nth-of-type(1) {
    background: #c8c8c8;
  }
  > div:nth-of-type(2) {
    background: #00b0ff;
  }
}
.screenEqNameClass {
  border: solid 2px #09c3fc;
  border-radius: 4px;
}
.screenEqNameBox {
  width: 120px;
  // height: 40px;
  position: absolute;
  top: -40px;
  left: 30px;
  line-height: 1;
  text-align: center;
  padding: 10px;
  padding-bottom: 22px;
  font-size: 10px;
  background-image: url(../../../assets/cloudControl/screenEqName.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  color: #07c3fc;
  z-index:10;
}
.textFalseBox {
  width: 120px;
  height: 40px;
  position: absolute;
  top: -40px;
  left: 30px;
  line-height: 28px;
  text-align: center;
  font-size: 10px;
  background-image: url(../../../assets/cloudControl/screenEqName.png);
  background-repeat: no-repeat;
  background-size: 100% 100%;
  color: #da4a64;
  opacity: 0;
  animation: fadenum 2s;
  z-index:100;
}
@keyframes fadenum {
  0% {
    opacity: 1;
  }
  99% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.siblings {
  position: fixed;
  top: 121px;
  width: 100%;
  height: 61.8%;

  .eqTypeListClass {
    float: left;
    width: 6%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    z-index:8;
  }

  //车道控制
  .indicatorLight {
    width: 30px;
    height: 33%;
    background: linear-gradient(
      90deg,
      rgba($color: #00aced, $alpha: 0.8),
      rgba($color: #0079db, $alpha: 0.8)
    );
    color: white;
    writing-mode: vertical-lr;
    letter-spacing: 5px;
    font-size: 16px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  //照明控制
  .brightnessControl {
    width: 30px;
    height: 33%;
    background: linear-gradient(
      90deg,
      rgba($color: #00aced, $alpha: 0.8),
      rgba($color: #0079db, $alpha: 0.8)
    );
    color: white;
    //垂直向下
    writing-mode: vertical-lr;
    text-align: center;
    //文字间隔
    letter-spacing: 5px;
    font-size: 16px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .el-icon-close:before {
    content: "\e6d9" !important;
  }
}
// 触发控制模块
.triggerControl {
  width: 30px;
  height: 33%;
  background: linear-gradient(
    90deg,
    rgba($color: #00aced, $alpha: 0.8),
    rgba($color: #0079db, $alpha: 0.8)
  );
  color: white;
  //垂直向下
  writing-mode: vertical-lr;
  text-align: center;
  //文字间隔
  letter-spacing: 5px;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

//抽屉的高度
::v-deep .el-drawer.rtl {
  // height:49%;
  // top:239px;
  width: 23% !important;
  font-size: 0.7vw;

  ::-webkit-scrollbar-track-piece {
    background-color: rgba($color: #00c2ff, $alpha: 0.1);
    border-left: 1px solid rgba(0, 0, 0, 0);
    width: 1000px;
    border-width: 50px;
  }

  ::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba($color: #00c2ff, $alpha: 0.6);
    background-clip: padding-box;

    min-height: 28px;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #00c2ff;
  }
}

.drawerTop {
  height: 62%;
  top: 120px;
  right: 38px;
}
.drawerCenter {
  height: 62%;
  top: 120px;
  right: 38px;

  // top: 33%;
}
.drawerBottom {
  height: 62%;
  top: 120px;
  right: 38px;

  // top: 54%;
}
.drawerBox {
  width: 25%;
  height: 25%;
  align-items: center;
  .drawerDirection {
    width: 100%;
    height: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
.drawerCheckbox {
  width: 100%;
  height: 50%;
  display: flex;
  align-items: center;
}
.control {
  width: 40px !important;
  height: 26px !important;
  text-align: center;
  line-height: 22px;
  padding: 0px 0px !important;
  font-size: 12px;
  // background-color: #00c2ff;
  // border-color: #00c2ff;
}

//小三角位置
// ::v-deep .el-icon-caret-left:before{
//   margin-top:-60px;
// }

//多选框选中样式
::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: white;
  width: 14px;
  height: 14px;
  // border: solid 1px #fff;
  // box-shadow: 1px 1px 2px #dcad76;
}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner::after {
  height: 9px;
  width: 6px;
  border: 2px solid #dcad76; // 是改这里的颜色
  border-left: 0;
  border-top: 0;
  top: 0px;
  left: 2px;
}
// ::v-deep .rtl .el-checkbox__input {
//   transform: translateX(28px);
// }
// ::v-deep .rtl .el-checkbox__label {
//   transform: translateX(-25px);
//   width: 26px;
//   height: 26px;
//   background: #2fc83a;
//   line-height: 26px;
//   color: white;
// }
// ::v-deep .rtl {
//   .checkbox {
//     margin-right: 20px !important;
//   }
//   .el-checkbox__input.is-checked + .el-checkbox__label {
//     background: #bd0a0a;
//   }
// }
//title
::v-deep .el-drawer__header {
  // background-color: #00C9FE;
  font-size: 14px;
  margin-bottom: 0px !important;
  height: 36px;
  line-height: 40px;
  // padding: 0 10px;
  color: #fff;
  // border-left: 5px solid #F19E39;
}

//开关

::v-deep .el-drawer__body {
  .bingZhou {
    display: flex;
    // width: 56px;
    // height: 28px;
    height: 50%;
    padding: 20px;

    border: solid 1px red;
    font-size: 12px;
    display: flex;
    > .number {
      width: 28px;
      height: 28px;
      // background-color:#00C7FE !important;
      color: white;
      text-align: center;
      line-height: 28px;
      vertical-align: middle;
    }
    > .checkbox {
      width: 28px;
      height: 28px;
    }
  }

  .ledLighting {
    height: 36px;
    // background-color: #4EAACF;
    line-height: 40px;
    padding-left: 14px;
    font-size: 14px;
    // color: #fff;

    // .el-switch__core:after {
    //   background-color: #0f8ab9;
    // }
  }

  .Time {
    display: flex;
    align-items: flex-start;
    height: 50px;
    padding-top: 10px;

    .setTime {
      white-space: nowrap;
      line-height: 32px;
    }

    .timeStart {
      display: flex;
      margin-right: 10px;
      margin-left: 14px;

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .timeEnd {
      display: flex;

      .el-button--mini {
        padding: 0px 0px !important;
      }

      .el-input--suffix .el-input__inner {
        padding-right: 0px !important;
      }
    }

    .handleLightClass {
      height: 28px;
      margin: 0 5px;
      width: 4vw;
      text-align: center;
      // background-color: #07C2FF !important;
      border: none;
    }
  }
}

::v-deep .el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 100%;
}

// ::v-deep .el-input--prefix .el-input__inner{
//       padding-left: 26px !important;
//       border: solid 1px #1088B9;
//       box-shadow:1px 1px 2px #02C6FA;
// }
// ::v-deep .el-input--suffix .el-input__inner {
//     padding-right: 10px;
//     border: solid 1px #1088B9;
// }
// ::v-deep .el-input--medium .el-input__inner {
//   width: 5.4vw;
// }
// ::v-deep .operationDiglog .el-input--medium .el-input__inner {
//   width: 100% !important;
// }
// ::v-deep .el-button--medium {
//   margin-left: 20px;
// }

// ::v-deep .el-checkbox__inner {
//   width: 26px;
//   height: 26px;
//   border: 1px solid #fff;
//   border-radius: 0px;
// }

.vehicleLane {
  height: 68%;
  align-items: center;
  width: 100%;
  display: flex;
  position: relative;
  margin-top: 2px;
}
.contentTopNav {
  width: 85%;
  left: 0px;
}
// .openSidebar .contentLeftNav{
//   width:88%;left:3.2%
// }
// .hideSidebar .contentLeftNav{
//   width:88%;left:2%
// }

#vehicle,
#energyConsumption,
#focusCar {
  width: 100%;
  height: calc(100% - 2vw);
  margin-top: 5px;
}

.el-input-number {
  width: 5.4vw;
  line-height: 28px;

  // .el-input-number__decrease, .el-input-number__increase {
  //   color: #e1feff;
  //   background-color: transparent !important;
  //   border: unset;
  //   width: 2vw;
  //   font-size: 10px;
  //   :hover {
  //     font-size: 15px;
  //   }
  // }
  // .el-input__inner {
  //   height: 30px;
  //   padding: 0;
  //   color: #e1feff;
  //   background-color: #0b1a46 !important;
  //   border: solid 1px #0b1a46;
  //   // box-shadow: 0px 0px 10px #68B5FF;
  // }
  // }
}

.footer {
  width: 100%;
  height: 25%;
  padding: 0px 0px 0px 16px;
  // margin-top: 10px;
  display: flex;
  padding-bottom: 5px;
  justify-content: space-between;
  margin-top: 8px;
  z-index:8;
  .fourBox {
    display: flex;
    justify-content: space-between;
    width: calc(100% - 30px);
    margin-right: 20px;
  }
  .footChangeButton {
    width: 30px;
    height: 100%;
    ::v-deep .el-radio-group {
      width: 100%;
      height: 100%;
      .el-radio-button {
        width: 100%;
        height: 50%;
        .el-radio-button__inner {
          padding: 0 !important;
          writing-mode: tb-rl;
          white-space: nowrap;
          text-align: center;
          width: 100%;
          height: 100%;
          line-height: 26px;
          letter-spacing: 16px;
          border-radius: 0;
          font-size: 16px;
        }
      }
    }
    .el-radio-button {
      border: solid 0.1px #0067b2 !important;
      border-radius: 0px !important;
    }
  }
  .footTitle {
    padding: 5px 20px;
    // line-height: 25px;
    font-size: 14px;
    display: flex;
    align-items: center;
    font-family: inherit;

    .footTriangle {
      width: 0;
      height: 0;
      border-top: 5px solid transparent;
      border-left: 10px solid #1590ff;
      border-bottom: 5px solid transparent;
    }

    .footTitleCont {
      width: 100%;
      height: 24px;
      margin-top: -4px;
      font-weight: bold;
      display: flex;
      align-items: center;
      font-family: PingFang;

      p:nth-of-type(2) {
        text-transform: uppercase;
        // font-weight: lighter;
        transform: scale(0.8, 0.8);
        opacity: 0.35;
      }
    }
  }

  .footBigBox {
    width: 52%;
    height: 100%;
  }

  .footMiniBox {
    width: 24.5%;
    height: 100%;
    overflow: hidden;
    // background-image: url(../../../assets/cloudControl/footer_bg.png);
    // background-position: center;
    // background-repeat: no-repeat;
    // background-size: 100% 100%;
    // background-color: rgba($color: #0b1329, $alpha: 0.4);
    // border: solid 1px #183b57;
    // color: white;
    .noPicBox{
      width:100%;
      height:200px;
      display:flex;
      justify-content: center;
      img{
        height:200px;
      }
    }
    video {
      height: 186px;
    }
    .listContent {
      height: 70%;
      font-size: 14px;
      overflow: hidden;
      ul {
        margin: 0;
      }
      > li {
        // margin-bottom: 6px;
        list-style: none;
        padding: 10px;
        padding-bottom: 0px;
      }
    }
  }
}

.rightButtonS {
  margin-top: 32px;
  font-size: 16px;
  // color: #0a88bd;
  width: 120px;
  height: 6vh;
  line-height: 6vh;
  padding-left: 20px;
  // background-image: url(../../../assets/cloudControl/rightButton.png);
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
  caret-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: left;

  img {
    width: 20px;
    height: 100%;
    object-fit: contain;
    margin-left: 20px;
    margin-right: 8px;
  }
}

.leftButtonS {
  position: relative;
  left: 0px;
  font-size: 16px;
  // width: 125px;
  height: 46px;
  line-height: 46px;
  font-weight: 500;
  caret-color: rgba(0, 0, 0, 0);
  text-align: center;
  margin-left: 16px;
  border-radius: 2px;
  cursor: pointer;
  img {
    width: 20px;
    height: 100%;
    object-fit: contain;
    margin-left: 8px;
    margin-right: 20px;
  }
}
.topNavRightDeawer {
  right: 0;
}
.openSidebar .leftNavRightDeawer {
  right: 240px;
}
.hideSidebar .leftNavRightDeawer {
  right: 0px;
}
// .bigTypeButton{
//     color: #baddff;
//     background-image: url(../../../assets/cloudControl/right_button.png);
// }
// .bigTypeButton:hover{
//     color: #fff;
//     background-image: url(../../../assets/cloudControl/right_button_hover2.png);
// }

.tunnelBox {
  overflow: hidden;
  margin-left: 100px;
  width: 85%;
  height: 2%;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.tunnelBoxTop {
  background-image: url(../../../assets/cloudControl/top.png);
  margin-top: 10px;
  z-index: 999;
}

.tunnelBoxBottom {
  background-image: url(../../../assets/cloudControl/bottom.png);
}

::v-deep .menu-b .el-select-dropdown {
  background-color: #03192e !important;
  color: white;
  border: solid 1px #e1feff;
  z-index: 100;
}

// ::v-deep .menu-b .el-input--mini .el-input__inner{
//     background-color: #e2f3fa !important;
//     border: solid 1px #0a88bd;
//     color: #0a88bd;
//     // box-shadow: 0px 0px 10px #68B5FF;
//     width: 170px;
// }
::v-deep .menu-b .el-select .el-input .el-select__caret {
  color: #e1feff;
}

::v-deep .menu-b .el-select > .el-input {
  margin-top: 5px;
}
.menu-b .el-button-group > .el-button + .el-button {
  margin-left: 10px;
}

// ::v-deep .menu-b .el-select--small{
//     transform: translateY(-6px);
// }

// 火灾报警弹窗
::v-deep .temperatureDialog .el-dialog {
  margin-top: 15vw !important;
  border: solid 4px #9f0000;
  background-color: rgba($color: #9d0000, $alpha: 0.5);
  border-radius: 4px;

  .el-dialog__body {
    display: flex;
    font-size: 20px;
    text-align: center;
    padding: 0%;

    > div {
      width: 50%;
      color: white;
      margin-bottom: 10%;

      > div:first-of-type {
        color: #c9c9c9;
        line-height: 50px;
      }
    }

    > div:nth-of-type(1) {
      border-right: solid 1px rgba(255, 255, 255, 0.5);
      z-index: 10;
    }

    > div:nth-of-type(2) {
      z-index: 10;
    }

    .HZdialogBg {
      width: 100%;
      height: calc(100% - 54px);
      position: absolute;
      background: url(../../../assets/image/fire_bg.png) no-repeat;
      opacity: 0.3;
      background-size: 85% 200%;
      background-position: 130px -20px;
    }
  }
}

// 隧道温度
.temperature {
  font-size: 16px;
  font-weight: bold;
  position: absolute;

  img {
    width: 25px;
    height: 25px;
  }
}

// 字幕滚动
.seamless-warp2 {
  background-image: url("../../../assets/image/scroll_bg.png");
  // transform: rotate(-90deg);
  // height: 25px;
  // width: 100px;
  overflow: hidden;
  position: absolute;
  top: 32%;
  right: 20%;
  background-size: 100% 100%;

  .item {
    padding-left: 0px;
    width: 400px;

    li {
      list-style: none;
      float: left;
      margin-right: 20px;
      letter-spacing: 2px;
      color: white;
      width: 30px;
      height: 150px;
    }
  }
}

.item {
  margin: 4px;
  background-color: transparent;
}

.left .el-tooltip__popper,
.right .el-tooltip__popper {
  padding: 8px 10px;
  background-color: #0000ff;
}

.app-container {
  padding: 0px !important;
}

.header {
  // color: #ced8e4;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.workbench-header {
  padding-right: 20px;
  height: 45px;
  margin-top: 2px;
  display: flex;
  justify-content: space-between;
}

.flex-row {
  display: flex;
  // flex-direction: row;
  height: 32px;
  align-items: center;
}

.my-back {
  // background-color: #cdedfa;
  // background-image: url("../../../assets/image/bg.png");
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-size: cover;
  -webkit-background-size: cover;
  /* 兼容Webkit内核浏览器如Chrome和Safari */
  -o-background-size: cover;
  /* 兼容Opera */
  zoom: 1;
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  // justify-content: center;
  position: relative;
}

.menu {
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.workbench-menu {
  padding: 5px 20px 10px 10px;
}

/* 隧道选项*/
.menu-button {
  float: left;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  display: flex;
  flex-direction: row;
  // flex-wrap: wrap;
  align-items: center;
}

/*自定义info类型按钮样式*/
// .el-button--info {
//   // color: #C0CCDA;
//   // background-color: #2e354f;
//   // border: 1px solid #4F5C69;
//   color: #0a88bd;
//   background-color: #112c5b !important;
//   border: 1px solid #3b548c;
// }

/* 鼠标在按钮上悬浮*/
// .el-button:hover {
//   background-color: #3a4e86; // #4e5e89
//   border: 1px solid #7f8c98;
// }

.tunnel-checkbox {
  margin: 10px 0 10px 10px;
  color: #c0ccda;
}

/* 显示编号*/
.display-box {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  padding-right: 10px;
  height: 32px;
}

.menu-title {
  width: 110px;
  color: #ced8e4;
  font-size: 15px;
  margin-bottom: 0px;
  margin-left: 30px;
  line-height: 40px;
}

//  .theme-dark .content::-webkit-scrollbar-thumb{
//       animation: movegundong 60s infinite linear;
//       // background-color:red;
//   }
// @keyframes movegundong {
//        0% {
//           transform: translateX(0px);
//       }
//       50% {
//           transform: translateX(200px);
//       }
//       75% {
//           transform: translateX(800px);
//       }
//       100% {
//           transform: translateX(1000px);
//       }
//    }

.system-state-normal {
  color: #00aa00;
  font-size: 15px;
  margin-bottom: 0px;
}

.system-state-abnormal {
  color: #ff0000;
  font-size: 15px;
  margin-bottom: 0px;
}

.menu-button-group {
  width: 90%;
  display: inline-block;

  // transform: translateY(-6px);
  .el-button {
    margin: 5px;
  }
}

/* 图片区域*/
.content {
  clear: both;
  text-align: center;
  width: 90%;
  height: 100%;
  display: flex;
  align-items: center;
  overflow-y: hidden;
  zoom: 100%;
  overflow-x: auto;
  display: inline-block;
  margin: 0 auto;
}

.workbench-content {
  // width: 90%;
  height: 100%;

  position: absolute;
  // top: 7%;
  // -webkit-user-select: none;
  // user-select: none;
  // display: flex;
  // justify-content: center;
  // overflow-x: auto;
  > div {
    width: 100%;
  }
}

// .content::-webkit-scrollbar-track-piece {
//     background-color: rgba(0, 0, 0, 0.1);
//     border-left: 1px solid rgba(0, 0, 0, 0);
// }
// .content::-webkit-scrollbar {
//     width: 5px;
//     height: 13px;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
// }
// .content::-webkit-scrollbar-thumb {
//     background-color: rgba($color: #00c2ff, $alpha: 0.6);
//     background-clip: padding-box;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
//     min-height: 28px;
// }
// .content::-webkit-scrollbar-thumb:hover {
//     background-color: #00c2ff;
//     -webkit-border-radius: 5px;
//     -moz-border-radius: 5px;
//     border-radius: 5px;
// }

.wb-direction-img {
  margin-bottom: 10px;
  padding-right: 20px;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;

  img {
    height: 10px;
    width: 70px;
  }
}

.details-row {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  height: 45px;
  // width: 1442px;
  // position: relative;
  z-index: 3;
  margin-left: 90px;
}

.icon-box {
  position: absolute;
  display: flex;
  flex-direction: column;
  // align-items: center;
  width: 0px !important;
}

// 鼠标经过盒子
.mouseHover {
  &:hover {
    z-index: 10;

    input {
      cursor: pointer;
      background-color: #eee;
      color: #000;
    }
  }
}

/* 被选择后*/
.focus {
  border: 1.3px dashed #1890ff;
  // height: 32px;
  border-radius: 4px;
  margin-bottom: 2px;
}

/* 详情信息*/
.input-box {
  width: 140px;
  height: 45px;
  border-radius: 4px;
  background-color: #192348;
  text-align: center;
  color: rgb(191, 203, 217);
  /*  padding: 10px 10px; */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  display: inline-block;
  position: absolute;
}

/* 风速等输入框*/
.details-input {
  background-color: #192348;
  border-color: #192348;
  color: #6ea2ec;
  width: 45px;
  padding: 0;
  border-radius: 2px;
}

/* 编号等输入框*/
.config-img-input {
  width: 80px;
  // background-color: #545559;
  // background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 0px 0 2px 0;
  border: 0;
  margin: -5px;
  border-radius: 2px;
  // color: #a2a2a3;
}

.s-config-img-input {
  width: 80px;
  // background-color: #545559;
  // background-color: rgba(84, 85, 89, 0.4);
  /* rgba(105, 105, 105, 1)*/
  padding: 0px 0 2px 0;
  border: 0;
  margin: -7px;
  border-radius: 2px;
  // color: #a2a2a3;
}

.el-input--small {
  // margin-top: 6px;
  font-size: 14px;
}

.small-title {
  margin: 0;
  color: #6ea2ec;
}

.param-name {
  width: 140px;
  color: #afafb0;
  font-size: 12px;
  margin-right: 0;
}

/* 车道图片*/
.lane-img {
  margin: 10px 10px 0 10px;
  width: 200px;
  height: 90px;
}

.dialog-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.lane-img-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 10px 0 0 5px;
}

.lane-radio {
  margin-top: 10px;
}

.equipment-img-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
  width: 100px;
  margin: 10px;
}

/* 画布区域*/
.config-img-area {
  // display: flex;
  // justify-content: center;
  // align-items: center;
  height: 580px;
  width: 100%;
  background-size: cover;
  // 工作台加滚动条 居中后左侧超出部分会溢出 加下面三行
  // position: absolute;
  inset: 0px;
  margin: 0px auto;
}

.back-img {
  height: 580px;
  // width: 1630px !important;
  position: absolute;
  display: block;
  // top: -22px;
}

.equipment-img {
  max-height: 150px;
  max-width: 150px;
}

.s-equipment-img {
  height: 20px;
  width: 22px;
}

.wrapper {
  height: 580px;
  width: 100%;
  /* border: 1px solid #000; */
  border-radius: 10px;
  position: relative;
  z-index: 1;
}

input {
  width: 200px;
  padding: 20px 30px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-size: 12px;
  text-align: center;
}

/* 右键菜单 */
.contextmenu {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: absolute;
  list-style-type: none;
  padding: 0 5px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: row;
  align-items: center;

  i {
    margin-right: 5px;
  }

  li {
    margin: 0;
    padding: 5px 5px;
    cursor: pointer;

    &:hover {
      background: #eee;
    }
  }
}

// 自动换行
::v-deep .wrap {
  word-wrap: break-word;
  word-break: normal;
  width: 100%;
  // .el-radio__input.is-checked + .el-radio__label{
  //   color: #c0ccda !important;
  // }
}

.form-direction {
  width: 60px;
  justify-content: space-around;
}

.el-radio__input.is-checked + .el-radio__label {
  color: #c0ccda !important;
  font-weight: bold;
}
::v-deep .siteClassDisabled {
  pointer-events: none;
  margin-left: 16px;
  .el-input__suffix-inner {
    pointer-events: none !important;
  }
}
/* 级联选择器 */
//  ::v-deep  .siteClass {
//   line-height: unset;
// 	/* 2022.03.25 修改
// 	width: 250px;
// 	margin-left: 100px;
// 	*/
// 	margin-left: 1vw;
//     .el-input__suffix{
//       color: #0a88bd !important;
//     }
//     .el-icon-arrow-down:before{
//       content:"\e6df";
//       border: dashed 1px #746a47;
//       color: #0a88bd;
//       font-size: 13px;
//     }
// }

// 机器人图片盒子
.robotBox {
  position: absolute;
  right: 0;
  top: 0;
  background-color: transparent;

  .el-image {
    width: 200px;
    height: 200px;
    vertical-align: middle;

    .el-image__error {
      background-color: lightslategray;
    }
  }

  .switchIcon {
    position: absolute;
    display: none;
    left: 50%;
    top: 35%;
    font-size: 20px;
    cursor: pointer;
    color: #fff;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    transform: translateX(-50%) rotate(90deg);
    z-index: 1;

    &:hover {
      border-color: rgb(104, 181, 255);
      box-shadow: rgb(104 181 255) 0px 0px 10px;
      color: black;
      background-color: powderblue !important;
    }
  }

  &:hover {
    .switchIcon {
      display: inline-block;
    }
  }
}

.robotTabs {
  ::v-deep .el-tabs__header {
    .el-tabs__nav {
      border: none;

      .el-tabs__item {
        border: none;
        // color: #fff;
      }

      .el-tabs__item.is-active {
        color: cornflowerblue;
      }

      .el-tabs__item:hover {
        color: skyblue;
      }

      .el-tabs__active-bar {
        background-color: blueviolet;
      }
    }
  }

  .el-tab-pane {
    div {
      margin: 5px 0;
    }
  }
}

#robotEchart {
  width: 100%;
  height: 15rem;
}
.eventDiglog,
.operationDiglog {
  ::v-deep .el-input-group__append {
    padding: 0;
    width: 60px;
    border-left: none !important;
    .el-button {
      height: 32px;
      border-top-right-radius: 3px !important;
      border-bottom-right-radius: 3px !important;
      border-top-left-radius: 0px !important;
      border-bottom-left-radius: 0px !important;
      // transform: translateX(20px);
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  .el-dialog .el-form {
    // padding: 15px !important;
    .el-form-item__content .el-button {
      width: 88px;
      height: 22px;
      border: none;
    }
    .el-form-item__content .el-button--mini {
      padding: 2px 15px !important;
    }
  }
  .el-dialog__body{
    padding:0 15px !important;
    .el-col{
      padding:0 !important;
    }
  }
  .el-table {
    // padding: 0 15px;
    margin-bottom: 20px;
  }
  // .el-tabs {
  //   padding: 0 15px;
  // }
}
::v-deep .eventDiglog .el-button--medium {
  height: 22px !important;
  line-height: 22px !important;
  padding: 0px !important;
}
.eventDiglog .el-table {
  padding: 15px;
  padding-top: 0;
  background-color: transparent !important;
  margin-bottom: 65px;
}
.el-table .fixed-width .el-button--mini {
  padding-left: 7px;
  padding-right: 10px;
  width: inherit;
  color: white;
}

// ::v-deep .switchStyle .el-switch__core:after{
//   background: #62708d;
// }
// ::v-deep .is-checked.el-switch__core:after{
//   background: white;
// }
// .el-button-group .el-button--info:last-child{
//   border-left-color:#3B548C !important;
// }
.el-button-group > .el-button:last-child {
  border-top-left-radius: 4px !important;
  border-bottom-left-radius: 4px !important;
}

.el-button-group > .el-button:first-child {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

// .el-button-group .el-button--info:first-child{
//   border-right-color:#3B548C !important;
// }
// .el-button-group .el-button--info:not(:first-child):not(:last-child){
//   border-left-color: #3B548C !important;
//   border-right-color: #3B548C !important;
// }
.el-button-group > .el-button:not(:first-child):not(:last-child) {
  border-radius: 4px !important;
}

::v-deep .app-main {
  height: calc(100vh - 52px) !important;
}
::v-deep .EqType31 .radioEqType31 .el-form-item__content {
  margin-left: 0px !important;
}
::v-deep .EqType31 .radioEqType31 .el-radio {
  padding: 0px 20px !important;
  height: 40px;
  line-height: 40px;
}
.reservePlan {
  cursor: pointer;
  width: 190px;
  border-radius: 5px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.reservePlan:hover {
  background: #00bbf5;
  width: 220px;
}
.paginationWorkbench {
  position: static;
  // bottom: 250px !important;
  height: 60px;
}
.labelClass {
  font-size: 0.6vw;
  position: absolute;
  padding-left: 5px;
  text-align: left;
  transform: translate(25px, 8px);
  text-decoration: underline;
  z-index: 4;
  white-space: nowrap;
  font-weight: bold;
}
</style>
<style lang="scss">
.popper-class-site {
  .el-cascader-menu__wrap {
    max-width: 245px;
  }
  .el-cascader-node__postfix {
    right: 18px !important;
  }
}
.tipCase.el-tooltip__popper[x-placement^="left"] .popper__arrow:after {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="left"] .popper__arrow {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="right"] .popper__arrow:after {
  display: none !important;
}

.tipCase.el-tooltip__popper[x-placement^="right"] .popper__arrow {
  display: none !important;
}

.tipCase {
  background: #cdedfa !important;
  border: solid 1px #1d58a9;
  color: #1d58a9 !important;
  transform: translateX(20px);
}

// .popper-class-site {
//   border-color: rgb(104, 181, 255);
//   box-shadow: rgb(104 181 255) 0px 0px 10px;
//   background-color: rgba(2, 19, 35, 1);

//   .el-cascader-menu {
//     color: #0a88bd;

//     .el-cascader-node {
//       &:hover {
//         color: #0a88bd;
//         background-color: rgba(176,224,230,0.8);
//       }
//     }
//     .el-cascader-node.in-active-path, .el-cascader-node.is-selectable.in-checked-path, .el-cascader-node.is-active {
//       box-shadow: rgb(104 181 255) 0px 0px 10px;
//       color: black;
//       background-color: powderblue;
//     }
//   }
// }
.workbench-msg {
  background-color: #1890ff;
  border-color: #1890ff;

  .el-message__content {
    color: #ffffff;
  }
}

/* 文字提示*/
.wb-tip.el-tooltip__popper {
  background-color: #153051;
  color: #fff;
}

/*左右方向*/
.wb-direction-input {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  padding-right: 10px;

  .el-input {
    width: 100px;

    .el-input__inner {
      background-color: transparent;
      border-color: transparent;
      color: #ced8e4;
      font-size: 15px;
      width: 95px;
      text-align: right;
    }
  }
}
.comClass {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
}
.chezhiDrawerDirection {
  width: 100%;
  height: 30px;
  padding-left: 10px;
  line-height: 30px;
}
.phoneBox1 {
  width: 100%;
  height: 40px;
  padding: 0 10px 0 5px;
  display: flex;
  align-items: center;
  .phoneCheckBox {
    margin-left: 10px;
    width: 100px;
  }
  .chezhiName {
    width: 80px;
    margin-left: 5px;
  }
  .chezhiControlButton {
    width: 50px;
    height: 32px;
    padding: 0;
    // border:solid 1px #A3B7CF;
    border-radius: 2px;
    margin-left: 8px;
    text-align: center;
    line-height: 31px;
    cursor: pointer;
    color: white;
  }
  .chezhiControlButton:hover {
    color: white;
  }
}
.chezhiDrawerInfo {
  width: 100%;
  height: 40px;
  padding: 0 10px 0 5px;
  display: flex;
  align-items: center;
  margin: 10px 0;
  .chezhiName {
    width: 40px;
    margin-left: 5px;
  }
  .chezhiLaneSelect {
    width: 160px;
  }
  .chezhiStateSelect {
    width: 100px;
  }
  .chezhiControlButton {
    width: 50px;
    height: 32px;
    padding: 0;
    // border:solid 1px #A3B7CF;
    border-radius: 2px;
    margin-left: 8px;
    text-align: center;
    line-height: 31px;
    cursor: pointer;
    color: white;
  }
  .chezhiControlButton:hover {
    color: white;
  }
}
/*单个设备配置框 */
.workbench-dialog {
  // .el-dialog{
  //     // left: 30%;
  //     background-image: url(../../../assets/cloudControl/dialog.png);
  //     background-repeat: no-repeat;
  //     background-size: 100% 100%;
  // }
  // .el-form-item__label {
  //   color: #c0ccda;
  // }

  .el-dialog__header {
    // background-color: #455d79;
    // color: #fff;
    // height: 30px;
    // padding: 0;
    // padding-left: 20px;
    // height: 30px;
    // line-height: 30px;
    // font-size: 14px;
  }

  .el-dialog__title {
    // color: #fff;
    // font-size: 14px;
    // line-height: 30px;
  }
  .el-dialog__headerbtn {
    // top: 6px !important;
  }
  .el-dialog__body {
    // color: #c0ccda;
    // background-color: #304156;
    padding: 0;
  }

  .el-dialog__footer {
    // color: #c0ccda;
    // background-color: #304156;
    padding-top: 0;
  }

  .el-form-item__content {
    // display: flex;
    // flex-direction: row;
    // align-items: center;
    // margin-left: 0;
  }

  .form-item-img {
    width: 25px;
    height: 25px;
    margin-left: 10px;
    margin-right: 10px;
  }

  .el-radio {
    /* padding: 5px 300px 5px 20px; */
    padding: 5px 150px 5px 20px;
    margin: 2px 0px;
    color: #c0ccda;
    border-radius: 4px;

    &:hover {
      background-color: #455d79;
    }
  }

  .el-radio-selcted {
    // padding: 5px 220px 5px 20px;
    margin: 2px 0px;
    color: #c0ccda;
    border-radius: 4px;
    background-color: #455d79;
  }

  /*select选择框的背景颜色*/
  .el-input__inner {
    background-color: #304156;
    color: #c0ccda;
  }

  /* 按钮*/
  .el-button {
    background-color: #455d79;
  }

  .el-icon-close:before {
    content: "\e79d";
    font-size: 20px;
  }
}
.realTimeTable {
  width: 100%;
  height: calc(100% - 50px);
  li {
    display: flex;
    justify-content: space-around;
    font-size: 12px;
    transform: translateX(-20px);
  }
}
/* 批量管理中的table*/
.batch-table {
  //table为空时
  // .el-table__empty-block {
  //   background-color: #fff;
  // }

  // .el-table .el-table__header-wrapper th {
  //   background-color: #fff !important;
  //   color:#0a88bd !important;
  // }

  // //鼠标停留在table
  // .el-table--enable-row-hover .el-table__body tr:hover > td {
  //   background-color: #455d79;
  // }

  //去掉边框
  .el-table__row > td {
    border: none;
  }

  .el-table th.is-leaf {
    border-bottom: none !important;
  }

  .el-table::before {
    height: 0;
  }

  // .el-table__body-wrapper {
  //   background-color: #304156;
  // }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    // background-color: #304156;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    // background-color: #455d79;
    border-radius: 3px;
  }
}

/* 设备含义中的table*/
.explain-table {
  .el-table .el-table__header-wrapper th {
    background-color: #304156;
  }

  .el-table::before {
    height: 0;
  }

  .el-table--mini td {
    padding: 2px 0;
  }

  .el-table .cell.el-tooltip {
    padding-top: 5px;
  }

  .el-table__body-wrapper {
    background-color: #304156;
  }

  /*table滚动条的宽度 */
  .el-table__body-wrapper::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    // background-color: #304156;
  }

  /* table滚动条的滑块*/
  .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: #455d79;
    border-radius: 3px;
  }
  .el-radio {
    width: 240px;
    height: 40px;
    line-height: 40px;
  }
}

.movableItem {
  height: 100%;
}

.strategyClass {
  .el-table .cell {
    min-height: 38px;
    white-space: nowrap;
    line-height: 38px;
  }

  // .el-table__body {
  //   .el-table__row {
  //     .el-table__cell:nth-child(2) {
  //       text-align: left;
  //       .el-button--text{
  //         color:#fff;
  //       }
  //     }
  //   }
  // }
}

.clickShowDrag {
  width: 40px;
  height: 40px;
  // border: solid 1px red;

  margin-left: 40px;
  margin-top: 100px;
  z-index: 960619;
}

#analogQuantity {
  width: 100%;
  height: 11rem;
}

.el-checkbox {
  color: #c0ccda;
}

.carContentRight,
.carContentLeft {
  overflow: hidden;
  width: 100%;
}

.runingCarRight {
  position: absolute;
  transition: 1.6s;
}

.runingCarLeft {
  position: absolute;
}

// 跑车
.sportCarBox {
  width: 100%;
}

.sportCar {
  height: 80px;
  position: absolute;
  transition: 1.6s;
}

.item-title {
  font-size: 16px;
}

.oldContent,
.newContent {
  .el-input__inner {
    background: #000000;
    border: unset;
    text-align: center;
    height: 80px;
    line-height: 80px;
    font-size: 20px;
  }
}

.fromTitle span {
  font-size: 18px;
  font-weight: bold;
}

.offline,
.online {
  .el-button {
    background-color: unset;
    border: unset;
    padding: 0px;
    color: white;
  }
}

.offline {
  .el-button {
    span {
      i {
        color: red;
      }
    }
  }
}

.online {
  .el-button {
    span {
      i {
        color: #1de31d;
      }
    }
  }
}

.iconList {
  .el-row {
    .el-col-4,
    .el-col-5 {
      .el-button {
        background-color: unset;
        border: unset;
        padding: 0px;
        color: white;
      }
    }

    i {
      color: white;
      font-size: 16px;
    }
  }
}
.icon-dialog{
  .el-dialog__body{
    max-height:70vh;
    overflow-y:auto;
    overflow-x:hidden;
  }
}
.youdaoDialog {
  .el-dialog {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
  }
}

.el-table .odd-row {
  background: #304156;
  color: #fff;
}

.el-table .even-row {
  background: slategrey;
  color: #fff;
}

.tooltip {
  position: absolute;
  top: 35px;
  left: -155px;
  padding: 10px 20px;
  white-space: nowrap;
  border-radius: 4px;
  font-size: 16px;
}

.tooltip::before {
  content: "";
  position: relative;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 10px solid #999999;
  top: -39px;
  left: 45%;
}

.robotAnimation {
  width: 20px;
  height: 20px;
  transform: translateY(-12px);
  animation: mymove 60s infinite linear;
  float: left;
  z-index: 10;
  cursor: pointer;
}

@keyframes mymove {
  0% {
    transform: translateX(0px);
  }

  50% {
    transform: translateX(1290px);
  }

  100% {
    transform: translateX(0px);
  }
}

.el-image__error {
  display: none;
}
.carBox {
}
.carBox div {
  display: block;
  width: 20px;
  height: 20px;
  position: absolute;
}

.accident svg {
  display: block;
  width: 40px;
  height: 40px;
  position: absolute;
}

.phoneBox1 .sliderClass {
  width: 150px;
  .el-slider__runway {
    width: 100%;
    // background-color: #006784;
    margin: 12px 0;
  }
  .el-slider__bar {
    background: linear-gradient(90deg, #00aded 0%, #007cdd 100%);
  }
  .el-slider__button {
    width: 10px;
    height: 10px;
    border: solid 1px #fff;
    background-color: #ff9300;
  }
}
.boardBox1 {
  position: absolute;
  overflow: hidden;
  writing-mode: tb-rl;
  white-space: nowrap;
  text-align: center;
  padding: 2px;
  border: solid 1.5px #f9b554;
  display: flex;
  align-items: center;
  border-radius: 2px;
  background: black;
  box-shadow: 0px 0px 2px #946f3b inset, 0px 0px 4px #946f3b inset;
}
.boardBox1 span {
  display: inline-block;
  /*inline样式不能使用动画*/
  // animation: boardBox1 10s linear infinite; /*滚动动画*/
}
@keyframes boardBox1 {
  from {
    transform: translateY(72px); /*div多宽就写多宽*/
  }

  to {
    transform: translateY(-100%);
  }
}
.boardBox2 {
  position: absolute;
  overflow: hidden;
  writing-mode: tb-rl;
  white-space: nowrap;
  // font-size: 15px;
  // color: #ffff07;
  text-align: center;
  padding: 4px;
  border: solid 1.5px #f9b554;
  display: flex;
  align-items: center;
  border-radius: 2px;
  background: black;
  box-shadow: 0px 0px 2px #946f3b inset, 0px 0px 4px #946f3b inset;
}
.boardBox2 span {
  display: inline-block;
  /*inline样式不能使用动画*/
  // animation: boardBox2 15s linear infinite; /*滚动动画*/
}
@keyframes boardBox2 {
  from {
    transform: translateY(190px); /*div多宽就写多宽*/
  }

  to {
    transform: translateY(-100%);
  }
}
.treeBox {
  position: absolute;
  z-index: 960619;
  top: 5%;
  // left: 54.5%;
  width: 13.5%;
  height: 60vh;
  overflow: scroll;
}
.treeBox::-webkit-scrollbar {
  display: none;
}
</style>

<style lang="scss" scoped>
.lineBT {
  width: 100%;
  margin: 2px 0px auto;
  // border-bottom: solid 1px white;
  // transform: translateY(-30px);
  display: flex;
  > div:nth-of-type(1) {
    width: 3%;
    border-bottom: #2dbaf5 solid 1px;
  }
  > div:nth-of-type(2) {
    width: 94%;
    border-bottom: 1px solid rgba($color: #00b0ff, $alpha: 0.2);
  }
  > div:nth-of-type(3) {
    width: 3%;
    border-bottom: #2dbaf5 solid 1px;
  }
}
.syxt_searchBox {
  position: absolute;
  top: 145px;
  right: 25px;
  width: 39%;
  z-index: 1996;
  background-color: #00335a;
  padding: 20px;
  box-sizing: border-box;
  ::v-deep .el-form-item {
    width: 100%;
    .el-form-item__content {
      width: 78%;
      .el-select {
        width: 100%;
      }
    }
  }
  .bottomBox {
    width: 100%;
    ::v-deep .el-form-item__content {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100% !important;
    }
  }
}
</style>
<!-- <style lang="scss" scoped>
.zj {
  ::v-deep input {
    width: 16.5vw !important;
  }

  ::v-deep .el-input-group__append {
    padding: 0;
  }

  ::v-deep button {
    margin: 0 !important;
    border-radius: 0px !important;
  }
}

</style> -->
