package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.repository.IProductoRepository;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;

import java.util.List;

public class ProductoService {

    private IProductoRepository productoRepository;
    public ProductoService() {
        this.productoRepository = new ProductoRepositoryImpl();
    }

    public void registrarProducto(Producto producto) throws Exception{
        if (producto == null){
            throw new Exception("El producto no puede ser nulo");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }

        if(producto.getPrecioCompra() <= 0){
            throw new Exception("El precio de compra debe ser mayor a cero") ;
        }

        List<Producto> nombreproducto = productoRepository.obtenerProductoPorNombre(producto.getNombre().trim());
        if (!nombreproducto.isEmpty()) {
            for (Producto p : nombreproducto) {
                if (p.getNombre().equalsIgnoreCase(producto.getNombre().trim())) {
                    throw new Exception("Validación fallida: Ya existe un registro con el nombre '" + producto.getNombre() + "'.");
                }
            }
        }


        // Validación de margen (Ejemplo: Mínimo 10% de ganancia)
        double precioMinimoVenta = producto.getPrecioCompra() * 1.10;
        if (producto.getPrecioVenta() < precioMinimoVenta) {
            throw new Exception("Protección de Margen: El precio de venta debe ser al menos un 10% mayor al precio de compra.");
        }

        /* if (producto.getTipoProducto().equalsIgnoreCase("PLATILLO")) {
        // Para platillos, el costo viene de los ingredientes. Se valida que se venda a un precio válido.
        if (producto.getPrecioVenta() <= 0) {
            throw new Exception("El platillo debe tener un precio de venta mayor a cero.");
        }
        // Aquí en el futuro llamarías a PlatilloService para calcular el costo de los ingredientes
    }*/


        if (producto.getUnidadMedida() == null) {
            throw new Exception("Debe asignar una Unidad de Medida al producto.");
        }



        boolean guardado = productoRepository.registrarProducto(producto);

        if (!guardado) {
            throw new Exception("Error interno: No se pudo guardar el producto en la base de datos.");
        }

    }


    public List<Producto> obtenerProductos() {
        return productoRepository.obtenerProductos();
    }

    public boolean actualzarProducto(Producto producto) throws Exception{
        if (producto == null){
            throw new Exception("El producto no puede ser nulo");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }

        if(producto.getPrecioCompra() <= 0){
            return false;
        }

        if (producto.getUnidadMedida() == null) {
            throw new Exception("Debe asignar una Unidad de Medida al producto.");
        }

        return productoRepository.actualizarProducto(producto);
    }


    public String  eliminarProducto(int id) throws Exception{

        if (id <= 0) {
            throw new Exception("ID de producto no válido.");
        }

        Producto producto = productoRepository.obtenerProductoPorId(id);
        if(producto == null){
            throw new Exception("El producto no existe");
        }

        boolean dependencia = productoRepository.estaAsociadoVentaOPlatillo(id);
        if(dependencia){
            boolean desactivado = productoRepository.desactivarProducto(id);
            if(!desactivado){
                throw new Exception("Error al desactivar");
            }
            return "Este producto esta asociado a venta o platillo y fue desactivado";

        }else{
            // Eliminamos  de la BD
            boolean eliminado = productoRepository.eliminarProducto(id);
            if (!eliminado) {
                throw new Exception("Error al intentar eliminar el producto permanentemente.");
            }
            return "El producto no tenía asociaciones y fue ELIMINADO de la base de datos.";
        }

    }


}
