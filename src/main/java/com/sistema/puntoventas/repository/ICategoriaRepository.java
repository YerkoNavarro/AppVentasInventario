package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.modelo.Categoria;

import java.util.List;

public interface ICategoriaRepository {

    boolean registrarCategoria(Categoria categoria);
    boolean existeCategoria(String nombre);
    boolean actualizarCategoria(int id);
    boolean eliminarCategoria(int id);
    List<Categoria> obtenerCategorias();
}
