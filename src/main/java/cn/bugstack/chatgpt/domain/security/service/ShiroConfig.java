package cn.bugstack.chatgpt.domain.security.service;

import cn.bugstack.chatgpt.domain.security.service.realm.JwtRealm;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Shiro 配置启动
 * Shiro在应用程序中提供身份验证、授权、加密和会话管理等安全功能
 * 下面代码的流程就是需要一个ShiroFilter过滤器，创建过滤器需要Manager，Manager的创建需要Factory,通过@Bean注解方法返回
 * 实例对象然后将对象注入进容器里，可以在方法里构建自定义类然后继承 提供的父类并重写他的方法
 *
 */
@Configuration
public class ShiroConfig {

    //在Config配置类里面把工厂方法作为Bean注入，在方法里面自定义了一个工厂类继承DWSubjectFactory重写里面的createSubject方法并返回

    @Bean
    public SubjectFactory subjectFactory(){
        class JwtDefaultSubjectFactory extends DefaultWebSubjectFactory{
            /*
            * Subject 对象表示当前正在与应用程序交互的用户
            * 它封装了与认证、授权和会话等相关的操作，并提供了一组方法来执行这些操作*/
            @Override
            public Subject createSubject(SubjectContext context) {
                return super.createSubject(context);
            }
        }
        return new JwtDefaultSubjectFactory();
    }
    @Bean
    public Realm realm(){
        return new JwtRealm();
    }

    /**
     * @deprecated 负责管理安全相关的对象
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置Realm，将用于进行身份验证和授权的逻辑
        securityManager.setRealm(realm());
        //关闭ShiroDAO功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        //不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setSubjectFactory(subjectFactory());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        //设置SecurityManager，将用于处理身份验证和授权的逻辑
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/unauthenticated");
        shiroFilter.setUnauthorizedUrl("/unauthorized");
        //添加jwt过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt",new JwtFilter());
        shiroFilter.setFilters(filterMap);
        return shiroFilter;
    }
}
