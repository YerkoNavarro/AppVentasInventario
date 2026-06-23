# Reporte de Status Final — Sistema ELUNEY (puntoVentas)

---

## 1. Resumen Ejecutivo

| Ítem | Detalle |
|---|---|
| **Proyecto** | Sistema de punto de venta y control de inventario ELUNEY |
| **Fecha del reporte** | 19 de junio de 2026 |
| **Tipo de aplicación** | Escritorio (JavaFX) + Scripts Python (IA) |
| **Estado general** | **Funcional — Con brechas respecto a la especificación original** |

### Leyenda de estados

| Símbolo | Significado |
|---|---|
| ✅ | Implementado completamente |
| ◐ | Implementado parcialmente / con diferencias |
| ❌ | No implementado |
| N/A | No aplica o fue descartado |

---

## 2. Comparativa: Especificación vs. Implementación

### 2.1 Gestión de Usuarios y Seguridad

| ID | Requisito | Estado | Observación |
|---|---|---|---|
| RF01 | RBAC con roles ADMIN y VENDEDOR | ✅ | Implementado con restricción de módulos por rol |
| RF02 | Restringir acceso VENDEDOR a costos/utilidades/edición inventario | ✅ | VENDEDOR no ve módulos de admin |
| RF03 | Autenticación (Login) con usuario y contraseña | ✅ | Login por RUT + contraseña |
| RNF01 | Contraseñas almacenadas con BCrypt | ❌ | Usa **SHA-256** (en `Encriptador.java:14`). No tiene salt ni costo computacional. |
| RNF02 | Datos sensibles encriptados/bloqueados para no autorizados | ◐ | Roles bloquean acceso, pero no hay encriptación de datos en BD |

### 2.2 Catálogo e Inventario

| ID | Requisito | Estado | Observación |
|---|---|---|---|
| RF04 | CRUD completo de productos | ✅ | Crear, leer, actualizar, desactivar/eliminar |
| RF05 | Registro de entrada de mercadería | ✅ | TipoMovimiento.ENTRADA en inventario |
| RF06 | Ajustes manuales con campo obligatorio "Nota de Motivo" | ✅ | Campo motivo obligatorio |
| RF07 | Alertas visuales de stock bajo en Dashboard | ✅ | Stock crítico resaltado |
| RNF03 | Borrado lógico si tiene historial de ventas | ✅ | Desactivación en lugar de eliminación física |
| RNF04 | Cada ajuste registra usuario, fecha y motivo en auditoría | ✅ | Auditoría funcional |

### 2.3 Ventas y POS

| ID | Requisito | Estado | Observación |
|---|---|---|---|
| RF08 | Registrar ventas con productos, precios y orden de pedido | ✅ | Ventas con autocompletado, cálculo automático de total |
| RF09 | Anular venta con reintegro automático de stock | ◐ | `anularVenta()` existe pero **solo cambia estado** (`estado=0`). **NO reintegra stock**. |
| RF10 | Escaneo por código de barras | ❌ | No implementado. No hay dependencias ni UI para scanner. |
| — | Cálculo de IVA en ventas | ❌ | No hay campo IVA ni cálculo de impuesto |
| — | Descuentos en ventas | ❌ | No hay campo descuento ni lógica de descuento |

### 2.4 Interfaz Web (Presencia Digital)

| ID | Requisito | Estado | Observación |
|---|---|---|---|
| RF12 | Página web con redes sociales y contacto | ❌ | **No existe componente web.** La aplicación es 100% escritorio JavaFX |
| RF13 | Formulario de contacto con notificación al admin | ❌ | No implementado |
| RNF06 | Diseño Mobile-First responsivo | ❌ | No aplica (no hay web) |
| RNF07 | Menú jerárquico máximo 3 niveles | N/A | No aplica |

### 2.5 Análisis, Estadísticas y Respaldo

| ID | Requisito | Estado | Observación |
|---|---|---|---|
| RF15 | Gráficos analíticos (ventas vs tiempo, productos más vendidos) | ❌ | `EstadisticasService.java` está **completamente comentado**. No hay gráficos (PieChart, BarChart, etc.) |
| RF16 | Respaldos automáticos de BD | ❌ | No hay funcionalidad de backup. Solo respaldo manual copiando archivos |
| RF17 | Resumen diario de ventas | ✅ | Mostrado en el dashboard |
| RNF08 | Disponibilidad 99.5% | N/A | Aplicación de escritorio local |
| RNF09 | Recuperación total en menos de 2 horas | ❌ | No hay procedimiento automatizado de restauración |

---

## 3. Estado de Validaciones (Matriz de Validaciones)

Las siguientes validaciones están documentadas como **"Faltante"** o **"Incompleta"** en la matriz de validaciones del documento original:

| Módulo | Validación | Estado real | Observación |
|---|---|---|---|
| Productos | Stock actual >= 0 | ❌ Faltante | No se valida que stock >= 0 al registrar |
| Productos | Stock mínimo >= 0 | ❌ Faltante | No se valida que stock mínimo >= 0 |
| Productos | Validación en UI (Controller) | ◐ Incompleta | Mayoría de validaciones solo en Service |
| Platillos | Ingredientes no vacíos | ◐ Solo en Controller | `PlatilloService.registrarPlatillo()` no lo valida |
| Usuarios | RUT válido (formato) | ✅ | Implementado en `UsuarioService.java:56-59` |
| Usuarios | Contraseña segura (mayúscula, número, símbolo, 8+ car.) | ❌ Faltante | Solo valida que no sea nula/vacía |
| Usuarios | Usuario activo en login | ❌ Faltante | No valida estado del usuario al iniciar sesión |
| Usuarios | Usuario no es admin (proteger admin) | ❌ Faltante | No impide desactivar al admin |
| Categorías | Nombre único | ✅ | Implementado en `ProductoService.java` |
| Categorías | No eliminar si tiene productos | ✅ | Implementado |
| Inventario | Cantidad requerida > 0 | ❌ Faltante | No se valida en `InventarioService` |
| Inventario | Explosión de receta | ◐ Incompleta | `validarStockIngredientes()` existe pero tiene limitantes |

---

## 4. Estado de Funcionalidades Adicionales

| Funcionalidad | Estado | Detalle |
|---|---|---|
| **IA Predictiva (Prophet)** | ✅ Implementado | Scripts Python generan predicciones, se almacenan en `modelos_ia/` |
| **IA-SQL (Gemini)** | ✅ Implementado | Lenguaje natural a SQL con 3 capas de seguridad |
| **Sesión persistente** | ✅ Implementado | Token en archivo `~/.eluney-session.token` |
| **Auditoría de eventos** | ✅ Implementado | Registro automático de altas, bajas y modificaciones |
| **Lógica de platillos con recetas** | ✅ Implementado | Descuento de ingredientes, rollover para gramos/ml |
| **Ranking de productos/vendedores** | ✅ Implementado | Reportes en módulo de estadísticas |
| **Cálculo de riesgo de stock** | ✅ Implementado | Algoritmo con 4 niveles de riesgo (0.1 a 0.9) |
| **Cierre de sesión por inactividad** | ❌ No implementado | No hay timeout de sesión |
| **Reseteo de contraseña (admin)** | ❌ No implementado | No hay función de reset para otros usuarios |

---

## 5. Incidentes e Impedimentos

### 5.1 Del registro de impedimentos original

| ID | Fecha | Descripción | Estado |
|---|---|---|---|
| 1 | 02-05-2026 | Bug visual en tabla de ventas (no se mostraba al agregar a tabla vacía) | ✅ Solucionado |
| 2 | 07-05-2026 | Bug botón vista (no funciona) | ✅ Solucionado |
| 3 | 18-05-2026 | Error en base de datos | ✅ Solucionado |
| 4 | 16-05-2026 | **Error matemático cálculo costo total producción platillo** | ❌ **Pendiente** |

### 5.2 Brechas identificadas (nuevas)

| ID | Descripción | Severidad | Impacto |
|---|---|---|---|
| B01 | Anulación de venta sin reintegro de stock | Alta | El stock no se recupera al anular una venta |
| B02 | Hash SHA-256 en lugar de BCrypt | Media | Vulnerabilidad en almacenamiento de contraseñas |
| B03 | Sin respaldos automáticos | Alta | Riesgo de pérdida de datos sin backup programado |
| B04 | Sin gráficos analíticos | Media | El módulo de estadísticas no tiene visualizaciones gráficas |
| B05 | Validaciones faltantes en UI | Media | Varias validaciones solo existen en Service, no en Controller |
| B06 | Sin validación de stock >= 0 | Media | Se pueden registrar stocks negativos |
| B07 | Componente web no implementado | Alta | La página web con carta digital no fue desarrollada |
| B08 | Sin cierre de sesión por inactividad | Baja | Sesión queda abierta indefinidamente |

---

## 6. Cobertura de Pruebas

| Módulo | Tests | Estado |
|---|---|---|
| ProductoService | 36 tests (TC-01 al TC-26) | ✅ Implementados |
| PlatilloService | 17 tests (TC-01 al TC-17) | ✅ Implementados |
| VentaService | 31 tests (TC-01 al TC-31) | ✅ Implementados |
| InventarioService | 12 tests (TC-01 al TC-12) | ✅ Implementados |
| UsuarioService | No especificado | Pendiente de verificar |
| EstadisticaService | 15 tests (TC-01 al TC-15) | ✅ Implementados |

---

## 7. Resumen de Cumplimiento por Módulo

| Módulo | Requisitos totales | ✅ Completos | ◐ Parciales | ❌ No implementados | % Cumplimiento |
|---|---|---|---|---|---|
| Usuarios y Seguridad | 5 | 3 | 1 | 1 | 70 % |
| Catálogo e Inventario | 6 | 6 | 0 | 0 | 100 % |
| Ventas y POS | 5 | 1 | 1 | 3 | 30 % |
| Web / Presencia Digital | 4 | 0 | 0 | 4 | 0 % |
| Análisis y Respaldo | 5 | 1 | 0 | 4 | 20 % |
| Validaciones (Matriz) | 14 | 4 | 6 | 4 | 50 % |
| **Total general** | **39** | **15** | **8** | **16** | **38 %** |

> **Nota:** El porcentaje de cumplimiento considera todos los requisitos funcionales y no funcionales documentados en la especificación original. El núcleo operativo del sistema (productos, inventario, ventas básicas, IA) está funcional.

---

## 8. Recomendaciones (Priorizadas)

### Prioridad Alta (antes de puesta en producción)

1. **Implementar reintegro de stock en anulación de ventas** — La función `anularVenta()` ya existe pero no repone inventario.
2. **Implementar respaldos automáticos** — Programar copia diaria de `DBventasInventario.db` con timestamp.
3. **Migrar a BCrypt** — Reemplazar SHA-256 por BCrypt para almacenamiento de contraseñas.

### Prioridad Media (próximo ciclo)

4. **Agregar validaciones faltantes en UI** — Stock >= 0, stock mínimo >= 0, contraseña fuerte.
5. **Implementar cierre de sesión por inactividad** — Timeout de 30 minutos como medida de seguridad.
6. **Implementar reseteo de contraseña por administrador** — Función de restablecimiento sin conocer la actual.
7. **Corregir error matemático en costo producción de platillos** — Impedimento #4 pendiente.

### Prioridad Baja (futuras versiones)

8. **Agregar gráficos analíticos** — Chart, PieChart o similar en el módulo de estadísticas.
9. **Implementar página web** — Carta digital con Mobile-First (requiere nuevo proyecto).
10. **Agregar soporte para código de barras** — Integración con scanner.
11. **Agregar cálculo de IVA y descuentos** — Campos en venta y producto.

---

## 9. Conclusión

El sistema **ELUNEY (puntoVentas)** tiene su **núcleo operativo funcional**: gestión de productos, platillos con recetas, ventas con descuento de inventario, control de movimientos, auditoría y módulos de inteligencia artificial (predicción Prophet y consultas Gemini).

Sin embargo, existen **brechas significativas** respecto a la especificación original documentada:

- **Anulación de ventas** implementada parcialmente (sin reintegro de stock).
- **Respaldos automáticos** no implementados.
- **Componente web** (carta digital, contacto, redes sociales) no desarrollado.
- **Gráficos analíticos** no implementados.
- **Seguridad de contraseñas** por debajo del estándar especificado (SHA-256 vs BCrypt).
- **Múltiples validaciones** documentadas como requeridas están ausentes en la capa de UI.

El sistema **puede operar en producción** para funciones básicas de venta e inventario, pero se recomienda abordar las brechas de prioridad alta antes de su implementación definitiva.

---

*Documento generado el 19 de junio de 2026.*
