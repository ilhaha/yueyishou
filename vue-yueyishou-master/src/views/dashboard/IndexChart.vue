<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="24" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总佣金收入" :total="totalCommissionIncome">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend :flag="syncyclicRatio > 0 ? 'up' : 'down'" style="margin-right: 16px">
              <span slot="term">周同比</span>
              {{ Math.abs(syncyclicRatio) }}%
            </trend>
            <trend :flag="isodiurnalRatio > 0 ? 'up' : 'down'">
              <span slot="term">日同比</span>
              {{ Math.abs(isodiurnalRatio) }}%
            </trend>
          </div>
          <template slot="footer"
            >今日佣金收入<span>￥ {{ todayCommissionIncome }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="24" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总订单量" :total="totalOrderCount">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area :dataSource="dailyOrderCountMap" />
          </div>
          <template slot="footer"
            >本周订单量<span> {{ currentWeekOrderCount }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="24" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总支付笔数" :total="totalOrderPayCount">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" :dataSource="dailyOrderPayCountMap" />
          </div>
          <template slot="footer"
            >本周转化率 <span>{{ conversionRate }}%</span></template
          >
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a @click="switchDataSource(1)">今日</a>
              <a @click="switchDataSource(2)">本周</a>
              <a @click="switchDataSource(3)">本月</a>
              <a @click="switchDataSource(4)">本年</a>
            </div>
          </div>
          <a-tab-pane loading="true" tab="佣金收入额" key="1">
            <a-row>
              <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
                <bar title="佣金收入额排行" :dataSource="barData" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计" :style="{ marginTop: '24px' }">
          <a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="environment" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="team" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="rise" style="font-size: 24px" />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'

import Trend from '@/components/Trend'
import { getLoginfo, getVisitInfo } from '@/api/api'
import { getAction } from '../../api/manage'

export default {
  name: 'IndexChart',
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo,
  },
  data() {
    return {
      loading: true,
      center: null,
      loginfo: {},
      visitFields: ['ip', 'visit'],
      visitInfo: [],
      indicator: <a-icon type="loading" style="font-size: 24px" spin />,
      totalCommissionIncome: '',
      syncyclicRatio: '',
      isodiurnalRatio: '',
      todayCommissionIncome: '',
      totalOrderCount: '',
      currentWeekOrderCount: '',
      dailyOrderCountMap: [],
      totalOrderPayCount: '',
      dailyOrderPayCountMap: [],
      conversionRate: [],
      yearCommissionIncome: [],
      timeCommissionIncome: [],
      weekCommissionIncome: [],
      monthCommissionIncome: [],
      barData: [],
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.initLogInfo()
    this.initIndexCollect()
  },
  methods: {
    // 切换佣金收入额排行版的数据
    switchDataSource(flag) {
      flag == 1
        ? (this.barData = this.timeCommissionIncome)
        : flag == 2
        ? (this.barData = this.weekCommissionIncome)
        : flag == 3
        ? (this.barData = this.monthCommissionIncome)
        : (this.barData = this.yearCommissionIncome)
    },
    // 初始化首页数据
    initIndexCollect() {
      getAction('/index/collect').then((res) => {
        this.totalCommissionIncome = res.data.totalCommissionIncome.toLocaleString('en-US', {
          minimumFractionDigits: 1,
          maximumFractionDigits: 2,
        })
        this.syncyclicRatio = res.data.syncyclicRatio.toFixed(0)
        this.isodiurnalRatio = res.data.isodiurnalRatio.toFixed(0)
        this.todayCommissionIncome = res.data.todayCommissionIncome
        this.totalOrderCount = res.data.totalOrderCount.toLocaleString('en-US')
        this.currentWeekOrderCount = res.data.currentWeekOrderCount.toLocaleString('en-US')
        this.dailyOrderCountMap = res.data.dailyOrderCountMap
        this.totalOrderPayCount = res.data.totalOrderPayCount.toString()
        this.dailyOrderPayCountMap = res.data.dailyOrderPayCountMap
        this.conversionRate = res.data.conversionRate.toFixed(0)
        this.yearCommissionIncome = res.data.yearCommissionIncome
        this.timeCommissionIncome = res.data.timeCommissionIncome
        this.weekCommissionIncome = res.data.weekCommissionIncome
        this.monthCommissionIncome = res.data.monthCommissionIncome
        this.barData = res.data.yearCommissionIncome
      })
    },
    initLogInfo() {
      getLoginfo(null).then((res) => {
        if (res.success) {
          Object.keys(res.result).forEach((key) => {
            res.result[key] = res.result[key] + ''
          })
          this.loginfo = res.result
        }
      })
      getVisitInfo().then((res) => {
        if (res.success) {
          this.visitInfo = res.result
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }
  p {
    line-height: 42px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>