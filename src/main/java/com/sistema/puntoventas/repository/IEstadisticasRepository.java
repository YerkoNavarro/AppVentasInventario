package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.RankingVendedoresDTO;
import com.sistema.puntoventas.modelo.moduloProducto.RankingProductosDTO;

import java.util.List;

public interface IEstadisticasRepository {
    double obtenerIngresosTotales(String periodo);
    List<RankingProductosDTO>obtenerRankingProductos(int limite);
    int obtenerVentasUsuario(int idUsuario);
    int prepararDatosParaIA();
    int prepararDatosStockParaIA();
    double obtenerPerdidasTotales(String periodo);
    List<RankingVendedoresDTO> obtenerRankingVendedores(int limite);
    List<String> obtenerUltimasActividades(int limite);
}
