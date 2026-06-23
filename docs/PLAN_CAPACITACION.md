# Plan de Capacitación — Sistema ELUNEY (puntoVentas)

## 1. Objetivo General
Capacitar al personal en el uso operativo y administrativo del sistema de punto de venta y control de inventario ELUNEY, asegurando la correcta ejecución de los procesos de venta, gestión de productos, inventario y uso de los módulos de inteligencia artificial.

## 2. Perfiles de Usuario

| Perfil | Rol en el Sistema | Módulos a los que accede |
|---|---|---|
| **VENDEDOR** | Operador de caja / atención al cliente | Dashboard, Productos (consulta), Ventas, Platillos, Categorías, IA-SQL |
| **ADMIN** | Administrador / dueño / encargado | Todos los módulos, incluyendo Usuarios, Estadísticas, Inventario, Auditoría |

## 3. Estructura de la Capacitación

La capacitación se divide en **6 módulos** impartidos en sesiones progresivas. Cada módulo incluye teoría (20 %) y práctica guiada (80 %).

| Módulo | Tema | Duración estimada | Dirigido a |
|---|---|---|---|
| I | Introducción y configuración del entorno | 1.5 h | ADMIN |
| II | Módulo de ventas (caja / POS) | 2 h | VENDEDOR, ADMIN |
| III | Gestión de productos, platillos y categorías | 2 h | ADMIN |
| IV | Control de inventario y movimientos | 1.5 h | ADMIN |
| V | Panel de administración: usuarios, estadísticas y auditoría | 1.5 h | ADMIN |
| VI | Módulos de inteligencia artificial (predicción y SQL natural) | 2 h | ADMIN |

**Duración total:** 10.5 horas (distribuible en jornadas de 2-3 horas).

---

## MÓDULO I — Introducción y Configuración del Entorno

### Dirigido a: ADMIN

### Contenido
1. **Arquitectura del sistema**
   - Stack tecnológico: Java 17 + JavaFX + SQLite + Python
   - Base de datos local (`DBventasInventario.db`)
   - Comunicación Java ↔ Python para módulos IA
2. **Requisitos técnicos**
   - Instalación de JDK 17
   - Instalación de Python 3.8+ con Prophet, pandas, google-genai
   - Verificación de dependencias
3. **Instalación y puesta en marcha**
   - Clonar repositorio / copiar ejecutable
   - Compilar con Maven (`mvn clean package`)
   - Ejecutar la aplicación (`mvn javafx:run` o JAR)
4. **Primer inicio**
   - Creación automática de tablas y datos semilla
   - Usuarios por defecto: `admin` / `admin`, `vendedor123` / `vendedor123`
   - Estructura de archivos generados

### Práctica
- Instalar dependencias y ejecutar la aplicación
- Verificar que la base de datos se crea correctamente
- Iniciar sesión con ambos roles y explorar el panel principal

### Criterio de evaluación
- El asistente logra ejecutar la aplicación sin errores
- Identifica correctamente los elementos del panel principal

---

## MÓDULO II — Módulo de Ventas (Caja / POS)

### Dirigido a: VENDEDOR, ADMIN

### Contenido
1. **Acceso al módulo**
   - Botón "Ventas" en el Dashboard o panel principal
   - Interfaz de registro de venta (`CargarVentaController`)
2. **Registro de una venta**
   - Campo de búsqueda con autocompletado por nombre de producto
   - Selección de productos DIRECTOS y PLATILLOS
   - Cantidad y cálculo automático del total
   - Tabla de venta temporal (items antes de confirmar)
3. **Confirmación de venta**
   - Tipos de pago (efectivo, transferencia, etc.)
   - Descuento automático del stock al confirmar
   - Visualización del comprobante
4. **Consulta de ventas históricas**
   - Filtro por fechas
   - Detalle de cada venta (productos, cantidades, total)
5. **Buenas prácticas**
   - Verificar stock antes de confirmar
   - Manejo de devoluciones (no implementado — policy operativa)
   - Cierre de caja (procedimiento externo al sistema)

### Práctica
- Realizar 3 ventas con diferentes combinaciones de productos/platillos
- Consultar el historial de ventas del día
- Verificar que el stock se descuenta correctamente

### Criterio de evaluación
- Registrar una venta completa de principio a fin
- Utilizar el autocompletado correctamente
- Interpretar la tabla de venta temporal

---

## MÓDULO III — Gestión de Productos, Platillos y Categorías

### Dirigido a: ADMIN

### Contenido

#### 3.1 Categorías
- Registro, edición y desactivación de categorías
- Asociación de productos a categorías

#### 3.2 Productos
- **Tipos de producto:**
  - `DIRECTO` — se vende tal cual (ej. gaseosa, lácteo)
  - `PLATILLO` — producto elaborado con receta (se maneja desde el módulo de platillos)
  - `SOLO_INVENTARIO` — insumo no vendible directamente (ej. harina, aceite)
- **Campos del formulario:** nombre, precio compra, precio venta, categoría, stock actual, stock mínimo, unidad de medida (UNIDAD / GRAMOS / MILILITROS), fecha de vencimiento
- **Regla de margen mínimo:** precio venta >= precio compra × 1.10
- **Stock crítico:** alerta cuando stock actual <= stock mínimo
- **Borrado lógico:** desactivación en lugar de eliminación física si el producto tiene ventas o está en recetas

#### 3.3 Platillos (Recetas)
- Registro de platillo compuesto por ingredientes (productos)
- Asignación de cantidad de cada ingrediente
- Cálculo automático del costo de producción
- Lógica de descuento de inventario al vender (descuenta ingredientes)
- **Rollover:** cuando se acaba la fracción (gramos/ml), descuenta una unidad completa y reinicia la fracción

### Práctica
- Crear 2 categorías
- Registrar 5 productos (de distintos tipos y unidades)
- Crear 1 platillo con 3 ingredientes
- Editar y desactivar un producto
- Verificar stock crítico

### Criterio de evaluación
- Registrar productos y platillos correctamente
- Explicar la diferencia entre los 3 tipos de producto
- Configurar correctamente unidades de medida

---

## MÓDULO IV — Control de Inventario y Movimientos

### Dirigido a: ADMIN

### Contenido
1. **Interfaz del Panel de Inventario**
   - KPIs: total productos, movimientos del día, alertas activas
   - Tabla de productos con stock actual
2. **Tipos de movimiento**
   - `ENTRADA` — ingreso de mercancía
   - `SALIDA_VENTA` — descuento automático (no se registra manualmente)
   - `MERMA` — pérdida o deterioro
   - `AJUSTE` — corrección de inventario
3. **Registro de movimientos manuales**
   - Selección de producto, tipo, cantidad y motivo
   - Actualización automática del stock
4. **Historial de movimientos**
   - Filtro por producto y fecha
   - Trazabilidad completa de cada producto
5. **Alertas de stock**
   - Identificación de productos con stock bajo el mínimo
   - Reporte visual en el panel

### Práctica
- Registrar 2 entradas de stock
- Registrar 1 merma y 1 ajuste
- Consultar el historial de movimientos de un producto
- Identificar productos con stock crítico

### Criterio de evaluación
- Registrar movimientos de inventario correctamente
- Interpretar los KPIs del panel
- Explicar la diferencia entre cada tipo de movimiento

---

## MÓDULO V — Panel de Administración: Usuarios, Estadísticas y Auditoría

### Dirigido a: ADMIN

### Contenido

#### 5.1 Gestión de Usuarios
- Registro de nuevos usuarios (nombre, apellido, RUT, teléfono, rol)
- Roles disponibles: ADMIN y VENDEDOR
- Edición y desactivación de usuarios
- Política de contraseñas (hash SHA-256)

#### 5.2 Estadísticas
- Reporte de ventas por período
- Ranking de productos más vendidos
- Balance financiero (ingresos totales)
- Ranking de vendedores (rendimiento)

#### 5.3 Auditoría
- Registro automático de eventos: altas, bajas y modificaciones
- Columnas: fecha, módulo, entidad, acción, detalle, usuario
- Consulta del historial de auditoría

### Práctica
- Crear un usuario VENDEDOR y uno ADMIN
- Consultar estadísticas del período actual
- Revisar el log de auditoría después de hacer cambios

### Criterio de evaluación
- Gestionar usuarios (alta, edición, desactivación)
- Interpretar los reportes estadísticos
- Explicar el propósito del módulo de auditoría

---

## MÓDULO VI — Módulos de Inteligencia Artificial

### Dirigido a: ADMIN

### Contenido

#### 6.1 IA Predictiva (Prophet)
- **¿Qué hace?** Predice la demanda de los próximos 7 días por producto
- **¿Cómo funciona?**
  - Exporta datos históricos a CSV (`datos_ventas.csv`, `datos_stock.csv`)
  - Ejecuta script Python (`modelo_predictivo.py`, `prediccion_stock.py`)
  - Genera modelos JSON en la carpeta `modelos_ia/`
- **Interpretación de resultados:**
  - Demanda estimada por producto
  - Días hasta agotamiento del stock
  - Cantidad sugerida de reorden
- **Limitaciones:** requiere datos históricos suficientes; la precisión mejora con el tiempo

#### 6.2 IA Conversacional — Lenguaje Natural a SQL (Gemini)
- **¿Qué hace?** Permite consultar la base de datos escribiendo preguntas en español
- **¿Cómo funciona?**
  - El usuario escribe una pregunta en la interfaz JavaFX
  - Se envía a Python que llama a Google Gemini 3.5 Flash
  - Gemini genera una consulta SQL
  - Java ejecuta la consulta en modo read-only y muestra resultados
- **Seguridad:**
  - Prompt restrictivo (solo SELECT, solo tablas conocidas)
  - Validación Java: solo permite sentencias SELECT
  - Conexión a BD en modo read-only
- **Configuración:** ingreso de API Key de Gemini (engranaje → configurar)
- **Ejemplos de preguntas:**
  - "¿Cuántos productos tenemos con stock bajo?"
  - "¿Cuál fue el producto más vendido la semana pasada?"
  - "¿Cuántas ventas hizo cada vendedor?"

### Práctica
- Ejecutar una predicción de stock desde el panel de estadísticas
- Interpretar los resultados de la predicción
- Configurar la API Key de Gemini
- Realizar 5 consultas en lenguaje natural y verificar resultados
- Probar una consulta maliciosa (ej. "DROP TABLE") y verificar que es bloqueada

### Criterio de evaluación
- Ejecutar el módulo de predicción y leer los resultados
- Configurar y utilizar el módulo IA-SQL correctamente
- Explicar las medidas de seguridad del módulo IA-SQL

---

## 4. Metodología de Capacitación

| Actividad | % del tiempo | Descripción |
|---|---|---|
| Exposición teórica | 20 % | Presentación de conceptos con apoyo visual (diapositivas / pantalla compartida) |
| Demostración en vivo | 20 % | El instructor realiza un flujo completo mientras los asistentes observan |
| Práctica guiada | 40 % | Los asistentes ejecutan ejercicios con supervisión del instructor |
| Ejercicio autónomo | 20 % | El asistente resuelve un caso práctico sin ayuda |

### Recursos necesarios
- Computador por asistente con JDK 17, Python 3.8+ y Maven instalados
- Proyector o monitor compartido para demostraciones
- Acceso a internet (para módulo IA-SQL con Gemini)
- Base de datos de prueba con datos ficticios (incluida en el repositorio)
- Guía rápida de referencia (`docs/GUIA_RAPIDA_REFERENCIA.md`)

---

## 5. Evaluación

### Evaluación por módulo
Al finalizar cada módulo, el asistente debe completar una **rúbrica de verificación** con los criterios listados en cada módulo.

### Evaluación final (integradora)
El asistente debe resolver un **caso práctico integral** que abarque todos los módulos:

1. Iniciar sesión como ADMIN
2. Crear un nuevo usuario VENDEDOR
3. Registrar una categoría y 3 productos
4. Registrar un platillo con 2 ingredientes
5. Realizar una venta con productos y platillos
6. Registrar una entrada de inventario
7. Consultar el historial de auditoría
8. Ejecutar una predicción de stock
9. Realizar 2 consultas con IA-SQL en lenguaje natural

### Criterios de aprobación
- 80 % de las tareas del caso práctico completadas correctamente
- Sin errores críticos (ej. pérdida de datos, venta mal registrada)

---

## 6. Material de Apoyo

| Recurso | Ubicación |
|---|---|
| Manual de instalación y configuración | `docs/MANUAL_SETUP.md` |
| Guía rápida de referencia | `docs/GUIA_RAPIDA_REFERENCIA.md` |
| Arquitectura IA predictiva | `docs/ARQUITECTURA_IA_PREDICCION.md` |
| Arquitectura IA SQL | `docs/ARQUITECTURA_IA_SQL.md` |
| Matriz de validaciones del sistema | `MATRIZ_VALIDACIONES.md` |
| Diagrama de componentes | `docs/diagramas/componentes.puml` |
| Diagrama de flujo de datos | `docs/diagramas/flujo_datos.puml` |
| Código fuente completo | `src/` |

---

## 7. Cronograma Sugerido

| Sesión | Módulos | Duración |
|---|---|---|
| Sesión 1 | Módulo I (Introducción) + Módulo II (Ventas) | 3 h |
| Sesión 2 | Módulo III (Productos, platillos, categorías) | 2.5 h |
| Sesión 3 | Módulo IV (Inventario) + Módulo V (Admin) | 2.5 h |
| Sesión 4 | Módulo VI (IA) + Evaluación final | 2.5 h |

**Total:** 4 sesiones / 10.5 horas

---

## 8. Responsabilidades

### Instructor
- Preparar el entorno de capacitación con anticipación
- Guiar las demostraciones y prácticas
- Resolver dudas durante los ejercicios
- Evaluar el desempeño de los asistentes

### Asistente
- Asistir a todas las sesiones
- Realizar los ejercicios prácticos
- Completar la evaluación final
- Firmar la constancia de capacitación

---

## 9. Glosario

| Término | Definición |
|---|---|
| **POS** | Point of Sale — Punto de venta |
| **Stock crítico** | Producto cuyo stock actual está por debajo del mínimo definido |
| **PLATILLO** | Producto elaborado compuesto por múltiples ingredientes |
| **DIRECTO** | Producto que se vende sin necesidad de elaboración |
| **SOLO_INVENTARIO** | Insumo que no se vende directamente (ej. materia prima) |
| **Rollover** | Mecanismo que descuenta una unidad completa cuando se agota la fracción de gramos/ml |
| **Prophet** | Biblioteca de Facebook para predicción de series temporales |
| **Gemini** | Modelo de lenguaje de Google IA |
| **API Key** | Clave de autenticación para consumir la API de Gemini |
| **Merma** | Pérdida de inventario por deterioro, vencimiento o rotura |
| **Auditoría** | Registro cronológico de eventos para trazabilidad |

---

*Documento generado el 19 de junio de 2026.*
