package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import com.changgou.weixinpay.feign.WeChatPayFeign;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderPayMessageListener {
    private final static String SUCCESS = "SUCCESS";

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    /***
     * 接收消息
     */
    @RabbitHandler
    public void consumerMessage(String msg) throws Exception {
        //将数据转成map
        Map<String,String> result = JSON.parseObject(msg, Map.class);
        System.out.println("监听到的支付结果:" + result);
        //通信标识 return_code=SUCCESS
        String return_code = result.get("return_code");

        //业务结果 result_code=SUCCESS/FAIL，修改订单状态
        if (return_code.equalsIgnoreCase("success")) { //支付成功，修改订单状态
            //业务结果
            String result_code = result.get("result_code");
            //获取订单号
            String outtradeno = result.get("out_trade_no");
            if (!SUCCESS.equals(result_code)) { //交易失败，关闭订单，从数据库中将订单状态修改为支付失败，回滚库存
                Map<String, String> closeResult = weChatPayFeign.closeOrder(outtradeno).getData();  //关闭订单时服务器返回的数据
                //如果错误代码为ORDERPAID则说明订单已经支付，当作正常订单处理,反之 回滚库存
                if (!("FAIL".equals(closeResult.get("result_code")) && "ORDERPAID".equals(closeResult.get("err_code")))) {
                    orderService.deleteOrder(outtradeno);
                    return;
                }
            }
            //修改订单状态
            String transactionId = result.get("transaction_id");   //微信支付订单号
            String timeEnd = result.get("time_end");               //支付完成时间
            orderService.updateStatus(outtradeno,timeEnd,transactionId);
        }
    }
}
