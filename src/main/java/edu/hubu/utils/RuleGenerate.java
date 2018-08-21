package edu.hubu.utils;

import com.google.common.base.Splitter;
import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;
import edu.hubu.enums.RuleResult;
import edu.hubu.vo.ExpenseRule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-14 09:19
 * @Description: 简单规则组成, 并不不包含drools复杂的语法规则
 */
@Data
@Slf4j
public class RuleGenerate {

    //规则包名
    private String packageName;

    //规则需要外部类的包
    private List<String> importPackages = new ArrayList<>();

    //规则名称
    private String ruleName;

    //规则匹配的类型，如$o:OrderItem($p:price,$code:costCode)中的OrderItem
    private String objectType;

    //规则匹配类型的变量名，如$o:OrderItem($p:price,$code:costCode)中的OrderItem中$o
    private String objectVar;

    //规则信息
    private ExpenseRule expenseRule;

    //规则执行动作结果赋值属性名，如 $invoice.setResult(RuleResult.REJECT); 中的result;
    private String resultPropertyName;

    //规则执行动作结果的提示信息赋值属性名，如   $invoice.setRemarkMessage("您的房费高于公司标准 ");中的remarkMessage.
    private String alertMessagePropertyName;

    //规则匹配属性别名，如data["EXPENSE_LEVEL"] not in ("总裁"),
    // 中的EXPENSE_LEVEL在travelRule中droolsRule中的名不一样，类似mybatis映射一下
    //不过key为正则表达式
    private Map<String, String> rulePropertyNameAlias = new HashMap<>();

    private boolean rulePropertyNameAliasIsRegularExpression = true;

    public String Generate() {
        StringBuilder sb = new StringBuilder();

        //规则头部
        sb.append("package ").append(packageName).append("\n").append("\n");

        importPackages.stream().forEach(t -> sb.append("import ").append(t).append("\n"));

        sb.append("\n\n");

        //规则名称
        sb.append("rule  \"").append(generateRuleName()).append("\n\n");

        //规则条件
        sb.append("when").append("\n");
        sb.append("  ").append(objectVar).append(" : ").append(objectType).append("(").append("\n");

        //规则条件内容
        List<String> rules = generateWhenContent();
        rules.stream().forEach(t -> sb.append("  ").append(t).append(",\n"));

        int lastIndexOf = sb.lastIndexOf(",");
        if (lastIndexOf > 0) {
            sb.deleteCharAt(lastIndexOf);
        }

        sb.append(" ) \n");


        //规则执行动作
        sb.append("then \n");

        //规则执行结果
        sb.append("  ").append(objectVar).append(".set").append(StringUtils.capitalize(resultPropertyName)).append("(");
        RuleResult ruleResult = RuleResult.valueOf(expenseRule.getRuleAction());
        sb.append(RuleResult.class.getSimpleName()).append(".").append(ruleResult.name()).append("); \n");

        //规则执行提示信息
        sb.append("  ").append(objectVar).append(".set").append(StringUtils.capitalize(alertMessagePropertyName)).append("(\"");
        //规则计算信息  $invoice.setRemarkMessage("您的<EXPENSE_TYPE>（<行号>）需进行计算 ");
        if (expenseRule.getRuleAction().equals("CALCULATE"))
            sb.append(expenseRule.getResultCalculate()).append(" \");");
        else
            sb.append(expenseRule.getAlertMessage()).append(" \");");




        sb.append("\n").append("end");

       /* String generate = sb.toString();
        KieSessionUtil kieSessionUtil = new KieSessionUtil();
        kieSessionUtil.addContent(generate, ResourceType.DRL);
        try {
            kieSessionUtil.verifyRules();
        } catch (Exception e) {
            throw new RuntimeException(generate, e);
        }
        expenseRule.setDroolsRule(generate);*/
        expenseRule.setDroolsRule(sb.toString());

        return expenseRule.getDroolsRule();
    }
    //{EXP_LEVEL}IN[一般员工] AND {ER_LOCATION}NOT IN[北京/上海/深圳] AND {ER_EXP_FEE}>{ER_DAYS}*300
    private List<String> generateWhenContent() {
        String rule = expenseRule.getRule().trim();
        if (rule.contains("OR") || rule.contains("or")) {
            throw new UnsupportedOperationException("请知悉drools 的基本使用：复杂的规则拆成简单的规则执行，or的规则请拆分");
        }

        if (rule.contains("and")) {
            throw new UnsupportedOperationException("AND条件单词大写");
        }


        List<String> ruelList = Splitter.on(Pattern.compile("AND")).trimResults().splitToList(rule);

        List<String> rules = ruelList.stream().map(this::generateARule).collect(Collectors.toList());
        return rules;
    }

    //去除AND条件的一条简单规则
    private String generateARule(final String rule) {
        if (StringUtils.deleteWhitespace(rule).contains("NOTIN")) {
            Pair<String, String> pair = getRulePropertyNameAliasAndValuesForIn(rule, "NOT");
            return pair.getKey() + " not in " + pair.getValue();
        }

        if (rule.contains("IN")) {
            Pair<String, String> pair = getRulePropertyNameAliasAndValuesForIn(rule, "IN");
            return pair.getKey() + " in " + pair.getValue();
        }

        if (rule.contains(">")) {
            return getRulePropertyNameAliasAndValuesForCompare(rule, ">");
        }
        if (rule.contains("!=")) {
            return getRulePropertyNameAliasAndValuesForCompare(rule, "!=");
        }
        if (rule.contains("=")) {
            return getRulePropertyNameAliasAndValuesForCompare(rule, "=");
        }

        throw new UnsupportedOperationException(rule);
    }

    private String generateACaculate(final String rule) {

        MatchInvoiceExpenselRuleRequestDto t = new MatchInvoiceExpenselRuleRequestDto();
        t.getData().put("","");
        t.setData(t.getData());

        return null;

    }

    //{EXP_LEVEL}IN[一般员工] , {ER_LOCATION}NOT IN[北京/上海/深圳]  to
    //data["EXPENSE_LEVEL"] in ("一般员工"),	data["city"] not in ("上海","北京","深圳","广州"),
    private Pair<String, String> getRulePropertyNameAliasAndValuesForIn(final String rule, final String separators) {
        String[] splitByWholeSeparators = StringUtils.splitByWholeSeparator(rule, separators);

        String name = splitByWholeSeparators[0];
        if (splitByWholeSeparators.length > 2) {
            name = name + "IN" + splitByWholeSeparators[1];
        }

        name = getRulePropertyNameAlias(name).getValue();

        name = "data[\"" + name + "\"]";

        String value = StringUtils.substring(rule
                , StringUtils.lastIndexOf(rule, "[") + 1
                , StringUtils.lastIndexOf(rule, "]"));

        List<String> values = Splitter.on("/").trimResults().splitToList(value);
        value = values.stream().map(t -> "\"" + t + "\"").collect(Collectors.joining(","));
        value = "(" + value + ")";
        return Pair.of(name, value);
    }

    //{ER_EXP_FEE}>{ER_DAYS}*300     to
    // BigDecimal.valueOf(Double.valueOf(data.["INVOICE_AMOUNT"])) > 300 * Double.valueOf(data["days"])
    private String getRulePropertyNameAliasAndValuesForCompare(final String rule, final String separator) {
        if (!Arrays.asList(">", "<", "=", "!=").contains(separator.trim())) {
            throw new UnsupportedOperationException("{rule:" + rule + ",separator:" + separator + "}");
        }

        List<String> list = Splitter.on(separator.trim()).trimResults().splitToList(rule);
        String express1 = list.get(0);
        String express2 = list.get(1);
        String template1 = "BigDecimal.valueOf(Double.valueOf(data[\"%s\"]))";
        String tempalte2 = "Double.valueOf(data[\"%s\"])";

        Pair<String, String> rulePropertyNameAlias1 = getRulePropertyNameAlias(StringUtils.removeAll(express1, "\\*|\\+|-|!"));
        express1 = String.format(template1, rulePropertyNameAlias1.getValue());

        Pair<String, String> rulePropertyNameAlias2 = getRulePropertyNameAlias(StringUtils.removeAll(express2, "\\*|\\+|-|!"));

        String replace = rule.replace(rulePropertyNameAlias1.getKey(), express1);

        if (rulePropertyNameAlias2 == null) {
            if (StringUtils.removeAll(express2, "\\*|\\+|-|!").matches("^\\d+$")){
                return replace;
            }
            String operator = (separator.equals("=")) ? "==" : "!=";
            return "data[\"" + rulePropertyNameAlias1.getValue() + "\"] " + operator + " null";
        }
        express2 = String.format(tempalte2, rulePropertyNameAlias2.getValue());

        replace = replace.replace(rulePropertyNameAlias2.getKey(), express2);
        return replace;
    }

    private Pair<String, String> getRulePropertyNameAlias( String content) {

        //值为空，null
        String empty = StringUtils.deleteWhitespace(content);
        if (empty.equals("[]") || empty.equals("")) {
            return null;
        }
        if (empty.matches("^\\d+$")){
            return null;
        }

        content = StringUtils.removeAll(content,"\\d");

        if (rulePropertyNameAliasIsRegularExpression) {
            for (Map.Entry<String, String> entry : rulePropertyNameAlias.entrySet()) {
                Pattern compile = Pattern.compile(entry.getKey());
                Matcher matcher = compile.matcher(content);
                if (matcher.find()) {
                    String name = content.substring(matcher.regionStart(), matcher.regionEnd());
                    String aliasName = entry.getValue();
                    return Pair.of(name, aliasName);
                }
            }
        }

        for (Map.Entry<String, String> entry : rulePropertyNameAlias.entrySet()) {
            String name = entry.getKey();
            if (name.equals(content.trim())) {
                return Pair.of(name, entry.getValue().trim());

            }
            if (name.equals(content.trim()) || ("{" + name + "}").equals(content.trim())) {
                return Pair.of("{" + name + "}", entry.getValue().trim());

            }
        }


        throw new UnsupportedOperationException(content);
    }

    private String generateRuleName() {
        StringBuilder sb = new StringBuilder();
        sb.append(expenseRule.getCompanyName())
                .append("-")
                .append(expenseRule.getExpenseType())
                .append("-")
                .append(ruleName).append("\"");

        return sb.toString();
    }


    //{ER_SCORE}={ER_SCORE}+{ER_EXP_FEE}*1.1
    //{ER_EXP_FEE}>{ER_DAY}*1.1+{ER_PARAM}
    private static Double jsTest(String expr) {
        double result;
        try {

            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            result = (double) engine.eval(expr);
            return result;
//            System.out.println(result);
        } catch (ScriptException e) {
            log.error("【运算表达式解析失败，不符合要求】，location：edu.hubu.utils.Test");
            return -1.0;
        }

    }

    //{ER_SCORE}={ER_SCORE}+{ER_EXP_FEE}*1.1
    //{ER_EXP_FEE}>{ER_DAY}*1.1+{ER_PARAM}
    public void dealCaculate(String command, Map<String, String> map) {
        //1、将符号命令转换成数值命令
        //2、使用js引擎求值

        List<String> list = Splitter.on("=").trimResults().splitToList(command);

        String express1 = list.get(0);

        String express2 = list.get(1);

        while (express2.contains("{")&&express2.contains("}")){
            int i = express2.indexOf("{");
            int j = express2.indexOf("}");
            express2= express2.replace( express2.substring(i , j+1) , map.get(getRulePropertyNameAlias(express2.substring(i , j+1)).getValue()));
        }

        map.put(getRulePropertyNameAlias(StringUtils.removeAll(express1, "\\*|\\+|-|!")).getValue(),String.format("%.0f",jsTest(express2)));


    }



}
