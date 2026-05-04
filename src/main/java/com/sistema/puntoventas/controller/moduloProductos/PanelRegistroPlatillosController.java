package com.sistema.puntoventas.controller.moduloProductos;

import com.sistema.puntoventas.modelo.moduloProducto.Categoria;
import com.sistema.puntoventas.modelo.moduloProducto.DetallePlatillo;
import com.sistema.puntoventas.modelo.moduloProducto.Platillo;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.modelo.moduloProducto.TipoProducto;
import com.sistema.puntoventas.service.PlatilloService;
import com.sistema.puntoventas.service.ProductoService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PanelRegistroPlatillosController {

    @FXML
    private Button btnAgregarIngrediente;

    @FXML
    private Button btnRegistrarPlatillo;

    @FXML
    private ComboBox<Producto> cmbIngredientes;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TableColumn<DetallePlatillo, Integer> colId;

    @FXML
    private TableColumn<DetallePlatillo, String> colNombre;

    @FXML
    private TableColumn<DetallePlatillo, Double> colTipoProducto;

    @FXML
    private TableColumn<DetallePlatillo, Double> colCostoProduccion;

    @FXML
    private TableColumn<DetallePlatillo, String> colUnidadMedida;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<DetallePlatillo> tableProductos;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextArea txtdescripcion;
    
    PlatilloService platilloService;
    ProductoService productoService;
    
    private ObservableList<DetallePlatillo> listaIngredientesTemporal = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        productoService = new ProductoService();
        platilloService = new PlatilloService();
        try {
            List<Categoria> categorias = productoService.obtenerCategorias();
            cmbCategoria.getItems().setAll(categorias);
            
            // Cargar productos para el ComboBox de ingredientes
            cmbIngredientes.getItems().setAll(platilloService.obtenerIngredientes());

            // Configurar el ComboBox para que muestre solo el nombre del ingrediente
            cmbIngredientes.setConverter(new javafx.util.StringConverter<Producto>() {
                @Override
                public String toString(Producto producto) {
                    return producto == null ? "" : producto.getNombre();
                }

                @Override
                public Producto fromString(String string) {
                    return null;
                }
            });

            // Configurar columnas de la tabla de ingredientes
            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));
            colTipoProducto.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCantidadIngrediente()).asObject());
            colTipoProducto.setText("Cantidad"); // Para reflejar el uso real
            
            colUnidadMedida.setCellValueFactory(cellData -> {
                var unidad = cellData.getValue().getProducto().getUnidadMedida();
                return new SimpleStringProperty(unidad != null ? unidad.name() : "");
            });
            colUnidadMedida.setText("Unidad Medida");
            
            colCostoProduccion.setCellValueFactory(cellData -> {
                double costoUnitario = cellData.getValue().getProducto().getPrecioCompra();
                double cantidad = cellData.getValue().getCantidadIngrediente();
                return new SimpleDoubleProperty(costoUnitario * cantidad).asObject();
            });
            colCostoProduccion.setText("Costo Total");
            
            tableProductos.setItems(listaIngredientesTemporal);

        } catch (Exception e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
        
        if (btnAgregarIngrediente != null) {
            btnAgregarIngrediente.setOnAction(this::agregarIngrediente);
        }
    }

    @FXML
    public void agregarIngrediente(ActionEvent event) {
        Producto prodSeleccionado = cmbIngredientes.getValue();
        String cantidadStr = txtCantidad.getText().trim();
        
        if (prodSeleccionado == null || cantidadStr.isEmpty()) {
            lblEstado.setText("Error: Seleccione un ingrediente y digite su cantidad.");
            lblEstado.setTextFill(Color.RED);
            return;
        }
        
        try {
            double cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                lblEstado.setText("Error: La cantidad debe ser mayor a 0.");
                lblEstado.setTextFill(Color.RED);
                return;
            }
            
            // Si la unidad es GRAMOS o MILILITROS, asumimos que el precio y stock base están en Kilos o Litros.
            // Entonces convertimos lo ingresado (ej. 200 gramos) a su equivalente base (0.2)
            if (prodSeleccionado.getUnidadMedida() != null) {
                switch (prodSeleccionado.getUnidadMedida()) {
                    case GRAMOS:
                    case MILILITROS:
                        cantidad = cantidad / 1000.0;
                        break;
                    default:
                        break;
                }
            }
            
            DetallePlatillo detalle = new DetallePlatillo();
            detalle.setProducto(prodSeleccionado);
            detalle.setCantidadIngrediente(cantidad);
            
            listaIngredientesTemporal.add(detalle);
            
            cmbIngredientes.getSelectionModel().clearSelection();
            txtCantidad.clear();
            lblEstado.setText("Ingrediente agregado temporalmente.");
            lblEstado.setTextFill(Color.BLUE);
            
        } catch (NumberFormatException e) {
            lblEstado.setText("Error: Cantidad inválida.");
            lblEstado.setTextFill(Color.RED);
        }
    }

    @FXML
    public void registrarPlatillo(ActionEvent event) {
        try {
            // 1. Validaciones básicas
            if (txtNombre.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                lblEstado.setText("Error: El nombre y el precio son obligatorios.");
                lblEstado.setTextFill(Color.RED);
                return;
            }

            if (cmbCategoria.getValue() == null) {
                lblEstado.setText("Error: Debes seleccionar una categoría.");
                lblEstado.setTextFill(Color.RED);
                return;
            }

            // 2. Construir el objeto PLATILLO
            Platillo nuevoPlatillo = new Platillo();
            nuevoPlatillo.setNombre(txtNombre.getText().trim());
            nuevoPlatillo.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            nuevoPlatillo.setCategoria(cmbCategoria.getValue());

            // 3. Asignaciones automáticas por contexto
            nuevoPlatillo.setTipoProducto(TipoProducto.PLATILLO);
            nuevoPlatillo.setCostoProduccion(0.0); // El costo se calculará en base a los ingredientes
            nuevoPlatillo.setStockActual(0); // Los platillos no tienen stock, se preparan en el momento
            nuevoPlatillo.setEstado(true); // Activo por defecto

            // Asignar lista de ingredientes
            if (listaIngredientesTemporal.isEmpty()) {
                lblEstado.setText("Error: El platillo debe tener al menos un ingrediente.");
                lblEstado.setTextFill(Color.RED);
                return;
            }
            nuevoPlatillo.setIngrediente(new ArrayList<>(listaIngredientesTemporal));

            // Calcular y asignar el costo total de producción en el servicio antes de registrar
            platilloService.calcularCostoProduccion(nuevoPlatillo);

            // 4. Enviar al Service (Asumiendo que hiciste un PlatilloService)
            platilloService.registrarPlatillo(nuevoPlatillo);

            // 5. Mensaje de éxito y limpieza
            lblEstado.setText("¡Platillo registrado con éxito!");
            lblEstado.setTextFill(Color.GREEN);
            limpiarFormulario(); // Tu método de limpieza

        } catch (NumberFormatException e) {
            lblEstado.setText("Error: El precio debe ser un número válido.");
            lblEstado.setTextFill(Color.RED);
        } catch (Exception e) {
            lblEstado.setText("Error: " + e.getMessage());
            lblEstado.setTextFill(Color.RED);
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtPrecio.clear();
        txtdescripcion.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        cmbIngredientes.getSelectionModel().clearSelection();
        txtCantidad.clear();
        listaIngredientesTemporal.clear();
    }

}
