# Manual del Vendedor — Sistema ELUNEY (puntoVentas)

---

## Tabla de Contenido

1. [Introducción](#1-introducción)
2. [Acceso al Sistema](#2-acceso-al-sistema)
3. [Panel Principal](#3-panel-principal)
4. [Módulo de Ventas](#4-módulo-de-ventas)
5. [Consulta de Productos](#5-consulta-de-productos)
6. [Consulta de Platillos y Categorías](#6-consulta-de-platillos-y-categorías)
7. [Módulo IA-SQL (Consulta en Lenguaje Natural)](#7-módulo-ia-sql-consulta-en-lenguaje-natural)
8. [Solución de Problemas Comunes](#8-solución-de-problemas-comunes)
9. [Glosario](#9-glosario)

---

## 1. Introducción

**ELUNEY** es un sistema de punto de venta y control de inventario diseñado para la gestión operativa del negocio. Este manual está dirigido al perfil **VENDEDOR**, encargado de la operación diaria de caja, atención al cliente y registro de ventas.

### Lo que puedes hacer como Vendedor

| Función | Descripción |
|---|---|
| Registrar ventas | Crear nuevas ventas con productos y platillos |
| Consultar productos | Ver información y stock de productos |
| Consultar platillos | Ver platillos disponibles y sus ingredientes |
| Consultar categorías | Ver las categorías de productos |
| IA-SQL | Hacer preguntas en español sobre los datos del sistema |

---

## 2. Acceso al Sistema

### 2.1 Requisitos

- Computador con el sistema ELUNEY instalado
- Credenciales de usuario (RUT y contraseña) proporcionadas por el administrador

### 2.2 Inicio de Sesión

1. Ejecute el sistema ELUNEY desde el acceso directo en el escritorio o desde el menú de inicio.
2. Aparecerá la pantalla de inicio de sesión.

**[CAPTURA DE PANTALLA: Pantalla de Login]**
*Imagen sugerida: Ventana de login con campos de RUT y Contraseña, y botón "Iniciar Sesión".*

3. Ingrese su **RUT** en el campo correspondiente.
4. Ingrese su **Contraseña**.
5. Haga clic en el botón **"Iniciar Sesión"**.

### 2.3 Inicio de Sesión Exitoso

Si las credenciales son correctas, ingresará al **Panel Principal** del sistema.

**[CAPTURA DE PANTALLA: Panel Principal - Vista Vendedor]**
*Imagen sugerida: Dashboard principal mostrando los módulos disponibles para el perfil VENDEDOR (Ventas, Productos, Platillos, Categorías, IA-SQL).*

### 2.4 Solución de Problemas de Acceso

| Problema | Posible causa | Solución |
|---|---|---|
| "Credenciales incorrectas" | RUT o contraseña mal escritos | Verifique que el RUT esté sin puntos y con guion. La contraseña distingue mayúsculas. |
| "Usuario desactivado" | Su cuenta fue desactivada por el administrador | Contacte al administrador del sistema. |
| La aplicación no abre | Java no está instalado | Verifique que JDK 17 esté instalado. |

---

## 3. Panel Principal

El Panel Principal es la pantalla que ve después de iniciar sesión. Desde aquí puede acceder a todos los módulos disponibles para su perfil.

**[CAPTURA DE PANTALLA: Panel Principal con botones de navegación]**
*Imagen sugerida: Panel principal con los botones de acceso a cada módulo y las métricas del dashboard.*

### 3.1 Elementos del Panel Principal

| Elemento | Descripción |
|---|---|
| **Barra superior** | Muestra el nombre del usuario logueado y la fecha/hora actual |
| **Botón Ventas** | Accede al módulo de registro de ventas |
| **Botón Productos** | Accede al catálogo de productos |
| **Botón Platillos** | Accede al catálogo de platillos |
| **Botón Categorías** | Accede a las categorías de productos |
| **Botón IA-SQL** | Accede al asistente de consultas en lenguaje natural |
| **Botón Cerrar Sesión** | Finaliza la sesión actual |
| **Dashboard** | Muestra métricas como total de productos, ventas del día, etc. |

### 3.2 Cerrar Sesión

1. Haga clic en el botón **"Cerrar Sesión"** en la barra superior.
2. El sistema le devolverá a la pantalla de inicio de sesión.

---

## 4. Módulo de Ventas

Este es el módulo principal para su labor diaria. Aquí registra las ventas a los clientes.

### 4.1 Acceder al Módulo de Ventas

Haga clic en el botón **"Ventas"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla principal del módulo de Ventas]**
*Imagen sugerida: Interfaz de ventas con campo de búsqueda, tabla de productos seleccionados, total y botón de confirmar.*

### 4.2 Elementos de la Pantalla de Ventas

| Elemento | Descripción |
|---|---|
| **Campo de búsqueda** | Escriba el nombre del producto o platillo para buscarlo |
| **Lista de sugerencias** | Muestra productos/platillos que coinciden con la búsqueda |
| **Tabla de venta temporal** | Muestra los items agregados a la venta actual |
| **Campo Total** | Muestra el monto total de la venta |
| **Botón Confirmar Venta** | Finaliza y guarda la venta |
| **Botón Limpiar** | Elimina todos los items de la venta actual |

### 4.3 Registrar una Venta — Paso a Paso

**Paso 1: Buscar un producto o platillo**

Escriba el nombre del producto o platillo en el campo de búsqueda. El sistema mostrará sugerencias automáticamente.

**[CAPTURA DE PANTALLA: Autocompletado de productos]**
*Imagen sugerida: Campo de búsqueda mostrando sugerencias desplegables mientras el usuario escribe.*

**Paso 2: Seleccionar el item**

Haga clic en el producto o platillo deseado de la lista de sugerencias. El item se agregará a la tabla de venta temporal.

**Paso 3: Ajustar la cantidad (si es necesario)**

Por defecto se agrega 1 unidad. Si necesita cambiar la cantidad, modifique el valor en la columna "Cantidad" de la tabla temporal.

**[CAPTURA DE PANTALLA: Tabla de venta temporal con items agregados]**
*Imagen sugerida: Tabla mostrando varios productos agregados, sus cantidades, precios unitarios y subtotales.*

**Paso 4: Repetir hasta completar la venta**

Agregue todos los productos y platillos que el cliente desea llevar.

**Paso 5: Verificar el total**

Confirme que el monto total mostrado sea correcto antes de finalizar.

**Paso 6: Confirmar la venta**

Haga clic en el botón **"Confirmar Venta"**. El sistema:
- Guarda la venta en la base de datos
- Descuenta automáticamente el stock de los productos vendidos
- Si es un platillo, descuenta los ingredientes de la receta

**Paso 7: Venta completada**

Aparecerá un mensaje de confirmación. La venta está lista.

**[CAPTURA DE PANTALLA: Mensaje de venta exitosa]**
*Imagen sugerida: Ventana emergente indicando que la venta se registró correctamente.*

### 4.4 Consultar Ventas Realizadas

1. En la pantalla de Ventas, utilice el filtro de **fecha** para seleccionar el período deseado.
2. La tabla mostrará las ventas realizadas en ese período.
3. Haga clic en una venta para ver su detalle (productos, cantidades, total).

**[CAPTURA DE PANTALLA: Historial de ventas con filtro de fechas]**
*Imagen sugerida: Tabla de ventas históricas con campos de filtro por fecha.*

### 4.5 Buenas Prácticas en Ventas

- **Verifique el stock** antes de confirmar una venta con grandes cantidades.
- **Confirme el total** con el cliente antes de finalizar.
- **No cierre el sistema** mientras haya una venta en proceso.
- **Reporte al administrador** si encuentra productos con precio incorrecto.

---

## 5. Consulta de Productos

Puede consultar el catálogo de productos para ver información como precio, stock y categoría.

### 5.1 Acceder a Productos

Haga clic en el botón **"Productos"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla de consulta de productos]**
*Imagen sugerida: Tabla con la lista de productos, mostrando nombre, precio, stock, categoría y estado.*

### 5.2 Información Disponible

| Campo | Descripción |
|---|---|
| Nombre | Nombre del producto |
| Precio Venta | Precio al público |
| Stock Actual | Cantidad disponible en inventario |
| Stock Mínimo | Cantidad mínima antes de alerta |
| Categoría | Categoría a la que pertenece |
| Unidad de Medida | UNIDAD, GRAMOS o MILILITROS |
| Estado | Activo o Inactivo |

### 5.3 Filtrar y Buscar Productos

- Utilice el campo de búsqueda para encontrar productos por nombre.
- Use los filtros de categoría para ver productos de una categoría específica.

### 5.4 Identificar Productos con Stock Crítico

Los productos cuyo stock actual está por debajo del stock mínimo aparecen resaltados o con una alerta visual. Informe al administrador si detecta productos en esta situación.

---

## 6. Consulta de Platillos y Categorías

### 6.1 Platillos

Haga clic en **"Platillos"** para ver la lista de platillos disponibles.

**[CAPTURA DE PANTALLA: Pantalla de consulta de platillos]**
*Imagen sugerida: Tabla de platillos con nombre, precio, categoría y estado.*

Cada platillo muestra:
- Nombre
- Precio de venta
- Categoría
- Costo de producción
- Estado (Disponible / No disponible)

### 6.2 Categorías

Haga clic en **"Categorías"** para ver las categorías de productos.

**[CAPTURA DE PANTALLA: Pantalla de categorías]**
*Imagen sugerida: Tabla de categorías con nombre y descripción.*

---

## 7. Módulo IA-SQL (Consulta en Lenguaje Natural)

Este módulo le permite hacer preguntas en español sobre los datos del sistema y obtener respuestas sin necesidad de saber bases de datos.

### 7.1 Acceder

Haga clic en el botón **"IA-SQL"** en el Panel Principal.

**[CAPTURA DE PANTALLA: Pantalla del módulo IA-SQL]**
*Imagen sugerida: Interfaz con campo de texto para escribir la pregunta y área para mostrar resultados.*

### 7.2 Cómo Usarlo

1. Escriba su pregunta en español en el campo de texto.
2. Haga clic en **"Consultar"**.
3. El sistema procesará su pregunta y mostrará los resultados en la tabla.

### 7.3 Ejemplos de Preguntas

| Pregunta | Qué devuelve |
|---|---|
| "¿Cuántos productos tenemos en total?" | Número total de productos activos |
| "¿Qué productos tienen stock bajo?" | Lista de productos con stock crítico |
| "¿Cuántas ventas se hicieron hoy?" | Cantidad de ventas del día |
| "¿Cuál es el producto más caro?" | Producto con mayor precio de venta |
| "¿Qué platillos cuestan menos de $5000?" | Platillos con precio menor a 5000 |

### 7.4 Limitaciones

- Solo puede hacer **consultas de lectura** (no puede modificar datos).
- La calidad de la respuesta depende de cómo esté redactada la pregunta.
- Si la pregunta es muy ambigua, el sistema puede devolver resultados inesperados.

---

## 8. Solución de Problemas Comunes

| Problema | Causa probable | Solución |
|---|---|---|
| No encuentro un producto en la búsqueda | El producto puede estar inactivo o mal escrito | Verifique la ortografía. Si está inactivo, contacte al administrador. |
| El total de la venta no se actualiza | Error temporal en la interfaz | Limpie la venta y vuelva a agregar los productos. |
| No puedo confirmar la venta | Puede faltar stock de algún producto | Revise los items en la tabla temporal. |
| IA-SQL no responde | Puede faltar la API Key de Gemini | Contacte al administrador para configurar la clave. |
| El sistema está lento | Muchos datos o procesos en segundo plano | Cierre sesión y vuelva a iniciar. Si persiste, avise al administrador. |
| Olvidé mi contraseña | — | Contacte al administrador para que la restablezca. |

---

## 9. Glosario

| Término | Definición |
|---|---|
| **POS** | Punto de venta (Point of Sale) |
| **Stock** | Cantidad disponible de un producto en inventario |
| **Stock crítico** | Producto con cantidad igual o menor al mínimo permitido |
| **PLATILLO** | Producto elaborado compuesto por varios ingredientes |
| **DIRECTO** | Producto que se vende tal cual, sin preparación |
| **SKU** | Código interno de identificación del producto (ID) |
| **Autocompletado** | Función que sugiere nombres mientras escribe |
| **IA-SQL** | Módulo que convierte preguntas en español a consultas a la base de datos |
| **RUT** | Rol Único Tributario, usado como identificador de usuario |

---

*Documento generado el 19 de junio de 2026.*
