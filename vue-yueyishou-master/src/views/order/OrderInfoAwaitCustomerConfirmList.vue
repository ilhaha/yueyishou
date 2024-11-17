      <template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="预约时间">
              <a-date-picker
                v-model:value="queryParam.appointmentTime"
                @change="searchQuery"
                :format="dateFormat"
                :value-format="dateFormat"
              />
            </a-form-item>
          </a-col>
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
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>
      </a-table>
    </div>

    <order-info-modal ref="modalForm" @ok="modalFormOk"></order-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import OrderInfoModal from './modules/OrderInfoModal'
import dayjs from 'dayjs'
import { httpAction, postAction } from '../../api/manage'
export default {
  name: 'OrderInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    OrderInfoModal,
  },
  data() {
    return {
      queryParam: {
        appointmentEndTime: dayjs().format('YYYY-MM-DD'),
        status: '4',
      },
      dateFormat: 'YYYY-MM-DD',
      description: '待顾客确认订单列表',
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
          title: '品类单价(元)',
          align: 'center',
          dataIndex: 'unitPrice',
        },
        {
          title: '回收重量(公斤)',
          align: 'center',
          dataIndex: 'recycleWeigh',
        },
        {
          title: '回收物实物照片',
          align: 'center',
          dataIndex: 'actualPhotos',
          scopedSlots: { customRender: 'imgSlot' },
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
          title: '预约回收时间',
          align: 'center',
          dataIndex: 'appointmentTime',
        },
        {
          title: '接单回收员',
          align: 'center',
          dataIndex: 'recyclerName',
        },
        {
          title: '接单时间',
          align: 'center',
          dataIndex: 'acceptTime',
        },
        {
          title: '到达时间',
          align: 'center',
          dataIndex: 'arriveTime',
        },
        {
          title: '到达超时时间(分钟)',
          align: 'center',
          dataIndex: 'arriveTimoutMin',
          customRender: function (t, r, index) {
            return t == 0 ? '按时到达' : t
          },
        },
        {
          title: '订单实回收金额(元)',
          align: 'center',
          dataIndex: 'realOrderRecycleAmount',
        },
        {
          title: '顾客实回收收入金额(元)',
          align: 'center',
          customRender: function (t, r, index) {
            return (
              r.realCustomerRecycleAmount ||
              r.realOrderRecycleAmount - r.expectCustomerPlatformAmount + (r.recyclerOvertimeCharges || 0)
            )
          },
        },
        {
          title: '回收员实回收支出金额(元)',
          align: 'center',
          dataIndex: 'realRecyclerAmount',
        },
        {
          title: '到达超时赔偿(元)',
          align: 'center',
          dataIndex: 'recyclerOvertimeCharges',
          customRender: function (t, r, index) {
            return t == 0 || t == null ? '无赔偿' : t
          },
        },
        {
          title: '回收员实支付平台订单金额(元)',
          align: 'center',
          dataIndex: 'realRecyclerPlatformAmount',
        },
        {
          title: '顾客实支付平台订单金额(元)',
          align: 'center',
          dataIndex: 'realCustomerPlatformAmount',
        },

        {
          title: '订单备注信息',
          align: 'center',
          dataIndex: 'remark',
        },
        {
          title: '订单状态',
          align: 'center',
          dataIndex: 'status',
          customRender: function (t, r, index) {
            return '待顾客确定'
          },
        },
      ],
      url: {
        list: '/order/list',
        delete: '/order/delete',
        deleteBatch: '/order/deleteBatch',
        exportXlsUrl: '/order/exportXls',
        importExcelUrl: '/order/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      rejectOrderList: [],
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
          this.$message.success('已审批')
          this.initRejectOrderList()
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