package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.Producto;

import java.util.List;

public interface IProductoRepository {
    boolean registrarProducto(Producto producto);
    List<Producto>obtenerProductos();
    List<Producto>obtenerProductoPorNombre(String nombre);
    boolean actualizarProducto(Producto producto);
    boolean eliminarProducto(int id);
    Producto obtenerProductoPorId(int id);
    List<Producto>obtenerStockCritico();
}
