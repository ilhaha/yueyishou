package com.ilhaha.yueyishou.model.constant;

/**
 * @Author ilhaha
 * @Create 2024/10/22 16:05
 * @Version 1.0
 *
 * 账户常量
 */
public class AccountDetailsConstant {

    /**
     * 明细交易类型：1支出
     */
    public static final String EXPENDITURE = "1";

    /**
     * 明细交易类型： 2收入
     */
    public static final String INCOME = "2";


    /**
     * 回收支付内容
     */
    public static final String RECYCLE_PAY_FEE_CONTENT = "结算订单";

    /**
     * 提现内容
     */
    public static final String ON_WITHDRAW_CONTENT = "提现到微信零钱";

    /**
     * 充值内容
     */
    public static final String ON_RECHARGE_CONTENT = "充值到悦易收账户";

    /**
     * 超过预约时间未服务，顾客获取补偿
     */
    public static final String TIMEOUT_SERVICE_COMPENSATION = "超时未服务补偿";


    /**
     * 超过预约时间未服务，回收员补偿
     */
    public static final String UNDELIVERED_SERVICE_COMPENSATION = "超时未服务赔偿";

    /**
     * 顾客取消已超过免费时限，需支付相关费用，回收员得到补偿
     */
    public static final String TIMEOUT_CANCELLATION_COMPENSATION = "超时取消补偿";

    /**
     * 顾客取消已超过免费时限，需支付相关费用，顾客支付赔偿
     */
    public static final String OVERDUE_CANCELLATION_COMPENSATION = "超时取消赔偿";

    /**
     * 回收员在预约时间不足60分钟取消，顾客得到补偿
     */
    public static final String CUSTOMER_SHORT_NOTICE_CANCELLATION = "短时取消补偿";
    /**
     * 回收员在预约时间不足60分钟取消，回收员支付补偿
     */
    public static final String RECYCLER_SHORT_NOTICE_CANCELLATION = "短时取消赔偿";

    /**
     * 回收员拒收订单得到补偿
     */
    public static final String RECYCLER_COMPENSATION_REJECTION = "拒收订单补偿";

    /**
     * 回收员拒收订单顾客支出赔偿
     */
    public static final String CUSTOMER_COMPENSATION_REJECTION = "被拒收订单赔偿";


}
