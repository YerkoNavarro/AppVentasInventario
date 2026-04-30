package com.sistema.puntoventas.modelo;

import java.util.List;

public class ventaAplicacion {
     
    private venta venta;
    private List<Producto> detalleVentas;

    public ventaAplicacion() {
    }

    public ventaAplicacion(venta venta, List<Producto> detalleVentas) {
        this.venta = venta;
        this.detalleVentas = detalleVentas;
    }

    public venta getVenta() {
        return venta;
    }

    public void setVenta(venta venta) {
        this.venta = venta;
    }

    public List<Producto> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<Producto> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }

    @Override
    public String toString() {
        return "ventaAplicacion{" +
                "venta=" + venta +
                ", detalleVentas=" + detalleVentas +
                '}';
    }




}
