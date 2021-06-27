package com.changgou.order.mq.listener;

import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import com.changgou.weixinpay.feign.WeChatPayFeign;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "orderListenerQueue")
public class DelayMessageQueueListener {
    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    /*@RabbitHandler
    public void getDelayMessage(String message){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("监听消息的时间:" + simpleDateFormat.format(new Date()));
        System.out.println("监听到的消息:"+message);
        //作业这点需要根据 orderService 和 weixinpayFeign 进行订单的回滚
    }*/
    @RabbitHandler
    public void getDelayMessage(String orderId) throws Exception {
        //作业这点需要根据 orderService 和 weixinpayFeign 进行订单的回滚
        Order order = orderService.findById(orderId);
        if ("0".equals(order.getPayStatus())){
            //0表示未支付，通知微信服务器取消订单，从数据库中删除订单，回滚库存
            Map<String, String> closeResult = weChatPayFeign.closeOrder(orderId).getData();
            //如果错误代码为ORDERPAID说明订单已经支付,当作正常订单处理，反之回滚库存
            if (!("FALL".equals(closeResult.get("result_code"))&&"ORDERPAID".equals(closeResult.get("err_code")))){
                orderService.deleteOrder(orderId);
            }
        }
    }
}
