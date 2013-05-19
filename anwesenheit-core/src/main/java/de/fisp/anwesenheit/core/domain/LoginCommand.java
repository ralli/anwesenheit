package de.fisp.anwesenheit.core.domain;

import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class LoginCommand {
  @NotBlank(message = "Geben Sie bitte einen Benutzernamen an")
  @Size(max = 30)
  private String login;
  @NotBlank(message = "Geben Sie bitte ein Passwort an")
  @Size(max = 30)
  private String password;
  private String redirectUrl;

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

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.append("login", login).append("password", "*****").append("redirectUrl", redirectUrl);
    return b.toString();
  }
}
