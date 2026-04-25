package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.repository.IDetalleVenta;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sistema.puntoventas.modelo.detalleVenta;


public class DetalleVentaImpl implements IDetalleVenta {

    String url = "jdbc:sqlite:DBventasInventario.db";
    
    public List<detalleVenta> obtenerDetalleVentasporIdVenta(int id) {  //id de la venta
        List<detalleVenta> listaDetalleVenta = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta WHERE idVenta = ?";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    detalleVenta detalleVenta = new detalleVenta();
                    detalleVenta.setIdDetalle(rs.getInt("idDetalle"));
                    detalleVenta.setIdVenta(rs.getInt("idVenta"));
                    detalleVenta.setIdProducto(rs.getInt("idProducto"));
                    System.out.println("detalleventa encontrado correctamente");
                    listaDetalleVenta.add(detalleVenta);
                    
                }
            }
            
            }catch (Exception e) {
                System.err.println("Error al obtener detalle venta: " + e.getMessage());
                
            }
        return listaDetalleVenta;
    }

    public List<String> obtenerInfoVentaDetalle(int id) { //idVenta

        List<String> listaInfoDetalleVenta = new ArrayList<>();

        String infoDetalleVenta = "";
        String sql = "SELECT dv.idDetalle, dv.idProducto, dv.idVenta, v.fechaHora, v.totalVenta, p.nombre, p.precioVenta " +
                     "FROM detalle_venta dv " +
                     "JOIN venta v ON dv.idVenta = v.idVenta " +
                     "JOIN producto p ON p.id = dv.idProducto " +
                     "WHERE v.idVenta = ?";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { // Changed from if to while to handle multiple results if the query was changed
                    infoDetalleVenta = "ID Detalle: " + rs.getInt("idDetalle") +
                                       ", ID Venta: " + rs.getInt("idVenta") +
                                       ", ID Producto: " + rs.getInt("idProducto") +
                                       ", Nombre Producto: " + rs.getString("nombre") +
                                       ", Precio Venta: " + rs.getDouble("precioVenta") + // Added precioVenta
                                       ", Fecha Venta: " + rs.getString("fechaHora") + 
                                       ", Total Venta: " + rs.getDouble("totalVenta"); 
                    listaInfoDetalleVenta.add(infoDetalleVenta);
                }
            }
           
            }catch (Exception e) {
                System.err.println("Error al obtener información del detalle de venta: " + e.getMessage());
                
            }
        return listaInfoDetalleVenta;

    }




}
