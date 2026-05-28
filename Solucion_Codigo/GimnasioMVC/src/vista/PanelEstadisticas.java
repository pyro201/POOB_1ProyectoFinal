package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import controlador.GimnasioControlador;
import modelo.Cliente;

public class PanelEstadisticas extends JPanel {

    private GimnasioControlador controlador;
    private JTextArea txtEstadisticas;

    public PanelEstadisticas(GimnasioControlador controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 248, 235));
        construirUI();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("Estadísticas del Gimnasio", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(211, 84, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(new Color(250, 248, 235));

        JButton btnHorasPico = crearBoton("Ver Horas Pico", new Color(211, 84, 0));
        JButton btnVencer = crearBoton("Membresías por Vencer (5 días)", new Color(192, 57, 43));
        JButton btnResumen = crearBoton("Resumen General", new Color(39, 174, 96));

        btnPanel.add(btnHorasPico);
        btnPanel.add(btnVencer);
        btnPanel.add(btnResumen);
        add(btnPanel, BorderLayout.CENTER);

        txtEstadisticas = new JTextArea();
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtEstadisticas.setBackground(new Color(44, 62, 80));
        txtEstadisticas.setForeground(new Color(241, 196, 15));
        txtEstadisticas.setText("Seleccione una opción para ver estadísticas...");
        txtEstadisticas.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(txtEstadisticas);
        scroll.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        scroll.setPreferredSize(new Dimension(0, 300));
        add(scroll, BorderLayout.SOUTH);

        btnHorasPico.addActionListener(e -> txtEstadisticas.setText(controlador.mostrarHorasPico()));

        btnVencer.addActionListener(e -> {
            ArrayList<Cliente> proximos = controlador.membresiasProximasAVencer();
            if (proximos.isEmpty()) {
                txtEstadisticas.setText("No hay membresías próximas a vencer en los siguientes 5 días.");
            } else {
                StringBuilder sb = new StringBuilder("=== MEMBRESÍAS POR VENCER (próximos 5 días) ===\n\n");
                for (Cliente c : proximos) {
                    sb.append("  Cliente: ").append(c.getNombre())
                            .append("\n  Plan: ").append(c.getPlanNombre())
                            .append("\n  Vence: ").append(c.getFechaCaducidad())
                            .append("\n  Teléfono: ").append(c.getTelefono())
                            .append("\n  -------------------------\n");
                }
                txtEstadisticas.setText(sb.toString());
            }
        });

        btnResumen.addActionListener(e -> {
            int totalClientes = controlador.listarClientes().size();
            int activosCount = 0;
            for (Cliente c : controlador.listarClientes())
                if (c.isActivo())
                    activosCount++;
            int totalClases = controlador.listarClases().size();
            int totalRutinas = controlador.listarRutinas().size();
            String resumen = "=== RESUMEN GENERAL DEL GIMNASIO ===\n\n"
                    + "  Total de Clientes registrados : " + totalClientes + "\n"
                    + "  Clientes con membresía activa : " + activosCount + "\n"
                    + "  Clases grupales disponibles   : " + totalClases + "\n"
                    + "  Rutinas registradas           : " + totalRutinas + "\n\n"
                    + controlador.mostrarHorasPico();
            txtEstadisticas.setText(resumen);
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }
}
