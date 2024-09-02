<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('order_info')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
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

  export default {
    name: 'OrderInfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      OrderInfoModal
    },
    data () {
      return {
        description: 'order_info管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'客户ID',
            align:"center",
            dataIndex: 'customerId'
          },
          {
            title:'订单号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'客户地点',
            align:"center",
            dataIndex: 'customerLocation'
          },
          {
            title:'客户地点经度',
            align:"center",
            dataIndex: 'customerPointLongitude'
          },
          {
            title:'客户地点伟度',
            align:"center",
            dataIndex: 'customerPointLatitude'
          },
          {
            title:'订单回收分类ID',
            align:"center",
            dataIndex: 'categoryId'
          },
          {
            title:'回收员ID',
            align:"center",
            dataIndex: 'recyclerId'
          },
          {
            title:'回收员接单时间',
            align:"center",
            dataIndex: 'acceptTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'回收员到达时间',
            align:"center",
            dataIndex: 'arriveTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'订单状态：1等待接单，2回收员已接单，3回收员已到达，4顾客确定订单，5回收员未付款，6回收员已付款，8订单已完成，9已取消',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'取消订单信息',
            align:"center",
            dataIndex: 'cancelMessage'
          },
          {
            title:'订单备注信息',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'isDeleted',
            align:"center",
            dataIndex: 'isDeleted'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/com.ilhaha.yueyishou.order/orderInfo/list",
          delete: "/com.ilhaha.yueyishou.order/orderInfo/delete",
          deleteBatch: "/com.ilhaha.yueyishou.order/orderInfo/deleteBatch",
          exportXlsUrl: "/com.ilhaha.yueyishou.order/orderInfo/exportXls",
          importExcelUrl: "com.ilhaha.yueyishou.order/orderInfo/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'customerId',text:'客户ID'})
        fieldList.push({type:'string',value:'orderNo',text:'订单号'})
        fieldList.push({type:'string',value:'customerLocation',text:'客户地点'})
        fieldList.push({type:'number',value:'customerPointLongitude',text:'客户地点经度'})
        fieldList.push({type:'number',value:'customerPointLatitude',text:'客户地点伟度'})
        fieldList.push({type:'int',value:'categoryId',text:'订单回收分类ID'})
        fieldList.push({type:'int',value:'recyclerId',text:'回收员ID'})
        fieldList.push({type:'date',value:'acceptTime',text:'回收员接单时间'})
        fieldList.push({type:'date',value:'arriveTime',text:'回收员到达时间'})
        fieldList.push({type:'int',value:'status',text:'订单状态：1等待接单，2回收员已接单，3回收员已到达，4顾客确定订单，5回收员未付款，6回收员已付款，8订单已完成，9已取消'})
        fieldList.push({type:'string',value:'cancelMessage',text:'取消订单信息'})
        fieldList.push({type:'string',value:'remark',text:'订单备注信息'})
        fieldList.push({type:'int',value:'isDeleted',text:'isDeleted'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>