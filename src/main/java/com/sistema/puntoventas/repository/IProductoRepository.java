package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.Categoria;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.TipoProducto;

import java.util.List;

public interface IProductoRepository {
    boolean registrarProducto(Producto producto);
    List<Producto>obtenerProductos();
    List<Producto>obtenerProductoPorNombre(String nombre);
    boolean actualizarProducto(Producto producto);
    boolean eliminarProducto(int id);
    boolean desactivarProducto(int id);
    Producto obtenerProductoPorId(int id);
    List<Producto> buscarPorTipoProducto(TipoProducto tipoProducto);
    boolean estaAsociadoVentaOPlatillo(int id);


}
