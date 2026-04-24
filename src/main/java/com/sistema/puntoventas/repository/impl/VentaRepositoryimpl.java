
package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.repository.IventaRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaRepositoryimpl implements IventaRepository {

    private static final String url = "jdbc:sqlite:DBventasInventario.db";
 
    @Override
    public Boolean registrarVentaCompleta(venta venta){
        String sql = "INSERT INTO venta (fechaHora, idUsuario, totalVenta, estado) VALUES (?, ?, ?, ?)";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, venta.getFechaHora());
                pstmt.setInt(2, venta.getIdUsuario());
                pstmt.setDouble(3, venta.getTotalVenta());
                pstmt.setInt(4, 1); // 1 significa que la venta está activa
                int rowsInserted = pstmt.executeUpdate();
                System.out.println("Venta registrada correctamente");
                return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarVenta(venta venta) {
        String sql = "UPDATE venta SET fechaHora = ?, idUsuario = ?, totalVenta = ? , estado = ? WHERE idVenta = ?";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venta.getFechaHora());
            pstmt.setInt(2, venta.getIdUsuario());
            pstmt.setDouble(3, venta.getTotalVenta());
            pstmt.setInt(4, 1); // 1 significa que la venta está activa
            pstmt.setInt(5, venta.getIdVenta());
            pstmt.executeUpdate();
            System.out.println("Venta actualizada correctamente");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar venta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public venta obtenerVentaPorId(int id) {
        venta v = new venta();
        String sql = "SELECT * FROM venta WHERE idVenta = ?";
        try (Connection connect = DriverManager.getConnection(url);
             PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    v.setIdVenta(rs.getInt(1));
                    v.setFechaHora(rs.getString(2));
                    v.setIdUsuario(rs.getInt(3));
                    v.setTotalVenta(rs.getDouble(4));
                    if (rs.getInt(5) == 1) {
                        v.setEstado(true);
                    } else {
                        v.setEstado(false);
                    }
                    System.out.println("Venta encontrada correctamente");
                    System.out.println(v.toString());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener venta: " + e.getMessage());
        }
        return v;
    }

    @Override
    public List<venta> obtenerVentas() {
        List<venta> listaVentas = new ArrayList<>();
        String sql = "SELECT * FROM venta";
        try (Connection conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                venta v = new venta();
                v.setIdVenta(rs.getInt(1));
                v.setFechaHora(rs.getString(2));
                v.setIdUsuario(rs.getInt(3));
                v.setTotalVenta(rs.getDouble(4));
                if (rs.getInt(5) == 1) {
                    v.setEstado(true);
                } else {
                    v.setEstado(false);
                }
                listaVentas.add(v);
                System.out.println(listaVentas);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return listaVentas;
    }

    @Override
    public void anularVenta(int id) {
        String sql = "UPDATE venta SET estado = 0 WHERE idVenta = ?";
        
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Venta anulada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al anular venta: " + e.getMessage());
        }
    }

    @Override
    public void activarVenta(int id) {
        String sql = "UPDATE venta SET estado = 1 WHERE idVenta = ?";
        
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Venta activada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al anular venta: " + e.getMessage());
        }
    }
}
