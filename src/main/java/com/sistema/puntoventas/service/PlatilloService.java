package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.moduloProducto.Platillo;
import com.sistema.puntoventas.modelo.moduloProducto.TipoProducto;
import com.sistema.puntoventas.repository.impl.PlatilloRepositoryImpl;
import com.sistema.puntoventas.repository.moduloProductos.IPlatilloRepository;

public class PlatilloService {

    private IPlatilloRepository platilloRepository;

    public PlatilloService(){
        this.platilloRepository = new PlatilloRepositoryImpl();
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
        if (!platilloRepository.existeNombre(platillo.getNombre().trim(), 0)) {
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
}
