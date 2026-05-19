package dao;

import database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActivityDAO {
    private Connection connection = Database.getConnection();

    public void log(String username, String action) {
        String sql = "INSERT INTO activity_logs (user_name, action) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}