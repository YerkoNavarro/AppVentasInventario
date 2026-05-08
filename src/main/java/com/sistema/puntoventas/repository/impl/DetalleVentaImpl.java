package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.repository.IDetalleVenta;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.modelo.detalleVenta;
import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;


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

    public List<ventaAplicacion> obtenerTodasLasVentas() {  //trae toda la info de ventas y producto asociados
        List<ventaAplicacion> listaVentas  = new ArrayList<>();
        Map<Integer,ventaAplicacion> mapVentas = new LinkedHashMap<>();
        String sql = "SELECT v.idVenta, v.fechaHora, v.totalVenta, v.tipoPago, v.descripcion, p.nombre, p.precioVenta " +
                     "FROM venta v " +
                     "JOIN detalle_venta dv ON v.idVenta = dv.idVenta " +
                     "JOIN producto p ON dv.idProducto = p.id";

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idVenta = rs.getInt("idVenta");
                    
                    
                    
                    if (!mapVentas.containsKey(idVenta)) { //si no contiene el id crea la venta app en el map
                       venta v = new venta();
                       v.setIdVenta(idVenta);
                       v.setFechaHora(rs.getString("fechaHora"));
                       v.setTotalVenta(rs.getDouble("totalVenta"));
                       v.setTipoPago(rs.getString("tipoPago"));
                       v.setDescripcion(rs.getString("descripcion"));
                        
                        ventaAplicacion ventaApp = new ventaAplicacion();
                        ventaApp.setVenta(v);
                        ventaApp.setDetalleVentas(new ArrayList<>());
                        mapVentas.put(idVenta, ventaApp);
                    }
                    Producto p = new Producto();
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecioVenta(rs.getDouble("precioVenta"));
                    
                    mapVentas.get(idVenta).getDetalleVentas().add(p); //busca la venta por id y le agrega el producto 

                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
        }
        listaVentas.addAll(mapVentas.values()); // pasa los valores del map a la lista
        return listaVentas;

    }


    public List<ventaAplicacion> obtenerTodasLasVentasporFecha(String fecha) {  //trae toda la info de ventas y producto asociados
        List<ventaAplicacion> listaVentas  = new ArrayList<>();
        Map<Integer,ventaAplicacion> mapVentas = new LinkedHashMap<>();
        String sql = "SELECT v.idVenta, v.fechaHora, v.totalVenta, v.tipoPago, v.descripcion, p.nombre, p.precioVenta " +
                     "FROM venta v " +
                     "JOIN detalle_venta dv ON v.idVenta = dv.idVenta " +
                     "JOIN producto p ON dv.idProducto = p.id"
                     + " WHERE v.fechaHora LIKE ?";

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            // ASIGNAR EL PARÁMETRO DE LA FECHA
            pstmt.setString(1, fecha);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idVenta = rs.getInt("idVenta");
                    
                    
                    
                    if (!mapVentas.containsKey(idVenta)) { //si no contiene el id crea la venta app en el map
                       venta v = new venta();
                       v.setIdVenta(idVenta);
                       v.setFechaHora(rs.getString("fechaHora"));
                       v.setTotalVenta(rs.getDouble("totalVenta"));
                       v.setTipoPago(rs.getString("tipoPago"));
                       v.setDescripcion(rs.getString("descripcion"));
                        
                        ventaAplicacion ventaApp = new ventaAplicacion();
                        ventaApp.setVenta(v);
                        ventaApp.setDetalleVentas(new ArrayList<>());
                        mapVentas.put(idVenta, ventaApp);
                    }
                    Producto p = new Producto();
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecioVenta(rs.getDouble("precioVenta"));
                    
                    mapVentas.get(idVenta).getDetalleVentas().add(p); //busca la venta por id y le agrega el producto 

                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
        }
        listaVentas.addAll(mapVentas.values()); // pasa los valores del map a la lista
        return listaVentas;

    }

        




}
