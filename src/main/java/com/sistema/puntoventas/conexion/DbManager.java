package com.sistema.puntoventas.conexion;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {


    public static void conectarBD() {
        String url = "jdbc:sqlite:DBventasInventario";

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
