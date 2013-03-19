package de.fisp.anwesenheit.web.controllers;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import de.fisp.anwesenheit.core.domain.AntragListe;
import de.fisp.anwesenheit.core.service.AntragService;

@Controller
@RequestMapping("/antraege")
public class AntragsController {
  @Autowired
  private AntragService antragService;

  private String getCurrentUser() {
    return (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
  }

  private String serializeAntragListe(AntragListe antragListe) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      StringWriter stringWriter = new StringWriter();
      mapper.writeValue(stringWriter, antragListe);
      return stringWriter.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @RequestMapping(method = RequestMethod.GET)
  @Transactional
  public String index(Model model) {
    final String benutzerId = getCurrentUser();
    AntragListe antragListe = antragService.findByBenutzer(benutzerId, benutzerId);
    model.addAttribute("benutzerId", benutzerId);
    model.addAttribute("antragListe", serializeAntragListe(antragListe));
    return "antraege/index";
  }
}
