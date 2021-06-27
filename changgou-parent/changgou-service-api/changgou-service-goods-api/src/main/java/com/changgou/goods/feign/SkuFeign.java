package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.changgou.goods.feign *
 * @since 1.0
 */
@FeignClient(value="goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * 修改sku map集合
     * @param map
     * @return
     */
    @PutMapping(value="/update/map")
    Result updateMap(@RequestBody Map<Long,Sku> map);

    /**
     * 根据sku_id集合查询sku集合
     * @param skuIds
     * @return
     */
    @PostMapping("/list/ids")
    Result<List<Sku>> findBySkuIds(@Param("skuIds") List<Long> skuIds);

    /***
     * 商品库存递减
     * @param decrmap Map<key,value> 其中key:要递减的商品ID value:要低见的数量
     * @return
     */
    @GetMapping(value = "/decr/count")
    Result decrCount(@RequestParam Map<String,Integer> decrmap);


    /**
     * 根据spuId查询商品库存信息生成购物车商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable Long id);

    /**
     * 查询符合条件的状态的SKU的列表
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable(name="status") String status);

    /**
     * 条件查询Sku返回数据生成详情页面
     *
     * @param sku
     * @return
     */
    @PostMapping(value = "/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

}
