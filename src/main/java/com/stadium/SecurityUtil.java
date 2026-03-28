package com.stadium;
 
import java.security.MessageDigest;
 
public class SecurityUtil {
 
    public static String hash(String input){
        try{
            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
 
            StringBuilder sb = new StringBuilder();
            for(byte b : hash)
                sb.append(String.format("%02x",b));
 
            return sb.toString();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
 