<template>
  <div id="echart" style="height: 22rem;width: 100%"></div>
</template>

<script>
  const echarts = require('echarts/lib/echarts');
  // 引入柱状图
  require('echarts/lib/chart/bar');
  require('echarts/lib/chart/line');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/legend');
  require('echarts/lib/component/dataZoom');
  require('echarts/lib/component/markLine');
  export default {
    name: "Total",
    props:{
      reInitCharts:{
        type:Number,
        default:0
      },
      Salary_scale:{
        type:Array,
        default: ()=>[]
      }
    },
    mounted() {
      this.initCharts(this.option);
    },
    watch:{
      reInitCharts(val){
        let series = [{name:'岗位总数',data:this.Salary_scale[0].nums},{name:'学历要求',data:this.Salary_scale[0].xueli},
          {name:'公司水平',data:this.Salary_scale[0].guimo},{name:'融资规模',data:this.Salary_scale[0].rongzi},
          {name:'工作经验',data:this.Salary_scale[0].jingyan}]
        this.initCharts({series})
        if(val<999){
          let myChart = echarts.init(document.getElementById('echart'));
          myChart.hideLoading();
        }
      }
    },

    data() {
      return {
        legends:['岗位总数' ,'工作经验', '公司水平','融资规模', '学历要求'],
        option : {
          color: ['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'],
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            data: this.legends
          },
          grid: {
            top:'25%',
            left: '2%',
            right: '4%',
            bottom: '12%',
            containLabel: true
          },
          dataZoom: [
            {   // 这个dataZoom组件，默认控制x轴。
              type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
              start: 0,      // 左边在 10% 的位置。
              end: 100,         // 右边在 60% 的位置。
              height: 15,

            },
            {   // 这个dataZoom组件，也控制x轴。
              type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
              start: 0,      // 左边在 10% 的位置。
              end: 100         // 右边在 60% 的位置。
            }
          ],
          xAxis: [
            {
              type: 'category',
              data: ['5k以下','5-10k','10-15k','15-20k','20k以上']
            }
          ],
          yAxis: [
            {
              type: 'value',
              name:'岗位数'
            }
          ],
          series: [
            {name: '岗位总数', type: 'line', data: [0,0,0,0,0]},
            {name: '学历要求', type: 'bar', data: [0,0,0,0,0]},
            {name: '公司水平', type: 'bar', data: [0,0,0,0,0]},
            {name: '融资规模', type: 'bar', data: [0,0,0,0,0]},
            {name: '工作经验', type: 'bar', data: [0,0,0,0,0]},
          ]
        }
      }
    },
    methods:{
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('echart'));
        myChart.showLoading();
        myChart.setOption(option)
      },
    }
  }
</script>

<style scoped>

</style>
