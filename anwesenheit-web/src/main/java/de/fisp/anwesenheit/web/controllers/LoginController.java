package de.fisp.anwesenheit.web.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    model.addAttribute("loginData", new LoginCommand());
    return "/login";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(@Valid LoginCommand loginData, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("loginData", loginData);
      return "/login";
    }
    if (!loginService.login(loginData)) {
      model.addAttribute("loginData", loginData);
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
