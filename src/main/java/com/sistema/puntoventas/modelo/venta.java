
package com.sistema.puntoventas.modelo;

public class venta {
    
    private int idVenta; 
    private String fechaHora;
    private int idUsuario;
    private double totalVenta;

    public venta() {
    }

    public venta(int idVenta, String fechaHora, int idUsuario, double totalVenta) {
        this.idVenta = idVenta;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
        this.totalVenta = totalVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }
    

}
