package edu.hubu.utils;

import edu.hubu.vo.ExpenseRule;
import org.kie.api.io.ResourceType;

import java.util.List;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-21 10:32
 * @Description: 规则KieSessionUtil单例
 */
public class SingletonKieSession {

    private SingletonKieSession() {}

    public static KieSessionUtil getInstance() {

        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;

        private KieSessionUtil singleton;
        //JVM会保证此方法绝对只调用一次
        Singleton(){
            singleton = new KieSessionUtil();
            List<ExpenseRule> entityList = SingletonListRules.getInstance();
            entityList.forEach(t -> singleton.addContent(t.getDroolsRule(), ResourceType.DRL));
        }
        public KieSessionUtil getInstance(){
            return singleton;
        }
    }
}
