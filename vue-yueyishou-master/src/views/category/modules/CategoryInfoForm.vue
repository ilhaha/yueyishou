<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryName">
              <a-input v-model="model.categoryName" placeholder="请输入分类名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上层分类ID，0表示顶级分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentId">
              <a-input-number v-model="model.parentId" placeholder="请输入上层分类ID，0表示顶级分类" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitPrice">
              <a-input-number v-model="model.unitPrice" placeholder="请输入单价" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unit">
              <a-input v-model="model.unit" placeholder="请输入单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分类描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-input v-model="model.description" placeholder="请输入分类描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态，1:正常 0:禁用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态，1:正常 0:禁用" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除标志（0未删除，1已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入删除标志（0未删除，1已删除）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'CategoryInfoForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
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
           categoryName: [
              { required: true, message: '请输入分类名称!'},
           ],
           status: [
              { required: true, message: '请输入状态，1:正常 0:禁用!'},
           ],
           isDeleted: [
              { required: true, message: '请输入删除标志（0未删除，1已删除）!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.category/categoryInfo/add",
          edit: "/com.ilhaha.yueyishou.category/categoryInfo/edit",
          queryById: "/com.ilhaha.yueyishou.category/categoryInfo/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>