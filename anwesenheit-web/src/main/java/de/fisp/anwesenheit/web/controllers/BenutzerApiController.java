package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.LabelValue;
import de.fisp.anwesenheit.core.service.BenutzerService;

@Controller
@RequestMapping("/api/benutzer")
public class BenutzerApiController {
  private BenutzerService benutzerService;

  @Autowired
  public BenutzerApiController(BenutzerService benutzerService) {
    this.benutzerService = benutzerService;
  }

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

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> search(@RequestParam(value = "term", required = true) String term) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<LabelValue> list = benutzerService.search(term);
    return new ResponseEntity<String>(toJson(list), headers, HttpStatus.OK);
  }

  @RequestMapping(value = "/current", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> findCurrentUser() {
    return findByBenutzerId(getCurrentUser());
  }

  @RequestMapping(value = "/{benutzerId}", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> findByBenutzerId(@PathVariable String benutzerId) {
    BenutzerDaten daten = benutzerService.findByBenutzerId(benutzerId);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    if (daten == null) {
      return new ResponseEntity<String>(jsonMessage("Benutzer nicht gefunden"), headers, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<String>(toJson(daten), headers, HttpStatus.OK);
  }
}
