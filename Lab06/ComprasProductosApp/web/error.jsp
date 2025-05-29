<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@page import="java.util.Date"%>
<%
    // Obtener información del error
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    
    // Si no hay código de estado, usar el de la página
    if (statusCode == null) {
        statusCode = response.getStatus();
    }
    
    // Generar ID único de error para seguimiento
    String errorId = "ERR-" + System.currentTimeMillis();
    
    // Registrar el error en el log del servidor
    String logMessage = "ERROR " + statusCode + " - ID: " + errorId + 
                       " | URI: " + requestUri + 
                       " | IP: " + request.getRemoteAddr() + 
                       " | Fecha: " + new Date();
    
    if (throwable != null) {
        logMessage += " | Excepción: " + throwable.getClass().getSimpleName() + 
                     " - " + throwable.getMessage();
    }
    
    System.err.println(logMessage);
    
    // Información del usuario si está logueado
    String usuarioLogueado = null;
    HttpSession sesion = request.getSession(false);
    if (sesion != null) {
        usuarioLogueado = (String) sesion.getAttribute("usuario_logueado");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Error <%= statusCode %> - Sistema de Compras</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body { 
            font-family: 'Arial', sans-serif; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #333;
        }
        
        .error-container { 
            background-color: white; 
            border-radius: 15px;
            padding: 40px; 
            margin: 20px; 
            max-width: 600px; 
            width: 100%;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        
        .error-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(90deg, #e74c3c 0%, #c0392b 100%);
        }
        
        .error-icon {
            font-size: 4em;
            margin-bottom: 20px;
            color: #e74c3c;
        }
        
        .error-title {
            color: #e74c3c;
            margin-bottom: 15px;
            font-size: 2.5em;
            font-weight: bold;
        }
        
        .error-description {
            color: #666;
            margin-bottom: 30px;
            font-size: 1.1em;
            line-height: 1.6;
        }
        
        .error-details {
            background-color: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
        }
        
        .error-details h4 {
            color: #495057;
            margin-bottom: 10px;
            font-size: 1.1em;
        }
        
        .error-details p {
            color: #6c757d;
            font-size: 0.9em;
            margin: 5px 0;
            word-break: break-all;
        }
        
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
        }
        
        .btn-secondary {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
        }
        
        .btn-home {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
        }
        
        .user-info {
            background: #e3f2fd;
            border-radius: 6px;
            padding: 10px;
            margin: 15px 0;
            font-size: 0.9em;
            color: #1565c0;
        }
        
        .error-id {
            font-family: monospace;
            background: #f8f9fa;
            padding: 8px;
            border-radius: 4px;
            border: 1px dashed #dee2e6;
            margin: 10px 0;
        }
        
        .help-text {
            color: #666;
            font-size: 0.9em;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <%
            String errorIcon = "❌";
            String errorTitle = "Error " + statusCode;
            String errorDesc = "Ha ocurrido un error inesperado";
            
            // Personalizar según el tipo de error
            switch (statusCode) {
                case 404:
                    errorIcon = "🔍";
                    errorTitle = "Página No Encontrada";
                    errorDesc = "La página que está buscando no existe o ha sido movida.";
                    break;
                case 403:
                    errorIcon = "🚫";
                    errorTitle = "Acceso Denegado";
                    errorDesc = "No tiene permisos para acceder a este recurso.";
                    break;
                case 500:
                    errorIcon = "⚙️";
                    errorTitle = "Error del Servidor";
                    errorDesc = "Ha ocurrido un error interno en el servidor.";
                    break;
                case 503:
                    errorIcon = "🔧";
                    errorTitle = "Servicio No Disponible";
                    errorDesc = "El servicio está temporalmente no disponible.";
                    break;
            }
        %>
        
        <div class="error-icon"><%= errorIcon %></div>
        <h2 class="error-title"><%= errorTitle %></h2>
        <p class="error-description"><%= errorDesc %></p>
        
        <% if (usuarioLogueado != null) { %>
            <div class="user-info">
                👤 Usuario conectado: <strong><%= usuarioLogueado %></strong>
            </div>
        <% } %>
        
        <div class="error-details">
            <h4>📋 Detalles del Error:</h4>
            <p><strong>Código:</strong> <%= statusCode %></p>
            <% if (requestUri != null) { %>
                <p><strong>URL solicitada:</strong> <%= requestUri %></p>
            <% } %>
            <p><strong>Fecha y hora:</strong> <%= new Date() %></p>
            <p><strong>IP del cliente:</strong> <%= request.getRemoteAddr() %></p>
            
            <div class="error-id">
                <strong>🔖 ID de Error:</strong> <%= errorId %>
            </div>
            
            <% if (throwable != null && exception != null) { %>
                <p><strong>Mensaje técnico:</strong> <%= exception.getMessage() != null ? exception.getMessage() : "Error interno" %></p>
            <% } else if (errorMessage != null) { %>
                <p><strong>Mensaje:</strong> <%= errorMessage %></p>
            <% } %>
        </div>
        
        <div style="margin-top: 30px;">
            <% if (usuarioLogueado != null) { %>
                <a href="index.jsp" class="btn btn-home">🏠 Ir al Inicio</a>
            <% } else { %>
                <a href="login.jsp" class="btn btn-home">🔐 Iniciar Sesión</a>
            <% } %>
            
            <button class="btn btn-secondary" onclick="history.back()">
                ⬅️ Página Anterior
            </button>
            
            <button class="btn" onclick="window.location.reload()">
                🔄 Reintentar
            </button>
        </div>
        
        <div class="help-text">
            <p><strong>💡 ¿Necesita ayuda?</strong></p>
            <p>Si el problema persiste, contacte al administrador del sistema proporcionando el <strong>ID de Error</strong> mostrado arriba.</p>
        </div>
    </div>
    
    <script>
        // Auto-recargar la página después de cierto tiempo para errores temporales
        if (<%= statusCode %> === 503) {
            setTimeout(function() {
                if (confirm('¿Desea reintentar automáticamente?')) {
                    window.location.reload();
                }
            }, 30000); // 30 segundos
        }
        
        // Copiar ID de error al portapapeles
        function copiarErrorId() {
            const errorId = '<%= errorId %>';
            if (navigator.clipboard) {
                navigator.clipboard.writeText(errorId).then(function() {
                    alert('ID de error copiado al portapapeles');
                });
            }
        }
        
        // Agregar funcionalidad de doble clic para copiar ID
        document.querySelector('.error-id').addEventListener('dblclick', copiarErrorId);
    </script>
</body>
</html>