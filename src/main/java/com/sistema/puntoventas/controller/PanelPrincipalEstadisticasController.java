package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.BalanceFinancieroDTO;
import com.sistema.puntoventas.util.MensajesAlerta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import com.sistema.puntoventas.util.MensajesAlerta;

import com.sistema.puntoventas.modelo.moduloProducto.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import com.sistema.puntoventas.repository.impl.EstadisticasRepositoryImpl;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;
import com.sistema.puntoventas.service.EstadisticaService;
import com.sistema.puntoventas.service.ProductoService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PanelPrincipalEstadisticasController implements Initializable {

    @FXML
    private Label lblIngresosTotales;

    @FXML
    private Label lblUtilidadNeta;

    @FXML
    private Label lblPerdidasTotales;

    @FXML
    private VBox vboxProductosTop;

    @FXML
    private VBox vboxVentasUsuarios;

    private EstadisticaService estadisticaService;
    private ProductoService productoService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IEstadisticasRepository estadisticasRepository = new EstadisticasRepositoryImpl();
        IProductoRepository productoRepository = new ProductoRepositoryImpl();
        this.estadisticaService = new EstadisticaService(estadisticasRepository, productoRepository);
        //this.productoService = new ProductoService(productoRepository);
        cargarReporteFinanciero();
        cargarIngresosTotales();
        cargarRankingProductos();
        cargarVentasUsuarios();
    }

    private void cargarReporteFinanciero() {

        String periodo = LocalDate.now().toString().substring(0, 7);

        // Traemos todo el paquete de datos desde el Service en una sola llamada
        BalanceFinancieroDTO reporte = estadisticaService.obtenerBalance(periodo);


        if (lblIngresosTotales != null && lblUtilidadNeta != null && lblPerdidasTotales != null) {

            // Usamos formato para moneda (₡) con 2 decimales
            lblIngresosTotales.setText(String.format("₡%.2f", reporte.getIngresosTotales()));
            lblPerdidasTotales.setText(String.format("₡%.2f", reporte.getPerdidasTotales()));
            lblUtilidadNeta.setText(String.format("₡%.2f", reporte.getUtilidadNeta()));


            if (reporte.getUtilidadNeta() < 0) {
                lblUtilidadNeta.setStyle("-fx-text-fill: #ffb3b3;"); // Un rojo suave
            } else {
                lblUtilidadNeta.setStyle("-fx-text-fill: #ffffff;"); // Blanco normal
            }
        }
    }




    private void cargarVentasUsuarios() {
        // Por ahora mostramos entradas de ejemplo para evitar dependencias con la BD
        vboxVentasUsuarios.getChildren().clear();

        for (int i = 1; i <= 5; i++) {
            Label lbl = new Label("Usuario " + i + " — 0 ventas");
            lbl.setFont(new Font("Arial", 14));
            vboxVentasUsuarios.getChildren().add(lbl);
        }
    }

    private void cargarIngresosTotales() {
        String periodo = LocalDate.now().toString().substring(0, 7); // Formato "YYYY-MM"
        double ingresosTotales = estadisticaService.obtenerIngresosTotales(periodo);
        lblIngresosTotales.setText("Ingresos Totales (" + periodo + "): $" + ingresosTotales);
    }

    private void cargarRankingProductos() {
        List<RankingProductosDTO> rankingProductos = estadisticaService.obtenerRankingProductos(10);

        if (rankingProductos.isEmpty()) {
            Label lblSinDatos = new Label("No hay datos de ventas para mostrar el ranking de productos.");
            lblSinDatos.setFont(new Font("Arial", 16));
            vboxProductosTop.getChildren().add(lblSinDatos);
            return;
        }

        for (RankingProductosDTO producto : rankingProductos) {
            HBox hboxProducto = new HBox(10);
            hboxProducto.setPadding(new Insets(5));

            Label lblNombre = new Label(producto.getNombreProducto());
            lblNombre.setFont(new Font("Arial", 14));
            //Label lblCantidadVendida = new Label("Cantidad Vendida: " + producto.getTotalCantidadVendida());
            //lblCantidadVendida.setFont(new Font("Arial", 14));

            //hboxProducto.getChildren().addAll(lblNombre, lblCantidadVendida);
            vboxProductosTop.getChildren().add(hboxProducto);
        }
    }
}
