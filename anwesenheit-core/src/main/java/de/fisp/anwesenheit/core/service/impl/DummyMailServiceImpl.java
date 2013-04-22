package de.fisp.anwesenheit.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.service.MailService;

public class DummyMailServiceImpl implements MailService {
  private static final Logger logger = LoggerFactory.getLogger(DummyMailServiceImpl.class);

  @Override
  public void sendeMail(String subject, String text, String from, String adressen) {
    logger.debug("sendeMail(subject={}, from={}, adressen={}, text={})", new Object[] { subject, from, adressen, text });
  }

}
