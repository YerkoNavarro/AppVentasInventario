package com.sistema.puntoVentas.serviceTest;

import com.sistema.puntoventas.modelo.BalanceFinancieroDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;
import com.sistema.puntoventas.service.EstadisticaService;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock
    private IEstadisticasRepository estadisticasRepository;

    @Mock
    private IProductoRepository productoRepository;

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
}
