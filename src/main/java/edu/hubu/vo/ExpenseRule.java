package edu.hubu.vo;

import lombok.Data;
import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.vo
 * @Author: Deson
 * @CreateTime: 2018-08-13 11:32
 * @Description: Excel数据对象
 */
@ExcelObject(parseType = ParseType.ROW,start = 2,end = 60)
@Data
public class ExpenseRule {
    /**交易银行名称.*/
    @ExcelField(position = 1)
    private String companyName;

    /**费用类型.*/
    @ExcelField(position = 2)
    private String expenseType;

    /**规则应用场景.*/
    @ExcelField(position = 3)
    private String applyPoint;

    /**规则结果.*/
    @ExcelField(position = 4)
    private String ruleAction;

    /**规则内容.*/
    @ExcelField(position = 5)
    private String rule;

    /**提示信息.*/
    @ExcelField(position = 6)
    private String alertMessage;

    /**规则id.*/
    @ExcelField(position = 7)
    private String ruleId;

    /**解析出的规则内容.*/
    @ExcelField(position = 8)
    private String droolsRule;

    /**解析出的计算方式.*/
    @ExcelField(position = 9)
    private String resultCalculate;
}
