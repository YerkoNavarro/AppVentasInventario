# Estructura de Base de Datos SQLite

## usuario
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- nombre: TEXT NOT NULL
- apellido: TEXT NOT NULL
- rut: TEXT UNIQUE NOT NULL
- password: TEXT NOT NULL
- telefono: TEXT
- rol: TEXT
- estado: INTEGER

## categoria
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- nombreCategoria: TEXT UNIQUE NOT NULL
- descripcion: TEXT
- activa: BOOLEAN

## producto
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- nombre: TEXT NOT NULL
- precioCompra: REAL
- precioVenta: REAL
- idCategoria: INTEGER NOT NULL FK->categoria(id) ON UPDATE CASCADE ON DELETE RESTRICT
- fechaVenc: TEXT
- stockActual: INTEGER
- stockMinimo: INTEGER
- imagen: TEXT DEFAULT 'IMG'
- unidadMedida: TEXT
- cantidad: DOUBLE
- tipoProducto: TEXT
- cantidadDefault: DOUBLE

## platillo
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- nombre: TEXT UNIQUE NOT NULL
- precio: DOUBLE
- idCategoria: INTEGER FK->categoria(id) ON UPDATE CASCADE ON DELETE RESTRICT
- estado: BOOLEAN DEFAULT 1
- costoProduccion: DOUBLE DEFAULT 0.0
- fabricables: INTEGER DEFAULT 0
- tipoProducto: TEXT DEFAULT 'PLATILLO'

## detalle_platillo
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- idPlatillo: INTEGER NOT NULL FK->platillo(id) ON DELETE CASCADE
- idProducto: INTEGER NOT NULL FK->producto(id)
- cantidadIngrediente: DOUBLE NOT NULL

## venta
- idVenta: INTEGER PRIMARY KEY AUTOINCREMENT
- fechaHora: TEXT NOT NULL
- idUsuario: INTEGER FK->usuario(id)
- totalVenta: REAL
- tipoPago: TEXT
- descripcion: TEXT
- estado: INTEGER

## detalle_venta
- idDetalle: INTEGER PRIMARY KEY AUTOINCREMENT
- idVenta: INTEGER FK->venta(idVenta) ON DELETE CASCADE
- idProducto: INTEGER FK->producto(id)
- idPlatillo: INTEGER FK->platillo(id)

## historial_inventario
- idMovimiento: INTEGER PRIMARY KEY AUTOINCREMENT
- idProducto: INTEGER NOT NULL FK->producto(id)
- tipoMovimiento: TEXT NOT NULL
- cantidad: INTEGER NOT NULL
- fecha: TEXT DEFAULT CURRENT_TIMESTAMP
- motivo: TEXT NOT NULL
- idUsuario: INTEGER NOT NULL FK->usuario(id)