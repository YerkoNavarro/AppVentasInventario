package com.sistema.puntoventas.controller;

import java.util.List;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.ventaAplicacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class VentaController {

    @FXML
    private TableColumn<ventaAplicacion, String> ColDescripcion;

    @FXML
    private TableColumn<ventaAplicacion, String> ColFecha;


    @FXML
    private TableColumn<ventaAplicacion, List<Producto>> ColProductos;

    @FXML
    private TableColumn<ventaAplicacion, String> ColTipoPago;

    @FXML
    private TableColumn<ventaAplicacion, Double> ColTotalVenta;

    @FXML
    private TableView<?> idTablaVentas;

    @FXML
    void agregarVenta(ActionEvent event) {

    }

    @FXML
    void cargarVentaAplicacion(ActionEvent event) {
        //lanzar el panel panelCargarVenta.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/panelCargarVenta.fxml"));
            Parent root = loader.load();
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setTitle("Cargar Venta");
            
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }


    @FXML
    void cargarNuevaVenta(ActionEvent event) {

    }

    @FXML
    void eliminarVenta(ActionEvent event) {

    }




}
