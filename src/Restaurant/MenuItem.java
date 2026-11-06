package Restaurant;

public class MenuItem {
    private String code;
    private String name;
    private int price;
    private int quantity;

    public MenuItem(String code, String name, int price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return price * quantity;
    }
}