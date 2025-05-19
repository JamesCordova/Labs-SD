package Lab04.Propuesto02;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class SistemaTarjetasImpl extends UnicastRemoteObject implements SistemaTarjetas {

    HashMap<String, Persona> personas = new HashMap<>();

    protected SistemaTarjetasImpl() throws RemoteException {
        super();
        // Inicialización de personas con datos predefinidos (opcional)
        Persona persona1 = new Persona("Juan", "Perez", "12345678");
        Persona persona2 = new Persona("Maria", "Gomez", "87654321");

        Tarjeta tarjeta1 = new Tarjeta(1, TipoTarjeta.CREDITO, "12/2023", "123", "Juan Perez", new BigDecimal("1000"));
        Tarjeta tarjeta2 = new Tarjeta(2, TipoTarjeta.DEBITO, "06/2024", "456", "Maria Gomez", new BigDecimal("2000"));

        persona1.addTarjeta(tarjeta1);
        persona2.addTarjeta(tarjeta2);

        personas.put(persona1.getDni(), persona1);
        personas.put(persona2.getDni(), persona2);
    }

    @Override
    public BigDecimal getSaldo(String dni, int numeroTarjeta) throws RemoteException {
        checkConsult(dni, numeroTarjeta);
        return personas.get(dni).getTarjeta(numeroTarjeta).getSaldo();
    }

    @Override
    public BigDecimal addSaldo(String dni, int numeroTarjeta, BigDecimal saldo) throws RemoteException {
        checkConsult(dni, numeroTarjeta);
        BigDecimal saldoActual = personas.get(dni).getTarjeta(numeroTarjeta).getSaldo();
        BigDecimal nuevoSaldo = saldoActual.add(saldo);
        personas.get(dni).getTarjeta(numeroTarjeta).setSaldo(nuevoSaldo);
        return nuevoSaldo;
    }

    @Override
    public BigDecimal subSaldo(String dni, int numeroTarjeta, BigDecimal saldo) throws RemoteException {
        checkConsult(dni, numeroTarjeta);
        BigDecimal saldoActual = personas.get(dni).getTarjeta(numeroTarjeta).getSaldo();
        BigDecimal nuevoSaldo = saldoActual.subtract(saldo);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RemoteException("Saldo insuficiente");
        }
        personas.get(dni).getTarjeta(numeroTarjeta).setSaldo(nuevoSaldo);
        return nuevoSaldo;
    }

    @Override
    public void agregarCliente(String nombre, String apellido, String dni) throws RemoteException {
        if (personas.containsKey(dni)) {
            throw new RemoteException("Cliente con DNI " + dni + " ya existe.");
        }
        Persona nuevaPersona = new Persona(nombre, apellido, dni);
        personas.put(dni, nuevaPersona);
    }

    @Override
    public void agregarTarjeta(String dni, TipoTarjeta tipo, String fechaVencimiento, String cvv, String nombreTitular,
            BigDecimal saldoInicial) throws RemoteException {
        if (!personas.containsKey(dni)) {
            throw new RemoteException("No se encontró persona con DNI " + dni);
        }
        Persona persona = personas.get(dni);
        Tarjeta nuevaTarjeta = new Tarjeta(persona.getTarjetas().size() + 1, tipo, fechaVencimiento, cvv, nombreTitular,
                saldoInicial);
        persona.addTarjeta(nuevaTarjeta);
    }

    private void checkConsult(String dni, int numeroTarjeta) throws RemoteException {
        if (!personas.containsKey(dni)) {
            throw new RemoteException("Persona no encontrada con DNI: " + dni);
        }
        if (!personas.get(dni).getTarjetas().containsKey(numeroTarjeta)) {
            throw new RemoteException("Tarjeta no encontrada con número: " + numeroTarjeta);
        }
    }
}