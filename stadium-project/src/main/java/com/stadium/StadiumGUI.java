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
    
    private String selectedStadiumName = ""; 
    private JButton lastSelectedCard = null;
    private String selectedTime = "";
    private JButton lastSelectedTimeBtn = null;
    private String startTime = "";
    private String endTime = "";
    private JButton firstTimeBtn = null;
    private JButton secondTimeBtn = null;
    private JSpinner dateSpinner; // ใช้ JSpinner แทน JComboBox สำหรับปฏิทินจริง
    private double selectedPricePerHr = 0.0;

 
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
 
        container.add(welcomePage(), "WELCOME");

        container.add(registerPage(), "REGISTER");

        container.add(loginPage(), "LOGIN");

        container.add(forgotPasswordPage(), "FORGOT"); 

        container.add(bookingPage(), "BOOKING");
 
        backgroundPanel.add(container);

        setContentPane(backgroundPanel);

        setVisible(true);

    }
 
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
 
    private JPanel welcomePage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // 1. ส่วนของรูปภาพกราฟิกด้านขวา
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // จัดให้อยู่ตรงกลางแนวตั้ง
        
        try {
            // โหลดรูปภาพ (แนะนำให้ใช้ไฟล์ .png ที่ไม่มีพื้นหลังจะสวยมาก)
            InputStream is = getClass().getResourceAsStream("/images/welcome_hero.png");
            if (is != null) {
                Image img = ImageIO.read(is);
                // ปรับขนาดรูปให้เหมาะสมกับพื้นที่ขาว (ประมาณ 250-300 px)
                ImageIcon icon = new ImageIcon(img.getScaledInstance(280, 450, Image.SCALE_SMOOTH));
                imageLabel.setIcon(icon);
            } else {
                // ถ้ายังไม่มีไฟล์รูป ให้แสดงข้อความ Placeholder ไว้ก่อน
                imageLabel.setText("🏟️"); 
                imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 100));
            }
        } catch (Exception e) {
            System.err.println("Could not load welcome image");
        }

        // 2. ปุ่ม Get Started
        JButton startBtn = new JButton("Get Started");
        styleCoralButton(startBtn);
        startBtn.addActionListener(e -> cardLayout.show(container, "REGISTER"));

        // 3. การจัดวาง (Layout)
        form.add(Box.createVerticalGlue());    // ดันเนื้อหาลงมาจากด้านบน
        form.add(imageLabel);                  // ใส่รูปภาพ
        form.add(Box.createVerticalStrut(30)); // ระยะห่างระหว่างรูปกับปุ่ม
        form.add(startBtn);                    // ใส่ปุ่ม
        form.add(Box.createVerticalGlue());    // ดันเนื้อหาขึ้นมาจากด้านล่าง

        return createSplitCard("Stadium Hub", "Welcome to the best booking system", form);
    }
 
    private JPanel registerPage() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();

        JSpinner birthDate = new JSpinner(new SpinnerDateModel());
        birthDate.setEditor(new JSpinner.DateEditor(birthDate, "yyyy-MM-dd"));

        styleField(user);
        styleField(firstName);
        styleField(lastName);
        styleField(phone);
        styleField(email);
        styleField(birthDate);

        JPanel passPanel = createPasswordFieldWithEye(pass);

        JButton btn = new JButton("Create Account");
        styleCoralButton(btn);

        // --- เพิ่มปุ่ม "Already have an account?" ตรงนี้ ---
        JButton toLoginBtn = new JButton("Already have an account? Login");
        toLoginBtn.setForeground(Color.GRAY);
        toLoginBtn.setBorder(null);
        toLoginBtn.setContentAreaFilled(false);
        toLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toLoginBtn.setAlignmentX(0.5f); // จัดกลาง
        toLoginBtn.addActionListener(e -> cardLayout.show(container, "LOGIN")); 
        // -------------------------------------------

        btn.addActionListener(e -> {
            String username = user.getText().trim();
            String password = new String(pass.getPassword());

            if (username.isEmpty() || password.isEmpty()
                    || firstName.getText().trim().isEmpty()
                    || lastName.getText().trim().isEmpty()
                    || phone.getText().trim().isEmpty()
                    || email.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "กรอกข้อมูลให้ครบ!");
                return;
            }

            if (UserStorage.userExists(username)) {
                JOptionPane.showMessageDialog(this, "Username นี้มีคนใช้แล้ว!");
                return;
            }

            User newUser = new User(
                username,
                password,
                firstName.getText(),
                lastName.getText(),
                phone.getText(),
                email.getText(),
                ((java.util.Date) birthDate.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                    .toString()
            );
            UserStorage.saveUser(newUser);

            JOptionPane.showMessageDialog(this, "สมัครสำเร็จ!");
            cardLayout.show(container, "LOGIN");
        });

        // ===== จัดวาง UI =====
        form.add(new JLabel("Username")); form.add(user);
        form.add(Box.createVerticalStrut(5));

        form.add(new JLabel("Password")); form.add(passPanel);
        form.add(Box.createVerticalStrut(5));

        // ลดขนาด VerticalStrut ลงนิดหน่อยเพื่อให้ปุ่มใหม่ไม่เบียดขอบล่างเกินไป
        form.add(new JLabel("First Name")); form.add(firstName);
        form.add(Box.createVerticalStrut(5));
        
        form.add(new JLabel("Last Name")); form.add(lastName); // เพิ่มบรรทัดนี้
        form.add(Box.createVerticalStrut(5));

        form.add(new JLabel("Phone Number")); form.add(phone); // เพิ่มบรรทัดนี้ที่หายไป
        form.add(Box.createVerticalStrut(5));

        form.add(new JLabel("Email")); form.add(email);
        form.add(Box.createVerticalStrut(5));

        form.add(new JLabel("Birth Date")); form.add(birthDate);
        form.add(Box.createVerticalStrut(15));

        form.add(btn);
        form.add(Box.createVerticalStrut(10)); 
        form.add(toLoginBtn); // วางปุ่มใหม่ไว้ใต้ปุ่ม Create Account

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
 
    // --- 1. เมธอดสร้างหน้าจอง (จำกัด 2 ชั่วโมง และปุ่มเวลาครบ) ---
    private JPanel bookingPage() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(10, 10, 20, 10));

        // --- 1. เลือกสนาม ---
        JLabel labelSport = new JLabel("1. Select Stadium");
        labelSport.setForeground(Color.WHITE);
        labelSport.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainContent.add(labelSport);
        mainContent.add(Box.createVerticalStrut(10));

        JPanel grid = new JPanel(new GridLayout(0, 3, 10, 10)); 
        grid.setOpaque(false);
        grid.add(createStadiumCard("Football A", "800", "/images/football_a.png"));
        grid.add(createStadiumCard("Football B", "800", "/images/football_b.png"));
        grid.add(createStadiumCard("Futsal", "500", "/images/futsal.png"));
        grid.add(createStadiumCard("Basketball", "600", "/images/basketball.png"));
        grid.add(createStadiumCard("Badminton", "200", "/images/badminton1.png"));
        grid.add(createStadiumCard("Badminton", "200", "/images/badminton2.png"));
        grid.add(createStadiumCard("Tennis", "400", "/images/tennis.png"));
        mainContent.add(grid);
        mainContent.add(Box.createVerticalStrut(25));

        // --- 2. เลือกวันที่ ---
        JLabel labelDate = new JLabel("2. Select Date");
        labelDate.setForeground(Color.WHITE);
        labelDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainContent.add(labelDate);
        mainContent.add(Box.createVerticalStrut(10));

        java.util.Date today = new java.util.Date();
        SpinnerDateModel model = new SpinnerDateModel(today, today, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        dateSpinner.setMaximumSize(new Dimension(350, 40));
        mainContent.add(dateSpinner);
        mainContent.add(Box.createVerticalStrut(25));

        // --- 3. เลือกช่วงเวลา ---
        JLabel labelTime = new JLabel("3. Select Time Range (Max 2 Hours)");
        labelTime.setForeground(Color.WHITE);
        labelTime.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainContent.add(labelTime);
        mainContent.add(Box.createVerticalStrut(5));
        
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        timePanel.setOpaque(false);
        String[] slots = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
        
        for (String slot : slots) {
            JButton tBtn = new JButton(slot);
            styleTimeButton(tBtn);
            tBtn.addActionListener(e -> {
                // ถ้ายังไม่ได้เลือกเวลาเริ่ม หรือเลือกครบสองอันแล้ว ให้เริ่มนับใหม่
                if (startTime.isEmpty() || (!startTime.isEmpty() && !endTime.isEmpty())) {
                    resetTimeSelection(timePanel); // เรียกใช้ฟังก์ชันล้างสีปุ่ม
                    startTime = slot;
                    endTime = ""; 
                    tBtn.setBackground(CORAL_PINK);
                } else {
                    // เลือกเวลาสิ้นสุด
                    int startH = Integer.parseInt(startTime.split(":")[0]);
                    int endH = Integer.parseInt(slot.split(":")[0]);
                    
                    if (endH <= startH) {
                        JOptionPane.showMessageDialog(this, "End time must be after start time!");
                        return;
                    }
                    if (endH - startH > 2) {
                        JOptionPane.showMessageDialog(this, "Maximum 2 hours allowed!");
                        return;
                    }
                    
                    endTime = slot;
                    tBtn.setBackground(CORAL_PINK);
                }
            });
            timePanel.add(tBtn);
        }
        mainContent.add(timePanel);
        mainContent.add(Box.createVerticalStrut(30));

        // --- 4. ปุ่มยืนยัน (จุดที่แก้ไขราคา) ---
        JButton confirmBtn = new JButton("Confirm Booking");
        styleCoralButton(confirmBtn); 
        confirmBtn.addActionListener(e -> {
            if (selectedStadiumName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select Stadium and Time Range!");
                return;
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String finalDate = sdf.format((java.util.Date) dateSpinner.getValue());

            int diff = Integer.parseInt(endTime.split(":")[0]) - Integer.parseInt(startTime.split(":")[0]);
            
            // แก้ไขตรงนี้: ใช้ selectedPricePerHr ที่เก็บจากการกดเลือกสนาม แทนเลข 800.0
            Booking b = new Booking(selectedStadiumName, finalDate, startTime, endTime, selectedPricePerHr * diff);
            showReceipt(b);
        });
        mainContent.add(confirmBtn);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setOpaque(false); scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return createSplitCard("Booking", "Custom Session", scrollPane);
    }

    private JButton createStadiumCard(String name, String price, String imgPath) {
        JButton cardBtn = new JButton();
        cardBtn.setLayout(new BorderLayout());
        cardBtn.setPreferredSize(new Dimension(160, 180));
        cardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardBtn.setFocusPainted(false);
        cardBtn.setOpaque(true);
        cardBtn.setBackground(new Color(30, 35, 50));
        cardBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1, true));

        try {
            InputStream is = getClass().getResourceAsStream(imgPath);
            if (is != null) {
                Image img = ImageIO.read(is).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                cardBtn.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        cardBtn.setText("<html><center><b style='color:white; font-size:10px;'>" + name + "</b><br>" +
                       "<span style='color:#D67B7B; font-size:9px;'>" + price + " THB/hr</span></center></html>");
        cardBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        cardBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        cardBtn.addActionListener(e -> {
            if (lastSelectedCard != null) {
                lastSelectedCard.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1, true));
            }
            selectedStadiumName = name;
            
            // แก้ไขตรงนี้: บันทึกราคาของสนามที่กดเลือกไว้ในตัวแปร
            this.selectedPricePerHr = Double.parseDouble(price);

            cardBtn.setBorder(BorderFactory.createLineBorder(CORAL_PINK, 3, true));
            lastSelectedCard = cardBtn;
        });

        return cardBtn;
    }

    // --- 3. เมธอดสร้าง SplitCard รองรับ JComponent ---
    private JPanel createSplitCard(String title, String subTitle, JComponent formPanel) {
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
        leftPanel.setPreferredSize(new Dimension(350, 650)); 
        leftPanel.setOpaque(false);
        JLabel logo = new JLabel("STADIUM");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42)); logo.setForeground(Color.WHITE);
        leftPanel.add(logo);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false); 
        rightPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel head = new JLabel(title); head.setFont(new Font("Segoe UI", Font.BOLD, 30)); head.setAlignmentX(0.5f);
        JLabel sub = new JLabel(subTitle); sub.setForeground(Color.GRAY); sub.setAlignmentX(0.5f);
        
        rightPanel.add(head); rightPanel.add(sub);
        rightPanel.add(Box.createVerticalStrut(15)); 
        rightPanel.add(formPanel); 
        
        mainCard.add(leftPanel, BorderLayout.WEST);
        mainCard.add(rightPanel, BorderLayout.CENTER);
        return mainCard;
    }
 
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

                      "<p style='font-size: 11px;'>Scan to Pay Now</p></body></html>";

        JLabel summary = new JLabel(text);

        summary.setAlignmentX(Component.CENTER_ALIGNMENT);

        receipt.add(summary);
 
        URL qrUrl = getClass().getResource("/images/qr_payment.png");

        if (qrUrl != null) {

            ImageIcon qrIcon = new ImageIcon(new ImageIcon(qrUrl).getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH));

            JLabel qrLabel = new JLabel(qrIcon);

            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            receipt.add(qrLabel);

        }
 
        receipt.add(Box.createVerticalStrut(15)); 

        JButton payBtn = new JButton("I have already paid");

        styleCoralButton(payBtn);

        payBtn.addActionListener(e -> {

            container.add(officialTicketPage(b), "TICKET");

            cardLayout.show(container, "TICKET");

        });

        receipt.add(payBtn);
 
        container.add(createSplitCard("Payment", "Step 4: QR Scan", receipt), "RECEIPT");

        cardLayout.show(container, "RECEIPT");

    }
 
    // --- หน้าสุดท้าย: ใบเสร็จทางการสำหรับยืนยัน ---

    private JPanel officialTicketPage(Booking b) {

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setOpaque(false);
 
        String ticketText = "<html><div style='text-align: center; background-color: #ffffff; padding: 25px; border: 2px solid #2D3250;'>" +

                            "<h1 style='color: #2D3250; margin: 0;'>BOOKING TICKET</h1>" +

                            "<p style='color: #D67B7B;'>Payment Successful</p><hr>" +

                            "<table style='margin: auto;'>" +

                            "<tr><td align='left'><b>Customer:</b></td><td align='left'>" + UserStorage.getCurrentUser().getUsername() + "</td></tr>" +

                            "<tr><td align='left'><b>Field:</b></td><td align='left'>" + b.getField() + "</td></tr>" +

                            "<tr><td align='left'><b>Date:</b></td><td align='left'>" + b.getDate() + "</td></tr>" +

                            "<tr><td align='left'><b>Time:</b></td><td align='left' style='color: #D67B7B; font-size: 14px;'><b>" + b.getStartTime() + " - " + b.getEndTime() + "</b></td></tr>" +

                            "</table><br>" +

                            "<p style='font-size: 9px; color: gray;'>Ref ID: " + (int)(Math.random()*900000) + "</p>" +

                            "</div></html>";
 
        JLabel ticketLabel = new JLabel(ticketText);

        ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(ticketLabel);
 
        panel.add(Box.createVerticalStrut(30));

        JButton logoutBtn = new JButton("Finish & Logout");

        styleCoralButton(logoutBtn);

        logoutBtn.addActionListener(e -> cardLayout.show(container, "WELCOME"));

        panel.add(logoutBtn);
 
        return createSplitCard("Success!", "Show this ticket to stadium staff", panel);

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
    // 👇 วางตรงนี้เลย
private void styleTimeButton(JButton b) {
    b.setPreferredSize(new Dimension(85, 40));
    b.setBackground(new Color(60, 65, 90));
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true));
}

// ฟังก์ชันนี้จะช่วยเปลี่ยนสีปุ่มเวลาทั้งหมดกลับเป็นสีเดิม เพื่อเริ่มเลือกใหม่
    private void resetTimeSelection(JPanel panel) {
        startTime = ""; 
        endTime = "";
        for (Component c : panel.getComponents()) {
            if (c instanceof JButton) {
                // เปลี่ยนกลับเป็นสีพื้นฐานที่คุณใช้ใน styleTimeButton (ปกติคือสีน้ำเงินเข้ม)
                c.setBackground(new Color(60, 65, 90)); 
            }
        }
    }
}