<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="回收员ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclerId">
              <a-input-number v-model="model.recyclerId" placeholder="请输入回收员ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收类型，多个使用逗号隔开" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclingType">
              <a-input v-model="model.recyclingType" placeholder="请输入回收类型，多个使用逗号隔开"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="服务状态 1：开始接单 0：未接单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serviceStatus">
              <a-input-number v-model="model.serviceStatus" placeholder="请输入服务状态 1：开始接单 0：未接单" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接单里程设置" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="acceptDistance">
              <a-input-number v-model="model.acceptDistance" placeholder="请输入接单里程设置" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否自动接单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isAutoAccept">
              <a-input-number v-model="model.isAutoAccept" placeholder="请输入是否自动接单" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isDeleted" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入isDeleted" style="width: 100%" />
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
    name: 'RecyclerPersonalizationForm',
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
           recyclerId: [
              { required: true, message: '请输入回收员ID!'},
           ],
           recyclingType: [
              { required: true, message: '请输入回收类型，多个使用逗号隔开!'},
           ],
           serviceStatus: [
              { required: true, message: '请输入服务状态 1：开始接单 0：未接单!'},
           ],
           acceptDistance: [
              { required: true, message: '请输入接单里程设置!'},
           ],
           isAutoAccept: [
              { required: true, message: '请输入是否自动接单!'},
           ],
           isDeleted: [
              { required: true, message: '请输入isDeleted!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.recycler/recyclerPersonalization/add",
          edit: "/com.ilhaha.yueyishou.recycler/recyclerPersonalization/edit",
          queryById: "/com.ilhaha.yueyishou.recycler/recyclerPersonalization/queryById"
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