package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.domain.AntragsDaten;
import de.fisp.anwesenheit.core.domain.CreateAntragCommand;
import de.fisp.anwesenheit.core.domain.UpdateAntragCommand;
import de.fisp.anwesenheit.core.service.AntragService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;
import de.fisp.anwesenheit.core.util.NotValidException;

@Controller
@RequestMapping("/api/antraege")
public class AntragApiController {
  @Autowired
  private AntragService antragService;

  private String getCurrentUser() {
    return (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
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

  private HttpHeaders createJsonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    headers.add("Pragma", "no-cache");
    headers.add("Cache-Control", "no-cache, no-store");
    return headers;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> index() {
    final String benutzerId = getCurrentUser();
    HttpHeaders headers = createJsonHeaders();
    try {
      AntragListe liste = antragService.findByBenutzer(getCurrentUser(), benutzerId);
      return new ResponseEntity<String>(toJson(liste), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> details(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      AntragsDaten daten = antragService.findAntragById(getCurrentUser(), id);
      return new ResponseEntity<String>(toJson(daten), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);

    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public @ResponseBody
  ResponseEntity<String> delete(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      antragService.deleteAntrag(getCurrentUser(), id);
      return new ResponseEntity<String>(jsonMessage("Ok"), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<String> insert(@RequestBody @Valid CreateAntragCommand createAntragCommand) {
    HttpHeaders headers = createJsonHeaders();
    try {
      createAntragCommand.setBenutzerId(getCurrentUser());
      long antragId = antragService.createAntrag(getCurrentUser(), createAntragCommand);
      Map<String, Object> result = new LinkedHashMap<String, Object>();
      result.put("antragId", antragId);
      return new ResponseEntity<String>(toJson(result), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public @ResponseBody
  ResponseEntity<String> update(@PathVariable long id, @RequestBody @Valid UpdateAntragCommand updateAntragCommand) {
    HttpHeaders headers = createJsonHeaders();
    try {
      AntragsDaten daten = antragService.updateAntrag(getCurrentUser(), id, updateAntragCommand);
      return new ResponseEntity<String>(toJson(daten), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }
}
