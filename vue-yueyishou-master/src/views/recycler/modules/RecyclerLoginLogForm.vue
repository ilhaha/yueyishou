<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="回收员id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclerId">
              <a-input v-model="model.recyclerId" placeholder="请输入回收员id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="登录IP地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ipaddr">
              <a-input v-model="model.ipaddr" placeholder="请输入登录IP地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="登录状态（0成功 1失败）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入登录状态（0成功 1失败）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="提示信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msg">
              <a-input v-model="model.msg" placeholder="请输入提示信息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除标记（0:不可用 1:可用）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入删除标记（0:不可用 1:可用）" style="width: 100%" />
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
    name: 'RecyclerLoginLogForm',
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
           isDeleted: [
              { required: true, message: '请输入删除标记（0:不可用 1:可用）!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.recycler/recyclerLoginLog/add",
          edit: "/com.ilhaha.yueyishou.recycler/recyclerLoginLog/edit",
          queryById: "/com.ilhaha.yueyishou.recycler/recyclerLoginLog/queryById"
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