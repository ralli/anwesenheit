package de.fisp.anwesenheit.web.config;

import de.fisp.anwesenheit.core.service.impl.MailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.fisp.anwesenheit.core.service.MailService;
import de.fisp.anwesenheit.core.service.impl.DummyMailServiceImpl;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.mail.Session;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/anwesenheit.properties")
public class MailConfig {
  private static final Logger log = LoggerFactory.getLogger(MailConfig.class);
  @Autowired
  private org.springframework.core.env.Environment env;

  private boolean isRunningOnJBoss() {
    return System.getProperty("jboss.server.name") != null;
  }

  @Bean
  public MailService mailService() {
    if(isRunningOnJBoss())
      return realMailService();
    else
      return dummyMailService();
  }

  private MailService dummyMailService() {
    log.info("Register dummy mail service...");
    return new DummyMailServiceImpl();
  }

  private Session getMailSession() {
    try {
      log.info("Getting Mail Session via JNDI...");
      JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
      String  mailJndiName =  env.getProperty("mail.jndiName");
      log.info("mailJndiName={}", mailJndiName);
      jndiFactory.setJndiName(mailJndiName);
      jndiFactory.afterPropertiesSet();
      Session mailSession = (Session) jndiFactory.getObject();
      return mailSession;
    }
    catch(Exception ex) {
      throw new RuntimeException("Error getting mail session", ex);
    }
  }

  private MailService realMailService() {
    log.info("Register real mail service...");
    return new MailServiceImpl(getMailSession());
  }
}
