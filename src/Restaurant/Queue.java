package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class Queue extends JPanel {

    private Map<String, ArrayList<Order>> tableOrdersMap;
    private JPanel ordersPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Queue(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.tableOrdersMap = new LinkedHashMap<>();

        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 250));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(230, 245, 250));
        JLabel title = new JLabel("Queue");
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        ordersPanel = new JPanel(null);
        ordersPanel.setBackground(new Color(230, 245, 250));

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(230, 245, 250));
        add(scrollPane, BorderLayout.CENTER);

        loadOrdersFromDatabase();
    }

    private void updateOrdersPanel() {
        ordersPanel.removeAll();

        int x = 10;
        int y = 10;
        int count = 0;

        for (String tableId : tableOrdersMap.keySet()) {
            ArrayList<Order> orders = tableOrdersMap.get(tableId);
            if (orders.isEmpty()) continue;

            String time = orders.get(0).getFormattedTime();
            JButton orderBtn = new JButton("<html><center>" + tableId + "<br/>Time " + time + "</center></html>");
            orderBtn.setBounds(x, y, 200, 80);
            orderBtn.setBackground(new Color(255, 150, 150));
            orderBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
            orderBtn.setFocusPainted(false);

            orderBtn.addActionListener(e -> {
                QueueFinish queueFinishPanel = new QueueFinish(orders, mainPanel, cardLayout);
                mainPanel.add(queueFinishPanel, "QueueFinish");
                cardLayout.show(mainPanel, "QueueFinish");
            });

            ordersPanel.add(orderBtn);

            x += 220;
            count++;
            if (count % 3 == 0) {
                x = 10;
                y += 100;
            }
        }

        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void loadOrdersFromDatabase() {
        tableOrdersMap.clear();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM chef_orders WHERE status = 'ออเดอร์' AND updated_at IN (" +
                             "SELECT MAX(updated_at) FROM chef_orders WHERE status = 'ออเดอร์' GROUP BY table_number" +
                     ") ORDER BY table_number, updated_at DESC")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tableNum = rs.getString("table_number");
                String menuCode = rs.getString("menu_code");
                String menuName = rs.getString("menu_name");
                int quantity = rs.getInt("quantity");
                Timestamp time = rs.getTimestamp("updated_at");
                String remark = rs.getString("remark");

                int price = getMenuPrice(conn, menuCode);

                Table table = new Table(tableNum);
                Order order = new Order(table, menuCode, menuName, price, quantity, time, remark);

                tableOrdersMap.putIfAbsent(tableNum, new ArrayList<>());
                tableOrdersMap.get(tableNum).add(order);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "ไม่สามารถโหลดคิวจากฐานข้อมูลได้\n" + ex.getMessage());
        }

        updateOrdersPanel();
    }

    private int getMenuPrice(Connection conn, String code) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT price FROM menu WHERE code = ?")) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}