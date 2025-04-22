package com.sawal.db;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class SchemaGenerator {

    private final DatabaseConnector databaseConnector;

    public SchemaGenerator(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Retrieves schema and data for the 'library' database.
     *
     * @return human-readable schema and table contents
     */
    public String getSchema() {
        StringBuilder sb = new StringBuilder();
        String catalog = "library";  // Target database

        try {
            Connection conn = databaseConnector.getConnection();
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet tables = meta.getTables(catalog, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    sb.append("Table ").append(tableName).append(":\n");

                    // Columns
                    try (ResultSet columns = meta.getColumns(catalog, null, tableName, "%")) {
                        while (columns.next()) {
                            String colName = columns.getString("COLUMN_NAME");
                            String colType = columns.getString("TYPE_NAME");
                            sb.append("  - ").append(colName).append(" ").append(colType).append("\n");
                        }
                    }

                    // Rows
                    sb.append("Contents:\n");
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT * FROM " + catalog + "." + tableName)) {

                        ResultSetMetaData rsMeta = rs.getMetaData();
                        int colCount = rsMeta.getColumnCount();

                        // Header
                        for (int i = 1; i <= colCount; i++) {
                            sb.append(rsMeta.getColumnLabel(i));
                            if (i < colCount) sb.append(" | ");
                        }
                        sb.append("\n").append("-".repeat(4 * colCount)).append("\n");

                        // Data rows
                        while (rs.next()) {
                            for (int i = 1; i <= colCount; i++) {
                                Object val = rs.getObject(i);
                                sb.append(val != null ? val.toString() : "NULL");
                                if (i < colCount) sb.append(" | ");
                            }
                            sb.append("\n");
                        }
                    }

                    sb.append("\n");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schema and data for 'library' database", e);
        }

        return sb.toString();
    }
}
