# 📐 DIAGRAMAS UML PARA PRESENTACIÓN

## 1️⃣ DIAGRAMA DE FLUJO PRINCIPAL (ASCII)

```
┌─────────────────────────────────────────────────────────────────────┐
│                   INICIO: PREDICCIÓN DE STOCK                        │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
                    ┌───────────────────────┐
                    │  Validar Datos (7+)   │
                    └───────────┬───────────┘
                                ↓
                    ┌───────────────────────┐
                    │  ERROR: ¿Datos OK?    │
                    └─┬─────────────────┬───┘
                  NO │                 │ YES
                     ↓                 ↓
            ┌──────────────┐  ┌──────────────────┐
            │ Return []    │  │ Exportar CSV     │
            │ (list vacía) │  │ (datos_stock)    │
            └──────────────┘  └────────┬─────────┘
                                       ↓
                    ┌─────────────────────────────┐
                    │ Ejecutar Python             │
                    │ $ python prediccion_stock.py│
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Prophet: Procesa datos      │
                    │ - Agrupa por producto       │
                    │ - Predice 7 días            │
                    │ - Calcula demanda promedio  │
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Output Python:              │
                    │ id,demanda                  │
                    │ 1,4.5                       │
                    │ 2,8.2                       │
                    │ 3,2.1                       │
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Java: Parsear Líneas        │
                    │ - Split por coma            │
                    │ - Obtener stock actual      │
                    │ - Calcular métricas         │
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Crear PrediccionStock       │
                    │ - diasParaAgotarse          │
                    │ - cantidadSugerida          │
                    │ - indiceRiesgo              │
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Agregar a List<>            │
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ Return List<PrediccionStock>│
                    └────────────┬────────────────┘
                                 ↓
                    ┌─────────────────────────────┐
                    │ UI: Mostrar Tabla           │
                    │ (Resultados al usuario)     │
                    └─────────────────────────────┘
                                 ↓
┌─────────────────────────────────────────────────────────────────────┐
│                        FIN: ÉXITO ✓                                  │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2️⃣ DIAGRAMA DE CLASES

```
┌──────────────────────────────────────────────────────────────┐
│                  <<interface>>                               │
│              IEstadisticasRepository                         │
├──────────────────────────────────────────────────────────────┤
│ + prepararDatosParaIA()                                      │
│ + prepararDatosStockParaIA()                                 │
│ + obtenerRankingProductos(int)                               │
└──────────────────────────────────────────────────────────────┘
                          ▲
                          │ implements
                          │
┌──────────────────────────────────────────────────────────────┐
│          EstadisticasRepositoryImpl                           │
├──────────────────────────────────────────────────────────────┤
│ - conexion: Conexion                                         │
├──────────────────────────────────────────────────────────────┤
│ + prepararDatosParaIA(): int                                 │
│   └─ Exporta datos a datos_ventas.csv                        │
│ + prepararDatosStockParaIA(): int                            │
│   └─ Exporta datos a datos_stock.csv                         │
│ + obtenerRankingProductos(limite): List<RankingDTO>         │
└──────────────────────────────────────────────────────────────┘


┌──────────────────────────────────────────────────────────────┐
│          EstadisticaService                                  │
├──────────────────────────────────────────────────────────────┤
│ - estadisticasRepository: IEstadisticasRepository            │
│ - productoRepository: IProductoRepository                    │
├──────────────────────────────────────────────────────────────┤
│ + ejecutarPrediccionStock(): List<PrediccionStock>           │
│   ├─ Valida datos (mín 7 días)                              │
│   ├─ Exporta CSV                                            │
│   ├─ Ejecuta Python                                         │
│   ├─ Parsea resultados                                      │
│   ├─ Calcula métricas                                       │
│   └─ Retorna list<PrediccionStock>                          │
│                                                              │
│ + ejecutarPrediccionProphet(): void                          │
│ + prepararDatosParaIA(): boolean                             │
│ + prepararDatosStockParaIA(): boolean                        │
└──────────────────────────────────────────────────────────────┘


┌──────────────────────────────────────────────────────────────┐
│                PrediccionStock                               │
├──────────────────────────────────────────────────────────────┤
│ - idPrediccion: int                                          │
│ - idProducto: int                                            │
│ - diasParaAgotarse: int        ◄─ Stock / Demanda            │
│ - cantidadSugerida: int        ◄─ Demanda * 7               │
│ - indiceRiesgo: double         ◄─ 0.9 o 0.2                │
│ - nivelConfianza: double                                     │
│ - fechaProyectada: String                                    │
│ - fechaCalculo: String                                       │
├──────────────────────────────────────────────────────────────┤
│ + getters/setters...                                         │
└──────────────────────────────────────────────────────────────┘
```

---

## 3️⃣ DIAGRAMA DE SECUENCIA (SIMPLE)

```
Usuario           UI           Service         Repository     Python
  │               │               │                │             │
  │─ Clic ────────>│               │                │             │
  │               │               │                │             │
  │               │─ ejecutar()──>│                │             │
  │               │               │                │             │
  │               │               │─ validar()────>│             │
  │               │               │<─ OK (15 filas)│             │
  │               │               │                │             │
  │               │               │─ export CSV───>│             │
  │               │               │                │             │
  │               │               │─ start Python──────────────>│
  │               │               │                            │
  │               │               │              Python procesando
  │               │               │              (5-10 segundos)
  │               │               │                            │
  │               │               │<─ output: 1,4.5 ◄─────────│
  │               │               │          2,8.2              │
  │               │               │          3,2.1              │
  │               │               │                            │
  │               │               │─ parsear()─────────────────>
  │               │               │ calcular PrediccionStock    │
  │               │               │                            │
  │               │<─ List<Pred>──│                            │
  │               │                                            │
  │<─ Tabla ──────│                                            │
  │               │                                            │
```

---

## 4️⃣ MAPA MENTAL: CONCEPTOS CLAVE

```
                           PREDICCIÓN STOCK
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
              ¿QUÉ CALCULA?            ¿CÓMO FUNCIONA?
                    │                           │
        ┌───────────┼───────────┐      ┌───────┼────────┐
        │           │           │      │       │        │
      Días      Cantidad    Riesgo    BD → CSV → Python
      hasta   sugerida    (0.2/0.9)   ↓   ↓     ↓
     agotarse  comprar               Prophet Analysis
                                      ↓         ↓
                                    Demanda   Predicción
                                    promedio  7 días
                                       ↓
                                    Java
                                    Cálculos


              ¿CUÁNDO ACTUAR?
                    │
        ┌───────────┼────────────┐
        │           │            │
      Riesgo      Riesgo      Riesgo
      0.2 BAJO    0.5 MED     0.9 ALTO
        │           │            │
      >10 días    6-10 días    ≤5 días
        │           │            │
      ✓ OK       ⚠ Pronto     🔴 AHORA
      Monitor   Planificar    Comprar!
```

---

## 5️⃣ TABLA: CÁLCULOS PASO A PASO

```
┌────────────────────────────────────────────────────────────────────┐
│ EJEMPLO REAL: PRODUCTO "TOMATES" (ID=7)                            │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│ PASO 1: DATOS HISTÓRICOS                                          │
│ ┌──────────────────────────────────────────┐                     │
│ │ Día 1: 100 kg  │  Día 5: 82 kg          │                     │
│ │ Día 2: 95 kg   │  Día 6: 78 kg          │                     │
│ │ Día 3: 90 kg   │  Día 7: 75 kg          │                     │
│ │ Día 4: 85 kg   │  TENDENCIA: Baja 5 kg/día │                 │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 2: PROPHET PREDICE PRÓXIMOS 7 DÍAS                          │
│ ┌──────────────────────────────────────────┐                     │
│ │ Día 8: 70 kg   │  Día 12: 50 kg         │                     │
│ │ Día 9: 65 kg   │  Día 13: 45 kg         │                     │
│ │ Día 10: 60 kg  │  Día 14: 40 kg         │                     │
│ │ Día 11: 55 kg  │                        │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 3: CALCULAR DEMANDA PROMEDIO                                │
│ ┌──────────────────────────────────────────┐                     │
│ │ Sum = 70+65+60+55+50+45+40 = 385 kg      │                     │
│ │ Promedio = 385 / 7 = 55 kg/día          │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 4: JAVA LEE DEMANDA                                         │
│ ┌──────────────────────────────────────────┐                     │
│ │ Entrada de Python: "7,55"                │                     │
│ │ idProducto = 7                           │                     │
│ │ demandaDiaria = 55 kg/día                │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 5: CONSULTAR STOCK ACTUAL                                   │
│ ┌──────────────────────────────────────────┐                     │
│ │ SELECT stock FROM producto WHERE id = 7  │                     │
│ │ → Resultado: 150 kg                      │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 6: CALCULAR MÉTRICAS                                        │
│ ┌──────────────────────────────────────────┐                     │
│ │ diasParaAgotarse = 150 / 55 = 2.7 ≈ 3   │ ← ⚠️ CRÍTICO       │
│ │ cantidadSugerida = 55 * 7 = 385 kg      │                     │
│ │ indiceRiesgo = 3 <= 5 ? 0.9 : 0.2 = 0.9  │ ← 🔴 ROJO           │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ PASO 7: CREAR OBJETO                                             │
│ ┌──────────────────────────────────────────┐                     │
│ │ PrediccionStock {                        │                     │
│ │   idProducto: 7                          │                     │
│ │   diasParaAgotarse: 3                    │                     │
│ │   cantidadSugerida: 385                  │                     │
│ │   indiceRiesgo: 0.9                      │                     │
│ │ }                                        │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
│ RESULTADO FINAL:                                                  │
│ ┌──────────────────────────────────────────┐                     │
│ │ 🔴 TOMATES: SE AGOTAN EN 3 DÍAS          │                     │
│ │ Comprar 385 kg INMEDIATAMENTE            │                     │
│ └──────────────────────────────────────────┘                     │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

---

## 6️⃣ DIAGRAMA: CICLO DE VIDA DEL PRODUCTO

```
STOCK NORMAL          STOCK BAJO           STOCK CRÍTICO        AGOTADO
(> 10 días)           (6-10 días)          (≤ 5 días)           (0 días)
     │                      │                    │                  │
     │◄─── Compra ──────────┴────────────────────┴──────────────────┤
     │     Reabastecimiento                                         │
     │                                                               │
     ├─────► Consumo Diario ──────► Consumo Diario ──────► Sin Stock
     │          5 kg/día               5 kg/día
     │
  Status:                 Status:            Status:             Status:
  ✓ Verde                 ⚠ Amarillo         🔴 Rojo             ❌ Negro
  ✓ Monitorear           ⚠ Planificar       🔴 Comprar AHORA    ❌ EMERGENCIA

Acción:                  Acción:            Acción:             Acción:
Monitor normal           Agenda compra      COMPRAR YA           Producción parada
Sin prisa                Próxima semana     Esta semana          ¡CRÍTICO!
```

---

## 7️⃣ TABLA: COMPARATIVA DE PRODUCTOS

```
┌───────┬──────────┬──────────┬──────────┬────────┬──────────┬───────────┐
│ ID    │ Producto │ Stock    │ Demanda  │ Días   │ Cantidad │ Riesgo    │
│       │ Nombre   │ Actual   │ Diaria   │ Falta  │ Sugerida │ (0.2/0.9) │
├───────┼──────────┼──────────┼──────────┼────────┼──────────┼───────────┤
│ 1     │ Tomate   │ 30 kg    │ 10 kg/d  │ 3      │ 70 kg    │ 🔴 0.9    │
│ 2     │ Cebolla  │ 100 kg   │ 5 kg/d   │ 20     │ 35 kg    │ 🟢 0.2    │
│ 3     │ Ajo      │ 15 kg    │ 2 kg/d   │ 7      │ 14 kg    │ 🟢 0.2    │
│ 4     │ Cilantro │ 5 kg     │ 0.5 kg/d │ 10     │ 3.5 kg   │ 🟢 0.2    │
│ 5     │ Pimienta │ 8 kg     │ 1.5 kg/d │ 5      │ 10.5 kg  │ 🔴 0.9    │
├───────┼──────────┼──────────┼──────────┼────────┼──────────┼───────────┤
│ ACCIÓN RECOMENDADA:                                                    │
│ · Producto 1: COMPRAR YA (3 días, riesgo alto)                         │
│ · Producto 5: COMPRAR YA (5 días, riesgo alto)                         │
│ · Producto 3: Monitor (7 días, ok por ahora)                           │
│ · Productos 2,4: Sin prisa (stock ok)                                  │
└───────┴──────────┴──────────┴──────────┴────────┴──────────┴───────────┘
```

---

## 📌 NOTAS IMPORTANTES

```
1. RUTA CRÍTICA:
   · Asegurar que hay 7+ días de historial
   · Ruta del script Python debe ser RELATIVA
   · BD y CSV deben estar en carpeta raíz

2. MANEJO DE ERRORES:
   · Si demanda = 0 → usar 1 para no dividir por cero
   · Si Prophet falla → capturar exception
   · Si Python no existe → mensaje claro al usuario

3. OPTIMIZACIONES:
   · Cachear resultado por 24 horas
   · Ejecutar predicción en thread separado (no bloquea UI)
   · Notificar solo productos con riesgo ALTO
```

---

**Última actualización:** Mayo 2026

Para visualizar los diagramas PlantUML:
- Copiar contenido de .puml
- Ir a: https://www.plantuml.com/plantuml/uml/
- Pegar código
- Ver diagrama rendered

