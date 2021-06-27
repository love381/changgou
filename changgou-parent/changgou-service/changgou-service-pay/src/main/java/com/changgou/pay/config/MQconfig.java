package com.changgou.pay.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;



@Configuration
public class MQconfig {

    @Autowired
    private Environment env;

    /*****普通订单队列创建*****/
    /***
     * 创建队列
     * @return
     */
    @Bean
    public Queue orderQueue(){
        return new Queue(env.getProperty("mq.pay.queue.order"));
    }

    /***
     * 创建交换机
     * @return
     */
    @Bean
    public Exchange orderExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"),true,false);
    }

    /***
     * 队列绑定交换机
     * @param orderQueue
     * @param orderExchange
     * @return
     */
    @Bean
    public Binding orederQueueExchange(Queue orderQueue,Exchange orderExchange){
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }
    /*****秒杀订单队列创建*****/

    @Bean
    public Queue orderSeckillQueue(){
        return new Queue(env.getProperty("mq.pay.queue.seckillorder"));
    }

    /***
     * 创建交换机
     * @return
     */
    @Bean
    public Exchange orderSeckillExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.seckillorder"),true,false);
    }

    /***
     * 队列绑定交换机
     * @param orderSeckillQueue
     * @param orderSeckillExchange
     * @return
     */
    @Bean
    public Binding orederSeckillQueueExchange(Queue orderSeckillQueue,Exchange orderSeckillExchange){
        return BindingBuilder.bind(orderSeckillQueue).to(orderSeckillExchange).with(env.getProperty("mq.pay.routing.seckillkey")).noargs();
    }
}
