package edu.hubu.rule;


import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Field;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.hubu.utils.RuleGenerate;
import edu.hubu.utils.SingletonListRules;
import edu.hubu.vo.ExpenseRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.rule
 * @Author: Deson
 * @CreateTime: 2018-08-14 11:33
 * @Description: 解析xlsx
 */
@Slf4j
public class FilloExcel {
    /*static String fileName = "RuleInformation.xlsx";
    static String sheetName = "Sheet1";


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
    public static Map<String, String> rulePropertyNameAlias = new HashMap<>();

    static {
        rulePropertyNameAlias.put("\\{.*LEVEL.*\\}", "EXPENSE_LEVEL");
        rulePropertyNameAlias.put("\\{.*LOCATION.*\\}", "city");
        rulePropertyNameAlias.put("\\{.*FEE.*\\}", "AMOUNT");
        rulePropertyNameAlias.put("\\{.*DAY.*\\}", "DAY");
        rulePropertyNameAlias.put("\\{.*SCORE.*\\}", "SCORE");
        rulePropertyNameAlias.put("\\{.*PARAM.*\\}", "PARAM");

    }

    public static void main(String[] args) {

        try {

            String filePath = FilloExcel.class.getClassLoader().getResource(fileName).getPath();

            filePath = java.net.URLDecoder.decode(filePath, "utf-8");

            System.out.println(filePath);

            Fillo fillo = new Fillo();

            Connection connection = fillo.getConnection(filePath);

            String strQuery = "Select * from sheet1";

            Recordset recordset = connection.executeQuery(strQuery);

            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            while (recordset.next()) {
                Field field = recordset.getField(1);
                System.out.println(gson.toJson(field));
            }

            SheetParser parser = new SheetParser();

            InputStream inputStream = FilloExcel.class.getClassLoader().getResourceAsStream(fileName);

            Sheet sheet = new XSSFWorkbook(inputStream).getSheet(sheetName);

            List<ExpenseRule> entityList = parser.createEntity(sheet, ExpenseRule.class, t -> t.printStackTrace());

            entityList = entityList.stream()
                    .filter(t -> t.getCompanyName() != null && !t.getCompanyName().isEmpty())
                    .collect(Collectors.toList());

            System.out.println(gson.toJson(entityList));

            Set<String> set = entityList.stream().map(ExpenseRule::getRuleId).collect(Collectors.toSet());
            if (set.size() != entityList.size()) {
                throw new IllegalArgumentException("id 有重复");
            }

            entityList.stream()
                    .map(FilloExcel::ruleGenerate)
                    .forEach(t -> {
                        System.out.println(t);
                    });

        } catch (UnsupportedEncodingException e) {
            log.error("FilloExcel filePath Decode exception :[{},row--30]", e.getMessage());
        } catch (FilloException e) {
            log.error("FilloExcel excel establish connection exception:[{},row--36]", e.getMessage());
        } catch (IOException e) {
            log.error("FilloExcel findSheet IOException exception:[{},row--62]", e.getMessage());
        } catch (Exception e) {
            log.error("FilloExcel findSheet test err:{}]", e.getMessage());
        }




        configRule();

    }

    public static void configRule() {
        List<ExpenseRule> entityList = expenseRuleEntityList();
        entityList.forEach(t-> System.out.println(t));
    }

    public static List<ExpenseRule> expenseRuleEntityList() {
        try {


            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


            List<ExpenseRule> entityList = SingletonListRules.getInstance();

            entityList = entityList.stream()
                    .filter(t -> t.getCompanyName() != null && !t.getCompanyName().isEmpty())
                    .collect(Collectors.toList());

            System.out.println(gson.toJson(entityList));

            Set<String> set = entityList.stream().map(ExpenseRule::getRuleId).collect(Collectors.toSet());
            if (set.size() != entityList.size()) {
                throw new IllegalArgumentException("id 有重复");
            }

           return entityList.stream()
                    .map(FilloExcel::ruleGenerate).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("FilloExcel findSheet test err:[{},expenseRuleEntityList]", e.getMessage());
            return null;

        }

    }

    public static ExpenseRule ruleGenerate(ExpenseRule expenseRule) {
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

    }*/


}
