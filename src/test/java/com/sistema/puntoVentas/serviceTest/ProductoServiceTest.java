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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    private Categoria categoria2;
    private Categoria categoria3;

    @BeforeEach
    public void setUp(){
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombreCategoria("Bebidas");
        categoria.setDescripcion("Productos líquidos para consumo");

        categoria2 = new Categoria();
        categoria.setId(2);
        categoria.setNombreCategoria("ingredientes");
        categoria.setDescripcion("Productos para preparar alimentos");

        categoria3 = new Categoria();
        categoria.setId(3);
        categoria.setNombreCategoria("pasteleria");
        categoria.setDescripcion("Productos dulces");
    }

    @Test
    @DisplayName("TC-01:Registrar producto exitoso")
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

        System.out.println("Test exitoso");


    }





    //Esperamos a que el guardado falle y lanze una excepción por el margen de ganancia insuficiente,
    // y que NUNCA se intente guardar en la base de datos ni registrar la auditoría
    @Test
    @DisplayName("TC-02:Registrar producto con margen de ganancia insuficiente")
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


        Exception excepcion = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto2);
        });

        assertEquals("Protección de Margen: El precio de venta debe ser al menos un 10% mayor al precio de compra.", excepcion.getMessage());


        // Verificamos que NUNCA se intentó guardar en la base de datos ni registrar la auditoría
        verify(productoRepository, never()).registrarProducto(any(Producto.class));
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-03:Intento de registrar un producto nulo")
    public void testRegistrarProductoNulo() throws Exception{
        Exception excepcion = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(null);
        });


        assertEquals("El producto no puede ser nulo", excepcion.getMessage());


        // Comprobamos y aseguramos que el sistema se protegió y NUNCA tocó tus repositorios ni auditorías
        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-04: Registrar producto con nombre vacío o puros espacios")
    public void testRegistrarProductoNombreVacio() throws Exception{

        Producto productoConNombreVacio = Producto.builder()
                .nombre("     ")
                .precioCompra(1000.0)
                .precioVenta(1500.0)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();


        Exception excepcion = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(productoConNombreVacio);
        });


        assertEquals("El nombre del producto es obligatorio.", excepcion.getMessage());


        // Confirmamos que el flujo se cortó inmediatamente y protegió la base de datos
        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-05: Registrar producto con precio compra inválido")
    public void testRegistrarProductoPrecioInvalido()throws Exception{

        Producto producto5 = Producto.builder()
                .nombre("empanada")
                .precioCompra(-300.0)
                .precioVenta(1500.0)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto5);
        });

        assertEquals("El precio de compra debe ser mayor a cero", exception.getMessage());


        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-06: Registrar producto con cantidad 0 para unidad de medida")
    public void testRegistrarProductoUnidadMedidaCero() throws Exception{
        Producto producto6 = Producto.builder()
                .nombre("harina")
                .precioCompra(500.0)
                .precioVenta(700.0)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.GRAMOS)
                .cantidad(0.0) // Cantidad inválida para unidad de medida
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto6);
        });

        assertEquals("La cantidad debe ser mayor a cero para unidades de medida en gramos o mililitros.", exception.getMessage());

        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-07: Registrar producto con stock actual negativo")
    public void testRegistrarProductoStockActualInvalido() throws Exception{
        Producto producto7 = Producto.builder()
                .nombre("azucar")
                .precioCompra(300.0)
                .precioVenta(500.0)
                .stockActual(-5) // Stock actual inválido
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.GRAMOS)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto7);
        });

        assertEquals("El stock actual debe ser mayor a cero", exception.getMessage());

        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");


    }


    @Test
    @DisplayName("TC-08: Registrar producto con stock mínimo negativo")
    public void testRegistrarProductoStockMinimoInvalido() throws Exception{
        Producto producto8 = Producto.builder()
                .nombre("sal")
                .precioCompra(200.0)
                .precioVenta(400.0)
                .stockActual(50)
                .stockMinimo(-3) // Stock mínimo inválido
                .categoria(categoria)
                .unidadMedida(UnidadMedida.GRAMOS)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto8);
        });

        assertEquals("El stock mínimo debe ser mayor a cero", exception.getMessage());

        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-09: Registrar producto sin categoría asignada")
    public void testRegistrarProductoSinCategoria() throws Exception{
        Producto producto8 = Producto.builder()
                .nombre("sal")
                .precioCompra(200.0)
                .precioVenta(400.0)
                .stockActual(50)
                .stockMinimo(3)
                .categoria(null) // Sin categoría
                .unidadMedida(UnidadMedida.GRAMOS)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();
        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto8);
        });

        assertEquals("El producto debe tener una categoría asignada", exception.getMessage());

        verifyNoInteractions(productoRepository);
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }

    @Test
    @DisplayName("TC-10: Registrar producto con nombre duplicado")
    public void testRegistrarProductoConNombreDuplicado() throws Exception{
        Producto producto10 = Producto.builder()
                .nombre("coca-cola")
                .precioCompra(1000.0)
                .precioVenta(1500.0)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();

        List<Producto> listaDuplicados = new ArrayList<>();
        listaDuplicados.add(producto10);

        when(productoRepository.obtenerProductoPorNombre("coca-cola")).thenReturn(listaDuplicados); // Simulamos que ya existe un producto con ese nombre

        Exception exception = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(producto10);
        });

        assertEquals("Ya existe un registro con el nombre '" + producto10.getNombre() , exception.getMessage());

        verify(productoRepository, times(1)).obtenerProductoPorNombre("coca-cola");
        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-11:Debería registrar producto SOLO_INVENTARIO forzando precio de venta a 0.0")
    public void registrarProducto_SoloInventario_ForzarPrecioVentaCero() throws Exception {
        Producto productoInventario = Producto.builder()
                .nombre("Insumo de prueba")
                .precioCompra(5000.0)
                .precioVenta(9999.0)
                .stockActual(10)
                .stockMinimo(2)
                .categoria(categoria2)
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
        System.out.println("Test exitoso");
    }


    @Test
    @DisplayName("TC-12: Error interno cuando el repositorio falla al guardar en SQLite")
    public void testRegistrarProductoErrorAlmacenamiento() throws Exception {

        Producto productoPerfecto = Producto.builder()
                .nombre("Agua Mineral")
                .precioCompra(1000.0)
                .precioVenta(1500.0)
                .stockActual(50)
                .stockMinimo(10)
                .categoria(categoria)
                .unidadMedida(UnidadMedida.UNIDAD)
                .cantidad(1.0)
                .activo(true)
                .tipoProducto(TipoProducto.DIRECTO)
                .build();


        when(productoRepository.obtenerProductoPorNombre("Agua Mineral")).thenReturn(new ArrayList<>());

        // Simulamos que el repositorio falla al intentar guardar el producto, devolviendo 'false'
        when(productoRepository.registrarProducto(productoPerfecto)).thenReturn(false);



        Exception excepcion = assertThrows(Exception.class, () -> {
            productoService.registrarProducto(productoPerfecto);
        });


        assertEquals("Error interno: No se pudo guardar el producto en la base de datos.", excepcion.getMessage());


        verify(productoRepository, times(1)).registrarProducto(productoPerfecto);


        verifyNoInteractions(auditoriaService);

        System.out.println("Test exitoso");
    }

    /*----------------------------------------------------------------------------------
    * ---------------------------------------------------------------------------------*/

}
