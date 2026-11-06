package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class QueueFinish extends JPanel {

    private String tableId;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public QueueFinish(ArrayList<Order> orders, JPanel mainPanel, CardLayout cardLayout) {
        this.tableId = orders.get(0).getTable().getTableNumber();
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(null);
        setBackground(new Color(230, 245, 250));

        JLabel title = new JLabel("Check Orders - โต๊ะ " + tableId);
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setBounds(280, 20, 400, 40);
        add(title);

        JTextArea orderArea = new JTextArea();
        orderArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        orderArea.setEditable(false);
        orderArea.setBackground(new Color(230, 245, 250));
        JScrollPane scrollPane = new JScrollPane(orderArea);
        scrollPane.setBounds(80, 80, 650, 200);
        scrollPane.setBorder(null);
        add(scrollPane);

        int total = 0;
        StringBuilder builder = new StringBuilder();
        for (Order order : orders) {
            int sum = order.getPrice() * order.getQuantity();
            builder.append(order.getMenuCode()).append(" - ").append(order.getMenuName())
                   .append(" x").append(order.getQuantity()).append(" = ").append(sum).append(" บาท");
            if (order.getRemark() != null && !order.getRemark().isEmpty()) {
                builder.append("  [").append(order.getRemark()).append("]");
            }
            builder.append("\n");
            total += sum;
        }
        orderArea.setText(builder.toString());

        JLabel totalLabel = new JLabel("รวมทั้งหมด: " + total + " บาท");
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        totalLabel.setBounds(80, 300, 400, 40);
        add(totalLabel);

        JButton finishBtn = new JButton("Finish");
        finishBtn.setBounds(200, 380, 150, 50);
        finishBtn.setBackground(new Color(180, 250, 180));
        finishBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
        finishBtn.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "")) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE chef_orders SET status = 'อาหารเสร็จแล้ว' WHERE table_number = ? AND menu_code = ?");
                for (Order o : orders) {
                    stmt.setString(1, tableId);
                    stmt.setString(2, o.getMenuCode());
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "ส่งอาหารเรียบร้อยแล้ว");
                backToQueue();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        add(finishBtn);

        JButton deleteBtn = new JButton("Delete All");
        deleteBtn.setBounds(400, 380, 150, 50);
        deleteBtn.setBackground(new Color(255, 150, 150));
        deleteBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
        deleteBtn.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "")) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM chef_orders WHERE table_number = ? AND menu_code = ?");
                for (Order o : orders) {
                    stmt.setString(1, tableId);
                    stmt.setString(2, o.getMenuCode());
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "ลบออเดอร์แล้ว");
                backToQueue();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        add(deleteBtn);
    }

    private void backToQueue() {
        Component[] components = mainPanel.getComponents();
        for (Component c : components) {
            if (c instanceof Queue) {
                mainPanel.remove(c);
                break;
            }
        }
        Queue newQueue = new Queue(mainPanel, cardLayout);
        mainPanel.add(newQueue, "queue");
        cardLayout.show(mainPanel, "queue");
    }
}