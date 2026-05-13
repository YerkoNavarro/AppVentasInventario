package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.service.ProductoService;
import com.sistema.puntoventas.service.UsuarioService;
import com.sistema.puntoventas.service.VentaService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent; // IMPORTANTE: Debe estar este import

import java.io.IOException;
import java.util.List;

public class DashboardVistaController {

    @FXML private Label lblTotalCompras;
    @FXML private Label lblTotalProductos;
    @FXML private Label lblTotalUsuarios;
    @FXML private StackPane contentArea;

    private UsuarioService usuarioService;
    private ProductoService productoService;
    private VentaService ventaService;

    @FXML
    public void initialize() {
        usuarioService = new UsuarioService();
        productoService = new ProductoService();
        ventaService = new VentaService();
        cargarMetricas();
    }

    private void cargarMetricas() {
        try {
            // Usuarios
            int totalUsuarios = usuarioService.obtenerUsuarios().size();
            lblTotalUsuarios.setText(String.valueOf(totalUsuarios));

            // Productos
            int totalProductos = productoService.obtenerProductos().size();
            lblTotalProductos.setText(String.valueOf(totalProductos));

            // Ventas
            List<ventaAplicacion> ventas = ventaService.traerTodasLasVentas();
            lblTotalCompras.setText(String.valueOf(ventas != null ? ventas.size() : 0));

        } catch (Exception e) {
            System.err.println("Error al cargar métricas: " + e.getMessage());
        }
    }

    // --- MÉTODOS PARA LAS TARJETAS (Ajustados con MouseEvent) ---

    @FXML
    public void abrirModuloDashboard(MouseEvent event) {
        cargarMetricas();
    }

    @FXML
    public void abrirModuloUsuarios(MouseEvent event) {
        // Verifica que esta ruta sea exacta en tu proyecto
        cargarVista("/com/sistema/puntoventas/PanelPrincipalUsuarios.fxml");
    }

    @FXML
    public void abrirModuloProductos(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/PanelPrincipalProductos.fxml");
    }

    @FXML
    public void abrirModuloOrdenes(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/panelVentas.fxml");
    }

    @FXML
    public void abrirModuloCategorias(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/PanelPrincipalCategorias.fxml");
    }

    @FXML
    public void abrirModuloReportes(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/PanelPrincipalEstadisticasVista.fxml");
    }

    @FXML
    public void abrirModuloPerfil(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/PanelPrincipalUsuarios.fxml");
    }

    @FXML
    public void abrirModuloTienda(MouseEvent event) {
        cargarVista("/com/sistema/puntoventas/PanelPrincipalUsuarios.fxml");
    }

    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}