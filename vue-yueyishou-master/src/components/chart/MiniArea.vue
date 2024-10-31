<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <v-chart :force-fit="true" :height="height" :data="data" :scale="scale" :padding="[36, 0, 18, 0]">
        <v-tooltip />
        <v-smooth-area position="x*y" />
      </v-chart>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MiniArea',
  props: {
    dataSource: {
      type: Array,
      default: () => [],
    },
    // x 轴别名
    x: {
      type: String,
      default: '日期',
    },
    // y 轴别名
    y: {
      type: String,
      default: '日订单量',
    },
  },
  data() {
    return {
      data: [],
      height: 100,
    }
  },
  computed: {
    scale() {
      return [
        { dataKey: 'x', title: this.x, alias: this.x },
        { dataKey: 'y', title: this.y, alias: this.y },
      ]
    },
  },
  created() {
    this.data = this.dataSource
  },
}
</script>

<style lang="less" scoped>
@import 'chart';
</style>