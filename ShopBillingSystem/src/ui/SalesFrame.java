package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import db.DBConnection;

public class SalesFrame extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton btnRefresh;

    // ── Main Dashboard Panel Se Exactly Matched Color Palette ──────────────────
    private final Color COLOR_BACKGROUND      = new Color(15, 18, 36);    // Deep Tech Slate Space
    private final Color COLOR_CARD_BG         = new Color(25, 30, 56);    // Center Dashboard Card Container
    private final Color COLOR_TEXT_LIGHT      = new Color(243, 244, 246); // Crisp off-white text
    private final Color COLOR_TEXT_MUTED      = new Color(148, 163, 184); // Cool slate gray for subtitles
    private final Color COLOR_INPUT_BORDER    = new Color(51, 65, 85);    // Premium dark border Grid
    private final Color COLOR_HEADER_BG       = new Color(30, 41, 59);    // Solid Slate Header Panel
    
    // --- Accent & Action Colors ---
    private final Color COLOR_PRIMARY         = new Color(99, 102, 241);  // Electric Indigo Neon
    private final Color COLOR_PRIMARY_HOVER   = new Color(79, 70, 229);
    private final Color COLOR_MINT_HIGHLIGHT  = new Color(52, 211, 153);  // Neon Digital Green
    private final Color COLOR_ID_HIGHLIGHT    = new Color(129, 140, 248); // Bright Lavender/Indigo for Sale IDs

    // Modern Typography
    private final Font FONT_TITLE        = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_SUBTITLE     = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_LABEL        = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_TABLE_CELL   = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_TABLE_ID     = new Font("Segoe UI", Font.BOLD, 13);

    public SalesFrame() {
        // --- Window Layout Configurations ---
        setTitle("Enterprise Sales Transaction Ledger");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_BACKGROUND);
        
        // Dynamic center configuration tool
        setLayout(new GridBagLayout());

        // ── CENTRAL CARD PANEL CONTAINER (Exactly Center Base) ──
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setPreferredSize(new Dimension(840, 560)); // Same box bounds as billing system
        mainContainer.setBackground(COLOR_CARD_BG);
        mainContainer.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));

        // --- Header Title ---
        JLabel lblTitle = new JLabel("Sales Ledger Dashboard");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_LIGHT);
        lblTitle.setBounds(40, 25, 350, 35);
        mainContainer.add(lblTitle);

        // --- Subtitle Description ---
        JLabel lblSub = new JLabel("Real-time database transaction history and invoice logging.");
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(COLOR_TEXT_MUTED);
        lblSub.setBounds(40, 58, 450, 20);
        mainContainer.add(lblSub);

        // --- Action Buttons ---
        btnRefresh = new JButton("Refresh Data");
        styleButton(btnRefresh, COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnRefresh.setBounds(640, 35, 160, 38);
        mainContainer.add(btnRefresh);

        // --- Modern Custom Table Setup ---
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        model.addColumn("Sale ID");
        model.addColumn("Product Name");
        model.addColumn("Quantity");
        model.addColumn("Total Amount");
        model.addColumn("Transaction Date");

        table = new JTable(model);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(40, 110, 760, 410);
        sp.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));
        sp.getViewport().setBackground(new Color(19, 24, 46)); // Matching table baseline drop shadow
        mainContainer.add(sp);

        // ── GRIDBAG PANEL BINDING (Symmetric Alignment Engine) ──
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(mainContainer, gbc);

        // --- Listeners ---
        btnRefresh.addActionListener(e -> {
            loadSales();
            JOptionPane.showMessageDialog(this, "Sales records synchronized successfully!");
        });

        // Initialize and trigger data load
        loadSales();

        // Screen configuration parameters
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setMinimumSize(new Dimension(950, 680));
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    public void loadSales() {
        try {
            model.setRowCount(0); // Clear old records

            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Sales");

            while(rs.next()) {
                model.addRow(new Object[]{
                    "#" + rs.getInt("SaleId"), 
                    rs.getString("ProductName"),
                    rs.getInt("Quantity") + " pcs", 
                    "₹" + String.format("%.2f", rs.getDouble("TotalAmount")), 
                    rs.getTimestamp("SaleDate")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load database records.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    //          REUSABLE STYLING HELPERS          
    // ==========================================
    private void styleTable(JTable table) {
        table.setFont(FONT_TABLE_CELL);
        table.setForeground(COLOR_TEXT_LIGHT);
        table.setBackground(new Color(19, 24, 46));
        table.setRowHeight(36); 
        table.setGridColor(COLOR_INPUT_BORDER);
        table.setShowVerticalLines(false); 
        table.setSelectionBackground(new Color(51, 65, 85)); 
        table.setSelectionForeground(Color.WHITE);

        // Fixed Table Header Layout Logic
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(COLOR_HEADER_BG);
                setForeground(COLOR_TEXT_LIGHT); 
                setFont(FONT_TABLE_HEADER);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_INPUT_BORDER));
                return this;
            }
        };
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);

        // ── CUSTOM CELL ACCENT SYSTEM ──
        
        // Column 0: Sale ID Bold Indigo Accent
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(FONT_TABLE_ID);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) c.setForeground(COLOR_ID_HIGHLIGHT);
                return c;
            }
        });

        // Column 1: Product Name Content
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) c.setForeground(COLOR_TEXT_LIGHT);
                return c;
            }
        });

        // Column 2: Quantity Center Render
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setForeground(COLOR_TEXT_LIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Column 3: Total Amount Monetary Neon Mint
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(FONT_TABLE_ID);
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (!isSelected) c.setForeground(COLOR_MINT_HIGHLIGHT);
                return c;
            }
        });

        // Column 4: Transaction Date
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private void styleButton(JButton button, Color normalColor, Color hoverColor) {
        button.setFont(FONT_LABEL);
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }
}