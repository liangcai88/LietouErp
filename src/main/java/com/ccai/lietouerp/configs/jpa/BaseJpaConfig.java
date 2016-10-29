package com.ccai.lietouerp.configs.jpa;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public abstract class BaseJpaConfig {

	private static final String HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String HIBERNATE_SHOW_SQL = "hibernate.show.sql";
	private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	private static final String HIBERNATE_SEARCH_PROVIDER="hibernate.search.default.directory_provider";
	private static final String HIBERNATE_SEARCH_INDEXBASE="hibernate.search.default.indexBase";
	@Resource
	protected Environment env;

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	protected Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT,"org.hibernate.dialect.MySQLDialect"));
		properties.put(HIBERNATE_SHOW_SQL, env.getProperty(HIBERNATE_SHOW_SQL,"false"));
		properties.put(HIBERNATE_HBM2DDL_AUTO, env.getProperty(HIBERNATE_HBM2DDL_AUTO,"update"));
//		properties.put(HIBERNATE_SEARCH_PROVIDER, env.getProperty(HIBERNATE_SEARCH_PROVIDER));
//		properties.put(HIBERNATE_SEARCH_INDEXBASE, env.getProperty(HIBERNATE_SEARCH_INDEXBASE));
		return properties;
	}

}
