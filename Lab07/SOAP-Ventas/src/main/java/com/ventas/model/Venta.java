package com.ventas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String cliente;
    private String emailCliente;
    private String fechaVenta;
    private List<ItemVenta> items;
    private BigDecimal total;
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA, ENTREGADA
    private String metodoPago;
    
    public Venta() {}
    
    public Venta(Long id, String cliente, String emailCliente, List<ItemVenta> items) {
        this.id = id;
        this.cliente = cliente;
        this.emailCliente = emailCliente;
        this.fechaVenta = LocalDateTime.now().toString();
        this.items = items;
        this.estado = "PENDIENTE";
        this.metodoPago = "PENDIENTE";
        calcularTotal();
    }
    
    private void calcularTotal() {
        if (items != null && !items.isEmpty()) {
            this.total = items.stream()
                .map(ItemVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    
    public String getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(String fechaVenta) { this.fechaVenta = fechaVenta; }
    
    public List<ItemVenta> getItems() { return items; }
    public void setItems(List<ItemVenta> items) { 
        this.items = items; 
        calcularTotal();
    }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}