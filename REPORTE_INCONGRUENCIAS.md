# Reporte de incongruencias, posibles errores y sugerencias

Este archivo resume los problemas más relevantes encontrados en el proyecto `puntoVentas`, con foco en incongruencias entre UI, servicios, repositorios, base de datos e integración con Python.

---

## 1) Problemas críticos

### 1.1 Mismatch entre versión de JavaFX y el proyecto
**Evidencia:**
- Los FXML declaran `xmlns="http://javafx.com/javafx/25"`.
- El proyecto usa dependencias JavaFX `17.0.14` en `pom.xml`.
- Ya apareció el warning: `Loading FXML document with JavaFX API of version 25 by JavaFX runtime of version 17.0.2-ea`.

**Impacto:**
- Puede generar advertencias, comportamiento inconsistente o fallos de compatibilidad.

**Sugerencia:**
- Unificar la versión de JavaFX entre `pom.xml` y los FXML.
- Si el runtime es 17, los FXML deberían quedar alineados a esa versión.

---

### 1.2 Integración de base de datos inconsistente: SQLite vs MySQL
**Evidencia:**
- `DbManager` y `ProductoRepositoryImpl` usan `jdbc:sqlite:DBventasInventario.db`.
- En un error previo del proyecto se reportó: `No suitable driver found for jdbc:mysql://localhost:3306/eluney`.

**Impacto:**
- Hay señales de que el proyecto ha mezclado configuraciones de SQLite y MySQL.
- Eso explica errores de conexión y drivers no encontrados.

**Sugerencia:**
- Definir una sola base de datos real para todo el proyecto.
- Eliminar URLs antiguas o código heredado que siga apuntando a MySQL si ya se migró a SQLite.

---

### 1.3 La carga de categorías y productos no está alineada con el esquema real
**Evidencia:**
- `DbManager.crearTablaProductos()` crea la columna `idCategoria`.
- En `ProductoRepositoryImpl`, varios métodos leen o consultan una columna llamada `categoria`:
  - `obtenerProductos()` usa `mapCategoria(rs.getString(5))`
  - `obtenerProductoPorNombre()` usa `mapCategoria(rs.getString(5))`
  - `obtenerStockCritico()` usa `rs.getString("categoria")`
  - `existeCategoria()` consulta `WHERE categoria = ?`
  - `eliminarCategoria()` consulta productos por `lower(trim(categoria))`

**Impacto:**
- Si la tabla usa `idCategoria`, esas consultas van a fallar o devolver datos incorrectos.
- Esto puede explicar errores como “la tabla categoría no existe” o filtros que no funcionan.

**Sugerencia:**
- Unificar el modelo de categoría: o se guarda por `idCategoria` o por nombre, pero no ambos mezclados.
- Corregir consultas y mapeos para que coincidan con la estructura real de la tabla.

---

### 1.4 `obtenerProductoPorId(int id)` no devuelve `null` cuando no encuentra el producto
**Evidencia:**
- En `ProductoRepositoryImpl`, el método crea `Producto producto = new Producto();` y siempre lo retorna.
- Si no hay resultados, devuelve un objeto vacío, no `null`.
- En `ProductoService.eliminarProducto(int id)` se hace:
  - `Producto producto = productoRepository.obtenerProductoPorId(id);`
  - `if (producto == null) ...`

**Impacto:**
- La validación de “producto no existe” puede no funcionar nunca.
- Se pueden ejecutar operaciones sobre un objeto vacío sin detectarlo.

**Sugerencia:**
- Hacer que el repositorio retorne `null` si no encuentra filas, o validar `producto.getId() == 0` en el service.

---

### 1.5 `actualizarProducto` tiene un error grave de parámetros SQL
**Evidencia:**
- SQL definido:
  - `UPDATE producto SET nombre = ?, precioCompra = ?, precioVenta = ?, idcategoria = ?, fechaVenc = ?, stockActual = ?, stockMinimo = ?, imagen = ?, unidadMedida = ? WHERE id = ?`
- Ese SQL tiene 10 parámetros.
- Pero el código hace:
  - `pstmt.setInt(10, producto.getId());`
  - `pstmt.setString(11, producto.getTipoProducto().name());`

**Impacto:**
- Hay un parámetro fuera de rango.
- El método va a fallar en ejecución.
- Además, `tipoProducto` ni siquiera está incluido en el `UPDATE`.

**Sugerencia:**
- Revisar el SQL y los índices de parámetros.
- Si `tipoProducto` debe actualizarse, debe estar en la sentencia SQL.
- Si no debe actualizarse, eliminar ese `setString(11, ...)`.

---

### 1.6 `registrarProducto` guarda la categoría usando `getId()`, pero el formulario solo llena el nombre
**Evidencia:**
- En `ProductoController`, se crea una `Categoria` con:
  - `categoriaObj.setNombreCategoria(txtCategoria.getText());`
- Pero en `ProductoRepositoryImpl.registrarProducto()` se usa:
  - `pstmt.setInt(4, producto.getCategoria().getId());`

**Impacto:**
- Si el ID nunca se asigna, se puede insertar `0` o un valor inválido.
- Eso rompe la relación con la tabla categoría.

**Sugerencia:**
- Buscar la categoría por nombre antes de registrar el producto.
- O cambiar el formulario para seleccionar una categoría existente por ID.

---

### 1.7 La navegación del panel principal referencia FXML que no existen
**Evidencia:**
- `PanelPrincipalVistaController` intenta cargar:
  - `PanelUsuarios-vista.fxml`
  - `PanelVentas-vista.fxml`
  - `PanelInventario-vista.fxml`
- En `src/main/resources/com/sistema/puntoventas` solo aparecen:
  - `DashboardVista.fxml`
  - `LoginVista.fxml`
  - `panelPrincipalVista.fxml`
  - `PanelRegistrarProductosvista.fxml`

**Impacto:**
- Al pulsar esos botones la app puede lanzar `IOException` por recurso inexistente.

**Sugerencia:**
- Crear esos FXML faltantes o corregir las rutas/nombres a archivos reales.

---

## 2) Problemas importantes de lógica

### 2.1 `ProductoService.ejecutarPrediccionProphet()` continúa aunque falten datos
**Evidencia:**
- Hace:
  - `boolean datosListos = prepararDatosParaIA();`
  - `if(!datosListos){ System.err.println(...); }`
  - pero luego sigue ejecutando Python igual.

**Impacto:**
- El script puede ejecutarse con datos insuficientes.
- Se generan errores evitables.

**Sugerencia:**
- Cortar la ejecución si `datosListos` es `false`.

---

### 2.2 Ruta del script Python frágil
**Evidencia:**
- Se llama:
  - `new ProcessBuilder("python", "src/main/java/com/sistema/puntoventas/modelo_predictivo.py")`

**Impacto:**
- Depende del directorio de ejecución.
- Ya hubo un error previo de archivo no encontrado.

**Sugerencia:**
- Resolver la ruta de forma robusta.
- Idealmente mover el script a una carpeta más estable como `src/main/resources` o una carpeta dedicada fuera del árbol Java.

---

### 2.3 `ProductoService.actualzarProducto(...)` tiene un nombre mal escrito
**Evidencia:**
- El método se llama `actualzarProducto` en vez de `actualizarProducto`.

**Impacto:**
- Es una incongruencia de nombre.
- Puede causar confusión y errores al invocarlo.

**Sugerencia:**
- Renombrar el método de forma consistente.

---

### 2.4 `ProductoService.eliminarCategoria(int id)` delega en el repositorio, pero el mensaje no refleja el motivo real
**Evidencia:**
- Si falla, lanza:
  - `No se pudo eliminar la categoría porque está asociada a un producto o platillo, no existe o hubo un error.`
- Pero el repositorio mezcla validaciones de existencia, asociación y eliminación en un solo boolean.

**Impacto:**
- El usuario no sabe la causa exacta del fallo.

**Sugerencia:**
- Separar validaciones en estados diferenciados.
- Retornar mensajes más específicos.

---

## 3) Incongruencias entre repositorio e interfaz

### 3.1 `IProductoRepository` mezcla operaciones por nombre, por ID y por estado sin un contrato claro
**Evidencia:**
- Tiene métodos como:
  - `existeCategoria(String nombre)`
  - `actualizarCategoria(int id)`
  - `eliminarCategoria(int id)`
  - `estaAsociadoVentaOPlatillo(int id)`
  - `obtenerStockActual(int id)`

**Impacto:**
- La interfaz tiene responsabilidades muy distintas en una sola capa.
- Se dificulta mantener y probar.

**Sugerencia:**
- Separar repositorios por dominio:
  - productos
  - categorías
  - estadísticas
  - movimientos

---

### 3.2 `actualizarCategoria(int id)` realmente solo alterna el estado `activa`
**Evidencia:**
- En el repositorio se ejecuta:
  - `UPDATE categoria SET activa = CASE WHEN activa = 1 THEN 0 ELSE 1 END WHERE id = ?`

**Impacto:**
- El nombre del método sugiere una actualización completa, pero solo hace toggle de estado.

**Sugerencia:**
- Renombrar a algo más claro, por ejemplo `cambiarEstadoCategoria(int id)`.

---

### 3.3 `eliminarCategoria(int id)` busca asociaciones por una columna que no coincide con la tabla producto
**Evidencia:**
- Usa `lower(trim(categoria))` en la tabla producto.
- Pero producto guarda `idCategoria`.

**Impacto:**
- Puede fallar siempre o lanzar error SQL.

**Sugerencia:**
- Hacer la validación por `idCategoria` y no por texto.

---

## 4) Problemas de UI / FXML

### 4.1 `styleClass="menu-button, menu-button-active"` está mal formado
**Evidencia:**
- En `panelPrincipalVista.fxml`, el botón de productos usa:
  - `styleClass="menu-button, menu-button-active"`

**Impacto:**
- Puede terminar como una sola clase mal escrita.
- El estilo activo puede no aplicarse correctamente.

**Sugerencia:**
- Separar las clases correctamente según la sintaxis de JavaFX.

---

### 4.2 El panel principal no integra realmente el contenido cargado si falla la vista
**Evidencia:**
- `contentArea` depende de que existan las vistas cargadas desde `initialize()`.
- Si la carga falla, el panel queda vacío o inestable.

**Impacto:**
- La navegación del dashboard puede romperse visualmente.

**Sugerencia:**
- Agregar manejo visual de error al cargar módulos.
- Cargar primero una vista fallback si la principal no existe.

---

### 4.3 `LoginController` no navega a otra pantalla al iniciar sesión correctamente
**Evidencia:**
- Solo muestra alerta de éxito.
- El comentario dice que luego se podría cargar otra vista, pero no está implementado.

**Impacto:**
- El flujo de login queda incompleto.

**Sugerencia:**
- Implementar la transición de pantalla después del login exitoso.

---

### 4.4 El formulario de productos mezcla texto libre y datos estructurados
**Evidencia:**
- `txtCategoria` es un `TextField`.
- `cmbTipoProducto` sí es `ComboBox`, pero no tiene `StringConverter`.
- `cmbUnidadMedida` sí está bien formateado.

**Impacto:**
- La categoría puede escribirse con nombres que no existen en BD.
- Esto produce errores de integridad y datos duplicados.

**Sugerencia:**
- Cambiar categoría por un `ComboBox<Categoria>` o buscar la categoría antes de guardar.

---

## 5) Observaciones sobre SQLite / esquema

### 5.1 La tabla `venta` usa `idVenta`, pero hay consultas que podrían asumir `id`
**Evidencia:**
- En `DbManager` la tabla `venta` tiene PK `idVenta`.
- En otras consultas del proyecto hay joins y referencias que deben ser revisadas con cuidado.

**Impacto:**
- Si algún SQL usa `id` en vez de `idVenta`, fallará.

**Sugerencia:**
- Normalizar el nombre de la PK de `venta` en todo el proyecto.

---

### 5.2 `categoria` tiene campo `activa` sin default explícito
**Evidencia:**
- `CREATE TABLE categoria (... activa boolean)`.

**Impacto:**
- Puede quedar `NULL` si no se inserta valor.

**Sugerencia:**
- Definir un valor por defecto o forzar el valor en inserción.

---

## 6) Limpieza técnica y mantenibilidad

### 6.1 Imports no usados en varias clases
**Ejemplos observados:**
- `HelloApplication` importa `Producto` y `ProductoRepositoryImpl` pero no los usa.
- `PanelPrincipalVistaController` tiene variables y métodos sin uso real.

**Impacto:**
- Ruido visual y mantenimiento más difícil.

**Sugerencia:**
- Eliminar imports y código muerto.

---

### 6.2 Existen clases muy similares o duplicadas
**Evidencia:**
- `EstadisticaService` y `EstadisticasService`.
- Una de ellas está comentada por completo.

**Impacto:**
- Confusión sobre cuál usar.

**Sugerencia:**
- Dejar una sola implementación y eliminar la duplicada o la obsoleta.

---

### 6.3 Nombres inconsistentes en archivos e interfaces
**Ejemplos:**
- `VentaRepositoryimpl.java` vs convención esperada `VentaRepositoryImpl.java`.
- `IventaRepository.java` con `i` minúscula intermedia.
- `actualzarProducto` mal escrito.

**Impacto:**
- Dificulta navegación, autocompletado y mantenimiento.

**Sugerencia:**
- Unificar convenciones de nombres en todo el proyecto.

---

## 7) Resumen priorizado

### Prioridad alta
1. Alinear versión de JavaFX con los FXML.
2. Corregir el esquema de categorías (`idCategoria` vs `categoria`).
3. Arreglar `actualizarProducto()` por los parámetros SQL incorrectos.
4. Corregir `obtenerProductoPorId()` para que devuelva `null` si no encuentra resultados.
5. Revisar la integración con Python y la ruta del script.
6. Eliminar referencias a FXML que no existen.

### Prioridad media
1. Hacer más clara la lógica de categorías.
2. Mejorar el login para navegar a otra vista.
3. Revisar estilos y clases CSS mal escritas.
4. Separar repositorios por responsabilidad.

### Prioridad baja
1. Eliminar imports no usados.
2. Unificar nombres de clases y métodos.
3. Limpiar clases comentadas o duplicadas.

---

## 8) Conclusión

El proyecto tiene una base funcional, pero hay varias incongruencias importantes entre:
- UI y controladores,
- controladores y servicios,
- servicios y repositorios,
- repositorios y esquema de base de datos,
- Java y Python.

Los problemas más delicados están en:
- el mapeo de productos/categorías,
- la actualización de productos,
- la validación de existencia,
- la navegación hacia vistas inexistentes,
- y la integración del script predictivo.

Este reporte puede servir como hoja de ruta para corregir el sistema por etapas sin romper más partes del proyecto.

