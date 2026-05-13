# 🤖 ARQUITECTURA: MÓDULO DE IA PARA PREDICCIÓN DE STOCK

**Versión:** 1.0  
**Fecha:** Mayo 2026  
**Autor:** Sistema ELUNEY - Punto de Ventas  
**Propósito:** Documentar la arquitectura del módulo de IA que predice el agotamiento de stock

---

## 📋 TABLA DE CONTENIDOS

1. [Descripción General](#descripción-general)
2. [Componentes del Sistema](#componentes-del-sistema)
3. [Flujo de Datos](#flujo-de-datos)
4. [Tecnologías Utilizadas](#tecnologías-utilizadas)
5. [Configuración y Requisitos](#configuración-y-requisitos)
6. [Integración Java-Python](#integración-java-python)

---

## 🎯 Descripción General

El módulo de IA tiene dos funcionalidades principales:

| Funcionalidad | Objetivo | Entrada | Salida |
|---|---|---|---|
| **Prophet Ventas** | Predecir ingresos totales | `datos_ventas.csv` | Predicción de ventas 7 días |
| **Prophet Stock** | Predecir agotamiento | `datos_stock.csv` | Días restantes + Sugerencia compra |

---

## 🏗️ Componentes del Sistema

### 1. **Capa de Datos (Java)**

#### EstadisticasRepositoryImpl
```
Responsabilidad: Extraer datos de la BD y exportarlos a CSV

Métodos clave:
├─ prepararDatosParaIA()
│  └─ Exporta: id_producto, cantidad, fecha → datos_ventas.csv
├─ prepararDatosStockParaIA()
│  └─ Exporta: id_producto, stock, fecha → datos_stock.csv
└─ obtenerRankingProductos(limite)
   └─ Retorna: TOP N productos más vendidos
```

#### ProductoRepositoryImpl
```
Responsabilidad: Consultar stock y detalles de productos

Métodos clave:
├─ obtenerStockActual(idProducto)
│  └─ Retorna: Stock disponible en este momento
└─ obtenerProductos()
   └─ Lista todos los productos activos
```

---

### 2. **Capa de Negocio (Java)**

#### EstadisticaService
```
Responsabilidad: Orquestar la predicción

Métodos públicos:
├─ prepararDatosParaIA()
│  └─ Valida: Mínimo 7 días de datos
├─ prepararDatosStockParaIA()
│  └─ Valida: Mínimo 7 días de historial
├─ ejecutarPrediccionProphet()
│  └─ Llama: modelo_predictivo.py
└─ ejecutarPrediccionStock()
   └─ Llama: prediccion_stock.py
   └─ Retorna: List<PrediccionStock> con cálculos
```

---

### 3. **Capa de IA (Python)**

#### modelo_predictivo.py
```python
Algoritmo: Facebook Prophet
Entrada: datos_ventas.csv
Salida: fecha,monto_predicho (para 7 días)

Proceso:
1. Lee CSV con histórico de ventas
2. Aplica Prophet con seasonalidad semanal
3. Predice 7 días futuros
4. Imprime resultados línea por línea
```

#### prediccion_stock.py
```python
Algoritmo: Facebook Prophet + Análisis de Demanda
Entrada: datos_stock.csv
Salida: id_producto,demanda_diaria_promedio

Proceso:
1. Lee CSV con histórico de stock por producto
2. Por cada producto:
   - Aplica Prophet
   - Calcula demanda promedio de próximos 7 días
   - Evita números negativos
3. Imprime resultados línea por línea
```

---

### 4. **Modelo de Datos**

#### PrediccionStock (Java Object)
```java
├─ idPrediccion: int
├─ idProducto: int
├─ diasParaAgotarse: int         // Stock ÷ Demanda Diaria
├─ cantidadSugerida: int         // Demanda × 7 días
├─ indiceRiesgo: double          // 0.2 (bajo) o 0.9 (alto)
├─ nivelConfianza: double
├─ fechaProyectada: String
└─ fechaCalculo: String
```

---

## 📊 Flujo de Datos

```
┌─────────────────────────────────────────────────────────────────────┐
│                    INICIO: ejecutarPrediccionStock()                 │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  1. Validar Datos (prepararDatosStockParaIA)                        │
│     ├─ Consulta: SELECT id, stock, fecha FROM producto              │
│     ├─ Valida: Mínimo 7 días de historial                           │
│     └─ Exporta: datos_stock.csv                                     │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
        ┌─────────────────────────────────────────┐
        │  Formato de datos_stock.csv:            │
        │  ┌──────────────────────────────────┐   │
        │  │ ds,y,id_producto                 │   │
        │  │ 2026-05-01,150,1                 │   │
        │  │ 2026-05-01,300,2                 │   │
        │  │ 2026-05-02,140,1                 │   │
        │  │ ...                              │   │
        │  └──────────────────────────────────┘   │
        └─────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  2. Ejecutar Script Python (ProcessBuilder)                         │
│     └─ Comando: python prediccion_stock.py                          │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
        ┌─────────────────────────────────────────┐
        │  Salida de prediccion_stock.py:         │
        │  ┌──────────────────────────────────┐   │
        │  │ 1,4.5                            │   │
        │  │ 2,8.2                            │   │
        │  │ 3,2.1                            │   │
        │  │ ...                              │   │
        │  └──────────────────────────────────┘   │
        │  (id_producto,demanda_diaria)           │
        └─────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  3. Parsear Resultados en Java                                      │
│     ├─ Divide cada línea por coma                                   │
│     ├─ idProducto = parte[0]                                        │
│     └─ demandaDiaria = parte[1]                                     │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  4. Calcular Métricas por Producto                                  │
│     ├─ stockActual = obtenerStockActual(idProducto)                │
│     ├─ diasParaAgotarse = stockActual / demandaDiaria              │
│     ├─ cantidadSugerida = demandaDiaria * 7                        │
│     └─ indiceRiesgo = diasParaAgotarse <= 5 ? 0.9 : 0.2           │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  5. Crear Objeto PrediccionStock                                    │
│     ├─ setIdProducto(idProducto)                                    │
│     ├─ setDiasParaAgotarse(diasParaAgotarse)                       │
│     ├─ setCantidadSugerida(cantidadSugerida)                       │
│     └─ setIndiceRiesgo(indiceRiesgo)                               │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  6. Agregar a Lista y Retornar                                      │
│     └─ Retorna: List<PrediccionStock>                              │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│  FIN: Datos listos para mostrar en UI o persistir                   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología | Versión | Propósito |
|---|---|---|---|
| **Base de Datos** | SQLite | 3.43.2.0 | Persistencia de datos |
| **Exportación** | JDBC | - | Conexión DB → Java |
| **IA - Predicción** | Facebook Prophet | 1.1+ | Análisis de series temporales |
| **Análisis de Datos** | Pandas | 1.3+ | Manipulación de DataFrames |
| **Comunicación** | ProcessBuilder | Java NIO | Ejecución de procesos Python |

---

## ⚙️ Configuración y Requisitos

### Requisitos del Sistema

```bash
# Python 3.8+
python --version

# Librerías Python necesarias
pip install fbprophet
pip install pandas
pip install numpy
```

### Rutas Críticas (en código)

```java
// En EstadisticaService
ProcessBuilder pb = new ProcessBuilder(
    "python", 
    "src/main/java/com/sistema/puntoventas/prediccion_stock.py"
);
```

**IMPORTANTE:** La ruta debe ser relativa al directorio donde se ejecuta Java.

### Archivos Generados

```
puntoVentas/
├─ datos_stock.csv        (Generado por EstadisticasRepository)
├─ datos_ventas.csv       (Generado por EstadisticasRepository)
└─ src/main/java/com/sistema/puntoventas/
   ├─ prediccion_stock.py
   └─ modelo_predictivo.py
```

---

## 🔗 Integración Java-Python

### Flujo de Comunicación

```
Java (EstadisticaService)
    ↓
    ├─→ Genera: datos_stock.csv
    ├─→ Crea ProcessBuilder
    ├─→ Ejecuta: python prediccion_stock.py
    ↓
Python (prediccion_stock.py)
    ├─→ Lee: datos_stock.csv
    ├─→ Procesa con Prophet
    ├─→ Escribe en STDOUT: "id_producto,demanda"
    ↓
Java (EstadisticaService)
    ├─→ Lee BufferedReader
    ├─→ Parsea cada línea
    ├─→ Calcula métricas
    ├─→ Crea PrediccionStock
    ↓
UI / Controlador
    └─→ Recibe List<PrediccionStock>
```

### Manejo de Errores

```java
try {
    ProcessBuilder pb = new ProcessBuilder("python", "...");
    pb.redirectErrorStream(true);  // Capturar stderr y stdout
    Process p = pb.start();
    
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(p.getInputStream())
    );
    
    String linea;
    while ((linea = reader.readLine()) != null) {
        if(linea.contains("ERROR")) continue;  // Ignorar líneas de error
        // Procesar línea
    }
    
    int exitCode = p.waitFor();
    if(exitCode != 0) {
        System.err.println("Fallo: código " + exitCode);
    }
} catch (Exception e) {
    System.err.println("Error: " + e.getMessage());
}
```

---

## 📈 Interpretación de Resultados

### PrediccionStock Output

```json
{
  "idProducto": 5,
  "diasParaAgotarse": 3,
  "cantidadSugerida": 42,
  "indiceRiesgo": 0.9
}
```

**Interpretación:**
- ⚠️ **Producto ID 5** se agotará en **3 días**
- 📦 Se sugiere comprar **42 unidades**
- 🔴 Riesgo **ALTO** (0.9)

### Tabla de Riesgos

| Días Restantes | Índice Riesgo | Recomendación |
|---|---|---|
| ≤ 5 días | 0.9 | 🔴 URGENTE: Comprar ahora |
| > 5 días | 0.2 | 🟢 Normal: Monitorear |

---

## 🎓 Caso de Uso

### Escenario: Predicción de Stock para Tomates

```
Entrada:
├─ Stock actual: 150 kg
├─ Histórico 7 días: [45, 50, 48, 52, 49, 51, 50] kg/día
└─ Promedio: 49.3 kg/día

Proceso:
├─ Prophet predice: 49.5 kg/día los próximos 7 días
├─ Días restantes: 150 / 49.5 = 3.03 ≈ 3 días
├─ Sugerencia: 49.5 * 7 = 346.5 ≈ 347 kg
└─ Riesgo: 3 ≤ 5 → 0.9 (ALTO)

Salida:
{
  "idProducto": 7,
  "diasParaAgotarse": 3,
  "cantidadSugerida": 347,
  "indiceRiesgo": 0.9
}

Acción del Gerente:
"Los tomates se agotan en 3 días. Necesito comprar 347 kg."
```

---

## 📚 Recursos Adicionales

- [Documentación Prophet](https://facebook.github.io/prophet/docs/quick_start.html)
- [Pandas Documentation](https://pandas.pydata.org/docs/)
- [ProcessBuilder Java](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)

---

**Última actualización:** Mayo 2026

