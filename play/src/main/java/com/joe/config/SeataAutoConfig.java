package com.joe.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class SeataAutoConfig {

	private final static Logger logger = LoggerFactory.getLogger(SeataAutoConfig.class);

	@Autowired
	private DataSourceProperties dataSourceProperties;

	/**
	 * 在同样的DataSource中，首先使用被标注的DataSource
	 */
	@Bean
	public DataSource hikariDataSource() {
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(dataSourceProperties.getUrl());
		hikariDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		hikariDataSource.setUsername(dataSourceProperties.getUsername());
		hikariDataSource.setPassword(dataSourceProperties.getPassword());
		logger.info("装载dataSource........");
		return hikariDataSource;
	}

	/**
	 * 初始化代理数据源
	 */
	@Primary
	@Bean("dataSource")
	public DataSourceProxy dataSource(DataSource hikariDataSource) {
		logger.info("代理dataSource........");
		return new DataSourceProxy(hikariDataSource);
	}


	/**
	 * 这里用SqlSessionFactory也就是MyBatis的 代理数据源生效
	 * @param dataSourceProxy
	 * @return
	 * @throws Exception
	 */
	/*@Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        return factoryBean.getObject();
    }*/

	/**
	 * 这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
	 * @param dataSourceProxy
	 * @return
	 * @throws Exception
	 */
	@Bean
	public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSourceProxy dataSourceProxy) throws Exception {
		MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSourceProxy);
		mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources("classpath*:/mapper/*.xml"));
		return mybatisSqlSessionFactoryBean;
	}

}
