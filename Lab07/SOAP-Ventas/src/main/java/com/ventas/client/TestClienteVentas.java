package com.ventas.client;

import java.math.BigDecimal;
import java.util.Arrays;

public class TestClienteVentas {
    public static void main(String[] args) {
        System.out.println("🧪 === CLIENTE DE PRUEBA PARA SERVICIOS SOAP ===\n");

        // Instrucciones para probar con herramientas externas
        mostrarEjemplosSOAP();

        // En una implementación real, aquí iría el código del cliente generado
        System.out.println("💡 Para generar un cliente automático en NetBeans:");
        System.out.println("   1. Click derecho en proyecto → New → Web Service Client");
        System.out.println("   2. Usar WSDL: http://localhost:8080/ventas/productos?wsdl");
        System.out.println("   3. NetBeans generará todas las clases necesarias");
    }

    private static void mostrarEjemplosSOAP() {
        System.out.println("📋 EJEMPLOS DE REQUESTS SOAP:\n");

        System.out.println("1️⃣ LISTAR PRODUCTOS:");
        System.out.println(
            "POST http://localhost:8080/ventas/productos\n" +
            "Content-Type: text/xml; charset=utf-8\n" +
            "SOAPAction: \"\"\n\n" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Body>\n" +
            "        <listarProductos xmlns=\"http://services.ventas.com/\" />\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"
        );

        System.out.println("\n2️⃣ BUSCAR POR CATEGORÍA:");
        System.out.println(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Body>\n" +
            "        <buscarPorCategoria xmlns=\"http://services.ventas.com/\">\n" +
            "            <arg0>ELECTRONICA</arg0>\n" +
            "        </buscarPorCategoria>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"
        );

        System.out.println("\n3️⃣ CREAR VENTA:");
        System.out.println(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Body>\n" +
            "        <crearVenta xmlns=\"http://services.ventas.com/\">\n" +
            "            <arg0>Juan Pérez</arg0>\n" +
            "            <arg1>juan@email.com</arg1>\n" +
            "            <arg2>\n" +
            "                <productoId>1</productoId>\n" +
            "                <cantidad>2</cantidad>\n" +
            "            </arg2>\n" +
            "        </crearVenta>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"
        );
    }
}
