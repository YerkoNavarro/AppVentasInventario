package com.sistema.puntoventas.modelo;

public class DetallePlatillo {
    private int id;
    private int idPlatillo;
    private Platillo platillo;
    private double cantidadIngrediente;

    public DetallePlatillo() {
    }

    public DetallePlatillo(int id, double cantidadIngrediente, Platillo platillo, int idPlatillo) {
        this.id = id;
        this.cantidadIngrediente = cantidadIngrediente;
        this.platillo = platillo;
        this.idPlatillo = idPlatillo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(int idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public Platillo getPlatillo() {
        return platillo;
    }

    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
    }

    public double getCantidadIngrediente() {
        return cantidadIngrediente;
    }

    public void setCantidadIngrediente(double cantidadIngrediente) {
        this.cantidadIngrediente = cantidadIngrediente;
    }
}
