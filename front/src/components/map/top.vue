<template>
  <div id="top" style="height: 23rem;background-color: #2c3e50">
  </div>
</template>

<script>
  import {returnHotTech} from "../../network/request";

  const echarts = require('echarts/lib/echarts');
  // 引入柱状图
  require('echarts/lib/chart/bar');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/legend');
  require('echarts/lib/component/dataZoom');
  require('echarts/lib/component/markLine');
  export default {
    name: "top",
    props:{
     input:{
       type:String,
       default:''
     }
    },
    mounted() {
      this.initCharts(this.option);
    },
    created() {
      returnHotTech(this.input).then(res=>{
        if(res.data!=='err'){
          let name_data = res.data.hot_num
          let data = [];
          for(let i =9;i>=0;i--){
            let info = {};
            info.value = name_data[i];
            info.goods_name = res.data.hot_name[i];
            data.push(info)
          }
          this.option.dataset.source = data
        }
      })
    },
    watch:{
      option: {
        handler(option) {
          this.initCharts(this.option)
        },
        deep: true
      },
    },
    data(){
      return{
        option :{
          title: {
            subtext: '技术排名'
          },
          tooltip: {
            trigger: 'axis',
            // formatter: '{a}<br/>{b}：<br/> {c} ({d}%)',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: []
          },
          grid: {
            left: '-2%',
            right: '12%',
            bottom: '3%',
            containLabel: true
          },
          dataset: {
            dimensions: ["goods_name", "value"],
            source: []
          },
          xAxis: [
            {
              splitLine: {
                show: false
              },
              type: "value",
              axisLine: {
                show: false,
                lineStyle: {
                  color: "rgba(255, 255, 255, 0.3)",
                  width: 1
                }
              },
              axisTick: {
                show: false
              },
              axisLabel: {
                show: true,
                margin: 50,
                textStyle: {
                  align: "left",
                  color: "#00FFFF", //更改坐标轴文字颜色
                  fontSize: 10 //更改坐标轴文字大小
                }
              }
            }
          ],
          yAxis: [
            {
              type: "category",
              // data: ["泽兰", "人参", "枸杞", "3.紫苏", "2.香叶", "1.车前草"],
              //boundaryGap: ["1%", "1%"],  类目起始和结束两端空白策略
              inverse: true,
              axisLine: {
                lineStyle: {
                  color: "rgba(255, 255, 255, 0.2)",
                  width: 1
                }
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: function(value, index) {
                    return index <= 2 ? "#FDAD00" : "#fff";
                  },
                  //更改坐标轴文字颜色
                  fontSize: 12 //更改坐标轴文字大小
                },
                formatter: function(value, index) {
                  if (index === 0) {
                    return "{first|}" + "  1 " + value;
                  } else if (index === 1) {
                    return "{second|}" + "  2 " + value;
                  } else if (index === 2) {
                    return "{third|}" + "  3 " + value;
                  } else {
                    return "       " + (index + 1) + " " + value;
                  }
                },
                rich: {
                  value: {
                    color: "#fff",
                    fontSize: 12
                  },
                  first: {
                    color: "FDAD00",
                    backgroundColor: {

                    }
                  },
                  second: {
                    color: "FDAD00",
                    backgroundColor: {

                    }
                  },
                  third: {
                    color: "FDAD00",
                    backgroundColor: {

                    }
                  }
                }
              }
            }
          ],
          series: [
            {
              // name:'2011年',
              type: "bar",
              // data: [200, 210, 220, 230, 240, 250],
              itemStyle: {
                normal: {
                  //每根柱子颜色设置
                  color: function(params) {
                    var _this = this;
                    let colorList = [
                      ["#E56E6E", "#EFA4A4"],
                      ["#FEB763", "#F9CF9E"],
                      ["#00C0DD", "#00C0DD"],
                      ["#23C83E", "#9BF194"],
                      ["#1AA291", "#1AA291"],
                      ["#871fff", "rgba(135,31,255,0.4)"],
                      [ "#07ff8e","#8bf6bc"],
                      ["#4186EC", "#3AB3FB"],
                      ["#ece062", "#fbebcc"],
                      ["#ec0e74", "#fba3c5"],
                    ];
                    var index = params.dataIndex;
                    return {
                      colorStops: [
                        {
                          offset: 0.8, //颜色的开始位置
                          color: colorList[index][0] // 0% 处的颜色
                        },
                        {
                          offset: 0, //颜色的结束位置
                          color: colorList[index][1] // 100% 处的颜色
                        }
                      ]
                    };
                  }
                }
              }, //柱状图上显示数据
              label: {
                show: "true",
                position: [200, "20%"],
                color: "#FFF",
                fontSize: "12"
              }
            }

          ]
        }

    }
    },
    methods: {
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('top'));
        myChart.setOption(option)
      },
    }
  }
</script>

<style scoped>

</style>
