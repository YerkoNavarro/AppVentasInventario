package com.sistema.puntoventas;

import com.sistema.puntoventas.repository.impl.EstadisticasRepositoryImpl;
import com.sistema.puntoventas.service.EstadisticaService;

public class PruebaIA {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de integración Java-Python...");

        // 1. Instanciamos nuestras clases
        EstadisticasRepositoryImpl repository = new EstadisticasRepositoryImpl();
        EstadisticaService service = new EstadisticaService(repository);

        // 2. Ejecutamos el flujo completo
        // Esto creará el CSV y luego ejecutará el script de Python
        service.ejecutarPrediccionProphet();

       System.out.println("Prueba finalizada.");
   }
}
