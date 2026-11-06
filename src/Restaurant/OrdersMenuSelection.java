package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class OrdersMenuSelection extends JPanel {
    private JPanel menuPanel;
    private JTextField searchField;
    private Orders parentOrders;
    private String tableId;
    private ArrayList<MenuItem> selectedItems = new ArrayList<>();

    // === สำหรับเรียกจาก Orders.java ===
    public OrdersMenuSelection(String tableId, Orders parentOrders) {
        this.tableId = tableId;
        this.parentOrders = parentOrders;
        setupUI();
    }

    // === สำหรับเรียกจาก EditOrders.java (ไม่มีคลาส Orders) ===
    public OrdersMenuSelection(String tableId, JPanel mainPanel, CardLayout cardLayout) {
        this.tableId = tableId;
        this.parentOrders = new Orders(mainPanel, cardLayout); // mock object สำหรับทำงานร่วม
        setupUI();
    }

    // === UI เดิมทั้งหมด แยกออกมา ===
    private void setupUI() {
        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("เลือกเมนูสำหรับโต๊ะ " + tableId);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(30, 20, 500, 40);
        add(title);

        JButton backBtn = new JButton("ย้อนกลับ");
        backBtn.setBounds(720, 25, 100, 30);
        backBtn.setBackground(new Color(255, 200, 200));
        backBtn.addActionListener(e -> parentOrders.showTables());
        add(backBtn);

        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(new Color(210, 235, 245));
        searchPanel.setBounds(30, 80, 880, 50);
        add(searchPanel);

        searchField = new JTextField();
        searchField.setBounds(10, 10, 730, 30);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("ค้นหา");
        searchBtn.setBounds(750, 10, 100, 30);
        searchBtn.setBackground(new Color(220, 245, 255));
        searchPanel.add(searchBtn);

        menuPanel = new JPanel(null);
        menuPanel.setBackground(new Color(230, 250, 255));
        menuPanel.setPreferredSize(new Dimension(860, 1000)); // ให้ความสูงพอเลื่อน

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBounds(30, 150, 880, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(230, 250, 255));
        scrollPane.setBorder(null);

        add(scrollPane);

        JButton finishBtn = new JButton("Finish");
        finishBtn.setBounds(400, 620, 180, 40);
        finishBtn.setBackground(new Color(180, 255, 180));
        finishBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        finishBtn.setFocusPainted(false);
        finishBtn.addActionListener(e -> confirmMenu());
        add(finishBtn);
        
        

        searchBtn.addActionListener(e -> selectMenu());
        selectMenu();
    }

    public void selectMenu() {
        loadMenuFromDatabase(searchField.getText().trim());
    }

    public void confirmMenu() {
    	OrdersFinish finishPanel = new OrdersFinish(tableId, selectedItems, parentOrders, parentOrders.getMainPanel(), parentOrders.getCardLayout());        parentOrders.getMainPanel().add(finishPanel, "finish");
        parentOrders.getCardLayout().show(parentOrders.getMainPanel(), "finish");
    }

    private void loadMenuFromDatabase(String keyword) {
        menuPanel.removeAll();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT code, name, price FROM menu WHERE code LIKE ? OR name LIKE ?")) {

            String keyPattern = "%" + keyword + "%";
            stmt.setString(1, keyPattern);
            stmt.setString(2, keyPattern);

            ResultSet rs = stmt.executeQuery();
            int x = 0, y = 0;

            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                int price = rs.getInt("price");

                JButton btn = new JButton(code + " " + name);
                btn.setBounds(x, y, 250, 60);
                btn.setBackground(new Color(240, 230, 255));
                btn.setFocusPainted(false);

                btn.addActionListener(e -> openQuantityDialog(code, name, price, btn));
                menuPanel.add(btn);

                x += 290;
                if (x > 800) {
                    x = 0;
                    y += 80;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "โหลดเมนูไม่สำเร็จ");
        }

        menuPanel.repaint();
        menuPanel.revalidate();
    }

    private void openQuantityDialog(String code, String name, int price, JButton sourceBtn) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "เลือกจำนวน", true);
        dialog.setSize(300, 200);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(this);

        JLabel infoLabel = new JLabel("เมนู: " + code + " " + name);
        infoLabel.setBounds(30, 20, 240, 30);
        dialog.add(infoLabel);

        JLabel qtyLabel = new JLabel("จำนวน:");
        qtyLabel.setBounds(30, 60, 100, 30);
        dialog.add(qtyLabel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        quantitySpinner.setBounds(100, 60, 60, 30);
        dialog.add(quantitySpinner);

        JButton addBtn = new JButton("เพิ่มเมนู");
        addBtn.setBounds(80, 110, 120, 30);
        addBtn.setBackground(new Color(200, 255, 200));
        dialog.add(addBtn);

        addBtn.addActionListener(e -> {
            int qty = (int) quantitySpinner.getValue();
            selectedItems.add(new MenuItem(code, name, price, qty));
            sourceBtn.setEnabled(false);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}