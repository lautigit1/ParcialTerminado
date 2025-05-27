package com.example.dao;

import com.example.model.Producto;
import java.util.List;

public interface ProductoDAO {
    int crear(Producto producto);
    Producto buscarPorId(int id);
    List<Producto> listarTodos();
    boolean actualizar(Producto producto);
    boolean eliminar(int id);
}
