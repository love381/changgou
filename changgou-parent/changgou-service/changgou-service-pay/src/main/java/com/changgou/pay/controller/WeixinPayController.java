package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Autowired
    private WeixinPayService wexinPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.pay.exchange.order}")
    private String exchange;

    @Value("${mq.pay.queue.order}")
    private String queue;

    @Value("${mq.pay.routing.key}")
    private String routing;


    /***
     * 支付回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request){
        InputStream inStream;
        try {
            //读取支付回调数据
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 将支付回调数据转换成xml字符串
            String result = new String(outSteam.toByteArray(), "UTF-8");
            //将xml字符串转换成Map结构
            Map<String, String> map = WXPayUtil.xmlToMap(result);

            //获取自定义参数
            String attach = map.get("attach");
            Map<String,String> accachMap = JSON.parseObject(attach, Map.class);

            //将消息发送给RabbitMq
            //rabbitTemplate.convertAndSend(exchange,routing, JSON.toJSONString(map));
            rabbitTemplate.convertAndSend(accachMap.get("exchange"),accachMap.get("routingkey"),JSON.toJSONString(map));



            //响应数据设置
            Map respMap = new HashMap();
            respMap.put("return_code","SUCCESS");
            respMap.put("return_msg","OK");
            return WXPayUtil.mapToXml(respMap);
        } catch (Exception e) {
            e.printStackTrace();
            //记录错误日志
        }
        return null;
    }

    /***
     * 检测支付状态
     * @param outtradeno
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String outtradeno){
        Map<String,String> resultMap = wexinPayService.queryPayStatus(outtradeno);
        return new Result(true,StatusCode.OK,"查询状态成功！",resultMap);
    }
    /***
     * 创建二维码
     * *
     * 普通订单:
     *          exchange : exchange.order
     *          routingkey: queue.order
     * 秒杀订单:
     *          exchange:exchange.seckillorder
     *          routingkey:queue.seckillorder
     *     exchange+routingkey->打包成JSON->存到attach发送给微信服务器 实现自定义数据传递
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(@RequestParam Map<String,String> parameterMap){
        Map<String,String> resultMap = wexinPayService.createNative(parameterMap);
        return new Result(true, StatusCode.OK,"创建二维码预付订单成功！",resultMap);
    }
}
