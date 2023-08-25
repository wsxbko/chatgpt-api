可以更方便的在application.properties配置文件中进行配置
eg:
mysql.jdbcName= CutiePiePie
然后可以通过注解注入属性值
eg: @Value("${mysql.jdbcName"})
但假如有很多个自定义熟悉，可以定义一个配置类
eg:
@Component
@ConfigurationProperties(prefix="mysql")