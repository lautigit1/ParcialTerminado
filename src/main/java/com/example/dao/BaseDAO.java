package com.example.dao;

import com.example.util.Databaseutil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseDAO<T> implements RepositorioDAO<T> {

    private static final Logger logger = Logger.getLogger(BaseDAO.class.getName());

    protected abstract String obtenerSQLInsertar();
    protected abstract void configurarParametrosInsertar(PreparedStatement stmt, T entidad) throws SQLException;

    protected abstract String obtenerSQLActualizar();
    protected abstract void configurarParametrosActualizar(PreparedStatement stmt, T entidad) throws SQLException;

    protected abstract String obtenerNombreTabla();
    protected abstract T mapearDesdeResultSet(ResultSet rs) throws SQLException;

    @Override
    public int crear(T entidad) {
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(obtenerSQLInsertar(), Statement.RETURN_GENERATED_KEYS)) {
            configurarParametrosInsertar(stmt, entidad);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al crear entidad: " + entidad, e);
        }
        return -1;
    }

    @Override
    public T buscarPorId(int id) {
        String sql = "SELECT * FROM " + obtenerNombreTabla() + " WHERE id = ?";
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar entidad ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<T> listarTodos() {
        List<T> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + obtenerNombreTabla();
        try (Connection conn = Databaseutil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearDesdeResultSet(rs));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar entidades", e);
        }
        return lista;
    }

    @Override
    public boolean actualizar(T entidad) {
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(obtenerSQLActualizar())) {
            configurarParametrosActualizar(stmt, entidad);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar entidad: " + entidad, e);
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + obtenerNombreTabla() + " WHERE id = ?";
        try (Connection conn = Databaseutil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar entidad ID: " + id, e);
        }
        return false;
    }
}
