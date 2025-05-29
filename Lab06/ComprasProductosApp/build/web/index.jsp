<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Verificar si el usuario está logueado
    HttpSession sesion = request.getSession(false);
    String usuarioLogueado = null;
    String nombreCompleto = null;
    String rolUsuario = null;
    
    if (sesion != null) {
        usuarioLogueado = (String) sesion.getAttribute("usuario_logueado");
        nombreCompleto = (String) sesion.getAttribute("nombre_completo");
        rolUsuario = (String) sesion.getAttribute("rol_usuario");
    }
    
    // Si no hay sesión válida, redirigir al login
    if (usuarioLogueado == null) {
        response.sendRedirect("login.jsp?error=required");
        return;
    }
    
    // Verificar mensaje de login exitoso
    String loginSuccess = request.getParameter("login");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sistema de Compras - Bienvenido <%= nombreCompleto %></title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0; 
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
        }
        
        .header-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .user-info {
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
        }
        
        .user-details h3 {
            margin: 0;
            font-size: 16px;
        }
        
        .user-details p {
            margin: 0;
            font-size: 12px;
            opacity: 0.9;
        }
        
        .logout-btn {
            background: rgba(255,255,255,0.2);
            color: white;
            border: 1px solid rgba(255,255,255,0.3);
            padding: 8px 16px;
            border-radius: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            font-size: 14px;
        }
        
        .logout-btn:hover {
            background: rgba(255,255,255,0.3);
            transform: translateY(-1px);
        }
        
        .container { 
            max-width: 900px; 
            margin: 20px auto; 
            background: white; 
            padding: 30px; 
            border-radius: 15px; 
            box-shadow: 0 10px 30px rgba(0,0,0,0.1); 
        }
        
        .welcome-message {
            background: linear-gradient(135deg, #e8f5e8 0%, #d4f1f4 100%);
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            border-left: 5px solid #4CAF50;
        }
        
        .header { 
            text-align: center; 
            color: #333; 
            margin-bottom: 30px; 
        }
        
        .header h1 {
            color: #667eea;
            margin-bottom: 10px;
        }
        
        .producto-row { 
            display: flex; 
            gap: 10px; 
            margin-bottom: 15px; 
            align-items: center; 
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #e9ecef;
        }
        
        .producto-row input, .producto-row select { 
            padding: 10px; 
            border: 2px solid #e1e5e9; 
            border-radius: 6px; 
            font-size: 14px;
            transition: border-color 0.3s ease;
        }
        
        .producto-row input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .btn { 
            background-color: #4CAF50; 
            color: white; 
            padding: 12px 24px; 
            border: none; 
            border-radius: 6px; 
            cursor: pointer; 
            font-size: 16px; 
            transition: all 0.3s ease;
        }
        
        .btn:hover { 
            background-color: #45a049; 
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
        }
        
        .btn-add { 
            background-color: #2196F3; 
            margin-left: 10px; 
        }
        
        .btn-add:hover {
            background-color: #1976D2;
        }
        
        .btn-remove { 
            background-color: #f44336; 
        }
        
        .btn-remove:hover {
            background-color: #d32f2f;
        }
        
        .btn-secondary {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
        }
        
        .btn-secondary:hover {
            background: linear-gradient(135deg, #495057 0%, #343a40 100%);
        }
        
        .error { 
            color: red; 
            font-weight: bold; 
        }
        
        .total-section { 
            background: linear-gradient(135deg, #e8f5e8 0%, #d4f1f4 100%); 
            padding: 20px; 
            border-radius: 10px; 
            margin-top: 20px; 
            border: 1px solid #4CAF50;
        }
        
        .stats-bar {
            display: flex;
            justify-content: space-around;
            background: #f8f9fa;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        
        .stat-item {
            text-align: center;
        }
        
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
            display: block;
        }
        
        .stat-label {
            font-size: 12px;
            color: #666;
            text-transform: uppercase;
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            padding: 12px 20px;
            border-radius: 6px;
            border: 1px solid #c3e6cb;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
    </style>
    <script>
        let contadorProductos = 1;
        
        function agregarProducto() {
            contadorProductos++;
            const container = document.getElementById('productos-container');
            const nuevaFila = document.createElement('div');
            nuevaFila.className = 'producto-row';
            nuevaFila.id = 'producto-' + contadorProductos;
            
            nuevaFila.innerHTML = `
                <input type="text" name="producto[]" placeholder="Nombre del producto" required style="flex: 2;">
                <input type="number" name="precio[]" placeholder="Precio $" step="0.01" min="0.01" required style="flex: 1;">
                <input type="number" name="cantidad[]" placeholder="Cantidad" min="0" required style="flex: 1;">
                <button type="button" class="btn btn-remove" onclick="eliminarProducto(${contadorProductos})">🗑️ Eliminar</button>
            `;
            
            container.appendChild(nuevaFila);
            
            // Actualizar estadísticas
            actualizarEstadisticas();
            
            // Enfocar el primer input del nuevo producto
            nuevaFila.querySelector('input[name="producto[]"]').focus();
        }
        
        function eliminarProducto(id) {
            const elemento = document.getElementById('producto-' + id);
            if (elemento && document.querySelectorAll('.producto-row').length > 1) {
                elemento.remove();
                actualizarEstadisticas();
            } else {
                alert('⚠️ Debe mantener al menos un producto');
            }
        }
        
        function actualizarEstadisticas() {
            const productos = document.querySelectorAll('.producto-row').length;
            document.querySelector('#stat-productos .stat-number').textContent = productos;
        }
        
        function validarFormulario() {
            const productos = document.getElementsByName('producto[]');
            const precios = document.getElementsByName('precio[]');
            const cantidades = document.getElementsByName('cantidad[]');
            
            for (let i = 0; i < productos.length; i++) {
                if (!productos[i].value.trim()) {
                    alert('⚠️ Por favor ingrese el nombre del producto en la fila ' + (i + 1));
                    productos[i].focus();
                    return false;
                }
                if (precios[i].value <= 0) {
                    alert('⚠️ El precio debe ser mayor a 0 en la fila ' + (i + 1));
                    precios[i].focus();
                    return false;
                }
                if (cantidades[i].value < 0) {
                    alert('⚠️ Lo siento, ingrese una cantidad positiva en la fila ' + (i + 1));
                    cantidades[i].focus();
                    return false;
                }
            }
            return true;
        }
        
        function nuevaCompra() {
            if (confirm('¿Está seguro que desea limpiar el formulario y hacer una nueva compra?')) {
                window.location.href = window.location.pathname;
            }
        }
        
        // Actualizar reloj en tiempo real
        function actualizarReloj() {
            const ahora = new Date();
            const opciones = { 
                weekday: 'long', 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            };
            document.getElementById('reloj').textContent = ahora.toLocaleDateString('es-ES', opciones);
        }
        
        // Confirmar logout
        function confirmarLogout() {
            return confirm('¿Está seguro que desea cerrar sesión?');
        }
        
        // Inicializar página
        window.onload = function() {
            actualizarEstadisticas();
            actualizarReloj();
            setInterval(actualizarReloj, 1000);
            
            // Ocultar mensaje de bienvenida después de 5 segundos
            const welcomeAlert = document.querySelector('.alert-success');
            if (welcomeAlert) {
                setTimeout(() => {
                    welcomeAlert.style.transition = 'opacity 0.5s ease';
                    welcomeAlert.style.opacity = '0';
                    setTimeout(() => welcomeAlert.remove(), 500);
                }, 5000);
            }
        };
                
    </script>
</head>
<body>
    <!-- Barra superior con información del usuario -->
    <div class="header-bar">
        <div class="user-info">
            <div class="user-avatar">
                <%= usuarioLogueado.substring(0,1).toUpperCase() %>
            </div>
            <div class="user-details">
                <h3>👋 Bienvenido, <%= nombreCompleto %></h3>
                <p>Rol: <%= rolUsuario %> | <span id="reloj"></span></p>
            </div>
        </div>
        <a href="logout.jsp" class="logout-btn" onclick="return confirmarLogout()">
            🚪 Cerrar Sesión
        </a>
    </div>
    
    <div class="container">
        <!-- Mensaje de login exitoso -->
        <% if ("success".equals(loginSuccess)) { %>
            <div class="alert-success">
                <span>✅</span>
                <div>
                    <strong>¡Bienvenido al sistema!</strong><br>
                    <small>Sesión iniciada correctamente como <%= nombreCompleto %></small>
                </div>
            </div>
        <% } %>
        
        <div class="header">
            <h1>🛒 Sistema de Compras de Productos</h1>
            <p>Gestione sus compras de manera eficiente</p>
        </div>
        
        <!-- Barra de estadísticas -->
        <div class="stats-bar">
            <div class="stat-item" id="stat-productos">
                <span class="stat-number">1</span>
                <span class="stat-label">Productos</span>
            </div>
            <div class="stat-item">
                <span class="stat-number"><%= usuarioLogueado %></span>
                <span class="stat-label">Usuario Activo</span>
            </div>
            <div class="stat-item">
                <span class="stat-number"><%= rolUsuario %></span>
                <span class="stat-label">Nivel de Acceso</span>
            </div>
        </div>
        
        <form action="procesar_compra.jsp" method="post" onsubmit="return validarFormulario()" target="_blank">
            <!-- Campo oculto para mantener la sesión -->
            <input type="hidden" name="usuario_sesion" value="<%= usuarioLogueado %>">
            
            <div id="productos-container">
                <div class="producto-row" id="producto-1">
                    <input type="text" name="producto[]" placeholder="Nombre del producto" required style="flex: 2;">
                    <input type="number" name="precio[]" placeholder="Precio $" step="0.01" min="0.01" required style="flex: 1;">
                    <input type="number" name="cantidad[]" placeholder="Cantidad" min="0" required style="flex: 1;">
                    <button type="button" class="btn btn-add" onclick="agregarProducto()">➕ Agregar Producto</button>
                </div>
            </div>
            
            <div style="text-align: center; margin-top: 30px;">
                <button type="submit" class="btn" style="font-size: 18px; padding: 15px 30px;">
                    💰 Calcular Total de Compra
                </button>
                <button type="button" class="btn btn-secondary" onclick="nuevaCompra()" style="font-size: 16px; padding: 12px 24px; margin-left: 15px;">
                    🔄 Nueva Compra
                </button>
            </div>
        </form>
        
        <div class="total-section">
            <h3>📋 Instrucciones:</h3>
            <ul style="margin: 10px 0; padding-left: 20px;">
                <li>Ingrese el nombre, precio y cantidad de cada producto</li>
                <li>Use el botón "Agregar Producto" para añadir más items</li>
                <li>Use el botón "Nueva Compra" para limpiar el formulario</li>
                <li>El sistema validará automáticamente los datos ingresados</li>
                <li>La factura se abrirá en una nueva ventana</li>
            </ul>
            
            <div style="margin-top: 20px; padding-top: 15px; border-top: 1px solid #ddd; color: #666; font-size: 14px;">
                <p><strong>💡 Nota:</strong> Su sesión permanecerá activa por 30 minutos de inactividad.</p>
                <p><strong>🔒 Seguridad:</strong> No olvide cerrar sesión al terminar.</p>
            </div>
        </div>
    </div>
</body>
</html>