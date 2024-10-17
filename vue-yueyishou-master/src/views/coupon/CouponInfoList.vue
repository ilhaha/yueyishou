<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="服务费抵扣券名字">
              <a-input placeholder="请输入服务费抵扣券名字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            :preview="record.id"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <coupon-info-modal ref="modalForm" @ok="modalFormOk"></coupon-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import CouponInfoModal from './modules/CouponInfoModal'

export default {
  name: 'CouponInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CouponInfoModal,
  },
  data() {
    return {
      description: 'coupon_info管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '服务费抵扣券名字',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '服务费抵扣券类型',
          align: 'center',
          dataIndex: 'couponType',
          customRender: function (t, r, index) {
            return t == '1' ? '折扣' : '免单'
          },
        },
        {
          title: '折扣',
          align: 'center',
          dataIndex: 'discount',
          customRender: function (t, r, index) {
            return t == 0 ? '免单' : t + '折'
          },
        },
        {
          title: '使用门槛',
          align: 'center',
          dataIndex: 'conditionAmount',
          customRender: function (t, r, index) {
            return t == '0' ? '无门槛' : t
          },
        },
        {
          title: '每人限领张数',
          align: 'center',
          dataIndex: 'perLimit',
          customRender: function (t, r, index) {
            return t == '0' ? '无限制' : t
          },
        },
        {
          title: '已使用数量',
          align: 'center',
          dataIndex: 'useCount',
        },
        {
          title: '领取数量',
          align: 'center',
          dataIndex: 'receiveCount',
        },
        {
          title: '领取几天后过期',
          align: 'center',
          dataIndex: 'expireTime',
          customRender: function (text) {
            return text == '0' ? '无期限' : text
          },
        },
        {
          title: '服务费抵扣券描述',
          align: 'center',
          dataIndex: 'description',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          customRender: function (t, r, index) {
            return t == '0' ? '未发布' : t == '1' ? '已发布' : '已到期'
          },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/coupon/list',
        delete: '/coupon/delete',
        deleteBatch: '/coupon/deleteBatch',
        exportXlsUrl: '/coupon/exportXls',
        importExcelUrl: '/couponInfo/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'couponType', text: '服务费抵扣券类型' })
      fieldList.push({ type: 'string', value: 'name', text: '服务费抵扣券名字' })
      fieldList.push({ type: 'number', value: 'discount', text: '折扣' })
      fieldList.push({ type: 'number', value: 'conditionAmount', text: '使用门槛' })
      fieldList.push({ type: 'int', value: 'perLimit', text: '每人限领张数' })
      fieldList.push({ type: 'int', value: 'useCount', text: '已使用数量' })
      fieldList.push({ type: 'int', value: 'receiveCount', text: '领取数量' })
      fieldList.push({ type: 'int', value: 'expireTime', text: '领取后几天到期' })
      fieldList.push({ type: 'string', value: 'description', text: '服务费抵扣券描述' })
      fieldList.push({ type: 'int', value: 'status', text: '状态' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>