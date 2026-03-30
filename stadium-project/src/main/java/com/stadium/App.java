package com.stadium;

import javax.swing.SwingUtilities;

/**
 * Main Application Class
 * ทำหน้าที่เป็นจุดเริ่มต้นของโปรแกรม Stadium Booking System
 */
public class App {
    public static void main(String[] args) {
        // เริ่มต้นการทำงานของ GUI บน Event Dispatch Thread เพื่อความปลอดภัยของ Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // เรียกใช้ Main.java เพื่อตั้งค่า Theme และ Font ก่อนแสดงผล
                Main.main(args);
            } catch (Exception e) {
                System.err.println("Could not start the application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}