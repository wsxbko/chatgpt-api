package cn.bugstack.chatgpt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Application {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(String token) {
        logger.info("验证 token：{}", token);
        if ("success".equals(token)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/success")
    public String success(){
        return "test success by xfg";
    }

}


/*
* 具体来说，@RestController 注解是 @Controller 和 @ResponseBody 的结合体
* @Controller 用于将一个类声明为控制器
* @ResponseBody 则指示 Spring MVC 将方法的返回值直接作为 HTTP 响应的内容进行输出
* @RestController 注解可以简化编写 RESTful Web 服务的代码
* */


    /*
    @RequestMapping 可以处理各种 HTTP 请求方法（GET、POST、PUT、DELETE 等），并可以指定更多的请求参数。
    @GetMapping 是专门用于处理 HTTP GET 请求的注解。它只处理 GET 请求，并且省略了其他请求方法的定义。
    * */
    //localhost/api/?token=success
//记得加入端口，端口是localhost:11180

/*
* ResponseEntity<String> 是 Spring Framework 中的一个类，用于表示 HTTP 响应的实体。
* 它将响应的数据和相关的元数据（如状态码、头部信息等）封装在一个对象中，并可以方便地进行处理和返回
*
* 通过调用 build() 方法创建一个完整的 ResponseEntity 实例，并将其返回给客户端
* */