package com.sawal.db;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SchemaGenerator {

    private final DatabaseConnector databaseConnector;

    public SchemaGenerator(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Retrieves the database schema: tables and their column names/types.
     *
     * @return a human‑readable representation of all tables and columns
     */
    public String getSchema() {
        StringBuilder sb = new StringBuilder();
        try {
            Connection conn = databaseConnector.getConnection();
            DatabaseMetaData meta = conn.getMetaData();

            // Iterate over all tables in the current schema
            try (ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    sb.append("Table ").append(tableName).append(":\n");

                    // For each table, list its columns and types
                    try (ResultSet columns = meta.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            String colName = columns.getString("COLUMN_NAME");
                            String colType = columns.getString("TYPE_NAME");
                            sb.append("  - ").append(colName)
                              .append(" ").append(colType)
                              .append("\n");
                        }
                    }

                    sb.append("\n");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching database schema", e);
        }

        return sb.toString();
    }
}
