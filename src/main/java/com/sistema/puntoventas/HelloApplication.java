package com.sistema.puntoventas;

import com.sistema.puntoventas.conexion.DbManager;
import com.sistema.puntoventas.modelo.Producto;

import com.sistema.puntoventas.modelo.detalleVenta;
import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.repository.impl.DetalleVentaImpl;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;
import com.sistema.puntoventas.repository.impl.VentaRepositoryimpl;
import com.sistema.puntoventas.service.VentaService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/sistema/puntoventas/panelPrincipalVista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Eluney");
        stage.setScene(scene);
        stage.show();

        DbManager dbManager = new DbManager();
        dbManager.conectarBD();
        dbManager.crearTodasLasTablas();
        dbManager.crearUsuarioAdmin();

        /* 
        VentaRepositoryimpl ventaRepositoryimpl = new VentaRepositoryimpl();

        venta venta = new venta(0, "2024-05-20 10:30:00", 0,150.75);
   
        ArrayList<Integer> idProductos = new ArrayList<>();
        idProductos.add(1);
        idProductos.add(2);
        idProductos.add(3);

        ventaRepositoryimpl.registrarVentaCompleta(venta, idProductos);
       
        ProductoRepositoryImpl productoRepositoryImpl = new ProductoRepositoryImpl();

        Producto producto = new Producto(0, "producto1", 50000, 10000, "fritura", "2024-12-31", 0, 0, "", 0);
        productoRepositoryImpl.registrarProducto(producto);
        
         
       DetalleVentaImpl detalleVentaImpl = new DetalleVentaImpl();

        List<detalleVenta> detalleVentas = detalleVentaImpl.obtenerDetalleVentasporIdVenta(1);

        for (detalleVenta detalleVenta : detalleVentas) {
            System.out.println(detalleVenta.toString());
        }
        
        

        ProductoRepositoryImpl productoRepositoryImpl = new ProductoRepositoryImpl();
        Producto producto = new Producto(3, "producto3", 50000, 10000, "fritura", "2024-12-31", 0, 0, "", 0);
        productoRepositoryImpl.registrarProducto(producto);
        */

        
       VentaService ventaService = new VentaService();



      List<ventaAplicacion> ventas = ventaService.traerTodasLasVentas();

        

        for (ventaAplicacion va : ventas) {
    System.out.println("idVenta: " + va.getVenta().getIdVenta() + 
                       " | fecha: " + va.getVenta().getFechaHora() + 
                       " | total: " + va.getVenta().getTotalVenta());

    for (Producto p : va.getDetalleVentas()) {
        System.out.println("  -> " + p.getNombre() + " $" + p.getPrecioVenta());
    }
    System.out.println("----------");
}
    
    


    }
}
