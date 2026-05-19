package dao;

import database.Database;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection = Database.getConnection();

    // MODIFIED: Updated SQL to include student_id
    public boolean registerUser(String name, String studentID, String email, String password, String role) {
        String sql = "INSERT INTO users(name, student_id, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, studentID);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // MODIFIED: Updated Result Set to fetch student_id
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("student_id"), // MODIFIED: Added student_id
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
