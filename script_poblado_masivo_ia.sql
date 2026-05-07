-- Script SQLite para recrear y poblar la base de datos del proyecto puntoVentas.
-- Está pensado para pruebas de IA predictiva, stock y reportes.

PRAGMA foreign_keys = OFF;

PRAGMA foreign_keys = ON;

BEGIN TRANSACTION;

INSERT INTO categoria (id, nombreCategoria, descripcion, activa) VALUES
(1, 'Bebidas', 'Bebidas frías y calientes para venta directa o preparación', 1),
(2, 'Panadería', 'Productos base de panadería y masas', 1),
(3, 'Comida Rápida', 'Platillos listos para consumo inmediato', 1),
(4, 'Insumos', 'Materia prima e ingredientes de cocina', 1),
(5, 'Proteínas', 'Carnes y fuentes de proteína', 1),
(6, 'Verduras', 'Vegetales frescos para preparación', 1),
(7, 'Salsas', 'Salsas y acompañamientos', 1),
(8, 'Postres', 'Productos dulces y postres', 1);

INSERT INTO usuario (id, nombre, apellido, rut, password, telefono, rol, estado) VALUES
(1, 'Admin', 'Sistema', '12345678-9', 'admin', '999999999', 'ADMIN', 1),
(2, 'Ana', 'Pérez', '11111111-1', '1234', '912345678', 'USER', 1),
(3, 'Luis', 'Gómez', '22222222-2', '1234', '923456789', 'USER', 1),
(4, 'Marta', 'Soto', '33333333-3', '1234', '934567890', 'USER', 1),
(5, 'Diego', 'Rojas', '44444444-4', '1234', '945678901', 'USER', 1),
(6, 'Carla', 'Navarro', '55555555-5', '1234', '956789012', 'USER', 1),
(7, 'Pedro', 'Castro', '66666666-6', '1234', '967890123', 'USER', 1),
(8, 'Valentina', 'Diaz', '77777777-7', '1234', '978901234', 'USER', 1);

INSERT INTO producto (id, nombre, precioCompra, precioVenta, idCategoria, fechaVenc, stockActual, stockMinimo, imagen, unidadMedida, cantidad, tipoProducto) VALUES
(1, 'Harina Panadera', 1200, 1800, 4, '2026-12-31', 180, 50, 'harina.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(2, 'Azucar Blanca', 900, 1500, 4, '2026-12-31', 160, 40, 'azucar.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(3, 'Aceite Vegetal', 3500, 5200, 4, '2026-12-31', 90, 20, 'aceite.png', 'MILILITROS', 1000, 'SOLO_INVENTARIO'),
(4, 'Leche Entera', 1200, 1800, 1, '2026-10-31', 140, 30, 'leche.png', 'MILILITROS', 1000, 'DIRECTO'),
(5, 'Cafe Molido', 5000, 7800, 1, '2026-12-31', 75, 15, 'cafe.png', 'GRAMOS', 250, 'SOLO_INVENTARIO'),
(6, 'Bebida Cola', 900, 1500, 1, '2026-09-30', 300, 80, 'cola.png', 'UNIDAD', 1, 'DIRECTO'),
(7, 'Agua Mineral', 700, 1200, 1, '2026-09-30', 260, 60, 'agua.png', 'UNIDAD', 1, 'DIRECTO'),
(8, 'Jugo Naranja', 1100, 1700, 1, '2026-11-30', 180, 50, 'jugo.png', 'UNIDAD', 1, 'DIRECTO'),
(9, 'Pan Hamburguesa', 450, 900, 2, '2026-08-31', 200, 40, 'pan_hamburguesa.png', 'UNIDAD', 1, 'SOLO_INVENTARIO'),
(10, 'Pan HotDog', 400, 800, 2, '2026-08-31', 180, 40, 'pan_hotdog.png', 'UNIDAD', 1, 'SOLO_INVENTARIO'),
(11, 'Pollo Desmenuzado', 6500, 9200, 5, '2026-07-15', 90, 20, 'pollo.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(12, 'Carne Molida', 7200, 9800, 5, '2026-07-15', 85, 20, 'carne.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(13, 'Queso Mozzarella', 8000, 11000, 5, '2026-07-15', 70, 15, 'queso.png', 'GRAMOS', 500, 'SOLO_INVENTARIO'),
(14, 'Lechuga Fresca', 1500, 2400, 6, '2026-06-20', 55, 15, 'lechuga.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(15, 'Tomate', 1800, 2600, 6, '2026-06-20', 60, 20, 'tomate.png', 'GRAMOS', 1000, 'SOLO_INVENTARIO'),
(16, 'Salsa Tomate', 1300, 2200, 7, '2026-12-31', 140, 30, 'salsa.png', 'MILILITROS', 1000, 'SOLO_INVENTARIO'),
(17, 'Papas Fritas Bolsa', 1600, 2600, 3, '2026-09-15', 220, 50, 'papas.png', 'UNIDAD', 1, 'DIRECTO'),
(18, 'Empanada Pollo', 2200, 3500, 3, '2026-05-30', 65, 15, 'empanada.png', 'UNIDAD', 1, 'PLATILLO'),
(19, 'Hamburguesa Clasica', 3500, 6500, 3, '2026-05-30', 55, 12, 'hamburguesa.png', 'UNIDAD', 1, 'PLATILLO'),
(20, 'Pizza Familiar', 8000, 15000, 3, '2026-05-30', 30, 8, 'pizza.png', 'UNIDAD', 1, 'PLATILLO'),
(21, 'Ensalada Cesar', 2800, 5200, 3, '2026-05-30', 40, 10, 'ensalada.png', 'UNIDAD', 1, 'PLATILLO'),
(22, 'Cafe Latte', 2500, 4500, 1, '2026-05-30', 70, 18, 'latte.png', 'UNIDAD', 1, 'PLATILLO'),
(23, 'Torta Chocolate', 12000, 22000, 8, '2026-05-30', 20, 5, 'torta.png', 'UNIDAD', 1, 'PLATILLO'),
(24, 'Hot Dog Especial', 2400, 4600, 3, '2026-05-30', 48, 10, 'hotdog.png', 'UNIDAD', 1, 'PLATILLO'),
(25, 'Brownie con Helado', 2800, 5400, 8, '2026-05-30', 35, 8, 'brownie.png', 'UNIDAD', 1, 'PLATILLO');

INSERT INTO platillo (id, nombre, precio, idCategoria, estado, costoProduccion, stockActual, tipoProducto) VALUES
(1, 'Empanada Pollo', 3500, 3, 1, 1600, 18, 'PLATILLO'),
(2, 'Hamburguesa Clasica', 6500, 3, 1, 2900, 22, 'PLATILLO'),
(3, 'Pizza Familiar', 15000, 3, 1, 7500, 10, 'PLATILLO'),
(4, 'Ensalada Cesar', 5200, 3, 1, 2100, 15, 'PLATILLO'),
(5, 'Cafe Latte', 4500, 1, 1, 1400, 25, 'PLATILLO'),
(6, 'Torta Chocolate', 22000, 8, 1, 9800, 8, 'PLATILLO'),
(7, 'Hot Dog Especial', 4600, 3, 1, 2100, 16, 'PLATILLO'),
(8, 'Brownie con Helado', 5400, 8, 1, 2400, 14, 'PLATILLO');

INSERT INTO detalle_platillo (id, idPlatillo, idProducto, cantidadIngrediente) VALUES
(1, 1, 1, 200),
(2, 1, 11, 120),
(3, 1, 16, 15),
(4, 2, 9, 1),
(5, 2, 12, 150),
(6, 2, 13, 25),
(7, 2, 15, 20),
(8, 3, 1, 300),
(9, 3, 16, 80),
(10, 3, 13, 60),
(11, 3, 12, 100),
(12, 4, 14, 60),
(13, 4, 15, 30),
(14, 4, 13, 20),
(15, 4, 11, 90),
(16, 5, 4, 250),
(17, 5, 5, 20),
(18, 6, 1, 250),
(19, 6, 2, 150),
(20, 6, 4, 200),
(21, 6, 3, 50),
(22, 7, 10, 1),
(23, 7, 11, 100),
(24, 7, 13, 20),
(25, 7, 16, 15),
(26, 8, 1, 150),
(27, 8, 2, 100),
(28, 8, 4, 150),
(29, 8, 3, 50);

INSERT INTO venta (idVenta, fechaHora, idUsuario, totalVenta, estado)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 120
)
SELECT
    n,
    datetime('2026-01-01', '+' || (n - 1) || ' days', '+' || (n % 12) || ' hours'),
    ((n - 1) % 8) + 1,
    ROUND(
        (SELECT precioVenta FROM producto WHERE id = ((n - 1) % 25) + 1) * CASE WHEN n % 3 = 0 THEN 2 ELSE 1 END +
        (SELECT precioVenta FROM producto WHERE id = ((n + 6) % 25) + 1) * CASE WHEN n % 4 = 0 THEN 3 ELSE 1 END,
        2
    ),
    CASE WHEN n % 13 = 0 THEN 0 ELSE 1 END
FROM seq;

INSERT INTO detalle_venta (idVenta, idProducto, cantidad, precioUnitario)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 120
)
SELECT
    n,
    ((n - 1) % 25) + 1,
    CASE WHEN n % 3 = 0 THEN 2 ELSE 1 END,
    (SELECT precioVenta FROM producto WHERE id = ((n - 1) % 25) + 1)
FROM seq
UNION ALL
SELECT
    n,
    ((n + 6) % 25) + 1,
    CASE WHEN n % 4 = 0 THEN 3 ELSE 1 END,
    (SELECT precioVenta FROM producto WHERE id = ((n + 6) % 25) + 1)
FROM seq;

INSERT INTO historial_inventario (idProducto, tipoMovimiento, cantidad, fecha, motivo, idUsuario)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 180
)
SELECT
    ((n - 1) % 25) + 1,
    CASE (n % 4)
        WHEN 0 THEN 'ENTRADA'
        WHEN 1 THEN 'SALIDA_VENTA'
        WHEN 2 THEN 'MERMA'
        ELSE 'AJUSTE'
    END,
    CASE (n % 4)
        WHEN 0 THEN 20 + (n % 7)
        WHEN 1 THEN 1 + (n % 4)
        WHEN 2 THEN 1 + (n % 3)
        ELSE 2 + (n % 5)
    END,
    datetime('2026-01-01', '+' || (n - 1) || ' days', '+' || (n % 8) || ' hours'),
    CASE (n % 4)
        WHEN 0 THEN 'Reposición por compra'
        WHEN 1 THEN 'Salida por venta'
        WHEN 2 THEN 'Merma o pérdida'
        ELSE 'Ajuste manual'
    END,
    ((n - 1) % 8) + 1
FROM seq;

COMMIT;

-- Fin del script.
