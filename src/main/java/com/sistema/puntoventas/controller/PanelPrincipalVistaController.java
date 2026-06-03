package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.util.MensajesAlerta; // Importación de tu utilidad de alertas
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelPrincipalVistaController {

    @FXML private Button btnDashboard;
    @FXML private Button btnUsuarios;
    @FXML private Button btnProductos;
    @FXML private Button btnVentas;
    @FXML private Button btnInventario;
    @FXML private Button btnPlatillos;
    @FXML private Button btnEstadisticas;
    @FXML private Button btnCategorias;

    // ==============================================================================
    // BOTÓN DE CERRAR SESIÓN
    // ==============================================================================
    @FXML private Button btnCerrarSesion;

    @FXML private StackPane contentArea;

    // Variable para no perder el dashboard original al cambiar de pantallas
    private Node vistaDashboardInicial;

    // Cache para almacenar las vistas ya cargadas y preservar su estado (tablas, textos, etc.)
    private final Map<String, Node> vistasCache = new HashMap<>();

    @FXML
    public void initialize() {
        // Cargar el Dashboard por defecto al iniciar
        cargarVistaModulo("DashboardVista.fxml", null);

        // Configuración de las acciones de los botones (Evitamos usar FXML MouseEvent para componentes Sidebar)
        if (btnDashboard != null) {
            btnDashboard.setOnAction(e -> cargarVistaModulo("DashboardVista.fxml", btnDashboard));
        }
        if (btnUsuarios != null) {
            btnUsuarios.setOnAction(e -> cargarVistaModulo("PanelPrincipalUsuarios.fxml", btnUsuarios));
        }
        if (btnProductos != null) {
            btnProductos.setOnAction(e -> cargarVistaModulo("PanelPrincipalProductos.fxml", btnProductos));
        }
        if (btnVentas != null) {
            btnVentas.setOnAction(e -> cargarVistaModulo("panelVentas.fxml", btnVentas));
        }
        if (btnInventario != null) {
            btnInventario.setOnAction(e -> cargarVistaModulo("PanelInventario.fxml", btnInventario));
        }
        if (btnCategorias != null) {
            btnCategorias.setOnAction(e -> cargarVistaModulo("PanelPrincipalCategorias.fxml", btnCategorias));
        }
        if (btnEstadisticas != null) {
            btnEstadisticas.setOnAction(e -> cargarVistaModulo("PanelPrincipalEstadisticasVista.fxml", btnEstadisticas));
        }

        // ==============================================================================
        // ASIGNAR ACCIÓN AL BOTÓN CERRAR SESIÓN
        // ==============================================================================
        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnAction(e -> cerrarSesion());
        }
    }

    /**
     * Lógica de Cierre de Sesión con Ventana de Confirmación
     */
    private void cerrarSesion() {
        // 1. Desplegamos la alerta de confirmación usando tu clase utilitaria MensajesAlerta
        boolean confirmar = MensajesAlerta.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Estás seguro de que deseas salir del sistema?",
                Alert.AlertType.CONFIRMATION
        );

        // 2. Si el usuario presiona "OK", procedemos con la desconexión segura
        if (confirmar) {
            try {
                // Limpiamos los datos del usuario logueado en memoria por seguridad
                LoginController.usuarioLogueado = null;
                System.out.println("Sesión finalizada con éxito.");

                // Cargar la vista del Login desde tus recursos
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/LoginVista.fxml"));
                Parent root = loader.load();

                // Obtener el Stage de manera segura usando el nodo contenedor principal
                Stage stageActual = (Stage) contentArea.getScene().getWindow();

                // Reemplazar la escena actual por la del Login
                Scene loginScene = new Scene(root);
                stageActual.setScene(loginScene);
                stageActual.setTitle("Sistema Punto de Ventas - Iniciar Sesión");

                // Desactivar el maximizado si estaba activo para que el Login se vea bien proporcionado
                stageActual.setMaximized(false);
                stageActual.setWidth(1920); // Dimensiones por defecto de tu LoginVista.fxml
                stageActual.setHeight(1080);
                stageActual.centerOnScreen();

                stageActual.show();

            } catch (IOException e) {
                System.err.println("❌ ERROR: No se pudo cargar el LoginVista.fxml al cerrar sesión.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Cierre de sesión cancelado de forma voluntaria.");
        }
    }

    private void cargarVistaModulo(String archivoFxml, Button botonActivo) {
        try {
            Node vista;

            if (archivoFxml.equals("DashboardVista.fxml")) {
                if (vistaDashboardInicial == null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/" + archivoFxml));
                    vistaDashboardInicial = loader.load();
                }
                vista = vistaDashboardInicial;
            } else if (archivoFxml.equals("panelVentas.fxml") && vistasCache.containsKey(archivoFxml)) {
                vista = vistasCache.get(archivoFxml);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/" + archivoFxml));
                vista = loader.load();

                if (archivoFxml.equals("panelVentas.fxml")) {
                    vistasCache.put(archivoFxml, vista);
                }
            }

            contentArea.getChildren().setAll(vista);
            actualizarEstiloBotones(botonActivo);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista interna: " + archivoFxml);
            e.printStackTrace();
        }
    }

    private void actualizarEstiloBotones(Button botonActivo) {
        List<Button> botones = Arrays.asList(
                btnDashboard, btnUsuarios, btnProductos, btnVentas,
                btnInventario, btnPlatillos, btnEstadisticas, btnCategorias, btnCerrarSesion
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
        // Reservado para clicks auxiliares sobre componentes del Dashboard central
    }
}