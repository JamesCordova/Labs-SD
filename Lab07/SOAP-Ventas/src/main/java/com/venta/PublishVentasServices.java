package com.venta;

import javax.xml.ws.Endpoint;
import com.ventas.services.ProductoServiceImpl;
import com.ventas.services.VentaServiceImpl;

public class PublishVentasServices {
    public static void main(String[] args) {
        System.out.println("🛒 === SISTEMA DE VENTAS ONLINE SOAP === 🛒");
        System.out.println("Iniciando servicios web...\n");
        
        try {
            // URLs de los servicios
            String urlProductos = "http://localhost:8080/ventas/productos";
            String urlVentas = "http://localhost:8080/ventas/ventas";
            
            // Publicar servicio de productos
            Endpoint.publish(urlProductos, new ProductoServiceImpl());
            System.out.println("✅ Servicio de Productos ACTIVO");
            System.out.println("   📍 URL: " + urlProductos);
            System.out.println("   📄 WSDL: " + urlProductos + "?wsdl\n");
            
            // Publicar servicio de ventas
            Endpoint.publish(urlVentas, new VentaServiceImpl());
            System.out.println("✅ Servicio de Ventas ACTIVO");
            System.out.println("   📍 URL: " + urlVentas);
            System.out.println("   📄 WSDL: " + urlVentas + "?wsdl\n");
            
            System.out.println("🚀 SISTEMA LISTO PARA RECIBIR PETICIONES");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Funcionalidades disponibles:");
            System.out.println("📦 Gestión de productos (CRUD completo)");
            System.out.println("🛒 Gestión de ventas (crear, confirmar, entregar)");
            System.out.println("📊 Consultas y reportes de ventas");
            System.out.println("💰 Control de inventario automático");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("\n⏹️ Presiona Ctrl+C para detener los servicios...");
            
            // Mantener el servidor corriendo
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar servicios: " + e.getMessage());
            e.printStackTrace();
        }
    }
}