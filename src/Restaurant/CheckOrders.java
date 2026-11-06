package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class CheckOrders extends JPanel {
    private JPanel parentPanel;
    private CardLayout cardLayout;
    private JPanel buttonPanel;

    public CheckOrders(JPanel parentPanel, CardLayout cardLayout) {
        this.parentPanel = parentPanel;
        this.cardLayout = cardLayout;

        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Check Orders");
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

        buttonPanel = new JPanel(null);
        buttonPanel.setBackground(new Color(230, 250, 255));
        buttonPanel.setBounds(0, 80, 900, 500);
        add(buttonPanel);

        loadTableButtons();

        // โหลดใหม่ทุกครั้งที่หน้า CheckOrders ถูกแสดง
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadTableButtons();
            }
        });
    }

    public void loadTableButtons() {
        buttonPanel.removeAll();

        String[][] tableIds = {
            {"A1", "B1", "C1"},
            {"A2", "B2", "C2"},
            {"A3", "B3", "C3"},
            {"A4", "B4", "C4"}
        };

        HashMap<String, String> tableStatusMap = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_number, status FROM tables")) {

            while (rs.next()) {
                tableStatusMap.put(rs.getString("table_number"), rs.getString("status"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "โหลดสถานะโต๊ะไม่สำเร็จ");
        }

        int x = 50, y = 0;
        for (int i = 0; i < tableIds.length; i++) {
            for (int j = 0; j < tableIds[i].length; j++) {
                String tableId = tableIds[i][j];
                String status = tableStatusMap.getOrDefault(tableId, "โต๊ะว่าง");

                JButton btn = new JButton(tableId);
                btn.setBounds(x + j * 250, y, 200, 60);
                btn.setFocusPainted(false);

                if (status.equals("โต๊ะไม่ว่าง")) {
                    btn.setBackground(new Color(255, 150, 150));
                    btn.addActionListener(e -> openEditOrdersIfAvailable(tableId));
                } else if (status.equals("ชำระเงินแล้ว")) {
                    btn.setBackground(new Color(180, 255, 180));
                } else {
                    btn.setBackground(new Color(240, 230, 255));
                }

                buttonPanel.add(btn);
            }
            y += 80;
        }

        buttonPanel.repaint();
        buttonPanel.revalidate();
    }

    private void openEditOrdersIfAvailable(String tableId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM chef_orders WHERE table_number = ? AND status = 'ออเดอร์'")) {
            stmt.setString(1, tableId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                EditOrders editOrders = new EditOrders(tableId, parentPanel, cardLayout);
                parentPanel.add(editOrders, "EditOrders");
                cardLayout.show(parentPanel, "EditOrders");
            } else {
                JOptionPane.showMessageDialog(this, "ไม่สามารถแก้ไขได้ เชฟอาจเริ่มทำแล้ว");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการเปิดหน้าแก้ไข");
        }
    }
}