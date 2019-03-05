package com.ccsu.mysql.datasource.rule;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author wenzhenyu
 * @description 分库
 * @date 2019/3/4
 */

public class DatabaseShardingAlogorithmRule implements SingleKeyDatabaseShardingAlgorithm<Long> {

    @Override
    public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Long> shardingValue) {
        for (String each : databaseNames) {
            System.out.println("查看一下对应的值："+each+"----"+shardingValue.getValue().toString());
            if (each.endsWith(Long.parseLong(shardingValue.getValue().toString()) % 2 + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Long>  shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(databaseNames.size());
        for (Long value : shardingValue.getValues()) {
            for (String tableName : databaseNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }


    @Override
    public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Long>  shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(databaseNames.size());
        Range<Long> range = (Range<Long>) shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : databaseNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
