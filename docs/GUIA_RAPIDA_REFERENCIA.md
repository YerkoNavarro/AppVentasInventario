# 📚 GUÍA RÁPIDA DE REFERENCIA: IA PREDICCIÓN STOCK

**Para desarrolladores:** Referencia rápida de conceptos, clases y métodos.

---

## 🎯 ¿QUÉ HACE ESTE MÓDULO?

Predice **cuántos días faltan para que se agote el stock** de cada producto usando:
- **Datos históricos** de las últimas ventas (mínimo 7 días)
- **Algoritmo Prophet** (Inteligencia Artificial de Facebook)
- **Cálculos automáticos** de demanda promedio

**Resultado:** Una lista de advertencias sobre qué productos comprar y cuándo.

---

## 🏗️ ARQUITECTURA EN 4 CAPAS

```
┌─────────────────────────────────────┐
│  1. UI (Panel / Tabla)              │ ← Ver resultados
├─────────────────────────────────────┤
│  2. SERVICE (EstadisticaService)    │ ← Orquesta predicción
├─────────────────────────────────────┤
│  3. REPOSITORY (Java)               │ ← Lee/escribe datos
├─────────────────────────────────────┤
│  4. PYTHON (Prophet)                │ ← Cálculos IA
└─────────────────────────────────────┘
```

---

## 📌 CLASES PRINCIPALES

### EstadisticaService.java

```java
public List<PrediccionStock> ejecutarPrediccionStock()
```

**Qué hace:**
1. Valida que hay 7+ días de datos
2. Exporta datos a CSV
3. Ejecuta script Python
4. Parsea resultados
5. Calcula métricas
6. Retorna lista de predicciones

**Cómo usarlo:**

```java
EstadisticaService service = new EstadisticaService(
    repositoryEstadistica,
    repositoryProducto
);

List<PrediccionStock> resultados = service.ejecutarPrediccionStock();

for (PrediccionStock p : resultados) {
    System.out.println("Producto " + p.getIdProducto() + 
                      ": Agotamiento en " + p.getDiasParaAgotarse() + " días");
}
```

---

### PrediccionStock.java

```java
public class PrediccionStock {
    private int idProducto;           // ¿Cuál producto?
    private int diasParaAgotarse;     // ¿Cuántos días?
    private int cantidadSugerida;     // ¿Cuánto comprar?
    private double indiceRiesgo;      // ¿Qué tan urgente? (0.2 o 0.9)
}
```

**Interpretación:**

| Campo | Significado | Ejemplo |
|---|---|---|
| `idProducto` | ID del producto analizado | `7` (tomates) |
| `diasParaAgotarse` | Días hasta que se acabe el stock | `3` → URGENTE |
| `cantidadSugerida` | Unidades a comprar (para 7 días) | `40 kg` |
| `indiceRiesgo` | 0.9 = ALTO (compra ya), 0.2 = BAJO (ok) | `0.9` → ROJO |

---

## 🐍 SCRIPTS PYTHON

### prediccion_stock.py

**Entrada:** `datos_stock.csv`

```csv
ds,y,id_producto
2026-05-01,150,1
2026-05-02,145,1
...
```

**Proceso:**
1. Lee CSV agrupando por id_producto
2. Aplica Prophet a cada grupo
3. Predice 7 días futuros
4. Calcula promedio de demanda

**Salida:**

```
1,4.5
2,8.2
3,2.1
```

**Formato:** `id_producto,demanda_diaria_promedio`

---

### modelo_predictivo.py

Igual que anterior pero para **predicción de ingresos (ventas)**, no stock.

---

## 🧮 CÁLCULOS CLAVE

### 1. Demanda Diaria Promedio

```
Prophet predice: [50, 48, 52, 49, 51, 50, 52] para próximos 7 días
Demanda = (50+48+52+49+51+50+52) / 7 = 50.29 kg/día
```

**En Python:** `sum(predicciones) / 7`

---

### 2. Días para Agotarse

```
Stock actual = 150 kg
Demanda = 50.29 kg/día

Días = 150 / 50.29 = 2.98 ≈ 3 días
```

**Código Java:**

```java
int diasParaAgotarse = (int) (stockActual / demandaDiaria);
```

---

### 3. Cantidad Sugerida

```
Demanda = 50.29 kg/día
Sugerir para = 7 días

Cantidad = 50.29 * 7 = 352 kg
```

**Código Java:**

```java
int cantidadSugerida = (int) (demandaDiaria * 7);
```

---

### 4. Índice de Riesgo

```
SI días <= 5:
    riesgo = 0.9  (🔴 ROJO - URGENTE)
SI NO:
    riesgo = 0.2  (🟢 VERDE - OK)
```

**Código Java:**

```java
double indiceRiesgo = diasParaAgotarse <= 5 ? 0.9 : 0.2;
```

---

## 🚀 FLUJO COMPLETO

```
┌─ Usuario hace clic en botón "Predicción"
│
├─ EstadisticaService.ejecutarPrediccionStock()
│  ├─ prepararDatosStockParaIA()
│  │  └─ Exporta: BD → datos_stock.csv
│  │
│  ├─ Ejecutar: python prediccion_stock.py
│  │  └─ Calcula demanda promedio por producto
│  │
│  ├─ Parsear output
│  │  ├─ Lee: "1,4.5" → idProducto=1, demanda=4.5
│  │  ├─ obtenerStockActual(1) → 150 kg
│  │  ├─ Calcula: dias = 150/4.5 = 33
│  │  └─ Crea: PrediccionStock(id=1, dias=33, qty=32, risk=0.2)
│  │
│  └─ return List<PrediccionStock>
│
└─ UI muestra tabla con resultados
```

---

## 📊 TABLA DE REFERENCIA RÁPIDA

| Métrica | Fórmula | Interpretación |
|---|---|---|
| **Días Restantes** | stock ÷ demanda | 3 días = comprar hoy |
| **Qty Sugerida** | demanda × 7 | Cobertura 1 semana |
| **Riesgo** | si días ≤ 5 → 0.9 | Color rojo o verde |
| **Demanda Promedio** | sum(7_días) ÷ 7 | Consumo por día |

---

## ⚠️ VALIDACIONES IMPORTANTES

### Mínimo 7 días de datos

```java
if (filasExportadas < 7) {
    System.err.println("Datos insuficientes");
    return new ArrayList<>();  // ← Retorna lista vacía
}
```

### Demanda no puede ser negativa

```python
demanda_diaria = sum(dias_futuros['yhat'].apply(lambda x: max(0, x))) / 7
# max(0, x) asegura valores >= 0
```

### Division by zero

```java
int dias = (int) (stock / (demanda == 0 ? 1 : demanda));
// Si demanda = 0, evitar división por cero
```

---

## 🔧 DEBUGGING (Solución de Problemas)

### "No hay predicciones"

```powershell
# 1. Verificar datos en BD
sqlite3 DBventasInventario.db "SELECT COUNT(*) FROM producto"

# 2. Verificar CSV generado
cat datos_stock.csv | head -20

# 3. Ejecutar Python manualmente
python src/main/java/com/sistema/puntoventas/prediccion_stock.py
```

### "Error: No module named prophet"

```powershell
pip install fbprophet
```

### "Ruta del script incorrecta"

En `EstadisticaService.java`, verificar:

```java
ProcessBuilder pb = new ProcessBuilder(
    "python",
    "src/main/java/com/sistema/puntoventas/prediccion_stock.py"
    //↑ Ruta relativa desde carpeta raíz del proyecto
);
```

---

## 📋 CASOS DE USO

### Caso 1: Tomate (ALTO RIESGO)

```
Stock: 30 kg
Demanda: 10 kg/día
Días: 30 ÷ 10 = 3 días ← ⚠️ URGENTE
Riesgo: 0.9 (ALTO)
Acción: "Comprar 70 kg HOY"
```

### Caso 2: Aceite (BAJO RIESGO)

```
Stock: 500 L
Demanda: 2 L/día
Días: 500 ÷ 2 = 250 días ← ✓ OK
Riesgo: 0.2 (BAJO)
Acción: "Sin prisa"
```

---

## 🎓 PARA ENTENDER PROPHET

**¿Qué es Prophet?**

Algoritmo de Facebook que analiza patrones históricos y predice el futuro.

**¿Cómo funciona?**

1. **Analiza** datos históricos
2. **Detecta** tendencias (sube/baja) y ciclos
3. **Predice** valores futuros
4. **Retorna** predicciones con intervalos de confianza

**¿Por qué lo usamos?**

- Maneja datos con "saltos" (fines de semana, feriados)
- Detecta patrones estacionales
- Más preciso que promedios simples

---

## 🔗 REFERENCIAS RÁPIDAS

| Concepto | Archivo | Línea |
|---|---|---|
| Ejecutar predicción | `EstadisticaService.java` | 105 |
| Crear PrediccionStock | `EstadisticaService.java` | 135 |
| Parsear output Python | `EstadisticaService.java` | 125 |
| Cálculo índice riesgo | `EstadisticaService.java` | 140 |
| Script Python | `prediccion_stock.py` | 25 |

---

## ✅ CHECKLIST RÁPIDO

Antes de ejecutar:

```
□ BD tiene >= 7 días de ventas
□ Python 3.8+ instalado
□ pip install fbprophet
□ Archivo prediccion_stock.py existe
□ Ruta en ProcessBuilder es correcta
```

Al ejecutar:

```
□ Ve CSV generado: datos_stock.csv
□ Ve output Python en consola
□ Ve PrediccionStock objects creados
□ Ve resultados en UI
```

---

**¿Necesitas ayuda?** Consulta:
- **Arquitectura:** ARQUITECTURA_IA_PREDICCION.md
- **Especificaciones:** SPEC_PREDICCION_STOCK.md
- **Setup:** MANUAL_SETUP.md

---

**Última actualización:** Mayo 2026

