package edu.hubu.controller;

import edu.hubu.converter.OrderForm2MierrDTO;
import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.enums.ResultEnum;
import edu.hubu.exception.DroolsDemoException;
import edu.hubu.form.OrderForm;
import edu.hubu.utils.ResultVOUtil;
import edu.hubu.vo.ResultVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.controller
 * @Author: Deson
 * @CreateTime: 2018-08-22 11:00
 * @Description: 处理订单的接口
 */
@RequestMapping("/OrderFlow")
@Slf4j
@RestController
public class OrderInputController {
    @ApiOperation(value="输入流水规则引擎过滤", notes="根据流水form",httpMethod = "Post")
    @ApiImplicitParam(name = "orderForm", value = "订单流水form", required = true, dataType = "OrderForm", paramType = "path")
    @PostMapping("/input")
    public ResultVO<Map<String, String>> input(@Valid OrderForm orderForm,
                                               BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.error("【输入流水】参数不正确, orderForm={}", orderForm);
            throw new DroolsDemoException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        MatchInvoiceExpenselRuleRequestDto dto = OrderForm2MierrDTO.convert(orderForm);



        return ResultVOUtil.success();
    }
}
