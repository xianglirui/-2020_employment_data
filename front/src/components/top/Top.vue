<template>
  <div>
    <div class="top"><slot name="home-top"></slot></div>
  </div>
</template>

<script>
  export default {
    name: "Top"
  }
</script>

<style scoped>
  .top{
    position: fixed;
    right: 0;
    top: 0;
    left: 0;
    z-index: 9;
    width: 100%;
    height: 50px;
    background-color: #2c3e50;
  }
</style>
