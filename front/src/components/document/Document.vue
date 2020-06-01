<template>
  <div class="row">
    <van-nav-bar title="文档"  left-text="返回" left-arrow @click-left="onClickLeft" fixed>
      <template #right>
        <span style="color:#65a1fa" @click="go">码云</span><van-icon name="share" />
      </template>
    </van-nav-bar>

    <div style="width: 90%;margin:0 auto;overflow: hidden">
      <markdown/>
<!--      <div v-html="md" class="md"></div>-->
    </div>
  </div>

</template>

<script>
  import {Toast} from 'vant'
  import {returnTrend} from "../../network/request";
  import Markdown from "./Markdown";
  export default {

    name: 'Document',
    components:{Markdown},
    data() {
      return {
        // md:markdown
      };
    },
    created() {
      // console.log(markdown)
    },
    methods: {
      onClickLeft(){
        this.$router.push({path:'/home'})
      },
      go(){
        Toast('需要登录码云');
        setTimeout(()=>{
          window.open('https://gitee.com/xianglirui/k2020_employment_data','_blank')
        },2000)
      }
    },
  };
</script>

<style scoped>




</style>
