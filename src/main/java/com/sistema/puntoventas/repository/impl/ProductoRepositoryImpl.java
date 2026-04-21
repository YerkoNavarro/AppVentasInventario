package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.conexion.Conexion.DatabaseConnection;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.UnidadMedida;
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
        String sql = "INSERT INTO productos (nombre, precio_compra, precio_venta, categoria, fecha_venc, stock_actual, stock_minimo, imagen,unidadMedida) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
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
            ps.setString(9, producto.getUnidadMedida().name());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }



    @Override
    public List<Producto> obtenerProductos( ) {
        List<Producto> listaProductos = new ArrayList<>();

        String sql = "SELECT * FROM productos ORDER BY nombre ASC";
        try(Connection connect = conexion.getConnection();
        PreparedStatement ps  = connect.prepareStatement(sql);
        ResultSet rs =  ps.executeQuery()){
            // Recorremos los resultados fila por fila
            while (rs.next()) {
                Producto producto = new Producto();

                // Extraemos la información de la base de datos y la metemos en el objeto
                producto.setId(rs.getInt(1));
                producto.setNombre(rs.getString(2));
                producto.setPrecioCompra(rs.getDouble(3));
                producto.setPrecioVenta(rs.getDouble(4));
                producto.setCategoria(rs.getString(5));
                producto.setFechaVenc(rs.getString(6));
                producto.setStockActual(rs.getInt(7));
                producto.setStockMinimo(rs.getInt(8));
                producto.setImagen(rs.getString(9));
                producto.setUnidadMedida(UnidadMedida.valueOf(rs.getString(10).toUpperCase()));
                // Agregamos el producto armado a nuestra lista
                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());

        }
        return listaProductos;
    }

    @Override
    public List<Producto> obtenerProductoPorNombre(String nombre) {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try(Connection connect = conexion.getConnection();
            PreparedStatement ps  = connect.prepareStatement(sql)){
            ps.setString(1,"%" + nombre + "%");
            try(ResultSet rs =  ps.executeQuery()){
                // Recorremos los resultados fila por fila
                while (rs.next()) {
                    Producto producto = new Producto();

                    // Extraemos la información de la base de datos y la metemos en el objeto
                    producto.setId(rs.getInt(1));
                    producto.setNombre(rs.getString(2));
                    producto.setPrecioCompra(rs.getDouble(3));
                    producto.setPrecioVenta(rs.getDouble(4));
                    producto.setCategoria(rs.getString(5));
                    producto.setFechaVenc(rs.getString(6));
                    producto.setStockActual(rs.getInt(7));
                    producto.setStockMinimo(rs.getInt(8));
                    producto.setImagen(rs.getString(9));
                    producto.setUnidadMedida(UnidadMedida.valueOf(rs.getString(10)));

                    // Agregamos el producto armado a nuestra lista
                    listaProductos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());

        }
        return listaProductos;
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

