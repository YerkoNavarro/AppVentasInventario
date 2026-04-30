package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.modelo.detalleVenta;


import com.sistema.puntoventas.repository.impl.DetalleVentaImpl;

import com.sistema.puntoventas.repository.impl.VentaRepositoryimpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Observable;

public class VentaService {
    
    DetalleVentaImpl detalleVentaImpl = new DetalleVentaImpl();

    VentaRepositoryimpl ventaRepositoryimpl = new VentaRepositoryimpl();

    public boolean guardarVenta(venta venta, List<Integer> idProductos){

        if (idProductos != null && !idProductos.isEmpty() && venta != null){
            boolean ventaRegistrada ;
            ventaRegistrada = ventaRepositoryimpl.registrarVentaCompleta(venta, idProductos);
            return ventaRegistrada;
        }
        else{
            System.err.println("Error al registrar la venta: idProductos o venta son nulos o vacíos.");
            return false;
        }
    }


    public List<ventaAplicacion> traerTodasLasVentas(){
        return detalleVentaImpl.obtenerTodasLasVentas();
    }
    
        
    public ObservableList<String> obtenerFechasTableView(){
        try {
            List<String> listaFechas = ventaRepositoryimpl.obtenerTodasLasFechas();

            ObservableList<String> obsListFechas = FXCollections.observableArrayList(listaFechas);
            return obsListFechas;
            
        } catch (Exception e) {
            System.err.println("Error al obtener fechas: " + e.getMessage());
            return null;
        }
        

    }

}
