package Restaurant;

public class User {
    private int userId;
    private String name;
    private String password;
    private String role;

    public User(int userId, String name, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean authenticate(String username, String password) {
        return this.name.equals(username) && this.password.equals(password);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}