package com.sistema.puntoventas.controller;

import java.lang.annotation.Repeatable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.sistema.puntoventas.modelo.moduloProducto.Producto;
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

    
    private ProductoService productoService; // Declarar como miembro de la clase


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
                // Buscar el producto real por su nombre para obtener su ID
                // Iteramos sobre los productos disponibles para encontrar el que coincide con el nombre
                Producto productoSeleccionado = null;
                for (Producto p : productosDisponibles) {
                    if (p.getNombre().equalsIgnoreCase(textFieldProducto.getText().trim())) {
                        productoSeleccionado = p;
                        break;
                    }
                }
                if (productoSeleccionado == null) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Producto no encontrado", "El producto '" + textFieldProducto.getText() + "' no existe en el inventario o no fue seleccionado correctamente.");
                    return;
                }
                productoTemporal = productoSeleccionado; // Ahora productoTemporal tiene el ID y otros detalles
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

    private final ContextMenu contextMenu = new ContextMenu(); // Menú contextual para autocompletar
    private final List<Producto> productosDisponibles = new ArrayList<>(); // Almacena objetos Producto completos

    private void configurarAutoComplete() {

    textFieldProducto.textProperty().addListener((obs, oldVal, newVal) -> {

        if (newVal == null || newVal.isBlank()) {
            contextMenu.hide();
            return;
        }

        // Obtiene solo el último fragmento después de la última "," o " "
        String[] partes = newVal.split("[,\\s]+");
        String ultimaParte = partes[partes.length - 1].trim();

        // Si el último fragmento está vacío (ej: el usuario escribió "Pan, ")
        // no busca sugerencias
        if (ultimaParte.isBlank()) {
            contextMenu.hide();
            return;
        }

        List<String> filtrados = productosDisponibles.stream()
            .map(Producto::getNombre) // Mapear a nombres para el filtrado
            .filter(p -> p.toLowerCase().contains(ultimaParte.toLowerCase()))
            .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            contextMenu.hide();
            return;
        }

        contextMenu.getItems().clear();
        for (String sugerencia : filtrados) {
            MenuItem item = new MenuItem(sugerencia);
            item.setOnAction(e -> {

                // Reemplaza solo la última parte, conserva lo anterior
                String textoActual = textFieldProducto.getText();
                int ultimaSeparador = Math.max(
                    textoActual.lastIndexOf(","),
                    textoActual.lastIndexOf(" ")
                );

                String prefijo = ultimaSeparador >= 0
                    ? textoActual.substring(0, ultimaSeparador + 1) + " "
                    : "";

                textFieldProducto.setText(prefijo + sugerencia);
                textFieldProducto.positionCaret(textFieldProducto.getText().length());
                contextMenu.hide();
            });
            contextMenu.getItems().add(item);
        }

        contextMenu.show(textFieldProducto, Side.BOTTOM, 0, 0);
    });

    textFieldProducto.focusedProperty().addListener((obs, oldVal, isFocused) -> {
        if (!isFocused) contextMenu.hide(); // Ocultar al perder el foco
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
    void guardarTablaEnBD(ActionEvent event) {
        //captura los datos actuales de la tabla y los guarda en la BD con los metodos de service
        if(idTablaVentas.getItems().isEmpty()){
            mostrarAlerta(Alert.AlertType.WARNING, "Tabla vacía", "No hay datos en la tabla para guardar.");
            return;
            
        }
        else{
            ArrayList<ventaAplicacion> tablaVentaAplicacion = new ArrayList<>(idTablaVentas.getItems());
            Boolean ventaRegistrada = ventaService.subirTablaBD(tablaVentaAplicacion);

        }
        
        
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

    // Inicializar con la fecha y hora actual formateada
    LocalDateTime ahora = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    textfieldFecha.setText(ahora.format(formatter));


    this.productoService = new ProductoService(); // Inicializar el servicio de productos
    productosDisponibles.addAll(this.productoService.obtenerProductos()); // Cargar todos los productos disponibles
    configurarAutoComplete(); // Configurar el autocompletado con los productos cargados
    
    System.out.println("el estado es: "+tablaEstaVacia);
    for (Producto p : productosDisponibles) {
        System.out.println(p.getNombre() + " (ID: " + p.getId() + ")"); // Imprimir también el ID para verificar
    }
    
    

}
   

}
