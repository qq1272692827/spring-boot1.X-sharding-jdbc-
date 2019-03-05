package com.ccsu.mysql.service;


import com.ccsu.mysql.entity.OrderEntity;

import java.util.List;

public interface OrderService {

    public List<OrderEntity> queryOrderList();

    public void insert(long orderID, long userId,String addressId,String price);

    public List<OrderEntity> queryOrderList(long userId);

}
