package com.compras.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filtro para asegurar codificación UTF-8 en todas las peticiones
 */
public class characterEncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    private boolean forceEncoding = true;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null) {
            encoding = encodingParam;
        }
        
        String forceParam = filterConfig.getInitParameter("forceEncoding");
        if (forceParam != null) {
            forceEncoding = Boolean.parseBoolean(forceParam);
        }
        
        System.out.println("CharacterEncodingFilter inicializado - Encoding: " + encoding + 
                         ", Force: " + forceEncoding);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        // Establecer encoding para request
        if (forceEncoding || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        
        // Establecer encoding para response
        if (forceEncoding || response.getCharacterEncoding() == null) {
            response.setCharacterEncoding(encoding);
        }
        
        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Limpiar recursos si es necesario
    }
}