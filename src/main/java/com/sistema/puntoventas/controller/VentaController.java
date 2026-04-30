package com.sistema.puntoventas.controller;

import java.lang.annotation.Repeatable;
import java.util.List;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.service.VentaService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
    private TableColumn<ventaAplicacion, String> ColProductos;

    @FXML
    private TableColumn<ventaAplicacion, String> ColTipoPago;

    @FXML
    private TableColumn<ventaAplicacion, Double> ColTotalVenta;

    @FXML
    private TableView<ventaAplicacion> idTablaVentas;

    @FXML
    void agregarVenta(ActionEvent event) {

    }

    @FXML
    void cargarVentaAplicacion(ActionEvent event) {
        //lanzar el panel panelCargarVenta.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/panelCargarVenta.fxml"));
            Parent root = loader.load();
            
            //  referencia al controlador del panel de carga
            CargarVentaController cargarController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setTitle("Cargar Venta");
            
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.showAndWait();

            // la fecha seleccionada desde el controlador del diálogo
            String fechaElegida = cargarController.getFechaSeleccionada();
            
            // Llamamos al método para cargar los datos si se eligió una fecha
            if (fechaElegida != null && !fechaElegida.isEmpty()) {
                System.out.println("Fecha recibida para filtrar: " + fechaElegida);
                cargarVentasTableView(fechaElegida);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }


    VentaService ventaService = new VentaService();


    private void cargarVentasTableView(String fecha) {
        List<ventaAplicacion> listaVentas = ventaService.obtenerVentasporFecha(fecha);
         if (listaVentas != null) {
            // Mapeo de datos para las columnas existentes
            ColFecha.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getVenta().getFechaHora()));
            
            ColTotalVenta.setCellValueFactory(cellData -> 
                new SimpleObjectProperty<>(cellData.getValue().getVenta().getTotalVenta()));
            
            ColProductos.setCellValueFactory(cellData -> 
                new SimpleObjectProperty<>(cellData.getValue().getNombreProducto()));

            // Columnas solicitadas vacías (temporalmente)
            ColDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(""));
            ColTipoPago.setCellValueFactory(cellData -> new SimpleStringProperty(""));

            // Cargar la lista en el TableView
            idTablaVentas.setItems(FXCollections.observableArrayList(listaVentas));
        }
        
    }


    @FXML
    void cargarNuevaVenta(ActionEvent event) {

    }

    @FXML
    void eliminarVenta(ActionEvent event) {

    }


    

}
