package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuCategory extends JPanel {

    private JTextField codeField;
    private JTextField nameField;
    private JTextField priceField;
    private JPanel parentPanel;
    private CardLayout cardLayout;

    public MenuCategory(JPanel parentPanel, CardLayout cardLayout) {
        this.parentPanel = parentPanel;
        this.cardLayout = cardLayout;

        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel header = new JLabel("Add Menu");
        header.setFont(new Font("Tahoma", Font.BOLD, 26));
        header.setForeground(new Color(70, 130, 150));
        header.setBounds(30, 20, 300, 40);
        add(header);

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(210, 235, 245));
        formPanel.setBounds(30, 80, 780, 130);
        add(formPanel);

        JLabel codeLabel = new JLabel("Code");
        codeLabel.setBounds(30, 30, 100, 25);
        formPanel.add(codeLabel);

        codeField = new JTextField();
        codeField.setBounds(130, 30, 200, 30);
        formPanel.add(codeField);

        JLabel nameLabel = new JLabel("Menu Name");
        nameLabel.setBounds(30, 70, 100, 25);
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 70, 200, 30);
        formPanel.add(nameField);

        JLabel priceLabel = new JLabel("Price");
        priceLabel.setBounds(370, 70, 60, 25);
        formPanel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(430, 70, 200, 30);
        formPanel.add(priceField);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(570, 230, 90, 35);
        cancelBtn.setBackground(new Color(255, 150, 150));
        add(cancelBtn);

        JButton addBtn = new JButton("Add");
        addBtn.setBounds(670, 230, 90, 35);
        addBtn.setBackground(new Color(200, 240, 255));
        add(addBtn);

        addBtn.addActionListener(e -> execute());

        cancelBtn.addActionListener(e -> {
            codeField.setText("");
            nameField.setText("");
            priceField.setText("");
        });
    }
    
    // เมธอดหลักของ MenuCategory
    public void execute() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กรุณากรอก Code, ชื่อเมนู และราคา");
            return;
        }

        try {
            int price = Integer.parseInt(priceText);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO menu (code, name, price) VALUES (?, ?, ?)")) {

                stmt.setString(1, code);
                stmt.setString(2, name);
                stmt.setInt(3, price);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "เพิ่มเมนูเรียบร้อยแล้ว");

                
                for (Component comp : parentPanel.getComponents()) {
                    if (comp instanceof MenuList) {
                        ((MenuList) comp).execute();
                        break;
                    }
                }

                cardLayout.show(parentPanel, "Menu List");

            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Code นี้มีอยู่แล้ว กรุณาเปลี่ยนใหม่");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการเพิ่มเมนู");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกราคาเป็นตัวเลข");
        }
    }
}