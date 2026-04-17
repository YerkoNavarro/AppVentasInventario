package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.IUsuarioRepository;
import com.sun.jdi.connect.spi.Connection;
import java.sql.SQLException;

@Override
public class UsuarioRepositoryImpl  implements IUsuarioRepository {
    public boolean registrarUsuario (Usuario Usuario){
        String sql= "INSERT INTO Usuario (nombre, apellido, rut, contraseña, telefono, rol, estado) VALUES (?,?,?,?,?,?,?)";
        try (Connection connect = conexion.getConnection();
             preparedstatement ps= connect.preparedstatement(sql)){
            ps.setString(1, Usuario.getNombre());
            ps.setString(2, Usuario.getApellido());
            ps.setString(3, Usuario.getRut());
            ps.setString(4, Usuario.getContraseña());
            ps.setString(5, Usuario.getTelefono());
            ps.setString(6, Usuario.getRol());
            ps.setString(7, Usuario.getEstado());
            int rowsInserted = ps.executeUpdate());
            return rowsInserted >0;

        }catch (SQLException e){
            system.err.println("error al registrar usuarios" +e.getMessage());
            retunr false;
        }
    }
}
