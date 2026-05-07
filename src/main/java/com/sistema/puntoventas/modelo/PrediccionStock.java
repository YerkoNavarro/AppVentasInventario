package com.sistema.puntoventas.modelo;

public class PrediccionStock {

    private int idPrediccion;
    private int idProducto;
    private String fechaProyectada;
    private String fechaCalculo;
    private int cantidadSugerida;
    private int diasParaAgotarse;
    private double indiceRiesgo;
    private double nivelConfianza;

    public PrediccionStock(double nivelConfianza, double indiceRiesgo, int diasParaAgotarse, int cantidadSugerida, int idProducto, int idPrediccion, String fechaProyectada, String fechaCalculo) {
        this.nivelConfianza = nivelConfianza;
        this.indiceRiesgo = indiceRiesgo;
        this.diasParaAgotarse = diasParaAgotarse;
        this.cantidadSugerida = cantidadSugerida;
        this.idProducto = idProducto;
        this.idPrediccion = idPrediccion;
        this.fechaProyectada = fechaProyectada;
        this.fechaCalculo = fechaCalculo;
    }

    public PrediccionStock() {
    }

    public int getIdPrediccion() {
        return idPrediccion;
    }

    public void setIdPrediccion(int idPrediccion) {
        this.idPrediccion = idPrediccion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getFechaProyectada() {
        return fechaProyectada;
    }

    public void setFechaProyectada(String fechaProyectada) {
        this.fechaProyectada = fechaProyectada;
    }

    public String getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(String fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }

    public int getCantidadSugerida() {
        return cantidadSugerida;
    }

    public void setCantidadSugerida(int cantidadSugerida) {
        this.cantidadSugerida = cantidadSugerida;
    }

    public int getDiasParaAgotarse() {
        return diasParaAgotarse;
    }

    public void setDiasParaAgotarse(int diasParaAgotarse) {
        this.diasParaAgotarse = diasParaAgotarse;
    }

    public double getIndiceRiesgo() {
        return indiceRiesgo;
    }

    public void setIndiceRiesgo(double indiceRiesgo) {
        this.indiceRiesgo = indiceRiesgo;
    }

    public double getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(double nivelConfianza) {
        this.nivelConfianza = nivelConfianza;
    }




}
