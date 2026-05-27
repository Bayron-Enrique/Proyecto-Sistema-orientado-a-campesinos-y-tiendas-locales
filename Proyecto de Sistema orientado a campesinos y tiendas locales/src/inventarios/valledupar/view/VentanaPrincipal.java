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
    private CardLayout cardLayout;

    public VentanaPrincipal(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Inventarios - Valledupar");
        setSize(950, 600);
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

        btnProductos   = new JButton("Productos");
        btnMovimientos = new JButton("Movimientos");
        btnAlertas     = new JButton("Alertas");
        btnReportes    = new JButton("Reportes");
        btnSalir       = new JButton("Cerrar sesion");

        panelMenu.add(btnProductos);
        panelMenu.add(btnMovimientos);
        panelMenu.add(btnAlertas);
        panelMenu.add(btnReportes);
        panelMenu.add(btnSalir);
        add(panelMenu, BorderLayout.WEST);

        // Panel contenido con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        JLabel lblInicio = new JLabel("Selecciona un modulo del menu lateral.", SwingConstants.CENTER);
        lblInicio.setFont(new Font("Arial", Font.PLAIN, 13));
        panelContenido.add(lblInicio, "inicio");

        VentanaProductos panelProductos = new VentanaProductos();
        panelProductos.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        panelContenido.add(panelProductos.getContentPane(), "productos");

        VentanaMovimientos panelMovimientos = new VentanaMovimientos(usuarioActivo);
        panelMovimientos.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        panelContenido.add(panelMovimientos.getContentPane(), "movimientos");

        VentanaAlertas panelAlertas = new VentanaAlertas();
        panelAlertas.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        panelContenido.add(panelAlertas.getContentPane(), "alertas");

        VentanaReportes panelReportes = new VentanaReportes(usuarioActivo);
        panelReportes.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        panelContenido.add(panelReportes.getContentPane(), "reportes");

        add(panelContenido, BorderLayout.CENTER);

        // Acciones botones
        btnProductos.addActionListener(e -> cardLayout.show(panelContenido, "productos"));
        btnMovimientos.addActionListener(e -> cardLayout.show(panelContenido, "movimientos"));
        btnAlertas.addActionListener(e -> cardLayout.show(panelContenido, "alertas"));
        btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "reportes"));
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