package com.ccsu.mysql.service.impl;

import com.ccsu.mysql.dao.user.UserMapper;
import com.ccsu.mysql.entity.UserEntity;
import com.ccsu.mysql.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersServiceIMPL implements UsersService {

    @Autowired
    UserMapper userMapper;

    public List<UserEntity> queryUsers(){

            return  userMapper.queryUser();
    }



}
