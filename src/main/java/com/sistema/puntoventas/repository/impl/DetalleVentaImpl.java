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
        detalleVenta detalleVenta = new detalleVenta();
        String sql = "SELECT * FROM detalle_venta WHERE idVenta = ?";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    detalleVenta.setIdDetalle(rs.getInt("id"));
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

    public String obtenerInfoVentaDetalle(int id) { //idDetalleVenta

        String infoDetalleVenta = "";
        String sql = "SELECT detalle_venta.*, venta.fechaVenta, venta.totalVenta FROM detalle_venta JOIN venta ON detalle_venta.idVenta = venta.id";
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    infoDetalleVenta = "ID Detalle: " + rs.getInt("id") +
                                       ", ID Venta: " + rs.getInt("idVenta") +
                                       ", ID Producto: " + rs.getInt("idProducto") +
                                       ", Fecha Venta: " + rs.getString("fechaVenta") +
                                       ", Total Venta: " + rs.getDouble("totalVenta");
                }
            }
           
            }catch (Exception e) {
                System.err.println("Error al obtener información del detalle de venta: " + e.getMessage());
                
            }
        return infoDetalleVenta;

    }




}
