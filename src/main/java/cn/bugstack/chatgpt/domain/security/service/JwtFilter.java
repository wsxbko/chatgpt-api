package cn.bugstack.chatgpt.domain.security.service;

import cn.bugstack.chatgpt.domain.security.model.vo.JwtToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessControlFilter 抽象类定义了需要进行访问控制的过滤器应该具备的方法和功能。
 * 可以创建自定义的访问控制过滤器，并在 Apache Shiro 中使用它来控制对受保护资源的访问
 */
public class JwtFilter extends AccessControlFilter {
    private Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    //判断当前请求是否被允许访问（即是否有权限）
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    //当访问被拒绝时，将会调用这个方法。可以在这个方法中执行一些额外的处理，例如重定向到登录页面或返回错误信息
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //假设设定的token放在header中
        JwtToken jwtToken = new JwtToken(request.getParameter("token"));

        try {
            /**
             * getSubject(servletRequest, servletResponse).login(jwtToken) 的作用是使用 Subject 对象进行用户的身份验证和登录操作。
             * 在这段代码中，getSubject(servletRequest, servletResponse) 方法返回一个 Subject 对象，表示当前正在访问应用程序的用户。
             * 然后，通过调用 login(jwtToken) 方法，将传入的 jwtToken 进行身份验证和登录操作。
             * 具体而言，jwtToken 是一个包含用户身份信息的令牌（Token），可能是从请求的参数中获取的。该令牌将被传递给 Subject 对象的 login 方法，用于验证用户身份
             */
            getSubject(servletRequest,servletResponse).login(jwtToken);
            return true;
        } catch (Exception e) {
            logger.error("鉴权认证失败",e);
            onLoginFail(servletResponse);
            return false;
        }



    }
    /*
    * 授权失败默认返回401*/
    private void onLoginFail(ServletResponse response) throws IOException{
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Auth Err! write by QTP : ) ");
    }


}
