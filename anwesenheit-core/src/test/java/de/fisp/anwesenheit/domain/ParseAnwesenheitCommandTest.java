package de.fisp.anwesenheit.domain;

import java.io.StringReader;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.domain.CreateAntragCommand;

public class ParseAnwesenheitCommandTest {
  private static final String JSON = "{ \"benutzerId\": \"juhnke_r\", \"von\": \"2013-11-13\", \"bis\": \"2013-11-20\", \"bewilliger\": [\"boss\", \"chef\"]}";
  private static final Logger logger = LoggerFactory
          .getLogger(ParseAnwesenheitCommandTest.class);

  @Test
  public void testParse() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    CreateAntragCommand cmd = mapper.readValue(new StringReader(JSON),
            CreateAntragCommand.class);
    logger.info("{}", cmd);
  }
}
