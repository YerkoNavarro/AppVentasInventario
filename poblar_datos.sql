INSERT INTO categoria (nombreCategoria, descripcion, activa) VALUES
                                                                 ('Bebidas', 'Bebidas frías y jugos', 1),
                                                                 ('Carnes', 'Cortes de vacuno, pollo y cerdo', 1),
                                                                 ('Verduras', 'Vegetales e insumos frescos', 1),
                                                                 ('Comida Rápida', 'Hamburguesas, completos, papas fritas', 1),
                                                                 ('Insumos Secos', 'Pan, salsas, condimentos', 1);

-- 2. USUARIOS (estado = 1 ACTIVO)
-- (El sistema inserta "Admin" automáticamente, pero agregamos más)
INSERT INTO usuario (nombre, apellido, rut, password, telefono, rol, estado) VALUES
                                                                                 ('Juan', 'Perez', '11111111-1', 'usuario123', '988888888', 'CAJERO', 1),
                                                                                 ('Maria', 'Gomez', '22222222-2', 'cocina123', '977777777', 'COCINERO', 1),
                                                                                 ('Pedro', 'Soto', '33333333-3', 'admin000', '966666666', 'ADMIN', 1);

-- 3. PRODUCTOS
-- Tipos posibles asumidos: DIRECTO, INGREDIENTE
INSERT INTO producto (nombre, precioCompra, precioVenta, idCategoria, fechaVenc, stockActual, stockMinimo, imagen, unidadMedida, cantidad, tipoProducto) VALUES
                                                                                                                                                             ('Coca Cola 350ml', 500, 1000, 1, '2026-12-31', 100, 20, 'IMG', 'UNIDAD', 1, 'DIRECTO'),
                                                                                                                                                             ('Jugo de Naranja', 400, 900, 1, '2026-10-15', 50, 10, 'IMG', 'UNIDAD', 1, 'DIRECTO'),
                                                                                                                                                             ('Carne de Hamburguesa', 800, 0, 2, '2026-06-30', 200, 50, 'IMG', 'UNIDAD', 1, 'SOLO_INVENARIO'),
                                                                                                                                                             ('Pan de Hamburguesa', 200, 0, 5, '2026-05-15', 100, 20, 'IMG', 'UNIDAD', 1, 'SOLO_INVENARIO'),
                                                                                                                                                             ('Tomate', 1000, 0, 3, '2026-05-10', 15, 5, 'IMG', 'KG', 1, 'SOLO_INVENARIO'),
                                                                                                                                                             ('Lechuga', 800, 0, 3, '2026-05-10', 10, 3, 'IMG', 'KG', 1, 'SOLO_INVENARIO'),
                                                             (2, 5, 0.1),
                                                                               (2, 6, 0.05),
                                                                               (2, 7, 0.1); -- 100g de queso

-- 6.
