package de.fisp.anwesenheit.core;

import de.fisp.anwesenheit.core.service.MailService;
import de.fisp.anwesenheit.core.service.impl.DummyMailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"de.fisp.anwesenheit.core"})
public class TestConfig {
  @Bean
  public MailService mailService() {
    return new DummyMailServiceImpl();
  }
}
