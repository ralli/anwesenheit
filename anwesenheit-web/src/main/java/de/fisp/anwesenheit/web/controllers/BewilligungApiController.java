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

import de.fisp.anwesenheit.core.domain.AddBewilligungCommand;
import de.fisp.anwesenheit.core.domain.BewilligungListe;
import de.fisp.anwesenheit.core.domain.BewilligungsDaten;
import de.fisp.anwesenheit.core.domain.BewilligungsDetails;
import de.fisp.anwesenheit.core.domain.UpdateBewilligungCommand;
import de.fisp.anwesenheit.core.service.BewilligungService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;
import de.fisp.anwesenheit.core.util.NotValidException;

@Controller
@RequestMapping("/api/bewilligung")
public class BewilligungApiController {
  @Autowired
  private BewilligungService bewilligungService;

  private HttpHeaders createJsonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    headers.add("Pragma", "no-cache");
    headers.add("Cache-Control", "no-cache, no-store");
    return headers;
  }

  private String getCurrentUser() {
    String result = (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
    if (result == null) {
      throw new NotAuthorizedException("Nicht angemeldet");
    }
    return result;
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

  @RequestMapping(method = RequestMethod.GET)
  public
  @ResponseBody
  ResponseEntity<String> findByCurrentUser() {
    HttpHeaders headers = createJsonHeaders();
    try {
      String benutzerId = getCurrentUser();
      BewilligungListe liste = bewilligungService.findByBenutzer(benutzerId, benutzerId);
      return new ResponseEntity<String>(toJson(liste), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public
  @ResponseBody
  ResponseEntity<String> show(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      BewilligungsDetails details = bewilligungService.leseBewilligungsDetails(getCurrentUser(), id);
      return new ResponseEntity<String>(toJson(details), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public
  @ResponseBody
  ResponseEntity<String> deleteBewilligung(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      bewilligungService.deleteBewilligung(getCurrentUser(), id);
      return new ResponseEntity<String>(jsonMessage("Ok"), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public
  @ResponseBody
  ResponseEntity<String> addBewilligung(@Valid @RequestBody AddBewilligungCommand addBewilligungCommand) {
    HttpHeaders headers = createJsonHeaders();
    try {
      BewilligungsDaten bewilligungsDaten = bewilligungService.addBewilligung(getCurrentUser(), addBewilligungCommand);
      return new ResponseEntity<String>(toJson(bewilligungsDaten), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public
  @ResponseBody
  ResponseEntity<String> updateBewilligung(@PathVariable long id, @Valid @RequestBody UpdateBewilligungCommand command) {
    HttpHeaders headers = createJsonHeaders();
    try {
      BewilligungsDaten bewilligungsDaten;

      command.setId(id);
      bewilligungsDaten = bewilligungService.updateBewilligungStatus(getCurrentUser(), command);
      return new ResponseEntity<String>(toJson(bewilligungsDaten), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.UNAUTHORIZED);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }
}
