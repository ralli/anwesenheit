package de.fisp.anwesenheit.core;

import org.springframework.context.annotation.Configuration;

import de.fisp.anwesenheit.core.service.MailService;
import de.fisp.anwesenheit.core.service.impl.DummyMailServiceImpl;

@Configuration
public class TestDataConfig {
  public TestDataFactory testDataFactory() {
    return new TestDataFactory();
  }

  public MailService mailService() {
    return new DummyMailServiceImpl();
  }
}
