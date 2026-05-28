package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import modelo.ClaseGrupal;
import modelo.Cliente;
import modelo.Rutina;

public class GimnasioControlador {

    private ArrayList<Cliente> clientes;
    private ArrayList<ClaseGrupal> clases;
    private ArrayList<Rutina> rutinas;

    private static final String ARCHIVO_CLIENTES = "dat/clientes.dat";
    private static final String ARCHIVO_CLASES = "dat/clases.dat";
    private static final String ARCHIVO_RUTINAS = "dat/rutinas.dat";

    private int contadorClientes = 1;
    private int contadorClases = 1;
    private int contadorRutinas = 1;

    public GimnasioControlador() {
        clientes = new ArrayList<>();
        clases = new ArrayList<>();
        rutinas = new ArrayList<>();
        new File("dat").mkdirs();
        cargarDatos();
        actualizarContadores();
    }

    // ===================== CLIENTES =====================

    public String registrarCliente(String nombre, String cedula, String telefono,
            String plan, LocalDate inicio, LocalDate caducidad) {
        // Validar cédula duplicada
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                return "ERROR: Ya existe un cliente con esa cédula.";
            }
        }
        Cliente nuevo = new Cliente(contadorClientes++, nombre, cedula, telefono, plan, inicio, caducidad);
        clientes.add(nuevo);
        guardarClientes();
        return "Cliente registrado exitosamente con ID: " + nuevo.getId();
    }

    public ArrayList<Cliente> listarClientes() {
        return clientes;
    }

    public Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    public String validarAcceso(int clienteId) {
        Cliente c = buscarClientePorId(clienteId);
        if (c == null)
            return "ERROR: Cliente no encontrado.";
        LocalDate hoy = LocalDate.now();
        if (!c.isActivo())
            return "ACCESO DENEGADO: Cliente inactivo.";
        if (hoy.isAfter(c.getFechaCaducidad())) {
            c.setActivo(false);
            guardarClientes();
            return "ACCESO DENEGADO: Membresía vencida el " + c.getFechaCaducidad();
        }
        return "ACCESO PERMITIDO: Bienvenido " + c.getNombre() + " (Plan: " + c.getPlanNombre() + ")";
    }

    public ArrayList<Cliente> membresiasProximasAVencer() {
        ArrayList<Cliente> proximos = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(5);
        for (Cliente c : clientes) {
            if (c.isActivo() && !c.getFechaCaducidad().isBefore(hoy)
                    && !c.getFechaCaducidad().isAfter(limite)) {
                proximos.add(c);
            }
        }
        return proximos;
    }

    // ===================== CLASES GRUPALES =====================

    public String registrarClase(String nombre, String horario, int aforoMaximo) {
        ClaseGrupal clase = new ClaseGrupal(contadorClases++, nombre, horario, aforoMaximo);
        clases.add(clase);
        guardarClases();
        return "Clase registrada exitosamente con ID: " + clase.getId();
    }

    public ArrayList<ClaseGrupal> listarClases() {
        return clases;
    }

    public String reservarClase(int clienteId, int claseId) {
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null)
            return "ERROR: Cliente no encontrado.";

        String acceso = validarAcceso(clienteId);
        if (acceso.startsWith("ACCESO DENEGADO"))
            return acceso;

        ClaseGrupal clase = null;
        for (ClaseGrupal cl : clases) {
            if (cl.getId() == claseId) {
                clase = cl;
                break;
            }
        }
        if (clase == null)
            return "ERROR: Clase no encontrada.";
        if (clase.clienteYaReservado(clienteId))
            return "ERROR: El cliente ya tiene reserva en esta clase.";
        if (!clase.hayCupo())
            return "ERROR: No hay cupos disponibles en " + clase.getNombre();

        clase.agregarReserva(clienteId);
        guardarClases();
        return "Reserva exitosa: " + cliente.getNombre() + " en clase " + clase.getNombre() + " (" + clase.getHorario()
                + ")";
    }

    // ===================== RUTINAS =====================

    public String registrarRutina(int clienteId, String claseNombre, String descripcion,
            LocalDate fecha, String ejercicios) {
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null)
            return "ERROR: Cliente no encontrado.";

        for (Rutina r : rutinas) {
            if (r.getClienteId() == clienteId && r.getFecha().equals(fecha)) {
                return "ERROR: El cliente ya tiene una rutina registrada para el día " + fecha;
            }
        }
        Rutina rutina = new Rutina(contadorRutinas++, clienteId, claseNombre, descripcion, fecha, ejercicios);
        rutinas.add(rutina);
        guardarRutinas();
        return "Rutina registrada exitosamente con ID: " + rutina.getId();
    }

    public ArrayList<Rutina> listarRutinasPorCliente(int clienteId) {
        ArrayList<Rutina> resultado = new ArrayList<>();
        for (Rutina r : rutinas) {
            if (r.getClienteId() == clienteId)
                resultado.add(r);
        }
        return resultado;
    }

    public ArrayList<Rutina> listarRutinas() {
        return rutinas;
    }

    public String mostrarHorasPico() {
        if (clases.isEmpty())
            return "No hay clases registradas.";
        StringBuilder sb = new StringBuilder("=== HORAS PICO ===\n");
        ClaseGrupal[] arr = clases.toArray(new ClaseGrupal[0]);
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j].getTotalReservados() < arr[j + 1].getTotalReservados()) {
                    ClaseGrupal temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (ClaseGrupal c : arr) {
            sb.append("  ").append(c.getNombre()).append(" | ").append(c.getHorario())
                    .append(" | Reservas: ").append(c.getTotalReservados()).append("\n");
        }
        return sb.toString();
    }

    // ===================== SERIALIZACIÓN =====================

    @SuppressWarnings("unchecked")
    private void guardarClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    private void guardarClases() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLASES))) {
            oos.writeObject(clases);
        } catch (IOException e) {
            System.out.println("Error al guardar clases: " + e.getMessage());
        }
    }

    private void guardarRutinas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RUTINAS))) {
            oos.writeObject(rutinas);
        } catch (IOException e) {
            System.out.println("Error al guardar rutinas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        File f1 = new File(ARCHIVO_CLIENTES);
        if (f1.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f1))) {
                clientes = (ArrayList<Cliente>) ois.readObject();
            } catch (Exception e) {
                clientes = new ArrayList<>();
            }
        }
        File f2 = new File(ARCHIVO_CLASES);
        if (f2.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f2))) {
                clases = (ArrayList<ClaseGrupal>) ois.readObject();
            } catch (Exception e) {
                clases = new ArrayList<>();
            }
        }
        File f3 = new File(ARCHIVO_RUTINAS);
        if (f3.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f3))) {
                rutinas = (ArrayList<Rutina>) ois.readObject();
            } catch (Exception e) {
                rutinas = new ArrayList<>();
            }
        }
    }

    private void actualizarContadores() {
        for (Cliente c : clientes)
            if (c.getId() >= contadorClientes)
                contadorClientes = c.getId() + 1;
        for (ClaseGrupal cl : clases)
            if (cl.getId() >= contadorClases)
                contadorClases = cl.getId() + 1;
        for (Rutina r : rutinas)
            if (r.getId() >= contadorRutinas)
                contadorRutinas = r.getId() + 1;
    }
}
