package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;
import java.util.HashMap;

public class Orders extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Table selectedTable;
    private JPanel buttonPanel;

    public Orders(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Orders");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(30, 20, 400, 40);
        add(title);

        Color colorEmpty = new Color(240, 230, 255);
        Color colorOccupied = new Color(255, 150, 150);
        Color colorPaid = new Color(180, 255, 180);

        JLabel status1 = new JLabel("โต๊ะไม่ว่าง", SwingConstants.CENTER);
        status1.setOpaque(true);
        status1.setBackground(colorOccupied);
        status1.setBounds(250, 30, 100, 30);
        add(status1);

        JLabel status2 = new JLabel("ชำระเงินแล้ว", SwingConstants.CENTER);
        status2.setOpaque(true);
        status2.setBackground(colorPaid);
        status2.setBounds(370, 30, 100, 30);
        add(status2);

        JLabel status3 = new JLabel("โต๊ะว่าง", SwingConstants.CENTER);
        status3.setOpaque(true);
        status3.setBackground(colorEmpty);
        status3.setBounds(490, 30, 100, 30);
        add(status3);

        buttonPanel = new JPanel(null);
        buttonPanel.setBackground(new Color(230, 250, 255));
        buttonPanel.setBounds(0, 80, 900, 500);
        add(buttonPanel);

        loadTableButtons();

        // โหลดปุ่มใหม่อัตโนมัติเมื่อเปิดหน้า
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

        Color colorEmpty = new Color(240, 230, 255);
        Color colorOccupied = new Color(255, 150, 150);
        Color colorPaid = new Color(180, 255, 180);

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

                JButton tableBtn = new JButton(tableId);
                tableBtn.setBounds(x + j * 250, y, 200, 60);
                tableBtn.setFocusPainted(false);

                switch (status) {
                    case "โต๊ะไม่ว่าง": tableBtn.setBackground(colorOccupied); break;
                    case "ชำระเงินแล้ว": tableBtn.setBackground(colorPaid); break;
                    default: tableBtn.setBackground(colorEmpty); break;
                }

                tableBtn.addActionListener(e -> {
                    selectedTable = new Table(tableId, status, null);
                    OrdersMenuSelection menuPanel = new OrdersMenuSelection(tableId, this);
                    mainPanel.add(menuPanel, "menu");
                    cardLayout.show(mainPanel, "menu");
                });

                buttonPanel.add(tableBtn);
            }
            y += 80;
        }

        buttonPanel.repaint();
        buttonPanel.revalidate();
    }

    // === เมธอดตาม UML ===
    public void showTables() {
        cardLayout.show(mainPanel, "Orders");
    }

    public void sendOrder() {
        if (selectedTable != null) {
            System.out.println("ส่งออเดอร์สำหรับโต๊ะ: " + selectedTable.getTableNumber());
        } else {
            System.out.println("ยังไม่ได้เลือกโต๊ะ");
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public Table getSelectedTable() {
        return selectedTable;
    }

    public void reloadOrdersPage() {
        mainPanel.removeAll();
        Orders refreshed = new Orders(mainPanel, cardLayout);
        mainPanel.add(refreshed, "Orders");
        cardLayout.show(mainPanel, "Orders");
    }
}