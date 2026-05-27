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

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbInventario = new JRadioButton("Inventario general", true);
        rbMovimientos = new JRadioButton("Movimientos por periodo");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbInventario);
        grupo.add(rbMovimientos);
        panelSuperior.add(rbInventario);
        panelSuperior.add(rbMovimientos);
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFechas.add(new JLabel("Desde (yyyy-MM-dd):"));
        txtFechaInicio = new JTextField(12);
        panelFechas.add(txtFechaInicio);
        panelFechas.add(new JLabel("Hasta (yyyy-MM-dd):"));
        txtFechaFin = new JTextField(12);
        panelFechas.add(txtFechaFin);
        btnGenerar = new JButton("Generar reporte");
        panelFechas.add(btnGenerar);
        add(panelFechas, BorderLayout.CENTER);

        txtContenido = new JTextArea();
        txtContenido.setEditable(false);
        txtContenido.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(txtContenido), BorderLayout.SOUTH);

        btnGenerar.addActionListener(e -> {
            int idUsuario = usuarioActivo != null ? usuarioActivo.getIdUsuario() : 0;
            Reporte reporte;

            if (rbInventario.isSelected()) {
                reporte = reporteService.generarReporteInventario(idUsuario);
                txtContenido.setText(reporte.getContenido());
            } else {
                String inicio = txtFechaInicio.getText().trim();
                String fin = txtFechaFin.getText().trim();
                if (inicio.isEmpty() || fin.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Ingresa las dos fechas en formato yyyy-MM-dd");
                    return;
                }
                reporte = reporteService.generarReporteMovimientos(idUsuario, inicio, fin);
                txtContenido.setText(reporte.getContenido());
            }
        });
    }
}