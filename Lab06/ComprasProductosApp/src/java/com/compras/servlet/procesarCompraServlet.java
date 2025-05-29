package com.compras.servlet;

import com.compras.productos.ComprasProductos;
import com.compras.productos.ComprasProductos_Service;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcesarCompraServlet", urlPatterns = {"/procesar_compra"})
public class procesarCompraServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            // Obtener datos del formulario
            String[] productos = request.getParameterValues("producto[]");
            String[] preciosStr = request.getParameterValues("precio[]");
            String[] cantidadesStr = request.getParameterValues("cantidad[]");
            
            if (productos == null || preciosStr == null || cantidadesStr == null) {
                out.println("<html><body><h2>Error: Datos incompletos</h2>");
                out.println("<a href='index.jsp'>Volver</a></body></html>");
                return;
            }
            
            // Convertir arrays
            List<String> productosLista = new ArrayList<>();
            List<Double> preciosLista = new ArrayList<>();
            List<Integer> cantidadesLista = new ArrayList<>();
            
            for (int i = 0; i < productos.length; i++) {
                if (productos[i] != null && !productos[i].trim().isEmpty()) {
                    try {
                        double precio = Double.parseDouble(preciosStr[i]);
                        int cantidad = Integer.parseInt(cantidadesStr[i]);
                        
                        // Validación de cantidad negativa
                        if (cantidad < 0) {
                            out.println("<html><body>");
                            out.println("<div style='color: red; font-weight: bold; text-align: center; margin: 50px;'>");
                            out.println("<h2>Error de Validación</h2>");
                            out.println("<p>Lo siento, ingrese una cantidad positiva para: " + productos[i] + "</p>");
                            out.println("<br><a href='javascript:history.back()'>Volver al formulario</a>");
                            out.println("</div></body></html>");
                            return;
                        }
                        
                        productosLista.add(productos[i]);
                        preciosLista.add(precio);
                        cantidadesLista.add(cantidad);
                        
                    } catch (NumberFormatException e) {
                        out.println("<html><body><h2>Error: Formato de número inválido</h2>");
                        out.println("<a href='index.jsp'>Volver</a></body></html>");
                        return;
                    }
                }
            }
            
            // Calcular totales y generar factura
            double total = 0.0;
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Factura de Compra</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println(".factura { max-width: 600px; margin: 0 auto; border: 2px solid #333; padding: 20px; }");
            out.println(".header { text-align: center; margin-bottom: 30px; }");
            out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
            out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; font-weight: bold; }");
            out.println(".total-row { background-color: #e8f5e8; font-weight: bold; }");
            out.println(".footer { text-align: center; margin-top: 30px; color: #666; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='factura'>");
            out.println("<div class='header'>");
            out.println("<h1>🧾 FACTURA DE COMPRA</h1>");
            out.println("<p>Sistema de Compras de Productos</p>");
            out.println("</div>");
            
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Producto</th>");
            out.println("<th>Precio Unitario</th>");
            out.println("<th>Cantidad</th>");
            out.println("<th>Subtotal</th>");
            out.println("</tr>");
            
            // Generar filas de productos
            for (int i = 0; i < productosLista.size(); i++) {
                String producto = productosLista.get(i);
                double precio = preciosLista.get(i);
                int cantidad = cantidadesLista.get(i);
                double subtotal = precio * cantidad;
                total += subtotal;
                
                out.println("<tr>");
                out.println("<td>" + producto + "</td>");
                out.println("<td>$" + String.format("%.2f", precio) + "</td>");
                out.println("<td>" + cantidad + "</td>");
                out.println("<td>$" + String.format("%.2f", subtotal) + "</td>");
                out.println("</tr>");
            }
            
            // Fila de total
            out.println("<tr class='total-row'>");
            out.println("<td colspan='3'><strong>TOTAL A PAGAR:</strong></td>");
            out.println("<td><strong>$" + String.format("%.2f", total) + "</strong></td>");
            out.println("</tr>");
            
            out.println("</table>");
            
            out.println("<div class='footer'>");
            out.println("<p><strong>¡Gracias por su compra!</strong></p>");
            out.println("<p><small>Fecha: " + new java.util.Date() + "</small></p>");
            out.println("<hr>");
            out.println("<button onclick='window.print()'>🖨️ Imprimir Factura</button>");
            out.println("<button onclick='window.close()' style='margin-left: 10px;'>❌ Cerrar</button>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            response.getWriter().println("<html><body><h2>Error: " + e.getMessage() + "</h2>");
            response.getWriter().println("<a href='index.jsp'>Volver</a></body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}