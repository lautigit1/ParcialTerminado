package com.example.util;

import java.sql.*;

public class Databaseutil {
    private static final String DB_URL_SERVER = "jdbc:mysql://localhost:3306/"; // URL del servidor MySQL
    private static final String DB_USER = "root"; // Usuario de la base de datos
    private static final String DB_PASSWORD = ""; // Contraseña del usuario
    private static final String DB_NAME = "tienda"; // Nombre de la base de datos

    // Obtener conexión a la base de datos específica (tienda)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL_SERVER + DB_NAME, DB_USER, DB_PASSWORD);
    }

    public static void initDatabase() {
        // Paso 1: Conectarse al servidor MySQL para crear la base de datos si no existe
        try (Connection serverConn = DriverManager.getConnection(DB_URL_SERVER, DB_USER, DB_PASSWORD);
             Statement stmt = serverConn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            // System.out.println("Base de datos '" + DB_NAME + "' asegurada/creada."); // Log opcional

        } catch (SQLException e) {
            System.err.println("Error al crear/asegurar la base de datos '" + DB_NAME + "':");
            e.printStackTrace();
            // Considerar si se debe detener la aplicación si la BD no puede ser creada
            return;
        }

        // Paso 2: Conectarse a la base de datos 'tienda' para crear las tablas
        try (Connection connTienda = getConnection(); // Usa el método getConnection() que ya apunta a DB_NAME
             Statement stmtTienda = connTienda.createStatement()) {

            String sqlCategorias = "CREATE TABLE IF NOT EXISTS categorias (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL UNIQUE)"; // Nombre de categoría debería ser único

            String sqlProductos = "CREATE TABLE IF NOT EXISTS productos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "descripcion TEXT, " +
                    "precio DECIMAL(10,2) NOT NULL, " +
                    "stock INT NOT NULL, " +
                    "categoria_id INT, " + // Columna para la clave foránea
                    "FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL ON UPDATE CASCADE)";
            // ON DELETE SET NULL: si se borra una categoría, los productos asociados tendrán categoria_id = NULL
            // ON UPDATE CASCADE: si se actualiza el id de una categoría, se actualiza en productos

            stmtTienda.executeUpdate(sqlCategorias);
            stmtTienda.executeUpdate(sqlProductos);

            System.out.println("Tablas 'categorias' y 'productos' inicializadas/aseguradas correctamente en la base de datos '" + DB_NAME + "'.");

            // Opcional: Cargar datos predeterminados si es necesario (ej. con DatabaseCargador)
            // DatabaseCargador.insertarCategoriasPredeterminadasSiVacia(connTienda); // Podrías pasar la conexión

        } catch (SQLException e) {
            System.err.println("Error al inicializar las tablas en la base de datos '" + DB_NAME + "':");
            e.printStackTrace();
        }
    }

    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar recurso:");
                    e.printStackTrace(); // Considerar loggear esto con tu logger preferido
                }
            }
        }
    }
}