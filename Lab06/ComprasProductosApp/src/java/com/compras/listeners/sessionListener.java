package com.compras.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

/**
 * Listener para monitorear sesiones de usuario
 */
public class sessionListener implements HttpSessionListener {
    
    // Mapa para mantener estadísticas de sesiones
    private static final ConcurrentHashMap<String, Date> sessionMap = new ConcurrentHashMap<>();
    private static int sessionCount = 0;
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        sessionCount++;
        String sessionId = se.getSession().getId();
        Date createdTime = new Date();
        
        sessionMap.put(sessionId, createdTime);
        
        // Configurar timeout de sesión (30 minutos)
        se.getSession().setMaxInactiveInterval(30 * 60);
        
        System.out.println("SESIÓN CREADA - ID: " + sessionId + 
                         " | Total activas: " + sessionCount + 
                         " | Fecha: " + createdTime);
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sessionCount--;
        String sessionId = se.getSession().getId();
        Date destroyedTime = new Date();
        
        // Obtener información del usuario si está disponible
        String usuario = null;
        try {
            usuario = (String) se.getSession().getAttribute("usuario_logueado");
        } catch (Exception e) {
            // Sesión ya invalidada, ignorar
        }
        
        // Remover del mapa
        Date createdTime = sessionMap.remove(sessionId);
        
        // Calcular duración de la sesión
        long duration = 0;
        if (createdTime != null) {
            duration = (destroyedTime.getTime() - createdTime.getTime()) / 1000; // en segundos
        }
        
        System.out.println("SESIÓN DESTRUIDA - ID: " + sessionId + 
                         (usuario != null ? " | Usuario: " + usuario : "") +
                         " | Duración: " + duration + " segundos" +
                         " | Total activas: " + sessionCount + 
                         " | Fecha: " + destroyedTime);
    }
    
    /**
     * Método estático para obtener el número de sesiones activas
     */
    public static int getActiveSessionCount() {
        return sessionCount;
    }
    
    /**
     * Método estático para obtener información de todas las sesiones
     */
    public static ConcurrentHashMap<String, Date> getSessionMap() {
        return new ConcurrentHashMap<>(sessionMap);
    }
}