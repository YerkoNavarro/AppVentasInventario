package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasRepositoryImpl implements IEstadisticasRepository {
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
        int cantidadVentas = 0;
        String sql = "SELECT COUNT(idVenta) " +
                     "COALESCE(SUM(totalVenta)) AS TotalVentas " +
                    "FROM venta " +
                    "WHERE idUsuario = ? AND estado = 1";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try(ResultSet rs = pstmt.executeQuery()){
                cantidadVentas = rs.getInt("TotalVentas");
            }
        }catch (SQLException e) {
            System.err.println("Error al obtener ventas por usuario: " + e.getMessage());
        }

        return cantidadVentas;
    }

    @Override
    public int prepararDatosParaIA() {
        int filasExportadas = 0;
        String rutaArchivo = "datos_ventas.csv";
        String sql = "SELECT DATE(fechaHora) AS ds," +
                      "SUM(totalVenta) AS y " +
                      "FROM venta " +
                      "WHERE estado = 1 " +
                      "GROUP BY DATE(fechaHora) " +
                      "ORDER BY DATE(fechaHora) ASC";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
             FileWriter fw = new FileWriter(rutaArchivo);
             PrintWriter pw = new PrintWriter(fw)){
            pw.println("ds,y");

            while(rs.next()){
                String fecha = rs.getString("ds");
                double total = rs.getInt("y");
                pw.println(fecha + "," + total);
                filasExportadas++;
            }

            System.out.println("Éxito: Archivo CSV generado con " + filasExportadas + " días de historial.");

        } catch (SQLException e) {
            throw new RuntimeException("Error al preparar datos para IA: " + e.getMessage(), e);
        }catch (Exception e){
            throw new RuntimeException("Error al escribir el archivo CSV: " + e.getMessage(), e);
        }
        return filasExportadas;
    }
}
