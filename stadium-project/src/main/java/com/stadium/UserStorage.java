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

    // แก้ไข: เช็คชื่อผู้ใช้ซ้ำ
    public static boolean userExists(String username) {
        File f = new File(FILE);
        if(!f.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(username.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    // แก้ไข: บันทึกข้อมูลแบบ Hash รหัสผ่านก่อนลงไฟล์
    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            // สำคัญ: ต้องใช้ SecurityUtil.hash ก่อนบันทึก เพื่อให้ตรงกับตอน Login
            String hashedPass = SecurityUtil.hash(user.getPasswordHash());

            bw.write(user.getUsername().trim() + "," +
                     hashedPass + "," +
                     user.getFirstName() + "," +
                     user.getLastName() + "," +
                     user.getPhone() + "," +
                     user.getEmail());
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // แก้ไข: ตรวจสอบ Login โดยเทียบค่า Hash
    public static boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            String inputHash = SecurityUtil.hash(password); // แปลงรหัสที่กรอกเป็น Hash

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String storedUser = parts[0].trim();
                    String storedPass = parts[1].trim();

                    // เทียบชื่อ และ เทียบรหัสผ่านที่ Hash แล้ว
                    // Also support legacy plain-text stored passwords by checking against the raw password too
                    if (storedUser.equals(username.trim()) && (storedPass.equals(inputHash) || storedPass.equals(password))) {
                        // Build current user with available fields if present
                        String first = (parts.length > 2 && parts[2] != null && !"null".equals(parts[2])) ? parts[2] : "";
                        String last = (parts.length > 3 && parts[3] != null && !"null".equals(parts[3])) ? parts[3] : "";
                        String phone = (parts.length > 4 && parts[4] != null && !"null".equals(parts[4])) ? parts[4] : "";
                        String email = (parts.length > 5 && parts[5] != null && !"null".equals(parts[5])) ? parts[5] : "";
                        String birth = (parts.length > 6 && parts[6] != null && !"null".equals(parts[6])) ? parts[6] : "";

                        // Store current user using constructor that keeps plain password (we keep the storedPass/hash as passwordHash)
                        User u = new User(storedUser, storedPass, first, last, phone, email, birth);
                        currentUser = u;
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }
}