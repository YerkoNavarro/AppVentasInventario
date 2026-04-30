package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class PanelRegistrarVentaController {

    @FXML
    private TableView<ventaAplicacion> idTableViewAgregarVenta;


    @FXML
    private TableColumn<ventaAplicacion, String> idColumnDescripcionNuevo;

    @FXML
    private TableColumn<ventaAplicacion, String> idColumnFechaNueva;

    @FXML
    private TableColumn<ventaAplicacion, String> idColumnProductosNuevo;

    @FXML
    private TableColumn<ventaAplicacion, String> idColumnTipoPagoNuevo;

    @FXML
    private TableColumn<ventaAplicacion, Double> idColumnTotalNuevo;

    @FXML
    void GuardarNuevaVenta(ActionEvent event) {

        if (guardarFila()) {
            Stage stage = (Stage) idTableViewAgregarVenta.getScene().getWindow();
            stage.close();
            
        }

    }


    private boolean guardarFila(){
        return true;
    }

}
