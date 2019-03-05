package com.ccsu.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wenzhenyu
 * @description 分库分表sharding-jdbc实现
 * @date 2019/3/4
 */

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class ShardingjdbcApplication {

    public static void main(String[]args){
        SpringApplication.run(ShardingjdbcApplication.class,args);

    }

}
