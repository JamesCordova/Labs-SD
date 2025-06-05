package com.ventas.services;

import java.math.BigDecimal;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import com.ventas.model.Producto;

@WebService
public interface ProductoService {
    
    @WebMethod
    List<Producto> listarProductos();
    
    @WebMethod
    List<Producto> buscarPorCategoria(String categoria);
    
    @WebMethod
    Producto buscarProductoPorId(Long id);
    
    @WebMethod
    List<Producto> buscarProductosPorNombre(String nombre);
    
    @WebMethod
    boolean agregarProducto(Producto producto);
    
    @WebMethod
    boolean actualizarPrecio(Long productoId, BigDecimal nuevoPrecio);
    
    @WebMethod
    boolean actualizarStock(Long productoId, Integer nuevoStock);
    
    @WebMethod
    boolean desactivarProducto(Long productoId);
    
    @WebMethod
    List<String> obtenerCategorias();
}