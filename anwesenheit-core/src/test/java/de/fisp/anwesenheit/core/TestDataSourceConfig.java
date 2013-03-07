package de.fisp.anwesenheit.core;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataSourceConfig {
	@Bean
	public DataSource dataSource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl("jdbc:mysql://localhost/anwesenheit");
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUsername("anwesenheit");
		datasource.setPassword("anwesenheit");
		datasource.setInitialSize(5);
		datasource.setMaxActive(10);
		datasource.setMaxIdle(5);
		return datasource;
	}
}
