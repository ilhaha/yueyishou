package org.jeecg.modules.mgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.model.vo.customer.ExamineInfoVo;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/7 21:52
 * @Version 1.0
 */
public interface CustomerService {

    /**
     * 客户分页列表查询
     *
     * @param customerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CustomerInfo> queryPageList(CustomerInfo customerInfo, Integer pageNo, Integer pageSize);


    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm);

    /**
     * 添加操作顾客账户记录
     * @param customerAccountOperate
     * @return
     */
    Boolean add(CustomerAccountOperate customerAccountOperate);

    /**
     * 获取顾客账号状态操作日志
     *
     * @param pageNo
     * @param pageSize
     * @param customerId
     * @return
     */
    Page<CustomerAccountOperate> getOperateList(Integer pageNo, Integer pageSize, Long customerId);



}
