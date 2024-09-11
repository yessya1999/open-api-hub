package com.yupi.openapigateway;

import com.yupi.project.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author yess
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
@Service
public class OpenapiGatewayApplication {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(OpenapiGatewayApplication.class, args);
        OpenapiGatewayApplication application = context.getBean(OpenapiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHello2("world");
        System.out.println("result: " + result);
        System.out.println("result: " + result2);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHello2(String name) {
        return demoService.sayHello2(name);
    }


//    // 这个注解用于创建一个 Spring Bean，即一个路由规则的构建器
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        // 创建路由规则的构建器
//        return builder.routes()
//                // 定义路由规则，给该规则起一个名字 "tobaidu"
//                .route("tobaidu", r -> r.path("/baidu")
//                        // 将满足 "/baidu" 路径的请求转发到 "https://www.baidu.com"
//                        .uri("https://www.baidu.com"))
//                // 定义路由规则，给该规则起一个名字 "toyupiicu"
//                .route("toyupiicu", r -> r.path("/yupiicu")
//                        // 将满足 "/yupiicu" 路径的请求转发到 "http://yupi.icu"
//                        .uri("http://yupi.icu"))
//                // 创建并返回路由规则配置对象
//                .build();
//    }

}



