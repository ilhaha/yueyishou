<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryName">
              <a-input v-model="model.categoryName" placeholder="请输入分类名称"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
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
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
export default {
  name: 'CategoryInfoForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {
        categoryName:'',
        icon: '',
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
        icon: [{ required: true, message: '请上传icon!' }],
      },
      url: {
        add: '/category/add',
        edit: '/category/edit',
        queryById: '/category/queryById',
      },
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
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
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
        this.fileList = [file] // 确保 fileList 也被更新
        this.loading = false
        this.$message.success('icon上传成功')
      } else if (file.status === 'error') {
        this.$message.error('icon上传失败')
        this.loading = false
      }
    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
  },
}
</script>