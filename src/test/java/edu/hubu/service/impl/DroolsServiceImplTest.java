package edu.hubu.service.impl;

import edu.hubu.service.DroolsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


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


}