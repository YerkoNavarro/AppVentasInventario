# Manual del Administrador — Sistema ELUNEY (puntoVentas)

---

## Tabla de Contenido

1. [Introducción](#1-introducción)
2. [Acceso al Sistema](#2-acceso-al-sistema)
3. [Panel Principal](#3-panel-principal)
4. [Gestión de Usuarios](#4-gestión-de-usuarios)
5. [Gestión de Productos](#5-gestión-de-productos)
6. [Gestión de Platillos (Recetas)](#6-gestión-de-platillos-recetas)
7. [Gestión de Categorías](#7-gestión-de-categorías)
8. [Módulo de Ventas](#8-módulo-de-ventas)
9. [Control de Inventario](#9-control-de-inventario)
10. [Estadísticas y Reportes](#10-estadísticas-y-reportes)
11. [Módulo de Auditoría](#11-módulo-de-auditoría)
12. [Módulo IA — Predicción de Stock](#12-módulo-ia--predicción-de-stock)
13. [Módulo IA-SQL (Lenguaje Natural)](#13-módulo-ia-sql-lenguaje-natural)
14. [Anulación de Operaciones](#14-anulación-de-operaciones)
15. [Respaldo y Mantenimiento](#15-respaldo-y-mantenimiento)
16. [Solución de Problemas Comunes](#16-solución-de-problemas-comunes)
17. [Apéndice: Estructura de la Base de Datos](#17-apéndice-estructura-de-la-base-de-datos)
18. [Glosario](#18-glosario)

---

## 1. Introducción

**ELUNEY** es un sistema de punto de venta y control de inventario con módulos de inteligencia artificial integrados. Este manual está dirigido al perfil **ADMINISTRADOR**, responsable de la configuración, operación avanzada y mantenimiento del sistema.

### Privilegios del Administrador

| Función | Descripción |
|---|---|
| Gestión de usuarios | Crear, editar y desactivar usuarios |
| CRUD de productos | Registrar, editar y desactivar productos |
| CRUD de platillos | Registrar platillos con recetas e ingredientes |
| CRUD de categorías | Administrar categorías de productos |
| Ventas | Registrar ventas (misma funcionalidad que vendedor) |
| Inventario | Registrar movimientos (entradas, mermas, ajustes) |
| Estadísticas | Ver reportes y rankings |
| Auditoría | Consultar el registro de eventos del sistema |
| IA Predictiva | Ejecutar predicciones de stock |
| IA-SQL | Consultar datos en lenguaje natural |
| Anulación | Anular ventas y operaciones |
| Respaldo | Realizar copias de seguridad |

---

## 2. Acceso al Sistema

### 2.1 Inicio de Sesión

1. Ejecute el sistema ELUNEY.
2. Aparecerá la pantalla de inicio de sesión.

**[CAPTURA DE PANTALLA: Pantalla de Login]**
*Imagen sugerida: Ventana de login con campos RUT y Contraseña, botón "Iniciar Sesión".*

3. Ingrese su **RUT** y **Contraseña** de administrador.
4. Haga clic en **"Iniciar Sesión"**.

> **Credenciales por defecto:** RUT: `admin` / Contraseña: `admin`
> *Se recomienda cambiar la contraseña inmediatamente después del primer inicio de sesión.*

### 2.2 Panel Principal (Vista Administrador)

Al iniciar sesión como administrador, verá todos los módulos disponibles.

**[CAPTURA DE PANTALLA: Panel Principal - Vista Administrador]**
*Imagen sugerida: Dashboard completo con todos los módulos visibles: Ventas, Productos, Platillos, Categorías, Inventario, Usuarios, Estadísticas, IA-SQL, Cerrar Sesión.*

---

## 3. Panel Principal

### 3.1 Elementos del Dashboard

| Elemento | Descripción |
|---|---|
| **Total Productos** | Cantidad de productos activos en el sistema |
| **Total Usuarios** | Cantidad de usuarios registrados |
| **Total Ventas** | Ventas registradas en el período |
| **Stock Crítico** | Número de productos con stock bajo el mínimo |
| **Botones de acceso** | Acceso directo a cada módulo del sistema |
| **Barra superior** | Nombre del usuario logueado y botón de cierre de sesión |

### 3.2 Navegación

Para acceder a cualquier módulo, haga clic en su botón correspondiente en el Panel Principal.

**[CAPTURA DE PANTALLA: Dashboard con métricas]**
*Imagen sugerida: Vista del dashboard mostrando las tarjetas con métricas (total productos, ventas, etc.).*

---

## 4. Gestión de Usuarios

### 4.1 Acceder al Módulo

Haga clic en el botón **"Usuarios"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla de gestión de usuarios]**
*Imagen sugerida: Tabla de usuarios con columnas: nombre, RUT, rol, estado, y botones de acción (editar, desactivar).*

### 4.2 Registrar un Nuevo Usuario

1. Haga clic en el botón **"Agregar Usuario"**.
2. Complete el formulario con los siguientes campos:

| Campo | Descripción | Obligatorio |
|---|---|---|
| Nombre | Nombre del usuario | Sí |
| Apellido | Apellido del usuario | Sí |
| RUT | RUT sin puntos y con guion (ej: 12345678-9) | Sí |
| Teléfono | Número de contacto | No |
| Contraseña | Contraseña de acceso | Sí |
| Rol | ADMIN o VENDEDOR | Sí |

**[CAPTURA DE PANTALLA: Formulario de registro de usuario]**
*Imagen sugerida: Ventana o panel con campos del formulario de nuevo usuario.*

3. Haga clic en **"Guardar"** para registrar el usuario.

### 4.3 Editar un Usuario

1. Seleccione el usuario de la tabla y haga clic en el botón **"Editar"**.
2. Modifique los campos necesarios.
3. Haga clic en **"Guardar Cambios"**.

### 4.4 Desactivar un Usuario

1. Seleccione el usuario de la tabla.
2. Haga clic en **"Desactivar"**.
3. Confirme la operación. El usuario desactivado no podrá iniciar sesión.
4. Para reactivarlo, haga clic en **"Activar"**.

> **Nota:** No se eliminan usuarios físicamente para mantener la integridad de las ventas históricas. Solo se desactivan.

### 4.5 Roles del Sistema

| Rol | Permisos |
|---|---|
| **ADMIN** | Acceso total a todos los módulos y funciones |
| **VENDEDOR** | Ventas, consulta de productos/platillos/categorías, IA-SQL |

---

## 5. Gestión de Productos

### 5.1 Acceder al Módulo

Haga clic en el botón **"Productos"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla principal de productos]**
*Imagen sugerida: Tabla de productos con columnas: nombre, precio compra, precio venta, stock, categoría, tipo, unidad, estado, y botones de acción.*

### 5.2 Registrar un Nuevo Producto

1. Haga clic en el botón **"Agregar Producto"**.
2. Complete el formulario:

**[CAPTURA DE PANTALLA: Formulario de registro de producto]**
*Imagen sugerida: Formulario con todos los campos para registrar un producto.*

| Campo | Descripción | Obligatorio |
|---|---|---|
| Nombre | Nombre del producto | Sí |
| Tipo Producto | DIRECTO, PLATILLO o SOLO_INVENTARIO | Sí |
| Precio Compra | Precio al que se compró el producto | Sí |
| Precio Venta | Precio de venta al público | Sí |
| Categoría | Categoría a la que pertenece | Sí |
| Unidad de Medida | UNIDAD, GRAMOS o MILILITROS | Sí |
| Stock Actual | Cantidad inicial en inventario | Sí |
| Stock Mínimo | Cantidad mínima para alerta | Sí |
| Fecha Vencimiento | Fecha de vencimiento (si aplica) | No |
| Cantidad por Unidad | Cantidad por empaque (ej: 500 en "500 gr") | No |

**Regla de margen mínimo:** El precio de venta debe ser al menos 10 % mayor que el precio de compra.

3. Haga clic en **"Guardar"**.

### 5.3 Tipos de Producto

| Tipo | Descripción | Ejemplo |
|---|---|---|
| **DIRECTO** | Se vende tal cual, sin preparación | Gaseosa, galletas, leche |
| **PLATILLO** | Producto elaborado (se gestiona como platillo con receta) | Hamburguesa, pizza, café |
| **SOLO_INVENTARIO** | Insumo que no se vende directamente | Harina, aceite, sal |

### 5.4 Editar un Producto

1. Seleccione el producto de la tabla y haga clic en **"Editar"**.
2. Modifique los campos necesarios.
3. Haga clic en **"Guardar Cambios"**.

### 5.5 Desactivar un Producto

1. Seleccione el producto de la tabla.
2. Haga clic en **"Desactivar"**.
3. Si el producto tiene ventas asociadas o está en recetas de platillos, se realizará un **borrado lógico** (queda inactivo pero no se elimina).

---

## 6. Gestión de Platillos (Recetas)

### 6.1 Acceder al Módulo

Haga clic en el botón **"Platillos"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla principal de platillos]**
*Imagen sugerida: Tabla de platillos con nombre, precio, categoría, costo de producción, estado.*

### 6.2 Registrar un Nuevo Platillo

1. Haga clic en el botón **"Agregar Platillo"**.
2. Complete los datos generales:

| Campo | Descripción | Obligatorio |
|---|---|---|
| Nombre | Nombre del platillo | Sí |
| Precio | Precio de venta | Sí |
| Categoría | Categoría a la que pertenece | Sí |
| Fabricables | Cantidad de unidades que se pueden producir (opcional) | No |

3. Agregue los **ingredientes** (productos existentes en el sistema):

**[CAPTURA DE PANTALLA: Formulario de platillo con sección de ingredientes]**
*Imagen sugerida: Formulario mostrando la lista de ingredientes (producto + cantidad) y botón para agregar más.*

4. Para cada ingrediente, seleccione el producto y la cantidad necesaria.
5. El sistema calculará automáticamente el **costo de producción**.
6. Haga clic en **"Guardar"**.

### 6.3 Editar un Platillo

Puede modificar el nombre, precio, categoría y la lista de ingredientes seleccionando **"Editar"** en la tabla de platillos.

### 6.4 Desactivar un Platillo

1. Seleccione el platillo en la tabla.
2. Haga clic en **"Desactivar"**.
3. El platillo dejará de estar disponible para la venta.

> **Importante:** Al vender un platillo, el sistema descuenta automáticamente el stock de cada ingrediente de la receta.

---

## 7. Gestión de Categorías

### 7.1 Acceder al Módulo

Haga clic en el botón **"Categorías"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla de categorías]**
*Imagen sugerida: Tabla de categorías con nombre, descripción y estado.*

### 7.2 Registrar una Categoría

1. Haga clic en **"Agregar Categoría"**.
2. Complete:

| Campo | Descripción | Obligatorio |
|---|---|---|
| Nombre | Nombre de la categoría | Sí |
| Descripción | Descripción breve | No |

3. Haga clic en **"Guardar"**.

### 7.3 Editar y Desactivar Categorías

- **Editar:** Seleccione la categoría y haga clic en **"Editar"**.
- **Desactivar:** Seleccione la categoría y haga clic en **"Desactivar"**. Las categorías desactivadas no aparecerán al registrar nuevos productos.

---

## 8. Módulo de Ventas

### 8.1 Registrar una Venta

El proceso de venta es idéntico al del perfil VENDEDOR. Consulte el [Manual del Vendedor](./MANUAL_VENDEDOR.md#4-módulo-de-ventas) para detalles.

**[CAPTURA DE PANTALLA: Pantalla de ventas con productos agregados]**
*Imagen sugerida: Interfaz de ventas con campo de búsqueda, tabla temporal y total.*

### 8.2 Resumen del Proceso

1. Busque productos/platillos por nombre (autocompletado).
2. Seleccione los items deseados.
3. Verifique cantidades y total.
4. Haga clic en **"Confirmar Venta"**.
5. El sistema descuenta stock automáticamente.

---

## 9. Control de Inventario

### 9.1 Acceder al Módulo

Haga clic en el botón **"Inventario"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Panel de inventario con KPIs]**
*Imagen sugerida: Panel mostrando KPIs (total productos, movimientos del día, alertas activas) y tabla de productos.*

### 9.2 KPIs del Panel

| Indicador | Descripción |
|---|---|
| Total Productos | Cantidad de productos activos |
| Movimientos Hoy | Movimientos de inventario registrados hoy |
| Alertas Activas | Productos con stock crítico |

### 9.3 Registrar un Movimiento de Inventario

1. Haga clic en **"Nuevo Movimiento"**.
2. Complete el formulario:

| Campo | Descripción | Obligatorio |
|---|---|---|
| Producto | Seleccione el producto del catálogo | Sí |
| Tipo Movimiento | ENTRADA, MERMA o AJUSTE | Sí |
| Cantidad | Cantidad a registrar | Sí |
| Motivo | Razón del movimiento | Sí |

**[CAPTURA DE PANTALLA: Formulario de registro de movimiento]**
*Imagen sugerida: Formulario con campos: producto (desplegable), tipo movimiento (radio/select), cantidad, motivo.*

### 9.4 Tipos de Movimiento

| Tipo | Cuándo usarlo | Ejemplo |
|---|---|---|
| **ENTRADA** | Ingreso de mercancía al almacén | Llegada de proveedor |
| **MERMA** | Pérdida de producto por deterioro, vencimiento o rotura | Producto vencido, empaque roto |
| **AJUSTE** | Corrección de diferencias en el inventario | Error de conteo, sobrante |

> **SALIDA_VENTA** no se registra manualmente. Se genera automáticamente al confirmar una venta.

### 9.5 Historial de Movimientos

La tabla de historial muestra todos los movimientos registrados, con filtros por:

- **Producto:** Seleccione un producto específico.
- **Fecha:** Seleccione un rango de fechas.

**[CAPTURA DE PANTALLA: Historial de movimientos con filtros]**
*Imagen sugerida: Tabla de historial con filtros de producto y fecha, mostrando tipo, cantidad, fecha y usuario.*

### 9.6 Alertas de Stock Crítico

Los productos con stock actual menor o igual al stock mínimo se muestran con una alerta visual (color rojo o icono de advertencia). Revise periódicamente esta sección para evitar desabastecimiento.

---

## 10. Estadísticas y Reportes

### 10.1 Acceder al Módulo

Haga clic en el botón **"Estadísticas"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Panel de estadísticas con reportes]**
*Imagen sugerida: Panel con secciones: ventas por período, ranking de productos, balance financiero, ranking de vendedores.*

### 10.2 Reportes Disponibles

| Reporte | Descripción |
|---|---|
| **Ventas por Período** | Total de ventas agrupadas por día, semana o mes |
| **Ranking de Productos** | Productos más vendidos en un período |
| **Balance Financiero** | Ingresos totales, costos y margen |
| **Ranking de Vendedores** | Rendimiento de cada vendedor (ventas realizadas) |

### 10.3 Filtrar Reportes

Seleccione la fecha de inicio y fecha de fin para ajustar el período del reporte.

### 10.4 Interpretar los Resultados

- Los reportes se muestran en tablas ordenadas por defecto de mayor a menor.
- El balance financiero muestra el total de ingresos del período seleccionado.

**[CAPTURA DE PANTALLA: Ejemplo de reporte de ranking de productos]**
*Imagen sugerida: Tabla con productos ordenados por cantidad vendida, mostrando nombre, cantidad, total.*

---

## 11. Módulo de Auditoría

### 11.1 Acceder al Módulo

Haga clic en el botón **"Auditoría"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla de auditoría]**
*Imagen sugerida: Tabla de eventos de auditoría con columnas: fecha, módulo, entidad, acción, detalle, usuario.*

### 11.2 ¿Qué se Registra?

El sistema registra automáticamente:

| Evento | Ejemplo |
|---|---|
| Alta de producto | "Se registró el producto 'Harina' " |
| Edición de producto | "Se modificó el precio de 'Aceite' de 1000 a 1200" |
| Desactivación de producto | "Se desactivó el producto 'Galletas' " |
| Alta de usuario | "Se registró el usuario 'Pedro López' " |
| Desactivación de usuario | "Se desactivó el usuario 'vendedor123' " |
| Alta de categoría | "Se registró la categoría 'Lácteos' " |
| Alta de platillo | "Se registró el platillo 'Pizza Familiar' " |
| Modificación de platillo | "Se modificó la receta de 'Hamburguesa' " |

### 11.3 Consultar la Auditoría

Utilice los filtros disponibles:

- **Módulo:** Filtrar por módulo (Productos, Usuarios, etc.)
- **Fecha:** Rango de fechas
- **Usuario:** Eventos realizados por un usuario específico

### 11.4 Propósito

La auditoría permite:
- Rastrear quién hizo qué cambio y cuándo
- Detectar cambios no autorizados
- Mantener un historial completo para fines de control interno

---

## 12. Módulo IA — Predicción de Stock

### 12.1 Acceder al Módulo

Haga clic en el botón **"Estadísticas"** y luego en la sección de predicción.

**[CAPTURA DE PANTALLA: Sección de predicción de stock]**
*Imagen sugerida: Panel con botón "Ejecutar Predicción" y tabla con resultados de la predicción.*

### 12.2 ¿Qué Hace?

Utiliza inteligencia artificial (Facebook Prophet) para:
- Predecir la demanda de los próximos **7 días**.
- Calcular **días hasta agotamiento** del stock actual.
- Sugerir **cantidad de reorden** recomendada.

### 12.3 Requisitos

- **Python 3.8+** instalado con las librerías `prophet` y `pandas`.
- **Datos históricos:** El sistema exporta automáticamente los datos de ventas y stock a archivos CSV.

### 12.4 Ejecutar una Predicción

1. Haga clic en el botón **"Ejecutar Predicción"**.
2. El sistema exportará los datos y ejecutará el script de Python.
3. Espere mientras el proceso se completa (puede tomar algunos segundos).
4. Los resultados se mostrarán en la tabla de predicción.

### 12.5 Interpretar Resultados

| Columna | Descripción |
|---|---|
| Producto | Nombre del producto |
| Demanda Estimada (7 días) | Cantidad que se proyecta vender en los próximos 7 días |
| Stock Actual | Cantidad disponible actualmente |
| Días hasta Agotamiento | Estimación de días hasta que el stock llegue a 0 |
| Cantidad Sugerida de Reorden | Cantidad recomendada para reabastecer |

**[CAPTURA DE PANTALLA: Resultados de predicción con tabla de datos]**
*Imagen sugerida: Tabla con los resultados de la predicción mostrando las columnas descritas.*

> **Nota:** La precisión de la predicción mejora cuanto más datos históricos tenga el sistema. Las primeras predicciones pueden tener menor precisión.

---

## 13. Módulo IA-SQL (Lenguaje Natural)

### 13.1 Acceder al Módulo

Haga clic en el botón **"IA-SQL"** en el Panel Principal.

### 13.2 Configuración Inicial (API Key)

Antes de usar el módulo, debe configurar la **API Key de Google Gemini**:

1. Haga clic en el icono de **engranaje** (configuración) en la esquina superior.
2. Ingrese su API Key de Gemini en el campo correspondiente.
3. Haga clic en **"Guardar"**.

**[CAPTURA DE PANTALLA: Ventana de configuración de API Key]**
*Imagen sugerida: Ventana emergente con campo para ingresar la API Key y botón Guardar.*

> **¿Cómo obtener una API Key?**
> 1. Visite https://aistudio.google.com/apikey
> 2. Inicie sesión con su cuenta de Google.
> 3. Cree una nueva API Key (es gratuita).
> 4. Cópiela y péguela en el sistema.

### 13.3 Realizar una Consulta

1. Escriba su pregunta en español en el campo de texto.
2. Haga clic en **"Consultar"**.
3. El sistema convertirá su pregunta a SQL, ejecutará la consulta y mostrará los resultados.

### 13.4 Ejemplos de Consultas Útiles

| Pregunta | Para qué sirve |
|---|---|
| "¿Qué productos tienen menos de 10 unidades en stock?" | Identificar productos próximos a agotarse |
| "¿Cuánto se vendió en total la semana pasada?" | Reporte de ingresos semanales |
| "¿Cuál es el producto que más se ha vendido este mes?" | Identificar el producto más popular |
| "¿Qué vendedor hizo más ventas hoy?" | Evaluar desempeño del personal |
| "¿Cuántos productos están por vencer?" | Control de fechas de vencimiento |
| "¿Cuál es el platillo más rentable?" | Análisis de rentabilidad |

### 13.5 Seguridad

El módulo IA-SQL tiene tres capas de seguridad:
1. **Prompt restrictivo:** Gemini recibe instrucciones de solo generar SELECT.
2. **Validación en Java:** El sistema verifica que la consulta generada sea solo SELECT.
3. **Conexión read-only:** La base de datos se abre en modo solo lectura.

> **No es posible modificar o eliminar datos a través de este módulo.**

---

## 14. Anulación de Operaciones

### 14.1 Anular una Venta

Actualmente, el sistema no cuenta con un botón exclusivo de "Anular Venta". Para manejar devoluciones o anulaciones:

1. **Registre una venta negativa:** Realice una venta con cantidades negativas de los productos a devolver.
2. **Registre una entrada de inventario:** Para reponer el stock de los productos devueltos, registre una entrada manual en el módulo de Inventario.
3. **Documente la anulación:** Anote el motivo en el campo "Motivo" del movimiento de inventario para mantener trazabilidad.

> **Política recomendada:** Establezca un procedimiento operativo para anulaciones y devoluciones, y asegúrese de que todo quede registrado en la auditoría.

---

## 15. Respaldo y Mantenimiento

### 15.1 Archivos a Resguardar

| Archivo | Ubicación | Frecuencia recomendada |
|---|---|---|
| Base de datos | `DBventasInventario.db` (raíz del proyecto) | Diaria |
| Base secundaria | `identifier.sqlite` (raíz del proyecto) | Diaria |
| Modelos IA generados | `modelos_ia/` | Semanal |
| Archivos CSV exportados | `datos_ventas.csv`, `datos_stock.csv` | Semanal |

### 15.2 Cómo Hacer un Respaldo Manual

1. Asegúrese de que el sistema esté **cerrado** (nadie lo está usando).
2. Copie los archivos de base de datos a una ubicación segura:
   ```
   backup_2026-06-19/
   ├── DBventasInventario.db
   └── identifier.sqlite
   ```
3. (Opcional) Comprima la carpeta en un archivo ZIP.

### 15.3 Cómo Restaurar un Respaldo

1. Cierre el sistema completamente.
2. Reemplace el archivo `DBventasInventario.db` con la copia de seguridad.
3. Inicie el sistema nuevamente.

> **Advertencia:** Al restaurar un respaldo, perderá todos los datos generados después de la fecha del respaldo.

### 15.4 Mantenimiento Preventivo

| Tarea | Frecuencia | Descripción |
|---|---|---|
| Respaldo de BD | Diario | Copiar `DBventasInventario.db` a un disco externo o nube |
| Verificar alertas de stock | Diario | Revisar productos con stock crítico desde el panel |
| Revisar auditoría | Semanal | Verificar eventos inusuales en el log de auditoría |
| Limpiar archivos temporales | Mensual | Eliminar archivos CSV antiguos y modelos IA obsoletos |
| Verificar espacio en disco | Mensual | Asegurar que hay espacio suficiente para la BD |
| Actualizar API Key Gemini | Cuando expire | Verificar que la API Key siga activa |

---

## 16. Solución de Problemas Comunes

### 16.1 Problemas de Acceso

| Problema | Causa | Solución |
|---|---|---|
| No recuerda la contraseña | — | Use el usuario por defecto `admin`/`admin` si no se ha cambiado, o restaure desde respaldo |
| Usuario bloqueado | Intentos fallidos repetidos | Verifique la contraseña, si persiste, contacte al soporte técnico |
| Error "Base de datos no encontrada" | Archivo `.db` eliminado o movido | Restaure desde el respaldo más reciente |

### 16.2 Problemas de Ventas

| Problema | Causa | Solución |
|---|---|---|
| No se puede confirmar venta | Stock insuficiente | Verifique la cantidad disponible del producto |
| El total no es correcto | Error en precio de producto | Edite el producto y corrija el precio |
| No aparece un producto en la búsqueda | Producto inactivo o mal tipeado | Verifique que el producto esté activo |

### 16.3 Problemas de Inventario

| Problema | Causa | Solución |
|---|---|---|
| El stock no se actualizó después de una venta | Error en el proceso | Registre un movimiento de ajuste manual |
| Movimiento duplicado | Error humano o del sistema | Registre un movimiento inverso para corregir |
| Stock negativo | Error en la lógica de descuento | Revise y ajuste el stock manualmente |

### 16.4 Problemas de IA

| Problema | Causa | Solución |
|---|---|---|
| La predicción no se ejecuta | Python no instalado o librerías faltantes | Verifique que Python 3.8+ esté instalado y ejecute `pip install prophet pandas` |
| Error "API Key no configurada" | Falta la clave de Gemini | Configure la API Key en el módulo IA-SQL |
| La IA-SQL da resultados incorrectos | Pregunta ambigua | Reformule la pregunta con más detalle |

### 16.5 Problemas Generales

| Problema | Causa | Solución |
|---|---|---|
| El sistema no inicia | Java no instalado o versión incorrecta | Verifique `java -version` (debe ser 17+) |
| Error "Tabla no encontrada" | Base de datos corrupta o incompleta | Restaure desde respaldo o ejecute `DbManager` para recrear tablas |
| El sistema se congela | Proceso de Python en segundo plano | Espere a que termine o reinicie el sistema |

---

## 17. Apéndice: Estructura de la Base de Datos

| Tabla | Propósito |
|---|---|
| `usuario` | Usuarios del sistema (id, nombre, rut, password, rol, estado) |
| `categoria` | Categorías de productos (id, nombre, descripción, activa) |
| `producto` | Catálogo de productos (id, nombre, precio, stock, tipo, unidad, etc.) |
| `platillo` | Catálogo de platillos con recetas (id, nombre, precio, costo, estado) |
| `detalle_platillo` | Ingredientes de cada platillo (idPlatillo, idProducto, cantidad) |
| `venta` | Registro de ventas (id, fecha, idUsuario, total, tipoPago, estado) |
| `detalle_venta` | Items de cada venta (idVenta, idProducto, idPlatillo) |
| `historial_inventario` | Movimientos de stock (idProducto, tipo, cantidad, fecha, motivo) |
| `sesion` | Tokens de sesión persistente |
| `auditoria_eventos` | Registro de auditoría (módulo, entidad, acción, detalle, usuario) |
| `llm` | API Key de Gemini |

---

## 18. Glosario

| Término | Definición |
|---|---|
| **API Key** | Clave de autenticación para consumir servicios de Google Gemini |
| **Auditoría** | Registro cronológico de eventos del sistema para trazabilidad |
| **Borrado lógico** | Desactivación de un registro en lugar de eliminación física |
| **CRUD** | Create, Read, Update, Delete — operaciones básicas de gestión |
| **CSV** | Formato de archivo de valores separados por comas para exportación |
| **IA Predictiva** | Módulo que usa Prophet para pronosticar demanda futura |
| **IA-SQL** | Módulo que convierte lenguaje natural a consultas SQL mediante Gemini |
| **KPI** | Indicador clave de rendimiento (Key Performance Indicator) |
| **Merma** | Pérdida de inventario por deterioro, vencimiento o rotura |
| **Prophet** | Librería de Facebook para predicción de series temporales |
| **Rollover** | Mecanismo que descuenta una unidad completa al agotarse la fracción |
| **Stock crítico** | Producto con stock igual o inferior al mínimo definido |
| **Token de sesión** | Identificador único que mantiene la sesión activa del usuario |

---

*Documento generado el 19 de junio de 2026.*
