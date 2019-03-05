package com.ccsu.mysql.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ccsu.mysql.datasource.rule.DatabaseShardingAlogorithmRule;
import com.ccsu.mysql.datasource.rule.TableShardingAlgorithmRule;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenzhenyu
 * @description
 * @date 2019/3/4
 */
@Configuration
@MapperScan(basePackages = "com.ccsu.mysql.dao.user", sqlSessionFactoryRef = "sqlSessionFactory")
public class DatasourceConfig {


//    static final String PACKAGE = "com.ccsu.mysql.dao.read";
    static final String MAPPER_LOCATION = "classpath:mapper/user/*.xml";

//    @Value("${spring.write.datasource.url}")
//    private String jdbcUrl;
//    @Value("${spring.write.datasource.username}")
//    private String username;
//    @Value("${spring.write.datasource.password}")
//    private String password;
//    @Value("${spring.write.datasource.driverClassName}")
//    private String driverClass;


//    @Bean(name="readDataSource")
    @Bean(name="dataSource")
    @ConfigurationProperties(prefix = "spring.write.datasource")
    public DataSource dataSource(){
//        DataSource result = new DruidDataSource();
//        ((DruidDataSource) result).setDriverClassName(driverClass);
//        ((DruidDataSource) result).setUrl(jdbcUrl);
//        ((DruidDataSource) result).setUsername(username);
//        ((DruidDataSource) result).setPassword(password);
//        return result;

        return new DruidDataSource();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource shardingDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(shardingDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "dataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dataSourceSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
