package com.ccsu.mysql.service.impl;

import com.ccsu.mysql.dao.order.OrderMapper;
import com.ccsu.mysql.entity.OrderEntity;
import com.ccsu.mysql.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wenzhenyu
 * @description
 * @date 2019/3/4
 */
@Service
public class OrderServiceIMPL implements OrderService {


    @Autowired
    OrderMapper orderMapper;

    public List<OrderEntity> queryOrderList(){

        return orderMapper.queryOrderList();

    }

    public void insert(long orderID, long userId,String addressId,String price){

        orderMapper.insert(new OrderEntity(0,orderID,userId,addressId,price));
    }


    public List<OrderEntity> queryOrderList(long userId){
        return orderMapper.queryOrdersByUser(userId);
    }

}
