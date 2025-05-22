package com.example.util;

import java.sql.*;

public class Databaseutil {
    private static final String DB_URL_SERVER = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_NAME = "tienda";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL_SERVER + DB_NAME, DB_USER, DB_PASSWORD);
    }

    public static void initDatabase() {
        try (Connection serverConn = DriverManager.getConnection(DB_URL_SERVER, DB_USER, DB_PASSWORD);
             Statement stmt = serverConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo crear la base de datos '" + DB_NAME + "'");
            e.printStackTrace();
            return;
        }

        try (Connection connTienda = getConnection();
             Statement stmtTienda = connTienda.createStatement()) {

            String sqlCategorias = """
                CREATE TABLE IF NOT EXISTS categorias (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL UNIQUE
                )
                """;

            String sqlProductos = """
                CREATE TABLE IF NOT EXISTS productos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    descripcion TEXT,
                    precio DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL,
                    categoria_id INT,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE
                )
                """;

            stmtTienda.executeUpdate(sqlCategorias);
            stmtTienda.executeUpdate(sqlProductos);

            System.out.println("Tablas inicializadas correctamente en la base de datos '" + DB_NAME + "'.");

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudieron crear las tablas en '" + DB_NAME + "'");
            e.printStackTrace();
        }
    }

    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable r : resources) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar recurso:");
                    e.printStackTrace();
                }
            }
        }
    }
}
