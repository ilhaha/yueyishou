<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
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
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
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

          <a-menu-item key="1" @click="switchStatus(1, selectedRowKeys)" v-if="queryParam.authStatus == '2'"
            ><a-icon type="check" />启用</a-menu-item
          >
          <a-menu-item key="2" @click="switchStatus(2, selectedRowKeys)" v-if="queryParam.authStatus == '2'"
            ><a-icon type="close" />停用</a-menu-item
          >
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
          <a @click="showExamineDialog(2, record)" v-if="record.authStatus == 1">通过审核</a>
          <a-divider type="vertical" v-if="record.authStatus == 1" />
          <a @click="showExamineDialog(-1, record)" v-if="record.authStatus == 1">未过审核</a>
          <a @click="showExamineDialog(1, record)" v-if="record.authStatus == 2">重新审核</a>
          <!-- <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)" v-if="record.authStatus == -1">
            <a>删除</a>
          </a-popconfirm> -->
          <a-divider type="vertical" v-if="record.authStatus != -1" />
          <a @click="showExamineOperateList(record)">审核操作日志</a>
          <a-divider type="vertical" />
          <a @click="showOperateList(record)">账号操作日志</a>
        </span>
      </a-table>
    </div>

    <a-modal
      v-model:open="examineDialogVisible"
      :title="examineTitle"
      @ok="examineConfirm"
      width="60%"
      @cancel="clearData"
    >
      <j-form-container>
        <a-form-model ref="examineForm" :model="examineModel" :rules="examineValidatorRules" slot="detail">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="审核理由" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reason">
                <a-textarea v-model="examineModel.reason" placeholder="请输入审核理由"></a-textarea>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="佐证材料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="proof">
                <a-upload
                  :key="examineUploadKey"
                  v-model:file-list="examineFileList"
                  :remove="examineHandleRemove"
                  name="file"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :action="uploadUrl"
                  :before-upload="beforeExamineUpload"
                  @change="handleExamineChange"
                  :headers="uploadHeaders"
                  :data="uploadData"
                  :multiple="true"
                >
                  <div v-if="examineFileList.length < 3">
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
      v-model:open="examineOperateListDialogVisible"
      title="回收员认证审核操作日志列表"
      width="80%"
      :footer="null"
      @cancel="examineOperateListClearData"
    >
      <j-form-container>
        <a-table
          ref="operateList"
          size="middle"
          :scroll="{ x: true }"
          bordered
          rowKey="id"
          :columns="examineOperateListColumns"
          :dataSource="examineOperateList"
          :pagination="examineOperateListPagination"
          :loading="loading"
          class="j-table-force-nowrap"
          @change="handleExamineOperateListChange"
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
      title="回收员账号操作日志列表"
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

    <recycler-info-modal ref="modalForm" @ok="modalFormOk"></recycler-info-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import RecyclerInfoModal from './modules/RecyclerInfoModal'
import { getAction, postAction, putAction } from '../../api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
export default {
  name: 'RecyclerInfoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    RecyclerInfoModal,
  },
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
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
          title: '正脸照',
          align: 'center',
          width: 150,
          dataIndex: 'fullFaceUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '人脸模型',
          align: 'center',
          width: 150,
          dataIndex: 'faceRecognitionUrl',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          customRender: (text, record) => {
            const isChecked = text == '1'
            const isDisabled = record.authStatus != 2
            return (
              <a-space direction="vertical">
                <a-switch
                  checked={isChecked}
                  disabled={isDisabled}
                  checked-children="启用"
                  un-checked-children="禁用"
                  onChange={(checked) => this.switchStatusDialogVisible(checked, record)}
                />
              </a-space>
            )
          },
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
      examineOperateListColumns: [
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
          title: '操作类型',
          align: 'center',
          dataIndex: 'examineStatus',
          customRender: function (text) {
            return text == '1' ? '重新认证' : text == '2' ? '认证通过' : '认证未通过'
          },
        },
        {
          title: '操作时间',
          align: 'center',
          dataIndex: 'createTime',
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
      examineDialogVisible: false,
      examineTitle: '提交回收员认证通过操作记录',
      examineModel: {
        proof: '',
        reason: '',
        examineStatus: '',
        recyclerId: '',
      },
      examineValidatorRules: {
        proof: [{ required: true, message: '请上传佐证材料!' }],
        reason: [{ required: true, message: '请输入审核理由!' }],
      },
      examineRecycler: {},
      examineStatus: '',
      uploadUrl: `${process.env.VUE_APP_API_BASE_URL}/cos/upload`, // 替换为实际上传图片的API地址
      imageUrl: '', // 用于存储上传后的图片URL
      fileList: [], // 文件列表
      examineFileList: [],
      loading: false, // 用于控制loading效果
      uploadHeaders: {
        'x-access-token': Vue.ls.get(ACCESS_TOKEN), // 替换为实际的认证令牌
      },
      uploadData: { path: 'recycler' },
      uploadKey: Date.now(),
      examineUploadKey: Date.now(),
      examineOperateList: [],
      examineOperateListDialogVisible: false,
      examineOperateListPagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showSizeChanger: true,
        total: 0,
      },
      statusDialogVisible: false,
      operateRecycler: {},
      operateStatus: '',
      statusTitle: '提交禁用回收员账号操作记录',
      model: {
        proof: '',
        reason: '',
      },
      validatorRules: {
        proof: [{ required: true, message: '请上传佐证材料!' }],
        reason: [{ required: true, message: '请输入操作原因!' }],
      },
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
      operateList: [],
      operateTableDialogVisible: false,
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
    examineHandleRemove(file) {
      // 确保过滤掉被删除的文件
      this.examineFileList = this.examineFileList.filter((item) => item.uid !== file.uid)

      // 更新 model.proof，仅包含未被删除的文件的 URL
      this.examineModel.proof = this.examineFileList
        .map(function (f) {
          return (f.response && f.response.result && f.response.result.url) || f.url
        })
        .join(',')
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
        this.operateRecycler,
        this.operateListPagination.current,
        this.operateListPagination.pageSize
      )
    },
    // 获取操作日志列表
    showOperateList(recycler, pageNo = 1, pageSize = 10) {
      this.operateRecycler = recycler
      getAction(`/recycler/account/list?recyclerId=${recycler.id}&pageNo=${pageNo}&pageSize=${pageSize}`).then(
        (res) => {
          this.operateList = res.result.records
          this.operateTableDialogVisible = true
          this.operateListPagination.total = res.result.total
        }
      )
    },
    // 提交操作日志
    confirm() {
      console.log(this.model.proof)

      this.$refs.form.validate((valid) => {
        if (valid) {
          this.model.recyclerId = this.operateRecycler.id
          postAction('/recycler/account/operate/add', this.model).then((res) => {
            if (res.success) {
              this.$message.success('已提交')
              this.statusDialogVisible = false
              this.clearData()
              this.handleStatusChange(this.operateStatus, this.operateRecycler)
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      })
    },
    switchStatusDialogVisible(checked, record) {
      this.statusDialogVisible = !this.statusDialogVisible
      this.operateRecycler = record
      this.operateStatus = checked
      if (checked) {
        this.statusTitle = '提交启用回收员账号操作记录'
      } else {
        this.statusTitle = '提交禁用回收员账号操作记录'
      }
    },
    // 关闭提交回收员审核操作记录列表时初始化分页控件
    examineOperateListClearData() {
      this.examineOperateListPagination = {
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
    // 切换提交回收员审核操作记录分页
    handleExamineOperateListChange(pagination) {
      this.examineOperateListPagination.current = pagination.current
      this.examineOperateListPagination.pageSize = pagination.pageSize
      this.showExamineOperateList(
        this.examineRecycler,
        this.examineOperateListPagination.current,
        this.examineOperateListPagination.pageSize
      )
    },
    // 查看提交回收员审核操作记录
    showExamineOperateList(recycler, pageNo = 1, pageSize = 10) {
      this.examineRecycler = recycler
      getAction(`/recycler/examine/list?recyclerId=${recycler.id}&pageNo=${pageNo}&pageSize=${pageSize}`).then(
        (res) => {
          this.examineOperateList = res.result.records
          this.examineOperateListDialogVisible = true
          this.examineOperateListPagination.total = res.result.total
        }
      )
    },
    // 提交回收员审核操作记录
    examineConfirm() {
      this.$refs.examineForm.validate((valid) => {
        if (valid) {
          this.examineModel.recyclerId = this.examineRecycler.id
          this.examineModel.examineStatus = this.examineStatus
          postAction('/recycler/examine/operate/add', this.examineModel).then((res) => {
            if (res.success) {
              this.$message.success('已提交')
              this.examineDialogVisible = false
              this.clearData()
              this.review(this.examineStatus, this.examineRecycler)
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      })
    },
    // 打开提交回收员审核操作记录弹窗
    showExamineDialog(authStatus, record) {
      this.examineDialogVisible = true
      this.examineRecycler = record
      this.examineStatus = authStatus
      if (authStatus == 2) {
        this.examineTitle = '提交回收员认证通过操作记录'
      } else if (authStatus == 1) {
        this.examineTitle = '提交回收员需重新认证通过操作记录'
      } else if (authStatus == -1) {
        this.examineTitle = '提交回收员认证未通过操作记录'
      }
    },
    // 清空数据
    clearData() {
      this.fileList = []
      this.examineFileList = []
      this.uploadKey = Date.now()
      this.examineUploadKey = Date.now()
      this.examineModel.proof = ''
      this.examineModel.reason = ''
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
    beforeExamineUpload(file) {
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
      if (this.examineFileList.length >= 3) {
        this.$message.error('最多上传三张图片!')
        return false
      }
      this.loading = true
      return true
    },
    handleExamineChange(info) {
      const { file } = info
      if (file.status === 'done') {
        // 成功上传后，获取图片URL
        const { response } = file
        if (response && response.result) {
          this.examineFileList = [...this.examineFileList, { ...file, url: response.result.showUrl }]

          this.examineModel.proof = this.examineFileList.map((f) => f.response.result.url).join(',') // 保存多个图片路径
          this.$message.success('图片上传成功')
        }
        this.loading = false
      } else if (file.status === 'error') {
        this.$message.error('图片上传失败')
        this.loading = false
      }
    },
    review(authStatus, record) {
      let recyclerIds = []
      recyclerIds.push(record.id)
      this.batchReview(authStatus, recyclerIds)
    },
    batchReview(authStatus, recyclerIds) {
      postAction('/recycler/auth', { recyclerIds: recyclerIds, authStatus: authStatus }).then((res) => {
        // this.$message.success(res.message)
        this.loadData()
      })
    },
    switchStatus(status, ids) {
      postAction('/recycler/switch/status', { recyclerIds: ids, status: status }).then((res) => {
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