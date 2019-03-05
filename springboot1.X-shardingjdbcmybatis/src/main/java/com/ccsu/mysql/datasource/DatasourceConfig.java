package com.ccsu.mysql.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ccsu.mysql.datasource.rule.DatabaseShardingAlogorithmRule;
import com.ccsu.mysql.datasource.rule.TableShardingAlgorithmRule;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wenzhenyu
 * @description
 * @date 2019/3/4
 */
@Configuration
@MapperScan(basePackages = "com.ccsu.mysql.dao", sqlSessionFactoryRef = "readSqlSessionFactory")
public class DatasourceConfig {


//    static final String PACKAGE = "com.ccsu.mysql.dao.read";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Value("${spring.read.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.read.datasource.username}")
    private String username;
    @Value("${spring.read.datasource.password}")
    private String password;
    @Value("${spring.read.datasource.driverClassName}")
    private String driverClass;
//    @Primary
//    @Bean(name = "readDataSource")
//    public DataSource getMysqlDataSource() throws SQLException {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(driverClass);
//        dataSource.setUrl(jdbcUrl);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }

//    @Bean(name="readDataSource")
    @Bean(name="dataSource0")
    @ConfigurationProperties(prefix = "spring.read.datasource")
    public DataSource dataSource0(){
        return DataSourceBuilder.create().build();
    }

//    @Bean(name="writeDataSource")
    @Bean(name="dataSource1")
    @ConfigurationProperties(prefix = "spring.write.datasource")
    public DataSource dataSource1(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceRule dataSourceRule(@Qualifier("dataSource0") DataSource dataSource0,
                                         @Qualifier("dataSource1") DataSource dataSource1){
        Map<String, DataSource> dataSourceMap = new HashMap<String,DataSource>(); //设置分库映射
        dataSourceMap.put("dataSource0", dataSource0);
        dataSourceMap.put("dataSource1", dataSource1);
        return new DataSourceRule(dataSourceMap, "dataSource0"); //设置默认库，两个库以上时必须设置默认库。默认库的数据源名称必须是dataSourceMap的key之一
    }


    @Bean
    public ShardingRule shardingRule(DataSourceRule dataSourceRule){
        //具体分库分表策略
        TableRule orderTableRule = TableRule.builder("t_order")
                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
                .tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithmRule()))
                .dataSourceRule(dataSourceRule)
                .build();

        //绑定表策略，在查询时会使用主表策略计算路由的数据源，因此需要约定绑定表策略的表的规则需要一致，可以一定程度提高效率
        List<BindingTableRule> bindingTableRules = new ArrayList<BindingTableRule>();
        bindingTableRules.add(new BindingTableRule(Arrays.asList(orderTableRule)));
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule))
                .bindingTableRules(bindingTableRules)
                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new DatabaseShardingAlogorithmRule()))
                .tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithmRule()))
                .build();
    }
    @Bean(name="dataSource")
    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }


    @Bean(name = "readSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "readDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readDataSourceSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("readSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
