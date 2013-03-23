package de.fisp.anwesenheit.core.service;

import de.fisp.anwesenheit.core.domain.ChangePasswordCommand;
import de.fisp.anwesenheit.core.domain.LoginCommand;

public interface LoginService {
  boolean login(LoginCommand loginData);

  void changePassword(ChangePasswordCommand command);
}
