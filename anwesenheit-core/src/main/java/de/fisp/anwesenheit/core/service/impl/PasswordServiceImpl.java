package de.fisp.anwesenheit.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import de.fisp.anwesenheit.core.service.PasswordService;

public class PasswordServiceImpl implements PasswordService {
  @Override
  public String generateSalt() {
    final int numBytes = 20;
    Random random = new Random();
    byte[] bytes = new byte[numBytes];
    random.nextBytes(bytes);
    return Base64.encodeBase64String(bytes);
  }

  @Override
  public String encodePassword(String salt, String password) {
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      stream.write(Base64.decodeBase64(salt));
      stream.write(password.getBytes("UTF-8"));
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] encoded = md.digest(stream.toByteArray());
      return Base64.encodeBase64String(encoded);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }  
  }

  @Override
  public boolean checkPassword(String salt, String passwordHash, String password) {
    return encodePassword(salt, password).equals(passwordHash);
  }
}
