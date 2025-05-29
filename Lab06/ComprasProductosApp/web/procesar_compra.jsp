<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%
    // Verificar sesión antes de procesar
    HttpSession sesion = request.getSession(false);
    String usuarioLogueado = null;
    String nombreCompleto = null;
    String rolUsuario = null;
    
    if (sesion != null) {
        usuarioLogueado = (String) sesion.getAttribute("usuario_logueado");
        nombreCompleto = (String) sesion.getAttribute("nombre_completo");
        rolUsuario = (String) sesion.getAttribute("rol_usuario");
    }
    
    // Si no hay sesión válida, mostrar error
    if (usuarioLogueado == null) {
%>
<!DOCTYPE html>
<html>
<head>
    <title>Sesión Expirada</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 100px; background-color: #f5f5f5; }
        .error-container { background-color: #fee; border: 2px solid #f00; padding: 30px; margin: 20px auto; max-width: 500px; border-radius: 10px; }
        .btn { background-color: #667eea; color: white; padding: 12px 24px; border: none; border-radius: 6px; cursor: pointer; text-decoration: none; display: inline-block; margin: 10px; }
    </style>
</head>
<body>
    <div class="error-container">
        <h2>🔒 Sesión Expirada</h2>
        <p>Su sesión ha expirado. Por favor, inicie sesión nuevamente para continuar.</p>
        <a href="../login.jsp" class="btn">🔐 Iniciar Sesión</a>
    </div>
</body>
</html>
<%
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Factura de Compra - <%= nombreCompleto %></title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px; 
            background-color: #f5f5f5;
        }
        .factura { 
            max-width: 800px; 
            margin: 0 auto; 
            background: white; 
            border: 2px solid #333; 
            padding: 30px; 
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .header { 
            text-align: center; 
            margin-bottom: 30px; 
            border-bottom: 2px solid #eee;
            padding-bottom: 20px;
        }
        .header h1 {
            color: #2c3e50;
            margin: 0;
            font-size: 2.5em;
        }
        .header p {
            color: #7f8c8d;
            margin: 10px 0;
        }
        .user-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .user-details {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: rgba(255,255,255,0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            font-weight: bold;
        }
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin: 20px 0; 
            font-size: 14px;
        }
        th, td { 
            border: 1px solid #ddd; 
            padding: 12px 8px; 
            text-align: left; 
        }
        th { 
            background-color: #34495e; 
            color: white;
            font-weight: bold; 
            text-align: center;
        }
        .total-row { 
            background-color: #2ecc71; 
            color: white;
            font-weight: bold; 
            font-size: 16px;
        }
        .subtotal-cell {
            text-align: right;
            font-weight: bold;
        }
        .error { 
            color: #e74c3c; 
            font-weight: bold; 
            text-align: center; 
            margin: 20px;
            background-color: #fadbd8;
            padding: 20px;
            border-radius: 5px;
            border: 1px solid #e74c3c;
        }
        .footer { 
            text-align: center; 
            margin-top: 30px; 
            padding-top: 20px;
            border-top: 2px solid #eee;
            color: #666; 
        }
        .btn {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin: 5px;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        .btn:hover {
            background-color: #2980b9;
            transform: translateY(-1px);
        }
        .btn-print {
            background-color: #27ae60;
        }
        .btn-close {
            background-color: #e74c3c;
        }
        .btn-new {
            background-color: #f39c12;
        }
        .info-box {
            background-color: #ecf0f1;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
        }
        .transaction-id {
            background: #f8f9fa;
            border: 1px dashed #dee2e6;
            padding: 10px;
            border-radius: 5px;
            font-family: monospace;
            text-align: center;
            margin: 15px 0;
        }
        @media print {
            .btn, .footer button { display: none; }
            .factura { border: none; box-shadow: none; }
        }
    </style>
</head>
<body>
    <%
        try {
            // Generar ID único de transacción
            String transactionId = "TXN-" + System.currentTimeMillis() + "-" + usuarioLogueado.toUpperCase();
            
            // Obtener datos del formulario
            String[] productos = request.getParameterValues("producto[]");
            String[] preciosStr = request.getParameterValues("precio[]");
            String[] cantidadesStr = request.getParameterValues("cantidad[]");
            
            // Validar que los datos existan
            if (productos == null || preciosStr == null || cantidadesStr == null) {
                throw new Exception("Datos incompletos. Por favor, complete todos los campos.");
            }
            
            // Validar que todos los arrays tengan el mismo tamaño
            if (productos.length != preciosStr.length || preciosStr.length != cantidadesStr.length) {
                throw new Exception("Error en los datos enviados. Intente nuevamente.");
            }
            
            // Variables para el procesamiento
            double total = 0.0;
            DecimalFormat df = new DecimalFormat("#,##0.00");
            int totalProductos = 0;
            boolean hayErrores = false;
            String mensajeError = "";
            
            // Listas para almacenar productos válidos
            List<String> productosValidos = new ArrayList<>();
            List<Double> preciosValidos = new ArrayList<>();
            List<Integer> cantidadesValidas = new ArrayList<>();
            List<Double> subtotalesValidos = new ArrayList<>();
            
            // Procesar cada producto
            for (int i = 0; i < productos.length; i++) {
                try {
                    // Validar que el producto no esté vacío
                    if (productos[i] == null || productos[i].trim().isEmpty()) {
                        continue; // Saltar productos vacíos
                    }
                    
                    // Convertir precio y cantidad
                    double precio = Double.parseDouble(preciosStr[i]);
                    int cantidad = Integer.parseInt(cantidadesStr[i]);
                    
                    // Validar precio positivo
                    if (precio <= 0) {
                        mensajeError = "El precio debe ser mayor a cero para: " + productos[i];
                        hayErrores = true;
                        break;
                    }
                    
                    // VALIDACIÓN ESPECÍFICA REQUERIDA: cantidad negativa
                    if (cantidad < 0) {
                        mensajeError = "Lo siento, ingrese una cantidad positiva para: " + productos[i];
                        hayErrores = true;
                        break;
                    }
                    
                    // Si todo está bien, agregar a las listas
                    double subtotal = precio * cantidad;
                    
                    productosValidos.add(productos[i]);
                    preciosValidos.add(precio);
                    cantidadesValidas.add(cantidad);
                    subtotalesValidos.add(subtotal);
                    
                    total += subtotal;
                    totalProductos += cantidad;
                    
                } catch (NumberFormatException e) {
                    mensajeError = "Error en el formato de los números para: " + productos[i];
                    hayErrores = true;
                    break;
                }
            }
            
            // Si hay errores, mostrar mensaje de error
            if (hayErrores) {
    %>
                <div class="factura">
                    <div class="error">
                        <h2>❌ Error de Validación</h2>
                        <p><%= mensajeError %></p>
                        <br>
                        <button class="btn" onclick="history.back()">⬅️ Volver al Formulario</button>
                    </div>
                </div>
    <%
                return;
            }
            
            // Si no hay productos válidos
            if (productosValidos.isEmpty()) {
    %>
                <div class="factura">
                    <div class="error">
                        <h2>⚠️ Sin Productos</h2>
                        <p>No se encontraron productos válidos para procesar.</p>
                        <br>
                        <button class="btn" onclick="history.back()">⬅️ Volver al Formulario</button>
                    </div>
                </div>
    <%
                return;
            }
            
            // Registrar la transacción exitosa
            System.out.println("COMPRA PROCESADA - Usuario: " + usuarioLogueado + 
                             " | Productos: " + productosValidos.size() + 
                             " | Total: $" + df.format(total) + 
                             " | ID: " + transactionId + 
                             " | Fecha: " + new Date());
            
            // Generar la factura
    %>
            <div class="factura">
                <div class="header">
                    <h1>🧾 FACTURA DE COMPRA</h1>
                    <p>Sistema de Compras de Productos</p>
                    <p><strong>Fecha:</strong> <%= new java.util.Date() %></p>
                </div>
                
                <!-- Información del usuario -->
                <div class="user-info">
                    <div class="user-details">
                        <div class="user-avatar">
                            <%= usuarioLogueado.substring(0,1).toUpperCase() %>
                        </div>
                        <div>
                            <strong><%= nombreCompleto %></strong><br>
                            <small>Usuario: <%= usuarioLogueado %> | Rol: <%= rolUsuario %></small>
                        </div>
                    </div>
                    <div>
                        <strong>Transacción Autorizada</strong><br>
                        <small>✅ Procesada exitosamente</small>
                    </div>
                </div>
                
                <!-- ID de transacción -->
                <div class="transaction-id">
                    <strong>🔖 ID de Transacción:</strong> <%= transactionId %>
                </div>
                
                <div class="info-box">
                    <strong>📊 Resumen de la compra:</strong><br>
                    • Total de productos diferentes: <%= productosValidos.size() %><br>
                    • Cantidad total de artículos: <%= totalProductos %><br>
                    • Monto total: $<%= df.format(total) %><br>
                    • Procesado por: <%= nombreCompleto %>
                </div>
                
                <table>
                    <thead>
                        <tr>
                            <th style="width: 5%;">#</th>
                            <th style="width: 40%;">Producto</th>
                            <th style="width: 20%;">Precio Unitario</th>
                            <th style="width: 15%;">Cantidad</th>
                            <th style="width: 20%;">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            // Generar filas de productos
                            for (int i = 0; i < productosValidos.size(); i++) {
                                String producto = productosValidos.get(i);
                                double precio = preciosValidos.get(i);
                                int cantidad = cantidadesValidas.get(i);
                                double subtotal = subtotalesValidos.get(i);
                        %>
                        <tr>
                            <td style="text-align: center;"><%= (i + 1) %></td>
                            <td><%= producto %></td>
                            <td style="text-align: right;">$<%= df.format(precio) %></td>
                            <td style="text-align: center;"><%= cantidad %></td>
                            <td class="subtotal-cell">$<%= df.format(subtotal) %></td>
                        </tr>
                        <%
                            }
                        %>
                        
                        <!-- Fila de total -->
                        <tr class="total-row">
                            <td colspan="4" style="text-align: center;">
                                <strong>🏆 TOTAL A PAGAR</strong>
                            </td>
                            <td style="text-align: right; font-size: 18px;">
                                <strong>$<%= df.format(total) %></strong>
                            </td>
                        </tr>
                    </tbody>
                </table>
                
                <div class="footer">
                    <p><strong>🎉 ¡Gracias por su compra, <%= nombreCompleto %>!</strong></p>
                    <p><em>Conserve esta factura para sus registros</em></p>
                    <p><small>Factura generada el <%= new Date() %> por el Sistema de Compras</small></p>
                    
                    <div style="margin-top: 20px;">
                        <button class="btn btn-print" onclick="window.print()">
                            🖨️ Imprimir Factura
                        </button>
                        <button class="btn btn-close" onclick="window.close()">
                            ❌ Cerrar Ventana
                        </button>
                        <button class="btn btn-new" onclick="window.opener.location.reload(); window.close();">
                            🔄 Nueva Compra
                        </button>
                    </div>
                </div>
            </div>
            
    <%
        } catch (Exception e) {
            // Registrar el error
            System.err.println("ERROR EN COMPRA - Usuario: " + usuarioLogueado + 
                             " | Error: " + e.getMessage() + 
                             " | Fecha: " + new Date());
    %>
            <div class="factura">
                <div class="error">
                    <h2>💥 Error Inesperado</h2>
                    <p><strong>Ha ocurrido un error al procesar la compra:</strong></p>
                    <p><%= e.getMessage() %></p>
                    <p><small>Error reportado automáticamente al administrador del sistema.</small></p>
                    <br>
                    <button class="btn" onclick="history.back()">⬅️ Volver al Formulario</button>
                    <button class="btn" onclick="window.opener.location.href='index.jsp'; window.close();">🏠 Ir al Inicio</button>
                </div>
            </div>
    <%
        }
    %>
</body>
</html>