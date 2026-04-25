package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasRepositoryImpl implements IEstadisticas {
    private static final String url = "jdbc:sqlite:DBventasInventario.db";

    @Override
    public int obtenerIngresosTotales(String periodo) {
        if (periodo == null || periodo.trim().isEmpty()) {
            return 0;
        }

        String sql = "SELECT COALESCE(SUM(totalVenta), 0) AS total "
                + "FROM venta "
                + "WHERE fechaHora LIKE ? AND estado = 1"
                +"ORDER BY fechaHora DESC" ;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + periodo.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return (int) Math.round(rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ingresos totales: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public List<RankingProductosDTO> obtenerRankingProductos(int limite) {
        List<RankingProductosDTO> ranking = new ArrayList<>();

        String sql = "SELECT " +
                "p.nombre AS nombreProducto, " +
                "SUM(d.cantidad) AS totalCantidadVendida " +
                "FROM detalle_venta d  " +
                "INNER JOIN venta v ON d.idVenta = v.idVenta  " +
                "INNER JOIN producto p ON d.idProducto = p.id  " +
                "WHERE v.estado = 1  " +
                "GROUP BY p.id, p.nombre  " +
                "ORDER BY totalCantidadVendida DESC  " +
                "LIMIT ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setInt(1, limite);


            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    String nombre = rs.getString("nombreProducto");
                    int cantidad = rs.getInt("totalCantidadVendida");

                    // Construimos el DTO y lo agregamos a la lista
                    RankingProductosDTO productoDTO = new RankingProductosDTO(nombre, cantidad);
                    ranking.add(productoDTO);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking de productos: " + e.getMessage());
        }

        return ranking;
    }

    @Override
    public int obtenerVentasUsuario(int idUsuario) {
        return 0;
    }

    @Override
    public int prepararDatosParaIA() {
        return 0;
    }
}
