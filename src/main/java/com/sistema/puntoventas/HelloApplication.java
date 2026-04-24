package com.sistema.puntoventas;

import com.sistema.puntoventas.conexion.DbManager;
import com.sistema.puntoventas.modelo.Categoria;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.TipoProducto;
import com.sistema.puntoventas.modelo.UnidadMedida;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/sistema/puntoventas/panelPrincipalVista.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cafeteria Eluney");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        DbManager dbManager = new DbManager();
        dbManager.conectarBD();
        dbManager.crearTodasLasTablas();


        Categoria categoria1  =new Categoria(1,true, "Bebidas calientes y frías", "Bebidas");
        Producto producto1 = new Producto(2, "Café Americano", 1000, 1500, null, "Cafetería Eluney", 100, 100, "cafe_americano.jpg", UnidadMedida.UNIDAD, TipoProducto.DIRECTO);

       

    }
}
