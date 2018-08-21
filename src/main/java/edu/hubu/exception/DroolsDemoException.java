package edu.hubu.exception;

import edu.hubu.enums.ResultEnum;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.exception
 * @Author: Deson
 * @CreateTime: 2018-08-14 15:49
 * @Description: 运行异常
 */
public class DroolsDemoException extends RuntimeException {
    private Integer code;

    public DroolsDemoException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public DroolsDemoException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
