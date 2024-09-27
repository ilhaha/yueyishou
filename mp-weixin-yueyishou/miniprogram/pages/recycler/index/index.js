import {
  reqRecyclerBaseInfo
} from '../../../api/recycler/recycler'

Page({
  data: {
    tabList: [{
      title: '待接单',
      status: 1
    }, {
      title: '待上门',
      status: 3
    }, {
      title: '待确认',
      status: 4
    }, {
      title: '待付款',
      status: 6
    }],
  },
  onLoad() {
    this.getRecyclerBaseInfo()
  },
  async getRecyclerBaseInfo() {
    const res = await reqRecyclerBaseInfo();
    console.log(res);
  }
})