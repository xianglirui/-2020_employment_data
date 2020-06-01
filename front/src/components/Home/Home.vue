<template>
  <div>
    <top class="top">
      <div slot="home-top">
        <van-row>
          <van-col span="4" style="font-size: 12px;color: white;position: relative;top: 15px" offset="1">
            <icon-svg iconClass="icon-zuobiao1" style="font-size: 16px"/>&nbsp;{{$store.state.chooseCity.name}}</van-col>
          <van-col span="16">
            <van-search v-model="input" background="#2c3e50" shape="round" placeholder="请输入职业如：java开发" style="height: 50px" @search="onsearch"/>
          </van-col>
          <van-col span="2">
            <van-icon name="apps-o" size="20" color="white" style="position: relative;top: 15px" @click="show=!show"/>
            <van-popup v-model="show" position="bottom" :style="{ height: '30%' }" :overlay-style="{marginTop:'50px'}">
              <van-tabs v-model="active1">
                <van-tab title="切换城市">
                  <van-row style="width: 90%;margin: 10px auto">
                    <van-col span="6" v-for="(item,index) in citys" :key="index" style="margin-top: 10px">
                      <van-button :type="buttonActiveIndex.city===item?'info':'default'" size="small" @click="choose_button(item,index)">{{item}}</van-button>
                    </van-col>
                    <span class="hope">其他城市正在开通中，敬请期待~</span>
                  </van-row>
                </van-tab>
                <van-tab title="数据源">
                  <van-row style="width: 90%;margin: 10px auto">
                    <van-col span="6" v-for="(item,index) in website" :key="index" style="margin-top: 10px">
                      <van-button :type="buttonActiveIndex.dataSource===index?'info':'default'" size="small">{{item}}</van-button>
                    </van-col>
                  </van-row>
                </van-tab>
                <van-tab title="指南">
                  <van-row style="width: 90%;margin: 20px auto">
                    <van-col span="8"><van-button type="default" size="small" @click="$router.push('/docs')">文档</van-button></van-col>
                    <van-col span="8"><van-button type="default" size="small" @click="gitee">源码</van-button></van-col>
                    <van-col span="8"><van-button type="default" size="small">app下载</van-button></van-col>
                    <van-col span="24" style="margin-top: 25px"><van-button type="info" size="small" style="width: 70%" @click="logout">退出</van-button></van-col>
                  </van-row>
                </van-tab>
              </van-tabs>
            </van-popup>
          </van-col>
        </van-row>
      </div>
    </top>

    <van-tabs v-model="active" scrollspy sticky offset-top="50" style="position: relative;top: -10px" background="#f2f2f2">

      <van-tab title="职位地图">
        <el-card :body-style="{ padding: '0px', }" style="margin-bottom: 10px;">
          <maps :region="region" :map-max="mapMax"/>
        </el-card>
      </van-tab>

      <van-tab title="条件筛选">
        <el-card :body-style="{ padding: '0px', }" style="margin-bottom: 10px;">
          <div style="border: 1px solid #fafafa;background-color: #eee;width: 100%;height: 200px;font-size: 12px;">
            <van-row style="padding: 10px;" v-for="(item,index) in display" :key="index">
              <van-col span="5"><span style="font-weight: bold;font-size: 14px">{{item.name}}：</span></van-col>
              <van-col span="19">
                <van-row type="flex" justify="space-between">
                  <van-col v-for="(info,count) in item.info" :key="count" :style="flag[index].index===count?choiceStyle:{}" style="padding: 2px">
                    <span id="kls" @click="choice(index,count)">{{info}}</span></van-col>
                </van-row>
              </van-col>
            </van-row>
            <van-row style="width: 70%;margin: 5px auto"><van-button size="mini" style="width: 100%" type="warning" @click="clickOk">确定</van-button></van-row>
          </div>
          <total :reInitCharts="reInitCharts" :Salary_scale="Salary_scale"/>
          <pie :Salary_scale="Salary_scale[0].nums" :reInitCharts="reInitCharts"/>
        </el-card>
      </van-tab>

      <van-tab title="搜索热度">
        <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 10px;"><lines :trends="trends" :reLine="reLine"/></el-card>
      </van-tab>

      <van-tab title="技术要求">
        <el-card :body-style="{ padding: '0px'}">
          <van-radio-group v-model="radio" direction="horizontal" style="margin-top: 10px;margin-left: 10px">
            <van-radio name="1">技术排名</van-radio>
            <van-radio name="2">技术比例</van-radio>
            <van-radio name="3">纠错</van-radio>
          </van-radio-group>
          <div style="margin-top: 20px" v-if="!hotTechnology.err">
            <ro v-if="radio==='1'"  :input="input"/>
            <pie1 v-if="radio==='2'"  :input="input"/>
            <div style="height: 390px;background-color: #2c3e50;color: #fff" v-if="radio==='3'">
              <van-row style="color: blanchedalmond;margin-bottom: 25px;margin-top: 5px">
                <van-col span="8">技术名称</van-col>
                <van-col span="8">要求数</van-col>
                <van-col span="8">操作</van-col>
              </van-row>
              <van-row v-for="index in 10" style="margin-bottom: 10px" :key="index">
                <van-col span="8">{{hotTechnology.name[10-index]}}</van-col>
                <van-col span="8">{{hotTechnology.num[10-index]}}</van-col>
                <van-col span="8"><van-button type="warning" size="mini" @click="jiucuo(index)">不合适</van-button></van-col>
              </van-row>
            </div>
          </div>
          <div v-else style="background-color:#2c3e50;height: 23rem;margin-top: 20px">
            <van-loading style="position: relative;top: 40%"/></div>
        </el-card>
      </van-tab>
    </van-tabs>

    <Foots/>
  </div>
</template>

<script>
  import Top from "../top/Top";
  import {noSuit, returnArea, returnHotTech, returnTrend, search, totalData} from "../../network/request";
  import cookie from 'js-cookie'
  import {Dialog, Toast} from 'vant';
  import Total from "../map/Total";
  import pie from "../map/pie";
  import Lines from "../map/Lines";
  import ro from '../map/top'
  import pie1 from "../map/pie1";
  import maps from "../map/mapss";
  import Foots from "../foots/Foots";
  import {wuhan} from "../../assets/js/city"
  import geo from '../../utils/compare'

  export default {
    name: "Home",
    components:{
      Top,Total,pie,Lines,ro,pie1,Foots,maps
    },
    data(){
      return{
        input:'',
        show:false,
        value1: 0,
        active:0,
        active1:0,
        active3:0,
        radio:'1',
        value2: 'a',
        citys:['北京',"上海","杭州","广州","深圳","成都","江苏","武汉"],
        website:['拉勾网'],
        activeId: 1,
        activeIndex: 0,
        display:[
            {name:'工作经验',info:['应届毕业生','1-3年','3-5年','5-10年','10年以上']},
            {name:'学历要求',info:['大专及以上','本科及以上','硕士及以上','不限']},
            {name:'公司规模',info:['<15人','15-150','150-500','500-2000','>2000']},
            {name:'融资阶段',info:['未融资','天使轮','A轮','B轮','C轮','D轮','上市','无需']},
        ],
        choiceStyle:{'background-color':'rgba(66,154,255,0.8)','color':'#fff'},
        flag:[{index:2},{index:1},{index:2},{index:3}],
        clickInfo:[],
        Salary_scale:[{nums:[],xueli:[],guimo:[],rongzi:[],jingyan:[]}],
        reInitCharts:0,
        reLine:0,
        region:[],
        mapMax:0,
        buttonActiveIndex:{city:'武汉',dataSource:0},
        hotTechnology:{name:[],num:[],err:false},
        chooseCity:this.$store.state.chooseCity.index,
        trends:{name:[],baidu:[],sougou:[],_360:[]}
      }
    },

    created() {
      //条件筛选
      this.clickOk()
      this.hotTech()

      //地图
      this.selectMap()

      this.trend()

    },
    methods:{

      choice(index,nums){
        this.flag[index].index = nums
        let info = {row:index,col:nums}
        this.clickInfo.push(info)
      },

      choose_button(item,index){
        this.buttonActiveIndex.city = item
        let city = {name:item,index:index}
        this.$store.commit('SET_CHOOSECITY',city)
        this.show = false
        if(Object.keys(geo()).length!==0){
          this.clickOk()
          this.hotTech()
          this.selectMap()
          this.trend()
        }else {
          Toast("该城市暂未开通")
        }

      },
      onsearch(){
        let re = /[A-Za-z]+/;
        if(this.input.search(re)<0&&this.input!==''){
          Toast("关键字里要有一门编程语言")
        }else {
          this.clickOk()
          this.hotTech()
          this.selectMap()
          this.trend()
          search(this.input).then(res=>{
            console.log(res.data)
            if(res.data.None){
              Toast.loading({message: '爬虫调用成功', forbidClick: true,});
              // this.reInitCharts=999
            }
            if(res.data.exist){
              Dialog.alert({message: '小爬虫出门工作了，待会再来吧',}).then(() => {});
              // this.reInitCharts=999
            }
            if(res.data.once){
              Dialog.alert({message: '小爬虫工作累了，正在睡觉，待会再来吧',}).then(() => {})
            }
          })
        }
      },
      selectMap(){
        //地图
        // if(Object.keys(geo()).length!==0){
        if(Object.keys(geo()).length===0){
          location.reload()
        }
        if(Object.keys(geo()).length!==0) {
          returnArea(this.input).then(res => {
            if (res.data !== "err") {
              this.region = geo().js
              for (let i of this.region) {
                const names = i.name
                i.value = res.data[names]
              }
              for (let i of this.region) {
                if (i.value === undefined) {
                  i.value = 0
                }
              }
              this.mapMax = Math.max(...Object.values(res.data))
            }
          })
        }else {
          Toast("该城市暂未开通")
        }

      },
      hotTech(){
        returnHotTech(this.input).then(res=>{
          if(res.data==='err'){
            this.hotTechnology.err = true
          }else {
            this.hotTechnology.name = res.data.hot_name;
            this.hotTechnology.num = res.data.hot_num;
            this.hotTechnology.err = false
          }
        })
      },
      jiucuo(index){
        noSuit(this.input,this.hotTechnology.name[10-index]).then(res=>{
          if(res.data==='ok'){
            Toast('纠错成功')
          }
        })
      },
      trend(){
        returnTrend(this.input).then(res=>{
          // console.log(res.data)
          if(!res.data.err){
            this.trends._360 = res.data._360
            this.trends.baidu = res.data.baidu
            this.trends.sougou = res.data.sougou
            this.trends.name = res.data.date
            this.reLine = 9999
          }
        })
      },
      clickOk(){
        let index1 = this.flag[0].index;
        let index2 = this.flag[1].index;
        let index3 = this.flag[2].index;
        let index4 = this.flag[3].index;
        switch (index1) {
          case 0:
            this.clickInfo[0]='经验应届毕业生';break;
          case 1:
            this.clickInfo[0]='经验1-3年';break;
          case 2:
            this.clickInfo[0]='经验3-5年';break;
          case 3:
            this.clickInfo[0]='经验5-10年';break;
          case 4:
            this.clickInfo[0]='10年以上';
        }
        switch (index2) {
          case 0:
            this.clickInfo[1]='大专及以上';break;
          case 1:
            this.clickInfo[1]='本科及以上';break;
          case 2:
            this.clickInfo[1]='硕士及以上';break;
          case 3:
            this.clickInfo[1]='学历不限';break;
        }
        switch (index3) {
          case 0:
            this.clickInfo[2]='少于15人';break;
          case 1:
            this.clickInfo[2]=[15,150];break;
          case 2:
            this.clickInfo[2]='150-500人';break;
          case 3:
            this.clickInfo[2]='500-2000人';break;
          case 4:
            this.clickInfo[2]='2000人以上';
        }
        switch (index4) {
          case 0:
            this.clickInfo[3]='未融资';break;
          case 1:
            this.clickInfo[3]='天使轮';break;
          case 2:
            this.clickInfo[3]='A轮';break;
          case 3:
            this.clickInfo[3]='B轮';break;
          case 4:
            this.clickInfo[3]='C轮';break;
          case 5:
            this.clickInfo[3]='D轮';break;
          case 6:
            this.clickInfo[3]='上市公司';break;
          case 7:
            this.clickInfo[3]='不需要融资';break;
        }
        this.clickInfo[4] = this.input;
        totalData(this.clickInfo).then(res=>{
          // console.log(res.data)
          if(res.data.err){
            Toast("未知错误")
            this.input=''
          }
          if(res.data.null){
            Toast("该条件无岗位，换个条件")
            this.input=''
          }else {
            this.Salary_scale[0].nums = res.data.section;
            this.Salary_scale[0].xueli = res.data.xueli;
            this.Salary_scale[0].guimo = res.data.guimo;
            this.Salary_scale[0].rongzi = res.data.rongzi;
            this.Salary_scale[0].jingyan = res.data.jingyan;
            this.reInitCharts+=1
          }

        })
      },
      logout(){
        Dialog.confirm({
          message: '确认退出吗，退出会清除您的浏览信息',
        }).then(() => {
          cookie.remove('token')
          cookie.remove('ROBOTCODE')
          this.$router.push('/cover')
        }).catch(()=>{});
      },
      gitee(){
        Toast('需要登录码云');
        setTimeout(()=>{
          window.open('https://gitee.com/xianglirui/k2020_employment_data','_blank')
        },2000)
      },
    }
  }
</script>

<style scoped>
  .page{
    width: 60%;
    display: flex;
    justify-content: space-between;
    margin: 0 auto;
  }
  .omd{
    color: #ffffff;
    font-size: 12px;
    position: relative;
    top: -1px;
  }
  .hope{
    position: relative;
    left: -15vw;
    top: 15px;
    font-size: 12px;
  }
</style>
