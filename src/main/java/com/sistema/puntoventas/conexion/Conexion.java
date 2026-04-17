package com.sistema.puntoventas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Conexion {


    /**
     * Provee conexiones individuales a SQLite.
     * Cada llamada a getConnection() retorna una conexión nueva
     * para ser usada con try-with-resources (se cierra automáticamente).
     */
    public static class DatabaseConnection {

        private static final Logger LOGGER =
                Logger.getLogger(DatabaseConnection.class.getName());

        private static final String DB_URL = "jdbc:sqlite:app_database.db";

        private static DatabaseConnection instance;

        private DatabaseConnection() {
        }

        public static synchronized DatabaseConnection getInstance() {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
            return instance;
        }

        /**
         * Retorna una conexión NUEVA cada vez.
         * Debe usarse dentro de try-with-resources para cerrarse sola.
         */
        public Connection getConnection() throws SQLException {

            return DriverManager.getConnection(DB_URL);

        }
    }
}
