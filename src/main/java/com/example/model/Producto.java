package com.example.model;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int categoriaId; // Campo para la relación con Categoria

    public Producto() {}

    // Constructor para crear un nuevo producto antes de guardarlo (sin ID)
    public Producto(String nombre, String descripcion, double precio, int stock, int categoriaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    // Constructor para instanciar un producto desde la base de datos (con ID)
    public Producto(int id, String nombre, String descripcion, double precio, int stock, int categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', desc='%s', precio=%.2f, stock=%d, categoriaId=%d}",
                id, nombre, descripcion, precio, stock, categoriaId);
    }
}