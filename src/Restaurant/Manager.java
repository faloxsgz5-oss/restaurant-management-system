package Restaurant;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Enumeration;

public class Manager extends User {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JTable table;
    private DefaultTableModel model;
    private int selectedId = -1;
    private JTextField searchField;

    public Manager(int userId, String username, String password) {
        super(userId, username, password, "Manager");

        setUIFont();
        frame = new JFrame("Manager");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/Restaurant.png"));
        frame.setIconImage(icon.getImage());
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
 

        Color bgLeft = new Color(200, 230, 240);

        JPanel sidePanel = new JPanel(null);
        sidePanel.setBackground(bgLeft);
        sidePanel.setBounds(0, 0, 200, 700);
        frame.add(sidePanel);

        String[] menuItems = {
                "Employee List", "Daily Report", "Add Employee", "Add Menu", "Menu List"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = new JButton(menuItems[i]);
            btn.setBounds(30, 60 + i * 60, 140, 40);
            btn.setBackground(new Color(230, 250, 255));
            btn.setFocusPainted(false);
            String name = menuItems[i];
            btn.addActionListener(e -> {
                cardLayout.show(contentPanel, name);
                if (name.equals("Employee List")) loadEmployees(null);
            });
            sidePanel.add(btn);
        }

        JButton logoutBtn = new JButton("Log out");
        logoutBtn.setBounds(30, 550, 140, 40);
        logoutBtn.setBackground(new Color(255, 150, 150));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new Login();
        });
        sidePanel.add(logoutBtn);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(200, 0, 900, 700);
        frame.add(contentPanel);

        contentPanel.add(createEmployeeListPanel(), "Employee List");
        contentPanel.add(new DailyReport(), "Daily Report");
        contentPanel.add(new AddEmployee(), "Add Employee");
        contentPanel.add(new MenuCategory(contentPanel, cardLayout), "Add Menu");
        contentPanel.add(new MenuList(), "Menu List");

        frame.setVisible(true);
        cardLayout.show(contentPanel, "Employee List");
        loadEmployees(null);
    }

    private JPanel createEmployeeListPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Employee List");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(120, 180, 200));
        title.setBounds(30, 20, 300, 40);
        panel.add(title);

        searchField = new JTextField();
        searchField.setBounds(30, 70, 600, 30);
        panel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(640, 70, 100, 30);
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadEmployees(keyword.isEmpty() ? null : keyword);
        });
        panel.add(searchBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(750, 70, 100, 30);
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());
        panel.add(deleteBtn);

        model = new DefaultTableModel(new String[]{"ID", "Username", "Firstname", "Lastname", "Password", "Role"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedId = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 120, 830, 500);
        panel.add(scrollPane);

        return panel;
    }

    private void loadEmployees(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM users";
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasKeyword) {
            sql += " WHERE username LIKE ? OR firstname LIKE ? OR lastname LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (hasKeyword) {
                String pattern = "%" + keyword + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
            }

            ResultSet rs = stmt.executeQuery();
            boolean found = false;

            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("password"),
                    rs.getString("role")
                });
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "ไม่พบข้อมูลพนักงาน", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "เกิดข้อผิดพลาดในการโหลดข้อมูลพนักงาน");
        }
    }

    private void deleteSelectedEmployee() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(frame, "กรุณาเลือกพนักงานก่อน", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "ลบพนักงาน ID " + selectedId + " ใช่หรือไม่?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

                stmt.setInt(1, selectedId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "ลบสำเร็จ");
                loadEmployees(null);
                selectedId = -1;

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "เกิดข้อผิดพลาดในการลบ");
            }
        }
    }
    
    public void execute(String panelName) {
        Component panel = findPanelByName(panelName);
        if (panel == null) {
            System.out.println("ไม่พบ panel: " + panelName);
            return;
        }

        try {
            panel.getClass().getMethod("execute").invoke(panel);
        } catch (NoSuchMethodException e) {
            System.out.println("ไม่มีเมธอด execute() ในคลาส: " + panel.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === เมธอดค้นหา panel จากชื่อใน CardLayout ===
    private Component findPanelByName(String name) {
        for (Component comp : contentPanel.getComponents()) {
            if (name.equals(contentPanel.getLayout().toString())) {
                return comp;
            }
            if (comp.getName() != null && comp.getName().equals(name)) {
                return comp;
            }
            if (comp instanceof JPanel && ((JPanel) comp).getClientProperty("name") != null) {
                if (((JPanel) comp).getClientProperty("name").equals(name)) {
                    return comp;
                }
            }
        }
        return null;
    }

    public static void setUIFont() {
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, new FontUIResource(thaiFont));
            }
        }
    }
}