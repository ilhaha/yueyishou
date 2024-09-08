<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="客户昵称">
              <a-input placeholder="请输入客户昵称" v-model="queryParam.nickname"></a-input>
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
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="switchStatus(1, selectedRowKeys)"><a-icon type="check" />启用</a-menu-item>
          <a-menu-item key="2" @click="switchStatus(2, selectedRowKeys)"><a-icon type="close" />停用</a-menu-item>
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
      </a-table>
    </div>

    <customer-info-modal ref="modalForm" @ok="modalFormOk"></customer-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import CustomerInfoModal from './modules/CustomerInfoModal'
import { getAction, postAction, putAction } from '../../api/manage'

export default {
  name: 'CustomerInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CustomerInfoModal,
  },
  data() {
    return {
      description: 'customer_info管理页面',
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
          title: '客户昵称',
          align: 'center',
          dataIndex: 'nickname',
        },
        {
          title: '性别',
          align: 'center',
          dataIndex: 'gender',
          customRender: (text, record) => {
            return text == '1'? '男' : '女';
          }
        },
        {
          title: '头像',
          align: 'center',
          dataIndex: 'avatarUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '电话',
          align: 'center',
          dataIndex: 'phone',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          customRender: (text, record) => {
            const isChecked = text == '1'
            return (
              <a-space direction="vertical">
                <a-switch
                  checked={isChecked} // 1 为启用, 2 为禁用
                  checked-children="启用"
                  un-checked-children="禁用"
                  onChange={(checked) => this.handleStatusChange(checked, record)}
                />
              </a-space>
            )
          },
        },
      ],
      url: {
        list: '/customer/list',
        delete: '/customer/delete',
        deleteBatch: '/customer/deleteBatch',
        exportXlsUrl: '/customer/exportXls',
        importExcelUrl: 'customer/importExcel',
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
    switchStatus(status, ids) {
      postAction('/customer/switch/status', { customerIds: ids, status: status }).then((res) => {
        this.$message.success(res.message)
        this.loadData()
      })
    },
    handleStatusChange(checked, record) {
      const newStatus = checked ? 1 : 2
      let id = []
      id.push(record.id)
      this.switchStatus(newStatus, id)
    },

    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'nickname', text: '客户昵称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gender', text: '性别', dictCode: '' })
      fieldList.push({ type: 'string', value: 'avatarUrl', text: '头像', dictCode: '' })
      fieldList.push({ type: 'string', value: 'phone', text: '电话', dictCode: '' })
      fieldList.push({ type: 'int', value: 'status', text: '1有效，2禁用', dictCode: '' })
      fieldList.push({ type: 'date', value: 'createTime', text: '创建时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>