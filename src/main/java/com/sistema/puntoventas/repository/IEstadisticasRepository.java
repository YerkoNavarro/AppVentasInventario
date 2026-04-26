package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.RankingProductosDTO;

import java.util.List;

public interface IEstadisticasRepository {
    int obtenerIngresosTotales(String periodo);
    List<RankingProductosDTO>obtenerRankingProductos(int limite);
    int obtenerVentasUsuario(int idUsuario);
    int prepararDatosParaIA();
}
