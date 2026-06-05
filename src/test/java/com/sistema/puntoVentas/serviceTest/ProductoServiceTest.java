package com.sistema.puntoVentas.serviceTest;

import com.sistema.puntoventas.modelo.AuditoriaEvento;
import com.sistema.puntoventas.modelo.moduloProducto.Categoria;
import com.sistema.puntoventas.modelo.moduloProducto.Producto;
import com.sistema.puntoventas.modelo.moduloProducto.TipoProducto;
import com.sistema.puntoventas.modelo.moduloProducto.UnidadMedida;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;
import com.sistema.puntoventas.service.AuditoriaService;
import com.sistema.puntoventas.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private IProductoRepository productoRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private ProductoService productoService;

    @InjectMocks
    private Categoria categoria;

    @BeforeEach
    public void setUp(){
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombreCategoria("Bebidas");
        categoria.setDescripcion("Productos líquidos para consumo");

        categoria = new Categoria();
        categoria.setId(2);
        categoria.setNombreCategoria("ingredientes");
        categoria.setDescripcion("Productos para preparar alimentos");
    }

    @Test
    @DisplayName("Registrar producto exitoso")
    public void testRegistrarProductoExitoso()throws Exception{
        Producto producto1 = Producto.builder()
                .nombre("coca-cola")
                .precioCompra(1000.0)
                .precioVenta(1500.0) // Cumple el margen (> 10%)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        when(productoRepository.obtenerProductoPorNombre("coca-cola")).thenReturn(new ArrayList<>());
        when(productoRepository.registrarProducto(producto1)).thenReturn(true);
        when(auditoriaService.registrarEvento(any(AuditoriaEvento.class))).thenReturn(true);

        assertDoesNotThrow(() -> productoService.registrarProducto(producto1));

        // Verificamos que se llamó a guardar en la BD y a la auditoría
        verify(productoRepository, times(1)).registrarProducto(producto1);
        verify(auditoriaService, times(1)).registrarEvento(any(AuditoriaEvento.class));


    }


    @Test
    @DisplayName("Debería registrar producto SOLO_INVENTARIO forzando precio de venta a 0.0")
    void registrarProducto_SoloInventario_ForzarPrecioVentaCero() throws Exception {
        Producto productoInventario = Producto.builder()
                .nombre("Insumo de prueba")
                .precioCompra(5000.0)
                .precioVenta(9999.0)
                .stockActual(10)
                .stockMinimo(2)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.GRAMOS)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.SOLO_INVENTARIO) // Condición clave
                .build();

        when(productoRepository.obtenerProductoPorNombre(anyString())).thenReturn(new ArrayList<>());
        when(productoRepository.registrarProducto(productoInventario)).thenReturn(true);

        assertDoesNotThrow(() -> productoService.registrarProducto(productoInventario));

        // Verificamos que el Service realmente modificó el precio de venta a 0.0
        assertEquals(0.0, productoInventario.getPrecioVenta());
        System.out.println("Precio de venta forzado a: " + productoInventario.getPrecioVenta());
    }


    @Test
    @DisplayName("Registrar producto con margen de ganancia insuficiente")
    public void testRegistrarProductoErroneo() throws Exception{
        Producto producto2 = Producto.builder()
                .nombre("donuts")
                .precioCompra(1000.0)
                .precioVenta(1050.0) // No cumple el margen (> 10%)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        when(productoRepository.obtenerProductoPorNombre("donuts")).thenReturn(new ArrayList<>());
        when(productoRepository.registrarProducto(producto2)).thenReturn(true);
        when(auditoriaService.registrarEvento(any(AuditoriaEvento.class))).thenReturn(true);

        assertDoesNotThrow(() -> productoService.registrarProducto(producto2));

        verify(productoRepository, times(1)).registrarProducto(producto2);
        verify(auditoriaService, times(1)).registrarEvento(any(AuditoriaEvento.class));

    }

}
