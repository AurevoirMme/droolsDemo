package edu.hubu.entity;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: demodrool
 * @BelongsPackage: edu.hubu.entity
 * @Author: Deson
 * @CreateTime: 2018-08-07 16:30
 * @Description: 订单信息
 */
@Data
public class Order {
    /** 下单日期. */
    private Date bookingDate;

    /** 订单原价金额.*/
    private int amount;

    /** 下单人.*/
    private User user;

    private int score;

}

