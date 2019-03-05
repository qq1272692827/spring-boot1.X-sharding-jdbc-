package com.ccsu.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {

    private int userId;
    private String userName;
    private int gender;

}
