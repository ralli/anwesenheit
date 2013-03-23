package de.fisp.anwesenheit.web.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import de.fisp.anwesenheit.core.domain.LoginCommand;
import de.fisp.anwesenheit.core.service.LoginService;

@Controller
public class LoginController {
  @Autowired
  private LoginService loginService;
  private Logger logger = LoggerFactory.getLogger(LoginController.class);

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    model.addAttribute("loginData", new LoginCommand());
    return "/login";
  }
  
  @ModelAttribute("loginData")
  public LoginCommand createLoginCommand() {
    return new LoginCommand();
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(@Valid LoginCommand loginData, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {      
      logger.debug("Login enthält fehlerhafte Angaben");
      return "/login";
    }
    if (!loginService.login(loginData)) {
      logger.debug("Login ist fehlgeschlagen");
      bindingResult.addError(new ObjectError("Login", "Falscher Benutzer oder Passwort"));
      return "/login";
    }
    RequestContextHolder.currentRequestAttributes().setAttribute("benutzerId", loginData.getLogin(),
        RequestAttributes.SCOPE_SESSION);
    return "redirect:/";
  }
  
  @RequestMapping(value = "/logoff", method = RequestMethod.GET)
  public String logoff() {
    
    RequestContextHolder.currentRequestAttributes().removeAttribute("benutzerId", RequestAttributes.SCOPE_SESSION);
    return "redirect:/login";
  }
}
