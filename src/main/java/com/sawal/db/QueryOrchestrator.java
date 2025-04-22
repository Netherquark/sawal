package com.sawal.db;

import com.sawal.ai.SpringAIProcessor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Component
public class QueryOrchestrator {

    private final SchemaGenerator schemaGen;
    private final SpringAIProcessor aiProcessor;
    private final DatabaseConnector dbConnector;

    public QueryOrchestrator(SchemaGenerator schemaGen,
                             SpringAIProcessor aiProcessor,
                             DatabaseConnector dbConnector) {
        this.schemaGen = schemaGen;
        this.aiProcessor = aiProcessor;
        this.dbConnector = dbConnector;
    }

    /**
     * End‑to‑end: fetch schema, generate SQL via LLM, run it, and return console‑ready output.
     */
    public String handle(String naturalLanguageQuery) {
        // 1) Schema
        String schema = schemaGen.getSchema();

        // 2) Prompt + LLM
        String enriched = "Database schema:\n" + schema + "\n\n" + naturalLanguageQuery;
        String fencedSql = aiProcessor.processQuery(enriched);

        // 3) Extract raw SQL
        String sql = extractSql(fencedSql);

        // 4) Execute and collect output
        try {
            if (isSelect(sql)) {
                return executeSelect(sql);
            } else {
                int rows = dbConnector.update(sql, new Object[]{});
                return "Rows affected: " + rows;
            }
        } catch (SQLException e) {
            return "Execution error: " + e.getMessage();
        }
    }

    private boolean isSelect(String sql) {
        return sql.trim().toLowerCase().startsWith("select");
    }

    private String extractSql(String fenced) {
        return fenced
            .replaceAll("(?is)^```sql\\s*", "")
            .replaceAll("(?is)```$", "")
            .trim();
    }

    private String executeSelect(String sql) throws SQLException {
        StringBuilder out = new StringBuilder();
        try (ResultSet rs = dbConnector.query(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            // Header
            for (int i = 1; i <= cols; i++) {
                out.append(md.getColumnLabel(i));
                if (i < cols) out.append(" | ");
            }
            out.append("\n").append("-".repeat(4 * cols)).append("\n");

            // Rows
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    Object v = rs.getObject(i);
                    out.append(v == null ? "NULL" : v.toString());
                    if (i < cols) out.append(" | ");
                }
                out.append("\n");
            }
        }
        return out.toString();
    }
}
