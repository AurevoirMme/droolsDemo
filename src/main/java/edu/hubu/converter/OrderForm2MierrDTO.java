package edu.hubu.converter;

import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.form.OrderForm;

import java.util.Map;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils.serializer
 * @Author: Deson
 * @CreateTime: 2018-08-22 20:20
 * @Description:OrderForm转换成 MatchInvoiceExpenselRuleRequestDto
 */
public class OrderForm2MierrDTO {
    public static MatchInvoiceExpenselRuleRequestDto convert(OrderForm orderForm) {
        MatchInvoiceExpenselRuleRequestDto dto = new MatchInvoiceExpenselRuleRequestDto();
        Map<String, String> map = Object2Map.obj2Map(orderForm);
        dto.setData(map);

        return dto;
    }
}
