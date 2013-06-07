package de.fisp.anwesenheit.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ReportController {
  private static final Logger log = LoggerFactory.getLogger(ReportController.class);

  @RequestMapping(value = "/report", method = RequestMethod.GET)
  public String showReport(@RequestParam(value = "datum", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datum) {
    log.debug("showReport: {}", datum);
    return "redirect:/";
  }
}
