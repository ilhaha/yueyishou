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
     * 回收员账户明细交易类型：1回收支付费用
     */
    public static final String RECYCLE_PAY_FEE = "1";

    /**
     * 回收员账户明细交易类型： 2提现
     */
    public static final String ON_WITHDRAW = "2";

    /**
     * 回收员账户明细交易类型： 3充值
     */
    public static final String ON_RECHARGE = "3";

    /**
     * 顾客账户明细交易类型：1回收收入
     */
    public static final String CUSTOMER_RECYCLE_PAY_FEE = "1";

    /**
     * 顾客账户明细交易类型： 2提现
     */
    public static final String CUSTOMER_ON_WITHDRAW = "2";

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


}
