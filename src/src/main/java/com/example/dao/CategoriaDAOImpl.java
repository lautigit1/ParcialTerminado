package com.example.dao;

import com.example.model.Categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDAOImpl extends BaseDAO<Categoria> {

    @Override
    protected String obtenerSQLInsertar() {
        return "INSERT INTO categorias (nombre) VALUES (?)";
    }

    @Override
    protected void configurarParametrosInsertar(PreparedStatement stmt, Categoria categoria) throws SQLException {
        stmt.setString(1, categoria.getNombre());
    }

    @Override
    protected String obtenerSQLActualizar() {
        return "UPDATE categorias SET nombre = ? WHERE id = ?";
    }

    @Override
    protected void configurarParametrosActualizar(PreparedStatement stmt, Categoria categoria) throws SQLException {
        stmt.setString(1, categoria.getNombre());
        stmt.setInt(2, categoria.getId());
    }

    @Override
    protected String obtenerNombreTabla() {
        return "categorias";
    }

    @Override
    protected Categoria mapearDesdeResultSet(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id"),
                rs.getString("nombre")
        );
    }
}

