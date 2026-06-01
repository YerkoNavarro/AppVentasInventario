package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.controller.moduloProductos.ProductoController;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.service.ProductoService;
import com.sistema.puntoventas.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class LoginController {

    @FXML
    private TextField rutTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button continuarButton;

    // --- ELEMENTOS GRÁFICOS DE LAS MÉTRICAS DEL DASHBOARD ---
    @FXML private Label lblProductosActivos;
    @FXML private Label lblPlatillosActivos;
    @FXML private Label lblCategoriasActivas;
    @FXML private Label lblBajoStock;

    // ==============================================================================
    // VARIABLE GLOBAL DE SESIÓN: Guarda el usuario activo para todo el software
    // ==============================================================================
    public static Usuario usuarioLogueado;

    private UsuarioService usuarioService = new UsuarioService();
    private ProductoService productoService = new ProductoService(); // Servicio para las métricas

    @FXML
    public void handleLogin() {
        String rut = rutTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (rut.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, ingresa tu RUT y contraseña");
            return;
        }

        Usuario usuario = usuarioService.iniciarSesion(rut, password);

        if (usuario != null) {
            // ==============================================================================
            // REGISTRO DE SESIÓN: Guardamos al usuario antes de proceder a la interfaz principal
            // ==============================================================================
            usuarioLogueado = usuario;

            mostrarMensaje("Éxito", "Bienvenido " + usuario.getNombre(), Alert.AlertType.INFORMATION);

            // Ejecuta el cálculo automático de métricas en la interfaz
            actualizarMetricas();

        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "RUT o contraseña incorrectos");
        }
    }

    /**
     * Calcula y actualiza las métricas visuales del inventario en base a streams de Java
     */
    private void actualizarMetricas() {
        try {
            // Obtenemos la lista desde el servicio de productos
            List<Producto> lista = productoService.obtenerProductos();

            if (lista == null) {
                return;
            }

            long totalProductos = lista.stream().count();

            long totalPlatillos = lista.stream()
                    .filter(p -> p.getTipoProducto() != null && p.getTipoProducto().name().equals("PLATILLO"))
                    .count();

            long totalCategorias = lista.stream()
                    .filter(p -> p.getCategoria() != null)
                    .map(p -> p.getCategoria().getNombreCategoria())
                    .distinct()
                    .count();

            long bajoStock = lista.stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .count();

            long noBajoStock = lista.stream()
                    .filter(p -> p.getStockActual() > p.getStockMinimo())
                    .count();

            // Seteamos los textos de forma segura verificando que las etiquetas existan
            if (lblProductosActivos != null) lblProductosActivos.setText(String.valueOf(totalProductos));
            if (lblPlatillosActivos != null) lblPlatillosActivos.setText(String.valueOf(totalPlatillos));
            if (lblCategoriasActivas != null) lblCategoriasActivas.setText(String.valueOf(totalCategorias));

            if (lblBajoStock != null) {
                if (bajoStock > 0) {
                    lblBajoStock.setText(String.valueOf(bajoStock));
                } else {
                    lblBajoStock.setText("No bajo stock");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al actualizar metricas: " + e.getMessage());
            mostrarMensaje("Error", "No se pudieron actualizar las métricas", Alert.AlertType.ERROR);
        }
    }

    // --- MÉTODOS AUXILIARES PARA ALERTAS (Mantienen tus firmas originales) ---

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void mostrarMensaje(String titulo, String encabezado, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(null);
        alerta.showAndWait();
    }
}