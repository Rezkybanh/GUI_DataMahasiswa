/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author REZKY
 */
public class AutoNumber {
    private Connection connection;
    private String tableName;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAutoNumberInt() {
        String sql = "SELECT MAX(id) FROM " + tableName; // Mengasumsikan kolom ID bernama 'id'
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.valueOf(maxId + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "1"; // Nilai default jika tidak ada data
    }
}