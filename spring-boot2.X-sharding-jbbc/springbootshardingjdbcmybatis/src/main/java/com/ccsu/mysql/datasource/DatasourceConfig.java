package com.ccsu.mysql.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ccsu.mysql.datasource.rule.DatabaseShardingAlogorithmRule;
import com.ccsu.mysql.datasource.rule.TableShardingAlgorithmRule;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingjdbc.core.rule.ShardingRule;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.write.datasource.url}")
    private String writeJdbcUrl;
    @Value("${spring.write.datasource.username}")
    private String writeUsername;
    @Value("${spring.write.datasource.password}")
    private String writePassword;

//    @Bean(name="readDataSource")
    @Bean(name="dataSource0")
    @ConfigurationProperties(prefix = "spring.read.datasource")
    public DataSource dataSource0(){
//        DataSource result = new DruidDataSource();
//        ((DruidDataSource) result).setDriverClassName(driverClass);
//        ((DruidDataSource) result).setUrl(jdbcUrl);
//        ((DruidDataSource) result).setUsername(username);
//        ((DruidDataSource) result).setPassword(password);
//        return result;

        return new DruidDataSource();
    }

//    @Bean(name="writeDataSource")
    @Bean(name="dataSource1")
    @ConfigurationProperties(prefix = "spring.write.datasource")
    public DataSource dataSource1(){
//        DataSource result = new DruidDataSource();
//        ((DruidDataSource) result).setDriverClassName(driverClass);
//        ((DruidDataSource) result).setUrl(writeJdbcUrl);
//        ((DruidDataSource) result).setUsername(writeUsername);
//        ((DruidDataSource) result).setPassword(writePassword);
//        return result;

        return new DruidDataSource();
    }


    /**
     * 配置分库分表策略
     *
     * @return
     * @throws SQLException
     */
    @Bean(name = "shardingDataSource")
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig;
        shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", DatabaseShardingAlogorithmRule.class.getName()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", TableShardingAlgorithmRule.class.getName()));

        return new ShardingDataSource(shardingRuleConfig.build(createDataSourceMap()));
    }


    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("realpay_test_0", dataSource0());
        result.put("realpay_test_1", dataSource1());
        return result;
    }


//    private DataSource createDataSource(final String dataSourceName) {
//        DataSource result = new DruidDataSource();
//        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
//        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s", dataSourceName));
//        result.setUsername("root");
//        result.setPassword("123456");
//        return result;
//    }

    /**
     * 设置表的node
     * @return
     */
    @Bean
    TableRuleConfiguration getUserTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
//        orderTableRuleConfig.setActualDataNodes("realpay_test_0.t_order_${0..1}");
        orderTableRuleConfig.setActualDataNodes("realpay_test_0.t_order_0,realpay_test_0.t_order_1,realpay_test_1.t_order_0,realpay_test_1.t_order_1");
//        orderTableRuleConfig.setActualDataNodes("t_order_0,t_order_1");
        orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
        return orderTableRuleConfig;
    }
//    TableRuleConfiguration.setActualDataNodes


//    @Bean
//    public DataSourceRule dataSourceRule(@Qualifier("dataSource0") DataSource dataSource0,
//                                         @Qualifier("dataSource1") DataSource dataSource1){
//        Map<String, DataSource> dataSourceMap = new HashMap<String,DataSource>(); //设置分库映射
//        dataSourceMap.put("dataSource0", dataSource0);
//        dataSourceMap.put("dataSource1", dataSource1);
//        return new DataSourceRule(dataSourceMap, "dataSource0"); //设置默认库，两个库以上时必须设置默认库。默认库的数据源名称必须是dataSourceMap的key之一
//    }
//    @Bean
//    public ShardingRule shardingRule(DataSourceRule dataSourceRule){
//        //具体分库分表策略
//        TableRule orderTableRule = TableRule.builder("t_order")
//                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
//                .tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithmRule()))
//                .dataSourceRule(dataSourceRule)
//                .build();
//
//        //绑定表策略，在查询时会使用主表策略计算路由的数据源，因此需要约定绑定表策略的表的规则需要一致，可以一定程度提高效率
//        List<BindingTableRule> bindingTableRules = new ArrayList<BindingTableRule>();
//        bindingTableRules.add(new BindingTableRule(Arrays.asList(orderTableRule)));
//        return ShardingRule.builder()
//                .dataSourceRule(dataSourceRule)
//                .tableRules(Arrays.asList(orderTableRule))
//                .bindingTableRules(bindingTableRules)
//                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new DatabaseShardingAlogorithmRule()))
//                .tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithmRule()))
//                .build();
//    }


//    @Bean(name="dataSource")
//    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
//        return ShardingDataSourceFactory.createDataSource(shardingRule);
//    }


    @Bean(name = "readSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("shardingDataSource") DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(shardingDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "readDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("shardingDataSource") DataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    @Bean(name = "readDataSourceSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("readSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
