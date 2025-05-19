package Lab04.Propuesto02;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SistemaTarjetas extends Remote {
    public BigDecimal getSaldo(String dni, int numeroTarjeta) throws RemoteException;

    public BigDecimal addSaldo(String dni, int numeroTarjeta, BigDecimal saldo) throws RemoteException;

    public BigDecimal subSaldo(String dni, int numeroTarjeta, BigDecimal saldo) throws RemoteException;

    public void agregarCliente(String nombre, String apellido, String dni) throws RemoteException;

    public void agregarTarjeta(String dni, TipoTarjeta tipo, String fechaVencimiento, String cvv, String nombreTitular,
            BigDecimal saldoInicial) throws RemoteException;
}