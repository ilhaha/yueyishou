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
      <a-button type="primary" icon="download" @click="handleExportXls('coupon_info')">导出</a-button>
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
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CouponInfoModal
    },
    data () {
      return {
        description: 'coupon_info管理页面',
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
            title:'服务费抵扣券类型 1 折扣 2 免单',
            align:"center",
            dataIndex: 'couponType'
          },
          {
            title:'服务费抵扣券名字',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title:'折扣：取值[1 到 10]',
            align:"center",
            dataIndex: 'discount'
          },
          {
            title:'使用门槛 0->没门槛',
            align:"center",
            dataIndex: 'conditionAmount'
          },
          {
            title:'每人限领张数，0-不限制 1-限领1张 2-限领2张...',
            align:"center",
            dataIndex: 'perLimit'
          },
          {
            title:'已使用数量',
            align:"center",
            dataIndex: 'useCount'
          },
          {
            title:'领取数量',
            align:"center",
            dataIndex: 'receiveCount'
          },
          {
            title:'过期时间',
            align:"center",
            dataIndex: 'expireTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'服务费抵扣券描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'状态[0-未发布，1-已发布， -1-已过期]',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'删除标记（0:未删除 1:已删除）',
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
          list: "/com.ilhaha.yueyishou.coupon/couponInfo/list",
          delete: "/com.ilhaha.yueyishou.coupon/couponInfo/delete",
          deleteBatch: "/com.ilhaha.yueyishou.coupon/couponInfo/deleteBatch",
          exportXlsUrl: "/com.ilhaha.yueyishou.coupon/couponInfo/exportXls",
          importExcelUrl: "com.ilhaha.yueyishou.coupon/couponInfo/importExcel",
          
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
        fieldList.push({type:'int',value:'couponType',text:'服务费抵扣券类型 1 折扣 2 免单'})
        fieldList.push({type:'string',value:'name',text:'服务费抵扣券名字'})
        fieldList.push({type:'number',value:'amount',text:'金额'})
        fieldList.push({type:'number',value:'discount',text:'折扣：取值[1 到 10]'})
        fieldList.push({type:'number',value:'conditionAmount',text:'使用门槛 0->没门槛'})
        fieldList.push({type:'int',value:'perLimit',text:'每人限领张数，0-不限制 1-限领1张 2-限领2张...'})
        fieldList.push({type:'int',value:'useCount',text:'已使用数量'})
        fieldList.push({type:'int',value:'receiveCount',text:'领取数量'})
        fieldList.push({type:'date',value:'expireTime',text:'过期时间'})
        fieldList.push({type:'string',value:'description',text:'服务费抵扣券描述'})
        fieldList.push({type:'int',value:'status',text:'状态[0-未发布，1-已发布， -1-已过期]'})
        fieldList.push({type:'int',value:'isDeleted',text:'删除标记（0:未删除 1:已删除）'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>