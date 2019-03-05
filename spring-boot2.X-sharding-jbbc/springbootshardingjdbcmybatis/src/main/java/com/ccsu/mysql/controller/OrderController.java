package com.ccsu.mysql.controller;

import com.ccsu.mysql.dto.ResultData;
import com.ccsu.mysql.entity.GoodsEntity;
import com.ccsu.mysql.entity.OrderEntity;
import com.ccsu.mysql.entity.UserEntity;
import com.ccsu.mysql.service.GoodsService;
import com.ccsu.mysql.service.OrderService;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/query")
    public ResultData test(){

        List<OrderEntity>  resultData = orderService.queryOrderList();
        return  new ResultData(0,"success",resultData);
    }

    @RequestMapping("/add")
    public ResultData insert(@RequestParam long orderId,@RequestParam long userId,@RequestParam String addressId,@RequestParam String price){
        orderService.insert(orderId,userId,addressId,price);
        return  new ResultData(0,"success",null);
    }



    @RequestMapping("/queryByUser")
    public ResultData insert(@RequestParam long userId){
        List<OrderEntity> users = orderService.queryOrderList(userId);
        return  new ResultData(0,"success",users);
    }






}
