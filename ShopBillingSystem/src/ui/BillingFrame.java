 package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import db.DBConnection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BillingFrame extends JFrame {

    // UI Components Declarations
    JComboBox<String> cmbProduct;
    JTextField txtQty;
    JButton btnAddCart, btnGenerateBill, btnSaveBill;
    JTextArea txtBill; 

    JTable table;
    DefaultTableModel model;

    JLabel lblSubtotal, lblGST, lblGrandTotal;
    double subtotal = 0;

    // ── Main Dashboard Panel Se Exactly Matched Color Palette ──────────────────
    private final Color COLOR_BACKGROUND      = new Color(15, 18, 36);    // Deep Tech Slate Space
    private final Color COLOR_CARD_BG         = new Color(25, 30, 56);    // Dashboard Product Suite Card Box
    private final Color COLOR_TEXT_LIGHT      = new Color(243, 244, 246); // Crisp off-white text
    private final Color COLOR_TEXT_MUTED      = new Color(148, 163, 184); // Cool slate gray
    private final Color COLOR_INPUT_BORDER    = new Color(51, 65, 85);    // Premium dark border
    private final Color COLOR_HEADER_BG       = new Color(30, 41, 59);    // Solid Slate Header Panel
    
    // --- Button Accents ---
    private final Color COLOR_PRIMARY         = new Color(99, 102, 241);  // Electric Indigo Neon
    private final Color COLOR_PRIMARY_HOVER   = new Color(79, 70, 229);
    private final Color COLOR_SUCCESS         = new Color(16, 185, 129);  // Vibrant Mint Emerald
    private final Color COLOR_SUCCESS_HOVER   = new Color(5, 150, 105);
    private final Color COLOR_SECONDARY       = new Color(100, 116, 139); // Modern Muted Platinum
    private final Color COLOR_SECONDARY_HOVER = new Color(71, 85, 105);

    // Modern Typography
    private final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_INPUT  = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_TOTALS = new Font("Segoe UI", Font.PLAIN, 15);

    public BillingFrame() {

        // --- Window Standard Configurations ---
        setTitle("Enterprise Billing & Checkout Console");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_BACKGROUND);
        
        // Pure window ko seamless main menu dashboard jaisa look dene ke liye layout manager use kiya hai
        setLayout(new GridBagLayout()); 

        // ── MAIN CENTRAL PANEL CONTAINER DEPLOYMENT (Exactly Center Base) ──
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(null);
        mainContainer.setPreferredSize(new Dimension(840, 560)); // Premium standard bounding constraints
        mainContainer.setBackground(COLOR_CARD_BG);
        mainContainer.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));

        // --- Container Header Title ---
        JLabel lblTitle = new JLabel("Billing & Checkout Console");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_LIGHT);
        lblTitle.setBounds(40, 25, 400, 35);
        mainContainer.add(lblTitle);

        // --- Product Dropdown ---
        JLabel lblProduct = new JLabel("SELECT PRODUCT");
        styleLabel(lblProduct);
        lblProduct.setBounds(40, 85, 120, 30);
        mainContainer.add(lblProduct);

        cmbProduct = new JComboBox<>();
        cmbProduct.addItem("Pen");
        cmbProduct.addItem("Book");
        cmbProduct.addItem("Mouse");
        styleComboBox(cmbProduct);
        cmbProduct.setBounds(160, 85, 190, 32);
        mainContainer.add(cmbProduct);

        // --- Quantity Field ---
        JLabel lblQty = new JLabel("QUANTITY");
        styleLabel(lblQty);
        lblQty.setBounds(40, 135, 120, 30);
        mainContainer.add(lblQty);

        txtQty = new JTextField();
        styleTextField(txtQty);
        txtQty.setBounds(160, 135, 190, 32);
        mainContainer.add(txtQty);

        // --- Add To Cart Button ---
        btnAddCart = new JButton("Add To Cart");
        styleButton(btnAddCart, COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnAddCart.setBounds(160, 185, 190, 40);
        mainContainer.add(btnAddCart);

        // --- JTable Setup ---
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Product");
        model.addColumn("Price");
        model.addColumn("Qty");
        model.addColumn("Total");

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 250, 490, 270);
        scrollPane.getViewport().setBackground(new Color(19, 24, 46)); // Rich inner contrasting table container
        scrollPane.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));
        mainContainer.add(scrollPane);

        // --- Live Receipt Terminal Preview (JTextArea) ---
        txtBill = new JTextArea();
        txtBill.setEditable(false);
        txtBill.setBackground(new Color(10, 13, 28)); 
        txtBill.setForeground(new Color(52, 211, 153)); 
        txtBill.setFont(new Font("Consolas", Font.PLAIN, 12)); 
        txtBill.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane billScroll = new JScrollPane(txtBill);
        billScroll.setBounds(560, 85, 240, 200); 
        billScroll.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));
        mainContainer.add(billScroll);

        // --- Live Calculation Labels ---
        lblSubtotal = new JLabel("Subtotal : ₹0.00");
        lblSubtotal.setFont(FONT_TOTALS);
        lblSubtotal.setForeground(COLOR_TEXT_MUTED);
        lblSubtotal.setBounds(560, 300, 240, 25);
        mainContainer.add(lblSubtotal);

        lblGST = new JLabel("GST (18%) : ₹0.00");
        lblGST.setFont(FONT_TOTALS);
        lblGST.setForeground(COLOR_TEXT_MUTED);
        lblGST.setBounds(560, 325, 240, 25);
        mainContainer.add(lblGST);

        JSeparator sep = new JSeparator();
        sep.setBounds(560, 360, 240, 2);
        sep.setBackground(COLOR_INPUT_BORDER);
        sep.setForeground(COLOR_INPUT_BORDER);
        mainContainer.add(sep);

        lblGrandTotal = new JLabel("Grand Total : ₹0.00");
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGrandTotal.setForeground(new Color(129, 140, 248)); 
        lblGrandTotal.setBounds(560, 375, 240, 30);
        mainContainer.add(lblGrandTotal);

        // --- Billing Action Buttons ---
        btnGenerateBill = new JButton("Generate Bill");
        styleButton(btnGenerateBill, COLOR_SUCCESS, COLOR_SUCCESS_HOVER);
        btnGenerateBill.setBounds(560, 425, 240, 42); 
        mainContainer.add(btnGenerateBill);

        btnSaveBill = new JButton("Save Bill");
        styleButton(btnSaveBill, COLOR_SECONDARY, COLOR_SECONDARY_HOVER);
        btnSaveBill.setBounds(560, 478, 240, 42); 
        mainContainer.add(btnSaveBill);

        // ── GRIDBAG PANEL BINDING (Force Matrix To Center Screen) ──
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Perfectly centers the card container horizontally and vertically
        add(mainContainer, gbc);

        // ==========================================
        //             LOGIC & LISTENERS             
        // ==========================================

        btnAddCart.addActionListener(e -> {
            try {
                String product = cmbProduct.getSelectedItem().toString();
                int qty = Integer.parseInt(txtQty.getText().trim());

                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity zero se zyada honi chahiye!");
                    return;
                }

                double price = 0;
                if(product.equals("Pen")) price = 10;
                else if(product.equals("Book")) price = 50;
                else if(product.equals("Mouse")) price = 500;

                double total = price * qty;
                model.addRow(new Object[]{ product, "₹" + String.format("%.2f", price), qty, "₹" + String.format("%.2f", total) });

                subtotal += total;
                double gst = subtotal * 0.18;
                double grandTotal = subtotal + gst;

                lblSubtotal.setText(String.format("Subtotal : ₹%.2f", subtotal));
                lblGST.setText(String.format("GST (18%%) : ₹%.2f", gst));
                lblGrandTotal.setText(String.format("Grand Total : ₹%.2f", grandTotal));

                txtQty.setText("");

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Kripya sahi numerical quantity enter karein!");
            }
        });

        btnGenerateBill.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Cart khali hai! Pehle products add karein.");
                return;
            }

            StringBuilder bill = new StringBuilder();
            bill.append("==============================\n");
            bill.append("     SHOP BILLING SYSTEM      \n");
            bill.append("==============================\n\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                bill.append(String.format("%s\n  %s x %s\n\n",
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2)
                ));
            }

            double gst = subtotal * 0.18;
            double grandTotal = subtotal + gst;

            bill.append("------------------------------\n");
            bill.append(String.format("Subtotal    : ₹%.2f\n", subtotal));
            bill.append(String.format("GST (18%%)   : ₹%.2f\n", gst));
            bill.append(String.format("Grand Total : ₹%.2f\n", grandTotal));
            bill.append("------------------------------\n");
            bill.append("   Thank You For Shopping!   \n");

            txtBill.setText(bill.toString());
            
            try {
                Connection con = DBConnection.getConnection();
                for(int i = 0; i < model.getRowCount(); i++) {
                    String productName = model.getValueAt(i, 0).toString();
                    int quantity = Integer.parseInt(model.getValueAt(i, 2).toString());
                    String totalStr = model.getValueAt(i, 3).toString().replace("₹", "");
                    double totalAmount = Double.parseDouble(totalStr);

                    String sql = "INSERT INTO Sales(ProductName, Quantity, TotalAmount) VALUES(?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, productName);
                    ps.setInt(2, quantity);
                    ps.setDouble(3, totalAmount);
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Sales Saved Successfully");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        btnSaveBill.addActionListener(e -> {
            String billText = txtBill.getText().trim();
            if (billText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pehle 'Generate Bill' par click karein!");
                return;
            }
            try {
                FileWriter writer = new FileWriter("bill.txt");
                writer.write(billText);
                writer.close();
                JOptionPane.showMessageDialog(this, "Bill successfully 'bill.txt' mein save ho gaya hai!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "File save karne mein koi dikkat aayi!");
                ex.printStackTrace();
            }
        });

        // Window Launch System Core Upgrades
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Automatically launches app in seamless full screen dashboard
        setMinimumSize(new Dimension(950, 680));
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    // ==========================================
    //          REUSABLE STYLING HELPERS          
    // ==========================================
    private void styleTable(JTable table) {
        table.setFont(FONT_INPUT);
        table.setForeground(COLOR_TEXT_LIGHT);
        table.setBackground(new Color(19, 24, 46));
        table.setRowHeight(34);
        table.setGridColor(COLOR_INPUT_BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(51, 65, 85));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(COLOR_HEADER_BG);
                setForeground(COLOR_TEXT_LIGHT); 
                setFont(FONT_LABEL);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_INPUT_BORDER));
                return this;
            }
        };
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                c.setForeground(COLOR_TEXT_LIGHT);
                return c;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerCellRenderer);
        }
    }

    private void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT_MUTED);
    }

    private void styleTextField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setForeground(COLOR_TEXT_LIGHT);
        field.setBackground(new Color(15, 18, 36));
        field.setCaretColor(COLOR_TEXT_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_INPUT_BORDER, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(FONT_INPUT);
        comboBox.setForeground(COLOR_TEXT_LIGHT);
        comboBox.setBackground(new Color(15, 18, 36));
        comboBox.setBorder(new LineBorder(COLOR_INPUT_BORDER, 1));
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