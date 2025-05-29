<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login - Sistema de Compras</title>
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
        }
        
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            position: relative;
            overflow: hidden;
        }
        
        .login-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .login-header h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 2em;
        }
        
        .login-header p {
            color: #666;
            font-size: 0.9em;
        }
        
        .form-group {
            margin-bottom: 20px;
            position: relative;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #f8f9fa;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .btn-login {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
        }
        
        .btn-login:active {
            transform: translateY(0);
        }
        
        .error-message {
            background-color: #fee;
            color: #c33;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            border-left: 4px solid #c33;
            font-size: 14px;
        }
        
        .demo-credentials {
            background-color: #f0f7ff;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            border-left: 4px solid #667eea;
        }
        
        .demo-credentials h4 {
            color: #667eea;
            margin-bottom: 8px;
            font-size: 14px;
        }
        
        .demo-credentials p {
            color: #555;
            font-size: 12px;
            margin: 2px 0;
        }
        
        .remember-me {
            display: flex;
            align-items: center;
            margin-top: 15px;
        }
        
        .remember-me input[type="checkbox"] {
            width: auto;
            margin-right: 8px;
        }
        
        .remember-me label {
            margin-bottom: 0;
            font-size: 14px;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🔐 Iniciar Sesión</h1>
            <p>Sistema de Compras de Productos</p>
        </div>
        
        <!-- Mostrar mensaje de error si existe -->
        <%
            String error = request.getParameter("error");
            if (error != null) {
        %>
            <div class="error-message">
                <%
                    if ("invalid".equals(error)) {
                        out.print("❌ Usuario o contraseña incorrectos");
                    } else if ("required".equals(error)) {
                        out.print("⚠️ Por favor, inicie sesión para acceder");
                    } else if ("session".equals(error)) {
                        out.print("⏰ Su sesión ha expirado. Inicie sesión nuevamente");
                    } else {
                        out.print("❌ Error de autenticación");
                    }
                %>
            </div>
        <%
            }
        %>
        
        <form action="autenticar.jsp" method="post">
            <div class="form-group">
                <label for="usuario">👤 Usuario:</label>
                <input type="text" 
                       id="usuario" 
                       name="usuario" 
                       placeholder="Ingrese su usuario"
                       required 
                       autofocus
                       value="<%= request.getParameter("usuario") != null ? request.getParameter("usuario") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="password">🔑 Contraseña:</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="Ingrese su contraseña"
                       required>
            </div>
            
            <div class="remember-me">
                <input type="checkbox" id="recordar" name="recordar" value="true">
                <label for="recordar">Recordar mi sesión</label>
            </div>
            
            <button type="submit" class="btn-login">
                🚀 Iniciar Sesión
            </button>
        </form>
        
        <!-- Credenciales de demostración -->
        <div class="demo-credentials">
            <h4>📋 Credenciales de Prueba:</h4>
            <p><strong>Usuario:</strong> admin</p>
            <p><strong>Contraseña:</strong> 123456</p>
            <hr style="margin: 8px 0; border: none; border-top: 1px solid #ddd;">
            <p><strong>Usuario:</strong> usuario</p>
            <p><strong>Contraseña:</strong> password</p>
        </div>
    </div>
    
    <script>
        // Auto-focus en el campo de usuario al cargar la página
        document.addEventListener('DOMContentLoaded', function() {
            const usuarioInput = document.getElementById('usuario');
            if (usuarioInput && !usuarioInput.value) {
                usuarioInput.focus();
            }
        });
        
        // Validación del formulario
        document.querySelector('form').addEventListener('submit', function(e) {
            const usuario = document.getElementById('usuario').value.trim();
            const password = document.getElementById('password').value;
            
            if (!usuario) {
                alert('⚠️ Por favor ingrese su usuario');
                e.preventDefault();
                document.getElementById('usuario').focus();
                return;
            }
            
            if (!password) {
                alert('⚠️ Por favor ingrese su contraseña');
                e.preventDefault();
                document.getElementById('password').focus();
                return;
            }
            
            if (password.length < 3) {
                alert('⚠️ La contraseña debe tener al menos 3 caracteres');
                e.preventDefault();
                document.getElementById('password').focus();
                return;
            }
        });
    </script>
</body>
</html>