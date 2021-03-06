package de.fisp.anwesenheit.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class HomeController {
  private String getCurrentUser() {
    return (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
  }


  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() {
    if (getCurrentUser() == null)
      return "redirect:/login";
    return "home";
  }
}
