package com.ccsu.mysql.controller;

import com.ccsu.mysql.dto.ResultData;
import com.ccsu.mysql.entity.GoodsEntity;
import com.ccsu.mysql.entity.OrderEntity;
import com.ccsu.mysql.entity.UserEntity;
import com.ccsu.mysql.service.GoodsService;
import com.ccsu.mysql.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wenzhenyu
 * @description 测试
 * @date 2019/3/4
 */
@RestController
public class TestController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    UsersService usersService;

    @RequestMapping("/query")
    public ResultData test(){

        List<GoodsEntity>  resultData = goodsService.queryGoodsList();
        return  new ResultData(0,"success",resultData);
    }

    @RequestMapping("add")
    public ResultData insert(@RequestParam String goodsId,@RequestParam String goodsName){
        goodsService.insert(goodsId,goodsName);
        return  new ResultData(0,"success",null);
    }


    @RequestMapping("/queryByUser")
    public ResultData insert(){
        List<UserEntity> users = usersService.queryUsers();
        return  new ResultData(0,"success",users);
    }







}
