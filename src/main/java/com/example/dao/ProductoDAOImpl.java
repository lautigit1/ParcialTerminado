package com.example.dao;

import com.example.model.Producto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.sql.*;

public class ProductoDAOImpl extends BaseDAO<Producto> implements ProductoDAO {

    private static final Logger logger = LogManager.getLogger(ProductoDAOImpl.class);

    @Override
    protected String obtenerSQLInsertar() {
        return "INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void configurarParametrosInsertar(PreparedStatement stmt, Producto p) throws SQLException {
        stmt.setString(1, p.getNombre());
        stmt.setString(2, p.getDescripcion());
        stmt.setDouble(3, p.getPrecio());
        stmt.setInt(4, p.getStock());
        if (p.getCategoriaId() > 0)
            stmt.setInt(5, p.getCategoriaId());
        else
            stmt.setNull(5, Types.INTEGER);
    }

    @Override
    protected String obtenerSQLActualizar() {
        return "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria_id = ? WHERE id = ?";
    }

    @Override
    protected void configurarParametrosActualizar(PreparedStatement stmt, Producto p) throws SQLException {
        configurarParametrosInsertar(stmt, p);
        stmt.setInt(6, p.getId());
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
                rs.getInt("categoria_id")
        );
    }
}
