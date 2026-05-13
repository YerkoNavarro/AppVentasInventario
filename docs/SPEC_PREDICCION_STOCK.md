# 📊 ESPECIFICACIÓN TÉCNICA: PREDICCIÓN DE STOCK CON PROPHET

**Versión:** 1.0  
**Módulo:** IA - Predicción de Agotamiento de Stock  
**Fecha:** Mayo 2026

---

## 📋 TABLA DE CONTENIDOS

1. [Especificaciones del Algoritmo](#especificaciones-del-algoritmo)
2. [Formato de Datos](#formato-de-datos)
3. [Fórmulas de Cálculo](#fórmulas-de-cálculo)
4. [Parámetros de Entrada](#parámetros-de-entrada)
5. [Salida del Sistema](#salida-del-sistema)
6. [Validaciones](#validaciones)
7. [Ejemplos de Uso](#ejemplos-de-uso)

---

## 🤖 Especificaciones del Algoritmo

### Facebook Prophet

**¿Qué es Prophet?**

Prophet es una librería de Python desarrollada por Facebook para el análisis y predicción de series temporales. Es especialmente útil para datos con:
- Tendencias claras
- Patrones estacionales
- Datos faltantes o outliers

**Configuración en este Proyecto:**

```python
modelo = Prophet(
    weekly_seasonality=True,      # Detecta patrones semanales
    yearly_seasonality=False,     # No hay datos de años
    daily_seasonality=False       # El stock cambia por semana, no por hora
)
```

### Proceso Paso a Paso

```
1. Lectura de Datos
   └─ CSV con columnas: ds (fecha), y (valor), id_producto

2. Preparación
   └─ Agrupar por id_producto

3. Entrenamiento
   └─ Para cada producto:
      └─ modelo.fit(datos_históricos)

4. Predicción
   └─ futuro = make_future_dataframe(periods=7)  // Próximos 7 días
   └─ predicciones = modelo.predict(futuro)

5. Extracción de Resultados
   └─ Tomar solo los últimos 7 días predichos
   └─ Calcular promedio de demanda diaria
   └─ Asegurar valores positivos (max(0, x))

6. Salida
   └─ Imprimir: id_producto,demanda_promedio
```

---

## 📁 Formato de Datos

### Entrada: datos_stock.csv

```csv
ds,y,id_producto
2026-05-01,150,1
2026-05-01,300,2
2026-05-01,75,3
2026-05-02,145,1
2026-05-02,295,2
2026-05-02,70,3
2026-05-03,155,1
2026-05-03,305,2
2026-05-03,80,3
2026-05-04,140,1
2026-05-04,290,2
2026-05-04,65,3
2026-05-05,150,1
2026-05-05,310,2
2026-05-05,75,3
2026-05-06,148,1
2026-05-06,305,2
2026-05-06,72,3
2026-05-07,152,1
2026-05-07,315,2
2026-05-07,78,3
```

**Especificación de Columnas:**

| Columna | Tipo | Descripción | Ejemplo |
|---|---|---|---|
| `ds` | DATE (YYYY-MM-DD) | Fecha del registro | `2026-05-01` |
| `y` | INTEGER | Stock disponible ese día | `150` |
| `id_producto` | INTEGER | ID del producto | `1` |

**Requisitos:**
- Mínimo 7 días de historial por producto
- Fechas en orden ascendente
- No puede haber NULLs en columnas

---

### Salida: prediccion_stock.py

```
1,4.5
2,8.2
3,2.1
```

**Formato:**
```
id_producto,demanda_diaria_promedio
```

**Especificación:**

| Campo | Tipo | Descripción | Rango |
|---|---|---|---|
| id_producto | INTEGER | ID del producto | 1-999999 |
| demanda_diaria_promedio | FLOAT | Promedio de venta diaria (próximos 7 días) | 0.0-99999.99 |

---

## 🧮 Fórmulas de Cálculo

### 1. Demanda Diaria Promedio (Python)

```
demanda_diaria = SUM(predicciones_próximos_7_días) / 7

Donde predicciones_próximos_7_días son los valores de 'yhat' 
devueltos por Prophet para los próximos 7 días
```

**Pseudocódigo:**

```python
futuro = modelo.make_future_dataframe(periods=7)
prediccion = modelo.predict(futuro)
dias_futuros = prediccion.tail(7)  # Últimos 7 días

demanda_diaria = sum(dias_futuros['yhat'].apply(lambda x: max(0, x))) / 7
```

**Nota:** Se aplica `max(0, x)` para asegurar que no haya demandas negativas (contradicción lógica).

---

### 2. Días para Agotarse (Java)

```
diasParaAgotarse = stockActual / demandaDiaria

Donde:
- stockActual: stock disponible en el momento
- demandaDiaria: demanda promedio calculada por Prophet
```

**Código Java:**

```java
int stockActual = productoRepository.obtenerStockActual(idProducto);
int diasParaAgotarse = (int) (stockActual / (demandaDiaria == 0 ? 1 : demandaDiaria));
```

**Ejemplo:**

```
Stock actual: 150 kg
Demanda diaria: 4.5 kg/día
Días para agotarse: 150 / 4.5 = 33.33 ≈ 33 días
```

---

### 3. Cantidad Sugerida para Compra (Java)

```
cantidadSugerida = demandaDiaria * PERIODO_RESGUARDO

Donde PERIODO_RESGUARDO = 7 días
```

**Código Java:**

```java
int cantidadSugerida = (int) (demandaDiaria * 7);
```

**Interpretación:**

Se sugiere comprar stock para cubrir 7 días de demanda promedio. Esto proporciona un colchón de seguridad.

**Ejemplo:**

```
Demanda diaria: 4.5 kg/día
Cantidad sugerida: 4.5 * 7 = 31.5 ≈ 32 kg
(Esto cubre 7 días de venta promedio)
```

---

### 4. Índice de Riesgo (Java)

```
Si diasParaAgotarse <= 5:
    indiceRiesgo = 0.9  (ALTO)
Sino:
    indiceRiesgo = 0.2  (BAJO)
```

**Código Java:**

```java
double indiceRiesgo = diasParaAgotarse <= 5 ? 0.9 : 0.2;
```

**Matriz de Riesgo:**

| Días Restantes | Índice | Severidad | Acción |
|---|---|---|---|
| ≤ 5 | 0.9 | 🔴 CRÍTICO | Comprar INMEDIATAMENTE |
| 6-10 | 0.9 | 🟡 ALTO | Comprar pronto |
| 11-20 | 0.2 | 🟢 NORMAL | Monitorear |
| > 20 | 0.2 | 🟢 BAJO | Sin acción |

---

## 📥 Parámetros de Entrada

### Desde Java: EstadisticaService

```java
public List<PrediccionStock> ejecutarPrediccionStock()
```

**Parámetros internos:**

| Parámetro | Valor | Descripción |
|---|---|---|
| `comando` | `"python"` | Ejecutor del script |
| `script` | `"prediccion_stock.py"` | Archivo Python a ejecutar |
| `archivo_csv` | `"datos_stock.csv"` | Entrada de datos |
| `períodos` | `7` | Días a predecir |

---

### Desde Python: prediccion_stock.py

```python
def predecir_agotamiento():
    df = pd.read_csv("datos_stock.csv")  # ENTRADA
    
    # Parámetros de Prophet
    modelo = Prophet(
        weekly_seasonality=True,
        yearly_seasonality=False,
        daily_seasonality=False
    )
    
    # Parámetros de predicción
    futuro = modelo.make_future_dataframe(periods=7)  # Predecir 7 días
```

**Configuración de Prophet:**

| Parámetro | Valor | Razón |
|---|---|---|
| `weekly_seasonality` | `True` | Stock varía por día de la semana |
| `yearly_seasonality` | `False` | No tenemos datos de múltiples años |
| `daily_seasonality` | `False` | Stock no cambia por hora |

---

## 📤 Salida del Sistema

### Objeto PrediccionStock (Java)

```java
public class PrediccionStock {
    private int idProducto;           // ID del producto analizado
    private int diasParaAgotarse;     // Estimación en días
    private int cantidadSugerida;     // Unidades a comprar
    private double indiceRiesgo;      // 0.2 (bajo) o 0.9 (alto)
    private double nivelConfianza;    // Precisión de la predicción
    private String fechaProyectada;   // Fecha estimada de agotamiento
    private String fechaCalculo;      // Cuándo se hizo la predicción
}
```

### Lista de Predicciones

```java
List<PrediccionStock> resultados = service.ejecutarPrediccionStock();

// Ejemplo de output:
[
    PrediccionStock {
        idProducto: 1,
        diasParaAgotarse: 33,
        cantidadSugerida: 32,
        indiceRiesgo: 0.2
    },
    PrediccionStock {
        idProducto: 2,
        diasParaAgotarse: 3,
        cantidadSugerida: 57,
        indiceRiesgo: 0.9
    },
    PrediccionStock {
        idProducto: 3,
        diasParaAgotarse: 8,
        cantidadSugerida: 15,
        indiceRiesgo: 0.2
    }
]
```

---

## ✅ Validaciones

### Validación 1: Datos Suficientes

```java
public boolean prepararDatosStockParaIA() {
    int filasExportadas = estadisticasRepository.prepararDatosStockParaIA();
    
    if (filasExportadas < 7) {
        System.err.println("Datos insuficientes");
        return false;
    }
    return true;
}
```

**Regla:**
- Mínimo 7 registros (1 día de datos por producto)
- Si no hay suficientes datos, retorna lista vacía

---

### Validación 2: Demanda Válida

```python
# En prediccion_stock.py
demanda_diaria = sum(dias_futuros['yhat'].apply(lambda x: max(0, x))) / 7
# max(0, x) asegura que no haya demandas negativas
```

**Regla:**
- La demanda no puede ser negativa
- Si Prophet predice negativo → se convierte a 0

---

### Validación 3: Rango de Días

```java
// Convertir a int (puede causar truncamiento)
int diasParaAgotarse = (int) (stockActual / demandaDiaria);

// Si demanda = 0, evitar división por cero
diasParaAgotarse = (int) (stockActual / (demandaDiaria == 0 ? 1 : demandaDiaria));
```

**Regla:**
- Si demandaDiaria = 0 → diasParaAgotarse = stockActual (asumiendo compra lenta)

---

## 🎯 Ejemplos de Uso

### Ejemplo 1: Tomates (Riesgo ALTO)

**Datos Históricos (datos_stock.csv):**

```csv
ds,y,id_producto
2026-05-01,100,7
2026-05-02,95,7
2026-05-03,90,7
2026-05-04,85,7
2026-05-05,82,7
2026-05-06,78,7
2026-05-07,75,7
```

**Ejecución Python:**

```python
df_prod = df[df['id_producto'] == 7]
modelo = Prophet(...)
modelo.fit(df_prod)
# Prophet detecta tendencia descendente: baja 5 kg/día

# Predicción para próximos 7 días: ~40 kg promedio
demanda_diaria = 40 / 7 ≈ 5.7 kg/día
```

**Output Python:**

```
7,5.7
```

**Cálculo en Java:**

```java
idProducto = 7
demandaDiaria = 5.7
stockActual = 30  // Stock actual en BD

diasParaAgotarse = 30 / 5.7 = 5.26 ≈ 5 días
cantidadSugerida = 5.7 * 7 = 39.9 ≈ 40 kg
indiceRiesgo = 5 <= 5 ? 0.9 : 0.2 = 0.9  // ⚠️ ALTO
```

**Resultado:**

```json
{
    "idProducto": 7,
    "diasParaAgotarse": 5,
    "cantidadSugerida": 40,
    "indiceRiesgo": 0.9
}
```

**Interpretación para el Gerente:**

> "Los tomates se AGOTAN en 5 días. Riesgo ALTO. Necesito comprar 40 kg."

---

### Ejemplo 2: Sauce (Riesgo BAJO)

**Datos Históricos (datos_stock.csv):**

```csv
ds,y,id_producto
2026-05-01,1000,15
2026-05-02,998,15
2026-05-03,997,15
2026-05-04,995,15
2026-05-05,993,15
2026-05-06,992,15
2026-05-07,990,15
```

**Ejecución Python:**

```python
# Tendencia muy lenta: baja 1.4 kg/día
demanda_diaria = 1.4 / 7 ≈ 0.2 kg/día
```

**Output Python:**

```
15,0.2
```

**Cálculo en Java:**

```java
idProducto = 15
demandaDiaria = 0.2
stockActual = 500

diasParaAgotarse = 500 / 0.2 = 2500 días (MUCHO)
cantidadSugerida = 0.2 * 7 = 1.4 ≈ 2 kg
indiceRiesgo = 2500 <= 5 ? 0.9 : 0.2 = 0.2  // ✅ BAJO
```

**Resultado:**

```json
{
    "idProducto": 15,
    "diasParaAgotarse": 2500,
    "cantidadSugerida": 2,
    "indiceRiesgo": 0.2
}
```

**Interpretación para el Gerente:**

> "Sauce: Stock suficiente para 2500 días. Sin urgencia de compra. Riesgo BAJO."

---

## 🔍 Tabla Comparativa

| Métrica | Tomates (7) | Sauce (15) |
|---|---|---|
| Stock Actual | 30 kg | 500 kg |
| Demanda Diaria | 5.7 kg/día | 0.2 kg/día |
| Días Restantes | 5 | 2500 |
| Cantidad Sugerida | 40 kg | 2 kg |
| Índice Riesgo | 0.9 (🔴) | 0.2 (🟢) |
| Acción | Comprar AHORA | Monitorear |

---

**Última actualización:** Mayo 2026

