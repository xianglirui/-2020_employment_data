<template>
  <div id="pie1" style="height: 23rem"></div>
</template>

<script>
  import {returnHotTech} from "../../network/request";

  const echarts = require('echarts');
  // 引入柱状图
  require('echarts/lib/chart/pie');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/legend');
  export default {
    name: "pie1",
    props:{
      input:{
        type:String,
        default:''
      }
    },
    created() {
      returnHotTech(this.input).then(res=>{
        if(res.data!=='err'){
          this.option.legend.data = res.data.hot_name;
          let name_data = res.data.hot_num
          let data = [];
          for(let i =0;i<10;i++){
            let info = {};
            info.value = name_data[i];
            info.name = res.data.hot_name[i];
            data.push(info)
          }
          this.option.series[0].data =data
        }
      })
    },
    watch:{
      option: {
        handler(option) {
          this.initCharts(this.option)
          let myChart = echarts.init(document.getElementById('pie1'));
          myChart.hideLoading()
        },
        deep: true
      },
    },
    mounted() {
      this.initCharts(this.option);
      let myChart = echarts.init(document.getElementById('pie1'));
      myChart.hideLoading()
    },
    data(){
      return{
        option: {
          backgroundColor: '#2c3e50',
          tooltip: {
            trigger: 'item',
            formatter: '{b}：<br/> {c} ({d}%)'
          },
          legend: {
            orient: 'horizontal',
            left: 10,
            icon: 'circle',
            textStyle:{
              color:'#fff'
            },
            data: []
          },

          series: [
            {
              name: '访问来源',
              color: ['#0E7CE2', '#FF8352', '#E271DE', '#F8456B', '#00FFFF',
                '#4AEAB0','#730ffb','#0cff21','#ffd362',
                '#ff09cb','#ff6a8f'],
              type: 'pie',
              top:50,
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '23',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: []
            }
          ]
        }
      }
    },
    methods:{
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('pie1'));
        myChart.setOption(option)
        myChart.showLoading();
        window.onresize = myChart.resize;
      },
    }
  }
</script>

<style scoped>

</style>
