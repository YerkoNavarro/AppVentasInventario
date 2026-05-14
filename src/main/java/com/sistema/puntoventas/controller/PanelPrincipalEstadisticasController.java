package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.sistema.puntoventas.modelo.moduloProducto.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import com.sistema.puntoventas.repository.impl.EstadisticasRepositoryImpl;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;
import com.sistema.puntoventas.service.EstadisticaService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.sistema.puntoventas.service.UsuarioService;
import com.sistema.puntoventas.repository.impl.VentaRepositoryimpl;

public class PanelPrincipalEstadisticasController implements Initializable {

    @FXML
    private Label lblIngresosTotales;

    @FXML
    private Label lblUtilidadNeta;

    @FXML
    private Label lblPerdidasTotales;

    @FXML
    private Label lblTicketPromedio;

    @FXML
    private TableView<RankingProductosDTO> tableRankingProductos;

    @FXML
    private TableColumn<RankingProductosDTO, String> colRankingProducto;

    @FXML
    private TableColumn<RankingProductosDTO, Integer> colRankingCantidad;

    @FXML
    private VBox vboxVentasUsuarios;

    @FXML
    private VBox vboxHistorial;

    @FXML
    private TableView<PrediccionStock> tablePrediccionStock;

    @FXML
    private TableColumn<PrediccionStock, String> colNombre;

    @FXML
    private TableColumn<PrediccionStock, Integer> colStockActual;

    @FXML
    private TableColumn<PrediccionStock, Integer> colDiasRestantes;

    @FXML
    private TableColumn<PrediccionStock, Double> colIndiceRiesgo;

    @FXML
    private TableColumn<PrediccionStock, String> colSugerenciaCompra;

    private EstadisticaService estadisticaService;
    private Timeline actualizacionAutomatica;
    private static final int INTERVALO_ACTUALIZACION = 30; // segundos

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IEstadisticasRepository estadisticasRepository = new EstadisticasRepositoryImpl();
        IProductoRepository productoRepository = new ProductoRepositoryImpl();
        this.estadisticaService = new EstadisticaService(estadisticasRepository, productoRepository);

        // Configurar columnas de la tabla
        configurarColumnasTabla();

        // Cargar datos iniciales
        cargarReporteFinanciero();
        cargarPrediccionStock();
        cargarRankingProductos();
        cargarVentasUsuarios();
        cargarHistorialActividad();

        // Iniciar actualización automática en tiempo real
        iniciarActualizacionAutomatica();
    }

    private void iniciarActualizacionAutomatica() {
        // Crear Timeline que se ejecuta cada 30 segundos
        actualizacionAutomatica = new Timeline(new KeyFrame(Duration.seconds(INTERVALO_ACTUALIZACION), event -> {
            cargarReporteFinanciero();
            cargarPrediccionStock();
            cargarRankingProductos();
            cargarVentasUsuarios();
            cargarHistorialActividad();
        }));
        
        // El Timeline se repite indefinidamente
        actualizacionAutomatica.setCycleCount(Timeline.INDEFINITE);
        actualizacionAutomatica.play();
    }

    public void detenerActualizacion() {
        if (actualizacionAutomatica != null) {
            actualizacionAutomatica.stop();
        }
    }

    private void configurarColumnasTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colDiasRestantes.setCellValueFactory(new PropertyValueFactory<>("diasParaAgotarse"));
        colIndiceRiesgo.setCellValueFactory(new PropertyValueFactory<>("indiceRiesgo"));
        colSugerenciaCompra.setCellValueFactory(new PropertyValueFactory<>("cantidadSugerida"));

        if (colRankingProducto != null && colRankingCantidad != null) {
            colRankingProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
            colRankingCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadVendida"));
        }

        if (tableRankingProductos != null) {
            tableRankingProductos.setPlaceholder(new Label("No hay ventas registradas aún."));
        }
    }

    private void cargarReporteFinanciero() {
        String periodo = LocalDate.now().toString().substring(0, 7);

        BalanceFinancieroDTO reporte = estadisticaService.obtenerBalance(periodo);

        if (lblIngresosTotales != null && lblUtilidadNeta != null && lblPerdidasTotales != null && lblTicketPromedio != null) {
            lblIngresosTotales.setText(String.format("$ %.1f", reporte.getIngresosTotales()));
            lblPerdidasTotales.setText(String.format("$ %.1f", reporte.getPerdidasTotales()));
            lblUtilidadNeta.setText(String.format("$ %.1f", reporte.getUtilidadNeta()));
            
            // Calcular ticket promedio: ingresos / cantidad de transacciones (estimado)
            double ticketPromedio = reporte.getIngresosTotales() > 0 ? reporte.getIngresosTotales() / 20.0 : 0;
            lblTicketPromedio.setText(String.format("$ %.2f", ticketPromedio));
        }
    }

    private void cargarPrediccionStock() {
        try {
            List<PrediccionStock> predicciones = estadisticaService.ejecutarPrediccionStock();

            if (predicciones != null && !predicciones.isEmpty()) {
                tablePrediccionStock.getItems().setAll(predicciones);
            } else {
                tablePrediccionStock.getItems().clear();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar predicción de stock: " + e.getMessage());
        }
    }

    private void cargarVentasUsuarios() {
        vboxVentasUsuarios.getChildren().clear();

        List<RankingVendedoresDTO> rankingVendedores = estadisticaService.obtenerRankingVendedores(5);

        if(rankingVendedores.isEmpty()){
            vboxVentasUsuarios.getChildren().add(new Label("No hay datos de ventas por usuario para mostrar."));
            return;
        }

        for (RankingVendedoresDTO vendedor : rankingVendedores){
            Label lbl = new Label(vendedor.getNombreVendedor() + " — " + vendedor.getCantidadVentas() + " ventas");
            vboxVentasUsuarios.getChildren().add(lbl);
        }

    }

    private void cargarRankingProductos() {
        List<RankingProductosDTO> topProductos = estadisticaService.obtenerRankingProductos(5);

        if (tableRankingProductos == null) {
            return;
        }

        if (topProductos.isEmpty()) {
            tableRankingProductos.getItems().clear();
            return;
        }

        tableRankingProductos.getItems().setAll(topProductos);
    }

    private void cargarHistorialActividad() {
        vboxHistorial.getChildren().clear();

        for (int i = 1; i <= 5; i++) {
            HBox hboxActividad = new HBox(15);
            hboxActividad.setPadding(new Insets(8, 10, 8, 10));

            Label lblFecha = new Label("2026-05-" + String.format("%02d", i) + " 10:30");
            lblFecha.setMinWidth(150);

            Label lblAccion = new Label("Cambio en precio de Producto " + i);
            lblAccion.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lblAccion, javafx.scene.layout.Priority.ALWAYS);

            Label lblTipo = new Label("Modificación");
            lblTipo.setMinWidth(100);

            hboxActividad.getChildren().addAll(lblFecha, lblAccion, lblTipo);
            vboxHistorial.getChildren().add(hboxActividad);
        }
    }
}
