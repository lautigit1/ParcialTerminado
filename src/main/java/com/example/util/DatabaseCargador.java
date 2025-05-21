package com.example.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseCargador {

    public static void insertarCategoriasPredeterminadas() {
        String[] categorias = {"Electrónica", "Ropa", "Alimentos", "Libros"};
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection conn = com.example.util.Databaseutil.getConnection()) {
            for (String nombre : categorias) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nombre);
                    stmt.executeUpdate();
                }
            }
            System.out.println("Categorías predeterminadas insertadas.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
