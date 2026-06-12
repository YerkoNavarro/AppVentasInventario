package com.sistema.puntoVentas.serviceTest;

import com.sistema.puntoventas.modelo.BalanceFinancieroDTO;
import com.sistema.puntoventas.modelo.PrediccionStockDTO;
import com.sistema.puntoventas.modelo.RankingVendedoresDTO;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.modelo.moduloProducto.RankingProductosDTO;
import com.sistema.puntoventas.repository.IAuditoriaRepository;
import com.sistema.puntoventas.repository.IEstadisticasRepository;
import com.sistema.puntoventas.service.EstadisticaService;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock
    private IEstadisticasRepository estadisticasRepository;

    @Mock
    private IProductoRepository productoRepository;

    @Mock
    private IAuditoriaRepository auditoriaRepository;

    @InjectMocks
    private EstadisticaService estadisticaService;

    @Test
    @DisplayName("TC-01: Obtener ingresos totales exitoso")
    public void testObtenerIngresosTotales_Exitoso() {

        String periodo = "MES";
        when(estadisticasRepository.obtenerIngresosTotales(periodo)).thenReturn(5000.0);
        double ingresos = estadisticaService.obtenerIngresosTotales(periodo);
        assertEquals(5000.0, ingresos);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-02: Obtener balance con periodo nulo")
    public void testObtenerBalanceFinanciero_PeriodoNulo() {
        // Act
        BalanceFinancieroDTO balance = estadisticaService.obtenerBalance(null);

        // Assert
        assertEquals(0.0, balance.getIngresosTotales());
        assertEquals(0.0, balance.getPerdidasTotales());
        assertEquals(0.0, balance.getUtilidadNeta());
        // Verificamos que el repositorio nunca fue llamado para proteger la BD
        verifyNoInteractions(estadisticasRepository);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-03: Obtener balance financiero exitoso")
    public void testObtenerBalanceFinanciero_Exitoso() {

        String periodo = "MES";
        when(estadisticasRepository.obtenerIngresosTotales(periodo)).thenReturn(5000.0);
        when(estadisticasRepository.obtenerPerdidasTotales(periodo)).thenReturn(2000.0);


        BalanceFinancieroDTO balance = estadisticaService.obtenerBalance(periodo);


        assertNotNull(balance, "El balance devuelto no debería ser nulo");
        assertEquals(5000.0, balance.getIngresosTotales(), "Los ingresos no coinciden");
        assertEquals(2000.0, balance.getPerdidasTotales(), "Las pérdidas no coinciden");
        assertEquals(3000.0, balance.getUtilidadNeta(), "La utilidad neta (balance) debe ser 3000.0");


        verify(estadisticasRepository, times(1)).obtenerIngresosTotales(periodo);
        verify(estadisticasRepository, times(1)).obtenerPerdidasTotales(periodo);

        System.out.println("Test TC-03 exitoso");
    }

    @Test
    @DisplayName("TC-04: Obtener balance financiero con ingresos cero (Evitar error)")
    public void testObtenerBalanceFinanciero_IngresosCero() {
        // Arrange
        String periodo = "MES";
        when(estadisticasRepository.obtenerIngresosTotales(periodo)).thenReturn(0.0);
        when(estadisticasRepository.obtenerPerdidasTotales(periodo)).thenReturn(500.0);

        // Act
        BalanceFinancieroDTO balance = estadisticaService.obtenerBalance(periodo);

        // Assert
        assertEquals(0.0, balance.getIngresosTotales());
        assertEquals(500.0, balance.getPerdidasTotales());
        assertEquals(-500.0, balance.getUtilidadNeta()); // 0 - 500 = -500 (Pérdida)

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-05: Obtener ranking de productos exitoso")
    public void testObtenerRankingProductos_Exitoso() {

        List<RankingProductosDTO> listaMock = new ArrayList<>();
        listaMock.add(new RankingProductosDTO());
        when(estadisticasRepository.obtenerRankingProductos(10)).thenReturn(listaMock);

        List<RankingProductosDTO> resultado = estadisticaService.obtenerRankingProductos(10);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(estadisticasRepository, times(1)).obtenerRankingProductos(10);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-06: Obtener ranking de vendedores exitoso")
    public void testObtenerRankingVendedores_Exitoso() {

        List<RankingVendedoresDTO> listaMock = new ArrayList<>();
        listaMock.add(new RankingVendedoresDTO());
        when(estadisticasRepository.obtenerRankingVendedores(5)).thenReturn(listaMock);


        List<RankingVendedoresDTO> resultado = estadisticaService.obtenerRankingVendedores(5);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(estadisticasRepository, times(1)).obtenerRankingVendedores(5);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-07: Predicción retorna vacío si hay datos insuficientes (< 7 registros)")
    public void testPrediccionStock_DatosInsuficientes() {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(6);

        List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía cuando hay menos de 7 registros de stock");
        verify(estadisticasRepository, times(1)).prepararDatosStockParaIA();

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-08: Predicción retorna vacío si hay datos suficientes pero Python falla")
    public void testPrediccionStock_PythonFalla() {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía si el script Python falla");
        verify(estadisticasRepository, times(1)).prepararDatosStockParaIA();

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-09: Algoritmo de Riesgo Preventivo (días para agotarse entre 7 y 14)")
    public void testPrediccionStock_RiesgoPreventivo() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,2.0\n".getBytes(StandardCharsets.UTF_8));

        Producto producto = Producto.builder()
                .id(101)
                .nombre("Insumo Preventivo")
                .stockActual(20)
                .stockMinimo(10)
                .build();

        Map<Integer, Producto> productosMap = new HashMap<>();
        productosMap.put(101, producto);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(productosMap);

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());

            PrediccionStockDTO prediccion = resultado.get(0);
            assertEquals(101, prediccion.getIdProducto());
            assertEquals("Insumo Preventivo", prediccion.getNombreProducto());
            assertEquals(20, prediccion.getStockActual());
            assertEquals(10, prediccion.getDiasParaAgotarse());
            assertEquals(0.3, prediccion.getIndiceRiesgo(), "El riesgo preventivo debe ser 0.3");
            assertEquals(0, prediccion.getCantidadSugerida(), "No debe sugerir compra en riesgo preventivo");
        }
    }

    @Test
    @DisplayName("TC-10: Algoritmo de Riesgo Bajo (días para agotarse > 14)")
    public void testPrediccionStock_RiesgoBajo() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,2.0\n".getBytes(StandardCharsets.UTF_8));

        Producto producto = Producto.builder()
                .id(101)
                .nombre("Insumo Saludable")
                .stockActual(30)
                .stockMinimo(10)
                .build();

        Map<Integer, Producto> productosMap = new HashMap<>();
        productosMap.put(101, producto);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(productosMap);

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());

            PrediccionStockDTO prediccion = resultado.get(0);
            assertEquals(101, prediccion.getIdProducto());
            assertEquals("Insumo Saludable", prediccion.getNombreProducto());
            assertEquals(30, prediccion.getStockActual());
            assertEquals(15, prediccion.getDiasParaAgotarse());
            assertEquals(0.1, prediccion.getIndiceRiesgo(), "El riesgo bajo debe ser 0.1");
            assertEquals(0, prediccion.getCantidadSugerida(), "No debe sugerir compra para evitar sobre-stock");
        }
    }

    @Test
    @DisplayName("TC-11: Control de Demanda Nula (producto sin ventas históricas)")
    public void testPrediccionStock_DemandaNula() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,0.0\n".getBytes(StandardCharsets.UTF_8));

        Producto producto = Producto.builder()
                .id(101)
                .nombre("Producto Sin Ventas")
                .stockActual(50)
                .stockMinimo(10)
                .build();

        Map<Integer, Producto> productosMap = new HashMap<>();
        productosMap.put(101, producto);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(productosMap);

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());

            PrediccionStockDTO prediccion = resultado.get(0);
            assertEquals(101, prediccion.getIdProducto());
            assertEquals("Producto Sin Ventas", prediccion.getNombreProducto());
            assertEquals(50, prediccion.getStockActual());
            assertEquals(999, prediccion.getDiasParaAgotarse(), "Sin demanda debe asignar 999 días de holgura");
            assertEquals(0.1, prediccion.getIndiceRiesgo(), "El riesgo debe ser bajo (0.1)");
            assertEquals(0, prediccion.getCantidadSugerida(), "No debe sugerir compra para evitar sobre-stock");
        }
    }

    @Test
    @DisplayName("TC-12: Redondeo de Abastecimiento (cálculo fraccionario con Math.ceil)")
    public void testPrediccionStock_RedondeoAbastecimiento() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,2.5\n".getBytes(StandardCharsets.UTF_8));

        Producto producto = Producto.builder()
                .id(101)
                .nombre("Insumo Fraccionario")
                .stockActual(16)
                .stockMinimo(10)
                .build();

        Map<Integer, Producto> productosMap = new HashMap<>();
        productosMap.put(101, producto);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(productosMap);

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertFalse(resultado.isEmpty());
            assertEquals(1, resultado.size());

            PrediccionStockDTO prediccion = resultado.get(0);
            assertEquals(101, prediccion.getIdProducto());
            assertEquals("Insumo Fraccionario", prediccion.getNombreProducto());
            assertEquals(16, prediccion.getStockActual());
            assertEquals(6, prediccion.getDiasParaAgotarse());
            assertEquals(0.5, prediccion.getIndiceRiesgo(), "El riesgo medio debe ser 0.5");
            assertEquals(12, prediccion.getCantidadSugerida(), "Debe redondear 11.5 hacia arriba (Math.ceil) → 12");
        }
    }

    @Test
    @DisplayName("TC-13: Colecciones Vacías (inventario vacío retorna lista vacía sin NPE)")
    public void testPrediccionStock_ColeccionesVacias() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(new HashMap<>());

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertTrue(resultado.isEmpty(), "Con inventario vacío debe retornar lista vacía, no NPE");
        }
    }

    @Test
    @DisplayName("TC-14: Trazabilidad de Auditoría Batch (registra evento en persistencia)")
    public void testPrediccionStock_AuditoriaBatch() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,2.0\n102,1.5\n".getBytes(StandardCharsets.UTF_8));

        Producto producto1 = Producto.builder().id(101).nombre("Producto A").stockActual(20).stockMinimo(10).build();
        Producto producto2 = Producto.builder().id(102).nombre("Producto B").stockActual(15).stockMinimo(5).build();

        Map<Integer, Producto> productosMap = new HashMap<>();
        productosMap.put(101, producto1);
        productosMap.put(102, producto2);

        when(estadisticasRepository.obtenerProductosPorIds(anyList())).thenReturn(productosMap);

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertFalse(resultado.isEmpty());
            assertEquals(2, resultado.size());

            assertEquals("Producto A", resultado.get(0).getNombreProducto());
            assertEquals("Producto B", resultado.get(1).getNombreProducto());
        }

        verify(auditoriaRepository, times(1)).registrarEvento(argThat(evento ->
                "INVENTARIO".equals(evento.getModulo()) &&
                "BATCH_PREDICCION".equals(evento.getAccion()) &&
                "STOCK".equals(evento.getEntidad()) &&
                evento.getDetalle() != null &&
                evento.getDetalle().contains("2")
        ));
    }

    @Test
    @DisplayName("TC-15: Resiliencia ante Fallos Críticos (SQLException en BD no propaga error)")
    public void testPrediccionStock_SQLException() throws Exception {
        when(estadisticasRepository.prepararDatosStockParaIA()).thenReturn(7);

        InputStream inputStream = new ByteArrayInputStream("101,2.0\n".getBytes(StandardCharsets.UTF_8));

        when(estadisticasRepository.obtenerProductosPorIds(anyList()))
                .thenThrow(new SQLException("Base de datos desconectada - SQLite"));

        try (MockedConstruction<ProcessBuilder> mocked = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    Process process = mock(Process.class);
                    when(mock.start()).thenReturn(process);
                    when(process.getInputStream()).thenReturn(inputStream);
                    when(process.waitFor()).thenReturn(0);
                })) {

            List<PrediccionStockDTO> resultado = estadisticaService.ejecutarPrediccionStock();

            assertTrue(resultado.isEmpty(), "Debe retornar lista vacía ante SQLException, no propagar el error");
        }
    }
}
