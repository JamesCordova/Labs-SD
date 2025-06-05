package com.ventas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemVenta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    
    public ItemVenta() {}
    
    public ItemVenta(Long productoId, String nombreProducto, 
                    Integer cantidad, BigDecimal precioUnitario) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
    
    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad;
        if (precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario;
        if (cantidad != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}