package de.fisp.anwesenheit.web.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

  @Bean
  public DataSource dataSource() {
    log.info("Register data source...");
    BasicDataSource datasource = new BasicDataSource();
    datasource.setUrl("jdbc:mysql://localhost/anwesenheit");
    datasource.setDriverClassName("com.mysql.jdbc.Driver");
    datasource.setUsername("anwesenheit");
    datasource.setPassword("anwesenheit");
    datasource.setInitialSize(5);
    datasource.setMaxActive(10);
    datasource.setMaxIdle(0);
    datasource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 10);
    datasource.setMinEvictableIdleTimeMillis(1000 * 60 * 15);
    return datasource;
  }
  //    @Bean
//    public DataSource dataSource() throws Exception {
//        DataSource ds;
//
//        JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
//        jndiFactory.setJndiName("java:jboss/datasources/PetclinicDS");
//        jndiFactory.afterPropertiesSet();
//        ds = (DataSource) jndiFactory.getObject();
//        return ds;
//    }
}
