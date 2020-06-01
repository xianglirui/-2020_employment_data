<template>
  <div id="mapss" style="height: 18rem;background-color: #fef8ef"></div>
</template>

<script>
  const echarts = require('echarts/lib/echarts');
  // 引入柱状图
  require('echarts/lib/chart/map');

  import geoJson from '../../utils/compare'
  // 引入提示框和标题组件
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/title');
  require('echarts/lib/component/geo')
  require('echarts/lib/component/visualMap');
  export default {
    name: "mapss",
    mounted() {
      this.initCharts(this.option);
    },
    props:{
      region:{
        type:Array,
        default:()=>[]
      },
      mapMax: {
        type:Number,
        default:100
      }
    },
    watch:{
      mapMax(val){
        let series = [{data:this.region}]
        let visualMap = {max:val}
        let title = {text:this.$store.state.chooseCity.name+"岗位地图"}
        this.initCharts({title,visualMap,series})
      }
    },
    data(){
      return{
        option : {
          title: {
            text: '武汉岗位地图',
            subtext:'单位：/家',
            textStyle:{
              fontSize:14
            },
            top:10,
            left:10
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b}<br/>{c} 家'
          },

          visualMap: {
            min: 0,
            max: 100,
            text: ['高', '低'],
            realtime: true,
            calculable: true,
            itemWidth:15,
            itemHeight:100,
            left:10,
            inRange: {
              color: ['rgba(67,148,255,0.08)','#ffd362','rgb(255,138,39)','#ff133d']
            }
          },
          geo:{
            map: "HK",
            zoom: 1, //当前视角的缩放比例
            roam: true, //是否开启平游或缩放
            scaleLimit: { //滚轮缩放的极限控制
              min: 1,
              max: 2
            },
          },
          series: [
            {
              name: 'map',
              type: 'map',
              mapType: 'HK', // 自定义扩展图表类型
              label: {
                show: false
              },
              geoIndex: 0,
              data: []
            }
          ]
        }
      }
    },
    methods:{
      initCharts(option) {
        let myChart = echarts.init(document.getElementById('mapss'));
        echarts.registerMap('HK', geoJson().json);
        myChart.setOption(option)
      },
    }
  }
</script>

<style scoped>

</style>
