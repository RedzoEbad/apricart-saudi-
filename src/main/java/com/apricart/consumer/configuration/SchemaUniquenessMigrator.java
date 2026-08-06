package com.apricart.consumer.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Hibernate ddl-auto=update does not drop obsolete unique constraints.
 * Subcategories used to be globally unique by name; they are now unique per category.
 * This migrator drops the old single-column unique constraints so same names can exist
 * under different categories.
 */
@Component
public class SchemaUniquenessMigrator implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaUniquenessMigrator.class);

    private final JdbcTemplate jdbcTemplate;

    public SchemaUniquenessMigrator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        dropSingleColumnUniques("sub_category", "name");
        dropSingleColumnUniques("sub_category", "arabic_name");
        ensureCompositeUnique("sub_category", "uk_subcategory_category_name", "category_id", "name");
        ensureCompositeUnique("sub_category", "uk_subcategory_category_arabic_name", "category_id", "arabic_name");
        ensureCompositeUnique("product", "uk_product_subcategory_title", "sub_category_id", "title");
        ensureCompositeUnique("product", "uk_product_subcategory_arabic_title", "sub_category_id", "arabic_title");
    }

    private void dropSingleColumnUniques(String table, String column) {
        try {
            List<Map<String, Object>> constraints = jdbcTemplate.queryForList(
                    "SELECT c.conname AS name " +
                            "FROM pg_constraint c " +
                            "JOIN pg_class t ON c.conrelid = t.oid " +
                            "WHERE t.relname = ? AND c.contype = 'u' " +
                            "AND array_length(c.conkey, 1) = 1 " +
                            "AND (SELECT a.attname FROM pg_attribute a " +
                            "     WHERE a.attrelid = t.oid AND a.attnum = c.conkey[1]) = ?",
                    table, column);

            for (Map<String, Object> row : constraints) {
                String name = String.valueOf(row.get("name"));
                LOGGER.info("Dropping obsolete unique constraint {}.{}", table, name);
                jdbcTemplate.execute("ALTER TABLE " + table + " DROP CONSTRAINT IF EXISTS " + name);
            }

            // Also drop unique indexes that are not constraints (Hibernate sometimes creates these)
            List<Map<String, Object>> indexes = jdbcTemplate.queryForList(
                    "SELECT i.relname AS name " +
                            "FROM pg_index ix " +
                            "JOIN pg_class t ON t.oid = ix.indrelid " +
                            "JOIN pg_class i ON i.oid = ix.indexrelid " +
                            "JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY(ix.indkey) " +
                            "WHERE t.relname = ? AND ix.indisunique = true AND ix.indisprimary = false " +
                            "AND array_length(ix.indkey, 1) = 1 AND a.attname = ?",
                    table, column);

            for (Map<String, Object> row : indexes) {
                String name = String.valueOf(row.get("name"));
                LOGGER.info("Dropping obsolete unique index {}.{}", table, name);
                jdbcTemplate.execute("DROP INDEX IF EXISTS " + name);
            }
        } catch (Exception e) {
            LOGGER.warn("Could not drop single-column unique on {}.{}: {}", table, column, e.getMessage());
        }
    }

    private void ensureCompositeUnique(String table, String constraintName, String col1, String col2) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM pg_constraint WHERE conname = ?",
                    Integer.class, constraintName);
            if (count != null && count > 0) {
                return;
            }
            LOGGER.info("Creating unique constraint {} on {}.({}, {})", constraintName, table, col1, col2);
            jdbcTemplate.execute(
                    "ALTER TABLE " + table + " ADD CONSTRAINT " + constraintName +
                            " UNIQUE (" + col1 + ", " + col2 + ")");
        } catch (Exception e) {
            LOGGER.warn("Could not create unique constraint {} on {}: {}. " +
                            "Clean duplicate rows if needed, then restart.",
                    constraintName, table, e.getMessage());
        }
    }
}
