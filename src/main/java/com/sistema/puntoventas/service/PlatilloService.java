package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.moduloProducto.*;
import com.sistema.puntoventas.repository.impl.PlatilloRepositoryImpl;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;
import com.sistema.puntoventas.repository.moduloProductos.IPlatilloRepository;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import com.sistema.puntoventas.modelo.moduloProducto.MetricasDTO;

import java.util.List;

public class PlatilloService {

    private IPlatilloRepository platilloRepository;
    private IProductoRepository productoRepository;

    public PlatilloService(){
        this.platilloRepository = new PlatilloRepositoryImpl();
        this.productoRepository = new ProductoRepositoryImpl();
    }



    public void registrarPlatillo(Platillo platillo) throws Exception {
        if (platillo == null) {
            throw new Exception("El platillo no puede ser nulo");
        }

        if (platillo.getNombre() == null || platillo.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del platillo es obligatorio.");
        }

        if (platillo.getPrecio() <= 0) {
            throw new Exception("El platillo debe tener un precio de venta mayor a cero.");
        }

        // Validar que el nombre del platillo sea único
        if (platilloRepository.existeNombre(platillo.getNombre().trim(), 0)) {
            throw new Exception("Ya existe un platillo con el nombre '" + platillo.getNombre() + "'.");
        }

        // Aquí podrías agregar lógica adicional para validar los ingredientes, etc.

       if(platillo.getTipoProducto() == TipoProducto.PLATILLO) {
           platilloRepository.registrarPlatillo(platillo);
           System.out.println("Platillo registrado exitosamente: " + platillo.getNombre());
       } else {
           throw new Exception("El tipo de producto debe ser PLATILLO para registrar un platillo.");
       }

    }

    public List<Platillo> obtenerPlatillos() throws Exception {

        return platilloRepository.obtenerPlatillos();
    }


    public List<Producto> obtenerIngredientes() throws Exception{
        return productoRepository.buscarPorTipoProducto(TipoProducto.SOLO_INVENTARIO);
    }
    

    public void calcularCostoProduccion(Platillo platillo){
        double costoTotal = 0.0;
        
        if(platillo.getIngrediente() != null){
            for (DetallePlatillo detalle : platillo.getIngrediente()){
                if(detalle.getProducto() != null){
                    double costoIngrediente = detalle.getProducto().getPrecioCompra();
                    double cantidadUtilizada = detalle.getCantidadIngrediente();
                    costoTotal += costoIngrediente * cantidadUtilizada;
                    System.out.println(costoTotal);

                }
            }
            
            platillo.setCostoProduccion(costoTotal);
        }


    }


    public void validarStockIngredientes(Producto ingrediente, double cantidadNueva, List<DetallePlatillo> ingredientesActuales) throws Exception{
        if(ingrediente == null){
            throw new Exception("Debes seleccionar un ingrediente");
        }

        if(cantidadNueva <= 0){
            throw new Exception("La cantidad del ingrediente debe ser mayor a cero");
        }

        double cantidadAcumulada = 0.0;
        if(ingredientesActuales != null){
            for (DetallePlatillo detalle : ingredientesActuales){
                if(detalle.getProducto() != null && detalle.getProducto().getId() == ingrediente.getId()){
                    cantidadAcumulada += detalle.getCantidadIngrediente();
                }
            }
        }

        double totalRequerido = cantidadAcumulada + cantidadNueva;

        if(totalRequerido > ingrediente.getStockActual()){
            throw new Exception("No hay suficiente stock del ingrediente '"
                    + ingrediente.getNombre() + "'. Stock actual: "
                    + ingrediente.getStockActual() + ", cantidad requerida: " + totalRequerido);
        }

    }




    public double convertirCantidad(Producto producto, double cantidadIngresada){
        if(producto.getUnidadMedida() != null){
            switch (producto.getUnidadMedida()){
                case GRAMOS:
                case MILILITROS:
                    return cantidadIngresada / 1000.0;
                default:
                    return cantidadIngresada;
            }
        }

        return cantidadIngresada;
    }





}
