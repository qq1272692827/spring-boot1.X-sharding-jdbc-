package com.ccsu.mysql.service;

import com.ccsu.mysql.entity.GoodsEntity;

import java.util.List;

public interface GoodsService {

    public List<GoodsEntity> queryGoodsList();

    public void insert(String goodsID,String goodsName);

}
