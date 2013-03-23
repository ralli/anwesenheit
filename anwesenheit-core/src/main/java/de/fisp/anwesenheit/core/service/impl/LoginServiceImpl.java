package de.fisp.anwesenheit.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.ChangePasswordCommand;
import de.fisp.anwesenheit.core.domain.LoginCommand;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.service.LoginService;
import de.fisp.anwesenheit.core.service.PasswordService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;

@Service
public class LoginServiceImpl implements LoginService {
  private BenutzerDao benutzerDao;
  private PasswordService passwordService;
  // private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

  @Autowired
  public LoginServiceImpl(BenutzerDao benutzerDao, PasswordService passwordService) {
    this.benutzerDao = benutzerDao;
    this.passwordService = passwordService;
  }

  @Override
  @Transactional
  public boolean login(LoginCommand loginData) {
    Benutzer benutzer = benutzerDao.findById(loginData.getLogin());
    if (benutzer == null) {
      return false;
    }
    if (!passwordService.checkPassword(benutzer.getSalt(), benutzer.getPasswordHash(), loginData.getPassword())) {
      return false;
    }
    return true;
  }

  @Override
  @Transactional
  public void changePassword(ChangePasswordCommand command) {
    Benutzer benutzer = benutzerDao.findById(command.getLogin());
    if (benutzer == null) {
      throw new NotFoundException("Benutzer nicht gefunden");
    }

    if (!passwordService.checkPassword(benutzer.getSalt(), benutzer.getPasswordHash(), command.getOldPassword())) {
      throw new NotAuthorizedException("Das Passwort ist falsch");
    }

    benutzer.setPasswordHash(passwordService.encodePassword(benutzer.getSalt(), command.getNewPassword()));
    benutzerDao.update(benutzer);
  }

}
