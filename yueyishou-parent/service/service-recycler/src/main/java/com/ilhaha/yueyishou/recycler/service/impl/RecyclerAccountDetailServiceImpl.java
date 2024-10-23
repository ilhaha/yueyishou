package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountDetail;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountDetailVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerAccountDetailMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountDetailService;
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
public class RecyclerAccountDetailServiceImpl extends ServiceImpl<RecyclerAccountDetailMapper, RecyclerAccountDetail> implements IRecyclerAccountDetailService {

    /**
     * 获取账户明细
     *
     * @param accountId
     * @param detailsTime
     * @return
     */
    @Override
    public List<RecyclerAccountDetailVo> getRecyclerAccountDetail(Long accountId, String detailsTime) {
        // 创建 LambdaQueryWrapper 查询条件
        LambdaQueryWrapper<RecyclerAccountDetail> recyclerAccountDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerAccountDetailLambdaQueryWrapper.eq(RecyclerAccountDetail::getRecyclerAccountId, accountId);

        // 如果 detailsTime 不为空，则添加该月的范围查询条件
        if (!ObjectUtils.isEmpty(detailsTime)) {
            // 将 detailsTime 转换为该月的起始时间和结束时间
            LocalDate startOfMonth = LocalDate.parse(detailsTime + "-01");
            // 当月第一天 00:00:00
            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
            // 当月最后一天 23:59:59
            LocalDateTime endDateTime = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth()).atTime(23, 59, 59);
            recyclerAccountDetailLambdaQueryWrapper.between(RecyclerAccountDetail::getCreateTime, startDateTime, endDateTime);
        }

        // 按照创建时间降序排列
        recyclerAccountDetailLambdaQueryWrapper.orderByDesc(RecyclerAccountDetail::getCreateTime);

        // 查询数据库
        List<RecyclerAccountDetail> recyclerAccountDetailListDB = this.list(recyclerAccountDetailLambdaQueryWrapper);

        // 如果结果为空，返回空列表而不是
        if (ObjectUtils.isEmpty(recyclerAccountDetailListDB)) {
            return Collections.emptyList();
        }

        return recyclerAccountDetailListDB.stream().map(item -> {
            RecyclerAccountDetailVo recyclerAccountDetailVo = new RecyclerAccountDetailVo();
            BeanUtils.copyProperties(item, recyclerAccountDetailVo);
            return recyclerAccountDetailVo;
        }).collect(Collectors.toList());
    }

    /**
     * 增加明细
     * @param addDetailsForm
     */
    @Override
    public void addDetails(AddDetailsForm addDetailsForm) {
        RecyclerAccountDetail recyclerAccountDetail = new RecyclerAccountDetail();
        recyclerAccountDetail.setRecyclerAccountId(addDetailsForm.getAccountId());
        recyclerAccountDetail.setTradeType(addDetailsForm.getTradeType());
        recyclerAccountDetail.setAmount(addDetailsForm.getAmount());
        recyclerAccountDetail.setContent(addDetailsForm.getContent());
        recyclerAccountDetail.setCreateTime(new Date());
        recyclerAccountDetail.setUpdateTime(new Date());
        this.save(recyclerAccountDetail);
    }
}
