package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.Role;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.service.UsuarioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PanelPrincipalUsuariosController implements Initializable {

    // --- ELEMENTOS GRÁFICOS INTACTOS ---
    @FXML private Pane CardUsuariosActivos;
    @FXML private Pane CardUsuariosAdmin;
    @FXML private Pane CardUsuariosVendedor;

    @FXML private Button btnAgregarUsuario;
    @FXML private Button btnEditarUsuario;
    @FXML private Button btnEliminarUsuario;

    @FXML private Label lblUsuariosActivos;
    @FXML private Label lblUsuariosAdmin;
    @FXML private Label lblUsuariosVendedor;

    // --- TABLA Y COLUMNAS ---
    @FXML private TableView<Usuario> tableUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colRut;
    @FXML private TableColumn<Usuario, String> colContraseña;
    @FXML private TableColumn<Usuario, String> colTelefono;
    @FXML private TableColumn<Usuario, Role> colRol;
    @FXML private TableColumn<Usuario, Boolean> colEstado;

    // --- VARIABLES DE LÓGICA ---
    private UsuarioService usuarioService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Inicializar el Servicio
        usuarioService = new UsuarioService();

        // 2. Configurar las Columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // === CONFIGURACIÓN DE CONTRASEÑA (OCULTA CON ASTERISCOS) ===
        colContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colContraseña.setCellFactory(columna -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String contraseñaReal, boolean vacio) {
                super.updateItem(contraseñaReal, vacio);
                if (vacio || contraseñaReal == null) {
                    setText(null);
                } else {
                    setText("*".repeat(contraseñaReal.length()));
                }
            }
        });

        // === CONFIGURACIÓN DE ESTADO (Boolean -> Texto) ===
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(columna -> new TableCell<Usuario, Boolean>() {
            @Override
            protected void updateItem(Boolean estado, boolean vacio) {
                super.updateItem(estado, vacio);
                if (vacio || estado == null) {
                    setText(null);
                } else {
                    setText(estado ? "Activo" : "Inactivo");
                }
            }
        });

        // 3. Cargar la Tabla y los KPIs
        cargarUsuarios();

        // 4. Asignar acciones a los botones
        btnEliminarUsuario.setOnAction(this::eliminarUsuarioSeleccionado);

        // === NUEVO: ACCIÓN PARA AGREGAR ===
        btnAgregarUsuario.setOnAction(event -> abrirVentanaUsuario(null));

        // === NUEVO: ACCIÓN PARA EDITAR ===
        btnEditarUsuario.setOnAction(event -> {
            Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado == null) {
                mostrarAlerta("Atención", "Ningún usuario seleccionado", "Selecciona un usuario de la tabla para editar.", Alert.AlertType.WARNING);
            } else {
                abrirVentanaUsuario(usuarioSeleccionado);
            }
        });
    }

    // --- MÉTODOS PROPIOS ---

    // MÉTODO PARA ABRIR LA VENTANA (RECICLADA PARA AGREGAR Y EDITAR)
    private void abrirVentanaUsuario(Usuario usuario) {
        try {
            // Cargar el FXML de la ventana de formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/puntoventas/PanelAgregarUsuario.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de esa ventana
            PanelAgregarUsuarioController controlador = loader.getController();

            // Si se pasó un usuario, activar el modo edición en el otro controlador
            if (usuario != null) {
                controlador.cargarDatosUsuario(usuario);
            }

            // Crear y configurar la ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle(usuario == null ? "Agregar Nuevo Usuario" : "Editar Usuario");
            stage.setScene(new Scene(root,1200,768));

            // Hacerla modal (bloquea la ventana principal hasta cerrar esta)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Al cerrar la ventana, recargamos la tabla para ver cambios (nuevos o editados)
            cargarUsuarios();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarUsuarios() {
        List<Usuario> listaUsuarios = usuarioService.obtenerUsuarios();
        ObservableList<Usuario> datosTabla = FXCollections.observableArrayList(listaUsuarios);
        tableUsuarios.setItems(datosTabla);

        int conteoActivos = 0;
        int conteoAdmins = 0;
        int conteoVendedores = 0;

        for (Usuario user : listaUsuarios) {
            if (user.getEstado() != null && user.getEstado()) conteoActivos++;

            if (user.getRol() != null) {
                if (user.getRol().name().equalsIgnoreCase("ADMIN")) conteoAdmins++;
                if (user.getRol().name().equalsIgnoreCase("VENDEDOR")) conteoVendedores++;
            }
        }

        lblUsuariosActivos.setText(String.valueOf(conteoActivos));
        lblUsuariosAdmin.setText(String.valueOf(conteoAdmins));
        lblUsuariosVendedor.setText(String.valueOf(conteoVendedores));
    }

    private void eliminarUsuarioSeleccionado(ActionEvent event) {
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            mostrarAlerta("Atención", "Ningún usuario seleccionado", "Por favor, selecciona un usuario de la tabla para eliminarlo.", Alert.AlertType.WARNING);
            return;
        }

        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar");
        alertaConfirmacion.setHeaderText("Eliminar Usuario");
        alertaConfirmacion.setContentText("¿Estás seguro de que deseas eliminar al usuario " + usuarioSeleccionado.getNombre() + " (RUT: " + usuarioSeleccionado.getRut() + ")?");

        alertaConfirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Usuario eliminado = usuarioService.eliminarUsuario(usuarioSeleccionado.getRut());

                if (eliminado != null) {
                    mostrarAlerta("Éxito", "Usuario eliminado", "El usuario fue borrado correctamente.", Alert.AlertType.INFORMATION);
                    cargarUsuarios();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar", "Hubo un error al intentar eliminar el usuario de la base de datos.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}