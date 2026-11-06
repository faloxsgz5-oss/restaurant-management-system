package Restaurant;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.*;

public class DailyReport extends JPanel {

    private JLabel saleTotal;
    private DefaultTableModel model;
    private DefaultPieDataset dataset;
    private JTable table;

    public DailyReport() {
        setLayout(null);
        setBackground(new Color(230, 250, 255));

        JLabel header = new JLabel("Daily Report");
        header.setFont(new Font("Tahoma", Font.BOLD, 26));
        header.setBounds(30, 20, 300, 40);
        add(header);

        saleTotal = new JLabel("Daily sales : ...");
        saleTotal.setFont(new Font("Tahoma", Font.PLAIN, 18));
        saleTotal.setBounds(30, 60, 400, 30);
        add(saleTotal);

        dataset = new DefaultPieDataset();

        String[] columns = {"Menu", "Price", "Amount", "Total", "Remark"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 420, 840, 250);
        add(scrollPane);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.setBounds(700, 60, 150, 30);
        exportBtn.setBackground(new Color(200, 255, 200));
        add(exportBtn);

        exportBtn.addActionListener(e -> exportToCSV(model));

        JFreeChart pieChart = ChartFactory.createPieChart("", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBounds(30, 100, 400, 300);
        add(chartPanel);

        // เรียกใช้ทันทีเมื่อสร้าง
        execute();
    }

    public void execute() {
        model.setRowCount(0);
        dataset.clear();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT m.name, m.price, SUM(co.quantity) AS total_qty, " +
                             "GROUP_CONCAT(DISTINCT co.remark SEPARATOR ', ') AS remarks " +
                             "FROM chef_orders co " +
                             "JOIN menu m ON co.menu_code = m.code " +
                             "WHERE DATE(co.updated_at) = CURDATE() " +
                             "GROUP BY m.code, m.name, m.price")) {

            ResultSet rs = stmt.executeQuery();

            double totalSales = 0.0;
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int qty = rs.getInt("total_qty");
                String remark = rs.getString("remarks");
                int total = price * qty;
                totalSales += total;

                dataset.setValue(name, total);
                model.addRow(new Object[]{name, price, qty, total, remark != null ? remark : "-"});
            }

            saleTotal.setText("Daily sales : " + String.format("%,.2f", totalSales) + " Bath");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ไม่สามารถโหลดข้อมูลยอดขายได้");
        }
    }

    private void exportToCSV(DefaultTableModel model) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("บันทึกรายงานเป็น CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getAbsolutePath() + ".csv");
                }

                OutputStreamWriter writer = new OutputStreamWriter(
                        new FileOutputStream(file), "UTF-8");
                writer.write('\uFEFF'); // BOM for Excel

                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.append(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) writer.append(",");
                }
                writer.append("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        writer.append(val != null ? val.toString().replace(",", " ") : "-");
                        if (j < model.getColumnCount() - 1) writer.append(",");
                    }
                    writer.append("\n");
                }

                writer.flush();
                writer.close();
                JOptionPane.showMessageDialog(this, "บันทึกไฟล์ CSV เรียบร้อยแล้ว");

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "ไม่สามารถบันทึกไฟล์ได้: " + e.getMessage());
            }
        }
    }
}