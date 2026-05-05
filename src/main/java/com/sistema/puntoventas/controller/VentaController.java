package com.sistema.puntoventas.controller;

import java.lang.annotation.Repeatable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.venta;
import com.sistema.puntoventas.modelo.ventaAplicacion;
import com.sistema.puntoventas.service.ProductoService;
import com.sistema.puntoventas.service.VentaService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VentaController {
    Boolean tablaEstaVacia = true;

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
    private MenuItem BotonNuevaVenta;



    //del tab agregar venta
    @FXML
    private TextField textFieldProducto;

    @FXML
    private TextField textfieldDescripcion;

    @FXML
    private TextField textfieldFecha;

    @FXML
    private TextField textfieldTipoPago;

    @FXML
    private TextField textfieldTotal;

    

   @FXML
    void agregarVenta(ActionEvent event) {
    // Mostrar alerta si ALGÚN campo está vacío
        if (textfieldFecha.getText().isEmpty() || textfieldTotal.getText().isEmpty() ||
            textfieldTipoPago.getText().isEmpty() ||
            textFieldProducto.getText().isEmpty()) {

            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, complete los campos para registrar la venta.");

        } else {
            try {
                venta nuevaVenta = new venta();
                nuevaVenta.setTotalVenta(Double.parseDouble(textfieldTotal.getText()));
                nuevaVenta.setFechaHora(textfieldFecha.getText());
                nuevaVenta.setTipoPago(textfieldTipoPago.getText());
                nuevaVenta.setDescripcion(textfieldDescripcion.getText());

                ventaAplicacion nuevaVentaAplicacion = new ventaAplicacion();
                nuevaVentaAplicacion.setVenta(nuevaVenta);
                List<Producto> listaProductos = new ArrayList<>();
                Producto productoTemporal = new Producto();
                productoTemporal.setNombre(textFieldProducto.getText());
                listaProductos.add(productoTemporal);
                nuevaVentaAplicacion.setDetalleVentas(listaProductos);


                idTablaVentas.getItems().add(nuevaVentaAplicacion);
                tablaEstaVacia = false;

                mostrarAlerta(Alert.AlertType.INFORMATION, "Venta registrada", "La venta se ha registrado correctamente.");
                limpiarCamposVenta();
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de formato", "El total debe ser un número válido.");
            }
        }
    }

    private final ContextMenu contextMenu = new ContextMenu();
    private final List<String> productosSujerencia = new ArrayList<>();

    private void configurarAutoComplete() {

        textFieldProducto.textProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal == null || newVal.isBlank()) {
                contextMenu.hide();
                return;
            }

            List<String> filtrados = productosSujerencia.stream()
                .filter(p -> p.toLowerCase().contains(newVal.toLowerCase()))
                .collect(Collectors.toList());

            if (filtrados.isEmpty()) {
                contextMenu.hide();
                return;
            }

            contextMenu.getItems().clear();
            for (String sugerencia : filtrados) {
                MenuItem item = new MenuItem(sugerencia);
                item.setOnAction(e -> {
                    textFieldProducto.setText(sugerencia);
                    textFieldProducto.positionCaret(sugerencia.length());
                    contextMenu.hide();
                });
                contextMenu.getItems().add(item);
            }

            contextMenu.show(textFieldProducto, Side.BOTTOM, 0, 0);
        });

        textFieldProducto.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!isFocused) contextMenu.hide();
        });
    }
   



    
    
    @FXML
    void cargarVentaAplicacion(ActionEvent event) {
        //lanzar el panel panelCargarVenta.fxml
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/panelCargarVenta.fxml"));
            Parent root = loader1.load();
            
            //  referencia al controlador del panel de carga
            CargarVentaController cargarController = loader1.getController();

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
                System.out.println("el estado es: "+tablaEstaVacia);
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

            ColDescripcion.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getVenta().getDescripcion()));
            
            ColTipoPago.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getVenta().getTipoPago()));

            // Cargar la lista en el TableView
            idTablaVentas.setItems(FXCollections.observableArrayList(listaVentas));
            tablaEstaVacia = false;
        }
        
    }


    @FXML
    void cargarNuevaVenta(ActionEvent event) { //al pulsar el botonNuevaVenta
        //pone la tabla con una lista vacia
        System.out.println("tabla seteada vacia");
        List<ventaAplicacion> listaNueva = new ArrayList<>();

        var listaNuevaObs = FXCollections.observableArrayList(listaNueva);
        idTablaVentas.setItems(listaNuevaObs);


    }

    @FXML
    void eliminarVenta(ActionEvent event) {
        ventaAplicacion seleccionada = idTablaVentas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            idTablaVentas.getItems().remove(seleccionada);
            if (idTablaVentas.getItems().isEmpty()) {
                tablaEstaVacia = true;
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione una venta de la tabla para eliminar.");
        }
    }


     @FXML
    void guardarEnBD(ActionEvent event) {

    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCamposVenta() {
        textFieldProducto.clear();
        textfieldDescripcion.clear();
        textfieldTipoPago.clear();
        textfieldTotal.clear();
    }

    @FXML
    public void initialize() {
    // Sin CONSTRAINED_RESIZE_POLICY
    
    ColFecha.prefWidthProperty().bind(idTablaVentas.widthProperty().multiply(0.2));
    ColProductos.prefWidthProperty().bind(idTablaVentas.widthProperty().multiply(0.2));
    ColTipoPago.prefWidthProperty().bind(idTablaVentas.widthProperty().multiply(0.2));
    ColTotalVenta.prefWidthProperty().bind(idTablaVentas.widthProperty().multiply(0.2));
    ColDescripcion.prefWidthProperty().bind(idTablaVentas.widthProperty().multiply(0.2));

    // Inicializar con la fecha y hora actual formateada
    LocalDateTime ahora = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    textfieldFecha.setText(ahora.format(formatter));


    ProductoService productoService = new ProductoService();
    List<Producto> productosServiceList = productoService.obtenerProductos();
    productosSujerencia.addAll(productosServiceList.stream().map(Producto::getNombre).toList());
    configurarAutoComplete();
    
    System.out.println("el estado es: "+tablaEstaVacia);
    for (int i = 0; i < productosSujerencia.size(); i++) {
        System.out.println(productosSujerencia.get(i));
    }
    
    

}
   

}
