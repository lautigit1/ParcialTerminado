package com.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Databaseutil {

    private static final Logger logger = Logger.getLogger(Databaseutil.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tienda";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10); // opcional
        config.setMinimumIdle(2);      // opcional
        config.setIdleTimeout(30000);  // opcional
        config.setPoolName("TiendaPool");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void initDatabase() {
        // Crear base de datos si no existe
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS tienda");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear base de datos.", e);
        }

        // Crear tablas si no existen
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

            System.out.println("Base de datos inicializada.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear tablas.", e);
        }
    }
}
