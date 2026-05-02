package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.moduloProducto.DetallePlatillo;
import com.sistema.puntoventas.modelo.moduloProducto.Platillo;
import com.sistema.puntoventas.repository.moduloProductos.IPlatilloRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatilloRepositoryImpl implements IPlatilloRepository {

    private static final String url = "jdbc:sqlite:DBventasInventario.db";

    @Override
    public boolean registrarPlatillo(Platillo platillo) {
        String sqlPlatillo = "INSERT INTO platillo (nombre, precio, idCategoria, estado, costoProduccion, stockActual) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detallePlatillo (idPlatillo, idProducto, cantidadIngrediente) VALUES (?, ?, ?)";
        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sqlPlatillo)) {

            try (PreparedStatement stmtPlatillo = conn.prepareStatement(sqlPlatillo, Statement.RETURN_GENERATED_KEYS)) {
                // 3. Seteamos los datos del Platillo (Calculados previamente por el sistema)
                stmtPlatillo.setString(1, platillo.getNombre());
                stmtPlatillo.setDouble(2, platillo.getPrecio());
                stmtPlatillo.setInt(3, platillo.getCategoria().getId());
                stmtPlatillo.setBoolean(4, platillo.isEstado());
                stmtPlatillo.setDouble(5, platillo.getCostoProduccion());
                stmtPlatillo.setInt(6, platillo.getStockActual());

                int affectedRows = stmtPlatillo.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No se pudo crear el platillo.");
                }

                int idPlatilloGenerado;
                try (ResultSet generatedKeys = stmtPlatillo.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPlatilloGenerado = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID del platillo.");
                    }
                }

                // 5. GUARDAR LOS INGREDIENTES: Usamos Batch para mayor eficiencia
                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (DetallePlatillo item : platillo.getIngrediente()) {
                        stmtDetalle.setInt(1, idPlatilloGenerado);
                        stmtDetalle.setInt(2, item.getProducto().getId());
                        stmtDetalle.setDouble(3, item.getCantidadIngrediente());
                        stmtDetalle.addBatch(); // Se agrega a la cola
                    }
                    stmtDetalle.executeBatch(); // Se ejecutan todos los inserts de una sola vez
                }

                conn.commit();
                return true;

            } catch (SQLException e) {

                conn.rollback();
                System.err.println("Error en la transacción: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return false;
        }

    }

    @Override
    public List<Platillo> obtenerPlatillos() {
        List<Platillo> listaPlatillos = new ArrayList<>();
        String sql = "SELECT * FROM platillo WHERE estado = 1 ORDER BY ASC"; // Solo platillos activos
        try(Connection conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Platillo platillo = new Platillo();
                platillo.setId(rs.getInt("id"));
                platillo.setNombre(rs.getString("nombre"));
                platillo.setPrecio(rs.getDouble("precio"));
                // Aquí podrías cargar la categoría y los ingredientes si lo deseas
                listaPlatillos.add(platillo);
                System.out.println("Platillo encontrados: " + listaPlatillos);
            }

        } catch (Exception e) {

            System.err.println("Error al obtener platillos: " + e.getMessage());
        }

        return listaPlatillos;

    }

    @Override
    public List<Platillo> obtenerPlatilloPorNombre(String nombre) {
        List<Platillo> listaPlatillos = new ArrayList<>();
        String sql = "SELECT p.*,c.nombreCategoria FROM platillo p " +
                     "INNER JOIN categoria c ON p.idCategoria = p.id" +
                     " WHERE nombre LIKE ? AND estado = 1 ORDER BY ASC"; // Solo platillos activos
        try(Connection conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Platillo platillo = new Platillo();
                    platillo.setId(rs.getInt("id"));
                    platillo.setNombre(rs.getString("nombre"));
                    platillo.setPrecio(rs.getDouble("precio"));
                    platillo.setCategoria(null);
                    platillo.setCostoProduccion(rs.getDouble("costoProduccion"));
                    platillo.setStockActual(rs.getInt("stockActual"));
                    listaPlatillos.add(platillo);
                    System.out.println("Platillo encontrado por nombre: " + platillo.getNombre());
                }
            }

        } catch (Exception e) {

            System.err.println("Error al obtener platillos por nombre: " + e.getMessage());
        }

        return listaPlatillos;
    }

    @Override
    public boolean actualizarPlatillo(Platillo platillo) {
        return false;
    }

    @Override
    public boolean eliminarPlatillo(int id) {
        return false;
    }

    @Override
    public boolean desactivarPlatillo(int id) {
        return false;
    }

    @Override
    public boolean existeNombre(String nombre, int id) {
        return false;
    }

    @Override
    public Platillo obtenerPlatilloPorId(int id) {
        return null;
    }

    @Override
    public boolean estaAsociadoVenta(int id) {
        return false;
    }
}
