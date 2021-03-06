package de.fisp.anwesenheit.core.util;

/**
 * Exception, die signalsiert, dass der Benutzer eine Aktion ausführt, für die
 * er nicht authorisiert ist.
 */
public class NotAuthorizedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotAuthorizedException() {
    super();
  }

  public NotAuthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAuthorizedException(String message) {
    super(message);
  }

  public NotAuthorizedException(Throwable cause) {
    super(cause);
  }
}
