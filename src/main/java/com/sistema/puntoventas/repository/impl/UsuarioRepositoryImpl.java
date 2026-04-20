package com.sistema.puntoventas.repository.impl;

import com.sistema.puntoventas.conexion.Conexion;
import com.sistema.puntoventas.modelo.Role;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.IUsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements IUsuarioRepository {

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nombre, apellido, rut, contraseña, telefono, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Conexion.DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getRut());
            ps.setString(4, usuario.getContraseña());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getRol().toString());
            ps.setBoolean(7, usuario.getEstado());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Connection connection = Conexion.DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setRut(rs.getString("rut"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setRol(Role.valueOf(rs.getString("rol")));
                usuario.setEstado(rs.getBoolean("estado"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario obtenerUsuarioPorRut(String rut) {
        String sql = "SELECT * FROM Usuario WHERE rut = ?";
        try (Connection connection = Conexion.DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setRut(rs.getString("rut"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setRol(Role.valueOf(rs.getString("rol")));
                    usuario.setEstado(rs.getBoolean("estado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por RUT: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario iniciarSesion(String rut, String contraseña) {
        String sql = "SELECT * FROM Usuario WHERE rut = ? AND contraseña = ?";
        try (Connection connection = Conexion.DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, rut);
            ps.setString(2, contraseña);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setRut(rs.getString("rut"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setRol(Role.valueOf(rs.getString("rol")));
                    usuario.setEstado(rs.getBoolean("estado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario eliminarUsuario(String rut) {
        Usuario usuario = obtenerUsuarioPorRut(rut);
        if (usuario != null) {
            String sql = "DELETE FROM Usuario WHERE rut = ?";
            try (Connection connection = Conexion.DatabaseConnection.getInstance().getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, rut);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al eliminar usuario: " + e.getMessage());
                return null;
            }
        }
        return usuario;
    }
}
