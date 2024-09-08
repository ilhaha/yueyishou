<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="昵称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nickname">
              <a-input v-model="model.nickname" placeholder="请输入昵称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="avatarUrl">
              <j-image-upload isMultiple  v-model="model.avatarUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <a-input v-model="model.phone" placeholder="请输入电话"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="性别 1:男 2：女" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gender">
              <a-input v-model="model.gender" placeholder="请输入性别 1:男 2：女"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="birthday">
              <j-date placeholder="请选择生日" v-model="model.birthday"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardNo">
              <a-input v-model="model.idcardNo" placeholder="请输入身份证号码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardAddress">
              <a-input v-model="model.idcardAddress" placeholder="请输入身份证地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证有效期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardExpire">
              <j-date placeholder="请选择身份证有效期" v-model="model.idcardExpire"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证正面" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardFrontUrl">
              <j-image-upload isMultiple  v-model="model.idcardFrontUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证背面" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardBackUrl">
              <j-image-upload isMultiple  v-model="model.idcardBackUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手持身份证" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idcardHandUrl">
              <j-image-upload isMultiple  v-model="model.idcardHandUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="腾讯云人脸模型id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="faceModelId">
              <a-input v-model="model.faceModelId" placeholder="请输入腾讯云人脸模型id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员工号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jobNo">
              <a-input v-model="model.jobNo" placeholder="请输入回收员工号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="score">
              <a-input-number v-model="model.score" placeholder="请输入评分" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单量统计" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderCount">
              <a-input-number v-model="model.orderCount" placeholder="请输入订单量统计" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="authStatus">
              <a-input-number v-model="model.authStatus" placeholder="请输入认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态，1正常，2禁用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态，1正常，2禁用" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建时间" v-model="model.createTime"  style="width: 100%" />
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
    name: 'RecyclerInfoForm',
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
           nickname: [
              { required: true, message: '请输入昵称!'},
           ],
           gender: [
              { required: true, message: '请输入性别 1:男 2：女!'},
           ],
           score: [
              { required: true, message: '请输入评分!'},
           ],
           orderCount: [
              { required: true, message: '请输入订单量统计!'},
           ],
           authStatus: [
              { required: true, message: '请输入认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过!'},
           ],
           status: [
              { required: true, message: '请输入状态，1正常，2禁用!'},
           ],
           createTime: [
              { required: true, message: '请输入创建时间!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.recycler/recyclerInfo/add",
          edit: "/com.ilhaha.yueyishou.recycler/recyclerInfo/edit",
          queryById: "/com.ilhaha.yueyishou.recycler/recyclerInfo/queryById"
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