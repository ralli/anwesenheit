package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.service.BewilligungService;

@Controller
@RequestMapping("/api/bewilligung")
public class BewilligungApiController {
  @Autowired
  private BewilligungService bewilligungService;
  private static final Logger logger = LoggerFactory.getLogger(BewilligungApiController.class);

  private String getCurrentUser() {
    return "juhnke_r";
  }

  private String toJson(Object object) {
    try {
      StringWriter stringWriter = new StringWriter();
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(stringWriter, object);
      return stringWriter.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private String jsonMessage(String message) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    map.put("message", message);
    return toJson(map);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public @ResponseBody
  ResponseEntity<String> deleteBewilligung(@PathVariable long id) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    try {
      boolean found = bewilligungService.deleteBewilligung(getCurrentUser(), id);
      if (found == false) {
        return new ResponseEntity<String>(jsonMessage("Bewilligung nicht gefunden"), headers, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<String>(jsonMessage("Ok"), headers, HttpStatus.OK);
    } catch (Exception ex) {
      logger.warn("Fehler beim Löschen", ex);
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<String> addBewilligung(@Valid @RequestBody AddBewilligungCommand addBewilligungCommand) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");

    try {
      BewilligungsDaten bewilligungsDaten = bewilligungService.addBewilligung(getCurrentUser(), addBewilligungCommand);
      if (bewilligungsDaten == null) {
        return new ResponseEntity<String>(jsonMessage("Daten nicht gefunden"), headers, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<String>(toJson(bewilligungsDaten), headers, HttpStatus.OK);
    } catch (Exception ex) {
      logger.warn("Fehler beim Hinzufügen", ex);
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }
}
