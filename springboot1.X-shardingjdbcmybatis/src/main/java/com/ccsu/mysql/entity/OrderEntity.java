package com.ccsu.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity implements Serializable {

    private int id;
    private long orderId;
    private long userId;
    private String addressId;
    private String price;

}
