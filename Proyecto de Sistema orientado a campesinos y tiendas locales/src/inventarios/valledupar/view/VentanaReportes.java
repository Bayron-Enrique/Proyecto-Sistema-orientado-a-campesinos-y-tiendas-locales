package inventarios.valledupar.view;

import inventarios.valledupar.model.Reporte;
import inventarios.valledupar.model.Usuario;
import inventarios.valledupar.service.ReporteService;
import javax.swing.*;
import java.awt.*;

public class VentanaReportes extends JFrame {

    private JRadioButton rbInventario;
    private JRadioButton rbMovimientos;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextArea txtContenido;
    private JButton btnGenerar;
    private ReporteService reporteService;
    private Usuario usuarioActivo;

    public VentanaReportes() {
        reporteService = new ReporteService();
        initComponents();
    }

    public VentanaReportes(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        reporteService = new ReporteService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Reportes");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbInventario = new JRadioButton("Inventario general", true);
        rbMovimientos = new JRadioButton("Movimientos por periodo");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbInventario);
        grupo.add(rbMovimientos);
        panelSuperior.add(rbInventario);
        panelSuperior.add(rbMovimientos);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel de fechas
        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFechas.add(new JLabel("Desde:"));
        txtFechaInicio = new JTextField(10);
        panelFechas.add(txtFechaInicio);
        panelFechas.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(10);
        panelFechas.add(txtFechaFin);
        btnGenerar = new JButton("Generar reporte");
        panelFechas.add(btnGenerar);
        add(panelFechas, BorderLayout.CENTER);

        // Area de contenido
        txtContenido = new JTextArea();
        txtContenido.setEditable(false);
        txtContenido.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(txtContenido), BorderLayout.SOUTH);

        // Accion boton
        btnGenerar.addActionListener(e -> {
            int idUsuario = usuarioActivo != null ? usuarioActivo.getIdUsuario() : 0;
            Reporte reporte;

            if (rbInventario.isSelected()) {
                reporte = reporteService.generarReporteInventario(idUsuario);
            } else {
                String inicio = txtFechaInicio.getText().trim();
                String fin = txtFechaFin.getText().trim();
                if (inicio.isEmpty() || fin.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Ingresa las dos fechas para el reporte.");
                    return;
                }
                reporte = reporteService.generarReporteMovimientos(idUsuario, inicio, fin);
            }
            txtContenido.setText(reporte.getContenido());
        });
    }
}