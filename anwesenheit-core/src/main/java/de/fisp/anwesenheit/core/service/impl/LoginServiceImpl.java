package de.fisp.anwesenheit.core.service.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.fisp.anwesenheit.core.dao.BenutzerDao;
import de.fisp.anwesenheit.core.domain.BenutzerDaten;
import de.fisp.anwesenheit.core.domain.ChangePasswordCommand;
import de.fisp.anwesenheit.core.domain.LoginCommand;
import de.fisp.anwesenheit.core.entities.Benutzer;
import de.fisp.anwesenheit.core.service.LoginService;
import de.fisp.anwesenheit.core.service.PasswordService;
import de.fisp.anwesenheit.core.util.NotAuthorizedException;
import de.fisp.anwesenheit.core.util.NotFoundException;

@Service
public class LoginServiceImpl implements LoginService {
  private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
  private BenutzerDao benutzerDao;
  private PasswordService passwordService;

  // private static final Logger logger =
  // LoggerFactory.getLogger(LoginServiceImpl.class);

  @Autowired
  public LoginServiceImpl(BenutzerDao benutzerDao, PasswordService passwordService) {
    this.benutzerDao = benutzerDao;
    this.passwordService = passwordService;
  }

  private static final String PROVIDER_URL = "ldap://1822-s-inform.de:389";
  private static final String DOMAIN = "1822SINF-D001";

  private String getPrincipal(String domain, String user) {
    return String.format("%s\\%s", domain, user);
  }

  private boolean loginViaLDAP(LoginCommand loginData) {
    try {
      Hashtable<String, String> env = new Hashtable<String, String>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, PROVIDER_URL);

      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, getPrincipal(DOMAIN, loginData.getLogin()));
      env.put(Context.SECURITY_CREDENTIALS, loginData.getPassword());

      DirContext ctx = new InitialDirContext(env);
      ctx.close();
      return true;
    } catch (Exception ex) {
      logger.error("loginViaLDAP", ex);
      return false;
    }
  }

  @Override
  @Transactional
  public BenutzerDaten login(LoginCommand loginData) {
    Benutzer benutzer = benutzerDao.findById(loginData.getLogin().toLowerCase());
    if (benutzer == null) {
      return null;
    }
    if ("ldap".equals(benutzer.getBenutzertyp())) {
      if (!loginViaLDAP(loginData)) {
        return null;
      }
    } else if (!passwordService.checkPassword(benutzer.getSalt(), benutzer.getPasswordHash(), loginData.getPassword())) {
      return null;
    }
    return new BenutzerDaten(benutzer.getBenutzerId(), benutzer.getVorname(), benutzer.getNachname(), benutzer.getEmail());
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
