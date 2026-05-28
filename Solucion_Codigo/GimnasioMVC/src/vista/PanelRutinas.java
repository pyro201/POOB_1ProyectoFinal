package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.GimnasioControlador;
import modelo.Rutina;

public class PanelRutinas extends JPanel {

    private GimnasioControlador controlador;
    private DefaultTableModel modeloTabla;
    private JTextField txtClienteId, txtClase, txtFecha, txtBuscarClienteId;
    private JTextArea txtDescripcion, txtEjercicios, txtResultado;

    public PanelRutinas(GimnasioControlador controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 245, 255));
        construirUI();
        actualizarTabla();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Gestión de Rutinas de Entrenamiento", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(142, 68, 173));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Rutina (una por cliente por día)"));
        formPanel.setBackground(new Color(245, 235, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        txtClienteId = new JTextField(15);
        formPanel.add(txtClienteId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Clase (CrossFit/Spinning/Yoga):"), gbc);
        gbc.gridx = 1;
        txtClase = new JTextField(15);
        formPanel.add(txtClase, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(15);
        formPanel.add(txtFecha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Descripción breve:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextArea(2, 15);
        txtDescripcion.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Ejercicios:"), gbc);
        gbc.gridx = 1;
        txtEjercicios = new JTextArea(2, 15);
        txtEjercicios.setLineWrap(true);
        formPanel.add(new JScrollPane(txtEjercicios), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(new Color(245, 235, 255));

        JButton btnRegistrar = crearBoton("Registrar Rutina", new Color(142, 68, 173));
        JButton btnVer = crearBoton("Ver Mis Rutinas", new Color(52, 152, 219));
        txtBuscarClienteId = new JTextField(5);
        txtBuscarClienteId.setToolTipText("ID Cliente");

        btnPanel.add(btnRegistrar);
        btnPanel.add(new JLabel("ID Cliente:"));
        btnPanel.add(txtBuscarClienteId);
        btnPanel.add(btnVer);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        btnRegistrar.addActionListener(e -> registrarRutina());
        btnVer.addActionListener(e -> verRutinasCliente());

        String[] cols = { "ID", "Cliente ID", "Clase", "Descripción", "Fecha", "Ejercicios" };
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setBackground(new Color(142, 68, 173));
        tabla.getTableHeader().setForeground(Color.DARK_GRAY);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Rutinas Registradas"));

        txtResultado = new JTextArea(3, 0);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultado.setBackground(new Color(44, 62, 80));
        txtResultado.setForeground(new Color(155, 89, 182));
        JScrollPane scrollRes = new JScrollPane(txtResultado);
        scrollRes.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBackground(new Color(250, 245, 255));
        centro.add(formPanel, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(scrollRes, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
    }

    private void registrarRutina() {
        try {
            int clienteId = Integer.parseInt(txtClienteId.getText().trim());
            String clase = txtClase.getText().trim();
            String sDate = txtFecha.getText().trim();
            String desc = txtDescripcion.getText().trim();
            String ejer = txtEjercicios.getText().trim();

            if (clase.isEmpty() || sDate.isEmpty()) {
                mostrarMensaje("Complete todos los campos obligatorios.");
                return;
            }
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(sDate, fmt);
            mostrarMensaje(controlador.registrarRutina(clienteId, clase, desc, fecha, ejer));
            actualizarTabla();
            limpiar();
        } catch (NumberFormatException ex) {
            mostrarMensaje("ID Cliente debe ser un número.");
        } catch (DateTimeParseException ex) {
            mostrarMensaje("Formato de fecha incorrecto. Use yyyy-MM-dd");
        }
    }

    private void verRutinasCliente() {
        String texto = txtBuscarClienteId.getText().trim();
        if (texto.isEmpty()) {
            mostrarMensaje("Ingrese el ID del cliente.");
            return;
        }
        try {
            int id = Integer.parseInt(texto);
            ArrayList<Rutina> rutinas = controlador.listarRutinasPorCliente(id);
            modeloTabla.setRowCount(0);
            if (rutinas.isEmpty()) {
                mostrarMensaje("No hay rutinas para el cliente ID: " + id);
            } else {
                for (Rutina r : rutinas) {
                    modeloTabla.addRow(new Object[] {
                            r.getId(), r.getClienteId(), r.getClaseNombre(),
                            r.getDescripcion(), r.getFecha(), r.getEjercicios()
                    });
                }
                mostrarMensaje("Rutinas del cliente ID " + id + ": " + rutinas.size() + " encontradas.");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("ID inválido.");
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Rutina r : controlador.listarRutinas()) {
            modeloTabla.addRow(new Object[] {
                    r.getId(), r.getClienteId(), r.getClaseNombre(),
                    r.getDescripcion(), r.getFecha(), r.getEjercicios()
            });
        }
    }

    private void limpiar() {
        txtClienteId.setText("");
        txtClase.setText("");
        txtFecha.setText("");
        txtDescripcion.setText("");
        txtEjercicios.setText("");
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }

    private void mostrarMensaje(String msg) {
        txtResultado.setText(msg);
    }
}
