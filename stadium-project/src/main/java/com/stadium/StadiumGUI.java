package com.stadium;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.time.*;
import java.util.Date;

public class StadiumGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    public StadiumGUI() {
        setTitle("Stadium Booking System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            Image bgImage;
            {
                try {
                    InputStream is = getClass().getResourceAsStream("/images/background.png");
                    if (is != null) {
                        bgImage = ImageIO.read(is);
                    }
                } catch (Exception ignored) {}
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0, 0, 0, 110));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        backgroundPanel.setLayout(new CardLayout());
        cardLayout = (CardLayout) backgroundPanel.getLayout();
        container = backgroundPanel;

        container.add(welcomePage(), "WELCOME");
        container.add(registerPage(), "REGISTER");
        container.add(loginPage(), "LOGIN");
        container.add(bookingPage(), "BOOKING");

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JPanel createCard(String title) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 450)); // ปรับขนาดให้รองรับข้อมูลที่มากขึ้น
        card.setBackground(new Color(255, 255, 255, 210));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(header);
        card.add(Box.createVerticalStrut(20));
        wrapper.add(card);
        return wrapper;
    }

    private void styleButton(JButton b) {
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setMaximumSize(new Dimension(250, 45));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JPanel welcomePage() {
        JPanel panel = createCard("Welcome");
        JPanel card = (JPanel) panel.getComponent(0);
        JLabel sub = new JLabel("Book your stadium in seconds");
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton start = new JButton("Get Started");
        styleButton(start);
        start.addActionListener(e -> cardLayout.show(container, "REGISTER"));
        card.add(sub);
        card.add(Box.createVerticalStrut(30));
        card.add(start);
        return panel;
    }

    private JPanel registerPage() {
        JPanel panel = createCard("Register");
        JPanel card = (JPanel) panel.getComponent(0);
        JTextField user = new JTextField(15);
        JPasswordField pass = new JPasswordField(15);
        JButton btn = new JButton("Register Account");
        styleButton(btn);

        btn.addActionListener(e -> {
            User newUser = new User(user.getText(), new String(pass.getPassword()));
            UserStorage.saveUser(newUser);
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            cardLayout.show(container, "LOGIN");
        });

        card.add(new JLabel("Username:"));
        card.add(user);
        card.add(Box.createVerticalStrut(10));
        card.add(new JLabel("Password:"));
        card.add(pass);
        card.add(Box.createVerticalStrut(25));
        card.add(btn);
        return panel;
    }

    private JPanel loginPage() {
        JPanel panel = createCard("Login");
        JPanel card = (JPanel) panel.getComponent(0);
        JTextField user = new JTextField(15);
        JPasswordField pass = new JPasswordField(15);
        JButton btn = new JButton("Login");
        styleButton(btn);

        btn.addActionListener(e -> {
            if (UserStorage.validateLogin(user.getText(), new String(pass.getPassword()))) {
                cardLayout.show(container, "BOOKING");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        });

        card.add(new JLabel("Username:"));
        card.add(user);
        card.add(Box.createVerticalStrut(10));
        card.add(new JLabel("Password:"));
        card.add(pass);
        card.add(Box.createVerticalStrut(25));
        card.add(btn);
        return panel;
    }

    private JPanel bookingPage() {
        JPanel panel = createCard("Book Stadium");
        JPanel card = (JPanel) panel.getComponent(0);

        // รายการกีฬาที่เพิ่มขึ้น
        String[] sports = {
            "Football (Outdoor)", "Futsal (Indoor)", "Basketball", 
            "Badminton", "Tennis", "Volleyball", "Fitness Gym"
        };
        JComboBox<String> field = new JComboBox<>(sports);

        JSpinner date = new JSpinner(new SpinnerDateModel());
        date.setEditor(new JSpinner.DateEditor(date, "yyyy-MM-dd"));

        JComboBox<String> start = new JComboBox<>(generateTime());
        JComboBox<String> end = new JComboBox<>(generateTime());

        JButton next = new JButton("Confirm Booking");
        styleButton(next);

        next.addActionListener(e -> {
            Date d = (Date) date.getValue();
            LocalDate local = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (local.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Cannot book a date in the past!");
                return;
            }

            LocalTime s = LocalTime.parse(start.getSelectedItem().toString());
            LocalTime en = LocalTime.parse(end.getSelectedItem().toString());

            if (!en.isAfter(s)) {
                JOptionPane.showMessageDialog(this, "End time must be after start time!");
                return;
            }

            // คำนวณราคาเบื้องต้น (ตัวอย่าง 1 ชม. = 1 หน่วยราคา)
            long hours = java.time.Duration.between(s, en).toHours();
            Booking booking = new Booking(field.getSelectedItem().toString(), local, s, en);
            
            showReceipt(booking);
        });

        // ปรับการจัดวาง Layout เพิ่ม Label นำหน้า
        card.add(new JLabel("Select Sport:"));
        card.add(field);
        card.add(Box.createVerticalStrut(10));
        card.add(new JLabel("Select Date:"));
        card.add(date);
        card.add(Box.createVerticalStrut(10));
        
        JPanel timePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        timePanel.setOpaque(false);
        timePanel.add(new JLabel("Start:"));
        timePanel.add(new JLabel("End:"));
        timePanel.add(start);
        timePanel.add(end);
        
        card.add(timePanel);
        card.add(Box.createVerticalStrut(25));
        card.add(next);

        return panel;
    }

    private void showReceipt(Booking booking) {
        JPanel panel = createCard("Booking Receipt");
        JPanel card = (JPanel) panel.getComponent(0);

        // แสดงรายละเอียดการจอง
        JLabel info1 = new JLabel("ID: " + booking.getId());
        JLabel info2 = new JLabel("Sport: " + booking.getField());
        JLabel info3 = new JLabel("Time: " + booking.getStartTime() + " - " + booking.getEndTime());
        JLabel info4 = new JLabel("Total Price: " + booking.calculatePrice() + " THB");

        for (JLabel l : new JLabel[]{info1, info2, info3, info4}) {
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            card.add(l);
            card.add(Box.createVerticalStrut(10));
        }

        JButton finish = new JButton("Go Back Home");
        styleButton(finish);
        finish.addActionListener(e -> cardLayout.show(container, "WELCOME"));
        card.add(Box.createVerticalStrut(20));
        card.add(finish);

        container.add(panel, "RECEIPT");
        cardLayout.show(container, "RECEIPT");
    }

    private String[] generateTime() {
        String[] t = new String[15];
        for (int i = 0; i < 15; i++)
            t[i] = String.format("%02d:00", 8 + i);
        return t;
    }
}