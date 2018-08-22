package edu.hubu.converter;

import edu.hubu.form.OrderForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Object2MapTest {

    @Test
    public void obj2Map() {
        OrderForm orderForm = new OrderForm();
        orderForm.setCompany("中国银联");
        orderForm.setCount("1");
        orderForm.setFee("300");
        orderForm.setLevel("5");
        orderForm.setLocation("湖北");
        orderForm.setScore("200");
        Map<String, String> map = Object2Map.obj2Map(orderForm);
        log.info("map : {}",map);
        Assert.assertNotNull(map);
    }
}