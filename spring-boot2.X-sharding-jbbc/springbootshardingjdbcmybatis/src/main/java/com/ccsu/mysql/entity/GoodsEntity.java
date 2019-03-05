package com.ccsu.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenzhenyu
 * @description 商品
 * @date 2019/3/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsEntity {

    private int id;
    private String goodsId;
    private String goodsName;


}
