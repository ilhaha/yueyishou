<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"> </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="switchStatus(1, selectedRowKeys)"><a-icon type="check" />启用</a-menu-item>
          <a-menu-item key="2" @click="switchStatus(2, selectedRowKeys)"><a-icon type="close" />停用</a-menu-item>
          <a-menu-item key="3" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
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
        :pagination="false"
        :dataSource="dataSource"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无icon</span>
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
          <span v-if="record.parentId == 0">
            <a @click="openAdd(record)">添加下级</a>
            <a-divider type="vertical" />
          </span>
          <a @click="handleEdit(record)" v-if="record.parentId == 0">编辑</a>
          <a @click="openUpdate(record)" v-else>编辑</a>

          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <a-modal
      v-model:open="addSonOpen"
      width="60%"
      title="添加子品类"
      @ok="addSon"
      @cancel="clearData()"
      :maskClosable="false"
    >
      <j-form-container>
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryName">
                <a-input v-model="model.categoryName" placeholder="请输入分类名称"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="icon" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="icon">
                <a-upload
                  v-model:file-list="fileList"
                  name="file"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :show-upload-list="false"
                  :action="uploadUrl"
                  :before-upload="beforeUpload"
                  @change="handleChange"
                  :headers="uploadHeaders"
                  :data="uploadData"
                >
                  <img
                    v-if="model.icon"
                    :src="imageUrl ? imageUrl : model.icon"
                    alt="icon"
                    style="width: 100%; height: 100%; object-fit: cover"
                  />
                  <div v-else>
                    <div class="ant-upload-text">上传icon</div>
                  </div>
                </a-upload>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitPrice">
                <a-input-number v-model="model.unitPrice" placeholder="请输入单价" style="width: 100%" />
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="品类描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryDescribe">
                <a-input v-model="model.categoryDescribe" placeholder="请输入品类描述"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </j-form-container>
    </a-modal>

    <a-modal
      v-model:open="updateSonOpen"
      title="修改子品类"
      @ok="updateSon"
      @cancel="updateClearData()"
      :maskClosable="false"
      width="60%"
    >
      <j-form-container>
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryName">
                <a-input v-model="model.categoryName" placeholder="请输入分类名称"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="icon" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="icon">
                <a-upload
                  v-model:file-list="fileList"
                  name="file"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :show-upload-list="false"
                  :action="uploadUrl"
                  :before-upload="beforeUpload"
                  @change="handleChange"
                  :headers="uploadHeaders"
                  :data="uploadData"
                >
                  <img
                    v-if="model.icon"
                    :src="imageUrl ? imageUrl : model.icon"
                    alt="icon"
                    style="width: 100%; height: 100%; object-fit: cover"
                  />
                  <div v-else>
                    <div class="ant-upload-text">上传icon</div>
                  </div>
                </a-upload>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitPrice">
                <a-input-number v-model="model.unitPrice" placeholder="请输入单价" style="width: 100%" />
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="品类描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryDescribe">
                <a-input v-model="model.categoryDescribe" placeholder="请输入品类描述"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </j-form-container>
    </a-modal>
    <category-info-modal ref="modalForm" @ok="modalFormOk"></category-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import CategoryInfoModal from './modules/CategoryInfoModal'
import { getAction, postAction, putAction } from '../../api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
export default {
  name: 'CategoryInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    CategoryInfoModal,
  },
  data() {
    return {
      updateModelBack: {
        categoryName: '',
        parentId: 0,
        unitPrice: '',
        unit: '',
        icon: '',
        categoryDescribe: '',
      },
      updateSonOpen: false,
      model: {
        categoryName: '',
        parentId: 0,
        unitPrice: '',
        unit: '',
        icon: '',
        categoryDescribe: '',
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        categoryName: [{ required: true, message: '请输入分类名称!' }],
        parentId: [{ required: true, message: '请输入上层分类ID，0表示顶级分类!' }],
        unitPrice: [{ required: true, message: '请输入单价!' }],
        icon: [{ required: true, message: '请上传icon!' }],
      },
      addSonOpen: false,
      description: 'category_info管理页面',
      // 表头
      columns: [
        {
          title: '品类名称',
          align: 'center',
          dataIndex: 'categoryName',
        },
        {
          title: 'icon',
          align: 'center',
          width: 150,
          dataIndex: 'icon',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '单价',
          align: 'center',
          dataIndex: 'unitPrice',
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'unit',
        },
        {
          title: '品类描述',
          align: 'center',
          dataIndex: 'categoryDescribe',
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
        list: '/category/list',
        delete: '/category/delete',
        deleteBatch: '/category/deleteBatch',
        exportXlsUrl: '/category/exportXls',
        importExcelUrl: 'category/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
      uploadUrl: `${process.env.VUE_APP_API_BASE_URL}/cos/upload`, // 替换为实际上传图片的API地址
      imageUrl: '', // 用于存储上传后的图片URL
      fileList: [], // 文件列表
      loading: false, // 用于控制loading效果
      uploadHeaders: {
        'x-access-token': Vue.ls.get(ACCESS_TOKEN), // 替换为实际的认证令牌
      },
      uploadData: { path: 'category' }, // 额外的上传参数
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
    beforeUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('只能上传 JPG/PNG 格式的icon!')
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('icon大小必须小于 2MB!')
        return false
      }
      this.loading = true
      return true
    },
    handleChange(info) {
      const { file } = info
      if (file.status === 'done') {
        // 成功上传后，获取图片URL
        this.imageUrl = info.file.response.result.showUrl
        this.model.icon = info.file.response.result.url
        this.loading = false
        this.$message.success('icon上传成功')
      } else if (file.status === 'error') {
        this.$message.error('icon上传失败')
        this.loading = false
      }
    },
    switchStatus(status, categoryIds) {
      postAction('/category/switch/status', { categoryIds, status }).then((res) => {
        this.$message.success(res.message)
        this.loadData()
      })
    },
    handleStatusChange(checked, record) {
      const newStatus = checked ? 1 : 2
      let categoryIds = []
      categoryIds.push(record.id)
      this.switchStatus(newStatus, categoryIds)
    },
    openUpdate(item) {
      this.updateSonOpen = true
      this.model = item
      this.updateModelBack = this.model
    },
    updateSon() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          putAction('/category/edit', this.model).then((res) => {
            if (res.success) {
              this.$message.success('修改成功')
              this.updateSonOpen = false
              this.updateClearData()
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      })
    },
    openAdd(item) {
      if (item.status == 2) {
        this.$message.warning('禁用状态不能添加子品类')
        return
      }
      this.addSonOpen = true
      this.model.parentId = item.id
    },
    addSon() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          postAction('/category/add', this.model).then((res) => {
            if (res.success) {
              this.$message.success('添加成功')
              this.addSonOpen = false
              this.clearData()
              this.loadData()
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      })
    },
    clearData() {
      this.model.categoryName = ''
      this.model.parentId = 0
      this.model.unitPrice = ''
      this.model.unit = ''
      this.model.categoryDescribe = ''
      this.model.icon = ''
    },
    updateClearData() {
      this.clearData()
      this.loadData()
      this.model.id = ''
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'categoryName', text: '分类名称', dictCode: '' })
      fieldList.push({ type: 'int', value: 'parentId', text: '上层分类ID，0表示顶级分类', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'unitPrice', text: '单价', dictCode: '' })
      fieldList.push({ type: 'string', value: 'unit', text: '单位', dictCode: '' })
      fieldList.push({ type: 'int', value: 'status', text: '状态，1:正常 2:禁用', dictCode: '' })
      fieldList.push({ type: 'string', value: 'categoryDescribe', text: '描述', dictCode: '' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>