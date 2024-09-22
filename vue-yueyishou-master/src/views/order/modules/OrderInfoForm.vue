<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="客户ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerId">
              <a-input-number v-model="model.customerId" placeholder="请输入客户ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderNo">
              <a-input v-model="model.orderNo" placeholder="请输入订单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="客户地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerLocation">
              <a-input v-model="model.customerLocation" placeholder="请输入客户地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单回收分类ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="categoryId">
              <a-input-number v-model="model.categoryId" placeholder="请输入订单回收分类ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclerId">
              <a-input-number v-model="model.recyclerId" placeholder="请输入回收员ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员接单时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="acceptTime">
              <j-date placeholder="请选择回收员接单时间" v-model="model.acceptTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员到达时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="arriveTime">
              <j-date placeholder="请选择回收员到达时间" v-model="model.arriveTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单状态：1等待接单，2回收员已接单，3回收员前往回收点，4回收员已到达，5顾客确定订单，6回收员未付款，7回收员已付款订单完成，8待评价，9订单已取消" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入订单状态：1等待接单，2回收员已接单，3回收员前往回收点，4回收员已到达，5顾客确定订单，6回收员未付款，7回收员已付款订单完成，8待评价，9订单已取消" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取消订单信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cancelMessage">
              <a-input v-model="model.cancelMessage" placeholder="请输入取消订单信息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单备注信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入订单备注信息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员预支付订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expectRecyclerAmount">
              <a-input-number v-model="model.expectRecyclerAmount" placeholder="请输入回收员预支付订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员预支付平台订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expectRecyclerPlatformAmount">
              <a-input-number v-model="model.expectRecyclerPlatformAmount" placeholder="请输入回收员预支付平台订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="顾客预支付平台订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expectCustomerPlatformAmount">
              <a-input-number v-model="model.expectCustomerPlatformAmount" placeholder="请输入顾客预支付平台订单金额" style="width: 100%" />
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
    name: 'OrderInfoForm',
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
           customerId: [
              { required: true, message: '请输入客户ID!'},
           ],
           orderNo: [
              { required: true, message: '请输入订单号!'},
           ],
           customerLocation: [
              { required: true, message: '请输入客户地点!'},
           ],
           categoryId: [
              { required: true, message: '请输入订单回收分类ID!'},
           ],
           status: [
              { required: true, message: '请输入订单状态：1等待接单，2回收员已接单，3回收员前往回收点，4回收员已到达，5顾客确定订单，6回收员未付款，7回收员已付款订单完成，8待评价，9订单已取消!'},
           ],
           createTime: [
              { required: true, message: '请输入创建时间!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.order/orderInfo/add",
          edit: "/com.ilhaha.yueyishou.order/orderInfo/edit",
          queryById: "/com.ilhaha.yueyishou.order/orderInfo/queryById"
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