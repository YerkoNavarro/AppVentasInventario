# IA Predictiva de Stock

## Una explicación simple de qué es, cómo funciona y cómo se relaciona con cada capa del sistema

---

## PARTE 1: PARA CUALQUIERA

### ¿Qué es la IA predictiva de stock?

Imaginate que tenés un ayudante que se pasa el día mirando el historial de tu negocio. Revisa cuánto vendiste cada día, cada producto, y empieza a notar patrones. Con el tiempo, ese ayudante aprende a decirte:

- "Che, los tomates se te van a acabar en 3 días"
- "Conviene que compres 385 kg la semana que viene"
- "Este producto está tranquilo, tenés para 20 días"

Eso es la IA predictiva de stock. No es magia, es una herramienta que mira el pasado para anticipar el futuro.

### ¿Qué problema resuelve?

Sin esta IA, el dueño de un negocio tiene que estar revisando stock mentalmente o con planillas. Muchas veces se da cuenta de que falta un producto cuando ya se terminó. Para ese momento, ya perdió ventas.

Con la IA predictiva:

- No necesitás revisar producto por producto
- Te avisa antes de que falte, no después
- Te dice cuánto comprar, no solo "está bajo"
- Podés planificar compras con días de anticipación

### Ejemplo cotidiano

Pensá en un kiosco que vende gaseosas. El dueño sabe más o menos cada cuánto se venden, pero si llegan muchos clientes seguido, puede desabastecerse sin darse cuenta.

La IA mira el historial de los últimos 7 días, ve que se venden 20 gaseosas por día en promedio, y mira que quedan 40. Entonces calcula:

- Con 40 gaseosas y un ritmo de 20 por día, alcanza para 2 días
- Si el proveedor tarda 3 días en traer más, ya es tarde
- Recomienda comprar YA mismo

El dueño no tuvo que hacer ninguna cuenta. La IA le avisó sola.

---

## PARTE 2: EL VIAJE A TRAVÉS DE LAS CAPAS

El sistema está organizado en capas. Cada capa tiene una responsabilidad clara, como en una empresa:

| Capa | Rol | ¿Qué hace? |
|------|-----|------------|
| **Presentación** | La vidriera | Muestra la pantalla y los resultados al usuario |
| **Negocio** | El coordinador | Recibe órdenes, valida datos, coordina el trabajo |
| **Datos** | El archivista | Busca información en la base de datos |
| **IA** | El analista externo | Hace los cálculos predictivos |

### Capa de Presentación (JavaFX)

Es lo que ve el usuario. Botones, tablas, colores. Cuando el usuario hace clic en "Predecir stock", el controlador (DashBoardController o similar) le avisa a la capa de negocio.

No hace cálculos. Solo muestra lo que le pasan.

### Capa de Negocio (EstadisticaService)

Es el cerebro de la operación. Cuando recibe la orden de predecir:

1. Le pide a la capa de datos que prepare el historial
2. Verifica que haya suficiente información (mínimo 7 días)
3. Ejecuta el script de IA (Python)
4. Lee los resultados que devuelve Python
5. Calcula métricas finales: días hasta agotarse, cantidad sugerida, nivel de riesgo
6. Devuelve todo listo a la capa de presentación

Es como un coordinador de proyectos: no hace el trabajo pesado, pero sabe quién debe hacer cada cosa y en qué orden.

### Capa de Datos (EstadisticasRepository)

Esta capa habla con la base de datos SQLite. Su trabajo es:

1. Hacer consultas SQL (`SELECT stock, fecha FROM producto`)
2. Validar que haya datos
3. Exportar todo a un archivo CSV (`datos_stock.csv`)

El CSV es como un "idioma puente": lo entiende Java y lo entiende Python. Es el sobre que pasa información de una capa a otra.

### Capa de IA (Python + Prophet)

Esta capa vive afuera del sistema principal. Es un script de Python que usa una librería llamada **Prophet** (creada por Facebook).

Su trabajo:

1. Lee el CSV que generó Java
2. Agrupa los datos por producto
3. Para cada producto, analiza la tendencia de los últimos días
4. Predice cómo se va a comportar la demanda en los próximos 7 días
5. Devuelve el resultado: "producto X va a tener una demanda promedio de Y por día"

Prophet es una herramienta estadística que entiende patrones. Si un producto se vende más los fines de semana, Prophet lo nota y lo tiene en cuenta.

---

## PARTE 3: RECORRIDO PASO A PASO

Sigamos un ejemplo de principio a fin.

**Escenario:**
- Producto: Tomates
- Stock actual: 150 kg
- Historial de 7 días: se fueron consumiendo 100, 95, 90, 85, 82, 78, 75 kg

### Paso 1: El usuario hace clic

En la pantalla del sistema, el usuario presiona "Predecir stock". La capa de presentación llama a `EstadisticaService.ejecutarPrediccionStock()`.

### Paso 2: Se preparan los datos

El servicio le pide al repositorio que prepare los datos. El repositorio hace una consulta a la base de datos SQLite:

```
SELECT id_producto, stock, fecha FROM historial_inventario
```

Toma los últimos movimientos de cada producto y los guarda en `datos_stock.csv`.

### Paso 3: Se validan los datos

El servicio revisa: ¿hay al menos 7 días de histórico para los tomates? Sí, hay 7. Perfecto.

### Paso 4: Se ejecuta Python

El servicio lanza el script Python:

```
python prediccion_stock.py
```

Este script lee `datos_stock.csv`, agarra los datos de los tomates, y se los pasa a Prophet.

### Paso 5: Prophet analiza

Prophet mira los números:

- Día 1: 100 kg → Día 2: 95 kg → Día 3: 90 kg → ... → Día 7: 75 kg

Detecta una tendencia: el consumo de tomates baja aproximadamente 5 kg por día. Con esa información, predice los próximos 7 días:

- Día 8: 70 kg, Día 9: 65 kg, Día 10: 60 kg... y así.

Saca un promedio: **55 kg por día**.

### Paso 6: Python devuelve el resultado

El script imprime en pantalla:

```
7,55
```

Significa: producto ID 7, demanda diaria promedio 55 kg.

### Paso 7: Java recibe y calcula

El servicio lee esa línea y hace las cuentas:

- **Días hasta agotarse:** 150 kg / 55 kg por día = 2.7 → **3 días**
- **Cantidad sugerida:** 55 kg × 7 días = **385 kg**
- **Riesgo:** 3 días es menor que 5 → **ALTO (rojo)**

### Paso 8: Se muestra al usuario

La capa de presentación recibe estos datos y los muestra en una tabla:

| Producto | Stock | Demanda diaria | Días restantes | Sugerido | Riesgo |
|----------|-------|----------------|----------------|----------|--------|
| Tomates | 150 kg | 55 kg/día | 3 días | 385 kg | 🔴 ALTO |

El usuario ve que los tomates están en riesgo y puede comprar antes de que falten.

---

## PARTE 4: RESUMEN VISUAL

### Las capas explicadas en una frase

| Capa | Explicación simple |
|------|-------------------|
| **Presentación** | La pantalla que el usuario mira y toca |
| **Negocio** | El que dice "chequeá esto, pasale aquello, calculá esto otro" |
| **Datos** | El que va al archivo, busca los papeles y los ordena |
| **IA (Python)** | El analista externo que recibe los papeles y devuelve cuentas |
| **Base de datos** | El placard donde está guardado todo el historial |

### El viaje de los datos en 4 líneas

```
Usuario hace clic
  → Servicio coordina
    → Repositorio busca datos de la BD y los pasa a CSV
      → Python + Prophet analizan y predicen
    → Servicio recibe, calcula y arma resultado
  → Controlador recibe y prepara para mostrar
→ Usuario ve la tabla con predicciones
```

### Algoritmo de decisión en lenguaje simple

```
Si los días hasta agotarse son 5 o menos:
  → Riesgo ALTO → Comprar YA
Si no:
  → Riesgo BAJO → Monitorear nomás
```

---

## Conclusión

La IA predictiva de stock no reemplaza al dueño del negocio. Es una herramienta que le ahorra tiempo y le da información anticipada para tomar mejores decisiones.

En lugar de revisar 50 productos uno por uno, el sistema le dice: "estos 3 productos están en riesgo, el resto está bien". El dueño solo tiene que ocuparse de los que realmente necesitan atención.

> **"No se trata de que la IA decida por vos. Se trata de que veas lo que antes no podías ver a tiempo."**
