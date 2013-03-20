package de.fisp.anwesenheit.core.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LoginCommand {
  @NotNull
  @Size(min = 3, max = 30)
  private String login;
  @NotNull
  @Size(min = 3, max = 30)
  private String password;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.append("login", login).append("password", "*****");
    return b.toString();
  }
}
