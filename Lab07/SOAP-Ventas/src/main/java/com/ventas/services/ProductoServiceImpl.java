package com.ventas.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.jws.WebService;
import com.ventas.model.Producto;

@WebService(endpointInterface = "com.ventas.services.ProductoService")
public class ProductoServiceImpl implements ProductoService {
    
    // Simulación de base de datos en memoria
    private static List<Producto> productos = new ArrayList<>(Arrays.asList(
        new Producto(1L, "Laptop Dell Inspiron", "Laptop Dell Inspiron 15 - 8GB RAM, 256GB SSD", 
                    new BigDecimal("2499.99"), 15, "ELECTRONICA"),
        new Producto(2L, "Mouse Logitech MX", "Mouse inalámbrico Logitech MX Master 3", 
                    new BigDecimal("89.99"), 30, "ELECTRONICA"),
        new Producto(3L, "Teclado Mecánico RGB", "Teclado mecánico con iluminación RGB", 
                    new BigDecimal("159.99"), 20, "ELECTRONICA"),
        new Producto(4L, "Monitor Samsung 24\"", "Monitor Samsung 24 pulgadas Full HD", 
                    new BigDecimal("449.99"), 12, "ELECTRONICA"),
        new Producto(5L, "Effective Java", "Libro: Effective Java - Joshua Bloch", 
                    new BigDecimal("65.99"), 25, "LIBROS"),
        new Producto(6L, "Clean Code", "Libro: Clean Code - Robert C. Martin", 
                    new BigDecimal("59.99"), 18, "LIBROS"),
        new Producto(7L, "Auriculares Sony", "Auriculares inalámbricos Sony WH-1000XM4", 
                    new BigDecimal("349.99"), 8, "AUDIO"),
        new Producto(8L, "Webcam Logitech", "Webcam Logitech C920 HD Pro", 
                    new BigDecimal("129.99"), 22, "ELECTRONICA")
    ));
    
    @Override
    public List<Producto> listarProductos() {
        System.out.println("📋 Listando todos los productos activos...");
        return productos.stream()
                .filter(Producto::isActivo)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Producto> buscarPorCategoria(String categoria) {
        System.out.println("🔍 Buscando productos en categoría: " + categoria);
        return productos.stream()
                .filter(p -> p.isActivo() && p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
    
    @Override
    public Producto buscarProductoPorId(Long id) {
        System.out.println("🔍 Buscando producto con ID: " + id);
        return productos.stream()
                .filter(p -> p.getId().equals(id) && p.isActivo())
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) {
        System.out.println("🔍 Buscando productos que contengan: " + nombre);
        return productos.stream()
                .filter(p -> p.isActivo() && 
                        p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean agregarProducto(Producto producto) {
        try {
            System.out.println("➕ Agregando nuevo producto: " + producto.getNombre());
            // Generar ID automático
            Long maxId = productos.stream()
                    .mapToLong(Producto::getId)
                    .max()
                    .orElse(0L);
            producto.setId(maxId + 1);
            producto.setActivo(true);
            productos.add(producto);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizarPrecio(Long productoId, BigDecimal nuevoPrecio) {
        System.out.println("💰 Actualizando precio del producto ID: " + productoId);
        Producto producto = buscarProductoPorId(productoId);
        if (producto != null && nuevoPrecio.compareTo(BigDecimal.ZERO) > 0) {
            producto.setPrecio(nuevoPrecio);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean actualizarStock(Long productoId, Integer nuevoStock) {
        System.out.println("📦 Actualizando stock del producto ID: " + productoId);
        Producto producto = buscarProductoPorId(productoId);
        if (producto != null && nuevoStock >= 0) {
            producto.setStock(nuevoStock);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean desactivarProducto(Long productoId) {
        System.out.println("🚫 Desactivando producto ID: " + productoId);
        Producto producto = productos.stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElse(null);
        if (producto != null) {
            producto.setActivo(false);
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> obtenerCategorias() {
        System.out.println("📂 Obteniendo categorías disponibles...");
        return productos.stream()
                .filter(Producto::isActivo)
                .map(Producto::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}