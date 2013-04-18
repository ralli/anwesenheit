package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.service.FeiertagService;

@Controller
@RequestMapping("/api/arbeitstage")
public class ArbeitstageApiController {
  @Autowired
  private FeiertagService feiertagService;

  private String toJson(Object object) {
    try {
      StringWriter stringWriter = new StringWriter();
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
      objectMapper.writeValue(stringWriter, object);
      return stringWriter.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private HttpHeaders createJsonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    headers.add("Pragma", "no-cache");
    headers.add("Cache-Control", "no-cache, no-store");
    return headers;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> index(
      @RequestParam(value = "von", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date von,
      @RequestParam(value = "bis", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date bis) {
    HttpHeaders headers = createJsonHeaders();

    Map<String, Double> map = new HashMap<String, Double>();
    map.put("arbeitsTage", feiertagService.berechneAnzahlArbeitstage(von, bis));
    return new ResponseEntity<String>(toJson(map), headers, HttpStatus.OK);
  }

}
