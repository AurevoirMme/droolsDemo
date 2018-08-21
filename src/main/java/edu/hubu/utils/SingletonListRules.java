package edu.hubu.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.hubu.rule.FilloExcel;
import edu.hubu.vo.ExpenseRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-20 22:32
 * @Description: 解析后的RulesEntity
 */
@Slf4j
public class SingletonListRules {

    private static String fileName = "RuleInformation.xlsx";
    private static String sheetName = "Sheet1";

    private static volatile List<ExpenseRule> instance;


    /*
    static String fileName = "RuleInformation.xlsx";
    static String sheetName = "Sheet1";*/

    static String packageName = "rules";
    //    static String packageName = "edu.hubu.rule";
    static List<String> importPackages = Arrays.asList(
            "java.math.*"
            , "edu.hubu.entity.*"
            ,"edu.hubu.dto.*"
            ,"edu.hubu.enums.*");



    static String objectType = "MatchInvoiceExpenselRuleRequestDto";
    static String objectVar = "$invoice";

    static String resultPropertyName = "result";
    static String alertMessagePropertyName = "remarkMessage";

    //规则内容属性别名，如{EXP_LEVEL}IN[一般会员]，EXP_LEVEL 可以起别名
    private static Map<String, String> rulePropertyNameAlias = new HashMap<>();

    static {
        rulePropertyNameAlias.put("\\{.*LEVEL.*\\}", "EXPENSE_LEVEL");
        rulePropertyNameAlias.put("\\{.*LOCATION.*\\}", "city");
        rulePropertyNameAlias.put("\\{.*FEE.*\\}", "AMOUNT");
        rulePropertyNameAlias.put("\\{.*DAY.*\\}", "DAY");
        rulePropertyNameAlias.put("\\{.*SCORE.*\\}", "SCORE");
        rulePropertyNameAlias.put("\\{.*PARAM.*\\}", "PARAM");

    }

    public static Map<String, String> getRulePropertyNameAlias() {
        return rulePropertyNameAlias;
    }

    private SingletonListRules() {

        instance = this.getInstance(fileName, sheetName);
    }

    public static List<ExpenseRule> getInstance() {

        if (instance == null) {

            synchronized (SingletonListRules.class) {

                if (instance == null) {

                    instance = SingletonListRules.expenseRuleEntityList(fileName, sheetName);

                }
            }
        }

        return instance;
    }

    private static List<ExpenseRule> getInstance(String fileName, String sheetName) {

        try {

            SheetParser parser = new SheetParser();

            InputStream inputStream = FilloExcel.class.getClassLoader().getResourceAsStream(fileName);

            Sheet sheet = new XSSFWorkbook(inputStream).getSheet(sheetName);

            List<ExpenseRule> entityList = parser.createEntity(sheet, ExpenseRule.class, t -> t.printStackTrace());

            return entityList;
        } catch (IOException e) {

            log.error("package edu.hubu.utils.SingletonListRules getInstance(String fileName, String sheetName) IOException exception:【{}】", e.getMessage());
            return null;
        }
    }

    private static List<ExpenseRule> expenseRuleEntityList(String fileName, String sheetName) {
        try {


            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


            List<ExpenseRule> entityList =SingletonListRules.getInstance(fileName, sheetName);

            entityList = entityList.stream()
                    .filter(t -> t.getCompanyName() != null && !t.getCompanyName().isEmpty())
                    .collect(Collectors.toList());

            System.out.println(gson.toJson(entityList));

            Set<String> set = entityList.stream().map(ExpenseRule::getRuleId).collect(Collectors.toSet());
            if (set.size() != entityList.size()) {
                throw new IllegalArgumentException("id 有重复");
            }

            return entityList.stream()
                    .map(SingletonListRules::ruleGenerate).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("FilloExcel findSheet test err:[{},expenseRuleEntityList]", e.getMessage());
            return null;

        }

    }

    private static ExpenseRule ruleGenerate(ExpenseRule expenseRule) {
        RuleGenerate ruleGenerate = new RuleGenerate();
        ruleGenerate.setPackageName(packageName);
        ruleGenerate.setImportPackages(importPackages);
        ruleGenerate.setRuleName(expenseRule.getRule());
        ruleGenerate.setObjectType(objectType);
        ruleGenerate.setObjectVar(objectVar);
        ruleGenerate.setExpenseRule(expenseRule);
        ruleGenerate.setResultPropertyName(resultPropertyName);
        ruleGenerate.setAlertMessagePropertyName(alertMessagePropertyName);
        ruleGenerate.setRulePropertyNameAlias(rulePropertyNameAlias);
        String generate = ruleGenerate.Generate();
        expenseRule.setDroolsRule(generate);
        return expenseRule;

    }

}
