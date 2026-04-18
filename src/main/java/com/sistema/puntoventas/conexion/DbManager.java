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
                + " imagen TEXT"
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
