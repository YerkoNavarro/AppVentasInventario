package com.sistema.puntoventas;

import com.sistema.puntoventas.conexion.DbManager;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;

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
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Eluney");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();

        DbManager dbManager = new DbManager();
        dbManager.conectarBD();
        dbManager.crearTodasLasTablas();
        dbManager.crearUsuarioAdmin();


    
    


    }
}
