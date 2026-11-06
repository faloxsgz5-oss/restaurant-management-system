package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditOrders extends JPanel {
    private String tableId;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public EditOrders(String tableId, JPanel mainPanel, CardLayout cardLayout) {
        this.tableId = tableId;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Check Orders");
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setBounds(320, 20, 300, 40);
        title.setForeground(new Color(100, 150, 170));
        add(title);

        JLabel tableLabel = new JLabel(tableId, SwingConstants.CENTER);
        tableLabel.setBounds(620, 20, 80, 40);
        tableLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableLabel.setBackground(new Color(210, 235, 245));
        tableLabel.setOpaque(true);
        add(tableLabel);

        loadOrderDetails();
    }

    private void loadOrderDetails() {
        int y = 100;
        boolean hasOrder = false;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM chef_orders WHERE table_number = ? AND status = 'ออเดอร์' ORDER BY updated_at DESC")) {

            stmt.setString(1, tableId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hasOrder = true;
                int orderId = rs.getInt("id");
                String code = rs.getString("menu_code");
                String name = rs.getString("menu_name");
                int quantity = rs.getInt("quantity");
                int price = getMenuPrice(conn, code);
                int total = price * quantity;

                JLabel nameLabel = new JLabel(code + " " + name + " x" + quantity);
                nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
                nameLabel.setBounds(100, y, 400, 30);
                add(nameLabel);

                JLabel priceLabel = new JLabel(total + " บาท");
                priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
                priceLabel.setBounds(520, y, 120, 30);
                add(priceLabel);

                JButton editBtn = new JButton("Edit");
                editBtn.setBounds(660, y, 80, 30);
                editBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
                editBtn.setBackground(new Color(180, 250, 180));
                editBtn.addActionListener(e -> {
                    OrdersMenuSelection menuSelection = new OrdersMenuSelection(tableId, mainPanel, cardLayout);
                    mainPanel.add(menuSelection, "OrdersMenuSelection");
                    cardLayout.show(mainPanel, "OrdersMenuSelection");
                });
                add(editBtn);

                JButton deleteBtn = new JButton("Delete");
                deleteBtn.setBounds(750, y, 90, 30);
                deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
                deleteBtn.setBackground(new Color(255, 150, 150));
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "ต้องการลบออเดอร์นี้หรือไม่?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteOrder(orderId);
                    }
                });
                add(deleteBtn);

                y += 50;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "โหลดข้อมูลล้มเหลว");
        }

        if (!hasOrder) {
            JOptionPane.showMessageDialog(this, "ไม่พบออเดอร์ที่สามารถแก้ไขได้");
            backToCheckOrders();
        }
    }

    private void deleteOrder(int orderId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement delStmt = conn.prepareStatement("DELETE FROM chef_orders WHERE id = ?")) {

            delStmt.setInt(1, orderId);
            int rows = delStmt.executeUpdate();

            if (rows > 0) {
                boolean hasMoreOrders = checkRemainingOrders(conn);
                if (!hasMoreOrders) {
                    updateTableStatusToEmpty(conn);
                    JOptionPane.showMessageDialog(this, "ลบออเดอร์แล้ว โต๊ะไม่มีออเดอร์เหลือ");
                    backToCheckOrders();
                } else {
                    JOptionPane.showMessageDialog(this, "ลบออเดอร์สำเร็จ");
                    mainPanel.add(new EditOrders(tableId, mainPanel, cardLayout), "EditOrders");
                    cardLayout.show(mainPanel, "EditOrders");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkRemainingOrders(Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM chef_orders WHERE table_number = ? AND status = 'ออเดอร์'")) {
            stmt.setString(1, tableId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private int getMenuPrice(Connection conn, String code) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE code = ?")) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("price");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void updateTableStatusToEmpty(Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE tables SET status = 'โต๊ะว่าง' WHERE table_number = ?")) {
            stmt.setString(1, tableId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void backToCheckOrders() {
        mainPanel.add(new CheckOrders(mainPanel, cardLayout), "CheckOrders");
        cardLayout.show(mainPanel, "CheckOrders");
    }
}