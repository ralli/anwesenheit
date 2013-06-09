package de.fisp.anwesenheit.core.service.impl;

import de.fisp.anwesenheit.core.service.ChecksumService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ChecksumServiceImpl implements ChecksumService {
  public static final String SECRET = "Anwesenheit";

  @Override
  public String generateChecksum(String input) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    StringBuffer sb = new StringBuffer("");
    sb.append(input);
    sb.append(SECRET);

    byte[] ba = sb.toString().getBytes();
    byte[] hash = digest.digest(ba);

    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      hexString.append(Integer.toHexString(0xFF & hash[i]));
    }
    return hexString.toString();
  }
}
