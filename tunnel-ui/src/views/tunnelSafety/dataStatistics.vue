<template>
    <div class="app-container">
        <div style="margin-bottom: 20px;">
            <b style="font-size: 16px;margin-right: 15px;">隧道名称</b>
            <el-button-group class="menu-button-group" v-for="(item, index) in tunnelData" >
                <el-button
                    type="info"
                    size="small"
                    :style="tunnelBtnStyle[index]"
                    @click="setTunnel(item, index)"
                >
                  {{ item.tunnelName }}
                </el-button>
            </el-button-group>
        </div>
      <div class="modularFive">
          <div class="modularBox">
            <div class="modularTitle">在途车流量</div>
            <div class="modularCharts" id="trafficFlowTunnel"></div>
          </div>
          <div class="modularBox">
            <div class="modularTitle">平均速度</div>
            <div class="modularCharts" id="averageVelocity"></div>
          </div>
          <div class="modularBox">
            <div class="modularTitle">今日车型占比</div>
            <div class="modularCharts" id="modelProp"></div>
          </div>
          <div class="modularBox">
            <div class="modularTitle">各隧道车流量占比</div>
            <div class="modularCharts" id="propTrafficFlow"></div>
          </div>
          <div class="modularBox modularBox4">
              <div class="modularTitle">预警总数(个)</div>
              <div style="margin-left: 50px;margin-top: 10px;font-weight: bold;font-size: 30px;
                        text-shadow: 0 0 10px #b9b9b9, 0 10px 10px rgba(0, 0, 0, 0.3);">{{warningNum}}</div>
          </div>
      </div>
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="日期选择">
              <el-radio-group v-model="queryParams.pickerType" style="margin-right: 15px" @change="changeDate()">
                <el-radio-button label="year">年</el-radio-button>
                <el-radio-button label="month">月</el-radio-button>
                <el-radio-button label="day">日</el-radio-button>
              </el-radio-group>
          </el-form-item>
          <el-form-item>
              <el-date-picker v-if="queryParams.pickerType == 'day'" type="date" size="small" v-model="dateTime"
                placeholder="选择日期"  @change="changeDate()" key='day'>
              </el-date-picker>
              <el-date-picker v-if="queryParams.pickerType == 'month'" type="month" size="small" v-model="monthTime"
                placeholder="选择月"  @change="changeDate()" key='month'>
              </el-date-picker>

              <el-date-picker v-if="queryParams.pickerType == 'year'" type="year" size="small" v-model="yearTime"
                placeholder="选择年" @change="changeDate()" key='year'>
              </el-date-picker>
          </el-form-item>
          <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
      </el-form>
      <div id="drivingTrendChart"></div>
      <div id="earlyWarning"></div>
    </div>
</template>

<script>
import { getCarNumberByMonth, getCarNumberByDay, getCarNumberByHour, getTheWarningDataOfToday, getCarFlowNumberOfTodayGroupByTunnel, getWarningDataAnalysis} from "@/api/tunnelSafety/dataStatistics.js";
import { listTunnels } from "@/api/equipment/tunnel/api";
import * as echarts from "echarts";
import moment from "moment";
export default {
  name: "dataStatistics",

  data() {
    return {
        tunnelData:[],
        // 查询参数
        queryParams: {
           dateTimes: new Date(),
           pickerType: "year",
        },
        dateTime:new Date(),
        monthTime: new Date(),
        yearTime: new Date(),
        tunnelBtnStyle: [
          {
             "border": "1px solid rgb(217, 220, 227)",
             "box-shadow": "0px 0px 5px #9fa1a6",
             "color":'#000'
          },
        ],
        // 车型占比 echarts
        modelProp:null,
        // 车流量占比
        propTrafficFlow:null,
        // 交通事件趋势分析
        drivingTrendChart:null,
        // 在途车流量
        trafficFlowTunnel:null,
        // 平均速度
        averageVelocity:null,
        // 预警事件
        earlyWarning:null,
        // 隧道Id
        tunnelId:'',
        // 隧道名称
        tunnelName:'',
        // 预警数量
        warningNum:'',
        // 各隧道车流量占比 假数据
        trafficFlowData:[
            {
                tunnel_name:'迎春坡隧道',
                num:0
            },
            {
                tunnel_name:'毓秀山隧道',
                num:0
            },
            {
                tunnel_name:'红山隧道',
                num:0
            },
            {
                tunnel_name:'徐庄长山隧道',
                num:0
            },
        ]
    }
  },
  created() {
    // this.getList();
    this.getTunnels()

  },
  mounted() {
      this.flowGroupTunnel()
  },
  methods: {
      // 查隧道的接口
      getTunnels() {
        listTunnels().then((response) => {
           this.tunnelData = response.rows.slice(0,4);
           if(!this.tunnelId){
               this.tunnelId = response.rows[0].tunnelId
               this.tunnelName = response.rows[0].tunnelName
           }
           // 获取预警事件
           this.warningData()
           // 获取车流量
           this.getList();
        });
       },
       // 选择隧道
       setTunnel(item, index) {
           console.log(item,"item")
           this.tunnelBtnStyle = []
           this.tunnelBtnStyle[index] = {
              "box-shadow": "0px 0px 5px #9fa1a6",
           };

           this.tunnelId = item.tunnelId
           this.tunnelName = item.tunnelName
           // 获取预警事件
           this.warningData()
           this.todayMeg()
       },
       // 选择时间 年月日
       changeDate(){
               if (this.queryParams.pickerType == 'day') {
               	this.queryParams.dateTimes = this.dateTime;
               } else if (this.queryParams.pickerType == 'month') {
               	this.queryParams.dateTimes = this.monthTime;
               } else if (this.queryParams.pickerType == 'year') {
               	this.queryParams.dateTimes = this.yearTime
               }
       },

      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      getList(){
          this.queryParams.params = {}
          let time =  this.queryParams.dateTimes
          let times = moment(time).format("YYYY-MM-DD")
          this.queryParams.params[this.queryParams.pickerType] = times
          const query = {
              tunnelId : this.tunnelId,
              tunnelName: this.tunnelName,
              params : this.queryParams.params
          }
          // 选择年月日 查车流量、预警事件echarts
          switch (this.queryParams.pickerType) {
          	case "year":
          		getCarNumberByMonth(query).then((res) =>{
                    this.trendChart(res.data,"year")
          		})
                getWarningDataAnalysis(query).then((response) =>{
                    this.earlyWarningCharts(response.data[0].year,"year")
          		})
          		break;
          	case "month":
                getCarNumberByDay(query).then((res) =>{
          		    this.trendChart(res.data,"month")
          		})
                getWarningDataAnalysis(query).then((response) =>{
                    this.earlyWarningCharts(response.data[0].month,"month")
                })
          		break;
            case "day":
            	getCarNumberByHour(query).then((res) =>{
            	    this.trendChart(res.data,"day")
            	})
                getWarningDataAnalysis(query).then((response) =>{
                    console.log(response,"response")
                    this.earlyWarningCharts(response.data[0].date,"day")
                })
            	break;
          }
         this.todayMeg()


      },
       // 根据日期选择 日 返回 今日在途车流量 平均速度 车型占比
      todayMeg(){

          let now = new Date()
          let today = moment(now).format("YYYY-MM-DD")
          var params = {}
          params['day'] = today
          const data = {
              tunnelId : this.tunnelId,
              tunnelName: this.tunnelName,
              params : params
          }
          getCarNumberByHour(data).then((res) =>{
              // 在途车流量
              this.trafficFlowTunnelCharts(res.data)
              // 平均速度
              this.averageVelocityCharts(res.data)
              // 今日车型占比
              this.modelPropCharts(res.data)
          })

      },
      // 预警信息
      warningData(){
          this.warningNum = 0
          const query = {
              tunnelId : this.tunnelId
          }
          getTheWarningDataOfToday(query).then((res) =>{
              if(res.data.length>0){
                  this.warningNum = res.data[0].num
              }
          })
      },
      // 今日各隧道车流量
      flowGroupTunnel(){
          getCarFlowNumberOfTodayGroupByTunnel().then((res) =>{
              this.trafficFlowCharts(res.data)
          })
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams= {
           dateTimes: new Date(),
           pickerType: "year",
        },
        this.handleQuery();
      },
      // 今日车型占比
      modelPropCharts(data){
          console.log(data,"车型data")
          this.modelProp = echarts.init(document.getElementById('modelProp'));
          let color = ['#aaff7f', '#55ff7f', '#55aa7f', '#F8456B', '#00FFFF', '#4AEAB0'];
          let echartData = [];
          let addFlow = 0
          let addBveh = 0
          let addMveh = 0
          let addSveh = 0
          for (var i = 0; i < data.length; i++) {
          	addBveh += parseFloat(data[i].flow_bveh)
            addMveh += parseFloat(data[i].flow_mveh)
            addSveh += parseFloat(data[i].flow_sveh)
          }
          var sveh = {
              name : '小型车',
              value: addSveh
          }
          var mveh = {
              name : '中型车',
              value: addMveh
          }
          var bveh = {
              name : '大型车',
              value: addBveh
          }
          var flowArr = []
          flowArr = flowArr.concat(sveh,mveh,bveh)
          console.log(flowArr,"flowArr")
          for (var i = 0; i < flowArr.length; i++) {
              addFlow += parseFloat(flowArr[i].value)
          }
          console.log(addFlow,"addFlow")
          for (var i = 0; i < flowArr.length; i++) {
              console.log(flowArr[i].value / addFlow )
              if( addFlow != 0 ){
                  var json = {
                      name: flowArr[i].name+'\n'+flowArr[i].value+'辆\n'+(parseFloat(flowArr[i].value / addFlow) *100 ).toFixed(2) + '%',
                      value: flowArr[i].value
                  }
              }else{
                  var json = {
                      name: flowArr[i].name+'\n'+flowArr[i].value+'辆\n'+'0%',
                      value: flowArr[i].value
                  }
              }
              echartData.push(json)
          }

          var option = {
            tooltip: {
                	trigger: 'item',
                    formatter: function(params) {
                        var str = ''
                        	str += params.name + '<br>'
                        return str;
                    }
            },
          	color: color,
          	series: [{
          		type: 'pie',
          		radius: ['30%', '65%'],
          		center: ['50%', '50%'],
          		data: echartData,
          		hoverAnimation: false,
          		itemStyle: {
          			normal: {
          				borderWidth: 4,
                        borderColor:"#E5EEF7",
          			}
          		},
          		labelLine: {
          			normal: {
          				length: 5,
          				//length2: 100,
          				lineStyle: {
          					color: '#00458f'
          				}
          			}
          		},
          		label: {
          			normal: {
          				formatter: params => {
          					return (
          						'{name|' + params.name + '}'
          					);
          				},
          				// padding: [0 , -100, 25, -100],
          				padding: [2.5, -2.5, 2.5, -2.5],
          				rich: {
          					icon: {
          						fontSize: 16
          					},
          					name: {
          						fontSize: 14,
          						// padding: [30, 10, 30, 4],
          						color: '#00458f'
          					},
          					value: {
          						fontSize: 18,
          						fontWeight: 'bold',
          						color: '#00458f'
          					}
          				}
          			}
          		},
          	}]
          };
          this.modelProp.setOption(option)
      },
      // 各隧道车流量占比
      trafficFlowCharts(data){
          console.log(data,"data")
          this.propTrafficFlow = echarts.init(document.getElementById('propTrafficFlow'));
          let color = ['#aa00ff', '#ff007f', '#ff00ff', '#F8456B', '#00FFFF', '#4AEAB0'];
          let echartData = [];
          let addFlow = 0
          if(data.length==0){
              data = this.trafficFlowData
              for (var i = 0; i < data.length; i++) {
              	addFlow += parseFloat(data[i].num)
              }
              for (var i = 0; i < data.length; i++) {
              	var json = {
              		name: data[i].tunnel_name+'\n'+data[i].num+'辆\n'+ '0%',
              		value: data[i].num
              	}
              	echartData.push(json)
              }
          }else{
              for (var i = 0; i < data.length; i++) {
              	addFlow += parseFloat(data[i].num)
              }
              for (var i = 0; i < data.length; i++) {
              	var json = {
              		name: data[i].tunnel_name+'\n'+data[i].num+'辆\n'+ (parseFloat(data[i].value / addFlow) *100 ).toFixed(2) + '%',
              		value: data[i].num
              	}
              	echartData.push(json)
              }
          }





          var option = {
            tooltip: {
              	trigger: 'item',
                formatter: function(params) {
                    var str = ''
                    	str += params.name + '<br>'
                    return str;
                }
            },
          	color: color,
            // legend: {
            // 	data: ['小型车', '中型车', '车流量'],
            // 	textStyle: { //图例文字的样式
            // 		color: '#000',
            // 		fontSize: 14
            // 	},
            // 	icon: 'circle',
            // },
          	series: [{
          		type: 'pie',
          		radius: ['30%', '65%'],
          		center: ['50%', '50%'],
          		data: echartData,
          		hoverAnimation: false,
          		itemStyle: {
          			normal: {
          				borderWidth: 4,
                        borderColor:"#E5EEF7",
          			}
          		},
          		labelLine: {
          			normal: {
          				length: 5,
          				//length2: 100,
          				lineStyle: {
          					color: '#00458f'
          				}
          			}
          		},
          		label: {
          			normal: {
          				formatter: params => {
          					return (
          						'{name|' + params.name + '}'
          					);
          				},
          				// padding: [0 , -100, 25, -100],
          				padding: [2.5, -2.5, 2.5, -2.5],
          				rich: {
          					icon: {
          						fontSize: 16
          					},
          					name: {
          						fontSize: 14,
          						// padding: [30, 10, 30, 4],
          						color: '#00458f'
          					},
          					value: {
          						fontSize: 18,
          						fontWeight: 'bold',
          						color: '#00458f'
          					}
          				}
          			}
          		},
          	}]
          };
          this.propTrafficFlow.setOption(option)
      },
      // 车流量
      trendChart(data,type) {
      	this.drivingTrendChart = echarts.init(document.getElementById('drivingTrendChart'))
        var xData = []
        var flow_sveh = []
        var flow_mveh = []
        var flow_bveh = []
        var avg_speed = []
        for(var item of data){
            flow_sveh.push(item.flow_sveh)
            flow_mveh.push(item.flow_mveh)
            flow_bveh.push(item.flow_bveh)
            avg_speed.push(item.avg_speed)
            if(type == 'year'){
                xData.push(item.date)
            }else if(type == 'month'){
                xData.push(item.curr_date)
            }else if(type == 'day'){
                xData.push(item.order_hour+':00')
            }
        }
      	var allData = {
      		"company": xData,
      		"data": {
      			"onDuty": flow_sveh,
      			"home": flow_mveh,
      			"rate": avg_speed,
      			"adsa": avg_speed
      		}
      	}

      	var that = this
      	// echarts配置项

      	var option = {
            title: {
            	text: '车流量',
            	textStyle: {
            		color: '#000',
            		fontSize: 16,
                  fontWeight:'bold',
                  // padding:10
            	}
            },
      		tooltip: {
      			trigger: 'axis',

      		},
      		legend: {
      			show: true,
                icon: "circle",
      			x: 'center',
      			data: ['小型车', '中型车', '大型车', '平均速度'],
      			textStyle:{//图例文字的样式
      			            color:'#000',
      			            fontSize:14
      			        }
      		},
      		calculable: true,
      		grid: {
      			top: '25%',
      			bottom: '10%',
      			left: '10%',
      			right: '10%',
      		},
      		xAxis: [{
      			type: 'category',
      			axisTick: {
      				show: false
      			},
      			splitLine: {
      				show: false
      			},

      			axisLabel: {
      				textStyle: {
      					color: '#00458f',
      					fontSize: 14
      				}
      			},
      			axisLine: {
      				show: true,
      				lineStyle: {
      					color: '#00458f'
      				}
      			},
      			data: allData.company
      		}],
      		yAxis: [{
      			name:'辆',
      			// nameTextStyle: {
      			// 	padding: [0, 0, 0, -40],
      			// 	color: "#000",
      			// },
      			type: 'value',
      			minInterval:1,
      			// max: 3000,
      			axisTick: {
      				show: false
      			},
      			splitLine: {
      				show: false
      			},

      			axisLine: {
      			       show: true,
      			       lineStyle: {
      			        color: '#0E7CE2'
      			       }
      			      },
      			axisLabel: {
      				textStyle: {
      					color: '#0E7CE2',
      					fontSize: 14
      				}
      			},
      		}, {
      			name:'km/h',
      			// nameTextStyle: {
      			// 	padding: [0, 0, 0, 30],
      			// 	color: "#000",
      			// },
      			type: 'value',
      			minInterval:1,
      			axisTick: {
      				show: false
      			},
      			splitLine: {
      				show: false
      			},
      			axisLine: {
      			       show: true,
      			       lineStyle: {
      			        color: '#FDA975'
      			       }
      			      },
      			axisLabel: {
      				textStyle: {
      					color: '#FDA975',
      					fontSize: 14
      				}
      			},
      		}],
      		series: [{
      				name: '小型车',
      				stack: '车流量',
      				yAxisIndex: 0,
      				type: 'bar',
      				itemStyle: {
      					normal: {
      						color: "#0E7CE2",
      						barBorderRadius: 0,
      						label: {
      							show: false,
      							textStyle: {
      								color: "#fff"
      							},
      							formatter: function(p) {
      								return p.value > 0 ? (p.value) : '';
      							}
      						}
      					}
      				},
      				barWidth: 30,
      				label: {
      					normal: {
      						show: false,
      						position: 'inside',
      						formatter: '{c}'
      					}
      				},
      				data: allData.data.onDuty
      			}, {
      				name: '中型车',
      				stack: '车流量',
      				yAxisIndex: 0,
      				type: 'bar',
      				itemStyle: {
      					normal: {
      						color: "#7d79e2",
      						label: {
      							show: false,
      							textStyle: {
      								"color": "#fff"
      							},
      							formatter: function(p) {
      								return p.value > 0 ? (p.value) : '';
      							}
      						}
      					}
      				},
      				barWidth: 30,
      				label: {
      					normal: {
      						show: false,
      						position: 'inside',
      						formatter: '{c}'
      					}
      				},
      				data: allData.data.home
      			}, {
      				name: '大型车',
      				stack: '车流量',
      				yAxisIndex: 0,
      				type: 'bar',
      				itemStyle: {
      					normal: {
      						color: "#00aaff",
      						label: {
      							show: false,
      							textStyle: {
      								"color": "#fff"
      							},
      							formatter: function(p) {
      								return p.value > 0 ? (p.value) : '';
      							}
      						}
      					}
      				},
      				barWidth: 30,
      				label: {
      					normal: {
      						show: false,
      						position: 'inside',
      						formatter: '{c}'
      					}
      				},
      				data: allData.data.adsa
      			},
      			{
      				name: '平均速度',
      				yAxisIndex: 1,
      				type: 'line',
      				symbolSize: 10,
      				symbol: 'circle',
      				itemStyle: {
      					normal: {
      						color: '#FDA975',
      						barBorderRadius: 0,
      						label: {
      							show: false,
      							position: "top",
      							formatter: function(p) {
      								return p.value > 0 ? (p.value + 'km/h') : 0 + 'km/h';
      							}
      						}
      					}
      				},
      				data: allData.data.rate
      			}
      		]
      	}
      	this.drivingTrendChart.setOption(option)
      },
      // 预警事件
      earlyWarningCharts(data,type) {
            this.earlyWarning = echarts.init(document.getElementById('earlyWarning'))
            var xData = []
            var yData = []
            for(var item of data){
                if(type == 'year'){
                    xData.push(item.date)
                    yData.push(item.num)
                }else if(type == 'month'){
                    xData.push(item.curr_date)
                    yData.push(item.sum_flow_num)
                }else if(type == 'day'){
                    xData.push(item.order_hour+':00')
                    yData.push(item.num)
                }

            }
            var option = {
                 color: ['#0E7CE2', '#85BDE8', '#61A0A8', '#D48265', '#91C7AE','#749F83'],
                title: {
                    text: '预警事件',
                    textStyle: {
                        color: '#000',
                        fontSize: 16
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    // formatter: function(params) {
                    // 	var str = '时间：' + params[0].name + '<br>'
                    // 	for (let item of params) {
                    // 		str += item.seriesName + ' : ' + item.value + '<br>'
                    // 	}
                    // 	return str;
                    // }
                },
                legend: {
                    data: ['事故', '预警', '车流量'],
                    textStyle: { //图例文字的样式
                        color: '#000',
                        fontSize: 14
                    },
                    icon: 'circle',
                },
                grid: {
                    top: '25%',
                    bottom: '10%',
                    left: '10%',
                    right: '10%',
                },
                toolbox: {
                    /*  feature: {
                          saveAsImage: {}
                      } */
                },

                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#00458f'
                        },
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#00458f',
                            fontSize: 14
                        }
                    },
                    data: xData
                },
                yAxis: {
                    name:'件',
                    type: 'value',
                    axisLine: {
                        show: true,
                        lineStyle: {
                            color: '#00458f'
                        },
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#00458f',
                            fontSize: 14
                        }
                    },
                },
                series: [

                    {
                        name: '预警',
                        type: 'line',
                        data: yData
                    },


                ]
            }
            this.earlyWarning.setOption(option)
        },
      // 在途车流量
      trafficFlowTunnelCharts(data){
          this.trafficFlowTunnel = echarts.init(document.getElementById('trafficFlowTunnel'));
          var xData = []
          var yData = []
          var total_flow = 0
          for(var item of data){
              xData.push(item.order_hour+':00')
              yData.push(item.total_flow)
              total_flow += item.total_flow
          }
              var option = {
                  tooltip:{
                     trigger: 'axis',
                     formatter: function(params) {
                       console.log(params)
                         var str = ''
                         	str += params[0].marker
                          str += params[0].name +'   '
                          str += params[0].seriesName +':  '+ params[0].value
                         return str;
                     }
                  },
                  title: {
                  	text: total_flow+' 辆',
                  	textStyle: {
                  		color: '#000',
                  		fontSize: 16,
                        fontWeight:'bold',
                        // padding:10
                  	}
                  },
                  grid: {
                  	left: '5%',
                  	right: '8%',
                  	bottom: '5%',
                    top:'20%',
                  	containLabel: true
                  },
                xAxis: {
                  type: 'category',
                  axisLabel: {
                  	textStyle: {
                  		color: '#00458f',
                  		fontSize: 12
                  	}
                  },
                  axisLine: {
                  	show: true,
                  	lineStyle: {
                  		color: '#00458f'
                  	},
                  },
                  data: xData,

                },
                yAxis: {
                  type: 'value',
                  axisLine: {
                  	show: true,
                  	lineStyle: {
                  		color: '#00458f'
                  	},
                  },
                  axisLabel: {
                  	textStyle: {
                  		color: '#00458f',
                  		fontSize: 12
                  	}
                  },
                },
                series: [
                  {
                    data: yData,
                    name:'在途车流量',
                    type: 'line',
                    smooth: true,

                  }
                ]
              };
          this.trafficFlowTunnel.setOption(option)
      },
      // 平均速度
      averageVelocityCharts(data){

          this.averageVelocity = echarts.init(document.getElementById('averageVelocity'));
          var xData = []
          var yData = []
          var avg_speed = 0
          for(var item of data){
              xData.push(item.order_hour+':00')
              yData.push(item.avg_speed)
              avg_speed = parseFloat(avg_speed/24) *100
          }
              var option = {
                  color:['#aa00ff'],
                  title: {
                  	text: avg_speed + ' km/h',
                  	textStyle: {
                  		color: '#000',
                  		fontSize: 16,
                        fontWeight:'bold',
                        // padding:10
                  	}
                  },
                  tooltip:{
                     trigger: 'axis',
                     formatter: function(params) {
                       console.log(params)
                         var str = ''
                         	str += params[0].marker
                          str += params[0].name +'   '
                          str += params[0].seriesName +':  '+ params[0].value
                         return str;
                     }
                  },
                  grid: {
                  	left: '5%',
                  	right: '8%',
                  	bottom: '5%',
                    top:'20%',
                  	containLabel: true
                  },
                xAxis: {
                  type: 'category',
                  axisLabel: {
                  	textStyle: {
                  		color: '#00458f',
                  		fontSize: 12
                  	}
                  },
                  axisLine: {
                  	show: true,
                  	lineStyle: {
                  		color: '#00458f'
                  	},
                  },
                  data: xData,

                },
                yAxis: {
                  type: 'value',
                  axisLine: {
                  	show: true,
                  	lineStyle: {
                  		color: '#00458f'
                  	},
                  },
                  axisLabel: {
                  	textStyle: {
                  		color: '#00458f',
                  		fontSize: 12
                  	}
                  },
                },
                series: [
                  {
                    data: yData,
                    name:'平均速度',
                    type: 'line',
                    smooth: true
                  }
                ]
              };
          this.averageVelocity.setOption(option)
      }
  }
}
</script>

<style scoped lang="scss">
    #drivingTrendChart,#earlyWarning{
        width: 100%;
        height: 300px;
        margin-top: 20px;
        >div{
            height: 300px;
        }
    }
    .modularFive{
        width: 100%;
        height: 200px;
        display: flex;
        margin-bottom: 20px;
        .modularBox{
            width: 20%;
            height: 100%;
            border: solid 1px #0056B3;
            background-color: rgba(0,86,179,0.1);
            margin-right: 2px;
        }
        .modularBox4{
            margin-right: 0px !important;
        }
        .modularCharts{
            width: 100%;
            height: calc(100% - 30px);
        }
        .modularTitle{
            padding-left: 20px;
            padding-top: 4px;
            font-size: 18px;
        }
        .modularNum{
            font-weight: bold;
            font-size: 35px;
            text-shadow: 0 0 10px #b9b9b9, 0 10px 10px rgba(0, 0, 0, 0.3);
            margin-top: 20px;
        }

    }
    .menu-button-group{
        .el-button--info{
            background: transparent;
            color: black;
            margin-right: 4px;
            border: solid 1px rgb(217, 220, 227);
        }
    }
    ::v-deep .el-input__suffix{
      display: none !important;
    }
</style>
