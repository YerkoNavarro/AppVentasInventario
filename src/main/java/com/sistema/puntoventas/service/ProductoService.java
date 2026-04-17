package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.repository.IProductoRepository;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;

public class ProductoService {

    private IProductoRepository productoRepository;
    public ProductoService() {
        this.productoRepository = new ProductoRepositoryImpl();
    }

    public String registrarProducto(Producto producto){
        if (producto == null){
            return "Un producto no puede ser nulo";
        }

        if (producto.getNombre() == null || producto.getNombre().isEmpty()){
            return "El nombre del producto no puede ser nulo o vacío";
        }

        if(producto.getPrecioCompra() <= 0){
            return "El precio de compra debe ser mayor a cero";
        }

        if (productoRepository.obtenerProductoPorNombre(producto.getNombre()) != null) {
            return "El producto ya existe, no se puede registrar";
        }
        return "";
    }
}
