<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Configurar headers para no cachear
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    try {
        // Obtener información de la sesión antes de destruirla
        HttpSession sesion = request.getSession(false);
        String usuario = "";
        
        if (sesion != null) {
            usuario = (String) sesion.getAttribute("usuario_logueado");
            
            // Registrar el logout
            if (usuario != null) {
                System.out.println("LOGOUT - Usuario: " + usuario + 
                                 " | IP: " + request.getRemoteAddr() + 
                                 " | Fecha: " + new java.util.Date());
            }
            
            // Invalidar la sesión
            sesion.invalidate();
        }
        
        // Eliminar cookies de recordar usuario
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("usuario_recordado".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        
    } catch (Exception e) {
        System.err.println("Error en logout: " + e.getMessage());
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cerrando Sesión - Sistema de Compras</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
        }
        
        .logout-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 400px;
            width: 100%;
        }
        
        .logout-icon {
            font-size: 4em;
            margin-bottom: 20px;
            color: #667eea;
        }
        
        .logout-message {
            color: #333;
            margin-bottom: 30px;
        }
        
        .logout-message h2 {
            margin-bottom: 10px;
            color: #667eea;
        }
        
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
        }
        
        .countdown {
            color: #666;
            font-size: 14px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="logout-container">
        <div class="logout-icon">👋</div>
        <div class="logout-message">
            <h2>Sesión Cerrada</h2>
            <p>Ha salido del sistema exitosamente.</p>
            <p>¡Gracias por usar nuestro Sistema de Compras!</p>
        </div>
        
        <a href="login.jsp" class="btn-login">
            🔐 Iniciar Sesión Nuevamente
        </a>
        
        <div class="countdown">
            <p>Será redirigido automáticamente en <span id="countdown">5</span> segundos...</p>
        </div>
    </div>
    
    <script>
        // Countdown para redirección automática
        let tiempoRestante = 5;
        const countdownElement = document.getElementById('countdown');
        
        const intervalId = setInterval(function() {
            tiempoRestante--;
            countdownElement.textContent = tiempoRestante;
            
            if (tiempoRestante <= 0) {
                clearInterval(intervalId);
                window.location.href = 'login.jsp';
            }
        }, 1000);
        
        // Limpiar cualquier dato sensible del navegador
        if (typeof(Storage) !== "undefined") {
            // Limpiar localStorage si se usó
            localStorage.clear();
            // Limpiar sessionStorage
            sessionStorage.clear();
        }
        
        // Prevenir el botón "Atrás" del navegador
        history.pushState(null, document.title, location.href);
        window.addEventListener('popstate', function (event) {
            history.pushState(null, document.title, location.href);
        });
    </script>
</body>
</html>