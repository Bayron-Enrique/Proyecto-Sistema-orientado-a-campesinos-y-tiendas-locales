package inventarios.valledupar.view;

import inventarios.valledupar.model.Alerta;
import inventarios.valledupar.service.AlertaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaAlertas extends JFrame {

    private JTable tablaAlertas;
    private DefaultTableModel modeloTabla;
    private JButton btnAtender;
    private JButton btnActualizar;
    private JRadioButton rbPendientes;
    private JRadioButton rbTodas;
    private AlertaService alertaService;

    public VentanaAlertas() {
        alertaService = new AlertaService();
        initComponents();
        cargarAlertas();
    }

    private void initComponents() {
        setTitle("Alertas de Stock");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbPendientes = new JRadioButton("Pendientes", true);
        rbTodas = new JRadioButton("Todas");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbPendientes);
        grupo.add(rbTodas);
        btnAtender = new JButton("Marcar atendida");
        btnActualizar = new JButton("Actualizar");
        panelSuperior.add(rbPendientes);
        panelSuperior.add(rbTodas);
        panelSuperior.add(btnAtender);
        panelSuperior.add(btnActualizar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Producto", "Stock al momento", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAlertas = new JTable(modeloTabla);
        tablaAlertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaAlertas), BorderLayout.CENTER);

        // Acciones
        btnActualizar.addActionListener(e -> cargarAlertas());
        rbPendientes.addActionListener(e -> cargarAlertas());
        rbTodas.addActionListener(e -> cargarTodasLasAlertas());
        btnAtender.addActionListener(e -> {
            int fila = tablaAlertas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una alerta primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            alertaService.atenderAlerta(id);
            JOptionPane.showMessageDialog(this, "Alerta marcada como atendida.");
            cargarAlertas();
        });
    }

    private void cargarAlertas() {
        modeloTabla.setRowCount(0);
        List<Alerta> alertas = alertaService.listarAlertasPendientes();
        for (Alerta a : alertas) {
            modeloTabla.addRow(new Object[]{
                a.getIdAlerta(),
                a.getIdProducto(),
                a.getStockAlMomento(),
                a.getFechaAlerta(),
                a.getEstadoAlerta()
            });
        }
    }

    private void cargarTodasLasAlertas() {
        modeloTabla.setRowCount(0);
        List<Alerta> alertas = alertaService.listarTodasLasAlertas();
        for (Alerta a : alertas) {
            modeloTabla.addRow(new Object[]{
                a.getIdAlerta(),
                a.getIdProducto(),
                a.getStockAlMomento(),
                a.getFechaAlerta(),
                a.getEstadoAlerta()
            });
        }
    }
}