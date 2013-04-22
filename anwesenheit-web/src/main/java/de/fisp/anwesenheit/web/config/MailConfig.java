package de.fisp.anwesenheit.web.config;

import org.springframework.context.annotation.Configuration;

import de.fisp.anwesenheit.core.service.MailService;
import de.fisp.anwesenheit.core.service.impl.DummyMailServiceImpl;

@Configuration
public class MailConfig {
  public MailService mailService() {
    return new DummyMailServiceImpl();
  }
}
