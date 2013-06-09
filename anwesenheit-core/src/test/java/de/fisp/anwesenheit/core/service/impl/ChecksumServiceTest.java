package de.fisp.anwesenheit.core.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChecksumServiceTest {
  ChecksumServiceImpl checksumService;
  public static final Logger logger = LoggerFactory.getLogger(ChecksumServiceTest.class);

  @Before
  public void setUp() {
    checksumService =  new ChecksumServiceImpl();
  }

  @Test
  public void testChecksumService() {
    final String input = "Hase";
    final String expectedResult = "2089452dc7cb7edd6fb697c82b9714c1d97c4fee";
    final String result = checksumService.generateChecksum(input);
    Assert.assertEquals(expectedResult, result);
  }
}
