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

            //limpia la hora de las fechas y solo agrega si no se repite
            for (int i = 0; i < listaFechas.size(); i++) {
            String fecha = listaFechas.get(i).split(" ")[0];
            listaFechas.set(i, fecha);
        }

            // Elimina duplicados en la misma lista
            for (int i = 0; i < listaFechas.size(); i++) {
                for (int j = i + 1; j < listaFechas.size(); j++) {
                    if (listaFechas.get(i).equals(listaFechas.get(j))) {
                        listaFechas.remove(j);
                        j--;
                    }
                }
        }
            

            ObservableList<String> obsListFechas = FXCollections.observableArrayList(listaFechas);
            return obsListFechas;
            
        } catch (Exception e) {
            System.err.println("Error al obtener fechas: " + e.getMessage());
            return null;
        }
        

    }

    public ObservableList<ventaAplicacion> obtenerVentasporFecha(String fecha){

        //formatear string fecha para solo obtener la fecha y no la hora ej: 2025-04-01 09:15:00 a 2025-04-01
        
        fecha = "%" + fecha + "%"; //% % para el LIKE del query SQL
        System.out.println("la fecha se formateó a: " + fecha);

        try {
            ObservableList<ventaAplicacion> obsListVentas = FXCollections.observableArrayList(detalleVentaImpl.obtenerTodasLasVentasporFecha(fecha));
            return obsListVentas;

        } catch (Exception e) {
            System.err.println("Error al obtener ventas por fecha: " + e.getMessage());
            return null;
        }
    }
        


}
