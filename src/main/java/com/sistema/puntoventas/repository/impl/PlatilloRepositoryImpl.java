package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.moduloProducto.DetallePlatillo;
import com.sistema.puntoventas.modelo.moduloProducto.Platillo;
import com.sistema.puntoventas.modelo.moduloProducto.TipoProducto;
import com.sistema.puntoventas.repository.moduloProductos.IPlatilloRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatilloRepositoryImpl implements IPlatilloRepository {

    private static final String url = "jdbc:sqlite:DBventasInventario.db";

    @Override
    public boolean registrarPlatillo(Platillo platillo) {
        String sqlPlatillo = "INSERT INTO platillo (nombre, precio, idCategoria, estado, costoProduccion, stockActual,tipoPlatillo) VALUES (?, ?, ?, ?, ?, ?,?)";
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
                stmtPlatillo.setString(7, "PLATILLO");

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
        String sql = "SELECT * FROM platillo WHERE estado = 1 ORDER BY id ASC"; // Solo platillos activos
        try(Connection conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Platillo platillo = new Platillo();
                platillo.setId(rs.getInt("id"));
                platillo.setNombre(rs.getString("nombre"));
                platillo.setPrecio(rs.getDouble("precio"));
                platillo.setTipoProducto(TipoProducto.PLATILLO);
                platillo.setCategoria(null);
                platillo.setCostoProduccion(rs.getDouble("costoProduccion"));
                platillo.setStockActual(rs.getInt("stockActual"));
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
        String sqlUpdatePlatillo = "UPDATE platillo SET nombre = ?, precio = ?, idCategoria = ?, estado = ?, costoProduccion = ?, stockActual = ?, tipoPlatillo = ? WHERE id = ?";
        String sqlDeleteDetalle = "DELETE FROM detallePlatillo WHERE idPlatillo = ?";
        String sqlInsertDetalle = "INSERT INTO detallePlatillo (idPlatillo, idProducto, cantidadIngrediente) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmtPlatillo = conn.prepareStatement(sqlUpdatePlatillo)) {
                stmtPlatillo.setString(1, platillo.getNombre());
                stmtPlatillo.setDouble(2, platillo.getPrecio());
                stmtPlatillo.setInt(3, platillo.getCategoria().getId());
                stmtPlatillo.setBoolean(4, platillo.isEstado());
                stmtPlatillo.setDouble(5, platillo.getCostoProduccion());
                stmtPlatillo.setInt(6, platillo.getStockActual());
                stmtPlatillo.setString(7, "PLATILLO");
                stmtPlatillo.setInt(8, platillo.getId());

                int affectedRows = stmtPlatillo.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No se pudo actualizar el platillo, ID no encontrado.");
                }
            }

            try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDeleteDetalle)) {
                stmtDelete.setInt(1, platillo.getId());
                stmtDelete.executeUpdate();
            }

            if (platillo.getIngrediente() != null && !platillo.getIngrediente().isEmpty()) {
                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlInsertDetalle)) {
                    for (DetallePlatillo item : platillo.getIngrediente()) {
                        stmtDetalle.setInt(1, platillo.getId());
                        stmtDetalle.setInt(2, item.getProducto().getId());
                        stmtDetalle.setDouble(3, item.getCantidadIngrediente());
                        stmtDetalle.addBatch();
                    }
                    stmtDetalle.executeBatch();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar platillo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarPlatillo(int id) {
        String sqlDeleteDetalle = "DELETE FROM detallePlatillo WHERE idPlatillo = ?";
        String sqlDeletePlatillo = "DELETE FROM platillo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDeleteDetalle)) {
                stmtDetalle.setInt(1, id);
                stmtDetalle.executeUpdate();
            }

            try (PreparedStatement stmtPlatillo = conn.prepareStatement(sqlDeletePlatillo)) {
                stmtPlatillo.setInt(1, id);
                int affectedRows = stmtPlatillo.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No se pudo eliminar el platillo, ID no encontrado.");
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar platillo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean desactivarPlatillo(int id) {
        String sql = "UPDATE platillo SET estado = 0 WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al desactivar platillo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existeNombre(String nombre, int id) {
        String sql = "SELECT COUNT(*) FROM platillo WHERE nombre = ? AND id != ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setInt(2, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Si el conteo es mayor a 0, el nombre ya existe
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de nombre: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Platillo obtenerPlatilloPorId(int id) {
        String sql = "SELECT * FROM platillo WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Platillo platillo = new Platillo();
                    platillo.setId(rs.getInt("id"));
                    platillo.setNombre(rs.getString("nombre"));
                    platillo.setPrecio(rs.getDouble("precio"));
                    // Aquí podrías cargar la categoría y los ingredientes si lo deseas
                    return platillo;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener platillo por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean estaAsociadoVenta(int id) {
        String sql = "SELECT COUNT(*) FROM detalleVenta WHERE idPlatillo = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Si el conteo es mayor a 0, el platillo está asociado a una venta
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar asociación con ventas: " + e.getMessage());
        }
        return false;
    }
}
