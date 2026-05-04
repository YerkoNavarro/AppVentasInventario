package com.sistema.puntoventas.controller.moduloProductos;

import com.sistema.puntoventas.modelo.moduloProducto.Platillo;
import com.sistema.puntoventas.service.PlatilloService;
import com.sistema.puntoventas.util.MensajesAlerta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.sistema.puntoventas.util.MensajesAlerta.mostrarConfirmacion;
import static com.sistema.puntoventas.util.MensajesAlerta.mostrarMensaje;

public class PanelPrincipalPlatillosController {

    @FXML
    private Label lblProductosActivos;

    @FXML
    private Label lblCategoriasActivas;

    @FXML
    private Label lblPlatillosActivos;

    @FXML
    private Label lblBajoStock;

    @FXML
    private Pane CardProductos;

    @FXML
    private Pane CardCategorias;

    @FXML
    private Pane CardActivos;

    @FXML
    private Pane CardBajoStock;

    @FXML
    private Button btnAgregarPlatillo;

    @FXML
    private Button btnEditarPlatillo;

    @FXML
    private Button btnEliminarPlatillo;

    @FXML
    private Button btnVerPlatillos;

    @FXML
    private TableView<Platillo> tablePlatillos;

    @FXML
    private TableColumn<Platillo, Integer> colId;

    @FXML
    private TableColumn<Platillo, String> colNombre;

    @FXML
    private TableColumn<Platillo, String> colCategoria;

    @FXML
    private TableColumn<Platillo, Double> colPrecio;

    @FXML
    private TableColumn<Platillo, Double> colCostoProduccion;

    @FXML
    private TableColumn<Platillo, Boolean> colEstado;

    private PlatilloService platilloService;

    public void initialize() {
        platilloService = new PlatilloService();

        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cellData -> {
            var categoria = cellData.getValue().getCategoria();
            return new javafx.beans.property.SimpleStringProperty(
                    categoria != null ? categoria.getNombreCategoria() : ""
            );
        });
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCostoProduccion.setCellValueFactory(new PropertyValueFactory<>("costoProduccion"));
        colEstado.setCellValueFactory(cellData -> {
            boolean estado = cellData.getValue().isEstado();
            return new javafx.beans.property.SimpleBooleanProperty(estado);
        });

        // Configurar convertidores para mostrar texto en lugar de true/false
        colEstado.setCellFactory(column -> new TableCell<Platillo, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Activo" : "Inactivo");
                }
            }
        });

        //cargarPlatillos();

        // Configurar botones
        if (btnAgregarPlatillo != null) {
            btnAgregarPlatillo.setOnAction(event -> abrirFormularioAgregarPlatillo());
        }
        /*if (btnEditarPlatillo != null) {
            btnEditarPlatillo.setOnAction(event -> editarPlatillo());
        }
        if (btnEliminarPlatillo != null) {
            btnEliminarPlatillo.setOnAction(event -> eliminarPlatillo());
        }
        if (btnVerPlatillos != null) {
            btnVerPlatillos.setOnAction(event -> cargarPlatillos());
        }*/
    }

    /*private void cargarPlatillos() {
        try {
            var platillos = platilloService.obtenerPlatillos();
            tablePlatillos.getItems().setAll(platillos);

            // Actualizar métricas
            int platillosActivos = (int) platillos.stream().filter(Platillo::isEstado).count();
            lblPlatillosActivos.setText(String.valueOf(platillosActivos));
            lblProductosActivos.setText(String.valueOf(platillos.size()));
            lblCategoriasActivas.setText(String.valueOf(platillos.stream().map(p -> p.getCategoria().getId()).distinct().count()));
            
        } catch (Exception e) {
            mostrarMensaje("Error", "Error al cargar platillos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }*/

    private void abrirFormularioAgregarPlatillo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/PanelRegistroPlatillos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Agregar Platillo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            //cargarPlatillos();
        } catch (Exception e) {
            mostrarMensaje("Error", "Error al abrir formulario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /*private void editarPlatillo() {
        Platillo seleccionado = tablePlatillos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Advertencia", "Selecciona un platillo para editar", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/PanelRegistroPlatillos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Editar Platillo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarPlatillos();
        } catch (Exception e) {
            mostrarMensaje("Error", "Error al abrir formulario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }*/

   /* private void eliminarPlatillo() {
        Platillo seleccionado = tablePlatillos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Advertencia", "Selecciona un platillo para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmar = mostrarConfirmacion("Confirmación", "¿Estás seguro de que deseas eliminar este platillo?", Alert.AlertType.CONFIRMATION);
        if (confirmar) {
            try {
                platilloService.eliminarPlatillo(seleccionado.getId());
                mostrarMensaje("Éxito", "Platillo eliminado correctamente", Alert.AlertType.INFORMATION);
                cargarPlatillos();
            } catch (Exception e) {
                mostrarMensaje("Error", "Error al eliminar platillo: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }*/
}

