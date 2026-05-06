package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.Role;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.service.UsuarioService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

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

    // --- TABLA Y COLUMNAS CON SUS TIPOS CORREGIDOS ---
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

        // 2. Configurar las Columnas (los nombres deben coincidir con tu clase Usuario)
        // Nota: Si tu clase Usuario no tiene la variable "id", esta columna saldrá vacía.
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        // Darle un formato bonito a la columna de Estado (Boolean -> Texto)
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

        // 4. Asignar acción al botón eliminar
        btnEliminarUsuario.setOnAction(this::eliminarUsuarioSeleccionado);
    }

    // --- MÉTODOS PROPIOS ---

    private void cargarUsuarios() {
        // Pedir la lista al servicio
        List<Usuario> listaUsuarios = usuarioService.obtenerUsuarios();

        // Llenar la tabla
        ObservableList<Usuario> datosTabla = FXCollections.observableArrayList(listaUsuarios);
        tableUsuarios.setItems(datosTabla);

        // --- CALCULAR Y ACTUALIZAR LOS KPIs (Tarjetas de colores) ---
        int conteoActivos = 0;
        int conteoAdmins = 0;
        int conteoVendedores = 0;

        for (Usuario user : listaUsuarios) {
            if (user.getEstado() != null && user.getEstado()) conteoActivos++;

            // Asumiendo que en tu Enum se llaman ADMIN y VENDEDOR (ajusta si se llaman diferente)
            if (user.getRol() != null) {
                if (user.getRol().name().equalsIgnoreCase("ADMIN")) conteoAdmins++;
                if (user.getRol().name().equalsIgnoreCase("VENDEDOR")) conteoVendedores++;
            }
        }

        // Mostrar en las etiquetas
        lblUsuariosActivos.setText(String.valueOf(conteoActivos));
        lblUsuariosAdmin.setText(String.valueOf(conteoAdmins));
        lblUsuariosVendedor.setText(String.valueOf(conteoVendedores));
    }

    private void eliminarUsuarioSeleccionado(ActionEvent event) {
        // Obtener el usuario seleccionado en la tabla
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            mostrarAlerta("Atención", "Ningún usuario seleccionado", "Por favor, selecciona un usuario de la tabla para eliminarlo.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmar eliminación
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar");
        alertaConfirmacion.setHeaderText("Eliminar Usuario");
        alertaConfirmacion.setContentText("¿Estás seguro de que deseas eliminar al usuario " + usuarioSeleccionado.getNombre() + " (RUT: " + usuarioSeleccionado.getRut() + ")?");

        // Si el usuario presiona "Aceptar", procedemos a borrarlo
        alertaConfirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Borrar de la base de datos usando el RUT
                Usuario eliminado = usuarioService.eliminarUsuario(usuarioSeleccionado.getRut());

                if (eliminado != null) {
                    mostrarAlerta("Éxito", "Usuario eliminado", "El usuario fue borrado correctamente.", Alert.AlertType.INFORMATION);
                    cargarUsuarios(); // Recargar la tabla para que desaparezca visualmente
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar", "Hubo un error al intentar eliminar el usuario de la base de datos.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    // Método auxiliar para mostrar alertas en pantalla
    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}