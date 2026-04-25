
package com.sistema.puntoventas.modelo;

public class detalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idProducto; 


    //constructor

    public detalleVenta() {
    }

    public detalleVenta(int idDetalle, int idVenta, int idProducto) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
    }

    //getters y setters 
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }   

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    //toString
    @Override
    public String toString() {
        return "detalleVenta{" +
                "idDetalle=" + idDetalle +
                ", idVenta=" + idVenta +
                ", idProducto=" + idProducto +
                '}';
    }

}
