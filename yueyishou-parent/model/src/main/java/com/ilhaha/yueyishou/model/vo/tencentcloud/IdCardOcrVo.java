package com.ilhaha.yueyishou.model.vo.tencentcloud;

import lombok.Data;

import java.util.Date;


@Data
public class IdCardOcrVo {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别 1：男 2：女
     */
    private String gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 身份证号码
     */
    private String idcardNo;

    /**
     * 身份证地址
     */
    private String idcardAddress;

    /**
     * 身份证有效期
     */
    private String idcardExpire;

    /**
     * 身份证正面
     */
    private String idcardFrontUrl;


    /**
     * 身份证背面
     */
    private String idcardBackUrl;


}