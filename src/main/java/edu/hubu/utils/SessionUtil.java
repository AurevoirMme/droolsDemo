package edu.hubu.utils;

import edu.hubu.vo.ExpenseRule;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-09-13 14:36
 * @Description: 获得Session
 */
public class SessionUtil {

    private SessionUtil() {}

    public static KieSession getInstance() {

        return SessionUtil.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;

        private KieSession singleton;
        //JVM会保证此方法绝对只调用一次
        Singleton(){
            singleton =  SingletonKieSession.getInstance().build().newKieSession();
        }
        public KieSession getInstance(){
            return singleton;
        }
    }
}
