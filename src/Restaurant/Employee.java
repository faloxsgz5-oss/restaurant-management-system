package Restaurant;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.event.ActionListener;

public class Employee extends User {
    private JFrame frame;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public Employee(int userId, String name, String password) {
        super(userId, name, password, "employee");
        initUI();
    }

    private void initUI() {
    	
        setUIFont();
        frame = new JFrame("Employee");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/Restaurant.png"));
        frame.setIconImage(icon.getImage());
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        

        Color bgLeft = new Color(200, 230, 240);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(bgLeft);
        sidePanel.setBounds(0, 0, 200, 700);
        sidePanel.setLayout(null);
        frame.add(sidePanel);

        String[] menuItems = {"Menu", "Orders", "Check Orders", "Payment"};
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = new JButton(menuItems[i]);
            btn.setBounds(30, 60 + i * 60, 140, 40);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(230, 250, 255));
            String name = menuItems[i];
            btn.addActionListener(e -> cardLayout.show(contentPanel, name));
            sidePanel.add(btn);
        }

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(30, 60 + menuItems.length * 60, 140, 40);
        refreshBtn.setBackground(new Color(220, 255, 220));
        refreshBtn.addActionListener(e -> {
            contentPanel.remove(1);
            contentPanel.add(new Orders(contentPanel, cardLayout), "Orders");
            contentPanel.add(new CheckOrders(contentPanel, cardLayout), "Check Orders");
            contentPanel.add(new Payment(), "Payment");
        });
        sidePanel.add(refreshBtn);

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

        contentPanel.add(createMenuPanel(), "Menu");
        contentPanel.add(new Orders(contentPanel, cardLayout), "Orders");
        contentPanel.add(new CheckOrders(contentPanel, cardLayout), "Check Orders");
        contentPanel.add(new Payment(), "Payment");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 250, 255));
        JLabel title = new JLabel("Menu");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBounds(30, 20, 300, 40);
        panel.add(title);

        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(new Color(210, 235, 245));
        searchPanel.setBounds(30, 70, 880, 50);
        panel.add(searchPanel);

        JTextField searchField = new JTextField();
        searchField.setBounds(10, 10, 730, 30);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("ค้นหา");
        searchBtn.setBounds(750, 10, 100, 30);
        searchBtn.setBackground(new Color(220, 245, 255));
        searchPanel.add(searchBtn);

        JPanel menuPanel = new JPanel(null);
        menuPanel.setBackground(new Color(230, 250, 255)); // สีพื้นหลังเดิม
        menuPanel.setPreferredSize(new Dimension(850, 1000)); // กำหนดขนาดเบื้องต้น

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBounds(30, 140, 880, 500);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(new Color(230, 250, 255)); 
        scrollPane.setBorder(null);
        panel.add(scrollPane);

        ArrayList<JButton> menuButtons = new ArrayList<>();

        ActionListener loadMenu = e -> {
            menuPanel.removeAll();
            menuButtons.clear();
            String keyword = searchField.getText().trim().toLowerCase();

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT code, name FROM menu WHERE code LIKE ? OR name LIKE ?")) {

                String pattern = "%" + keyword + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);

                ResultSet rs = stmt.executeQuery();

                int x = 0, y = 0;
                while (rs.next()) {
                    String label = rs.getString("code") + " " + rs.getString("name");
                    JButton btn = new JButton(label);
                    btn.setBounds(x, y, 250, 60);
                    btn.setBackground(new Color(240, 230, 255));
                    menuPanel.add(btn);
                    menuButtons.add(btn);

                    x += 290;
                    if (menuButtons.size() % 3 == 0) {
                        x = 0;
                        y += 80;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "โหลดเมนูไม่สำเร็จ");
            }

            menuPanel.repaint();
            menuPanel.revalidate();
        };

        searchBtn.addActionListener(loadMenu);
        loadMenu.actionPerformed(null);

        return panel;
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