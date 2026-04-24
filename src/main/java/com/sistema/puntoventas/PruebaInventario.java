package com.sistema.puntoventas;

import com.sistema.puntoventas.conexion.DbManager;
import com.sistema.puntoventas.modelo.MovimientoInventario;
import com.sistema.puntoventas.modelo.TipoMovimiento;
import com.sistema.puntoventas.repository.impl.MovimientoRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PruebaInventario {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("    INICIANDO PRUEBA DE BACKEND");
        System.out.println("==========================================");

        // 1. Inicializar la Base de Datos y las Tablas
        System.out.println("\n[Paso 1] Preparando Base de Datos...");
        DbManager dbManager = new DbManager();
        dbManager.crearTodasLasTablas();
        dbManager.crearUsuarioAdmin(); // Esto nos asegura que existe un usuario con ID para la prueba

        // 2. Crear un producto de prueba (Para evitar error de Foreign Key)
        System.out.println("\n[Paso 2] Insertando producto de prueba...");
        insertarProductoPrueba();

        // 3. Probar el Repositorio de Movimientos
        System.out.println("\n[Paso 3] Probando el registro de inventario...");
        MovimientoRepositoryImpl repoMovimientos = new MovimientoRepositoryImpl();

        // Creamos un movimiento: Una ENTRADA de 50 unidades
        MovimientoInventario nuevoMovimiento = new MovimientoInventario(
                1, // ID del producto (El que acabamos de crear)
                TipoMovimiento.ENTRADA,
                50, // Cantidad
                "Compra inicial a proveedor",
                1 // ID del usuario Admin
        );

        // A. Probamos guardar en el historial_inventario
        boolean registrado = repoMovimientos.registrarMovimiento(nuevoMovimiento);
        if (registrado) {
            System.out.println("✅ ÉXITO: El movimiento se guardó en historial_inventario.");
        } else {
            System.out.println("❌ ERROR: No se pudo guardar el movimiento.");
        }

        // B. Probamos actualizar el stock en la tabla producto (sumamos los 50)
        boolean stockActualizado = repoMovimientos.actualizarStockFisico(1, 50);
        if (stockActualizado) {
            System.out.println("✅ ÉXITO: El stock del producto se actualizó correctamente.");
        } else {
            System.out.println("❌ ERROR: No se pudo actualizar el stock del producto.");
        }

        // C. Probamos el sistema de alertas
        System.out.println("\n[Paso 4] Probando sistema de alertas...");
        // Simulamos una merma grande que deje el stock en 0 para forzar la alerta
        repoMovimientos.actualizarStockFisico(1, -60);
        repoMovimientos.generarAlertaStock();

        // 4. Verificación final directamente en la BD
        System.out.println("\n[Paso 5] Verificando datos finales en la BD...");
        mostrarDatosGuardados();
    }

    // --- MÉTODOS AUXILIARES SOLO PARA LA PRUEBA ---

    private static void insertarProductoPrueba() {
        String url = "jdbc:sqlite:DBventasInventario.db";
        // Usamos INSERT OR IGNORE para que no de error si corres la prueba varias veces
        String sql = "INSERT OR IGNORE INTO producto (id, nombre, precioCompra, precioVenta, categoria, stockActual, stockMinimo) " +
                "VALUES (1, 'Café de Grano 1Kg', 8000, 12000, 'Insumos', 10, 15)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("   -> Producto 'Café de Grano 1Kg' (ID: 1) creado/verificado.");
        } catch (SQLException e) {
            System.err.println("   -> Error al crear producto: " + e.getMessage());
        }
    }

    private static void mostrarDatosGuardados() {
        String url = "jdbc:sqlite:DBventasInventario.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            // Verificamos cómo quedó el stock
            var rsProd = conn.createStatement().executeQuery("SELECT nombre, stockActual FROM producto WHERE id = 1");
            if (rsProd.next()) {
                System.out.println("📦 ESTADO DEL PRODUCTO:");
                System.out.println("   - Nombre: " + rsProd.getString("nombre"));
                System.out.println("   - Stock Actual en BD: " + rsProd.getInt("stockActual"));
            }

            // Verificamos el último registro en el historial
            var rsHist = conn.createStatement().executeQuery("SELECT tipoMovimiento, cantidad, motivo FROM historial_inventario ORDER BY idMovimiento DESC LIMIT 1");
            if (rsHist.next()) {
                System.out.println("📝 ÚLTIMO MOVIMIENTO GUARDADO:");
                System.out.println("   - Tipo: " + rsHist.getString("tipoMovimiento"));
                System.out.println("   - Cantidad: " + rsHist.getInt("cantidad"));
                System.out.println("   - Motivo: " + rsHist.getString("motivo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer datos: " + e.getMessage());
        }
    }
}