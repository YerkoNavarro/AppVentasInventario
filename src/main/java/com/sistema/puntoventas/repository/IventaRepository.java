

package com.sistema.puntoventas.repository;

import java.util.List;

import com.sistema.puntoventas.modelo.venta;

public interface IventaRepository {
    public Boolean registrarVentaCompleta(venta venta);
    public boolean actualizarVenta(venta venta);
    public venta obtenerVentaPorId(int id);
    public List<venta> obtenerVentas();
    public void anularVenta(int id);
    
}
