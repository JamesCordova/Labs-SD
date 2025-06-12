package Lab04.Propuesto02;

import java.math.BigDecimal;

// Representa una tarjeta (crédito o débito) con información relevante como número, tipo, CVV, saldo, etc.

public class Tarjeta {

    private int numero;
    private TipoTarjeta tipo;
    private String fechaVencimiento;
    private String cvv;
    private String nombreTitular;
    private BigDecimal saldo;

    public Tarjeta(int numero, TipoTarjeta tipo, String fechaVencimiento, String cvv, String nombreTitular,
            BigDecimal saldo) {
        this.numero = numero;
        this.tipo = tipo;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
        this.nombreTitular = nombreTitular;
        this.saldo = saldo;
    }

    public Tarjeta(int numero, TipoTarjeta tipo, String fechaVencimiento, String cvv, String nombreTitular) {
        this.numero = numero;
        this.tipo = tipo;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
        this.nombreTitular = nombreTitular;
        this.saldo = new BigDecimal(0);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public TipoTarjeta getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarjeta tipo) {
        this.tipo = tipo;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

}