package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Databaseutil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tienda";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Cambiá si usás contraseña

    /**
     * Obtiene una conexión a la base de datos MySQL.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Cierra recursos de forma segura.
     */
    public static void closeResources(Connection conn, Statement stmt) {
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Inicializa la conexión (no crea tablas en MySQL).
     */
    public static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            System.out.println("✅ Conexión con MySQL establecida correctamente");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con MySQL");
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }
    }
}
