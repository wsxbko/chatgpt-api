package cn.cutiepie.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//返回的默认结果为字符串
public class HelloWorldTest {
    @RequestMapping("/hello")//映射信息，往往是URL的组成部分
    public String hello(){
        return "Hello world";
    }

}
/*
* Tomcat 根据请求中的路径 /hello 来匹配路由规则，确定该请求应该交给哪个控制器进行处理。
* 根据 Spring MVC 的映射规则和 @RequestMapping 注解中定义的路径，Tomcat 将请求转发给匹配的 HelloWorldTest 控制器的方法进行处理*/