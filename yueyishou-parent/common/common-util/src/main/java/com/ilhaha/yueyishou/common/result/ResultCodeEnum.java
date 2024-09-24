package com.ilhaha.yueyishou.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),

    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    FEIGN_FAIL(207, "远程调用失败"),
    LOGIN_AUTH(208, "未登录"),
    PERMISSION(209, "没有权限"),
    ARGUMENT_VALID_ERROR(210, "参数校验异常"),
    INCORRECT_LOGIN_INFORMATION(211,"登录信息有误"),
    ACCOUNT_STOP( 213, "账号已停用"),
    IMAGE_AUDITION_FAIL( 218, "图片存在违规"),
    IMAGE_UPLOAD_FAIL( 219, "图片上传失败"),
    CUSTOMER_INFO_NOT_EXIST(220,"顾客信息不存在"),
    CUSTOMER_ADDRESS_NOT_EXIST(230,"地址信息不存在"),
    SERVICE_ERROR(2012, "服务异常"),


    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
