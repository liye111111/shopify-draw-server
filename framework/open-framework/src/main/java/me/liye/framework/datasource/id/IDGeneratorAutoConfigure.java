package me.liye.framework.datasource.id;


import lombok.extern.slf4j.Slf4j;
import me.liye.framework.datasource.id.impl.CachedIDGenerator;
import me.liye.framework.datasource.id.impl.DefaultIDGenerator;
import me.liye.framework.datasource.id.impl.UidProperties;
import me.liye.framework.datasource.id.worker.DisposableWorkerIdAssigner;
import me.liye.framework.datasource.id.worker.WorkerIdAssigner;
import me.liye.framework.datasource.id.worker.dao.WorkerNodeDAO;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * UID 的自動配置
 *
 * @author knight@momo.com
 * @date 2019.02.20 10:57
 */
@Slf4j
@Configuration
@ConditionalOnClass({DefaultIDGenerator.class, CachedIDGenerator.class})
@EnableConfigurationProperties({UidProperties.class, IdGeneratorDatasourceProperties.class})
public class IDGeneratorAutoConfigure {

    @Primary
    @Bean("defaultIDGenerator")
    @ConditionalOnMissingBean
    DefaultIDGenerator defaultIDGenerator(UidProperties uidProperties) {
        return new DefaultIDGenerator(uidProperties);
    }

    @Bean("cachedIDGenerator")
    @ConditionalOnMissingBean
    CachedIDGenerator cachedIDGenerator(UidProperties uidProperties) {
        return new CachedIDGenerator(uidProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    WorkerIdAssigner workerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(WorkerNodeDAO.class)
    public MapperFactoryBean<WorkerNodeDAO> idGeneratorWorkerNodeDAO(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment.Builder("idGeneratorEnv")
                .transactionFactory(transactionFactory)
                .dataSource(dataSource)
                .build();

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(environment);
        //add  mapper
        configuration.addMapper(WorkerNodeDAO.class);
        //
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        MapperFactoryBean<WorkerNodeDAO> factoryBean = new MapperFactoryBean<>(WorkerNodeDAO.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;

    }
}
