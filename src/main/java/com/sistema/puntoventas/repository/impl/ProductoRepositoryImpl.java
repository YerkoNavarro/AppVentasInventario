package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.UnidadMedida;
import com.sistema.puntoventas.repository.IProductoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

public class ProductoRepositoryImpl implements IProductoRepository {

    private static final String SQL_INSERT = 
        "INSERT INTO producto (nombre, precioCompra, precioVenta, categoria, " +
        "fechaVenc, stockActual, stockMinimo, imagen, unidadMedida) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String url = "jdbc:sqlite:DBventasInventario.db";


    @Override
    public boolean registrarProducto(Producto producto) {
        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(SQL_INSERT)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioCompra());
            pstmt.setDouble(3, producto.getPrecioVenta());
            pstmt.setString(4, producto.getCategoria());
            pstmt.setString(5, producto.getFechaVenc());
            pstmt.setInt(6, producto.getStockActual());
            pstmt.setInt(7, producto.getStockMinimo());
            pstmt.setString(8, producto.getImagen());
            pstmt.setInt(9, producto.getUnidadMedida());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }



    @Override
    public List<Producto> obtenerProductos( ) {
        List<Producto> listaProductos = new ArrayList<>();

        String sql = "SELECT * FROM producto ORDER BY nombre ASC";
        try(Connection conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)){
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
                producto.setUnidadMedida(rs.getInt(10));
                // Agregamos el producto armado a nuestra lista
                listaProductos.add(producto);
                System.out.println(listaProductos);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());

        }
        return listaProductos;
    }

    @Override
    public List<Producto> obtenerProductoPorNombre(String nombre) {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        try(Connection connect = DriverManager.getConnection(url);
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
                    producto.setUnidadMedida(rs.getInt(10));

                    // Agregamos el producto armado a nuestra lista
                    listaProductos.add(producto);
                    System.out.println(listaProductos);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());

        }
        return listaProductos;
    }

    @Override
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precioCompra = ?, precioVenta = ?, categoria = ?, " +
                "fechaVenc = ?, stockActual = ?, stockMinimo = ?, imagen = ?, unidadMedida = ? WHERE id = ?";    

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioCompra());
            pstmt.setDouble(3, producto.getPrecioVenta());
            pstmt.setString(4, producto.getCategoria());
            pstmt.setString(5, producto.getFechaVenc());
            pstmt.setInt(6, producto.getStockActual());
            pstmt.setInt(7, producto.getStockMinimo());
            pstmt.setString(8, producto.getImagen());
            pstmt.setInt(9, producto.getUnidadMedida());
            pstmt.setInt(10, producto.getId());
            pstmt.executeUpdate();
            System.out.println("producto actualizado correctamente");

            
        return false;
        } catch (SQLException e) {

            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }     
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

