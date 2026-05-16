package inventarios.valledupar.view;

import inventarios.valledupar.model.Movimiento;
import inventarios.valledupar.service.InventarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaMovimientos extends JFrame {

    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnFiltrar;
    private InventarioService inventarioService;

    public VentanaMovimientos() {
        inventarioService = new InventarioService();
        initComponents();
        cargarMovimientos();
    }

    private void initComponents() {
        setTitle("Movimientos de Inventario");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Desde:"));
        txtFechaInicio = new JTextField(10);
        panelSuperior.add(txtFechaInicio);
        panelSuperior.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(10);
        panelSuperior.add(txtFechaFin);
        btnFiltrar = new JButton("Filtrar");
        btnRegistrar = new JButton("+ Registrar");
        btnActualizar = new JButton("Actualizar");
        panelSuperior.add(btnFiltrar);
        panelSuperior.add(btnRegistrar);
        panelSuperior.add(btnActualizar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Producto", "Usuario", "Tipo", "Cantidad", "Fecha", "Stock Resultante"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMovimientos = new JTable(modeloTabla);
        tablaMovimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaMovimientos), BorderLayout.CENTER);

        // Acciones
        btnActualizar.addActionListener(e -> cargarMovimientos());
        btnRegistrar.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Formulario de registro proximamente."));
        btnFiltrar.addActionListener(e -> {
            String inicio = txtFechaInicio.getText().trim();
            String fin = txtFechaFin.getText().trim();
            if (inicio.isEmpty() || fin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa las dos fechas para filtrar.");
                return;
            }
            cargarMovimientosPorPeriodo(inicio, fin);
        });
    }

    private void cargarMovimientos() {
        modeloTabla.setRowCount(0);
        List<Movimiento> movimientos = inventarioService.listarMovimientos();
        for (Movimiento m : movimientos) {
            modeloTabla.addRow(new Object[]{
                m.getIdMovimiento(),
                m.getIdProducto(),
                m.getIdUsuario(),
                m.getTipoMovimiento(),
                m.getCantidad(),
                m.getFechaMovimiento(),
                m.getStockResultante()
            });
        }
    }

    private void cargarMovimientosPorPeriodo(String inicio, String fin) {
        modeloTabla.setRowCount(0);
        List<Movimiento> movimientos = inventarioService.listarMovimientosPorPeriodo(inicio, fin);
        for (Movimiento m : movimientos) {
            modeloTabla.addRow(new Object[]{
                m.getIdMovimiento(),
                m.getIdProducto(),
                m.getIdUsuario(),
                m.getTipoMovimiento(),
                m.getCantidad(),
                m.getFechaMovimiento(),
                m.getStockResultante()
            });
        }
    }
}