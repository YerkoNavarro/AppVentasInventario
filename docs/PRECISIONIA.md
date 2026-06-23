Para medir la precisión de la IA predictiva de stock, necesitas comparar lo que predijo vs lo que realmente se vendió después, y calcular métricas de error.
¿Qué métricas usar?
Métrica	Qué mide
MAE (Error Absoluto Medio)	Desviación promedio
MAPE (Error Porcentual Absoluto Medio)	% de error relativo
RMSE (Raíz del Error Cuadrático Medio)	Penaliza errores grandes
¿Cómo implementarlo?
Agregar un método en EstadisticaService que:
Tome predicciones anteriores (guardadas al generarse) y las compare con ventas reales posteriores
Calcule las métricas por producto y un promedio global
Muestre un indicador en el panel de estadísticas (ej: "Precisión: 85%")
Ejemplo de enfoque:
Para cada producto:
- predicción del día X: 10 unidades
- venta real del día X: 8 unidades
- error = |10 - 8| / 8 = 25%

Precisión del producto = (1 - MAPE) * 100 = 75%
Opciones para mostrar el resultado:
Agregar una card en el panel de estadísticas con "Precisión de la IA: 85%"
Agregar columna en la tabla de predicciones mostrando el % de acierto histórico de cada producto
Log periódico cada vez que se ejecuta la predicción
