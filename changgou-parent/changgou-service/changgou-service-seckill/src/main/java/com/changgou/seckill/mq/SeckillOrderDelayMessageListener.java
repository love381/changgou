package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.impl.SeckillOrderServiceImpl;
import entity.SeckillStatus;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "seckillQueue")
public class SeckillOrderDelayMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderServiceImpl seckillOrderService;
    /***
     * 读取消息
     * 判断Redis中是否存在对应的订单
     * 如果存在，则关闭支付，再关闭订单
     */
    @RabbitHandler
    public void consumeMessage(String message){
        //获取用户的排队信息
        SeckillStatus seckillStatus =JSON.parseObject(message,SeckillStatus.class);

        //如果此时Redis中没有用户排队信息，则表明该订单已经处理,如果有用户排队信息，则表示用户尚未完成支付,关闭订单[关闭微信支付]
        Object userQueueStatus = redisTemplate.boundHashOps("UserQueueStatus").get(seckillStatus.getUsername());
        if (userQueueStatus!=null){
            //关闭微信支付
            //删除订单
            seckillOrderService.closeOrder(seckillStatus.getUsername());
        }

    }
}
