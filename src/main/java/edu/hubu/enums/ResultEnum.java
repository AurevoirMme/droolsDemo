package edu.hubu.enums;

import lombok.Getter;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.enums
 * @Author: Deson
 * @CreateTime: 2018-08-14 15:51
 * @Description: 异常原因
 */
@Getter
public enum ResultEnum implements CodeEnum{
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数不正确"),;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
