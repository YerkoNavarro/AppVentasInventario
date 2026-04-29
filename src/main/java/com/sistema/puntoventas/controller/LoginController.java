package com.sistema.puntoventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField rutTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button continuarButton;

    @FXML
    public void handleLogin() {
        String rut = rutTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (rut.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos",
                "Por favor, ingresa tu RUT y contraseña");
            return;
        }

        mostrarAlerta(Alert.AlertType.INFORMATION, "Login",
            "RUT: " + rut);
        passwordField.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
