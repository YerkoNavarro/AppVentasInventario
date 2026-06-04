package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.Role;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.service.UsuarioService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelAgregarUsuarioController implements Initializable {

    @FXML private Button btnRegistrar;
    @FXML private ComboBox<Role> cmbRol;
    @FXML private Label lblEstado;
    @FXML private TextField txtApellido;
    @FXML private TextField txtContraseña;
    @FXML private TextField txtNombre;
    @FXML private TextField txtRut;
    @FXML private TextField txtTelefono;

    private UsuarioService usuarioService;
    private boolean modoEdicion = false;
    private Usuario usuarioAEditar = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioService = new UsuarioService();
        cmbRol.setItems(FXCollections.observableArrayList(Role.values()));
        lblEstado.setText("Listo para registrar");
    }

    public void cargarDatosUsuario(Usuario usuario) {
        this.modoEdicion = true;
        this.usuarioAEditar = usuario;

        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtRut.setText(usuario.getRut());
        txtContraseña.setText(usuario.getContraseña());
        txtTelefono.setText(usuario.getTelefono());
        cmbRol.setValue(usuario.getRol());

        txtRut.setEditable(false);
        txtRut.setStyle("-fx-background-color: #e0e0e0;");

        btnRegistrar.setText("Guardar Cambios");
        lblEstado.setText("Editando usuario: " + usuario.getRut());
    }

    @FXML
    void registrarUsuario(ActionEvent event) {
        try {
            String mensajeRespuesta;

            if (modoEdicion) {
                // El controlador es TONTO: solo mapea los valores limpiando espacios de los extremos
                usuarioAEditar.setNombre(txtNombre.getText().trim());
                usuarioAEditar.setApellido(txtApellido.getText().trim());
                usuarioAEditar.setContraseña(txtContraseña.getText().trim());
                usuarioAEditar.setTelefono(txtTelefono.getText().trim());
                usuarioAEditar.setRol(cmbRol.getValue());

                // Se delega por completo la validación y actualización al servicio
                mensajeRespuesta = usuarioService.actualizarUsuario(usuarioAEditar);

                if (mensajeRespuesta.equals("Usuario actualizado exitosamente")) {
                    mostrarAlerta("Éxito", "Actualización Completada", mensajeRespuesta, Alert.AlertType.INFORMATION);
                } else {
                    // Si el servicio rebota la acción por campos vacíos, se muestra el error aquí
                    mostrarAlerta("Atención", "Campos obligatorios faltantes", mensajeRespuesta, Alert.AlertType.WARNING);
                }

            } else {
                // Modo Creación: recolección básica de datos
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(txtNombre.getText().trim());
                nuevoUsuario.setApellido(txtApellido.getText().trim());
                nuevoUsuario.setRut(txtRut.getText().trim());
                nuevoUsuario.setContraseña(txtContraseña.getText().trim());
                nuevoUsuario.setTelefono(txtTelefono.getText().trim());
                nuevoUsuario.setRol(cmbRol.getValue());
                nuevoUsuario.setEstado(true);

                // Se delega por completo la validación y registro al servicio
                mensajeRespuesta = usuarioService.registrarNuevoUsuario(nuevoUsuario);

                if (mensajeRespuesta.equals("Usuario registrado exitosamente")) {
                    mostrarAlerta("Éxito", "Registro Completado", mensajeRespuesta, Alert.AlertType.INFORMATION);
                    limpiarCampos();
                } else {
                    mostrarAlerta("Atención", "Campos obligatorios faltantes", mensajeRespuesta, Alert.AlertType.WARNING);
                }
            }

        } catch (Exception e) {
            mostrarAlerta("Error fatal", "Excepción inesperada", "Ha ocurrido un error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtRut.clear();
        txtContraseña.clear();
        txtTelefono.clear();
        cmbRol.getSelectionModel().clearSelection();
        lblEstado.setText("Usuario registrado correctamente");
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}