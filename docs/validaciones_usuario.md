# Validaciones del Sistema — Guía para el Usuario

A continuación se describen todos los mensajes y validaciones que el usuario puede ver al interactuar con el sistema, organizados por módulo.

---

## 1. Inicio de Sesión

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| El usuario deja vacío el RUT o la contraseña | "Por favor, ingresa tu RUT y contraseña" |
| El usuario ingresa credenciales incorrectas | "RUT o contraseña incorrectos" |
| El usuario ingresa credenciales correctas | "Bienvenido [nombre del usuario]" |
| No se puede cargar la pantalla principal después del login | "No se pudo cargar el Panel Principal." |
| Ocurre un error al actualizar los indicadores del inicio | "No se pudieron actualizar las métricas" |
| No hay productos con stock bajo | En la tarjeta correspondiente aparece "No bajo stock" |

---

## 2. Panel Principal (Dashboard)

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| El usuario intenta cerrar sesión | "¿Estás seguro de que deseas salir del sistema?" (el sistema pide confirmación) |

---

## 3. Usuarios

### Al registrar o editar un usuario

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| La pantalla se abre normalmente | "Listo para registrar" |
| Se está editando un usuario existente | "Modo Edición Activo" |
| El registro se completa con éxito | "Registro Completado" + "Usuario registrado exitosamente" |
| La actualización se completa con éxito | "Actualización Completada" + mensaje de respuesta |
| Hay un error de validación (datos incorrectos) | "Error de validación" + mensaje de respuesta |
| Ocurre un error inesperado | "Ha ocurrido un error: [detalle del error]" |
| Después de limpiar el formulario | "Usuario registrado correctamente" |

### Al eliminar un usuario

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| El usuario no selecciona ningún usuario de la tabla | "Ningún usuario seleccionado" + "Selecciona un usuario de la tabla para editar." |
| No se puede abrir la ventana de edición | "No se pudo abrir la ventana" |
| El usuario no selecciona un usuario para eliminar | "Ningún usuario seleccionado" + "Por favor, selecciona un usuario." |
| El sistema pide confirmación antes de eliminar | "¿Estás seguro de que deseas eliminar a [nombre del usuario]?" |
| La eliminación se completa con éxito | "Usuario eliminado" + "El usuario fue borrado correctamente." |
| El usuario tiene ventas registradas y no se puede eliminar | "No se pudo eliminar" + "El usuario tiene ventas asociadas y no puede ser eliminado." |

---

## 4. Categorías

### Al registrar o editar una categoría

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| El nombre de la categoría está vacío | "El nombre es obligatorio." |
| Se está editando una categoría existente | "Editando categoría" |
| La categoría se actualiza correctamente | "Categoría actualizada correctamente" |
| La categoría se registra correctamente | "Categoría registrada correctamente" |
| Ocurre un error al guardar | "No se pudo guardar la categoría: [detalle del error]" |

### Al eliminar una categoría

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No se pueden cargar las categorías | "Error al cargar categorías: [detalle del error]" |
| No se puede abrir el formulario | "No se pudo abrir el formulario de categoría: [detalle del error]" |
| El usuario no selecciona una categoría para editar | "Seleccione una categoría para editar." |
| El usuario no selecciona una categoría para eliminar | "Seleccione una categoría para eliminar." |
| El sistema pide confirmación antes de eliminar | "¿Desea eliminar la categoría seleccionada?" |
| La eliminación se completa con éxito | "Categoría eliminada correctamente." |

---

## 5. Productos

### Al registrar o editar un producto

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No hay categorías registradas en el sistema | "No hay categorías, se recomienda agregar una" |
| El nombre del producto está vacío | "Error: El nombre del producto no puede estar vacío." |
| Ya existe otro producto con el mismo nombre | "Error: Ya existe otro producto con ese nombre." |
| No se seleccionó una unidad de medida | "Error: Debe seleccionar una unidad de medida." |
| No se seleccionó un tipo de producto | "Error: Debe seleccionar un tipo de producto." |
| Faltan precios, stocks o cantidad | "Error: Los precios, stocks y cantidad son campos obligatorios." |
| El precio de compra es $0 o menor | "Error: El precio de compra debe ser mayor a 0." |
| El precio de venta no es al menos 10% mayor que el de compra | "Protección de Margen: El precio de venta debe ser al menos un 10% mayor al precio de compra." |
| El stock actual es 0 o menor | "Error: El stock actual debe ser mayor a 0." |
| El stock mínimo es 0 o menor | "Error: El stock mínimo debe ser mayor a 0." |
| El producto se registra con éxito | "¡Producto registrado con éxito!" |
| El producto se registra pero no se guarda el historial | "¡Producto registrado con éxito! (Sin historial)" |
| El producto se actualiza con éxito | "¡Producto actualizado con éxito!" |
| Los precios o stock no son números válidos | "Error: Los precios y el stock deben ser números válidos." |

### Al eliminar un producto

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No se pueden cargar los productos | "Error al obtener productos: [detalle del error]" |
| No hay productos registrados | "No hay productos para mostrar" |
| No se seleccionó un producto para editar | "Seleccione un producto para editar" |
| No se seleccionó un producto para eliminar | "Por favor seleccione un producto para eliminar" |
| El producto seleccionado no existe | "El producto seleccionado no existe" |
| El sistema pide confirmación antes de eliminar | "¿Está seguro que desea eliminar el producto seleccionado?" |
| El producto se elimina correctamente | "Producto eliminado correctamente" |
| El producto tiene asociaciones (ventas, etc.) | "El producto tiene asociaciones, Solo se desactivará [mensaje]" |
| No hay productos con stock bajo | "No hay" (en la tarjeta de stock bajo) |

---

## 6. Platillos (Platos/Dish)

### Al agregar ingredientes

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No se seleccionó un ingrediente o no se escribió la cantidad | "Error: Seleccione un ingrediente y digite su cantidad." |
| La cantidad del ingrediente es 0 o menor | "Error: La cantidad debe ser mayor a 0." |
| El ingrediente se agrega correctamente | "Ingrediente agregado temporalmente." |
| La cantidad ingresada no es un número válido | "Error: Cantidad inválida." |
| No hay suficiente stock del ingrediente | "Error: No hay suficiente stock del ingrediente '[nombre]'. Stock actual: [cantidad], cantidad requerida: [cantidad]" |

### Al registrar o editar un platillo

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| El nombre o el precio están vacíos | "Error: El nombre y el precio son obligatorios." |
| El precio es $0 o menor | "Error: Precio debe ser mayor a cero" |
| No se seleccionó una categoría | "Error: Debes seleccionar una categoría." |
| El platillo no tiene ingredientes | "Error: El platillo debe tener al menos un ingrediente." |
| El precio no es un número válido | "Error: El precio debe ser un número válido." |
| El platillo se registra con éxito | "¡Platillo registrado con éxito!" |
| No hay un platillo seleccionado para actualizar | "Error: No hay un platillo seleccionado para actualizar." |
| El platillo se actualiza con éxito | "¡Platillo actualizado con éxito!" |
| Se muestra el costo total de producción | "Costo Producción: $[monto]" o "Costo Producción: $0.00" (si hay error) |

### Al eliminar un platillo

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No se pueden cargar los platillos | "No se pudieron cargar los platillos" |
| No se puede abrir el formulario | "Error al abrir formulario: [detalle del error]" |
| No se seleccionó un platillo para editar | "Selecciona un platillo para editar" |
| No se seleccionó un platillo para eliminar | "Selecciona un platillo para eliminar" |
| El sistema pide confirmación antes de eliminar | "¿Estás seguro de que deseas eliminar este platillo?" |
| El platillo se elimina correctamente | "Platillo eliminado correctamente" |
| El platillo está asociado a una venta (solo se desactiva) | [Mensaje del sistema indicando que solo se desactivó] |

---

## 7. Ventas

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| Faltan campos obligatorios en el formulario | "Por favor, complete todos los campos requeridos." |
| La venta se agrega correctamente a la tabla temporal | "Venta agregada exitosamente a la tabla." |
| Los datos ingresados no son válidos | [Mensaje de error de validación] |
| No se seleccionó ningún registro para eliminar | "Seleccione un registro para eliminar." |
| No hay ventas pendientes por guardar | "No hay datos para guardar." |
| Las ventas se guardan correctamente | "Las ventas se han guardado correctamente." |

---

## 8. Inventario

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| Faltan el producto, la cantidad o el tipo de movimiento | "Campos incompletos" + "Por favor llena el Producto, Cantidad y Tipo de Movimiento." |
| El producto ingresado no existe en la base de datos | "Producto no encontrado" + "El producto ingresado no existe en la base de datos." |
| El movimiento se registra correctamente | "Movimiento Registrado" + "El inventario se ha actualizado correctamente." |
| Ocurre un error en la base de datos | "Error en Base de Datos" + "No se pudo registrar el movimiento." |
| La cantidad ingresada no es un número entero válido | "Datos inválidos" + "La Cantidad debe ser un número entero." |

---

## 9. Estadísticas

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| No hay ventas registradas (tabla de ranking de productos) | "No hay ventas registradas aún." |
| No hay actividad reciente (tabla de historial) | "No hay actividad reciente registrada." |
| No hay ventas de usuarios registradas | "No hay datos de ventas por usuario para mostrar." |
| Se muestra cada vendedor en el ranking | "[Nombre del vendedor] — [cantidad] ventas" |
| Sugerencia de compra en predicción de stock | "Comprar [cantidad] u." |
| Nivel de riesgo en predicción de stock | "ALTO", "MEDIO" o "BAJO" |

---

## 10. Consultas con IA (Asistente SQL)

| Cuándo ocurre | Mensaje que ve el usuario |
|---|---|
| La clave de la API está configurada | "API Key configurada" (indicador verde) |
| La clave de la API no está configurada | "API Key no configurada — haz clic en el icono de engranaje" |
| La pantalla se abre por primera vez | "Escribe una consulta y presiona 'Consultar' para ver los resultados." |
| Se genera una consulta SQL | "SQL generado: [consulta SQL]" |
| No hay clave API configurada al intentar consultar | "Debes configurar una API Key de Gemini antes de consultar. Haz clic en el icono de engranaje." |
| Ocurre un error al consultar | [Mensaje de error o "Error desconocido"] |
| La consulta no devuelve resultados | "No se encontraron resultados para tu consulta." |
| Ocurre un error al ejecutar la consulta | "Error al ejecutar la consulta." |

---

> Documento generado el — cubre todos los mensajes visibles para el usuario en los módulos del sistema.
