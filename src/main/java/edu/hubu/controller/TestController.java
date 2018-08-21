package edu.hubu.controller;

import edu.hubu.service.DroolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: demodrool
 * @BelongsPackage: edu.hubu.controller
 * @Author: Deson
 * @CreateTime: 2018-07-20 10:05
 * @Description: 测试用
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private DroolsService droolsService;

    @GetMapping("/showRults")
    public String showRults(){
        return droolsService.fireRule();
    }
    @GetMapping("/testBoot")
    public String testBoot(){
        return droolsService.countScore();
    }

}

