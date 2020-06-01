<template>
  <div>
    <top class="top">
      <div slot="home-top">
        <span style="color: white;font-weight: bold;position: relative;top: 15px">
          <icon-svg iconClass="icon-renzheng"/>&nbsp;Coder-xlr</span>
      </div>
    </top>
    <el-alert title="请不要禁用浏览器cookie" type="error" show-icon :closable="false" effect="dark" style="margin:100px auto;width: 65%"/>
    <el-card class="card">
      <div class="img"><img src="@/assets/img/hello.png" alt="" width="150px" style="position: relative;left: 10px"/>
        <icon-svg iconClass="icon-xiaolian" style="font-size: 20px;position: relative;top: 28px;left: -10px"/></div>
      <div class="txt">请证明你不是机器人</div>
      <van-tag color="#f2826a" plain>滑到{{aim}}</van-tag>
      <van-row style="width: 75%;margin: 20px auto;">
        <van-col span="21"><van-slider v-model="value1" bar-height="5px"/></van-col>
        <van-col span="1" offset="2" style="position: relative;top: -7px"><van-tag>{{value1}}</van-tag></van-col>
      </van-row>

      <div style="margin: 10px"></div>
      <transition name="van-slide-down">
        <el-alert title="验证成功" type="success" show-icon :closable="false" effect="dark" v-show="okShow"
                  style="width: 60%;margin: 0 auto;"></el-alert>
      </transition>
      </el-card>
  </div>
</template>

<script>
  import Top from "../top/Top";
  import {getRandom,getInfo} from "../../network/request";
  import cookie from "js-cookie";
  import {generateUUID} from "../../utils/utils";
  export default {
    name: "Login",
    components:{
      Top
    },
    watch:{
      value1(val){
        //要保证value1==aim同时不能再变化
        //不能用val去和aim比较，因为val太快了
        if(this.value1===this.aim) {
          setTimeout(()=>{
            if(this.value1===this.aim){
              this.okShow = true
              setTimeout(()=>{
                let code = generateUUID();
                cookie.set('ROBOTCODE',code,60*60);
                getInfo(code).then(res=>{
                  cookie.set('token',res.data,60*60)
                  this.$router.push('/home')
                }).catch(()=>{})
              },1000)
            }
          },1000)
        }
      }
    },
    data(){
      return{
        value1:0,
        ok:false,
        aim:0,
        okShow:false
      }
    },
    created() {
      getRandom().then(res=>{
        this.aim = res.data
      })
    }
  }
</script>

<style scoped>
  .card{
    width: 70%;
    margin: 50px auto;
    height: 350px;
    position: relative;
    top: -10vh;
  }
  .top{
    /*margin-top: -60px;*/
  }
  .img{
    display: flex;
    width: 100%;
    justify-content: center;
  }
  .txt{
    font-size: 16px;
    margin-bottom: 20px;
    margin-top: 0px;
  }
</style>
