package cn.bugstack.chatgpt.domain.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Signature;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description 获取JwtToken，获取JwtToken中封装的信息，判断JwtToken是否存在.
 *
 * * 1. encode()，参数是=签发人，存在时间，一些其他的信息。返回值是JwtToken对应的字符串
 * * 2. decode()，参数是=JwtToken=。返回值是荷载部分的键值对
 * * 3. isVerify()，参数是=JwtToken=。返回值是这个JwtToken是否存在
 */
public class JwtUtil {
    private Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    //创建默认的秘钥和算法供无参构造方法使用
    private static final String defaultBase64EncodedSecretKey = "B*B^";
    /* 令牌的创建、传输和验证规范。SignatureAlgorithm 枚举类定义了 JWT 中可用的各种签名算法，
     *用于对 JWT 进行数字签名以保证其完整性和认证
     */
    private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;


    public JwtUtil() {
        this(defaultBase64EncodedSecretKey, defaultSignatureAlgorithm);
        //在构造方法中调用了另一个构造方法 JwtUtil(String defaultBase64EncodedSecretKey, SignatureAlgorithm defaultSignatureAlgorithm) 来实现对象的初始化
    }

    private final String base64EncodedSecretKey;
    private final SignatureAlgorithm signatureAlgorithm;
    //有参构造
    public JwtUtil(String secretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(secretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * 这里就是产生jwt字符串的地方
     * jwt字符串包括三个部分
     *  1. header
     *      -当前字符串的类型，一般都是“JWT”
     *      -哪种算法加密，“HS256”或者其他的加密算法
     *      所以一般都是固定的，没有什么变化
     *  2. payload
     *      一般有四个最常见的标准字段（下面有）
     *      iat：签发时间，也就是这个jwt什么时候生成的
     *      jti：JWT的唯一标识
     *      iss：签发人，一般都是username或者userId
     *      exp：过期时间
     * */
    /**
     * encode : 编码
     * @param issuer 签发人
     * @param ttlMillis 生存时间
     * @param claims 还需要在jwt中存储的非隐私信息
     * @return
     */
    public String encode(String issuer, long ttlMillis, Map<String,Object> claims){
        if(claims == null){
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();
        JwtBuilder  builder = Jwts.builder()
                //荷载部分
                .setClaims(claims)
                //JWT唯一标识，该方法可以生成唯一的Id
                .setId(UUID.randomUUID().toString())
                //签发时间
                .setIssuedAt(new Date(nowMillis))
                //生成jwt使用的算法和秘钥
                .signWith(signatureAlgorithm,base64EncodedSecretKey);
        if(ttlMillis >= 0){
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        //compact() 方法用于生成最终的 JWT（JSON Web Token）字符串
        return builder.compact();
    }

    /**
     * @deprecated 析JWT令牌并获取其中封装的用户信息
     * @param jwtToken
     * @return
     */
    public Claims decode(String jwtToken){
    return Jwts.parser()
            //使用.setSigningKey()方法设置签名秘钥，用于验证JWT的真实性。签名秘钥必须与生成JWT时使用的秘钥相匹配
            .setSigningKey(base64EncodedSecretKey)
            //设置需要解析的jwt
            .parseClaimsJws(jwtToken)
            .getBody();

    }

    //判断jwtToken是否合法
    public boolean isVerify(String jwtToken){
        Algorithm algorithm = null;
        switch (signatureAlgorithm) {
            case HS256:
                algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
                break;
            default:
                throw new RuntimeException("不支持该算法");
        }
        //创建了一个验证令牌的对象，使用 verifier.verify(jwtToken) 方法对 JWT 令牌进行验证。这里的 jwtToken 是待验证的 JWT 令牌，verify() 方法会对令牌进行校验
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwtToken);
        return true;

    }


}
