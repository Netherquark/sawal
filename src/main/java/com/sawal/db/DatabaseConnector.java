package com.sawal.db;

import com.sawal.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DatabaseConnector {

    @Value("${spring.datasource.url}")
    private String url = "jdbc:mysql://localhost:3306/sawal?useSSL=false&serverTimezone=UTC";

    @Value("${spring.datasource.username}")
    private String username = "root";

    @Value("${spring.datasource.password}")
    private String password = "YOUR_PASSWORD";

    private Connection connection;

    @PostConstruct
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("MySQL Driver not found", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }

    /**
     * Exposes the underlying JDBC Connection for metadata introspection.
     */
    public Connection getConnection() {
        return this.connection;
    }

    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }

    public ResultSet query(String sql, Object... params) {
        try {
            PreparedStatement ps = prepare(sql, params);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseException("Error executing query: " + sql, e);
        }
    }

    public int update(String sql, Object... params) {
        try (PreparedStatement ps = prepare(sql, params)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error executing update: " + sql, e);
        }
    }

    public long insert(String table, Map<String, Object> data) {
        String cols = String.join(", ", data.keySet());
        String placeholders = String.join(", ", data.keySet().stream().map(k -> "?").toList());
        String sql = "INSERT INTO " + table + " (" + cols + ") VALUES (" + placeholders + ")";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int idx = 1;
            for (Object val : data.values()) {
                ps.setObject(idx++, val);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1L;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting into " + table, e);
        }
    }

    public int update(String table, Map<String, Object> data, String whereClause, Object... whereParams) {
        String setClause = data.keySet().stream()
            .map(col -> col + " = ?")
            .reduce((a,b) -> a + ", " + b)
            .orElse("");
        String sql = "UPDATE " + table + " SET " + setClause + " WHERE " + whereClause;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int idx = 1;
            for (Object val : data.values()) {
                ps.setObject(idx++, val);
            }
            for (Object wp : whereParams) {
                ps.setObject(idx++, wp);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating " + table, e);
        }
    }

    public int delete(String table, String whereClause, Object... whereParams) {
        String sql = "DELETE FROM " + table + " WHERE " + whereClause;
        return update(sql, whereParams);
    }

    public Map<String, Object> findById(String table, String idColumn, Object id) {
        String sql = "SELECT * FROM " + table + " WHERE " + idColumn + " = ?";
        try (ResultSet rs = query(sql, id)) {
            ResultSetMetaData md = rs.getMetaData();
            if (!rs.next()) return null;
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            return row;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching by ID from " + table, e);
        }
    }

    public void update(String rawSql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(rawSql);
        } catch (SQLException e) {
            throw new DatabaseException("Error executing DDL: " + rawSql, e);
        }
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new DatabaseException("Error closing database connection", e);
            }
        }
    }
}
