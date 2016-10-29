package com.ccai.lietouerp.configs.jpa;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 默认的数据源配置
 * @author Administrator
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.ccai.lietouerp.db.dao"}, 
entityManagerFactoryRef = "springEntityManagerFactory",
transactionManagerRef = "springTransactionManager")
@EnableTransactionManagement
public class SpringJapConfig extends BaseJpaConfig {

	private static final String DATABASE_DRIVER = "spring.datasource.driver";
	private static final String DATABASE_URL = "spring.datasource.url";
	private static final String DATABASE_USER = "spring.datasource.username";
	private static final String DATABASE_PASSWORD = "spring.datasource.password";
	private static final String PACKAGES_TO_SCAN = "spring.packagestoscan";

//	@ConfigurationProperties(prefix = "datasource.primary")
	@Bean
	public DataSource druidDataSource() {
		DruidDataSource source = new DruidDataSource();
		source.setDriverClassName(env.getRequiredProperty(DATABASE_DRIVER));
		source.setUrl(env.getRequiredProperty(DATABASE_URL));
		source.setUsername(env.getRequiredProperty(DATABASE_USER));
		source.setPassword(env.getRequiredProperty(DATABASE_PASSWORD));
		return source;
	}

	@Bean(name = "springEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean springEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(druidDataSource());
		factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factory.setPackagesToScan(env.getRequiredProperty(PACKAGES_TO_SCAN).split(","));
		factory.setJpaProperties(hibernateProperties());
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean(name = "springTransactionManager")
	@Primary
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager manager = new JpaTransactionManager();
		manager.setEntityManagerFactory(springEntityManagerFactory().getObject());
		return manager;
	}
}
