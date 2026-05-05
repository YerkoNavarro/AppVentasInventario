package com.sistema.puntoventas.modelo;

import java.util.List;

import com.sistema.puntoventas.modelo.moduloProducto.Producto;

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

    //get del nombre del producto
    public String getNombreProducto(){
        StringBuilder sb = new StringBuilder();
        for (Producto producto : detalleVentas) {
            sb.append(producto.getNombre()).append("- ");
        }
        return sb.toString();
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
