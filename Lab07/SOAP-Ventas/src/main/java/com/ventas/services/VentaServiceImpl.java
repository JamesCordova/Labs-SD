package com.ventas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.jws.WebService;
import com.ventas.model.Venta;
import com.ventas.model.ItemVenta;
import com.ventas.model.Producto;

@WebService(endpointInterface = "com.ventas.services.VentaService")
public class VentaServiceImpl implements VentaService {
    
    private static List<Venta> ventas = new ArrayList<>();
    private static AtomicLong contadorId = new AtomicLong(1);
    private ProductoServiceImpl productoService = new ProductoServiceImpl();
    
    @Override
    public Long crearVenta(String cliente, String emailCliente, List<ItemVenta> items) {
        try {
            System.out.println("🛒 Creando nueva venta para: " + cliente);
            
            // Validar que los items no estén vacíos
            if (items == null || items.isEmpty()) {
                System.err.println("❌ No se pueden crear ventas sin items");
                return null;
            }
            
            // Validar stock disponible y completar información de items
            for (ItemVenta item : items) {
                Producto producto = productoService.buscarProductoPorId(item.getProductoId());
                if (producto == null) {
                    System.err.println("❌ Producto no encontrado: " + item.getProductoId());
                    return null;
                }
                if (producto.getStock() < item.getCantidad()) {
                    System.err.println("❌ Stock insuficiente para: " + producto.getNombre());
                    return null;
                }
                
                // Completar información del item
                item.setNombreProducto(producto.getNombre());
                item.setPrecioUnitario(producto.getPrecio());
            }
            
            Long ventaId = contadorId.getAndIncrement();
            Venta venta = new Venta(ventaId, cliente, emailCliente, items);
            ventas.add(venta);
            
            System.out.println("✅ Venta creada exitosamente con ID: " + ventaId);
            return ventaId;
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear venta: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Venta consultarVenta(Long ventaId) {
        System.out.println("🔍 Consultando venta ID: " + ventaId);
        return ventas.stream()
                .filter(v -> v.getId().equals(ventaId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Venta> listarVentas() {
        System.out.println("📋 Listando todas las ventas...");
        return new ArrayList<>(ventas);
    }
    
    @Override
    public List<Venta> buscarVentasPorCliente(String cliente) {
        System.out.println("🔍 Buscando ventas del cliente: " + cliente);
        return ventas.stream()
                .filter(v -> v.getCliente().toLowerCase().contains(cliente.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean confirmarVenta(Long ventaId, String metodoPago) {
        System.out.println("✅ Confirmando venta ID: " + ventaId);
        Venta venta = consultarVenta(ventaId);
        if (venta != null && "PENDIENTE".equals(venta.getEstado())) {
            
            // Actualizar stock de productos
            boolean stockActualizado = true;
            for (ItemVenta item : venta.getItems()) {
                Producto producto = productoService.buscarProductoPorId(item.getProductoId());
                if (producto != null) {
                    int nuevoStock = producto.getStock() - item.getCantidad();
                    if (!productoService.actualizarStock(producto.getId(), nuevoStock)) {
                        stockActualizado = false;
                        break;
                    }
                }
            }
            
            if (stockActualizado) {
                venta.setEstado("CONFIRMADA");
                venta.setMetodoPago(metodoPago);
                System.out.println("✅ Venta confirmada exitosamente");
                return true;
            }
        }
        System.err.println("❌ No se pudo confirmar la venta");
        return false;
    }
    
    @Override
    public boolean cancelarVenta(Long ventaId) {
        System.out.println("❌ Cancelando venta ID: " + ventaId);
        Venta venta = consultarVenta(ventaId);
        if (venta != null && "PENDIENTE".equals(venta.getEstado())) {
            venta.setEstado("CANCELADA");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean marcarComoEntregada(Long ventaId) {
        System.out.println("📦 Marcando como entregada venta ID: " + ventaId);
        Venta venta = consultarVenta(ventaId);
        if (venta != null && "CONFIRMADA".equals(venta.getEstado())) {
            venta.setEstado("ENTREGADA");
            return true;
        }
        return false;
    }
    
    @Override
    public List<Venta> listarVentasPorEstado(String estado) {
        System.out.println("🔍 Listando ventas con estado: " + estado);
        return ventas.stream()
                .filter(v -> v.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}