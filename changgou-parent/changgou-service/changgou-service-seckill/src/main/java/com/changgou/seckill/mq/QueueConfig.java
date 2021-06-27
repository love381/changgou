package com.changgou.seckill.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 1.延时超时队列->负责数据暂时存储  Queue1
 * 2.真正监听的消息队列             Queue2
 */
@Configuration
public class QueueConfig {

    /***
     *延时超时队列->负责数据暂时存储  Queue1
     */
    public Queue DelaySeckillQueue(){
        return QueueBuilder.durable("delaySeckillQueue")
                .withArgument("x-dead-letter-exchange","seckillExchange") // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key","seckillQueue") // 绑定指定的routing-key
                .build();
    }

    /***
     *真正监听的消息队列 Queue2
     */
    @Bean
    public Queue seckillQueue() {
        return new Queue("seckillQueue");
    }
    /***
     * 秒杀交换机
     */
    public Exchange seckillExchange(){
        return new DirectExchange("seckillExchange");
    }


    /***
     * 交换机与队列绑定
     * @return
     */
    @Bean
    public Binding SeckillQueueBingdingExchange(Queue seckillQueue,Exchange seckillExchange) {
        return BindingBuilder.bind(seckillQueue).to(seckillExchange).with("seckillQueue").noargs();
    }
}

