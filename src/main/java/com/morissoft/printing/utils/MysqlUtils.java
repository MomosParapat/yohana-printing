package com.morissoft.printing.utils;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class MysqlUtils {

    public static List<String> getTables(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList("SELECT table_name FROM  information_schema.tables WHERE table_schema='yohana_printing'", String.class);
    }

//    public static List<String> getSchemas(JdbcTemplate jdbcTemplate) {
//        return jdbcTemplate.queryForList("SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT IN ('pg_catalog', 'information_schema', 'public', 'lendwise') AND nspname NOT LIKE 'pg_toast%' AND nspname NOT LIKE 'pg_temp%'", String.class);
//    }
}
