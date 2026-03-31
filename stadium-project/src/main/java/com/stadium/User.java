package com.stadium;

public class User {
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String birthDate;

    // สำหรับหน้า Register (รับ 7 ค่า)
    public User(String username, String password,
                String firstName, String lastName,
                String phone, String email, String birthDate) {

        this.username = username;
        // แนะนำ: ถ้ามี SecurityUtil ให้ใช้ SecurityUtil.hash(password) ตรงนี้ครับ
        this.passwordHash = password; 
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
    }

    // สำหรับหน้า Reset Password (รับ 2 ค่า)
    public User(String username, String password) {
        this.username = username;
        this.passwordHash = password;
    }

    // --- Getter Methods (เพิ่มให้ครบตามที่ UserStorage เรียกใช้) ---

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    
    public String getLastName() { return lastName; } // เพิ่มตัวนี้
    public String getPhone() { return phone; }       // เพิ่มตัวนี้
    public String getEmail() { return email; }       // เพิ่มตัวนี้
    public String getBirthDate() { return birthDate; } // เพิ่มตัวนี้ (เผื่อใช้ในอนาคต)
}