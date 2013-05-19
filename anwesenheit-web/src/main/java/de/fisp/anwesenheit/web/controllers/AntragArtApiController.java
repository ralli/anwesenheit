package de.fisp.anwesenheit.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fisp.anwesenheit.core.dao.AntragArtDao;
import de.fisp.anwesenheit.core.entities.AntragArt;

@Controller
@RequestMapping("/api/antragsarten")
public class AntragArtApiController {
  @Autowired
  private AntragArtDao antragArtDao;

  @RequestMapping(method = RequestMethod.GET)
  @Transactional
  public
  @ResponseBody
  List<AntragArt> index() {
    return antragArtDao.findAll();
  }
}
