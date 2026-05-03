package com.sistema.puntoventas.controller.moduloProductos;



import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.service.ProductoService;
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

public class PanelPrincipalProductosController {

    @FXML
    private Label lblProductosActivos;

    @FXML
    private Label lblCategoriasActivas;

    @FXML
    private Label lblPlatillosActivos;

    @FXML
    private Label lblBajoStock;

    @FXML
    private Pane CardActivos;

    @FXML
    private Pane CardCategorias;

    @FXML
    private Pane CardProductos;

    @FXML
    private Pane CardBajoStock;

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
    private TableColumn<Producto, Double> colCantidad;
    @FXML
    private TableColumn<Producto, String> colTipoProducto;



    private ProductoService productoService;

    
    
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
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory<>("tipoProducto"));

        obtenerProductos();
        actualizarMetricas();

        btnAgregarProducto.setOnAction(e -> cargarVistaAgregarProducto("PanelRegistrarProductosvista.fxml"));
        btnEditarProducto.setOnAction(e -> actualizarProductos());
        btnEliminarProducto.setOnAction(this::eliminarProducto);

        tableProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(newSelection != null){
                System.out.println("RECETA");
             //   btnVerReceta.setDisable(!newSelection.getTipoProducto().name().equals("PLATILLO"));
            }
        });
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
            obtenerProductos();

        } catch (Exception e) {
            MensajesAlerta.mostrarMensaje("ERROR","No se pudo cargar la vista: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private void cargarVistaActualizarProductos(Producto productoSeleccionado){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/PanelRegistrarProductosvista.fxml"));
            Parent root = loader.load();

            ProductoController controller = loader.getController();


            if (controller != null && productoSeleccionado != null) {
                controller.ActualizarProducto(productoSeleccionado);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Actualizar Producto");
            stage.setScene(new Scene(root,1200,768));
            stage.showAndWait();


            obtenerProductos();

        } catch (Exception e) {
            MensajesAlerta.mostrarMensaje("ERROR", "Error al abrir vista: " + e.getMessage(), Alert.AlertType.ERROR);
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
                MensajesAlerta.mostrarMensaje("AVISO","No hay productos para mostrar", Alert.AlertType.INFORMATION);
                return;
            }

            // Poblamos la tabla 
            if (tableProductos != null) {
                
                @SuppressWarnings("unchecked")
                javafx.collections.ObservableList<Producto> items = (javafx.collections.ObservableList<Producto>) tableProductos.getItems();
                items.setAll(productos);
            }

            MensajesAlerta.mostrarMensaje("ÉXITO","Productos cargados correctamente: " + productos.size(), Alert.AlertType.INFORMATION);
            actualizarMetricas();
        }catch (Exception e){
            MensajesAlerta.mostrarMensaje("ERROR","Error al obtener productos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }


    }

    @FXML
    public void actualizarProductos (){
       Producto productoSeleccionado  =tableProductos.getSelectionModel().getSelectedItem();

       if (productoSeleccionado == null ){
           System.out.println("Producto no seleccionado");
           MensajesAlerta.mostrarMensaje("AVISO","Seleccione un producto para editar", Alert.AlertType.WARNING);
           return;
       }

         cargarVistaActualizarProductos(productoSeleccionado);
    }




    @FXML
    public void eliminarProducto(javafx.event.ActionEvent event){
        Producto productoSeleccionado  =tableProductos.getSelectionModel().getSelectedItem();

        if(productoSeleccionado == null){
            mostrarMensaje("AVISO","Por favor seleccione un producto para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean respuesta = MensajesAlerta.mostrarConfirmacion("Confirmación","¿Está seguro que desea eliminar el producto seleccionado?", Alert.AlertType.CONFIRMATION);
        if(respuesta ){
            try {
                if (productoService == null) {
                    productoService = new ProductoService();
                }
                String eliminado = productoService.eliminarProducto(productoSeleccionado.getId());
                if (eliminado.equalsIgnoreCase("ELIMINADO")) {
                    mostrarMensaje("ÉXITO", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
                    obtenerProductos();
                } else {
                    mostrarMensaje("AVISO", "El producto tiene asociaciones, Solo se desactivara " + eliminado, Alert.AlertType.WARNING);
                    obtenerProductos();
                }
            } catch (Exception e) {
                MensajesAlerta.mostrarMensaje("ERROR", e.getMessage(), Alert.AlertType.ERROR);
                System.err.println("Error al eliminar producto: " + e.getMessage());
            }
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

            long bajoStock = lista.stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .count();

            long noBajoStock = lista.stream()
                            .filter(p -> p.getStockActual() > p.getStockMinimo())
                             .count();

            lblProductosActivos.setText(String.valueOf(totalProductos));
            lblPlatillosActivos.setText(String.valueOf(totalPlatillos));
            lblCategoriasActivas.setText(String.valueOf(totalCategorias));

            if(bajoStock>0) {
                lblBajoStock.setText(String.valueOf(bajoStock));
            }else{
                lblBajoStock.setText("No bajo stock");
            }



        } catch (Exception e) {
            System.err.println("Error al actualizar metricas" +e.getMessage());
            MensajesAlerta.mostrarMensaje("Error","No se pudieron actualizar las métricas", Alert.AlertType.ERROR);
        }
    }

}
