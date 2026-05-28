package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

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
import modelo.ClaseGrupal;

public class PanelClases extends JPanel {

    private GimnasioControlador controlador;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtHorario, txtAforo, txtClienteId, txtClaseId;
    private JTextArea txtResultado;

    public PanelClases(GimnasioControlador controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 245));
        construirUI();
        actualizarTabla();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Gestión de Clases Grupales", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(39, 174, 96));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formClase = new JPanel(new GridBagLayout());
        formClase.setBorder(BorderFactory.createTitledBorder("Registrar Clase Grupal"));
        formClase.setBackground(new Color(235, 250, 235));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formClase.add(new JLabel("Nombre de Clase (CrossFit/Spinning/Yoga):"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        formClase.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formClase.add(new JLabel("Horario (ej: Lunes 08:00):"), gbc);
        gbc.gridx = 1;
        txtHorario = new JTextField(15);
        formClase.add(txtHorario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formClase.add(new JLabel("Aforo Máximo:"), gbc);
        gbc.gridx = 1;
        txtAforo = new JTextField(15);
        formClase.add(txtAforo, gbc);

        JButton btnRegistrarClase = crearBoton("Registrar Clase", new Color(39, 174, 96));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formClase.add(btnRegistrarClase, gbc);
        btnRegistrarClase.addActionListener(e -> registrarClase());

        JPanel formReserva = new JPanel(new GridBagLayout());
        formReserva.setBorder(BorderFactory.createTitledBorder("Hacer Reserva en Clase"));
        formReserva.setBackground(new Color(235, 250, 235));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 8, 5, 8);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        gbc2.gridx = 0;
        gbc2.gridy = 0;
        formReserva.add(new JLabel("ID Cliente:"), gbc2);
        gbc2.gridx = 1;
        txtClienteId = new JTextField(10);
        formReserva.add(txtClienteId, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = 1;
        formReserva.add(new JLabel("ID Clase:"), gbc2);
        gbc2.gridx = 1;
        txtClaseId = new JTextField(10);
        formReserva.add(txtClaseId, gbc2);

        JButton btnReservar = crearBoton("Reservar Cupo", new Color(41, 128, 185));
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        gbc2.gridwidth = 2;
        formReserva.add(btnReservar, gbc2);
        btnReservar.addActionListener(e -> hacerReserva());

        JPanel panelSup = new JPanel(new GridLayout(1, 2, 10, 0));
        panelSup.setBackground(new Color(245, 250, 245));
        panelSup.add(formClase);
        panelSup.add(formReserva);

        String[] cols = { "ID", "Nombre", "Horario", "Aforo Máx.", "Reservados", "Cupos Libres" };
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setBackground(new Color(39, 174, 96));
        tabla.getTableHeader().setForeground(Color.DARK_GRAY);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Clases Registradas"));

        txtResultado = new JTextArea(4, 0);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultado.setBackground(new Color(44, 62, 80));
        txtResultado.setForeground(new Color(46, 204, 113));
        JScrollPane scrollRes = new JScrollPane(txtResultado);
        scrollRes.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBackground(new Color(245, 250, 245));
        centro.add(panelSup, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(scrollRes, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
    }

    private void registrarClase() {
        String nombre = txtNombre.getText().trim();
        String horario = txtHorario.getText().trim();
        String sAforo = txtAforo.getText().trim();
        if (nombre.isEmpty() || horario.isEmpty() || sAforo.isEmpty()) {
            mostrarMensaje("Complete todos los campos.");
            return;
        }
        try {
            int aforo = Integer.parseInt(sAforo);
            mostrarMensaje(controlador.registrarClase(nombre, horario, aforo));
            actualizarTabla();
            txtNombre.setText("");
            txtHorario.setText("");
            txtAforo.setText("");
        } catch (NumberFormatException e) {
            mostrarMensaje("El aforo debe ser un número entero.");
        }
    }

    private void hacerReserva() {
        try {
            int cId = Integer.parseInt(txtClienteId.getText().trim());
            int clId = Integer.parseInt(txtClaseId.getText().trim());
            mostrarMensaje(controlador.reservarClase(cId, clId));
            actualizarTabla();
        } catch (NumberFormatException e) {
            mostrarMensaje("IDs deben ser números enteros.");
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (ClaseGrupal c : controlador.listarClases()) {
            modeloTabla.addRow(new Object[] {
                    c.getId(), c.getNombre(), c.getHorario(),
                    c.getAforoMaximo(), c.getTotalReservados(),
                    c.getAforoMaximo() - c.getTotalReservados()
            });
        }
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
