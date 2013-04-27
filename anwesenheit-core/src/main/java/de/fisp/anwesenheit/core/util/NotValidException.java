package de.fisp.anwesenheit.core.util;

public class NotValidException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotValidException() {
    super();
  }

  public NotValidException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotValidException(String message) {
    super(message);
  }

  public NotValidException(Throwable cause) {
    super(cause);
  }
}
