package com.changgou;

import entity.FeignInterceptor;
import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign","com.changgou.weixinpay.feign"})
@MapperScan(basePackages = {"com.changgou.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

    /***
     * 将Feign调用拦截器注入到容器中
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }


    /***
     * 创建IdWorker
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}
