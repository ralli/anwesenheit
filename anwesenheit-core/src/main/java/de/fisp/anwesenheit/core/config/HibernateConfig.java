package de.fisp.anwesenheit.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import de.fisp.anwesenheit.core.entities.Antrag;
import de.fisp.anwesenheit.core.entities.AntragArt;
import de.fisp.anwesenheit.core.entities.AntragHistorie;
import de.fisp.anwesenheit.core.entities.AntragStatus;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.entities.BenutzerRolle;
import de.fisp.anwesenheit.core.entities.Bewilligung;
import de.fisp.anwesenheit.core.entities.BewilligungsStatus;
import de.fisp.anwesenheit.core.entities.Rolle;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;

@Configuration
public class HibernateConfig {
	@Bean
	public FactoryBean<SessionFactory> sessionFactory(DataSource dataSource) {
		AnnotationSessionFactoryBean localSessionFactoryBean = new AnnotationSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQL5InnoDBDialect");
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		Class<?>[] classes = { Antrag.class, AntragArt.class,
				AntragHistorie.class, AntragStatus.class, Benutzer.class,
				BenutzerRolle.class, Rolle.class, BewilligungsStatus.class,
				Bewilligung.class, SonderUrlaubArt.class };
		localSessionFactoryBean.setAnnotatedClasses(classes);
		return localSessionFactoryBean;
	}
}
