package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.modelo.detalleVenta;


import com.sistema.puntoventas.repository.impl.DetalleVentaImpl;

import com.sistema.puntoventas.repository.impl.VentaRepositoryimpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VentaService {
    
    DetalleVentaImpl detalleVentaImpl = new DetalleVentaImpl();
    VentaRepositoryimpl ventaRepositoryimpl = new VentaRepositoryimpl();
    ProductoService productoService = new ProductoService();

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
            Set<String> fechasUnicas = new HashSet<>();

            for (String fechaCompleta : listaFechas) {
                fechasUnicas.add(fechaCompleta.split(" ")[0]);
            }

            List<String> resultado = new ArrayList<>(fechasUnicas);
            resultado.sort((a, b) -> b.compareTo(a)); // Orden descendente
            return FXCollections.observableArrayList(resultado);
            
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
        

    public Boolean subirTablaBD(ArrayList<ventaAplicacion> tablaVentaAplicacion){
        return ventaRepositoryimpl.registrarTabladeVentaCompleta(tablaVentaAplicacion);
    }

    /**
     * Resuelve una cadena de texto con nombres de productos separados por comas
     * buscando coincidencias en el catálogo.
     * 
     * @param input Nombres ingresados por el usuario.
     * @param catalogo Lista de productos disponibles para comparar.
     * @return Lista de productos encontrados.
     * @throws Exception Si algún producto no existe o la entrada es inválida.
     */
    public List<Producto> resolverProductos(String input, List<Producto> catalogo) throws Exception {
        if (input == null || input.isBlank()) {
            throw new Exception("El campo de productos no puede estar vacío.");
        }

        String[] nombres = input.split(",");
        List<Producto> listaProductos = new ArrayList<>();
        List<String> noEncontrados = new ArrayList<>();

        for (String nombre : nombres) {
            String nombreLimpio = nombre.trim();
            if (nombreLimpio.isEmpty()) continue;

            Optional<Producto> encontrado = catalogo.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreLimpio))
                .findFirst();

            if (encontrado.isPresent()) {
                listaProductos.add(encontrado.get());
            } else {
                noEncontrados.add(nombreLimpio);
            }
        }

        if (!noEncontrados.isEmpty()) {
            throw new Exception("No se encontraron los siguientes productos: " + String.join(", ", noEncontrados));
        }

        return listaProductos;
    }

    public ventaAplicacion procesarNuevaVentaApp(String totalStr, String fecha, String pago, String desc, List<Producto> productos) throws Exception {
        double total;
        try {
            total = Double.parseDouble(totalStr);
        } catch (NumberFormatException e) {
            throw new Exception("El total de la venta no tiene un formato numérico válido.");
        }

        venta v = new venta();
        v.setTotalVenta(total);
        v.setFechaHora(fecha);
        v.setTipoPago(pago);
        v.setDescripcion(desc);

        ventaAplicacion nuevaVentaApp = new ventaAplicacion();
        nuevaVentaApp.setVenta(v);
        nuevaVentaApp.setDetalleVentas(productos);
        return nuevaVentaApp;
    }
    
}
