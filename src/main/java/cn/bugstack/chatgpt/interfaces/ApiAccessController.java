package cn.bugstack.chatgpt.interfaces;

import cn.bugstack.chatgpt.domain.security.service.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Api访问准入管理，当访问 OpenAI 接口时，需要进行准入验证
 */

@RestController
public class ApiAccessController {
    private Logger logger = LoggerFactory.getLogger(ApiAccessController.class);
    /**
     * 8080是因为服务器监听的是8080
     * http://localhost:8080/authorize?username=QTP&password=123
     */
    @RequestMapping("/authorize")
    public ResponseEntity<Map<String,String>> authorize(String username,String password){
        Map<String,String> map = new HashMap<>();
        //模拟账号和密码校验
        if(!"QTP".equals(username)||!"123".equals(password)){
            map.put("msg","用户名密码错误");
            /**
             * ResponseEntity<T>是Spring提供的一个泛型类，用于表示HTTP响应的实体
             */
            return ResponseEntity.ok(map);
        }
        //校验通过 生成token
        JwtUtil jwtUtil = new JwtUtil();
        Map<String,Object> chaim = new HashMap<>();
        chaim.put("username",username);
        String jwtToken = jwtUtil.encode(username,5*60*1000,chaim);
        map.put("msg","授权成功");
        map.put("token",jwtToken);
        //返回token码
        return ResponseEntity.ok(map);
    }
//shujuleiyuan kafka
    @RequestMapping("/verify")
    public ResponseEntity<String> verify(String token){
        logger.info("验证 token: {}",token);
        return ResponseEntity.status(HttpStatus.OK).body("verify success!");
    }
    /**
     * http://localhost/api/?token=eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTMxMzcyOTgsImlhdCI6MTY5MzEzNjk5OCwianRpIjoiMDUxNDRmOTMtN2ZiNi00NzZiLWEyNDItMjVlMWZhNzVjNDU3IiwidXNlcm5hbWUiOiJRVFAifQ.HQfdkDITZT0RB82YS9GLnCHBHgyNyEJRkLyO6PoSYNA
     */
    @RequestMapping("/success")
//    @GetMapping("/ccc/{id}")
//    public String serch(String id){
//
//    }
    public String success(){
    return "test success by QTP";
    }
}
