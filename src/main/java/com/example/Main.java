package com.example;

import com.example.dao.CategoriaDAOImpl;
import com.example.dao.ProductoDAOImpl;
import com.example.model.Categoria;
import com.example.model.Producto;
import com.example.util.Databaseutil; // Solo una importación
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        Databaseutil.initDatabase();
        Scanner scanner = new Scanner(System.in);
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();

        logger.info("Aplicación iniciada");

        while (true) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestión de Productos");
            System.out.println("2. Gestión de Categorías");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String opcionPrincipal = scanner.nextLine();

            try {
                switch (opcionPrincipal) {
                    case "1":
                        // Pasar categoriaDAO para validaciones de categoría en productos
                        menuProductos(scanner, productoDAO, categoriaDAO);
                        break;
                    case "2":
                        menuCategorias(scanner, categoriaDAO);
                        break;
                    case "0":
                        System.out.println("Saliendo...");
                        logger.info("Aplicación finalizada.");
                        scanner.close(); // Cerrar Scanner
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                // Usar logger para excepciones inesperadas en el flujo principal
                logger.error("Error inesperado en el menú principal: " + e.getMessage(), e);
                System.out.println("Ocurrió un error inesperado. Revise los logs.");
            }
        }
    }
    private static void menuProductos(Scanner scanner, ProductoDAOImpl productoDAO, CategoriaDAOImpl categoriaDAO) {
        while (true) {
            System.out.println("\n--- MENÚ PRODUCTOS ---");
            System.out.println("1. Crear producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        crearProducto(scanner, productoDAO, categoriaDAO);
                        break;
                    case "2":
                        listarProductos(productoDAO);
                        break;
                    case "3":
                        buscarProductoPorId(scanner, productoDAO);
                        break;
                    case "4":
                        actualizarProducto(scanner, productoDAO, categoriaDAO);
                        break;
                    case "5":
                        eliminarProducto(scanner, productoDAO);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                logger.error("Error en menú productos: " + e.getMessage(), e);
                System.out.println("Ocurrió un error en la gestión de productos: " + e.getMessage());
            }
        }
    }
    private static void crearProducto(Scanner scanner, ProductoDAOImpl productoDAO, CategoriaDAOImpl categoriaDAO) {
        System.out.println("\n--- CREAR NUEVO PRODUCTO ---");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();
        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre del producto no puede estar vacío.");
            return;
        }
        System.out.print("Descripción del producto: ");
        String descripcion = scanner.nextLine(); // Puede ser vacía
        double precio = -1;
        while (precio < 0) {
            System.out.print("Precio del producto: ");
            String precioStr = scanner.nextLine();
            try {
                precio = Double.parseDouble(precioStr);
                if (precio < 0) {
                    System.out.println("El precio no puede ser negativo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato de precio inválido. Debe ingresar un número (ej. 123.45).");
                precio = -1;
            }
        }
        int stock = -1;
        while (stock < 0) {
            System.out.print("Stock inicial del producto: ");
            try {
                stock = validarInt(scanner.nextLine());
                if (stock < 0) {
                    System.out.println("El stock no puede ser negativo.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                stock = -1;
            }
        }
        int categoriaId = 0;
        boolean categoriaValida = false;
        while(!categoriaValida){
            System.out.println("Categorías disponibles:");
            listarCategoriasSilencioso(categoriaDAO);
            System.out.print("ID de la categoría del producto (o 0 si no tiene): ");
            String catIdStr = scanner.nextLine();
            try {
                categoriaId = Integer.parseInt(catIdStr);
                if (categoriaId == 0) { // Permite no asignar categoría
                    categoriaValida = true;
                } else if (categoriaId > 0) {
                    Categoria cat = categoriaDAO.buscarPorId(categoriaId);
                    if (cat != null) {
                        categoriaValida = true;
                    } else {
                        System.out.println("Categoría con ID " + categoriaId + " no encontrada. Intente de nuevo.");
                    }
                } else {
                    System.out.println("ID de categoría inválido. Debe ser un número positivo o 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de categoría inválido. Debe ser un número.");
            }
        }
        Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock, categoriaId);
        int idGenerado = productoDAO.crear(nuevoProducto);

        if (idGenerado > 0) {
            System.out.println("Producto creado exitosamente con ID: " + idGenerado);
            logger.info("Producto creado: ID {}, Nombre {}", idGenerado, nuevoProducto.getNombre());
        } else {
            System.out.println("Error al crear el producto.");
            logger.error("Falló la creación del producto: {}", nuevoProducto.getNombre());
        }
    }
    private static void listarProductos(ProductoDAOImpl productoDAO) {
        List<Producto> productos = productoDAO.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            System.out.println("\n--- LISTA DE PRODUCTOS ---");
            productos.forEach(System.out::println);
        }
        logger.info("Listado de productos mostrado. Cantidad: {}", productos.size());
    }
    private static void buscarProductoPorId(Scanner scanner, ProductoDAOImpl productoDAO) {
        System.out.print("ID del producto a buscar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Producto encontrado = productoDAO.buscarPorId(id);
        if (encontrado != null) {
            System.out.println("Producto encontrado:");
            System.out.println(encontrado);
        } else {
            System.out.println("No existe el producto con ID: " + id);
            logger.warn("Intento fallido de buscar producto ID: {}", id);
        }
    }
    private static void actualizarProducto(Scanner scanner, ProductoDAOImpl productoDAO, CategoriaDAOImpl categoriaDAO) {
        System.out.print("ID del producto a actualizar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Producto actual = productoDAO.buscarPorId(id);
        if (actual == null) {
            System.out.println("No existe el producto con ID: " + id);
            logger.warn("Intento de actualizar producto no existente ID: {}", id);
            return;
        }
        System.out.println("Actualizando producto: " + actual); // Mostrar datos actuales
        System.out.print("Nuevo nombre ('" + actual.getNombre() + "') (dejar vacío para no cambiar): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.trim().isEmpty()) {
            actual.setNombre(nuevoNombre);
        }
        System.out.print("Nueva descripción ('" + actual.getDescripcion() + "') (dejar vacío para no cambiar): ");
        String nuevaDescripcion = scanner.nextLine();
        // El trim() aquí es importante si una descripción vacía significa "no cambiar"
        // Si una descripción vacía es válida, entonces no uses trim() para la condición.
        if (!nuevaDescripcion.isEmpty() || (nuevaDescripcion.isEmpty() && !actual.getDescripcion().isEmpty())) { // permite borrar desc
            actual.setDescripcion(nuevaDescripcion);
        }
        System.out.print("Nuevo precio (" + actual.getPrecio() + ") (dejar vacío para no cambiar): ");
        String nuevoPrecioStr = scanner.nextLine();
        if (!nuevoPrecioStr.trim().isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                if (nuevoPrecio >= 0) {
                    actual.setPrecio(nuevoPrecio);
                } else {
                    System.out.println("El precio no puede ser negativo. No se actualizó el precio.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato de precio inválido. No se actualizó el precio.");
            }
        }
        System.out.print("Nuevo stock (" + actual.getStock() + ") (dejar vacío para no cambiar): ");
        String nuevoStockStr = scanner.nextLine();
        if (!nuevoStockStr.trim().isEmpty()) {
            try {
                int nuevoStock = validarInt(nuevoStockStr);
                if (nuevoStock >= 0) {
                    actual.setStock(nuevoStock);
                } else {
                    System.out.println("El stock no puede ser negativo. No se actualizó el stock.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + ". No se actualizó el stock.");
            }
        }
        System.out.println("Categorías disponibles:");
        listarCategoriasSilencioso(categoriaDAO);
        System.out.print("Nuevo ID de categoría (" + actual.getCategoriaId() + ") (dejar vacío para no cambiar, 0 para ninguna): ");
        String nuevaCatIdStr = scanner.nextLine();
        if (!nuevaCatIdStr.trim().isEmpty()) {
            try {
                int nuevaCategoriaId = Integer.parseInt(nuevaCatIdStr);
                if (nuevaCategoriaId == 0) {
                    actual.setCategoriaId(0); // Asignar "sin categoría"
                } else if (nuevaCategoriaId > 0) {
                    Categoria cat = categoriaDAO.buscarPorId(nuevaCategoriaId);
                    if (cat != null) {
                        actual.setCategoriaId(nuevaCategoriaId);
                    } else {
                        System.out.println("Categoría con ID " + nuevaCategoriaId + " no encontrada. No se actualizó la categoría.");
                    }
                } else {
                    System.out.println("ID de categoría inválido. No se actualizó la categoría.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de categoría inválido. No se actualizó la categoría.");
            }
        }
        if (productoDAO.actualizar(actual)) {
            System.out.println("Producto actualizado correctamente.");
            logger.info("Producto actualizado ID: {}", id);
        } else {
            System.out.println("Error al actualizar el producto o no se realizaron cambios.");
            logger.error("Fallo al actualizar producto ID: {} (o no hubo cambios)", id);
        }
    }
    private static void eliminarProducto(Scanner scanner, ProductoDAOImpl productoDAO) {
        System.out.print("ID del producto a eliminar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Producto productoAEliminar = productoDAO.buscarPorId(id);
        if (productoAEliminar == null) {
            System.out.println("No se encontró producto con ID: " + id);
            return;
        }
        System.out.print("¿Está seguro de que desea eliminar el producto '" + productoAEliminar.getNombre() + "' (ID: " + id + ")? (s/N): ");
        String confirmacion = scanner.nextLine();
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        if (productoDAO.eliminar(id)) {
            System.out.println("Producto eliminado correctamente.");
            logger.warn("Producto eliminado ID: {}", id);
        } else {
            System.out.println("Error al eliminar el producto.");
            logger.error("Fallo al eliminar producto ID: {}", id);
        }
    }
    private static void menuCategorias(Scanner scanner, CategoriaDAOImpl categoriaDAO) {
        // (Sin cambios mayores, igual al que ya tenías, pero puedes revisar si quieres añadir logging más detallado o manejo de errores)
        while (true) {
            System.out.println("\n--- MENÚ CATEGORÍAS ---");
            System.out.println("1. Crear categoría");
            System.out.println("2. Listar categorías");
            System.out.println("3. Buscar categoría por ID");
            System.out.println("4. Actualizar categoría");
            System.out.println("5. Eliminar categoría");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        crearCategoria(scanner, categoriaDAO);
                        break;
                    case "2":
                        listarCategorias(categoriaDAO);
                        break;
                    case "3":
                        buscarCategoriaPorId(scanner, categoriaDAO);
                        break;
                    case "4":
                        actualizarCategoria(scanner, categoriaDAO);
                        break;
                    case "5":
                        eliminarCategoria(scanner, categoriaDAO);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                logger.error("Error en menú categorías: " + e.getMessage(), e);
                System.out.println("Ocurrió un error en la gestión de categorías: " + e.getMessage());
            }
        }
    }
    private static void crearCategoria(Scanner scanner, CategoriaDAOImpl categoriaDAO) {
        System.out.print("Nombre de la categoría: ");
        String nombre = scanner.nextLine();
        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre de la categoría no puede estar vacío.");
            return;
        }
        Categoria nueva = new Categoria(nombre); // Asume constructor Categoria(String nombre)
        int id = categoriaDAO.crear(nueva); // Asume que crear devuelve el ID
        if (id > 0) {
            System.out.println("Categoría creada con ID: " + id);
            logger.info("Categoría creada: ID {}, Nombre {}", id, nueva.getNombre());
        } else {
            System.out.println("Error al crear categoría (posiblemente ya existe un nombre igual si es UNIQUE).");
            logger.error("Falló la creación de la categoría: {}", nueva.getNombre());
        }
    }
    private static void listarCategorias(CategoriaDAOImpl categoriaDAO) {
        List<Categoria> categorias = categoriaDAO.listarTodos();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías registradas.");
        } else {
            System.out.println("\n--- LISTA DE CATEGORÍAS ---");
            categorias.forEach(System.out::println);
        }
        logger.info("Listado de categorías mostrado. Cantidad: {}", categorias.size());
    }
    private static void listarCategoriasSilencioso(CategoriaDAOImpl categoriaDAO) {
        List<Categoria> categorias = categoriaDAO.listarTodos();
        if (categorias.isEmpty()) {
            System.out.println("(No hay categorías registradas para asignar)");
        } else {
            categorias.forEach(cat -> System.out.println("  ID: " + cat.getId() + " - Nombre: " + cat.getNombre()));
        }
    }
    private static void buscarCategoriaPorId(Scanner scanner, CategoriaDAOImpl categoriaDAO) {
        System.out.print("ID de la categoría a buscar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Categoria encontrada = categoriaDAO.buscarPorId(id);
        if (encontrada != null) {
            System.out.println(encontrada);
            logger.info("Categoría encontrada: ID {}, Nombre {}", encontrada.getId(), encontrada.getNombre());
        } else {
            System.out.println("No existe la categoría con ID: " + id);
            logger.warn("Intento fallido de buscar categoría ID: {}", id);
        }
    }
    private static void actualizarCategoria(Scanner scanner, CategoriaDAOImpl categoriaDAO) {
        System.out.print("ID de la categoría a actualizar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Categoria actual = categoriaDAO.buscarPorId(id);
        if (actual == null) {
            System.out.println("No existe la categoría con ID: " + id);
            logger.warn("Intento de actualizar categoría no existente ID: {}", id);
            return;
        }
        System.out.println("Categoría encontrada: " + actual);
        System.out.print("Nuevo nombre ('" + actual.getNombre() + "') (dejar vacío para no cambiar): ");
        String nuevoNombre = scanner.nextLine();

        boolean cambioRealizado = false;
        if (!nuevoNombre.trim().isEmpty() && !nuevoNombre.trim().equals(actual.getNombre())) {
            actual.setNombre(nuevoNombre.trim());
            cambioRealizado = true;
        } else if (nuevoNombre.trim().isEmpty()){
            System.out.println("El nombre no se cambió.");
        } else {
            System.out.println("El nuevo nombre es igual al actual. No se realizaron cambios.");
        }
        if (cambioRealizado) {
            if (categoriaDAO.actualizar(actual)) {
                System.out.println("Categoría actualizada correctamente.");
                logger.info("Categoría actualizada ID: {}", id);
            } else {
                System.out.println("Error al actualizar la categoría (quizás el nuevo nombre ya existe).");
                logger.error("Fallo al actualizar categoría ID: {}", id);
            }
        }
    }
    private static void eliminarCategoria(Scanner scanner, CategoriaDAOImpl categoriaDAO) {
        System.out.print("ID de la categoría a eliminar: ");
        int id;
        try {
            id = validarInt(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Categoria categoriaAEliminar = categoriaDAO.buscarPorId(id);
        if (categoriaAEliminar == null) {
            System.out.println("No se encontró categoría con ID: " + id);
            return;
        }
        System.out.print("¿Está seguro de que desea eliminar la categoría '" + categoriaAEliminar.getNombre() + "' (ID: " + id + ")? (s/N): ");
        String confirmacion = scanner.nextLine();
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }
        if (categoriaDAO.eliminar(id)) {
            System.out.println("Categoría eliminada correctamente.");
            logger.warn("Categoría eliminada ID: {}", id);
        } else {
            System.out.println("Error al eliminar la categoría. Verifique si tiene productos asociados y la configuración de la clave foránea.");
            logger.error("Fallo al eliminar categoría ID: {}. Puede tener productos asociados.", id);
        }
    }
    private static int validarInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entrada inválida. Debe ingresar un número entero válido.");
        }
    }
}