package com.compras.productos;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "ComprasProductos")
public class comprasProductos {

    @WebMethod(operationName = "calcularCompra")
    public String calcularCompra(
            @WebParam(name = "productos") String[] productos,
            @WebParam(name = "precios") double[] precios,
            @WebParam(name = "cantidades") int[] cantidades) {
        
        try {
            // Validar que todos los arrays tengan el mismo tamaño
            if (productos.length != precios.length || precios.length != cantidades.length) {
                return "ERROR: Los datos de productos no coinciden";
            }
            
            double total = 0.0;
            StringBuilder factura = new StringBuilder();
            factura.append("<html><head><title>Factura de Compra</title></head><body>");
            factura.append("<h2>FACTURA DE COMPRA</h2>");
            factura.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            factura.append("<tr><th>Producto</th><th>Precio Unitario</th><th>Cantidad</th><th>Subtotal</th></tr>");
            
            // Procesar cada producto
            for (int i = 0; i < productos.length; i++) {
                // Validar cantidad positiva
                if (cantidades[i] < 0) {
                    return "ERROR: Lo siento, ingrese una cantidad positiva para " + productos[i];
                }
                
                double subtotal = precios[i] * cantidades[i];
                total += subtotal;
                
                factura.append("<tr>");
                factura.append("<td>").append(productos[i]).append("</td>");
                factura.append("<td>$").append(String.format("%.2f", precios[i])).append("</td>");
                factura.append("<td>").append(cantidades[i]).append("</td>");
                factura.append("<td>$").append(String.format("%.2f", subtotal)).append("</td>");
                factura.append("</tr>");
            }
            
            factura.append("<tr style='background-color: #f0f0f0; font-weight: bold;'>");
            factura.append("<td colspan='3'>TOTAL A PAGAR:</td>");
            factura.append("<td>$").append(String.format("%.2f", total)).append("</td>");
            factura.append("</tr>");
            factura.append("</table>");
            factura.append("<br><p><strong>¡Gracias por su compra!</strong></p>");
            factura.append("</body></html>");
            
            return factura.toString();
            
        } catch (Exception e) {
            return "ERROR: Ocurrió un error al procesar la compra: " + e.getMessage();
        }
    }
    
    @WebMethod(operationName = "validarProducto")
    public boolean validarProducto(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "precio") double precio,
            @WebParam(name = "cantidad") int cantidad) {
        
        return nombre != null && !nombre.trim().isEmpty() && 
               precio > 0 && cantidad >= 0;
    }
}