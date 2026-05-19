package dao;

import database.Database;
import model.Claim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClaimDAO {
    private final Connection connection = Database.getConnection();

    public boolean addClaim(int itemId, String itemName, String claimantName, String proof) {
        String sql = "INSERT INTO claims(item_id, item_name, claimant_name, proof, status) VALUES (?, ?, ?, ?, 'Pending')";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);
            statement.setString(2, itemName);
            statement.setString(3, claimantName);
            statement.setString(4, proof);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Claim> getAllClaims() {
        return fetchClaims("SELECT * FROM claims");
    }

    public List<Claim> getClaimsByUser(String claimantName) {
        return fetchClaims("SELECT * FROM claims WHERE claimant_name = '" + claimantName + "'");
    }

    private List<Claim> fetchClaims(String sql) {
        List<Claim> list = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                list.add(new Claim(
                        result.getInt("id"),
                        result.getInt("item_id"),
                        result.getString("item_name"),
                        result.getString("claimant_name"),
                        result.getString("proof"),
                        result.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateClaimStatus(int claimId, String status) {
        String sql = "UPDATE claims SET status=? WHERE id=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, claimId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
