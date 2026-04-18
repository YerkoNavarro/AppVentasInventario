package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.conexion.Conexion.DatabaseConnection;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.repository.IProductoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryImpl implements IProductoRepository {

    private final DatabaseConnection conexion = DatabaseConnection.getInstance();

    @Override
    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio_compra, precio_venta, categoria, fecha_venc, stock_actual, stock_minimo, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connect = conexion.getConnection();
             PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecioCompra());
            ps.setDouble(3, producto.getPrecioVenta());
            ps.setString(4, producto.getCategoria());
            ps.setString(5, producto.getFechaVenc());
            ps.setInt(6, producto.getStockActual());
            ps.setInt(7, producto.getStockMinimo());
            ps.setString(8, producto.getImagen());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> obtenerProductos() {
        return List.of();
    }

    @Override
    public List<Producto> obtenerProductoPorNombre(String nombre) {
        return List.of();
    }

    @Override
    public boolean actualizarProducto(Producto producto) {
        return false;
    }

    @Override
    public boolean eliminarProducto(int id) {
        return false;
    }

    @Override
    public Producto obtenerProductoPorId(int id) {
        return null;
    }

    @Override
    public List<Producto> obtenerStockCritico() {
        return List.of();
    }


}

