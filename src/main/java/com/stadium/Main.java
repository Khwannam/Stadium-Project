package com.stadium;
 
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
 
public class Main {
    public static void main(String[] args) {
 
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("defaultFont",
                    new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception ignored) {}
 
        SwingUtilities.invokeLater(StadiumGUI::new);
    }
}
 