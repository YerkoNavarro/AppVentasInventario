package com.sistema.puntoventas.repository;

import java.util.List;

import com.sistema.puntoventas.modelo.detalleVenta;
import com.sistema.puntoventas.modelo.venta;

public interface IDetalleVenta {

    public List<detalleVenta> obtenerDetalleVentasporIdVenta(int id);
    public String obtenerInfoVentaDetalle(int id); //idDetalleVenta

}
