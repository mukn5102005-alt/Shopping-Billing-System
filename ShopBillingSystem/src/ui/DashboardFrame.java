 package ui;



import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.awt.geom.*;



public class DashboardFrame extends JFrame {



    JButton btnProduct;

    JButton btnBilling;

    JButton btnSales;

    JButton btnExit;



    // ── Ultra-Modern Dashboard Premium Color Palette ──────────────────

    private static final Color BG_BASE      = new Color(15, 18, 36);

    private static final Color BG_PANEL     = new Color(22, 27, 49);

    private static final Color BG_HEADER    = new Color(11, 13, 26);



    private static final Color BTN_BLUE     = new Color(43, 79, 207);

    private static final Color BTN_BLUE_HOV = new Color(61, 99, 232);

    private static final Color BTN_BLUE_PRE = new Color(32, 62, 175);



    private static final Color BTN_RED      = new Color(139, 26, 42);

    private static final Color BTN_RED_HOV  = new Color(192, 36, 58);

    private static final Color BTN_RED_PRE  = new Color(108, 18, 30);



    private static final Color TEXT_WHITE   = new Color(243, 244, 246);

    private static final Color TEXT_MUTED   = new Color(148, 163, 184);

    private static final Color ACCENT_GREEN = new Color(52, 211, 153);

    private static final Color BORDER_DARK  = new Color(30, 37, 61);



    private static final Color BTN_BORDER_B = new Color(79, 102, 241);

    private static final Color BTN_BORDER_R = new Color(220, 38, 38);



    public DashboardFrame() {

        setTitle("Dashboard — Product Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);

        setMinimumSize(new Dimension(950, 680));

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());



        // ── Header Title Bar ──

        JPanel titleBar = new JPanel(new BorderLayout());

        titleBar.setBackground(BG_HEADER);

        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DARK));

        titleBar.setPreferredSize(new Dimension(0, 42));



        JLabel titleLabel = new JLabel("   Product Management System Console", JLabel.LEFT);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        titleLabel.setForeground(new Color(186, 198, 224));

        titleBar.add(titleLabel, BorderLayout.CENTER);



        // ── Animated Background Canvas ──

        JPanel content = new JPanel() {

            @Override

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



                // Gradient background

                GradientPaint gp = new GradientPaint(0, 0, BG_BASE, 0, getHeight(), BG_PANEL);

                g2.setPaint(gp);

                g2.fillRect(0, 0, getWidth(), getHeight());



                // Subtle geometric decorations

                g2.setStroke(new BasicStroke(1.5f));

                g2.setColor(new Color(99, 102, 241, 25));



                int w = getWidth();

                int h = getHeight();



                g2.draw(new RoundRectangle2D.Float(60, 100, 130, 130, 24, 24));

                g2.draw(new RoundRectangle2D.Float(100, 290, 90, 90, 16, 16));

                g2.drawLine(125, 230, 125, 290);

                g2.drawLine(190, 165, 270, 165);

                g2.drawOval(270, 160, 10, 10);



                g2.draw(new RoundRectangle2D.Float(w - 220, 140, 150, 110, 24, 24));

                g2.drawOval(w - 260, 330, 100, 100);

                g2.drawLine(w - 145, 250, w - 145, 330);

                g2.drawLine(w - 220, 195, w - 300, 195);



                // Fine grid

                g2.setColor(new Color(255, 255, 255, 4));

                for (int i = 0; i < w; i += 50) g2.drawLine(i, 0, i, h);

                for (int j = 0; j < h; j += 50) g2.drawLine(0, j, w, j);



                g2.dispose();

            }

        };

        content.setLayout(new GridBagLayout());

        content.setOpaque(false);



        // ── Central Glassmorphic Card ──

        JPanel card = new JPanel(new GridBagLayout()) {

            @Override

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(25, 30, 56, 235));

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));

                g2.setColor(new Color(255, 255, 255, 12));

                g2.setStroke(new BasicStroke(1.2f));

                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 24, 24));

                g2.dispose();

            }

        };

        card.setOpaque(false);

        card.setPreferredSize(new Dimension(500, 480));

        card.setBorder(BorderFactory.createEmptyBorder(25, 35, 35, 35));



        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;

        gbc.weightx = 1.0;



        // ── Logo / Brand Block ──

        JPanel logoBlock = new JPanel();

        logoBlock.setLayout(new BoxLayout(logoBlock, BoxLayout.Y_AXIS));

        logoBlock.setOpaque(false);



        JLabel iconLabel = new JLabel("🏢", JLabel.CENTER);

        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));

        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        JLabel nameLabel = new JLabel("Product Suite", JLabel.CENTER);

        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

        nameLabel.setForeground(TEXT_WHITE);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        JLabel subLabel = new JLabel("MANAGEMENT SYSTEM   v2.0", JLabel.CENTER);

        subLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));

        subLabel.setForeground(TEXT_MUTED);

        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        logoBlock.add(iconLabel);

        logoBlock.add(Box.createVerticalStrut(10));

        logoBlock.add(nameLabel);

        logoBlock.add(Box.createVerticalStrut(4));

        logoBlock.add(subLabel);



        gbc.gridy = 0;

        gbc.insets = new Insets(10, 0, 30, 0);

        card.add(logoBlock, gbc);



        // ── Navigation Buttons ──

        gbc.insets = new Insets(0, 0, 14, 0);



        btnProduct = createBtn("📦   Product Management", BTN_BLUE, BTN_BLUE_HOV, BTN_BLUE_PRE, BTN_BORDER_B);

        gbc.gridy = 1;

        card.add(btnProduct, gbc);



        btnBilling = createBtn("🧾   Generate Bill", BTN_BLUE, BTN_BLUE_HOV, BTN_BLUE_PRE, BTN_BORDER_B);

        gbc.gridy = 2;

        card.add(btnBilling, gbc);



        btnSales = createBtn("📊   Sales History", BTN_BLUE, BTN_BLUE_HOV, BTN_BLUE_PRE, BTN_BORDER_B);

        gbc.gridy = 3;

        card.add(btnSales, gbc);



        gbc.insets = new Insets(8, 0, 10, 0);

        btnExit = createBtn("🚪   Exit Application", BTN_RED, BTN_RED_HOV, BTN_RED_PRE, BTN_BORDER_R);

        gbc.gridy = 4;

        card.add(btnExit, gbc);



        content.add(card, new GridBagConstraints());



        // ── Button Action Listeners ──

        btnProduct.addActionListener(e -> openFrame(new ProductFrame()));

        btnBilling.addActionListener(e -> openFrame(new BillingFrame()));

        btnSales.addActionListener(e -> openFrame(new SalesFrame()));

        btnExit.addActionListener(e -> System.exit(0));



        // ── Status Bar ──

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));

        statusBar.setBackground(new Color(10, 12, 22));

        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_DARK));

        statusBar.setPreferredSize(new Dimension(0, 32));



        JLabel dot = new JLabel("●");

        dot.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        dot.setForeground(ACCENT_GREEN);



        JLabel statusTxt = new JLabel("System Operational Ready");

        statusTxt.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        statusTxt.setForeground(TEXT_MUTED);



        statusBar.add(dot);

        statusBar.add(statusTxt);



        add(titleBar,  BorderLayout.NORTH);

        add(content,   BorderLayout.CENTER);

        add(statusBar, BorderLayout.SOUTH);



        setLocationRelativeTo(null);

        setVisible(true);

    }



    private void openFrame(JFrame frame) {

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }



    private JButton createBtn(String text, Color base, Color hov, Color press, Color border) {

        JButton btn = new JButton(text) {

            @Override

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2.dispose();

                super.paintComponent(g);

            }



            @Override

            protected void paintBorder(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(border);

                g2.setStroke(new BasicStroke(1.2f));

                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth() - 1.2f, getHeight() - 1.2f, 12, 12));

                g2.dispose();

            }

        };



        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));

        btn.setForeground(TEXT_WHITE);

        btn.setBackground(base);

        btn.setOpaque(false);

        btn.setContentAreaFilled(false);

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(0, 50));

        btn.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));

        btn.setHorizontalAlignment(SwingConstants.LEFT);



        btn.addMouseListener(new MouseAdapter() {

            @Override public void mouseEntered(MouseEvent e)  { btn.setBackground(hov);   btn.repaint(); }

            @Override public void mouseExited(MouseEvent e)   { btn.setBackground(base);  btn.repaint(); }

            @Override public void mousePressed(MouseEvent e)  { btn.setBackground(press); btn.repaint(); }

            @Override public void mouseReleased(MouseEvent e) { btn.setBackground(hov);   btn.repaint(); }

        });



        return btn;

    }



    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception ignored) {}



        UIManager.put("swing.default-font", new Font("Segoe UI", Font.PLAIN, 14));

        System.setProperty("awt.useSystemAAFontSettings", "on");

        System.setProperty("swing.aatext", "true");



        SwingUtilities.invokeLater(DashboardFrame::new);

    }

} 