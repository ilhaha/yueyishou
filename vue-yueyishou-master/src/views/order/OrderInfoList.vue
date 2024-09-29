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
            <a-form-item label="订单状态">
              <a-select ref="select" v-model:value="queryParam.status" @change="searchQuery">
                <a-select-option value="1">待接单</a-select-option>
                <a-select-option value="2">已接单</a-select-option>
                <a-select-option value="3">前往回收点</a-select-option>
                <a-select-option value="4">待确认</a-select-option>
                <a-select-option value="5">未付款</a-select-option>
                <a-select-option value="6">已完成</a-select-option>
                <a-select-option value="7">待评价</a-select-option>
                <a-select-option value="8">已取消</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单号">
              <a-input placeholder="请输入订单号" v-model="queryParam.orderNo"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="queryParam.status != 1">
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

    <order-info-modal ref="modalForm" @ok="modalFormOk"></order-info-modal>
  </a-card>
</template>

      <script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import OrderInfoModal from './modules/OrderInfoModal'
import dayjs from 'dayjs'
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
          title: '订单号',
          align: 'center',
          dataIndex: 'orderNo',
        },
        {
          title: '订单回收分类',
          align: 'center',
          dataIndex: 'categoryName',
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
          title: '客户地点',
          align: 'center',
          dataIndex: 'customerLocation',
        },
        {
          title: '预约回收时间',
          align: 'center',
          dataIndex: 'appointmentTime',
          customRender: function (text, record) {
            let startTime = this.formatDate(text)
            let endTime = this.formatDate(record.appointmentEndTime)
            console.log(endTime)

            return startTime + ' ~ ' + endTime.substring(11, 16)
          }.bind(this),
        },
        {
          title: '订单预计回收总金额',
          align: 'center',
          dataIndex: 'estimatedTotalAmount',
        },
        {
          title: '订单预计回收总金额',
          align: 'center',
          dataIndex: 'expectRecyclerPlatformAmount',
        },
        {
          title: '顾客预支付平台订单金额',
          align: 'center',
          dataIndex: 'expectCustomerPlatformAmount',
        },
        {
          title: '订单状态',
          align: 'center',
          dataIndex: 'status',
        },
        // {
        //   title: '取消订单信息',
        //   align: 'center',
        //   dataIndex: 'cancelMessage',
        // },
        {
          title: '订单备注信息',
          align: 'center',
          dataIndex: 'remark',
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
        list: '/order/list',
        delete: '/order/delete',
        deleteBatch: '/order/deleteBatch',
        exportXlsUrl: '/order/exportXls',
        importExcelUrl: '/order/importExcel',
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
              title: '订单号',
              align: 'center',
              dataIndex: 'orderNo',
            },
            {
              title: '订单回收分类',
              align: 'center',
              dataIndex: 'categoryName',
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
              title: '客户地点',
              align: 'center',
              dataIndex: 'customerLocation',
            },
            {
              title: '预约回收时间',
              align: 'center',
              dataIndex: 'appointmentTime',
              customRender: function (text, record) {
                let startTime = this.formatDate(text)
                let endTime = this.formatDate(record.appointmentEndTime)
                console.log(endTime)

                return startTime + ' ~ ' + endTime.substring(11, 16)
              }.bind(this),
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
            },
            {
              title: '顾客预支付平台订单金额',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '订单状态',
              align: 'center',
              dataIndex: 'status',
            },
            {
              title: '订单备注信息',
              align: 'center',
              dataIndex: 'remark',
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: 'center',
              fixed: 'right',
              width: 147,
              scopedSlots: { customRender: 'action' },
            },
          ]
        } else if (newVal == 2 || newVal == 3) {
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
              title: '订单号',
              align: 'center',
              dataIndex: 'orderNo',
            },
            {
              title: '订单回收分类',
              align: 'center',
              dataIndex: 'categoryName',
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
              title: '客户地点',
              align: 'center',
              dataIndex: 'customerLocation',
            },
            {
              title: '预约回收时间',
              align: 'center',
              dataIndex: 'appointmentTime',
              customRender: function (text, record) {
                let startTime = this.formatDate(text)
                let endTime = this.formatDate(record.appointmentEndTime)
                console.log(endTime)

                return startTime + ' ~ ' + endTime.substring(11, 16)
              }.bind(this),
            },
            {
              title: '回收员姓名',
              align: 'center',
              dataIndex: 'recyclerName',
            },
            {
              title: '回收员接单时间',
              align: 'center',
              dataIndex: 'acceptTime',
              customRender: function (text) {
                return this.formatDate(text)
              }.bind(this),
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
            },
            {
              title: '顾客预支付平台订单金额',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '订单状态',
              align: 'center',
              dataIndex: 'status',
            },
            {
              title: '订单备注信息',
              align: 'center',
              dataIndex: 'remark',
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: 'center',
              fixed: 'right',
              width: 147,
              scopedSlots: { customRender: 'action' },
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
              title: '订单号',
              align: 'center',
              dataIndex: 'orderNo',
            },
            {
              title: '订单回收分类',
              align: 'center',
              dataIndex: 'categoryName',
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
              title: '客户地点',
              align: 'center',
              dataIndex: 'customerLocation',
            },
            {
              title: '预约回收时间',
              align: 'center',
              dataIndex: 'appointmentTime',
              customRender: function (text, record) {
                let startTime = this.formatDate(text)
                let endTime = this.formatDate(record.appointmentEndTime)
                console.log(endTime)

                return startTime + ' ~ ' + endTime.substring(11, 16)
              }.bind(this),
            },
            {
              title: '回收员姓名',
              align: 'center',
              dataIndex: 'recyclerName',
            },
            {
              title: '回收员接单时间',
              align: 'center',
              dataIndex: 'acceptTime',
              customRender: function (text) {
                return this.formatDate(text)
              }.bind(this),
            },
            {
              title: '回收员到达时间',
              align: 'center',
              dataIndex: 'arriveTime',
              customRender: function (text) {
                return this.formatDate(text)
              }.bind(this),
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'estimatedTotalAmount',
            },
            {
              title: '订单预计回收总金额',
              align: 'center',
              dataIndex: 'expectRecyclerPlatformAmount',
            },
            {
              title: '顾客预支付平台订单金额',
              align: 'center',
              dataIndex: 'expectCustomerPlatformAmount',
            },
            {
              title: '订单状态',
              align: 'center',
              dataIndex: 'status',
            },
            {
              title: '订单备注信息',
              align: 'center',
              dataIndex: 'remark',
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: 'center',
              fixed: 'right',
              width: 147,
              scopedSlots: { customRender: 'action' },
            },
          ]
        }
        // if (newVal != 1) {
        //   // 要插入的列
        //   const newColumns = [
        //     {
        //       title: '回收员姓名',
        //       align: 'center',
        //       dataIndex: 'recyclerName',
        //     },
        //     {
        //       title: '回收员接单时间',
        //       align: 'center',
        //       dataIndex: 'acceptTime',
        //       customRender: function (text) {
        //         return this.formatDate(text)
        //       }.bind(this),
        //     },
        //     {
        //       title: '回收员到达时间',
        //       align: 'center',
        //       dataIndex: 'arriveTime',
        //       customRender: function (text) {
        //         return this.formatDate(text)
        //       }.bind(this),
        //     },
        //   ]

        //   // 移除现有的同名列
        //   this.columns = this.columns.filter((column) => {
        //     return !['recyclerName', 'acceptTime', 'arriveTime'].includes(column.dataIndex)
        //   })

        //   // 找到要插入的位置（在预约回收时间之后）
        //   const insertIndex = this.columns.findIndex((column) => column.dataIndex === 'appointmentStartTime') + 1

        //   // 使用 splice 在特定位置插入新列
        //   this.columns.splice(insertIndex, 0, ...newColumns)
        // } else {
        //   const filteredColumns = this.columns.filter((column) => {
        //     // 判断条件：如果是回收员姓名、回收员接单时间或回收员到达时间列则排除掉
        //     return !['recyclerName', 'acceptTime', 'arriveTime'].includes(column.dataIndex)
        //   })
        //   this.columns = filteredColumns
        // }
      },
    },
  },
  methods: {
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
      fieldList.push({ type: 'int', value: 'categoryId', text: '订单回收分类ID', dictCode: '' })
      fieldList.push({ type: 'int', value: 'recyclerId', text: '回收员ID', dictCode: '' })
      fieldList.push({ type: 'date', value: 'acceptTime', text: '回收员接单时间' })
      fieldList.push({ type: 'date', value: 'appointmentTime', text: '预约时间' })
      fieldList.push({ type: 'date', value: 'arriveTime', text: '回收员到达时间' })
      fieldList.push({ type: 'int', value: 'status', text: '订单状态', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cancelMessage', text: '取消订单信息', dictCode: '' })
      fieldList.push({ type: 'string', value: 'remark', text: '订单备注信息', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'estimatedTotalAmount', text: '订单预计回收总金额', dictCode: '' })
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