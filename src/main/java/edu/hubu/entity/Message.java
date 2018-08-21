package edu.hubu.entity;

import lombok.Data;

/**
 * @BelongsProject: demodrool
 * @BelongsPackage: edu.hubu.domain
 * @Author: Deson
 * @CreateTime: 2018-07-20 09:55
 * @Description: Drools中需要使用到的Model
 */
@Data
public class Message {
    public static final Integer HELLO = 0;
    public static final Integer GOODBYE = 1;

    private String message;

    private Integer status;
}
