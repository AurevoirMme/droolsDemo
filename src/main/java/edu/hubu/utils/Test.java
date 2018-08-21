package edu.hubu.utils;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-20 15:15
 * @Description:
 */
@Slf4j
public class Test {
    /*//规则内容属性别名，如{EXP_LEVEL}IN[一般会员]，EXP_LEVEL 可以起别名
    static Map<String, String> rulePropertyNameAlias = new HashMap<>();

    private static boolean rulePropertyNameAliasIsRegularExpression = true;
    static {
        rulePropertyNameAlias.put("\\{.*LEVEL.*\\}", "EXPENSE_LEVEL");
        rulePropertyNameAlias.put("\\{.*LOCATION.*\\}", "city");
        rulePropertyNameAlias.put("\\{.*FEE.*\\}", "AMOUNT");
        rulePropertyNameAlias.put("\\{.*DAY.*\\}", "DAY");
        rulePropertyNameAlias.put("\\{.*SCORE.*\\}", "SCORE");
        rulePropertyNameAlias.put("\\{.*PARAM.*\\}", "PARAM");

//        rulePropertyNameAlias.put("\\{.*DAYS.*\\}", "days");
    }

    private static Pair<String, String> getRulePropertyNameAlias(String content) {

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
    public static void main(String[] args) {
       *//* String expr = "(1+2/5)*3";
        jsTest(expr);*//*
        String command = "{ER_SCORE}={ER_SCORE}+{ER_EXP_FEE}*1.1";

        //{ER_SCORE}+{ER_EXP_FEE}*1.1
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("AMOUNT", "300");
        map.put("SCORE", "8");
        map.put("DAY", "12");
        map.put("PARAM", "100");
        dealCaculate(command, map);
        System.out.println(map.get("SCORE"));

    }


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
    public static void dealCaculate(String command, Map<String, String> map) {
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


    }*/
}
