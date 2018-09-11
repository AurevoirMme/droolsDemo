package edu.hubu.dto;

import edu.hubu.enums.RuleResult;
import lombok.Data;

//import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject: demodrool
 * @BelongsPackage: edu.hubu.entity
 * @Author: Deson
 * @CreateTime: 2018-08-07 16:39
 * @Description: 订单信息
 */
@Data
public class MatchInvoiceExpenselRuleRequestDto {
    //规则ID
    private String ruleOID;
    
    //订单ID
    private String invoiceOID;
    
    //消费信息-会员级别、地点、费用等等
    private Map<String, String> data = new ConcurrentHashMap<>();
    
    //规则执行结果
    private RuleResult result = RuleResult.OK;
    
    //备注
    private String remarkMessage;

    //规则明细
    private String ruleMessage;
}
