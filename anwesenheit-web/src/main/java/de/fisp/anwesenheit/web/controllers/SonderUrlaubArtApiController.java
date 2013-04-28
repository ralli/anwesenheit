package de.fisp.anwesenheit.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.dao.SonderUrlaubArtDao;
import de.fisp.anwesenheit.core.entities.SonderUrlaubArt;

@Controller
@RequestMapping("/api/sonderurlaubarten")
public class SonderUrlaubArtApiController {
  @Autowired
  private SonderUrlaubArtDao sonderUrlaubArtDao;
  
  @RequestMapping(method = RequestMethod.GET)
  @Transactional
  public @ResponseBody
  List<SonderUrlaubArt> index() {
    return sonderUrlaubArtDao.findAll();
  }
}
