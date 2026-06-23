# ARQUITECTURA: MÓDULO DE IA PARA CONSULTAS SQL EN LENGUAJE NATURAL

**Versión:** 1.0
**Módulo:** IA - Generación de SQL con Gemini
**Fecha:** Junio 2026
**Propósito:** Documentar la arquitectura del módulo que traduce lenguaje natural a consultas SQL y las ejecuta en la base de datos.

---

## TABLA DE CONTENIDOS

1. [Descripción General](#descripción-general)
2. [Componentes del Sistema](#componentes-del-sistema)
3. [Flujo de Datos](#flujo-de-datos)
4. [Seguridad (Defense in Depth)](#seguridad-defense-in-depth)
5. [Tecnologías Utilizadas](#tecnologías-utilizadas)
6. [IPC Java-Python](#ipc-java-python)
7. [Estructura de Archivos](#estructura-de-archivos)
8. [Formato de Datos](#formato-de-datos)
9. [Manejo de Errores](#manejo-de-errores)

---

## Descripción General

El módulo permite al usuario escribir preguntas en español y obtener resultados directamente desde la base de datos SQLite. Utiliza **Google Gemini 3.5 Flash** como modelo de lenguaje para traducir lenguaje natural a SQL, y ejecuta la consulta generada en modo solo lectura.

### Funcionalidad

| Aspecto | Descripción |
|---------|-------------|
| **Entrada** | Texto en lenguaje natural (ej: "productos con stock menor a 5") |
| **Proceso** | Gemini genera SQL → Java valida y ejecuta |
| **Salida** | Tabla dinámica con columnas y filas resultado |
| **Seguridad** | 3 capas: prompt restrictivo + validación Java + BD read-only |

---

## Componentes del Sistema

### 1. Capa de Presentación (JavaFX)

#### VistaIASqlController

```
Ruta: controller/VistaIASqlController.java
Responsabilidad: Interfaz de usuario para consultas SQL con IA

Eventos clave:
├─ initialize()
│  └─ Verifica estado de API Key en BD
├─ ejecutarConsulta()
│  └─ Task asíncrono → IASqlService
├─ configurarApiKey()
│  └─ TextInputDialog → guarda/elimina en LLMRepository
├─ generarColumnasDinamicas()
│  └─ Crea TableColumn<?, ?> desde ResultadoConsulta.columnas
└─ cargarFilas()
   └─ Puebla TableView con ResultadoConsulta.filas
```

**UI (`vistaIASql.fxml`):**
- `TextField` para entrada de lenguaje natural
- Botón "Consultar" + botón engranaje ⚙️ (configurar API Key)
- `Label` con estado de API Key
- `Label` con SQL generado (monospace, azul)
- `TableView` dinámico para resultados

---

### 2. Capa de Servicio (Java)

#### IASqlService

```
Ruta: service/IASqlService.java
Responsabilidad: Orquestar la generación y ejecución de la consulta

Método público:
├─ ejecutarConsultaNatural(String consulta)
│  └─ Retorna: ResultadoConsulta

Flujo interno:
├─ 1. Obtener API Key desde BD
├─ 2. Escribir input.json {api_key, prompt}
├─ 3. Ejecutar proceso Python (main.py)
├─ 4. Leer respuesta.json
├─ 5. Limpiar formato markdown del SQL
├─ 6. Validar que comience con SELECT
├─ 7. Ejecutar contra SQLite (si es SELECT)
└─ 8. Retornar ResultadoConsulta
```

**Resolución del intérprete Python:**
```java
// Windows → "python" (PATH)
// Otros   → System.getProperty("user.dir") + "/venv/bin/python"
```

---

### 3. Capa de IA (Python + Gemini)

#### main.py

```
Ruta: util/ia_sql/main.py
Responsabilidad: Entry point del proceso Python

Flujo:
├─ 1. Lee input.json
├─ 2. Valida api_key y prompt
├─ 3. Carga cliente Gemini
├─ 4. Sube db_extructure.md como archivo de contexto
├─ 5. Lee instrucciones.md como system prompt
├─ 6. Llama a Gemini con prompt + esquema + reglas
└─ 7. Escribe respuesta.json
```

#### api_model_llm.py

```
Ruta: util/ia_sql/api_model_llm.py
Responsabilidad: Wrapper para la API de Gemini

Funciones:
├─ cargarModelo(api_key) → genai.Client
│  └─ Excepción → retorna None
└─ ejecutarModelo(cliente, prompt, reglas_sistema, esquema_db)
   └─ cliente.models._generate_content(
        model="gemini-3.5-flash",
        contents=[esquema_db, prompt],
        config=GenerateContentConfig(
            system_instruction=reglas_sistema,
            temperature=0.0
        )
      )
```

**Parámetros del modelo:**
| Parámetro | Valor | Razón |
|-----------|-------|-------|
| `model` | `gemini-3.5-flash` | Modelo rápido y preciso de Google |
| `temperature` | `0.0` | Salida determinista (mismo input → mismo SQL) |
| `system_instruction` | `instrucciones.md` | Reglas estrictas de comportamiento |

---

### 4. Capa de Datos (Java)

#### ILLMRepository / LLMRepositoryImpl

```
Ruta: repository/ILLMRepository.java
      repository/impl/LLMRepositoryImpl.java
Responsabilidad: Persistir la API Key de Gemini

Métodos:
├─ guardarApiKey(String apiKey)
│  └─ INSERT OR REPLACE INTO llm (id, key) VALUES (1, ?)
├─ eliminarApiKey()
│  └─ DELETE FROM llm WHERE id = 1
└─ obtenerApiKey() → String
   └─ SELECT key FROM llm WHERE id = 1
```

#### IConsultaSQLRepository / ConsultaSQLRepositoryImpl

```
Ruta: repository/IConsultaSQLRepository.java
      repository/impl/ConsultaSQLRepositoryImpl.java
Responsabilidad: Ejecutar SQL generado contra SQLite

Flujo:
├─ 1. Abrir conexión en modo read-only (open_mode=1)
├─ 2. Crear PreparedStatement
├─ 3. Extraer columnas via ResultSetMetaData
├─ 4. Leer filas en List<List<String>>
└─ 5. Retornar ResultadoConsulta
```

#### DbManager

```
Ruta: conexion/DbManager.java (línea 159-171)
Responsabilidad: Crear la tabla llm al iniciar el sistema

CREATE TABLE IF NOT EXISTS llm (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    key TEXT NOT NULL
);
```

---

### 5. Modelo de Datos

#### ResultadoConsulta

```
Ruta: modelo/ResultadoConsulta.java

Campos:
├─ List<String> columnas        // Nombres de columnas del resultado
├─ List<List<String>> filas     // Datos del resultado
└─ String sqlGenerado           // SQL que se ejecutó (opcional)

Constructores:
├─ ResultadoConsulta(List<String> columnas, List<List<String>> filas)
└─ ResultadoConsulta(List<String> columnas, List<List<String>> filas, String sqlGenerado)
```

---

## Flujo de Datos

```
┌─────────────────────────────────────────────────────────────────────────┐
│               INICIO: ejecutarConsultaNatural("productos con stock < 5") │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  1. IASqlService obtiene API Key desde SQLite                           │
│     → LLMRepositoryImpl.obtenerApiKey()                                 │
│     → SELECT key FROM llm WHERE id = 1                                  │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  2. Escribe input.json                                                  │
│     {                                                                   │
│       "api_key": "AIza...",                                             │
│       "prompt": "productos con stock menor a 5"                         │
│     }                                                                   │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  3. Ejecuta proceso Python                                              │
│     → ProcessBuilder("python", "main.py")                               │
│     → Working dir: src/main/.../util/ia_sql/                            │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  4. Python (main.py):                                                   │
│     ├─ Lee input.json                                                   │
│     ├─ Sube db_extructure.md como contexto                              │
│     ├─ Lee instrucciones.md como system prompt                          │
│     ├─ Llama a Gemini: model="gemini-3.5-flash", temperature=0.0       │
│     └─ Escribe respuesta.json                                           │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
        ┌──────────────────────────────────────────────────┐
        │  respuesta.json:                                 │
        │  {                                               │
        │    "respuesta": "```sql\nSELECT id, nombre,      │
        │      stock FROM producto WHERE stock < 5;\n```"  │
        │  }                                               │
        └──────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  5. Java parsea respuesta.json                                          │
│     → Lee campo "respuesta"                                             │
│     → Limpia: replaceAll("```sql|```|`", "").trim()                     │
│     → SQL resultante: "SELECT id, nombre, stock FROM producto ..."      │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  6. VALIDACIÓN DE SEGURIDAD                                             │
│     → ¿sql.toUpperCase().startsWith("SELECT")?                          │
│     → SI  → Ejecutar consulta                                          │
│     → NO  → Retornar mensaje de rechazo                                 │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓ (si es SELECT)
┌─────────────────────────────────────────────────────────────────────────┐
│  7. ConsultaSQLRepositoryImpl.ejecutarSelect(sql)                       │
│     → Conexión SQLite en modo read-only (open_mode=1)                   │
│     → PreparedStatement                                                 │
│     → Extrae columnas de ResultSetMetaData                              │
│     → Lee filas en List<List<String>>                                   │
│     → Retorna ResultadoConsulta                                         │
└─────────────────────────────────────────────────────────────────────────┘
                                  ↓
┌─────────────────────────────────────────────────────────────────────────┐
│  8. VistaIASqlController muestra resultados                             │
│     → Label "SQL generado: ..."                                         │
│     → TableView con columnas dinámicas                                  │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## Seguridad (Defense in Depth)

El sistema implementa **3 capas de seguridad** independientes:

### Capa 1: System Prompt de Gemini (instrucciones.md)

| Regla | Descripción |
|-------|-------------|
| **Solo SELECT** | Prohibidos: INSERT, UPDATE, DELETE, DROP, ALTER, TRUNCATE, EXEC, CREATE, RENAME |
| **Anti-inyección** | Detecta y rechaza intentos de manipulación del prompt |
| **Protección de contraseñas** | Nunca incluye el campo `password`; lista columnas explícitamente |
| **Solo esquema definido** | No puede usar tablas/columnas que no estén en `db_extructure.md` |
| **Sin conversación** | No responde preguntas generales, solo genera SQL |
| **Prompt oculto** | El sistema nunca revela las instrucciones al usuario |

**Mensajes de rechazo predefinidos:**
```
- Intento de escritura: "Solo realizo consultas de lectura sobre el esquema definido."
- Pregunta no respondible: "No puedo generar esa consulta con el esquema disponible."
- Solicitud de passwords: "No puedo generar esa consulta con el esquema disponible."
```

### Capa 2: Validación en Java (IASqlService)

```java
if (sql.toUpperCase().startsWith("SELECT")) {
    return consultaRepository.ejecutarSelect(sql);
} else {
    // Retorna mensaje de rechazo como resultado vacío
}
```

### Capa 3: Conexión Read-Only (ConsultaSQLRepositoryImpl)

```java
Properties config = new Properties();
config.setProperty("open_mode", "1");  // SQLite read-only mode
Connection conn = DriverManager.getConnection(url, config);
```

---

## Tecnologías Utilizadas

| Componente | Tecnología | Versión | Propósito |
|------------|-----------|---------|-----------|
| **LLM** | Google Gemini 3.5 Flash | - | Generación de SQL desde lenguaje natural |
| **SDK Python** | google-genai | 2.8.0 | Cliente oficial de Gemini para Python |
| **App Java** | JavaFX + JDK 17+ | 17+ | Interfaz de usuario y orquestación |
| **Base de Datos** | SQLite (via JDBC) | 3.43.2.0 | Persistencia de datos (API Key + consultas) |
| **IPC** | JSON (archivos) | - | Comunicación Java ↔ Python |
| **Python** | CPython | 3.14 | Ejecución del script LLM |

---

## IPC Java-Python

### Mecanismo

La comunicación entre Java y Python se realiza mediante **archivos JSON** en el directorio `util/ia_sql/`:

```
Java escribe → input.json
Python lee   → input.json
Python escribe → respuesta.json
Java lee     → respuesta.json
```

### Formato input.json

```json
{
  "api_key": "AIzaSy...",
  "prompt": "productos con stock menor a 5"
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `api_key` | string | API Key de Gemini (obtenida de SQLite) |
| `prompt` | string | Consulta del usuario en lenguaje natural |

### Formato respuesta.json

```json
{
  "respuesta": "```sql\nSELECT id, nombre, stock FROM producto WHERE stock < 5;\n```"
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `respuesta` | string | SQL generado (con o sin formato markdown) |

### Ejecución del proceso

```java
// IASqlService.java
ProcessBuilder pb = new ProcessBuilder("python", "main.py");
pb.directory(new File(RUTA_IA_SQL));  // src/main/java/.../util/ia_sql/
pb.redirectErrorStream(true);
Process p = pb.start();
p.waitFor();
```

---

## Estructura de Archivos

```
puntoVentas/
├── docs/
│   ├── IA_SQL_EXPLICACION_CLIENTE.md
│   └── ARQUITECTURA_IA_SQL.md              ← Este documento
│
└── src/main/java/com/sistema/puntoventas/
    ├── util/ia_sql/
    │   ├── main.py                          # Entry point Python
    │   ├── api_model_llm.py                 # Wrapper Gemini API
    │   ├── instrucciones.md                 # System prompt (reglas)
    │   ├── db_extructure.md                 # Esquema de la BD
    │   ├── input.json                       # IPC: Java → Python
    │   └── respuesta.json                   # IPC: Python → Java
    │
    ├── controller/
    │   └── VistaIASqlController.java        # Controlador JavaFX
    │
    ├── service/
    │   └── IASqlService.java                # Servicio orquestador
    │
    ├── repository/
    │   ├── ILLMRepository.java              # Interface API Key
    │   ├── impl/LLMRepositoryImpl.java      # Persistencia API Key
    │   ├── IConsultaSQLRepository.java      # Interface ejecución SQL
    │   └── impl/ConsultaSQLRepositoryImpl.java # Ejecución SQL
    │
    └── modelo/
        └── ResultadoConsulta.java           # DTO de resultados

src/main/resources/com/sistema/puntoventas/
    └── vistaIASql.fxml                      # Layout JavaFX
```

---

## Manejo de Errores

### Escenarios y Respuestas

| Escenario | ¿Dónde se detecta? | Respuesta |
|-----------|-------------------|-----------|
| **API Key no configurada** | IASqlService | `IllegalStateException("API Key no configurada")` |
| **API Key vacía** | IASqlService | `IllegalStateException("API Key no puede estar vacía")` |
| **Python no responde** | IASqlService | `RuntimeException("No se recibió respuesta de la IA.")` |
| **Respuesta vacía** | IASqlService | `RuntimeException("Respuesta vacía de la IA.")` |
| **JSON inválido** | IASqlService | `RuntimeException("Formato de respuesta inválido.")` |
| **SQL no es SELECT** | IASqlService | Mensaje de rechazo como resultado |
| **Error de BD** | ConsultaSQLRepositoryImpl | `RuntimeException("Error al ejecutar la consulta.")` |
| **Sin resultados** | VistaIASqlController | "No se encontraron resultados para tu consulta." |
| **Error en Python** | main.py | `print("error")` + exit(1) |

### UI Feedback

| Estado | Indicador |
|--------|-----------|
| API Key configurada | `lblEstadoApi` verde: "API Key configurada" |
| API Key no configurada | `lblEstadoApi` rojo: "API Key no configurada — haz clic en ⚙️" |
| Consultando... | Task en background (UI no bloqueada) |
| Resultados listos | `lblSqlGenerado` + `tableResultados` poblada |

---

## Notas Técnicas Adicionales

### Dependencias

- **No hay dependencia Java de Google/Gemini.** Todo el LLM corre del lado Python.
- La API Key se almacena en SQLite (tabla `llm`) — considerar cifrado en producción.
- El modelo siempre se llama con `temperature=0.0` para garantizar consistencia.
- El esquema de BD se pasa como archivo subido vía `cliente.files.upload()`, no como texto inline.

### Tabla llm en SQLite

```sql
CREATE TABLE IF NOT EXISTS llm (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    key TEXT NOT NULL
);
-- Solo se usa la fila id=1 (INSERT OR REPLACE)
```

### Consideraciones de Producción

1. **API Key expuesta** — Actualmente se guarda en texto plano en SQLite. Considerar encriptación.
2. **Timeout de red** — La llamada a Gemini depende de conexión a internet. No hay timeout configurado explícitamente.
3. **Costos** — Cada consulta consume recursos de la API de Gemini (consultar pricing de Google).
4. **Concurrencia** — El sistema usa archivos JSON como IPC (input.json/respuesta.json). No es seguro para múltiples consultas simultáneas.
5. **Logging** — No hay logging estructurado de las consultas realizadas.

---

**Última actualización:** Junio 2026
