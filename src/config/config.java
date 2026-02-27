package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class config {

    // =========================
    // DATABASE CONNECTION
    // =========================
    public static Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:motorparts.db");

            if (con != null) {
                System.out.println("Connection Successful");
            }

            return con;

        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================
    // INSERT RECORD (SAFE)
    // =========================
    public void addRecord(String sql, Object... values) {

        Connection conn = connectDB();

        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Database connection failed!");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding record: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    // =========================
    // AUTHENTICATION / GET DATA
    // =========================
    public ResultSet getData(String sql, Object... values) {

        try {
            Connection conn = connectDB();

            if (conn == null) {
                return null;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            return pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println("Query Error: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // DISPLAY TABLE DATA (REPLACEMENT FOR DbUtils)
    // =========================
    public void displayData(String sql, JTable table) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create table model
            DefaultTableModel model = new DefaultTableModel();

            // Add columns
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);

        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }
}