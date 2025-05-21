package com.example.dao;

import java.util.List;

public interface RepositorioDAO<T> {
    int crear(T t);
    T buscarPorId(int id);
    List<T> listarTodos();
    boolean actualizar(T t);
    boolean eliminar(int id);
}
