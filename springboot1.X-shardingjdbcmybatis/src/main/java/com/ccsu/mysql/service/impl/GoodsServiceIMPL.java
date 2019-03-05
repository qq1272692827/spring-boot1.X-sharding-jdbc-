package com.ccsu.mysql.service.impl;

import com.ccsu.mysql.entity.GoodsEntity;
import com.ccsu.mysql.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wenzhenyu
 * @description
 * @date 2019/3/4
 */
@Service
public class GoodsServiceIMPL implements GoodsService {


//    @Autowired
//    GoodsMapper goodsMapper;

    public List<GoodsEntity> queryGoodsList(){
//        return goodsMapper.queryGoodsList();
        return null;
    }

    public void insert(String goodsID,String goodsName){

//        goodsMapper.insert(new GoodsEntity(0,goodsID,goodsName));
    }


}
