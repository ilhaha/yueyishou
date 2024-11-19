      <template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单联系人">
              <a-input placeholder="请输入订单联系人" v-model="queryParam.orderContactPerson"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="回收员姓名">
              <a-input placeholder="请输入回收员姓名" v-model="queryParam.recyclerName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="orderId"
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
            v-for="(imgSrc, index) in text.split(',')"
            :key="index"
            :src="getImgView(imgSrc)"
            :preview="{ visible: true }"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic; margin-right: 8px"
          />
        </template>
        <span slot="action" slot-scope="text, record">
          <a-popconfirm
            title="确认是否同意回收员拒收该订单"
            ok-text="确认"
            cancel-text="取消"
            @confirm="review(2, record)"
            @cancel="cancel"
          >
            <a>通过申请</a>
          </a-popconfirm>

          <a-divider type="vertical" />
          <a @click="switchStatusDialogVisible(-1, record)">驳回申请</a>
        </span>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>
      </a-table>
    </div>
    <a-modal
      v-model:open="statusDialogVisible"
      title="提交拒收订单驳回申请操作记录"
      @ok="confirm"
      width="60%"
      @cancel="clearData"
    >
      <j-form-container>
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reason">
                <a-textarea v-model="model.reason" placeholder="请输入驳回原因"></a-textarea>
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
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import OrderInfoModal from './modules/OrderInfoModal'
import dayjs from 'dayjs'
import { httpAction, postAction } from '../../api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'OrderInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  data() {
    return {
      queryParam: {
        orderContactPerson: '',
        recyclerName: '',
      },
      dateFormat: 'YYYY-MM-DD',
      description: '申请拒收订单列表',
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
          title: '订单回收品类',
          align: 'center',
          dataIndex: 'categoryName',
        },
        {
          title: '回收重量(公斤)',
          align: 'center',
          dataIndex: 'recycleWeigh',
        },
        {
          title: '订单联系人',
          align: 'center',
          dataIndex: 'orderContactPerson',
        },
        {
          title: '订单联系人电话',
          align: 'center',
          dataIndex: 'orderContactPhone',
        },
        {
          title: '顾客地点',
          align: 'center',
          dataIndex: 'customerLocation',
        },
        {
          title: '回收物实物照片',
          align: 'center',
          dataIndex: 'actualPhotos',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '回收员名称',
          align: 'center',
          dataIndex: 'recyclerName',
        },
        {
          title: '拒收理由',
          align: 'center',
          dataIndex: 'cancelMessage',
        },
        {
          title: '回收员拒收回收物照片',
          align: 'center',
          dataIndex: 'rejectActualPhotos',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '拒收补偿(元)',
          align: 'center',
          dataIndex: 'rejectCompensation',
        },
        {
          title: '申请拒收时间',
          align: 'center',
          dataIndex: 'cancelTime',
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
        list: '/order/reject/list',
        delete: '/order/delete',
        deleteBatch: '/order/deleteBatch',
        exportXlsUrl: '/order/exportXls',
        importExcelUrl: '/order/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      rejectOrderList: [],
      statusDialogVisible: false,
      uploadUrl: `${process.env.VUE_APP_API_BASE_URL}/cos/upload`, // 替换为实际上传图片的API地址
      imageUrl: '', // 用于存储上传后的图片URL
      fileList: [], // 文件列表
      loading: false, // 用于控制loading效果
      uploadHeaders: {
        'x-access-token': Vue.ls.get(ACCESS_TOKEN), // 替换为实际的认证令牌
      },
      uploadData: { path: 'order' },
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
      operateOrder: {},
      operateStatus: '',
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
    switchStatusDialogVisible(rejectStatus, record) {
      this.statusDialogVisible = !this.statusDialogVisible
      this.operateOrder = record
      this.operateStatus = rejectStatus
    },
    // 提交操作日志
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.model.orderId = this.operateOrder.orderId
          postAction('/order/rejection/operate/add', this.model).then((res) => {
            if (res.success) {
              this.$message.success('已提交')
              this.statusDialogVisible = false
              this.clearData()
              this.review(this.operateStatus, this.operateOrder)
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
    // 处理申请通过、不通过
    review(rejectStatus, orderInfo) {
      postAction('/order/approval/reject', {
        rejectStatus: rejectStatus,
        orderId: orderInfo.orderId,
        customerId: orderInfo.customerId,
        recyclerId: orderInfo.recyclerId,
        rejectCompensation: orderInfo.rejectCompensation,
      }).then((res) => {
        if (res.result) {
          // this.$message.success('已审批')
          this.loadData()
        }
      })
    },
    formatDate(text) {
      if (!text) return ''
      const date = new Date(text)
      // 年月日
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0') // 月份从0开始，需要+1
      const day = String(date.getDate()).padStart(2, '0')

      // 时分
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')

      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'customerId', text: '客户ID', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orderNo', text: '订单号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'customerLocation', text: '客户地点', dictCode: '' })
      fieldList.push({ type: 'int', value: 'categoryId', text: '订单回收品类ID', dictCode: '' })
      fieldList.push({ type: 'int', value: 'recyclerId', text: '回收员ID', dictCode: '' })
      fieldList.push({ type: 'date', value: 'acceptTime', text: '回收员接单时间' })
      fieldList.push({ type: 'date', value: 'appointmentTime', text: '预约时间' })
      fieldList.push({ type: 'date', value: 'arriveTime', text: '回收员到达时间' })
      fieldList.push({ type: 'int', value: 'status', text: '订单状态', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cancelMessage', text: '取消订单信息', dictCode: '' })
      fieldList.push({ type: 'string', value: 'remark', text: '订单备注信息', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'estimatedTotalAmount', text: '订单预计回收金额', dictCode: '' })
      fieldList.push({
        type: 'string',
        value: 'orderContactPhone',
        text: '订单联系电话',
        dictCode: '',
      })
      fieldList.push({
        type: 'string',
        value: 'orderContactPerson',
        text: '订单联系人',
        dictCode: '',
      })
      fieldList.push({
        type: 'BigDecimal',
        value: 'expectRecyclerPlatformAmount',
        text: '回收员预支付平台订单金额',
        dictCode: '',
      })
      fieldList.push({
        type: 'BigDecimal',
        value: 'expectCustomerPlatformAmount',
        text: '顾客预支付平台订单金额',
        dictCode: '',
      })
      fieldList.push({ type: 'date', value: 'createTime', text: '创建时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
      <style scoped>
@import '~@assets/less/common.less';
</style>