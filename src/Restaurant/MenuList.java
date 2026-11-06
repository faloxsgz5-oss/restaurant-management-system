package Restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MenuList extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public MenuList() {
        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel title = new JLabel("Menu List");
        title.setFont(new Font("Tahoma", Font.BOLD, 26));
        title.setForeground(new Color(70, 130, 150));
        title.setBounds(30, 20, 300, 40);
        add(title);

        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(new Color(210, 235, 245));
        searchPanel.setBounds(30, 70, 880, 50);
        add(searchPanel);

        searchField = new JTextField();
        searchField.setBounds(10, 10, 730, 30);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(750, 10, 100, 30);
        searchBtn.setBackground(new Color(220, 245, 255));
        searchBtn.addActionListener(e -> execute());
        searchPanel.add(searchBtn);

        String[] columns = {"Menu", "Price", "Edit", "Delete"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };
        table.setRowHeight(40);

        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 130, 820, 500);
        add(scrollPane);

        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));

        execute(); // โหลดข้อมูลเมื่อเปิดหน้า
    }

    public void execute() {
        model.setRowCount(0);
        String keyword = searchField.getText().trim();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM menu WHERE name LIKE ? OR code LIKE ?")) {

            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                int price = rs.getInt("price");

                model.addRow(new Object[]{code + " " + name, price + " บาท", "Edit", "Delete"});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, String action) {
            super(checkBox);
            this.label = action;
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            JButton button = new JButton(label);
            button.addActionListener(e -> {
                String menu = model.getValueAt(row, 0).toString();
                String code = menu.split(" ")[0];

                if (label.equals("Edit")) {
                    String newPrice = JOptionPane.showInputDialog("แก้ไขราคา:");
                    try {
                        int price = Integer.parseInt(newPrice);
                        updatePrice(code, price);
                        execute();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "ราคาไม่ถูกต้อง");
                    }
                } else if (label.equals("Delete")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "ลบเมนูนี้หรือไม่?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteMenu(code);
                        execute();
                    }
                }
            });
            return button;
        }

        public Object getCellEditorValue() {
            return new String(label);
        }

        private void updatePrice(String code, int price) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("UPDATE menu SET price = ? WHERE code = ?")) {
                stmt.setInt(1, price);
                stmt.setString(2, code);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void deleteMenu(String code) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM menu WHERE code = ?")) {
                stmt.setString(1, code);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}