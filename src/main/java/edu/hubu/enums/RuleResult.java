package edu.hubu.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RuleResult implements CodeEnum {

    REJECT(1, "拒绝"),

    WARNING(2, "警告"),

    CALCULATE(3, "用于计算某个值"),

    OK(4, "OK");

    private Integer code;
    private String description;

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "RuleResult{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
