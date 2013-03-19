package de.fisp.anwesenheit.core.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LoginCommand {
  private String login;
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
