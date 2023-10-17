package cn.bugstack.chatgpt.domain.security.service.realm;


import cn.bugstack.chatgpt.domain.security.service.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义的验证服务
 */

/*
AuthorizingRealm 是 Shiro 框架中的一个核心类，用于实现授权和认证的领域（Realm）
AuthorizingRealm 是一个抽象类，提供了实现授权和认证逻辑的基本结构和方法，可以作为自定义领域的基类
* */
public class JwtRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    private static JwtUtil jwtUtil = new JwtUtil();

    /**
     * @deprecated 判断当前领域（Realm）是否支持指定类型的 AuthenticationToken
     * @param token 入参 AuthenticationToken 是一个接口，表示用户身份验证的令牌
     *              在这个方法中，开发人员可以根据实际情况判断当前领域是否支持该类型的令牌
     * @return 表示当前领域是否支持指定类型的令牌
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token);
    }

    /**
     * 授权（Authorization）：doGetAuthorizationInfo() 方法用于从数据源获取用户的授权信息，如用户拥有的角色和权限列表。
     * 子类需要根据实际情况重写该方法，完成用户授权逻辑
     * @param principalCollection 用户的身份信息集合
     *                            通常包含用户的主要身份标识，如用户名
     * @return 用户的授权信息
     * 包含用户拥有的角色和权限列表
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * @deprecated  用于封装用户身份验证的信息
     * @param authenticationToken 用户身份验证的令牌
     * @return 用户的身份验证信息结果
     * 它通常包含从数据源中获取到的用户信息，如用户名、密码、加密盐等
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String jwt = (String) authenticationToken.getPrincipal();
        if(jwt == null){
            throw new NullPointerException("jwtToken 不允许为空");
        }
        //判断
        if(!jwtUtil.isVerify(jwt)){
            throw new UnknownAccountException();
        }
        //可以获取username信息，并做一些处理
        String username = (String)jwtUtil.decode(jwt).get("username");
        return new SimpleAuthenticationInfo(jwt,jwt,"JwtRealm");
    }
}
