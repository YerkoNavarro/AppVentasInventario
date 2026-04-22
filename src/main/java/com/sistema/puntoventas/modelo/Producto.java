package com.sistema.puntoventas.modelo;

public class Producto {
    private int id ;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private String categoria;
    private String fechaVenc;
    private int stockActual;
    private int stockMinimo;
    private String imagen;
    private int unidadMedida;

    public Producto(int id, String nombre, double precioCompra, double precioVenta, String categoria, String fechaVenc, int stockActual, int stockMinimo, String imagen, int unidadMedida) {
        this.id = id;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.fechaVenc = fechaVenc;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.imagen = imagen;
        this.unidadMedida = unidadMedida;
    }


    public Producto() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFechaVenc() {
        return fechaVenc;
    }

    public void setFechaVenc(String fechaVenc) {
        this.fechaVenc = fechaVenc;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(int unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", categoria='" + categoria + '\'' +
                ", fechaVenc='" + fechaVenc + '\'' +
                ", stockActual=" + stockActual +
                ", stockMinimo=" + stockMinimo +
                ", imagen='" + imagen + '\'' +
                ", unidadMedida=" + unidadMedida +
                '}';
    }
}
