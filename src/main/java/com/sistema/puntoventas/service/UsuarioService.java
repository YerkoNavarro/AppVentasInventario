package com.sistema.puntoventas.service;

import com.sistema.puntoventas.modelo.AuditoriaEvento;
import com.sistema.puntoventas.modelo.Role; // Importamos el Enum de Roles
import com.sistema.puntoventas.modelo.Usuario;
import com.sistema.puntoventas.repository.IUsuarioRepository;
import com.sistema.puntoventas.repository.impl.UsuarioRepositoryImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.sistema.puntoventas.util.Encriptador;

public class UsuarioService {
    private IUsuarioRepository usuarioRepository;
    private AuditoriaService auditoriaService;

    private static final DateTimeFormatter formateadorTiempoReal = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public UsuarioService(){
        this.usuarioRepository = new UsuarioRepositoryImpl();
        this.auditoriaService = new AuditoriaService();
    }
    public UsuarioService(IUsuarioRepository usuarioRepository, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    // ==============================================================================
    // VALIDACIÓN ESTRICTA: Fuerza a que todos los campos tengan texto real
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
        if (usuario.getRut() == null || usuario.getRut().trim().isEmpty()) {
            return "El RUT del usuario es obligatorio.";
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            return "La contraseña del usuario es obligatoria.";
        }
        return null; // Todo está correcto
    }

    public String registrarNuevoUsuario(Usuario usuario){
        String errorValidacion = validarCamposObligatorios(usuario);
        if (errorValidacion != null) {
            return errorValidacion;
        }

        // Hashear la contraseña antes de persistir (controller/tests esperan hash)
        String contraseñaPlana = usuario.getContraseña();
        String hash = Encriptador.hashPassword(contraseñaPlana);
        usuario.setContraseña(hash);

        // Guardar en la base de datos
        boolean registrado = usuarioRepository.registrarUsuario(usuario);

        if (registrado) {
            String horaActual = LocalDateTime.now().format(formateadorTiempoReal);
            // Guardar auditoría
            AuditoriaEvento evento = new AuditoriaEvento();
            evento.setModulo("Usuarios");
            evento.setEntidad("Usuario");
            evento.setAccion("Registro");
            evento.setDetalle("Se registró un nuevo usuario: " + usuario.getNombre());
            auditoriaService.registrarEvento(evento);

            return "Usuario registrado exitosamente";
        } else {
            return "Error al registrar el usuario en la base de datos";
        }
    }

    public String actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getRut() == null || usuario.getRut().isEmpty()) {
            return "El RUT es obligatorio para actualizar";
        }

        boolean actualizado = usuarioRepository.actualizarUsuario(usuario);

        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setModulo("Usuarios");
        evento.setEntidad("Usuario");
        evento.setAccion("Actualización");
        evento.setDetalle("Se actualizo un  usuario: " + usuario.getNombre());

        boolean auditoriaRegistrada = auditoriaService.registrarEvento(evento);
        if (!auditoriaRegistrada) {
            System.out.println("El usuario se actualizo, pero no se pudo guardar el evento de auditoria.");
        }
        System.out.println("Evento registrado "+evento.getAccion()+" para el usuario: " + usuario.getNombre());

        if (actualizado) {
            return "Usuario actualizado exitosamente";
        } else {
            return "Error al actualizar el usuario en la base de datos";
        }
    }

    // ==============================================================================
    // INICIO DE SESIÓN CON VALIDACIÓN DE ROLES POR RUT
    // ==============================================================================
    public Usuario iniciarSesion(String rut, String contraseña) {
        if (rut == null || rut.isEmpty() || contraseña == null || contraseña.isEmpty()) {
            return null;
        }

        // 1. Hasheamos la contraseña y buscamos si el usuario existe en la base de datos con esas credenciales
        String hash = Encriptador.hashPassword(contraseña);
        Usuario usuario = usuarioRepository.iniciarSesion(rut, hash);

        // 2. Si las credenciales coinciden, validamos su Rol en base a su RUT real
        if (usuario != null) {

            // ⚠️ MODIFICA ESTOS RUTS CON LOS QUE TENGAS EN TU BASE DE DATOS EXACTAMENTE
            String rutAdminMaster = "12.345.678-9";
            String rutVendedorMaster = "23.456.789-0";

            if (usuario.getRut().equals(rutAdminMaster)) {
                usuario.setRol(Role.ADMIN);
                System.out.println("Login correcto: Asignado rol de ADMINISTRADOR al RUT: " + rut);

            } else if (usuario.getRut().equals(rutVendedorMaster)) {
                usuario.setRol(Role.VENDEDOR);
                System.out.println("Login correcto: Asignado rol de VENDEDOR al RUT: " + rut);

            } else {
                // Si tienes más usuarios en la base de datos que no son el principal,
                // por defecto podemos dejarlos como VENDEDOR o mantener el que traigan de la BD.
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
        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setModulo("Usuarios");
        evento.setEntidad("Usuario");
        evento.setAccion("Eliminación");
        evento.setDetalle("Se eliminó un nuevo usuario: " + usuarioEliminado.getNombre());

        boolean auditoriaRegistrada = auditoriaService.registrarEvento(evento);
        if (!auditoriaRegistrada) {
            System.out.println("El usuario se eliminó, pero no se pudo guardar el evento de auditoria.");
        }
        System.out.println("Evento registrado " + evento.getAccion() + " para el usuario: " + usuarioEliminado.getNombre());

        return usuarioEliminado;
    }
}