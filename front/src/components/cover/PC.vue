<template>
  <div class="row">
    <el-alert title="设备访问错误" type="info" description="请在手机端访问" :closable="false" show-icon effect="dark"></el-alert>
    <div style="margin-top: 50px">
      <div style="font-weight: bold;margin-bottom: 20px">电脑端模拟手机端方法：</div>
      <div style="color: #0ea5ae">谷歌浏览器:</div>
      <img src="~/assets/img/chrome.jpg" alt="" width="100%"/>
      <div style="color: #0ea5ae;margin-top: 30px">火狐浏览器:</div>
      <img src="~/assets/img/firefox.jpg" alt="" width="100%"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "PC"
  }
</script>

<style scoped>
  .row{
    margin: 0 auto;
    width: 70%;
    /*border: 1px solid red;*/
  }
</style>
