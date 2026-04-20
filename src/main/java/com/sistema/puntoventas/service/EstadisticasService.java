package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.EstadisticasUsuario;
import com.sistema.puntoventas.repository.IEstadisticasUsuario;
import com.sistema.puntoventas.repository.impl.EstadisticasUsuarioImpl;

public class EstadisticasService {

    // Referencia al repositorio (la interfaz que definimos al inicio)
    private final IEstadisticasUsuario estadisticasRepository;

    /**
     * Constructor del servicio.
     * Aquí instanciamos la implementación del repositorio.
     */
    public EstadisticasService() {
        this.estadisticasRepository = new EstadisticasUsuarioImpl();
    }

    /**
     * Método para obtener las estadísticas de un empleado.
     * @param idUsuario El identificador del empleado en la base de datos.
     * @return Un objeto EstadisticasUsuario con los totales, o null si no se encuentra.
     */
    public EstadisticasUsuario obtenerVentasUsuario(int idUsuario) {
        // Validamos que el ID sea válido antes de ir a la base de datos
        if (idUsuario <= 0) {
            System.err.println("Error: ID de usuario no válido.");
            return null;
        }

        // Llamamos al repositorio para obtener los datos reales de SQLite
        return estadisticasRepository.obtenerVentasUsuario(idUsuario);
    }
}