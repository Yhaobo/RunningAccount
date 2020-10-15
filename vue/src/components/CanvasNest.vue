<template>
  <vue-canvas-nest :config="config"></vue-canvas-nest>
</template>
<script>
import vueCanvasNest from "vue-canvas-nest"


export default {
  props: {
    num: {type: Number, default: 99}
  },
  data() {
    return {
      config: {
        color: '62,209,167',
        opacity: 1,
        zIndex: 0,
        count: this.num,
      }
    }
  },
  components: {vueCanvasNest}
}

</script>