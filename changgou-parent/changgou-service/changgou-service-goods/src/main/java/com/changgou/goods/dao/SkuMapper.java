package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
    /***
     * 库存递减
     * @param id
     * @param num
     * @return
     */
    @Update("update tb_sku set num=num-#{num} where id=#{id} and num>=#{num}")
    int decrCount(@Param(value = "id") Long id, @Param(value = "num") Integer num);

    /**
     * 根据sku_id集合查询sku集合
     * @param skuIds
     * @return
     */
    @Select("select * from tb_sku where id in (${skuIds})")
    List<Sku> findBySkuIds(@Param("skuIds") String skuIds);
}
