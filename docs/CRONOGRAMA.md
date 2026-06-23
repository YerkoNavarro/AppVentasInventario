# Cronograma del Proyecto ELUNEY — 18 Semanas

## Fase 1 — Fundación y Arquitectura (Semana 1–4)

| Semana | Actividades |
|--------|-------------|
| **1** | Configuración del proyecto Maven + JavaFX + SQLite. Esquema de base de datos (11 tablas). |
| **2** | Capa de repositorios (CRUD base) y conexión a BD. Modelo de dominio (Producto, Categoria, Usuario). |
| **3** | Login con roles (Admin/Vendedor), autenticación por RUT + contraseña, sesiones persistentes. |
| **4** | Dashboard principal con alertas de stock crítico. Navegación entre módulos. |

## Fase 2 — Funcionalidades Core e IA (Semana 5–16)

| Semana | Actividades |
|--------|-------------|
| **5** | CRUD completo de productos (tipos: directo, platillo, solo inventario). Categorías. |
| **6** | Módulo de platillos (recetas): ingredientes, cálculo de costo de producción. |
| **7** | Registro de ventas con selección de productos/platillos y cálculo de totales. |
| **8** | Detalle de venta, validaciones y finalización de transacciones. |
| **9** | Control de inventarios: entradas, salidas, ajustes con motivo obligatorio. |
| **10** | Alertas de stock bajo, actualización automática al vender. |
| **11** | Gestión de usuarios (CRUD, activar/inactivar, asignación de roles). |
| **12** | Refinamiento de dashboard con indicadores clave. |
| **13** | Estadísticas: ranking de productos y vendedores, reportes financieros. |
| **14** | Módulo de auditoría (trazabilidad de todas las operaciones). |
| **15** | **IA — Predicción de stock**: Prophet en Python, cálculo de riesgo de desabastecimiento (7 días). |
| **16** | **IA — Consultas SQL**: Google Gemini 3.5 Flash, IPC Java ⇄ Python vía JSON. |

## Fase 3 — Pruebas y Documentación (Semana 17–18)

| Semana | Actividades |
|--------|-------------|
| **17** | Pruebas unitarias (JUnit 5 + Mockito): ~100 tests en 5 clases. Corrección de bugs críticos. |
| **18** | Documentación: manuales de admin/vendedor/setup, matriz de validaciones, diagramas ASCII y PlantUML, reporte de estado final. |
