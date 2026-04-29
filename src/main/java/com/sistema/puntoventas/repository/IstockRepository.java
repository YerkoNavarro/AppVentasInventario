package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.Producto;

import java.util.List;

public interface IstockRepository {
    List<Producto> obtenerStockCritico();
    int obtenerStockActual(int id);
}
