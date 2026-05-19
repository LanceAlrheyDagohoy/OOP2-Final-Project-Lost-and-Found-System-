package model;

public class User {
    private final int id;
    private final String name;
    private final String studentID; // MODIFIED: Added field
    private final String email;
    private final String password;
    private final String role;

    // MODIFIED: Constructor now includes studentID
    public User(int id, String name, String studentID, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.studentID = studentID;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStudentID() { return studentID; } // MODIFIED: Added getter
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
