package com.sistema.puntoventas.conexion;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    String url = "jdbc:sqlite:DBventasInventario.db";

    public void conectarBD() {
        try (var conn = DriverManager.getConnection(this.url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearTablaProductos(){
        // SQL statement for creating a new table
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
                + " unidadMedida TEXT"
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Tabla productos creada correctamente");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearTablaUsuario(){

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nombre TEXT NOT NULL,"
                + " apellido TEXT NOT NULL,"
                + " rut TEXT UNIQUE NOT NULL,"
                + " contrasena TEXT NOT NULL,"
                + " telefono TEXT,"
                + " rol TEXT,"
                + " estado INTEGER" // En SQLite, los booleanos se manejan habitualmente como 0 o 1
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void creartablaVentas(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS venta ("
                + " idVenta INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " fechaHora TEXT NOT NULL,"
                + " idUsuario INTEGER,"
                + " totalVenta REAL,"
                + " estado INTEGER,"
                + " FOREIGN KEY (idUsuario) REFERENCES usuario(id)"
                + ");";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void crearTablaDetalleVenta(){ //tabla intermediaria de producto y venta
        // SQL statement for creating a new table
        String sql= "CREATE TABLE IF NOT EXISTS detalle_venta ("
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
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void crearTodasLasTablas(){
        crearTablaUsuario();
        creartablaVentas();
        crearTablaDetalleVenta();
        crearTablaProductos();
        System.out.println("Tablas creadas correctamente");
    }
}
