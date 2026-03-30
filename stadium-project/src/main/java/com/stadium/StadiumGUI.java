package com.stadium;
 
import javax.swing.*;

import javax.swing.border.EmptyBorder;

import javax.imageio.ImageIO;

import java.awt.*;

import java.awt.geom.*;

import java.io.InputStream;

import java.net.URL;

import java.time.*;

import java.util.Date;
 
public class StadiumGUI extends JFrame {
 
    private CardLayout cardLayout;

    private JPanel container;

    private final Color DARK_BLUE = new Color(45, 50, 80);    

    private final Color CORAL_PINK = new Color(214, 123, 123);

    private final Color FIELD_BORDER = new Color(230, 230, 230);
 
    public StadiumGUI() {

        setTitle("Stadium Booking System");

        setSize(1100, 800); 

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
 
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
 
        // เพิ่มหน้าจอทั้งหมดลงใน CardLayout

        container.add(welcomePage(), "WELCOME");

        container.add(registerPage(), "REGISTER");

        container.add(loginPage(), "LOGIN");

        container.add(forgotPasswordPage(), "FORGOT"); 

        container.add(bookingPage(), "BOOKING");
 
        backgroundPanel.add(container);

        setContentPane(backgroundPanel);

        setVisible(true);

    }
 
    // ================= Password Eye Toggle =================

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

        eyeBtn.setFocusPainted(false); eyeBtn.setBorderPainted(false); eyeBtn.setContentAreaFilled(false);

        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        char defaultChar = passField.getEchoChar();

        eyeBtn.addActionListener(e -> passField.setEchoChar(eyeBtn.isSelected() ? (char) 0 : defaultChar));
 
        passField.setLayout(new BorderLayout());

        passField.add(eyeBtn, BorderLayout.EAST);

        wrapper.add(passField, BorderLayout.CENTER);

        return wrapper;

    }
 
    // ================= Pages =================

    private JPanel welcomePage() {

        JPanel form = new JPanel();

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setOpaque(false);

        JButton startBtn = new JButton("Get Started");

        styleCoralButton(startBtn);

        startBtn.addActionListener(e -> cardLayout.show(container, "REGISTER"));

        form.add(Box.createVerticalGlue()); form.add(startBtn); form.add(Box.createVerticalGlue());

        return createSplitCard("Stadium Hub", "Welcome to the best booking system", form);

    }
 
    private JPanel registerPage() {

        JPanel form = new JPanel();

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setOpaque(false);

        JTextField user = new JTextField();

        JPasswordField pass = new JPasswordField();

        styleField(user);

        JPanel passPanel = createPasswordFieldWithEye(pass);

        JButton btn = new JButton("Create Account");

        styleCoralButton(btn);

        btn.addActionListener(e -> {

            if (user.getText().trim().isEmpty() || new String(pass.getPassword()).isEmpty()) {

                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");

            } else {

                UserStorage.saveUser(new User(user.getText().trim(), new String(pass.getPassword())));

                JOptionPane.showMessageDialog(this, "Registered successfully!");

                cardLayout.show(container, "LOGIN");

            }

        });

        form.add(new JLabel("Create Username")); form.add(user);

        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Create Password")); form.add(passPanel);

        form.add(Box.createVerticalStrut(20)); form.add(btn);

        return createSplitCard("Register", "Step 1: Create your identity", form);

    }
 
    private JPanel loginPage() {

        JPanel form = new JPanel();

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setOpaque(false);

        JTextField user = new JTextField();

        JPasswordField pass = new JPasswordField();

        styleField(user);

        JPanel passPanel = createPasswordFieldWithEye(pass);
 
        JButton forgotBtn = new JButton("Forgot Password?");

        forgotBtn.setForeground(Color.GRAY);

        forgotBtn.setBorder(null); forgotBtn.setContentAreaFilled(false);

        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        forgotBtn.setAlignmentX(0.5f);

        forgotBtn.addActionListener(e -> cardLayout.show(container, "FORGOT"));
 
        JButton btn = new JButton("Login");

        styleCoralButton(btn);

        btn.addActionListener(e -> {

            if (UserStorage.validateLogin(user.getText().trim(), new String(pass.getPassword()))) {

                cardLayout.show(container, "BOOKING");

            } else {

                JOptionPane.showMessageDialog(this, "Login Failed!", "Error", JOptionPane.ERROR_MESSAGE);

            }

        });
 
        form.add(new JLabel("Username")); form.add(user);

        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Password")); form.add(passPanel);

        form.add(Box.createVerticalStrut(5));

        form.add(forgotBtn);

        form.add(Box.createVerticalStrut(15)); form.add(btn);

        return createSplitCard("Sign In", "Step 2: Access your account", form);

    }
 
    private JPanel forgotPasswordPage() {

        JPanel form = new JPanel();

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setOpaque(false);

        JTextField user = new JTextField();

        JPasswordField newPass = new JPasswordField();

        styleField(user);

        JPanel passPanel = createPasswordFieldWithEye(newPass);
 
        JButton resetBtn = new JButton("Reset Password & Login");

        styleCoralButton(resetBtn);

        resetBtn.addActionListener(e -> {

            String u = user.getText().trim();

            String p = new String(newPass.getPassword());

            if (!u.isEmpty() && !p.isEmpty()) {

                UserStorage.saveUser(new User(u, p));

                JOptionPane.showMessageDialog(this, "Password Reset Success!");

                cardLayout.show(container, "LOGIN");

            }

        });
 
        JButton cancel = new JButton("Cancel");

        cancel.setAlignmentX(0.5f);

        cancel.addActionListener(e -> cardLayout.show(container, "LOGIN"));
 
        form.add(new JLabel("Confirm Your Username")); form.add(user);

        form.add(Box.createVerticalStrut(10));

        form.add(new JLabel("Set New Password")); form.add(passPanel);

        form.add(Box.createVerticalStrut(20)); form.add(resetBtn);

        form.add(Box.createVerticalStrut(10)); form.add(cancel);

        return createSplitCard("Reset Password", "Create your new credential", form);

    }
 
    private JPanel bookingPage() {

        JPanel form = new JPanel();

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setOpaque(false);

        String[] options = {"Football Arena A", "Football Arena B", "Futsal Zone", "Basketball Court", "Badminton 1", "Badminton 2", "Tennis Court"};

        JComboBox<String> sports = new JComboBox<>(options);

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

        form.add(new JLabel("Select Time")); form.add(timeGrid);

        form.add(Box.createVerticalStrut(20)); form.add(confirmBtn);

        return createSplitCard("Booking", "Step 3: Pick your field", form);

    }
 
    // --- หน้าจอสุดท้าย: QR Code และปุ่ม Finish (ดึงกลับมาให้แล้วครับ) ---

    private void showReceipt(Booking b) {

        JPanel receipt = new JPanel();

        receipt.setLayout(new BoxLayout(receipt, BoxLayout.Y_AXIS));

        receipt.setOpaque(false);
 
        String text = "<html><body style='width: 250px; text-align: center;'>" +

                      "<h3>Booking Summary</h3>" +

                      "<b>Stadium:</b> " + b.getField() + "<br>" +

                      "<b>Date:</b> " + b.getDate() + "<br>" +

                      "<b>Time:</b> " + b.getStartTime() + " - " + b.getEndTime() + "<br>" +

                      "<h3 style='color: #D67B7B;'>Total: " + b.calculatePrice() + " THB</h3>" +

                      "<p style='font-size: 9px;'>Scan to Pay Now</p></body></html>";

        JLabel summary = new JLabel(text);

        summary.setAlignmentX(Component.CENTER_ALIGNMENT);

        receipt.add(summary);
 
        // QR Code (250x250)

        URL qrUrl = getClass().getResource("/images/qr_payment.png");

        if (qrUrl != null) {

            ImageIcon qrIcon = new ImageIcon(new ImageIcon(qrUrl).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));

            JLabel qrLabel = new JLabel(qrIcon);

            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            receipt.add(qrLabel);

        }
 
        receipt.add(Box.createVerticalStrut(15));
 
        JButton finishBtn = new JButton("Finish & Logout");

        styleCoralButton(finishBtn);

        finishBtn.addActionListener(e -> cardLayout.show(container, "WELCOME"));

        receipt.add(finishBtn);
 
        // นำหน้า Receipt ใส่เข้าไปใน CardLayout และแสดงผล

        container.add(createSplitCard("Receipt", "Final Step: Payment Info", receipt), "RECEIPT");

        cardLayout.show(container, "RECEIPT");

    }
 
    private JPanel createSplitCard(String title, String subTitle, JPanel formPanel) {

        JPanel mainCard = new JPanel(new BorderLayout()) {

            @Override protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.dispose();

            }

        };

        mainCard.setPreferredSize(new Dimension(850, 650));

        mainCard.setOpaque(false);
 
        JPanel leftPanel = new JPanel(new GridBagLayout()) {

            @Override protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(DARK_BLUE); g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() + 50, getHeight(), 40, 40));

                g2.dispose();

            }

        };

        leftPanel.setPreferredSize(new Dimension(350, 650)); leftPanel.setOpaque(false);

        JLabel logo = new JLabel("STADIUM");

        logo.setFont(new Font("Segoe UI", Font.BOLD, 42)); logo.setForeground(Color.WHITE);

        leftPanel.add(logo);
 
        JPanel rightPanel = new JPanel();

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.setOpaque(false); rightPanel.setBorder(new EmptyBorder(20, 60, 20, 60));

        JLabel head = new JLabel(title); head.setFont(new Font("Segoe UI", Font.BOLD, 30)); head.setAlignmentX(0.5f);

        JLabel sub = new JLabel(subTitle); sub.setForeground(Color.GRAY); sub.setAlignmentX(0.5f);

        rightPanel.add(head); rightPanel.add(sub);

        rightPanel.add(Box.createVerticalStrut(15)); rightPanel.add(formPanel);
 
        mainCard.add(leftPanel, BorderLayout.WEST);

        mainCard.add(rightPanel, BorderLayout.CENTER);

        return mainCard;

    }
 
    private void styleField(JComponent c) {

        c.setMaximumSize(new Dimension(350, 40)); c.setPreferredSize(new Dimension(350, 40));

        c.setBackground(Color.WHITE);

        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(FIELD_BORDER, 2, true), BorderFactory.createEmptyBorder(2, 10, 2, 10)));

    }
 
    private void styleCoralButton(JButton b) {

        b.setMaximumSize(new Dimension(350, 45)); b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);

        b.setForeground(Color.WHITE); b.setFont(new Font("Segoe UI", Font.BOLD, 15)); b.setAlignmentX(0.5f);

        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {

            @Override public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CORAL_PINK); g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);

                super.paint(g2, c); g2.dispose();

            }

        });

    }
 
    private String[] generateTime() {

        String[] t = new String[15];

        for (int i = 0; i < 15; i++) t[i] = String.format("%02d:00", 8 + i);

        return t;

    }

}
 