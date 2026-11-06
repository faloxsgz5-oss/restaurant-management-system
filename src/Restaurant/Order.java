package Restaurant;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Order {
    private Table table;
    private Timestamp orderTime;
    private String menuCode;
    private String menuName;
    private int price;
    private int quantity;
    private String remark;

    // Constructor สำหรับออเดอร์ที่ไม่มีรายละเอียดเมนู
    public Order(Table table, Timestamp orderTime) {
        this.table = table;
        this.orderTime = orderTime;
    }

    // Constructor สำหรับออเดอร์ที่มีรายละเอียดเมนู + remark
    public Order(Table table, String menuCode, String menuName, int price, int quantity, Timestamp orderTime, String remark) {
        this.table = table;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
        this.orderTime = orderTime;
        this.remark = remark;
    }

    
    public Order(Table table, String menuCode, String menuName, int price, int quantity, Timestamp orderTime) {
        this(table, menuCode, menuName, price, quantity, orderTime, "");
    }

    public Table getTable() {
        return table;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public String getFormattedTime() {
        if (orderTime == null) return "";
        return new SimpleDateFormat("HH:mm:ss").format(orderTime);
    }

    public String getMenuCode() {
        return menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}