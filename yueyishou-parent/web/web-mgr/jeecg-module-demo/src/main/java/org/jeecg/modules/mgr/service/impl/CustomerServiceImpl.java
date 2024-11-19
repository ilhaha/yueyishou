package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.client.CustomerAccountOperateFeignClient;
import com.ilhaha.yueyishou.customer.client.CustomerInfoFeignClient;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.model.vo.customer.ExamineInfoVo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.mgr.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/7 21:52
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerInfoFeignClient customerInfoFeignClient;

    @Resource
    private CustomerAccountOperateFeignClient customerAccountOperateFeignClient;

    /**
     * 客户分页列表查询
     *
     * @param customerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CustomerInfo> queryPageList(CustomerInfo customerInfo, Integer pageNo, Integer pageSize) {
        return customerInfoFeignClient.queryPageList(customerInfo,pageNo,pageSize).getData();
    }

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm) {
        return customerInfoFeignClient.switchStatus(updateCustomerStatusForm).getData();
    }

    /**
     * 添加操作顾客账户记录
     * @param customerAccountOperate
     * @return
     */
    @Override
    public Boolean add(CustomerAccountOperate customerAccountOperate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        customerAccountOperate.setOperator(sysUser.getRealname());
        return customerAccountOperateFeignClient.add(customerAccountOperate).getData();
    }

    /**
     * 获取顾客账号状态操作日志
     *
     * @param pageNo
     * @param pageSize
     * @param customerId
     * @return
     */
    @Override
    public Page<CustomerAccountOperate> getOperateList(Integer pageNo, Integer pageSize, Long customerId) {
        return customerAccountOperateFeignClient.getOperateList(pageNo,pageSize,customerId).getData();
    }

}
