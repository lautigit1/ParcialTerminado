# 🛒 Tienda - Aplicación Java con JDBC, DAO Genérico, Log4j2 y Pool de Conexiones (HikariCP)

Este proyecto es una aplicación de consola desarrollada en Java como parte del práctico de **Laboratorio II** (Tecnicatura Universitaria en Programación, 2024). Permite la gestión completa de productos y categorías almacenados en una base de datos **MySQL**, con funcionalidades CRUD, arquitectura en capas y uso de buenas prácticas profesionales.

---

## ⚙️ Tecnologías utilizadas

- 💻 **Java 17**
- 🛢️ **MySQL 8** (localhost)
- 🔌 **JDBC** (con consultas parametrizadas)
- 📦 **Gradle** como sistema de construcción
- 📊 **Log4j 2** para logging estructurado
- 🚀 **HikariCP** para manejo eficiente de conexiones (pool)

---

## 🧠 Características principales

- CRUD completo para las entidades `Producto` y `Categoria`
- DAO genérico `BaseDAO<T>` con implementación por entidad
- Arquitectura en capas: separación clara entre modelo, DAO, lógica y presentación
- Validaciones de entrada por consola (campos obligatorios, tipo de datos)
- Logging de operaciones (`info`, `warn`, `error`) con consola y archivos rotativos
- Inicialización automática de base de datos y tablas desde el código
- Pool de conexiones configurado con **HikariCP** (alto rendimiento)

---
## 🧪 Funcionalidades implementadas

| Funcionalidad                | Implementado |
|-----------------------------|--------------|
| Crear categorías y productos | ✅           |
| Listar, buscar por ID        | ✅           |
| Actualizar y eliminar        | ✅           |
| Validar campos desde consola | ✅           |
| Relación entre entidades     | ✅ (`producto.categoria_id`) |
| Manejo de errores            | ✅ con logs y mensajes |
| Logging detallado            | ✅ Log4j2 + RollingFile |
| Pool de conexiones           | ✅ HikariCP integrado |
| Inicialización automática    | ✅ BD y tablas |

---
## 🧰 Requisitos

- JDK 17 o superior
- MySQL instalado localmente
- Usuario: `root` – Contraseña: _(vacía por defecto)_
- Puerto por defecto: `3306`
---
## 🚀 Cómo ejecutar el proyecto

1. **Clonar el repositorio**
   git clone https://github.com/lautigit1/ParcialTerminado.git
   cd ParcialTerminado
2. **Compilar y ejecutar con Gradle**
./gradlew run

El programa creará automáticamente:

La base de datos tienda si no existe

Las tablas categorias y productos

Usar el menú interactivo desde consola

📝 Detalles de configuración

Base de datos
El archivo Databaseutil.java utiliza HikariCP para manejar un pool de conexiones:
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/tienda");
config.setUsername("root");
config.setPassword("");
Logging
El archivo log4j2.xml está configurado para:
Mostrar logs en consola
Guardar logs en archivo (logs/app.log)
Rotar archivos de depuración (logs/app-debug-*.log)

📌 Notas adicionales
El código es fácilmente adaptable a otro motor (como H2 o PostgreSQL), cambiando la cadena de conexión en Databaseutil.
Las clases DAO están diseñadas para ser reutilizables y extensibles.
El pool de conexiones optimiza el rendimiento incluso en múltiples operaciones seguidas.

👨‍💻 Autor
Lautaro Salinas
Materia: Programacion 2 Laboratorio 
Carrera: Tecnicatura Universitaria en Programación
Año: 2025
Repositorio: github.com/lautigit1/ParcialTerminado
