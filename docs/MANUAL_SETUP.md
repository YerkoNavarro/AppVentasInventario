# 🚀 MANUAL DE CONFIGURACIÓN: MÓDULO IA PREDICCIÓN STOCK

**Versión:** 1.0  
**Propósito:** Guía paso a paso para configurar y ejecutar el módulo de IA  
**Fecha:** Mayo 2026

---

## 📋 TABLA DE CONTENIDOS

1. [Requisitos del Sistema](#requisitos-del-sistema)
2. [Instalación Paso a Paso](#instalación-paso-a-paso)
3. [Configuración del Proyecto](#configuración-del-proyecto)
4. [Prueba de Funcionamiento](#prueba-de-funcionamiento)
5. [Solución de Problemas](#solución-de-problemas)

---

## ✅ Requisitos del Sistema

### Software Obligatorio

| Componente | Versión Mínima | Propósito |
|---|---|---|
| **Java** | 11 | Compilación y ejecución |
| **Python** | 3.8 | Ejecución de scripts IA |
| **Maven** | 3.6 | Gestión de dependencias Java |
| **SQLite** | 3.0 | Base de datos |

### Verificar Instalación

```powershell
# Verificar Java
java -version

# Verificar Python
python --version

# Verificar Maven
mvn --version
```

---

## 🔧 Instalación Paso a Paso

### Paso 1: Instalar Python (si no está instalado)

**Windows:**

1. Descargar desde: https://www.python.org/downloads/
2. Ejecutar instalador
3. ✅ Marcar: "Add Python to PATH"
4. Instalar

**Verificar:**

```powershell
python --version
# Salida esperada: Python 3.x.x
```

---

### Paso 2: Instalar Librerías Python Requeridas

**Abrir PowerShell en la carpeta del proyecto** y ejecutar:

```powershell
# Instalar Prophet (incluye Pandas, NumPy)
pip install fbprophet

# Alternativa si fbprophet da problemas:
pip install prophet

# Instalar Pandas (si no se instaló con Prophet)
pip install pandas
```

**Verificar instalación:**

```powershell
python -c "import prophet; print('Prophet OK')"
python -c "import pandas; print('Pandas OK')"
```

**Salida esperada:**

```
Prophet OK
Pandas OK
```

---

### Paso 3: Configurar Proyecto Java

#### Opción A: Con Maven (RECOMENDADO)

```powershell
# 1. Navegar a carpeta raíz del proyecto
cd C:\Users\isaac\OneDrive\Proyectos\puntoVentas

# 2. Compilar
mvn clean compile

# 3. Empaquetar
mvn package

# Salida esperada:
# [INFO] BUILD SUCCESS
```

#### Opción B: Con IDE (IntelliJ IDEA)

1. Abrir proyecto en IntelliJ
2. `File → Project Structure → Project`
3. Seleccionar JDK 11+
4. Rebuild Project (`Build → Rebuild Project`)

---

### Paso 4: Verificar Base de Datos

```powershell
# Abrir PowerShell en la carpeta del proyecto
# Ejecutar aplicación (genera BD si no existe)

# O ejecutar clase DbManager para crear tablas
java -cp "target/classes;lib/*" com.sistema.puntoventas.conexion.DbManager
```

**Verificar tabla `producto` existe:**

```powershell
# Opción 1: Con SQLite CLI
sqlite3 DBventasInventario.db

# Dentro de SQLite:
sqlite> .tables
# Debe mostrar: producto, venta, detalle_venta, ...

sqlite> .quit
```

---

## ⚙️ Configuración del Proyecto

### 1. Archivo: pom.xml

Verificar que tenga dependencias:

```xml
<dependencies>
    <!-- SQLite JDBC -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.43.2.0</version>
    </dependency>
    
    <!-- SLF4J API -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
    
    <!-- JavaFX (si usa interfaz gráfica) -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
</dependencies>
```

---

### 2. Archivo: module-info.java

Debe contener:

```java
module com.sistema.puntoventas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;
    requires org.slf4j;
    
    opens com.sistema.puntoventas to javafx.fxml;
    opens com.sistema.puntoventas.controller to javafx.fxml;
}
```

---

### 3. Estructura de Carpetas Requerida

```
puntoVentas/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/sistema/puntoventas/
│       │       ├── modelo_predictivo.py        ← Script 1
│       │       ├── prediccion_stock.py         ← Script 2
│       │       ├── service/
│       │       │   └── EstadisticaService.java
│       │       ├── repository/
│       │       │   └── impl/
│       │       │       └── EstadisticasRepositoryImpl.java
│       │       └── pruebas/
│       │           └── PruebaPrediccionStock.java
│       └── resources/
├── docs/
│   ├── ARQUITECTURA_IA_PREDICCION.md
│   ├── SPEC_PREDICCION_STOCK.md
│   └── MANUAL_SETUP.md
├── datos_stock.csv        ← Generado automáticamente
├── datos_ventas.csv       ← Generado automáticamente
└── DBventasInventario.db  ← Base de datos SQLite
```

---

### 4. Variables de Entorno (Opcional pero Recomendado)

**Windows PowerShell:**

```powershell
# Agregar al perfil de PowerShell ($PROFILE)
[System.Environment]::SetEnvironmentVariable("PYTHON_HOME", "C:\Users\[TuUsuario]\AppData\Local\Programs\Python\Python311", "User")

# O ejecutar directamente:
$env:PYTHON_HOME = "C:\Users\isaac\AppData\Local\Programs\Python\Python311"
```

---

## 🧪 Prueba de Funcionamiento

### Opción 1: Ejecutar Clase de Prueba (RECOMENDADO)

```powershell
# Navegar a carpeta del proyecto
cd C:\Users\isaac\OneDrive\Proyectos\puntoVentas

# Ejecutar PruebaPrediccionStock
mvn exec:java -Dexec.mainClass="com.sistema.puntoventas.pruebas.PruebaPrediccionStock"
```

**Salida esperada:**

```
==================================================
   PRUEBA DE IA: PREDICCIÓN DE AGOTAMIENTO STOCK  
==================================================
[INFO] Iniciando proceso de análisis...
[INFO] (Esto generará el archivo 'datos_stock.csv' y ejecutará Python)

================ RESULTADOS ENCONTRADOS ==============
ID PRODUCTO | DÍAS RESTANTES | SUGERENCIA COMPRA | RIESGO
------------------------------------------------------------------
1           | 33             | 32                | BAJO
2           | 3              | 57                | ALTO (!)
3           | 8              | 15                | BAJO
------------------------------------------------------------------
[ÉXITO] Se analizaron 3 productos.

================ FIN DE LA PRUEBA ================
```

---

### Opción 2: Ejecutar desde IDE

**IntelliJ IDEA:**

1. Clic derecho en `PruebaPrediccionStock.java`
2. Seleccionar: `Run 'PruebaPrediccionStock.main()'`
3. Ver resultados en consola

---

### Opción 3: Prueba Manual (Paso a Paso)

```powershell
# 1. Generar datos CSV (desde Java)
java -cp "target/classes;lib/*" com.sistema.puntoventas.service.EstadisticaService

# 2. Verificar que se creó datos_stock.csv
Test-Path -Path "datos_stock.csv"
# Salida: True

# 3. Ver contenido del CSV
Get-Content datos_stock.csv | Select-Object -First 10

# 4. Ejecutar Python manualmente
python "src/main/java/com/sistema/puntoventas/prediccion_stock.py"

# Salida esperada:
# 1,4.5
# 2,8.2
# 3,2.1
```

---

## 🐛 Solución de Problemas

### Problema 1: "No suitable driver found"

**Error:**

```
java.sql.SQLException: No suitable driver found for jdbc:sqlite:...
```

**Solución:**

```powershell
# 1. Verificar que sqlite-jdbc está en lib/
ls lib/sqlite-jdbc*.jar

# 2. Si no existe, descargar:
# Descargar: https://github.com/xerial/sqlite-jdbc/releases
# Copicar JAR a carpeta: lib/

# 3. Recompilar Maven
mvn clean compile
```

---

### Problema 2: "can't open file 'prediccion_stock.py'"

**Error:**

```
python: can't open file 'C:\...\prediccion_stock.py': [Errno 2] No such file or directory
```

**Solución:**

```powershell
# 1. Verificar que archivo existe
Test-Path "src/main/java/com/sistema/puntoventas/prediccion_stock.py"

# 2. Si no existe, crear archivo (ver paso 3)

# 3. Verificar ruta en EstadisticaService.java:
# Debe ser: "src/main/java/com/sistema/puntoventas/prediccion_stock.py"
```

---

### Problema 3: "ImportError: No module named 'prophet'"

**Error:**

```
ImportError: No module named 'prophet'
```

**Solución:**

```powershell
# 1. Verificar instalación
pip show fbprophet

# 2. Si no está instalada:
pip install --upgrade pip
pip install fbprophet

# 3. Si sigue fallando:
pip install prophet  # Nombre alternativo

# 4. Verificar que funciona:
python -c "from prophet import Prophet; print('OK')"
```

---

### Problema 4: "Datos insuficientes"

**Error:**

```
Datos insuficientes 3 días. La IA necesita al menos 7 días
```

**Solución:**

```powershell
# 1. Agregar más datos a la base de datos
# Registrar varias ventas en varios días

# 2. O ejecutar script de población masiva:
# (Consultar SCRIPT_POBLADO_MASIVO_IA.sql)

# 3. Verificar datos en BD:
sqlite3 DBventasInventario.db "SELECT COUNT(*) FROM venta"
# Debe devolver > 7 registros
```

---

### Problema 5: "Module not found: mysql.connector.j"

**Error:**

```
java: module not found: mysql.connector.j
```

**Solución:**

Este proyecto usa SQLite, no MySQL. Verificar:

```xml
<!-- En pom.xml NO debe haber: -->
<!-- <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency> -->

<!-- DEBE haber: -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.43.2.0</version>
</dependency>
```

---

### Problema 6: JavaFX Version Mismatch

**Advertencia:**

```
WARNING: Loading FXML document with JavaFX API of version 25 
by JavaFX runtime of version 17.0.2-ea
```

**Solución:**

```xml
<!-- En pom.xml, asegurar versión consistente: -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>17.0.2</version>  ← DEBE coincidir con runtime
</dependency>
```

---

## ✅ Checklist de Verificación

Antes de ejecutar, verificar:

```
[ ] Java 11+ instalado
    java -version

[ ] Python 3.8+ instalado
    python --version

[ ] Prophet instalado
    pip show fbprophet

[ ] Pandas instalado
    pip show pandas

[ ] Carpeta docs/ existe
    Test-Path docs/

[ ] Scripts Python existen
    Test-Path src/main/java/com/sistema/puntoventas/prediccion_stock.py

[ ] BD SQLite existe
    Test-Path DBventasInventario.db

[ ] pom.xml configurado
    sqlite-jdbc en dependencies

[ ] Hay datos en tabla producto
    sqlite3 DBventasInventario.db "SELECT COUNT(*) FROM producto"
```

---

## 🚀 Próximos Pasos

1. ✅ Configuración completada
2. 🧪 Ejecutar `PruebaPrediccionStock`
3. 📊 Integrar resultados en UI
4. 📈 Monitorear en Dashboard
5. 📚 Leer documentación completa

---

**Última actualización:** Mayo 2026  
**Soporte:** Consultar ARQUITECTURA_IA_PREDICCION.md

