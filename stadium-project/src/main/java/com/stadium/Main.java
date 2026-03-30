package com.stadium;
 
import javax.swing.UIManager;
import java.awt.Font;
 
public class Main {
    public static void main(String[] args) {
 
        // 1. ตั้งค่า Theme
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception ignored) {}
 
        // 2. แสดงหน้าจอ
        new StadiumGUI().setVisible(true);
    }
}