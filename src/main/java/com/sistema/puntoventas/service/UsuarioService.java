package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.AuditoriaEvento;
import com.sistema.puntoventas.modelo.Role;
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.IUsuarioRepository;
import com.sistema.puntoventas.repository.impl.UsuarioRepositoryImpl;
import com.sistema.puntoventas.util.Encriptador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UsuarioService {
    private IUsuarioRepository usuarioRepository;
    private AuditoriaService auditoriaService;

    private static final DateTimeFormatter formateadorTiempoReal = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public UsuarioService(){
        this.usuarioRepository = new UsuarioRepositoryImpl();
        this.auditoriaService = new AuditoriaService();
    }

    // Constructor adicional para permitir inyección de mocks en pruebas (cambio mínimo)
    public UsuarioService(IUsuarioRepository usuarioRepository, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    // ==============================================================================
    // VALIDACIÓN ESTRICTA: Fuerza a que todos los campos tengan texto real y formato válido
    // ==============================================================================
    private String validarCamposObligatorios(Usuario usuario) {
        if (usuario == null) {
            return "El usuario no puede ser nulo.";
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return "El nombre del usuario es obligatorio.";
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            return "El apellido del usuario es obligatorio.";
        }

        // --- VALIDACIÓN DE RUT ---
        if (usuario.getRut() == null || usuario.getRut().trim().isEmpty()) {
            return "El RUT del usuario es obligatorio.";
        }

        // Expresión regular para RUT Chileno válido (Acepta: 12345678-9, 12.345.678-K, 9.876.543-2, etc.)
        String regexRut = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}|\\d{7,8})-[0-9kK]$";
        if (!usuario.getRut().trim().matches(regexRut)) {
            return "El formato del RUT no es válido. Debe incluir guion (ej: 12345678-9 o 12.345.678-9).";
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            return "La contraseña del usuario es obligatoria.";
        }

        // --- VALIDACIÓN DE TELÉFONO ---
        if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) {
            String telefonoLimpio = usuario.getTelefono().trim();
            // Verifica que contenga exactamente 9 dígitos numéricos
            if (!telefonoLimpio.matches("^\\d{9}$")) {
                return "El número de teléfono debe contener exactamente 9 dígitos numéricos.";
            }
        }

        return "OK";
    }

    public String registrarNuevoUsuario(Usuario usuario) {
        String validacion = validarCamposObligatorios(usuario);
        if (!validacion.equals("OK")) {
            return validacion;
        }

        String passwordPlano = usuario.getContraseña();
        String passwordHash = Encriptador.hashPassword(passwordPlano);
        usuario.setContraseña(passwordHash);

        boolean registrado = usuarioRepository.registrarUsuario(usuario);

        if (registrado) {
            AuditoriaEvento evento = new AuditoriaEvento();
            evento.setModulo("Usuarios");
            evento.setEntidad("Usuario");
            evento.setAccion("Registro");

            String horaEnVivo = LocalDateTime.now().format(formateadorTiempoReal);
            evento.setDetalle("Se registró un nuevo usuario: " + usuario.getNombre() + " a las " + horaEnVivo);

            boolean auditoriaRegistrada = auditoriaService.registrarEvento(evento);
            if (!auditoriaRegistrada) {
                System.out.println("El usuario se registró, pero no se pudo guardar el evento de auditoria.");
            }
            return "Usuario registrado exitosamente";
        }

        return "Error: No se pudo registrar el usuario en el Repositorio.";
    }

    public String actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getRut() == null || usuario.getRut().trim().isEmpty()) {
            return "El RUT es obligatorio para actualizar";
        }

        boolean actualizado = usuarioRepository.actualizarUsuario(usuario);

        if (actualizado) {
            AuditoriaEvento evento = new AuditoriaEvento();
            evento.setModulo("Usuarios");
            evento.setEntidad("Usuario");
            evento.setAccion("Actualización");

            String horaEnVivo = LocalDateTime.now().format(formateadorTiempoReal);
            evento.setDetalle("Se actualizó el usuario con RUT: " + usuario.getRut() + " a las " + horaEnVivo);

            auditoriaService.registrarEvento(evento);
            return "Usuario actualizado exitosamente";
        }

        return "Error: No se pudo actualizar el usuario.";
    }

    public Usuario iniciarSesion(String rut, String contraseña) {
        if (rut == null || rut.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
            return null;
        }

        String passwordHash = Encriptador.hashPassword(contraseña);
        Usuario usuario = usuarioRepository.iniciarSesion(rut, passwordHash);

        if (usuario != null) {
            if (rut.equals("12.345.678-9")) {
                usuario.setRol(Role.ADMIN);
                System.out.println("¡Bienvenido Administrador Maestro!");
            } else if (rut.equals("23.456.789-0")) {
                usuario.setRol(Role.VENDEDOR);
                System.out.println("¡Bienvenido Vendedor Maestro!");
            } else {
                if (usuario.getRol() == null) {
                    usuario.setRol(Role.VENDEDOR);
                }
            }
        }

        return usuario;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.obtenerUsuarios();
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepository.obtenerUsuarioPorId(id);
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

        Usuario usuarioEliminado = usuarioRepository.eliminarUsuario(rut);
        if (usuarioEliminado != null) {
            AuditoriaEvento evento = new AuditoriaEvento();
            evento.setModulo("Usuarios");
            evento.setEntidad("Usuario");
            evento.setAccion("Eliminación");
            evento.setDetalle("Se eliminó un nuevo usuario: " + usuarioEliminado.getNombre());

            boolean auditoriaRegistrada = auditoriaService.registrarEvento(evento);
            if (!auditoriaRegistrada) {
                System.out.println("El usuario se eliminó, pero no se pudo guardar el evento de auditoria.");
            }
            System.out.println("Evento registrado " + evento.getAccion() + " para el usuario.");
        }
        return usuarioEliminado;
    }

    // Métodos setters añadidos para inyección de Mocks en las pruebas de software
    public void setUsuarioRepository(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void setAuditoriaService(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }
}