package com.sistema.puntoventas.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PanelPrincipalVistaController {







        // 1. CONEXIÓN CON LOS ELEMENTOS DEL FXML
        @FXML private Button btnUsuarios;
        @FXML private Button btnProductos;
        @FXML private Button btnVentas;
        @FXML private Button btnInventario;
        @FXML private Button btnPlatillos;

        @FXML private StackPane contentArea;

        // Variable para no perder el dashboard original al cambiar de pantallas
        private Node vistaDashboardInicial;

        // 2. MÉTODO DE INICIALIZACIÓN
        @FXML
        public void initialize() {
            cargarVistaModulo("DashboardVista.fxml", null);

            // Asignamos qué archivo FXML se abrirá al hacer clic en cada botón
            // (Asegúrate de que los nombres de los archivos .fxml coincidan con los tuyos)
            btnUsuarios.setOnAction(e -> cargarVistaModulo("PanelUsuarios-vista.fxml", btnUsuarios));
            btnProductos.setOnAction(e -> cargarVistaModulo("PanelPrincipalProductos.fxml", btnProductos));
            btnVentas.setOnAction(e -> cargarVistaModulo("PanelVentas-vista.fxml", btnVentas));
            btnInventario.setOnAction(e -> cargarVistaModulo("PanelInventario-vista.fxml", btnInventario));
            btnPlatillos.setOnAction(e -> cargarVistaModulo("PanelPrincipalPlatillosVista.fxml", btnPlatillos));
        }

        // 3. SISTEMA DE NAVEGACIÓN DINÁMICA
        private void cargarVistaModulo(String archivoFxml, Button botonActivo) {
            try {
                // Cargar el archivo FXML del módulo correspondiente
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/" + archivoFxml));
                Parent nuevaVista = loader.load();

                // Reemplazar el contenido del centro por la nueva vista
                contentArea.getChildren().setAll(nuevaVista);

                // Cambiar el color del botón
                actualizarEstiloBotones(botonActivo);

            } catch (IOException e) {
                System.err.println("Error al cargar la vista: " + archivoFxml);
                e.printStackTrace();
            }
        }

        // Método extra: Para poder volver al Dashboard desde el código
        private void volverAlDashboard(Button botonActivo) {
            if (vistaDashboardInicial != null) {
                contentArea.getChildren().setAll(vistaDashboardInicial);
                actualizarEstiloBotones(botonActivo);
            }
        }

        // 4. CAMBIO DE COLOR DEL BOTÓN (Estado Activo/Inactivo)
        private void actualizarEstiloBotones(Button botonActivo) {
            // Agrupamos los botones en una lista
            List<Button> botones = Arrays.asList(btnUsuarios, btnProductos, btnVentas, btnInventario);

            // A todos les quitamos la clase azul
            for (Button btn : botones) {
                if (btn != null) {
                    btn.getStyleClass().remove("menu-button-active");
                }
            }

            // Solo al que presionamos le ponemos la clase azul
            if (botonActivo != null) {
                botonActivo.getStyleClass().add("menu-button-active");
            }
        }

        // 5. EVENTOS DE LAS TARJETAS DEL DASHBOARD
        // En tu FXML pusiste 'onMouseClicked="#handleNavegacion"' en las VBox del centro
        @FXML
        public void handleNavegacion(MouseEvent event) {
        /* Aquí puedes detectar en qué tarjeta se hizo clic.
           Por ejemplo, si hacen clic en la tarjeta "Productos",
           podrías llamar a cargarVistaMódulo("PanelRegistrarProductosvista.fxml", btnProductos);
        */
            System.out.println("Clic detectado en una tarjeta del Dashboard!");
        }
    }

