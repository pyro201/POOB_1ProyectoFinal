package modelo;

import java.io.Serializable;

public class ClaseGrupal implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String horario;
    private int aforoMaximo;
    private int[] reservas;
    private int totalReservados;

    public ClaseGrupal() {
    }

    public ClaseGrupal(int id, String nombre, String horario, int aforoMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.aforoMaximo = aforoMaximo;
        this.reservas = new int[aforoMaximo];
        this.totalReservados = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(int aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public int[] getReservas() {
        return reservas;
    }

    public void setReservas(int[] reservas) {
        this.reservas = reservas;
    }

    public int getTotalReservados() {
        return totalReservados;
    }

    public void setTotalReservados(int totalReservados) {
        this.totalReservados = totalReservados;
    }

    public boolean hayCupo() {
        return totalReservados < aforoMaximo;
    }

    public boolean agregarReserva(int clienteId) {
        if (!hayCupo())
            return false;
        reservas[totalReservados] = clienteId;
        totalReservados++;
        return true;
    }

    public boolean clienteYaReservado(int clienteId) {
        for (int i = 0; i < totalReservados; i++) {
            if (reservas[i] == clienteId)
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " | Horario: " + horario
                + " | Cupos: " + totalReservados + "/" + aforoMaximo;
    }
}
