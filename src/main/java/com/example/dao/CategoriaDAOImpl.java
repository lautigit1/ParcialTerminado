package com.example.dao;

import com.example.model.Categoria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

public class CategoriaDAOImpl extends BaseDAO<Categoria> implements CategoriaDAO {

    private static final Logger logger = LogManager.getLogger(CategoriaDAOImpl.class);

    @Override
    protected String obtenerSQLInsertar() {
        return "INSERT INTO categorias (nombre) VALUES (?)";
    }

    @Override
    protected void configurarParametrosInsertar(PreparedStatement stmt, Categoria c) throws SQLException {
        stmt.setString(1, c.getNombre());
    }

    @Override
    protected String obtenerSQLActualizar() {
        return "UPDATE categorias SET nombre = ? WHERE id = ?";
    }

    @Override
    protected void configurarParametrosActualizar(PreparedStatement stmt, Categoria c) throws SQLException {
        stmt.setString(1, c.getNombre());
        stmt.setInt(2, c.getId());
    }

    @Override
    protected String obtenerNombreTabla() {
        return "categorias";
    }

    @Override
    protected Categoria mapearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Categoria(rs.getInt("id"), rs.getString("nombre"));
    }

    @Override
    public List<Categoria> listarTodas() {
        return super.listarTodos();
    }
}
