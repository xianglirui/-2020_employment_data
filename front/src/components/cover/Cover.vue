<template>
  <div class="row">
    <van-swipe style="height:100vh;" vertical :show-indicators=false  @change="onChange">
      <van-swipe-item>
        <div class="img"><img src="../../assets/img/cover.png" style="width: 100%;height: 100vh"/></div>
      </van-swipe-item>
      <van-swipe-item>
        <div style="margin: 10rem"></div>
        <div style="margin-bottom: 10rem">
          <van-divider :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }">
            再往上一点
          </van-divider>
        </div>
        <Login/>
      </van-swipe-item>
    </van-swipe>
  </div>
</template>

<script>
  import Login from "../Login/Login";
  export default {
    name: "Cover",
    components:{Login},
    methods: {
      onChange(index) {
        if(index===1){
          this.$router.push('/login')
        }
      },
    },
  }
</script>

<style scoped>
  .row{
    margin-top: -60px;
  }
  .img{
    position: fixed;
    left: 0;
    top: 0;
    z-index: 99;
  }
</style>
