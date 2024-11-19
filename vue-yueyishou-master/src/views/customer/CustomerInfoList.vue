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

      <!-- <a-table
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
      > -->
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
          <a @click="showOperateList(record)">账号操作日志</a>
        </span>
      </a-table>
    </div>

    <customer-info-modal ref="modalForm" @ok="modalFormOk"></customer-info-modal>

    <a-modal v-model:open="statusDialogVisible" :title="statusTitle" @ok="confirm" width="60%" @cancel="clearData">
      <j-form-container>
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="操作原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reason">
                <a-textarea v-model="model.reason" placeholder="请输入操作原因"></a-textarea>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="佐证材料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="proof">
                <a-upload
                  :key="uploadKey"
                  v-model:file-list="fileList"
                  name="file"
                  :max-count="3"
                  :remove="handleRemove"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :action="uploadUrl"
                  :before-upload="beforeUpload"
                  @change="handleChange"
                  :headers="uploadHeaders"
                  :data="uploadData"
                  :multiple="true"
                >
                  <div v-if="fileList.length < 3">
                    <div class="ant-upload-text">上传佐证材料</div>
                  </div>
                </a-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </j-form-container>
    </a-modal>

    <a-modal
      v-model:open="operateTableDialogVisible"
      title="顾客账号操作日志列表"
      width="80%"
      :footer="null"
      @cancel="operateListClearData"
    >
      <j-form-container>
        <a-table
          ref="operateList"
          size="middle"
          :scroll="{ x: true }"
          bordered
          rowKey="id"
          :columns="operateListColumns"
          :dataSource="operateList"
          :pagination="operateListPagination"
          :loading="loading"
          class="j-table-force-nowrap"
          @change="handleOperateListChange"
        >
          <template slot="htmlSlot" slot-scope="text">
            <div v-html="text"></div>
          </template>
          <template slot="imgSlot" slot-scope="text, record">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
            <img
              v-for="(imgSrc, index) in text.split(',')"
              :key="index"
              :src="getImgView(imgSrc)"
              :preview="{ visible: true }"
              height="25px"
              alt=""
              style="max-width: 80px; font-size: 12px; font-style: italic; margin-right: 8px"
            />
          </template>
        </a-table>
      </j-form-container>
    </a-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import CustomerInfoModal from './modules/CustomerInfoModal'
import { getAction, postAction, putAction } from '../../api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'CustomerInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CustomerInfoModal,
  },
  data() {
    return {
      description: 'customer_info管理页面',
      statusTitle: '提交禁用顾客账号操作记录',
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
            return text == '1' ? '男' : '女'
          },
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
                  checked={isChecked}
                  checked-children="启用"
                  un-checked-children="禁用"
                  onChange={(checked) => this.switchStatusDialogVisible(checked, record)}
                />
              </a-space>
            )
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
        list: '/customer/list',
        delete: '/customer/delete',
        deleteBatch: '/customer/deleteBatch',
        exportXlsUrl: '/customer/exportXls',
        importExcelUrl: 'customer/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      statusDialogVisible: false,
      uploadUrl: `${process.env.VUE_APP_API_BASE_URL}/cos/upload`, // 替换为实际上传图片的API地址
      imageUrl: '', // 用于存储上传后的图片URL
      fileList: [], // 文件列表
      loading: false, // 用于控制loading效果
      uploadHeaders: {
        'x-access-token': Vue.ls.get(ACCESS_TOKEN), // 替换为实际的认证令牌
      },
      uploadData: { path: 'customer' },
      uploadKey: Date.now(),
      model: {
        proof: '',
        reason: '',
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      validatorRules: {
        proof: [{ required: true, message: '请上传佐证材料!' }],
        reason: [{ required: true, message: '请输入操作原因!' }],
      },
      operateCustomer: {},
      operateStatus: '',
      operateList: [],
      operateTableDialogVisible: false,
      operateListColumns: [
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
          title: '操作原因',
          align: 'center',
          dataIndex: 'reason',
        },
        {
          title: '操作佐证',
          align: 'center',
          dataIndex: 'proof',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '操作人',
          align: 'center',
          dataIndex: 'operator',
        },
        {
          title: '操作时间',
          align: 'center',
          dataIndex: 'createTime',
        },
      ],
      operateListPagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showSizeChanger: true,
        total: 0,
      },
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
    handleRemove(file) {
      // 确保过滤掉被删除的文件
      this.fileList = this.fileList.filter((item) => item.uid !== file.uid)

      // 更新 model.proof，仅包含未被删除的文件的 URL
      this.model.proof = this.fileList
        .map(function (f) {
          return (f.response && f.response.result && f.response.result.url) || f.url
        })
        .join(',')
    },
    // 关闭操作日志列表时初始化分页控件
    operateListClearData() {
      this.operateListPagination = {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showSizeChanger: true,
        total: 0,
      }
    },
    // 切换操作日志列表分页
    handleOperateListChange(pagination) {
      this.operateListPagination.current = pagination.current
      this.operateListPagination.pageSize = pagination.pageSize
      this.showOperateList(
        this.operateCustomer,
        this.operateListPagination.current,
        this.operateListPagination.pageSize
      )
    },
    // 获取操作日志列表
    showOperateList(customerInfo, pageNo = 1, pageSize = 10) {
      this.operateCustomer = customerInfo
      getAction(`/customer/account/list?customerId=${customerInfo.id}&pageNo=${pageNo}&pageSize=${pageSize}`).then(
        (res) => {
          this.operateList = res.result.records
          this.operateTableDialogVisible = true
          this.operateListPagination.total = res.result.total
        }
      )
    },
    // 提交操作日志
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.model.customerId = this.operateCustomer.id
          postAction('/customer/account/operate/add', this.model).then((res) => {
            if (res.success) {
              this.$message.success('已提交')
              this.statusDialogVisible = false
              this.clearData()
              this.handleStatusChange(this.operateStatus, this.operateCustomer)
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      })
    },
    clearData() {
      this.fileList = []
      this.uploadKey = Date.now()
      this.model.proof = ''
      this.model.reason = ''
    },
    beforeUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('只能上传 JPG/PNG 格式的图片!')
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('图片大小必须小于 2MB!')
        return false
      }
      if (this.fileList.length >= 3) {
        this.$message.error('最多上传三张图片!')
        return false
      }
      this.loading = true
      return true
    },
    handleChange(info) {
      const { file } = info
      if (file.status === 'done') {
        // 成功上传后，获取图片URL
        const { response } = file
        if (response && response.result) {
          this.fileList = [...this.fileList, { ...file, url: response.result.showUrl }]

          this.model.proof = this.fileList.map((f) => f.response.result.url).join(',') // 保存多个图片路径
          this.$message.success('图片上传成功')
        }
        this.loading = false
      } else if (file.status === 'error') {
        this.$message.error('图片上传失败')
        this.loading = false
      }
    },
    switchStatusDialogVisible(checked, record) {
      this.statusDialogVisible = !this.statusDialogVisible
      this.operateCustomer = record
      this.operateStatus = checked
      if (checked) {
        this.statusTitle = '提交启用顾客账号操作记录'
      } else {
        this.statusTitle = '提交禁用顾客账号操作记录'
      }
    },
    switchStatus(status, ids) {
      postAction('/customer/switch/status', { customerIds: ids, status: status }).then((res) => {
        // this.$message.success(res.message)
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