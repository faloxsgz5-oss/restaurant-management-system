package Restaurant;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.sql.*;
import java.util.Enumeration;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login() {
        setTitle("Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        Color inputColor = new Color(240, 240, 240);
        Color accentColor = new Color(0, 229, 255);

        JPanel header = new JPanel();
        header.setBackground(Color.BLACK);
        header.setBounds(0, 0, 800, 80);
        header.setLayout(null);
        add(header);

        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 28));
        title.setBounds(0, 20, 800, 40);
        header.add(title);

        JPanel panel = new JPanel(null);
        panel.setBounds(0, 80, 800, 420);
        panel.setBackground(Color.WHITE);
        add(panel);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        userLabel.setBounds(180, 70, 100, 30);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        usernameField.setBounds(300, 70, 320, 40);
        usernameField.setBackground(inputColor);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passLabel.setBounds(180, 140, 100, 30);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordField.setBounds(300, 140, 320, 40);
        passwordField.setBackground(inputColor);
        panel.add(passwordField);

        JLabel warningLabel = new JLabel("");
        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        warningLabel.setBounds(300, 185, 300, 20);
        panel.add(warningLabel);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        loginButton.setBounds(300, 230, 180, 50);
        loginButton.setBackground(accentColor);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        loginButton.addActionListener(e -> routeToRole(warningLabel));
        setVisible(true);
    }

    public void routeToRole(JLabel warningLabel) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            warningLabel.setText("กรุณากรอกชื่อผู้ใช้และรหัสผ่าน");
            return;
        }

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String uname = rs.getString("username");
                String pwd = rs.getString("password");
                String role = rs.getString("role");

                dispose();
                switch (role.toLowerCase()) {
                    case "manager": new Manager(userId, uname, pwd); break;
                    case "employee": new Employee(userId, uname, pwd); break;
                    case "chef": new Chef(userId, uname, pwd); break;
                    default: JOptionPane.showMessageDialog(this, "บทบาทผู้ใช้ไม่ถูกต้อง");
                }
            } else {
                warningLabel.setText("ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
            }

        } catch (Exception e) {
            e.printStackTrace(); // แสดงข้อความ error แบบเต็มใน console
            JOptionPane.showMessageDialog(this, "เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล:\n" + e.getMessage());
        }
    }

    public static void setUIFont() {
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, new FontUIResource(thaiFont));
            }
        }
    }

    public static void main(String[] args) {
        setUIFont();
        new Login();
    }
}