package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Payment extends JPanel {
    private HashMap<String, JButton> tableButtons = new HashMap<>();

    public Payment() {
        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Payment");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBounds(30, 20, 300, 40);
        add(title);

        Color colorEmpty = new Color(240, 230, 255);
        Color colorOccupied = new Color(255, 150, 150);
        Color colorPaid = new Color(180, 255, 180);

        JLabel statusBusy = new JLabel("โต๊ะไม่ว่าง", SwingConstants.CENTER);
        statusBusy.setOpaque(true);
        statusBusy.setBackground(colorOccupied);
        statusBusy.setBounds(250, 30, 100, 30);
        add(statusBusy);

        JLabel statusPaid = new JLabel("ชำระเงินแล้ว", SwingConstants.CENTER);
        statusPaid.setOpaque(true);
        statusPaid.setBackground(colorPaid);
        statusPaid.setBounds(370, 30, 100, 30);
        add(statusPaid);

        JLabel statusEmpty = new JLabel("โต๊ะว่าง", SwingConstants.CENTER);
        statusEmpty.setOpaque(true);
        statusEmpty.setBackground(colorEmpty);
        statusEmpty.setBounds(490, 30, 100, 30);
        add(statusEmpty);

        String[][] tableIds = {
                {"A1", "B1", "C1"},
                {"A2", "B2", "C2"},
                {"A3", "B3", "C3"},
                {"A4", "B4", "C4"}
        };

        int x = 50, y = 80;
        for (String[] row : tableIds) {
            for (String id : row) {
                JButton tableBtn = new JButton(id);
                tableBtn.setBounds(x, y, 200, 60);
                tableBtn.setFocusPainted(false);
                tableButtons.put(id, tableBtn);
                add(tableBtn);
                x += 250;
            }
            x = 50;
            y += 80;
        }

        refreshTableStatus();
    }

    private void refreshTableStatus() {
        HashMap<String, String> map = getTableStatuses();
        Color colorEmpty = new Color(240, 230, 255);
        Color colorOccupied = new Color(255, 150, 150);
        Color colorPaid = new Color(180, 255, 180);

        for (String tableId : tableButtons.keySet()) {
            JButton button = tableButtons.get(tableId);
            String status = map.getOrDefault(tableId, "โต๊ะว่าง");

            button.setBackground(status.equals("โต๊ะไม่ว่าง") ? colorOccupied :
                                 status.equals("ชำระเงินแล้ว") ? colorPaid : colorEmpty);

            for (ActionListener a : button.getActionListeners()) {
                button.removeActionListener(a);
            }

            if (status.equals("โต๊ะไม่ว่าง")) {
                button.addActionListener(e -> processPayment(tableId));
            }
        }
    }

    private HashMap<String, String> getTableStatuses() {
        HashMap<String, String> map = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_number, status FROM tables")) {
            while (rs.next()) {
                map.put(rs.getString("table_number"), rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void processPayment(String tableId) {
        StringBuilder details = new StringBuilder();
        int total = 0;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT menu_code, menu_name, quantity FROM chef_orders " +
                     "WHERE table_number = ? AND updated_at = (SELECT MAX(updated_at) FROM chef_orders WHERE table_number = ?)"
             )) {

            stmt.setString(1, tableId);
            stmt.setString(2, tableId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString("menu_code");
                String name = rs.getString("menu_name");
                int qty = rs.getInt("quantity");
                int price = getMenuPrice(conn, code);
                int subtotal = price * qty;
                total += subtotal;

                details.append("- ").append(name)
                       .append(" x").append(qty)
                       .append(" = ").append(subtotal).append(" บาท\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "โหลดข้อมูลไม่สำเร็จ");
            return;
        }

        if (total == 0) {
            JOptionPane.showMessageDialog(this, "ไม่พบรายการอาหารล่าสุดของโต๊ะนี้");
            return;
        }

        String msg = "โต๊ะ: " + tableId + "\n\n" +
                     "รายการอาหาร:\n" + details +
                     "\nรวมทั้งหมด: " + total + " บาท\n\n" +
                     "ยืนยันการชำระเงินหรือไม่?";

        int confirm = JOptionPane.showConfirmDialog(this, msg, "ยืนยัน", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            markAsPaid(tableId);
            refreshTableStatus();
            revertAfterDelay(tableId);
        }
    }

    private int getMenuPrice(Connection conn, String code) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE code = ?")) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("price");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void markAsPaid(String tableId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE tables SET status = 'ชำระเงินแล้ว', updated_at = NOW() WHERE table_number = ?")) {
            stmt.setString(1, tableId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "ชำระเงินโต๊ะ " + tableId + " สำเร็จแล้ว");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void revertAfterDelay(String tableId) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                     PreparedStatement stmt = conn.prepareStatement("UPDATE tables SET status = 'โต๊ะว่าง' WHERE table_number = ?")) {
                    stmt.setString(1, tableId);
                    stmt.executeUpdate();
                    SwingUtilities.invokeLater(() -> refreshTableStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 60000);
    }
}