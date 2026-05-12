package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.BalanceFinancieroDTO;
import com.sistema.puntoventas.modelo.PrediccionStock;
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
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.impl.VentaRepositoryimpl;
import com.sistema.puntoventas.modelo.venta;

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
    private VBox vboxProductosTop;

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
    }

    private void cargarReporteFinanciero() {
        String periodo = LocalDate.now().toString().substring(0, 7);

        BalanceFinancieroDTO reporte = estadisticaService.obtenerBalance(periodo);

        if (lblIngresosTotales != null && lblUtilidadNeta != null && lblPerdidasTotales != null && lblTicketPromedio != null) {
            lblIngresosTotales.setText(String.format("₡%.2f", reporte.getIngresosTotales()));
            lblPerdidasTotales.setText(String.format("₡%.2f", reporte.getPerdidasTotales()));
            lblUtilidadNeta.setText(String.format("₡%.2f", reporte.getUtilidadNeta()));
            
            // Calcular ticket promedio: ingresos / cantidad de transacciones (estimado)
            double ticketPromedio = reporte.getIngresosTotales() > 0 ? reporte.getIngresosTotales() / 20.0 : 0;
            lblTicketPromedio.setText(String.format("₡%.2f", ticketPromedio));
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

        try {
            UsuarioService usuarioService = new UsuarioService();
            List<Usuario> usuarios = usuarioService.obtenerUsuarios();

            VentaRepositoryimpl ventaRepo = new VentaRepositoryimpl();
            List<venta> ventas = ventaRepo.obtenerVentas();

            // Contabilizar ventas por idUsuario
            Map<Integer, Integer> ventasPorUsuario = new HashMap<>();
            for (venta v : ventas) {
                if (v == null) continue;
                int idU = v.getIdUsuario();
                ventasPorUsuario.put(idU, ventasPorUsuario.getOrDefault(idU, 0) + 1);
            }

            // Ordenar usuarios por cantidad de ventas descendente y mostrar top 5
            List<Usuario> topUsuarios = usuarios.stream()
                    .sorted(Comparator.comparingInt((Usuario u) -> ventasPorUsuario.getOrDefault(u.getId(), 0)).reversed())
                    .limit(5)
                    .collect(Collectors.toList());

            if (topUsuarios.isEmpty()) {
                Label lbl = new Label("No hay vendedores o no se han registrado ventas aún.");
                vboxVentasUsuarios.getChildren().add(lbl);
                return;
            }

            for (Usuario u : topUsuarios) {
                int cantidad = ventasPorUsuario.getOrDefault(u.getId(), 0);
                Label lbl = new Label(u.getNombre() + " " + u.getApellido() + " — " + cantidad + " ventas");
                vboxVentasUsuarios.getChildren().add(lbl);
            }

        } catch (Exception e) {
            System.err.println("Error al cargar ventas por usuario: " + e.getMessage());
            Label lbl = new Label("Error al cargar datos de vendedores");
            vboxVentasUsuarios.getChildren().add(lbl);
        }
    }

    private void cargarRankingProductos() {
        vboxProductosTop.getChildren().clear();

        List<RankingProductosDTO> rankingProductos = estadisticaService.obtenerRankingProductos(10);

        if (rankingProductos == null || rankingProductos.isEmpty()) {
            Label lblSinDatos = new Label("No hay datos de ventas para mostrar el ranking de productos.");
            vboxProductosTop.getChildren().add(lblSinDatos);
            return;
        }

        for (int i = 0; i < rankingProductos.size(); i++) {
            RankingProductosDTO producto = rankingProductos.get(i);
            
            HBox hboxProducto = new HBox(15);
            hboxProducto.setPadding(new Insets(8, 10, 8, 10));

            Label lblPosicion = new Label("#" + (i + 1));
            lblPosicion.setMinWidth(30);

            Label lblNombre = new Label(producto.getNombreProducto());
            lblNombre.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lblNombre, javafx.scene.layout.Priority.ALWAYS);

            Label lblCantidad = new Label("Vendidas: " + producto.getCantidadVendida());
            lblCantidad.setMinWidth(100);

            hboxProducto.getChildren().addAll(lblPosicion, lblNombre, lblCantidad);
            vboxProductosTop.getChildren().add(hboxProducto);
        }
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
