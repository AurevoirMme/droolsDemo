package edu.hubu.form;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.form
 * @Author: Deson
 * @CreateTime: 2018-08-22 11:26
 * @Description: 流水form
 */
@Data
public class OrderForm {

    /**
     * 用户等级
     */
    @NotEmpty(message = "用户等级不能为空")
    private String level;

    /**
     * 流水发生地区
     */
    @NotEmpty(message = "流水发生地区不能为空")
    private String location;

    /**
     * 金额
     */
    @NotEmpty(message = "金额不能为空")
    private String fee;

    /**
     * 用户积分
     */
    @NotEmpty(message = "积分不能为空")
    private String score;

    /**
     * 该地区流水产生次数
     */
    @NotEmpty(message = "该地区流水产生次数不能为空")
    private String count;

    /**
     * 用户所在企业
     */
    @NotEmpty(message = "用户所在企业不能为空")
    private String company;
}

