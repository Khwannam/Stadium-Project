package com.stadium;
 
import java.io.*;
 
public class UserStorage {
 
    private static final String FILE = "users.txt";
 
    public static void saveUser(User user) {
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(FILE, true))) {
 
            bw.write(user.getUsername() + "," +
                    user.getPasswordHash());
            bw.newLine();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static boolean validateLogin(String username,
                                        String password){
 
        try (BufferedReader br =
                     new BufferedReader(new FileReader(FILE))) {
 
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts[0].equals(username) &&
                        parts[1].equals(
                                SecurityUtil.hash(password)))
                    return true;
            }
 
        } catch (Exception ignored) {}
 
        return false;
    }
}
 