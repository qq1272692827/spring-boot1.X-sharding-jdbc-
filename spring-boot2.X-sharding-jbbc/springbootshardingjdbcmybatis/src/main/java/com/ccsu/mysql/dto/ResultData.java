package com.ccsu.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wenzhenyu
 * @description 返回实体
 * @date 2019/3/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultData<T> implements Serializable {

    private int code;
    private String msg;
    private T data;
}
