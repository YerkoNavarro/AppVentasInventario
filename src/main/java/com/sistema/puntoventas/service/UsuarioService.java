package com.sistema.puntoventas.service;
import com.sistema.puntoventas.repository.IUsuarioRepository;
import com.sistema.puntoventas.repository.impl.UsuarioRepositoryImpl;

public class UsuarioService {
    private IUsuarioRepository usuarioRepository;
    public UsuarioService(){
        this.usuarioRepository= new UsuarioRepositoryImpl();


    }
    public String registrarNuevoUsuario(Usuario Usuario){
        if (usuario == null ){
            return "no puede ser nulo ";
        }

    }

}
