package com.ccsu.mysql.datasource.rule;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * @author wenzhenyu
 * @description 分库分表算法
 * @date 2019/3/4
 */

public class TableShardingAlgorithmRule implements PreciseShardingAlgorithm<Long> {


    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        for (String each : collection) {

            System.out.println("分表的规则："+each+"--"+preciseShardingValue.getValue().toString());
            if (each.endsWith(Long.parseLong(preciseShardingValue.getValue().toString()) % 2+"")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

}
