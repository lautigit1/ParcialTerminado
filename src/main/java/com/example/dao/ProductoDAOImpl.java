package com.example.dao;

import com.example.model.Producto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAOImpl extends BaseDAO<Producto> implements ProductoDAO { // Implementa ProductoDAO

    private static final Logger logger = LogManager.getLogger(ProductoDAOImpl.class);

    @Override
    protected String obtenerSQLInsertar() {
        // Incluye categoria_id en la inserción
        return "INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void configurarParametrosInsertar(PreparedStatement stmt, Producto producto) throws SQLException {
        stmt.setString(1, producto.getNombre());
        stmt.setString(2, producto.getDescripcion());
        stmt.setDouble(3, producto.getPrecio());
        stmt.setInt(4, producto.getStock());
        if (producto.getCategoriaId() > 0) { // Asumiendo que un ID de categoría válido es > 0
            stmt.setInt(5, producto.getCategoriaId());
        } else {
            stmt.setNull(5, java.sql.Types.INTEGER); // Permitir nulo si no hay categoría
        }
    }

    @Override
    protected String obtenerSQLActualizar() {
        // Incluye categoria_id en la actualización
        return "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria_id = ? WHERE id = ?";
    }

    @Override
    protected void configurarParametrosActualizar(PreparedStatement stmt, Producto producto) throws SQLException {
        stmt.setString(1, producto.getNombre());
        stmt.setString(2, producto.getDescripcion());
        stmt.setDouble(3, producto.getPrecio());
        stmt.setInt(4, producto.getStock());
        if (producto.getCategoriaId() > 0) {
            stmt.setInt(5, producto.getCategoriaId());
        } else {
            stmt.setNull(5, java.sql.Types.INTEGER);
        }
        stmt.setInt(6, producto.getId()); // El ID del producto va al final
    }

    @Override
    protected String obtenerNombreTabla() {
        return "productos";
    }

    @Override
    protected Producto mapearDesdeResultSet(ResultSet rs) throws SQLException {
        // Mapea categoria_id desde el ResultSet
        return new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getInt("categoria_id") // Obtener el categoria_id
        );
    }

    // Los overrides para logging son opcionales si BaseDAO ya loggea,
    // pero si quieres logs específicos de ProductoDAOImpl, están bien.
    @Override
    public int crear(Producto producto) {
        logger.info("Intentando crear producto: {}", producto.getNombre());
        int idCreado = super.crear(producto);
        if (idCreado > 0) {
            logger.info("Producto creado con ID: {}", idCreado);
        } else {
            logger.error("Fallo al crear producto: {}", producto.getNombre());
        }
        return idCreado;
    }

    @Override
    public boolean actualizar(Producto producto) {
        logger.info("Intentando actualizar producto con ID: {}", producto.getId());
        boolean actualizado = super.actualizar(producto);
        if (actualizado) {
            logger.info("Producto actualizado con ID: {}", producto.getId());
        } else {
            logger.error("Fallo al actualizar producto con ID: {}", producto.getId());
        }
        return actualizado;
    }

    @Override
    public boolean eliminar(int id) {
        // Podrías buscar el producto primero para loggear su nombre si lo deseas
        logger.warn("Intentando eliminar producto con ID: {}", id);
        boolean eliminado = super.eliminar(id);
        if (eliminado) {
            logger.info("Producto eliminado con ID: {}", id);
        } else {
            logger.error("Fallo al eliminar producto con ID: {}", id);
        }
        return eliminado;
    }
}