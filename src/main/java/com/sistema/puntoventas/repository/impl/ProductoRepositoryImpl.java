package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.repository.IProductoRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductoRepositoryImpl implements IProductoRepository {

    @Override
    public boolean registrarProducto(Producto producto){
        String sql = "INSERT INTO productos (nombre, precio_compra, precio_venta, categoria, fecha_venc, stock_actual, stock_minimo, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connect = conexion.getConnection();
                preparedstatement ps = connect.prepareStatement(sql)){
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioCompra());
            ps.setDouble(3, producto.getPrecioVenta());
            ps.setString(4, producto.getCategoria());
            ps.setString(5, producto.getFechaVenc());
            ps.setInt(6, producto.getStockActual());
            ps.setInt(7, producto.getStockMinimo());
            ps.setString(8, producto.getImagen());
            int rowsInserted = ps.executeUpdate());
            return rowsInserted > 0;
        } catch (SQLException e){
            system.err.println("Error al registrar producto"+e.getMessage());
            return false;
        }
    }

}
