package com.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Databaseutil {

    private static final String DB_NAME = "tienda";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static HikariDataSource dataSource;

    static {
        crearBaseSiNoExiste();
        inicializarPool();
    }

    private static void crearBaseSiNoExiste() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Error al crear la base de datos '" + DB_NAME + "'");
            e.printStackTrace();
        }
    }

    private static void inicializarPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setPoolName("TiendaPool");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS categorias (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL UNIQUE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS productos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    descripcion TEXT,
                    precio DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL,
                    categoria_id INT,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
                        ON DELETE SET NULL ON UPDATE CASCADE
                )
            """);

            System.out.println("✅ Tablas creadas correctamente en la base '" + DB_NAME + "'.");

        } catch (SQLException e) {
            System.err.println("❌ Error al crear las tablas.");
            e.printStackTrace();
        }
    }
}
