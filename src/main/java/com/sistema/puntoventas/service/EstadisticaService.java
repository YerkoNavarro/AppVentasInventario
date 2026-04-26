package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.RankingProductosDTO;
import com.sistema.puntoventas.repository.IEstadisticasRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaService {

    private final IEstadisticasRepository estadisticasRepository;

    public EstadisticaService(IEstadisticasRepository estadisticasRepository) {
        this.estadisticasRepository = estadisticasRepository;
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


    public void ejecutarPrediccionProphet(){
        boolean datosListos = prepararDatosParaIA();

        if(!datosListos){
            System.err.println("No se puede ejecutar la predicción, faltan datos");
        }

        System.out.println("Ejecutando la IA Predictiva");

        try{
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/java/com/sistema/puntoventas/modelo_predictivo.py");

            // Unimos los errores de Python a la salida estándar para verlos en Java
            processBuilder.redirectErrorStream(true);

            Process procesoPython = processBuilder.start(); //Iniciamos python

            BufferedReader reader = new BufferedReader(new InputStreamReader(procesoPython.getInputStream()));
            String linea;
            System.out.println("--- Respuesta de la IA ---");
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea); // Aquí podríamos capturar el texto para mandarlo a JavaFX
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
}
