package org.jeecg.modules.mgr.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.order.CollectVo;

/**
 * @Author ilhaha
 * @Create 2024/10/30 15:39
 * @Version 1.0
 */
public interface IndexService {

    /**
     * 后台管理系统汇总数据
     * @return
     */
    Result<CollectVo> collect();

}
