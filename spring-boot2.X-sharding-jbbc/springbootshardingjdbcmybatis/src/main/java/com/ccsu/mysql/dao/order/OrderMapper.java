package com.ccsu.mysql.dao.order;

import com.ccsu.mysql.entity.GoodsEntity;
import com.ccsu.mysql.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Qualifier("readSqlSessionFactory")
public interface OrderMapper {

    public List<OrderEntity> queryOrderList();

    public void insert(OrderEntity orderEntity);

    public List<OrderEntity> queryOrdersByUser(long userId);

}
