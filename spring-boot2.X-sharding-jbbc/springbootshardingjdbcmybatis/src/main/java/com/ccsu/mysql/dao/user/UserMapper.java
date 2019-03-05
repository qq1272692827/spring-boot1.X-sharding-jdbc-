package com.ccsu.mysql.dao.user;

import com.ccsu.mysql.entity.OrderEntity;
import com.ccsu.mysql.entity.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Qualifier("dataSource0")
public interface UserMapper {

    public List<UserEntity> queryUser();

//    public

//    public void insert(OrderEntity orderEntity);
}
