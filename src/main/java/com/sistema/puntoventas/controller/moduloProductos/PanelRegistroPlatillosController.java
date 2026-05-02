package com.sistema.puntoventas.controller.moduloProductos;

import com.sistema.puntoventas.modelo.moduloProducto.Categoria;
import com.sistema.puntoventas.service.ProductoService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class PanelRegistroPlatillosController {

    @FXML
    private ComboBox<?> cmbIngredientes;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableColumn<?, ?> colTipoProducto;

    @FXML
    private TableColumn<?, ?> colTipoProducto1;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<?> tableProductos;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextArea txtdescripcion;
    
    ProductoService productoService;
    
    public void initialize() throws Exception {
        productoService = new ProductoService();
        try {
            List<Categoria> categorias = productoService.obtenerCategorias();
            cmbCategoria.getItems().setAll(categorias);
        } catch (Exception e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }

}
