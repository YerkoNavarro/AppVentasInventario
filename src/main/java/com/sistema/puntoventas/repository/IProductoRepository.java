package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.Producto;

import java.util.List;

public interface IProductoRepository {
    boolean registrarProducto(Producto producto);
    List<Producto>obtenerProductos();
    List<Producto>obtenerProductoPorNombre(String nombre);
}
