package de.fisp.anwesenheit.core.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.service.PasswordService;

public class PasswordServiceTest {
  private PasswordService passwordService;
  private static final Logger logger = LoggerFactory.getLogger(PasswordServiceTest.class);

  @Before
  public void setUp() {
    passwordService = new PasswordServiceImpl();
  }

  @Test
  public void testGenerateSaltIsBase64() throws Exception {
    String salt = passwordService.generateSalt();
    Base64.decodeBase64(salt);
  }

  @Test
  public void testEncodeEqualsDecode() {
    final String salt = "qgfB6sybMH662ZICb44/GM2dzb0=";
    final String password = "testpassword";
    final String differentPassword = "somethingdifferent";
    final String passwordHash = passwordService.encodePassword(salt, password);
    logger.info("salt={}, passwordHash={}", salt, passwordHash);
    Assert.assertTrue(passwordService.checkPassword(salt, passwordHash, password));
    Assert.assertFalse(passwordService.checkPassword(salt, passwordHash, differentPassword));
  }
}
