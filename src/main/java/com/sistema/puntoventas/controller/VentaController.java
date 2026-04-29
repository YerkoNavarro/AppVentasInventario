package com.sistema.puntoventas.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class VentaController {

    @FXML
    private Button idBotonEliminar;

    @FXML
    private Button idBotonAgregar; // si tienes uno

    @FXML
    private TableView<?> idTablaVentas; // el fx:id de tu tabla

    @FXML
    void initialize() {
        idTablaVentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
