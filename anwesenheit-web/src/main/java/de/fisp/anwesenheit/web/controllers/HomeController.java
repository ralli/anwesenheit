package de.fisp.anwesenheit.web.controllers;

import de.fisp.anwesenheit.core.service.BerechtigungsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
  @Autowired
  private BerechtigungsService berechtigungsService;

  private String getCurrentUser() {
    return (String) RequestContextHolder.currentRequestAttributes().getAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(@RequestParam(value = "deepLink", required = false) String deepLink, Model model) {
    if (getCurrentUser() == null) {
      String redirectUrl = StringUtils.defaultString(deepLink);
      String result = "redirect:/login?redirectUrl=" + redirectUrl;
      logger.debug("redirect to: {}", result);
      return result;
    }
    boolean hatSonderBerechtigung = berechtigungsService.hatSonderBerechtigungen(getCurrentUser());
    logger.debug("hatSonderberechtigung: {}", hatSonderBerechtigung);
    model.addAttribute("hatSonderBerechtigung", hatSonderBerechtigung);
    if (StringUtils.isNotBlank(deepLink)) {
      String result = "redirect:/#!/" + deepLink;
      logger.debug("Angemeldet. redirect to: {}", result);
      return result;
    }
    return "home";
  }
}
