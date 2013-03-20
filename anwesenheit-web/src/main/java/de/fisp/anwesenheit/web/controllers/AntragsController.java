package de.fisp.anwesenheit.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
@RequestMapping("/antraege")
public class AntragsController {
  private String getCurrentUser() {
    return (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
  }

  @RequestMapping(method = RequestMethod.GET)
  @Transactional
  public String index(Model model) {
    final String benutzerId = getCurrentUser();
    if(benutzerId == null) {
      return "redirect:/";
    }
    return "antraege/index";
  }
}
