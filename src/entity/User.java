package entity;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // "admin" or "customer"
    private int bookingCount; // for loyalty discounts

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.bookingCount = 0;
    }

    // Constructor for database loading
    public User(int id, String username, String password, String role, int bookingCount) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.bookingCount = bookingCount;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getBookingCount() { return bookingCount; }

    public void setBookingCount(int bookingCount) { this.bookingCount = bookingCount; }
    public void incrementBookingCount() { this.bookingCount++; }
}