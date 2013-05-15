package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;
import java.util.*;

import javax.validation.Valid;

import de.fisp.anwesenheit.core.domain.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
    String result = (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId",
        RequestAttributes.SCOPE_SESSION);
    if (result == null) {
      throw new NotAuthorizedException("Sie sind nicht angemeldet");
    }
    return result;
  }

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

  @RequestMapping(value = "/uebersicht", method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> uebersicht(@RequestParam(value="von", required=false) @DateTimeFormat(iso=ISO.DATE) Date von,
                                    @RequestParam(value="bis", required=false) @DateTimeFormat(iso=ISO.DATE) Date bis,
                                    @RequestParam(value="statusOffen", required=false) Boolean statusOffen,
                                    @RequestParam(value="statusBewilligt", required=false) Boolean statusBewilligt,
                                    @RequestParam(value="statusAbgelehnt", required=false) Boolean statusAbgelehnt,
                                    @RequestParam(value="statusStorniert", required=false) Boolean statusStorniert) {
    HttpHeaders headers = createJsonHeaders();
    try {
      final String benutzerId = getCurrentUser();
      AntragUebersichtFilter filter = new AntragUebersichtFilter();
      List<String> statusFilter = new ArrayList<String>();
      if(Boolean.TRUE.equals(statusOffen))
        statusFilter.add("OFFEN");
      if(Boolean.TRUE.equals(statusBewilligt))
        statusFilter.add("BEWILLIGT");
      if(Boolean.TRUE.equals(statusAbgelehnt))
        statusFilter.add("ABGELEHNT");
      if(Boolean.TRUE.equals(statusStorniert))
        statusFilter.add("STORNIERT");
      filter.setStatusList(statusFilter);
      filter.setVon(von);
      filter.setBis(bis);
      AntragListe liste = antragService.findSichtbareByFilter(benutzerId, filter);
      return new ResponseEntity<String>(toJson(liste), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    } catch (NotValidException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  ResponseEntity<String> index(@RequestParam(value = "von", required = false) @DateTimeFormat(iso = ISO.DATE) Date von,
      @RequestParam(value = "status", required = false) String status) {
    HttpHeaders headers = createJsonHeaders();
    try {
      final String benutzerId = getCurrentUser();
      AntragsFilter filter = new AntragsFilter();
      filter.setVon(von);
      filter.setAntragsStatusFilter(status);
      AntragListe liste = antragService.findEigeneByFilter(benutzerId, filter);
      return new ResponseEntity<String>(toJson(liste), headers, HttpStatus.OK);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/{id}/historie", method = RequestMethod.GET)
  @Transactional
  public @ResponseBody
  ResponseEntity<String> historie(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      List<AntragHistorieDaten> list = antragService.leseHistorie(getCurrentUser(), id);
      return new ResponseEntity<String>(toJson(list), headers, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<String>(jsonMessage(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
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

  @RequestMapping(value="/{id}/storno", method=RequestMethod.PUT)
  public @ResponseBody 
  ResponseEntity<String> storniereAntrag(@PathVariable long id) {
    HttpHeaders headers = createJsonHeaders();
    try {
      antragService.storniereAntrag(getCurrentUser(), id);
      return new ResponseEntity<String>(jsonMessage("OK"), headers, HttpStatus.OK);
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
