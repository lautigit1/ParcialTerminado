package com.example;

import com.example.dao.CategoriaDAOImpl;
import com.example.dao.ProductoDAOImpl;
import com.example.model.Categoria;
import com.example.model.Producto;
import com.example.util.Databaseutil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Databaseutil.initDatabase();
        Scanner scanner = new Scanner(System.in);
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();
        logger.info("Aplicación iniciada.");

        while (true) {
            System.out.println("\nMENÚ PRINCIPAL");
            System.out.println("1. Productos");
            System.out.println("2. Categorías");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String opt = scanner.nextLine();
            try {
                switch (opt) {
                    case "1" -> menuProductos(scanner, productoDAO, categoriaDAO);
                    case "2" -> menuCategorias(scanner, categoriaDAO);
                    case "0" -> {
                        logger.info("¡Cerraste la aplicacion con exito!.");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                logger.error("Error general en menú principal", e);
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    private static void menuProductos(Scanner s, ProductoDAOImpl pDAO, CategoriaDAOImpl cDAO) {
        while (true) {
            System.out.println("\nGESTIÓN DE PRODUCTOS");
            System.out.println("1. Crear");
            System.out.println("2. Listar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String opt = s.nextLine();
            try {
                if ("0".equals(opt)) return;
                switch (opt) {
                    case "1" -> {
                        System.out.print("Nombre: ");
                        String nom = s.nextLine().trim();
                        if (nom.isEmpty()) {
                            System.out.println("Nombre no puede estar vacío.");
                            break;
                        }

                        System.out.print("Descripción: ");
                        String desc = s.nextLine().trim();

                        double pre;
                        while (true) {
                            try {
                                System.out.print("Precio: ");
                                pre = Double.parseDouble(s.nextLine());
                                if (pre <= 0) {
                                    System.out.println("Debe ser mayor a cero.");
                                } else break;
                            } catch (NumberFormatException e) {
                                System.out.println("Número inválido.");
                            }
                        }

                        int sto;
                        while (true) {
                            try {
                                System.out.print("Stock: ");
                                sto = validarInt(s.nextLine());
                                if (sto < 0) {
                                    System.out.println("No puede ser negativo.");
                                } else break;
                            } catch (NumberFormatException e) {
                                System.out.println("Stock inválido.");
                            }
                        }

                        listarCategoriasSilencioso(cDAO);
                        System.out.print("ID Categoría (0 si ninguna): ");
                        int catId = validarInt(s.nextLine());
                        if (catId != 0 && cDAO.buscarPorId(catId) == null) {
                            System.out.println("Categoría no existe.");
                            break;
                        }

                        Producto nuevo = new Producto(nom, desc, pre, sto, catId);
                        int idGen = pDAO.crear(nuevo);
                        System.out.println(idGen > 0 ? "Creado. ID: " + idGen : "Error al crear.");
                    }
                    case "2" -> {
                        List<Producto> productos = pDAO.listarTodos();
                        if (productos.isEmpty()) System.out.println("No hay productos.");
                        else productos.forEach(System.out::println);
                    }
                    case "3" -> {
                        System.out.print("ID producto: ");
                        int id = validarInt(s.nextLine());
                        Producto p = pDAO.buscarPorId(id);
                        System.out.println(p != null ? p : "No encontrado.");
                    }
                    case "4" -> {
                        System.out.print("ID a actualizar: ");
                        int id = validarInt(s.nextLine());
                        Producto p = pDAO.buscarPorId(id);
                        if (p == null) {
                            System.out.println("Producto no existe.");
                            break;
                        }
                        System.out.print("Nuevo nombre (" + p.getNombre() + "): ");
                        String nuevoNom = s.nextLine().trim();
                        if (!nuevoNom.isEmpty()) p.setNombre(nuevoNom);

                        System.out.print("Nueva descripción (" + p.getDescripcion() + "): ");
                        String nuevaDesc = s.nextLine().trim();
                        if (!nuevaDesc.isEmpty()) p.setDescripcion(nuevaDesc);

                        System.out.print("Nuevo precio (" + p.getPrecio() + "): ");
                        String nuevoPre = s.nextLine();
                        if (!nuevoPre.isEmpty()) {
                            try {
                                double precio = Double.parseDouble(nuevoPre);
                                if (precio > 0) p.setPrecio(precio);
                            } catch (NumberFormatException ignored) {}
                        }

                        System.out.print("Nuevo stock (" + p.getStock() + "): ");
                        String nuevoStock = s.nextLine();
                        if (!nuevoStock.isEmpty()) {
                            try {
                                int stock = validarInt(nuevoStock);
                                if (stock >= 0) p.setStock(stock);
                            } catch (NumberFormatException ignored) {}
                        }

                        listarCategoriasSilencioso(cDAO);
                        System.out.print("Nueva categoría (" + p.getCategoriaId() + "): ");
                        String nuevaCat = s.nextLine();
                        if (!nuevaCat.isEmpty()) {
                            int catId = validarInt(nuevaCat);
                            if (catId == 0 || cDAO.buscarPorId(catId) != null) {
                                p.setCategoriaId(catId);
                            }
                        }

                        System.out.println(pDAO.actualizar(p) ? "Actualizado." : "Error al actualizar.");
                    }
                    case "5" -> {
                        System.out.print("ID a eliminar: ");
                        int id = validarInt(s.nextLine());
                        Producto p = pDAO.buscarPorId(id);
                        if (p == null) {
                            System.out.println("No existe.");
                            break;
                        }
                        System.out.print("¿Eliminar '" + p.getNombre() + "'? (s/N): ");
                        if (s.nextLine().equalsIgnoreCase("s")) {
                            System.out.println(pDAO.eliminar(id) ? "Eliminado." : "Error al eliminar.");
                        }
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                logger.error("Error en productos", e);
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuCategorias(Scanner s, CategoriaDAOImpl cDAO) {
        while (true) {
            System.out.println("\nGESTIÓN DE CATEGORÍAS");
            System.out.println("1. Crear");
            System.out.println("2. Listar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String opt = s.nextLine();
            try {
                if ("0".equals(opt)) return;
                switch (opt) {
                    case "1" -> {
                        System.out.print("Nombre: ");
                        String nom = s.nextLine().trim();
                        if (nom.isEmpty()) {
                            System.out.println("No puede estar vacío.");
                            break;
                        }
                        int id = cDAO.crear(new Categoria(nom));
                        System.out.println(id > 0 ? "Creada. ID: " + id : "Error al crear.");
                    }
                    case "2" -> {
                        List<Categoria> cats = cDAO.listarTodas();
                        if (cats.isEmpty()) System.out.println("No hay categorías.");
                        else cats.forEach(System.out::println);
                    }
                    case "3" -> {
                        System.out.print("ID: ");
                        int id = validarInt(s.nextLine());
                        Categoria c = cDAO.buscarPorId(id);
                        System.out.println(c != null ? c : "No encontrada.");
                    }
                    case "4" -> {
                        System.out.print("ID a actualizar: ");
                        int id = validarInt(s.nextLine());
                        Categoria c = cDAO.buscarPorId(id);
                        if (c == null) {
                            System.out.println("No existe.");
                            break;
                        }
                        System.out.print("Nuevo nombre (" + c.getNombre() + "): ");
                        String nuevoNom = s.nextLine().trim();
                        if (!nuevoNom.isEmpty()) {
                            c.setNombre(nuevoNom);
                            System.out.println(cDAO.actualizar(c) ? "Actualizada." : "Error al actualizar.");
                        }
                    }
                    case "5" -> {
                        System.out.print("ID a eliminar: ");
                        int id = validarInt(s.nextLine());
                        Categoria c = cDAO.buscarPorId(id);
                        if (c == null) {
                            System.out.println("No existe.");
                            break;
                        }
                        System.out.print("¿Eliminar '" + c.getNombre() + "'? (s/N): ");
                        if (s.nextLine().equalsIgnoreCase("s")) {
                            System.out.println(cDAO.eliminar(id) ? "Eliminada." : "Error al eliminar.");
                        }
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                logger.error("Error en categorías", e);
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listarCategoriasSilencioso(CategoriaDAOImpl dao) {
        List<Categoria> cats = dao.listarTodas();
        if (cats.isEmpty()) System.out.println("  (No hay categorías)");
        else cats.forEach(c -> System.out.println("  ID: " + c.getId() + " - " + c.getNombre()));
    }

    private static int validarInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Debe ser número entero.");
        }
    }
}
