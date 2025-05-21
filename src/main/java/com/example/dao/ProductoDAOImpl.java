package com.example.dao;

import com.example.model.Producto;
import com.example.util.Databaseutil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDAOImpl extends BaseDAO<Producto> {

    private static final Logger logger = Logger.getLogger(ProductoDAOImpl.class.getName());

    @Override
    protected String obtenerSQLInsertar() {
        return "INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void configurarParametrosInsertar(PreparedStatement stmt, Producto producto) throws SQLException {
        stmt.setString(1, producto.getNombre());
        stmt.setString(2, producto.getDescripcion());
        stmt.setDouble(3, producto.getPrecio());
        stmt.setInt(4, producto.getStock());
        stmt.setInt(5, producto.getCategoriaId());
    }

    @Override
    protected String obtenerSQLActualizar() {
        return "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria_id = ? WHERE id = ?";
    }

    @Override
    protected void configurarParametrosActualizar(PreparedStatement stmt, Producto producto) throws SQLException {
        stmt.setString(1, producto.getNombre());
        stmt.setString(2, producto.getDescripcion());
        stmt.setDouble(3, producto.getPrecio());
        stmt.setInt(4, producto.getStock());
        stmt.setInt(5, producto.getCategoriaId());
        stmt.setInt(6, producto.getId());
    }

    @Override
    protected String obtenerNombreTabla() {
        return "productos";
    }

    @Override
    protected Producto mapearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getInt("categoria_id"),
                rs.getDate("fecha_creacion")
        );
    }

    // Consulta avanzada: buscar por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultados.add(mapearDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar productos por nombre: " + nombre, e);
        }
        return resultados;
    }

    // Consulta avanzada: contar productos por categoría
    public int contarPorCategoria(int categoriaId) {
        String sql = "SELECT COUNT(*) FROM productos WHERE categoria_id = ?";
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoriaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al contar productos por categoría", e);
        }
        return 0;
    }
}
