package com.stadium;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.io.InputStream;
import java.time.*;
import java.util.Date;

public class StadiumGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    
    // UI Colors ตามเรฟเฟอเรนซ์
    private final Color DARK_BLUE = new Color(45, 50, 80);    
    private final Color CORAL_PINK = new Color(214, 123, 123); 
    private final Color FIELD_BORDER = new Color(230, 230, 230);

    public StadiumGUI() {
        setTitle("Stadium Booking System");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- ส่วน Background Overlay ---
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            Image bgImage;
            {
                try {
                    InputStream is = getClass().getResourceAsStream("/images/background.png");
                    if (is != null) bgImage = ImageIO.read(is);
                } catch (Exception ignored) {}
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(0, 0, 0, 130)); 
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            }
        };

        container = new JPanel(new CardLayout());
        container.setOpaque(false);
        cardLayout = (CardLayout) container.getLayout();

        // --- ลำดับหน้าจอ (Flow) ---
        container.add(welcomePage(), "WELCOME");   // 1. Get Started
        container.add(registerPage(), "REGISTER"); // 2. Create Account
        container.add(loginPage(), "LOGIN");       // 3. Login
        container.add(bookingPage(), "BOOKING");   // 4. Selection

        backgroundPanel.add(container);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // ================= ส่วนจัดการ "ตาแมว" (Password View Toggle) =================

    private JPanel createPasswordFieldWithEye(JPasswordField passField) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(350, 40));
        wrapper.setPreferredSize(new Dimension(350, 40));
        wrapper.setOpaque(false);

        styleField(passField);

        JToggleButton eyeBtn = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(1.8f));
                g2.setColor(isSelected() ? CORAL_PINK : Color.LIGHT_GRAY);

                int w = 18, h = 10;
                int x = (getWidth() - w) / 2;
                int y = (getHeight() - h) / 2;

                g2.draw(new Arc2D.Float(x, y - 4, w, h + 8, 0, -180, Arc2D.OPEN));
                g2.draw(new Arc2D.Float(x, y - 4, w, h + 8, 0, 180, Arc2D.OPEN));
                g2.fillOval(x + w/2 - 2, y + h/2 - 2, 4, 4);
                if (!isSelected()) g2.drawLine(x - 1, y + h + 1, x + w + 1, y - 1);
                g2.dispose();
            }
        };

        eyeBtn.setPreferredSize(new Dimension(45, 40));
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBorderPainted(false);
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        char defaultChar = passField.getEchoChar();
        eyeBtn.addActionListener(e -> {
            passField.setEchoChar(eyeBtn.isSelected() ? (char) 0 : defaultChar);
            eyeBtn.repaint();
        });

        passField.setLayout(new BorderLayout());
        passField.add(eyeBtn, BorderLayout.EAST);
        wrapper.add(passField, BorderLayout.CENTER);
        return wrapper;
    }

    // ================= ส่วนประกอบหน้าจอ (Pages) =================

    // หน้าที่ 1: Get Started
    private JPanel welcomePage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        
        JButton startBtn = new JButton("Get Started");
        styleCoralButton(startBtn);
        startBtn.addActionListener(e -> cardLayout.show(container, "REGISTER"));

        form.add(Box.createVerticalGlue());
        form.add(startBtn);
        form.add(Box.createVerticalGlue());

        return createSplitCard("Stadium Hub", "Welcome to the best booking system", form);
    }

    // หน้าที่ 2: Create Account
    private JPanel registerPage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        styleField(user);
        JPanel passPanel = createPasswordFieldWithEye(pass); // ใส่ตาแมว

        JButton btn = new JButton("Create Account");
        styleCoralButton(btn);
        btn.addActionListener(e -> {
            String u = user.getText().trim();
            String p = new String(pass.getPassword()).trim();
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            } else {
                UserStorage.saveUser(new User(u, p));
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                cardLayout.show(container, "LOGIN");
            }
        });

        form.add(new JLabel("Create Username")); form.add(user);
        form.add(Box.createVerticalStrut(15));
        form.add(new JLabel("Create Password")); form.add(passPanel);
        form.add(Box.createVerticalStrut(30));
        form.add(btn);

        return createSplitCard("Register", "Step 1: Create your identity", form);
    }

    // หน้าที่ 3: Login (พร้อมระบบตรวจสอบข้อมูล)
    private JPanel loginPage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        styleField(user);
        JPanel passPanel = createPasswordFieldWithEye(pass); // ใส่ตาแมว

        JButton btn = new JButton("Login");
        styleCoralButton(btn);
        btn.addActionListener(e -> {
            String u = user.getText().trim();
            String p = new String(pass.getPassword()).trim();
            // ตรวจสอบข้อมูลจาก UserStorage
            if (UserStorage.validateLogin(u, p)) {
                cardLayout.show(container, "BOOKING");
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed! Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(new JLabel("Username")); form.add(user);
        form.add(Box.createVerticalStrut(15));
        form.add(new JLabel("Password")); form.add(passPanel);
        form.add(Box.createVerticalStrut(30));
        form.add(btn);

        return createSplitCard("Sign In", "Step 2: Access your account", form);
    }

    // หน้าที่ 4: Selection (เลือกสนาม/วัน/เวลา)
    private JPanel bookingPage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        String[] stadiumOptions = {"Football Arena A", "Football Arena B", "Futsal Zone", "Basketball Court", "Badminton Court 1", "Badminton Court 2", "Tennis Court"};
        JComboBox<String> sports = new JComboBox<>(stadiumOptions);
        
        JSpinner dateSpin = new JSpinner(new SpinnerDateModel());
        dateSpin.setEditor(new JSpinner.DateEditor(dateSpin, "yyyy-MM-dd"));
        
        JComboBox<String> startT = new JComboBox<>(generateTime());
        JComboBox<String> endT = new JComboBox<>(generateTime());

        styleField(sports); styleField(dateSpin); styleField(startT); styleField(endT);

        JButton confirmBtn = new JButton("Book & View Receipt");
        styleCoralButton(confirmBtn);
        confirmBtn.addActionListener(e -> {
            LocalDate d = ((Date) dateSpin.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime s = LocalTime.parse(startT.getSelectedItem().toString());
            LocalTime en = LocalTime.parse(endT.getSelectedItem().toString());
            showReceipt(new Booking(sports.getSelectedItem().toString(), d, s, en));
        });

        form.add(new JLabel("Choose Stadium")); form.add(sports);
        form.add(Box.createVerticalStrut(10));
        form.add(new JLabel("Choose Date")); form.add(dateSpin);
        form.add(Box.createVerticalStrut(10));
        
        JPanel timeGrid = new JPanel(new GridLayout(1, 2, 10, 0));
        timeGrid.setOpaque(false); timeGrid.add(startT); timeGrid.add(endT);
        form.add(new JLabel("Select Time (Start - End)"));
        form.add(timeGrid);
        form.add(Box.createVerticalStrut(25));
        form.add(confirmBtn);

        return createSplitCard("Booking", "Step 3: Pick your field", form);
    }

    // หน้าสุดท้าย: Receipt (แสดงรายละเอียดและชำระเงิน)
    private void showReceipt(Booking b) {
        JPanel receipt = new JPanel();
        receipt.setLayout(new BoxLayout(receipt, BoxLayout.Y_AXIS));
        receipt.setOpaque(false);

        String text = "<html><body style='width: 250px; text-align: center;'>" +
                      "<h2>Booking Summary</h2>" +
                      "<b>Stadium:</b> " + b.getField() + "<br>" +
                      "<b>Date:</b> " + b.getDate() + "<br>" +
                      "<b>Time:</b> " + b.getStartTime() + " - " + b.getEndTime() + "<br><br>" +
                      "<h2 style='color: #D67B7B;'>Total: " + b.calculatePrice() + " THB</h2>" +
                      "<p>Please proceed with payment at the counter.</p>" +
                      "</body></html>";
        
        JLabel summary = new JLabel(text);
        summary.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton finishBtn = new JButton("Finish & Logout");
        styleCoralButton(finishBtn);
        finishBtn.addActionListener(e -> cardLayout.show(container, "WELCOME"));

        receipt.add(summary);
        receipt.add(Box.createVerticalStrut(20));
        receipt.add(finishBtn);

        container.add(createSplitCard("Receipt", "Final Step: Payment Info", receipt), "RECEIPT");
        cardLayout.show(container, "RECEIPT");
    }

    // ================= ส่วน Styling และ Helper =================

    private JPanel createSplitCard(String title, String subTitle, JPanel formPanel) {
        JPanel mainCard = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        mainCard.setPreferredSize(new Dimension(850, 550));
        mainCard.setOpaque(false);

        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK_BLUE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() + 50, getHeight(), 40, 40));
                g2.dispose();
            }
        };
        leftPanel.setPreferredSize(new Dimension(350, 550));
        leftPanel.setOpaque(false);
        JLabel logo = new JLabel("STADIUM");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logo.setForeground(Color.WHITE);
        leftPanel.add(logo);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel head = new JLabel(title);
        head.setFont(new Font("Segoe UI", Font.BOLD, 32));
        head.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel(subTitle);
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(head); rightPanel.add(sub);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(formPanel);

        mainCard.add(leftPanel, BorderLayout.WEST);
        mainCard.add(rightPanel, BorderLayout.CENTER);
        return mainCard;
    }

    private void styleField(JComponent c) {
        c.setMaximumSize(new Dimension(350, 40));
        c.setPreferredSize(new Dimension(350, 40));
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 2, true),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));
    }

    private void styleCoralButton(JButton b) {
        b.setMaximumSize(new Dimension(350, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CORAL_PINK);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    private String[] generateTime() {
        String[] t = new String[15];
        for (int i = 0; i < 15; i++) t[i] = String.format("%02d:00", 8 + i);
        return t;
    }
}