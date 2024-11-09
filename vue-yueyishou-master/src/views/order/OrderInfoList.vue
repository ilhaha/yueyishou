      <template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单状态">
              <a-select ref="select" v-model:value="queryParam.status" @change="searchQuery">
                <a-select-option value="1">待接单</a-select-option>
                <a-select-option value="2">待服务</a-select-option>
                <a-select-option value="3">待回收员确认</a-select-option>
                <a-select-option value="4">待顾客确认</a-select-option>
                <a-select-option value="5">待付款</a-select-option>
                <a-select-option value="6">待评价</a-select-option>
                <a-select-option value="7">已完成</a-select-option>
                <a-select-option value="8">已取消</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
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
        </a-row>
        <a-row :gutter="24">
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

    <div style="margin-top: 20px">
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">申请拒收订单列表</div>
    </div>

    <div>
      <a-table
        ref="rejectOrderTable"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="orderId"
        :columns="rejectColumns"
        :dataSource="rejectOrderList"
        :pagination="rejectOrderIpagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleRejectTableChange"
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
          <a @click="review(2, record)">通过申请</a>
          <a-divider type="vertical" />
          <a @click="review(-1, record)">驳回申请</a>
        </span>
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
        status: '1',
      },
      dateFormat: 'YYYY-MM-DD',
      description: 'order_info管理页面',
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
          title: '订单预计回收金额(元)',
          align: 'center',
          dataIndex: 'estimatedTotalAmount',
        },
        {
          title: '顾客预回收收入金额(元)',
          align: 'center',
          customRender: function (t, r, index) {
            return r.estimatedTotalAmount - r.expectCustomerPlatformAmount
          },
        },
        {
          title: '回收员预回收支出金额(元)',
          align: 'center',
          customRender: function (t, r, index) {
            return r.estimatedTotalAmount + r.expectRecyclerPlatformAmount
          },
        },
        {
          title: '顾客预支付平台订单金额(元)',
          align: 'center',
          dataIndex: 'expectCustomerPlatformAmount',
        },
        {
          title: '回收员预支付平台订单金额(元)',
          align: 'center',
          dataIndex: 'expectRecyclerPlatformAmount',
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
            return '待接单'
          },
        },
      ],
      rejectColumns: [
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
      rejectOrderIpagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
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
    this.initRejectOrderList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  watch: {
    'queryParam.status': {
      handler(newVal) {
        if (newVal == 1) {
          this.columns = [
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
              title: '订单预计回收金额(元)',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '顾客预回收收入金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount - r.expectCustomerPlatformAmount
              },
            },
            {
              title: '回收员预回收支出金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount + r.expectRecyclerPlatformAmount
              },
            },
            {
              title: '顾客预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '回收员预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
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
                return '待接单'
              },
            },
          ]
        } else if (newVal == 2) {
          this.columns = [
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
              title: '订单预计回收金额(元)',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '顾客预回收收入金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount - r.expectCustomerPlatformAmount
              },
            },
            {
              title: '回收员预回收支出金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount + r.expectRecyclerPlatformAmount
              },
            },
            {
              title: '顾客预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '回收员预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
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
                return '待服务'
              },
            },
          ]
        } else if (newVal == 3) {
          this.columns = [
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
              title: '订单预计回收金额(元)',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '顾客预回收收入金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount - r.expectCustomerPlatformAmount + (r.recyclerOvertimeCharges || 0)
              },
            },
            {
              title: '回收员预回收支出金额(元)',
              align: 'center',
              customRender: function (t, r, index) {
                return r.estimatedTotalAmount + r.expectRecyclerPlatformAmount + (r.recyclerOvertimeCharges || 0)
              },
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
              title: '顾客预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '回收员预支付平台订单金额(元)',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
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
                return '待回收员确定'
              },
            },
          ]
        } else if (newVal == 4) {
          this.columns = [
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
          ]
        } else if (newVal == 5) {
          this.columns = [
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
              dataIndex: 'realCustomerRecycleAmount',
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
              title: '回收员服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'recyclerCouponAmount',
            },
            {
              title: '顾客服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'customerCouponAmount',
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
                return '待付款'
              },
            },
          ]
        } else if (newVal == 6) {
          this.columns = [
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
              title: '付款时间',
              align: 'center',
              dataIndex: 'payTime',
            },
            {
              title: '订单实回收金额(元)',
              align: 'center',
              dataIndex: 'realOrderRecycleAmount',
            },
            {
              title: '顾客实回收收入金额(元)',
              align: 'center',
              dataIndex: 'realCustomerRecycleAmount',
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
              title: '回收员服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'recyclerCouponAmount',
            },
            {
              title: '顾客服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'customerCouponAmount',
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
                return '待评价'
              },
            },
          ]
        } else if (newVal == 7) {
          this.columns = [
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
              title: '付款时间',
              align: 'center',
              dataIndex: 'payTime',
            },
            {
              title: '评价时间',
              align: 'center',
              dataIndex: 'commentTime',
            },
            {
              title: '评分',
              align: 'center',
              dataIndex: 'rate',
            },
            {
              title: '评价内容',
              align: 'center',
              dataIndex: 'reviewContent',
            },
            {
              title: '订单实回收金额(元)',
              align: 'center',
              dataIndex: 'realOrderRecycleAmount',
            },
            {
              title: '顾客实回收收入金额(元)',
              align: 'center',
              dataIndex: 'realCustomerRecycleAmount',
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
              title: '回收员服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'recyclerCouponAmount',
            },
            {
              title: '顾客服务抵扣劵抵扣金额(元)',
              align: 'center',
              dataIndex: 'customerCouponAmount',
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
                return '已完成'
              },
            },
          ]
        } else if (newVal == 8) {
          this.columns = [
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
              title: '订单备注信息',
              align: 'center',
              dataIndex: 'remark',
            },
            {
              title: '取消时间',
              align: 'center',
              dataIndex: 'cancelTime',
            },
            {
              title: '顾客短时取消赔偿(元)',
              align: 'center',
              dataIndex: 'customerLateCancellationFee',
              customRender: function (t, r, index) {
                return r.customerLateCancellationFee || '无赔偿'
              },
            },
            {
              title: '回收员短时取消赔偿(元)',
              align: 'center',
              dataIndex: 'recyclerLateCancellationFee',
              customRender: function (t, r, index) {
                return r.recyclerLateCancellationFee || '无赔偿'
              },
            },
            {
              title: '回收员超时未服务取消赔偿(元)',
              align: 'center',
              dataIndex: 'serviceOvertimePenalty',
              customRender: function (t, r, index) {
                return r.serviceOvertimePenalty || '无赔偿'
              },
            },
            {
              title: '顾客支付被拒收订单赔偿(元)',
              align: 'center',
              dataIndex: 'rejectCompensation',
              customRender: function (t, r, index) {
                return r.rejectCompensation || '无赔偿'
              },
            },

            {
              title: '取消备注信息',
              align: 'center',
              dataIndex: 'cancelMessage',
            },
            {
              title: '订单状态',
              align: 'center',
              dataIndex: 'status',
              customRender: function (t, r, index) {
                return '已取消'
              },
            },
          ]
        }
      },
    },
  },
  methods: {
    // 分页查询
    handleRejectTableChange(pagination) {
      this.rejectOrderIpagination.current = pagination.current
      this.rejectOrderIpagination.pageSize = pagination.pageSize
      this.initRejectOrderList(this.rejectOrderIpagination.current, this.rejectOrderIpagination.pageSize)
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
          this.$message.success('已审批')
          this.initRejectOrderList()
        }
      })
    },
    // 获取申请拒收订单列表
    initRejectOrderList(pageNo = 1, pageSize = 10) {
      postAction(`/order/reject/list?pageNo=${pageNo}&pageSize=${pageSize}`).then((res) => {
        this.rejectOrderList = res.result.records
        this.rejectOrderIpagination.total = res.result.total
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