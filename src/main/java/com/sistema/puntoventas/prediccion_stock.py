import pandas as pd
from prophet import Prophet
import sys
import logging

logging.getLogger("prophet").setLevel(logging.WARNING)

def predecir_agotamiento():
    try:
        # 1. Leer los nuevos datos
        df = pd.read_csv("datos_stock.csv")

        # 2. Obtener lista de productos únicos
        productos = df['id_producto'].unique()

        # 3. Iterar y predecir por cada producto
        for prod_id in productos:
            # Filtramos solo los datos de este producto en específico
            df_prod = df[df['id_producto'] == prod_id][['ds', 'y']]

            # Prophet necesita mínimo 2-3 datos históricos para no dar error
            if len(df_prod) < 3:
                continue

            modelo = Prophet(weekly_seasonality=True, yearly_seasonality=False, daily_seasonality=False)
            modelo.fit(df_prod)

            # Predecir los próximos 7 días de este producto
            futuro = modelo.make_future_dataframe(periods=7)
            prediccion = modelo.predict(futuro)
            dias_futuros = prediccion.tail(7)

            # Calcular la demanda promedio diaria (evitando números negativos)
            demanda_diaria = sum(dias_futuros['yhat'].apply(lambda x: max(0, x))) / 7

            # Imprimir al formato: id_producto, demanda_diaria_promedio
            # Ejemplo de salida: 12, 4.5
            print(f"{prod_id},{round(demanda_diaria, 2)}")

    except Exception as e:
        print(f"ERROR: {e}")
        sys.exit(1)

if __name__ == '__main__':
    predecir_agotamiento()