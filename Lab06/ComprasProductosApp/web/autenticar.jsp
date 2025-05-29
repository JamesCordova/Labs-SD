<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
    // Configurar la página para no ser cacheada
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    try {
        // Obtener parámetros del formulario
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String recordar = request.getParameter("recordar");
        
        // Validar que los campos no estén vacíos
        if (usuario == null || password == null || 
            usuario.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=invalid");
            return;
        }
        
        // Limpiar los datos de entrada
        usuario = usuario.trim().toLowerCase();
        password = password.trim();
        
        // Base de datos simulada de usuarios (en producción usar base de datos real)
        Map<String, String> usuarios = new HashMap<>();
        usuarios.put("admin", "123456");
        usuarios.put("usuario", "password");
        usuarios.put("compras", "compras123");
        usuarios.put("demo", "demo");
        
        // Validar credenciales
        boolean credencialesValidas = false;
        String nombreCompleto = "";
        String rol = "";
        
        if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(password)) {
            credencialesValidas = true;
            
            // Asignar roles y nombres según el usuario
            switch (usuario) {
                case "admin":
                    nombreCompleto = "Administrador del Sistema";
                    rol = "ADMIN";
                    break;
                case "usuario":
                    nombreCompleto = "Usuario Regular";
                    rol = "USER";
                    break;
                case "compras":
                    nombreCompleto = "Gerente de Compras";
                    rol = "MANAGER";
                    break;
                case "demo":
                    nombreCompleto = "Usuario de Demostración";
                    rol = "DEMO";
                    break;
                default:
                    nombreCompleto = "Usuario";
                    rol = "USER";
            }
        }
        
        if (credencialesValidas) {
            // Crear sesión exitosa
            HttpSession sesion = request.getSession(true);
            
            // Configurar timeout de sesión (30 minutos)
            sesion.setMaxInactiveInterval(30 * 60);
            
            // Guardar información del usuario en la sesión
            sesion.setAttribute("usuario_logueado", usuario);
            sesion.setAttribute("nombre_completo", nombreCompleto);
            sesion.setAttribute("rol_usuario", rol);
            sesion.setAttribute("fecha_login", new Date());
            sesion.setAttribute("ip_usuario", request.getRemoteAddr());
            
            // Si marcó "recordar", crear cookie (opcional)
            if ("true".equals(recordar)) {
                Cookie cookieUsuario = new Cookie("usuario_recordado", usuario);
                cookieUsuario.setMaxAge(7 * 24 * 60 * 60); // 7 días
                cookieUsuario.setPath("/");
                response.addCookie(cookieUsuario);
            }
            
            // Registrar el login exitoso (en producción, guardar en log)
            System.out.println("LOGIN EXITOSO - Usuario: " + usuario + 
                             " | IP: " + request.getRemoteAddr() + 
                             " | Fecha: " + new Date());
            
            // Redirigir al sistema principal
            response.sendRedirect("index.jsp?login=success");
            
        } else {
            // Credenciales inválidas
            // Registrar intento fallido (en producción, implementar límite de intentos)
            System.out.println("LOGIN FALLIDO - Usuario: " + usuario + 
                             " | IP: " + request.getRemoteAddr() + 
                             " | Fecha: " + new Date());
            
            // Pequeña pausa para prevenir ataques de fuerza bruta
            Thread.sleep(1000);
            
            // Redirigir con error
            response.sendRedirect("login.jsp?error=invalid&usuario=" + 
                                java.net.URLEncoder.encode(usuario, "UTF-8"));
        }
        
    } catch (Exception e) {
        // Error inesperado
        System.err.println("Error en autenticación: " + e.getMessage());
        e.printStackTrace();
        
        response.sendRedirect("login.jsp?error=system");
    }
%>
