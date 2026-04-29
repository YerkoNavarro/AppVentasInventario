package com.sistema.puntoventas.controller;



import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.service.ProductoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

public class PanelPrincipalProductosController {

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
    private TableView<?> tableProductos;

    private ProductoService productoService;


    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo){

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();


    }
    
    
    public void initialize(){

        obtenerProductos();
        // corregir el nombre del recurso FXML y asignar el manejador para abrir la ventana de registrar
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

}
