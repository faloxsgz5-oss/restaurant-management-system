package Restaurant;

import java.sql.Timestamp;

public class Table {
    private String tableNumber;
    private String status;
    private Timestamp updatedAt;

    // Constructor หลัก (ใช้ในระบบทั่วไป)
    public Table(String tableNumber, String status, Timestamp updatedAt) {
        this.tableNumber = tableNumber;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // ✅ Constructor สำหรับใช้งานแบบง่าย เช่น ใน Queue.java
    public Table(String tableNumber) {
        this.tableNumber = tableNumber;
        this.status = null;
        this.updatedAt = null;
    }

    // Getter และ Setter
    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString สำหรับ debug หรือแสดงผล
    @Override
    public String toString() {
        return "Table{" +
                "tableNumber='" + tableNumber + '\'' +
                ", status='" + status + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}