package de.fisp.anwesenheit.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import de.fisp.anwesenheit.core.entities.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
public class HibernateConfig {
  @Bean
  public FactoryBean<SessionFactory> sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
    localSessionFactoryBean.setDataSource(dataSource);
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
    localSessionFactoryBean.setHibernateProperties(hibernateProperties);
    Class<?>[] classes = {
            Antrag.class, AntragArt.class, AntragHistorie.class, AntragStatus.class, Benutzer.class, BenutzerRolle.class, Rolle.class, BewilligungsStatus.class, Bewilligung.class, SonderUrlaubArt.class, FeiertagDefinition.class, Feiertag.class, Parameter.class
    };
    localSessionFactoryBean.setAnnotatedClasses(classes);
    return localSessionFactoryBean;
  }
}
