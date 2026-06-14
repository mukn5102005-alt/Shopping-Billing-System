 package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginFrame extends JFrame {

    JLabel lblTitle, lblSubtitle, lblUser, lblPass;
    JTextField txtUser;
    JPasswordField txtPass;
    JButton btnLogin;

    // ── Deep space aurora palette ──────────────────────────────────────────────
    private static final Color BG_BASE      = new Color(6,   7,  20);
    private static final Color BG_MID       = new Color(10,  14,  38);
    private static final Color ORBS_A       = new Color(72,  52, 212, 90);   // violet
    private static final Color ORBS_B       = new Color(14, 165, 233, 70);   // sky
    private static final Color ORBS_C       = new Color(168,  85, 247, 60);  // purple
    private static final Color GLASS_BG     = new Color(15,  20,  50, 175);
    private static final Color GLASS_BORDER = new Color(255, 255, 255, 28);
    private static final Color TEXT_PRIMARY  = new Color(241, 245, 249);
    private static final Color TEXT_MUTED    = new Color(148, 163, 184);
    private static final Color ACCENT_START  = new Color(99, 102, 241);
    private static final Color ACCENT_END    = new Color(139,  92, 246);
    private static final Color FIELD_BG     = new Color(255, 255, 255, 15);
    private static final Color FIELD_BORDER = new Color(255, 255, 255, 35);

    // ── Animated orb state ────────────────────────────────────────────────────
    private final List<AnimOrb> orbs = new ArrayList<>();
    private Timer animTimer;
    private BufferedImage noiseTex;

    public LoginFrame() {
        setTitle("Login — Aurora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        generateNoise();
        createOrbs();

        // ── Animated background panel ──────────────────────────────────────────
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(true);

        // ── Glass card panel ───────────────────────────────────────────────────
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), arc = 24;

                // glass fill
                g2.setColor(GLASS_BG);
                g2.fillRoundRect(0, 0, w, h, arc, arc);

                // top shine
                GradientPaint shine = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 18),
                        0, h / 3f, new Color(255, 255, 255, 0));
                g2.setPaint(shine);
                g2.fillRoundRect(0, 0, w, h, arc, arc);

                // border
                g2.setColor(GLASS_BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(44, 48, 44, 48));
        card.setPreferredSize(new Dimension(420, 460));

        // ── Title & subtitle ───────────────────────────────────────────────────
        lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(loadFont("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TEXT_PRIMARY);

        lblSubtitle = new JLabel("Sign in to continue");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(TEXT_MUTED);

        // ── Fields ─────────────────────────────────────────────────────────────
        lblUser = makeLabel("Username");
        lblPass = makeLabel("Password");

        txtUser = makeTextField();
        txtPass = makePasswordField();

        // ── Gradient Login Button ──────────────────────────────────────────────
        btnLogin = new JButton("Sign In") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                Color s = hovered ? ACCENT_END   : ACCENT_START;
                Color e = hovered ? ACCENT_START : ACCENT_END;
                g2.setPaint(new GradientPaint(0, 0, s, w, 0, e));
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                g2.setFont(getFont());
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(getText())) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(320, 46));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);

        // ── Layout card ────────────────────────────────────────────────────────
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        c.insets = new Insets(0, 0, 4, 0);
        card.add(lblTitle, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 28, 0);
        card.add(lblSubtitle, c);

        c.gridy = 2; c.gridwidth = 1;
        c.insets = new Insets(0, 0, 6, 0);
        card.add(lblUser, c);

        c.gridy = 3;
        c.insets = new Insets(0, 0, 18, 0);
        card.add(txtUser, c);

        c.gridy = 4;
        c.insets = new Insets(0, 0, 6, 0);
        card.add(lblPass, c);

        c.gridy = 5;
        c.insets = new Insets(0, 0, 32, 0);
        card.add(txtPass, c);

        c.gridy = 6;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(btnLogin, c);

        mainPanel.add(card);
        add(mainPanel);

        // ── Start animation ────────────────────────────────────────────────────
        animTimer = new Timer(16, e -> {
            orbs.forEach(AnimOrb::update);
            mainPanel.repaint();
        });
        animTimer.start();

        // ── Login action ───────────────────────────────────────────────────────
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());
            if (username.equals("admin") && password.equals("123")) {
                JOptionPane.showMessageDialog(this, "✓  Login Successful", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new DashboardFrame();
            } else {
                shake(txtUser);
                shake(txtPass);
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        txtPass.addActionListener(e -> btnLogin.doClick());

        setVisible(true);
    }

    // ── Background painting ───────────────────────────────────────────────────
    private void drawBackground(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,   RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // base gradient
        g2.setPaint(new GradientPaint(0, 0, BG_BASE, w, h, BG_MID));
        g2.fillRect(0, 0, w, h);

        // animated orbs (soft radial blobs)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        for (AnimOrb orb : orbs) {
            RadialGradientPaint rp = new RadialGradientPaint(
                    new Point2D.Float(orb.x, orb.y),
                    orb.radius,
                    new float[]{0f, 1f},
                    new Color[]{orb.color, new Color(0, 0, 0, 0)});
            g2.setPaint(rp);
            g2.fillOval((int)(orb.x - orb.radius), (int)(orb.y - orb.radius),
                        (int)(orb.radius * 2),       (int)(orb.radius * 2));
        }

        // noise overlay for film-grain texture
        if (noiseTex != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.045f));
            for (int tx = 0; tx < w; tx += noiseTex.getWidth())
                for (int ty = 0; ty < h; ty += noiseTex.getHeight())
                    g2.drawImage(noiseTex, tx, ty, null);
        }

        // subtle horizontal scanlines
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.025f));
        g2.setColor(Color.BLACK);
        for (int y = 0; y < h; y += 3)
            g2.drawLine(0, y, w, y);

        g2.dispose();
    }

    // ── Noise texture ─────────────────────────────────────────────────────────
    private void generateNoise() {
        int size = 128;
        noiseTex = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Random rnd = new Random(42);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int v = rnd.nextInt(256);
                noiseTex.setRGB(x, y, new Color(v, v, v, 255).getRGB());
            }
        }
    }

    // ── Orbs ──────────────────────────────────────────────────────────────────
    private void createOrbs() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int sw = screen.width, sh = screen.height;
        orbs.add(new AnimOrb(sw * 0.2f, sh * 0.25f, sw * 0.35f, ORBS_A,  0.18f, 0.10f));
        orbs.add(new AnimOrb(sw * 0.75f, sh * 0.6f, sw * 0.30f, ORBS_B,  0.12f, -0.15f));
        orbs.add(new AnimOrb(sw * 0.5f,  sh * 0.8f, sw * 0.28f, ORBS_C, -0.14f, 0.08f));
        orbs.add(new AnimOrb(sw * 0.85f, sh * 0.15f, sw * 0.22f, ORBS_A, 0.09f, 0.13f));
    }

    private static class AnimOrb {
        float x, y, radius, vx, vy;
        Color color;
        float ox, oy;
        AnimOrb(float x, float y, float r, Color c, float vx, float vy) {
            this.x = x; this.y = y; this.radius = r;
            this.color = c; this.vx = vx; this.vy = vy;
            this.ox = x; this.oy = y;
        }
        void update() {
            x += vx; y += vy;
            Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
            if (x < -radius || x > s.width  + radius) vx = -vx;
            if (y < -radius || y > s.height + radius) vy = -vy;
        }
    }

    // ── Shake animation ───────────────────────────────────────────────────────
    private void shake(Component comp) {
        Point origin = comp.getLocation();
        Timer t = new Timer(30, null);
        int[] steps = {-8, 8, -6, 6, -3, 3, 0};
        int[] idx = {0};
        t.addActionListener(e -> {
            if (idx[0] < steps.length) {
                comp.setLocation(origin.x + steps[idx[0]], origin.y);
                idx[0]++;
            } else {
                comp.setLocation(origin);
                t.stop();
            }
        });
        t.start();
    }

    // ── Helper builders ───────────────────────────────────────────────────────
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    private JTextField makeTextField() {
        JTextField f = new JTextField(20) {
            @Override protected void paintComponent(Graphics g) {
                paintGlassField(g, this); super.paintComponent(g);
            }
        };
        styleField(f);
        return f;
    }

    private JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField(20) {
            @Override protected void paintComponent(Graphics g) {
                paintGlassField(g, this); super.paintComponent(g);
            }
        };
        styleField(f);
        return f;
    }

    private void styleField(JTextComponent f) {
        f.setOpaque(false);
        f.setBackground(new Color(0, 0, 0, 0));
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        f.setPreferredSize(new Dimension(320, 48));
    }

    private void paintGlassField(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(FIELD_BG);
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
        g2.setColor(FIELD_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
        g2.dispose();
    }

    private Font loadFont(String name, int style, int size) {
        return new Font(name, style, size);
    }

    public static void main(String[] args) {
        // Enable GPU acceleration
        System.setProperty("sun.java2d.opengl", "true");
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.FALSE);
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}