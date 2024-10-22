package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountDetail;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountDetailVo;

import java.util.List;

public interface IRecyclerAccountDetailService extends IService<RecyclerAccountDetail> {

    /**
     * 获取账户明细
     * @param accountId
     * @param detailsTime
     * @return
     */
    List<RecyclerAccountDetailVo> getRecyclerAccountDetail(Long accountId, String detailsTime);

    /**
     * 增加明细
     * @param addDetailsForm
     */
    void addDetails(AddDetailsForm addDetailsForm);

}
