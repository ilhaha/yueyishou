package com.ilhaha.yueyishou.model.entity.recycler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("recycler_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;


	/**微信openId*/
    private String wxOpenId;
	/**昵称*/
    private String nickname;
	/**头像*/
    private String avatarUrl;
	/**电话*/
    private String phone;
	/**姓名*/
    private String name;
	/**性别 1:男 2：女*/
    private String gender;
	/**生日*/
    private Date birthday;
	/**身份证号码*/
    private String idcardNo;
	/**身份证地址*/
    private String idcardAddress;
	/**身份证有效期*/
    private Date idcardExpire;
	/**身份证正面*/
    private String idcardFrontUrl;
	/**身份证背面*/
    private String idcardBackUrl;
	/**手持身份证*/
    private String idcardHandUrl;
	/**腾讯云人脸模型id*/
    private String faceModelId;
	/**回收员工号*/
    private String jobNo;
	/**评分*/
    private BigDecimal score;
	/**订单量统计*/
    private Integer orderCount;
	/**认证状态 1：审核中 2：认证通过 -1：认证未通过*/
    private Integer authStatus;
	/**状态，1正常，2禁用*/
    private Integer status;
}
