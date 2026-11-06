package Restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddEmployee extends JPanel {

    private JTextField idField, userField, fnameField, lnameField, roleField;
    private JPasswordField passField;

    public AddEmployee() {
        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel header = new JLabel("Add Employee");
        header.setFont(new Font("Tahoma", Font.BOLD, 26));
        header.setForeground(new Color(70, 130, 150));
        header.setBounds(30, 20, 300, 40);
        add(header);

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(210, 240, 250));
        formPanel.setBounds(30, 80, 700, 400);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(formPanel);

        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(30, 30, 100, 25);
        formPanel.add(idLabel);
        idField = new JTextField();
        idField.setBounds(150, 30, 200, 30);
        formPanel.add(idField);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30, 70, 100, 25);
        formPanel.add(userLabel);
        userField = new JTextField();
        userField.setBounds(150, 70, 200, 30);
        formPanel.add(userField);

        JLabel fnameLabel = new JLabel("Firstname");
        fnameLabel.setBounds(30, 110, 100, 25);
        formPanel.add(fnameLabel);
        fnameField = new JTextField();
        fnameField.setBounds(150, 110, 200, 30);
        formPanel.add(fnameField);

        JLabel lnameLabel = new JLabel("Lastname");
        lnameLabel.setBounds(30, 150, 100, 25);
        formPanel.add(lnameLabel);
        lnameField = new JTextField();
        lnameField.setBounds(150, 150, 200, 30);
        formPanel.add(lnameField);

        JLabel roleLabel = new JLabel("Role");
        roleLabel.setBounds(30, 190, 100, 25);
        formPanel.add(roleLabel);
        roleField = new JTextField();
        roleField.setBounds(150, 190, 200, 30);
        formPanel.add(roleField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(30, 230, 100, 25);
        formPanel.add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(150, 230, 200, 30);
        formPanel.add(passField);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(370, 330, 90, 35);
        cancelBtn.setBackground(new Color(255, 150, 150));
        formPanel.add(cancelBtn);

        JButton addBtn = new JButton("Add");
        addBtn.setBounds(470, 330, 90, 35);
        addBtn.setBackground(new Color(200, 240, 255));
        formPanel.add(addBtn);

        addBtn.addActionListener(e -> execute());

        cancelBtn.addActionListener(e -> {
            idField.setText("");
            userField.setText("");
            fnameField.setText("");
            lnameField.setText("");
            passField.setText("");
            roleField.setText("");
        });
    }

    // เมธอดหลักของ AddEmployee
    public void execute() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (id, username, firstname, lastname, password, role) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, Integer.parseInt(idField.getText().trim()));
            stmt.setString(2, userField.getText().trim());
            stmt.setString(3, fnameField.getText().trim());
            stmt.setString(4, lnameField.getText().trim());
            stmt.setString(5, new String(passField.getPassword()));
            stmt.setString(6, roleField.getText().trim());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "เพิ่มพนักงานสำเร็จแล้ว");

            idField.setText("");
            userField.setText("");
            fnameField.setText("");
            lnameField.setText("");
            passField.setText("");
            roleField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการบันทึกข้อมูล");
        }
    }
}