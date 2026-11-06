package Restaurant;

public class OrderItem {
    private MenuItem menu;
    private int quantity;

    public OrderItem(MenuItem menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    
    public MenuItem getMenu() {
        return menu;
    }

    public int getQuantity() {
        return quantity;
    }

    
    public double getSubtotal() {
        
        if (menu != null) {
            return menu.getPrice() * quantity;
        } else {
            return 0.0; 
        }
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}