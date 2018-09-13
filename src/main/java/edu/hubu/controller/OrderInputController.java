package edu.hubu.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.hubu.converter.OrderForm2MierrDTO;
import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.enums.ResultEnum;
import edu.hubu.exception.DroolsDemoException;
import edu.hubu.form.OrderForm;
import edu.hubu.service.DroolsService;
import edu.hubu.utils.ResultVOUtil;
import edu.hubu.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "OrderInputController", description = "处理订单的接口")
public class OrderInputController {
    @Autowired
    DroolsService droolsService;

    @ApiOperation(value="输入流水规则引擎过滤", notes="根据流水form",httpMethod = "Post")
    @ApiImplicitParam(name = "orderForm", value = "订单流水form", required = true, dataType = "OrderForm", paramType = "path")
    @PostMapping("/input")
    public ResultVO<Map<String, String>> input(@Valid OrderForm orderForm,
                                               BindingResult bindingResult){

//        StringBuffer info = new StringBuffer();
        StringBuilder info = new StringBuilder();
        info.append("\n\n\n\n***【Controller--Begin】***\n\n");

        if (bindingResult.hasErrors()) {
            log.error("【输入流水】参数不正确, orderForm={}", orderForm);
            throw new DroolsDemoException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        MatchInvoiceExpenselRuleRequestDto dto = OrderForm2MierrDTO.convert(orderForm);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        dto.getData().forEach((k, v) ->
                info.append(k).append(":").append(v).append(" | "));
        info.append(dto.getRemarkMessage()).append("\n\n***【after】***\n\n");
//        log.info("before:{}",before);

        int flag = Integer.valueOf(dto.getData().get("count"));

        droolsService.AddScore(dto);

        //数量加1操作。
        if (flag == Integer.valueOf(dto.getData().get("count"))) {
            dto.getData().put("count", String.valueOf(flag + 1));
        }

        dto.getData().forEach((k,v)->{
            info.append(k).append(":").append(v).append(" | ");
        });

        info.append(dto.getRemarkMessage()).append("\n\n***【Controller--End】***\n\n\n\n");
        log.info("Controller通过规则引擎情况：{}",info);
        String after = gson.toJson(dto);
//        log.info("after:{}",after);

        return ResultVOUtil.success();
    }
}
