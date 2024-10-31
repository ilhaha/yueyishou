package org.jeecg.modules.mgr.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.order.CollectVo;
import org.jeecg.modules.mgr.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/10/30 15:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    private IndexService indexService;

    /**
     * 后台管理系统汇总数据
     * @return
     */
    @GetMapping("/collect")
    public Result<CollectVo> collect(){
        return indexService.collect();
    }
}
