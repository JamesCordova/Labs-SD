package Lab04.Propuesto02;

import java.util.HashMap;

public class Persona {
    private String nombre;
    private String apellido;
    private String dni;
    private HashMap<Integer, Tarjeta> tarjetas;
    private int cantidadTarjetas = 0;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Persona(String nombre, String apellido, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.tarjetas = new HashMap<>();
    }

    public void addTarjeta(Tarjeta tarjeta) {
        cantidadTarjetas++;
        tarjeta.setNumero(cantidadTarjetas);
        this.tarjetas.put(cantidadTarjetas, tarjeta);
    }

    public Tarjeta getTarjeta(int numero) {
        return tarjetas.get(numero);
    }

    public HashMap<Integer, Tarjeta> getTarjetas() {
        return tarjetas;
    }

}