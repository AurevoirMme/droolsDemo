package edu.hubu.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.hubu.converter.OrderForm2MierrDTO;
import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.form.OrderForm;
import edu.hubu.service.DroolsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DroolsServiceImplTest {

    @Autowired
    private DroolsService droolsService;

    @Test
    public void fireRule() {
        String result = droolsService.fireRule();
        log.info("fireRule():result =\n {}",result);
        Assert.assertNotNull(result);

    }

    @Test
    public void countScore() {
        String result = droolsService.countScore();
        log.info("countScore():result =\n {}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testAddScore() {
        String result = droolsService.testAddScore();
        Assert.assertNotNull(result);
    }

    @Test
    public void AddScore() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        List<MatchInvoiceExpenselRuleRequestDto> dtoList = getObject();
        dtoList.forEach(o ->
        {
            int flag = Integer.valueOf(o.getData().get("count"));
//            System.out.println(gson.toJson(o));
            droolsService.AddScore(o);

            if (flag == Integer.valueOf(o.getData().get("count"))) {
                o.getData().put("count", String.valueOf(flag + 1));
            }
//            System.out.println(gson.toJson(o));
        });

    }

    private List<MatchInvoiceExpenselRuleRequestDto> getObject() {
        List<OrderForm> orderForms = new ArrayList<>();

        {
            OrderForm o = new OrderForm();
            o.setScore("100");
            o.setLocation("湖北省");
            o.setLevel("3");
            o.setFee("100");
            o.setCompany("hubu");
            o.setCount("3");

            orderForms.add(o);
        }
        {
            OrderForm o = new OrderForm();
            o.setScore("120");
            o.setLocation("广东省");
            o.setLevel("4");
            o.setFee("200");
            o.setCompany("hubu1");
            o.setCount("5");

            orderForms.add(o);
        }
        {
            OrderForm o = new OrderForm();
            o.setScore("130");
            o.setLocation("北京市");
            o.setLevel("3");
            o.setFee("672");
            o.setCompany("hubu2");
            o.setCount("9");

            orderForms.add(o);
        }
        {
            OrderForm o = new OrderForm();
            o.setScore("232");
            o.setLocation("深圳市");
            o.setLevel("3");
            o.setFee("500");
            o.setCompany("hubu3");
            o.setCount("4");

            orderForms.add(o);
        }
        {
            OrderForm o = new OrderForm();
            o.setScore("546");
            o.setLocation("贵州省");
            o.setLevel("2");
            o.setFee("342");
            o.setCompany("hubu4");
            o.setCount("1");

            orderForms.add(o);
        }
        {
            OrderForm o = new OrderForm();
            o.setScore("332");
            o.setLocation("河南省");
            o.setLevel("2");
            o.setFee("32");
            o.setCompany("hubu5");
            o.setCount("9");

            orderForms.add(o);
        }

        List<MatchInvoiceExpenselRuleRequestDto> dtoList = orderForms.stream().map(OrderForm2MierrDTO::convert).collect(Collectors.toList());

        return dtoList;
    }

}