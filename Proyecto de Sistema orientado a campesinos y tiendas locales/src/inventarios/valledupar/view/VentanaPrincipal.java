package inventarios.valledupar.view;

import inventarios.valledupar.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private Usuario usuarioActivo;
    private JPanel panelMenu;
    private JPanel panelContenido;
    private JLabel lblBienvenida;
    private JButton btnProductos;
    private JButton btnMovimientos;
    private JButton btnAlertas;
    private JButton btnReportes;
    private JButton btnSalir;

    public VentanaPrincipal(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Inventarios - Valledupar");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblBienvenida = new JLabel("Usuario: " + usuarioActivo.getNombre()
                + "  |  Rol: " + usuarioActivo.getRol());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 13));
        panelSuperior.add(lblBienvenida);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel menu lateral
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1, 5, 5));
        panelMenu.setPreferredSize(new Dimension(160, 0));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnProductos = new JButton("Productos");
        btnMovimientos = new JButton("Movimientos");
        btnAlertas = new JButton("Alertas");
        btnReportes = new JButton("Reportes");
        btnSalir = new JButton("Cerrar sesion");

        panelMenu.add(btnProductos);
        panelMenu.add(btnMovimientos);
        panelMenu.add(btnAlertas);
        panelMenu.add(btnReportes);
        panelMenu.add(btnSalir);
        add(panelMenu, BorderLayout.WEST);

        // Panel contenido central
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblInicio = new JLabel("Selecciona un modulo del menu lateral.",
                SwingConstants.CENTER);
        lblInicio.setFont(new Font("Arial", Font.PLAIN, 13));
        panelContenido.add(lblInicio, BorderLayout.CENTER);
        add(panelContenido, BorderLayout.CENTER);

        // Acciones botones
        btnProductos.addActionListener(e -> new VentanaProductos().setVisible(true));
        btnMovimientos.addActionListener(e -> new VentanaMovimientos().setVisible(true));
        btnAlertas.addActionListener(e -> new VentanaAlertas().setVisible(true));
        btnReportes.addActionListener(e -> new VentanaReportes().setVisible(true));
        btnSalir.addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Deseas cerrar sesion?", "Salir",
                    JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                new VentanaLogin().setVisible(true);
                this.dispose();
            }
        });
    }
}