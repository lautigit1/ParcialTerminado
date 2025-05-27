package com.example.dao;

import com.example.model.Categoria;
import java.util.List;

public interface CategoriaDAO {
    int crear(Categoria categoria);
    Categoria buscarPorId(int id);
    List <Categoria> listarTodas();
    boolean actualizar(Categoria categoria);
    boolean eliminar(int id);
}
