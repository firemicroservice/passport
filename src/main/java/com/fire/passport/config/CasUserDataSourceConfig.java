package com.fire.passport.config;

import com.fire.passport.mapper.CasUserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 因为使用的是在spring.factories中添加配置所以这里不能使用@EnableAutoConfiguration
 */
@Configuration("CasUserDataSourceFireConfig")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "com.fire.passport.mapper", sqlSessionTemplateRef  = "casSqlSessionTemplate")
public class CasUserDataSourceConfig {

//    @Primary
    @Bean(name = "casDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cas")
    public DataSource casDataSource() {
        return DataSourceBuilder.create().build();
    }


    //主数据源 ds1数据源
//    @Primary
    @Bean("casSqlSessionFactory")
    public SqlSessionFactory casSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(casDataSource());
//        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
//                getResources("classpath*:com/web/ds1/**/*.xml"));

        //不能使用这种方式，只能用MapperScan
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.addMapper(CasUserMapper.class);
//        sqlSessionFactory.setConfiguration(configuration);

        sqlSessionFactory.setMapperLocations(new Resource[] {
                new ClassPathResource("/com/fire/passport/mapper/CasUserMapper.xml")
        });


        return sqlSessionFactory.getObject();
    }

//    @Primary
    @Bean(name = "casTransactionManager")
    public DataSourceTransactionManager casTransactionManager() {
        return new DataSourceTransactionManager(casDataSource());
    }

//    @Primary
    @Bean(name = "casSqlSessionTemplate")
    public SqlSessionTemplate ds1SqlSessionTemplate(@Qualifier("casSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
