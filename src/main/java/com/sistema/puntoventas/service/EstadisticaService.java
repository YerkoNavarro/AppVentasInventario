package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.PrediccionStock;
import com.sistema.puntoventas.modelo.moduloProducto.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;
import com.sistema.puntoventas.repository.moduloProductos.IProductoRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaService {

    private final IEstadisticasRepository estadisticasRepository;
    private final IProductoRepository productoRepository;

    public EstadisticaService(IEstadisticasRepository estadisticasRepository, IProductoRepository productoRepository) {
        this.estadisticasRepository = estadisticasRepository;
        this.productoRepository = productoRepository;
    }


    public int obtenerIngresosTotales(String periodo) {
        return estadisticasRepository.obtenerIngresosTotales(periodo);
    }

    public List<RankingProductosDTO> obtenerRankingProductos(int limite){
        if(limite <= 0){
            System.err.println("El límite debe ser mayor cero");
            return new ArrayList<>();
        }

        if(limite > 50){
            limite = 50; //ponemos limite para no colapsar la memoria
        }

        return estadisticasRepository.obtenerRankingProductos(limite);
    }

    public boolean prepararDatosParaIA(){
        int filasExportadas = estadisticasRepository.prepararDatosParaIA();

        if(filasExportadas <7){
            System.err.println("Datos insuficientes " + filasExportadas + "días. La IA necesita al menos 7 días");
            return false;
        }
        return true;
    }



    public boolean prepararDatosStockParaIA() {

        int filasExportadas = estadisticasRepository.prepararDatosStockParaIA();


        if (filasExportadas < 7) {
            System.err.println("Datos de stock insuficientes (" + filasExportadas + "). Se requieren al menos 7 días de historial.");
            return false;
        }

        System.out.println("Datos de stock preparados con éxito.");
        return true;
    }


    public void ejecutarPrediccionProphet(){
        boolean datosListos = prepararDatosParaIA();

        if(!datosListos){
            System.err.println("No se puede ejecutar la predicción, faltan datos");
        }

        System.out.println("Ejecutando la IA Predictiva");

        try{
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/java/com/sistema/puntoventas/modelo_predictivo.py");


            processBuilder.redirectErrorStream(true);

            Process procesoPython = processBuilder.start(); //Iniciamos python

            BufferedReader reader = new BufferedReader(new InputStreamReader(procesoPython.getInputStream()));
            String linea;
            System.out.println("--- Respuesta de la IA ---");
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
            System.out.println("-----------------------------------------------------------------");

            int exitCode = procesoPython.waitFor(); //esperamos a que termine el proceso
            if(exitCode == 0){
                System.out.println("Predicción terminada correctamente");
            }else{
                System.err.println("El script de python fallo, código de error "+exitCode);
            }

        }catch (Exception e){
            System.err.println("Error al ejecutar el modelo predictivo: " + e.getMessage());
        }

    }



    public List<PrediccionStock> ejecutarPrediccionStock() {
        List<PrediccionStock> predicciones = new ArrayList<>();

        if (!prepararDatosStockParaIA()) {
            return predicciones; // retornamos una lista vacía si no hay datos suficientes
        }

        estadisticasRepository.prepararDatosStockParaIA();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/java/com/sistema/puntoventas/prediccion_stock.py");
            processBuilder.redirectErrorStream(true);
            Process procesoPython = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(procesoPython.getInputStream()));
            String linea;

            while ((linea = reader.readLine()) != null) {
                if(linea.contains("ERROR")) continue;

                String[] partes = linea.split(",");
                if(partes.length == 2) {
                    int idProducto = Integer.parseInt(partes[0]);
                    double demandaDiaria = Double.parseDouble(partes[1]);


                    int stockActual = productoRepository.obtenerStockActual(idProducto);

                    // 2. Calculamos los días restantes
                    int diasParaAgotarse = (int) (stockActual / (demandaDiaria == 0 ? 1 : demandaDiaria));

                    // 3. Calculamos riesgo (Si se agota en menos de 5 días = Riesgo Alto)
                    double indiceRiesgo = diasParaAgotarse <= 5 ? 0.9 : 0.2;

                    // 4. Llenamos tu objeto PrediccionStock
                    PrediccionStock ps = new PrediccionStock();
                    ps.setIdProducto(idProducto);
                    ps.setDiasParaAgotarse(diasParaAgotarse);
                    ps.setCantidadSugerida((int) (demandaDiaria * 7)); // Sugerir comprar para 7 días
                    ps.setIndiceRiesgo(indiceRiesgo);

                    predicciones.add(ps);
                }
            }
            procesoPython.waitFor();

        } catch (Exception e) {
            System.err.println("Error en IA: " + e.getMessage());
        }

        return predicciones;
    }
}
