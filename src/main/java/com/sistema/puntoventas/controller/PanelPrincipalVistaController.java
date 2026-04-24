package com.sistema.puntoventas.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class PanelPrincipalVistaController {

    // Vincula los IDs que pusiste en Scene Builder (fx:id)
    @FXML
    private VBox btnDashboard;

    @FXML
    private VBox btnOrdenes;

    @FXML
    private VBox btnProductos;

    /**
     * Este método maneja los clics en las tarjetas del centro.
     * En Scene Builder debe estar en la sección 'On Mouse Clicked'.
     */
    @FXML
    private void handleNavegacion(MouseEvent event) {
        // Obtenemos el nodo que recibió el clic
        VBox source = (VBox) event.getSource();

        // Determinamos a qué vista ir según el ID del componente
        if (source.getId() != null) {
            switch (source.getId()) {
                case "btnDashboard":
                    System.out.println("Navegando a Dashboard...");
                    // cargarVista("/vistas/DashboardVista.fxml");
                    break;
                case "btnOrdenes":
                    System.out.println("Navegando a Órdenes...");
                    break;
                case "btnProductos":
                    System.out.println("Navegando a Productos...");
                    break;
                default:
                    System.out.println("Botón no reconocido: " + source.getId());
                    break;
            }
        }
    }

    /**
     * Método opcional para inicializar configuraciones al cargar la vista
     */
    @FXML
    public void initialize() {
        System.out.println("Controlador del Panel Principal listo.");
    }
}
