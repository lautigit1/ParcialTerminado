package com.example.dao;

import com.example.model.Producto;
import com.example.util.Databaseutil;
import com.example.util.Databaseutil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    // CREATE
    @Override
    public int crear(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";
        try (
                Connection conn = Databaseutil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // READ - Buscar por ID
    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (
                Connection conn = Databaseutil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - Listar todos
    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (
                Connection conn = Databaseutil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    // UPDATE
    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE id = ?";
        try (
                Connection conn = Databaseutil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (
                Connection conn = Databaseutil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}