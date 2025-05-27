# Tienda - Proyecto Java con JDBC, Log4j2 y HikariCP

Este es un proyecto de consola en Java que desarrollé para el práctico de Laboratorio II. Utiliza JDBC para conectarse a una base de datos MySQL y permite gestionar productos y categorías con operaciones CRUD completas. También implementé Log4j2 para el manejo de logs, HikariCP para el pool de conexiones y seguí una arquitectura en capas con DAO genérico.

## Tecnologías utilizadas

- Java 17
- MySQL 8
- JDBC
- Gradle
- Log4j 2
- HikariCP 

## Estructura del proyecto

- `model/`: clases de dominio (`Producto`, `Categoria`)
- `dao/`: interfaces e implementaciones DAO (`BaseDAO`, `CategoriaDAO`, `ProductoDAO`)
- `util/`: clase de conexión a base de datos (`Databaseutil`)
- `Main.java`: menú principal e interacción con el usuario

## Funcionalidades

- Crear, listar, buscar por ID, actualizar y eliminar productos y categorías
- Validaciones de entrada desde consola (por ejemplo, evitar campos vacíos o precios negativos)
- Relación entre productos y categorías (clave foránea)
- Logs en consola y archivos (`app.log`, `app-debug.log`) con niveles `info`, `warn` y `error`
- Uso de HikariCP para manejo eficiente de conexiones

## Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/lautigit1/ParcialTerminado.git
   cd ParcialTerminado
