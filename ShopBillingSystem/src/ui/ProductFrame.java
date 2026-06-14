 package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import db.DBConnection;

public class ProductFrame extends JFrame {

    // UI Components Declarations
    JTextField txtName, txtPrice, txtStock, txtSearch;
    JButton btnAdd, btnUpdate, btnDelete, btnSearch;
    JTable table;
    DefaultTableModel model;

    // ── ADVANCED CYBER EMERALD / DARK HIGH-CONTRAST PALETTE ──────────────────
    private final Color COLOR_BACKGROUND      = new Color(11, 15, 26);    // Midnight Deep Slate
    private final Color COLOR_CARD_BG         = new Color(20, 26, 46);    // Obsidian Card Panel
    private final Color COLOR_INPUT_BG        = new Color(15, 19, 36);    // Hollow Input Base
    private final Color COLOR_TEXT_LIGHT      = new Color(248, 250, 252); // Ultra White
    private final Color COLOR_TEXT_MUTED      = new Color(148, 163, 184); // Cool Steel Gray
    private final Color COLOR_INPUT_BORDER    = new Color(38, 48, 79);    // Subtle Frosted Border
    
    // --- Hyper Accent Glow Colors ---
    private final Color COLOR_EMERALD         = new Color(5, 206, 145);   // Electric Neo Mint
    private final Color COLOR_EMERALD_HOVER   = new Color(4, 161, 114);
    private final Color COLOR_AMBER           = new Color(245, 158, 11);  // Warning Cyber Amber
    private final Color COLOR_AMBER_HOVER     = new Color(217, 119, 6);
    private final Color COLOR_CRIMSON         = new Color(244, 63, 94);   // Hot Coral Crimson
    private final Color COLOR_CRIMSON_HOVER   = new Color(225, 29, 72);
    private final Color COLOR_PURPLE          = new Color(129, 140, 248); // Electric Violet
    private final Color COLOR_PURPLE_HOVER    = new Color(99, 102, 241);

    // Typography
    private final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 26);
    private final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_INPUT  = new Font("Segoe UI", Font.PLAIN, 14);

    public ProductFrame() {
        // --- Frame Core Configurations ---
        setTitle("Product Management Console Pro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_BACKGROUND);
        
        setLayout(new GridBagLayout());

        // ── MAIN CENTRAL CARD PANEL CONTAINER ──
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setPreferredSize(new Dimension(860, 580)); 
        mainContainer.setBackground(COLOR_CARD_BG);
        // Clean double-layered border simulation
        mainContainer.setBorder(BorderFactory.createLineBorder(new Color(30, 41, 59), 1));

        // --- Status Glow Indicator & Header Title ---
        JLabel lblDot = new JLabel("●");
        lblDot.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDot.setForeground(COLOR_EMERALD);
        lblDot.setBounds(40, 28, 20, 30);
        mainContainer.add(lblDot);

        JLabel lblTitle = new JLabel("Product Management");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_LIGHT);
        lblTitle.setBounds(65, 25, 400, 35);
        mainContainer.add(lblTitle);

        // --- Product Name Input ---
        JLabel lblName = new JLabel("PRODUCT NAME");
        styleLabel(lblName);
        lblName.setBounds(40, 85, 120, 30);
        mainContainer.add(lblName);

        txtName = new JTextField();
        styleTextField(txtName);
        txtName.setBounds(40, 115, 350, 38);
        mainContainer.add(txtName);

        // --- Price Input ---
        JLabel lblPrice = new JLabel("PRICE (₹)");
        styleLabel(lblPrice);
        lblPrice.setBounds(40, 165, 120, 30);
        mainContainer.add(lblPrice);

        txtPrice = new JTextField();
        styleTextField(txtPrice);
        txtPrice.setBounds(40, 195, 350, 38);
        mainContainer.add(txtPrice);

        // --- Stock Quantity Input ---
        JLabel lblStock = new JLabel("STOCK QUANTITY");
        styleLabel(lblStock);
        lblStock.setBounds(40, 245, 120, 30);
        mainContainer.add(lblStock);

        txtStock = new JTextField();
        styleTextField(txtStock);
        txtStock.setBounds(40, 275, 350, 38);
        mainContainer.add(txtStock);

        // --- Search Control Bar ---
        JLabel lblSearch = new JLabel("QUICK SEARCH");
        styleLabel(lblSearch);
        lblSearch.setBounds(40, 335, 120, 30);
        mainContainer.add(lblSearch);

        txtSearch = new JTextField();
        styleTextField(txtSearch);
        txtSearch.setBounds(40, 365, 230, 38);
        mainContainer.add(txtSearch);

        btnSearch = new JButton("Search");
        styleButton(btnSearch, COLOR_PURPLE, COLOR_PURPLE_HOVER);
        btnSearch.setBounds(280, 365, 110, 38);
        mainContainer.add(btnSearch);

        // --- Action Operational Buttons ---
        btnAdd = new JButton("Add Item");
        styleButton(btnAdd, COLOR_EMERALD, COLOR_EMERALD_HOVER);
        btnAdd.setBounds(40, 445, 110, 42);
        mainContainer.add(btnAdd);

        btnUpdate = new JButton("Modify");
        styleButton(btnUpdate, COLOR_AMBER, COLOR_AMBER_HOVER);
        btnUpdate.setBounds(160, 445, 110, 42);
        mainContainer.add(btnUpdate);

        btnDelete = new JButton("Remove");
        styleButton(btnDelete, COLOR_CRIMSON, COLOR_CRIMSON_HOVER);
        btnDelete.setBounds(280, 445, 110, 42);
        mainContainer.add(btnDelete);

        // --- Inventory Sub-Header ---
        JLabel lblSubDot = new JLabel("●");
        lblSubDot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubDot.setForeground(COLOR_EMERALD);
        lblSubDot.setBounds(440, 43, 15, 20);
        mainContainer.add(lblSubDot);

        JLabel lblInventory = new JLabel("LIVE INVENTORY STATUS");
        styleLabel(lblInventory);
        lblInventory.setForeground(COLOR_TEXT_MUTED);
        lblInventory.setBounds(460, 40, 200, 25);
        mainContainer.add(lblInventory);

        // --- Inventory Data JTable View ---
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Product Name");
        model.addColumn("Price (₹)");
        model.addColumn("Stock");

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(440, 85, 380, 440); 
        scrollPane.getViewport().setBackground(COLOR_INPUT_BG);
        scrollPane.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));
        mainContainer.add(scrollPane);

        // GridBag layout engine integration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(mainContainer, gbc);

        // ==========================================
        //         CRUD LOGIC INTERACTION HOOKS      
        // ==========================================

        loadProducts();

        // Add Listener
        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String stockStr = txtStock.getText().trim();

            if(name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kripya saare fields fill karein!");
                return;
            }
            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO Products(ProductName, Price, Stock) VALUES(?,?,?)");
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setInt(3, stock);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product successfully save ho gaya!");
                clearFields();
                loadProducts();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Data processing error: Numerical value check karein.");
                ex.printStackTrace();
            }
        });

        // Selection Event to Autofill Fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    txtName.setText(model.getValueAt(row, 0).toString());
                    String price = model.getValueAt(row, 1).toString().replace("₹", "").trim();
                    txtPrice.setText(price);
                    txtStock.setText(model.getValueAt(row, 2).toString());
                }
            }
        });

        // Update Listener
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Kripya table se pehle ek product select karein jise update karna hai!");
                return;
            }

            String oldName = model.getValueAt(row, 0).toString();
            String newName = txtName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String stockStr = txtStock.getText().trim();

            if (newName.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Koi bhi field khali nahi honi chahiye!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE Products SET ProductName=?, Price=?, Stock=? WHERE ProductName=?");
                ps.setString(1, newName);
                ps.setDouble(2, price);
                ps.setInt(3, stock);
                ps.setString(4, oldName);

                int updatedRows = ps.executeUpdate();
                if (updatedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Product successfully update ho gaya!");
                    clearFields();
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(this, "Product update nahi ho paya.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid number formats ya database issue.");
                ex.printStackTrace();
            }
        });

        // Delete Listener
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Kripya table se pehle ek product select karein jise delete karna hai!");
                return;
            }

            String productName = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Kya aap sach mein '" + productName + "' ko delete karna chahte hain?", 
                    "Delete Product", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM Products WHERE ProductName=?");
                    ps.setString(1, productName);

                    int deletedRows = ps.executeUpdate();
                    if (deletedRows > 0) {
                        JOptionPane.showMessageDialog(this, "Product successfully delete ho gaya!");
                        clearFields();
                        loadProducts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Product delete nahi ho paya.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Database error occurs!");
                    ex.printStackTrace();
                }
            }
        });

        // Search Listener
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadProducts();
                return;
            }
            try {
                model.setRowCount(0);
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT ProductName, Price, Stock FROM Products WHERE ProductName LIKE ?");
                ps.setString(1, "%" + keyword + "%");
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("ProductName"),
                        "₹" + String.format("%.2f", rs.getDouble("Price")),
                        rs.getInt("Stock")
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Window Configurations
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setMinimumSize(new Dimension(980, 700));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ==========================================
    //           CORE CONTROLLER METHODS         
    // ==========================================
    private void loadProducts() {
        try {
            model.setRowCount(0);
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT ProductName, Price, Stock FROM Products");
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ProductName"),
                    "₹" + String.format("%.2f", rs.getDouble("Price")),
                    rs.getInt("Stock")
                });
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        txtSearch.setText("");
    }

    // ==========================================
    //          UPGRADED ADVANCED UI HELPERS     
    // ==========================================
    private void styleTable(JTable table) {
        table.setFont(FONT_INPUT);
        table.setForeground(COLOR_TEXT_LIGHT);
        table.setBackground(COLOR_INPUT_BG);
        table.setRowHeight(38); // Marginally increased for better touch padding
        table.setGridColor(new Color(30, 41, 59));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(30, 58, 138)); // Deep futuristic blue highlight
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(15, 23, 42)); // Jet black premium header background
                setForeground(COLOR_EMERALD);         // text glows in neon mint
                setFont(FONT_LABEL);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        };
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setForeground(COLOR_TEXT_LIGHT);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT_MUTED);
    }

    private void styleTextField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setForeground(COLOR_TEXT_LIGHT);
        field.setBackground(COLOR_INPUT_BG);
        field.setCaretColor(COLOR_EMERALD); // Caret glows neon mint
        
        LineBorder normalBorder = new LineBorder(COLOR_INPUT_BORDER, 1);
        LineBorder focusBorder = new LineBorder(COLOR_EMERALD, 1); // Dynamic glow on click

        field.setBorder(BorderFactory.createCompoundBorder(
            normalBorder,
            BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));

        // Interactive Focus Transitions
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(focusBorder, BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                field.setBackground(new Color(22, 28, 54));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(normalBorder, BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                field.setBackground(COLOR_INPUT_BG);
            }
        });
    }

    private void styleButton(JButton button, Color normalColor, Color hoverColor) {
        button.setFont(FONT_LABEL);
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(normalColor.brighter(), 1));
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

    public static void main(String[] args) {
        // Modern OS theme sync override
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ProductFrame());
    }
}