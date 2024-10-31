package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author ilhaha
 * @Create 2024/10/30 15:06
 * @Version 1.0
 *
 * 后台管理系统汇总Vo
 */
@Data
public class CollectVo {

    /**
     * 总佣金收入
     */
    private BigDecimal totalCommissionIncome;

    /**
     * 佣金同周比
     */
    private BigDecimal syncyclicRatio;

    /**
     * 佣金同日比
     */
    private BigDecimal isodiurnalRatio;

    /**
     * 今日佣金收入
     */
    private BigDecimal todayCommissionIncome;

    /**
     * 总订单量
     */
    private Long totalOrderCount;

    /**
     * 本周订单量
     */
    private Long currentWeekOrderCount;

    /**
     * 本周每日订单量
     */
    private List<Map<String, Object>> dailyOrderCountMap;

    /**
     * 订单总支付数
     */
    private Long totalOrderPayCount;

    /**
     * 本周每日支付数
     */
    private List<Map<String, Object>> dailyOrderPayCountMap;

    /**
     * 转换率
     */
    private BigDecimal conversionRate;

    /**
     * 本年每个月佣金收入
     */
    private List<Map<String,Object>> yearCommissionIncome;

    /**
     * 今日每个时辰佣金收入
     */
    private List<Map<String,Object>> timeCommissionIncome;

    /**
     * 本周每天佣金收入
     */
    private List<Map<String,Object>> weekCommissionIncome;

    /**
     * 本月每天佣金收入
     */
    private List<Map<String,Object>> monthCommissionIncome;

}
