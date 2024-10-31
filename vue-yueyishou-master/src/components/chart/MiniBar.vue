<template>
  <div :style="{ width: width == null ? 'auto' : width + 'px' }">
    <v-chart :forceFit="width == null" :height="height" :data="data" :scale="scale" padding="0">
      <v-tooltip />
      <v-bar position="x*y" />
    </v-chart>
  </div>
</template>

<script>
export default {
  name: 'MiniBar',
  props: {
    dataSource: {
      type: Array,
      default: () => [],
    },
    width: {
      type: Number,
      default: null,
    },
    height: {
      type: Number,
      default: 200,
    },
    // x 轴别名
    x: {
      type: String,
      default: '日期',
    },
    // y 轴别名
    y: {
      type: String,
      default: '日订单支付量',
    },
  },
  created() {
    this.data = this.dataSource
  },
  computed: {
    scale() {
      return [
        { dataKey: 'x', title: this.x, alias: this.x },
        { dataKey: 'y', title: this.y, alias: this.y },
      ]
    },
  },
  data() {
    return {
      data: [],
    }
  },
}
</script>

<style lang="less" scoped>
@import 'chart';
</style>