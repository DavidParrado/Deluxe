package Helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {

  public  String encryptPassword(String password) {
    try {
      // Create MessageDigest instance for SHA-256
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      // Add password bytes to digest
      md.update(password.getBytes());
      // Get the hash's bytes
      byte[] bytes = md.digest();
      // Convert bytes to hexadecimal format
      StringBuilder sb = new StringBuilder();
      for (byte aByte : bytes) {
        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
      }
      // Get complete hashed password in hexadecimal format
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void main(String[] args) {
    String password = "12345678";
    String encryptedPassword = encryptPassword(password);
    System.out.println("Encrypted Password: " + encryptedPassword);
  }
}