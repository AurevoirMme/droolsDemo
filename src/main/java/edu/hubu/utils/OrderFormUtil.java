package edu.hubu.utils;

import edu.hubu.exception.DroolsDemoException;
import edu.hubu.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-28 14:43
 * @Description: 创建压测数据
 */
@Slf4j
public class OrderFormUtil {
   /* "score": "332",(0-100w)
            "level": "2",(1-100)
            "fee": "32",
            "count": "9",(1-1k)
            "location": "湖北省",
            "company": "hubu5"*/
    public static void createOrderForm(int count) throws Exception {
        String p = " 北京市，天津市，上海市，重庆市，河北省，山西省，辽宁省，吉林省，黑龙江省，江苏省，浙江省，安徽省，福建省，江西省，山东省，河南省，湖北省，湖南省，广东省，海南省，四川省，贵州省，云南省，陕西省，甘肃省，青海省，台湾省，内蒙古自治区，广西壮族自治区，西藏自治区，宁夏回族自治区，新疆维吾尔自治区，香港特别行政区，澳门特别行政区";
        List<String> provinces =Arrays.asList(p.split("，"));

        List<OrderForm> orderForms = new ArrayList<>(count);

        for (int j = 0; j < count; j++) {
            OrderForm o = new OrderForm();
            o.setScore(randomStr(0, 100 * 10000));
            int i = Integer.valueOf(o.getScore());
            int level = i / 10000;
            o.setLevel(String.valueOf(level));
            o.setFee(randomStr(1, i/100));
            if (i == 0) {
                o.setCount("0");
            } else {
                o.setCount(randomStr(1, i/1000));
            }
            o.setLocation(provinces.get(random(0, provinces.size() - 1)));
            o.setCompany("hubu"+random(1,50));
            orderForms.add(o);
        }

        System.out.println("create OrderForms");

        File file = new File("D:/OrderForms.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);

       orderForms.forEach(o->{
           StringBuilder sb = new StringBuilder();
           Class clazz = o.getClass();
           Field[] fields = o.getClass().getDeclaredFields();

           for(Field field:fields){
               try {
                   PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                   Method getMethod = pd.getReadMethod();//获得get方法
                   getMethod.invoke(o);//此处为执行该Object对象的get方法
                   sb.append(getMethod.invoke(o)).append(",");

               } catch (IntrospectionException e) {
                   log.error("【err】:{}",e.getMessage());
               }catch (InvocationTargetException e) {
                   log.error("【err】:{}",e.getMessage());
               }catch (IllegalAccessException e){
                   log.error("【err】:{}",e.getMessage());
               }
           }
           String row = sb.substring(0, sb.length() - 1);

           try {
               raf.seek(raf.length());
               raf.write(row.getBytes());
               raf.write("\r\n".getBytes());
           } catch (IOException e) {
               log.error("创建文件错误【err】：",e.getMessage());
           }

           System.out.println("write to file : " +row);
       });
        raf.close();

        System.out.println("over");

    }

    private static String i2s(int i) {
        return String.valueOf(i);
    }


    private static int random(int a, int b) {
        if (a > b) {
            log.error("生成随机数出错：{}>{}",a,b);
            throw new DroolsDemoException(-1, "生成随机数出错");
        }
        Random rand = new Random();
        return rand.nextInt(b - a + 1) + a;
    }

    private static String randomStr(int a, int b) {
        return i2s(random(a, b));
    }
    public static void main(String[] args) throws Exception {
        createOrderForm(10);
    }
}
