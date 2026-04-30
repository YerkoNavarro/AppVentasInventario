package com.sistema.puntoventas.controller;



import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.service.ProductoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.util.PropertyPermission;

public class PanelPrincipalProductosController {

    @FXML
    private Label lblProductosActivos;

    @FXML
    private Label lblCategoriasActivas;

    @FXML
    private Label lblPlatillosActivos;

    @FXML
    private Pane CardActivos;

    @FXML
    private Pane CardCategorias;

    @FXML
    private Pane CardProductos;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnEditarProducto;

    @FXML
    private Button btnEliminarProducto;

    @FXML
    private Button btnVerPlatillos;

    @FXML
    private TableView<Producto> tableProductos;

    @FXML
    private TableColumn<Producto, Integer> colId;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Double> colPrecioCompra;
    @FXML
    private TableColumn<Producto, Double> colPrecioVenta;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, String> colFechaVenc;
    @FXML
    private TableColumn<Producto, Integer> colStockActual;
    @FXML
    private TableColumn<Producto, Integer> colStockMin;
    @FXML
    private TableColumn<Producto, String> colUnidadMedida;
    @FXML
    private TableColumn<Producto, String> colTipoProducto;



    private ProductoService productoService;


    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo){

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();


    }
    
    
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colFechaVenc.setCellValueFactory(new PropertyValueFactory<>("fechaVenc"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStockMin.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colUnidadMedida.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory<>("tipoProducto"));

        obtenerProductos();
        actualizarMetricas();

        btnAgregarProducto.setOnAction(e -> cargarVistaAgregarProducto("PanelRegistrarProductosvista.fxml"));
    }

    private void cargarVistaAgregarProducto(String fxml) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/" + fxml));
            Parent root = loader.load();

            // Abrir la vista en una nueva ventana modal
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Registrar producto");
            stage.setScene(new Scene(root,1200,768));
            stage.showAndWait();

        } catch (Exception e) {
            mostrarMensaje("ERROR","No se pudo cargar la vista: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void obtenerProductos(){

        try{
            
            if (productoService == null) {
                productoService = new ProductoService();
            }


            java.util.List<Producto> productos = productoService.obtenerProductos();

            // Si no hay productos, limpiamos la tabla y mostramos aviso
            if (productos == null || productos.isEmpty()){
                if (tableProductos != null) {
                    tableProductos.getItems().clear();
                }
                mostrarMensaje("AVISO","No hay productos para mostrar", Alert.AlertType.INFORMATION);
                return;
            }

            // Poblamos la tabla 
            if (tableProductos != null) {
                
                @SuppressWarnings("unchecked")
                javafx.collections.ObservableList<Producto> items = (javafx.collections.ObservableList<Producto>) tableProductos.getItems();
                items.setAll(productos);
            }

            mostrarMensaje("ÉXITO","Productos cargados correctamente: " + productos.size(), Alert.AlertType.INFORMATION);

        }catch (Exception e){
            mostrarMensaje("ERROR","Error al obtener productos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }


    }


    private void actualizarMetricas(){
        try{
            if(productoService == null){
                productoService = new ProductoService();
            }

            java.util.List<Producto> lista = productoService.obtenerProductos();
            int totalProductos = lista.size();
            long totalPlatillos = lista.stream()
                    .filter(p -> p.getTipoProducto() != null && p.getTipoProducto().name().equals("PLATILLO"))
                    .count();

            long totalCategorias = lista.stream()
                    .filter(p -> p.getCategoria() != null)
                    .map(p -> p.getCategoria().getNombreCategoria())
                    .distinct()
                    .count();

            lblProductosActivos.setText(String.valueOf(totalProductos));
            lblPlatillosActivos.setText(String.valueOf(totalPlatillos));
            lblCategoriasActivas.setText(String.valueOf(totalCategorias));



        } catch (Exception e) {
            System.err.println("Error al actualizar metricas" +e.getMessage());
            mostrarMensaje("Error","No se pudieron actualizar las métricas", Alert.AlertType.ERROR);
        }
    }

}
