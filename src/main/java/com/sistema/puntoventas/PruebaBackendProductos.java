package com.sistema.puntoventas;

import com.sistema.puntoventas.conexion.DbManager;
import com.sistema.puntoventas.modelo.Categoria;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.TipoProducto;
import com.sistema.puntoventas.modelo.UnidadMedida;
import com.sistema.puntoventas.service.ProductoService;

public class PruebaBackendProductos {
    public static void main(String[] args) {

        System.out.println("--- INICIANDO PRUEBA DE BASE DE DATOS ---");



        ProductoService productoService = new ProductoService();

        Categoria categoria1  =new Categoria(1,true, "Bebidas calientes y frías", "Bebidas");
        Categoria categoria2  =new Categoria(2,true, "pasteles", "pasteleria");
        Producto producto1 = new Producto(
                4,
                "pie de limon",
                1000,
                1500,
                categoria2,
                "2026-10-01",
                100,
                1,
                "cafe_americano.jpg",
                UnidadMedida.UNIDAD,
                TipoProducto.PLATILLO
        );


        // 3. Intentamos guardarlo usando el Service
        try {
            System.out.println("Enviando producto al servicio...");

           /* productoService.eliminarProducto(1);
            productoService.eliminarCategoria(1);
            productoService.eliminarCategoria(2);*/

            // Llamamos a tu método registrarProducto
            productoService.registrarProducto(producto1);
            productoService.registrarCategoria(categoria1);
            productoService.registrarCategoria(categoria2);


            System.out.println("¡ÉXITO! El producto '" + producto1.getNombre() + "' fue guardado en la BD.");
            System.out.println("¡ÉXITO! La categoria '" + categoria1.getNombreCategoria() + "' fue guardado en la BD.");
            System.out.println("¡ÉXITO! La categoria '" + categoria2.getNombreCategoria() + "' fue guardado en la BD.");

        } catch (Exception e) {
            // Si alguna validación falla (ej. precio <= 0, o nombre duplicado), caerá aquí
            System.err.println(" ERROR: " + e.getMessage());
        }

        System.out.println("--- FIN DE LA PRUEBA ---");
    }
}
