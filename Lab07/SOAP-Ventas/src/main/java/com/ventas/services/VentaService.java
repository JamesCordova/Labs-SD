package com.ventas.services;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import com.ventas.model.ItemVenta;
import com.ventas.model.Venta;

@WebService
public interface VentaService {
    
    @WebMethod
    Long crearVenta(String cliente, String emailCliente, List<ItemVenta> items);
    
    @WebMethod
    Venta consultarVenta(Long ventaId);
    
    @WebMethod
    List<Venta> listarVentas();
    
    @WebMethod
    List<Venta> buscarVentasPorCliente(String cliente);
    
    @WebMethod
    boolean confirmarVenta(Long ventaId, String metodoPago);
    
    @WebMethod
    boolean cancelarVenta(Long ventaId);
    
    @WebMethod
    boolean marcarComoEntregada(Long ventaId);
    
    @WebMethod
    List<Venta> listarVentasPorEstado(String estado);
}