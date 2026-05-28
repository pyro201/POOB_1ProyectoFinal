package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Rutina implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int clienteId;
    private String claseNombre;
    private String descripcion;
    private LocalDate fecha;
    private String ejercicios;

    public Rutina() {
    }

    public Rutina(int id, int clienteId, String claseNombre, String descripcion,
            LocalDate fecha, String ejercicios) {
        this.id = id;
        this.clienteId = clienteId;
        this.claseNombre = claseNombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.ejercicios = ejercicios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClaseNombre() {
        return claseNombre;
    }

    public void setClaseNombre(String claseNombre) {
        this.claseNombre = claseNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(String ejercicios) {
        this.ejercicios = ejercicios;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Cliente ID: " + clienteId + " | Clase: " + claseNombre
                + " | Fecha: " + fecha + " | " + descripcion;
    }
}
