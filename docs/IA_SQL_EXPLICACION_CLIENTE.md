# Asistente SQL con IA

## Una explicación simple de qué hace, cómo funciona y por qué es útil

---

## PARTE 1: PARA CUALQUIERA

### ¿Qué es el asistente SQL con IA?

Imaginate que tenés un empleado que entiende perfectamente la base de datos de tu negocio, pero no sabe usar programas complicados. En lugar de tener que aprender a escribir consultas técnicas, vos simplemente le decís en español:

- "Mostrame los productos que están por agotarse"
- "¿Cuánto vendí la semana pasada?"
- "Decime los clientes que más compraron este mes"

Y él automáticamente busca la información en el sistema y te la muestra en una tabla, sin que tengas que saber nada de bases de datos ni SQL.

Eso es el **Asistente SQL con IA**. No es magia, es un traductor: vos hablás en español, y la IA lo convierte en consultas que la base de datos entiende.

### ¿Qué problema resuelve?

Sin esta herramienta, si querés hacer una pregunta específica sobre tus datos (algo que no esté en una pantalla predefinida), necesitás:

- Saber SQL (un lenguaje de consulta de bases de datos)
- Pedirle a un programador que te haga un reporte
- Revisar mil números manualmente

Con el asistente:

- Escribís la pregunta como se te ocurra
- La IA la entiende y la traduce automáticamente
- El sistema busca los datos y te los muestra al instante
- No necesitás saber nada de programación

### Ejemplo cotidiano

Supongamos que querés saber **qué productos tienen menos de 10 unidades en stock**:

1. Escribís en el cuadro de texto: *"mostrame los productos con stock menor a 10"*
2. La IA interpreta tu pedido
3. El sistema busca en la base de datos
4. Te muestra una tabla con los resultados

Sin el asistente, alguien tendría que escribir algo como `SELECT * FROM producto WHERE stock < 10` — un código que para la mayoría de las personas es chino básico.

---

## PARTE 2: ¿ES SEGURO?

### Solo lectura, sin riesgo

El asistente **solamente puede leer datos**. No puede modificar, borrar ni crear nada en el sistema. Es como darle a alguien una ventana para mirar el depósito, pero sin la llave para entrar.

Si alguien intenta pedirle "eliminá todos los productos" o "cambiá el precio de tal cosa", la IA lo rechaza automáticamente y responde: *"Solo realizo consultas de lectura sobre el esquema definido."*

### Tres barreras de seguridad

| Capa | ¿Qué hace? |
|------|------------|
| **La IA** | Está programada para rechazar cualquier intento de modificación |
| **El sistema** | Solo ejecuta las consultas que empiezan con SELECT (lectura) |
| **La base de datos** | Se abre en modo solo lectura, no permite cambios |

---

## PARTE 3: ¿QUÉ NECESITO PARA USARLO?

Para que el asistente funcione, necesitás:

1. **Una clave API de Gemini** (es como una llave digital para usar la inteligencia artificial de Google)
2. **Conexión a internet** (la IA necesita consultar a los servidores de Google)
3. **Escribir la pregunta** en lenguaje natural, como se te ocurra

Una vez que configurás la clave (solo una vez, con el botón de engranaje ⚙️), el asistente está listo para usar.

---

## PARTE 4: ¿QUÉ TIPO DE PREGUNTAS PUEDO HACER?

| Categoría | Ejemplos |
|-----------|----------|
| **Stock** | "Productos con menos de 5 unidades", "lo que está por agotarse" |
| **Ventas** | "Ventas de la semana pasada", "cuánto se vendió ayer" |
| **Clientes** | "Clientes que más compraron", "los que no compran hace un mes" |
| **Productos** | "Los 10 productos más caros", "productos por categoría" |
| **Platillos** | "Platillos que se pueden fabricar", "recetas con más ingredientes" |

### Preguntas que NO puede responder

- ❌ "Eliminá tal producto" → lo rechaza por ser una modificación
- ❌ "¿Cuál es la capital de Francia?" → no tiene esa información
- ❌ "Dame las contraseñas de los usuarios" → las contraseñas están protegidas

---

## PARTE 5: RECORRIDO PASO A PASO

**Paso 1:** Abrís la pantalla "Consultas Inteligentes SQL".

**Paso 2:** Si ves "API Key no configurada", hacés clic en el engranaje ⚙️ y pegás tu clave de Gemini.

**Paso 3:** Escribís tu pregunta, por ejemplo: *"mostrame los tres platillos más caros"*.

**Paso 4:** Presionás "Consultar".

**Paso 5:** En unos segundos ves:
   - El SQL generado (por si te interesa saber qué consulta se hizo)
   - Una tabla con los resultados

**Paso 6:** Si querés hacer otra pregunta, simplemente escribís una nueva y repetís.

---

## Resumen

> **"No necesitás saber SQL para preguntarle a tu base de datos. Solo necesitás saber lo que querés saber."**

El asistente SQL con IA es un traductor entre el español y la base de datos. Vos preguntás como se te ocurra, y él se encarga del resto. Con la tranquilidad de que solo puede leer información, nunca modificarla.
