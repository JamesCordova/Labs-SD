package com.compras.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filtro de seguridad para proteger páginas que requieren autenticación
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {
    "/index.jsp", 
    "/procesar_compra.jsp", 
    "/procesar_compra"
})
public class securityFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    
    public SecurityFilter() {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Configurar headers de seguridad
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Obtener la sesión sin crear una nueva
        HttpSession session = httpRequest.getSession(false);
        String usuarioLogueado = null;
        
        if (session != null) {
            usuarioLogueado = (String) session.getAttribute("usuario_logueado");
        }
        
        // Verificar si el usuario está autenticado
        if (usuarioLogueado == null || usuarioLogueado.trim().isEmpty()) {
            // Usuario no autenticado, redirigir al login
            String requestURI = httpRequest.getRequestURI();
            String contextPath = httpRequest.getContextPath();
            
            // Registrar intento de acceso no autorizado
            System.out.println("ACCESO NO AUTORIZADO - URI: " + requestURI + 
                             " | IP: " + httpRequest.getRemoteAddr() + 
                             " | User-Agent: " + httpRequest.getHeader("User-Agent") + 
                             " | Fecha: " + new java.util.Date());
            
            // Redirigir al login con parámetro de error
            httpResponse.sendRedirect(contextPath + "/login.jsp?error=required");
            return;
        }
        
        // Usuario autenticado, verificar validez de la sesión
        try {
            // Verificar timeout de sesión
            long lastAccessed = session.getLastAccessedTime();
            long currentTime = System.currentTimeMillis();
            int maxInactive = session.getMaxInactiveInterval() * 1000; // Convertir a milisegundos
            
            if ((currentTime - lastAccessed) > maxInactive) {
                // Sesión expirada
                session.invalidate();
                
                System.out.println("SESIÓN EXPIRADA - Usuario: " + usuarioLogueado + 
                                 " | IP: " + httpRequest.getRemoteAddr() + 
                                 " | Fecha: " + new java.util.Date());
                
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=session");
                return;
            }
            
            // Actualizar información de la sesión para auditoría
            session.setAttribute("ultimo_acceso", new java.util.Date());
            session.setAttribute("ip_actual", httpRequest.getRemoteAddr());
            
            // Registrar acceso exitoso (solo para páginas importantes)
            String uri = httpRequest.getRequestURI();
            if (uri.contains("procesar_compra")) {
                System.out.println("ACCESO AUTORIZADO - Usuario: " + usuarioLogueado + 
                                 " | Página: " + uri + 
                                 " | IP: " + httpRequest.getRemoteAddr() + 
                                 " | Fecha: " + new java.util.Date());
            }
            
        } catch (Exception e) {
            System.err.println("Error en SecurityFilter: " + e.getMessage());
            e.printStackTrace();
            
            // En caso de error, invalidar sesión por seguridad
            if (session != null) {
                session.invalidate();
            }
            
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=system");
            return;
        }
        
        // Todo OK, continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Limpiar recursos si es necesario
        this.filterConfig = null;
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        
        // Registrar inicialización del filtro
        System.out.println("SecurityFilter inicializado correctamente - " + new java.util.Date());
    }
}