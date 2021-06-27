package com.changgou.order.dao;
import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:OrderItem的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface OrderItemMapper extends Mapper<OrderItem> {
    @Select("select * from tb_order_item where order_id = #{orderId}")
    List<OrderItem> findByOrderId(String orderId);
}
