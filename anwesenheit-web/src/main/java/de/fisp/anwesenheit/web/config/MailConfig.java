package de.fisp.anwesenheit.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import de.fisp.anwesenheit.core.service.MailService;
import de.fisp.anwesenheit.core.service.impl.DummyMailServiceImpl;

@Configuration
public class MailConfig {
  private static final Logger log = LoggerFactory.getLogger(MailConfig.class);

  public MailService mailService() {
    log.info("Register mail service...");
    return new DummyMailServiceImpl();
  }
}
