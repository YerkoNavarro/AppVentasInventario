package com.sistema.puntoventas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PanelPrincipalVistaController {

    @FXML private Button btnDashboard; // <--- NUEVO
    @FXML private Button btnUsuarios;
    @FXML private Button btnProductos;
    @FXML private Button btnVentas;
    @FXML private Button btnInventario;
    @FXML private Button btnPlatillos;
    @FXML private Button btnEstadisticas;
    @FXML private Button btnCategorias;

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        // Al iniciar, cargamos el Dashboard y marcamos el botón como activo
        cargarVistaModulo("DashboardVista.fxml", btnDashboard);

        // --- ASIGNACIÓN DE ACCIONES ---

        // Acción para el nuevo botón Inicio
        btnDashboard.setOnAction(e -> cargarVistaModulo("DashboardVista.fxml", btnDashboard));

        btnUsuarios.setOnAction(e -> cargarVistaModulo("PanelPrincipalUsuarios.fxml", btnUsuarios));
        btnProductos.setOnAction(e -> cargarVistaModulo("PanelPrincipalProductos.fxml", btnProductos));
        btnVentas.setOnAction(e -> cargarVistaModulo("panelVentas.fxml", btnVentas));
        btnInventario.setOnAction(e -> cargarVistaModulo("PanelInventario.fxml", btnInventario));
        btnPlatillos.setOnAction(e -> cargarVistaModulo("PanelPrincipalPlatillosVista.fxml", btnPlatillos));
        btnEstadisticas.setOnAction(e -> cargarVistaModulo("PanelPrincipalEstadisticasVista.fxml", btnEstadisticas));
        btnCategorias.setOnAction(e -> cargarVistaModulo("PanelPrincipalCategorias.fxml", btnCategorias));
    }

    private void cargarVistaModulo(String archivoFxml, Button botonActivo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/" + archivoFxml));
            Parent nuevaVista = loader.load();

            contentArea.getChildren().setAll(nuevaVista);

            // Actualizar estilo de los botones para que el seleccionado se vea azul
            actualizarEstiloBotones(botonActivo);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + archivoFxml);
            e.printStackTrace();
        }
    }

    private void actualizarEstiloBotones(Button botonActivo) {
        // Agregamos btnDashboard a la lista para que también se limpie su estilo
        List<Button> botones = Arrays.asList(
                btnDashboard, btnUsuarios, btnProductos, btnVentas,
                btnInventario, btnPlatillos, btnEstadisticas, btnCategorias
        );

        for (Button btn : botones) {
            if (btn != null) {
                btn.getStyleClass().remove("menu-button-active");
            }
        }

        if (botonActivo != null) {
            botonActivo.getStyleClass().add("menu-button-active");
        }
    }

    @FXML
    public void handleNavegacion(MouseEvent event) {
        System.out.println("Clic detectado en una tarjeta del Dashboard!");
    }
}