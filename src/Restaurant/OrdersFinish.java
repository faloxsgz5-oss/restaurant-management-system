package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class OrdersFinish extends JPanel {
    private String tableId;
    private ArrayList<MenuItem> selectedItems;
    private Orders parentOrders;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextArea remarkArea;

    public OrdersFinish(String tableId, ArrayList<MenuItem> selectedItems, Orders parentOrders, JPanel mainPanel, CardLayout cardLayout) {
        this.tableId = tableId;
        this.selectedItems = selectedItems;
        this.parentOrders = parentOrders;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Orders");
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setBounds(30, 20, 300, 40);
        add(title);

        JLabel tableLabel = new JLabel("โต๊ะ " + tableId, SwingConstants.CENTER);
        tableLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        tableLabel.setOpaque(true);
        tableLabel.setBackground(new Color(210, 240, 250));
        tableLabel.setBounds(450, 20, 150, 40);
        add(tableLabel);

        int y = 100;
        int total = 0;
        for (MenuItem item : selectedItems) {
            String detail = item.getCode() + " " + item.getName() +
                    "  x" + item.getQuantity() +
                    "   = " + item.getTotal() + " บาท";
            JLabel itemLabel = new JLabel(detail);
            itemLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
            itemLabel.setBounds(100, y, 600, 30);
            add(itemLabel);

            total += item.getTotal();
            y += 40;
        }

        JLabel totalLabel = new JLabel("รวมทั้งหมด: " + total + " บาท");
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        totalLabel.setBounds(100, y + 10, 400, 40);
        add(totalLabel);

        JLabel remarkLabel = new JLabel("หมายเหตุ (ถ้ามี):");
        remarkLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        remarkLabel.setBounds(100, y + 60, 150, 30);
        add(remarkLabel);

        remarkArea = new JTextArea();
        remarkArea.setBounds(250, y + 60, 400, 60);
        remarkArea.setLineWrap(true);
        add(remarkArea);

        JButton finishBtn = new JButton("Finish");
        finishBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        finishBtn.setBackground(new Color(180, 250, 180));
        finishBtn.setBounds(450, y + 140, 200, 60);
        add(finishBtn);

        finishBtn.addActionListener(e -> {
            confirmOrder();
            cardLayout.show(mainPanel, "Orders");
        });

        JButton backBtn = new JButton("ย้อนกลับ");
        backBtn.setBounds(250, y + 140, 150, 60);
        backBtn.setBackground(new Color(255, 200, 200));
        backBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(backBtn);

        backBtn.addActionListener(e -> {
            OrdersMenuSelection menuPanel = new OrdersMenuSelection(tableId, parentOrders);
            mainPanel.add(menuPanel, "menu");
            cardLayout.show(mainPanel, "menu");
        });
    }

    public void confirmOrder() {
        if (selectedItems == null || selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกเมนูก่อนกด Finish");
            return;
        }

        String remark = remarkArea.getText().trim();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "")) {
            for (MenuItem item : selectedItems) {
                String sql = "INSERT INTO chef_orders (table_number, menu_code, menu_name, quantity, status, remark) VALUES (?, ?, ?, ?, 'ออเดอร์', ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, tableId);
                    stmt.setString(2, item.getCode());
                    stmt.setString(3, item.getName());
                    stmt.setInt(4, item.getQuantity());
                    stmt.setString(5, remark);
                    stmt.executeUpdate();
                }
            }

            String updateTable = "UPDATE tables SET status = 'โต๊ะไม่ว่าง', updated_at = NOW() WHERE table_number = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateTable)) {
                stmt.setString(1, tableId);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "ส่งออเดอร์สำเร็จ");

            // ลบและโหลดหน้าใหม่ทันที
            Component[] comps = mainPanel.getComponents();
            for (Component comp : comps) {
                if (comp instanceof Orders || comp instanceof CheckOrders || comp instanceof Payment) {
                    mainPanel.remove(comp);
                }
            }

            Orders newOrders = new Orders(mainPanel, cardLayout);
            CheckOrders newCheck = new CheckOrders(mainPanel, cardLayout);
            Payment newPayment = new Payment();

            mainPanel.add(newOrders, "Orders");
            mainPanel.add(newCheck, "Check Orders");
            mainPanel.add(newPayment, "Payment");

            cardLayout.show(mainPanel, "Orders");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }
}