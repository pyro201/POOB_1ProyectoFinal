package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import controlador.GimnasioControlador;

public class VentanaPrincipal extends JFrame {

    private GimnasioControlador controlador;
    private PanelClientes panelClientes;
    private PanelClases panelClases;
    private PanelRutinas panelRutinas;
    private PanelEstadisticas panelEstadisticas;

    public VentanaPrincipal() {

        controlador = new GimnasioControlador();
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("Sistema de Monitoreo de Gimnasios y Planes de Suscripción");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void construirUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblTitulo = new JLabel("🏋 GymSystem - Sistema de Gimnasio", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("POO - MVC con Serialización | Arreglos", SwingConstants.RIGHT);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(189, 195, 199));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblSubtitulo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(new Color(236, 240, 241));

        panelClientes = new PanelClientes(controlador);
        tabs.addTab("👤 Clientes", panelClientes);

        panelClases = new PanelClases(controlador);
        tabs.addTab("🤸 Clases Grupales", panelClases);

        panelRutinas = new PanelRutinas(controlador);
        tabs.addTab("📋 Rutinas", panelRutinas);

        panelEstadisticas = new PanelEstadisticas(controlador);
        tabs.addTab("📊 Estadísticas", panelEstadisticas);

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 0)
                panelClientes.actualizarTabla();
            if (idx == 1)
                panelClases.actualizarTabla();
            if (idx == 2)
                panelRutinas.actualizarTabla();
        });

        add(tabs, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        statusBar.setBackground(new Color(44, 62, 80));
        JLabel status = new JLabel("  Sistema listo | Datos guardados en archivos .dat (serialización)");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        status.setForeground(new Color(149, 165, 166));
        statusBar.add(status);
        add(statusBar, BorderLayout.SOUTH);
    }
}
