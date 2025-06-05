package com.ventas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String categoria;
    private boolean activo;
    
    public Producto() {}
    
    public Producto(Long id, String nombre, String descripcion, 
                   BigDecimal precio, Integer stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', precio=%.2f, stock=%d, categoria='%s'}", 
                           id, nombre, precio, stock, categoria);
    }
}