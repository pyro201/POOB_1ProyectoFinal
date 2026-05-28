package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import modelo.Cliente;

public class PanelClientes extends JPanel {

    private GimnasioControlador controlador;
    private DefaultTableModel modeloTabla;
    private JTable tablaClientes;
    private JTextField txtNombre, txtCedula, txtTelefono, txtPlan, txtInicio, txtCaducidad, txtBuscarId;
    private JTextArea txtResultado;

    public PanelClientes(GimnasioControlador controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));
        construirUI();
        actualizarTabla();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Gestión de Clientes y Membresías", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(235, 240, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = agregarCampo(formPanel, gbc, "Nombre:", 0);
        txtCedula = agregarCampo(formPanel, gbc, "Cédula:", 1);
        txtTelefono = agregarCampo(formPanel, gbc, "Teléfono:", 2);
        txtPlan = agregarCampo(formPanel, gbc, "Plan (Básico/Plus/VIP):", 3);
        txtInicio = agregarCampo(formPanel, gbc, "Fecha Inicio (yyyy-MM-dd):", 4);
        txtCaducidad = agregarCampo(formPanel, gbc, "Fecha Caducidad (yyyy-MM-dd):", 5);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(new Color(235, 240, 250));

        JButton btnRegistrar = crearBoton("Registrar", new Color(52, 152, 219));
        JButton btnLimpiar = crearBoton("Limpiar", new Color(149, 165, 166));
        JButton btnAcceso = crearBoton("Verificar Acceso", new Color(46, 204, 113));
        JButton btnProximos = crearBoton("Membresías por Vencer (5 días)", new Color(230, 126, 34));
        txtBuscarId = new JTextField(5);
        txtBuscarId.setToolTipText("ID Cliente");

        btnPanel.add(btnRegistrar);
        btnPanel.add(btnLimpiar);
        btnPanel.add(new JLabel("ID:"));
        btnPanel.add(txtBuscarId);
        btnPanel.add(btnAcceso);
        btnPanel.add(btnProximos);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnAcceso.addActionListener(e -> verificarAcceso());
        btnProximos.addActionListener(e -> mostrarProximos());

        String[] columnas = { "ID", "Nombre", "Cédula", "Teléfono", "Plan", "Inicio", "Caducidad", "Activo" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(22);
        tablaClientes.getTableHeader().setBackground(new Color(52, 73, 94));
        tablaClientes.getTableHeader().setForeground(Color.DARK_GRAY);
        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setPreferredSize(new Dimension(0, 180));

        txtResultado = new JTextArea(4, 0);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultado.setBackground(new Color(44, 62, 80));
        txtResultado.setForeground(new Color(46, 204, 113));
        JScrollPane scrollRes = new JScrollPane(txtResultado);
        scrollRes.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBackground(new Color(245, 245, 250));
        centro.add(formPanel, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(scrollRes, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
    }

    private JTextField agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField campo = new JTextField(18);
        panel.add(campo, gbc);
        return campo;
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

    private void registrarCliente() {
        try {
            String nombre = txtNombre.getText().trim();
            String cedula = txtCedula.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String plan = txtPlan.getText().trim();
            String sInicio = txtInicio.getText().trim();
            String sCaducidad = txtCaducidad.getText().trim();

            if (nombre.isEmpty() || cedula.isEmpty() || plan.isEmpty()
                    || sInicio.isEmpty() || sCaducidad.isEmpty()) {
                mostrarMensaje("Por favor complete todos los campos obligatorios.");
                return;
            }
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inicio = LocalDate.parse(sInicio, fmt);
            LocalDate caducidad = LocalDate.parse(sCaducidad, fmt);

            String resultado = controlador.registrarCliente(nombre, cedula, telefono, plan, inicio, caducidad);
            mostrarMensaje(resultado);
            actualizarTabla();
            limpiarCampos();
        } catch (DateTimeParseException ex) {
            mostrarMensaje("Error: Formato de fecha incorrecto. Use yyyy-MM-dd (ej: 2025-01-15)");
        }
    }

    private void verificarAcceso() {
        String texto = txtBuscarId.getText().trim();
        if (texto.isEmpty()) {
            mostrarMensaje("Ingrese el ID del cliente.");
            return;
        }
        try {
            int id = Integer.parseInt(texto);
            mostrarMensaje(controlador.validarAcceso(id));
        } catch (NumberFormatException e) {
            mostrarMensaje("ID inválido.");
        }
    }

    private void mostrarProximos() {
        ArrayList<Cliente> proximos = controlador.membresiasProximasAVencer();
        if (proximos.isEmpty()) {
            mostrarMensaje("No hay membresías próximas a vencer en los siguientes 5 días.");
        } else {
            StringBuilder sb = new StringBuilder("=== Membresías por vencer (próximos 5 días) ===\n");
            for (Cliente c : proximos) {
                sb.append("  ").append(c.getNombre()).append(" | Plan: ").append(c.getPlanNombre())
                        .append(" | Vence: ").append(c.getFechaCaducidad()).append("\n");
            }
            mostrarMensaje(sb.toString());
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : controlador.listarClientes()) {
            modeloTabla.addRow(new Object[] {
                    c.getId(), c.getNombre(), c.getCedula(), c.getTelefono(),
                    c.getPlanNombre(), c.getFechaInicio(), c.getFechaCaducidad(), c.isActivo() ? "Sí" : "No"
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtPlan.setText("");
        txtInicio.setText("");
        txtCaducidad.setText("");
    }

    private void mostrarMensaje(String msg) {
        txtResultado.setText(msg);
    }
}
