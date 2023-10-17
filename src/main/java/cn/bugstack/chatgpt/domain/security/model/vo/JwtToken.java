package cn.bugstack.chatgpt.domain.security.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 设置用户ID和密码
 */
/*
 * Apache Shiro 是一个功能强大且灵活的 Java 安全框架，用于身份验证、授权和会话管理等安全相关的操作
 * AuthenticationToken 接口是 Apache Shiro 中的一个核心接口，用于表示身份验证的令牌
 * */
public class JwtToken implements AuthenticationToken {

    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    /**
     * 用于返回用户的主体，即表示用户身份的对象。在身份验证过程中，该方法通常返回用户名或用户实体对象
     * 等同于账户
     * @return 账户
     */
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    /**
     * 用于返回用户的凭证，即用户提供的用于身份验证的信息。在身份验证过程中，该方法通常返回密码、令牌、证书或其他形式的凭证数据
     * @return 密码
     */
    @Override
    public Object getCredentials() {
        return jwt;
    }
}
