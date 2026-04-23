package com.sistema.puntoventas.conexion;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    // URL de conexión consistente con el resto de los repositorios
    private final String url = "jdbc:sqlite:DBventasInventario.db";

    public void conectarBD() {
        try (var conn = DriverManager.getConnection(this.url)) {
            System.out.println("Conexión a SQLite establecida correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }

    public void crearTablaUsuario() {
        // Se utiliza 'password' para evitar errores de caracteres especiales como la 'ñ'
        String sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nombre TEXT NOT NULL,"
                + " apellido TEXT NOT NULL,"
                + " rut TEXT UNIQUE NOT NULL,"
                + " password TEXT NOT NULL,"
                + " telefono TEXT,"
                + " rol TEXT,"
                + " estado INTEGER" // Manejado como 0 o 1 en SQLite[cite: 2]
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'usuario' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla usuario: " + e.getMessage());
        }
    }

    public void crearTablaProductos() {
        String sql = "CREATE TABLE IF NOT EXISTS producto ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nombre TEXT NOT NULL,"
                + " precioCompra REAL,"
                + " precioVenta REAL,"
                + " categoria TEXT,"
                + " fechaVenc TEXT,"
                + " stockActual INTEGER,"
                + " stockMinimo INTEGER,"
                + " imagen TEXT,"
                + " unidadMedida TEXT,"
                + "tipoProducto TEXT"
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'producto' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla productos: " + e.getMessage());
        }
    }

    public void creartablaVentas() {
        String sql = "CREATE TABLE IF NOT EXISTS venta ("
                + " idVenta INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " fechaHora TEXT NOT NULL,"
                + " idUsuario INTEGER,"
                + " totalVenta REAL,"
                + " FOREIGN KEY (idUsuario) REFERENCES usuario(id)"
                + ");";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'venta' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla ventas: " + e.getMessage());
        }
    }

    public void crearTablaDetalleVenta() {
        String sql = "CREATE TABLE IF NOT EXISTS detalle_venta ("
                + " idDetalle INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " idVenta INTEGER,"
                + " idProducto INTEGER,"
                + " cantidad INTEGER NOT NULL,"
                + " precioUnitario REAL NOT NULL,"
                + " FOREIGN KEY (idVenta) REFERENCES venta(idVenta) ON DELETE CASCADE,"
                + " FOREIGN KEY (idProducto) REFERENCES producto(id)"
                + ");";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'detalle_venta' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla detalle_venta: " + e.getMessage());
        }
    }

    public void crearTodasLasTablas() {
        crearTablaUsuario();
        creartablaVentas();
        crearTablaDetalleVenta();
        crearTablaProductos();
        System.out.println("Inicialización de todas las tablas completada.");
    }

    public void crearUsuarioAdmin() {
        // El nombre de la columna debe ser 'password' para coincidir con la tabla
        String sql = "INSERT OR IGNORE INTO usuario (nombre, apellido, rut, password, telefono, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Admin");
            pstmt.setString(2, "Admin");
            pstmt.setString(3, "12345678-9"); // Tu RUT de acceso
            pstmt.setString(4, "admin");      // Tu contraseña de acceso
            pstmt.setString(5, "99999999");
            pstmt.setString(6, "ADMIN");
            pstmt.setInt(7, 1); // Estado activo
            pstmt.executeUpdate();
            System.out.println("Usuario administrador verificado/creado.");
        } catch (SQLException e) {
            System.err.println("Error al crear admin: " + e.getMessage());
        }
    }
}