<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app',
  components: {},
}
</script>

<style>
#app {
  font-family: "Microsoft YaHei", "微软雅黑", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  background-color: transparent;
}

html {
  background-image: url(assets/background.png);
  background-repeat: no-repeat;
  background-size: 100%;
  background-attachment: fixed;
}
</style>
