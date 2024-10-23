package com.ilhaha.yueyishou.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountDetail;
import com.ilhaha.yueyishou.customer.mapper.CustomerAccountDetailMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountDetailService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountDetail;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountDetailVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerAccountDetailServiceImpl extends ServiceImpl<CustomerAccountDetailMapper, CustomerAccountDetail> implements ICustomerAccountDetailService {

    /**
     * 增加明细
     * @param addDetailsForm
     */
    @Override
    public void addDetails(AddDetailsForm addDetailsForm) {
        CustomerAccountDetail customerAccountDetail = new CustomerAccountDetail();
        customerAccountDetail.setCustomerAccountId(addDetailsForm.getAccountId());
        customerAccountDetail.setTradeType(addDetailsForm.getTradeType());
        customerAccountDetail.setAmount(addDetailsForm.getAmount());
        customerAccountDetail.setContent(addDetailsForm.getContent());
        customerAccountDetail.setCreateTime(new Date());
        customerAccountDetail.setUpdateTime(new Date());
        this.save(customerAccountDetail);
    }

    /**
     * 获取账户明细信息
     * @param accountId
     * @param detailsTime
     * @return
     */
    @Override
    public List<CustomerAccountDetailVo> getCustomerAccountDetail(Long accountId, String detailsTime) {
        LambdaQueryWrapper<CustomerAccountDetail> customerAccountDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerAccountDetailLambdaQueryWrapper.eq(CustomerAccountDetail::getCustomerAccountId,accountId);

        // 如果 detailsTime 不为空，则添加该月的范围查询条件
        if (!ObjectUtils.isEmpty(detailsTime)) {
            // 将 detailsTime 转换为该月的起始时间和结束时间
            LocalDate startOfMonth = LocalDate.parse(detailsTime + "-01");
            // 当月第一天 00:00:00
            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
            // 当月最后一天 23:59:59
            LocalDateTime endDateTime = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth()).atTime(23, 59, 59);
            customerAccountDetailLambdaQueryWrapper.between(CustomerAccountDetail::getCreateTime, startDateTime, endDateTime);
        }
        // 按照创建时间降序排列
        customerAccountDetailLambdaQueryWrapper.orderByDesc(CustomerAccountDetail::getCreateTime);

        // 查询数据库
        List<CustomerAccountDetail> customerAccountDetailListDB = this.list(customerAccountDetailLambdaQueryWrapper);

        // 如果结果为空，返回空列表而不是
        if (ObjectUtils.isEmpty(customerAccountDetailLambdaQueryWrapper)) {
            return Collections.emptyList();
        }

        return customerAccountDetailListDB.stream().map(item -> {
            CustomerAccountDetailVo customerAccountDetailVo = new CustomerAccountDetailVo();
            BeanUtils.copyProperties(item, customerAccountDetailVo);
            return customerAccountDetailVo;
        }).collect(Collectors.toList());
    }
}
