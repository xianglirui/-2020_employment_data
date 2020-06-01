<template>
  <div id="pie" style="height: 15rem;"></div>
</template>

<script>
  const echarts = require('echarts/lib/echarts');
  // 引入柱状图
  require('echarts/lib/chart/pie');
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/legend');
  require('echarts/lib/component/dataZoom');
  require('echarts/lib/component/markLine');
  export default {
    name: "pie",
    props: {
      Salary_scale:{
        type:Array,
        default:()=>[]
      },
      reInitCharts:{
        type:Number,
        default:0
      }
    },
    mounted() {
      this.initCharts(this.option);
    },

    watch:{
      reInitCharts(val){
        let series=[{data:[{name:'5k以下',value:this.Salary_scale[0]},{name:'5-10k',value:this.Salary_scale[1]},{name:'10-15k',value:this.Salary_scale[2]},
            {name:'15-20k',value:this.Salary_scale[3]},{name:'20k以上',value:this.Salary_scale[4]}]}]
        this.initCharts({series})
        if(val<999){
          let myChart = echarts.init(document.getElementById('pie'));
          myChart.hideLoading();
        }
      }
    },
    data(){
      return{
        option : {
          title: {
            subtext: '月薪比例',
            // top:'5%',
            left: 'right'
          },

          tooltip: {
            trigger: 'item',
            formatter: '{b} <br/> {c} ({d}%)'
          },

          legend: {
            orient: 'horizontal',
            left: 'left',
            icon: 'circle',
            data: ['5k以下','5-10k','10-15k','15-20k','20k以上']
          },
          series: [
            {
              // name: '月薪比例',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: [
                {value: 0, name: '5k以下'},
                {value: 0, name: '5-10k'},
                {value: 0, name: '10-15k'},
                {value: 0, name: '15-20k'},
                {value: 0, name: '20k以上'}
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }

    }
    },
    methods:{
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('pie'));
        myChart.setOption(option)
        myChart.showLoading();
      },
    }
  }
</script>

<style scoped>

</style>
