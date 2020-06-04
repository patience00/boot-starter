# boot-starter
SpringBoot开发脚手架

1.swagger的yml配置

```yaml
swagger:
  enable: true  #是否启用swagger文档,生产建议关闭
  headers:
    Authorization: dfsfsfwerwe #可以配置swagger文档中的自定义header
    myHeader: ffffff
  base-packages: com.baidu  #哪些包下的controller需要生成swagger文档
```



2.使用线程池

yml配置:

```yml
system:
  core-pool-size: 50
  maximum-pool-size: 100
  keep-alive-time: 0
  blocking-queue-size: 5000
```

注入使用:

```java
    @Resource(name = "threadPool")
    private ExecutorService executorService;
```

3.redis分布式锁

```java
 @Autowired
 private RedisHelper redisHelper;


// 第一个参数为key,第二个为过期时间的时间戳,第三个为自旋时长 
boolean lock = redisHelper.lock("redis_lock#" + id, System.currentTimeMillis() + 30000L, 1000L);

```

4.全局异常处理:

脚手架中已配置好全局异常处理

5.ElasticSearch:

最新版支持7.6.2的ES,只需按yml提示配置好就行

```yaml
spring:
  elasticsearch:
    rest:
      password: ${ES_PASSWORD}
      username: ${ES_USERNAME}
      uris: ${ES_URI}
```

