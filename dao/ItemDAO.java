package dao;

import database.Database;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    Connection connection;

    public ItemDAO() {
        connection = Database.getConnection();
    }
    public boolean addItem(Item item) {
        // Check for duplicates before adding
        if (isDuplicate(item.getItemName(), item.getLocationFound())) {
            return false;
        }

        String sql = "INSERT INTO items(item_name, description, category, location_found, status, reportedBy, imagePath, archived) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getCategory());
            statement.setString(4, item.getLocationFound());
            statement.setString(5, item.getStatus());
            statement.setString(6, item.getReportedBy());
            statement.setString(7, item.getImagePath());
            statement.setBoolean(8, false);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        // Only show items that are NOT archived in the main list
        String sql = "SELECT * FROM items WHERE archived = false";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Item item = new Item(
                        result.getString("item_name"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getString("location_found"),
                        result.getString("status"),
                        result.getString("reportedBy"),
                        result.getString("imagePath"),
                        result.getBoolean("archived")
                );
                item.setId(result.getInt("id"));
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public boolean isDuplicate(String name, String location) {
        String sql = "SELECT COUNT(*) FROM items WHERE item_name = ? AND location_found = ? AND archived = false";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, location);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void archiveOldItems() {
        // Automatically archives items reported more than 30 days ago
        // Assumes you have a 'date_reported' column in your DB
        String sql = "UPDATE items SET archived = true WHERE date_reported < DATE_SUB(NOW(), INTERVAL 30 DAY)";
        try {
            connection.createStatement().executeUpdate(sql);
            System.out.println("Cleanup: Old items archived.");
        } catch (Exception e) { e.printStackTrace(); }
    }
    public boolean deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_name=?, description=?, category=?, location_found=?, status=? WHERE id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getCategory());
            statement.setString(4, item.getLocationFound());
            statement.setString(5, item.getStatus());
            statement.setInt(6, item.getId()); // Use the ID to find the right row

            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateItemStatus(int itemId, String status) {
        String sql = "UPDATE items SET status=? WHERE id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
