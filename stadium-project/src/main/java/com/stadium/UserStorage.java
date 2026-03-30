package com.stadium;

import java.io.*;

public class UserStorage {

    private static final String FILE = "users.txt";
    
    // --- ส่วนที่เพิ่มใหม่ (ห้ามลบ) ---
    private static User currentUser; 

    public static User getCurrentUser() {
        return (currentUser != null) ? currentUser : new User("Guest", "");
    }
    // ----------------------------

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
                                SecurityUtil.hash(password))) {
                    
                    // --- ส่วนที่เพิ่มใหม่: เก็บข้อมูล User เมื่อ login สำเร็จ ---
                    currentUser = new User(username, parts[1]); 
                    // --------------------------------------------------
                    
                    return true;
                }
            }

        } catch (Exception ignored) {}

        return false;
    }
}