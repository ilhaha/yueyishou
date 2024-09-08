<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="认证状态">
              <a-select ref="select" v-model:value="queryParam.authStatus" @change="searchQuery">
                <a-select-option value="1">待审核</a-select-option>
                <a-select-option value="2">已认证</a-select-option>
                <a-select-option value="-1">认证未通过</a-select-option>
              </a-select>
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
      <!-- <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="3" @click="batchReview(2, selectedRowKeys)" v-if="queryParam.authStatus == '1'"
            ><a-icon type="audit" />通过审核</a-menu-item
          >
          <a-menu-item key="4" @click="batchReview(-1, selectedRowKeys)" v-if="queryParam.authStatus == '1'"
            ><a-icon type="audit" />未过审核</a-menu-item
          >

           <a-menu-item key="5" @click="batchReview(1, selectedRowKeys)" v-if="queryParam.authStatus == '2'"
            ><a-icon type="audit" />重新审核</a-menu-item
          >

            <a-menu-item key="6" @click="batchDel" v-if="queryParam.authStatus == '-1'"
            ><a-icon type="delete" />删除</a-menu-item
          >

          <a-menu-item key="1" @click="switchStatus(1, selectedRowKeys)"  v-if="queryParam.authStatus == '2'"><a-icon type="check" />启用</a-menu-item>
          <a-menu-item key="2" @click="switchStatus(2, selectedRowKeys)"  v-if="queryParam.authStatus == '2'"><a-icon type="close" />停用</a-menu-item>
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
          <a @click="review(2,record)" v-if="record.authStatus == 1">通过审核</a>
          <a-divider type="vertical" v-if="record.authStatus == 1"/>
          <a @click="review(-1,record)" v-if="record.authStatus == 1">未过审核</a>
          <a @click="review(1,record)" v-if="record.authStatus == 2">重新审核</a>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)"  v-if="record.authStatus == -1">
                  <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <recycler-info-modal ref="modalForm" @ok="modalFormOk"></recycler-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import RecyclerInfoModal from './modules/RecyclerInfoModal'
import { getAction, postAction, putAction } from '../../api/manage'

export default {
  name: 'RecyclerInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    RecyclerInfoModal,
  },
  data() {
    return {
      queryParam: {
        authStatus: '1',
        name: '',
      },
      description: 'recycler_info管理页面',
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
          title: '姓名',
          align: 'center',
          dataIndex: 'name',
        },

        {
          title: '身份证号码',
          align: 'center',
          dataIndex: 'idcardNo',
        },
        {
          title: '身份证地址',
          align: 'center',
          dataIndex: 'idcardAddress',
        },
        {
          title: '身份证有效期',
          align: 'center',
          dataIndex: 'idcardExpire',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
        },
        {
          title: '身份证正面',
          align: 'center',
          width: 150,
          dataIndex: 'idcardFrontUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '身份证背面',
          align: 'center',
          width: 150,
          dataIndex: 'idcardBackUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '手持身份证',
          align: 'center',
          width: 150,
          dataIndex: 'idcardHandUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '认证状态',
          align: 'center',
          dataIndex: 'authStatus',
          customRender: function (text) {
            if (text == '0') {
              return '未认证'
            } else if (text == '1') {
              return '待审核'
            } else if (text == '2') {
              return '已通过'
            } else {
              return '未通过'
            }
          },
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          customRender: (text, record) => {
            const isChecked = text == '1'
            const isDisabled = record.authStatus != 2;
            return (
              <a-space direction="vertical">
                <a-switch
                  checked={isChecked} // 1 为启用, 2 为禁用
                  checked-children="启用"
                  un-checked-children="禁用"
                  disabled={isDisabled}
                  onChange={(checked) => this.handleStatusChange(checked, record)}
                />
              </a-space>
            )
          },
        },
        {
          title: '回收员工号',
          align: 'center',
          dataIndex: 'jobNo',
        },
        {
          title: '昵称',
          align: 'center',
          dataIndex: 'nickname',
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
          title: '性别',
          align: 'center',
          dataIndex: 'gender',
          customRender: function (text) {
            return text === 1 ? '男' : '女'
          },
        },
        {
          title: '生日',
          align: 'center',
          dataIndex: 'birthday',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
        },
        {
          title: '评分',
          align: 'center',
          dataIndex: 'score',
        },
        {
          title: '订单量统计',
          align: 'center',
          dataIndex: 'orderCount',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          // customRender: function (text) {
          //   return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          // },
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
        list: '/recycler/list',
        delete: '/recycler/delete',
        deleteBatch: '/recycler/deleteBatch',
        exportXlsUrl: '/recycler/exportXls',
        importExcelUrl: 'recycler/importExcel',
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
    review(authStatus,record){
      let recyclerIds = []
      recyclerIds.push(record.id)
      this.batchReview(authStatus, recyclerIds)
    },
    batchReview(authStatus, recyclerIds) {
      postAction('/recycler/auth', { recyclerIds: recyclerIds, authStatus: authStatus }).then((res) => {
        this.$message.success(res.message)
        this.loadData()
      })
    },
    switchStatus(status, ids) {
      postAction('/recycler/switch/status', { recyclerIds: ids, status: status }).then((res) => {
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
      fieldList.push({ type: 'string', value: 'nickname', text: '昵称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'avatarUrl', text: '头像', dictCode: '' })
      fieldList.push({ type: 'string', value: 'phone', text: '电话', dictCode: '' })
      fieldList.push({ type: 'string', value: 'name', text: '姓名', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gender', text: '性别 1:男 2：女', dictCode: '' })
      fieldList.push({ type: 'date', value: 'birthday', text: '生日' })
      fieldList.push({ type: 'string', value: 'idcardNo', text: '身份证号码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'idcardAddress', text: '身份证地址', dictCode: '' })
      fieldList.push({ type: 'date', value: 'idcardExpire', text: '身份证有效期' })
      fieldList.push({ type: 'string', value: 'idcardFrontUrl', text: '身份证正面', dictCode: '' })
      fieldList.push({ type: 'string', value: 'idcardBackUrl', text: '身份证背面', dictCode: '' })
      fieldList.push({ type: 'string', value: 'idcardHandUrl', text: '手持身份证', dictCode: '' })
      fieldList.push({ type: 'string', value: 'faceModelId', text: '腾讯云人脸模型id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jobNo', text: '回收员工号', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'score', text: '评分', dictCode: '' })
      fieldList.push({ type: 'int', value: 'orderCount', text: '订单量统计', dictCode: '' })
      fieldList.push({
        type: 'int',
        value: 'authStatus',
        text: '认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过',
        dictCode: '',
      })
      fieldList.push({ type: 'int', value: 'status', text: '状态，1正常，2禁用', dictCode: '' })
      fieldList.push({ type: 'date', value: 'createTime', text: '创建时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>