package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.MovimientoInventario;
import com.sistema.puntoventas.modelo.TipoMovimiento;
import com.sistema.puntoventas.repository.impl.MovimientoRepositoryImpl;
// import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl; // Descomenta cuando tengas tu repo de productos
import com.sistema.puntoventas.service.InventarioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PanelInventarioController implements Initializable {

    // --- ELEMENTOS DE LA INTERFAZ ---
    @FXML private Button BtnRegistrarMovimiento;
    @FXML private ComboBox<TipoMovimiento> CmbComboBox; // Corregido a TipoMovimiento
    @FXML private TextField TxtCantidad;
    @FXML private TextArea TxtMotivo;
    @FXML private TextField TxtProductoNombre;

    // Tabla y Columnas corregidas para usar la clase MovimientoInventario
    @FXML private TableView<MovimientoInventario> tablaMovimientos; // ¡Asegúrate de agregar este fx:id en Scene Builder!
    @FXML private TableColumn<MovimientoInventario, String> ColFechaHora;
    @FXML private TableColumn<MovimientoInventario, Integer> ColProducto;
    @FXML private TableColumn<MovimientoInventario, String> ColTipo;
    @FXML private TableColumn<MovimientoInventario, Integer> ColCantidad;
    @FXML private TableColumn<MovimientoInventario, String> ColMotivo;
    @FXML private TableColumn<MovimientoInventario, Integer> ColUsuario;

    // Tarjetas y KPIs
    @FXML private Label lblTotalProductos;
    @FXML private Label lblMovimientoHoy;
    @FXML private Label lblAlertaStock;

    // --- VARIABLES DEL BACKEND ---
    private MovimientoRepositoryImpl movimientoRepo;
    private InventarioService inventarioService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Inicializar Repositorios y Servicios
        movimientoRepo = new MovimientoRepositoryImpl();

        // NOTA: Para el InventarioService necesitas el ProductoRepository.
        // Si aún no lo tienes, temporalmente puedes pasar null u omitirlo dependiendo de tu constructor.
        // inventarioService = new InventarioService(movimientoRepo, new ProductoRepositoryImpl());

        // 2. Configurar el ComboBox con los valores del Enum
        CmbComboBox.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));

        // 3. Configurar las columnas de la tabla (Los nombres entre comillas deben coincidir EXACTO con los atributos de MovimientoInventario)
        ColFechaHora.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        ColProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        ColTipo.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));
        ColCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        ColMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        ColUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));

        // 4. Cargar los datos desde la BD
        cargarHistorial();

        // 5. Asignar la acción al botón directamente aquí
        BtnRegistrarMovimiento.setOnAction(this::registrarMovimientoAction);
    }

    // --- MÉTODOS DE LÓGICA ---

    private void cargarHistorial() {
        // Traemos la lista de la base de datos
        List<MovimientoInventario> ultimosMovimientos = movimientoRepo.obtenerUltimosMovimientos();

        // Convertimos la lista de Java a una lista observable de JavaFX y la ponemos en la tabla
        ObservableList<MovimientoInventario> datosTabla = FXCollections.observableArrayList(ultimosMovimientos);
        tablaMovimientos.setItems(datosTabla);
    }

    private void registrarMovimientoAction(ActionEvent event) {
        try {
            // 1. Validar que los campos no estén vacíos
            if (TxtProductoNombre.getText().isEmpty() || TxtCantidad.getText().isEmpty() || CmbComboBox.getValue() == null) {
                mostrarAlerta("Error", "Campos incompletos", "Por favor llena el Producto, Cantidad y Tipo de Movimiento.", Alert.AlertType.WARNING);
                return;
            }

            // 2. Extraer datos del formulario
            int idProducto = Integer.parseInt(TxtProductoNombre.getText()); // Por ahora asumimos que el usuario teclea el ID numérico
            int cantidad = Integer.parseInt(TxtCantidad.getText());
            TipoMovimiento tipo = CmbComboBox.getValue();
            String motivo = TxtMotivo.getText() != null ? TxtMotivo.getText() : "Sin motivo";
            int idUsuarioAdmin = 1; // ID 1 por defecto (Admin)

            // 3. Crear el objeto
            MovimientoInventario nuevoMovimiento = new MovimientoInventario(idProducto, tipo, cantidad, motivo, idUsuarioAdmin);

            // 4. Procesar el movimiento en la Base de Datos
            // Aquí idealmente usarías `inventarioService.procesarMovimiento(nuevoMovimiento);`
            // Por ahora usamos el repo directo para la prueba gráfica:
            boolean registrado = movimientoRepo.registrarMovimiento(nuevoMovimiento);
            boolean stockActualizado = movimientoRepo.actualizarStockFisico(idProducto, cantidad);

            if (registrado && stockActualizado) {
                mostrarAlerta("Éxito", "Movimiento Registrado", "El inventario se ha actualizado correctamente.", Alert.AlertType.INFORMATION);

                // Limpiar campos
                TxtProductoNombre.clear();
                TxtCantidad.clear();
                TxtMotivo.clear();
                CmbComboBox.getSelectionModel().clearSelection();

                // Refrescar la tabla para que aparezca el nuevo registro
                cargarHistorial();
            } else {
                mostrarAlerta("Error", "Error en Base de Datos", "No se pudo registrar el movimiento.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Datos inválidos", "El Producto y la Cantidad deben ser números enteros.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error fatal", "Excepción", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método auxiliar para mostrar ventanas emergentes bonitas (Pop-ups)
    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}