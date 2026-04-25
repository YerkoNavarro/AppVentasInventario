package com.sistema.puntoventas.service;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.IUsuarioRepository;
import com.sistema.puntoventas.repository.impl.ProductoRepositoryImpl;
import com.sistema.puntoventas.repository.impl.UsuarioRepositoryImpl;

import java.util.List;

public class UsuarioService {
    private IUsuarioRepository usuarioRepository;
    public UsuarioService(){
        this.usuarioRepository= new UsuarioRepositoryImpl();


    }
    public String registrarNuevoUsuario(Usuario usuario){
        if (usuario == null ){
            return "El usuario no puede ser nulo";
        }
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()){
            return "El nombre del usuario no puede ser nulo o vacío";
        }
        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()){
            return "El apellido del usuario no puede ser nulo o vacío";
        }
        if (usuario.getRut() == null || usuario.getRut().isEmpty()){
            return "El RUT del usuario no puede ser nulo o vacío";
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().isEmpty()){
            return "La contraseña del usuario no puede ser nula o vacía";
        }
        // Verificar si el usuario ya existe
        if (usuarioRepository.obtenerUsuarioPorRut(usuario.getRut()) != null) {
            return "El usuario con este RUT ya existe";
        }
        // Registrar el usuario
        boolean registrado = usuarioRepository.registrarUsuario(usuario);
        if (registrado) {
            return "Usuario registrado exitosamente";
        } else {
            return "Error al registrar el usuario";
        }
    }

    public Usuario iniciarSesion(String rut, String contraseña) {
        if (rut == null || rut.isEmpty() || contraseña == null || contraseña.isEmpty()) {
            return null;
        }
        return usuarioRepository.iniciarSesion(rut, contraseña);
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.obtenerUsuarios();
    }

    public Usuario obtenerUsuarioPorRut(String rut) {
        if (rut == null || rut.isEmpty()) {
            return null;
        }
        return usuarioRepository.obtenerUsuarioPorRut(rut);
    }

    public Usuario eliminarUsuario(String rut) {
        if (rut == null || rut.isEmpty()) {
            return null;
        }
        return usuarioRepository.eliminarUsuario(rut);
    }
}

