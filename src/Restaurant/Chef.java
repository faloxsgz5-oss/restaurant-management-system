package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Chef extends User {

    private JFrame frame;
    private HashMap<String, JButton> tableButtons = new HashMap<>();
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Chef(int userId, String name, String password) {
        super(userId, name, password, "Chef");
      
        
        frame = new JFrame("Chef");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/Restaurant.png"));
        frame.setIconImage(icon.getImage());
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(200, 0, 800, 700);
        frame.add(mainPanel);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(200, 230, 240));
        sidePanel.setBounds(0, 0, 200, 700);
        sidePanel.setLayout(null);
        frame.add(sidePanel);

        JLabel menuLabel = new JLabel("Chef Menu");
        menuLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        menuLabel.setBounds(30, 20, 140, 30);
        sidePanel.add(menuLabel);

        JButton checkOrdersButton = new JButton("Check Orders");
        checkOrdersButton.setBounds(30, 70, 140, 40);
        checkOrdersButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        checkOrdersButton.setBackground(new Color(230, 250, 255));
        checkOrdersButton.addActionListener(e -> showPanel("checkOrders"));
        sidePanel.add(checkOrdersButton);

        JButton queueButton = new JButton("Queue");
        queueButton.setBounds(30, 130, 140, 40);
        queueButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        queueButton.setBackground(new Color(230, 250, 255));
        queueButton.addActionListener(e -> showPanel("queue"));
        sidePanel.add(queueButton);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(30, 190, 140, 40);
        refreshBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
        refreshBtn.setBackground(new Color(220, 255, 220));
        refreshBtn.addActionListener(e -> loadTableStatus());
        sidePanel.add(refreshBtn);

        JButton logoutButton = new JButton("Log out");
        logoutButton.setBounds(30, 600, 140, 40);
        logoutButton.setBackground(new Color(255, 150, 150));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new Login();
        });
        sidePanel.add(logoutButton);

        JPanel checkOrdersPanel = new JPanel(null);
        checkOrdersPanel.setBackground(new Color(240, 250, 255));
        JLabel checkOrdersTitle = new JLabel("Check Orders");
        checkOrdersTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
        checkOrdersTitle.setBounds(300, 20, 300, 40);
        checkOrdersPanel.add(checkOrdersTitle);

        String[][] tableIds = {
            {"A1", "B1", "C1"},
            {"A2", "B2", "C2"},
            {"A3", "B3", "C3"},
            {"A4", "B4", "C4"}
        };

        Color emptyColor = new Color(240, 230, 255);
        int x = 50, y = 100;
        for (String[] row : tableIds) {
            for (String tableId : row) {
                JButton tableBtn = new JButton(tableId);
                tableBtn.setBounds(x, y, 180, 60);
                tableBtn.setBackground(emptyColor);
                tableBtn.setFocusPainted(false);
                tableBtn.addActionListener(e -> openLatestOrder(tableId));
                checkOrdersPanel.add(tableBtn);
                tableButtons.put(tableId, tableBtn);
                x += 230;
            }
            x = 50;
            y += 80;
        }

        mainPanel.add(checkOrdersPanel, "checkOrders");
        mainPanel.add(new Queue(mainPanel, cardLayout), "queue");

        loadTableStatus();
        frame.setVisible(true);
    }

    private void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    private void loadTableStatus() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT table_number FROM chef_orders WHERE status = 'ออเดอร์'")) {

            for (JButton button : tableButtons.values()) {
                button.setBackground(new Color(240, 230, 255));
            }

            while (rs.next()) {
                String tableNumber = rs.getString("table_number");
                JButton button = tableButtons.get(tableNumber);
                if (button != null) {
                    button.setBackground(new Color(255, 150, 150));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "ไม่สามารถโหลดสถานะโต๊ะได้");
        }
    }

    private void openLatestOrder(String tableId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM chef_orders WHERE table_number = ? AND updated_at = (" +
                             "SELECT MAX(updated_at) FROM chef_orders WHERE table_number = ? AND status = 'ออเดอร์')")) {

            stmt.setString(1, tableId);
            stmt.setString(2, tableId);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Order> orders = new ArrayList<>();
            while (rs.next()) {
                String code = rs.getString("menu_code");
                String name = rs.getString("menu_name");
                int qty = rs.getInt("quantity");
                Timestamp time = rs.getTimestamp("updated_at");
                String remark = rs.getString("remark");

                int price = getMenuPrice(conn, code);
                orders.add(new Order(new Table(tableId), code, name, price, qty, time, remark));
            }

            if (!orders.isEmpty()) {
                QueueFinish queueFinish = new QueueFinish(orders, mainPanel, cardLayout);
                mainPanel.add(queueFinish, "QueueFinish");
                cardLayout.show(mainPanel, "QueueFinish");
            } else {
                JOptionPane.showMessageDialog(frame, "ไม่พบออเดอร์ล่าสุดของโต๊ะนี้");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "เกิดข้อผิดพลาดในการโหลดออเดอร์");
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
}