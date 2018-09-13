package edu.hubu.service.impl;

import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.entity.Message;
import edu.hubu.entity.Order;
import edu.hubu.entity.User;
import edu.hubu.enums.RuleResult;
import edu.hubu.rule.FilloExcel;
import edu.hubu.service.DroolsService;
import edu.hubu.utils.*;
import edu.hubu.vo.ExpenseRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: demodrool
 * @BelongsPackage: edu.hubu.service.impl
 * @Author: Deson
 * @CreateTime: 2018-07-20 10:02
 * @Description: Drool处理规则Service层
 */
@Service
@Slf4j
public class DroolsServiceImpl implements DroolsService {
    public String fireRule() {
        try {
            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("HelloWorldKS");

            // go !
            Message message = new Message();
            message.setMessage("Hello World");
            message.setStatus(Message.HELLO);
            kSession.insert(message);//插入
            kSession.fireAllRules();//执行规则
            kSession.dispose();
            return message.getMessage();
        } catch (Exception e) {
            log.error("edu.hubu.service.impl.DroolsServiceImpl-fireRule[err]:{}", e.getMessage());
            return null;
        }

    }

    /**
     * 计算额外积分金额 规则如下: 订单原价金额
     * 100以下, 不加分
     * 100-500 加100分
     * 500-1000 加500分
     * 1000 以上 加1000分
     *
     * @return
     */
    @Override
    public String countScore() {
        try {
            KieServices kieServices = KieServices.Factory.get();
            KieContainer kContainer = kieServices.getKieClasspathContainer();
            KieSession ksession = kContainer.newKieSession("point-rulesKS");
            StringBuilder stringBuilder = new StringBuilder();


            List<Order> orderList = getInitData();
            orderList.forEach(o -> {
                ksession.insert(o);
                ksession.fireAllRules();
                stringBuilder.append(o.getUser().getName() + "|" + o.getScore() + "\n");

            });

            return stringBuilder.toString();
        } catch (Exception e) {
            log.error("edu.hubu.service.impl.DroolsServiceImpl-countScore[err]:{}", e.getMessage());
            return null;
        }


    }

    @Override
    public String testAddScore() {
        try {
            KieSessionUtil kieSessionUtil = SingletonKieSession.getInstance();

            KieSession ksession = kieSessionUtil.build().newKieSession();

            StringBuilder stringBuilder = new StringBuilder();

            List<MatchInvoiceExpenselRuleRequestDto> dtoList = getTestData();

            RuleGenerate generate = new RuleGenerate();
            generate.setRulePropertyNameAlias(SingletonListRules.getRulePropertyNameAlias());

            dtoList.forEach(o -> {
                ksession.insert(o);
                ksession.fireAllRules();
                stringBuilder.append(o.getRemarkMessage() + "|" + o.getData().get("AMOUNT") + "|" + o.getData().get("SCORE") + "\n");

                // 处理计算后的值
                //{ER_SCORE}={ER_SCORE}+{ER_EXP_FEE}*1.1
                // if (!StringUtils.isEmpty(o.getRemarkMessage()))
                if (o.getResult().getCode() == RuleResult.CALCULATE.getCode()) {
                    generate.dealCaculate(o.getRemarkMessage(), o.getData());
                }
            });
            System.out.println(stringBuilder);
            System.out.println("\n【after】\n");
            StringBuilder sb = new StringBuilder();

            dtoList.forEach(o -> sb.append(o.getRemarkMessage() + "|" + o.getData().get("AMOUNT") + "|" + o.getData().get("SCORE") + "\n"));

            System.out.println(sb);
            return stringBuilder.toString();

        } catch (Exception e) {
            log.error("edu.hubu.service.impl.DroolsServiceImpl-countScore[err]:{}", e.getMessage());
            return null;
        }
    }

    @Override
    public MatchInvoiceExpenselRuleRequestDto AddScore(MatchInvoiceExpenselRuleRequestDto o) {
//        KieSessionUtil kieSessionUtil = SingletonKieSession.getInstance();

//        KieSession ksession = kieSessionUtil.build().newKieSession();
        KieSession ksession = SessionUtil.getInstance();

        StringBuffer info = new StringBuffer();

        info.append("\n\n\n\n***【Service--Begin】***\n\n");

        RuleGenerate generate = new RuleGenerate();
        generate.setRulePropertyNameAlias(SingletonListRules.getRulePropertyNameAlias());


        o.getData().forEach((k, v) ->
                info.append(k).append(":").append(v).append(" | "));
        info.append(o.getRemarkMessage()).append("\n\n***【after】***\n\n");

        ksession.insert(o);
        ksession.fireAllRules();


        // 处理计算后的值
        //{ER_SCORE}={ER_SCORE}+{ER_EXP_FEE}*1.1
        // if (!StringUtils.isEmpty(o.getRemarkMessage()))
        if (o.getResult().getCode() == RuleResult.CALCULATE.getCode()) {
            generate.dealCaculate(o.getRemarkMessage(), o.getData());
        }


        o.getData().forEach((k,v)->{
            info.append(k).append(":").append(v).append(" | ");
        });

        info.append(o.getRemarkMessage()).append("\n\n***【Service--End】***\n\n\n\n");
        log.info("Service通过规则引擎情况：{}",info);
        return o;
    }

    private static List<MatchInvoiceExpenselRuleRequestDto> getTestData() {
        try {
            List<MatchInvoiceExpenselRuleRequestDto> dtoList = new ArrayList<>();

            {
                MatchInvoiceExpenselRuleRequestDto dto = new MatchInvoiceExpenselRuleRequestDto();
                Map<String, String> map = new HashMap<>();
                map.put("AMOUNT", "1000");
                map.put("SCORE", "0");
                map.put("DAY", "20");
                map.put("PARAM", "100");
                dto.setData(map);
                dtoList.add(dto);
            }
            {
                MatchInvoiceExpenselRuleRequestDto dto = new MatchInvoiceExpenselRuleRequestDto();
                Map<String, String> map = new HashMap<>();
                map.put("AMOUNT", "200");
                map.put("DAY", "20");
                map.put("SCORE", "25");
                map.put("PARAM", "100");
                dto.setData(map);
                dtoList.add(dto);
            }
            {
                MatchInvoiceExpenselRuleRequestDto dto = new MatchInvoiceExpenselRuleRequestDto();
                Map<String, String> map = new HashMap<>();
                map.put("AMOUNT", "300");
                map.put("SCORE", "30");
                map.put("DAY", "12");
                map.put("PARAM", "100");
                dto.setData(map);
                dtoList.add(dto);
            }
            {
                MatchInvoiceExpenselRuleRequestDto dto = new MatchInvoiceExpenselRuleRequestDto();
                Map<String, String> map = new HashMap<>();
                map.put("AMOUNT", "600");
                map.put("DAY", "21");
                map.put("SCORE", "6");
                map.put("PARAM", "100");
                dto.setData(map);
                dtoList.add(dto);
            }


            return dtoList;
        } catch (Exception e) {
            log.error("edu.hubu.service.impl.DroolsServiceImpl-getTestData[err]:{}", e.getMessage());

            return null;
        }
    }

    private static List<Order> getInitData() {
        try {
            List<Order> orderList = new ArrayList<Order>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            {
                Order order = new Order();
                order.setAmount(120);
                order.setBookingDate(df.parse("2015-07-01"));
                User user = new User();
                user.setLevel(1);
                user.setName("Name1");
                order.setUser(user);
                order.setScore(111);
                orderList.add(order);
            }
            {
                Order order = new Order();
                order.setAmount(200);
                order.setBookingDate(df.parse("2015-07-02"));
                User user = new User();
                user.setLevel(2);
                user.setName("Name2");
                order.setUser(user);
                orderList.add(order);
            }
            {
                Order order = new Order();
                order.setAmount(800);
                order.setBookingDate(df.parse("2015-07-03"));
                User user = new User();
                user.setLevel(3);
                user.setName("Name3");
                order.setUser(user);
                orderList.add(order);
            }
            {
                Order order = new Order();
                order.setAmount(1500);
                order.setBookingDate(df.parse("2015-07-04"));
                User user = new User();
                user.setLevel(4);
                user.setName("Name4");
                order.setUser(user);
                orderList.add(order);
            }
            return orderList;

        } catch (Exception e) {
            log.error("edu.hubu.service.impl.DroolsServiceImpl-getInitData[err]:{}", e.getMessage());

            return null;
        }

    }

}
