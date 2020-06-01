<template>
  <div id="lines" style="height: 20rem;margin-top: 10px"></div>
</template>

<script>
  const echarts = require('echarts/lib/echarts');
  // 引入柱状图
  require('echarts/lib/chart/line');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/legend');
  require('echarts/lib/component/dataZoom');
  require('echarts/lib/component/markLine');
  export default {
    name: "Lines",
    mounted() {
      this.initCharts(this.option);
    },
    props:{
      reLine:{
        type:Number,
        default:0
      },
      trends:{
        type:Object,
        default: ()=>{}
      }
    },
    watch:{
      reLine(val){
        let data = this.trends.name
        let baidu = this.trends.baidu
        let sougou = this.trends.sougou
        let _360 = this.trends._360
        let xA = [{type: 'category', boundaryGap: false, data: data}]
        let series = [{name:'百度',data:baidu},{name:'搜狗',data:sougou},{name:'360',data:_360}]
        this.initCharts({xAxis:xA,series:series})
      }
    },
    data(){
      return{
        option : {
          title: {
            subtext:'职位搜索趋势'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          legend: {
            data: ['百度', '搜狗', '360']
          },

          grid: {
            left: '2%',
            right: '8%',
            bottom: '5%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              data: []
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '百度',
              type: 'line',
              stack: '总量',
              areaStyle: {},
              data: []
            },
            {
              name: '搜狗',
              type: 'line',
              stack: '总量',
              areaStyle: {},
              data: []
            },
            {
              name: '360',
              type: 'line',
              stack: '总量',
              areaStyle: {},
              data: []
            },
          ]
        }
      }
    },
    methods:{
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('lines'));
        myChart.setOption(option)

      },
    }
  }
</script>

<style scoped>

</style>
